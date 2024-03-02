package com.app.bigprize.Adapter;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieDrawable;
import com.app.bigprize.Async.Models.Big_Quiz_My_History_Item;
import com.app.bigprize.Async.Models.Big_Response_Model;
import com.app.bigprize.R;
import com.app.bigprize.utils.BIG_Common_Utils;
import com.app.bigprize.utils.BIG_SharePrefs;
import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxError;
import com.applovin.mediation.nativeAds.MaxNativeAdListener;
import com.applovin.mediation.nativeAds.MaxNativeAdLoader;
import com.applovin.mediation.nativeAds.MaxNativeAdView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;

import java.util.List;



public class Big_Quiz_My_History_Adapter extends RecyclerView.Adapter<Big_Quiz_My_History_Adapter.SavedHolder> {
    private Big_Response_Model responseMain;
    public List<Big_Quiz_My_History_Item> listPointHistory;
    private Context context;
    private RequestOptions requestOptions = new RequestOptions();

    public Big_Quiz_My_History_Adapter(List<Big_Quiz_My_History_Item> list, Context context) {
        this.listPointHistory = list;
        this.context = context;
        requestOptions = requestOptions.transforms(new CenterCrop(), new RoundedCorners(context.getResources().getDimensionPixelSize(R.dimen.dim_5)));
        responseMain = new Gson().fromJson(BIG_SharePrefs.getInstance().getString(BIG_SharePrefs.HomeData), Big_Response_Model.class);
    }

    @NonNull
    @Override
    public SavedHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.big_item_quiz_my_history, parent, false);
        return new SavedHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SavedHolder holder, final int position) {
        try {
            if ((position + 1) % 5 == 0 && BIG_Common_Utils.isShowAppLovinNativeAds()) {
                loadAppLovinNativeAds(holder.fl_adplaceholder);
            }

            if (!BIG_Common_Utils.isStringNullOrEmpty(listPointHistory.get(position).getImage())) {
                holder.layoutImage.setVisibility(VISIBLE);
                if (listPointHistory.get(position).getImage().contains(".json")) {
                    holder.ivImage.setVisibility(View.GONE);
                    holder.ivLottie.setVisibility(View.VISIBLE);
                    BIG_Common_Utils.setLottieAnimation(holder.ivLottie, listPointHistory.get(position).getImage());
                    holder.ivLottie.setRepeatCount(LottieDrawable.INFINITE);
                } else {
                    holder.ivLottie.setVisibility(View.GONE);
                    holder.ivImage.setVisibility(View.VISIBLE);
                    Glide.with(context)
                            .load(listPointHistory.get(position).getImage())
                            .into(holder.ivImage);
                }
            } else {
                holder.layoutImage.setVisibility(GONE);
            }
            holder.tvQuestion.setText(listPointHistory.get(position).getQuestion());

            holder.tvResultPending.setVisibility(listPointHistory.get(position).getIsSattel().equals("1") ? View.GONE : View.VISIBLE);
            holder.layoutPoints.setVisibility(listPointHistory.get(position).getIsSattel().equals("1") ? View.VISIBLE : View.GONE);
            holder.layoutResult.setVisibility(listPointHistory.get(position).getIsSattel().equals("1") ? View.VISIBLE : View.GONE);
            holder.tvPoints.setText(listPointHistory.get(position).getQuizPoints());
            if (!BIG_Common_Utils.isStringNullOrEmpty(listPointHistory.get(position).getUserAnswer())) {
                if (BIG_Common_Utils.isStringNullOrEmpty(listPointHistory.get(position).getAnswer())) {
                    holder.tvMyAnswer.setTextColor(context.getColor(R.color.dark_blue));
                } else {
                    holder.tvMyAnswer.setTextColor(context.getColor(BIG_Common_Utils.isStringNullOrEmpty(listPointHistory.get(position).getAnswer()) && listPointHistory.get(position).getAnswer().equals(listPointHistory.get(position).getUserAnswer()) ? R.color.colorPrimaryDark : R.color.red));
                }
                if (listPointHistory.get(position).getUserAnswer().equalsIgnoreCase("A")) {
                    holder.tvMyAnswer.setText(listPointHistory.get(position).getOptionA());
                } else if (listPointHistory.get(position).getUserAnswer().equalsIgnoreCase("B")) {
                    holder.tvMyAnswer.setText(listPointHistory.get(position).getOptionB());
                } else if (listPointHistory.get(position).getUserAnswer().equalsIgnoreCase("C")) {
                    holder.tvMyAnswer.setText(listPointHistory.get(position).getOptionC());
                } else if (listPointHistory.get(position).getUserAnswer().equalsIgnoreCase("D")) {
                    holder.tvMyAnswer.setText(listPointHistory.get(position).getOptionD());
                } else {
                    holder.tvMyAnswer.setText("-");
                }
            } else {
                holder.tvMyAnswer.setText("-");
            }
            if (listPointHistory.get(position).getIsSattel().equals("1")) {
                holder.tvWinningPoints.setText(listPointHistory.get(position).getPoints());
                holder.tvResultDate.setText(BIG_Common_Utils.modifyDateLayout(listPointHistory.get(position).getWiningDate()));
                if (!BIG_Common_Utils.isStringNullOrEmpty(listPointHistory.get(position).getAnswer())) {
                    if (listPointHistory.get(position).getAnswer().equalsIgnoreCase("A")) {
                        holder.tvAnswer.setText(listPointHistory.get(position).getOptionA());
                    } else if (listPointHistory.get(position).getAnswer().equalsIgnoreCase("B")) {
                        holder.tvAnswer.setText(listPointHistory.get(position).getOptionB());
                    } else if (listPointHistory.get(position).getAnswer().equalsIgnoreCase("C")) {
                        holder.tvAnswer.setText(listPointHistory.get(position).getOptionC());
                    } else if (listPointHistory.get(position).getAnswer().equalsIgnoreCase("D")) {
                        holder.tvAnswer.setText(listPointHistory.get(position).getOptionD());
                    } else {
                        holder.tvAnswer.setText("-");
                    }
                } else {
                    holder.tvAnswer.setText("-");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return listPointHistory.size();
    }

    public class SavedHolder extends RecyclerView.ViewHolder {
        private TextView tvPoints, tvMyAnswer, tvQuestion, tvResultPending, tvResultDate, tvWinningPoints, tvAnswer;
        private RelativeLayout layoutPoints;
        private FrameLayout fl_adplaceholder;
        private LinearLayout layoutResult;
        private LottieAnimationView ivLottie;
        private ImageView ivImage;
        private RelativeLayout layoutImage;

        public SavedHolder(@NonNull View itemView) {
            super(itemView);
            tvMyAnswer = itemView.findViewById(R.id.tvMyAnswer);
            layoutResult = itemView.findViewById(R.id.layoutResult);
            fl_adplaceholder = itemView.findViewById(R.id.fl_adplaceholder);
            tvResultDate = itemView.findViewById(R.id.tvResultDate);
            tvQuestion = itemView.findViewById(R.id.tvQuestion);
            tvWinningPoints = itemView.findViewById(R.id.tvWinningPoints);
            tvPoints = itemView.findViewById(R.id.tvPoints);
            tvAnswer = itemView.findViewById(R.id.tvAnswer);
            tvResultPending = itemView.findViewById(R.id.tvResultPending);
            layoutPoints = itemView.findViewById(R.id.layoutPoints);
            ivLottie = itemView.findViewById(R.id.ivLottie);
            ivImage = itemView.findViewById(R.id.ivImage);
            layoutImage = itemView.findViewById(R.id.layoutImage);
        }
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
                }

                @Override
                public void onNativeAdLoadFailed(final String adUnitId, final MaxError error) {
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
}
