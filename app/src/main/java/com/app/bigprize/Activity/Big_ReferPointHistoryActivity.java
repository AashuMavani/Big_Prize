/*
 * Copyright (c) 2021.  Hurricane Development Studios
 */

package com.app.bigprize.Activity;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.app.bigprize.Adapter.Big_Refer_History_Tab_Adapter;
import com.app.bigprize.Async.Models.Big_Point_History_Model;
import com.app.bigprize.Async.Models.Big_Response_Model;
import com.app.bigprize.R;
import com.app.bigprize.Value.BIG_Constants;
import com.app.bigprize.utils.BIG_Common_Utils;
import com.app.bigprize.utils.BIG_SharePrefs;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;

import java.util.ArrayList;

public class Big_ReferPointHistoryActivity extends AppCompatActivity {
    private TextView tvPoints;
    private TabLayout tabLayout;
    private ArrayList<String> mFragmentItems;
    private ViewPager viewpager;
    private Big_Refer_History_Tab_Adapter customTabAdapter;
    private Big_Response_Model responseMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        BIG_Common_Utils.setDayNightTheme(Big_ReferPointHistoryActivity.this);
        setContentView(R.layout.big_activity_refer_point_history);
        responseMain = new Gson().fromJson(BIG_SharePrefs.getInstance().getString(BIG_SharePrefs.HomeData), Big_Response_Model.class);

        tvPoints = findViewById(R.id.tvPoints);
        tvPoints.setText(BIG_SharePrefs.getInstance().getEarningPointString());

        ImageView imBack = findViewById(R.id.ivBack);
        imBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        viewpager = findViewById(R.id.viewpager);
        tabLayout = findViewById(R.id.tabLayout);

        setupViewPager(viewpager);
        tabLayout.setupWithViewPager(viewpager);
        viewpager.setCurrentItem(0);
    }

    private void setupViewPager(ViewPager viewPager) {
        mFragmentItems = new ArrayList<>();
        mFragmentItems.add("Refer Income");
        mFragmentItems.add("Referred Users");

        customTabAdapter = new Big_Refer_History_Tab_Adapter(getSupportFragmentManager(), mFragmentItems);
        viewPager.setAdapter(customTabAdapter);
        viewPager.setOffscreenPageLimit(1);
        customTabAdapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {

            super.onBackPressed();

    }

    private Big_Point_History_Model responseModel;


    public void setData(String type, Big_Point_History_Model responseModel1) {
        responseModel=responseModel1;

        if (type.equals(BIG_Constants.HistoryType.REFER_POINT)) {
            customTabAdapter.getReferPointHistoryFragment().setData(responseModel);
        } else {
            customTabAdapter.getReferUserHistoryFragment().setData(responseModel);
        }
        tvPoints.setText(BIG_SharePrefs.getInstance().getEarningPointString());
    }

}