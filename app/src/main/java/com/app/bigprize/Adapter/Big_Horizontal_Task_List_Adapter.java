package com.app.bigprize.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
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
import com.app.bigprize.Async.Models.Big_TaskOffer;
import com.app.bigprize.R;
import com.app.bigprize.utils.BIG_Common_Utils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;



public class Big_Horizontal_Task_List_Adapter extends RecyclerView.Adapter<Big_Horizontal_Task_List_Adapter.ViewHolder> {
    public ArrayList<Big_TaskOffer> listTask;
    private Context context;
    private ClickListener clickListener;

    public Big_Horizontal_Task_List_Adapter(Context context, ArrayList<Big_TaskOffer> list, ClickListener fileListClickInterface) {
        this.context = context;
        this.listTask = list;
        this.clickListener = fileListClickInterface;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.big_item_horizontal_task_list, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        try {
            if (listTask.get(position).getIcon() != null) {

                if (listTask.get(position).getIcon().contains(".json")) {
                    holder.ivIcon.setVisibility(View.GONE);
                    holder.ivLottie.setVisibility(View.VISIBLE);
                    BIG_Common_Utils.setLottieAnimation(holder.ivLottie, listTask.get(position).getIcon());
                    holder.ivLottie.setRepeatCount(LottieDrawable.INFINITE);
                    holder.probr.setVisibility(View.GONE);
                } else {
                    holder.ivIcon.setVisibility(View.VISIBLE);
                    holder.ivLottie.setVisibility(View.GONE);
                    Glide.with(context)
                            .load(listTask.get(position).getIcon())
                            .override(context.getResources().getDimensionPixelSize(R.dimen.dim_54), context.getResources().getDimensionPixelSize(R.dimen.dim_54))
                            .addListener(new RequestListener<Drawable>() {
                                @Override
                                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                    holder.probr.setVisibility(View.GONE);
                                    return false;
                                }
                            })
                            .into(holder.ivIcon);
                }
            }

            if (listTask.get(position).getTitle() != null) {
                holder.tvTitle.setText(listTask.get(position).getTitle());
            }
            if (listTask.get(position).getTitleTextColor() != null) {
                holder.tvTitle.setTextColor(Color.parseColor(listTask.get(position).getTitleTextColor()));
            } else {
                holder.tvTitle.setTextColor(context.getColor(R.color.black_font));
            }

            if (listTask.get(position).getPoints().matches("0")) {
                holder.layoutPoints.setVisibility(View.GONE);
            } else {
                holder.layoutPoints.setVisibility(View.VISIBLE);
                if (listTask.get(position).getPoints() != null) {
                    holder.tvPoints.setText(listTask.get(position).getPoints());
                }
                if (listTask.get(position).getBtnColor() != null && listTask.get(position).getBtnColor().length() > 0) {
                    Drawable mDrawable = ContextCompat.getDrawable(context, R.drawable.big_bg_sidemenu_social);
                    mDrawable.setColorFilter(new PorterDuffColorFilter(Color.parseColor(listTask.get(position).getBtnColor()), PorterDuff.Mode.SRC_IN));
                    holder.layoutPoints.setBackground(mDrawable);

//                    holder.layoutPoints.setBackgroundColor(Color.parseColor(listTask.get(position).getBtnColor()));
                    Drawable mDrawable2 = ContextCompat.getDrawable(context, R.drawable.big_bg_sidemenu_social);
                    mDrawable2.setColorFilter(new PorterDuffColorFilter(Color.parseColor(listTask.get(position).getBtnColor().replace("#", "#4D")), PorterDuff.Mode.SRC_IN));
                    holder.layoutParentBorder.setBackground(mDrawable2);
                }

                if (listTask.get(position).getBtnTextColor() != null && listTask.get(position).getBtnTextColor().length() > 0) {
                    holder.tvPoints.setTextColor(Color.parseColor(listTask.get(position).getBtnTextColor()));
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    @Override
    public int getItemCount() {
        return listTask == null ? 0 : listTask.size();
    }

    public interface ClickListener {
        void onItemClick(int position, View v);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView tvTitle, tvPoints, tvLabel;
        private ImageView ivIcon;
        private FrameLayout layoutMain;
        private CardView cardBanner;
        private LottieAnimationView ivLottie;
        private ProgressBar probr;
        private RelativeLayout layoutIcon;
        private LinearLayout layoutPoints, layoutParent, layoutContent,layoutParentBorder;

        public ViewHolder(View view) {
            super(view);
            layoutIcon=itemView.findViewById(R.id.layoutIcon);
            cardBanner=itemView.findViewById(R.id.cardBanner);
            layoutParentBorder=itemView.findViewById(R.id.layoutParentBorder);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvTitle.setSelected(true);
            ivIcon = itemView.findViewById(R.id.ivIcon);
            ivLottie = itemView.findViewById(R.id.ivLottie);
            tvLabel = itemView.findViewById(R.id.tvLabel);
            tvPoints = itemView.findViewById(R.id.tvPoints);
            probr = itemView.findViewById(R.id.probr);
            layoutPoints = itemView.findViewById(R.id.layoutPoints);
            layoutParent = itemView.findViewById(R.id.layoutParent);
            layoutMain = itemView.findViewById(R.id.layoutMain);

            layoutMain.setOnClickListener(this);
            layoutContent = itemView.findViewById(R.id.layoutContent);

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            clickListener.onItemClick(this.getLayoutPosition(), v);
        }
    }
}
