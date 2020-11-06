package com.example.daisoinmyhouse;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class WishlistAdapter extends RecyclerView.Adapter<WishlistAdapter.ViewHolder> implements  WishlistClickListener {

    ArrayList<Wishlist> items = new ArrayList<Wishlist>();
    WishlistClickListener listener;



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.fragment_home_item, parent, false);

        return new ViewHolder(itemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Wishlist wishlist = items.get(position);
        holder.setItem(wishlist);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItem(Wishlist wishlist){
        items.add(wishlist);
    }

    public void setItems(ArrayList<Wishlist> items){
        this.items = items;
    }

    public Wishlist getItem(int position){
        return items.get(position);
    }

    public void setItem(int position, Wishlist wishlist){
        items.set(position, wishlist);
    }

    public void setOnWishlistClickListener(WishlistClickListener listener){
        this.listener = listener;
    }

    @Override
    public void onItemClick(WishlistAdapter.ViewHolder holder, View view, int position) {
        if(listener != null){
            listener.onItemClick(holder, view, position);
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvName;
        TextView tvPrice;
        TextView tvLocation;
        TextView tvTime;

        ImageView imageView1;

        public ViewHolder(View WishlistView, final WishlistClickListener listener){
            super(WishlistView);

            tvName = WishlistView.findViewById(R.id.tv_item_name);
            tvPrice = WishlistView.findViewById(R.id.tv_item_price);
            tvLocation = WishlistView.findViewById(R.id.tv_item_location);
            tvTime = WishlistView.findViewById(R.id.tv_item_time);

            imageView1 = WishlistView.findViewById(R.id.imageView_item);

            WishlistView.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();

                    if(listener != null){
                        listener.onItemClick(ViewHolder.this, view, position);
                    }
                }
            });
        }

        public void setItem(Wishlist wishlist){

            tvName.setText(wishlist.getProduct_name());
            tvPrice.setText(String.valueOf(wishlist.getProduct_price()));
            tvLocation.setText(wishlist.getLocation());
            tvTime.setText(wishlist.getTime());

            imageView1.setImageResource(wishlist.getImageRes());
        }
    }


}