package com.app.bigprize.Activity;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.app.bigprize.Adapter.Big_MinesFinderAdapter;
import com.app.bigprize.Async.Big_GetMinesFinderAsync;
import com.app.bigprize.Async.Big_SaveMinesFinderAsync;
import com.app.bigprize.Async.Models.Big_MinesFinderResponseModel;
import com.app.bigprize.Async.Models.Big_Response_Model;
import com.app.bigprize.R;
import com.app.bigprize.Value.BIG_Constants;
import com.app.bigprize.utils.BIG_Ads_Utils;
import com.app.bigprize.utils.BIG_Common_Utils;
import com.app.bigprize.utils.BIG_SharePrefs;
import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxAdViewAdListener;
import com.applovin.mediation.MaxError;
import com.applovin.mediation.ads.MaxAdView;
import com.applovin.mediation.nativeAds.MaxNativeAdListener;
import com.applovin.mediation.nativeAds.MaxNativeAdLoader;
import com.applovin.mediation.nativeAds.MaxNativeAdView;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;

public class Big_MinesFinderActivity extends AppCompatActivity {
    public static boolean isVisible = false;
    public RelativeLayout layoutMain;
    MediaPlayer bombvoice;
    Big_MinesFinderAdapter mineadp;
    int numberText = 0;
    private Big_Response_Model responseMain;
    private LinearLayout layoutPoints;
    private TextView tvDailyPuzzle, tvRemainPuzzle;
    private MaxAd nativeAdWin;
    private MaxNativeAdLoader nativeAdLoaderWin;
    private LinearLayout layoutRemainingTime;
    private TextView tvRemainingTime, tvWinningPoints;
    private boolean isTimerSet = false;
    private String gameTime;
    private int time;
    private CountDownTimer timer;
    private String todayDate;
    private String lastDate;
    private ImageView ivHistory, ivHelp;
    private TextView tvPoints, lblLoadingAds;
    private LinearLayout layoutAds;
    private FrameLayout frameLayoutNativeAd, frameLovinBanner;
    private MaxNativeAdLoader nativeAdLoader;
    private MaxAd nativeAd;
    private TextView lblFindAll;
    private RecyclerView listdatagame;
    private Big_MinesFinderResponseModel objMinesweeperModel;
    private boolean isClick = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        BIG_Common_Utils.setDayNightTheme(Big_MinesFinderActivity.this);
        setContentView(R.layout.big_activity_mines_finder);
        isVisible = false;

        responseMain = new Gson().fromJson(BIG_SharePrefs.getInstance().getString(BIG_SharePrefs.HomeData), Big_Response_Model.class);
        frameLovinBanner = findViewById(R.id.frameLovinBanner);
        ivHelp = findViewById(R.id.ivHelp);
        layoutAds = findViewById(R.id.layoutAds);
        frameLayoutNativeAd = findViewById(R.id.fl_adplaceholder);
        lblLoadingAds = findViewById(R.id.lblLoadingAds);
        lblFindAll = findViewById(R.id.lblFindAll);
        layoutMain = findViewById(R.id.layoutMain);


        tvDailyPuzzle = findViewById(R.id.tvDailyPuzzle);
        tvRemainPuzzle = findViewById(R.id.tvRemainPuzzle);

        layoutRemainingTime = findViewById(R.id.layoutRemainingTime);
        tvRemainingTime = findViewById(R.id.tvRemainingTime);

        listdatagame = findViewById(R.id.listdatagame);
        ivHistory = findViewById(R.id.ivHistory);
        layoutPoints = findViewById(R.id.layoutPoints);
        tvPoints = findViewById(R.id.tvPoints);
        tvWinningPoints = findViewById(R.id.tvWinningPoints);

        tvPoints.setText(BIG_SharePrefs.getInstance().getEarningPointString());
        layoutPoints.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (BIG_SharePrefs.getInstance().getBoolean(BIG_SharePrefs.IS_LOGIN)) {
                    startActivity(new Intent(Big_MinesFinderActivity.this, Big_PointHistoryActivity.class));
                } else {
                    BIG_Common_Utils.NotifyLogin(Big_MinesFinderActivity.this);
                }
            }
        });
        ImageView imBack = findViewById(R.id.ivBack);
        imBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        ivHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (BIG_SharePrefs.getInstance().getBoolean(BIG_SharePrefs.IS_LOGIN)) {
                    startActivity(new Intent(Big_MinesFinderActivity.this, Big_PointHistoryActivity.class)
                            .putExtra("type", BIG_Constants.HistoryType.MINE_SWEEPER)
                            .putExtra("title", "Minesweeper History"));
                } else {
                    BIG_Common_Utils.NotifyLogin(Big_MinesFinderActivity.this);
                }
            }
        });

        new Big_GetMinesFinderAsync(Big_MinesFinderActivity.this);
    }

    @Override
    public void onBackPressed() {

            super.onBackPressed();

    }

    public void setData(Big_MinesFinderResponseModel responseModel) {
        objMinesweeperModel = responseModel;

        if (responseModel.getStatus().equals("2")) {
            showLimitOverView();
            BIG_Ads_Utils.showAppLovinInterstitialAd(Big_MinesFinderActivity.this, null);
        } else {

            if ((!BIG_Common_Utils.isStringNullOrEmpty(responseModel.getTotalGameCount()) && responseModel.getTotalGameCount().equals("0")) || (!BIG_Common_Utils.isStringNullOrEmpty(responseModel.getRemainGameCount()) && responseModel.getRemainGameCount().equals("0"))) {
                showLimitOverView();
                BIG_Ads_Utils.showAppLovinInterstitialAd(Big_MinesFinderActivity.this, null);
            } else {

                isVisible = false;
                isClick = true;
                if (responseModel.getPoints() != null) {
                    tvWinningPoints.setText(responseModel.getPoints());
                }
                setAd(responseModel.getAdType());
                if (responseModel.getMainNote() != null) {
                    lblFindAll.setText(responseModel.getMainNote());
                }

                if (responseModel.getRemainGameCount() != null) {
                    tvRemainPuzzle.setText(responseModel.getRemainGameCount());
                }
                if (responseModel.getTodayDate() != null) {
                    todayDate = responseModel.getTodayDate();
                }
                if (responseModel.getLastDate() != null) {
                    lastDate = responseModel.getLastDate();
                }
                if (responseModel.getTotalGameCount() != null) {
                    tvDailyPuzzle.setText(responseModel.getTotalGameCount());
                }

                if (!BIG_Common_Utils.isStringNullOrEmpty(responseModel.getNextGameTimer())) {
                    gameTime = responseModel.getNextGameTimer();
                }
                setTimer(true);

                try {
                    LinearLayout layoutCompleteTask = findViewById(R.id.layoutCompleteTask);
                    if (!isTimerSet && !BIG_Common_Utils.isStringNullOrEmpty(responseModel.getIsTodayTaskCompleted()) && responseModel.getIsTodayTaskCompleted().equals("0")) {
                        layoutCompleteTask.setVisibility(VISIBLE);

                        TextView tvTaskNote = findViewById(R.id.tvTaskNote);
                        tvTaskNote.setText(responseModel.getTaskNote());
                        Button btnCompleteTask = findViewById(R.id.btnCompleteTask);
                        if (!BIG_Common_Utils.isStringNullOrEmpty(responseModel.getTaskButton())) {
                            btnCompleteTask.setText(responseModel.getTaskButton());
                        }
                        btnCompleteTask.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                finish();
                                if (!BIG_Common_Utils.isStringNullOrEmpty(responseModel.getTaskId())) {
                                    Intent intent = new Intent(Big_MinesFinderActivity.this, Big_TaskDetailsInfoActivity.class);
                                    intent.putExtra("taskId", responseModel.getTaskId());
                                    startActivity(intent);
                                } else {
                                    Intent intent = new Intent(Big_MinesFinderActivity.this, Big_TaskDetailsInfoActivity.class);
                                    startActivity(intent);
                                }
                            }
                        });
                    } else {
                        layoutCompleteTask.setVisibility(GONE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                // Load home note webview top
                try {
                    if (!BIG_Common_Utils.isStringNullOrEmpty(responseModel.getHomeNote())) {
                        WebView webNote = findViewById(R.id.webNote);
                        webNote.getSettings().setJavaScriptEnabled(true);
                        webNote.setVisibility(View.VISIBLE);
                        webNote.loadDataWithBaseURL(null, responseModel.getHomeNote(), "text/html", "UTF-8", null);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                // Load top ad
                try {
                    if (responseModel.getTopAds() != null && !BIG_Common_Utils.isStringNullOrEmpty(responseModel.getTopAds().getImage())) {
                        LinearLayout layoutTopAds = findViewById(R.id.layoutTopAds);
                        BIG_Common_Utils.loadTopBannerAd(Big_MinesFinderActivity.this, layoutTopAds, responseModel.getTopAds());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (!BIG_Common_Utils.isStringNullOrEmpty(responseModel.getHelpVideoUrl())) {
                    ivHelp.setVisibility(VISIBLE);
                    ivHelp.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            BIG_Common_Utils.openUrl(Big_MinesFinderActivity.this, responseModel.getHelpVideoUrl());
                        }
                    });
                }
            }
        }

        setMinesData();

    }

    public void setAd(String adType) {
        if (responseMain.getIsAppLovinAdShow() != null && responseMain.getIsAppLovinAdShow().equals("1")) {
            if (adType != null) {
                if (adType.equals("1")) {
                    frameLovinBanner.setVisibility(GONE);
                    layoutAds.setVisibility(VISIBLE);
                    loadAppLovinNativeAds(false);
                } else if (adType.equals("2")) {
                    layoutAds.setVisibility(VISIBLE);
                    frameLovinBanner.setVisibility(GONE);
                    loadAppLovinNativeAds(true);
                } else if (adType.equals("3")) {
                    layoutAds.setVisibility(View.GONE);
                    frameLovinBanner.setVisibility(View.VISIBLE);
                    loadBannerAds(Big_MinesFinderActivity.this, frameLovinBanner);
                } else {
                    loadAppLovinNativeAds(false);
                }
            } else {
                loadAppLovinNativeAds(false);
            }
        }
    }

    private void loadBannerAds(Activity c, FrameLayout layoutBannerAd) {
        try {
            if (BIG_Common_Utils.isShowAppLovinBannerAds()) {
                MaxAdView adView = new MaxAdView(BIG_Common_Utils.getRandomAdUnitId(new Gson().fromJson(BIG_SharePrefs.getInstance().getString(BIG_SharePrefs.HomeData), Big_Response_Model.class).getLovinBannerID()), c);
                adView.setListener(new MaxAdViewAdListener() {
                    @Override
                    public void onAdExpanded(MaxAd ad) {

                    }

                    @Override
                    public void onAdCollapsed(MaxAd ad) {

                    }

                    @Override
                    public void onAdLoaded(MaxAd ad) {
//                        Logger_App.getInstance().e("APPLOVIN BANNER onAdLoaded==", "===" + ad);
                        layoutBannerAd.removeAllViews();
                        layoutBannerAd.addView(adView);
                    }

                    @Override
                    public void onAdDisplayed(MaxAd ad) {
//                        Logger_App.getInstance().e("APPLOVIN BANNER onAdDisplayed==", "===" + ad);
                    }

                    @Override
                    public void onAdHidden(MaxAd ad) {

                    }

                    @Override
                    public void onAdClicked(MaxAd ad) {

                    }

                    @Override
                    public void onAdLoadFailed(String adUnitId, MaxError error) {
//                        Logger_App.getInstance().e("APPLOVIN BANNER onAdLoadFailed==", "===" + error.getMessage());
                        layoutBannerAd.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAdDisplayFailed(MaxAd ad, MaxError error) {
//                        Logger_App.getInstance().e("APPLOVIN BANNER onAdDisplayFailed==", "===" + error.getMessage());
                        layoutBannerAd.setVisibility(View.GONE);
                    }
                });
                adView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, c.getResources().getDimensionPixelSize(R.dimen.applovin_banner_height)));
                adView.loadAd();
            } else {
                layoutBannerAd.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            layoutBannerAd.setVisibility(View.GONE);
        }
    }

    public void updateDataChanges(Big_MinesFinderResponseModel responseModel) {
        objMinesweeperModel = responseModel;
        if (!BIG_Common_Utils.isStringNullOrEmpty(responseModel.getEarningPoint())) {
            BIG_SharePrefs.getInstance().putString(BIG_SharePrefs.EarnedPoints, responseModel.getEarningPoint());
        }
        if (!BIG_Common_Utils.isStringNullOrEmpty(responseModel.getNextGameTimer())) {
            gameTime = responseModel.getNextGameTimer();
        }
        if (responseModel.getTodayDate() != null) {
            todayDate = responseModel.getTodayDate();
        }
        if (responseModel.getLastDate() != null) {
            lastDate = responseModel.getLastDate();
        }

        if (!BIG_Common_Utils.isStringNullOrEmpty(responseModel.getWinningPoints())) {
            if (responseModel.getWinningPoints().equals("0")) {
                BIG_Common_Utils.logFirebaseEvent(Big_MinesFinderActivity.this, "FeatureUsabilityItemId", "FeatureUsabilityEvent", "Minesweeper_Game_BigPrize", "Minesweeper Game - Better Luck");
                showBetterluckPopup();
            } else {
                BIG_Common_Utils.logFirebaseEvent(Big_MinesFinderActivity.this, "FeatureUsabilityItemId", "FeatureUsabilityEvent", "Minesweeper_Game_BigPrize", "Minesweeper Game - Win");
                showWinPopup(responseModel.getWinningPoints());
            }
        }
    }

    public void setTimer(boolean isFromOnCreate) {
        if (BIG_Common_Utils.timeDiff(todayDate, lastDate) > Integer.parseInt(gameTime)) {
            isTimerSet = false;
        } else {
            isTimerSet = true;
            layoutRemainingTime.setVisibility(VISIBLE);
            if (timer != null) {
                timer.cancel();
            }
            time = BIG_Common_Utils.timeDiff(todayDate, lastDate);
            timer = new CountDownTimer((Integer.parseInt(gameTime) - time) * 60000L, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    tvRemainingTime.setText(BIG_Common_Utils.updateTimeRemaining(millisUntilFinished));
                }

                @Override
                public void onFinish() {
                    layoutRemainingTime.setVisibility(GONE);
                    numberText = 0;
                    isClick = true;
                    isVisible = false;
                    setMinesData();
                    setAd(objMinesweeperModel.getAdType());
                    tvRemainPuzzle.setText(objMinesweeperModel.getRemainGameCount());
                }
            }.start();
            if (isFromOnCreate) {
                BIG_Ads_Utils.showAppLovinInterstitialAd(Big_MinesFinderActivity.this, null);
            }
        }
    }

    private void setMinesData() {
        if (listdatagame != null) {
            listdatagame.removeAllViews();
        }
        mineadp = new Big_MinesFinderAdapter(objMinesweeperModel.getData(), Big_MinesFinderActivity.this, new Big_MinesFinderAdapter.ClickListener() {
            @Override
            public void onItemClick(int position, View v, ImageView ivimgbox, LottieAnimationView ivimgboxlottie) {
                if (BIG_SharePrefs.getInstance().getBoolean(BIG_SharePrefs.IS_LOGIN)) {
                    if (v.isEnabled()) {
                        if (isClick) {
                            v.setEnabled(false);

                            if (objMinesweeperModel.getData().get(position).getIcon().contains(".json")) {
                                ivimgbox.setVisibility(View.INVISIBLE);
                                ivimgboxlottie.setVisibility(View.VISIBLE);
                                ivimgboxlottie.setMinAndMaxFrame(30, 70);
                                BIG_Common_Utils.setLottieAnimation(ivimgboxlottie, objMinesweeperModel.getData().get(position).getIcon());
                                ivimgboxlottie.playAnimation();
                            } else {
                                ivimgboxlottie.setVisibility(View.INVISIBLE);
                                ivimgbox.setVisibility(View.VISIBLE);
                                Glide.with(getApplicationContext())
                                        .load(objMinesweeperModel.getData().get(position).getIcon())
                                        .into(ivimgbox);
                            }

                            if (objMinesweeperModel.getData().get(position).getCount() != null) {
                                if (objMinesweeperModel.getData().get(position).getCount().equals("b")) {

                                    isClick = false;
                                    isVisible = true;
                                    mineadp.notifyDataSetChanged();
                                    bombvoice = MediaPlayer.create(Big_MinesFinderActivity.this, R.raw.big_bombsound);
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            bombvoice.start();
                                        }
                                    }, 800);

                                    Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            if (numberText <= 0) {
                                                new Big_SaveMinesFinderAsync(Big_MinesFinderActivity.this, "0");
                                            } else {
                                                new Big_SaveMinesFinderAsync(Big_MinesFinderActivity.this, String.valueOf(numberText));
                                            }
                                        }
                                    }, 1500);
                                } else {
                                    numberText += Integer.parseInt(objMinesweeperModel.getData().get(position).getCount());
//                                    BIG_AppLogger.getInstance().e("point===",""+objMinesweeperModel.getData().get(position).getCount());
                                    tvWinningPoints.setText(numberText + "");
                                    objMinesweeperModel.getData().get(position).setShown(true);
                                    mineadp.notifyItemChanged(position);
                                }
                            }
                        }
                    }
                } else {
                    BIG_Common_Utils.NotifyLogin(Big_MinesFinderActivity.this);
                }

            }
        });
        listdatagame.setLayoutManager(new GridLayoutManager(Big_MinesFinderActivity.this, 6));
        listdatagame.setAdapter(mineadp);
    }

    public void showWinPopup(String point) {
        try {
            Dialog dialogWin = new Dialog(Big_MinesFinderActivity.this, android.R.style.Theme_Light);
            dialogWin.getWindow().setBackgroundDrawableResource(R.color.black_transparent);
            dialogWin.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialogWin.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            dialogWin.setCancelable(false);
            dialogWin.setCanceledOnTouchOutside(false);
            dialogWin.setContentView(R.layout.big_dialog_win_spin);
            dialogWin.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

            FrameLayout fl_adplaceholder = dialogWin.findViewById(R.id.fl_adplaceholder);
            TextView lblLoadingAds = dialogWin.findViewById(R.id.lblLoadingAds);
            if (BIG_Common_Utils.isShowAppLovinNativeAds()) {
                loadAppLovinNativeAds(fl_adplaceholder, lblLoadingAds);
            } else {
                lblLoadingAds.setVisibility(View.GONE);
            }

            TextView tvPoint = dialogWin.findViewById(R.id.tvPoints);

            LottieAnimationView animation_view = dialogWin.findViewById(R.id.animation_view);
            BIG_Common_Utils.setLottieAnimation(animation_view, responseMain.getCelebrationLottieUrl());
            animation_view.addAnimatorListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationStart(Animator animation, boolean isReverse) {
                    super.onAnimationStart(animation, isReverse);
                    BIG_Common_Utils.startTextCountAnimation(tvPoint, point);
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    animation_view.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationStart(Animator animation) {
                    super.onAnimationStart(animation);
                }
            });
            ImageView ivClose = dialogWin.findViewById(R.id.ivClose);
            ivClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    BIG_Ads_Utils.showAppLovinInterstitialAd(Big_MinesFinderActivity.this, new BIG_Ads_Utils.AdShownListener() {
                        @Override
                        public void onAdDismiss() {
                            if (dialogWin != null) {
                                dialogWin.dismiss();
                            }
                        }
                    });
                }
            });

            TextView lblPoints = dialogWin.findViewById(R.id.lblPoints);
            AppCompatButton btnOk = dialogWin.findViewById(R.id.btnOk);
            try {
                int pt = Integer.parseInt(point);
                lblPoints.setText((pt <= 1 ? "Ruby" : "Rubies"));
            } catch (NumberFormatException e) {
                e.printStackTrace();
                lblPoints.setText("Rubies");
            }

            btnOk.setOnClickListener(v -> {
                BIG_Ads_Utils.showAppLovinInterstitialAd(Big_MinesFinderActivity.this, new BIG_Ads_Utils.AdShownListener() {
                    @Override
                    public void onAdDismiss() {
                        if (dialogWin != null) {
                            dialogWin.dismiss();
                        }
                    }
                });
            });
            dialogWin.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {
                    BIG_Common_Utils.GetCoinAnimation(Big_MinesFinderActivity.this, layoutMain, layoutPoints);
                    tvPoints.setText(BIG_SharePrefs.getInstance().getEarningPointString());
                    Log.d("dialogWin1", "onDismiss: "+tvPoint);
                    if (!point.equals("0")) {
                        BIG_Common_Utils.GetCoinAnimation(Big_MinesFinderActivity.this, layoutMain, layoutPoints);
                    }
                    tvPoints.setText(objMinesweeperModel.getEarningPoint());
                    Log.d("dialogWin2", "onDismiss: "+tvPoint);
                    if (!BIG_Common_Utils.isStringNullOrEmpty(objMinesweeperModel.getRemainGameCount()) && objMinesweeperModel.getRemainGameCount().equals("0")) {
                        showLimitOverView();
                    } else {
                        tvWinningPoints.setText("0");
                        Log.d("dialogWin3", "onDismiss: "+tvWinningPoints);
                        setTimer(false);
                    }
                }
            });
            if (!isFinishing() && !dialogWin.isShowing()) {
                dialogWin.show();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        animation_view.setVisibility(View.VISIBLE);
                        animation_view.playAnimation();
                    }
                }, 500);
            }
        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    public void showBetterluckPopup() {
        Dialog dilaogBetterluck = new Dialog(Big_MinesFinderActivity.this, android.R.style.Theme_Light);
        dilaogBetterluck.getWindow().setBackgroundDrawableResource(R.color.black_transparent);
        dilaogBetterluck.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dilaogBetterluck.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        dilaogBetterluck.setCancelable(false);
        dilaogBetterluck.setCanceledOnTouchOutside(false);
        dilaogBetterluck.setContentView(R.layout.big_popup_betterluck);

        TextView tvMessage = dilaogBetterluck.findViewById(R.id.tvMessage);
        tvMessage.setText("Oops! Game is over. Better luck next time!");

        Button lDone = dilaogBetterluck.findViewById(R.id.btnOk);

        lDone.setOnClickListener(v -> {
            BIG_Ads_Utils.showAppLovinInterstitialAd(Big_MinesFinderActivity.this, new BIG_Ads_Utils.AdShownListener() {
                @Override
                public void onAdDismiss() {
                    if (dilaogBetterluck != null) {
                        dilaogBetterluck.dismiss();
                    }
                }
            });
        });

        dilaogBetterluck.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                if (!BIG_Common_Utils.isStringNullOrEmpty(objMinesweeperModel.getRemainGameCount()) && objMinesweeperModel.getRemainGameCount().equals("0")) {
                    showLimitOverView();
                } else {
                    tvWinningPoints.setText("0");
                    setTimer(false);
                }
            }
        });

        if (!isFinishing() && !dilaogBetterluck.isShowing()) {
            dilaogBetterluck.show();
        }
    }

    private void showLimitOverView() {
        setAd(objMinesweeperModel.getAdType());
        tvRemainPuzzle.setText(objMinesweeperModel.getRemainGameCount());
        tvDailyPuzzle.setText(objMinesweeperModel.getTotalGameCount());
        layoutRemainingTime.setVisibility(VISIBLE);
        TextView lblTimer = findViewById(R.id.lblTimer);
        lblTimer.setText("You have exhausted today's  Game limit, please try again tomorrow.");
        tvRemainingTime.setVisibility(GONE);
    }

    private void loadAppLovinNativeAds(boolean isSmall) {
        try {
            nativeAdLoader = new MaxNativeAdLoader(isSmall ? BIG_Common_Utils.getRandomAdUnitId(responseMain.getLovinSmallNativeID()) : BIG_Common_Utils.getRandomAdUnitId(responseMain.getLovinNativeID()), Big_MinesFinderActivity.this);
            nativeAdLoader.setNativeAdListener(new MaxNativeAdListener() {
                @Override
                public void onNativeAdLoaded(final MaxNativeAdView nativeAdView, final MaxAd ad) {
                    frameLayoutNativeAd.setVisibility(VISIBLE);
                    if (nativeAd != null) {
                        nativeAdLoader.destroy(nativeAd);
                    }
                    nativeAd = ad;
                    frameLayoutNativeAd.removeAllViews();
                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) frameLayoutNativeAd.getLayoutParams();
                    params.height = isSmall ? getResources().getDimensionPixelSize(R.dimen.dim_150) : getResources().getDimensionPixelSize(R.dimen.dim_300);
                    params.width = LinearLayout.LayoutParams.MATCH_PARENT;
                    frameLayoutNativeAd.setLayoutParams(params);
                    frameLayoutNativeAd.setPadding((int) getResources().getDimension(R.dimen.dim_10), (int) getResources().getDimension(R.dimen.dim_10), (int) getResources().getDimension(R.dimen.dim_10), (int) getResources().getDimension(R.dimen.dim_10));
                    frameLayoutNativeAd.addView(nativeAdView);
                    lblLoadingAds.setVisibility(View.GONE);
                    //Logger_App.getInstance().e("AppLovin Loaded: ", "===");
                }

                @Override
                public void onNativeAdLoadFailed(final String adUnitId, final MaxError error) {
                    layoutAds.setVisibility(View.GONE);
                    //Logger_App.getInstance().e("AppLovin Failed: ", error.getMessage());
                }

                @Override
                public void onNativeAdClicked(final MaxAd ad) {

                }
            });
            nativeAdLoader.loadAd();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (isFinishing()) {
            try {
                if (nativeAd != null && nativeAdLoader != null) {
                    nativeAdLoader.destroy(nativeAd);
                    nativeAd = null;
                    frameLayoutNativeAd = null;
                }
                if (nativeAdWin != null && nativeAdLoaderWin != null) {
                    nativeAdLoaderWin.destroy(nativeAdWin);
                    nativeAdWin = null;
                }
                if (timer != null) {
                    timer.cancel();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void loadAppLovinNativeAds(FrameLayout frameLayoutNativeAd, TextView lblLoadingAds) {
        try {
            nativeAdLoaderWin = new MaxNativeAdLoader(BIG_Common_Utils.getRandomAdUnitId(responseMain.getLovinNativeID()), Big_MinesFinderActivity.this);
            nativeAdLoaderWin.setNativeAdListener(new MaxNativeAdListener() {
                @Override
                public void onNativeAdLoaded(final MaxNativeAdView nativeAdView, final MaxAd ad) {
                    if (nativeAdWin != null) {
                        nativeAdLoaderWin.destroy(nativeAdWin);
                    }
                    nativeAdWin = ad;
                    frameLayoutNativeAd.removeAllViews();
                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) frameLayoutNativeAd.getLayoutParams();
                    params.height = getResources().getDimensionPixelSize(R.dimen.dim_300);
                    params.width = LinearLayout.LayoutParams.MATCH_PARENT;
                    frameLayoutNativeAd.setLayoutParams(params);
                    frameLayoutNativeAd.setPadding((int) getResources().getDimension(R.dimen.dim_10), (int) getResources().getDimension(R.dimen.dim_10), (int) getResources().getDimension(R.dimen.dim_10), (int) getResources().getDimension(R.dimen.dim_10));
                    lblLoadingAds.setVisibility(View.GONE);
                    frameLayoutNativeAd.addView(nativeAdView);
                    frameLayoutNativeAd.setVisibility(VISIBLE);
//                    Logger_App.getInstance().e("AppLovin Loaded WIN: ", "===WIN");
                }

                @Override
                public void onNativeAdLoadFailed(final String adUnitId, final MaxError error) {
//                    Logger_App.getInstance().e("AppLovin Failed WIN: ", error.getMessage());
                    frameLayoutNativeAd.setVisibility(View.GONE);
                    lblLoadingAds.setVisibility(View.GONE);
                }

                @Override
                public void onNativeAdClicked(final MaxAd ad) {

                }
            });
            nativeAdLoaderWin.loadAd();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}