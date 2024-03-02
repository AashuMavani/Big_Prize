package com.app.bigprize.Activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.app.bigprize.Adapter.Big_Horizontal_Task_List_Adapter;
import com.app.bigprize.Adapter.Big_Task_Offer_List_Adapter;
import com.app.bigprize.Async.Big_Get_Task_Offer_List_Async;
import com.app.bigprize.Async.Models.Big_Home_Slider_Item;
import com.app.bigprize.Async.Models.Big_Response_Model;
import com.app.bigprize.Async.Models.Big_TaskOffer;
import com.app.bigprize.Async.Models.Big_Task_OfferList_Response_Model;
import com.app.bigprize.Customviews.Big_Recyclerview.Big_Pager_Adapter_Small;
import com.app.bigprize.Customviews.Big_Recyclerview.Big_Recycler_ViewPager_Small;
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

public class Big_TasksCategoryTypeActivity extends AppCompatActivity {
    private int pageNo = 1;
    private NestedScrollView nestedScrollView;
    private long numOfPage;
    private TextView tvPoints, lblLoadingAds,tvAllTasks,tvAllTasksCount,tvHighestPayingTask,tvHighestPayingCount;
    private LottieAnimationView ivLottieNoData;
    private RecyclerView rvTaskList;
    private final List<Big_TaskOffer> listTasks = new ArrayList<>();
    private LinearLayout layoutPoints,layoutOptions;
    private ImageView ivHistory,ivAll,ivHighestPaying;
    private RelativeLayout layoutSlider;
    private Big_Recycler_ViewPager_Small rvSlider;
    private String taskTypeId;
    private TextView tvTitle;
    private Big_Response_Model responseMain;
    private MaxAd nativeAd;
    private MaxNativeAdLoader nativeAdLoader;
    private FrameLayout frameLayoutNativeAd;
    private LinearLayout layoutAds;
    private String selectedTaskType = BIG_Constants.TASK_TYPE_ALL;
    private View viewAll, viewHighestPaying;
    private Big_Task_Offer_List_Adapter taskAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        BIG_Common_Utils.setDayNightTheme(Big_TasksCategoryTypeActivity.this);
        setContentView(R.layout.big_activity_tasks_category_type2);
        responseMain = new Gson().fromJson(BIG_SharePrefs.getInstance().getString(BIG_SharePrefs.HomeData), Big_Response_Model.class);

        if (getIntent().getExtras() != null) {
            if (getIntent().getExtras().containsKey("title")) {
                tvTitle = findViewById(R.id.tvTitle);
                tvTitle.setText(getIntent().getStringExtra("title"));
            }
            taskTypeId = getIntent().getStringExtra("taskTypeId");
        }

        ImageView ivBack = findViewById(R.id.ivBack);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        tvAllTasks = findViewById(R.id.tvAllTasks);
        tvHighestPayingTask = findViewById(R.id.tvHighestPayingTask);
        layoutPoints = findViewById(R.id.layoutPoints);
        layoutAds = findViewById(R.id.layoutAds);
        rvSlider = findViewById(R.id.rvSlider);
        tvAllTasksCount= findViewById(R.id.tvAllTasksCount);
        viewAll = findViewById(R.id.viewAll);
        ivHighestPaying = findViewById(R.id.ivHighestPaying);
        tvHighestPayingCount= findViewById(R.id.tvHighestPayingCount);
        ivAll = findViewById(R.id.ivAll);
        layoutSlider = findViewById(R.id.layoutSlider);
        viewHighestPaying=findViewById(R.id.viewHighestPaying);
        layoutOptions = findViewById(R.id.layoutOptions);
        layoutOptions.setVisibility(View.INVISIBLE);
        tvPoints = findViewById(R.id.tvPoints);
        tvPoints.setText(BIG_SharePrefs.getInstance().getEarningPointString());
        layoutPoints = findViewById(R.id.layoutPoints);
        rvTaskList=findViewById(R.id.rvTaskList);

        layoutPoints.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (BIG_SharePrefs.getInstance().getBoolean(BIG_SharePrefs.IS_LOGIN)) {
                    startActivity(new Intent(Big_TasksCategoryTypeActivity.this, Big_My_Wallet_Activity.class));
                } else {
                    BIG_Common_Utils.NotifyLogin(Big_TasksCategoryTypeActivity.this);
                }
            }
        });

        ivHistory = findViewById(R.id.ivHistory);
        //Common_Utils.startRoundAnimation(TasksCategoryTypeActivity.this, ivHistory);
        ivHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (BIG_SharePrefs.getInstance().getBoolean(BIG_SharePrefs.IS_LOGIN)) {
                    startActivity(new Intent(Big_TasksCategoryTypeActivity.this, Big_PointHistoryActivity.class)
                            .putExtra("type", BIG_Constants.HistoryType.TASK)
                            .putExtra("title", "Task History"));
                } else {
                    BIG_Common_Utils.NotifyLogin(Big_TasksCategoryTypeActivity.this);
                }
            }
        });
        rvTaskList.setLayoutManager(new LinearLayoutManager(Big_TasksCategoryTypeActivity.this));
        taskAdapter = new Big_Task_Offer_List_Adapter(listTasks, Big_TasksCategoryTypeActivity.this, new Big_Task_Offer_List_Adapter.ClickListener() {
            @Override
            public void onItemClick(int position, View v) {

                if (listTasks.get(position).getIsShowDetails() != null && listTasks.get(position).getIsShowDetails().equals("1")) {
                    BIG_AppLogger.getInstance().e("task",""+listTasks.get(position).getIsShowDetails());
                    Intent intent = new Intent(Big_TasksCategoryTypeActivity.this, Big_TaskDetailsInfoActivity.class);
                    intent.putExtra("taskId",listTasks.get(position).getId());
                    startActivity(intent);
                } else {
                    BIG_Common_Utils.Redirect(Big_TasksCategoryTypeActivity.this, listTasks.get(position).getScreenNo(), listTasks.get(position).getTitle()
                            , listTasks.get(position).getUrl(), null, listTasks.get(position).getId(), listTasks.get(position).getIcon());
                }
            }
        });
        rvTaskList.setAdapter(taskAdapter);

        ivLottieNoData = findViewById(R.id.ivLottieNoData);
        nestedScrollView = findViewById(R.id.nestedScrollView);
        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()) {
                    if (pageNo < numOfPage) {
                        new Big_Get_Task_Offer_List_Async(Big_TasksCategoryTypeActivity.this, taskTypeId, String.valueOf(pageNo + 1));
                    }
                }
            }
        });
        new Big_Get_Task_Offer_List_Async(Big_TasksCategoryTypeActivity.this, taskTypeId, String.valueOf(pageNo));

        tvAllTasks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedTaskType = BIG_Constants.TASK_TYPE_ALL;
                viewHighestPaying.setVisibility(View.GONE);
                viewAll.setVisibility(View.VISIBLE);

                Drawable mDrawable = ContextCompat.getDrawable(Big_TasksCategoryTypeActivity.this, R.drawable.big_ic_coins);
                mDrawable.setColorFilter(getColor(R.color.grey_color), PorterDuff.Mode.SRC_IN);
                ivHighestPaying.setImageDrawable(mDrawable);

                Drawable mDrawable1 = ContextCompat.getDrawable(Big_TasksCategoryTypeActivity.this, R.drawable.big_ic_all_task);
                mDrawable1.setColorFilter(getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
                ivAll.setImageDrawable(mDrawable1);


                tvAllTasks.setTextColor(getColor(R.color.colorPrimary));
                tvHighestPayingTask.setTextColor(getColor(R.color.grey_color));
                callApi();
            }
        });

        tvHighestPayingTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedTaskType = BIG_Constants.TASK_TYPE_HIGHEST_PAYING;
                tvHighestPayingTask.setTextColor(getColor(R.color.colorPrimaryDark));
                viewHighestPaying.setVisibility(View.VISIBLE);
                viewAll.setVisibility(View.GONE);

                Drawable mDrawable = ContextCompat.getDrawable(Big_TasksCategoryTypeActivity.this, R.drawable.big_ic_coins);
                mDrawable.setColorFilter(getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
                ivHighestPaying.setImageDrawable(mDrawable);

                Drawable mDrawable1 = ContextCompat.getDrawable(Big_TasksCategoryTypeActivity.this, R.drawable.big_ic_all_task);
                mDrawable1.setColorFilter(getColor(R.color.grey_color), PorterDuff.Mode.SRC_IN);
                ivAll.setImageDrawable(mDrawable1);

                tvAllTasks.setTextColor(getColor(R.color.grey_color));
                callApi();
            }
        });


        layoutPoints.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (BIG_SharePrefs.getInstance().getBoolean(BIG_SharePrefs.IS_LOGIN)) {
                    startActivity(new Intent(Big_TasksCategoryTypeActivity.this, Big_My_Wallet_Activity.class));
                } else {
                    BIG_Common_Utils.NotifyLogin(Big_TasksCategoryTypeActivity.this);
                }
            }
        });
    }
    private void callApi() {
        pageNo = 1;
        numOfPage = 0;
        rvTaskList.setVisibility(View.INVISIBLE);
        listTasks.clear();
        taskAdapter.notifyDataSetChanged();
        layoutAds.setVisibility(View.GONE);
        ivLottieNoData.setVisibility(View.GONE);
        nestedScrollView.scrollTo(0, 0);
        new Big_Get_Task_Offer_List_Async(Big_TasksCategoryTypeActivity.this, selectedTaskType, String.valueOf(pageNo));
    }

    @Override
    public void onBackPressed() {

            super.onBackPressed();

    }
    private Big_Task_OfferList_Response_Model responseModel;

    public void setData(Big_Task_OfferList_Response_Model responseModel1) {

        layoutOptions.setVisibility(View.VISIBLE);
        responseModel=responseModel1;


        try {
            tvPoints.setText(BIG_SharePrefs.getInstance().getEarningPointString());
            if (responseModel != null && responseModel.getTaskCategoryList() != null && responseModel.getTaskCategoryList().size() > 0) {
                BIG_AppLogger.getInstance().e("tasklist",""+responseModel1);
                for (int i = 0; i < responseModel.getTaskCategoryList().size(); i++) {
                    if (taskTypeId.equals(responseModel.getTaskCategoryList().get(i).getId())) {
                        tvTitle.setText(responseModel.getTaskCategoryList().get(i).getName());
                    }
                }
            }
            if (listTasks.size() == 0) {
                // load horizontal tasks
                RecyclerView rvAdAppList = findViewById(R.id.rvAdAppList);
                LinearLayout layoutTodayStory = findViewById(R.id.layoutTodayStory);
                if (responseModel.getHorizontalTaskList() != null && responseModel.getHorizontalTaskList().size() > 0) {
                    layoutTodayStory.setVisibility(View.VISIBLE);
                    TextView tvTodayStory = findViewById(R.id.tvTodayStory);
                    tvTodayStory.setText(responseModel.getHorizontalTaskLabel());
                    rvAdAppList.setLayoutManager(new LinearLayoutManager(Big_TasksCategoryTypeActivity.this, RecyclerView.HORIZONTAL, false));

                    Big_Horizontal_Task_List_Adapter adAppListAdapter = new Big_Horizontal_Task_List_Adapter(Big_TasksCategoryTypeActivity.this, (ArrayList<Big_TaskOffer>) responseModel.getHorizontalTaskList(), new Big_Horizontal_Task_List_Adapter.ClickListener() {
                        @Override
                        public void onItemClick(int position, View v) {
                            try {
                                if (responseModel.getHorizontalTaskList().get(position).getIsShowDetails() != null && responseModel.getHorizontalTaskList().get(position).getIsShowDetails().equals("1")) {
                                    BIG_AppLogger.getInstance().e("TaskList",""+responseModel.getHorizontalTaskList());
                                    Intent intent = new Intent(Big_TasksCategoryTypeActivity.this, Big_TaskDetailsInfoActivity.class);
                                    intent.putExtra("taskId", responseModel.getHorizontalTaskList().get(position).getId());
                                    startActivity(intent);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    rvAdAppList.setAdapter(adAppListAdapter);
                } else {
                    layoutTodayStory.setVisibility(View.GONE);
                }
            }
//            if (responseModel != null && responseModel.getTaskOffers() != null && responseModel.getTaskOffers().size() > 0) {
                int prevItemCount = listTasks.size();
                listTasks.addAll(responseModel.getTaskOffers());
                if (prevItemCount == 0) {
                    rvTaskList.getAdapter().notifyDataSetChanged();
                    if (selectedTaskType.equals(BIG_Constants.TASK_TYPE_ALL)) {
                        tvAllTasksCount.setVisibility(View.VISIBLE);
                        tvAllTasksCount.setText(""+responseModel.getTotalIteam());

                        tvHighestPayingCount.setVisibility(View.VISIBLE);
                        tvHighestPayingCount.setText("" + responseModel.getHighPoinCount());


                    } else {
                        Log.d("point====", "setData: "+responseModel.getTotalIteam());
                        tvHighestPayingCount.setVisibility(View.VISIBLE);
                        tvHighestPayingCount.setText(""+responseModel.getTotalIteam());
                    }
                } else {
                    rvTaskList.getAdapter().notifyItemRangeInserted(prevItemCount, responseModel.getTaskOffers().size());
                }

                numOfPage = responseModel.getTotalPage();
                pageNo = Integer.parseInt(responseModel.getCurrentPage());
//            }
            if (listTasks.isEmpty()) {

                layoutAds.setVisibility(View.VISIBLE);
                frameLayoutNativeAd = findViewById(R.id.fl_adplaceholder);
                lblLoadingAds = findViewById(R.id.lblLoadingAds);
                if (BIG_Common_Utils.isShowAppLovinNativeAds()) {
                    loadAppLovinNativeAds();
                } else {
                    layoutAds.setVisibility(View.GONE);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        try {
            if (rvSlider.getListSize() == 0) {
                if (responseModel.getHomeSlider() != null && responseModel.getHomeSlider().size() > 0) {
                    layoutSlider.setVisibility(View.VISIBLE);
                    rvSlider.setClear();
                    rvSlider.addAll((ArrayList<Big_Home_Slider_Item>) responseModel.getHomeSlider());
                    rvSlider.start();
                    rvSlider.setOnItemClickListener(new Big_Pager_Adapter_Small.OnItemClickListener() {
                        @Override
                        public void onItemClick(int position) {
                            BIG_Common_Utils.Redirect(Big_TasksCategoryTypeActivity.this, responseModel.getHomeSlider().get(position).getScreenNo(), responseModel.getHomeSlider().get(position).getTitle()
                                    , responseModel.getHomeSlider().get(position).getUrl(), responseModel.getHomeSlider().get(position).getId(), null, responseModel.getHomeSlider().get(position).getImage());
                        }
                    });
                } else {
                    layoutSlider.setVisibility(View.GONE);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        rvTaskList.setVisibility(listTasks.isEmpty() ? View.GONE : View.VISIBLE);
        ivLottieNoData.setVisibility(listTasks.isEmpty() ? View.VISIBLE : View.GONE);
        if (listTasks.isEmpty())
            ivLottieNoData.playAnimation();
    }


    private void loadAppLovinNativeAds() {
        try {
            nativeAdLoader = new MaxNativeAdLoader(BIG_Common_Utils.getRandomAdUnitId(responseMain.getLovinNativeID()), Big_TasksCategoryTypeActivity.this);
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

}