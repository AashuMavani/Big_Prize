package com.app.bigprize.Activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.app.bigprize.Async.Models.Big_Response_Model;
import com.app.bigprize.Async.Models.Big_User_Details;
import com.app.bigprize.R;
import com.app.bigprize.Value.BIG_Constants;
import com.app.bigprize.utils.BIG_Common_Utils;
import com.app.bigprize.utils.BIG_SharePrefs;
import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxError;
import com.applovin.mediation.nativeAds.MaxNativeAdListener;
import com.applovin.mediation.nativeAds.MaxNativeAdLoader;
import com.applovin.mediation.nativeAds.MaxNativeAdView;
import com.google.gson.Gson;

public class Big_My_Wallet_Activity extends AppCompatActivity {
    private AppCompatButton btnWithdraw;

    private View viewShine;
    private Big_User_Details userDetails;
    private TextView lblLoadingAds, tvWalletPoints, tvWalletRupees, tvPoints, tvRupees;
    private Big_Response_Model responseMain;
    private MaxAd nativeAd;
    private MaxNativeAdLoader nativeAdLoader;
    private FrameLayout frameLayoutNativeAd;
    private LinearLayout layoutAds, layoutPointHistory, layoutWithdrawalHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        BIG_Common_Utils.setDayNightTheme(Big_My_Wallet_Activity.this);
        setContentView(R.layout.big_activity_my_wallet);


        userDetails = new Gson().fromJson(BIG_SharePrefs.getInstance().getString(BIG_SharePrefs.User_Details), Big_User_Details.class);
        responseMain = new Gson().fromJson(BIG_SharePrefs.getInstance().getString(BIG_SharePrefs.HomeData), Big_Response_Model.class);
        viewShine = findViewById(R.id.viewShine);

        lblLoadingAds = findViewById(R.id.lblLoadingAds);
        layoutAds = findViewById(R.id.layoutAds);
        layoutAds.setVisibility(View.VISIBLE);
        frameLayoutNativeAd = findViewById(R.id.fl_adplaceholder);
        if (BIG_Common_Utils.isShowAppLovinNativeAds()) {
            loadAppLovinNativeAds();
        } else {
            layoutAds.setVisibility(View.GONE);
        }

        btnWithdraw = findViewById(R.id.btnWithdraw);
        btnWithdraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent inSpin = new Intent(Big_My_Wallet_Activity.this, Big_WithdrawTypesActivity.class);
                startActivity(inSpin);
            }
        });

        ImageView imBack = findViewById(R.id.ivBack);
        imBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        layoutAds = findViewById(R.id.layoutAds);

        layoutPointHistory = findViewById(R.id.layoutPointHistory);
        layoutPointHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (BIG_SharePrefs.getInstance().getBoolean(BIG_SharePrefs.IS_LOGIN)) {
                    startActivity(new Intent(Big_My_Wallet_Activity.this, Big_PointHistoryActivity.class));
                } else {
                    BIG_Common_Utils.NotifyLogin(Big_My_Wallet_Activity.this);
                }
            }
        });
        layoutWithdrawalHistory = findViewById(R.id.layoutWithdrawalHistory);
        layoutWithdrawalHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (BIG_SharePrefs.getInstance().getBoolean(BIG_SharePrefs.IS_LOGIN)) {
                    Intent intent = new Intent(Big_My_Wallet_Activity.this, Big_PointHistoryActivity.class);
                    intent.putExtra("type", BIG_Constants.HistoryType.WITHDRAW_HISTORY);
                    intent.putExtra("title", "Withdrawal History");
                    startActivity(intent);
                } else {
                    BIG_Common_Utils.NotifyLogin(Big_My_Wallet_Activity.this);
                }
            }
        });

        tvWalletPoints = findViewById(R.id.tvWalletPoints);
        tvWalletRupees = findViewById(R.id.tvWalletRupees);
        tvPoints = findViewById(R.id.tvPoints);
        tvRupees = findViewById(R.id.tvRupees);
        Animation animUpDown = AnimationUtils.loadAnimation(Big_My_Wallet_Activity.this, R.anim.big_left_right);
        animUpDown.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                viewShine.startAnimation(animUpDown);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        // start the animation
        viewShine.startAnimation(animUpDown);
    }

    protected void onResume() {
        super.onResume();
        try {
            tvWalletPoints.setText(BIG_SharePrefs.getInstance().getEarningPointString());
            tvRupees.setText("₹ 1");
            if (responseMain.getPointValue() != null) {
                tvPoints.setText(responseMain.getPointValue() + " Rubies");
                tvWalletRupees.setText(BIG_Common_Utils.convertPointsInINR(BIG_SharePrefs.getInstance().getEarningPointString(), responseMain.getPointValue()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    private void loadAppLovinNativeAds() {
        try {
            nativeAdLoader = new MaxNativeAdLoader(BIG_Common_Utils.getRandomAdUnitId(responseMain.getLovinNativeID()), Big_My_Wallet_Activity.this);
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
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
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}