package com.app.bigprize.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.bigprize.Async.Models.Big_Alphabet_Item;
import com.app.bigprize.R;

import java.util.List;


public class Big_AlphabetAdapter extends RecyclerView.Adapter<Big_AlphabetAdapter.SavedHolder>  {
    public List<Big_Alphabet_Item> data;
    private Context context;

    private ClickListener clickListener;

    public Big_AlphabetAdapter(List<Big_Alphabet_Item> list, Context context, ClickListener clickListener) {
        this.data = list;
        this.context = context;
        this.clickListener = clickListener;

    }

    @NonNull
    @Override
    public SavedHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.big_item_alphabet, parent, false);
        return new SavedHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SavedHolder holder, int position) {
//        try {

        holder.tvAlphabet.setText(data.get(position).getValue());

//        } catch (Exception e) {
//            e.printStackTrace();
//        }

    }

    @Override
    public int getItemCount() {
        return data.size();
    }
    public class SavedHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView tvAlphabet;

        public SavedHolder(@NonNull View itemView) {
            super(itemView);
            tvAlphabet = itemView.findViewById(R.id.tvAlphabet);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            clickListener.onItemClick(this.getLayoutPosition(), v,tvAlphabet);
        }
    }
    public interface ClickListener {
        void onItemClick(int position, View v,TextView textView);
    }
}
