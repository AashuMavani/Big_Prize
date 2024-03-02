package com.app.bigprize.Adapter;

import android.animation.Animator;
import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieDrawable;
import com.app.bigprize.Async.Models.BottomGrid;
import com.app.bigprize.R;
import com.app.bigprize.utils.BIG_Common_Utils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.List;

public class Big_HomeBottomSheet extends RecyclerView.Adapter<Big_HomeBottomSheet.MyViewHolder> {

    private Activity activity;
    private List<BottomGrid> bottomGrids;
    private ClickListener clickListener;
    public Big_HomeBottomSheet(Activity activity, List<BottomGrid> bottomGrids, ClickListener clickListener) {
        this.activity=activity;
        this.bottomGrids=bottomGrids;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public Big_HomeBottomSheet.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.big_home_bottom_grid,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Big_HomeBottomSheet.MyViewHolder holder, int position) {

        holder.probr.setVisibility(View.VISIBLE);
        if (bottomGrids.get(position).getImage() != null) {
            if (bottomGrids.get(position).getImage().contains(".json")) {
                holder.image_gif.setVisibility(View.GONE);
                holder.imageBanner.setVisibility(View.GONE);
                holder.animation_view.setVisibility(View.VISIBLE);
                BIG_Common_Utils.setLottieAnimation(holder.animation_view, bottomGrids.get(position).getImage());
                holder.animation_view.setRepeatCount(LottieDrawable.INFINITE);
                holder.animation_view.addAnimatorListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        if (holder.probr != null) {
                            holder.probr.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
            } else if (bottomGrids.get(position).getImage().contains(".gif")) {
                holder.image_gif.setVisibility(View.VISIBLE);
                holder.animation_view.setVisibility(View.GONE);
                holder.imageBanner.setVisibility(View.GONE);

                Glide.with(activity)
                        .load(bottomGrids.get(position).getImage())
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, com.bumptech.glide.load.DataSource dataSource, boolean isFirstResource) {
                                if (holder.probr != null) {
                                    holder.probr.setVisibility(View.GONE);
                                }
                                return false;
                            }
                        })
                        .into(holder.image_gif);
            } else {
                holder.image_gif.setVisibility(View.GONE);
                holder.animation_view.setVisibility(View.GONE);
                holder.imageBanner.setVisibility(View.VISIBLE);
                Glide.with(activity)
                        .load(bottomGrids.get(position).getImage())
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, com.bumptech.glide.load.DataSource dataSource, boolean isFirstResource) {
                                if (holder.probr != null) {
                                    holder.probr.setVisibility(View.GONE);
                                }
                                return false;
                            }
                        })
                        .into(holder.imageBanner);
            }
        }
    }

    @Override
    public int getItemCount() {
        return bottomGrids.size();
    }

    public interface ClickListener {
        void onItemClick(int position, View v);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView imageBanner;
        private ProgressBar probr;
        private LottieAnimationView animation_view;
        private ImageView image_gif;
        private RelativeLayout relStory;
        private LinearLayout cardContent;
        private LinearLayout layoutContent;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imageBanner = itemView.findViewById(R.id.ivBanner);
            probr = itemView.findViewById(R.id.probr);
            animation_view = itemView.findViewById(R.id.ivLottieView);
//            txtLable = itemView.findViewById(R.id.txtLable);
//            txtPoints = itemView.findViewById(R.id.txtPoints);
            image_gif = itemView.findViewById(R.id.ivGIF);
//            relStory = itemView.findViewById(R.id.relStory);
            layoutContent = itemView.findViewById(R.id.layoutContent);
            cardContent = itemView.findViewById(R.id.cardContent);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            clickListener.onItemClick(this.getLayoutPosition(), v);

        }
    }
}
