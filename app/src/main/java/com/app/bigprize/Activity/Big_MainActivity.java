package com.app.bigprize.Activity;

import static com.willy.ratingbar.BaseRatingBar.TAG;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
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
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieDrawable;
import com.airbnb.lottie.RenderMode;
import com.app.bigprize.Adapter.Big_DrawerMenuAdapter;
import com.app.bigprize.Adapter.Big_DrawerMenuChildView;
import com.app.bigprize.Adapter.Big_DrawerMenuParentView;
import com.app.bigprize.Adapter.Big_EarnGridAdapter;
import com.app.bigprize.Adapter.Big_HomeBottomSheet;
import com.app.bigprize.Adapter.Big_HomeGiveawayCodesAdapter;
import com.app.bigprize.Adapter.Big_Home_Single_Slider_Adapter;
import com.app.bigprize.Adapter.Big_Home_Single_Task_Adapter;
import com.app.bigprize.Adapter.Big_Home_Story_Adapter;
import com.app.bigprize.Adapter.Big_Home_Task_Offer_List_Adapter;
import com.app.bigprize.Adapter.Big_QuickTasksAdapter;
import com.app.bigprize.Async.Big_GetWalletBalanceAsync;
import com.app.bigprize.Async.Big_Home_Data_Async;
import com.app.bigprize.Async.Big_SaveQuickTaskAsync;
import com.app.bigprize.Async.Models.Big_Home_Data_Item;
import com.app.bigprize.Async.Models.Big_Home_Data_List_Item;
import com.app.bigprize.Async.Models.Big_Home_Slider_Item;
import com.app.bigprize.Async.Models.Big_Menu_ListItem;
import com.app.bigprize.Async.Models.Big_Push_Notification_Model;
import com.app.bigprize.Async.Models.Big_Response_Model;
import com.app.bigprize.Async.Models.Big_Story_View_Item;
import com.app.bigprize.Async.Models.Big_Sub_MenuList_Item;
import com.app.bigprize.Async.Models.Big_UserProfileDetails;
import com.app.bigprize.Async.Models.BottomGrid;
import com.app.bigprize.Big_App_Controller;
import com.app.bigprize.Customviews.Big_Recyclerview.Big_Pager_Adapter;
import com.app.bigprize.Customviews.Big_Recyclerview.Big_Recycler_ViewPager;
import com.app.bigprize.Customviews.Big_Story_View;
import com.app.bigprize.Customviews.Big_Storyview.Big_callback.Big_OnStory_Changed_Callback;
import com.app.bigprize.Customviews.Big_Storyview.Big_callback.Big_Story_Click_Listeners;
import com.app.bigprize.R;
import com.app.bigprize.Value.BIG_Constants;
import com.app.bigprize.utils.BIG_Activity_Manager;
import com.app.bigprize.utils.BIG_Ads_Utils;
import com.app.bigprize.utils.BIG_AppLogger;
import com.app.bigprize.utils.BIG_Common_Utils;
import com.app.bigprize.utils.BIG_SharePrefs;
import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxError;
import com.applovin.mediation.nativeAds.MaxNativeAdListener;
import com.applovin.mediation.nativeAds.MaxNativeAdLoader;
import com.applovin.mediation.nativeAds.MaxNativeAdView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.onesignal.Continue;
import com.onesignal.OneSignal;
import com.skydoves.progressview.ProgressView;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Big_MainActivity extends AppCompatActivity {
    private DrawerLayout drawer;
    private NavigationView nav_view_left;
    private int selectedQuickTaskPos = -1;

    private boolean isCheckedForUpdate = false;
    private Big_QuickTasksAdapter quickTasksAdapter;
    private CountDownTimer  timerQuickTask;

    private boolean isTimerSet = false, isTimerOver = false;
    private Handler handlerExit;
    private Big_Response_Model responseMain;
    private Dialog dialog, dialogExitDialogAfterInterstitial;
    private boolean doubleBackToExitPressedOnce = false, isExitNativeNotLoaded = false, isHomeSelected = false;
    private MaxAd nativeAdExit;
    private AppCompatButton btnWithdraw;
    private MaxNativeAdLoader nativeAdLoaderExit;
    private FrameLayout frameLayoutExit;
    private LinearLayout layoutHome, layoutReward, layoutTasks, layoutInvite, layoutGames, layoutPoints, layoutGiveawayCode, layoutHotOffers, linearProgress;
    private ImageView ivHome, ivGames, ivReward, ivInvite, imgStory, ivMenu, ivAdjoe, ivTasks, ivAdjoeLeaderboard, task_ic;
    private TextView lblHome, lblReward, lblTasks, lblInvite, lblGames, tvName, tvEmail, tvRewardPoints, tvPoints, lblGiveawayCode, tvNextPayout, tvWithdrawProgress;
    private CircleImageView ivProfilePic;
    private RelativeLayout layoutSlider, layoutTasks1,withdraw_box;
    private Big_Recycler_ViewPager rvSlider;
    private LinearLayout layoutInflate;
    private MaxAd nativeAd;
    private MaxNativeAdLoader nativeAdLoader;
    private LottieAnimationView lottieAdjoe, lottieViewTask;
    private Animation blinkAnimation;
    private LottieAnimationView progressBarWithdraw, ivScanAndPay;
    private View quickTaskView;
    private LottieAnimationView lottie_withdraw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        BIG_Common_Utils.setDayNightTheme(Big_MainActivity.this);
        setContentView(R.layout.big_activity_main);


        ivScanAndPay = findViewById(R.id.ivScanAndPay);
        blinkAnimation = new AlphaAnimation(0.3f, 1.0f);
        blinkAnimation.setDuration(500); //You can manage the blinking time with this parameter
        blinkAnimation.setStartOffset(20);
        blinkAnimation.setRepeatMode(Animation.REVERSE);
        blinkAnimation.setRepeatCount(Animation.INFINITE);


        printHashKey(Big_MainActivity.this);


        responseMain = new Gson().fromJson(BIG_SharePrefs.getInstance().getString(BIG_SharePrefs.HomeData), Big_Response_Model.class);
        if (responseMain == null || (getIntent().getExtras() != null && getIntent().getExtras().containsKey("isFromLogin"))) {
            new Big_Home_Data_Async(Big_MainActivity.this);
        } else {
            setData();
        }
        if (BIG_SharePrefs.getInstance().getBoolean(BIG_SharePrefs.isFromNotification) || !BIG_SharePrefs.getInstance().getBoolean(BIG_SharePrefs.IS_LOGIN)) {
            BIG_Common_Utils.InitializeApplovinSDK();
        }

        registerBroadcast();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            if (Big_App_Controller.packageInstallBroadcast != null) {
                unregisterReceiver(Big_App_Controller.packageInstallBroadcast);
                Big_App_Controller.packageInstallBroadcast = null;
                //AppLogger.getInstance().e("PACKAGE onDestroy=======", "UNREGISTER");
            }
        } catch (Exception e) {
            Big_App_Controller.packageInstallBroadcast = null;
            e.printStackTrace();
        }
    }

    private void setData() {
        initView();
        initSlideMenuUI();
        showExitDialog(false);

        if (!BIG_SharePrefs.getInstance().getBoolean(BIG_SharePrefs.IS_LOGIN)) {
            progressBarWithdraw.setVisibility(View.GONE);
        }

        if (!BIG_Common_Utils.isStringNullOrEmpty(responseMain.getIsShowAdjoeLeaderboardIcon()) && responseMain.getIsShowAdjoeLeaderboardIcon().equals("1")) {
            ivAdjoeLeaderboard.setVisibility(View.VISIBLE);
        } else {
            ivAdjoeLeaderboard.setVisibility(View.GONE);
        }
        try {
            if (!BIG_Common_Utils.isStringNullOrEmpty(responseMain.getWithdrawLottie())) {
                if (responseMain.getWithdrawLottie().endsWith(".json")) {
                    withdraw_box.setVisibility(View.VISIBLE);
                    lottie_withdraw.setVisibility(View.VISIBLE);
                    BIG_Common_Utils.setLottieAnimation(lottie_withdraw, responseMain.getWithdrawLottie());
                }
            } else {
                withdraw_box.setVisibility(View.GONE);
                lottie_withdraw.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            if (BIG_SharePrefs.getInstance().getBoolean(BIG_SharePrefs.IS_LOGIN)) {
                if (!BIG_Common_Utils.isStringNullOrEmpty(responseMain.getIsShowHomeBottomSheet()) && responseMain.getIsShowHomeBottomSheet().equals("1")) {
                    showBottomDialog(responseMain.getBottomGrid());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            if (!BIG_Common_Utils.isStringNullOrEmpty(responseMain.getIsShowFooterTaskIcon()) && responseMain.getIsShowFooterTaskIcon().equals("1")) {
                if (!BIG_Common_Utils.isStringNullOrEmpty(responseMain.getFooterTaskIcon())) {
                    if (responseMain.getFooterTaskIcon().endsWith(".json")) {

//                    Log.e("Json--)","Play"+responseMain.getFooterTaskIcon());
                        lottieViewTask.setVisibility(View.VISIBLE);
                        ivTasks.setVisibility(View.GONE);
                        lottieViewTask.setAnimationFromUrl(responseMain.getFooterTaskIcon());
                        lottieViewTask.setRenderMode(RenderMode.HARDWARE);
                        lottieViewTask.playAnimation();
                        BIG_Common_Utils.setLottieAnimation(lottieViewTask, responseMain.getFooterTaskIcon());
                    } else {
                        Glide.with(Big_MainActivity.this).load(responseMain.getFooterTaskIcon()).into(ivTasks);
                        ivTasks.setVisibility(View.VISIBLE);
                        lottieViewTask.setVisibility(View.GONE);
                    }
                } else {
                    lottieViewTask.setVisibility(View.VISIBLE);
                    ivTasks.setVisibility(View.GONE);
                    lottieViewTask.setAnimation(R.raw.big_ic_tack_anim);
                }
            } else {
                layoutTasks1.setVisibility(View.GONE);
                layoutTasks.setVisibility(View.GONE);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            if (!BIG_Common_Utils.isStringNullOrEmpty(responseMain.getIsScanAndPayShow()) && responseMain.getIsScanAndPayShow().equalsIgnoreCase("1")) {
                ivScanAndPay.setVisibility(View.VISIBLE);
                ivScanAndPay.playAnimation();
                ivScanAndPay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        openScanScreen();
                    }
                });
            } else {
                ivScanAndPay.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            if (!BIG_Common_Utils.isStringNullOrEmpty(responseMain.getHotOffersScreenNo()) && !BIG_Common_Utils.isStringNullOrEmpty(responseMain.getIsShowHotOffers()) && responseMain.getIsShowHotOffers().equalsIgnoreCase("1")) {
                layoutHotOffers.setVisibility(View.VISIBLE);
                layoutHotOffers.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        BIG_Common_Utils.Redirect(Big_MainActivity.this, responseMain.getHotOffersScreenNo(), "", "", "", "", "");
                    }
                });

            } else {
                layoutHotOffers.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }



        try {
            if (!BIG_Common_Utils.isStringNullOrEmpty(responseMain.getIsShowPlaytimeIcon()) && responseMain.getIsShowPlaytimeIcon().equalsIgnoreCase("1")) {
                if (!BIG_Common_Utils.isStringNullOrEmpty(responseMain.getImageAdjoeIcon())) {
                    if (responseMain.getImageAdjoeIcon().endsWith(".json")) {
                        lottieAdjoe.setVisibility(View.VISIBLE);
                        lottieAdjoe.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                BIG_Common_Utils.openAdjoeOfferWall(Big_MainActivity.this);
                            }
                        });
                        BIG_Common_Utils.setLottieAnimation(lottieAdjoe, responseMain.getImageAdjoeIcon());
                    } else {
                        ivAdjoe.setVisibility(View.VISIBLE);
                        ivAdjoe.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                BIG_Common_Utils.openAdjoeOfferWall(Big_MainActivity.this);
                            }
                        });
                        Glide.with(Big_MainActivity.this).load(responseMain.getImageAdjoeIcon()).override(getResources().getDimensionPixelSize(R.dimen.dim_32)).into(ivAdjoe);
                    }
                }
            } else {
                ivAdjoe.setVisibility(View.GONE);
                lottieAdjoe.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            if (!BIG_Common_Utils.isStringNullOrEmpty(responseMain.getFooterImage())) {
                ImageView ivFooterImage = findViewById(R.id.ivFooterImage);
                Glide.with(Big_MainActivity.this).load(responseMain.getFooterImage()).into(ivFooterImage);
                ivFooterImage.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            if (responseMain.getStoryView() != null && responseMain.getStoryView().size() > 0) {
                imgStory.setVisibility(View.VISIBLE);
                imgStory.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        new Big_Story_View.Builder(Big_MainActivity.this.getSupportFragmentManager()).setStoriesList((ArrayList<Big_Story_View_Item>) responseMain.getStoryView()).setStoryDuration(5000).setTitleText("").setSubtitleText("").setStoryClickListeners(new Big_Story_Click_Listeners() {
                            @Override
                            public void onDescriptionClickListener(int position) {
                                BIG_Common_Utils.Redirect(Big_MainActivity.this, responseMain.getStoryView().get(position).getScreenNo(), responseMain.getStoryView().get(position).getTitle(), responseMain.getStoryView().get(position).getClickUrl(), responseMain.getStoryView().get(position).getId(), responseMain.getStoryView().get(position).getTaskId(), null);
                            }

                            @Override
                            public void onTitleIconClickListener(int position) {

                            }
                        }).setOnStoryChangedCallback(new Big_OnStory_Changed_Callback() {
                            @Override
                            public void storyChanged(int position) {
                            }
                        }).setStartingIndex(0).setRtl(false).build().show();
                    }
                });
            } else {
                imgStory.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            if (!BIG_Common_Utils.isStringNullOrEmpty(responseMain.getIsShowGiveawayCode()) && responseMain.getIsShowGiveawayCode().equals("1")) {
                layoutGiveawayCode.setVisibility(View.VISIBLE);
                RecyclerView rvGiveawayCodes = findViewById(R.id.rvGiveawayCodes);

                if (!BIG_Common_Utils.isStringNullOrEmpty(responseMain.getGiveawayCode())) {
                    lblGiveawayCode.setText("Apply Giveaway Code");

                    rvGiveawayCodes.setVisibility(View.VISIBLE);
                    List<String> list = Arrays.asList(responseMain.getGiveawayCode().split(","));
                    rvGiveawayCodes.setLayoutManager(new LinearLayoutManager(Big_MainActivity.this, LinearLayoutManager.HORIZONTAL, false));
                    rvGiveawayCodes.setAdapter(new Big_HomeGiveawayCodesAdapter(list, Big_MainActivity.this, new Big_HomeGiveawayCodesAdapter.ClickListener() {
                        @Override
                        public void onClick(int position, View v, LinearLayout linearLayout) {
                            startActivity(new Intent(Big_MainActivity.this, Big_GiveAwaySocialActivity.class));
                        }
                    }));

                } else {
                    lblGiveawayCode.setText("Have a Giveaway Code?");
                    rvGiveawayCodes.setVisibility(View.GONE);
                }
                rvGiveawayCodes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(Big_MainActivity.this, Big_GiveAwaySocialActivity.class));
                        Toast.makeText(Big_MainActivity.this, "code", Toast.LENGTH_SHORT).show();
                    }
                });

                layoutGiveawayCode.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(Big_MainActivity.this, Big_GiveAwaySocialActivity.class));
                    }
                });
            } else {
                layoutGiveawayCode.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        try {
            if (responseMain.getHomeSlider() != null && responseMain.getHomeSlider().size() > 0) {
                layoutSlider.setVisibility(View.VISIBLE);
                rvSlider.setClear();
                rvSlider.addAll((ArrayList<Big_Home_Slider_Item>) responseMain.getHomeSlider());
                rvSlider.start();
                rvSlider.setOnItemClickListener(new Big_Pager_Adapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        BIG_Common_Utils.Redirect(Big_MainActivity.this, responseMain.getHomeSlider().get(position).getScreenNo(), responseMain.getHomeSlider().get(position).getTitle(), responseMain.getHomeSlider().get(position).getUrl(), responseMain.getHomeSlider().get(position).getId(), null, responseMain.getHomeSlider().get(position).getImage());
                    }
                });
            } else {
                layoutSlider.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Load home note webview top
        try {
            if (!BIG_Common_Utils.isStringNullOrEmpty(responseMain.getHomeNote())) {
                WebView webNote = findViewById(R.id.webNote);
                webNote.getSettings().setJavaScriptEnabled(true);
                webNote.setVisibility(View.VISIBLE);
                webNote.loadDataWithBaseURL(null, responseMain.getHomeNote(), "text/html", "UTF-8", null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (layoutInflate != null) {
            layoutInflate.removeAllViews();
        }
        layoutInflate.setVisibility(View.VISIBLE);
        try {
            if (responseMain.getHomeDataList() != null && responseMain.getHomeDataList().size() > 0) {
                for (int i = 0; i < responseMain.getHomeDataList().size(); i++) {
                    try {
                        inflateHomeScreenData(responseMain.getHomeDataList().get(i).getType(), responseMain.getHomeDataList().get(i));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        showHomeDialog();
        if (!isCheckedForUpdate) {
            isCheckedForUpdate = true;
            if (responseMain.getAppVersion() != null) {
                try {
                    PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
                    String version = pInfo.versionName;
                    if (!responseMain.getAppVersion().equals(version)) {
                        BIG_Common_Utils.UpdateApp(Big_MainActivity.this, responseMain.getIsForceUpdate(), responseMain.getAppUrl(), responseMain.getUpdateMessage());
                    }
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        try {
            if (BIG_SharePrefs.getInstance().getBoolean(BIG_SharePrefs.isFromNotification)) {
                BIG_SharePrefs.getInstance().putBoolean(BIG_SharePrefs.isFromNotification, false);
                Big_Push_Notification_Model notificationModel = new Gson().fromJson(BIG_SharePrefs.getInstance().getString(BIG_SharePrefs.notificationData), Big_Push_Notification_Model.class);
                BIG_Common_Utils.Redirect(Big_MainActivity.this, notificationModel.getScreenNo(), notificationModel.getTitle(), notificationModel.getUrl(), notificationModel.getId(), notificationModel.getTaskId(), notificationModel.getImage());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!BIG_Common_Utils.isStringNullOrEmpty(responseMain.getIsShowWelcomeBonusPopup()) && responseMain.getIsShowWelcomeBonusPopup().equals("1") && !BIG_SharePrefs.getInstance().getBoolean(BIG_SharePrefs.IS_WELCOME_POPUP_SHOWN)) {
            BIG_AppLogger.getInstance().e("login===", "" + responseMain.getIsShowWelcomeBonusPopup());
            BIG_Common_Utils.logFirebaseEvent(Big_MainActivity.this, "FeatureUsabilityItemId", "FeatureUsabilityEvent", "Sign_up_BigPrize", "Sign up");
            showWelcomeBonusPopup(responseMain.getWelcomeBonus());
        }

        updateNextWithdrawAmount();
//        POC_Common_Utils.showWatchWebDialog(Big_MainActivity.this);
    }

    private void inflateHomeScreenData(String type, Big_Home_Data_List_Item categoryModel) {

        switch (type) {
            case BIG_Constants.HomeDataType.TASK_LIST:
                if (categoryModel.getData() != null && categoryModel.getData().size() > 0) {
                    View viewTaskList = getLayoutInflater().inflate(R.layout.big_inflate_home_general_layout, null);
                    RecyclerView taskList = viewTaskList.findViewById(R.id.rvIconlist);
                    TextView taskListHeader = viewTaskList.findViewById(R.id.txtTitleHeader);
                    if (categoryModel.getTitle() != null && !categoryModel.getTitle().isEmpty()) {
                        taskListHeader.setVisibility(View.VISIBLE);
                        taskListHeader.setText(categoryModel.getTitle());
                    } else {
                        taskListHeader.setVisibility(View.GONE);
                    }
                    Big_Home_Task_Offer_List_Adapter taskListAdapter = new Big_Home_Task_Offer_List_Adapter(categoryModel.getData(), Big_MainActivity.this, new Big_Home_Task_Offer_List_Adapter.ClickListener() {
                        @Override
                        public void onItemClick(int position, View v) {
                            BIG_Common_Utils.Redirect(Big_MainActivity.this, categoryModel.getData().get(position).getScreenNo(), categoryModel.getData().get(position).getTitle(), categoryModel.getData().get(position).getUrl(), categoryModel.getData().get(position).getId(), categoryModel.getData().get(position).getTaskId(), categoryModel.getData().get(position).getImage());
                        }
                    });
                    taskList.setLayoutManager(new LinearLayoutManager(Big_MainActivity.this, LinearLayoutManager.VERTICAL, false));
                    taskList.setAdapter(taskListAdapter);
                    layoutInflate.addView(viewTaskList);
                }
                break;
            case BIG_Constants.HomeDataType.ICON_LIST:
                if (categoryModel.getData() != null && categoryModel.getData().size() > 0) {
                    View iconView = getLayoutInflater().inflate(R.layout.big_inflate_home_iconlist, null);
                    RecyclerView rvIconlist = iconView.findViewById(R.id.rvIconlist);
                    TextView txtTitleHeader = iconView.findViewById(R.id.txtTitleHeader);

                    if (categoryModel.getTitle() != null && !categoryModel.getTitle().isEmpty()) {
                        txtTitleHeader.setVisibility(View.VISIBLE);
                        txtTitleHeader.setText(categoryModel.getTitle());
                    } else {
                        txtTitleHeader.setVisibility(View.GONE);
                    }
                    Big_Home_Story_Adapter homeStoryAdapter = new Big_Home_Story_Adapter(Big_MainActivity.this, categoryModel.getData(), new Big_Home_Story_Adapter.ClickListener() {
                        @Override
                        public void onItemClick(int position, View v) {
                            BIG_Common_Utils.Redirect(Big_MainActivity.this, categoryModel.getData().get(position).getScreenNo(), categoryModel.getData().get(position).getTitle(), categoryModel.getData().get(position).getUrl(), categoryModel.getData().get(position).getId(), categoryModel.getData().get(position).getTaskId(), categoryModel.getData().get(position).getImage());
                        }
                    });
                    rvIconlist.setLayoutManager(new LinearLayoutManager(Big_MainActivity.this, LinearLayoutManager.HORIZONTAL, false));
                    rvIconlist.setAdapter(homeStoryAdapter);
                    layoutInflate.addView(iconView);
                }
                break;
            case BIG_Constants.HomeDataType.SINGLE_SLIDER:
                if (categoryModel.getData() != null && categoryModel.getData().size() > 0) {
                    View iconSingleSlider = getLayoutInflater().inflate(R.layout.big_inflate_home_general_layout, null);
                    RecyclerView rvSliderlist = iconSingleSlider.findViewById(R.id.rvIconlist);
                    TextView txtHeader = iconSingleSlider.findViewById(R.id.txtTitleHeader);

                    if (categoryModel.getTitle() != null && !categoryModel.getTitle().isEmpty()) {
                        txtHeader.setVisibility(View.VISIBLE);
                        txtHeader.setText(categoryModel.getTitle());
                    } else {
                        txtHeader.setVisibility(View.GONE);
                    }
                    ////Loge("Size--)", "" + categoryModel.getData().size());
                    Big_Home_Single_Slider_Adapter homeSingleSilderAdapter = new Big_Home_Single_Slider_Adapter(Big_MainActivity.this, categoryModel.getData(), false, new Big_Home_Single_Slider_Adapter.ClickListener() {
                        @Override
                        public void onItemClick(int position, View v) {
                            BIG_Common_Utils.Redirect(Big_MainActivity.this, categoryModel.getData().get(position).getScreenNo(), categoryModel.getData().get(position).getTitle(), categoryModel.getData().get(position).getUrl(), categoryModel.getData().get(position).getId(), categoryModel.getData().get(position).getTaskId(), categoryModel.getData().get(position).getImage());
                        }
                    });
                    rvSliderlist.setLayoutManager(new LinearLayoutManager(Big_MainActivity.this, LinearLayoutManager.VERTICAL, false));
                    rvSliderlist.setAdapter(homeSingleSilderAdapter);
                    layoutInflate.addView(iconSingleSlider);
                }
                break;
            case BIG_Constants.HomeDataType.GRID:
                if (categoryModel.getData() != null && categoryModel.getData().size() > 0) {
                    View viewEarnGrid = getLayoutInflater().inflate(R.layout.big_inflate_home_grid, null);
                    RecyclerView gridList = viewEarnGrid.findViewById(R.id.rvIconlist);
                    TextView earnGridHeader = viewEarnGrid.findViewById(R.id.txtTitleHeader);

                    if (categoryModel.getTitle() != null && !categoryModel.getTitle().isEmpty()) {
                        earnGridHeader.setVisibility(View.VISIBLE);
                        earnGridHeader.setText(categoryModel.getTitle());
                    } else {
                        earnGridHeader.setVisibility(View.GONE);
                    }
                    Big_EarnGridAdapter earnGridAdapter = new Big_EarnGridAdapter(Big_MainActivity.this, categoryModel.getData(), new Big_EarnGridAdapter.ClickListener() {
                        @Override
                        public void onItemClick(int position, View v) {
                            BIG_Common_Utils.Redirect(Big_MainActivity.this, categoryModel.getData().get(position).getScreenNo(), categoryModel.getData().get(position).getTitle(), categoryModel.getData().get(position).getUrl(), categoryModel.getData().get(position).getId(), categoryModel.getData().get(position).getTaskId(), categoryModel.getData().get(position).getImage());
                        }
                    });
                    gridList.setLayoutManager(new GridLayoutManager(Big_MainActivity.this, 2));
                    gridList.setAdapter(earnGridAdapter);
                    layoutInflate.addView(viewEarnGrid);
                }
                break;
            case BIG_Constants.HomeDataType.TWO_GRID:
                if (categoryModel.getData() != null && categoryModel.getData().size() > 0) {
                    View twoGrid = getLayoutInflater().inflate(R.layout.big_inflate_home_grid, null);
                    RecyclerView rvGridlist = twoGrid.findViewById(R.id.rvIconlist);
                    TextView txtGridHeader = twoGrid.findViewById(R.id.txtTitleHeader);

                    if (categoryModel.getTitle() != null && !categoryModel.getTitle().isEmpty()) {
                        txtGridHeader.setVisibility(View.VISIBLE);
                        txtGridHeader.setText(categoryModel.getTitle());
                    } else {
                        txtGridHeader.setVisibility(View.GONE);
                    }
                    Big_Home_Single_Slider_Adapter homeGridAdpater = new Big_Home_Single_Slider_Adapter(Big_MainActivity.this, categoryModel.getData(), true, new Big_Home_Single_Slider_Adapter.ClickListener() {
                        @Override
                        public void onItemClick(int position, View v) {
                            BIG_Common_Utils.Redirect(Big_MainActivity.this, categoryModel.getData().get(position).getScreenNo(), categoryModel.getData().get(position).getTitle(), categoryModel.getData().get(position).getUrl(), categoryModel.getData().get(position).getId(), categoryModel.getData().get(position).getTaskId(), categoryModel.getData().get(position).getImage());
                        }
                    });
                    rvGridlist.setLayoutManager(new GridLayoutManager(Big_MainActivity.this, 2));
                    rvGridlist.setAdapter(homeGridAdpater);
                    layoutInflate.addView(twoGrid);
                }
                break;
            case BIG_Constants.HomeDataType.SINGLE_BIG_TASK:
                if (categoryModel.getData() != null && categoryModel.getData().size() > 0) {
                    View viewSingleBigTaskRow = getLayoutInflater().inflate(R.layout.big_inflate_home_general_layout, null);
                    RecyclerView rvSingleBiglist = viewSingleBigTaskRow.findViewById(R.id.rvIconlist);
                    TextView txtSingleBigHeader = viewSingleBigTaskRow.findViewById(R.id.txtTitleHeader);
                    if (categoryModel.getTitle() != null && !categoryModel.getTitle().isEmpty()) {
                        txtSingleBigHeader.setVisibility(View.VISIBLE);
                        txtSingleBigHeader.setText(categoryModel.getTitle());
                    } else {
                        txtSingleBigHeader.setVisibility(View.GONE);
                    }
                    Big_Home_Single_Task_Adapter homeSingleBiogtaskAdapter = new Big_Home_Single_Task_Adapter(Big_MainActivity.this, categoryModel.getData(), categoryModel.getPointBackgroundColor(), categoryModel.getPointTextColor(), new Big_Home_Single_Task_Adapter.ClickListener() {
                        @Override
                        public void onItemClick(int position, View v) {
                            BIG_Common_Utils.Redirect(Big_MainActivity.this, categoryModel.getData().get(position).getScreenNo(), categoryModel.getData().get(position).getTitle(), categoryModel.getData().get(position).getUrl(), categoryModel.getData().get(position).getId(), categoryModel.getData().get(position).getTaskId(), categoryModel.getData().get(position).getImage());
                        }
                    });
                    rvSingleBiglist.setLayoutManager(new LinearLayoutManager(Big_MainActivity.this, LinearLayoutManager.VERTICAL, false));
                    rvSingleBiglist.setAdapter(homeSingleBiogtaskAdapter);
                    layoutInflate.addView(viewSingleBigTaskRow);
                }
                break;
            case BIG_Constants.HomeDataType.NATIVE_AD:
                View viewNativeAd = getLayoutInflater().inflate(R.layout.big_inflate_native_ad, null);
                FrameLayout fl_adplaceholder = viewNativeAd.findViewById(R.id.fl_adplaceholder);
                TextView lblLoadingAds = viewNativeAd.findViewById(R.id.lblLoadingAds);
                if (BIG_Common_Utils.isShowAppLovinNativeAds()) {
                    loadAppLovinNativeAds(fl_adplaceholder, lblLoadingAds);
                    layoutInflate.addView(viewNativeAd);
                }
                break;
            case BIG_Constants.HomeDataType.EARN_GRID:
                if (categoryModel.getData() != null && categoryModel.getData().size() > 0) {
                    View viewEarnGrid = getLayoutInflater().inflate(R.layout.big_inflate_home_grid, null);
                    RecyclerView gridList = viewEarnGrid.findViewById(R.id.rvIconlist);
                    TextView earnGridHeader = viewEarnGrid.findViewById(R.id.txtTitleHeader);

                    if (categoryModel.getTitle() != null && !categoryModel.getTitle().isEmpty()) {
                        earnGridHeader.setVisibility(View.VISIBLE);
                        earnGridHeader.setText(categoryModel.getTitle());
                    } else {
                        earnGridHeader.setVisibility(View.GONE);
                    }
                    Big_EarnGridAdapter earnGridAdapter = new Big_EarnGridAdapter(Big_MainActivity.this, categoryModel.getData(), new Big_EarnGridAdapter.ClickListener() {
                        @Override
                        public void onItemClick(int position, View v) {
                            BIG_Common_Utils.Redirect(Big_MainActivity.this, categoryModel.getData().get(position).getScreenNo(), categoryModel.getData().get(position).getTitle(), categoryModel.getData().get(position).getUrl(), categoryModel.getData().get(position).getId(), categoryModel.getData().get(position).getTaskId(), categoryModel.getData().get(position).getImage());
                        }
                    });
                    gridList.setLayoutManager(new GridLayoutManager(Big_MainActivity.this, 2));
                    gridList.setAdapter(earnGridAdapter);
                    layoutInflate.addView(viewEarnGrid);
                }
                break;
            case BIG_Constants.HomeDataType.QUICK_TASK:
                if (categoryModel.getData() != null && categoryModel.getData().size() > 0) {
                    quickTaskView = getLayoutInflater().inflate(R.layout.big_inflate_quick_tasks, layoutInflate, false);
                    RecyclerView rvSliderlist = quickTaskView.findViewById(R.id.rvData);
                    TextView txtHeader = quickTaskView.findViewById(R.id.txtTitleHeader);

                    if (categoryModel.getTitle() != null && !categoryModel.getTitle().isEmpty()) {
                        txtHeader.setVisibility(View.VISIBLE);
                        txtHeader.setText(categoryModel.getTitle());
                    } else {
                        txtHeader.setVisibility(View.GONE);
                    }
                    LinearLayout layoutContent = quickTaskView.findViewById(R.id.layoutContent);
                    GradientDrawable drawable = (GradientDrawable) ContextCompat.getDrawable(this, R.drawable.big_rectangle_white);
                    drawable.mutate(); // only change this instance of the xml, not all components using this xml
                    drawable.setStroke(getResources().getDimensionPixelSize(R.dimen.dim_1_5), Color.parseColor(categoryModel.getIconBGColor())); // set stroke width and stroke color
                    drawable.setColor(Color.parseColor(categoryModel.getBgColor()));
                    layoutContent.setBackground(drawable);

                    CardView cardContent = quickTaskView.findViewById(R.id.cardContent);
                    cardContent.setCardBackgroundColor(Color.parseColor(categoryModel.getBgColor()));
                    quickTasksAdapter = new Big_QuickTasksAdapter(categoryModel.getData(), Big_MainActivity.this, new Big_QuickTasksAdapter.ClickListener() {
                        @Override
                        public void onItemClick(int position, View v) {
                            if (BIG_SharePrefs.getInstance().getBoolean(BIG_SharePrefs.IS_LOGIN)) {
                                selectedQuickTaskPos = position;
                                BIG_Activity_Manager.isShowAppOpenAd = false;
                                BIG_Common_Utils.Redirect(Big_MainActivity.this, categoryModel.getData().get(position).getScreenNo(), categoryModel.getData().get(position).getTitle(), categoryModel.getData().get(position).getUrl(), categoryModel.getData().get(position).getId(), categoryModel.getData().get(position).getTaskId(), categoryModel.getData().get(position).getImage());
                                startQuickTaskTimer(categoryModel.getData(), position);
                            } else {
                                BIG_Common_Utils.NotifyLogin(Big_MainActivity.this);
                            }
                        }
                    });
                    rvSliderlist.setLayoutManager(new LinearLayoutManager(Big_MainActivity.this, LinearLayoutManager.VERTICAL, false));
                    rvSliderlist.setAdapter(quickTasksAdapter);
                    layoutInflate.addView(quickTaskView);
                }
                break;

            case BIG_Constants.HomeDataType.LIVE_CONTEST:
                View viewLiveContest = getLayoutInflater().inflate(R.layout.big_inflate_home_live_contest, layoutInflate, false);
                TextView tvLabel1 = viewLiveContest.findViewById(R.id.tvLabel);
                View viewShineHeader = viewLiveContest.findViewById(R.id.viewShineHeader);
                Animation animShine = AnimationUtils.loadAnimation(Big_MainActivity.this, R.anim.big_left_right_duration);
                animShine.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        viewShineHeader.startAnimation(animShine);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });

                viewShineHeader.setVisibility(View.VISIBLE);
                viewShineHeader.startAnimation(animShine);
                if (!BIG_Common_Utils.isStringNullOrEmpty(categoryModel.getLabel())) {
                    tvLabel1.setText(categoryModel.getLabel());
                    tvLabel1.setVisibility(View.VISIBLE);
                    if (!BIG_Common_Utils.isStringNullOrEmpty(categoryModel.getIsBlink()) && categoryModel.getIsBlink().equals("1")) {
                        tvLabel1.startAnimation(blinkAnimation);
                    }
                } else {
                    tvLabel1.setVisibility(View.GONE);
                }

                LinearLayout layoutYourRank = viewLiveContest.findViewById(R.id.layoutYourRank);
                layoutYourRank.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            startActivity(new Intent(Big_MainActivity.this, Big_AdjoeLeaderboardActivity.class));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

                LinearLayout layoutContentClick = viewLiveContest.findViewById(R.id.layoutContentClick);
                layoutContentClick.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            BIG_Common_Utils.setEnableDisable(Big_MainActivity.this, view);
                            BIG_Common_Utils.Redirect(Big_MainActivity.this, categoryModel.getScreenNo(), categoryModel.getTitle(), categoryModel.getUrl(), categoryModel.getId(), categoryModel.getTaskId(), categoryModel.getImage());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

                CardView cardContent1 = viewLiveContest.findViewById(R.id.cardContent);
                cardContent1.setCardBackgroundColor(Color.parseColor(categoryModel.getBgColor()));

                LinearLayout layoutContent1 = viewLiveContest.findViewById(R.id.layoutContent);
                TextView lblMilestonesSubTitle1 = viewLiveContest.findViewById(R.id.lblSubTitle);

                if (!BIG_Common_Utils.isStringNullOrEmpty(categoryModel.getBgColor())) {

                    GradientDrawable drawable = (GradientDrawable) ContextCompat.getDrawable(this, R.drawable.big_rectangle_white);
                    drawable.mutate(); // only change this instance of the xml, not all components using this xml
                    drawable.setStroke(getResources().getDimensionPixelSize(R.dimen.dim_1_5), Color.parseColor(categoryModel.getIconBGColor())); // set stroke width and stroke color
                    drawable.setColor(Color.parseColor(categoryModel.getBgColor()));
                    layoutContent1.setBackground(drawable);

                    Drawable mDrawable2 = ContextCompat.getDrawable(Big_MainActivity.this, R.drawable.big_ad_live_leaderboard_sub_title);
                    mDrawable2.setColorFilter(new PorterDuffColorFilter(Color.parseColor(categoryModel.getIconBGColor()), PorterDuff.Mode.SRC_IN));
                    lblMilestonesSubTitle1.setBackground(mDrawable2);
                }
                TextView lblMilestones1 = viewLiveContest.findViewById(R.id.lblTitle);

                lblMilestones1.setText(categoryModel.getTitle());
                if (!BIG_Common_Utils.isStringNullOrEmpty(categoryModel.getSubTitle())) {
                    lblMilestonesSubTitle1.setText(categoryModel.getSubTitle());
                } else {
                    lblMilestonesSubTitle1.setVisibility(View.GONE);
                }
                if (!BIG_Common_Utils.isStringNullOrEmpty(categoryModel.getLottieBgUrl())) {
                    LottieAnimationView lottieBg = viewLiveContest.findViewById(R.id.lottieBg);
                    lottieBg.setVisibility(View.VISIBLE);
                    BIG_Common_Utils.setLottieAnimation(lottieBg, categoryModel.getLottieBgUrl());
                }
                TextView tvYourRank = viewLiveContest.findViewById(R.id.tvYourRank);
                TextView lblPoints = viewLiveContest.findViewById(R.id.lblPoints);
                TextView tvPointsLiveContest = viewLiveContest.findViewById(R.id.tvPoints);

                tvPointsLiveContest.setText(categoryModel.getWinningPoints());
                tvYourRank.setText(categoryModel.getLeaderboardRank());
                try {
                    if (Integer.parseInt(categoryModel.getLeaderboardRank()) > 3) {
                        lblPoints.setText("Chance to Win");
                    } else {
                        lblPoints.setText("Win Rubies");
                    }
                } catch (NumberFormatException e) {
                    lblPoints.setText("Chance to Win");
                }
                layoutInflate.addView(viewLiveContest);
                break;
            case BIG_Constants.HomeDataType.PUBSCALE:
                View spinView = getLayoutInflater().inflate(R.layout.big_item_pubscale_layout, null);
                BIG_AppLogger.getInstance().e("Reward Screen DATA --)", "" + type);

                if (!BIG_Common_Utils.isStringNullOrEmpty(categoryModel.getFullImage())) {
                    CardView cardContent = spinView.findViewById(R.id.cardContent);
                    cardContent.setVisibility(View.GONE);
                    ImageView ivIconFullImage = spinView.findViewById(R.id.ivIconFullImage);
                    LottieAnimationView ivLottieFullImage = spinView.findViewById(R.id.ivLottieFullImage);
                    ProgressBar progressBarFullImage = spinView.findViewById(R.id.progressBarFullImage);
                    if (categoryModel.getFullImage().contains(".json")) {
                        ivIconFullImage.setVisibility(View.GONE);
                        ivLottieFullImage.setVisibility(View.VISIBLE);
                        BIG_Common_Utils.setLottieAnimation(ivLottieFullImage, categoryModel.getFullImage());
                        ivLottieFullImage.setRepeatCount(LottieDrawable.INFINITE);
                        progressBarFullImage.setVisibility(View.GONE);
                    } else {
                        ivIconFullImage.setVisibility(View.VISIBLE);
                        ivLottieFullImage.setVisibility(View.GONE);
                        Glide.with(Big_MainActivity.this).load(categoryModel.getFullImage()).listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, com.bumptech.glide.load.DataSource dataSource, boolean isFirstResource) {
                                progressBarFullImage.setVisibility(View.GONE);
                                return false;
                            }
                        }).into(ivIconFullImage);
                    }
                    CardView cardFullImage = spinView.findViewById(R.id.cardFullImage);
                    cardFullImage.setVisibility(View.VISIBLE);
                    cardFullImage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            try {
                                BIG_Common_Utils.Redirect(Big_MainActivity.this, categoryModel.getScreenNo(), "", "", "", "", "");

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    layoutInflate.addView(spinView);
                    break;
                }


        }
    }


    private void showHomeDialog() {
        try {
            if (responseMain.getHomeDialog() != null) {
                if (BIG_SharePrefs.getInstance().getString(BIG_SharePrefs.homeDialogShownDate + responseMain.getHomeDialog().getId()).length() == 0 || (!BIG_Common_Utils.isStringNullOrEmpty(responseMain.getHomeDialog().getIsShowEverytime()) && responseMain.getHomeDialog().getIsShowEverytime().equals("1")) || !BIG_SharePrefs.getInstance().getString(BIG_SharePrefs.homeDialogShownDate + responseMain.getHomeDialog().getId()).equals(BIG_Common_Utils.getCurrentDate())) {
                    if (BIG_Common_Utils.isStringNullOrEmpty(responseMain.getHomeDialog().getPackagename()) || (!BIG_Common_Utils.isStringNullOrEmpty(responseMain.getHomeDialog().getPackagename()) && !BIG_Common_Utils.appInstalledOrNot(Big_MainActivity.this, responseMain.getHomeDialog().getPackagename()))) {
                        BIG_SharePrefs.getInstance().putString(BIG_SharePrefs.homeDialogShownDate + responseMain.getHomeDialog().getId(), BIG_Common_Utils.getCurrentDate());

                        Dialog dialog = new Dialog(Big_MainActivity.this, android.R.style.Theme_Light);
                        dialog.getWindow().setBackgroundDrawableResource(R.color.black_transparent);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                        dialog.setContentView(R.layout.big_popup_home_data);
                        Button btnOk = dialog.findViewById(R.id.btnSubmit);
                        TextView txtTitle = dialog.findViewById(R.id.txtTitle);
                        TextView btnCancel = dialog.findViewById(R.id.btnCancel);
                        ProgressBar probrBanner = dialog.findViewById(R.id.probrBanner);
                        ImageView imgBanner = dialog.findViewById(R.id.imgBanner);
                        txtTitle.setText(responseMain.getHomeDialog().getTitle());
                        TextView txtMessage = dialog.findViewById(R.id.txtMessage);
                        RelativeLayout relPopup = dialog.findViewById(R.id.relPopup);
                        LottieAnimationView ivLottieView = dialog.findViewById(R.id.ivLottieView);
                        txtMessage.setText(responseMain.getHomeDialog().getDescription());
                        if (!BIG_Common_Utils.isStringNullOrEmpty(responseMain.getHomeDialog().getIsForce()) && responseMain.getHomeDialog().getIsForce().equals("1")) {
                            btnCancel.setVisibility(View.GONE);
                        } else {
                            btnCancel.setVisibility(View.VISIBLE);
                        }

                        if (!BIG_Common_Utils.isStringNullOrEmpty(responseMain.getHomeDialog().getBtnName())) {
                            btnOk.setText(responseMain.getHomeDialog().getBtnName());
                        }

                        if (!BIG_Common_Utils.isStringNullOrEmpty(responseMain.getHomeDialog().getImage())) {
                            if (responseMain.getHomeDialog().getImage().contains("json")) {
                                probrBanner.setVisibility(View.GONE);
                                imgBanner.setVisibility(View.GONE);
                                ivLottieView.setVisibility(View.VISIBLE);
                                BIG_Common_Utils.setLottieAnimation(ivLottieView, responseMain.getHomeDialog().getImage());
                                ivLottieView.setRepeatCount(LottieDrawable.INFINITE);
                            } else {
                                imgBanner.setVisibility(View.VISIBLE);
                                ivLottieView.setVisibility(View.GONE);
                                Glide.with(Big_MainActivity.this).load(responseMain.getHomeDialog().getImage()).apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE)).addListener(new RequestListener<Drawable>() {
                                    @Override
                                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                        return false;
                                    }

                                    @Override
                                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                        probrBanner.setVisibility(View.GONE);
                                        return false;
                                    }
                                }).into(imgBanner);
                            }
                        } else {
                            imgBanner.setVisibility(View.GONE);
                            probrBanner.setVisibility(View.GONE);
                        }
                        btnCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
                        relPopup.setOnClickListener(v -> BIG_Common_Utils.Redirect(Big_MainActivity.this, responseMain.getHomeDialog().getScreenNo(), responseMain.getHomeDialog().getTitle(), responseMain.getHomeDialog().getUrl(), responseMain.getHomeDialog().getId(), null, responseMain.getHomeDialog().getImage()));
                        btnOk.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                                BIG_Common_Utils.Redirect(Big_MainActivity.this, responseMain.getHomeDialog().getScreenNo(), responseMain.getHomeDialog().getTitle(), responseMain.getHomeDialog().getUrl(), responseMain.getHomeDialog().getId(), null, responseMain.getHomeDialog().getImage());
                            }
                        });

                        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialogInterface) {
                                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        showPushNotificationSettingsDialog();
                                    }
                                }, 500);
                            }
                        });
                        dialog.show();
                    } else {
                        showPushNotificationSettingsDialog();
                    }
                } else {
                    showPushNotificationSettingsDialog();
                }
            } else {
                showPushNotificationSettingsDialog();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showPushNotificationSettingsDialog() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
//                OneSignal.promptForPushNotifications();
                try {
                    OneSignal.getNotifications().requestPermission(true, Continue.with(r -> {
                        if (r.isSuccess()) {
                            if (Boolean.TRUE.equals(r.getData())) {
                                // `requestPermission` completed successfully and the user has accepted permission
                            } else {
                                // `requestPermission` completed successfully but the user has rejected permission
                            }
                        } else {
                            // `requestPermission` completed unsuccessfully, check `r.getThrowable()` for more info on the failure reason
                        }
                    }));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 2000);
    }


    private void loadAppLovinNativeAds(FrameLayout frameLayout, TextView lblLoadingAds) {
        try {
            nativeAdLoader = new MaxNativeAdLoader(BIG_Common_Utils.getRandomAdUnitId(responseMain.getLovinNativeID()), Big_MainActivity.this);
            nativeAdLoader.setNativeAdListener(new MaxNativeAdListener() {
                @Override
                public void onNativeAdLoaded(final MaxNativeAdView nativeAdView, final MaxAd ad) {
                    if (nativeAd != null) {
                        nativeAdLoader.destroy(nativeAd);
                    }
                    nativeAd = ad;
                    frameLayout.removeAllViews();
                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) frameLayout.getLayoutParams();
                    params.height = getResources().getDimensionPixelSize(R.dimen.dim_300);
                    params.width = LinearLayout.LayoutParams.MATCH_PARENT;
                    frameLayout.setLayoutParams(params);
                    frameLayout.setPadding((int) getResources().getDimension(R.dimen.dim_10), (int) getResources().getDimension(R.dimen.dim_10), (int) getResources().getDimension(R.dimen.dim_10), (int) getResources().getDimension(R.dimen.dim_10));
                    frameLayout.addView(nativeAdView);
                    frameLayout.setVisibility(View.VISIBLE);
                    lblLoadingAds.setVisibility(View.GONE);
                    BIG_AppLogger.getInstance().e("AppLovin Loaded: ", "===");
                }

                @Override
                public void onNativeAdLoadFailed(final String adUnitId, final MaxError error) {
                    frameLayout.setVisibility(View.GONE);
                    lblLoadingAds.setVisibility(View.GONE);
                    BIG_AppLogger.getInstance().e("AppLovin Failed: ", error.getMessage());
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

    private void initView() {
        btnWithdraw = findViewById(R.id.btnWithdraw);
        btnWithdraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (BIG_SharePrefs.getInstance().getBoolean(BIG_SharePrefs.IS_LOGIN)) {
                    startActivity(new Intent(Big_MainActivity.this, Big_WithdrawTypesActivity.class));
                } else {
                    BIG_Common_Utils.NotifyLogin(Big_MainActivity.this);
                }
            }
        });
        tvNextPayout = findViewById(R.id.tvNextPayout);
        tvWithdrawProgress = findViewById(R.id.tvWithdrawProgress);
        progressBarWithdraw = findViewById(R.id.progressBarWithdraw);
        ivTasks = findViewById(R.id.ivTasks);
        lottieViewTask = findViewById(R.id.lottieViewTask);
        lblGiveawayCode = findViewById(R.id.lblGiveawayCode);
        layoutGiveawayCode = findViewById(R.id.layoutGiveawayCode);
        rvSlider = findViewById(R.id.rvSlider);
        layoutSlider = findViewById(R.id.layoutSlider);
        layoutInflate = findViewById(R.id.layoutInflate);
        layoutHotOffers = findViewById(R.id.layoutHotOffers);
        ivAdjoe = findViewById(R.id.ivAdjoe);
//        lottieAdjoe = findViewById(R.id.lottieAdjoe);
//        imgStory = findViewById(R.id.ivStories);
        lottie_withdraw = findViewById(R.id.lottie_withdraw);
        withdraw_box=findViewById(R.id.withdraw_box);
        ivMenu = findViewById(R.id.ivMenu);
        ivMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDrawer();
            }
        });

        tvPoints = findViewById(R.id.tvPoints);
        layoutPoints = findViewById(R.id.layoutPoints);

        layoutPoints.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (BIG_SharePrefs.getInstance().getBoolean(BIG_SharePrefs.IS_LOGIN)) {
                    startActivity(new Intent(Big_MainActivity.this, Big_My_Wallet_Activity.class));
                } else {
                    BIG_Common_Utils.NotifyLogin(Big_MainActivity.this);
                }
            }
        });

        layoutHome = findViewById(R.id.layoutHome);
        layoutHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                performHomeClick();
            }
        });
        layoutReward = findViewById(R.id.layoutReward);
        layoutReward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                performRewardClick();
            }
        });
        layoutTasks = findViewById(R.id.layoutTasks);
        layoutTasks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                performTaskClick();
            }
        });
        layoutTasks1 = findViewById(R.id.layoutTasks1);
        layoutTasks1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                performTaskClick();
            }
        });
        layoutInvite = findViewById(R.id.layoutInvite);
        layoutInvite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                performInviteClick();
            }
        });
        layoutGames = findViewById(R.id.layoutGames);
        layoutGames.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                performGameClick();
            }
        });
        ivGames = findViewById(R.id.ivGames);
        ivHome = findViewById(R.id.ivHome);
        ivReward = findViewById(R.id.ivReward);
        ivInvite = findViewById(R.id.ivInvite);
        task_ic = findViewById(R.id.task_ic);
        lblHome = findViewById(R.id.lblHome);
        lblReward = findViewById(R.id.lblReward);
        tvRewardPoints = findViewById(R.id.tvRewardPoints);
        lblTasks = findViewById(R.id.lblTasks);
        lblInvite = findViewById(R.id.lblInvite);
        lblGames = findViewById(R.id.lblGames);

        ivHome.setImageResource(R.drawable.big_ic_home);
        lblHome.setTextColor(getColor(R.color.menu_selected));


        if (!BIG_Common_Utils.isStringNullOrEmpty(responseMain.getRewardLabel())) {
            tvRewardPoints.setText(responseMain.getRewardLabel());
        } else {
            tvRewardPoints.setVisibility(View.INVISIBLE);
        }
        ivAdjoeLeaderboard = findViewById(R.id.ivAdjoeLeaderboard);

        ivAdjoeLeaderboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Big_MainActivity.this, Big_AdjoeLeaderboardActivity.class));
            }
        });
    }

    public void performInviteClick() {
        try {
            isHomeSelected = false;

            ivInvite.setImageResource(R.drawable.big_ic_refer);
            lblInvite.setTextColor(getColor(R.color.menu_selected));

            ivHome.setImageResource(R.drawable.big_home_l);
            lblHome.setTextColor(getColor(R.color.grey_blue_black_font));

            task_ic.setImageResource(R.drawable.big_task_l);
            lblTasks.setTextColor(getColor(R.color.light_grey_font));

            ivReward.setImageResource(R.drawable.big_reward_ic);
            lblReward.setTextColor(getColor(R.color.light_grey_font));

            ivGames.setImageResource(R.drawable.big_profile_ic);
            lblGames.setTextColor(getColor(R.color.light_grey_font));
            startActivity(new Intent(Big_MainActivity.this, Big_ReferUsersActivity.class));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void performHomeClick() {
        try {
            isHomeSelected = true;
            ivInvite.setImageResource(R.drawable.big_refer_ic);
            lblInvite.setTextColor(getColor(R.color.light_grey_font));

            ivHome.setImageResource(R.drawable.big_ic_home);
            lblHome.setTextColor(getColor(R.color.menu_selected));

            task_ic.setImageResource(R.drawable.big_task_l);
            lblTasks.setTextColor(getColor(R.color.light_grey_font));

            ivReward.setImageResource(R.drawable.big_reward_ic);
            lblReward.setTextColor(getColor(R.color.light_grey_font));

            ivGames.setImageResource(R.drawable.big_profile_ic);
            lblGames.setTextColor(getColor(R.color.light_grey_font));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void performRewardClick() {
        try {
            isHomeSelected = false;
            ivInvite.setImageResource(R.drawable.big_refer_ic);
            lblInvite.setTextColor(getColor(R.color.light_grey_font));

            ivHome.setImageResource(R.drawable.big_home_l);
            lblHome.setTextColor(getColor(R.color.light_grey_font));

            task_ic.setImageResource(R.drawable.big_task_l);
            lblTasks.setTextColor(getColor(R.color.light_grey_font));

            ivReward.setImageResource(R.drawable.big_ic_earn);
            lblReward.setTextColor(getColor(R.color.menu_selected));

            ivGames.setImageResource(R.drawable.big_profile_ic);
            lblGames.setTextColor(getColor(R.color.light_grey_font));
            startActivity(new Intent(Big_MainActivity.this, Big_EarningOptionsActivity.class));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void performGameClick() {
        try {
            isHomeSelected = false;

            ivInvite.setImageResource(R.drawable.big_refer_ic);
            lblInvite.setTextColor(getColor(R.color.light_grey_font));

            ivHome.setImageResource(R.drawable.big_home_l);
            lblHome.setTextColor(getColor(R.color.light_grey_font));

            task_ic.setImageResource(R.drawable.big_task_l);
            lblTasks.setTextColor(getColor(R.color.light_grey_font));

            ivReward.setImageResource(R.drawable.big_reward_ic);
            lblReward.setTextColor(getColor(R.color.light_grey_font));

            ivGames.setImageResource(R.drawable.big_ic_profile);
            lblGames.setTextColor(getColor(R.color.menu_selected));
            startActivity(new Intent(Big_MainActivity.this, Big_UserProfileActivity.class));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void performTaskClick() {
        try {
            isHomeSelected = false;

            ivInvite.setImageResource(R.drawable.big_refer_ic);
            lblInvite.setTextColor(getColor(R.color.light_grey_font));

            ivHome.setImageResource(R.drawable.big_home_l);
            lblHome.setTextColor(getColor(R.color.light_grey_font));

            task_ic.setImageResource(R.drawable.big_ic_task);
            lblTasks.setTextColor(getColor(R.color.menu_selected));

            ivReward.setImageResource(R.drawable.big_reward_ic);
            lblReward.setTextColor(getColor(R.color.light_grey_font));

            ivGames.setImageResource(R.drawable.big_profile_ic);
            lblGames.setTextColor(getColor(R.color.light_grey_font));
            startActivity(new Intent(Big_MainActivity.this, Big_TasksCategoryTypeActivity.class));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setHomeData() {
        responseMain = new Gson().fromJson(BIG_SharePrefs.getInstance().getString(BIG_SharePrefs.HomeData), Big_Response_Model.class);
        setData();
    }


    @Override
    protected void onResume() {
        super.onResume();
        BIG_Common_Utils.initializeAdJoe(Big_MainActivity.this, false);
        setProfileData();
        performHomeClick();
        if (isTimerSet && isTimerOver && selectedQuickTaskPos >= 0) {
            new Big_SaveQuickTaskAsync(Big_MainActivity.this, quickTasksAdapter.listTasks.get(selectedQuickTaskPos).getPoints(), quickTasksAdapter.listTasks.get(selectedQuickTaskPos).getId());
        }
        isTimerSet = false;
        isTimerOver = false;
        new Big_GetWalletBalanceAsync(Big_MainActivity.this);
    }


    @Override
    protected void onStop() {
        super.onStop();
        if (isFinishing()) {
            try {
                if (handlerExit != null) {
                    handlerExit.removeCallbacksAndMessages(null);
                }
                if (nativeAdExit != null && nativeAdLoaderExit != null) {
                    nativeAdLoaderExit.destroy(nativeAdExit);
                    nativeAdExit = null;
                    frameLayoutExit = null;
                }
                if (nativeAd != null && nativeAdLoader != null) {
                    nativeAdLoader.destroy(nativeAd);
                    nativeAd = null;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void initSlideMenuUI() {
        drawer = findViewById(R.id.drawer_layout);
        nav_view_left = findViewById(R.id.nav_view_left);

        /* Left slide menu UI */

        ivProfilePic = nav_view_left.findViewById(R.id.ivProfilePic);
        LinearLayout layoutUserProfile = nav_view_left.findViewById(R.id.layoutUserProfile);
        layoutUserProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeDrawer();
                startActivity(new Intent(Big_MainActivity.this, Big_UserProfileActivity.class));
            }
        });
        tvName = nav_view_left.findViewById(R.id.tvName);
        tvEmail = nav_view_left.findViewById(R.id.tvEmail);
        setProfileData();

        TextView tvVersionName = nav_view_left.findViewById(R.id.tvVersionName);
        tvVersionName.setText("App Version " + BIG_Common_Utils.getVersionName(Big_MainActivity.this));

        ImageView menuAdBanner = nav_view_left.findViewById(R.id.menuAdBanner);
        if (responseMain.getMenuBanner() != null && !BIG_Common_Utils.isStringNullOrEmpty(responseMain.getMenuBanner().getImage())) {
            menuAdBanner.setVisibility(View.VISIBLE);
            Glide.with(Big_MainActivity.this).load(responseMain.getMenuBanner().getImage()).apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE)).into(menuAdBanner);
            menuAdBanner.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!BIG_Common_Utils.isStringNullOrEmpty(responseMain.getMenuBanner().getUrl())) {
                        BIG_Common_Utils.openUrl(Big_MainActivity.this, responseMain.getMenuBanner().getUrl());
                    }
                }
            });
        }

        LinearLayout layoutTelegram = nav_view_left.findViewById(R.id.layoutTelegram);
        layoutTelegram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BIG_Common_Utils.openUrl(Big_MainActivity.this, responseMain.getTelegramUrl());
            }
        });
        LinearLayout layoutYoutube = nav_view_left.findViewById(R.id.layoutYoutube);
        layoutYoutube.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BIG_Common_Utils.openUrl(Big_MainActivity.this, responseMain.getYoutubeUrl());
            }
        });
        LinearLayout layoutInstagram = nav_view_left.findViewById(R.id.layoutInstagram);
        layoutInstagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BIG_Common_Utils.openUrl(Big_MainActivity.this, responseMain.getInstagramUrl());
            }
        });
//        LinearLayout layoutMyWallet = nav_view_left.findViewById(R.id.layoutMyWallet);
//        layoutMyWallet.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (POC_SharePrefs.getInstance().getBoolean(POC_SharePrefs.IS_LOGIN)) {
//                    startActivity(new Intent(Big_MainActivity.this, Big_My_Wallet_Activity.class));
//                } else {
//                    POC_Common_Utils.NotifyLogin(Big_MainActivity.this);
//                }
//            }
//        });

//        LinearLayout layoutWithdrawalHistory = nav_view_left.findViewById(R.id.layoutWithdrawalHistory);
//        layoutWithdrawalHistory.setOnClickListener(new View.OnClickListener() {
//            @SuppressLint("ResourceType")
//            @Override
//            public void onClick(View view) {
//                if (POC_SharePrefs.getInstance().getBoolean(POC_SharePrefs.IS_LOGIN)) {
//                    Intent intent = new Intent(Big_MainActivity.this, Big_PointHistoryActivity.class);
//                    intent.putExtra("type", POC_Constants.HistoryType.WITHDRAW_HISTORY);
//                    intent.putExtra("title", "Withdrawal History");
//                    startActivity(intent);
//                } else {
//                    POC_Common_Utils.NotifyLogin(Big_MainActivity.this);
//                }
//            }
//        });
//        LinearLayout layoutWithdraw = nav_view_left.findViewById(R.id.layoutWithdraw);
//        layoutWithdraw.setOnClickListener(new View.OnClickListener() {
//            @SuppressLint("ResourceAsColor")
//            @Override
//            public void onClick(View view) {
//                if (POC_SharePrefs.getInstance().getBoolean(POC_SharePrefs.IS_LOGIN)) {
////                    layoutWithdrawalHistory.setBackground(null);
//                    Intent inSpin = new Intent(Big_MainActivity.this, Big_WithdrawTypesActivity.class);
//                    startActivity(inSpin);
//                } else {
//                    POC_Common_Utils.NotifyLogin(Big_MainActivity.this);
//                }
//            }
//        });

        RecyclerView rvMenuList = nav_view_left.findViewById(R.id.rvMenuList);
        if (responseMain.getSideMenuList() != null && responseMain.getSideMenuList().size() > 0) {
            List<Big_DrawerMenuParentView> listSideMenu = new ArrayList<>();
            for (Big_Menu_ListItem objMenu : responseMain.getSideMenuList()) {
                List<Big_DrawerMenuChildView> subMenuList = new ArrayList<>();
                if (objMenu.getSubMenuList() != null && objMenu.getSubMenuList().size() > 0) {
                    for (Big_Sub_MenuList_Item objSubMenu : objMenu.getSubMenuList()) {
                        subMenuList.add(new Big_DrawerMenuChildView(objSubMenu));
                    }
                }
                Big_DrawerMenuParentView obj = new Big_DrawerMenuParentView(objMenu, subMenuList);
                listSideMenu.add(obj);
            }
            Big_DrawerMenuAdapter mAdapter = new Big_DrawerMenuAdapter(this, listSideMenu);
            rvMenuList.setAdapter(mAdapter);
            rvMenuList.setLayoutManager(new LinearLayoutManager(this));
            rvMenuList.setVisibility(View.VISIBLE);
        } else {
            rvMenuList.setVisibility(View.GONE);
        }


    }

    private void setProfileData() {
        try {

            tvPoints.setText(BIG_SharePrefs.getInstance().getEarningPointString());
            if (BIG_SharePrefs.getInstance().getBoolean(BIG_SharePrefs.IS_LOGIN)) {
                try {
                    BIG_AppLogger.getInstance().e("#profile1", "dsssss");
                    Big_UserProfileDetails userDetails = new Gson().fromJson(BIG_SharePrefs.getInstance().getString(BIG_SharePrefs.User_Details), Big_UserProfileDetails.class);

                    tvEmail.setText(userDetails.getEmailId());
                    BIG_AppLogger.getInstance().e("#tvEmail", userDetails.getEmailId());
                    tvName.setText(userDetails.getFirstName() + " " + userDetails.getLastName());
                    BIG_AppLogger.getInstance().e("#tvName", userDetails.getFirstName() + "==" + userDetails.getLastName());

                    if (userDetails.getProfileImage() != null) {
                        Glide.with(Big_MainActivity.this).load(userDetails.getProfileImage()).override(getResources().getDimensionPixelSize(R.dimen.dim_90), getResources().getDimensionPixelSize(R.dimen.dim_90)).into(ivProfilePic);
                    }

                } catch (Exception e) {
                    e.printStackTrace();

                }
            } else {
                tvEmail.setVisibility(View.GONE);
                tvName.setText("Login / Signup");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void openDrawer() {
        try {
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else {
                drawer.openDrawer(GravityCompat.START);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void closeDrawer() {
        try {
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        try {
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else if (!isHomeSelected) {
                performHomeClick();
            } else if (doubleBackToExitPressedOnce) {
                if (BIG_Common_Utils.isShowAppLovinAds() && responseMain.getIsBackAdsInterstitial() != null) {
                    if (responseMain.getIsBackAdsInterstitial().equals(BIG_Constants.APPLOVIN_INTERSTITIAL)) {
                        BIG_Ads_Utils.showAppLovinInterstitialAd(Big_MainActivity.this, new BIG_Ads_Utils.AdShownListener() {
                            @Override
                            public void onAdDismiss() {
                                showFinalExitPopup();
                            }
                        });
                    } else if (responseMain.getIsBackAdsInterstitial().equals(BIG_Constants.APPLOVIN_REWARD)) {
                        BIG_Ads_Utils.showAppLovinRewardedAd(Big_MainActivity.this, new BIG_Ads_Utils.AdShownListener() {
                            @Override
                            public void onAdDismiss() {
                                showFinalExitPopup();
                            }
                        });
                    } else {
                        BIG_Common_Utils.logFirebaseEvent(Big_MainActivity.this, "FeatureUsabilityItemId", "FeatureUsabilityEvent", "Home_BigPrize", "Interstitial Ad Not Loaded -> Exit");
                        exitApp();
                    }
                } else {
                    BIG_Common_Utils.logFirebaseEvent(Big_MainActivity.this, "FeatureUsabilityItemId", "FeatureUsabilityEvent", "Home_BigPrize", "Not Show Ad -> Exit");
                    exitApp();
                }
            } else {
                showExitDialog(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showFinalExitPopup() {
        try {
            dialogExitDialogAfterInterstitial = new Dialog(Big_MainActivity.this, android.R.style.Theme_Light);
            dialogExitDialogAfterInterstitial.getWindow().setBackgroundDrawableResource(R.color.black_transparent);
            dialogExitDialogAfterInterstitial.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialogExitDialogAfterInterstitial.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            dialogExitDialogAfterInterstitial.setContentView(R.layout.big_popup_app_exit_after_interstitial);
            dialogExitDialogAfterInterstitial.setCancelable(true);
            TextView tvTitle = dialogExitDialogAfterInterstitial.findViewById(R.id.tvTitle);
            tvTitle.setText("Thank You For Using\n" + getString(R.string.app_name) + "!");

            ImageView ivClose = dialogExitDialogAfterInterstitial.findViewById(R.id.ivClose);
            ivClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialogExitDialogAfterInterstitial.dismiss();
                }
            });
            dialogExitDialogAfterInterstitial.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {
                    if (handlerExit != null) handlerExit.removeCallbacksAndMessages(null);
                }
            });
            dialogExitDialogAfterInterstitial.setOnKeyListener(new Dialog.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialog1, int keyCode, KeyEvent event) {
                    if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                        dialogExitDialogAfterInterstitial.dismiss();
                    }
                    return true;
                }
            });

            Button btnOk = dialogExitDialogAfterInterstitial.findViewById(R.id.btnOk);
            btnOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    BIG_Common_Utils.logFirebaseEvent(Big_MainActivity.this, "FeatureUsabilityItemId", "FeatureUsabilityEvent", "Home_BigPrize", "Show Interstitial Ad -> Exit");
                    exitApp();
                }
            });

            dialogExitDialogAfterInterstitial.show();
            ProgressView progressView = dialogExitDialogAfterInterstitial.findViewById(R.id.progressBar);
            progressView.progressAnimate();
            progressView.setProgress(100);
            handlerExit = new Handler(Looper.getMainLooper());
            handlerExit.postDelayed(new Runnable() {
                @Override
                public void run() {
                    BIG_Common_Utils.logFirebaseEvent(Big_MainActivity.this, "FeatureUsabilityItemId", "FeatureUsabilityEvent", "Home_BigPrize", "Show Interstitial Ad -> Exit");
                    exitApp();
                }
            }, 2500);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void exitApp() {
        try {
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
            if (dialogExitDialogAfterInterstitial != null && dialogExitDialogAfterInterstitial.isShowing()) {
                dialogExitDialogAfterInterstitial.dismiss();
            }
            finishAffinity();
            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showExitDialog(boolean isShow) {
        try {
            if (isShow && BIG_Common_Utils.isShowAppLovinAds() && responseMain.getIsBackAdsInterstitial() != null && !responseMain.getIsBackAdsInterstitial().equals("0")) {
                try {
                    doubleBackToExitPressedOnce = true;
                    BIG_Common_Utils.setToast(this, getString(R.string.tap_to_exit));
                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            doubleBackToExitPressedOnce = false;
                        }
                    }, 2000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return;
            }
            if (isShow && ((responseMain.getExitDialog() == null && responseMain.getIsShowNativeAdsOnAppExit() != null && responseMain.getIsShowNativeAdsOnAppExit().equals("0")) || isExitNativeNotLoaded)) {
                try {
                    doubleBackToExitPressedOnce = true;
                    BIG_Common_Utils.setToast(this, getString(R.string.tap_to_exit));
                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            doubleBackToExitPressedOnce = false;
                        }
                    }, 2000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return;
            }
            if (dialog == null) {
                dialog = new Dialog(Big_MainActivity.this, android.R.style.Theme_Light);
                dialog.getWindow().setBackgroundDrawableResource(R.color.black_transparent);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                dialog.setContentView(R.layout.big_popup_app_exit);
                dialog.setCancelable(false);


                LinearLayout layoutParent = dialog.findViewById(R.id.layoutParent);
                frameLayoutExit = dialog.findViewById(R.id.fl_adplaceholder);
                TextView tvTapAgainToExit = dialog.findViewById(R.id.tvTapAgainToExit);
                if (responseMain.getExitDialog() != null) {
                    View viewCustomAd = getLayoutInflater().inflate(R.layout.big_ad_exit_dialog_custom_ad, null);
                    ImageView ivExitDialogImage = viewCustomAd.findViewById(R.id.ad_media);
                    LottieAnimationView ivLottieView = dialog.findViewById(R.id.ivLottieView);
                    if (!BIG_Common_Utils.isStringNullOrEmpty(responseMain.getExitDialog().getImage())) {
                        if (responseMain.getExitDialog().getImage().contains("json")) {
                            ivExitDialogImage.setVisibility(View.GONE);
                            ivLottieView.setVisibility(View.VISIBLE);
                            BIG_Common_Utils.setLottieAnimation(ivLottieView, responseMain.getExitDialog().getImage());
                            ivLottieView.setRepeatCount(LottieDrawable.INFINITE);
                            ivLottieView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    BIG_Common_Utils.Redirect(Big_MainActivity.this, responseMain.getExitDialog().getScreenNo(), responseMain.getExitDialog().getTitle(), responseMain.getExitDialog().getUrl(), null, null, responseMain.getExitDialog().getImage());
                                }
                            });
                        } else {
                            Glide.with(Big_MainActivity.this).load(responseMain.getExitDialog().getImage()).apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE)).into(ivExitDialogImage);
                            ivExitDialogImage.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    BIG_Common_Utils.Redirect(Big_MainActivity.this, responseMain.getExitDialog().getScreenNo(), responseMain.getExitDialog().getTitle(), responseMain.getExitDialog().getUrl(), null, null, responseMain.getExitDialog().getImage());
                                }
                            });
                        }
                    } else {
                        ivExitDialogImage.setVisibility(View.GONE);
                    }
                    TextView tvTitle = viewCustomAd.findViewById(R.id.ad_headline);
                    if (!BIG_Common_Utils.isStringNullOrEmpty(responseMain.getExitDialog().getTitle())) {
                        tvTitle.setText(responseMain.getExitDialog().getTitle());
                    } else {
                        tvTitle.setVisibility(View.GONE);
                    }
                    TextView tvDescription = viewCustomAd.findViewById(R.id.ad_body);
                    if (!BIG_Common_Utils.isStringNullOrEmpty(responseMain.getExitDialog().getDescription())) {
                        tvDescription.setText(responseMain.getExitDialog().getDescription());
                    } else {
                        tvDescription.setVisibility(View.GONE);
                    }
                    Button btnAction = viewCustomAd.findViewById(R.id.ad_call_to_action);
                    if (!BIG_Common_Utils.isStringNullOrEmpty(responseMain.getExitDialog().getBtnName())) {
                        btnAction.setText(responseMain.getExitDialog().getBtnName());
                    }
                    if (!BIG_Common_Utils.isStringNullOrEmpty(responseMain.getExitDialog().getBtnColor())) {
                        btnAction.getBackground().setTint(Color.parseColor(responseMain.getExitDialog().getBtnColor()));
                    }

                    btnAction.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            BIG_Common_Utils.Redirect(Big_MainActivity.this, responseMain.getExitDialog().getScreenNo(), responseMain.getExitDialog().getTitle(), responseMain.getExitDialog().getUrl(), null, null, responseMain.getExitDialog().getImage());
                        }
                    });
                    // Ensure that the parent view doesn't already contain an ad view.
                    frameLayoutExit.removeAllViews();
                    // Place the AdView into the parent.
                    frameLayoutExit.addView(viewCustomAd);
                    frameLayoutExit.setVisibility(View.VISIBLE);
                } else {
                    if (responseMain.getIsShowNativeAdsOnAppExit() != null && responseMain.getIsShowNativeAdsOnAppExit().equals(BIG_Constants.APPlOVIN_AD)) {
                        loadAppLovinNativeAdsExit();
                    } else {
                        isExitNativeNotLoaded = true;
                    }
                }
                tvTapAgainToExit.setOnClickListener(view -> {
                    exitApp();
                });
                layoutParent.setOnClickListener(view -> {
                    dialog.dismiss();
                });
            }
            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    doubleBackToExitPressedOnce = false;
                }
            });
            dialog.setOnKeyListener(new Dialog.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialog1, int keyCode, KeyEvent event) {
                    if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                        if (doubleBackToExitPressedOnce) {
                            BIG_Common_Utils.logFirebaseEvent(Big_MainActivity.this, "FeatureUsabilityItemId", "FeatureUsabilityEvent", "Home_BigPrize", isExitNativeNotLoaded ? "Exit Dialog With Custom Ad -> Exit" : "Exit Dialog With Native Ad -> Exit");
                            exitApp();
                        }
                    }
                    return true;
                }
            });
            if (isShow && !dialog.isShowing()) {
                doubleBackToExitPressedOnce = true;
                dialog.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadAppLovinNativeAdsExit() {
        try {
            nativeAdLoaderExit = new MaxNativeAdLoader(BIG_Common_Utils.getRandomAdUnitId(responseMain.getLovinNativeID()), Big_MainActivity.this);
            nativeAdLoaderExit.setNativeAdListener(new MaxNativeAdListener() {
                @Override
                public void onNativeAdLoaded(final MaxNativeAdView nativeAdView, final MaxAd ad) {
                    if (nativeAdExit != null) {
                        nativeAdLoaderExit.destroy(nativeAdExit);
                    }
                    nativeAdExit = ad;
                    frameLayoutExit.removeAllViews();

                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) frameLayoutExit.getLayoutParams();
                    params.height = getResources().getDimensionPixelSize(R.dimen.dim_300);
                    params.width = LinearLayout.LayoutParams.MATCH_PARENT;
                    frameLayoutExit.setLayoutParams(params);
                    frameLayoutExit.setPadding((int) getResources().getDimension(R.dimen.dim_10), (int) getResources().getDimension(R.dimen.dim_10), (int) getResources().getDimension(R.dimen.dim_10), (int) getResources().getDimension(R.dimen.dim_10));
                    frameLayoutExit.setBackgroundResource(R.drawable.big_rectangle_white);
                    frameLayoutExit.addView(nativeAdView);
                    frameLayoutExit.setVisibility(View.VISIBLE);

                }

                @Override
                public void onNativeAdLoadFailed(final String adUnitId, final MaxError error) {
                    //AppLogger.getInstance().e("AppLovin Failed: ", error.getMessage());
                    isExitNativeNotLoaded = true;
                }

                @Override
                public void onNativeAdClicked(final MaxAd ad) {

                }
            });
            nativeAdLoaderExit.loadAd();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void doLogout() {
        BIG_Common_Utils.doLogout(Big_MainActivity.this);
        finishAffinity();
    }

    private void openScanScreen() {
        BIG_Common_Utils.Redirect(Big_MainActivity.this, "47", "", "", "", "", "");
    }

    private void updateNextWithdrawAmount() {
        try {
            responseMain = new Gson().fromJson(BIG_SharePrefs.getInstance().getString(BIG_SharePrefs.HomeData), Big_Response_Model.class);

            tvNextPayout.setText("₹ " + responseMain.getNextWithdrawAmount());
            tvWithdrawProgress.setText(BIG_SharePrefs.getInstance().getEarningPointString() + "/" + (Integer.parseInt(responseMain.getNextWithdrawAmount()) * Integer.parseInt(responseMain.getPointValue())));


            linearProgress = findViewById(R.id.linearProgress);
            int height = linearProgress.getHeight();
            int width = linearProgress.getWidth();
            if (width != 0) {
                ViewGroup.LayoutParams params = progressBarWithdraw.getLayoutParams();
                params.height = height;
                if (Integer.parseInt(BIG_SharePrefs.getInstance().getEarningPointString()) >= (Integer.parseInt(responseMain.getNextWithdrawAmount()) * Integer.parseInt(responseMain.getPointValue()))) {
                    params.width = params.MATCH_PARENT;
                } else {
                    params.width = (width * Integer.parseInt(BIG_SharePrefs.getInstance().getEarningPointString())) / (Integer.parseInt(responseMain.getNextWithdrawAmount()) * Integer.parseInt(responseMain.getPointValue()));

                }
                progressBarWithdraw.setLayoutParams(params);
                progressBarWithdraw.playAnimation();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private BroadcastReceiver dataChangedBroadcast;
    private IntentFilter intentFilter;

    public void registerBroadcast() {
        if (dataChangedBroadcast == null) {
            dataChangedBroadcast = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    //AppLogger.getInstance().e("WATCH WEBSITE", "Broadcast Received==" + intent.getAction());
                    String id = intent.getExtras().getString("id");
                   if (intent.getAction().equals(BIG_Constants.QUICK_TASK_RESULT)) {
                        if (intent.getExtras().getString("status").equals(BIG_Constants.STATUS_SUCCESS)) {
                            for (int i = 0; i < quickTasksAdapter.listTasks.size(); i++) {
                                if (quickTasksAdapter.listTasks.get(i).getId().equals(id)) {
                                    quickTasksAdapter.listTasks.remove(i);
                                    quickTasksAdapter.notifyDataSetChanged();
                                    break;
                                }
                            }
                            tvPoints.setText(BIG_SharePrefs.getInstance().getEarningPointString());
                            updateNextWithdrawAmount();
                            if (quickTasksAdapter.listTasks.size() == 0) {
                                layoutInflate.removeView(quickTaskView);
                            }
                        }
                        selectedQuickTaskPos = -1;
                    }
                }
            };
            intentFilter = new IntentFilter();
            intentFilter.addAction(BIG_Constants.QUICK_TASK_RESULT);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                registerReceiver(dataChangedBroadcast, intentFilter, RECEIVER_NOT_EXPORTED);
            } else {
                registerReceiver(dataChangedBroadcast, intentFilter);
            }
        }
    }

    public void onUpdateWalletBalance() {
        tvPoints.setText(BIG_SharePrefs.getInstance().getEarningPointString());
        updateNextWithdrawAmount();
    }

    private void showWelcomeBonusPopup(String points) {
        Dialog dialogWin = new Dialog(Big_MainActivity.this, android.R.style.Theme_Light);
        dialogWin.getWindow().setBackgroundDrawableResource(R.color.black_transparent);
        dialogWin.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogWin.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        dialogWin.setCancelable(false);
        dialogWin.setCanceledOnTouchOutside(false);
        dialogWin.setContentView(R.layout.big_popup_welcome_bonus);
        dialogWin.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

        TextView tvPoint = dialogWin.findViewById(R.id.tvPoints);
//       tvPoint.setText(points);

        LottieAnimationView animation_view = dialogWin.findViewById(R.id.animation_view);
        animation_view.addAnimatorListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation, boolean isReverse) {
                super.onAnimationStart(animation, isReverse);
                BIG_Common_Utils.startTextCountAnimation(tvPoint, points);
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
        BIG_Common_Utils.setLottieAnimation(animation_view, responseMain.getCelebrationLottieUrl());

        TextView lblPoints = dialogWin.findViewById(R.id.lblPoints);
        try {
            int pt = Integer.parseInt(points);
            lblPoints.setText((pt <= 1 ? "Ruby" : "Rubies"));
        } catch (Exception e) {
            e.printStackTrace();
            lblPoints.setText("Rubies");
        }
        AppCompatButton btnOk = dialogWin.findViewById(R.id.btnOk);
        btnOk.setText("Ok");
        btnOk.setOnClickListener(v -> {
            BIG_Ads_Utils.showAppLovinInterstitialAd(Big_MainActivity.this, new BIG_Ads_Utils.AdShownListener() {
                @Override
                public void onAdDismiss() {
                    if (dialogWin != null) {
                        dialogWin.dismiss();
                    }
                }
            });
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
            BIG_SharePrefs.getInstance().putBoolean(BIG_SharePrefs.IS_WELCOME_POPUP_SHOWN, true);
        }
    }

    private void startQuickTaskTimer(List<Big_Home_Data_Item> data, int position) {
        if (timerQuickTask != null) {
            timerQuickTask.cancel();
        }
        timerQuickTask = new CountDownTimer(Integer.parseInt(data.get(position).getDelay()) * 1000L, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                isTimerOver = true;
                timerQuickTask.cancel();
                timerQuickTask = null;
            }
        };
        isTimerSet = true;
        timerQuickTask.start();
    }

    public static void printHashKey(Context pContext) {
        try {
            PackageInfo info = pContext.getPackageManager().getPackageInfo(pContext.getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String hashKey = new String(Base64.encode(md.digest(), 0));
                Log.i(TAG, "printHashKey() Hash Key: " + hashKey);
            }
        } catch (NoSuchAlgorithmException e) {
            Log.e(TAG, "printHashKey()", e);
        } catch (Exception e) {
            Log.e(TAG, "printHashKey()", e);
        }
    }

    private void showBottomDialog(List<BottomGrid> bottomGrid) {

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.big_home_bottom_sheet_layout);
        TextView homeBottomTitle = dialog.findViewById(R.id.homeBottomTitle);
        TextView homeBottomDesc = dialog.findViewById(R.id.homeBottomDesc);
        ImageView homeBottomClose = dialog.findViewById(R.id.homeBottomClose);
        RecyclerView rvHomeBottomList = dialog.findViewById(R.id.rvHomeBottomList);

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.big_bottom_dialog));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);

        homeBottomTitle.setText(responseMain.getBottomGridTitle());
        homeBottomDesc.setText(responseMain.getBottomGridDesc());

        Big_HomeBottomSheet homeBottomSheet = new Big_HomeBottomSheet(Big_MainActivity.this, bottomGrid, new Big_HomeBottomSheet.ClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                BIG_Common_Utils.Redirect(Big_MainActivity.this, responseMain.getBottomGrid().get(position).getScreenNo(), "", "", "", "", "");
            }
        });
        rvHomeBottomList.setAdapter(homeBottomSheet);
        rvHomeBottomList.setLayoutManager(new GridLayoutManager(getApplicationContext(), Integer.parseInt(responseMain.getBottomGridSpan())));
//        rvHomeBottomList.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL,false));
        homeBottomClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }


}