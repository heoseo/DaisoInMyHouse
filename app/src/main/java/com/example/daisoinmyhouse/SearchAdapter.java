package com.example.daisoinmyhouse;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> implements SearchItemClickListener {

    ArrayList<Item> items = new ArrayList<Item>();
    SearchItemClickListener listener;


    @NonNull
    @Override
    public SearchAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.fragment_home_item, parent, false);

        return new SearchAdapter.ViewHolder(v, this);

    }

    @Override
    public void onBindViewHolder(@NonNull SearchAdapter.ViewHolder holder, int position) {
        Item item = items.get(position);
        holder.setItem(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItem(Item item){
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

    public void setOnSearchClickListener(SearchItemClickListener listener){
        this.listener = listener;
    }

    @Override
    public void onItemClick(SearchAdapter.ViewHolder holder, View view, int position) {
        if(listener != null){
            listener.onItemClick(holder, view, position);
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvName;
        TextView tvPrice;
        TextView tvLocation;
        TextView tvTime;

        ImageView imageView;

        public ViewHolder(View SearchView, final SearchItemClickListener listener){
            super(SearchView);

            tvName = SearchView.findViewById(R.id.tv_item_name);
            tvPrice = SearchView.findViewById(R.id.tv_item_price);
            tvLocation = SearchView.findViewById(R.id.tv_item_location);
            tvTime = SearchView.findViewById(R.id.tv_item_time);

            imageView = SearchView.findViewById(R.id.imageView_item);

            SearchView.setOnClickListener(new View.OnClickListener() {
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
            tvName.setText(item.getProduct_name());
            tvPrice.setText(String.valueOf(item.getProduct_price()));
            tvLocation.setText(item.getLocation());
            imageView.setImageDrawable(item.getImageRes());

            imageView.setImageDrawable(item.getImageRes());
        }

        public String getGap(String productTime) throws ParseException {

            String strGap="";

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            Date productTimeDate = dateFormat.parse(productTime);
            long productTimeLong = productTimeDate.getTime();

            Date curTime = new Date();
            curTime = dateFormat.parse(dateFormat.format(curTime));
            long curTimeLong = curTime.getTime();


            //분으로 표현
            long gap = (curTimeLong - productTimeLong) / 60000;
            strGap = gap+"분";
            if(gap > 60)// 60분 넘으면
            {
                gap = gap / 60;	// 시간으로
                strGap = gap+"시간";
                if(gap > 24)	// 24시간 넘으면
                {
                    long calDate = curTime.getTime() - productTimeDate.getTime();

                    // Date.getTime() 은 해당날짜를 기준으로1970년 00:00:00 부터 몇 초가 흘렀는지를 반환해준다.
                    // 이제 24*60*60*1000(각 시간값에 따른 차이점) 을 나눠주면 일수가 나온다.
                    long calDateDays = calDate / ( 24*60*60*1000);

                    gap = Math.abs(calDateDays);	// 일로.
                    strGap = gap+"일";
                }


            }
            Log.i("timeTest", strGap);

            return strGap;
        }
    }
}