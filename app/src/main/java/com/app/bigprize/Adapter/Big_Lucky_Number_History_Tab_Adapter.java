package com.app.bigprize.Adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.app.bigprize.Fragments.Big_AllLucky_Number_ContestHistory_Fragment;
import com.app.bigprize.Fragments.Big_My_LuckyNumber_ContestHistory_Fragment;

import java.util.ArrayList;



public class Big_Lucky_Number_History_Tab_Adapter extends FragmentPagerAdapter {
    public ArrayList<String> mFragmentItems;
    private Big_My_LuckyNumber_ContestHistory_Fragment fragmentMyContest;
    private Big_AllLucky_Number_ContestHistory_Fragment fragmentAllContest;

    public Big_Lucky_Number_History_Tab_Adapter(FragmentManager fm, ArrayList<String> fragmentItems) {
        super(fm);
        this.mFragmentItems = fragmentItems;
        fragmentMyContest = new Big_My_LuckyNumber_ContestHistory_Fragment();
        fragmentAllContest = new Big_AllLucky_Number_ContestHistory_Fragment();
    }

    @Override
    public Fragment getItem(int i) {
        if (i == 0) {
            return fragmentMyContest;
        } else if (i == 1) {
            return fragmentAllContest;
        }
        return null;
    }

    public Big_My_LuckyNumber_ContestHistory_Fragment getMyContestHistoryFragment() {
        return fragmentMyContest;
    }

    public Big_AllLucky_Number_ContestHistory_Fragment getAllContestHistoryFragment() {
        return fragmentAllContest;
    }

    @Override
    public int getCount() {
        return mFragmentItems.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentItems.get(position);
    }
}
