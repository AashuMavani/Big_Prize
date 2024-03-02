package com.app.bigprize.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.app.bigprize.Async.Models.Big_Daily_Bonus_Item;
import com.app.bigprize.R;

import java.util.List;


public class Big_DailyClaimAdapter extends RecyclerView.Adapter<Big_DailyClaimAdapter.SavedHolder> {

    public List<Big_Daily_Bonus_Item> listDailyBonus;
    private Context context;
    private int lastClaimedDay, isLoginToday;
    private ClickListener clickListener;
    private boolean isClicked = false;

    public Big_DailyClaimAdapter(List<Big_Daily_Bonus_Item> list, Context context, int lastClaimedDay, int isLoginToday, ClickListener clickListener) {
        this.listDailyBonus = list;
        this.context = context;
        this.lastClaimedDay = lastClaimedDay;
        this.isLoginToday = isLoginToday;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public SavedHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.big_item_daily_login, parent, false);
        return new SavedHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SavedHolder holder, final int position) {
        try {
            holder.tvDay.setText("Day " + listDailyBonus.get(position).getDay_id());
            holder.tvPoints.setText(listDailyBonus.get(position).getDay_points());
            if (isLoginToday != 1 && Integer.parseInt(listDailyBonus.get(position).getDay_id()) == lastClaimedDay + 1 && position > 0) {
                holder.ivLock.setVisibility(View.GONE);
            } else if (Integer.parseInt(listDailyBonus.get(position).getDay_id()) > lastClaimedDay && position > 0) {
                holder.ivLock.setVisibility(View.VISIBLE);
            } else {
                holder.ivLock.setVisibility(View.GONE);
            }
//            if ((Integer.parseInt(listDailyBonus.get(position).getDay_id()) == lastClaimedDay + 1 || (lastClaimedDay == 0 && position == 0)) && isLoginToday != 1 && !isClicked && context instanceof ActivityDailyLogin) {
//                holder.viewTouch.setVisibility(View.VISIBLE);
//                holder.viewTouch.playAnimation();
//            } else {
//                holder.viewTouch.setVisibility(View.GONE);
//                holder.viewTouch.clearAnimation();
//            }
            if ((Integer.parseInt(listDailyBonus.get(position).getDay_id()) == lastClaimedDay + 1 || (lastClaimedDay == 0 && position == 0)) && isLoginToday != 1) {
                holder.tvPoints.setTextColor(context.getColor(R.color.white));
                holder.lottieLight.setVisibility(View.VISIBLE);
                holder.lottieLight.playAnimation();
            } else {
                holder.tvPoints.setTextColor(context.getColor(R.color.white));
                holder.lottieLight.setVisibility(View.GONE);
                holder.lottieLight.clearAnimation();
            }
//             holder.layoutContent.getBackground().setTint(Integer.parseInt(listDailyBonus.get(position).getDay_id()) <= lastClaimedDay ? Color.parseColor("#065e2b") : context.getColor(R.color.colorPrimary));

            holder.ivDone.setVisibility(Integer.parseInt(listDailyBonus.get(position).getDay_id()) <= lastClaimedDay ? View.VISIBLE : View.GONE);
            holder.ivCoin.setVisibility(Integer.parseInt(listDailyBonus.get(position).getDay_id()) <= lastClaimedDay ? View.GONE : View.VISIBLE);
            holder.tvPoints.setVisibility(Integer.parseInt(listDailyBonus.get(position).getDay_id()) <= lastClaimedDay ? View.GONE : View.VISIBLE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setLastClaimedData(int lastClaimedDay, int isLoginToday) {
        this.lastClaimedDay = lastClaimedDay;
        this.isLoginToday = isLoginToday;
        notifyDataSetChanged();
    }

    public void setClicked() {
        isClicked = true;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return listDailyBonus.size();
    }

    public class SavedHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private LinearLayout layoutContent,done_bg;
        private TextView tvPoints, tvDay;
        private ImageView ivLock, ivDone, ivCoin;
        private LottieAnimationView viewTouch, lottieLight;

        public SavedHolder(@NonNull View itemView) {
            super(itemView);
//            done_bg=itemView.findViewById(R.id.done_bg);
            tvPoints = itemView.findViewById(R.id.tvPoints);
            tvDay = itemView.findViewById(R.id.tvDay);
            layoutContent = itemView.findViewById(R.id.layoutContent);
            ivLock = itemView.findViewById(R.id.ivLock);
            ivDone = itemView.findViewById(R.id.ivDone);
            viewTouch = itemView.findViewById(R.id.viewTouch);
            lottieLight = itemView.findViewById(R.id.lottieLight);
            ivCoin = itemView.findViewById(R.id.ivCoin);
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
