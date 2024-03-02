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

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.app.bigprize.Adapter.Big_AlphabetAdapter;
import com.app.bigprize.Async.Big_Get_Alphabet_Async;
import com.app.bigprize.Async.Models.Big_Alphabet_Item;
import com.app.bigprize.Async.Models.Big_Alphabet_model;
import com.app.bigprize.Async.Models.Big_Response_Model;
import com.app.bigprize.Async.Big_Save_Alphabet_Async;
import com.app.bigprize.R;
import com.app.bigprize.Value.BIG_Constants;
import com.app.bigprize.utils.BIG_Ads_Utils;
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
import java.util.Locale;

import io.reactivex.annotations.NonNull;

public class Big_AlphabetgameActivity extends AppCompatActivity {
    private Big_Response_Model responseMain;
    private RecyclerView rvAlphabet;
    private ImageView ivHistory;

    private FrameLayout frameLayoutNativeAd;
    private MaxAd nativeAd, nativeAdWin, nativeAdTask;
    private MaxNativeAdLoader nativeAdLoader, nativeAdLoaderWin, nativeAdLoaderTask;
    private String todayDate, lastDate;
    int gameTime, nextGameTime;

    private boolean isAssending = true, isTimerOver, isTimerOn = false;

    Big_Alphabet_model alphaModel;
    private long lastClickTime = 0;

    private Big_AlphabetAdapter alphabetAdapter;

    private CountDownTimer timer, Maintimer;
    int time, selPos = 0;

    private LottieAnimationView ltStartTimer;

    private LinearLayout layoutAds, llLimit, layoutPoints, layoutNoData, llRecycle, layoutCompleteTask;
    private TextView tvInfo, tvRemaining, tvWinPoints, tvPoints, lblLoadingAds, tvLeftCount, tvNote, tvTimeUp;
    public ArrayList<String> temp = new ArrayList<>();

    private RelativeLayout ilAttempt, layoutMain, relStartTimer;
    public List<Big_Alphabet_Item> data;

    boolean isWrongSelect = false;

    @SuppressLint("MissingInflatedId")

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.big_activity_alphabetgame);


        responseMain = new Gson().fromJson(BIG_SharePrefs.getInstance().getString(BIG_SharePrefs.HomeData), Big_Response_Model.class);
        tvRemaining = findViewById(R.id.tvRemaining);
        rvAlphabet = findViewById(R.id.rvAlphabet);
        lblLoadingAds = findViewById(R.id.lblLoadingAds);
        llLimit = findViewById(R.id.llLimit);
        relStartTimer = findViewById(R.id.relStartTimer);
        ltStartTimer = findViewById(R.id.ltStartTimer);
        ilAttempt = findViewById(R.id.ilAttempt);
        llRecycle = findViewById(R.id.llRecycle);
        layoutMain = findViewById(R.id.layoutMain);
        layoutCompleteTask = findViewById(R.id.layoutCompleteTask);
        tvTimeUp = findViewById(R.id.tvTimeUp);
        ivHistory = findViewById(R.id.ivHistory);
        tvLeftCount = findViewById(R.id.tvLeftCount);
        layoutAds = findViewById(R.id.layoutAds);
        tvInfo = findViewById(R.id.tvInfo);
        tvNote = findViewById(R.id.tvNote);
        tvPoints = findViewById(R.id.tvPoints);
        layoutPoints = findViewById(R.id.layoutPoints);
        tvWinPoints = findViewById(R.id.tvWinPoints);
        tvPoints.setText(BIG_SharePrefs.getInstance().getEarningPointString());

        layoutPoints.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (BIG_SharePrefs.getInstance().getBoolean(BIG_SharePrefs.IS_LOGIN)) {
                    startActivity(new Intent(Big_AlphabetgameActivity.this, Big_My_Wallet_Activity.class));
                } else {
                    BIG_Common_Utils.NotifyLogin(Big_AlphabetgameActivity.this);
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
            layoutAds.setVisibility(GONE);
        }
        ivHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (BIG_SharePrefs.getInstance().getBoolean(BIG_SharePrefs.IS_LOGIN)) {
                    startActivity(new Intent(Big_AlphabetgameActivity.this, Big_PointHistoryActivity.class)
                            .putExtra("type", BIG_Constants.HistoryType.Alpha)
                            .putExtra("title", "ABCD Game"));
                } else {
                    BIG_Common_Utils.NotifyLogin(Big_AlphabetgameActivity.this);
                }
            }
        });

        new Big_Get_Alphabet_Async(Big_AlphabetgameActivity.this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (isFinishing()) {
            try {
                if (timer != null) {
                    timer.cancel();
                }
                if (Maintimer != null) {
                    Maintimer.cancel();
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

    public void setGameStartTimer() {
          if (BIG_SharePrefs.getInstance().getBoolean(BIG_SharePrefs.IS_LOGIN)){
              relStartTimer.setVisibility(VISIBLE);
              if (layoutCompleteTask.getVisibility() == GONE)
              {
                  relStartTimer.setVisibility(VISIBLE);
                  ltStartTimer.setMinFrame(60);
                  ltStartTimer.playAnimation();
                  ltStartTimer.addAnimatorListener(new Animator.AnimatorListener() {
                      @Override
                      public void onAnimationStart(@NonNull Animator animation) {

                      }

                      @Override
                      public void onAnimationEnd(@NonNull Animator animation) {
                          relStartTimer.setVisibility(GONE);
                          setTimer();

                      }

                      @Override
                      public void onAnimationCancel(@NonNull Animator animation) {

                      }

                      @Override
                      public void onAnimationRepeat(@NonNull Animator animation) {

                      }
                  });
              }
          }




    }

    private void loadAppLovinNativeAds() {
        try {
            nativeAdLoader = new MaxNativeAdLoader(BIG_Common_Utils.getRandomAdUnitId(responseMain.getLovinNativeID()), Big_AlphabetgameActivity.this);
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
            nativeAdLoaderWin = new MaxNativeAdLoader(BIG_Common_Utils.getRandomAdUnitId(responseMain.getLovinNativeID()), Big_AlphabetgameActivity.this);
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
    private Big_Alphabet_model responseModel;
    @SuppressLint("SetTextI18n")
    public void setData(Big_Alphabet_model responseModel1) {
        responseModel=responseModel1;
        try {

            if (responseModel.getStatus().equals("2")) {
                BIG_Ads_Utils.showAppLovinInterstitialAd(Big_AlphabetgameActivity.this, null);
                llLimit.setVisibility(VISIBLE);
                if (responseModel.getGameText() != null) {
                    tvInfo.setText(responseModel.getGameText());
                }

                if (responseModel.getRemainGameCount() != null) {
                    tvLeftCount.setText("Today's " + " " + responseModel.getRemainGameCount());
                }
                isTimerOn = true;
                tvTimeUp.setVisibility(GONE);
                tvNote.setText("You have exhausted today's ABCD Game limit, please try again tomorrow.");
                data = new ArrayList<>();
                data.addAll(responseModel.getData());
                AdpaterData(data);
                if (responseModel.getPoints() != null) {
                    tvWinPoints.setText(responseModel.getPoints());
                }
                tvRemaining.setText(BIG_Common_Utils.updateTimeRemainingLuckyNumber(Integer.parseInt(alphaModel.getGameTime()) * 1000L));
            } else {
                alphaModel = responseModel;
                if (!BIG_Common_Utils.isStringNullOrEmpty(responseModel.getEarningPoint())) {
                    BIG_SharePrefs.getInstance().putString(BIG_SharePrefs.EarnedPoints, responseModel.getEarningPoint());
                    tvPoints.setText(BIG_SharePrefs.getInstance().getEarningPointString());
                }
                try {
                    if (!BIG_Common_Utils.isStringNullOrEmpty(alphaModel.getHomeNote())) {
                        WebView webNote = findViewById(R.id.webNote);
                        webNote.getSettings().setJavaScriptEnabled(true);
                        webNote.setVisibility(View.VISIBLE);
                        webNote.loadDataWithBaseURL(null, alphaModel.getHomeNote(), "text/html", "UTF-8", null);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    LinearLayout layoutCompleteTask = findViewById(R.id.layoutCompleteTask);
                    if (!BIG_Common_Utils.isStringNullOrEmpty(alphaModel.getIsTodayTaskCompleted()) && alphaModel.getIsTodayTaskCompleted().equals("0")) {
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
                        tvTaskNote.setText(alphaModel.getTaskNote());

                        Button btnCompleteTask = findViewById(R.id.btnCompleteTask);
                        if (!BIG_Common_Utils.isStringNullOrEmpty(alphaModel.getTaskButton())) {
                            btnCompleteTask.setText(alphaModel.getTaskButton());
                        }
                        btnCompleteTask.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                if (!BIG_Common_Utils.isStringNullOrEmpty(alphaModel.getScreenNo())) {

                                        BIG_Common_Utils.Redirect(Big_AlphabetgameActivity.this, alphaModel.getScreenNo(), "", "", "", "", "");

                                } else if (!BIG_Common_Utils.isStringNullOrEmpty(alphaModel.getTaskId())) {
                                    Intent intent = new Intent(Big_AlphabetgameActivity.this, Big_TaskDetailsInfoActivity.class);
                                    intent.putExtra("taskId", alphaModel.getTaskId());
                                    startActivity(intent);
                                } else {
                                    Intent intent = new Intent(Big_AlphabetgameActivity.this, Big_TasksCategoryTypeActivity.class);
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

                if (responseModel.getTodayDate() != null) {
                    todayDate = responseModel.getTodayDate();
                }
                if (responseModel.getLastDate() != null) {
                    lastDate = responseModel.getLastDate();
                }
                if (responseModel.getGameTime() != null) {
                    gameTime = Integer.parseInt(responseModel.getGameTime());
                }
                if (responseModel.getNextGameTime() != null) {
                    nextGameTime = Integer.parseInt(responseModel.getNextGameTime());
                }

                if (responseModel.getRemainGameCount() != null) {
                    tvLeftCount.setText("Today's " + " " + responseModel.getRemainGameCount());
                }
                if (responseModel.getGameText() != null) {
                    tvInfo.setText(responseModel.getGameText());
                }
                data = new ArrayList<>();
                data.addAll(responseModel.getData());
                temp = new ArrayList<>();
                for (int i = 0; i < responseModel.getData().size(); i++) {

                    temp.add((String.valueOf(responseModel.getData().get(i).getValue())));
                }

                Collections.sort(temp);

                setTimer1(true);

                AdpaterData(data);
                if (responseModel.getPoints() != null) {
                    tvWinPoints.setText(responseModel.getPoints());
                }
                tvRemaining.setText(BIG_Common_Utils.updateTimeRemainingLuckyNumber(Integer.parseInt(alphaModel.getGameTime()) * 1000L));
            }
        } catch (Exception e) {

        }
    }

    private void loadAppLovinNativeAdsTask(LinearLayout layoutAds, FrameLayout frameLayoutNativeAd, TextView lblLoadingAds) {
        try {
            nativeAdLoaderTask = new MaxNativeAdLoader(BIG_Common_Utils.getRandomAdUnitId(responseMain.getLovinNativeID()), Big_AlphabetgameActivity.this);
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

    public void AdpaterData(List<Big_Alphabet_Item> data) {
        alphabetAdapter = new Big_AlphabetAdapter(data, Big_AlphabetgameActivity.this, new Big_AlphabetAdapter.ClickListener() {
            @SuppressLint({"ResourceAsColor", "UseCompatLoadingForDrawables"})
            @Override
            public void onItemClick(int position, View v, TextView textView) {
                if (SystemClock.elapsedRealtime() - lastClickTime < 400) {
                    return;
                }

                lastClickTime = SystemClock.elapsedRealtime();
                if (relStartTimer.getVisibility() == GONE) {
                    if (!isTimerOn) {
                        if (BIG_SharePrefs.getInstance().getBoolean(BIG_SharePrefs.IS_LOGIN)) {
                            setTimer();
                            if (temp.get(selPos).equals((textView.getText().toString()))) {
                                textView.setBackground(getResources().getDrawable(R.drawable.big_bg_count_));
                                v.setEnabled(false);
                                temp.remove(selPos);
                                if (temp.isEmpty()) {
                                    timer.cancel();
                                    new Big_Save_Alphabet_Async(Big_AlphabetgameActivity.this, alphaModel.getPoints());
                                }
                            } else {
                                isWrongSelect = true;
                                timer.cancel();
                                new Big_Save_Alphabet_Async(Big_AlphabetgameActivity.this, "0");
                            }
                        } else {
                            BIG_Common_Utils.NotifyLogin(Big_AlphabetgameActivity.this);
                        }
                    }
                }

            }
        });
        GridLayoutManager mGridLayoutManager = new GridLayoutManager(Big_AlphabetgameActivity.this, 5);
        rvAlphabet.setLayoutManager(mGridLayoutManager);
        rvAlphabet.setAdapter(alphabetAdapter);
    }

    public void setTimer() {
        try {
            if (timer == null) {
                timer = new CountDownTimer(Integer.parseInt(alphaModel.getGameTime()) * 1000L, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        isTimerOver = false;
                        tvRemaining.setText(BIG_Common_Utils.updateTimeRemainingLuckyNumber(millisUntilFinished));
                    }

                    @Override
                    public void onFinish() {
                        isTimerOver = true;
                        BIG_Common_Utils.logFirebaseEvent(Big_AlphabetgameActivity.this, "FeatureUsabilityItemId", "FeatureUsabilityEvent", "Alphabet_BigPrize", "Time Over");
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                new Big_Save_Alphabet_Async(Big_AlphabetgameActivity.this, "0");
                            }
                        }, 100);
                    }
                }.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setTimer1(boolean isFromOnCreate) {

        if (timeDiff(todayDate, lastDate) > nextGameTime) {
            setGameStartTimer();
        } else {
            isTimerOn = true;
            tvTimeUp.setText("Try After Some Time");
            llLimit.setVisibility(VISIBLE);

            if (Maintimer != null) {
                Maintimer.cancel();
            }
            time = timeDiff(todayDate, lastDate);

            Maintimer = new CountDownTimer((nextGameTime - time) * 60000L, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    isTimerOn = true;
                    tvTimeUp.setText(updateTimeRemaining(millisUntilFinished));
                }

                @Override
                public void onFinish() {
                    setGameStartTimer();
                    selPos = 0;
                    timer = null;
                    isTimerOn = false;
                    llLimit.setVisibility(GONE);
                    temp = new ArrayList<>();
                    for (int i = 0; i < alphaModel.getData().size(); i++) {

                        temp.add((String.valueOf(alphaModel.getData().get(i).getValue())));
                    }
                    Collections.sort(temp);

                    data = new ArrayList<>();
                    data.addAll(alphaModel.getData());
                    AdpaterData(data);
                }
            }.start();
            if (isFromOnCreate && relStartTimer.getVisibility() == GONE) {
                BIG_Ads_Utils.showAppLovinInterstitialAd(Big_AlphabetgameActivity.this, null);
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
            Dialog dialogWin = new Dialog(Big_AlphabetgameActivity.this, android.R.style.Theme_Light);
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
                    BIG_Ads_Utils.showAppLovinInterstitialAd(Big_AlphabetgameActivity.this, new BIG_Ads_Utils.AdShownListener() {
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
                lblPoints.setText((pt <= 1 ? "Point" : "Points"));
            } catch (NumberFormatException e) {
                e.printStackTrace();
                lblPoints.setText("Points");
            }

            btnOk.setOnClickListener(v -> {

                BIG_Ads_Utils.showAppLovinInterstitialAd(Big_AlphabetgameActivity.this, new BIG_Ads_Utils.AdShownListener() {
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
                    tvLeftCount.setText("Today's " + " " + alphaModel.getRemainGameCount());
                    tvRemaining.setText(BIG_Common_Utils.updateTimeRemainingLuckyNumber(Integer.parseInt(alphaModel.getGameTime()) * 1000));

                    BIG_Common_Utils.GetCoinAnimation(Big_AlphabetgameActivity.this, layoutMain, layoutPoints);
                    tvPoints.setText(BIG_SharePrefs.getInstance().getEarningPointString());
                    if (!BIG_Common_Utils.isStringNullOrEmpty(alphaModel.getRemainGameCount()) && alphaModel.getRemainGameCount().equals("0")) {
                        isTimerOn = true;
                        llLimit.setVisibility(VISIBLE);
                        tvTimeUp.setVisibility(GONE);
                        tvNote.setText("You have exhausted today's ABCD Game limit, please try again tomorrow.");
                    } else {
                        llLimit.setVisibility(VISIBLE);
                        tvNote.setText("Try After Some Time");
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

    public void showBetterluckPopup(String message) {
        try {
            final Dialog dilaogBetterluck = new Dialog(Big_AlphabetgameActivity.this, android.R.style.Theme_Light);
            dilaogBetterluck.getWindow().setBackgroundDrawableResource(R.color.black_transparent);
            dilaogBetterluck.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dilaogBetterluck.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            dilaogBetterluck.setContentView(R.layout.big_dialog_notify);
            dilaogBetterluck.setCancelable(false);

            Button btnOk = dilaogBetterluck.findViewById(R.id.btnOk);

            TextView tvMessage = dilaogBetterluck.findViewById(R.id.tvMessage);
            tvMessage.setText(message);
            btnOk.setOnClickListener(v -> {
                BIG_Ads_Utils.showAppLovinInterstitialAd(Big_AlphabetgameActivity.this, new BIG_Ads_Utils.AdShownListener() {
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
                    tvLeftCount.setText("Today's " + " " + alphaModel.getRemainGameCount());
                    tvRemaining.setText(BIG_Common_Utils.updateTimeRemainingLuckyNumber(Integer.parseInt(alphaModel.getGameTime()) * 1000));
                    if (!BIG_Common_Utils.isStringNullOrEmpty(alphaModel.getRemainGameCount()) && alphaModel.getRemainGameCount().equals("0")) {
                        isTimerOn = true;
                        llLimit.setVisibility(VISIBLE);
                        tvTimeUp.setVisibility(GONE);
                        tvNote.setText("You have exhausted today's Alphabet Game limit, please try again tomorrow.");
                    } else {
                        llLimit.setVisibility(VISIBLE);
                        tvNote.setText("Try After Some Time");
                        setTimer1(false);
                    }
                }
            });

            if (!isFinishing() && !dilaogBetterluck.isShowing()) {
                dilaogBetterluck.show();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void changeCountDataValues(Big_Alphabet_model responseModel) {
        alphaModel = responseModel;
        BIG_SharePrefs.getInstance().putString(BIG_SharePrefs.EarnedPoints, responseModel.getEarningPoint());

        if (responseModel.getTodayDate() != null) {
            todayDate = responseModel.getTodayDate();
        }
        if (responseModel.getLastDate() != null) {
            lastDate = responseModel.getLastDate();
        }
        if (responseModel.getGameTime() != null) {
            gameTime = Integer.parseInt(responseModel.getGameTime());
        }
        if (responseModel.getNextGameTime() != null) {
            nextGameTime = Integer.parseInt(responseModel.getNextGameTime());
        }
        if (responseModel.getGameText() != null) {
            tvInfo.setText(responseModel.getGameText());
        }


        if (responseModel.getWinningPoints().equals("0")) {
            BIG_Common_Utils.logFirebaseEvent(Big_AlphabetgameActivity.this, "FeatureUsabilityItemId", "FeatureUsabilityEvent", "Alphabet_BigPrize", "Better Luck");
            if (isWrongSelect) {
                showBetterluckPopup("Oops, you chose wrong alphabet. Better luck next time!");
            } else {
                showBetterluckPopup("Oops, time is over. Better luck, next time!");
            }
        } else {
            BIG_Common_Utils.logFirebaseEvent(Big_AlphabetgameActivity.this, "FeatureUsabilityItemId", "FeatureUsabilityEvent", "Alphabet_BigPrize", "Alphabet Got Reward");
            showWinPopup(responseModel.getWinningPoints(), responseModel.getIsShowAds());
        }
        isWrongSelect = false;

    }
}