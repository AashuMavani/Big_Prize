package com.app.bigprize.Adapter;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.app.bigprize.Async.Models.Big_Home_Data_Item;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.List;

public class Big_Home_Horizontal_Task_Adapter extends RecyclerView.Adapter<Big_Home_Horizontal_Task_Adapter.ViewHolder> {
    private Activity activity;
    private List<Big_Home_Data_Item> data;
    private static ClickListener clickListener;
    public Big_Home_Horizontal_Task_Adapter(final Activity context, List<Big_Home_Data_Item> data, ClickListener clickListener) {
        activity = context;
        this.data = data;
        this.clickListener = clickListener;
    }


    @NonNull
    @Override
    public Big_Home_Horizontal_Task_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = null;
       // v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_home_horizontal_task, parent, false);
        return new ViewHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull Big_Home_Horizontal_Task_Adapter.ViewHolder holder, int position) {
        if (data.get(position).getIsNewLable() != null) {
            if (data.get(position).getIsNewLable().matches("1")) {
                holder.txtLable.setVisibility(View.VISIBLE);
                holder.lNew.setVisibility(View.VISIBLE);
            } else {
                holder.txtLable.setVisibility(View.GONE);
                holder.lNew.setVisibility(View.GONE);
            }
        } else {
            holder.txtLable.setVisibility(View.GONE);
            holder.lNew.setVisibility(View.GONE);
        }

        if (data.get(position).getIcon() != null) {
            Glide.with(activity)
                    .load(data.get(position).getIcon())
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, com.bumptech.glide.load.DataSource dataSource, boolean isFirstResource) {
//                            holder.ivIcon.setBackground(activity.getResources().getDrawable(R.drawable.bg_slider));
                            return false;
                        }
                    })
                    .into(holder.ivIcon);

        }

        if (data.get(position).getPoints() != null) {
            holder.txtRuppes.setText(data.get(position).getPoints());
        }

        if (data.get(position).getTitle() != null) {
            holder.txtTitle.setText(data.get(position).getTitle());
        }
        holder.txtTitle.setSelected(true);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView txtRuppes, txtTitle;
        private ImageView ivIcon;
        private RelativeLayout relStory;
        TextView txtLable;
        LinearLayout lNew;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
//            ivIcon = itemView.findViewById(R.id.ivSmallIcon);
//            txtRuppes = itemView.findViewById(R.id.txtRuppes);
//            txtTitle = itemView.findViewById(R.id.txtTitle);
//            relStory = itemView.findViewById(R.id.relStory);
//            txtLable = itemView.findViewById(R.id.txtLable);
//            lNew = itemView.findViewById(R.id.lNew);
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
