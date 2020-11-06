package com.example.daisoinmyhouse;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Locale;

public class CategoryItemAdapter extends RecyclerView.Adapter<CategoryItemAdapter.ViewHolder> implements CategoryItemClickListener{

    ArrayList<CategoryItem> items = new ArrayList<CategoryItem>();
    CategoryItemClickListener listener;


    @NonNull
    @Override
    public CategoryItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.fragment_home_item, parent, false);

        return new CategoryItemAdapter.ViewHolder(itemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryItemAdapter.ViewHolder holder, int position) {
        CategoryItem categoryItem = items.get(position);
        holder.setItem(categoryItem);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItem(CategoryItem categoryItem){
        items.add(categoryItem);
    }

    public void setItems(ArrayList<CategoryItem> items){
        this.items = items;
    }

    public CategoryItem getItem(int position){
        return items.get(position);
    }

    public void setItem(int position, CategoryItem categoryItem){
        items.set(position, categoryItem);
    }

    public void setOnCategoryItemClickListener(CategoryItemClickListener listener){
        this.listener = listener;
    }

    @Override
    public void onItemClick(CategoryItemAdapter.ViewHolder holder, View view, int position) {
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

        public ViewHolder(View CategoryItemView, final CategoryItemClickListener listener){
            super(CategoryItemView);

            tv1 = CategoryItemView.findViewById(R.id.tv_item_name);
            tv2 = CategoryItemView.findViewById(R.id.tv_item_price);
            tv3 = CategoryItemView.findViewById(R.id.tv_item_location);
            tv4 = CategoryItemView.findViewById(R.id.tv_item_time);

            imageView = CategoryItemView.findViewById(R.id.imageView_item);

            CategoryItemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();

                    if(listener != null){
                        listener.onItemClick(CategoryItemAdapter.ViewHolder.this, view, position);
                    }
                }
            });
        }

        public void setItem(CategoryItem categoryItem){
            tv1.setText(categoryItem.getItem_name());
            tv2.setText(String.valueOf(categoryItem.getPrice()));
            tv3.setText(categoryItem.getLocation());
            tv4.setText(categoryItem.getTime());

            imageView.setImageResource(categoryItem.getImageRes());
        }
    }
}