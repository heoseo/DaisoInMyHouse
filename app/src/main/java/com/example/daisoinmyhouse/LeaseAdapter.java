package com.example.daisoinmyhouse;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class LeaseAdapter extends RecyclerView.Adapter<LeaseAdapter.ViewHolder> implements LeaseItemClickListener{

    ArrayList<Lease> items = new ArrayList<Lease>();

    LeaseItemClickListener listener;

    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType){
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View itemView = inflater.inflate(R.layout.activity_rent_list_item, viewGroup, false);

        return new ViewHolder(itemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull LeaseAdapter.ViewHolder holder, int position) {
        Lease item = items.get(position);
        holder.setItem(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItem(Lease item){
        items.add(item);
    }

    public void setItems(ArrayList<Lease> items){
        this.items = items;
    }

    public Lease getItem(int position){
        return items.get(position);
    }

    public void setItem(int position, Lease item){
        items.set(position, item);
    }

    public void setOnItemClickListener(LeaseItemClickListener listener){
        this.listener = listener;
    }

    public void onItemClick(ViewHolder holder, View view, int position){
        if(listener != null){
            listener.onItemClick(holder, view, position);
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView tv1;
        TextView tv2;
        TextView tv3;
        TextView tv4;

        ImageView imageView;

        public ViewHolder(View itemView, final LeaseItemClickListener listener){
            super(itemView);

            tv1 = itemView.findViewById(R.id.tv_rent_item_name);
            tv2 = itemView.findViewById(R.id.tv_rent_item_price);
            tv3 = itemView.findViewById(R.id.tv_rent_item_id);
            tv4 = itemView.findViewById(R.id.tv_rent_item_date);

            imageView = itemView.findViewById(R.id.imageView_rent_item);

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();

                    if(listener != null){
                        listener.onItemClick(ViewHolder.this, view, position);
                    }
                }
            });
        }

        public void setItem(Lease item){
            tv1.setText(item.getItem_name());
            tv2.setText(String.valueOf(item.getPrice()));
            tv3.setText(item.getId());
            tv4.setText(item.getDate());

            imageView.setImageResource(item.getImageRes());
        }
    }
}
