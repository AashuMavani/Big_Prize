package com.app.bigprize.Activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieDrawable;
import com.app.bigprize.Adapter.Big_Point_History_Adapter;
import com.app.bigprize.Adapter.Big_Withdraw_Point_History_Adapter;
import com.app.bigprize.Async.Big_Get_Point_History_Async;
import com.app.bigprize.Async.Models.Big_Point_History_Model;
import com.app.bigprize.Async.Models.Big_Response_Model;
import com.app.bigprize.Async.Models.Big_Wallet_ListItem;
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
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class Big_PointHistoryActivity extends AppCompatActivity {
    private RecyclerView rvHistoryList;
    private List<Big_Wallet_ListItem> listPointHistory = new ArrayList<>();
    private TextView lblLoadingAds, tvTitle, tvPoints;
    private LottieAnimationView ivLottieNoData;
    private Big_Response_Model responseMain;
    private MaxAd nativeAd;
    private MaxNativeAdLoader nativeAdLoader;
    private FrameLayout frameLayoutNativeAd;
    private LinearLayout layoutAds;
    private int pageNo = 1;
    private String type = BIG_Constants.HistoryType.ALL;
    private NestedScrollView nestedScrollView;
    private long numOfPage;
    private boolean isAdLoaded = false;
    private int selectedPos = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        BIG_Common_Utils.setDayNightTheme(Big_PointHistoryActivity.this);
        setContentView(R.layout.big_activity_point_history);


        responseMain = new Gson().fromJson(BIG_SharePrefs.getInstance().getString(BIG_SharePrefs.HomeData), Big_Response_Model.class);
        tvTitle = findViewById(R.id.tvTitle);


        if (getIntent().getExtras() != null && getIntent().getExtras().containsKey("type")) {
            type = getIntent().getStringExtra("type");
            tvTitle.setText(getIntent().getStringExtra("title"));
        }
        rvHistoryList = findViewById(R.id.rvHistoryList);
        if (type.equals(BIG_Constants.HistoryType.WITHDRAW_HISTORY)||type.equals(BIG_Constants.HistoryType.SCAN_AND_PAY)) {
            rvHistoryList.setAdapter(new Big_Withdraw_Point_History_Adapter(listPointHistory, Big_PointHistoryActivity.this,new Big_Withdraw_Point_History_Adapter.ClickListener(){
                @Override
                public void onItemClick(int position, View v) {

                }
                @Override
                public void onSeeDetailsClick(int position, View v) {
                    try {
                        if (listPointHistory.get(position).getWithdraw_type().equals(BIG_Constants.UPI_EARNING_TYPE)) {
                            startActivity(new Intent(Big_PointHistoryActivity.this, Big_ScanAndPayDetails_Activity.class)
                                    .putExtra("withdrawID", listPointHistory.get(position).getId()));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onCopyButtonClicked(int position, View v) {
                    String val = listPointHistory.get(position).getCouponeCode();
                    if (val != null) {
                        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                        ClipData clip = ClipData.newPlainText("Copied Text", val);
                        clipboard.setPrimaryClip(clip);
                        BIG_Common_Utils.setToast(Big_PointHistoryActivity.this, "Copied!");
                        BIG_Ads_Utils.showAppLovinRewardedAd(Big_PointHistoryActivity.this, null);
                    }
                }

                @Override
                public void onRaiseTicketButtonClicked(int position, View v) {
                    try {
                        selectedPos = position;
                        if (!BIG_Common_Utils.isStringNullOrEmpty(listPointHistory.get(position).getRaisedTicketId()) && !listPointHistory.get(position).getRaisedTicketId().equals("0")) {
                            startActivityForResult(new Intent(Big_PointHistoryActivity.this, Big_FeedbackSubmitActivity.class)
                                    .putExtra("withdrawId", listPointHistory.get(position).getId())
                                    .putExtra("transactionId", listPointHistory.get(position).getTxnID())
                                    .putExtra("title", "Check Ticket Status")
                                    .putExtra("ticketId", listPointHistory.get(position).getRaisedTicketId()), 1000);
                        } else {
                            startActivityForResult(new Intent(Big_PointHistoryActivity.this, Big_FeedbackSubmitActivity.class)
                                    .putExtra("withdrawId", listPointHistory.get(position).getId())
                                    .putExtra("transactionId", listPointHistory.get(position).getTxnID())
                                    .putExtra("title", "Raise a Ticket"), 1000);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }


            }));
        } else {
            rvHistoryList.setAdapter(new Big_Point_History_Adapter(listPointHistory, Big_PointHistoryActivity.this, type));
        }
        rvHistoryList.setLayoutManager(new LinearLayoutManager(this));

        ivLottieNoData = findViewById(R.id.ivLottieNoData);
        tvPoints = findViewById(R.id.tvPoints);
        tvPoints.setText(BIG_SharePrefs.getInstance().getEarningPointString());
        ImageView imBack = findViewById(R.id.ivBack);
        imBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        new Big_Get_Point_History_Async(Big_PointHistoryActivity.this, type, String.valueOf(pageNo));

//        BIG_AppLogger.getInstance().e("aaaaaaaaaa",""+type);

        nestedScrollView = findViewById(R.id.nestedScrollView);
        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()) {
                    if (pageNo < numOfPage) {
                        new Big_Get_Point_History_Async(Big_PointHistoryActivity.this, type, String.valueOf(pageNo + 1));
                    }
                }
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == 1000 && resultCode == RESULT_OK) {
                listPointHistory.get(selectedPos).setRaisedTicketId(data.getStringExtra("ticketId"));
                rvHistoryList.getAdapter().notifyItemChanged(selectedPos);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        selectedPos = -1;
    }


    @Override
    public void onBackPressed() {

            super.onBackPressed();

    }


    public void setData(Big_Point_History_Model responseModel) {

        if ((responseModel.getWalletList() != null && responseModel.getWalletList().size() > 0) || (responseModel.getData() != null && responseModel.getData().size() > 0)) {
            int prevItemCount = listPointHistory.size();

            if (!isAdLoaded && responseModel.getIsShowInterstitial() != null && responseModel.getIsShowInterstitial().equals(BIG_Constants.APPLOVIN_INTERSTITIAL)) {
                BIG_Ads_Utils.showAppLovinInterstitialAd(Big_PointHistoryActivity.this, null);
            } else if (!isAdLoaded && responseModel.getIsShowInterstitial() != null && responseModel.getIsShowInterstitial().equals(BIG_Constants.APPLOVIN_REWARD)) {
                BIG_Ads_Utils.showAppLovinRewardedAd(Big_PointHistoryActivity.this, null);
            }
            if (type.equals(BIG_Constants.HistoryType.WITHDRAW_HISTORY) && responseModel.getData() != null && responseModel.getData().size() > 0) {
                listPointHistory.addAll(responseModel.getData());
                if (prevItemCount == 0) {
                    rvHistoryList.getAdapter().notifyDataSetChanged();
                } else {
                    rvHistoryList.getAdapter().notifyItemRangeInserted(prevItemCount, responseModel.getData().size());
                }
            } else {
                listPointHistory.addAll(responseModel.getWalletList());
                if (prevItemCount == 0) {
                    rvHistoryList.getAdapter().notifyDataSetChanged();
                } else {
                    rvHistoryList.getAdapter().notifyItemRangeInserted(prevItemCount, responseModel.getWalletList().size());
                }
            }
            numOfPage = responseModel.getTotalPage();
            pageNo = Integer.parseInt(responseModel.getCurrentPage());
            if (!isAdLoaded) {
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
                        BIG_Common_Utils.loadTopBannerAd(Big_PointHistoryActivity.this, layoutTopAds, responseModel.getTopAds());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (responseModel.getMiniAds() != null && !BIG_Common_Utils.isStringNullOrEmpty(responseModel.getMiniAds().getImage()) && (BIG_SharePrefs.getInstance().getString(BIG_SharePrefs.pointHistoryMiniAdShownDate + responseModel.getMiniAds().getId()).length() == 0 || !BIG_SharePrefs.getInstance().getString(BIG_SharePrefs.pointHistoryMiniAdShownDate + responseModel.getMiniAds().getId()).equals(BIG_Common_Utils.getCurrentDate()))) {
                    try {
                        RelativeLayout layoutMiniAd = findViewById(R.id.layoutMiniAd);
                        ProgressBar progressBar = findViewById(R.id.progressBar);
                        if (responseModel.getMiniAds() != null) {
                            if (responseModel.getMiniAds().getImage().endsWith(".json")) {
                                LottieAnimationView ivLottieViewMiniAd = findViewById(R.id.ivLottieViewMiniAd);
                                ivLottieViewMiniAd.setVisibility(android.view.View.VISIBLE);
                                BIG_Common_Utils.setLottieAnimation(ivLottieViewMiniAd, responseModel.getMiniAds().getImage());
                                ivLottieViewMiniAd.setRepeatCount(LottieDrawable.INFINITE);
                                ivLottieViewMiniAd.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        BIG_Common_Utils.Redirect(Big_PointHistoryActivity.this, responseModel.getMiniAds().getScreenNo(), responseModel.getMiniAds().getTitle(), responseModel.getMiniAds().getUrl(), responseModel.getMiniAds().getId(), responseModel.getMiniAds().getTaskId(), responseModel.getMiniAds().getImage());
                                    }
                                });
                                progressBar.setVisibility(View.GONE);
                            } else {
                                ImageView ivMiniAd = findViewById(R.id.ivMiniAd);
                                ivMiniAd.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        BIG_Common_Utils.Redirect(Big_PointHistoryActivity.this, responseModel.getMiniAds().getScreenNo(), responseModel.getMiniAds().getTitle(), responseModel.getMiniAds().getUrl(), responseModel.getMiniAds().getId(), responseModel.getMiniAds().getTaskId(), responseModel.getMiniAds().getImage());
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
                                                ivMiniAd.setVisibility(View.VISIBLE);
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
                            layoutMiniAd.setVisibility(View.VISIBLE);
                            BIG_SharePrefs.getInstance().putString(BIG_SharePrefs.pointHistoryMiniAdShownDate + responseModel.getMiniAds().getId(), BIG_Common_Utils.getCurrentDate());
                        } else {
                            layoutMiniAd.setVisibility(View.GONE);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            isAdLoaded = true;
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
        rvHistoryList.setVisibility(listPointHistory.isEmpty() ? View.GONE : View.VISIBLE);
        ivLottieNoData.setVisibility(listPointHistory.isEmpty() ? View.VISIBLE : View.GONE);
        if (listPointHistory.isEmpty())
            ivLottieNoData.playAnimation();
        // Load Bottom banner ad
        try {
            if (!listPointHistory.isEmpty() && listPointHistory.size() < 5) {
                LinearLayout layoutBannerAdBottom = findViewById(R.id.layoutBannerAdBottom);
                layoutBannerAdBottom.setVisibility(View.VISIBLE);
                TextView lblAdSpaceBottom = findViewById(R.id.lblAdSpaceBottom);
                BIG_Common_Utils.loadBannerAds(Big_PointHistoryActivity.this, layoutBannerAdBottom, lblAdSpaceBottom);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadAppLovinNativeAds() {
        try {
            nativeAdLoader = new MaxNativeAdLoader(BIG_Common_Utils.getRandomAdUnitId(responseMain.getLovinNativeID()), Big_PointHistoryActivity.this);
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
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}