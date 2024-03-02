package com.app.bigprize.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.app.bigprize.Async.Models.Big_EarnedPointHistoryModel;
import com.app.bigprize.R;

public class Big_ReferUserHistoryFragment extends Fragment {



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.big_fragment_points_history, container, false);
    }

    public void setData(Big_EarnedPointHistoryModel responseModel) {
    }
}