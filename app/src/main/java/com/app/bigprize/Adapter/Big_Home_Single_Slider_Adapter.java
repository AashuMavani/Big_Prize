package com.app.bigprize.Adapter;

import android.animation.Animator;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieDrawable;
import com.app.bigprize.Async.Models.Big_Home_Data_Item;
import com.app.bigprize.R;
import com.app.bigprize.utils.BIG_Common_Utils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.List;


public class Big_Home_Single_Slider_Adapter extends RecyclerView.Adapter<Big_Home_Single_Slider_Adapter.ViewHolder> {

    private Activity activity;
    private List<Big_Home_Data_Item> data;
    private boolean isGrid;
    private ClickListener clickListener;

    public Big_Home_Single_Slider_Adapter(final Activity context, List<Big_Home_Data_Item> data, boolean isGrid, ClickListener clickListener) {
        activity = context;
        this.data = data;
        this.isGrid = isGrid;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = null;
        if (isGrid) {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.big_row_home_two_grid, parent, false);
        } else {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.big_row_home_single_slider, parent, false);
        }
        return new ViewHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.probr.setVisibility(View.VISIBLE);
        if (isGrid) {
            if (data.get(position).getIcon() != null) {
                if (data.get(position).getIcon().contains(".json")) {
                    holder.image_gif.setVisibility(View.GONE);
                    holder.imageBanner.setVisibility(View.GONE);
                    holder.animation_view.setVisibility(View.VISIBLE);
                    BIG_Common_Utils.setLottieAnimation(holder.animation_view, data.get(position).getIcon());
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
                } else if (data.get(position).getIcon().contains(".gif")) {
                    holder.image_gif.setVisibility(View.VISIBLE);
                    holder.animation_view.setVisibility(View.GONE);
                    holder.imageBanner.setVisibility(View.GONE);

                    Glide.with(activity)
                            .load(data.get(position).getIcon())
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
                            .load(data.get(position).getIcon())
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
            if (data.get(position).getTitle() != null && data.get(position).getTitle().length() > 0) {
                holder.txtLable.setVisibility(View.VISIBLE);
                holder.txtLable.setText(data.get(position).getTitle());
            } else {
                holder.txtLable.setVisibility(View.GONE);
            }
            if (!BIG_Common_Utils.isStringNullOrEmpty(data.get(position).getBtnColor())) {
                GradientDrawable drawable = (GradientDrawable) ContextCompat.getDrawable(activity, R.drawable.big_bg_slider);
                drawable.mutate(); // only change this instance of the xml, not all components using this xml
               drawable.setStroke(activity.getResources().getDimensionPixelSize(R.dimen.dim_1), Color.parseColor(data.get(position).getBtnColor())); // set stroke width and stroke color
                drawable.setColor(Color.parseColor(data.get(position).getBtnColor().replace("#", "#1a")));
                holder.layoutContent.setBackground(drawable);

                GradientDrawable drawable1 = (GradientDrawable) ContextCompat.getDrawable(activity, R.drawable.big_bg_slider);
                drawable1.mutate(); // only change this instance of the xml, not all components using this xml
               drawable1.setStroke(activity.getResources().getDimensionPixelSize(R.dimen.dim_1), Color.parseColor(data.get(position).getBtnColor())); // set stroke width and stroke color
                drawable1.setColor(Color.parseColor(data.get(position).getBtnColor()));
                holder.layoutPoints.setBackground(drawable1);
            }
            if (data.get(position).getPoints() != null && data.get(position).getPoints().length() > 0) {
                holder.layoutPoints.setVisibility(View.VISIBLE);
                holder.txtPoints.setText(data.get(position).getPoints());
            } else {
                holder.layoutPoints.setVisibility(View.GONE);
            }

        } else {
            BIG_Common_Utils.showImageLotteGIF(activity, data.get(position).getJsonImage(), data.get(position).getImage(), holder.animation_view, holder.imageBanner, holder.image_gif, holder.probr, null);
            if (data.get(position).getLable() != null && data.get(position).getLable().length() > 0) {
                holder.txtLable.setVisibility(View.VISIBLE);
                holder.txtLable.setText(data.get(position).getLable());
            } else {
                holder.txtLable.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public int getItemCount() {
                   return data.size();
    }

    public interface ClickListener {
        void onItemClick(int position, View v);
    }
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView imageBanner;
        private ProgressBar probr;
        private LottieAnimationView animation_view;
        private TextView txtLable, txtPoints;
        private ImageView image_gif;
        private RelativeLayout relStory;
        private CardView cardContent;
        private LinearLayout layoutContent,layoutPoints;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageBanner = itemView.findViewById(R.id.ivBanner);
            probr = itemView.findViewById(R.id.probr);
            animation_view = itemView.findViewById(R.id.ivLottieView);
            txtLable = itemView.findViewById(R.id.txtLable);
            txtPoints = itemView.findViewById(R.id.txtPoints);
            image_gif = itemView.findViewById(R.id.ivGIF);
            relStory = itemView.findViewById(R.id.relStory);
            layoutContent = itemView.findViewById(R.id.layoutContent);
            layoutPoints=itemView.findViewById(R.id.layoutPoints);
//            cardContent = itemView.findViewById(R.id.cardContent);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            clickListener.onItemClick(this.getLayoutPosition(), v);
        }
    }

}
