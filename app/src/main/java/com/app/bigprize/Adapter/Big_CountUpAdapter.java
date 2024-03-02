package com.app.bigprize.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.bigprize.Async.Models.Big_Count_Up_Item;
import com.app.bigprize.R;

import java.util.List;


public class Big_CountUpAdapter extends RecyclerView.Adapter<Big_CountUpAdapter.SavedHolder>  {
    public List<Big_Count_Up_Item> data;
    private Context context;

    private ClickListener clickListener;

    public Big_CountUpAdapter(List<Big_Count_Up_Item> list, Context context, ClickListener clickListener) {
        this.data = list;
        this.context = context;
        this.clickListener = clickListener;

    }

    @NonNull
    @Override
    public SavedHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.big_item_count, parent, false);
        return new SavedHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SavedHolder holder, int position) {
//        try {

        holder.tvNumber.setText(data.get(position).getValue());
        if (data.get(position).getValue().equals("")){
            holder.tvNumber.setVisibility(View.GONE);
        }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

    }

    @Override
    public int getItemCount() {
        return data.size();
    }
    public class SavedHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView tvNumber;

        public SavedHolder(@NonNull View itemView) {
            super(itemView);
            tvNumber = itemView.findViewById(R.id.tvNumber);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            clickListener.onItemClick(this.getLayoutPosition(), v,tvNumber);
        }
    }
    public interface ClickListener {
        void onItemClick(int position, View v,TextView textView);
    }
}
