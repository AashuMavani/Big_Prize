package com.app.bigprize.Activity;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.airbnb.lottie.LottieAnimationView;
import com.app.bigprize.Async.Big_GetTypedTextAsync;
import com.app.bigprize.Async.Big_SaveTypedTextAsync;
import com.app.bigprize.Async.Models.Big_Response_Model;
import com.app.bigprize.Async.Models.Big_TypeTextResponseModel;
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

public class Big_TypeTextGameActivity extends AppCompatActivity {
    private TextView tvDailyLimit, tvText, tvRemainCount, tvRemainingTime, tvWinningPoints, tvPoints, lblLoadingAds, tvMainTimer, lblTimer, tvAttemptsLeft;
    private EditText etText;
    private Button btnClaimNow;
    private Big_Response_Model responseMain;
    private LinearLayout layoutPoints, layoutTimer, layoutContent, layoutCompleteTask;
    private CountDownTimer mainTimer, textTypingTimer;
    private int remainCount, time, lifeline, textTypingTime;
    private String todayDate, lastDate, mainTimerTime;
    private Big_TypeTextResponseModel objTextTypingData;
    private ImageView ivHistory, ivHelp;
    private RelativeLayout layoutMain;
    private MaxAd nativeAd, nativeAdWin, nativeAdTask, nativeAdTimer;
    private MaxNativeAdLoader nativeAdLoader, nativeAdLoaderWin, nativeAdLoaderTask, nativeAdLoaderTimer;
    private boolean isTimerSet = false, isTimerOver = false;
    private LinearLayout layoutAds;
    private FrameLayout frameLayoutNativeAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        BIG_Common_Utils.setDayNightTheme(Big_TypeTextGameActivity.this);
        setContentView(R.layout.big_activity_type_text_game);


        responseMain = new Gson().fromJson(BIG_SharePrefs.getInstance().getString(BIG_SharePrefs.HomeData), Big_Response_Model.class);
        layoutMain = findViewById(R.id.layoutMain);
        layoutContent = findViewById(R.id.layoutContent);
        layoutContent.setVisibility(View.INVISIBLE);
        ivHistory = findViewById(R.id.ivHistory);
        layoutTimer = findViewById(R.id.layoutTimer);
        layoutPoints = findViewById(R.id.layoutPoints);
        layoutPoints.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (BIG_SharePrefs.getInstance().getBoolean(BIG_SharePrefs.IS_LOGIN)) {
                    startActivity(new Intent(Big_TypeTextGameActivity.this, Big_My_Wallet_Activity.class));
                } else {
                    BIG_Common_Utils.NotifyLogin(Big_TypeTextGameActivity.this);
                }
            }
        });
        ivHelp = findViewById(R.id.ivHelp);
        tvPoints = findViewById(R.id.tvPoints);
        tvPoints.setText(BIG_SharePrefs.getInstance().getEarningPointString());
        tvWinningPoints = findViewById(R.id.tvWinningPoints);
        tvDailyLimit = findViewById(R.id.tvDailyLimit);
        tvRemainCount = findViewById(R.id.tvRemainCount);
        tvRemainingTime = findViewById(R.id.tvRemainingTime);
        tvMainTimer = findViewById(R.id.tvMainTimer);
        lblTimer = findViewById(R.id.lblTimer);
        tvAttemptsLeft = findViewById(R.id.tvAttemptsLeft);
        tvText = findViewById(R.id.tvText);
        etText = findViewById(R.id.etText);
        etText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                try {
                    if (etText.getText().toString().length() > 0) {
                        setTimer();
                    }
                    if (tvText.getText().toString().trim().length() > 0 && etText.getText().toString().length() >= tvText.getText().toString().trim().length()) {
                        btnClaimNow.setEnabled(true);

                    } else {
                        btnClaimNow.setEnabled(false);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        etText.setCustomSelectionActionModeCallback(new ActionMode.Callback() {

            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            public void onDestroyActionMode(ActionMode mode) {
            }

            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                return false;
            }
        });
        etText.setLongClickable(false);
        etText.setTextIsSelectable(false);

        btnClaimNow = findViewById(R.id.btnClaimNow);
        btnClaimNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    BIG_Common_Utils.setEnableDisable(Big_TypeTextGameActivity.this, v);
                    if (BIG_SharePrefs.getInstance().getBoolean(BIG_SharePrefs.IS_LOGIN)) {
                        if (remainCount > 0) {
                            if (lifeline > 0 && !tvText.getText().toString().trim().equals(etText.getText().toString())) {
                                lifeline = lifeline - 1;
                                tvAttemptsLeft.setText(String.valueOf(lifeline));
                                BIG_Common_Utils.NotifyMessage(Big_TypeTextGameActivity.this, "Text Didn't Match!", "Sorry, typed text did not match with given text, please try again.\n\n" + lifeline + " attempt is left.", false);
                            } else {
                                if (!isTimerOver) {
                                    if (textTypingTimer != null) {
                                        textTypingTimer.cancel();
                                        textTypingTimer = null;
                                    }
                                    BIG_AppLogger.getInstance().e("#poinht",objTextTypingData.getData().getPoints());

                                    new Big_SaveTypedTextAsync(Big_TypeTextGameActivity.this, objTextTypingData.getData().getPoints(), objTextTypingData.getData().getId(), etText.getText().toString());
                                } else {
                                    BIG_Common_Utils.Notify(Big_TypeTextGameActivity.this, getString(R.string.app_name), "Time is over. Better luck, next time!", false);
                                }

                            }
                        } else {
                            BIG_Common_Utils.Notify(Big_TypeTextGameActivity.this, "Type & win Limit Over", "You have exhausted your Type & win daily limit, please try again tomorrow.", false);
                        }
                    } else {
                        BIG_Common_Utils.NotifyLogin(Big_TypeTextGameActivity.this);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
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
                    startActivity(new Intent(Big_TypeTextGameActivity.this, Big_PointHistoryActivity.class)
                            .putExtra("type", BIG_Constants.HistoryType.TYPE_TEXT_TYPING)
                            .putExtra("title", "Type & win History"));
                } else {
                    BIG_Common_Utils.NotifyLogin(Big_TypeTextGameActivity.this);
                }
            }
        });
        new Big_GetTypedTextAsync(Big_TypeTextGameActivity.this);
    }

    public void changeTextTypingDataValues(Big_TypeTextResponseModel responseModel) {
        BIG_SharePrefs.getInstance().putString(BIG_SharePrefs.EarnedPoints, responseModel.getEarningPoint());

        tvDailyLimit.setText(responseModel.getTotalCount());

        if (responseModel.getTodayDate() != null) {
            todayDate = responseModel.getTodayDate();
        }
        if (responseModel.getLastDate() != null) {
            lastDate = responseModel.getLastDate();
        }

        if (responseModel.getMainTimer() != null) {
            mainTimerTime = responseModel.getMainTimer();
        }
        if (responseModel.getRemainCount() != null) {
            tvRemainCount.setText(responseModel.getRemainCount());
            remainCount = Integer.parseInt(responseModel.getRemainCount());
        }
        objTextTypingData.setData(responseModel.getData());
        if (responseModel.getWinningPoints().equals("0")) {
            BIG_Common_Utils.logFirebaseEvent(Big_TypeTextGameActivity.this, "FeatureUsabilityItemId", "FeatureUsabilityEvent", "Text_Typing_BigPrize", "Better Luck");
            showBetterluckPopup();
        } else {
            BIG_Common_Utils.logFirebaseEvent(Big_TypeTextGameActivity.this, "FeatureUsabilityItemId", "FeatureUsabilityEvent", "Text_Typing_BigPrize", "Type & win  Got Reward");
            showWinPopup(responseModel.getWinningPoints());
        }
    }

    public void setTimer() {
        try {
            if (textTypingTimer == null) {
                isTimerOver = false;
                textTypingTimer = new CountDownTimer(textTypingTime * 1000L, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        isTimerOver = false;
                        tvRemainingTime.setText(BIG_Common_Utils.updateTimeRemainingLuckyNumber(millisUntilFinished));
                    }

                    @Override
                    public void onFinish() {
                        isTimerOver = true;
                        BIG_Common_Utils.logFirebaseEvent(Big_TypeTextGameActivity.this, "FeatureUsabilityItemId", "FeatureUsabilityEvent", "Text_Typing_BigPrize", "Time Over");
//                        notifyTimeOver(Big_TypeTextGameActivity.this, getString(R.string.app_name), "Oops, time is over. Better luck, next time!", true);
                        new Big_SaveTypedTextAsync(Big_TypeTextGameActivity.this, "0", objTextTypingData.getData().getId(), etText.getText().toString());

                        if (textTypingTimer != null) {
                            textTypingTimer.cancel();
                            textTypingTimer = null;
                        }
                    }
                }.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    private void notifyTimeOver(final Activity activity, String title, String message, boolean isFinish) {
//        try {
//            if (activity != null) {
//                final Dialog dialog1 = new Dialog(activity, android.R.style.Theme_Light);
//                dialog1.getWindow().setBackgroundDrawableResource(R.color.black_transparent);
//                dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
//                dialog1.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//                dialog1.setContentView(R.layout.big_popup_message_notify);
//                dialog1.setCancelable(false);
//
//                Button btnOk = dialog1.findViewById(R.id.btnOk);
//                TextView tvTitle = dialog1.findViewById(R.id.tvTitle);
//                tvTitle.setText(title);
//                TextView tvMessage = dialog1.findViewById(R.id.tvMessage);
//                tvMessage.setText(message);
//                btnOk.setOnClickListener(v -> {
//                    BIG_Ads_Utils.showAppLovinInterstitialAd(Big_TypeTextGameActivity.this, new BIG_Ads_Utils.AdShownListener() {
//                        @Override
//                        public void onAdDismiss() {
//                            try {
//                                dialog1.dismiss();
//                                if (isFinish && !activity.isFinishing()) {
////                                    activity.finish();
//                                }
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    });
//                });
//                if (!activity.isFinishing()) {
//                    dialog1.show();
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }


    public void showBetterluckPopup() {
        Dialog dilaogBetterluck = new Dialog(Big_TypeTextGameActivity.this, android.R.style.Theme_Light);
        dilaogBetterluck.getWindow().setBackgroundDrawableResource(R.color.black_transparent);
        dilaogBetterluck.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dilaogBetterluck.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        dilaogBetterluck.setCancelable(false);
        dilaogBetterluck.setCanceledOnTouchOutside(false);
        dilaogBetterluck.setContentView(R.layout.big_popup_betterluck);

        TextView tvMessage = dilaogBetterluck.findViewById(R.id.tvMessage);
        tvMessage.setText("Sorry, text didn't match.\nBetter luck next time!");
        Button lDone = dilaogBetterluck.findViewById(R.id.btnOk);

        lDone.setOnClickListener(v -> {
            BIG_Ads_Utils.showAppLovinInterstitialAd(Big_TypeTextGameActivity.this, new BIG_Ads_Utils.AdShownListener() {
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
                tvText.setText(objTextTypingData.getData().getText());
                tvWinningPoints.setText(objTextTypingData.getData().getPoints());
                if (objTextTypingData.getData().getTimer() != null) {
                    textTypingTime = Integer.parseInt(objTextTypingData.getData().getTimer());// minutes
                }
                tvRemainingTime.setText(BIG_Common_Utils.updateTimeRemainingLuckyNumber(textTypingTime * 1000L));
                etText.setText("");
                lifeline = Integer.parseInt(objTextTypingData.getLifeline());
                tvAttemptsLeft.setText(String.valueOf(lifeline));
                setMainTimer(false);
            }
        });
        if (!isFinishing() && !dilaogBetterluck.isShowing()) {
            dilaogBetterluck.show();
        }
    }

    public void showWinPopup(String point) {
        try {
            Dialog dialogWin = new Dialog(Big_TypeTextGameActivity.this, android.R.style.Theme_Light);
            dialogWin.getWindow().setBackgroundDrawableResource(R.color.black_transparent);
            dialogWin.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialogWin.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            dialogWin.setCancelable(false);
            dialogWin.setCanceledOnTouchOutside(false);
            dialogWin.setContentView(R.layout.big_popup_win_points);
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
                    BIG_Ads_Utils.showAppLovinRewardedAd(Big_TypeTextGameActivity.this, new BIG_Ads_Utils.AdShownListener() {
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
                BIG_Ads_Utils.showAppLovinRewardedAd(Big_TypeTextGameActivity.this, new BIG_Ads_Utils.AdShownListener() {
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
                    BIG_Common_Utils.GetCoinAnimation(Big_TypeTextGameActivity.this, layoutMain, layoutPoints);
                    tvPoints.setText(BIG_SharePrefs.getInstance().getEarningPointString());
                    tvText.setText(objTextTypingData.getData().getText());
                    tvWinningPoints.setText(objTextTypingData.getData().getPoints());
                    if (objTextTypingData.getData().getTimer() != null) {
                        textTypingTime = Integer.parseInt(objTextTypingData.getData().getTimer());// minutes
                    }
                    tvRemainingTime.setText(BIG_Common_Utils.updateTimeRemainingLuckyNumber(textTypingTime * 1000L));
                    etText.setText("");
                    lifeline = Integer.parseInt(objTextTypingData.getLifeline());
                    tvAttemptsLeft.setText(String.valueOf(lifeline));
                    setMainTimer(false);
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

    private void loadAppLovinNativeAds(FrameLayout frameLayoutNativeAd, TextView lblLoadingAds) {
        try {
            nativeAdLoaderWin = new MaxNativeAdLoader(BIG_Common_Utils.getRandomAdUnitId(responseMain.getLovinNativeID()), Big_TypeTextGameActivity.this);
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
                    //AppLogger.getInstance().e("AppLovin Loaded WIN: ", "===WIN");
                }

                @Override
                public void onNativeAdLoadFailed(final String adUnitId, final MaxError error) {
                    //AppLogger.getInstance().e("AppLovin Failed WIN: ", error.getMessage());
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
    protected void onStop() {
        super.onStop();
        if (isFinishing()) {
            try {
                if (textTypingTimer != null) {
                    textTypingTimer.cancel();
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
                if (nativeAdTimer != null && nativeAdLoaderTimer != null) {
                    nativeAdLoaderTimer.destroy(nativeAdTimer);
                    nativeAdTimer = null;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onBackPressed() {

            super.onBackPressed();

    }

    public void setData(Big_TypeTextResponseModel responseModel) {
        objTextTypingData = responseModel;


        if (responseModel.getData() != null) {
            layoutContent.setVisibility(View.VISIBLE);
            try {
                if (objTextTypingData.getTodayDate() != null) {
                    todayDate = objTextTypingData.getTodayDate();
                }
                if (objTextTypingData.getLastDate() != null) {
                    lastDate = objTextTypingData.getLastDate();
                }

                if (objTextTypingData.getMainTimer() != null) {
                    mainTimerTime = objTextTypingData.getMainTimer();
                }

                if (objTextTypingData.getRemainCount() != null) {
                    tvRemainCount.setText(objTextTypingData.getRemainCount());
                    remainCount = Integer.parseInt(objTextTypingData.getRemainCount());
                }
                if (objTextTypingData.getData().getTimer() != null) {
                    textTypingTime = Integer.parseInt(objTextTypingData.getData().getTimer());// minutes
                }
                tvRemainingTime.setText(BIG_Common_Utils.updateTimeRemainingLuckyNumber(textTypingTime * 1000L));
                tvDailyLimit.setText(objTextTypingData.getTotalCount());
                tvText.setText(objTextTypingData.getData().getText());
                tvWinningPoints.setText(objTextTypingData.getData().getPoints());
                if (objTextTypingData.getLifeline() != null) {
                    lifeline = Integer.parseInt(objTextTypingData.getLifeline());
                    tvAttemptsLeft.setText(String.valueOf(lifeline));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            setMainTimer(true);
            checkForTaskCompletion();

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
                    BIG_Common_Utils.loadTopBannerAd(Big_TypeTextGameActivity.this, layoutTopAds, responseModel.getTopAds());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (!BIG_Common_Utils.isStringNullOrEmpty(objTextTypingData.getHelpVideoUrl())) {
                ivHelp.setVisibility(VISIBLE);
                ivHelp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        BIG_Common_Utils.openUrl(Big_TypeTextGameActivity.this, objTextTypingData.getHelpVideoUrl());
                    }
                });
            }
            loadNativeAdInMainScreen();
        }
    }

    private void loadNativeAdInMainScreen() {
        layoutAds = findViewById(R.id.layoutAds);
        frameLayoutNativeAd = findViewById(R.id.fl_adplaceholder);
        lblLoadingAds = findViewById(R.id.lblLoadingAds);
        if (layoutTimer.getVisibility() == GONE && layoutCompleteTask.getVisibility() == GONE) {
            if (BIG_Common_Utils.isShowAppLovinNativeAds()) {
                lblLoadingAds.setVisibility(VISIBLE);
                loadAppLovinNativeAds();
            } else {
                layoutAds.setVisibility(GONE);
            }
        } else {
            layoutAds.setVisibility(GONE);
        }
    }

    private void checkForTaskCompletion() {
        try {
            layoutCompleteTask = findViewById(R.id.layoutCompleteTask);
            if (remainCount > 0 && !isTimerSet && !BIG_Common_Utils.isStringNullOrEmpty(objTextTypingData.getIsTodayTaskCompleted()) && objTextTypingData.getIsTodayTaskCompleted().equals("0")) {
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
                tvTaskNote.setText(objTextTypingData.getTaskNote());
                Button btnCompleteTask = findViewById(R.id.btnCompleteTask);
                if (!BIG_Common_Utils.isStringNullOrEmpty(objTextTypingData.getTaskButton())) {
                    btnCompleteTask.setText(objTextTypingData.getTaskButton());
                }
                btnCompleteTask.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!BIG_Common_Utils.isStringNullOrEmpty(objTextTypingData.getScreenNo())) {
//                            if (!POC_Common_UtilsUtils.hasUsageAccessPermission(TypeTextGameActivity.this)) {
//                                POC_Common_UtilsUtils.showUsageAccessPermissionDialog(TypeTextGameActivity.this);
//                                return;
//                            } else {
                            BIG_Common_Utils.Redirect(Big_TypeTextGameActivity.this, objTextTypingData.getScreenNo(), "", "", "", "", "");
//                            }
                        } else if (!BIG_Common_Utils.isStringNullOrEmpty(objTextTypingData.getTaskId())) {
                            Intent intent = new Intent(Big_TypeTextGameActivity.this, Big_TaskDetailsInfoActivity.class);
                            intent.putExtra("taskId", objTextTypingData.getTaskId());
                            startActivity(intent);
                        } else {
                            Intent intent = new Intent(Big_TypeTextGameActivity.this, Big_TaskDetailsInfoActivity.class);
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

    private void loadAppLovinNativeAds() {
        try {
            nativeAdLoader = new MaxNativeAdLoader(BIG_Common_Utils.getRandomAdUnitId(responseMain.getLovinNativeID()), Big_TypeTextGameActivity.this);
            nativeAdLoader.setNativeAdListener(new MaxNativeAdListener() {
                @Override
                public void onNativeAdLoaded(final MaxNativeAdView nativeAdView, final MaxAd ad) {
                    frameLayoutNativeAd.setVisibility(View.VISIBLE);
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

    private void loadAppLovinNativeAdsTask(LinearLayout layoutAds, FrameLayout frameLayoutNativeAd, TextView lblLoadingAds) {
        try {
            nativeAdLoaderTask = new MaxNativeAdLoader(BIG_Common_Utils.getRandomAdUnitId(responseMain.getLovinNativeID()), Big_TypeTextGameActivity.this);
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

    private void loadAppLovinNativeAdsTimer(LinearLayout layoutAds, FrameLayout frameLayoutNativeAd, TextView lblLoadingAds) {
        try {
            nativeAdLoaderTimer = new MaxNativeAdLoader(BIG_Common_Utils.getRandomAdUnitId(responseMain.getLovinNativeID()), Big_TypeTextGameActivity.this);
            nativeAdLoaderTimer.setNativeAdListener(new MaxNativeAdListener() {
                @Override
                public void onNativeAdLoaded(final MaxNativeAdView nativeAdView, final MaxAd ad) {
                    if (nativeAdTimer != null) {
                        nativeAdLoaderTimer.destroy(nativeAdTimer);
                    }
                    nativeAdTimer = ad;
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
            nativeAdLoaderTimer.loadAd();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setMainTimer(boolean isFromOnCreate) {
        if (remainCount == 0) {
            isTimerSet = false;
            showTimerView(true);
            if (isFromOnCreate) {
                BIG_Ads_Utils.showAppLovinInterstitialAd(Big_TypeTextGameActivity.this, null);
            }
            return;
        }
        if (BIG_Common_Utils.timeDiff(todayDate, lastDate) > Integer.parseInt(mainTimerTime)) {
            isTimerSet = false;
        } else {
            isTimerSet = true;
            showTimerView(false);
            if (mainTimer != null) {
                mainTimer.cancel();
            }
            time = BIG_Common_Utils.timeDiff(todayDate, lastDate);
            mainTimer = new CountDownTimer((Integer.parseInt(mainTimerTime) - time) * 60000L, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    tvMainTimer.setText(BIG_Common_Utils.updateTimeRemaining(millisUntilFinished));
                }

                @Override
                public void onFinish() {
                    try {
                        isTimerSet = false;
                        layoutTimer.setVisibility(View.GONE);
                        tvMainTimer.setText("");
                        checkForTaskCompletion();
                        if (layoutAds.getVisibility() == GONE) {
                            loadNativeAdInMainScreen();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }.start();
            if (isFromOnCreate) {
                BIG_Ads_Utils.showAppLovinInterstitialAd(Big_TypeTextGameActivity.this, null);
            }
        }
    }

    private void showTimerView(boolean isLimitOver) {
        try {
            if (nativeAd != null && nativeAdLoader != null) {
                nativeAdLoader.destroy(nativeAd);
                nativeAd = null;
                frameLayoutNativeAd = null;
                layoutAds.setVisibility(GONE);
            }
            layoutTimer.setVisibility(VISIBLE);
            if (isLimitOver) {
                tvMainTimer.setVisibility(GONE);
                lblTimer.setText("You have exhausted your Type & win  daily limit, please try again tomorrow.");
            } else {
                tvMainTimer.setVisibility(VISIBLE);
                lblTimer.setText("Please wait, Type & win  will get unlock in ");
            }
            LinearLayout layoutAdsTimer = findViewById(R.id.layoutAdsTimer);
            TextView lblLoadingAdsTimer = findViewById(R.id.lblLoadingAdsTimer);
            FrameLayout nativeAdTimer = findViewById(R.id.fl_adplaceholder_timer);

            if (BIG_Common_Utils.isShowAppLovinNativeAds()) {
                loadAppLovinNativeAdsTimer(layoutAdsTimer, nativeAdTimer, lblLoadingAdsTimer);
            } else {
                layoutAdsTimer.setVisibility(GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onResume() {

        super.onResume();
        new Big_GetTypedTextAsync(Big_TypeTextGameActivity.this);

    }
}