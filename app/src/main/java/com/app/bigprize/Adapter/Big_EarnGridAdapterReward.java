package com.app.bigprize.Adapter;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieDrawable;
import com.app.bigprize.Async.Models.Big_Home_Data_Item;
import com.app.bigprize.R;
import com.app.bigprize.utils.BIG_Common_Utils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.List;


public class Big_EarnGridAdapterReward extends RecyclerView.Adapter<Big_EarnGridAdapterReward.ViewHolder> {

    private Activity activity;
    private List<Big_Home_Data_Item> data;
    private ClickListener clickListener;

    public Big_EarnGridAdapterReward(final Activity context, List<Big_Home_Data_Item> data, ClickListener clickListener) {
        this.clickListener = clickListener;
        activity = context;
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.big_row_earn_grid_reward, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.probr.setVisibility(View.VISIBLE);
        String image = null;
        if (!BIG_Common_Utils.isStringNullOrEmpty(data.get(position).getJsonImage())) {
            image = data.get(position).getJsonImage();
        } else if (!BIG_Common_Utils.isStringNullOrEmpty(data.get(position).getImage())) {
            image = data.get(position).getImage();
        }
        if (image != null) {
            if (image.contains(".json")) {
                holder.ivIcon.setVisibility(View.GONE);
                holder.ivLottie.setVisibility(View.VISIBLE);
                BIG_Common_Utils.setLottieAnimation(holder.ivLottie, image);
                holder.ivLottie.setRepeatCount(LottieDrawable.INFINITE);
                holder.probr.setVisibility(View.GONE);
            } else {
                holder.ivIcon.setVisibility(View.VISIBLE);
                holder.ivLottie.setVisibility(View.GONE);
                Glide.with(activity).load(image).addListener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        holder.probr.setVisibility(View.GONE);
                        return false;
                    }
                }).into(holder.ivIcon);
            }
        } else {
            holder.ivIcon.setVisibility(View.GONE);
            holder.ivLottie.setVisibility(View.GONE);
            holder.probr.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ProgressBar probr;
        private ImageView ivIcon;
        private LottieAnimationView ivLottie;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivLottie = itemView.findViewById(R.id.ivLottie);
            ivIcon = itemView.findViewById(R.id.ivIcon);
            probr = itemView.findViewById(R.id.progressBar);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            clickListener.onItemClick(this.getLayoutPosition(), v);
        }
    }

    public interface ClickListener {
        void onItemClick(int position, View v);
    }
}