package com.app.bigprize.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.app.bigprize.Activity.Big_MinesFinderActivity;
import com.app.bigprize.Async.Models.Big_MinesFinderDataItem;
import com.app.bigprize.R;
import com.app.bigprize.utils.BIG_Common_Utils;
import com.bumptech.glide.Glide;

import java.util.List;


public class Big_MinesFinderAdapter extends RecyclerView.Adapter<Big_MinesFinderAdapter.SavedHolder> {

    public List<Big_MinesFinderDataItem> listData;
    private Context context;
    private ClickListener clickListener;

    public Big_MinesFinderAdapter(List<Big_MinesFinderDataItem> list, Context context, ClickListener clickListener) {
        this.listData = list;
        this.context = context;
        this.clickListener = clickListener;
    }


    @NonNull
    @Override
    public SavedHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.big_item_mines_finder, parent, false);
        return new SavedHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SavedHolder holder, final int position) {
        try {
            if (Big_MinesFinderActivity.isVisible && listData.get(position).getCount().equals("b")) {
                holder.ivimgbox.setVisibility(View.INVISIBLE);
                holder.ivimgboxlottie.setVisibility(View.VISIBLE);
                holder.ivimgboxlottie.setMinAndMaxFrame(30, 70);
                holder.ivimgboxlottie.playAnimation();
                BIG_Common_Utils.setLottieAnimation(holder.ivimgboxlottie, listData.get(position).getIcon());
            } else {
                holder.ivimgbox.setVisibility(View.VISIBLE);
                holder.ivimgboxlottie.setVisibility(View.INVISIBLE);
                if (listData.get(position).isShown()) {
                    Glide.with(context).load(listData.get(position).getIcon()).into(holder.ivimgbox);
                } else {
                    holder.ivimgbox.setImageResource(R.drawable.big_mines_finder_box);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public interface ClickListener {
        void onItemClick(int position, View v, ImageView ivimgbox, LottieAnimationView ivimgboxlottie);
    }

    public class SavedHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView ivimgbox;
        private LottieAnimationView ivimgboxlottie;
        private RelativeLayout layoutMain;

        public SavedHolder(@NonNull View itemView) {
            super(itemView);
            ivimgbox = itemView.findViewById(R.id.ivimgbox);
            ivimgboxlottie = itemView.findViewById(R.id.ivimgboxlottie);
            layoutMain = itemView.findViewById(R.id.layoutMain);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            clickListener.onItemClick(this.getLayoutPosition(), v, ivimgbox, ivimgboxlottie);
        }
    }
}

