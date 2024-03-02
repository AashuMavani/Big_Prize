package com.app.bigprize.Activity;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieDrawable;
import com.app.bigprize.Async.Big_Get_Spin_Async;
import com.app.bigprize.Async.Models.Big_Exit_Dialog;
import com.app.bigprize.Async.Models.Big_Response_Model;
import com.app.bigprize.Async.Models.Big_Spin_Data_Model;
import com.app.bigprize.Async.Big_Save_Spin_Async;
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
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import rubikstudio.library.LuckyWheelView;
import rubikstudio.library.model.LuckyItem;

public class Big_LuckySpinWheelActivity extends AppCompatActivity {
    private TextView tvDailySpin, tvRemainSpin, tvRemainingTime, lblDailySpin, lblRemainSpin, tvPoints, txtTimeRemain;
    private LottieAnimationView btnSpinNow, parentLottie, ivSpin;
    private Big_Response_Model responseMain;
    private LinearLayout layoutDailySpin, layoutRemainSpin, layoutPoints;
    private LuckyWheelView luckyWheelView;
    private CountDownTimer timer;
    private int remainSpin, time;
    private String todayDate, lastDate, spinTime;
    private Big_Spin_Data_Model objSpinModel;
    private ImageView ivHistory;
    private LinearLayout layoutContent;
    private MaxAd nativeAdWin,nativeAdTask;
    private MaxNativeAdLoader nativeAdLoaderWin,nativeAdLoaderTask;
    private RelativeLayout layoutMain;
    private boolean isTimerSet = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        BIG_Common_Utils.setDayNightTheme(Big_LuckySpinWheelActivity.this);
        setContentView(R.layout.big_activity_lucky_spin_wheel);
        responseMain = new Gson().fromJson(BIG_SharePrefs.getInstance().getString(BIG_SharePrefs.HomeData), Big_Response_Model.class);
        layoutMain = findViewById(R.id.layoutMain);
        layoutContent = findViewById(R.id.layoutContent);
        ivHistory = findViewById(R.id.ivHistory);
        layoutPoints = findViewById(R.id.layoutPoints);
        layoutPoints.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (BIG_SharePrefs.getInstance().getBoolean(BIG_SharePrefs.IS_LOGIN)) {
                    startActivity(new Intent(Big_LuckySpinWheelActivity.this, Big_My_Wallet_Activity.class));
                } else {
                    BIG_Common_Utils.NotifyLogin(Big_LuckySpinWheelActivity.this);
                }
            }
        });
        luckyWheelView = findViewById(R.id.luckyWheel);
        tvPoints = findViewById(R.id.tvPoints);
        tvPoints.setText(BIG_SharePrefs.getInstance().getEarningPointString());
        tvDailySpin = findViewById(R.id.tvDailySpin);
        tvRemainSpin = findViewById(R.id.tvRemainSpin);
        tvRemainingTime = findViewById(R.id.tvRemainingTime);

        lblDailySpin = findViewById(R.id.lblDailySpin);
        lblRemainSpin = findViewById(R.id.lblRemainSpin);

        btnSpinNow = findViewById(R.id.btnSpinNow);
        parentLottie = findViewById(R.id.parentLottie);
        btnSpinNow = findViewById(R.id.btnSpinNow);
        ivSpin = findViewById(R.id.ivSpin);

        layoutDailySpin = findViewById(R.id.layoutDailySpin);
        layoutRemainSpin = findViewById(R.id.layoutRemainSpin);

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
                    startActivity(new Intent(Big_LuckySpinWheelActivity.this, Big_PointHistoryActivity.class)
                            .putExtra("type", BIG_Constants.HistoryType.SPIN)
                            .putExtra("title", "Spin Wheel History"));
                } else {
                    BIG_Common_Utils.NotifyLogin(Big_LuckySpinWheelActivity.this);
                }
            }
        });
        new Big_Get_Spin_Async(Big_LuckySpinWheelActivity.this);
    }

    public void changeSpinDataValues(Big_Spin_Data_Model responseModel) {
        BIG_SharePrefs.getInstance().putString(BIG_SharePrefs.EarnedPoints, responseModel.getEarningPoint());
        tvDailySpin.setText(responseModel.getDailySpinnerLimit());

        if (responseModel.getTodayDate() != null) {
            todayDate = responseModel.getTodayDate();
        }
        if (responseModel.getLastDate() != null) {
            lastDate = responseModel.getLastDate();
        }

        if (responseModel.getSpinTime() != null) {
            spinTime = responseModel.getSpinTime();
        }
        if (responseModel.getRemainSpin() != null) {
            tvRemainSpin.setText(responseModel.getRemainSpin());
            Log.d("AAAAAAAAAAA", "changeSpinDataValues: "+tvRemainSpin);
            remainSpin = Integer.parseInt(responseModel.getRemainSpin());
        }

        setTimer(false);

        if (responseModel.getPoint().equals("0")) {
            BIG_Common_Utils.logFirebaseEvent(Big_LuckySpinWheelActivity.this, "FeatureUsabilityItemId", "FeatureUsabilityEvent", "Spin_BigPrize", "Better Luck");
            showBetterluckPopup();
        } else {
            BIG_Common_Utils.logFirebaseEvent(Big_LuckySpinWheelActivity.this, "FeatureUsabilityItemId", "FeatureUsabilityEvent", "Spin_BigPrize", "Spin Got Reward");
            showWinPopup(responseModel.getPoint(), responseModel.getIsShowAds());
        }
    }
    public void showWinPopup(String point, String isShowAds) {
        try {
            Dialog dialogWin = new Dialog(Big_LuckySpinWheelActivity.this, android.R.style.Theme_Light);
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
                    if (!BIG_Common_Utils.isStringNullOrEmpty(isShowAds) && isShowAds.equals("1")) {
                        BIG_Ads_Utils.showAppLovinInterstitialAd(Big_LuckySpinWheelActivity.this, new BIG_Ads_Utils.AdShownListener() {
                            @Override
                            public void onAdDismiss() {
                                if (dialogWin != null) {
                                    dialogWin.dismiss();
                                }
                            }
                        });
                    } else {
                        if (dialogWin != null) {
                            dialogWin.dismiss();
                        }
                    }
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
                if (!BIG_Common_Utils.isStringNullOrEmpty(isShowAds) && isShowAds.equals("1")) {
                    BIG_Ads_Utils.showAppLovinInterstitialAd(Big_LuckySpinWheelActivity.this, new BIG_Ads_Utils.AdShownListener() {
                        @Override
                        public void onAdDismiss() {
                            if (dialogWin != null) {
                                dialogWin.dismiss();
                            }
                        }
                    });
                } else {
                    if (dialogWin != null) {
                        dialogWin.dismiss();
                    }
                }
            });
            dialogWin.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {
                    BIG_Common_Utils.GetCoinAnimation(Big_LuckySpinWheelActivity.this, layoutMain, layoutPoints);
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
            nativeAdLoaderWin = new MaxNativeAdLoader(BIG_Common_Utils.getRandomAdUnitId(responseMain.getLovinNativeID()), Big_LuckySpinWheelActivity.this);
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
                if (timer != null) {
                    timer.cancel();
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

    @Override
    public void onBackPressed() {

            super.onBackPressed();

    }


    public void setData(Big_Spin_Data_Model responseModel) {
        objSpinModel = responseModel;

        if (responseModel.getData() != null && responseModel.getData().size() > 0) {
            layoutContent.setVisibility(View.VISIBLE);
            BIG_Common_Utils.showProgressLoader(Big_LuckySpinWheelActivity.this);
            try {
                if (responseModel.getTodayDate() != null) {
                    todayDate = responseModel.getTodayDate();
                }
                if (responseModel.getLastDate() != null) {
                    lastDate = responseModel.getLastDate();
                }

                if (responseModel.getSpinTime() != null) {
                    spinTime = responseModel.getSpinTime();
                }
                if (responseModel.getRemainSpin() != null) {
                    tvRemainSpin.setText(responseModel.getRemainSpin());
                    Log.d("spin=====", "setData: "+tvRemainSpin);
                    remainSpin = Integer.parseInt(responseModel.getRemainSpin());
                }

                tvDailySpin.setText(responseModel.getDailySpinnerLimit());

                lblRemainSpin.setTextColor(Color.parseColor(responseModel.getButtonTextColor()));
                lblDailySpin.setTextColor(Color.parseColor(responseModel.getButtonTextColor()));

                tvDailySpin.setTextColor(Color.parseColor(responseModel.getButtonTextColor()));
                tvRemainSpin.setTextColor(Color.parseColor(responseModel.getButtonTextColor()));

                tvRemainingTime.setTextColor(Color.parseColor(responseModel.getTimerTextColor()));

                BIG_Common_Utils.setLottieAnimation(ivSpin, responseModel.getSpinImage());

                BIG_Common_Utils.setLottieAnimation(btnSpinNow, responseModel.getButtonImage());

                BIG_Common_Utils.setLottieAnimation(parentLottie, responseModel.getBackgroundImage());

                Glide.with(this)
                        .asBitmap()
                        .load(responseModel.getLabelBackgroundImage())
                        .into(new CustomTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                try {
                                    layoutDailySpin.setBackground(new BitmapDrawable(getResources(), resource));
                                    layoutRemainSpin.setBackground(new BitmapDrawable(getResources(), resource));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onLoadFailed(@Nullable Drawable errorDrawable) {
                                super.onLoadFailed(errorDrawable);
                            }

                            @Override
                            public void onLoadCleared(@Nullable Drawable placeholder) {
                            }
                        });

                List<LuckyItem> data = new ArrayList<>();
                ArrayList<Bitmap> bitmap = new ArrayList<>(responseModel.getData().size());
                for (int ii = 0; ii < responseModel.getData().size(); ii++) {
                    if (responseModel.getData().get(ii).getBlockIcon() != null && !BIG_Common_Utils.isStringNullOrEmpty(responseModel.getData().get(ii).getBlockIcon())) {
                        try {
                            Glide.with(Big_LuckySpinWheelActivity.this)
                                    .asBitmap()
                                    .load(responseModel.getData().get(ii).getBlockIcon())
                                    .into(new CustomTarget<Bitmap>() {
                                        @Override
                                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                            try {
                                                bitmap.add(resource);
                                                if (bitmap.size() == responseModel.getData().size()) {
                                                    for (int i = 0; i < responseModel.getData().size(); i++) {
                                                        LuckyItem luckyItem1 = new LuckyItem();
                                                        luckyItem1.text = responseModel.getData().get(i).getBlockPoints();
                                                        luckyItem1.color = Color.parseColor(responseModel.getData().get(i).getBlockBg());
                                                        luckyItem1.textColor = Color.parseColor(responseModel.getData().get(i).getBlockTextColor());
                                                        luckyItem1.id = responseModel.getData().get(i).getBlockId();
                                                        luckyItem1.bitmap = bitmap.get(i);
                                                        data.add(luckyItem1);
                                                    }
                                                    luckyWheelView.setData(data);
                                                    BIG_Common_Utils.dismissProgressLoader();
                                                }
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }

                                        @Override
                                        public void onLoadCleared(@Nullable Drawable placeholder) {
                                        }
                                    });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        try {
                            bitmap.add(null);
                            if (bitmap.size() == responseModel.getData().size()) {
                                for (int i = 0; i < responseModel.getData().size(); i++) {
                                    LuckyItem luckyItem1 = new LuckyItem();
                                    luckyItem1.text = responseModel.getData().get(i).getBlockPoints();
                                    luckyItem1.color = Color.parseColor(responseModel.getData().get(i).getBlockBg());
                                    luckyItem1.textColor = Color.parseColor(responseModel.getData().get(i).getBlockTextColor());
                                    luckyItem1.id = responseModel.getData().get(i).getBlockId();
                                    luckyItem1.bitmap = bitmap.get(i);
                                    data.add(luckyItem1);
                                }
                                luckyWheelView.setData(data);
                                BIG_Common_Utils.dismissProgressLoader();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                setTimer(true);
                luckyWheelView.setRound(6);
                luckyWheelView.setLuckyRoundItemSelectedListener(new LuckyWheelView.LuckyRoundItemSelectedListener() {
                    @Override
                    public void LuckyRoundItemSelected(final int index) {
                        BIG_Ads_Utils.showAppLovinInterstitialAd(Big_LuckySpinWheelActivity.this, new BIG_Ads_Utils.AdShownListener() {
                            @Override
                            public void onAdDismiss() {
                                // api call
                                int pointAdd = Integer.parseInt(responseModel.getData().get(index != 0 ? index - 1 : 0).getBlockPoints());
                                new Big_Save_Spin_Async(Big_LuckySpinWheelActivity.this, String.valueOf(pointAdd), responseModel.getData().get(index != 0 ? index - 1 : 0).getBlockId());
                            }
                        });
                    }
                });


                btnSpinNow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            BIG_Common_Utils.setEnableDisable(Big_LuckySpinWheelActivity.this, v);
                            if (BIG_SharePrefs.getInstance().getBoolean(BIG_SharePrefs.IS_LOGIN)) {
                                if (remainSpin > 0) {
                                    if (tvRemainingTime.getText().toString().trim().length() == 0) {
                                        int random;
                                        if (BIG_SharePrefs.getInstance().getInt(BIG_SharePrefs.LastSpinIndex, -1) < 0) {
                                            random = BIG_Common_Utils.getRandomNumberBetweenRange(1, responseModel.getData().size() + 1);
                                            BIG_SharePrefs.getInstance().putInt(BIG_SharePrefs.LastSpinIndex, random);
                                            //AppLogger.getInstance().e("SPIN INDEX", "NEW INDEX : " + random);
                                        } else {
                                            random = BIG_SharePrefs.getInstance().getInt(BIG_SharePrefs.LastSpinIndex);
                                            //AppLogger.getInstance().e("SPIN INDEX", "PREF INDEX: " + random);
                                        }
                                        ivSpin.pauseAnimation();
                                        luckyWheelView.startLuckyWheelWithTargetIndex(random);
                                    } else {
                                        showRemainTimeDialog();
                                    }
                                } else {
                                    BIG_Common_Utils.Notify(Big_LuckySpinWheelActivity.this, "Spin Limit", "Your spin limit for today is over, please wait until it gets credited again.", false);
                                }
                            } else {
                                BIG_Common_Utils.NotifyLogin(Big_LuckySpinWheelActivity.this);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                try {
                    LinearLayout layoutCompleteTask = findViewById(R.id.layoutCompleteTask);
                    if (!isTimerSet && !BIG_Common_Utils.isStringNullOrEmpty(responseModel.getIsTodayTaskCompleted()) && responseModel.getIsTodayTaskCompleted().equals("0")) {
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
                        if(!BIG_Common_Utils.isStringNullOrEmpty(responseModel.getTaskButton())){
                            btnCompleteTask.setText(responseModel.getTaskButton());
                        }
                        btnCompleteTask.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                if (!BIG_Common_Utils.isStringNullOrEmpty(objSpinModel.getScreenNo())) {
//                                    if (!BIG_Common_Utils.hasUsageAccessPermission(Big_LuckySpinWheelActivity.this)) {
//                                        BIG_Common_Utils.showUsageAccessPermissionDialog(Big_LuckySpinWheelActivity.this);
//                                        return;
//                                    } else {
//
//                                    }
                                    BIG_Common_Utils.Redirect(Big_LuckySpinWheelActivity.this, objSpinModel.getScreenNo(), "", "", "", "", "");
                                } else if (!BIG_Common_Utils.isStringNullOrEmpty(objSpinModel.getTaskId())) {
                                    Intent intent = new Intent(Big_LuckySpinWheelActivity.this, Big_TaskDetailsInfoActivity.class);
                                    intent.putExtra("taskId", objSpinModel.getTaskId());
                                    startActivity(intent);
                                } else {
                                    Intent intent = new Intent(Big_LuckySpinWheelActivity.this, Big_TasksCategoryTypeActivity.class);
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
                    BIG_Common_Utils.loadTopBannerAd(Big_LuckySpinWheelActivity.this, layoutTopAds, responseModel.getTopAds());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Load Bottom banner ad
            try {
                LinearLayout layoutBannerAdBottom = findViewById(R.id.layoutBannerAdBottom);
                TextView lblAdSpaceBottom = findViewById(R.id.lblAdSpaceBottom);
                BIG_Common_Utils.loadBannerAds(Big_LuckySpinWheelActivity.this, layoutBannerAdBottom, lblAdSpaceBottom);
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Load floating ad
            try {
                if (responseModel.getFloatingAds() != null && !BIG_Common_Utils.isStringNullOrEmpty(responseModel.getFloatingAds().getImage())) {
                    if (responseModel.getFloatingAds().getImage().endsWith(".json")) {
                        LottieAnimationView floatAdLottie = findViewById(R.id.floatAdLottie);
                        floatAdLottie.setVisibility(android.view.View.VISIBLE);
                        BIG_Common_Utils.setLottieAnimation(floatAdLottie, responseModel.getFloatingAds().getImage());
                        floatAdLottie.setRepeatCount(LottieDrawable.INFINITE);
                        floatAdLottie.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                BIG_Common_Utils.Redirect(Big_LuckySpinWheelActivity.this, responseModel.getFloatingAds().getScreenNo(), responseModel.getFloatingAds().getTitle(), responseModel.getFloatingAds().getUrl(), responseModel.getFloatingAds().getId(), responseModel.getFloatingAds().getTaskId(), responseModel.getFloatingAds().getImage());
                            }
                        });
                    } else {
                        ImageView floatAd = findViewById(R.id.floatAd);
                        floatAd.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                BIG_Common_Utils.Redirect(Big_LuckySpinWheelActivity.this, responseModel.getFloatingAds().getScreenNo(), responseModel.getFloatingAds().getTitle(), responseModel.getFloatingAds().getUrl(), responseModel.getFloatingAds().getId(), responseModel.getFloatingAds().getTaskId(), responseModel.getFloatingAds().getImage());
                            }
                        });
                        Glide.with(this)
                                .load(responseModel.getFloatingAds().getImage())
                                .override(getResources().getDimensionPixelSize(R.dimen.dim_65), getResources().getDimensionPixelSize(R.dimen.dim_65))
                                .addListener(new RequestListener<Drawable>() {
                                    @Override
                                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                        return false;
                                    }

                                    @Override
                                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                        floatAd.setVisibility(View.VISIBLE);
                                        return false;
                                    }
                                }).into(floatAd);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    private void loadAppLovinNativeAdsTask(LinearLayout layoutAds, FrameLayout frameLayoutNativeAd, TextView lblLoadingAds) {
        try {
            nativeAdLoaderTask = new MaxNativeAdLoader(BIG_Common_Utils.getRandomAdUnitId(responseMain.getLovinNativeID()), Big_LuckySpinWheelActivity.this);
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
    public void showBetterluckPopup() {
        Dialog dilaogBetterluck = new Dialog(Big_LuckySpinWheelActivity.this, android.R.style.Theme_Light);
        dilaogBetterluck.getWindow().setBackgroundDrawableResource(R.color.black_transparent);
        dilaogBetterluck.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dilaogBetterluck.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        dilaogBetterluck.setCancelable(false);
        dilaogBetterluck.setCanceledOnTouchOutside(false);
        dilaogBetterluck.setContentView(R.layout.big_dialog_better_luck);

        Button lDone = dilaogBetterluck.findViewById(R.id.btnOk);

        lDone.setOnClickListener(v -> {
            BIG_Ads_Utils.showAppLovinInterstitialAd(Big_LuckySpinWheelActivity.this, new BIG_Ads_Utils.AdShownListener() {
                @Override
                public void onAdDismiss() {
                    if (dilaogBetterluck != null) {
                        dilaogBetterluck.dismiss();
                    }
                }
            });
        });

        if (!isFinishing() && !dilaogBetterluck.isShowing()) {
            dilaogBetterluck.show();
        }
    }

    public void showRemainTimeDialog() {
        Dialog dilaogBetterluck = new Dialog(Big_LuckySpinWheelActivity.this, android.R.style.Theme_Light);
        dilaogBetterluck.getWindow().setBackgroundDrawableResource(R.color.black_transparent);
        dilaogBetterluck.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dilaogBetterluck.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        dilaogBetterluck.setCancelable(true);
        dilaogBetterluck.setCanceledOnTouchOutside(true);
        dilaogBetterluck.setContentView(R.layout.big_dialog_spin_time_out);

        txtTimeRemain = dilaogBetterluck.findViewById(R.id.txtTimeRemain);
        TextView txtOkRemian = dilaogBetterluck.findViewById(R.id.btnOk);
        TextView txtTitle = dilaogBetterluck.findViewById(R.id.txtTitle);
        TextView txtDesc = dilaogBetterluck.findViewById(R.id.txtDesc);
        Button btnCancel = dilaogBetterluck.findViewById(R.id.btnCancel);
        Button btnOk1 = dilaogBetterluck.findViewById(R.id.btnOk1);
        CardView cardExitPopup = dilaogBetterluck.findViewById(R.id.cardExitPopup);
        RelativeLayout relPopup = dilaogBetterluck.findViewById(R.id.relPopup);
        ProgressBar probrBanner = dilaogBetterluck.findViewById(R.id.probrBanner);
        LottieAnimationView ivLottieView = dilaogBetterluck.findViewById(R.id.ivLottieView);
        ImageView imgBanner = dilaogBetterluck.findViewById(R.id.imgBanner);
        Big_Exit_Dialog popData;
        if (objSpinModel.getExitDialog() != null) {
            popData = objSpinModel.getExitDialog();
        } else {
            popData = responseMain.getExitDialog();
        }
        if (popData != null) {
            cardExitPopup.setVisibility(VISIBLE);
            btnOk1.setVisibility(View.GONE);
            if (!BIG_Common_Utils.isStringNullOrEmpty(popData.getBtnName())) {
                txtOkRemian.setText(popData.getBtnName());
            }

            if (!BIG_Common_Utils.isStringNullOrEmpty(popData.getTitle())) {
                txtTitle.setText(popData.getTitle());
            }

            if (!BIG_Common_Utils.isStringNullOrEmpty(popData.getDescription())) {
                txtDesc.setText(popData.getDescription());
            }

            if (!BIG_Common_Utils.isStringNullOrEmpty(popData.getBtnColor())) {
                Drawable mDrawable = ContextCompat.getDrawable(Big_LuckySpinWheelActivity.this, R.drawable.big_ic_btn_gradient_rounded_corner_rect_new);
                mDrawable.setColorFilter(new PorterDuffColorFilter(Color.parseColor(popData.getBtnColor()), PorterDuff.Mode.SRC_IN));
                txtOkRemian.setBackground(mDrawable);
            }
            if (!BIG_Common_Utils.isStringNullOrEmpty(popData.getImage())) {
                if (popData.getImage().contains(".json")) {
                    probrBanner.setVisibility(View.GONE);
                    imgBanner.setVisibility(View.GONE);
                    ivLottieView.setVisibility(View.VISIBLE);
                    BIG_Common_Utils.setLottieAnimation(ivLottieView, popData.getImage());
                    ivLottieView.setRepeatCount(LottieDrawable.INFINITE);
                } else {
                    probrBanner.setVisibility(View.VISIBLE);
                    imgBanner.setVisibility(View.VISIBLE);
                    ivLottieView.setVisibility(View.GONE);
                    Glide.with(getApplicationContext())
                            .load(popData.getImage())
                            .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
                            .listener(new RequestListener<Drawable>() {
                                @Override
                                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, com.bumptech.glide.load.DataSource dataSource, boolean isFirstResource) {
                                    probrBanner.setVisibility(View.GONE);
                                    return false;
                                }
                            })
                            .into(imgBanner);
                }

            } else {
                imgBanner.setVisibility(View.GONE);
                probrBanner.setVisibility(View.GONE);
            }
            relPopup.setOnClickListener(v -> BIG_Common_Utils.Redirect(Big_LuckySpinWheelActivity.this, popData.getScreenNo(), popData.getTitle(), popData.getUrl(), null, null, popData.getImage()));
        } else {
            cardExitPopup.setVisibility(View.GONE);
            btnOk1.setVisibility(VISIBLE);
            btnOk1.setOnClickListener(v -> {
                dilaogBetterluck.dismiss();
            });
        }
        btnCancel.setOnClickListener(v -> {
            dilaogBetterluck.dismiss();
        });
        txtOkRemian.setOnClickListener(v -> {
            dilaogBetterluck.dismiss();
        });

        if (!isFinishing() && !dilaogBetterluck.isShowing()) {
            dilaogBetterluck.show();
            dilaogBetterluck.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {
                    txtTimeRemain = null;
                }
            });
        }
    }


    public void setTimer(boolean isFromOnCreate) {
        if (remainSpin == 0) {
            isTimerSet = false;
            tvRemainingTime.setVisibility(View.GONE);
            btnSpinNow.setAlpha(0.7f);
            ivSpin.pauseAnimation();
            if (isFromOnCreate) {
                BIG_Ads_Utils.showAppLovinInterstitialAd(Big_LuckySpinWheelActivity.this, null);
            }
            return;
        }
        if (BIG_Common_Utils.timeDiff(todayDate, lastDate) > Integer.parseInt(spinTime)) {
            isTimerSet = false;
            tvRemainingTime.setVisibility(View.GONE);
            btnSpinNow.setAlpha(1.0f);
            tvRemainingTime.setText("");
            ivSpin.playAnimation();
        } else {
            isTimerSet = true;
            ivSpin.pauseAnimation();
            tvRemainingTime.setVisibility(View.VISIBLE);
            btnSpinNow.setAlpha(0.7f);
            if (timer != null) {
                timer.cancel();
            }
            time = BIG_Common_Utils.timeDiff(todayDate, lastDate);
            timer = new CountDownTimer((Integer.parseInt(spinTime) - time) * 60000L, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    tvRemainingTime.setText(BIG_Common_Utils.updateTimeRemaining(millisUntilFinished));
                    if (txtTimeRemain != null) {
                        txtTimeRemain.setText(BIG_Common_Utils.calculateRemainingTime(millisUntilFinished));
                    }
                }

                @Override
                public void onFinish() {
                    isTimerSet = false;
                    tvRemainingTime.setVisibility(View.GONE);
                    tvRemainingTime.setText("");
                    btnSpinNow.setAlpha(1.0f);
                    ivSpin.playAnimation();
                }
            }.start();
            if (isFromOnCreate) {
                BIG_Ads_Utils.showAppLovinInterstitialAd(Big_LuckySpinWheelActivity.this, null);
            }
        }
    }
}