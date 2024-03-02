package com.app.bigprize.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.bigprize.Async.Models.Big_Cards_Item;
import com.app.bigprize.R;

import java.util.List;



public class Big_CardMatchAdapter extends RecyclerView.Adapter<Big_CardMatchAdapter.SavedHolder>  {
    public List<Big_Cards_Item> data;
    private Context context;

    private ClickListener clickListener;

    public Big_CardMatchAdapter(List<Big_Cards_Item> list, Context context, ClickListener clickListener) {
        this.data = list;
        this.context = context;
        this.clickListener = clickListener;

    }

    @NonNull
    @Override
    public SavedHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.big_item_cards, parent, false);
        return new SavedHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SavedHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return data.size();
    }
    public class SavedHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private ImageView ivCard;

        public SavedHolder(@NonNull View itemView) {
            super(itemView);
            ivCard = itemView.findViewById(R.id.ivCard);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            clickListener.onItemClick(this.getLayoutPosition(), v,ivCard);
        }
    }
    public interface ClickListener {
        void onItemClick(int position, View v, ImageView imageView);
    }
}
