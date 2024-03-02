package com.app.bigprize.Activity;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
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
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.app.bigprize.Adapter.Big_DailyClaimAdapter;
import com.app.bigprize.Async.Big_Save_Daily_Login_Async;
import com.app.bigprize.Async.Models.Big_Daily_Bonus;
import com.app.bigprize.Async.Models.Big_Daily_Bonus_Data_Model;
import com.app.bigprize.Async.Models.Big_Daily_Bonus_Item;
import com.app.bigprize.Async.Models.Big_Response_Model;
import com.app.bigprize.Async.Models.Big_Reward_Screen_Model;
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

public class Big_ActivityDailyLogin extends AppCompatActivity {
    private RecyclerView rvDailyLoginList;
    private LinearLayout layoutPoints;
    private ImageView ivHistory;
    private ArrayList<Big_Daily_Bonus_Item> listData = new ArrayList<>();
    private TextView tvPoints, lblDailyLogin, lblLoadingAds;
    private Big_Response_Model responseMain;
    private MaxAd nativeAd, nativeAdWin;
    private MaxNativeAdLoader nativeAdLoader, nativeAdLoaderWin;
    private LinearLayout layoutAds;
    private FrameLayout frameLayoutNativeAd;
    private int selectedPos = -1, lastClaimDay;
    private Big_DailyClaimAdapter dailyLoginAdapter;
    private RelativeLayout layoutMain;
    private Big_Reward_Screen_Model objRewardScreenModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        BIG_Common_Utils.setDayNightTheme(Big_ActivityDailyLogin.this);
        setContentView(R.layout.big_activity_daily_login2);
        responseMain = new Gson().fromJson(BIG_SharePrefs.getInstance().getString(BIG_SharePrefs.HomeData), Big_Response_Model.class);
        objRewardScreenModel = (Big_Reward_Screen_Model) getIntent().getSerializableExtra("objRewardScreenModel");



        layoutMain = findViewById(R.id.layoutMain);
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

        lblDailyLogin = findViewById(R.id.lblDailyLogin);
        tvPoints = findViewById(R.id.tvPoints);
        tvPoints.setText(BIG_SharePrefs.getInstance().getEarningPointString());
        layoutPoints = findViewById(R.id.layoutPoints);
        layoutPoints.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (BIG_SharePrefs.getInstance().getBoolean(BIG_SharePrefs.IS_LOGIN)) {
                    startActivity(new Intent(Big_ActivityDailyLogin.this, Big_My_Wallet_Activity.class));
                } else {
                    BIG_Common_Utils.NotifyLogin(Big_ActivityDailyLogin.this);
                }
            }
        });

        ivHistory = findViewById(R.id.ivHistory);
        //     Common_Utils.startRoundAnimation(ActivityDailyLogin.this, ivHistory);
        ivHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (BIG_SharePrefs.getInstance().getBoolean(BIG_SharePrefs.IS_LOGIN)) {
                    startActivity(new Intent(Big_ActivityDailyLogin.this, Big_PointHistoryActivity.class)
                            .putExtra("type", BIG_Constants.HistoryType.DAILY_LOGIN)
                            .putExtra("title", "Daily Login History"));
                } else {
                    BIG_Common_Utils.NotifyLogin(Big_ActivityDailyLogin.this);
                }
            }
        });

        try {
            TextView lblNote = findViewById(R.id.lblNote);
            LinearLayout layoutCompleteTask = findViewById(R.id.layoutCompleteTask);
            if (!BIG_Common_Utils.isStringNullOrEmpty(objRewardScreenModel.getIsTodayTaskCompleted()) && objRewardScreenModel.getIsTodayTaskCompleted().equals("0")) {
                layoutCompleteTask.setVisibility(VISIBLE);
                lblNote.setVisibility(GONE);
                TextView tvTaskNote = findViewById(R.id.tvTaskNote);
                tvTaskNote.setText(objRewardScreenModel.getTaskNote());
                Button btnCompleteTask = findViewById(R.id.btnCompleteTask);
                if (!BIG_Common_Utils.isStringNullOrEmpty(objRewardScreenModel.getTaskButton())) {
                    btnCompleteTask.setText(objRewardScreenModel.getTaskButton());
                }

                btnCompleteTask.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (!BIG_Common_Utils.isStringNullOrEmpty(objRewardScreenModel.getScreenNo())) {
                            BIG_Common_Utils.Redirect(Big_ActivityDailyLogin.this, objRewardScreenModel.getScreenNo(), "", "", "", "", "");  BIG_Common_Utils.Redirect(Big_ActivityDailyLogin.this, objRewardScreenModel.getScreenNo(), "", "", "", "", "");
                        } else if (!BIG_Common_Utils.isStringNullOrEmpty(objRewardScreenModel.getTaskId())) {
                            Intent intent = new Intent(Big_ActivityDailyLogin.this, Big_TaskDetailsInfoActivity.class);
                            intent.putExtra("taskId", objRewardScreenModel.getTaskId());
                            startActivity(intent);
                        } else {
                            Intent intent = new Intent(Big_ActivityDailyLogin.this, Big_TasksCategoryTypeActivity.class);
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

        try {
            listData.addAll(objRewardScreenModel.getDailyBonus().getData());
            lastClaimDay = Integer.parseInt(objRewardScreenModel.getDailyBonus().getLastClaimedDay());
            if (lastClaimDay > 0) {
                lblDailyLogin.setText("Checked in for " + lastClaimDay + (lastClaimDay == 1 ? " consecutive day" : " consecutive days"));
            } else {
                lblDailyLogin.setText("Check in for the first day to get reward Rubies!");
            }
            rvDailyLoginList = findViewById(R.id.rvDailyLoginList);
            dailyLoginAdapter = new Big_DailyClaimAdapter(listData, Big_ActivityDailyLogin.this, lastClaimDay, Integer.parseInt(objRewardScreenModel.getDailyBonus().getIsTodayClaimed()), new Big_DailyClaimAdapter.ClickListener() {
                @Override
                public void onItemClick(int position, View v) {
                    if (BIG_SharePrefs.getInstance().getBoolean(BIG_SharePrefs.IS_LOGIN)) {
                        if ((Integer.parseInt(listData.get(position).getDay_id())) <= lastClaimDay) {
                            BIG_Common_Utils.setToast(Big_ActivityDailyLogin.this, "You have already collected reward for day " + listData.get(position).getDay_id());
                        } else if ((objRewardScreenModel.getDailyBonus().getIsTodayClaimed() != null && objRewardScreenModel.getDailyBonus().getIsTodayClaimed().equals("1"))) {
                            BIG_Common_Utils.setToast(Big_ActivityDailyLogin.this, "You have already collected reward for today");
                        } else if (Integer.parseInt(listData.get(position).getDay_id()) > (lastClaimDay + 1)) {
                            BIG_Common_Utils.setToast(Big_ActivityDailyLogin.this, "Please claim reward for day " + (lastClaimDay + 1));
                        } else {
                            BIG_Ads_Utils.showAppLovinInterstitialAd(Big_ActivityDailyLogin.this, new BIG_Ads_Utils.AdShownListener() {
                                @Override
                                public void onAdDismiss() {
                                    selectedPos = position;
                                    dailyLoginAdapter.setClicked();
                                    new Big_Save_Daily_Login_Async(Big_ActivityDailyLogin.this, listData.get(position).getDay_points(), listData.get(position).getDay_id());

                                }
                            });
                        }
                    } else {
                        BIG_Common_Utils.NotifyLogin(Big_ActivityDailyLogin.this);
                    }
                }
            });

            GridLayoutManager mGridLayoutManager = new GridLayoutManager(Big_ActivityDailyLogin.this, 5);
            mGridLayoutManager.setOrientation(RecyclerView.VERTICAL);
            rvDailyLoginList.setLayoutManager(mGridLayoutManager);
            rvDailyLoginList.setItemAnimator(new DefaultItemAnimator());
            rvDailyLoginList.setAdapter(dailyLoginAdapter);

            // Load home note webview top
            try {
                if (!BIG_Common_Utils.isStringNullOrEmpty(objRewardScreenModel.getDailyBonus().getHomeNote())) {
                    WebView webNote = findViewById(R.id.webNote);
                    webNote.getSettings().setJavaScriptEnabled(true);
                    webNote.setVisibility(View.VISIBLE);
                    webNote.loadDataWithBaseURL(null, objRewardScreenModel.getDailyBonus().getHomeNote(), "text/html", "UTF-8", null);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Load top ad
            try {
                if (objRewardScreenModel.getDailyBonus().getTopAds() != null && !BIG_Common_Utils.isStringNullOrEmpty(objRewardScreenModel.getDailyBonus().getTopAds().getImage())) {
                    LinearLayout layoutTopAds = findViewById(R.id.layoutTopAds);
                    BIG_Common_Utils.loadTopBannerAd(Big_ActivityDailyLogin.this, layoutTopAds, objRewardScreenModel.getDailyBonus().getTopAds());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (objRewardScreenModel.getDailyBonus().getIsTodayClaimed() != null && objRewardScreenModel.getDailyBonus().getIsTodayClaimed().equals("1")) {
                BIG_Ads_Utils.showAppLovinInterstitialAd(Big_ActivityDailyLogin.this, null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();

    }

    private Big_Daily_Bonus_Data_Model responseModel;

    public void onDailyLoginDataChanged(Big_Daily_Bonus_Data_Model responseModel1) {
        responseModel = responseModel1;

        if (!BIG_Common_Utils.isStringNullOrEmpty(responseModel.getEarningPoint())) {
            BIG_SharePrefs.getInstance().putString(BIG_SharePrefs.EarnedPoints, responseModel.getEarningPoint());
        }
        if (responseModel.getStatus().equals(BIG_Constants.STATUS_SUCCESS) || responseModel.getStatus().equals("3")) {
            BIG_Common_Utils.logFirebaseEvent(Big_ActivityDailyLogin.this, "FeatureUsabilityItemId", "FeatureUsabilityEvent", "Daily_Login_BigPrize", "Got Reward");
            showWinPopup(listData.get(selectedPos).getDay_points(), responseModel);
        } else if (responseModel.getStatus().equals("2")) {// missed login
            BIG_Common_Utils.logFirebaseEvent(Big_ActivityDailyLogin.this, "FeatureUsabilityItemId", "FeatureUsabilityEvent", "Daily_Login_BigPrize", "Missed Daily Login");
            showMissedLoginDialog("Daily Login is Missed", responseModel);
        }
        lastClaimDay = Integer.parseInt(responseModel.getLastClaimedDay());
        if (lastClaimDay > 0) {
            lblDailyLogin.setText("Checked in for " + lastClaimDay + (lastClaimDay == 1 ? " consecutive day" : " consecutive days"));
        } else {
            lblDailyLogin.setText("Check in for the first day to get reward Rubies!");
        }


        dailyLoginAdapter.setLastClaimedData(lastClaimDay, Integer.parseInt(responseModel.getIsTodayClaimed()));
        Big_Daily_Bonus obj = objRewardScreenModel.getDailyBonus();
        obj.setLastClaimedDay(responseModel.getLastClaimedDay());
        obj.setIsTodayClaimed(responseModel.getIsTodayClaimed());
        objRewardScreenModel.setDailyBonus(obj);
        Big_EarningOptionsActivity.objRewardScreenModel = objRewardScreenModel;
    }

    public void showErrorMessage(String title, String message) {
        try {
            final Dialog dialog1 = new Dialog(Big_ActivityDailyLogin.this, android.R.style.Theme_Light);
            dialog1.getWindow().setBackgroundDrawableResource(R.color.black_transparent);
            dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog1.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            dialog1.setContentView(R.layout.big_dialog_notify);
            dialog1.setCancelable(false);

            Button btnOk = dialog1.findViewById(R.id.btnOk);
            TextView tvTitle = dialog1.findViewById(R.id.tvTitle);
            tvTitle.setText(title);

            TextView tvMessage = dialog1.findViewById(R.id.tvMessage);
            tvMessage.setText(message);
            btnOk.setOnClickListener(v -> {
                BIG_Ads_Utils.showAppLovinInterstitialAd(Big_ActivityDailyLogin.this, new BIG_Ads_Utils.AdShownListener() {
                    @Override
                    public void onAdDismiss() {
                        if (dialog1 != null) {
                            dialog1.dismiss();
                        }
                    }
                });
            });
            if (!isFinishing()) {
                dialog1.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showMissedLoginDialog(String title, Big_Daily_Bonus_Data_Model responseModel) {
        try {
            final Dialog dialog1 = new Dialog(Big_ActivityDailyLogin.this, android.R.style.Theme_Light);
            dialog1.getWindow().setBackgroundDrawableResource(R.color.black_transparent);
            dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog1.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            dialog1.setContentView(R.layout.big_dialog_notify);
            dialog1.setCancelable(false);

            Button btnOk = dialog1.findViewById(R.id.btnOk);
            TextView tvTitle = dialog1.findViewById(R.id.tvTitle);
            tvTitle.setText(title);

            TextView tvMessage = dialog1.findViewById(R.id.tvMessage);
            tvMessage.setText(responseModel.getMessage());
            btnOk.setOnClickListener(v -> {
                BIG_Ads_Utils.showAppLovinInterstitialAd(Big_ActivityDailyLogin.this, new BIG_Ads_Utils.AdShownListener() {
                    @Override
                    public void onAdDismiss() {
                        if (dialog1 != null) {
                            dialog1.dismiss();
                        }
                    }
                });
            });
            if (!isFinishing()) {
                dialog1.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void showWinPopup(String point, Big_Daily_Bonus_Data_Model responseModel) {
        Dialog dialogWin = new Dialog(Big_ActivityDailyLogin.this, android.R.style.Theme_Light);
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
//        Log.e("lottieeee", "showWinPopup: "+responseMain.getCelebrationLottieUrl());
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

        TextView lblPoints = dialogWin.findViewById(R.id.lblPoints);
        try {
            int pt = Integer.parseInt(point);
            lblPoints.setText((pt <= 1 ? "Ruby" : "Rubies"));
        } catch (NumberFormatException e) {
            e.printStackTrace();
            lblPoints.setText("Rubies");
        }

        AppCompatButton btnOk = dialogWin.findViewById(R.id.btnOk);
        ImageView ivClose = dialogWin.findViewById(R.id.ivClose);
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dialogWin != null) {
                    dialogWin.dismiss();
                }
            }
        });

        if (!BIG_Common_Utils.isStringNullOrEmpty(responseModel.getBtnName())) {
            btnOk.setText(responseModel.getBtnName());
        }

        if (!BIG_Common_Utils.isStringNullOrEmpty(responseModel.getBtnColor())) {
            Drawable mDrawable = ContextCompat.getDrawable(Big_ActivityDailyLogin.this, R.drawable.big_ic_btn_gradient_rounded_corner_rect_new);
            mDrawable.setColorFilter(new PorterDuffColorFilter(Color.parseColor(responseModel.getBtnColor()), PorterDuff.Mode.SRC_IN));
            btnOk.setBackground(mDrawable);
        }

        btnOk.setOnClickListener(v -> {
            BIG_Ads_Utils.showAppLovinRewardedAd(Big_ActivityDailyLogin.this, new BIG_Ads_Utils.AdShownListener() {
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
                selectedPos = -1;
                BIG_Common_Utils.GetCoinAnimation(Big_ActivityDailyLogin.this, layoutMain, layoutPoints);
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
    }

    private void loadAppLovinNativeAds(FrameLayout frameLayoutNativeAd, TextView lblLoadingAds) {
        try {
            nativeAdLoaderWin = new MaxNativeAdLoader(BIG_Common_Utils.getRandomAdUnitId(responseMain.getLovinNativeID()), Big_ActivityDailyLogin.this);
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

    private void loadAppLovinNativeAds() {
        try {
            nativeAdLoader = new MaxNativeAdLoader(BIG_Common_Utils.getRandomAdUnitId(responseMain.getLovinNativeID()), Big_ActivityDailyLogin.this);
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