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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieDrawable;
import com.app.bigprize.Async.Models.Big_Home_Data_Item;
import com.app.bigprize.Async.Models.Big_Response_Model;
import com.app.bigprize.R;
import com.app.bigprize.utils.BIG_AppLogger;
import com.app.bigprize.utils.BIG_Common_Utils;
import com.app.bigprize.utils.BIG_SharePrefs;
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


public class Big_Home_Task_Offer_List_Adapter extends RecyclerView.Adapter<Big_Home_Task_Offer_List_Adapter.SavedHolder> {


    private Big_Response_Model responseMain;
    public List<Big_Home_Data_Item> data;
    private Context context;
    private ClickListener clickListener;
    private RequestOptions requestOptions = new RequestOptions();

    public Big_Home_Task_Offer_List_Adapter(List<Big_Home_Data_Item> list, Context context, ClickListener clickListener) {
        this.data = list;
        this.context = context;
        this.clickListener = clickListener;
        requestOptions = requestOptions.transforms(new CenterCrop(), new RoundedCorners(context.getResources().getDimensionPixelSize(R.dimen.dim_5)));
        responseMain = new Gson().fromJson(BIG_SharePrefs.getInstance().getString(BIG_SharePrefs.HomeData), Big_Response_Model.class);
    }


    @NonNull
    @Override
    public SavedHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.big_item_home_task_offer_list, parent, false);
        return new SavedHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SavedHolder holder, final int position) {
        try {
            String image = null;
            if (!BIG_Common_Utils.isStringNullOrEmpty(data.get(position).getJsonImage())) {
                image = data.get(position).getJsonImage();
            } else if (!BIG_Common_Utils.isStringNullOrEmpty(data.get(position).getIcon())) {
                image = data.get(position).getIcon();
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
                    Glide.with(context)
                            .load(image)
                            .override(context.getResources().getDimensionPixelSize(R.dimen.dim_56), context.getResources().getDimensionPixelSize(R.dimen.dim_56))
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

            BIG_AppLogger.getInstance().e("#color",data.get(position).getBtnColor());
            if (data.get(position).getBtnColor() != null && data.get(position).getBtnColor().length() > 0) {
                GradientDrawable drawable = (GradientDrawable) ContextCompat.getDrawable(context, R.drawable.big_bg_points_gradient);
                drawable.mutate(); // only change this instance of the xml, not all components using this xml
                drawable.setColor(Color.parseColor(data.get(position).getBtnColor()));
                holder.layoutPoints.setBackground(drawable);

                GradientDrawable drawable1 = (GradientDrawable) ContextCompat.getDrawable(context, R.drawable.big_bg_earningtask);
                drawable1.mutate(); // only change this instance of the xml, not all components using this xml
                drawable1.setStroke(context.getResources().getDimensionPixelSize(R.dimen.dim_1_5), Color.parseColor(data.get(position).getBtnColor()));
                holder.layoutContent.setBackground(drawable1);
            }

            holder.tvName.setText(data.get(position).getTitle());
            holder.tvDescription.setText(data.get(position).getDescription());
//            holder.ivLottieScratch.setVisibility(data.get(position).getIsScratchCard() != null && data.get(position).getIsScratchCard().equals("1") ? View.VISIBLE : View.GONE);
            holder.tvPoints.setText("" + data.get(position).getPoints());

            if (data.get(position).getTagList() != null && !data.get(position).getTagList().isEmpty()) {
                String strTag = data.get(position).getTagList();
                List<String> arr = Arrays.asList(strTag.split("\\s*,\\s*"));

                for (int i = 0; i < arr.size(); i++) {
                    Drawable mDrawable = ContextCompat.getDrawable(context, R.drawable.big_bg_task_tagd);
                    int[] androidColors = context.getResources().getIntArray(R.array.androidcolors);
                    int randomAndroidColor = androidColors[new Random().nextInt(androidColors.length)];
                    mDrawable.setColorFilter(new PorterDuffColorFilter(randomAndroidColor, PorterDuff.Mode.SRC_IN));

                    FrameLayout.LayoutParams lparams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
                    lparams.setMargins(context.getResources().getDimensionPixelSize(R.dimen.dim_2), 0, context.getResources().getDimensionPixelSize(R.dimen.dim_2), context.getResources().getDimensionPixelSize(R.dimen.dim_2));
                    TextView tv = new TextView(context);
                    tv.setLayoutParams(lparams);
                    tv.setText(arr.get(i));
                    tv.setGravity(Gravity.CENTER);
                    tv.setPadding(context.getResources().getDimensionPixelSize(R.dimen.dim_5), context.getResources().getDimensionPixelSize(R.dimen.dim_2), context.getResources().getDimensionPixelSize(R.dimen.dim_5), context.getResources().getDimensionPixelSize(R.dimen.dim_2));
                    tv.setTextIsSelectable(false);
                    tv.setTextSize(10);
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

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class SavedHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView tvName, tvDescription,tvPoints;
        private ImageView ivIcon;
        private LottieAnimationView ivLottie;
        private ProgressBar probr;
        private FlexboxLayout layoutTags;
        private LinearLayout layoutPoints,layoutContent;


        public SavedHolder(@NonNull View itemView) {
            super(itemView);
            layoutPoints=itemView.findViewById(R.id.layoutPoints);
            ivLottie = itemView.findViewById(R.id.ivLottie);
            tvName = itemView.findViewById(R.id.tvName);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            ivIcon = itemView.findViewById(R.id.ivIcon);
            tvPoints = itemView.findViewById(R.id.tvPoints);
            probr = itemView.findViewById(R.id.probr);
            layoutTags = itemView.findViewById(R.id.layoutTags);
            layoutContent=itemView.findViewById(R.id.layoutContent);
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
