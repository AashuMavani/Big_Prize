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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.app.bigprize.Adapter.Big_Watch_Video_List_Adapter;
import com.app.bigprize.Async.Big_Get_Watch_Video_List_Async;
import com.app.bigprize.Async.Models.Big_Response_Model;
import com.app.bigprize.Async.Models.Big_Watch_Video_List;
import com.app.bigprize.Async.Models.Big_Watch_Video_Model;
import com.app.bigprize.Async.Big_Save_Watch_Video_List_Async;
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
import java.util.List;

public class Big_WatchVideoActivity extends AppCompatActivity {
    private LottieAnimationView ivLottieNoData;
    private RecyclerView rvVideoList;
    private TextView lblLoadingAds, tvPoints;
    private Big_Response_Model responseMain;
    private MaxAd nativeAd, nativeAdWin, nativeAdTask;
    private MaxNativeAdLoader nativeAdLoader, nativeAdLoaderWin, nativeAdLoaderTask;
    private FrameLayout frameLayoutNativeAd;
    private LinearLayout layoutAds;
    private Big_Watch_Video_List_Adapter mAdapter;
    private ImageView ivHistory;
    private LinearLayout layoutPoints;
    private String todayDate, lastDate, watchTime;
    private CountDownTimer timer;
    private int time, activeVideoPos = -1;
    public List<Big_Watch_Video_List> listVideos = new ArrayList<>();
    private boolean isTimerSet = false, isSetTimerValue = false;
    private RelativeLayout layoutMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        BIG_Common_Utils.setDayNightTheme(Big_WatchVideoActivity.this);
        setContentView(R.layout.big_activity_watch_video2);
        responseMain = new Gson().fromJson(BIG_SharePrefs.getInstance().getString(BIG_SharePrefs.HomeData), Big_Response_Model.class);
        layoutMain = findViewById(R.id.layoutMain);

        ivLottieNoData = findViewById(R.id.ivLottieNoData);
        rvVideoList = findViewById(R.id.rvVideoList);

        ivHistory = findViewById(R.id.ivHistory);
        ivHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (BIG_SharePrefs.getInstance().getBoolean(BIG_SharePrefs.IS_LOGIN)) {
                    startActivity(new Intent(Big_WatchVideoActivity.this, Big_PointHistoryActivity.class)
                            .putExtra("type", BIG_Constants.HistoryType.WATCH_VIDEO)
                            .putExtra("title", "Videos List"));
                } else {
                    BIG_Common_Utils.NotifyLogin(Big_WatchVideoActivity.this);
                }
            }
        });
        layoutPoints = findViewById(R.id.layoutPoints);
        layoutPoints.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (BIG_SharePrefs.getInstance().getBoolean(BIG_SharePrefs.IS_LOGIN)) {
                    startActivity(new Intent(Big_WatchVideoActivity.this, Big_My_Wallet_Activity.class));
                } else {
                    BIG_Common_Utils.NotifyLogin(Big_WatchVideoActivity.this);
                }
            }
        });
        tvPoints = findViewById(R.id.tvPoints);
        tvPoints.setText(BIG_SharePrefs.getInstance().getEarningPointString());
        ImageView imBack = findViewById(R.id.ivBack);
        imBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        new Big_Get_Watch_Video_List_Async(Big_WatchVideoActivity.this);
    }

    public void changeVideoDataValues(Big_Watch_Video_Model responseModel) {
        BIG_SharePrefs.getInstance().putString(BIG_SharePrefs.EarnedPoints, responseModel.getEarningPoint());
        if (responseModel.getTodayDate() != null) {
            todayDate = responseModel.getTodayDate();
        }
        if (responseModel.getLastVideoWatchedDate() != null) {
            lastDate = responseModel.getLastVideoWatchedDate();
        }
        if (responseModel.getWatchTime() != null) {
            watchTime = responseModel.getWatchTime();
        }
        BIG_Common_Utils.logFirebaseEvent(Big_WatchVideoActivity.this, "FeatureUsabilityItemId", "FeatureUsabilityEvent", "Watch_Video_BigPrize", "Watch Video Got Reward");
        showWinPopup(listVideos.get(activeVideoPos).getVideoPoints(), responseModel.getIsShowAds());
    }

    private void showWinPopup(String videoPoints, String isShowAds) {
        try {
            Dialog dialogWin = new Dialog(Big_WatchVideoActivity.this, android.R.style.Theme_Light);
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
                    BIG_Common_Utils.startTextCountAnimation(tvPoint, videoPoints);
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
                    if (isShowAds != null && isShowAds.equals("1")) {
                        BIG_Ads_Utils.showAppLovinInterstitialAd(Big_WatchVideoActivity.this, new BIG_Ads_Utils.AdShownListener() {
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
                int pt = Integer.parseInt(videoPoints);
                lblPoints.setText((pt <= 1 ? "Ruby" : "Rubies"));
            } catch (NumberFormatException e) {
                e.printStackTrace();
                lblPoints.setText("Rubies");
            }

            btnOk.setOnClickListener(v -> {
                BIG_Ads_Utils.showAppLovinInterstitialAd(Big_WatchVideoActivity.this, new BIG_Ads_Utils.AdShownListener() {
                    @Override
                    public void onAdDismiss() {
                        if (isShowAds != null && isShowAds.equals("1")) {
                            BIG_Ads_Utils.showAppLovinInterstitialAd(Big_WatchVideoActivity.this, new BIG_Ads_Utils.AdShownListener() {
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
            });
            dialogWin.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {
                    try {
                        BIG_Common_Utils.GetCoinAnimation(Big_WatchVideoActivity.this, layoutMain, layoutPoints);
                        tvPoints.setText(BIG_SharePrefs.getInstance().getEarningPointString());
                        listVideos.get(activeVideoPos).setWatchedVideoPoints(videoPoints);
                        listVideos.get(activeVideoPos).setButtonText(null);
                        mAdapter.updateLastWatchedVideo(Integer.parseInt(listVideos.get(activeVideoPos).getVideoId()));
                        mAdapter.notifyItemChanged(activeVideoPos);
                        activeVideoPos = activeVideoPos + 1;
                        mAdapter.notifyItemChanged(activeVideoPos);
                        setTimer(false);
                    } catch (Exception e) {
                        e.printStackTrace();
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
    private void loadAppLovinNativeAds(FrameLayout frameLayoutNativeAd, TextView lblLoadingAds) {
        try {
            nativeAdLoaderWin = new MaxNativeAdLoader(BIG_Common_Utils.getRandomAdUnitId(responseMain.getLovinNativeID()), Big_WatchVideoActivity.this);
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
    private void loadAppLovinNativeAdsTask(LinearLayout layoutAds, FrameLayout frameLayoutNativeAd, TextView lblLoadingAds) {
        try {
            nativeAdLoaderTask = new MaxNativeAdLoader(BIG_Common_Utils.getRandomAdUnitId(responseMain.getLovinNativeID()), Big_WatchVideoActivity.this);
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
    @Override
    public void onBackPressed() {

            super.onBackPressed();

    }

    private Big_Watch_Video_Model responseModel;
    public void setData(Big_Watch_Video_Model responseModel1) {
        responseModel=responseModel1;


        if (responseModel.getWatchVideoList() != null && responseModel.getWatchVideoList().size() > 0) {
            listVideos.addAll(responseModel.getWatchVideoList());
            if (responseModel.getTodayDate() != null) {
                todayDate = responseModel.getTodayDate();
            }
            if (responseModel.getLastVideoWatchedDate() != null) {
                lastDate = responseModel.getLastVideoWatchedDate();
            }
            if (responseModel.getWatchTime() != null) {
                watchTime = responseModel.getWatchTime();
            }
            for (int i = 0; i < listVideos.size(); i++) {
                if (Integer.parseInt(listVideos.get(i).getVideoId()) == (Integer.parseInt(responseModel.getLastWatchedVideoId()) + 1)) {
                    activeVideoPos = i;
                }
                if (responseModel.getWatchedVideoList() != null && responseModel.getWatchedVideoList().size() > 0) {
                    for (int j = 0; j < responseModel.getWatchedVideoList().size(); j++) {
                        if (listVideos.get(i).getVideoId().equals(responseModel.getWatchedVideoList().get(j).getVideoId())) {
                            listVideos.get(i).setWatchedVideoPoints(responseModel.getWatchedVideoList().get(j).getWatchedVideoPoints());
                        }
                    }
                }
            }
            mAdapter = new Big_Watch_Video_List_Adapter(listVideos, this, Integer.parseInt(responseModel.getLastWatchedVideoId()), (!BIG_Common_Utils.isStringNullOrEmpty(responseModel.getIsTodayTaskCompleted()) && responseModel.getIsTodayTaskCompleted().equals("1")), new Big_Watch_Video_List_Adapter.ClickListener() {
                @Override
                public void onItemClick(int position, View v) {
                    if (BIG_SharePrefs.getInstance().getBoolean(BIG_SharePrefs.IS_LOGIN)) {
                        try {
                            if (Integer.parseInt(listVideos.get(position).getVideoId()) == (Integer.parseInt(listVideos.get(activeVideoPos).getVideoId())) && !isTimerSet) {
                                if (BIG_Common_Utils.isNetworkAvailable(Big_WatchVideoActivity.this)) {
                                    if (listVideos.get(position).getAdsType().equals(BIG_Constants.APPLOVIN_INTERSTITIAL)) {
                                        BIG_Ads_Utils.showAppLovinInterstitialAd(Big_WatchVideoActivity.this, new BIG_Ads_Utils.VideoAdShownListener() {
                                            @Override
                                            public void onAdDismiss(boolean isAdShown) {
                                                if (isAdShown) {
                                                    // call api
                                                    new Big_Save_Watch_Video_List_Async(Big_WatchVideoActivity.this, listVideos.get(position).getVideoId(), listVideos.get(position).getVideoPoints());
                                                } else {
                                                    BIG_Common_Utils.setToast(Big_WatchVideoActivity.this, "Problem while displaying video");
                                                }
                                            }
                                        }, true);
                                    } else if (listVideos.get(position).getAdsType().equals(BIG_Constants.APPLOVIN_REWARD)) {
                                        BIG_Ads_Utils.showAppLovinRewardedAd(Big_WatchVideoActivity.this, new BIG_Ads_Utils.VideoAdShownListener() {
                                            @Override
                                            public void onAdDismiss(boolean isAdShown) {
                                                if (isAdShown) {
                                                    // call api
                                                    new Big_Save_Watch_Video_List_Async(Big_WatchVideoActivity.this, listVideos.get(position).getVideoId(), listVideos.get(position).getVideoPoints());
                                                } else {
                                                    BIG_Common_Utils.setToast(Big_WatchVideoActivity.this, "Problem while displaying video");
                                                }
                                            }
                                        }, true);
                                    }
                                } else {
                                    BIG_Common_Utils.setToast(Big_WatchVideoActivity.this, "No internet connection");
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        BIG_Common_Utils.NotifyLogin(Big_WatchVideoActivity.this);
                    }
                }
            });
            rvVideoList.setLayoutManager(new LinearLayoutManager(this));
            rvVideoList.setAdapter(mAdapter);
            if (!responseModel.getLastWatchedVideoId().equals(listVideos.get(listVideos.size() - 1).getVideoId())) {
                setTimer(true);
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

                                if (!BIG_Common_Utils.isStringNullOrEmpty(responseModel.getScreenNo())) {
//                                    if (!BIG_Common_Utils.hasUsageAccessPermission(Big_WatchVideoActivity.this)) {
//                                        BIG_Common_Utils.showUsageAccessPermissionDialog(Big_WatchVideoActivity.this);
//                                        return;
//                                    } else {
//
//                                    }
                                    BIG_Common_Utils.Redirect(Big_WatchVideoActivity.this, responseModel.getScreenNo(), "", "", "", "", "");
                                } else if (!BIG_Common_Utils.isStringNullOrEmpty(responseModel.getTaskId())) {
                                    Intent intent = new Intent(Big_WatchVideoActivity.this, Big_TaskDetailsInfoActivity.class);
                                    intent.putExtra("taskId", responseModel.getTaskId());
                                    startActivity(intent);
                                } else {
                                    Intent intent = new Intent(Big_WatchVideoActivity.this, Big_TasksCategoryTypeActivity.class);
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
            } else {
                BIG_Ads_Utils.showAppLovinInterstitialAd(Big_WatchVideoActivity.this, null);
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
                    BIG_Common_Utils.loadTopBannerAd(Big_WatchVideoActivity.this, layoutTopAds, responseModel.getTopAds());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            lblLoadingAds = findViewById(R.id.lblLoadingAds);
            layoutAds = findViewById(R.id.layoutAds);
            layoutAds.setVisibility(View.VISIBLE);
            frameLayoutNativeAd = findViewById(R.id.fl_adplaceholder);
            if (BIG_Common_Utils.isShowAppLovinNativeAds()) {
                loadAppLovinNativeAds();
            } else {
                layoutAds.setVisibility(View.GONE);
            }
        }
        rvVideoList.setVisibility(responseModel.getWatchVideoList() != null && responseModel.getWatchVideoList().size() > 0 ? View.VISIBLE : View.GONE);
        ivLottieNoData.setVisibility(responseModel.getWatchVideoList() != null && responseModel.getWatchVideoList().size() > 0 ? View.GONE : View.VISIBLE);
        if (responseModel.getWatchVideoList() == null && responseModel.getWatchVideoList().size() == 0)
            ivLottieNoData.playAnimation();
    }

    public void setTimer(boolean isFromOnCreate) {
        try {
            isSetTimerValue = false;
            if (BIG_Common_Utils.timeDiff(todayDate, lastDate) > Integer.parseInt(watchTime)) {
                isTimerSet = false;
                listVideos.get(activeVideoPos).setButtonText("Watch Now");
                mAdapter.notifyItemChanged(activeVideoPos);
            } else {
                isTimerSet = true;
                if (timer != null) {
                    timer.cancel();
                }
                time = BIG_Common_Utils.timeDiff(todayDate, lastDate);
                timer = new CountDownTimer((Integer.parseInt(watchTime) - time) * 60000L, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        try {
                            listVideos.get(activeVideoPos).setButtonText(BIG_Common_Utils.updateTimeRemainingWatchVideo(millisUntilFinished));
                            if (!isSetTimerValue) {
                                mAdapter.notifyItemChanged(activeVideoPos);
                            } else {
                                RecyclerView.ViewHolder v = rvVideoList.findViewHolderForAdapterPosition(activeVideoPos);
                                TextView tv = v.itemView.findViewById(R.id.tvButton);
                                tv.setText(BIG_Common_Utils.updateTimeRemainingWatchVideo(millisUntilFinished));
                            }
                            isSetTimerValue = true;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFinish() {
                        try {
                            isTimerSet = false;
                            listVideos.get(activeVideoPos).setButtonText("Watch Now");
                            mAdapter.notifyItemChanged(activeVideoPos);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
                if (isFromOnCreate) {
                    BIG_Ads_Utils.showAppLovinInterstitialAd(Big_WatchVideoActivity.this, null);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadAppLovinNativeAds() {
        try {
            nativeAdLoader = new MaxNativeAdLoader(BIG_Common_Utils.getRandomAdUnitId(responseMain.getLovinNativeID()), Big_WatchVideoActivity.this);
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
                    lblLoadingAds.setVisibility(View.GONE);
                    layoutAds.setVisibility(View.VISIBLE);

                    //AppLogger.getInstance().e("AppLovin Loaded: ", "===");
                }

                @Override
                public void onNativeAdLoadFailed(final String adUnitId, final MaxError error) {
                    //AppLogger.getInstance().e("AppLovin Failed: ", error.getMessage());
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

    @Override
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
                if (nativeAdTask != null && nativeAdLoaderTask != null) {
                    nativeAdLoaderTask.destroy(nativeAdTask);
                    nativeAdTask = null;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


}