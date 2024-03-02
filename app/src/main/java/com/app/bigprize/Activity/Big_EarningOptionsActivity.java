package com.app.bigprize.Activity;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieDrawable;
import com.app.bigprize.Adapter.Big_DailyClaimAdapter;
import com.app.bigprize.Adapter.Big_EarnGridAdapterReward;
import com.app.bigprize.Adapter.Big_HomeGiveawayCodesAdapter;
import com.app.bigprize.Adapter.Big_Home_Single_Slider_Adapter;
import com.app.bigprize.Adapter.Big_Home_Task_Offer_List_Adapter;
import com.app.bigprize.Adapter.Big_QuickTasksAdapter;
import com.app.bigprize.Async.Big_GetWalletBalanceAsync;
import com.app.bigprize.Async.Big_Get_Reward_Screen_Async;
import com.app.bigprize.Async.Big_SaveQuickTaskAsync;
import com.app.bigprize.Async.Models.Big_Home_Data_Item;
import com.app.bigprize.Async.Models.Big_Home_Data_List_Item;
import com.app.bigprize.Async.Models.Big_Response_Model;
import com.app.bigprize.Async.Models.Big_Reward_Screen_Model;
import com.app.bigprize.Async.Models.Big_Story_View_Item;
import com.app.bigprize.Customviews.Big_Story_View;
import com.app.bigprize.Customviews.Big_Storyview.Big_callback.Big_OnStory_Changed_Callback;
import com.app.bigprize.Customviews.Big_Storyview.Big_callback.Big_Story_Click_Listeners;
import com.app.bigprize.R;
import com.app.bigprize.Value.BIG_Constants;
import com.app.bigprize.utils.BIG_Activity_Manager;
import com.app.bigprize.utils.BIG_AppLogger;
import com.app.bigprize.utils.BIG_Common_Utils;
import com.app.bigprize.utils.BIG_SharePrefs;
import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxError;
import com.applovin.mediation.nativeAds.MaxNativeAdListener;
import com.applovin.mediation.nativeAds.MaxNativeAdLoader;
import com.applovin.mediation.nativeAds.MaxNativeAdView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.gson.Gson;
import com.skydoves.balloon.ArrowOrientation;
import com.skydoves.balloon.ArrowPositionRules;
import com.skydoves.balloon.Balloon;
import com.skydoves.balloon.BalloonAnimation;
import com.skydoves.balloon.BalloonSizeSpec;
import com.skydoves.balloon.OnBalloonClickListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Big_EarningOptionsActivity extends AppCompatActivity {

    private Big_Response_Model responseMain;
    private ImageView ivMenu, ivNotification, ivAdjoe, ivDice;
    private TextView tvPoints, tvTimer, lblGiveawayCode, tvTimerDailyTarget;
    private Balloon balloon;
    private LinearLayout layoutInflate, layoutPoints, layoutTimer, layoutGiveawayCode;
    private RecyclerView rvDailyLoginList;
    private Big_DailyClaimAdapter dailyLoginAdapter;
    public static Big_Reward_Screen_Model objRewardScreenModel;
    private MaxAd nativeAd;
    private MaxNativeAdLoader nativeAdLoader;
    private ImageView ivAdjoeLeaderboard;
    private String todayDate, endDate;
    private CountDownTimer timer;
    private Animation blinkAnimation;
    private View viewMilestones, viewDailyTarget;
    private int time;
    private boolean isTimerSet = false, isTimerOver = false;
    private NestedScrollView scroll;
    private LottieAnimationView lottieAdjoe;

    private View quickTaskView;
    private Big_QuickTasksAdapter quickTasksAdapter;
    private int selectedQuickTaskPos = -1, selectedDailyTargetPos = -1;
    private CountDownTimer timerQuickTask;
    private CountDownTimer timerDailyTarget;
    private int timeDailyTarget;
    private String todayDateDailyTarget, endDateDailyTarget;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        BIG_Common_Utils.setDayNightTheme(Big_EarningOptionsActivity.this);
        setContentView(R.layout.big_activity_earning_options);

        responseMain = new Gson().fromJson(BIG_SharePrefs.getInstance().getString(BIG_SharePrefs.HomeData), Big_Response_Model.class);

        lottieAdjoe = findViewById(R.id.lottieAdjoe);
        ivAdjoe = findViewById(R.id.ivAdjoe);
        lblGiveawayCode = findViewById(R.id.lblGiveawayCode);
        layoutGiveawayCode = findViewById(R.id.layoutGiveawayCode);
        blinkAnimation = new AlphaAnimation(0.3f, 1.0f);
        blinkAnimation.setDuration(500); //You can manage the blinking time with this parameter
        blinkAnimation.setStartOffset(20);
        blinkAnimation.setRepeatMode(Animation.REVERSE);
        blinkAnimation.setRepeatCount(Animation.INFINITE);

        ivMenu = findViewById(R.id.ivMenu);
        ivMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        layoutPoints = findViewById(R.id.layoutPoints);
        layoutPoints.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (BIG_SharePrefs.getInstance().getBoolean(BIG_SharePrefs.IS_LOGIN)) {
                    startActivity(new Intent(Big_EarningOptionsActivity.this, Big_My_Wallet_Activity.class));
                } else {
                    BIG_Common_Utils.NotifyLogin(Big_EarningOptionsActivity.this);
                }
            }
        });
        ivAdjoeLeaderboard = findViewById(R.id.ivAdjoeLeaderboard);

        ivAdjoeLeaderboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Big_EarningOptionsActivity.this, Big_AdjoeLeaderboardActivity.class));
            }
        });
        try {
            if (!BIG_Common_Utils.isStringNullOrEmpty(responseMain.getIsShowPlaytimeIcone()) && responseMain.getIsShowPlaytimeIcone().equalsIgnoreCase("1")) {
                if (!BIG_Common_Utils.isStringNullOrEmpty(responseMain.getImageAdjoeIcon())) {
                    if (responseMain.getImageAdjoeIcon().endsWith(".json")) {
                        lottieAdjoe.setVisibility(View.VISIBLE);
                        lottieAdjoe.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                BIG_Common_Utils.openAdjoeOfferWall(Big_EarningOptionsActivity.this);
                            }
                        });
                        BIG_Common_Utils.setLottieAnimation(lottieAdjoe, responseMain.getImageAdjoeIcon());
                    } else {
                        ivAdjoe.setVisibility(View.VISIBLE);
                        ivAdjoe.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                BIG_Common_Utils.openAdjoeOfferWall(Big_EarningOptionsActivity.this);
                            }
                        });
                        Glide.with(Big_EarningOptionsActivity.this)
                                .load(responseMain.getImageAdjoeIcon())
                                .override(getResources().getDimensionPixelSize(R.dimen.dim_32))
                                .into(ivAdjoe);
                    }
                }
            } else {
                ivAdjoe.setVisibility(View.GONE);
                lottieAdjoe.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!BIG_Common_Utils.isStringNullOrEmpty(responseMain.getIsShowAdjoeLeaderboardIcon()) && responseMain.getIsShowAdjoeLeaderboardIcon().equals("1")) {
            ivAdjoeLeaderboard.setVisibility(View.VISIBLE);
        } else {
            ivAdjoeLeaderboard.setVisibility(View.GONE);
        }


        scroll = findViewById(R.id.scroll);
        layoutInflate = findViewById(R.id.layoutInflate);
        tvPoints = findViewById(R.id.tvPoints);
        layoutPoints = findViewById(R.id.layoutPoints);

        layoutPoints.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (BIG_SharePrefs.getInstance().getBoolean(BIG_SharePrefs.IS_LOGIN)) {
                    startActivity(new Intent(Big_EarningOptionsActivity.this, Big_My_Wallet_Activity.class));
                } else {
                    BIG_Common_Utils.NotifyLogin(Big_EarningOptionsActivity.this);
                }
            }
        });

        new Big_Get_Reward_Screen_Async(Big_EarningOptionsActivity.this);
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            if (!isTimerSet && !isTimerOver && selectedQuickTaskPos < 0) {
                new Big_Get_Reward_Screen_Async(Big_EarningOptionsActivity.this);
            }
            tvPoints.setText(BIG_SharePrefs.getInstance().getEarningPointString());
            int lastClaimDay = Integer.parseInt(objRewardScreenModel.getDailyBonus().getLastClaimedDay());
            dailyLoginAdapter.setLastClaimedData(lastClaimDay, Integer.parseInt(objRewardScreenModel.getDailyBonus().getIsTodayClaimed()));
            if (lastClaimDay > 3) {
                LinearLayoutManager llm = (LinearLayoutManager) rvDailyLoginList.getLayoutManager();
                llm.scrollToPositionWithOffset(lastClaimDay - 1, 0);
            }
        } catch (Exception e) {
            //e.printStackTrace();
        }
        try {
            if (isTimerSet && isTimerOver && selectedQuickTaskPos >= 0) {
                new Big_SaveQuickTaskAsync(Big_EarningOptionsActivity.this, quickTasksAdapter.listTasks.get(selectedQuickTaskPos).getPoints(), quickTasksAdapter.listTasks.get(selectedQuickTaskPos).getId());

            }
            isTimerSet = false;
            isTimerOver = false;
        } catch (Exception e) {
            e.printStackTrace();
        }
        new Big_GetWalletBalanceAsync(Big_EarningOptionsActivity.this);

    }

    private void inflateRewardScreenData(String type, Big_Home_Data_List_Item categoryModel) {
        switch (type) {
            case BIG_Constants.RewardDataType.DAILY_BONUS:
                BIG_AppLogger.getInstance().e("Reward Screen DATA --)", "" + type);
                View viewDailyLogin = getLayoutInflater().inflate(R.layout.big_inflate_reward_daily_login, null);
                TextView lblDailyLoginSubTitle = viewDailyLogin.findViewById(R.id.lblSubTitle);
                lblDailyLoginSubTitle.setText(categoryModel.getSubTitle());
                rvDailyLoginList = viewDailyLogin.findViewById(R.id.rvDailyLoginList);
                if (objRewardScreenModel.getDailyBonus() != null && objRewardScreenModel.getDailyBonus().getData() != null && objRewardScreenModel.getDailyBonus().getData().size() > 0) {

                    int lastClaimDay = Integer.parseInt(objRewardScreenModel.getDailyBonus().getLastClaimedDay());
                    dailyLoginAdapter = new Big_DailyClaimAdapter(objRewardScreenModel.getDailyBonus().getData(), Big_EarningOptionsActivity.this, lastClaimDay, Integer.parseInt(objRewardScreenModel.getDailyBonus().getIsTodayClaimed()), new Big_DailyClaimAdapter.ClickListener() {
                        @Override
                        public void onItemClick(int position, View v) {
                            if (categoryModel.getIsActive() != null && categoryModel.getIsActive().equals("0")) {
                                BIG_Common_Utils.Notify(Big_EarningOptionsActivity.this, getString(R.string.app_name), categoryModel.getNotActiveMessage(), false);
                            } else {
                                startActivity(new Intent(Big_EarningOptionsActivity.this, Big_ActivityDailyLogin.class)
                                        .putExtra("objRewardScreenModel", objRewardScreenModel)
                                        .putExtra("title", categoryModel.getTitle())
                                        .putExtra("subTitle", categoryModel.getSubTitle()));
                            }
                        }
                    });
                    rvDailyLoginList.setAdapter(dailyLoginAdapter);
                    if (lastClaimDay > 3) {
                        LinearLayoutManager llm = (LinearLayoutManager) rvDailyLoginList.getLayoutManager();
                        llm.scrollToPositionWithOffset(lastClaimDay - 1, 0);
                    }
                } else {

                    rvDailyLoginList.setVisibility(View.GONE);
                }
                layoutInflate.addView(viewDailyLogin);
                break;
            case BIG_Constants.RewardDataType.GRID:
                BIG_AppLogger.getInstance().e("Reward Screen DATA --)", "" + type);
                View viewEarnGrid = getLayoutInflater().inflate(R.layout.big_inflate_home_general_layout, null);
                RecyclerView gridList = viewEarnGrid.findViewById(R.id.rvIconlist);
                TextView earnGridHeader = viewEarnGrid.findViewById(R.id.txtTitleHeader);
                earnGridHeader.setVisibility(View.GONE);

                Big_EarnGridAdapterReward earnGridAdapter = new Big_EarnGridAdapterReward(Big_EarningOptionsActivity.this, categoryModel.getData(), new Big_EarnGridAdapterReward.ClickListener() {
                    @Override
                    public void onItemClick(int position, View v) {
                        BIG_Common_Utils.Redirect(Big_EarningOptionsActivity.this, categoryModel.getData().get(position).getScreenNo(), categoryModel.getData().get(position).getTitle(), categoryModel.getData().get(position).getUrl(), categoryModel.getData().get(position).getId(), categoryModel.getData().get(position).getTaskId(), categoryModel.getData().get(position).getImage());
                    }
                });
                gridList.setLayoutManager(new GridLayoutManager(Big_EarningOptionsActivity.this, 2));
                gridList.setAdapter(earnGridAdapter);
                layoutInflate.addView(viewEarnGrid);

                break;

            case BIG_Constants.RewardDataType.SINGLE_SLIDER:
                BIG_AppLogger.getInstance().e("Reward Screen DATA --)", "" + type);
                View iconSingleSlider = getLayoutInflater().inflate(R.layout.big_inflate_home_general_layout, null);
                RecyclerView rvSliderlist = iconSingleSlider.findViewById(R.id.rvIconlist);
                TextView txtHeader = iconSingleSlider.findViewById(R.id.txtTitleHeader);

                if (categoryModel.getTitle() != null && !categoryModel.getTitle().isEmpty()) {
                    txtHeader.setVisibility(View.VISIBLE);
                    txtHeader.setText(categoryModel.getTitle());
                } else {
                    txtHeader.setVisibility(View.GONE);
                }

                Big_Home_Single_Slider_Adapter homeSingleSilderAdapter = new Big_Home_Single_Slider_Adapter(Big_EarningOptionsActivity.this, categoryModel.getData(), false, new Big_Home_Single_Slider_Adapter.ClickListener() {
                    @Override
                    public void onItemClick(int position, View v) {
                        if (categoryModel.getIsActive() != null && categoryModel.getIsActive().equals("0")) {
                            BIG_Common_Utils.Notify(Big_EarningOptionsActivity.this, getString(R.string.app_name), categoryModel.getNotActiveMessage(), false);
                        } else {
                            BIG_Common_Utils.Redirect(Big_EarningOptionsActivity.this, categoryModel.getData().get(position).getScreenNo(), categoryModel.getData().get(position).getTitle(), categoryModel.getData().get(position).getUrl(), categoryModel.getData().get(position).getId(), categoryModel.getData().get(position).getTaskId(), categoryModel.getData().get(position).getImage());
                        }
                    }
                });
                rvSliderlist.setLayoutManager(new LinearLayoutManager(Big_EarningOptionsActivity.this, LinearLayoutManager.VERTICAL, false));
                rvSliderlist.setAdapter(homeSingleSilderAdapter);
                layoutInflate.addView(iconSingleSlider);
                break;
            case BIG_Constants.RewardDataType.NATIVE_AD:

                View viewNativeAd = getLayoutInflater().inflate(R.layout.big_inflate_native_ad, null);
                FrameLayout fl_adplaceholder = viewNativeAd.findViewById(R.id.fl_adplaceholder);
                TextView lblLoadingAds = viewNativeAd.findViewById(R.id.lblLoadingAds);
                if (BIG_Common_Utils.isShowAppLovinNativeAds()) {
                    loadAppLovinNativeAds(fl_adplaceholder, lblLoadingAds);
                    layoutInflate.addView(viewNativeAd);
                }
                break;
            case BIG_Constants.RewardDataType.LIVE_CONTEST:
                View viewLiveContest = getLayoutInflater().inflate(R.layout.big_inflate_home_live_contest, layoutInflate, false);
                TextView tvLabel1 = viewLiveContest.findViewById(R.id.tvLabel);
                View viewShineHeader = viewLiveContest.findViewById(R.id.viewShineHeader);
                Animation animShine = AnimationUtils.loadAnimation(Big_EarningOptionsActivity.this, R.anim.big_left_right_duration);
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
                            startActivity(new Intent(Big_EarningOptionsActivity.this, Big_AdjoeLeaderboardActivity.class));
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
                            BIG_Common_Utils.setEnableDisable(Big_EarningOptionsActivity.this, view);
                            BIG_Common_Utils.Redirect(Big_EarningOptionsActivity.this, categoryModel.getScreenNo(), categoryModel.getTitle(), categoryModel.getUrl(), categoryModel.getId(), categoryModel.getTaskId(), categoryModel.getImage());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });



                LinearLayout layoutContent1 = viewLiveContest.findViewById(R.id.layoutContent);
                TextView lblMilestonesSubTitle1 = viewLiveContest.findViewById(R.id.lblSubTitle);

                if (!BIG_Common_Utils.isStringNullOrEmpty(categoryModel.getBgColor())) {

                    GradientDrawable drawable = (GradientDrawable) ContextCompat.getDrawable(this, R.drawable.big_reward_bg);
                    drawable.mutate(); // only change this instance of the xml, not all components using this xml
                    drawable.setStroke(getResources().getDimensionPixelSize(R.dimen.dim_1_5), Color.parseColor(categoryModel.getIconBGColor())); // set stroke width and stroke color
                    drawable.setColor(Color.parseColor(categoryModel.getBgColor()));
                    layoutContent1.setBackground(drawable);

                    Drawable mDrawable2 = ContextCompat.getDrawable(Big_EarningOptionsActivity.this, R.drawable.big_ad_live_leaderboard_sub_title);
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


            case BIG_Constants.HomeDataType.QUICK_TASK:
                BIG_AppLogger.getInstance().e("Reward Screen DATA --)", "" + type);
                if (categoryModel.getData() != null && categoryModel.getData().size() > 0) {
                    quickTaskView = getLayoutInflater().inflate(R.layout.big_inflate_quick_tasks, layoutInflate, false);
                    RecyclerView rvSliderlist1 = quickTaskView.findViewById(R.id.rvData);
                    TextView txtHeader1 = quickTaskView.findViewById(R.id.txtTitleHeader);

                    if (categoryModel.getTitle() != null && !categoryModel.getTitle().isEmpty()) {
                        txtHeader1.setVisibility(View.VISIBLE);
                        txtHeader1.setText(categoryModel.getTitle());
                    } else {
                        txtHeader1.setVisibility(View.GONE);
                    }
                    LinearLayout layoutContent = quickTaskView.findViewById(R.id.layoutContent);
                    GradientDrawable drawable = (GradientDrawable) ContextCompat.getDrawable(this, R.drawable.big_rectangle_white);
                    drawable.mutate(); // only change this instance of the xml, not all components using this xml
                    drawable.setStroke(getResources().getDimensionPixelSize(R.dimen.dim_1_5), Color.parseColor(categoryModel.getIconBGColor())); // set stroke width and stroke color
                    drawable.setColor(Color.parseColor(categoryModel.getBgColor()));
                    layoutContent.setBackground(drawable);

                    quickTasksAdapter = new Big_QuickTasksAdapter(categoryModel.getData(), Big_EarningOptionsActivity.this, new Big_QuickTasksAdapter.ClickListener() {
                        @Override
                        public void onItemClick(int position, View v) {
                            if (BIG_SharePrefs.getInstance().getBoolean(BIG_SharePrefs.IS_LOGIN)) {
                                selectedQuickTaskPos = position;
                                BIG_Activity_Manager.isShowAppOpenAd = false;
                                BIG_Common_Utils.Redirect(Big_EarningOptionsActivity.this, categoryModel.getData().get(position).getScreenNo(), categoryModel.getData().get(position).getTitle(), categoryModel.getData().get(position).getUrl(), categoryModel.getData().get(position).getId(), categoryModel.getData().get(position).getTaskId(), categoryModel.getData().get(position).getImage());
                                startQuickTaskTimer(categoryModel.getData(), position);
                            } else {
                                BIG_Common_Utils.NotifyLogin(Big_EarningOptionsActivity.this);
                            }
                        }
                    });
                    rvSliderlist1.setLayoutManager(new LinearLayoutManager(Big_EarningOptionsActivity.this, LinearLayoutManager.VERTICAL, false));
                    rvSliderlist1.setAdapter(quickTasksAdapter);
                    layoutInflate.addView(quickTaskView);
                }
                break;
            default:
                View spinView = getLayoutInflater().inflate(R.layout.big_inflate_reward_spin, null);
                BIG_AppLogger.getInstance().e("Reward Screen DATA --)", "" + type);
                FrameLayout layoutSpin = spinView.findViewById(R.id.layoutSpin);
                TextView tvLabel = spinView.findViewById(R.id.tvLabel);
                if (categoryModel.getLabel() != null && categoryModel.getLabel().length() > 0) {
                    tvLabel.setVisibility(View.VISIBLE);
                    tvLabel.setText(categoryModel.getLabel());
                } else {
                    tvLabel.setVisibility(View.GONE);
                }
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
                        Glide.with(Big_EarningOptionsActivity.this).load(categoryModel.getFullImage()).listener(new RequestListener<Drawable>() {
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
                    todayDateDailyTarget = categoryModel.getDailyRewardTodayDate();
                    endDateDailyTarget = categoryModel.getDailyRewardEndDate();

                    if (todayDateDailyTarget != null && endDateDailyTarget != null) {
                        tvTimerDailyTarget = viewDailyTarget.findViewById(R.id.tvTime);
                        setTimerDailyTarget();
                    }
                    CardView cardFullImage = spinView.findViewById(R.id.cardFullImage);
                    cardFullImage.setVisibility(View.VISIBLE);
                    cardFullImage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            try {
                                performItemClick(view, categoryModel);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                } else {
                    setDetailsView(spinView, type, categoryModel);
                    TextView lblSpin = spinView.findViewById(R.id.lblTitle);
                    ProgressBar probr = spinView.findViewById(R.id.probr);
                    TextView lblSpinSubTitle = spinView.findViewById(R.id.lblSubTitle);
                    LottieAnimationView ivLottieView = spinView.findViewById(R.id.ivLottie);
                    ivLottieView.setSpeed(0.6f);

                    ImageView ivIcon = spinView.findViewById(R.id.ivIcon);
                    if (categoryModel.getIcon() != null) {
                        if (categoryModel.getIcon().contains(".json")) {
                            ivIcon.setVisibility(View.GONE);
                            ivLottieView.setVisibility(View.VISIBLE);
                            BIG_Common_Utils.setLottieAnimation(ivLottieView, categoryModel.getIcon());
                            ivLottieView.setRepeatCount(LottieDrawable.INFINITE);
                            probr.setVisibility(View.GONE);
                        } else {
                            ivIcon.setVisibility(View.VISIBLE);
                            ivLottieView.setVisibility(View.GONE);
                            Glide.with(Big_EarningOptionsActivity.this)
                                    .load(categoryModel.getIcon())
                                    .override(getResources().getDimensionPixelSize(R.dimen.dim_56))
                                    .listener(new RequestListener<Drawable>() {
                                        @Override
                                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                            return false;
                                        }

                                        @Override
                                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, com.bumptech.glide.load.DataSource dataSource, boolean isFirstResource) {
                                            probr.setVisibility(View.GONE);
                                            return false;
                                        }
                                    })
                                    .into(ivIcon);
                        }
                    }

                    if (categoryModel.getType().equals(BIG_Constants.RewardDataType.DAILY_REWARDS_CHALLENGE)) {

                        todayDate = categoryModel.getDailyRewardTodayDate();
                        endDate = categoryModel.getDailyRewardEndDate();
                        if (todayDate != null && endDate != null) {

                            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) lblSpinSubTitle.getLayoutParams();
                            params.setMargins(0, getResources().getDimensionPixelSize(R.dimen.dim_5), getResources().getDimensionPixelSize(R.dimen.dim_10), getResources().getDimensionPixelSize(R.dimen.dim_8));
                            lblSpinSubTitle.setLayoutParams(params);
                            TextView tvPoints = spinView.findViewById(R.id.tvPoints);
                            tvPoints.setText(categoryModel.getDailyRewardPoints());


                            tvTimer = spinView.findViewById(R.id.tvTimer);
                            setTimer();
                        }
                    }

                    lblSpin.setText(categoryModel.getTitle());

                    lblSpinSubTitle.setText(categoryModel.getSubTitle());
                    layoutSpin.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            try {

                                if (categoryModel.getIsActive() != null && categoryModel.getIsActive().equals("0")) {
                                    BIG_Common_Utils.Notify(Big_EarningOptionsActivity.this, getString(R.string.app_name), categoryModel.getNotActiveMessage(), false);
                                } else {

                                    BIG_Common_Utils.Redirect(Big_EarningOptionsActivity.this, categoryModel.getScreenNo(), categoryModel.getTitle(), categoryModel.getUrl(), categoryModel.getId(), categoryModel.getTaskId(), categoryModel.getImage());
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
                layoutInflate.addView(spinView);
                break;
        }
    }

    private void setDetailsView(View spinView, String type, Big_Home_Data_List_Item categoryModel) {
        TextView tvLabel = spinView.findViewById(R.id.tvLabel);
        if (!BIG_Common_Utils.isStringNullOrEmpty(categoryModel.getLabel())) {
            tvLabel.setText(categoryModel.getLabel());
            tvLabel.setVisibility(View.VISIBLE);
            tvLabel.startAnimation(blinkAnimation);
        } else {
            tvLabel.setVisibility(View.GONE);
        }




        LinearLayout layoutContent = spinView.findViewById(R.id.layoutContent);
        if (!BIG_Common_Utils.isStringNullOrEmpty(categoryModel.getBgColor())) {
            GradientDrawable drawable = (GradientDrawable) ContextCompat.getDrawable(this, R.drawable.big_rectangle_white);
            drawable.mutate(); // only change this instance of the xml, not all components using this xml
//            drawable.setStroke(getResources().getDimensionPixelSize(R.dimen.dim_1_5), Color.parseColor(categoryModel.getIconBGColor())); // set stroke width and stroke color
            drawable.setColor(Color.parseColor(categoryModel.getBgColor()));
            layoutContent.setBackground(drawable);
        }
        try {
            ImageView ivLock = spinView.findViewById(R.id.ivLock);
            if (!BIG_Common_Utils.isStringNullOrEmpty(categoryModel.getIsTodayTaskCompleted()) && categoryModel.getIsTodayTaskCompleted().equals("0")) {
                ivLock.setVisibility(View.VISIBLE);
                Typeface typefaceMedium = ResourcesCompat.getFont(Big_EarningOptionsActivity.this, R.font.big_rubik_medium);
                ivLock.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            balloon = new Balloon.Builder(Big_EarningOptionsActivity.this).setArrowSize(10).setArrowOrientation(ArrowOrientation.TOP).setArrowPositionRules(ArrowPositionRules.ALIGN_ANCHOR).setArrowPosition(0.5f).setWidth(BalloonSizeSpec.WRAP).setHeight(65).setTextSize(15f).setPaddingHorizontal(10).setCornerRadius(4f).setAlpha(0.9f).setTextTypeface(typefaceMedium).setText("Complete <font color='#FFCC66'>" + categoryModel.getTaskCount() + " Tasks </font> to <font color='#FFCC66'>UNLOCK</font> " + categoryModel.getTitle()).setTextColor(ContextCompat.getColor(Big_EarningOptionsActivity.this, R.color.white)).setTextIsHtml(true).setBackgroundColor(ContextCompat.getColor(Big_EarningOptionsActivity.this, R.color.black_transparent)).setOnBalloonClickListener(new OnBalloonClickListener() {
                                @Override
                                public void onBalloonClick(@NonNull View view) {
                                    balloon.dismiss();
                                }
                            }).setBalloonAnimation(BalloonAnimation.FADE).setLifecycleOwner(Big_EarningOptionsActivity.this).build();
                            balloon.showAlignBottom(view);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            } else {

            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void performItemClick(View v, Big_Home_Data_List_Item categoryModel) {
        try {
            BIG_Common_Utils.setEnableDisable(Big_EarningOptionsActivity.this, v);
            if (!BIG_Common_Utils.isStringNullOrEmpty(categoryModel.getIsTodayTaskCompleted()) && categoryModel.getIsTodayTaskCompleted().equals("1")) {
                if (categoryModel.getIsActive() != null && categoryModel.getIsActive().equals("0")) {
                    BIG_Common_Utils.Notify(Big_EarningOptionsActivity.this, getString(R.string.app_name), categoryModel.getNotActiveMessage(), false);
                } else {
                    if (categoryModel.getType().equals(BIG_Constants.RewardDataType.DAILY_BONUS)) {
                        startActivity(new Intent(Big_EarningOptionsActivity.this, Big_ActivityDailyLogin.class).putExtra("objRewardScreenModel", objRewardScreenModel).putExtra("title", categoryModel.getTitle()).putExtra("subTitle", categoryModel.getSubTitle()));
                    } else {
                        BIG_AppLogger.getInstance().e("#screeno", categoryModel.getScreenNo());
                        BIG_Common_Utils.Redirect(Big_EarningOptionsActivity.this, categoryModel.getScreenNo(), categoryModel.getTitle(), categoryModel.getUrl(), categoryModel.getId(), categoryModel.getTaskId(), categoryModel.getImage());
                    }
                }
            } else {
                showTaskPopup(categoryModel);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showTaskPopup(Big_Home_Data_List_Item categoryModel) {
        try {
            Dialog dialogTaskList = new Dialog(Big_EarningOptionsActivity.this, android.R.style.Theme_Light);
            dialogTaskList.getWindow().setBackgroundDrawableResource(R.color.black_transparent);
            dialogTaskList.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialogTaskList.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            dialogTaskList.setCancelable(true);
            dialogTaskList.setCanceledOnTouchOutside(true);
            dialogTaskList.setContentView(R.layout.big_popup_complete_task);
            RecyclerView rvTasks = dialogTaskList.findViewById(R.id.rvTasks);
            TextView tvDescription = dialogTaskList.findViewById(R.id.tvDescription);
            if (!BIG_Common_Utils.isStringNullOrEmpty(objRewardScreenModel.getTodayCompletedTask())) {
                int completed = Integer.parseInt(objRewardScreenModel.getTodayCompletedTask());
                if (completed > 0) {
                    tvDescription.setText("Complete " + (Integer.parseInt(categoryModel.getTaskCount()) - completed) + " more easy tasks to unlock " + categoryModel.getTitle());
                } else {
                    tvDescription.setText("Complete " + categoryModel.getTaskCount() + " easy tasks to unlock " + categoryModel.getTitle());
                }
            } else {
                tvDescription.setText("Complete " + categoryModel.getTaskCount() + " easy tasks to unlock " + categoryModel.getTitle());
            }
            if (objRewardScreenModel.getTaskList() != null && objRewardScreenModel.getTaskList().size() > 0) {
                Big_Home_Task_Offer_List_Adapter taskListAdapter = new Big_Home_Task_Offer_List_Adapter(objRewardScreenModel.getTaskList(), Big_EarningOptionsActivity.this, new Big_Home_Task_Offer_List_Adapter.ClickListener() {
                    @Override
                    public void onItemClick(int position, View v) {
                        dialogTaskList.dismiss();
                        BIG_Common_Utils.Redirect(Big_EarningOptionsActivity.this, objRewardScreenModel.getTaskList().get(position).getScreenNo(), objRewardScreenModel.getTaskList().get(position).getTitle(), objRewardScreenModel.getTaskList().get(position).getUrl(), objRewardScreenModel.getTaskList().get(position).getId(), objRewardScreenModel.getTaskList().get(position).getTaskId(), objRewardScreenModel.getTaskList().get(position).getImage());
                    }
                });
                rvTasks.setLayoutManager(new LinearLayoutManager(Big_EarningOptionsActivity.this, LinearLayoutManager.VERTICAL, false));
                rvTasks.setAdapter(taskListAdapter);
            } else {
                rvTasks.setVisibility(View.GONE);
            }

            Button btnViewAll = dialogTaskList.findViewById(R.id.btnViewAll);
            btnViewAll.setOnClickListener(v -> {
                dialogTaskList.dismiss();
                startActivity(new Intent(Big_EarningOptionsActivity.this, Big_TasksCategoryTypeActivity.class));
            });

            dialogTaskList.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setTimer() {
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
                    tvTimer.setText("00:00:00");

                }
            }.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setTimerDailyTarget() {
        try {
            if (timerDailyTarget != null) {
                timerDailyTarget.cancel();
            }
            timeDailyTarget = BIG_Common_Utils.timeDiff(endDateDailyTarget, todayDateDailyTarget);
            timerDailyTarget = new CountDownTimer(timeDailyTarget * 60000L, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    tvTimerDailyTarget.setText(BIG_Common_Utils.updateTimeRemainingLuckyNumber(millisUntilFinished));
                }

                @Override
                public void onFinish() {
                    tvTimerDailyTarget.setText("00:00:00");
                }
            }.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadAppLovinNativeAds(FrameLayout frameLayout, TextView lblLoadingAds) {
        try {
            nativeAdLoader = new MaxNativeAdLoader(BIG_Common_Utils.getRandomAdUnitId(responseMain.getLovinNativeID()), Big_EarningOptionsActivity.this);
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

                }

                @Override
                public void onNativeAdLoadFailed(final String adUnitId, final MaxError error) {
                    frameLayout.setVisibility(View.GONE);
                    lblLoadingAds.setVisibility(View.GONE);

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
    protected void onDestroy() {
        super.onDestroy();
        removeAds();
        if (timer != null) {
            timer.cancel();
        }
    }

    public void removeAds() {

        try {
            if (nativeAd != null && nativeAdLoader != null) {
                nativeAdLoader.destroy(nativeAd);
                nativeAd = null;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    @Override
    public void onBackPressed() {

        super.onBackPressed();

    }

    public void onRewardDataChanged(Big_Reward_Screen_Model responseModel) {
        try {
            scroll.scrollTo(0, 0);
            objRewardScreenModel = responseModel;


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
            try {
                if (!BIG_Common_Utils.isStringNullOrEmpty(responseMain.getIsShowGiveawayCode()) && responseMain.getIsShowGiveawayCode().equals("1")) {
                    layoutGiveawayCode.setVisibility(View.VISIBLE);
                    RecyclerView rvGiveawayCodes = findViewById(R.id.rvGiveawayCodes);

                    if (!BIG_Common_Utils.isStringNullOrEmpty(responseMain.getGiveawayCode())) {
                        lblGiveawayCode.setText("Apply Giveaway Code");

                        rvGiveawayCodes.setVisibility(View.VISIBLE);
                        List<String> list = Arrays.asList(responseMain.getGiveawayCode().split(","));
                        rvGiveawayCodes.setLayoutManager(new LinearLayoutManager(Big_EarningOptionsActivity.this, LinearLayoutManager.HORIZONTAL, false));
                        rvGiveawayCodes.setAdapter(new Big_HomeGiveawayCodesAdapter(list, Big_EarningOptionsActivity.this, new Big_HomeGiveawayCodesAdapter.ClickListener() {
                            @Override
                            public void onClick(int position, View v, LinearLayout linearLayout) {
                                startActivity(new Intent(Big_EarningOptionsActivity.this, Big_GiveAwaySocialActivity.class));
                            }
                        }));

                    } else {
                        lblGiveawayCode.setText("Have a Giveaway Code?");
                        rvGiveawayCodes.setVisibility(View.GONE);
                    }
                    rvGiveawayCodes.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            startActivity(new Intent(Big_EarningOptionsActivity.this, Big_GiveAwaySocialActivity.class));
                            Toast.makeText(Big_EarningOptionsActivity.this, "code", Toast.LENGTH_SHORT).show();
                        }
                    });

                    layoutGiveawayCode.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            startActivity(new Intent(Big_EarningOptionsActivity.this, Big_GiveAwaySocialActivity.class));
                        }
                    });
                } else {
                    layoutGiveawayCode.setVisibility(View.GONE);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (layoutInflate != null) {
                layoutInflate.removeAllViews();
            }

            layoutInflate.setVisibility(View.VISIBLE);

            if (responseModel.getRewardDataList() != null && responseModel.getRewardDataList().size() > 0) {
                for (int i = 0; i < responseModel.getRewardDataList().size(); i++) {
                    try {
                        inflateRewardScreenData(responseModel.getRewardDataList().get(i).getType(), responseModel.getRewardDataList().get(i));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();

        }

    }



    public void onUpdateWalletBalance() {
        tvPoints.setText(BIG_SharePrefs.getInstance().getEarningPointString());
    }

    public void updateQuickTask(boolean isSuccess, String id) {
        try {
            if (isSuccess) {
                for (int i = 0; i < quickTasksAdapter.listTasks.size(); i++) {
                    if (quickTasksAdapter.listTasks.get(i).getId().equals(id)) {
                        quickTasksAdapter.listTasks.remove(i);
                        quickTasksAdapter.notifyDataSetChanged();
                        break;
                    }
                }
                tvPoints.setText(BIG_SharePrefs.getInstance().getEarningPointString());
                if (quickTasksAdapter.listTasks.size() == 0) {
                    layoutInflate.removeView(quickTaskView);
                }
                Intent i = new Intent();
                i.putExtra("isSuccess", isSuccess);
                i.putExtra("id", id);
                setResult(RESULT_OK, i);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        selectedQuickTaskPos = -1;
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
}