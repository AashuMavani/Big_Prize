package com.app.bigprize.Activity;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

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
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.app.bigprize.Adapter.Big_SortNumbersAdapter;
import com.app.bigprize.Async.Big_GetSortWordsAsync;
import com.app.bigprize.Async.Big_SaveSortWordsAsync;
import com.app.bigprize.Async.Models.Big_Response_Model;
import com.app.bigprize.Async.Models.Big_SortNumbersItem;
import com.app.bigprize.Async.Models.Big_SortWordResponseModel;
import com.app.bigprize.Customviews.Big_Storyview.Big_CountDownAnim;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Big_SortWordsActivity extends AppCompatActivity {
    public List<Big_SortNumbersItem> dataList = new ArrayList<>();
    public ArrayList<String> temp = new ArrayList<>();
    private Big_Response_Model responseMain;
    private RecyclerView rvNumber;
    private ImageView ivHistory;
    private FrameLayout frameLayoutNativeAd;
    private MaxAd nativeAd, nativeAdWin, nativeAdTask;
    private MaxNativeAdLoader nativeAdLoader, nativeAdLoaderWin, nativeAdLoaderTask;
    private String todayDate, lastDate;
    private int nextGameTime, time, selPos = 0;
    private boolean isTimerSet = false, isFinishGame = false;
    private Big_SortNumbersAdapter countUpAdapter;
    private CountDownTimer timer, mainTimer;
    private LinearLayout layoutAds, llLimit, layoutPoints, layoutCountDownTimer;
    private TextView tvRemaining, tvWinPoints, tvPoints, lblLoadingAds, tvLeftCount, tvNote, tvTimeUp;
    private RelativeLayout layoutMain, layoutData;
    private Big_CountDownAnim countDownAnim;

    private TextView textView;
    private Spinner spinner;
    private String betterLuckMessage = "Better luck, next time!";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.big_activity_sort_words);
        responseMain = new Gson().fromJson(BIG_SharePrefs.getInstance().getString(BIG_SharePrefs.HomeData), Big_Response_Model.class);
        setViews();
        new Big_GetSortWordsAsync(Big_SortWordsActivity.this);
    }
    private void setViews() {
        layoutCountDownTimer = findViewById(R.id.layoutCountDownTimer);
        textView = findViewById(R.id.textView);
        spinner = findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.animations_array,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        layoutData = findViewById(R.id.layoutData);
        layoutData.setVisibility(View.INVISIBLE);
        tvRemaining = findViewById(R.id.tvRemaining);
        rvNumber = findViewById(R.id.rvNumber);
        lblLoadingAds = findViewById(R.id.lblLoadingAds);
        llLimit = findViewById(R.id.llLimit);
        layoutMain = findViewById(R.id.layoutMain);
        tvTimeUp = findViewById(R.id.tvTimeUp);
        layoutAds = findViewById(R.id.layoutAds);
        tvLeftCount = findViewById(R.id.tvLeftCount);
        tvNote = findViewById(R.id.tvNote);
        tvPoints = findViewById(R.id.tvPoints);
        layoutPoints = findViewById(R.id.layoutPoints);
        tvWinPoints = findViewById(R.id.tvWinPoints);

        tvPoints.setText(BIG_SharePrefs.getInstance().getEarningPointString());

        layoutPoints.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (BIG_SharePrefs.getInstance().getBoolean(BIG_SharePrefs.IS_LOGIN)) {
                    startActivity(new Intent(Big_SortWordsActivity.this, Big_My_Wallet_Activity.class));
                } else {
                    BIG_Common_Utils.NotifyLogin(Big_SortWordsActivity.this);
                }
            }
        });
        ImageView ivBack = findViewById(R.id.ivBack);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        if (BIG_Common_Utils.isShowAppLovinNativeAds()) {
            loadAppLovinNativeAds();
        } else {
            layoutAds.setVisibility(View.GONE);
        }
        ivHistory = findViewById(R.id.ivHistory);
        ivHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (BIG_SharePrefs.getInstance().getBoolean(BIG_SharePrefs.IS_LOGIN)) {
                    startActivity(new Intent(Big_SortWordsActivity.this, Big_PointHistoryActivity.class)
                            .putExtra("type", BIG_Constants.HistoryType.WORD_SORTING)
                            .putExtra("title", "Word Game History"));
                } else {
                    BIG_Common_Utils.NotifyLogin(Big_SortWordsActivity.this);
                }
            }
        });
        initCountDownAnimation();
    }

    private void loadAppLovinNativeAds() {
        try {
            nativeAdLoader = new MaxNativeAdLoader(BIG_Common_Utils.getRandomAdUnitId(responseMain.getLovinNativeID()), Big_SortWordsActivity.this);
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

    private void loadAppLovinNativeAds(FrameLayout frameLayoutNativeAd, TextView lblLoadingAds) {
        try {
            nativeAdLoaderWin = new MaxNativeAdLoader(BIG_Common_Utils.getRandomAdUnitId(responseMain.getLovinNativeID()), Big_SortWordsActivity.this);
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

    private Big_SortWordResponseModel responseModel;

    public void setData(Big_SortWordResponseModel responseModel1) {
        responseModel = responseModel1;
        try {
            layoutData.setVisibility(VISIBLE);
            if (!BIG_Common_Utils.isStringNullOrEmpty(responseModel.getEarningPoint())) {
                BIG_SharePrefs.getInstance().putString(BIG_SharePrefs.EarnedPoints, responseModel.getEarningPoint());
                tvPoints.setText(BIG_SharePrefs.getInstance().getEarningPointString());
            }
            if (responseModel.getStatus().equals("2")) {
                BIG_Ads_Utils.showAppLovinInterstitialAd(Big_SortWordsActivity.this, null);
                llLimit.setVisibility(VISIBLE);
                tvTimeUp.setVisibility(GONE);
                tvNote.setText("You have exhausted today's Word Game Game limit, please try again tomorrow.");
            }
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
            if (responseModel.getTodayDate() != null) {
                todayDate = responseModel.getTodayDate();
            }
            if (responseModel.getLastDate() != null) {
                lastDate = responseModel.getLastDate();
            }
            if (responseModel.getNextGameTime() != null) {
                nextGameTime = Integer.parseInt(responseModel.getNextGameTime());
            }
            AdpaterData(dataList);
            if (Integer.parseInt(responseModel.getRemainGameCount()) > 0) {
                setTimer1(true);
            }
            try {
                LinearLayout layoutCompleteTask = findViewById(R.id.layoutCompleteTask);
                if (Integer.parseInt(responseModel.getRemainGameCount()) > 0 && !isTimerSet && !responseModel.getStatus().equals("2") && !BIG_Common_Utils.isStringNullOrEmpty(responseModel.getIsTodayTaskCompleted()) && responseModel.getIsTodayTaskCompleted().equals("0")) {
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
                    tvTaskNote.setText(responseModel.getTaskNote());

                    Button btnCompleteTask = findViewById(R.id.btnCompleteTask);
                    if (!BIG_Common_Utils.isStringNullOrEmpty(responseModel.getTaskButton())) {
                        btnCompleteTask.setText(responseModel.getTaskButton());
                    }
                    btnCompleteTask.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            if (!BIG_Common_Utils.isStringNullOrEmpty(responseModel.getScreenNo())) {
//                                if (!CommonMethodsUtils.hasUsageAccessPermission(WordStoringActivity.this)) {
//                                    CommonMethodsUtils.showUsageAccessPermissionDialog(WordStoringActivity.this);
//                                    return;
//                                } else {
                                BIG_Common_Utils.Redirect(Big_SortWordsActivity.this, responseModel.getScreenNo(), "", "", "", "", "");
//                                }
                            } else if (!BIG_Common_Utils.isStringNullOrEmpty(responseModel.getTaskId())) {
                                Intent intent = new Intent(Big_SortWordsActivity.this, Big_TaskDetailsInfoActivity.class);
                                intent.putExtra("taskId", responseModel.getTaskId());
                                startActivity(intent);
                            } else {
                                Intent intent = new Intent(Big_SortWordsActivity.this, Big_TaskDetailsInfoActivity.class);
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
            setGameData();
            if (Integer.parseInt(responseModel.getRemainGameCount()) > 0 && !isTimerSet && !responseModel.getStatus().equals("2") && !BIG_Common_Utils.isStringNullOrEmpty(responseModel.getIsTodayTaskCompleted()) && responseModel.getIsTodayTaskCompleted().equals("1") && BIG_SharePrefs.getInstance().getBoolean(BIG_SharePrefs.IS_LOGIN)) {
                startCountDownAnimation();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void setGameData() {
        if (responseModel.getRemainGameCount() != null) {
            tvLeftCount.setText(responseModel.getRemainGameCount());
        }

        dataList.clear();
        for (int i = 0; i < responseModel.getData().size(); i++) {
            dataList.add(new Big_SortNumbersItem("" + i, responseModel.getData().get(i)));
        }

        temp.clear();
        temp.addAll(responseModel.getData());
        Collections.sort(temp);
        BIG_AppLogger.getInstance().e("SORTED", "SORTED : " + temp.toString());
        countUpAdapter.notifyDataSetChanged();

        if (responseModel.getPoints() != null) {
            tvWinPoints.setText(responseModel.getPoints());
        }
        tvRemaining.setText(BIG_Common_Utils.updateTimeRemainingLuckyNumber(Integer.parseInt(responseModel.getGameTime()) * 1000L));
    }

    private void loadAppLovinNativeAdsTask(LinearLayout layoutAds, FrameLayout frameLayoutNativeAd, TextView lblLoadingAds) {
        try {
            nativeAdLoaderTask = new MaxNativeAdLoader(BIG_Common_Utils.getRandomAdUnitId(responseMain.getLovinNativeID()), Big_SortWordsActivity.this);
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

    public void setTimer() {
        try {
            if (timer == null) {
                timer = new CountDownTimer(Integer.parseInt(responseModel.getGameTime()) * 1000L, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        tvRemaining.setText(BIG_Common_Utils.updateTimeRemainingLuckyNumber(millisUntilFinished));
                    }

                    @Override
                    public void onFinish() {
                        isFinishGame = true;
                        BIG_Common_Utils.logFirebaseEvent(Big_SortWordsActivity.this, "FeatureUsabilityItemId", "FeatureUsabilityEvent", "Word_Sorting_BigPrize", "Time Over");
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                betterLuckMessage = "Oops, Time is over.\nBetter luck next time!";
                                new Big_SaveSortWordsAsync(Big_SortWordsActivity.this, "0");
                            }
                        }, 100);
                    }
                }.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void changeCountDataValues(Big_SortWordResponseModel responseModel1) {
        responseModel = responseModel1;
        BIG_SharePrefs.getInstance().putString(BIG_SharePrefs.EarnedPoints, responseModel.getEarningPoint());

        if (responseModel.getTodayDate() != null) {
            todayDate = responseModel.getTodayDate();
        }
        if (responseModel.getLastDate() != null) {
            lastDate = responseModel.getLastDate();
        }
        if (responseModel.getNextGameTime() != null) {
            nextGameTime = Integer.parseInt(responseModel.getNextGameTime());
        }
        if (responseModel.getWinningPoints().equals("0")) {
            BIG_Common_Utils.logFirebaseEvent(Big_SortWordsActivity.this, "FeatureUsabilityItemId", "FeatureUsabilityEvent", "Word_Sorting_BigPrize", "Better Luck");
            showBetterluckPopup();
        } else {
            BIG_Common_Utils.logFirebaseEvent(Big_SortWordsActivity.this, "FeatureUsabilityItemId", "FeatureUsabilityEvent", "Word_Sorting_BigPrize", "Word Game Got Reward");
            showWinPopup(responseModel.getWinningPoints());
        }
    }

    public void AdpaterData(List<Big_SortNumbersItem> dataList) {
        countUpAdapter = new Big_SortNumbersAdapter(dataList, Big_SortWordsActivity.this, new Big_SortNumbersAdapter.ClickListener() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onItemClick(int position, View v, TextView textView) {
                try {
                    BIG_AppLogger.getInstance().e("CLICKED", "CLICKED : " + dataList.get(position).getValue());
                    if (BIG_SharePrefs.getInstance().getBoolean(BIG_SharePrefs.IS_LOGIN)) {
                        if (!isTimerSet && !isFinishGame && !dataList.get(position).isSelected()) {
                            if (temp.get(selPos).equals(textView.getText().toString())) {
                                dataList.get(position).setSelected(true);
                                countUpAdapter.notifyItemChanged(position);
                                temp.remove(selPos);
                                if (temp.isEmpty()) {
                                    isFinishGame = true;
                                    if (timer != null) {
                                        timer.cancel();
                                    }
                                    Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            new Big_SaveSortWordsAsync(Big_SortWordsActivity.this, responseModel.getPoints());
                                        }
                                    }, 100);
                                }
                            } else {
                                isFinishGame = true;
                                if (timer != null) {
                                    timer.cancel();
                                }
                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        betterLuckMessage = "Oops, Wrong word selected.\nBetter luck next time!";
                                        new Big_SaveSortWordsAsync(Big_SortWordsActivity.this, "0");
                                    }
                                }, 100);
                            }
                        }
                    } else {
                        BIG_Common_Utils.NotifyLogin(Big_SortWordsActivity.this);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        GridLayoutManager mGridLayoutManager = new GridLayoutManager(Big_SortWordsActivity.this, 5);
        rvNumber.setLayoutManager(mGridLayoutManager);
        rvNumber.setAdapter(countUpAdapter);
    }


    public void showWinPopup(String point) {
        try {
            Dialog dialogWin = new Dialog(Big_SortWordsActivity.this, android.R.style.Theme_Light);
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
                    BIG_Ads_Utils.showAppLovinInterstitialAd(Big_SortWordsActivity.this, new BIG_Ads_Utils.AdShownListener() {
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
                BIG_Ads_Utils.showAppLovinInterstitialAd(Big_SortWordsActivity.this, new BIG_Ads_Utils.AdShownListener() {
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
                    BIG_Common_Utils.GetCoinAnimation(Big_SortWordsActivity.this, layoutMain, layoutPoints);
                    tvPoints.setText(BIG_SharePrefs.getInstance().getEarningPointString());
                    if (!BIG_Common_Utils.isStringNullOrEmpty(responseModel.getRemainGameCount()) && responseModel.getRemainGameCount().equals("0")) {
                        llLimit.setVisibility(VISIBLE);
                        tvTimeUp.setVisibility(GONE);
                        isTimerSet = true;
                        tvNote.setText("You have exhausted today's Word Game Game limit, please try again tomorrow.");
                        tvLeftCount.setText(responseModel.getRemainGameCount());
                        tvRemaining.setText(BIG_Common_Utils.updateTimeRemainingLuckyNumber(Integer.parseInt(responseModel.getGameTime()) * 1000L));
                    } else {
                        llLimit.setVisibility(VISIBLE);
                        tvNote.setText("Next game will be unlocked in");
                        setGameData();
                        setTimer1(false);
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
        Dialog dilaogBetterluck = new Dialog(Big_SortWordsActivity.this, android.R.style.Theme_Light);
        dilaogBetterluck.getWindow().setBackgroundDrawableResource(R.color.black_transparent);
        dilaogBetterluck.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dilaogBetterluck.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        dilaogBetterluck.setCancelable(false);
        dilaogBetterluck.setCanceledOnTouchOutside(false);
        dilaogBetterluck.setContentView(R.layout.big_popup_betterluck);

        TextView tvMessage = dilaogBetterluck.findViewById(R.id.tvMessage);
        tvMessage.setText(betterLuckMessage);

        Button lDone = dilaogBetterluck.findViewById(R.id.btnOk);

        lDone.setOnClickListener(v -> {
            BIG_Ads_Utils.showAppLovinInterstitialAd(Big_SortWordsActivity.this, new BIG_Ads_Utils.AdShownListener() {
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
                if (!BIG_Common_Utils.isStringNullOrEmpty(responseModel.getRemainGameCount()) && responseModel.getRemainGameCount().equals("0")) {
                    llLimit.setVisibility(VISIBLE);
                    isTimerSet = true;
                    tvTimeUp.setVisibility(GONE);
                    tvNote.setText("You have exhausted today's Word Game Game limit, please try again tomorrow.");
                    tvLeftCount.setText(responseModel.getRemainGameCount());
                    tvRemaining.setText(BIG_Common_Utils.updateTimeRemainingLuckyNumber(Integer.parseInt(responseModel.getGameTime()) * 1000L));
                } else {
                    llLimit.setVisibility(VISIBLE);
                    tvNote.setText("Next game will be unlocked in");
                    setGameData();
                    setTimer1(false);
                }
            }
        });

        if (!isFinishing() && !dilaogBetterluck.isShowing()) {
            dilaogBetterluck.show();
        }
    }

    public void setTimer1(boolean isFromOnCreate) {
        if (BIG_Common_Utils.timeDiffSeconds(todayDate, lastDate) > (nextGameTime * 60)) {
        } else {
            isTimerSet = true;
            llLimit.setVisibility(VISIBLE);

            if (mainTimer != null) {
                mainTimer.cancel();
            }
            time = BIG_Common_Utils.timeDiffSeconds(todayDate, lastDate);
            mainTimer = new CountDownTimer(((nextGameTime * 60L) - time) * 1000L, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    isTimerSet = true;
                    tvTimeUp.setText(BIG_Common_Utils.updateTimeRemainingLuckyNumber(millisUntilFinished));
                }

                @Override
                public void onFinish() {
                    selPos = 0;
                    timer = null;
                    llLimit.setVisibility(GONE);
                    isTimerSet = false;
                    isFinishGame = false;
                    if (!isShowingAd) {
                        startCountDownAnimation();
                    }
                }
            }.start();
            if (isFromOnCreate) {
                isShowingAd = true;
                BIG_Ads_Utils.showAppLovinInterstitialAd(Big_SortWordsActivity.this, new BIG_Ads_Utils.AdShownListener() {
                    @Override
                    public void onAdDismiss() {
                        isShowingAd = false;
                        if (!isTimerSet) {
                            startCountDownAnimation();
                        }
                    }
                });
            }
        }
    }
    private boolean isShowingAd = false;
    @Override
    protected void onStop() {
        super.onStop();
        if (isFinishing()) {
            try {
                cancelCountDownAnimation();
                if (timer != null) {
                    timer.cancel();
                }
                if (mainTimer != null) {
                    mainTimer.cancel();
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
                if (nativeAdTask != null && nativeAdLoaderTask != null) {
                    nativeAdLoaderTask.destroy(nativeAdTask);
                    nativeAdTask = null;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void initCountDownAnimation() {
        countDownAnim = new Big_CountDownAnim(textView, BIG_Constants.countDownTimerCount);
        countDownAnim.setCountDownListener(new Big_CountDownAnim.CountDownListener() {
            @Override
            public void onCountDownEnd(Big_CountDownAnim animation) {
                layoutCountDownTimer.setVisibility(GONE);
                setTimer();
            }
        });
    }

    private void startCountDownAnimation() {
        layoutCountDownTimer.setVisibility(VISIBLE);
        // Customizable animation
        if (spinner.getSelectedItemPosition() == 1) { // Scale
            // Use scale animation
            Animation scaleAnimation = new ScaleAnimation(1.0f, 0.0f, 1.0f,
                    0.0f, Animation.RELATIVE_TO_SELF, 0.5f,
                    Animation.RELATIVE_TO_SELF, 0.5f);
            countDownAnim.setAnimation(scaleAnimation);
        } else if (spinner.getSelectedItemPosition() == 2) { // Set (Scale +
            // Alpha)
            // Use a set of animations
            Animation scaleAnimation = new ScaleAnimation(1.0f, 0.0f, 1.0f,
                    0.0f, Animation.RELATIVE_TO_SELF, 0.5f,
                    Animation.RELATIVE_TO_SELF, 0.5f);
            Animation alphaAnimation = new AlphaAnimation(1.0f, 0.0f);
            AnimationSet animationSet = new AnimationSet(false);
            animationSet.addAnimation(scaleAnimation);
            animationSet.addAnimation(alphaAnimation);
            countDownAnim.setAnimation(animationSet);
        }
        // Customizable start count
        countDownAnim.setStartCount(BIG_Constants.countDownTimerCount);
        countDownAnim.start();
    }

    private void cancelCountDownAnimation() {
        countDownAnim.cancel();
    }
}