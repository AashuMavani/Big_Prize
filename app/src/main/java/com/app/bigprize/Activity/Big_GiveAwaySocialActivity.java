package com.app.bigprize.Activity;

import static android.view.View.VISIBLE;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.app.bigprize.Adapter.Big_GiveawayCodesListAdapter;
import com.app.bigprize.Adapter.Big_GiveawaySocialAdapter;
import com.app.bigprize.Async.Big_Get_GiveAway_List_Async;
import com.app.bigprize.Async.Big_Save_Give_Away_Async;
import com.app.bigprize.Async.Models.Big_Giveaway_Model;
import com.app.bigprize.Async.Models.Big_Response_Model;
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

public class Big_GiveAwaySocialActivity extends AppCompatActivity {
    private RecyclerView rvSocialPlatforms;
    private TextView lblLoadingAds, tvPoints, tvNote, tvStarLeft, tvStarRight;
    private LottieAnimationView ivLottieNoData;
    private Big_Response_Model responseMain;
    private MaxAd nativeAd, nativeAdWin;
    private MaxNativeAdLoader nativeAdLoader, nativeAdLoaderWin;
    private FrameLayout frameLayoutNativeAd;
    private LinearLayout layoutAds;
    private Big_GiveawaySocialAdapter mAdapter;
    private ImageView ivHistory;
    private LinearLayout layoutPoints, layoutContent;
    private EditText etCouponCode;
    private AppCompatButton btnClaimNow, btnHowToClaim;
    private RelativeLayout layoutMain;
    private TextView titleLabel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        BIG_Common_Utils.setDayNightTheme(Big_GiveAwaySocialActivity.this);
        setContentView(R.layout.big_activity_give_away_social);
        responseMain = new Gson().fromJson(BIG_SharePrefs.getInstance().getString(BIG_SharePrefs.HomeData), Big_Response_Model.class);

//        tvNote = findViewById(R.id.tvNote);
        layoutMain = findViewById(R.id.layoutMain);
        titleLabel = findViewById(R.id.titleLabel);
        titleLabel.setVisibility(View.GONE);
        Animation rotation = AnimationUtils.loadAnimation(Big_GiveAwaySocialActivity.this, R.anim.big_rotate);
        rotation.setFillAfter(true);
//
//        tvStarLeft = findViewById(R.id.tvStarLeft);
//        tvStarLeft.startAnimation(rotation);
//
//        tvStarRight = findViewById(R.id.tvStarRight);
//        tvStarRight.startAnimation(rotation);

        etCouponCode = findViewById(R.id.etCouponCode);
        etCouponCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                etCouponCode.post(new Runnable() {
                    @Override
                    public void run() {
                        etCouponCode.setLetterSpacing(etCouponCode.getText().toString().length() > 0 ? 0.2f : 0.0f);
                    }
                });
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        btnClaimNow = findViewById(R.id.btnClaimNow);
        btnHowToClaim = findViewById(R.id.btnHowToClaim);
        btnClaimNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BIG_Common_Utils.setEnableDisable(Big_GiveAwaySocialActivity.this, btnClaimNow);
                if (BIG_SharePrefs.getInstance().getBoolean(BIG_SharePrefs.IS_LOGIN)) {
                    if (etCouponCode.getText().toString().trim().length() > 0) {
                        new Big_Save_Give_Away_Async(Big_GiveAwaySocialActivity.this, etCouponCode.getText().toString().trim());
                    } else {
                        BIG_Common_Utils.setToast(Big_GiveAwaySocialActivity.this, "Enter giveaway code");
                    }
                } else {
                    BIG_Common_Utils.NotifyLogin(Big_GiveAwaySocialActivity.this);
                }
            }
        });

//        layoutContent = findViewById(R.id.layoutContent);
        ivLottieNoData = findViewById(R.id.ivLottieNoData);
        rvSocialPlatforms = findViewById(R.id.rvSocialPlatforms);

        ivHistory = findViewById(R.id.ivHistory);
        ivHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (BIG_SharePrefs.getInstance().getBoolean(BIG_SharePrefs.IS_LOGIN)) {
                    startActivity(new Intent(Big_GiveAwaySocialActivity.this, Big_PointHistoryActivity.class)
                            .putExtra("type", BIG_Constants.HistoryType.GIVE_AWAY)
                            .putExtra("title", "Giveaway Code History"));
                } else {
                    BIG_Common_Utils.NotifyLogin(Big_GiveAwaySocialActivity.this);
                }
            }
        });
        layoutPoints = findViewById(R.id.layoutPoints);
        layoutPoints.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (BIG_SharePrefs.getInstance().getBoolean(BIG_SharePrefs.IS_LOGIN)) {
                    startActivity(new Intent(Big_GiveAwaySocialActivity.this, Big_My_Wallet_Activity.class));
                } else {
                    BIG_Common_Utils.NotifyLogin(Big_GiveAwaySocialActivity.this);
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

        View viewShine = findViewById(R.id.viewShine);
        Animation animUpDown = AnimationUtils.loadAnimation(Big_GiveAwaySocialActivity.this, R.anim.big_left_right);
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

        new Big_Get_GiveAway_List_Async(Big_GiveAwaySocialActivity.this);
    }


    public void changeGiveawayDataValues(Big_Giveaway_Model responseModel) {
        if (responseModel.getStatus().equals(BIG_Constants.STATUS_SUCCESS)) {
            BIG_SharePrefs.getInstance().putString(BIG_SharePrefs.EarnedPoints, responseModel.getEarningPoint());
            BIG_Common_Utils.logFirebaseEvent(Big_GiveAwaySocialActivity.this, "FeatureUsabilityItemId", "FeatureUsabilityEvent", "Giveaway_BigPrize", "Giveaway Got Reward");
            showWinPopup(responseModel.getCouponPoints());
        } else if (responseModel.getStatus().equals(BIG_Constants.STATUS_ERROR) || responseModel.getStatus().equals("2")) {
            showErrorMessage("Daily Giveaway", responseModel);
        }
    }

    private void showErrorMessage(String title, Big_Giveaway_Model responseModel) {
        try {
            final Dialog dialog1 = new Dialog(Big_GiveAwaySocialActivity.this, android.R.style.Theme_Light);
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


            if (!BIG_Common_Utils.isStringNullOrEmpty(responseModel.getBtn_name())) {
                btnOk.setText(responseModel.getBtn_name());
            }
            btnOk.setOnClickListener(v -> {
                BIG_Ads_Utils.showAppLovinInterstitialAd(Big_GiveAwaySocialActivity.this, new BIG_Ads_Utils.AdShownListener() {
                    @Override
                    public void onAdDismiss() {
                        if (dialog1 != null) {
                            dialog1.dismiss();
                        }
                        if (!BIG_Common_Utils.isStringNullOrEmpty(responseModel.getScreenNo())) {
                            BIG_Common_Utils.Redirect(Big_GiveAwaySocialActivity.this, responseModel.getScreenNo(), "", "", "", "", "");
                        }
                        BIG_AppLogger.getInstance().e("Screenno=",""+responseModel.getScreenNo());
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

    public void showWinPopup(String point) {
        Dialog dialogWin = new Dialog(Big_GiveAwaySocialActivity.this, android.R.style.Theme_Light);
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
                if (dialogWin != null) {
                    dialogWin.dismiss();
                }
            }
        });

        TextView lblPoints = dialogWin.findViewById(R.id.lblPoints);
        AppCompatButton btnOk = dialogWin.findViewById(R.id.btnOk);
        try {
            int pt = Integer.parseInt(point);
            lblPoints.setText((pt <= 1 ? "Ruby" : "Rubies"));
        } catch (Exception e) {
            e.printStackTrace();
            lblPoints.setText("Rubies");
        }

        btnOk.setOnClickListener(v -> {
            BIG_Ads_Utils.showAppLovinInterstitialAd(Big_GiveAwaySocialActivity.this, new BIG_Ads_Utils.AdShownListener() {
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
                BIG_Common_Utils.GetCoinAnimation(Big_GiveAwaySocialActivity.this, layoutMain, layoutPoints);
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
            nativeAdLoaderWin = new MaxNativeAdLoader(BIG_Common_Utils.getRandomAdUnitId(responseMain.getLovinNativeID()), Big_GiveAwaySocialActivity.this);
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
   private Big_Giveaway_Model responseModel;
    public void setData(Big_Giveaway_Model responseModel1) {
        responseModel=responseModel1;
        Log.e("Social--" , "" + responseModel.getSocialMedia().size());
        if (responseModel.getSocialMedia() != null && responseModel.getSocialMedia().size() > 0) {
            BIG_Ads_Utils.showAppLovinInterstitialAd(Big_GiveAwaySocialActivity.this, null);


            mAdapter = new Big_GiveawaySocialAdapter(this, responseModel.getSocialMedia(), new Big_GiveawaySocialAdapter.ClickListener() {
                @Override
                public void onItemClick(int position, View v) {
                    BIG_Common_Utils.openUrl(Big_GiveAwaySocialActivity.this, responseModel.getSocialMedia().get(position).getUrl());
                }
            });
            rvSocialPlatforms.setAdapter(mAdapter);
            try {
                LinearLayout layoutGiveawayCodes = findViewById(R.id.layoutGiveawayCodes);
                if (responseModel.getGiveawayCodeList() != null && responseModel.getGiveawayCodeList().size() > 0) {
                    layoutGiveawayCodes.setVisibility(VISIBLE);
//                    titleLabel.setVisibility(VISIBLE);
                    RecyclerView rvGiveawayCodeList = findViewById(R.id.rvGiveawayCodeList);
                    Big_GiveawayCodesListAdapter mAdapter = new Big_GiveawayCodesListAdapter(this, responseModel.getGiveawayCodeList(), new Big_GiveawayCodesListAdapter.ClickListener() {
                        @Override
                        public void onItemClick(int position, View v) {
                        }

                        @Override
                        public void onCopyButtonClicked(int position, View v) {
                            String val = responseModel.getGiveawayCodeList().get(position).getCouponCode();
                            if (val != null) {
                                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                                ClipData clip = ClipData.newPlainText("Copied Text", val);
                                clipboard.setPrimaryClip(clip);
                                BIG_Common_Utils.setToast(Big_GiveAwaySocialActivity.this, "Copied!");
                                BIG_Ads_Utils.showAppLovinRewardedAd(Big_GiveAwaySocialActivity.this, null);
                            }
                        }

                        @Override
                        public void onCompleteTaskButtonClicked(int position, View v) {
                            BIG_Common_Utils.Redirect(Big_GiveAwaySocialActivity.this, responseModel.getGiveawayCodeList().get(position).getScreenNo(), "", "", "", "", "");
                        }
                    });
                    rvGiveawayCodeList.setLayoutManager(new LinearLayoutManager(Big_GiveAwaySocialActivity.this));
                    rvGiveawayCodeList.setAdapter(mAdapter);
                } else {
                    layoutGiveawayCodes.setVisibility(View.GONE);
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
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Load giveaway codes
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
                    BIG_Common_Utils.loadTopBannerAd(Big_GiveAwaySocialActivity.this, layoutTopAds, responseModel.getTopAds());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (!BIG_Common_Utils.isStringNullOrEmpty(responseModel.getHelpVideoUrl())) {
            btnHowToClaim.setVisibility(VISIBLE);
            btnHowToClaim.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    BIG_Common_Utils.openUrl(Big_GiveAwaySocialActivity.this, responseModel.getHelpVideoUrl());
                }
            });
        } else {
            btnHowToClaim.setVisibility(View.GONE);
        }
        lblLoadingAds = findViewById(R.id.lblLoadingAds);
        layoutAds = findViewById(R.id.layoutAds);
        layoutAds.setVisibility(View.VISIBLE);
        frameLayoutNativeAd = findViewById(R.id.fl_adplaceholder);
        if (BIG_Common_Utils.isShowAppLovinNativeAds()) {
            loadAppLovinNativeAds();
        } else {
            layoutAds.setVisibility(View.GONE);
        }

//        layoutContent.setVisibility(responseModel.getSocialMedia() != null && responseModel.getSocialMedia().size() > 0 ? View.VISIBLE : View.GONE);
        ivLottieNoData.setVisibility(responseModel.getSocialMedia() != null && responseModel.getSocialMedia().size() > 0 ? View.GONE : View.VISIBLE);
        if (responseModel.getSocialMedia() == null && responseModel.getSocialMedia().size() == 0)
            ivLottieNoData.playAnimation();
    }

    private void loadAppLovinNativeAds() {
        try {
            nativeAdLoader = new MaxNativeAdLoader(BIG_Common_Utils.getRandomAdUnitId(responseMain.getLovinNativeID()), Big_GiveAwaySocialActivity.this);
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