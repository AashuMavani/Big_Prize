package com.app.bigprize.Activity;

import static android.view.View.VISIBLE;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieDrawable;
import com.app.bigprize.Adapter.Big_AdjoeLeaderboardListAdapter;
import com.app.bigprize.Async.Big_GetAdjoeLeaderboardDataAsync;
import com.app.bigprize.Async.Models.Big_AdjoeLeaderboardItem;
import com.app.bigprize.Async.Models.Big_AdjoeLeaderboardResponseModel;
import com.app.bigprize.Async.Models.Big_Response_Model;
import com.app.bigprize.R;
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
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class Big_AdjoeLeaderboardActivity extends AppCompatActivity {
    private RecyclerView rvHistoryList;
    private List<Big_AdjoeLeaderboardItem> listLeaderboard = new ArrayList<>();
    private TextView lblLoadingAds, tvTimer;
    private LottieAnimationView ivLottieNoData;
    private Big_Response_Model responseMain;
    private MaxAd nativeAd;
    private MaxNativeAdLoader nativeAdLoader;
    private FrameLayout frameLayoutNativeAd;
    private LinearLayout layoutAds, layoutWinner;
    private String todayDate, endDate;
    private CountDownTimer timer;
    private int time;
    private NestedScrollView nestedScrollView;
    private Button lInstallBtn;
    private View viewShine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        BIG_Common_Utils.setDayNightTheme(Big_AdjoeLeaderboardActivity.this);
        setContentView(R.layout.big_activity_adjoe_leaderboard);
        responseMain = new Gson().fromJson(BIG_SharePrefs.getInstance().getString(BIG_SharePrefs.HomeData), Big_Response_Model.class);

        lInstallBtn = findViewById(R.id.lInstallBtn);
        lInstallBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (BIG_SharePrefs.getInstance().getBoolean(BIG_SharePrefs.IS_LOGIN)) {
                    BIG_Common_Utils.openAdjoeOfferWall(Big_AdjoeLeaderboardActivity.this);
                } else {
                    BIG_Common_Utils.NotifyLogin(Big_AdjoeLeaderboardActivity.this);
                }
            }
        });

        viewShine = findViewById(R.id.viewShine);
        Animation animUpDown = AnimationUtils.loadAnimation(Big_AdjoeLeaderboardActivity.this, R.anim.big_left_right);
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
        nestedScrollView = findViewById(R.id.nestedScrollView);
        nestedScrollView.setVisibility(View.INVISIBLE);
        layoutWinner = findViewById(R.id.layoutWinner);
        rvHistoryList = findViewById(R.id.rvHistoryList);
        rvHistoryList.setLayoutManager(new LinearLayoutManager(this));
        tvTimer = findViewById(R.id.tvTimer);
        ivLottieNoData = findViewById(R.id.ivLottieNoData);

        ImageView imBack = findViewById(R.id.ivBack);
        imBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        ImageView ivHistory = findViewById(R.id.ivHistory);
        ivHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Big_AdjoeLeaderboardActivity.this, Big_AdjoeLeaderboardHistoryActivity.class));
            }
        });

        new Big_GetAdjoeLeaderboardDataAsync(Big_AdjoeLeaderboardActivity.this);
    }


    @Override
    public void onBackPressed() {

            super.onBackPressed();

    }

   private Big_AdjoeLeaderboardResponseModel responseModel;
    public void setData(Big_AdjoeLeaderboardResponseModel responseModel1) {
        responseModel=responseModel1;


        nestedScrollView.setVisibility(VISIBLE);
        BIG_Ads_Utils.showAppLovinInterstitialAd(Big_AdjoeLeaderboardActivity.this, null);
        if (responseModel.getBtnColor() != null && responseModel.getBtnColor().length() > 0) {
            Drawable mDrawable = ContextCompat.getDrawable(Big_AdjoeLeaderboardActivity.this, R.drawable.big_ic_btn_gradient_rounded_corner_rect_new);
            mDrawable.setColorFilter(new PorterDuffColorFilter(Color.parseColor(responseModel.getBtnColor()), PorterDuff.Mode.SRC_IN));
            lInstallBtn.setBackground(mDrawable);
            lInstallBtn.setTextColor(Color.parseColor(responseModel.getBtnTextColor()));
        }


        if (responseModel.getBtnName() != null) {
            lInstallBtn.setText(responseModel.getBtnName());
        }

        if (responseModel.getTodayDate() != null) {
            todayDate = responseModel.getTodayDate();
        }
        if (responseModel.getEndDate() != null) {
            endDate = responseModel.getEndDate();
        }
        setTimer();
        if (responseModel.getData() != null && responseModel.getData().size() > 0) {
            ImageView ivIcon;
            ProgressBar probr;
            TextView tvName, tvPoints, tvWinPoints;
            listLeaderboard.clear();
            listLeaderboard.addAll(responseModel.getData());

            ivIcon = findViewById(R.id.ivIcon1);
            probr = findViewById(R.id.probr1);
            tvName = findViewById(R.id.tvName1);
            tvPoints = findViewById(R.id.tvPoints1);
            tvWinPoints = findViewById(R.id.tvWinPoints1);
            tvWinPoints.setText(responseModel.getWinPoint1());
            setWinnerData(listLeaderboard.get(0), ivIcon, probr, tvName, tvPoints);
            listLeaderboard.remove(0);
            if (listLeaderboard.size() < 1) {
                LinearLayout layoutWinner2 = findViewById(R.id.layoutWinner2);
                layoutWinner2.setVisibility(View.INVISIBLE);
            } else {
                ivIcon = findViewById(R.id.ivIcon2);
                probr = findViewById(R.id.probr2);
                tvName = findViewById(R.id.tvName2);
                tvPoints = findViewById(R.id.tvPoints2);
                tvWinPoints = findViewById(R.id.tvWinPoints2);
                tvWinPoints.setText(responseModel.getWinPoint2());
                setWinnerData(listLeaderboard.get(0), ivIcon, probr, tvName, tvPoints);
                listLeaderboard.remove(0);
            }
            if (listLeaderboard.size() < 1) {
                LinearLayout layoutWinner3 = findViewById(R.id.layoutWinner3);
                layoutWinner3.setVisibility(View.INVISIBLE);
            } else {
                ivIcon = findViewById(R.id.ivIcon3);
                probr = findViewById(R.id.probr3);
                tvName = findViewById(R.id.tvName3);
                tvPoints = findViewById(R.id.tvPoints3);
                tvWinPoints = findViewById(R.id.tvWinPoints3);
                tvWinPoints.setText(responseModel.getWinPoint3());
                setWinnerData(listLeaderboard.get(0), ivIcon, probr, tvName, tvPoints);
                listLeaderboard.remove(0);
            }

            rvHistoryList.setAdapter(new Big_AdjoeLeaderboardListAdapter(listLeaderboard, Big_AdjoeLeaderboardActivity.this));


            // Load home note webview top
            try {
                if (!BIG_Common_Utils.isStringNullOrEmpty(responseModel.getHomeNote())) {
                    WebView webNote = findViewById(R.id.webNote);
                    webNote.getSettings().setJavaScriptEnabled(true);
                    webNote.setVisibility(VISIBLE);
                    webNote.loadDataWithBaseURL(null, responseModel.getHomeNote(), "text/html", "UTF-8", null);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Load top ad
            try {
                if (responseModel.getTopAds() != null && !BIG_Common_Utils.isStringNullOrEmpty(responseModel.getTopAds().getImage())) {
                    LinearLayout layoutTopAds = findViewById(R.id.layoutTopAds);
                    BIG_Common_Utils.loadTopBannerAd(Big_AdjoeLeaderboardActivity.this, layoutTopAds, responseModel.getTopAds());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            // mini ads
            if (responseModel.getMiniAds() != null && !BIG_Common_Utils.isStringNullOrEmpty(responseModel.getMiniAds().getImage()) && (BIG_SharePrefs.getInstance().getString(BIG_SharePrefs.pointHistoryMiniAdShownDate + responseModel.getMiniAds().getId()).length() == 0 || !BIG_SharePrefs.getInstance().getString(BIG_SharePrefs.pointHistoryMiniAdShownDate + responseModel.getMiniAds().getId()).equals(BIG_Common_Utils.getCurrentDate()))) {
                try {
                    RelativeLayout layoutMiniAd = findViewById(R.id.layoutMiniAd);
                    ProgressBar progressBar = findViewById(R.id.progressBar);
                    if (responseModel.getMiniAds() != null) {
                        if (responseModel.getMiniAds().getImage().endsWith(".json")) {
                            LottieAnimationView ivLottieViewMiniAd = findViewById(R.id.ivLottieViewMiniAd);
                            ivLottieViewMiniAd.setVisibility(VISIBLE);
                            BIG_Common_Utils.setLottieAnimation(ivLottieViewMiniAd, responseModel.getMiniAds().getImage());
                            ivLottieViewMiniAd.setRepeatCount(LottieDrawable.INFINITE);
                            ivLottieViewMiniAd.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    BIG_Common_Utils.Redirect(Big_AdjoeLeaderboardActivity.this, responseModel.getMiniAds().getScreenNo(), responseModel.getMiniAds().getTitle(), responseModel.getMiniAds().getUrl(), responseModel.getMiniAds().getId(), responseModel.getMiniAds().getTaskId(), responseModel.getMiniAds().getImage());
                                }
                            });
                            progressBar.setVisibility(View.GONE);
                        } else {
                            ImageView ivMiniAd = findViewById(R.id.ivMiniAd);
                            ivMiniAd.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    BIG_Common_Utils.Redirect(Big_AdjoeLeaderboardActivity.this, responseModel.getMiniAds().getScreenNo(), responseModel.getMiniAds().getTitle(), responseModel.getMiniAds().getUrl(), responseModel.getMiniAds().getId(), responseModel.getMiniAds().getTaskId(), responseModel.getMiniAds().getImage());
                                }
                            });
                            Glide.with(this)
                                    .load(responseModel.getMiniAds().getImage())
                                    .override(getResources().getDimensionPixelSize(com.intuit.sdp.R.dimen._140sdp), getResources().getDimensionPixelSize(com.intuit.sdp.R.dimen._200sdp))
                                    .apply(new RequestOptions().transforms(new CenterCrop(), new RoundedCorners(getResources().getDimensionPixelSize(R.dimen.dim_5))))
                                    .addListener(new RequestListener<Drawable>() {
                                        @Override
                                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                            return false;
                                        }

                                        @Override
                                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                            progressBar.setVisibility(View.GONE);
                                            ivMiniAd.setVisibility(VISIBLE);
                                            return false;
                                        }
                                    }).into(ivMiniAd);
                        }
                        ImageView ivCloseMiniAd = findViewById(R.id.ivCloseMiniAd);
                        ivCloseMiniAd.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                layoutMiniAd.setVisibility(View.GONE);
                            }
                        });
                        layoutMiniAd.setVisibility(VISIBLE);
                        BIG_SharePrefs.getInstance().putString(BIG_SharePrefs.pointHistoryMiniAdShownDate + responseModel.getMiniAds().getId(), BIG_Common_Utils.getCurrentDate());
                    } else {
                        layoutMiniAd.setVisibility(View.GONE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            lblLoadingAds = findViewById(R.id.lblLoadingAds);
            layoutAds = findViewById(R.id.layoutAds);
            layoutAds.setVisibility(VISIBLE);
            frameLayoutNativeAd = findViewById(R.id.fl_adplaceholder);
            if (BIG_Common_Utils.isShowAppLovinNativeAds()) {
                loadAppLovinNativeAds();
            } else {
                layoutAds.setVisibility(View.GONE);
            }
        }
        try {
            layoutWinner.setVisibility(responseModel.getData() == null || responseModel.getData().isEmpty() ? View.GONE : VISIBLE);
            rvHistoryList.setVisibility(responseModel.getData() == null || responseModel.getData().isEmpty() ? View.GONE : VISIBLE);
            ivLottieNoData.setVisibility(responseModel.getData() == null || responseModel.getData().isEmpty() ? VISIBLE : View.GONE);

            if (responseModel.getData() == null ||responseModel.getData().isEmpty())
                ivLottieNoData.playAnimation();

            // Load Bottom banner ad
            if (responseModel.getData() != null && !responseModel.getData().isEmpty() && responseModel.getData().size() < 5) {
                LinearLayout layoutBannerAdBottom = findViewById(R.id.layoutBannerAdBottom);
                layoutBannerAdBottom.setVisibility(VISIBLE);
                TextView lblAdSpaceBottom = findViewById(R.id.lblAdSpaceBottom);
                BIG_Common_Utils.loadBannerAds(Big_AdjoeLeaderboardActivity.this, layoutBannerAdBottom, lblAdSpaceBottom);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setWinnerData(Big_AdjoeLeaderboardItem adjoeLeaderboardItem, ImageView ivIcon, ProgressBar probr, TextView tvName, TextView tvPoints) {
        try {
            if (!BIG_Common_Utils.isStringNullOrEmpty(adjoeLeaderboardItem.getProfileImage())) {
                Glide.with(Big_AdjoeLeaderboardActivity.this)
                        .load(adjoeLeaderboardItem.getProfileImage())
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                probr.setVisibility(View.GONE);
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                probr.setVisibility(View.GONE);
                                return false;
                            }
                        })
                        .into(ivIcon);
            } else {
                ivIcon.setImageResource(0);
                probr.setVisibility(View.GONE);
            }
            tvPoints.setText(adjoeLeaderboardItem.getPoints());
            tvName.setText(adjoeLeaderboardItem.getFirstName() + " " + adjoeLeaderboardItem.getLastName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadAppLovinNativeAds() {
        try {
            nativeAdLoader = new MaxNativeAdLoader(BIG_Common_Utils.getRandomAdUnitId(responseMain.getLovinNativeID()), Big_AdjoeLeaderboardActivity.this);
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
                    layoutAds.setVisibility(VISIBLE);


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

    public void setTimer() {
        try {
            if (timer != null) {
                timer.cancel();
            }
            time = BIG_Common_Utils.timeDiff(endDate, todayDate);
            timer = new CountDownTimer(time * 60000L, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    tvTimer.setText(BIG_Common_Utils.updateTimeRemainingLuckyNumber(millisUntilFinished));
                }

                @Override
                public void onFinish() {
                }
            }.start();
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
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}