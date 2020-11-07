package com.example.daisoinmyhouse;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> implements Filterable {

    Context context;
    ArrayList<Item> unFilteredlist;
    ArrayList<Item> filteredList;
    OnProductItemClickListener listener;

    public RecyclerViewAdapter(Context context, ArrayList<Item> list) {
        super();
        this.context = context;
        this.unFilteredlist = list;
        this.filteredList = list;
    }

    public RecyclerViewAdapter() {

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.from(context).inflate(R.layout.fragment_home_item, parent, false);
        return new MyViewHolder(view, this);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Item item = filteredList.get(position);
        holder.setItem(item);
        //holder.textView.setText(filteredList.get(position));
    }

    @Override
    public int getItemCount() {
        return filteredList.size();
    }

    public void addItem(Item item) { filteredList.add(item);}
    public void setItems(ArrayList<Item> filteredList){ this.filteredList = filteredList;}

    public void setOnItemClickListener(OnProductItemClickListener listener){
        this.listener = listener;
    }
    public Item getItem(int position){
        return filteredList.get(position);
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv1;
        TextView tv2;
        TextView tv3;
        TextView tv4;

        ImageView imageView1;

        public MyViewHolder(View itemView, RecyclerViewAdapter recyclerViewAdapter) {
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
//                        listener.onItemClick(MyViewHolder.this, view, position);
                    }
                }
            });
        }

        public void setItem(Item item) {
        }
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String charString = constraint.toString();
                if(charString.isEmpty()) {
                    filteredList = unFilteredlist;
                } else {
                    ArrayList<Item> filteringList = new ArrayList<>();
                    for(Item name : unFilteredlist) {
                        //if(name.toLowerCase().contains(charString.toLowerCase())) {
                        //    filteringList.add(name);
                        //}
                    }
                    filteredList = filteringList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filteredList = (ArrayList<Item>)results.values;
                notifyDataSetChanged();
            }
        };
    }
    
}