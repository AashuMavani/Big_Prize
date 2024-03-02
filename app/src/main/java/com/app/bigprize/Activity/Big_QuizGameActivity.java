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
import android.os.Handler;
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
import androidx.cardview.widget.CardView;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieDrawable;
import com.app.bigprize.Async.Big_Get_Quiz_Async;
import com.app.bigprize.Async.Models.Big_Quiz_Data_Model;
import com.app.bigprize.Async.Models.Big_Response_Model;
import com.app.bigprize.Async.Big_Save_Quiz_Async;
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
import com.bumptech.glide.Glide;
import com.google.gson.Gson;

public class Big_QuizGameActivity extends AppCompatActivity {
    private LinearLayout layoutPoints, layoutContent, layoutNoData, layoutOptionA, layoutOptionB, layoutOptionC, layoutOptionD,layoutAds;
    private ImageView ivHistory, ivImage;
    private TextView tvPoints, lblLoadingAds, tvWinningPoints, tvNote, tvQuestion, tvOptionA, tvOptionB, tvOptionC, tvOptionD;
    private Big_Response_Model responseMain;
    private MaxAd nativeAd, nativeAdWin;
    private MaxNativeAdLoader nativeAdLoader, nativeAdLoaderWin;
    private FrameLayout frameLayoutNativeAd;
    private Big_Quiz_Data_Model objQuizDataModel;
    private AppCompatButton btnSubmit;
    private LottieAnimationView ivLottieNoData, ivLottie;
    private boolean isEdit = false;
    private String selectedAnswer = "";
    private RelativeLayout layoutMain;
    private CardView cardImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        BIG_Common_Utils.setDayNightTheme(Big_QuizGameActivity.this);
        setContentView(R.layout.big_activity_quiz_game);
        responseMain = new Gson().fromJson(BIG_SharePrefs.getInstance().getString(BIG_SharePrefs.HomeData), Big_Response_Model.class);

        setViews();
    }
    private void setViews() {
        cardImage = findViewById(R.id.cardImage);
        ivImage = findViewById(R.id.ivImage);
        ivLottie = findViewById(R.id.ivLottie);
        tvNote = findViewById(R.id.tvNote);
        tvQuestion = findViewById(R.id.tvQuestion);

        layoutOptionA = findViewById(R.id.layoutOptionA);
        layoutOptionA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedAnswer = "A";
                selectAnswer();
            }
        });

        layoutOptionB = findViewById(R.id.layoutOptionB);
        layoutOptionB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedAnswer = "B";
                selectAnswer();
            }
        });

        layoutOptionC = findViewById(R.id.layoutOptionC);
        layoutOptionC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedAnswer = "C";
                selectAnswer();
            }
        });

        layoutOptionD = findViewById(R.id.layoutOptionD);
        layoutOptionD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedAnswer = "D";
                selectAnswer();
            }
        });

        tvOptionA = findViewById(R.id.tvOptionA);
        tvOptionB = findViewById(R.id.tvOptionB);
        tvOptionC = findViewById(R.id.tvOptionC);
        tvOptionD = findViewById(R.id.tvOptionD);
        layoutMain = findViewById(R.id.layoutMain);
        btnSubmit = findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    //AppLogger.getInstance().e("SELECTED ANSWER", "===" + selectedAnswer);
                    if (BIG_SharePrefs.getInstance().getBoolean(BIG_SharePrefs.IS_LOGIN)) {
                        if (selectedAnswer.length() > 0) {
                            BIG_Ads_Utils.showAppLovinRewardedAd(Big_QuizGameActivity.this, new BIG_Ads_Utils.AdShownListener() {
                                @Override
                                public void onAdDismiss() {
                                    new Big_Save_Quiz_Async(Big_QuizGameActivity.this, objQuizDataModel.getId(), selectedAnswer, objQuizDataModel.getPoints());
                                }
                            });
                        } else {
                            BIG_Common_Utils.setToast(Big_QuizGameActivity.this, "Please select any answer");
                        }
                    } else {
                        BIG_Common_Utils.NotifyLogin(Big_QuizGameActivity.this);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        tvNote = findViewById(R.id.tvNote);

        ivLottieNoData = findViewById(R.id.ivLottieNoData);
        layoutNoData = findViewById(R.id.layoutNoData);
        layoutContent = findViewById(R.id.layoutContent);
        layoutContent.setVisibility(View.INVISIBLE);

        tvWinningPoints = findViewById(R.id.tvWinningPoints);

        layoutAds = findViewById(R.id.layoutAds);
        frameLayoutNativeAd = findViewById(R.id.fl_adplaceholder);
        lblLoadingAds = findViewById(R.id.lblLoadingAds);
        if (BIG_Common_Utils.isShowAppLovinNativeAds()) {
            loadAppLovinNativeAds();
        } else {
            layoutAds.setVisibility(GONE);
        }

        ImageView ivBack = findViewById(R.id.ivBack);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        tvPoints = findViewById(R.id.tvPoints);
        tvPoints.setText(BIG_SharePrefs.getInstance().getEarningPointString());
        layoutPoints = findViewById(R.id.layoutPoints);
        layoutPoints.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (BIG_SharePrefs.getInstance().getBoolean(BIG_SharePrefs.IS_LOGIN)) {
                    startActivity(new Intent(Big_QuizGameActivity.this, Big_My_Wallet_Activity.class));
                } else {
                    BIG_Common_Utils.NotifyLogin(Big_QuizGameActivity.this);
                }
            }
        });

        ivHistory = findViewById(R.id.ivHistory);
//         POC_Common_Utils.startRoundAnimation(Big_QuizGameActivity.this, ivHistory);
        ivHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (BIG_SharePrefs.getInstance().getBoolean(BIG_SharePrefs.IS_LOGIN)) {
                    startActivity(new Intent(Big_QuizGameActivity.this, Big_QuizGameHistoryActivity.class));
                } else {
                    BIG_Common_Utils.NotifyLogin(Big_QuizGameActivity.this);
                }
            }
        });
        new Big_Get_Quiz_Async(Big_QuizGameActivity.this);
    }

    private void selectAnswer() {
        if (selectedAnswer.equalsIgnoreCase("A")) {
            layoutOptionA.setBackgroundResource(R.drawable.big_bg_answer_selected);
            tvOptionA.setTextColor(getColor(R.color.white));

            layoutOptionB.setBackgroundResource(R.drawable.big_bg_answer);
            tvOptionB.setTextColor(getColor(R.color.black_font));

            layoutOptionC.setBackgroundResource(R.drawable.big_bg_answer);
            tvOptionC.setTextColor(getColor(R.color.black_font));

            layoutOptionD.setBackgroundResource(R.drawable.big_bg_answer);
            tvOptionD.setTextColor(getColor(R.color.black_font));
        } else if (selectedAnswer.equalsIgnoreCase("B")) {
            layoutOptionA.setBackgroundResource(R.drawable.big_bg_answer);
            tvOptionA.setTextColor(getColor(R.color.black_font));

            layoutOptionB.setBackgroundResource(R.drawable.big_bg_answer_selected);
            tvOptionB.setTextColor(getColor(R.color.white));

            layoutOptionC.setBackgroundResource(R.drawable.big_bg_answer);
            tvOptionC.setTextColor(getColor(R.color.black_font));

            layoutOptionD.setBackgroundResource(R.drawable.big_bg_answer);
            tvOptionD.setTextColor(getColor(R.color.black_font));
        } else if (selectedAnswer.equalsIgnoreCase("C")) {
            layoutOptionA.setBackgroundResource(R.drawable.big_bg_answer);
            tvOptionA.setTextColor(getColor(R.color.black_font));

            layoutOptionB.setBackgroundResource(R.drawable.big_bg_answer);
            tvOptionB.setTextColor(getColor(R.color.black_font));

            layoutOptionC.setBackgroundResource(R.drawable.big_bg_answer_selected);
            tvOptionC.setTextColor(getColor(R.color.white));

            layoutOptionD.setBackgroundResource(R.drawable.big_bg_answer);
            tvOptionD.setTextColor(getColor(R.color.black_font));
        } else if (selectedAnswer.equalsIgnoreCase("D")) {
            layoutOptionA.setBackgroundResource(R.drawable.big_bg_answer);
            tvOptionA.setTextColor(getColor(R.color.black_font));

            layoutOptionB.setBackgroundResource(R.drawable.big_bg_answer);
            tvOptionB.setTextColor(getColor(R.color.black_font));

            layoutOptionC.setBackgroundResource(R.drawable.big_bg_answer);
            tvOptionC.setTextColor(getColor(R.color.black_font));

            layoutOptionD.setBackgroundResource(R.drawable.big_bg_answer_selected);
            tvOptionD.setTextColor(getColor(R.color.white));
        } else {
            layoutOptionA.setBackgroundResource(R.drawable.big_bg_answer);
            tvOptionA.setTextColor(getColor(R.color.black_font));

            layoutOptionB.setBackgroundResource(R.drawable.big_bg_answer);
            tvOptionB.setTextColor(getColor(R.color.black_font));

            layoutOptionC.setBackgroundResource(R.drawable.big_bg_answer);
            tvOptionC.setTextColor(getColor(R.color.black_font));

            layoutOptionD.setBackgroundResource(R.drawable.big_bg_answer);
            tvOptionD.setTextColor(getColor(R.color.black_font));
        }
        btnSubmit.setEnabled(isButtonEnabled());
    }

    public void updateDataChanges(Big_Quiz_Data_Model responseModel) {
        try {
            if (!isEdit) {
                if (objQuizDataModel.getIsInstantQuiz().equals("1")) {
                    BIG_SharePrefs.getInstance().putString(BIG_SharePrefs.EarnedPoints, responseModel.getEarningPoint());
                    BIG_AppLogger.getInstance().e("submit",""+objQuizDataModel.getPoints());
                    showWinPopup(objQuizDataModel.getPoints());
                } else {
                    isEdit = true;
                    btnSubmit.setText("Update");
                    BIG_Common_Utils.NotifySuccess(Big_QuizGameActivity.this, "Submit Quiz", responseModel.getMessage(), false);
                }
                BIG_Common_Utils.logFirebaseEvent(Big_QuizGameActivity.this, "FeatureUsabilityItemId", "FeatureUsabilityEvent", "Quiz Time", "Submit");
            } else {
                BIG_Common_Utils.NotifySuccess(Big_QuizGameActivity.this, "Update Quiz Answer", responseModel.getMessage(), false);
            }
            btnSubmit.setEnabled(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showWinPopup(String point) {
        try {
            Dialog dialogWin = new Dialog(Big_QuizGameActivity.this, android.R.style.Theme_Light);
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
                    BIG_Ads_Utils.showAppLovinInterstitialAd(Big_QuizGameActivity.this, new BIG_Ads_Utils.AdShownListener() {
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
                BIG_Ads_Utils.showAppLovinRewardedAd(Big_QuizGameActivity.this, new BIG_Ads_Utils.AdShownListener() {
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
                    BIG_Common_Utils.GetCoinAnimation(Big_QuizGameActivity.this, layoutMain, layoutPoints);
                    BIG_AppLogger.getInstance().e("#point", BIG_SharePrefs.getInstance().getEarningPointString());
                    tvPoints.setText(BIG_SharePrefs.getInstance().getEarningPointString());
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
            nativeAdLoaderWin = new MaxNativeAdLoader(BIG_Common_Utils.getRandomAdUnitId(responseMain.getLovinNativeID()), Big_QuizGameActivity.this);
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
    public void onBackPressed() {

            super.onBackPressed();

    }



    public void setData(Big_Quiz_Data_Model responseModel) {
        objQuizDataModel = responseModel;

        try {
            if (!BIG_Common_Utils.isStringNullOrEmpty(responseModel.getEarningPoint())) {
                BIG_SharePrefs.getInstance().putString(BIG_SharePrefs.EarnedPoints, responseModel.getEarningPoint());
                tvPoints.setText(BIG_SharePrefs.getInstance().getEarningPointString());
            }
            if (objQuizDataModel.getStatus().equals("2")) {
                layoutContent.setVisibility(GONE);
                layoutNoData.setVisibility(VISIBLE);
                ivLottieNoData.playAnimation();
                BIG_Ads_Utils.showAppLovinRewardedAd(Big_QuizGameActivity.this, null);
            } else {
                layoutContent.setVisibility(VISIBLE);
                layoutNoData.setVisibility(GONE);

                try {
                    LinearLayout layoutCompleteTask = findViewById(R.id.layoutCompleteTask);
                    if (!BIG_Common_Utils.isStringNullOrEmpty(objQuizDataModel.getIsTodayTaskCompleted()) && objQuizDataModel.getIsTodayTaskCompleted().equals("0")) {
                        layoutCompleteTask.setVisibility(VISIBLE);
                        TextView tvTaskNote = findViewById(R.id.tvTaskNote);
                        tvTaskNote.setText(objQuizDataModel.getTaskNote());
                        Button btnCompleteTask = findViewById(R.id.btnCompleteTask);
                        if(!BIG_Common_Utils.isStringNullOrEmpty(objQuizDataModel.getTaskButton())){
                            btnCompleteTask.setText(objQuizDataModel.getTaskButton());
                        }

                        btnCompleteTask.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                if (!BIG_Common_Utils.isStringNullOrEmpty(objQuizDataModel.getScreenNo())) {
//                                    if (!BIG_Common_Utils.hasUsageAccessPermission(Big_QuizGameActivity.this)) {
//                                        BIG_Common_Utils.showUsageAccessPermissionDialog(Big_QuizGameActivity.this);
//                                        return;
//                                    } else {
//
//                                    }
                                    BIG_Common_Utils.Redirect(Big_QuizGameActivity.this, objQuizDataModel.getScreenNo(), "", "", "", "", "");
                                } else if (!BIG_Common_Utils.isStringNullOrEmpty(objQuizDataModel.getTaskId())) {
                                    Intent intent = new Intent(Big_QuizGameActivity.this, Big_TaskDetailsInfoActivity.class);
                                    intent.putExtra("taskId", objQuizDataModel.getTaskId());
                                    startActivity(intent);
                                } else {
                                    Intent intent = new Intent(Big_QuizGameActivity.this, Big_TasksCategoryTypeActivity.class);
                                    intent.putExtra("taskTypeId", BIG_Constants.TASK_TYPE_ALL);
                                    intent.putExtra("title", "Tasks");
                                    startActivity(intent);
                                }
                                finish();
                            }
                        });
                        /*btnCompleteTask.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                finish();
                                if (!Common_Utils.isStringNullOrEmpty(objQuizDataModel.getTaskId())) {
                                    Intent intent = new Intent(QuizGameActivity.this, TaskDetailsInfoActivity.class);
                                    intent.putExtra("taskId", objQuizDataModel.getTaskId());
                                    startActivity(intent);
                                } else {
                                    Intent intent = new Intent(QuizGameActivity.this, TasksCategoryTypeActivity.class);
                                    intent.putExtra("taskTypeId", Constants.TASK_TYPE_ALL);
                                    intent.putExtra("title", "Tasks");
                                    startActivity(intent);
                                }
                            }
                        });*/
                    } else {
                        layoutCompleteTask.setVisibility(GONE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                tvWinningPoints.setText(objQuizDataModel.getPoints());

                if (!BIG_Common_Utils.isStringNullOrEmpty(objQuizDataModel.getImage())) {
                    cardImage.setVisibility(VISIBLE);
                    if (objQuizDataModel.getImage().contains(".json")) {
                        ivImage.setVisibility(View.GONE);
                        ivLottie.setVisibility(View.VISIBLE);
                        BIG_Common_Utils.setLottieAnimation(ivLottie, objQuizDataModel.getImage());
                        ivLottie.setRepeatCount(LottieDrawable.INFINITE);
                    } else {
                        ivLottie.setVisibility(View.GONE);
                        ivImage.setVisibility(View.VISIBLE);
                        Glide.with(Big_QuizGameActivity.this)
                                .load(objQuizDataModel.getImage())
                                .into(ivImage);
                    }
                } else {
                    cardImage.setVisibility(GONE);
                }

                if (!BIG_Common_Utils.isStringNullOrEmpty(objQuizDataModel.getNote())) {
                    tvNote.setText("NOTE: " + objQuizDataModel.getNote());
                }

                tvQuestion.setText(objQuizDataModel.getQuestion());

                if (!BIG_Common_Utils.isStringNullOrEmpty(objQuizDataModel.getOptionA())) {
                    layoutOptionA.setVisibility(VISIBLE);
                    tvOptionA.setText(objQuizDataModel.getOptionA());
                } else {
                    layoutOptionA.setVisibility(GONE);
                }
                if (!BIG_Common_Utils.isStringNullOrEmpty(objQuizDataModel.getOptionB())) {
                    layoutOptionB.setVisibility(VISIBLE);
                    tvOptionB.setText(objQuizDataModel.getOptionB());
                } else {
                    layoutOptionB.setVisibility(GONE);
                }
                if (!BIG_Common_Utils.isStringNullOrEmpty(objQuizDataModel.getOptionC())) {
                    layoutOptionC.setVisibility(VISIBLE);
                    tvOptionC.setText(objQuizDataModel.getOptionC());
                } else {
                    layoutOptionC.setVisibility(GONE);
                }
                if (!BIG_Common_Utils.isStringNullOrEmpty(objQuizDataModel.getOptionD())) {
                    layoutOptionD.setVisibility(VISIBLE);
                    tvOptionD.setText(objQuizDataModel.getOptionD());
                } else {
                    layoutOptionD.setVisibility(GONE);
                }
                if (!BIG_Common_Utils.isStringNullOrEmpty(objQuizDataModel.getUserAnswer())) {
                    selectedAnswer = objQuizDataModel.getUserAnswer();
                    isEdit = true;
                    btnSubmit.setText("Update");
                } else {
                    selectedAnswer = "";
                }

                selectAnswer();

                // Load home note webview top
                try {
                    if (!BIG_Common_Utils.isStringNullOrEmpty(objQuizDataModel.getHomeNote())) {
                        WebView webNote = findViewById(R.id.webNote);
                        webNote.getSettings().setJavaScriptEnabled(true);
                        webNote.setVisibility(View.VISIBLE);
                        webNote.loadDataWithBaseURL(null, objQuizDataModel.getHomeNote(), "text/html", "UTF-8", null);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                // Load top ad
                try {
                    if (objQuizDataModel.getTopAds() != null && !BIG_Common_Utils.isStringNullOrEmpty(objQuizDataModel.getTopAds().getImage())) {
                        LinearLayout layoutTopAds = findViewById(R.id.layoutTopAds);
                        BIG_Common_Utils.loadTopBannerAd(Big_QuizGameActivity.this, layoutTopAds, objQuizDataModel.getTopAds());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (!BIG_Common_Utils.isStringNullOrEmpty(selectedAnswer)) {
                    BIG_Ads_Utils.showAppLovinInterstitialAd(Big_QuizGameActivity.this, null);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isButtonEnabled() {
        if (isEdit) {
            return (!selectedAnswer.equals(objQuizDataModel.getUserAnswer()));
        } else {
            return (!BIG_Common_Utils.isStringNullOrEmpty(selectedAnswer));
        }
    }


    private void loadAppLovinNativeAds() {
        try {
            nativeAdLoader = new MaxNativeAdLoader(BIG_Common_Utils.getRandomAdUnitId(responseMain.getLovinNativeID()), Big_QuizGameActivity.this);
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
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}