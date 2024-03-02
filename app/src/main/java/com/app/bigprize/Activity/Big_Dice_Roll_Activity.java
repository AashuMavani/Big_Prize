package com.app.bigprize.Activity;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.app.bigprize.utils.BIG_Common_Utils.convertTimeInMillis;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.airbnb.lottie.LottieAnimationView;
import com.app.bigprize.Async.Big_Get_Dice_Async;
import com.app.bigprize.Async.Big_Save_Dice_Async;
import com.app.bigprize.Async.Models.Big_Dice_Data_Model;
import com.app.bigprize.Async.Models.Big_Response_Model;
import com.app.bigprize.R;
import com.app.bigprize.Value.BIG_Constants;
import com.app.bigprize.utils.BIG_Ads_Utils;
import com.app.bigprize.utils.BIG_AppLogger;
import com.app.bigprize.utils.BIG_Common_Utils;
import com.app.bigprize.utils.BIG_SharePrefs;
import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxError;
import com.applovin.mediation.nativeAds.MaxNativeAdListener;
import com.applovin.mediation.nativeAds.MaxNativeAdLoader;
import com.applovin.mediation.nativeAds.MaxNativeAdView;
import com.google.gson.Gson;

import java.util.Locale;
import java.util.Random;

public class Big_Dice_Roll_Activity extends AppCompatActivity {
    LottieAnimationView btnSpinNow3, btnSpinNow2, btnSpinNow1;
    private long lastClickTime = 0;
    private Big_Response_Model responseMain;
    private LinearLayout layoutAds, llLimit, layoutPoints;
    private String todayDate, lastDate;
    private FrameLayout frameLayoutNativeAd;
    private MaxAd nativeAd, nativeAdWin, nativeAdTask;
    private MaxNativeAdLoader nativeAdLoader, nativeAdLoaderWin, nativeAdLoaderTask;
    RelativeLayout llPlay, layoutMain,ilAttempt;
    private ImageView ivHistory;
    int counter = 0, ran1, ran2, ran3, todayLeft = 0, gameTime;
    Big_Dice_Data_Model diceDataModel;

    boolean isWin = false, isTimerOn = false;
    TextView tvMakeTotal, tvDice3, tvDice2, tvDice1, tvTotal2, tvPlayGame, lblLoadingAds, tvLeftCount, tvWinPoints, tvPoints, tvTotalPlay, tvRemaining;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        BIG_Common_Utils.setDayNightTheme(Big_Dice_Roll_Activity.this);
        setContentView(R.layout.big_activity_dice_roll);

        responseMain = new Gson().fromJson(BIG_SharePrefs.getInstance().getString(BIG_SharePrefs.HomeData), Big_Response_Model.class);
        tvMakeTotal = findViewById(R.id.tvMakeTotal);
        tvDice3 = findViewById(R.id.tvDice3);
        tvDice2 = findViewById(R.id.tvDice2);
        tvDice1 = findViewById(R.id.tvDice1);
        tvRemaining = findViewById(R.id.tvRemaining);
        tvTotal2 = findViewById(R.id.tvTotal2);
        layoutAds = findViewById(R.id.layoutAds);
        lblLoadingAds = findViewById(R.id.lblLoadingAds);
        layoutMain = findViewById(R.id.layoutMain);
        btnSpinNow3 = findViewById(R.id.btnSpinNow3);
        layoutPoints = findViewById(R.id.layoutPoints);
        tvTotalPlay = findViewById(R.id.tvTotalPlay);
        tvLeftCount = findViewById(R.id.tvLeftCount);
        tvWinPoints = findViewById(R.id.tvWinPoints);
        ivHistory = findViewById(R.id.ivHistory);
        tvPoints = findViewById(R.id.tvPoints);
        llLimit = findViewById(R.id.llLimit);
        llPlay = findViewById(R.id.llPlay);
        btnSpinNow2 = findViewById(R.id.btnSpinNow2);
        tvPlayGame = findViewById(R.id.tvPlayGame);
        btnSpinNow1 = findViewById(R.id.btnSpinNow1);
        ilAttempt = findViewById(R.id.ilAttempt);

        tvPoints.setText(BIG_SharePrefs.getInstance().getEarningPointString());
        layoutPoints.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (BIG_SharePrefs.getInstance().getBoolean(BIG_SharePrefs.IS_LOGIN)) {
                    startActivity(new Intent(Big_Dice_Roll_Activity.this, Big_My_Wallet_Activity.class));
                } else {
                    BIG_Common_Utils.NotifyLogin(Big_Dice_Roll_Activity.this);
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
                    startActivity(new Intent(Big_Dice_Roll_Activity.this, Big_PointHistoryActivity.class)
                            .putExtra("type", BIG_Constants.HistoryType.Dice_Game)
                            .putExtra("title", "Dice Game History"));
                } else {
                    BIG_Common_Utils.NotifyLogin(Big_Dice_Roll_Activity.this);
                }
            }
        });
        if (BIG_Common_Utils.isShowAppLovinNativeAds()) {
            loadAppLovinNativeAds();
        } else {
            layoutAds.setVisibility(View.GONE);
        }
        new Big_Get_Dice_Async(Big_Dice_Roll_Activity.this);
        btnSpinNow1.addAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(@NonNull Animator animation) {

            }

            @Override
            public void onAnimationEnd(@NonNull Animator animation) {

            }

            @Override
            public void onAnimationCancel(@NonNull Animator animation) {

            }

            @Override
            public void onAnimationRepeat(@NonNull Animator animation) {

            }
        });
    }
    private CountDownTimer timer;
    int time;

    private void loadAppLovinNativeAds() {
        try {
            nativeAdLoader = new MaxNativeAdLoader(BIG_Common_Utils.getRandomAdUnitId(responseMain.getLovinNativeID()), Big_Dice_Roll_Activity.this);
            nativeAdLoader.setNativeAdListener(new MaxNativeAdListener() {
                @Override
                public void onNativeAdLoaded(final MaxNativeAdView nativeAdView, final MaxAd ad) {
                    frameLayoutNativeAd = findViewById(R.id.fl_adplaceholder);
                    if (nativeAd != null) {
                        nativeAdLoader.destroy(nativeAd);
                    }
                    nativeAd = ad;
                    frameLayoutNativeAd.removeAllViews();
                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) frameLayoutNativeAd.getLayoutParams();
                    params.height = getResources().getDimensionPixelSize(R.dimen.dim_300);
                    params.width = LinearLayout.LayoutParams.MATCH_PARENT;
                    frameLayoutNativeAd.setLayoutParams(params);
                    frameLayoutNativeAd.setPadding((int) getResources().getDimension(R.dimen.dim_10), (int) getResources().getDimension(R.dimen.dim_10), (int) getResources().getDimension(R.dimen.dim_10), (int) getResources().getDimension(R.dimen.dim_10));
                    frameLayoutNativeAd.addView(nativeAdView);
                    layoutAds.setVisibility(View.VISIBLE);
                    lblLoadingAds.setVisibility(View.GONE);

                }

                @Override
                public void onNativeAdLoadFailed(final String adUnitId, final MaxError error) {
                    layoutAds.setVisibility(View.GONE);

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
    private void loadAppLovinNativeAdsTask(LinearLayout layoutAds, FrameLayout frameLayoutNativeAd, TextView lblLoadingAds) {
        try {
            nativeAdLoaderTask = new MaxNativeAdLoader(BIG_Common_Utils.getRandomAdUnitId(responseMain.getLovinNativeID()), Big_Dice_Roll_Activity.this);
            nativeAdLoaderTask.setNativeAdListener(new MaxNativeAdListener() {
                @Override
                public void onNativeAdLoaded(final MaxNativeAdView nativeAdView, final MaxAd ad) {
                    if (nativeAdTask != null) {
                        nativeAdLoaderTask.destroy(nativeAdTask);
                    }
                    nativeAdTask = ad;
                    frameLayoutNativeAd.removeAllViews();
                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) frameLayoutNativeAd.getLayoutParams();
                    params.height = getResources().getDimensionPixelSize(R.dimen.dim_300);
                    params.width = LinearLayout.LayoutParams.MATCH_PARENT;
                    frameLayoutNativeAd.setLayoutParams(params);
                    frameLayoutNativeAd.setPadding((int) getResources().getDimension(R.dimen.dim_10), (int) getResources().getDimension(R.dimen.dim_10), (int) getResources().getDimension(R.dimen.dim_10), (int) getResources().getDimension(R.dimen.dim_10));
                    lblLoadingAds.setVisibility(View.GONE);
                    frameLayoutNativeAd.addView(nativeAdView);
                    frameLayoutNativeAd.setVisibility(VISIBLE);
                    //AppLogger.getInstance().e("AppLovin Loaded WIN: ", "===WIN");
                }

                @Override
                public void onNativeAdLoadFailed(final String adUnitId, final MaxError error) {
                    layoutAds.setVisibility(View.GONE);
                }

                @Override
                public void onNativeAdClicked(final MaxAd ad) {

                }

            });
            nativeAdLoaderTask.loadAd();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void loadAppLovinNativeAds(FrameLayout frameLayoutNativeAd, TextView lblLoadingAds) {
        try {
            nativeAdLoaderWin = new MaxNativeAdLoader(BIG_Common_Utils.getRandomAdUnitId(responseMain.getLovinNativeID()), Big_Dice_Roll_Activity.this);
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

                }

                @Override
                public void onNativeAdLoadFailed(final String adUnitId, final MaxError error) {

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
    @Override
    public void onBackPressed() {

            super.onBackPressed();

    }

    private Big_Dice_Data_Model responseModel;
    @SuppressLint("SetTextI18n")
    public void setData(Big_Dice_Data_Model responseModel1) {
        responseModel=responseModel1;

        if (responseModel.getStatus().equals("2")) {
            BIG_Ads_Utils.showAppLovinInterstitialAd(Big_Dice_Roll_Activity.this, null);
            llLimit.setVisibility(VISIBLE);
            ilAttempt.setVisibility(GONE);
            llPlay.setVisibility(View.GONE);
        } else {

            diceDataModel = responseModel;

            if (responseModel.getData() != null && responseModel.getData().size() > 0) {
                try {
                    if (responseModel.getTodayDate() != null) {
                        todayDate = responseModel.getTodayDate();
                    }
                    if (responseModel.getLastDate() != null) {
                        lastDate = responseModel.getLastDate();
                    }
                    if (responseModel.getTotalGameCount() != null) {
                        tvTotalPlay.setText(responseModel.getTotalGameCount());
                    }
                    if (responseModel.getRemainGameCount() != null) {
                        tvRemaining.setText(responseModel.getRemainGameCount());
                    }

                    if (responseModel.getGameTime() != null) {
                        gameTime = Integer.parseInt(responseModel.getGameTime());
                    }

                    if (responseModel.getTotalCount() != null) {
                        todayLeft = Integer.parseInt(responseModel.getTotalCount());
                        counter = Integer.parseInt(responseModel.getTotalCount());
                        tvLeftCount.setText(responseModel.getTotalCount());

                    }

                    if (responseModel.getMakeTotal() != null) {
                        tvMakeTotal.setText(responseModel.getMakeTotal());
                    }
                    if (responseModel.getPoints() != null) {
                        tvWinPoints.setText(responseModel.getPoints());
                    }

                    tvTotal2.setText("0");

                    playDiesLottie(false);
                } catch (Exception e) {
                    e.printStackTrace();
                    BIG_Common_Utils.dismissProgressLoader();
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
                        BIG_Common_Utils.loadTopBannerAd(Big_Dice_Roll_Activity.this, layoutTopAds, responseModel.getTopAds());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    LinearLayout layoutCompleteTask = findViewById(R.id.layoutCompleteTask);
                    if (!BIG_Common_Utils.isStringNullOrEmpty(diceDataModel.getIsTodayTaskCompleted()) && diceDataModel.getIsTodayTaskCompleted().equals("0")) {
                        layoutCompleteTask.setVisibility(VISIBLE);
                        LinearLayout layoutAdsTask = findViewById(R.id.layoutAdsTask);
                        TextView lblLoadingAdsTask = findViewById(R.id.lblLoadingAdsTask);
                        FrameLayout nativeAdTask = findViewById(R.id.fl_adplaceholder_task);

                        if (BIG_Common_Utils.isShowAppLovinNativeAds()) {
                            loadAppLovinNativeAdsTask(layoutAdsTask, nativeAdTask, lblLoadingAdsTask);
                        } else {
                            layoutAdsTask.setVisibility(GONE);
                        }
                        TextView tvTaskNote = findViewById(R.id.tvTaskNote);
                        tvTaskNote.setText(diceDataModel.getTaskNote());

                        Button btnCompleteTask = findViewById(R.id.btnCompleteTask);
                        if (!BIG_Common_Utils.isStringNullOrEmpty(diceDataModel.getTaskButton())) {
                            btnCompleteTask.setText(diceDataModel.getTaskButton());
                        }
                        btnCompleteTask.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                if (!BIG_Common_Utils.isStringNullOrEmpty(diceDataModel.getScreenNo())) {

                                    BIG_Common_Utils.Redirect(Big_Dice_Roll_Activity.this, diceDataModel.getScreenNo(), "", "", "", "", "");
                                } else if (!BIG_Common_Utils.isStringNullOrEmpty(diceDataModel.getTaskId())) {
                                    Intent intent = new Intent(Big_Dice_Roll_Activity.this, Big_TaskDetailsInfoActivity.class);
                                    intent.putExtra("taskId", diceDataModel.getTaskId());
                                    startActivity(intent);
                                } else {
                                    Intent intent = new Intent(Big_Dice_Roll_Activity.this, Big_TasksCategoryTypeActivity.class);
                                    intent.putExtra("taskTypeId", BIG_Constants.TASK_TYPE_ALL);
                                    intent.putExtra("title", "Tasks");
                                    startActivity(intent);
                                }
                                finish();
                            }
                        });
                    } else {
                        layoutCompleteTask.setVisibility(GONE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }

            llPlay.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (SystemClock.elapsedRealtime() - lastClickTime < 1000) {
                        return;
                    }
                    lastClickTime = SystemClock.elapsedRealtime();

                    if (BIG_SharePrefs.getInstance().getBoolean(BIG_SharePrefs.IS_LOGIN)) {
                        if (!isTimerOn) {

                            Log.e("Playgame" , "" +Integer.parseInt(tvLeftCount.getText().toString()) +" "+ counter + Integer.parseInt(tvLeftCount.getText().toString()) + " " + isWin );
                            if (Integer.parseInt(tvLeftCount.getText().toString()) <= counter && Integer.parseInt(tvLeftCount.getText().toString()) != 0 && !isWin)
                            {
                                playDiesLottie(true);

                                tvDice1.setText(String.valueOf(ran1));
                                tvDice2.setText(String.valueOf(ran2));
                                tvDice3.setText(String.valueOf(ran3));
                                int sum_add = ran1+ ran2 + ran3;

                                tvTotal2.setText(String.valueOf(sum_add));

                                if (tvMakeTotal.getText().toString().matches(String.valueOf(sum_add))) {
                                    isWin = true;
                                    Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            BIG_AppLogger.getInstance().e("ATTEMPTS SUCCESS","SAVE: "+diceDataModel.getPoints());
                                            new Big_Save_Dice_Async(Big_Dice_Roll_Activity.this, diceDataModel.getPoints());
                                        }
                                    }, 500);
                                }
                                todayLeft--;
                                tvLeftCount.setText(String.valueOf(todayLeft));

                                if (todayLeft == 0 && !tvMakeTotal.getText().toString().matches(String.valueOf(sum_add))) {
                                    isWin = true;
                                    Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            BIG_AppLogger.getInstance().e("ATTEMPTS OVER","todayLeft: "+todayLeft);
                                            new Big_Save_Dice_Async(Big_Dice_Roll_Activity.this, "0");
                                        }
                                    }, 1000);

                                }
                            }

                        } else {
                            Toast.makeText(Big_Dice_Roll_Activity.this, "Please Wait ", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        BIG_Common_Utils.NotifyLogin(Big_Dice_Roll_Activity.this);
                    }
                }
            });

            setTimer(true);
        }

    }

    protected void onStop() {
        super.onStop();
        if (isFinishing()) {
            try {
                if (timer != null) {
                    timer.cancel();
                }
                if (nativeAd != null && nativeAdLoader != null) {
                    nativeAdLoader.destroy(nativeAd);
                    nativeAd = null;
                    frameLayoutNativeAd = null;
                }
                if (nativeAdWin != null && nativeAdLoaderWin != null) {
                    nativeAdLoaderWin.destroy(nativeAdWin);
                    nativeAdWin = null;
                }
                if (nativeAdTask != null && nativeAdTask != null) {
                    nativeAdLoaderTask.destroy(nativeAdTask);
                    nativeAdTask = null;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    public void setTimer(boolean isFromOnCreate) {

        if (timeDiff(todayDate, lastDate) > gameTime) {

            llPlay.setEnabled(true);
            tvPlayGame.setText("Play Game");
        } else {
            llPlay.setEnabled(false);
            if (timer != null) {
                timer.cancel();
            }
            time = timeDiff(todayDate, lastDate);

            timer = new CountDownTimer((gameTime - time) * 60000L, 1000) {
                @SuppressLint("ResourceAsColor")
                @Override
                public void onTick(long millisUntilFinished) {

                    tvPlayGame.setText(updateTimeRemaining(millisUntilFinished));
                    tvPlayGame.setTextColor(getResources().getColor(R.color.yellow));
                }

                @Override
                public void onFinish() {
                    isTimerOn = false;
                    llPlay.setEnabled(true);
                    tvPlayGame.setTextColor(getResources().getColor(R.color.white));
                    tvPlayGame.setText("Play Game");
                }
            }.start();
            if (isFromOnCreate) {
                BIG_Ads_Utils.showAppLovinInterstitialAd(Big_Dice_Roll_Activity.this, null);
            }
        }
    }

    public String updateTimeRemaining(long timeDiff) {
        if (timeDiff > 0) {
            int seconds = (int) (timeDiff / 1000) % 60;
            int minutes = (int) ((timeDiff / (1000 * 60)) % 60);
            int hours = (int) ((timeDiff / (1000 * 60 * 60)) % 24);
            int days = (int) (timeDiff / (1000 * 60 * 60 * 24));
            if (days > 3) {
                return String.format(Locale.getDefault(), "%02d days left", days);
            } else {
                return String.format(Locale.getDefault(), "%02d:%02d:%02d", hours + (days * 24), minutes, seconds);
            }
        } else {
            return "Time's up!!";
        }
    }

    private int timeDiff(String date1, String Date2) {
        long diff = convertTimeInMillis("yyyy-MM-dd HH:mm:ss", date1) - convertTimeInMillis("yyyy-MM-dd HH:mm:ss", Date2);
        double seconds = Math.abs(diff) / 1000;
        int minutes = (int) (seconds / 60);
        return minutes;
    }

    public void showWinPopup(String point, String isShowAds) {
        try {
            Dialog dialogWin = new Dialog(Big_Dice_Roll_Activity.this, android.R.style.Theme_Light);
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
            Log.d("points", "showWinPopup: "+responseMain.getCelebrationLottieUrl());
            LottieAnimationView animation_view = dialogWin.findViewById(R.id.animation_view);

            BIG_Common_Utils.setLottieAnimation(animation_view, responseMain.getCelebrationLottieUrl());
            Log.d("onAnimationStart1", "showWinPopup: "+"animation");

            animation_view.addAnimatorListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationStart(Animator animation, boolean isReverse) {
                    super.onAnimationStart(animation, isReverse);
                    Log.d("onAnimationStart", "showWinPopup: "+animation);
                    BIG_Common_Utils.startTextCountAnimation(tvPoint, point);
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    Log.d("onAnimationEnd", "showWinPopup: "+animation);
                    animation_view.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationStart(Animator animation) {
                    Log.d("onAnimationStart", "showWinPopup: "+animation);
                    super.onAnimationStart(animation);
                }
            });
            ImageView ivClose = dialogWin.findViewById(R.id.ivClose);
            ivClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    BIG_Ads_Utils.showAppLovinInterstitialAd(Big_Dice_Roll_Activity.this, new BIG_Ads_Utils.AdShownListener() {
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
                BIG_Ads_Utils.showAppLovinInterstitialAd(Big_Dice_Roll_Activity.this, new BIG_Ads_Utils.AdShownListener() {
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
                    tvRemaining.setText(diceDataModel.getRemainGameCount());

                    if (!BIG_Common_Utils.isStringNullOrEmpty(diceDataModel.getRemainGameCount()) && diceDataModel.getRemainGameCount().equals("0")) {
                        llLimit.setVisibility(VISIBLE);
                        llPlay.setVisibility(View.GONE);
                        ilAttempt.setVisibility(View.GONE);

                    }
                    BIG_Common_Utils.GetCoinAnimation(Big_Dice_Roll_Activity.this, layoutMain, layoutPoints);
                    tvPoints.setText(BIG_SharePrefs.getInstance().getEarningPointString());
                    setTimer(false);
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


    public void playDiesLottie(boolean isPlay) {
        ran1 = BIG_Common_Utils.getRandomNumberBetweenRange(1, 6);
        ran2 = BIG_Common_Utils.getRandomNumberBetweenRange(1, 6);
        ran3 = BIG_Common_Utils.getRandomNumberBetweenRange(1, 6);

        BIG_Common_Utils.setLottieAnimation(btnSpinNow1, diceDataModel.getData().get(ran1 - 1).getDice());

        BIG_Common_Utils.setLottieAnimation(btnSpinNow2, diceDataModel.getData().get(ran2 - 1).getDice());

        BIG_Common_Utils.setLottieAnimation(btnSpinNow3, diceDataModel.getData().get(ran3 - 1).getDice());


        if (isPlay) {
            btnSpinNow1.playAnimation();
            btnSpinNow2.playAnimation();
            btnSpinNow3.playAnimation();
        }
    }

    public int getRandom() {
        return new Random().nextInt((6 - 1) + 1) + 1;
    }

    public void changeDiceDataValues(Big_Dice_Data_Model responseModel) {
        diceDataModel = responseModel;

        BIG_SharePrefs.getInstance().putString(BIG_SharePrefs.EarnedPoints, responseModel.getEarningPoint());

        if (responseModel.getTodayDate() != null) {
            todayDate = responseModel.getTodayDate();
        }
        if (responseModel.getLastDate() != null) {
            lastDate = responseModel.getLastDate();
        }
        if (diceDataModel.getMakeTotal() != null) {
            tvMakeTotal.setText(diceDataModel.getMakeTotal());
        }
        if (diceDataModel.getTotalCount() != null) {
            todayLeft = Integer.parseInt(diceDataModel.getTotalCount());
            counter = Integer.parseInt(diceDataModel.getTotalCount());
            tvLeftCount.setText(diceDataModel.getTotalCount());

        }
        if (responseModel.getRemainGameCount() != null) {
            tvRemaining.setText(responseModel.getRemainGameCount());
        }

        if (responseModel.getWinningPoints().equals("0")) {
            BIG_AppLogger.getInstance().e("ASDDF","better luch");
            BIG_Common_Utils.logFirebaseEvent(Big_Dice_Roll_Activity.this, "FeatureUsabilityItemId", "FeatureUsabilityEvent", "Dice_Roll_BigPrize", "Better Luck");
            showBetterluckPopup("Oops, your attempts are over. Better luck next time!");
        } else {
            BIG_AppLogger.getInstance().e("ASDDF","win");
            BIG_Common_Utils.logFirebaseEvent(Big_Dice_Roll_Activity.this, "FeatureUsabilityItemId", "FeatureUsabilityEvent", "Dice_Roll_BigPrize", "Dice Roll Got Reward");
            showWinPopup(responseModel.getWinningPoints(), responseModel.getIsShowAds());
        }
        isWin = false;
    }

    public void showBetterluckPopup(String message) {
        try {
            final Dialog dilaogBetterluck = new Dialog(Big_Dice_Roll_Activity.this, android.R.style.Theme_Light);
            dilaogBetterluck.getWindow().setBackgroundDrawableResource(R.color.black_transparent);
            dilaogBetterluck.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dilaogBetterluck.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            dilaogBetterluck.setContentView(R.layout.big_dialog_notify);
            dilaogBetterluck.setCancelable(false);

            Button btnOk = dilaogBetterluck.findViewById(R.id.btnOk);

            TextView tvMessage = dilaogBetterluck.findViewById(R.id.tvMessage);
            tvMessage.setText(message);
            btnOk.setOnClickListener(v -> {
                BIG_Ads_Utils.showAppLovinInterstitialAd(Big_Dice_Roll_Activity.this, new BIG_Ads_Utils.AdShownListener() {
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
                    tvRemaining.setText(diceDataModel.getRemainGameCount());

                    if (!BIG_Common_Utils.isStringNullOrEmpty(diceDataModel.getRemainGameCount()) && diceDataModel.getRemainGameCount().equals("0")) {
                        llLimit.setVisibility(VISIBLE);
                        llPlay.setVisibility(View.GONE);
                        ilAttempt.setVisibility(View.GONE);
                    }

                    setTimer(false);
                }
            });

            if (!isFinishing() && !dilaogBetterluck.isShowing()) {
                dilaogBetterluck.show();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}