package com.app.bigprize.Activity;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import static com.app.bigprize.utils.BIG_Common_Utils.convertTimeInMillis;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
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
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.airbnb.lottie.LottieAnimationView;
import com.app.bigprize.Async.Big_Get_Brain_TrainerAsync;
import com.app.bigprize.Async.Models.Big_BrainTrainerModel;
import com.app.bigprize.Async.Models.Big_Response_Model;
import com.app.bigprize.Async.Models.Big_Save_Brain_TrainerAsync;
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


import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;

public class Big_Brain_trainerActivity extends AppCompatActivity {
    ImageView startButton;
    //Making ArrayList to sote the options
    ArrayList<Integer> options = new ArrayList<Integer>();
    private Big_Response_Model responseMain;
    private FrameLayout frameLayoutNativeAd;
    TextView resultTextView, timerTextView;
    TextView sumTextView, button2, button0;
    TextView button1, button3, playAgainButton;
    RelativeLayout gameRealtive;
    int locationOfCorrectAns;
    int score = 0;
    int numberOfQuestions = 0;
    LinearLayout layoutAds;
    ImageView ivBack, ivHistory;
    TextView tvTitle, tvPoints;
    LinearLayout layoutPoints, llLimit;
    private ProgressBar viewTimer;
    private MaxNativeAdLoader nativeAdLoader, nativeAdLoaderWin, nativeAdLoaderTask;
    private MaxAd nativeAd, nativeAdWin, nativeAdTask;
    private TextView lblLoadingAds;
    private CountDownTimer timer, timerSub;
    Big_BrainTrainerModel brainModel;
    private FlowLayout flow;
    TextView tvWinPoints, tvRemaining, tvTarget, tvTimeUp, tvNote;
    private String todayDate, lastDate;
    int nextGameTime;
    private int currentScore = 0;
    private MediaPlayer mMediaPlayerFail;
    private TextView txtScore;
    private boolean isTimerOn = false;

    int time;

    private MediaPlayer mMediaPlayer;
    private int currentblock = BIG_Constants.BLOCK_2;
    private boolean isFirstimeTouch = true;
    private float currentlight = BIG_Constants.LIGHT_EASY;
    private long lastClickTime = 0;
    boolean isWrongSelect = false;
    private RelativeLayout layoutMain;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        resultTextView = (TextView) findViewById(R.id.resultTextView);

        sumTextView = (TextView) findViewById(R.id.sumTextView);
        button0 = (TextView) findViewById(R.id.button2);
        button1 = (TextView) findViewById(R.id.button3);
        button2 = (TextView) findViewById(R.id.button4);
        button3 = (TextView) findViewById(R.id.button5);

        gameRealtive = (RelativeLayout) findViewById(R.id.gameRelative);
        layoutAds = (LinearLayout) findViewById(R.id.layoutAds);
        ivBack = (ImageView) findViewById(R.id.ivBack);
        ivHistory = (ImageView) findViewById(R.id.ivHistory);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvPoints = (TextView) findViewById(R.id.tvPoints);
        layoutPoints = (LinearLayout) findViewById(R.id.layoutPoints);
        lblLoadingAds = (TextView) findViewById(R.id.lblLoadingAds);
        tvWinPoints = (TextView) findViewById(R.id.tvWinPoints);
        tvRemaining = (TextView) findViewById(R.id.tvRemaining);
        tvTarget = (TextView) findViewById(R.id.tvTarget);
        llLimit = (LinearLayout) findViewById(R.id.llLimit);
        tvTimeUp = (TextView) findViewById(R.id.tvTimeUp);
        tvNote = (TextView) findViewById(R.id.tvNote);
        txtScore = (TextView) findViewById(R.id.txtscore);
        layoutMain = (RelativeLayout) findViewById(R.id.layoutMain);
        txtScore.setTypeface(BIG_Shared.appfontBold);


        mMediaPlayerFail = MediaPlayer.create(getApplicationContext(), R.raw.big_fail);


        viewTimer = (ProgressBar) findViewById(R.id.timer);


        txtScore.setTypeface(BIG_Shared.appfontBold);
        txtScore.setText(String.valueOf(currentScore));

        setGameTimer();
        tvPoints.setText(BIG_SharePrefs.getInstance().getEarningPointString());
        layoutPoints.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (BIG_SharePrefs.getInstance().getBoolean(BIG_SharePrefs.IS_LOGIN)) {
                    startActivity(new Intent(Big_Brain_trainerActivity.this, Big_My_Wallet_Activity.class));
                } else {
                    BIG_Common_Utils.NotifyLogin(Big_Brain_trainerActivity.this);
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
                    startActivity(new Intent(Big_Brain_trainerActivity.this, Big_PointHistoryActivity.class).putExtra("type", BIG_Constants.HistoryType.BRAIN_TRANER).putExtra("title", " Brain Trainer"));
                } else {
                    BIG_Common_Utils.NotifyLogin(Big_Brain_trainerActivity.this);
                }
            }
        });
        viewTimer.setProgress(30000);

        new Big_Get_Brain_TrainerAsync(Big_Brain_trainerActivity.this);
        generateQuestion();

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

    public void setData(Big_BrainTrainerModel responseModel) {
        brainModel = responseModel;

        try {
            if (responseModel.getStatus().equals("2")) {
                BIG_Ads_Utils.showAppLovinInterstitialAd(Big_Brain_trainerActivity.this, null);

//                flow.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        int width = flow.getWidth();
//                        ViewGroup.LayoutParams params = flow.getLayoutParams();
//                        params.width = width;
//                        params.height = params.width;
//                        flow.setLayoutParams(params);
//
//                        gameStart();
//                    }
//                });
                gameStart();

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
                tvNote.setText("You have exhausted today's Aansre Game limit, please try again tomorrow.");

            } else {
                gameStart();


                if (!BIG_Common_Utils.isStringNullOrEmpty(responseModel.getEarningPoint())) {
                    BIG_SharePrefs.getInstance().putString(BIG_SharePrefs.EarnedPoints, responseModel.getEarningPoint());
                    tvPoints.setText(BIG_SharePrefs.getInstance().getEarningPointString());
                }
                try {
                    if (!BIG_Common_Utils.isStringNullOrEmpty(brainModel.getHomeNote())) {
                        WebView webNote = findViewById(R.id.webNote);
                        webNote.getSettings().setJavaScriptEnabled(true);
                        webNote.setVisibility(View.VISIBLE);
                        webNote.loadDataWithBaseURL(null, brainModel.getHomeNote(), "text/html", "UTF-8", null);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    LinearLayout layoutCompleteTask = findViewById(R.id.layoutCompleteTask);
                    if (!BIG_Common_Utils.isStringNullOrEmpty(brainModel.getIsTodayTaskCompleted()) && brainModel.getIsTodayTaskCompleted().equals("0")) {
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
                        tvTaskNote.setText(brainModel.getTaskNote());

                        Button btnCompleteTask = findViewById(R.id.btnCompleteTask);
                        if (!BIG_Common_Utils.isStringNullOrEmpty(brainModel.getTaskButton())) {
                            btnCompleteTask.setText(brainModel.getTaskButton());
                        }
                        btnCompleteTask.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                if (!BIG_Common_Utils.isStringNullOrEmpty(brainModel.getScreenNo())) {
                                    BIG_Common_Utils.Redirect(Big_Brain_trainerActivity.this, brainModel.getScreenNo(), "", "", "", "", "");
                                } else if (!BIG_Common_Utils.isStringNullOrEmpty(brainModel.getTaskId())) {
                                    Intent intent = new Intent(Big_Brain_trainerActivity.this, Big_TaskDetailsInfoActivity.class);
                                    intent.putExtra("taskId", brainModel.getTaskId());
                                    startActivity(intent);
                                } else {
                                    Intent intent = new Intent(Big_Brain_trainerActivity.this, Big_TasksCategoryTypeActivity.class);
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
        start();
        generateQuestion();
        setGameTimer();
        chooseanswer();
    }

    private void chooseanswer() {
    }

    private void start() {

        gameRealtive.setVisibility(RelativeLayout.VISIBLE);
    }

    public void generateQuestion() {

        int incorrectAnswer;
        //Creating random numbers
        Random rand = new Random();
        int a = rand.nextInt(100);
        int b = rand.nextInt(100);

        sumTextView.setText(Integer.toString(a) + " + " + Integer.toString(b));

        locationOfCorrectAns = rand.nextInt(4);   //0,1,2,3
        //We need to clear our options ArrayList each time we make a new one
        options.clear();

        for (int i = 0; i < 4; i++) {
            if (i == locationOfCorrectAns)
                options.add(a + b);
            else {
                incorrectAnswer = rand.nextInt(100);
                while (incorrectAnswer == a + b)
                    incorrectAnswer = rand.nextInt(100);
                options.add(incorrectAnswer);
            }
        }
        button0.setText(Integer.toString(options.get(0)));
        button1.setText(Integer.toString(options.get(1)));
        button2.setText(Integer.toString(options.get(2)));
        button3.setText(Integer.toString(options.get(3)));

    }

    public void chooseanswer(View view) {

//        if (isTimerOn){
//            button0.setClickable(false);
//            button1.setClickable(false);
//            button2.setClickable(false);
//            button3.setClickable(false);
//
//        }else {

            gameStart();

//            setGameTimer();
            if (view.getTag().toString().equals(Integer.toString(locationOfCorrectAns))) {
                Log.i("Correct", view.getTag().toString());

                //Adding 1 to the score if the answer is correct
                score++;
                txtScore.setText(String.valueOf(score));
                resultTextView.setText("Correct Answer !");
                new Big_Save_Brain_TrainerAsync(Big_Brain_trainerActivity.this, brainModel.getPoints());

            } else {
                resultTextView.setText("InCorrect Answer !");
                new Big_Save_Brain_TrainerAsync(Big_Brain_trainerActivity.this, "0");


            }

            generateQuestion();

//        }

    }

    private void loadAppLovinNativeAdsTask(LinearLayout layoutAds, FrameLayout frameLayoutNativeAd, TextView lblLoadingAds) {
        try {
            nativeAdLoaderTask = new MaxNativeAdLoader(BIG_Common_Utils.getRandomAdUnitId(responseMain.getLovinNativeID()), Big_Brain_trainerActivity.this);
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
                    new Big_Save_Brain_TrainerAsync(Big_Brain_trainerActivity.this, brainModel.getPoints());
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

        }
    }


    private void setTimer1(boolean isFromOnCreate) {
        if (timeDiff(todayDate, lastDate) >= nextGameTime) {


        } else {
            isTimerOn = true;
            button0.setClickable(false);
            button1.setClickable(false);
            button2.setClickable(false);
            button3.setClickable(false);
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
                    currentblock = BIG_Constants.BLOCK_4;
                    currentScore = 0;
                    txtScore.setText(String.valueOf(currentScore));
                    isFirstimeTouch = true;

                    if (!isTimerOn) {

                        gameStart();
                    }
                    viewTimer.setProgress(100);
                    isTimerOn = true;
                    llLimit.setVisibility(GONE);
                    button0.setClickable(true);
                    button1.setClickable(true);
                    button2.setClickable(true);
                    button3.setClickable(true);

                }
            }.start();
            if (isFromOnCreate) {
                BIG_Ads_Utils.showAppLovinInterstitialAd(Big_Brain_trainerActivity.this, null);
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
            nativeAdLoader = new MaxNativeAdLoader(BIG_Common_Utils.getRandomAdUnitId(responseMain.getLovinNativeID()), Big_Brain_trainerActivity.this);
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
            nativeAdLoaderWin = new MaxNativeAdLoader(BIG_Common_Utils.getRandomAdUnitId(responseMain.getLovinNativeID()), Big_Brain_trainerActivity.this);
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
        if (!brainModel.getStatus().equals("2")) {
            if (BIG_SharePrefs.getInstance().getBoolean(BIG_SharePrefs.IS_LOGIN)) {
                if (!isTimerOn) {
                    if (v.getTag() != null) {
                        if ((boolean) v.getTag() == false) {
                            if (timerSub != null) timerSub.cancel();

                            isWrongSelect = true;
                            mMediaPlayerFail.start();
//                            new Big_Save_Brain_TrainerAsync(Big_Brain_trainerActivity.this, "0");
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

                            if (currentScore == Integer.parseInt(brainModel.getTargetScore())) {
                                if (timerSub != null) timerSub.cancel();

                                new Big_Save_Brain_TrainerAsync(Big_Brain_trainerActivity.this, brainModel.getPoints());
                                v.setEnabled(false);
                            } else {
                                gameStart();
                            }


                        }
                    }
                }
            } else {
                BIG_Common_Utils.NotifyLogin(Big_Brain_trainerActivity.this);
            }
        }

    }

    public void showWinPopup(String point, String isShowAds) {
        try {
            Dialog dialogWin = new Dialog(Big_Brain_trainerActivity.this, android.R.style.Theme_Light);
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

                    BIG_Ads_Utils.showAppLovinInterstitialAd(Big_Brain_trainerActivity.this, new BIG_Ads_Utils.AdShownListener() {
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
                BIG_Ads_Utils.showAppLovinInterstitialAd(Big_Brain_trainerActivity.this, new BIG_Ads_Utils.AdShownListener() {
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
                    tvRemaining.setText(brainModel.getRemainGameCount());
                    tvTarget.setText(brainModel.getTargetScore());
                    currentScore = 0;
                    txtScore.setText(String.valueOf(currentScore));
                    viewTimer.setProgress(0);

                    BIG_Common_Utils.GetCoinAnimation(Big_Brain_trainerActivity.this, layoutMain, layoutPoints);
                    tvPoints.setText(BIG_SharePrefs.getInstance().getEarningPointString());
                    llLimit.setVisibility(VISIBLE);

                    if (!BIG_Common_Utils.isStringNullOrEmpty(brainModel.getRemainGameCount()) && brainModel.getRemainGameCount().equals("0")) {
                        tvNote.setText("You have exhausted today's Brain Trainer limit, please try again tomorrow.");
                        isTimerOn = true;
                        tvTimeUp.setVisibility(View.GONE);
                    } else {
                        tvNote.setText("Next round will be unlocked inA RRRRyush");
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
            final Dialog dilaogBetterluck = new Dialog(Big_Brain_trainerActivity.this, android.R.style.Theme_Light);
            dilaogBetterluck.getWindow().setBackgroundDrawableResource(R.color.black_transparent);
            dilaogBetterluck.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dilaogBetterluck.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            dilaogBetterluck.setContentView(R.layout.big_dialog_notify);
            dilaogBetterluck.setCancelable(false);

            Button btnOk = dilaogBetterluck.findViewById(R.id.btnOk);

            TextView tvMessage = dilaogBetterluck.findViewById(R.id.tvMessage);
            tvMessage.setText(message);
            btnOk.setOnClickListener(v -> {
                BIG_Ads_Utils.showAppLovinInterstitialAd(Big_Brain_trainerActivity.this, new BIG_Ads_Utils.AdShownListener() {
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
                    tvRemaining.setText(brainModel.getRemainGameCount());
                    tvTarget.setText(brainModel.getTargetScore());
                    viewTimer.setProgress(0);


                    if (!BIG_Common_Utils.isStringNullOrEmpty(brainModel.getRemainGameCount()) && brainModel.getRemainGameCount().equals("0")) {
                        llLimit.setVisibility(VISIBLE);
                        tvTimeUp.setVisibility(GONE);
                        isTimerOn = true;
                        button0.setClickable(false);
                        button1.setClickable(false);
                        button2.setClickable(false);
                        button3.setClickable(false);
                        tvNote.setText("You have exhausted today's Brain Trainer limit, please try again tomorrow.");
                    } else {
                        llLimit.setVisibility(VISIBLE);
                        tvNote.setText("Next round will be unlocked in Ayush");
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

    public void changeBrainValues(Big_BrainTrainerModel responseModel) {
        brainModel = responseModel;
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
            BIG_Common_Utils.logFirebaseEvent(Big_Brain_trainerActivity.this, "FeatureUsabilityItemId", "FeatureUsabilityEvent", "Brain_Trainer_BigPrize", "Better Luck");
            if (isWrongSelect) {

            } else {
                showBetterluckPopup("Oops, time is over. Better luck, next time!");
            }

        } else {
            BIG_Common_Utils.logFirebaseEvent(Big_Brain_trainerActivity.this, "FeatureUsabilityItemId", "FeatureUsabilityEvent", "Brain_Trainer_BigPrize", "Brain Trainer Got Reward");
            showWinPopup(responseModel.getWinningPoints(), responseModel.getIsShowAds());

        }
        isWrongSelect = false;
    }
}