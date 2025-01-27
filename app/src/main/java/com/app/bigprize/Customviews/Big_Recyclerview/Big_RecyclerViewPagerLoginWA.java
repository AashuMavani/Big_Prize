package com.app.bigprize.Customviews.Big_Recyclerview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;


import com.app.bigprize.Async.Models.Big_Home_Slider_Item;
import com.app.bigprize.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class Big_RecyclerViewPagerLoginWA extends RecyclerView {

    private int count;
    private Timer timer = new Timer();
    private Big_PagerAdapterLoginWA adapter;
    private List<Big_Home_Slider_Item> pagerModels = new ArrayList<>();

    private boolean runAuto = false;
    private int colorActiveIndicator = 0xDE000000;
    private int colorInactiveIndicator = 0x33000000;
    private String TAG = "RecyclerViewPager";
    private int millis = 2000;


    public Big_RecyclerViewPagerLoginWA(@NonNull Context context) {
        super(context);
        initState();
    }


    public Big_RecyclerViewPagerLoginWA(@NonNull Context context, @Nullable AttributeSet attrs) {

        super(context, attrs);

        initState(context, attrs);

    }

    public Big_RecyclerViewPagerLoginWA(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {

        super(context, attrs, defStyle);

        initState(context, attrs);

    }

    private void initState() {
        LinearLayoutManager lm = new LinearLayoutManager(getContext());
        lm.setOrientation(LinearLayoutManager.HORIZONTAL);
        setLayoutManager(lm);

        setClipToPadding(false);
        setPaddingRelative((int) Big_Const.convertDpToPixel(5, getContext()), 0, (int) Big_Const.convertDpToPixel(5, getContext()), 0);

        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(this);

    }

    private void initState(Context context, AttributeSet attrs) {
        @SuppressLint("Recycle") final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.Recycler_ViewPager);
        LinearLayoutManager lm = new LinearLayoutManager(getContext());
        lm.setOrientation(LinearLayoutManager.HORIZONTAL);
        setLayoutManager(lm);

        setClipToPadding(false);
        setPaddingRelative((int) Big_Const.convertDpToPixel(5, getContext()), 0, (int) Big_Const.convertDpToPixel(5, getContext()), 0);

        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(this);
        runAuto = a.getBoolean(R.styleable.Recycler_ViewPager_svp_runAuto, false);
        colorActiveIndicator = a.getColor(R.styleable.Recycler_ViewPager_svp_colorActiveIndicator, 0xDE000000);
        colorInactiveIndicator = a.getColor(R.styleable.Recycler_ViewPager_svp_colorInactiveIndicator, 0x33000000);
        millis = a.getInt(R.styleable.Recycler_ViewPager_svp_timeMillis, 2000);


    }

    private void scrollToNext() {
        try {
            if (count >= getAdapter().getItemCount() - 1)
                count = -1;

            smoothScrollToPosition(count + 1);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void addItem(Big_Home_Slider_Item pagerModel) {
        this.pagerModels.add(pagerModel);
    }

    public void addAll(ArrayList<Big_Home_Slider_Item> pagerModel) {
        this.pagerModels.addAll(pagerModel);
    }

    public void start() {
        try {
            adapter = new Big_PagerAdapterLoginWA(getContext(), pagerModels);
            setAdapter(adapter);
            if (runAuto) {
                if (timer == null)
                    timer = new Timer();

                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        scrollToNext();
                    }
                }, millis, millis);
            } else {
                timer = null;
            }

            addItemDecoration(new Big_Indicator(colorActiveIndicator, colorInactiveIndicator));
            addOnScrollListener(new OnScrollListener() {
                @Override
                public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    LinearLayoutManager layoutManager = ((LinearLayoutManager) getLayoutManager());
                    int firstVisiblePosition = layoutManager.findFirstVisibleItemPosition();
                    count = firstVisiblePosition;
                }

                @Override
                public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setAutoRun(boolean auto) {
        this.runAuto = auto;
    }

    public void setColorActiveIndicator(int color) {
        this.colorActiveIndicator = color;
    }

    public void setColorInactiveIndicator(int color) {
        this.colorInactiveIndicator = color;
    }

    public void setTime(int timeMillis) {
        this.millis = millis;
    }

    public void setClear() {
        this.pagerModels.clear();
    }

    public void setOnItemClickListener(Big_PagerAdapterLoginWA.OnItemClickListener itemClickListener) {
        try {

            ((Big_PagerAdapterLoginWA) getAdapter()).setOnclickItemListener(itemClickListener);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
