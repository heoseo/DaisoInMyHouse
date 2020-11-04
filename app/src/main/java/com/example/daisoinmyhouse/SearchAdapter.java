package com.example.daisoinmyhouse;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> implements  SearchItemClickListener {

    ArrayList<Search> items = new ArrayList<Search>();
    SearchItemClickListener listener;

    @NonNull
    @Override
    public SearchAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.fragment_home_item, parent, false);

        return new SearchAdapter.ViewHolder(itemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchAdapter.ViewHolder holder, int position) {
        Search search = items.get(position);
        holder.setItem(search);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItem(Search search){
        items.add(search);
    }

    public void setItems(ArrayList<Search> items){
        this.items = items;
    }

    public Search getItem(int position){
        return items.get(position);
    }

    public void setItem(int position, Search search){
        items.set(position, search);
    }

    public void setOnSearchItemClickListener(SearchItemClickListener listener){
        this.listener = listener;
    }

    @Override
    public void onItemClick(SearchAdapter.ViewHolder holder, View view, int position) {
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

        public ViewHolder(View SearchView, final SearchItemClickListener listener){
            super(SearchView);

            tv1 = SearchView.findViewById(R.id.tv_item_name);
            tv2 = SearchView.findViewById(R.id.tv_item_price);
            tv3 = SearchView.findViewById(R.id.tv_item_location);
            tv4 = SearchView.findViewById(R.id.tv_item_time);

            imageView1 = SearchView.findViewById(R.id.imageView_item);

            SearchView.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();

                    if(listener != null){
                        listener.onItemClick(SearchAdapter.ViewHolder.this, view, position);
                    }
                }
            });
        }

        public void setItem(Search search){
            tv1.setText(search.getItem_name());
            tv2.setText(String.valueOf(search.getPrice()));
            tv3.setText(search.getLocation());
            tv4.setText(search.getTime());

            imageView1.setImageResource(search.getImageRes());
        }
    }


}
