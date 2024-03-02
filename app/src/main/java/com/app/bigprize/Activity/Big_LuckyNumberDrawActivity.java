package com.app.bigprize.Activity;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.app.bigprize.Adapter.Big_Lucky_Number_Adapter;
import com.app.bigprize.Async.Big_Get_Lucky_Number_Async;
import com.app.bigprize.Async.Models.Big_LuckyNumberItem;
import com.app.bigprize.Async.Models.Big_Lucky_Number_Data_Model;
import com.app.bigprize.Async.Models.Big_Response_Model;
import com.app.bigprize.Async.Big_Save_Lucky_Number_Async;
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

public class Big_LuckyNumberDrawActivity extends AppCompatActivity {
    private RecyclerView rvLuckyNumbers;
    private LinearLayout layoutPoints, layoutNoData;
    private RelativeLayout layoutContent;
    private ImageView ivHistory, ivHelp;
    private ArrayList<Big_LuckyNumberItem> listData = new ArrayList<>();
    private TextView tvPoints, lblLoadingAds, lblTitle, lblSubTitle, tvWinningPoints, tvSelectedNumbers, tvTimer, tvContestId;
    private String todayDate, endDate;
    private CountDownTimer timer;
    private int time;
    private Big_Response_Model responseMain;
    private MaxAd nativeAd;
    private MaxNativeAdLoader nativeAdLoader;
    private LinearLayout layoutAds;
    private FrameLayout frameLayoutNativeAd;
    private int selectedNumber1 = 0, selectedNumber2 = 0;
    private Big_Lucky_Number_Adapter luckyNumberAdapter;
    private Big_Lucky_Number_Data_Model objRewardScreenModel;
    private AppCompatButton btnSubmit;
    private LottieAnimationView ivLottieNoData;
    private boolean isEdit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        BIG_Common_Utils.setDayNightTheme(Big_LuckyNumberDrawActivity.this);
        setContentView(R.layout.big_activity_lucky_number_draw);

        responseMain = new Gson().fromJson(BIG_SharePrefs.getInstance().getString(BIG_SharePrefs.HomeData), Big_Response_Model.class);
        btnSubmit = findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    //AppLogger.getInstance().e("SELECTED NUMBERS", "===" + selectedNumber1 + "===" + selectedNumber2);
                    if (BIG_SharePrefs.getInstance().getBoolean(BIG_SharePrefs.IS_LOGIN)) {
                        if (selectedNumber1 > 0 && selectedNumber2 > 0) {
                            BIG_Ads_Utils.showAppLovinInterstitialAd(Big_LuckyNumberDrawActivity.this, new BIG_Ads_Utils.AdShownListener() {
                                @Override
                                public void onAdDismiss() {
                                    new Big_Save_Lucky_Number_Async(Big_LuckyNumberDrawActivity.this, String.valueOf(selectedNumber1), String.valueOf(selectedNumber2), objRewardScreenModel.getContestId());
                                }
                            });
                        } else {
                            BIG_Common_Utils.setToast(Big_LuckyNumberDrawActivity.this, "Please select 2 numbers");
                        }
                    } else {
                        BIG_Common_Utils.NotifyLogin(Big_LuckyNumberDrawActivity.this);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        rvLuckyNumbers = findViewById(R.id.rvLuckyNumbers);
        ivLottieNoData = findViewById(R.id.ivLottieNoData);
        layoutNoData = findViewById(R.id.layoutNoData);
        layoutContent = findViewById(R.id.layoutContent);
        layoutContent.setVisibility(View.INVISIBLE);
        lblTitle = findViewById(R.id.lblTitle);
        tvSelectedNumbers = findViewById(R.id.tvSelectedNumbers);
        tvWinningPoints = findViewById(R.id.tvWinningPoints);
        lblSubTitle = findViewById(R.id.lblSubTitle);
        tvTimer = findViewById(R.id.tvTimer);
        tvContestId = findViewById(R.id.tvContestId);
        ivHelp = findViewById(R.id.ivHelp);
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
        tvPoints = findViewById(R.id.tvPoints);
        tvPoints.setText(BIG_SharePrefs.getInstance().getEarningPointString());
        layoutPoints = findViewById(R.id.layoutPoints);
        layoutPoints.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (BIG_SharePrefs.getInstance().getBoolean(BIG_SharePrefs.IS_LOGIN)) {
                    startActivity(new Intent(Big_LuckyNumberDrawActivity.this, Big_My_Wallet_Activity.class));
                } else {
                    BIG_Common_Utils.NotifyLogin(Big_LuckyNumberDrawActivity.this);
                }
            }
        });

        ivHistory = findViewById(R.id.ivHistory);
        //      Common_Utils.startRoundAnimation(LuckyNumberDrawActivity.this, ivHistory);
        ivHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (BIG_SharePrefs.getInstance().getBoolean(BIG_SharePrefs.IS_LOGIN)) {
                    startActivity(new Intent(Big_LuckyNumberDrawActivity.this, Big_LuckyNumberDrawHistoryActivity.class));
                } else {
                    BIG_Common_Utils.NotifyLogin(Big_LuckyNumberDrawActivity.this);
                }
            }
        });
        new Big_Get_Lucky_Number_Async(Big_LuckyNumberDrawActivity.this);
    }



    private void loadAppLovinNativeAds() {
        try {
            nativeAdLoader = new MaxNativeAdLoader(BIG_Common_Utils.getRandomAdUnitId(responseMain.getLovinNativeID()), Big_LuckyNumberDrawActivity.this);
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
                if (timer != null) {
                    timer.cancel();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void updateDataChanges() {
        try {
            objRewardScreenModel.setSelectedNumber1(String.valueOf(selectedNumber1));
            objRewardScreenModel.setSelectedNumber2(String.valueOf(selectedNumber2));
            lblTitle.setText("Your Numbers:");
            tvSelectedNumbers.setText(objRewardScreenModel.getSelectedNumber1() + ", " + objRewardScreenModel.getSelectedNumber2());
            btnSubmit.setEnabled(false);
            if (!isEdit) {
                BIG_Common_Utils.logFirebaseEvent(Big_LuckyNumberDrawActivity.this, "FeatureUsabilityItemId", "FeatureUsabilityEvent", "Lucky_Number_BigPrize", "Submit");
            }
            isEdit = true;
            btnSubmit.setText("Update");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {

            super.onBackPressed();

    }


    public void setData(Big_Lucky_Number_Data_Model responseModel) {
        objRewardScreenModel = responseModel;

        try {
            if (!BIG_Common_Utils.isStringNullOrEmpty(responseModel.getEarningPoint())) {
                BIG_SharePrefs.getInstance().putString(BIG_SharePrefs.EarnedPoints, responseModel.getEarningPoint());
                tvPoints.setText(BIG_SharePrefs.getInstance().getEarningPointString());
            }
            if (objRewardScreenModel.getStatus().equals("2")) {
                layoutContent.setVisibility(GONE);
                layoutNoData.setVisibility(VISIBLE);
                ivLottieNoData.playAnimation();
                BIG_Ads_Utils.showAppLovinInterstitialAd(Big_LuckyNumberDrawActivity.this, null);
            } else {
                layoutContent.setVisibility(VISIBLE);
                layoutNoData.setVisibility(GONE);
                try {
                    TextView lblNote = findViewById(R.id.lblNote);
                    LinearLayout layoutCompleteTask = findViewById(R.id.layoutCompleteTask);
                    if (!BIG_Common_Utils.isStringNullOrEmpty(objRewardScreenModel.getIsTodayTaskCompleted()) && objRewardScreenModel.getIsTodayTaskCompleted().equals("0")) {
                        layoutCompleteTask.setVisibility(VISIBLE);
                        lblNote.setVisibility(GONE);
                        TextView tvTaskNote = findViewById(R.id.tvTaskNote);
                        tvTaskNote.setText(objRewardScreenModel.getTaskNote());
                        Button btnCompleteTask = findViewById(R.id.btnCompleteTask);
                        if(!BIG_Common_Utils.isStringNullOrEmpty(objRewardScreenModel.getTaskButton())){
                            btnCompleteTask.setText(objRewardScreenModel.getTaskButton());
                        }

                        btnCompleteTask.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                if (!BIG_Common_Utils.isStringNullOrEmpty(objRewardScreenModel.getScreenNo())) {
//                                    if (!BIG_Common_Utils.hasUsageAccessPermission(Big_LuckyNumberDrawActivity.this)) {
//                                        BIG_Common_Utils.showUsageAccessPermissionDialog(Big_LuckyNumberDrawActivity.this);
//                                        return;
//                                    } else {
//
//                                    }
                                    BIG_Common_Utils.Redirect(Big_LuckyNumberDrawActivity.this, objRewardScreenModel.getScreenNo(), "", "", "", "", "");
                                } else if (!BIG_Common_Utils.isStringNullOrEmpty(objRewardScreenModel.getTaskId())) {
                                    Intent intent = new Intent(Big_LuckyNumberDrawActivity.this, Big_TaskDetailsInfoActivity.class);
                                    intent.putExtra("taskId", objRewardScreenModel.getTaskId());
                                    startActivity(intent);
                                } else {
                                    Intent intent = new Intent(Big_LuckyNumberDrawActivity.this, Big_TasksCategoryTypeActivity.class);
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
                tvContestId.setText("Contest Id: " + objRewardScreenModel.getContestId());
                if (!BIG_Common_Utils.isStringNullOrEmpty(objRewardScreenModel.getSelectedNumber1()) && !BIG_Common_Utils.isStringNullOrEmpty(objRewardScreenModel.getSelectedNumber2())
                        && !objRewardScreenModel.getSelectedNumber1().equals("0") && !objRewardScreenModel.getSelectedNumber2().equals("0")) {
                    lblTitle.setText("My Selected Numbers:");
                    tvSelectedNumbers.setText(objRewardScreenModel.getSelectedNumber1() + ", " + objRewardScreenModel.getSelectedNumber2());
                    btnSubmit.setText("Update");
                    isEdit = true;
                } else {
                    lblTitle.setText("Select Any 2 Lucky Numbers");
                }
                tvWinningPoints.setText(objRewardScreenModel.getWiningPoints());
                if (!BIG_Common_Utils.isStringNullOrEmpty(objRewardScreenModel.getMaxLuckyNumber())) {
                    int no = Integer.parseInt(objRewardScreenModel.getMaxLuckyNumber());
                    for (int i = 0; i < no; i++) {
                        boolean isSelected = false;
                        if ((!BIG_Common_Utils.isStringNullOrEmpty(objRewardScreenModel.getSelectedNumber1()) && objRewardScreenModel.getSelectedNumber1().equals(String.valueOf(i + 1)))
                                || (!BIG_Common_Utils.isStringNullOrEmpty(objRewardScreenModel.getSelectedNumber2()) && objRewardScreenModel.getSelectedNumber2().equals(String.valueOf(i + 1)))) {
                            isSelected = true;
                        }
                        listData.add(new Big_LuckyNumberItem(i + 1, isSelected));
                    }
                }
                selectedNumber1 = Integer.parseInt(objRewardScreenModel.getSelectedNumber1());
                selectedNumber2 = Integer.parseInt(objRewardScreenModel.getSelectedNumber2());
                luckyNumberAdapter = new Big_Lucky_Number_Adapter(listData, Big_LuckyNumberDrawActivity.this, new Big_Lucky_Number_Adapter.ClickListener() {
                    @Override
                    public void onItemClick(int position, View v) {
                        if (selectedNumber1 > 0 && selectedNumber2 > 0 && !listData.get(position).getIsSelected()) {
                            BIG_Common_Utils.NotifyMessage(Big_LuckyNumberDrawActivity.this, "Lucky Number", "You have already selected 2 numbers, please deselect any selected number first to select another number", false);
                        } else {
                            if (listData.get(position).getIsSelected()) {
                                if (selectedNumber1 == listData.get(position).getNumber()) {
                                    selectedNumber1 = 0;
                                } else if (selectedNumber2 == listData.get(position).getNumber()) {
                                    selectedNumber2 = 0;
                                }
                            } else {
                                if (selectedNumber1 == 0) {
                                    selectedNumber1 = listData.get(position).getNumber();
                                } else if (selectedNumber2 == 0) {
                                    selectedNumber2 = listData.get(position).getNumber();
                                }
                            }
                            listData.get(position).setIsSelected(!listData.get(position).getIsSelected());
                            luckyNumberAdapter.notifyItemChanged(position);
                        }
                        btnSubmit.setEnabled(isButtonEnabled());
                    }
                });

                GridLayoutManager mGridLayoutManager = new GridLayoutManager(Big_LuckyNumberDrawActivity.this, 6);
                mGridLayoutManager.setOrientation(RecyclerView.VERTICAL);
                rvLuckyNumbers.setLayoutManager(mGridLayoutManager);
                rvLuckyNumbers.setItemAnimator(new DefaultItemAnimator());
                rvLuckyNumbers.setAdapter(luckyNumberAdapter);
                // Load home note webview top
                try {
                    if (!BIG_Common_Utils.isStringNullOrEmpty(objRewardScreenModel.getHomeNote())) {
                        WebView webNote = findViewById(R.id.webNote);
                        webNote.getSettings().setJavaScriptEnabled(true);
                        webNote.setVisibility(View.VISIBLE);
                        webNote.loadDataWithBaseURL(null, objRewardScreenModel.getHomeNote(), "text/html", "UTF-8", null);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                // Load top ad
                try {
                    if (objRewardScreenModel.getTopAds() != null && !BIG_Common_Utils.isStringNullOrEmpty(objRewardScreenModel.getTopAds().getImage())) {
                        LinearLayout layoutTopAds = findViewById(R.id.layoutTopAds);
                        BIG_Common_Utils.loadTopBannerAd(Big_LuckyNumberDrawActivity.this, layoutTopAds, objRewardScreenModel.getTopAds());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (objRewardScreenModel.getTodayDate() != null) {
                    todayDate = objRewardScreenModel.getTodayDate();
                }
                if (objRewardScreenModel.getEndDate() != null) {
                    endDate = objRewardScreenModel.getEndDate();
                }

                setTimer();
                if (!BIG_Common_Utils.isStringNullOrEmpty(objRewardScreenModel.getSelectedNumber1()) && !BIG_Common_Utils.isStringNullOrEmpty(objRewardScreenModel.getSelectedNumber2())
                        && !objRewardScreenModel.getSelectedNumber1().equals("0") && !objRewardScreenModel.getSelectedNumber2().equals("0")) {
                    BIG_Ads_Utils.showAppLovinInterstitialAd(Big_LuckyNumberDrawActivity.this, null);
                }
            }
            if (!BIG_Common_Utils.isStringNullOrEmpty(objRewardScreenModel.getHelpVideoUrl())) {
                ivHelp.setVisibility(VISIBLE);
                ivHelp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        BIG_Common_Utils.openUrl(Big_LuckyNumberDrawActivity.this, objRewardScreenModel.getHelpVideoUrl());
                    }
                });
            }
            btnSubmit.setEnabled(isButtonEnabled());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isButtonEnabled() {
        if (isEdit) {
            return (selectedNumber1 > 0 && selectedNumber1 != Integer.parseInt(objRewardScreenModel.getSelectedNumber1()))
                    ||
                    (selectedNumber2 > 0 && selectedNumber2 != Integer.parseInt(objRewardScreenModel.getSelectedNumber2()));
        } else {
            return (selectedNumber1 > 0 && selectedNumber1 != Integer.parseInt(objRewardScreenModel.getSelectedNumber1()))
                    &&
                    (selectedNumber2 > 0 && selectedNumber2 != Integer.parseInt(objRewardScreenModel.getSelectedNumber2()));
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
                    lblSubTitle.setText("Contest is over now, check contest result in History!");
                    tvTimer.setText("");
                }
            }.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}