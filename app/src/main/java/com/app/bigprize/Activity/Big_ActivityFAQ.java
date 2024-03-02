package com.app.bigprize.Activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.app.bigprize.Adapter.Big_FAQsAdapter;
import com.app.bigprize.Adapter.Big_FAQsChildView;
import com.app.bigprize.Adapter.Big_FAQsParentView;
import com.app.bigprize.Async.Big_FAQ_Data_Async;
import com.app.bigprize.Async.Models.Big_FAQ_ListItem;
import com.app.bigprize.Async.Models.Big_FAQ_Model;
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
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class Big_ActivityFAQ extends AppCompatActivity {
    private RecyclerView rvFAQs;
    private TextView lblLoadingAds;
    private LottieAnimationView ivLottieNoData;
    private Big_Response_Model responseMain;
    private MaxAd nativeAd;
    private MaxNativeAdLoader nativeAdLoader;
    private FrameLayout frameLayoutNativeAd;
    private LinearLayout layoutAds;
    private ImageView ivFeedback;
    private Big_FAQsAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        BIG_Common_Utils.setDayNightTheme(Big_ActivityFAQ.this);
        setContentView(R.layout.big_activity_faq);
        responseMain = new Gson().fromJson(BIG_SharePrefs.getInstance().getString(BIG_SharePrefs.HomeData), Big_Response_Model.class);

        ivLottieNoData = findViewById(R.id.ivLottieNoData);
        rvFAQs = findViewById(R.id.rvFAQs);

        ivFeedback = findViewById(R.id.ivFeedback);
        ivFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Big_ActivityFAQ.this, Big_FeedbackSubmitActivity.class).putExtra("title", "Give Feedback"));
            }
        });

        ImageView imBack = findViewById(R.id.ivBack);
        imBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        new Big_FAQ_Data_Async(Big_ActivityFAQ.this);
    }

    @Override
    public void onBackPressed() {

            super.onBackPressed();

    }

     private Big_FAQ_Model responseModel;
    public void setData(Big_FAQ_Model responseModel1) {
        responseModel = responseModel1;
        if (responseModel.getFAQList() != null && responseModel.getFAQList().size() > 0) {
            BIG_Ads_Utils.showAppLovinInterstitialAd(Big_ActivityFAQ.this, null);

            List<Big_FAQsParentView> listFAQsData = new ArrayList<>();
            for (Big_FAQ_ListItem objFAQ : responseModel.getFAQList()) {
                List<Big_FAQsChildView> answers = new ArrayList<>();
                answers.add(new Big_FAQsChildView(objFAQ.getAnswer()));
                Big_FAQsParentView obj = new Big_FAQsParentView(objFAQ.getQuestion(), answers);
                listFAQsData.add(obj);
            }
            mAdapter = new Big_FAQsAdapter(this, listFAQsData);
            rvFAQs.setAdapter(mAdapter);
            rvFAQs.setLayoutManager(new LinearLayoutManager(this));

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
                    BIG_Common_Utils.loadTopBannerAd(Big_ActivityFAQ.this, layoutTopAds, responseModel.getTopAds());
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
        rvFAQs.setVisibility(responseModel.getFAQList() != null && responseModel.getFAQList().size() > 0 ? View.VISIBLE : View.GONE);
        ivLottieNoData.setVisibility(responseModel.getFAQList() != null && responseModel.getFAQList().size() > 0 ? View.GONE : View.VISIBLE);
        if (responseModel.getFAQList() == null && responseModel.getFAQList().size() == 0)
            ivLottieNoData.playAnimation();
    }

    private void loadAppLovinNativeAds() {
        try {
            nativeAdLoader = new MaxNativeAdLoader(BIG_Common_Utils.getRandomAdUnitId(responseMain.getLovinNativeID()), Big_ActivityFAQ.this);
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
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}