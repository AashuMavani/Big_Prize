package com.app.bigprize.Activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.app.bigprize.Adapter.Big_Withdraw_Types_Adapter;
import com.app.bigprize.Async.Big_Get_Withdraw_Type_Async;
import com.app.bigprize.Async.Models.Big_Home_Slider_Item;
import com.app.bigprize.Async.Models.Big_Response_Model;
import com.app.bigprize.Async.Models.Big_Withdraw_Type;
import com.app.bigprize.Async.Models.Big_Withdraw_Types_Response_Model;
import com.app.bigprize.Customviews.Big_Recyclerview.Big_Pager_Adapter;
import com.app.bigprize.Customviews.Big_Recyclerview.Big_Recycler_ViewPager;
import com.app.bigprize.R;
import com.app.bigprize.Value.BIG_Constants;
import com.app.bigprize.utils.BIG_AppLogger;
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

public class Big_WithdrawTypesActivity extends AppCompatActivity {
    private RecyclerView rvList;
    private List<Big_Withdraw_Type> listWithdrawTypes = new ArrayList<>();
    private TextView tvPoints, lblLoadingAds;
    private LottieAnimationView ivLottieNoData;
    private LinearLayout layoutPoints;
    private ImageView ivHistory;
    private RelativeLayout layoutSlider;
    private Big_Recycler_ViewPager rvSlider;
    private Big_Response_Model responseMain;
    private MaxAd nativeAd;
    private MaxNativeAdLoader nativeAdLoader;
    private FrameLayout frameLayoutNativeAd;
    private LinearLayout layoutAds;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        BIG_Common_Utils.setDayNightTheme(Big_WithdrawTypesActivity.this);
        setContentView(R.layout.big_activity_withdraw_types);
        responseMain = new Gson().fromJson(BIG_SharePrefs.getInstance().getString(BIG_SharePrefs.HomeData), Big_Response_Model.class);
        BIG_AppLogger.getInstance().e("ABC",""+responseMain);

//        layoutPoints = findViewById(R.id.layoutPoints);
//        layoutPoints.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (BIG_SharePrefs.getInstance().getBoolean(BIG_SharePrefs.IS_LOGIN)) {
//                    startActivity(new Intent(Big_WithdrawTypesActivity.this, Big_My_Wallet_Activity.class));
//                } else {
//                    BIG_Common_Utils.NotifyLogin(Big_WithdrawTypesActivity.this);
//                }
//            }
//        });

        ivHistory = findViewById(R.id.ivHistory);
        //  Common_Utils.startRoundAnimation(WithdrawTypesActivity.this, ivHistory);
        ivHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (BIG_SharePrefs.getInstance().getBoolean(BIG_SharePrefs.IS_LOGIN)) {
                    Intent intent = new Intent(Big_WithdrawTypesActivity.this, Big_PointHistoryActivity.class);
                    intent.putExtra("type", BIG_Constants.HistoryType.WITHDRAW_HISTORY);
                    intent.putExtra("title", "Withdrawal History");
                    startActivity(intent);
                } else {
                    BIG_Common_Utils.NotifyLogin(Big_WithdrawTypesActivity.this);
                }
            }
        });

        tvPoints = findViewById(R.id.tvPoints);
        tvPoints.setText(BIG_SharePrefs.getInstance().getEarningPointString());

        rvList = findViewById(R.id.rvList);
        ivLottieNoData = findViewById(R.id.ivLottieNoData);

        rvSlider = findViewById(R.id.rvSlider);
        layoutSlider = findViewById(R.id.layoutSlider);

        ImageView imBack = findViewById(R.id.ivBack);
        imBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        new Big_Get_Withdraw_Type_Async(Big_WithdrawTypesActivity.this);
        BIG_AppLogger.getInstance().e("GVDHGDG",""+responseMain);

    }
    @Override
    public void onBackPressed() {

            super.onBackPressed();
    }

    private Big_Withdraw_Types_Response_Model responseModel;

    public void setData(Big_Withdraw_Types_Response_Model responseModel1) {
        responseModel = responseModel1;

        if (responseModel.getType() != null && responseModel.getType().size() > 0) {
            BIG_AppLogger.getInstance().e("SSSSS",""+responseMain);
            tvPoints.setText(BIG_SharePrefs.getInstance().getEarningPointString());
            listWithdrawTypes.addAll(responseModel.getType());
            if (BIG_Common_Utils.isShowAppLovinNativeAds()) {
                if (listWithdrawTypes.size() <= 4) {
                    listWithdrawTypes.add(listWithdrawTypes.size(), new Big_Withdraw_Type());
                } else {
                    for (int i2 = 0; i2 < this.listWithdrawTypes.size(); i2++) {
                        if ((i2 + 1) % 5 == 0) {
                            listWithdrawTypes.add(i2, new Big_Withdraw_Type());
                            break; // add only 1 ad view
                        }
                    }
                }
            }
            Big_Withdraw_Types_Adapter adapter = new Big_Withdraw_Types_Adapter(listWithdrawTypes, Big_WithdrawTypesActivity.this, new Big_Withdraw_Types_Adapter.ClickListener() {
                @Override
                public void onItemClick(int position, View v) {
                    if (listWithdrawTypes.get(position).getIsActive() != null && listWithdrawTypes.get(position).getIsActive().equals("1")) {
                        startActivity(new Intent(Big_WithdrawTypesActivity.this, Big_WithdrawTypesListActivity.class)
                                .putExtra("type", listWithdrawTypes.get(position).getType())
                                .putExtra("title", listWithdrawTypes.get(position).getTitle()));
                    }
                }
            });

            GridLayoutManager mGridLayoutManager = new GridLayoutManager(Big_WithdrawTypesActivity.this, 1);
            mGridLayoutManager.setOrientation(RecyclerView.VERTICAL);
            mGridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    if (adapter.getItemViewType(position) == Big_Withdraw_Types_Adapter.ITEM_AD) {
                        return 1;
                    }
                    return 1;
                }
            });
            rvList.setLayoutManager(mGridLayoutManager);
            rvList.setAdapter(adapter);
        }


        try {
            if (responseModel.getHomeSlider() != null && responseModel.getHomeSlider().size() > 0) {
                layoutSlider.setVisibility(View.VISIBLE);
                rvSlider.setClear();
                rvSlider.addAll((ArrayList<Big_Home_Slider_Item>) responseModel.getHomeSlider());
                rvSlider.start();
                rvSlider.setOnItemClickListener(new Big_Pager_Adapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        BIG_Common_Utils.Redirect(Big_WithdrawTypesActivity.this, responseModel.getHomeSlider().get(position).getScreenNo(), responseModel.getHomeSlider().get(position).getTitle()
                                , responseModel.getHomeSlider().get(position).getUrl(), responseModel.getHomeSlider().get(position).getId(), null, responseModel.getHomeSlider().get(position).getImage());
                    }
                });
            } else {
                layoutSlider.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        layoutAds = findViewById(R.id.layoutAds);
        layoutAds.setVisibility(View.VISIBLE);
        frameLayoutNativeAd = findViewById(R.id.fl_adplaceholder);
        lblLoadingAds = findViewById(R.id.lblLoadingAds);
        if (listWithdrawTypes.isEmpty() && BIG_Common_Utils.isShowAppLovinNativeAds()) {
            loadAppLovinNativeAds();
        } else {
            layoutAds.setVisibility(View.GONE);
        }

        rvList.setVisibility(listWithdrawTypes.isEmpty() ? View.GONE : View.VISIBLE);
        ivLottieNoData.setVisibility(listWithdrawTypes.isEmpty() ? View.VISIBLE : View.GONE);
        if (listWithdrawTypes.isEmpty())
            ivLottieNoData.playAnimation();
    }

    private void loadAppLovinNativeAds() {
        try {
            nativeAdLoader = new MaxNativeAdLoader(BIG_Common_Utils.getRandomAdUnitId(responseMain.getLovinNativeID()), Big_WithdrawTypesActivity.this);
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
    public void onResume() {
        super.onResume();
        updateUserPoints();
    }

    private void updateUserPoints() {
        try {
            tvPoints.setText(BIG_SharePrefs.getInstance().getEarningPointString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}