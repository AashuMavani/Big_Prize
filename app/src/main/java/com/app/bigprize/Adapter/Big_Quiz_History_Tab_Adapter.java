package com.app.bigprize.Adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.app.bigprize.Fragments.Big_All_Quiz_History_Fragment;
import com.app.bigprize.Fragments.Big_My_Quiz_History_Fragment;

import java.util.ArrayList;




public class Big_Quiz_History_Tab_Adapter extends FragmentPagerAdapter {
    public ArrayList<String> mFragmentItems;
    private Big_My_Quiz_History_Fragment fragmentMyQuiz;
    private Big_All_Quiz_History_Fragment fragmentAllQuiz;

    public Big_Quiz_History_Tab_Adapter(FragmentManager fm, ArrayList<String> fragmentItems) {
        super(fm);
        this.mFragmentItems = fragmentItems;
        fragmentMyQuiz = new Big_My_Quiz_History_Fragment();
        fragmentAllQuiz = new Big_All_Quiz_History_Fragment();
    }

    @Override
    public Fragment getItem(int i) {
        if (i == 0) {
            return fragmentMyQuiz;
        } else if (i == 1) {
            return fragmentAllQuiz;
        }
        return null;
    }

    public Big_My_Quiz_History_Fragment getMyQuizHistoryFragment() {
        return fragmentMyQuiz;
    }

    public Big_All_Quiz_History_Fragment getAllQuizHistoryFragment() {
        return fragmentAllQuiz;
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
