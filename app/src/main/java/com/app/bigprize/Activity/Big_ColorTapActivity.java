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
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.airbnb.lottie.LottieAnimationView;
import com.app.bigprize.Async.Big_Get_Color_Async;
import com.app.bigprize.Async.Models.Big_Color_model;
import com.app.bigprize.Async.Models.Big_Response_Model;
import com.app.bigprize.Async.Big_Save_Color_Async;
import com.app.bigprize.R;
import com.app.bigprize.Value.BIG_Constants;
import com.app.bigprize.Widget.FlowLayout;
import com.app.bigprize.utils.BIG_Ads_Utils;
import com.app.bigprize.utils.BIG_Common_Utils;
import com.app.bigprize.utils.BIG_SharePrefs;
import com.app.bigprize.utils.BIG_Shared;
import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxError;
import com.applovin.mediation.nativeAds.MaxNativeAdListener;
import com.applovin.mediation.nativeAds.MaxNativeAdLoader;
import com.applovin.mediation.nativeAds.MaxNativeAdView;
import com.google.gson.Gson;

import java.util.Locale;
import java.util.Random;

public class Big_ColorTapActivity extends AppCompatActivity implements View.OnClickListener {
    private Big_Response_Model responseMain;
    private int currentScore = 0;
    private ImageView ivHistory, ivBack;

    private long lastClickTime = 0;

    private FrameLayout frameLayoutNativeAd;
    private MaxAd nativeAd, nativeAdWin, nativeAdTask;
    private MaxNativeAdLoader nativeAdLoader, nativeAdLoaderWin, nativeAdLoaderTask;
    private int currentblock = BIG_Constants.BLOCK_2;
    private float currentlight = BIG_Constants.LIGHT_EASY;

    private String todayDate, lastDate;
    int nextGameTime;
    private boolean isTimerOn = false;

    private TextView txtScore;

    Big_Color_model colorModel;
    int time;

    private TextView tvInfo, tvRemaining, tvWinPoints, tvPoints, lblLoadingAds, tvLeftCount, tvNote, tvTimeUp, tvTarget;

    private ProgressBar viewTimer;
    private CountDownTimer timer, timerSub;
    private FlowLayout flow;
    private MediaPlayer mMediaPlayer;
    private MediaPlayer mMediaPlayerFail;
    private LottieAnimationView ivLottieNoData;

    private LinearLayout layoutAds, llLimit, layoutPoints;

    private RelativeLayout  layoutMain;
    private boolean isFirstimeTouch = true;
    boolean isWrongSelect = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        BIG_Common_Utils.setDayNightTheme(Big_ColorTapActivity.this);
        setContentView(R.layout.big_activity_color_tap);
        responseMain = new Gson().fromJson(BIG_SharePrefs.getInstance().getString(BIG_SharePrefs.HomeData), Big_Response_Model.class);
        tvRemaining = findViewById(R.id.tvRemaining);
        ivLottieNoData = findViewById(R.id.ivLottieNoData);
        llLimit = findViewById(R.id.llLimit);
        lblLoadingAds = findViewById(R.id.lblLoadingAds);
        layoutAds = findViewById(R.id.layoutAds);
        layoutMain = findViewById(R.id.layoutMain);
        tvTimeUp = findViewById(R.id.tvTimeUp);
        ivHistory = findViewById(R.id.ivHistory);
        tvTarget = findViewById(R.id.tvTarget);
        tvNote = findViewById(R.id.tvNote);
        tvPoints = findViewById(R.id.tvPoints);
        layoutPoints = findViewById(R.id.layoutPoints);
        tvWinPoints = findViewById(R.id.tvWinPoints);


        mMediaPlayerFail = MediaPlayer.create(getApplicationContext(), R.raw.big_fail);

        TextView t6 = (TextView) findViewById(R.id.textView6);

        txtScore = (TextView) findViewById(R.id.txtscore);

        viewTimer = (ProgressBar) findViewById(R.id.timer);
        flow = (FlowLayout) findViewById(R.id.flowLayout);

        t6.setTypeface(BIG_Shared.appfont);

        txtScore.setTypeface(BIG_Shared.appfontBold);

        txtScore.setText(String.valueOf(currentScore));


        tvPoints.setText(BIG_SharePrefs.getInstance().getEarningPointString());
        layoutPoints.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (BIG_SharePrefs.getInstance().getBoolean(BIG_SharePrefs.IS_LOGIN)) {
                    startActivity(new Intent(Big_ColorTapActivity.this, Big_My_Wallet_Activity.class));
                } else {
                    BIG_Common_Utils.NotifyLogin(Big_ColorTapActivity.this);
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
                    startActivity(new Intent(Big_ColorTapActivity.this, Big_PointHistoryActivity.class).putExtra("type", BIG_Constants.HistoryType.Color).putExtra("title", "Find Odd color"));
                } else {
                    BIG_Common_Utils.NotifyLogin(Big_ColorTapActivity.this);
                }
            }
        });
        viewTimer.setProgress(30000);

        new Big_Get_Color_Async(Big_ColorTapActivity.this);

    }

    public void onBackPressed() {
        super.onBackPressed();


        if (timerSub != null) {
            timerSub.cancel();
        }
        if (timer != null) {
            timer.cancel();
        }
        finish();
    }

    public void setData(Big_Color_model responseModel) {
        colorModel = responseModel;


        try {
            if (responseModel.getStatus().equals("2")) {
                BIG_Ads_Utils.showAppLovinInterstitialAd(Big_ColorTapActivity.this, null);

                flow.post(new Runnable() {
                    @Override
                    public void run() {
                        int width = flow.getWidth();
                        ViewGroup.LayoutParams params = flow.getLayoutParams();
                        params.width = width;
                        params.height = params.width;
                        flow.setLayoutParams(params);

                        gameStart();
                    }
                });


                tvWinPoints.setText(responseModel.getPoints());
                if (responseModel.getPoints() != null) {
                }

                if (responseModel.getRemainGameCount() != null) {
                    tvRemaining.setText(responseModel.getRemainGameCount());
                }
                if (responseModel.getTargetScore() != null) {
                    tvTarget.setText(responseModel.getTargetScore());
                }
                llLimit.setVisibility(VISIBLE);
                tvTimeUp.setVisibility(View.GONE);
                tvNote.setText("You have exhausted today's Find Odd Color Game limit, please try again tomorrow.");

            } else {

                flow.post(new Runnable() {
                    @Override
                    public void run() {
                        int width = flow.getWidth();
                        ViewGroup.LayoutParams params = flow.getLayoutParams();
                        params.width = width;
                        params.height = params.width;
                        flow.setLayoutParams(params);
                        gameStart();
                    }
                });

                if (!BIG_Common_Utils.isStringNullOrEmpty(responseModel.getEarningPoint())) {
                    BIG_SharePrefs.getInstance().putString(BIG_SharePrefs.EarnedPoints, responseModel.getEarningPoint());
                    tvPoints.setText(BIG_SharePrefs.getInstance().getEarningPointString());
                }
                try {
                    if (!BIG_Common_Utils.isStringNullOrEmpty(colorModel.getHomeNote())) {
                        WebView webNote = findViewById(R.id.webNote);
                        webNote.getSettings().setJavaScriptEnabled(true);
                        webNote.setVisibility(View.VISIBLE);
                        webNote.loadDataWithBaseURL(null, colorModel.getHomeNote(), "text/html", "UTF-8", null);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    LinearLayout layoutCompleteTask = findViewById(R.id.layoutCompleteTask);
                    if (!BIG_Common_Utils.isStringNullOrEmpty(colorModel.getIsTodayTaskCompleted()) && colorModel.getIsTodayTaskCompleted().equals("0")) {
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
                        tvTaskNote.setText(colorModel.getTaskNote());

                        Button btnCompleteTask = findViewById(R.id.btnCompleteTask);
                        if (!BIG_Common_Utils.isStringNullOrEmpty(colorModel.getTaskButton())) {
                            btnCompleteTask.setText(colorModel.getTaskButton());
                        }
                        btnCompleteTask.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                if (!BIG_Common_Utils.isStringNullOrEmpty(colorModel.getScreenNo())) {
                                    BIG_Common_Utils.Redirect(Big_ColorTapActivity.this, colorModel.getScreenNo(), "", "", "", "", "");
                                } else if (!BIG_Common_Utils.isStringNullOrEmpty(colorModel.getTaskId())) {
                                    Intent intent = new Intent(Big_ColorTapActivity.this, Big_TaskDetailsInfoActivity.class);
                                    intent.putExtra("taskId", colorModel.getTaskId());
                                    startActivity(intent);
                                } else {
                                    Intent intent = new Intent(Big_ColorTapActivity.this, Big_TasksCategoryTypeActivity.class);
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

                if (responseModel.getNextGameTime() != null) {
                    nextGameTime = Integer.parseInt(responseModel.getNextGameTime());
                }

                if (responseModel.getRemainGameCount() != null) {
                    tvRemaining.setText(responseModel.getRemainGameCount());
                }
                if (responseModel.getTargetScore() != null) {
                    tvTarget.setText(responseModel.getTargetScore());
                }
                setTimer1(true);

                if (responseModel.getPoints() != null) {
                    tvWinPoints.setText(responseModel.getPoints());
                }
            }
        } catch (Exception e) {

        }

    }

    private int counter = 100;

    @SuppressLint("SuspiciousIndentation")


    private void gameStart() {
        flow.removeAllViews();
        if (timerSub != null) timerSub.cancel();

        if (currentScore >= 3 && currentScore < 6) {
            currentblock = BIG_Constants.BLOCK_3;
        } else if (currentScore >= 6 && currentScore < 9) {
            currentblock = BIG_Constants.BLOCK_4;
        } else if (currentScore >= 9 && currentScore < 12) {
            currentblock = BIG_Constants.BLOCK_5;
            currentlight = BIG_Constants.LIGHT_MEDIUM;
        } else if (currentScore >= 12 && currentScore < 15) {
            currentblock = BIG_Constants.BLOCK_6;
        } else if (currentScore >= 15 && currentScore < 18) {
            currentblock = BIG_Constants.BLOCK_7;
            currentlight = BIG_Constants.LIGHT_HARD;
        } else if (currentScore >= 18) {
            currentblock = BIG_Constants.BLOCK_8;
        }

        Random rand = new Random();
        int n = rand.nextInt(BIG_Constants.colors.length);
        int nl = rand.nextInt(currentblock * currentblock);
        int color = Color.parseColor(BIG_Constants.colors[n]);
        int blockWidth = (flow.getWidth() - (currentblock * 2)) / currentblock;

        int Clcounter = 0;
        for (int i = 0; i < currentblock; i++) {
            for (int j = 0; j < currentblock; j++) {
                FlowLayout.LayoutParam params = new FlowLayout.LayoutParam(blockWidth, blockWidth);
                View v = new View(this);
                v.setBackgroundResource(R.drawable.big_border_view);
                GradientDrawable drawable = (GradientDrawable) v.getBackground();

                v.setTag(Clcounter == nl);
                if (Clcounter == nl) {
                    drawable.setColor(lighter(color, currentlight));
                } else drawable.setColor(color);

                params.setMargins(1, 1, 1, 1);
                v.setLayoutParams(params);
                flow.addView(v);
                v.setOnClickListener(Big_ColorTapActivity.this);

                Clcounter++;
            }
        }

        setGameTimer();


    }

    private void loadAppLovinNativeAdsTask(LinearLayout layoutAds, FrameLayout frameLayoutNativeAd, TextView lblLoadingAds) {
        try {
            nativeAdLoaderTask = new MaxNativeAdLoader(BIG_Common_Utils.getRandomAdUnitId(responseMain.getLovinNativeID()), Big_ColorTapActivity.this);
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
                    //AppLogger.getInstance().e("AppLovin Failed WIN: ", error.getMessage());
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


    private void setGameTimer() {
        int count = 4000;//
        int tick = 50;
        viewTimer.setProgress(100);
        counter = 100;
        timerSub = new CountDownTimer(count, tick) {
            @Override
            public void onTick(long millisUntilFinished) {
                counter--;
                viewTimer.setProgress(counter);
            }

            @Override
            public void onFinish() {
                //gameOver();
                mMediaPlayerFail.start();
                if (!isFirstimeTouch) {
                    new Big_Save_Color_Async(Big_ColorTapActivity.this, colorModel.getPoints());
                    isFirstimeTouch = true;

                }
                viewTimer.setProgress(0);
                counter = 100;

            }
        };

        if (!isFirstimeTouch) {
            timerSub.start();
        } else {
            timerSub.cancel();
            isFirstimeTouch = false;
        }
    }

    public static int lighter(int color, float factor) {
        int red = (int) ((Color.red(color) * (1 - factor) / 255 + factor) * 255);
        int green = (int) ((Color.green(color) * (1 - factor) / 255 + factor) * 255);
        int blue = (int) ((Color.blue(color) * (1 - factor) / 255 + factor) * 255);
        return Color.argb(Color.alpha(color), red, green, blue);
    }


    private void setTimer1(boolean isFromOnCreate) {
        if (timeDiff(todayDate, lastDate) >= nextGameTime) {

        } else {
            isTimerOn = true;
            tvNote.setText("Next round will be unlocked in");
            llLimit.setVisibility(VISIBLE);
            if (timer != null) {
                timer.cancel();
            }
            time = timeDiff(todayDate, lastDate);

            timer = new CountDownTimer((nextGameTime - time) * 60000L, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    isTimerOn = true;
                    tvTimeUp.setText(updateTimeRemaining(millisUntilFinished));
                }

                @Override
                public void onFinish() {
                    currentblock = BIG_Constants.BLOCK_2;
                    currentScore = 0;
                    txtScore.setText(String.valueOf(currentScore));
                    isFirstimeTouch = true;
                    flow.post(new Runnable() {
                        @Override
                        public void run() {
                            int width = flow.getWidth();
                            ViewGroup.LayoutParams params = flow.getLayoutParams();
                            params.width = width;
                            params.height = params.width;
                            flow.setLayoutParams(params);
                            if (!isTimerOn) {
                                gameStart();
                            }
                        }
                    });
                    viewTimer.setProgress(100);
                    isTimerOn = true;
                    llLimit.setVisibility(View.GONE);


                }
            }.start();
            if (isFromOnCreate) {
                BIG_Ads_Utils.showAppLovinInterstitialAd(Big_ColorTapActivity.this, null);
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


    private void loadAppLovinNativeAds() {
        try {
            nativeAdLoader = new MaxNativeAdLoader(BIG_Common_Utils.getRandomAdUnitId(responseMain.getLovinNativeID()), Big_ColorTapActivity.this);
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
                    //AppLogger.getInstance().e("AppLovin Loaded: ", "===");
                }

                @Override
                public void onNativeAdLoadFailed(final String adUnitId, final MaxError error) {
                    layoutAds.setVisibility(View.GONE);
                    //AppLogger.getInstance().e("AppLovin Failed: ", error.getMessage());
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
            nativeAdLoaderWin = new MaxNativeAdLoader(BIG_Common_Utils.getRandomAdUnitId(responseMain.getLovinNativeID()), Big_ColorTapActivity.this);
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

    public void onClick(View v) {
        if (SystemClock.elapsedRealtime() - lastClickTime < 500) {
            return;
        }

        lastClickTime = SystemClock.elapsedRealtime();
        if (!colorModel.getStatus().equals("2")) {
            if (BIG_SharePrefs.getInstance().getBoolean(BIG_SharePrefs.IS_LOGIN)) {
                if (!isTimerOn) {
                    if (v.getTag() != null) {
                        if ((boolean) v.getTag() == false) {
                            if (timerSub != null) timerSub.cancel();

                            isWrongSelect = true;
                            mMediaPlayerFail.start();
                            new Big_Save_Color_Async(Big_ColorTapActivity.this, "0");
                        } else {
                            if (mMediaPlayer != null) {
                                mMediaPlayer.stop();
                                mMediaPlayer.release();
                                mMediaPlayer = null;
                            }

                            mMediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.big_btn_sound);
                            mMediaPlayer.start();
                            currentScore++;
                            txtScore.setText(String.valueOf(currentScore));

                            if (currentScore == Integer.parseInt(colorModel.getTargetScore())) {
                                if (timerSub != null) timerSub.cancel();

                                new Big_Save_Color_Async(Big_ColorTapActivity.this, colorModel.getPoints());
                                v.setEnabled(false);
                            } else {
                                gameStart();
                            }



                        }
                    }
                }
            } else {
                BIG_Common_Utils.NotifyLogin(Big_ColorTapActivity.this);
            }
        }

    }

    public void showWinPopup(String point, String isShowAds) {
        try {
            Dialog dialogWin = new Dialog(Big_ColorTapActivity.this, android.R.style.Theme_Light);
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

                    BIG_Ads_Utils.showAppLovinInterstitialAd(Big_ColorTapActivity.this, new BIG_Ads_Utils.AdShownListener() {
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
                BIG_Ads_Utils.showAppLovinInterstitialAd(Big_ColorTapActivity.this, new BIG_Ads_Utils.AdShownListener() {
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
                    tvRemaining.setText(colorModel.getRemainGameCount());
                    tvTarget.setText(colorModel.getTargetScore());
                    currentScore = 0;
                    txtScore.setText(String.valueOf(currentScore));
                    viewTimer.setProgress(0);

                    BIG_Common_Utils.GetCoinAnimation(Big_ColorTapActivity.this, layoutMain, layoutPoints);
                    tvPoints.setText(BIG_SharePrefs.getInstance().getEarningPointString());
                    llLimit.setVisibility(VISIBLE);
                    if (!BIG_Common_Utils.isStringNullOrEmpty(colorModel.getRemainGameCount()) && colorModel.getRemainGameCount().equals("0")) {
                        tvNote.setText("You have exhausted today's Find Odd Color limit, please try again tomorrow.");
                        isTimerOn = true;
                        tvTimeUp.setVisibility(View.GONE);
                    } else {
                        tvNote.setText("Next round will be unlocked in");
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

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (timer != null) {
            timer.cancel();
        }

        if (timerSub != null) {
            timerSub.cancel();
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

    public void showBetterluckPopup(String message) {
        try {
            final Dialog dilaogBetterluck = new Dialog(Big_ColorTapActivity.this, android.R.style.Theme_Light);
            dilaogBetterluck.getWindow().setBackgroundDrawableResource(R.color.black_transparent);
            dilaogBetterluck.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dilaogBetterluck.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            dilaogBetterluck.setContentView(R.layout.big_dialog_notify);
            dilaogBetterluck.setCancelable(false);

            Button btnOk = dilaogBetterluck.findViewById(R.id.btnOk);

            TextView tvMessage = dilaogBetterluck.findViewById(R.id.tvMessage);
            tvMessage.setText(message);
            btnOk.setOnClickListener(v -> {
                BIG_Ads_Utils.showAppLovinInterstitialAd(Big_ColorTapActivity.this, new BIG_Ads_Utils.AdShownListener() {
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
                    currentScore = 0;
                    txtScore.setText(String.valueOf(currentScore));
                    tvRemaining.setText(colorModel.getRemainGameCount());
                    tvTarget.setText(colorModel.getTargetScore());
                    viewTimer.setProgress(0);


                    if (!BIG_Common_Utils.isStringNullOrEmpty(colorModel.getRemainGameCount()) && colorModel.getRemainGameCount().equals("0")) {
                        llLimit.setVisibility(VISIBLE);
                        tvTimeUp.setVisibility(GONE);
                        isTimerOn = true;
                        tvNote.setText("You have exhausted today's Find Odd Color limit, please try again tomorrow.");
                    } else {
                        llLimit.setVisibility(VISIBLE);
                        tvNote.setText("Next round will be unlocked in");
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

    public void changeColorValues(Big_Color_model responseModel) {
        colorModel = responseModel;
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
            BIG_Common_Utils.logFirebaseEvent(Big_ColorTapActivity.this, "FeatureUsabilityItemId", "FeatureUsabilityEvent", "Color_Tap_BigPrize", "Better Luck");
            if (isWrongSelect) {
                showBetterluckPopup("Oops, You Tapped Wrong Color Box! Better Luck Next Time!");
            } else {
                showBetterluckPopup("Oops, time is over. Better luck, next time!");
            }

        } else {
            BIG_Common_Utils.logFirebaseEvent(Big_ColorTapActivity.this, "FeatureUsabilityItemId", "FeatureUsabilityEvent", "Color_Tap_BigPrize", "Find Odd Color Got Reward");
            showWinPopup(responseModel.getWinningPoints(), responseModel.getIsShowAds());

        }
        isWrongSelect = false;
    }
}