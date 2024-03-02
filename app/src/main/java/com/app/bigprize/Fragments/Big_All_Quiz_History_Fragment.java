/*
 * Copyright (c) 2021.  Hurricane Development Studios
 */

package com.app.bigprize.Fragments;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieDrawable;
import com.app.bigprize.Adapter.Big_Quiz_AllHistory_Adapter;
import com.app.bigprize.Async.Big_Get_Quiz_Point_History_Async;
import com.app.bigprize.Async.Models.Big_Point_History_Model;
import com.app.bigprize.Async.Models.Big_Quiz_All_History_Item;
import com.app.bigprize.Async.Models.Big_Response_Model;
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



public class Big_All_Quiz_History_Fragment extends Fragment {
    private View view;
    private int pageNo = 1;
    private NestedScrollView nestedScrollView;
    private long numOfPage;
    private TextView lblLoadingAds;
    private LottieAnimationView ivLottieNoData;
    private RecyclerView rvHistoryList;
    private final List<Big_Quiz_All_History_Item> listPointHistory = new ArrayList<>();
    private MaxAd nativeAd;
    private MaxNativeAdLoader nativeAdLoader;
    private FrameLayout frameLayoutNativeAd;
    private LinearLayout layoutAds;
    private Big_Response_Model responseMain;
    private boolean isAdLoaded = false;

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.big_fragment_refer_point_history, container, false);
        }
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        responseMain = new Gson().fromJson(BIG_SharePrefs.getInstance().getString(BIG_SharePrefs.HomeData), Big_Response_Model.class);

        rvHistoryList = view.findViewById(R.id.rvHistoryList);
        rvHistoryList.setAdapter(new Big_Quiz_AllHistory_Adapter(listPointHistory, getActivity()));
        rvHistoryList.setLayoutManager(new LinearLayoutManager(getActivity()));
        ivLottieNoData = view.findViewById(R.id.ivLottieNoData);
        nestedScrollView = view.findViewById(R.id.nestedScrollView);
        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()) {
                    if (pageNo < numOfPage) {
                        new Big_Get_Quiz_Point_History_Async(getActivity(), BIG_Constants.HistoryType.QUIZ_ALL_CONTEST, String.valueOf(pageNo + 1));
                    }
                }
            }
        });
        new Big_Get_Quiz_Point_History_Async(getActivity(), BIG_Constants.HistoryType.QUIZ_ALL_CONTEST, String.valueOf(pageNo));
    }

    public void setData(Big_Point_History_Model responseModel) {
        if (responseModel != null && responseModel.getQuizAllHistoryList() != null && responseModel.getQuizAllHistoryList().size() > 0) {
            int prevItemCount = listPointHistory.size();
            if (!isAdLoaded && responseModel.getIsShowInterstitial() != null && responseModel.getIsShowInterstitial().equals(BIG_Constants.APPLOVIN_INTERSTITIAL)) {
                BIG_Ads_Utils.showAppLovinInterstitialAd(getActivity(), null);
            } else if (!isAdLoaded && responseModel.getIsShowInterstitial() != null && responseModel.getIsShowInterstitial().equals(BIG_Constants.APPLOVIN_REWARD)) {
                BIG_Ads_Utils.showAppLovinRewardedAd(getActivity(), null);
            }
            listPointHistory.addAll(responseModel.getQuizAllHistoryList());
            if (prevItemCount == 0) {
                rvHistoryList.getAdapter().notifyDataSetChanged();
            } else {
                rvHistoryList.getAdapter().notifyItemRangeInserted(prevItemCount, responseModel.getQuizAllHistoryList().size());
            }

            numOfPage = responseModel.getTotalPage();
            pageNo = Integer.parseInt(responseModel.getCurrentPage());

            if (!isAdLoaded) {
                // Load home note webview top
                try {
                    if (!BIG_Common_Utils.isStringNullOrEmpty(responseModel.getHomeNote())) {
                        WebView webNote = view.findViewById(R.id.webNote);
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
                        LinearLayout layoutTopAds = view.findViewById(R.id.layoutTopAds);
                        BIG_Common_Utils.loadTopBannerAd(getActivity(), layoutTopAds,  responseModel.getTopAds());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (responseModel.getMiniAds() != null && !BIG_Common_Utils.isStringNullOrEmpty(responseModel.getMiniAds().getImage()) && (BIG_SharePrefs.getInstance().getString(BIG_SharePrefs.pointHistoryMiniAdShownDate + responseModel.getMiniAds().getId()).length() == 0 || !BIG_SharePrefs.getInstance().getString(BIG_SharePrefs.pointHistoryMiniAdShownDate + responseModel.getMiniAds().getId()).equals(BIG_Common_Utils.getCurrentDate()))) {
                    try {
                        RelativeLayout layoutMiniAd = view.findViewById(R.id.layoutMiniAd);
                        ProgressBar progressBar = view.findViewById(R.id.progressBar);
                        if (responseModel.getMiniAds() != null) {
                            if (responseModel.getMiniAds().getImage().endsWith(".json")) {
                                LottieAnimationView ivLottieViewMiniAd = view.findViewById(R.id.ivLottieViewMiniAd);
                                ivLottieViewMiniAd.setVisibility(View.VISIBLE);
                                BIG_Common_Utils.setLottieAnimation(ivLottieViewMiniAd, responseModel.getMiniAds().getImage());
                                ivLottieViewMiniAd.setRepeatCount(LottieDrawable.INFINITE);
                                ivLottieViewMiniAd.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        BIG_Common_Utils.Redirect(getActivity(), responseModel.getMiniAds().getScreenNo(), responseModel.getMiniAds().getTitle(), responseModel.getMiniAds().getUrl(), responseModel.getMiniAds().getId(), responseModel.getMiniAds().getTaskId(), responseModel.getMiniAds().getImage());
                                    }
                                });
                                progressBar.setVisibility(View.GONE);
                            } else {
                                ImageView ivMiniAd = view.findViewById(R.id.ivMiniAd);
                                ivMiniAd.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        BIG_Common_Utils.Redirect(getActivity(), responseModel.getMiniAds().getScreenNo(), responseModel.getMiniAds().getTitle(), responseModel.getMiniAds().getUrl(), responseModel.getMiniAds().getId(), responseModel.getMiniAds().getTaskId(), responseModel.getMiniAds().getImage());
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
                            ImageView ivCloseMiniAd = view.findViewById(R.id.ivCloseMiniAd);
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
        }
        if (listPointHistory.isEmpty()) {
            layoutAds = view.findViewById(R.id.layoutAds);
            layoutAds.setVisibility(View.VISIBLE);
            frameLayoutNativeAd = view.findViewById(R.id.fl_adplaceholder);
            lblLoadingAds = view.findViewById(R.id.lblLoadingAds);
            if (BIG_Common_Utils.isShowAppLovinNativeAds()) {
                loadAppLovinNativeAds();
            } else {
                layoutAds.setVisibility(View.GONE);
            }
        }
        rvHistoryList.setVisibility(listPointHistory.isEmpty() ? View.GONE : View.VISIBLE);
        ivLottieNoData.setVisibility(listPointHistory.isEmpty() ? View.VISIBLE : View.GONE);
        if (listPointHistory.isEmpty() )
            ivLottieNoData.playAnimation();
        // Load Bottom banner ad
        try {
            if (!listPointHistory.isEmpty() && listPointHistory.size() < 5) {
                LinearLayout layoutBannerAdBottom = view.findViewById(R.id.layoutBannerAdBottom);
                layoutBannerAdBottom.setVisibility(View.VISIBLE);
                TextView lblAdSpaceBottom = view.findViewById(R.id.lblAdSpaceBottom);
                BIG_Common_Utils.loadBannerAds(getActivity(), layoutBannerAdBottom, lblAdSpaceBottom);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void loadAppLovinNativeAds() {
        try {
                nativeAdLoader = new MaxNativeAdLoader(BIG_Common_Utils.getRandomAdUnitId(responseMain.getLovinNativeID()), getActivity());
                nativeAdLoader.setNativeAdListener(new MaxNativeAdListener() {
                    @Override
                    public void onNativeAdLoaded(final MaxNativeAdView nativeAdView, final MaxAd ad) {
                        frameLayoutNativeAd = view.findViewById(R.id.fl_adplaceholder);
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
    public void onDestroyView() {
        super.onDestroyView();
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
