package com.app.bigprize.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
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
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieDrawable;
import com.app.bigprize.Async.Models.Big_Response_Model;
import com.app.bigprize.Async.Models.Big_TaskOffer;
import com.app.bigprize.R;
import com.app.bigprize.utils.BIG_Common_Utils;
import com.app.bigprize.utils.BIG_SharePrefs;
import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxError;
import com.applovin.mediation.nativeAds.MaxNativeAdListener;
import com.applovin.mediation.nativeAds.MaxNativeAdLoader;
import com.applovin.mediation.nativeAds.MaxNativeAdView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.google.android.flexbox.FlexboxLayout;
import com.google.gson.Gson;

import java.util.Arrays;
import java.util.List;
import java.util.Random;


public class Big_Task_Offer_List_Adapter extends RecyclerView.Adapter<Big_Task_Offer_List_Adapter.SavedHolder> {

    public List<Big_TaskOffer> listTasks;
    private Big_Response_Model responseMain;
    private Context context;
    private ClickListener clickListener;
    private RequestOptions requestOptions = new RequestOptions();

    public Big_Task_Offer_List_Adapter(List<Big_TaskOffer> list, Context context, ClickListener clickListener) {
        this.listTasks = list;
        this.context = context;
        this.clickListener = clickListener;
        responseMain = new Gson().fromJson(BIG_SharePrefs.getInstance().getString(BIG_SharePrefs.HomeData), Big_Response_Model.class);
        requestOptions = requestOptions.transforms(new CenterCrop(), new RoundedCorners(context.getResources().getDimensionPixelSize(R.dimen.dim_5)));
    }

    @NonNull
    @Override
    public SavedHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.big_item_tasks, parent, false);
        return new SavedHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SavedHolder holder, final int position) {
        try {
            if ((position + 1) % 5 == 0 && BIG_Common_Utils.isShowAppLovinNativeAds()) {
                loadAppLovinNativeAds(holder.fl_adplaceholder);
            }
            if (!BIG_Common_Utils.isStringNullOrEmpty(listTasks.get(position).getIsShowBanner()) && listTasks.get(position).getIsShowBanner().equals("1")) {
                holder.layoutFullImage.setVisibility(View.VISIBLE);
                holder.layoutMain.setVisibility(View.GONE);
                if (listTasks.get(position).getDisplayImage() != null) {
                    if (listTasks.get(position).getDisplayImage().contains(".json")) {
                        holder.ivFullImage.setVisibility(View.GONE);
                        holder.ivLottieFullImage.setVisibility(View.VISIBLE);
                        BIG_Common_Utils.setLottieAnimation(holder.ivLottieFullImage, listTasks.get(position).getDisplayImage());
                        holder.ivLottieFullImage.setRepeatCount(LottieDrawable.INFINITE);
                        holder.progressBarFullImage.setVisibility(View.GONE);
                    } else {
                        holder.ivFullImage.setVisibility(View.VISIBLE);
                        holder.ivLottieFullImage.setVisibility(View.GONE);
                        Glide.with(context)
                                .load(listTasks.get(position).getDisplayImage())
                                .addListener(new RequestListener<Drawable>() {
                                    @Override
                                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                        return false;
                                    }

                                    @Override
                                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                        holder.progressBarFullImage.setVisibility(View.GONE);
                                        return false;
                                    }
                                })
                                .into(holder.ivFullImage);
                    }
                }
            } else {
                holder.layoutFullImage.setVisibility(View.GONE);
                holder.layoutMain.setVisibility(View.VISIBLE);
//                holder.ivLottieScratch.setVisibility(listTasks.get(position).getIsScratchCard() != null && listTasks.get(position).getIsScratchCard().equals("1") ? View.VISIBLE : View.GONE);
                if (listTasks.get(position).getIcon() != null) {
                    if (listTasks.get(position).getIcon().contains(".json")) {
                        holder.ivIcon.setVisibility(View.GONE);
                        holder.ivLottie.setVisibility(View.VISIBLE);
                        BIG_Common_Utils.setLottieAnimation(holder.ivLottie, listTasks.get(position).getIcon());
                        holder.ivLottie.setRepeatCount(LottieDrawable.INFINITE);
                        holder.probr.setVisibility(View.GONE);
                    } else {
                        holder.ivIcon.setVisibility(View.VISIBLE);
                        holder.ivLottie.setVisibility(View.GONE);
                        Glide.with(context)
                                .load(listTasks.get(position).getIcon())
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

                if (listTasks.get(position).getTitle() != null) {
                    holder.tvTitle.setText(listTasks.get(position).getTitle());
                }
                if (listTasks.get(position).getTitleTextColor() != null) {
                    holder.tvTitle.setTextColor(Color.parseColor(listTasks.get(position).getTitleTextColor()));
                } else {
                    holder.tvTitle.setTextColor(context.getColor(R.color.black_font));
                }
                if (listTasks.get(position).getDescription() != null) {
                    holder.tvDescription.setText(listTasks.get(position).getDescription());
                }
                if (listTasks.get(position).getDescriptionTextColor() != null) {
                    holder.tvDescription.setTextColor(Color.parseColor(listTasks.get(position).getDescriptionTextColor()));
                } else {
                    holder.tvDescription.setTextColor(context.getColor(R.color.grey_color));
                }

                if (listTasks.get(position).getPoints().matches("0")) {
                    holder.layoutPoints.setVisibility(View.GONE);
                } else {
                    holder.layoutPoints.setVisibility(View.VISIBLE);
                    if (listTasks.get(position).getPoints() != null) {
                        holder.tvPoints.setText(listTasks.get(position).getIsScratchCard() != null && listTasks.get(position).getIsScratchCard().equals("1") ? ("" + listTasks.get(position).getPoints()) : listTasks.get(position).getPoints());
                    }
                }

                if (!BIG_Common_Utils.isStringNullOrEmpty(listTasks.get(position).getIsShareTask()) && listTasks.get(position).getIsShareTask().equals("1")) {
                    holder.layoutReferTaskPoints.setVisibility(View.VISIBLE);
                    holder.tvReferTaskPoints.setText(listTasks.get(position).getShareTaskPoint() + " / Task Referral");
                } else {
                    holder.layoutReferTaskPoints.setVisibility(View.GONE);
                }

                if (listTasks.get(position).getBtnColor() != null && listTasks.get(position).getBtnColor().length() > 0) {
                    GradientDrawable drawable = (GradientDrawable) ContextCompat.getDrawable(context, R.drawable.big_ic_btn);
                    drawable.mutate(); // only change this instance of the xml, not all components using this xml
                    drawable.setStroke(context.getResources().getDimensionPixelSize(R.dimen.dim_1_5), Color.parseColor(listTasks.get(position).getBtnColor())); // set stroke width and stroke color
                    drawable.setColor(Color.parseColor(listTasks.get(position).getBtnColor().replace("#", "#0D")));
                    holder.layoutPoints.setBackground(drawable);
                }

                if (listTasks.get(position).getTagList() != null && !listTasks.get(position).getTagList().isEmpty()) {
                    String strTag = listTasks.get(position).getTagList();
                    List<String> arr = Arrays.asList(strTag.split("\\s*,\\s*"));

                    for (int i = 0; i < arr.size(); i++) {
                        Drawable mDrawable = ContextCompat.getDrawable(context, R.drawable.big_bg_task_tagd);
                        int[] androidColors = context.getResources().getIntArray(R.array.androidcolors);
                        int randomAndroidColor = androidColors[new Random().nextInt(androidColors.length)];
                        mDrawable.setColorFilter(new PorterDuffColorFilter(randomAndroidColor, PorterDuff.Mode.SRC_IN));

                        FrameLayout.LayoutParams lparams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
                        lparams.setMargins(context.getResources().getDimensionPixelSize(R.dimen.dim_5), 0, context.getResources().getDimensionPixelSize(R.dimen.dim_5), context.getResources().getDimensionPixelSize(R.dimen.dim_5));
                        TextView tv = new TextView(context);
                        tv.setLayoutParams(lparams);
                        tv.setText(arr.get(i));
                        tv.setGravity(Gravity.CENTER);
                        tv.setPadding(context.getResources().getDimensionPixelSize(R.dimen.dim_8), context.getResources().getDimensionPixelSize(R.dimen.dim_2), context.getResources().getDimensionPixelSize(R.dimen.dim_8), context.getResources().getDimensionPixelSize(R.dimen.dim_2));
                        tv.setTextIsSelectable(false);
                        tv.setTextSize(11);
                        tv.setIncludeFontPadding(false);
                        tv.setTypeface(tv.getTypeface(), Typeface.NORMAL);
                        tv.setTextColor(context.getColor(R.color.white));
                        tv.setBackground(mDrawable);
                        holder.layoutTags.addView(tv);
                    }
                    holder.layoutTags.setVisibility(View.VISIBLE);
                } else {
                    holder.layoutTags.setVisibility(View.GONE);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return listTasks.size();
    }

    private void loadAppLovinNativeAds(FrameLayout adLayout) {
        try {
            MaxNativeAdLoader nativeAdLoader = new MaxNativeAdLoader(BIG_Common_Utils.getRandomAdUnitId(responseMain.getLovinNativeID()), context);
            nativeAdLoader.setNativeAdListener(new MaxNativeAdListener() {
                @Override
                public void onNativeAdLoaded(final MaxNativeAdView nativeAdView, final MaxAd ad) {
                    adLayout.removeAllViews();
                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) adLayout.getLayoutParams();
                    params.height = context.getResources().getDimensionPixelSize(R.dimen.dim_300);
                    params.width = LinearLayout.LayoutParams.MATCH_PARENT;
                    adLayout.setLayoutParams(params);
                    adLayout.setPadding((int) context.getResources().getDimension(R.dimen.dim_10), (int) context.getResources().getDimension(R.dimen.dim_10), (int) context.getResources().getDimension(R.dimen.dim_10), (int) context.getResources().getDimension(R.dimen.dim_10));
                    adLayout.addView(nativeAdView);
                    adLayout.setVisibility(View.VISIBLE);
                    //AppLogger.getInstance().e("TASK AppLovin Loaded: ", "===");
                }

                @Override
                public void onNativeAdLoadFailed(final String adUnitId, final MaxError error) {
                    //AppLogger.getInstance().e("TASK AppLovin Failed: ", error.getMessage());
                    adLayout.setVisibility(View.GONE);
                }

                @Override
                public void onNativeAdClicked(final MaxAd ad) {

                }
            });
            nativeAdLoader.loadAd();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public interface ClickListener {
        void onItemClick(int position, View v);
    }

    public class SavedHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView tvTitle, tvDescription, tvPoints, tvReferTaskPoints;
        private ImageView ivIcon, ivFullImage;
        private LottieAnimationView ivLottie, ivLottieFullImage, ivLottieScratch;
        private FlexboxLayout layoutTags;
        private LinearLayout layoutPoints, layoutReferTaskPoints,layoutMain;
        private ProgressBar probr, progressBarFullImage;
        private FrameLayout fl_adplaceholder;
        private RelativeLayout layoutFullImage;

        public SavedHolder(@NonNull View itemView) {
            super(itemView);
            layoutFullImage = itemView.findViewById(R.id.layoutFullImage);
            progressBarFullImage = itemView.findViewById(R.id.progressBarFullImage);
            ivFullImage = itemView.findViewById(R.id.ivFullImage);
            ivLottieFullImage = itemView.findViewById(R.id.ivLottieFullImage);
//            ivLottieScratch = itemView.findViewById(R.id.ivLottieScratch);
            fl_adplaceholder = itemView.findViewById(R.id.fl_adplaceholder);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            ivIcon = itemView.findViewById(R.id.ivIcon);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            ivLottie = itemView.findViewById(R.id.ivLottie);
            layoutTags = itemView.findViewById(R.id.layoutTags);
            tvReferTaskPoints = itemView.findViewById(R.id.tvReferTaskPoints);
            tvPoints = itemView.findViewById(R.id.tvPoints);
            layoutPoints = itemView.findViewById(R.id.layoutPoints);
            probr = itemView.findViewById(R.id.progressBar);
            layoutReferTaskPoints = itemView.findViewById(R.id.layoutReferTaskPoints);
            layoutMain = itemView.findViewById(R.id.layoutMain);
            layoutMain.setOnClickListener(this);
            layoutFullImage.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            clickListener.onItemClick(this.getLayoutPosition(), v);
        }
    }
}
