package com.example.daisoinmyhouse;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> implements OnProductItemClickListener {
    ArrayList<Item> items = new ArrayList<Item>();
    OnProductItemClickListener listener;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View itemView = inflater.inflate(R.layout.fragment_home_item, viewGroup, false);

        return new ViewHolder(itemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        Item item = items.get(position);
        viewHolder.setItem(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItem(Item item) {
        items.add(item);
    }

    public void setItems(ArrayList<Item> items){
        this.items = items;
    }

    public Item getItem(int position){
        return items.get(position);
    }

    public void setItem(int position, Item item){
        items.set(position, item);
    }

    public void setOnItemClickListener(OnProductItemClickListener listener){
        this.listener = listener;
    }

    @Override
    public void onItemClick(ViewHolder holder, View view, int position) {
        if(listener != null){
            listener.onItemClick(holder, view, position);
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView tv1;
        TextView tv2;
        TextView tv3;
        TextView tv4;

        ImageView imageView1;

        public ViewHolder(View itemView, final OnProductItemClickListener listener){
            super(itemView);

            tv1 = itemView.findViewById(R.id.tv_item_name);
            tv2 = itemView.findViewById(R.id.tv_item_price);
            tv3 = itemView.findViewById(R.id.tv_item_location);
            tv4 = itemView.findViewById(R.id.tv_item_time);

            imageView1 = itemView.findViewById(R.id.imageView_item);

            itemView.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();

                    if(listener != null){
                        listener.onItemClick(ViewHolder.this, view, position);
                    }
                }
            });
        }

        public void setItem(Item item){
            tv1.setText(item.getName());
            tv2.setText(String.valueOf(item.getPrice()));
            tv3.setText(item.getLocation());
            tv4.setText(item.getTime());

            imageView1.setImageResource(item.getImageRes());
        }
    }
}
