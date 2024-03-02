package com.app.bigprize.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.bigprize.Async.Models.Big_TaskOfferFootSteps;
import com.app.bigprize.R;
import com.app.bigprize.utils.BIG_Common_Utils;

import java.util.List;

public class Big_SimpleText_Adapter extends  RecyclerView.Adapter<Big_SimpleText_Adapter.SavedHolder>{
    public List<Big_TaskOfferFootSteps> listTasks;
    private Context context;

    public Big_SimpleText_Adapter(List<Big_TaskOfferFootSteps> list, Context context) {
        this.listTasks = list;
        this.context = context;
    }

    @NonNull
    @Override
    public SavedHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.big_item_simple_text, parent, false);
        return new SavedHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SavedHolder holder, final int position) {
        try {
            holder.tvText.setText(listTasks.get(position).getSteps());
            if (!BIG_Common_Utils.isStringNullOrEmpty(listTasks.get(position).getBgcolor())) {
                holder.tvText.setBackgroundColor(Color.parseColor(listTasks.get(position).getBgcolor()));
            }
            if (!BIG_Common_Utils.isStringNullOrEmpty(listTasks.get(position).getFontcolor())) {
                holder.tvText.setTextColor(Color.parseColor(listTasks.get(position).getFontcolor()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return listTasks.size();
    }

    public class SavedHolder extends RecyclerView.ViewHolder {
        private TextView tvText;

        public SavedHolder(@NonNull View itemView) {
            super(itemView);
            tvText = itemView.findViewById(R.id.tvText);
        }
    }
}
