/*
 * Copyright (c) 2021.  Hurricane Development Studios
 */

package com.app.bigprize.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.app.bigprize.Activity.Big_GiveAwaySocialActivity;
import com.app.bigprize.R;

import java.util.List;

public class Big_HomeGiveawayCodesAdapter extends RecyclerView.Adapter<Big_HomeGiveawayCodesAdapter.SavedHolder> {

    public List<String> listTasks;
    private Context context;
    private Big_HomeGiveawayCodesAdapter.ClickListener clickListener;

    public Big_HomeGiveawayCodesAdapter(List<String> list, Context context,Big_HomeGiveawayCodesAdapter.ClickListener clickListener) {
        this.listTasks = list;
        this.context = context;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public SavedHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view= inflater.inflate(R.layout.big_item_home_giveaway_codes, parent, false);
        return new SavedHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SavedHolder holder, final int position) {
        try {
            holder.tvGiveawayCode.setText(listTasks.get(position));
            holder.lnr_itemgiveaway.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    context.startActivity(new Intent(context, Big_GiveAwaySocialActivity.class));
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return listTasks.size();
    }

    public class SavedHolder extends RecyclerView.ViewHolder {
        private TextView tvGiveawayCode;
        private LinearLayout lnr_itemgiveaway;

        public SavedHolder(@NonNull View itemView) {
            super(itemView);
            tvGiveawayCode = itemView.findViewById(R.id.tvGiveawayCode);
            lnr_itemgiveaway =itemView.findViewById(R.id.lnr_itemgiveaway);
        }
        public void onClick(View v){
            clickListener.onClick(this.getLayoutPosition(), v,lnr_itemgiveaway);
        }
    }
    public interface ClickListener {
        void onClick(int position, View v, LinearLayout linearLayout);
    }
}
