package com.app.bigprize.Adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.app.bigprize.Fragments.Big_Refer_Point_History_Fragment;
import com.app.bigprize.Fragments.Big_Refer_User_History_Fragment;

import java.util.ArrayList;


public class Big_Refer_History_Tab_Adapter extends FragmentPagerAdapter {
    public ArrayList<String> mFragmentItems;
    private Big_Refer_Point_History_Fragment fragmentPointHistory;
    private Big_Refer_User_History_Fragment fragmentUserHistory;

    public Big_Refer_History_Tab_Adapter(FragmentManager fm, ArrayList<String> fragmentItems) {
        super(fm);
        this.mFragmentItems = fragmentItems;
        fragmentPointHistory = new Big_Refer_Point_History_Fragment();
        fragmentUserHistory = new Big_Refer_User_History_Fragment();
    }

    @Override
    public Fragment getItem(int i) {
        if (i == 0) {
            return fragmentPointHistory;
        } else if (i == 1) {
            return fragmentUserHistory;
        }
        return null;
    }

    public Big_Refer_Point_History_Fragment getReferPointHistoryFragment() {
        return fragmentPointHistory;
    }

    public Big_Refer_User_History_Fragment getReferUserHistoryFragment() {
        return fragmentUserHistory;
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
