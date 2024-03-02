
package com.app.bigprize.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.bigprize.Async.Models.Big_How_To_Work;
import com.app.bigprize.R;
import com.app.bigprize.utils.BIG_Common_Utils;

import java.util.List;

public class Big_ReferSuggestionAdapter extends RecyclerView.Adapter<Big_ReferSuggestionAdapter.MyViewHolder> {

    Activity activity;
    List<Big_How_To_Work> howToWorkList;
    public Big_ReferSuggestionAdapter(Activity activity, List<Big_How_To_Work> howToWork) {
        this.activity=activity;
        howToWorkList=howToWork;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.big_item_refer_earn,parent,false);
        return new MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        if (!BIG_Common_Utils.isStringNullOrEmpty(howToWorkList.get(position).getTitle()))
        {
            holder.txtReferTitle.setText(howToWorkList.get(position).getTitle());
        }

        if (!BIG_Common_Utils.isStringNullOrEmpty(howToWorkList.get(position).getDescription()))
        {
            holder.txtReferDesc.setText(howToWorkList.get(position).getDescription());
        }
    }

    @Override
    public int getItemCount() {
        return howToWorkList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView txtReferTitle,txtReferDesc;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            txtReferTitle=itemView.findViewById(R.id.txtReferTitle);
            txtReferDesc=itemView.findViewById(R.id.txtReferDec);
        }
    }
}
