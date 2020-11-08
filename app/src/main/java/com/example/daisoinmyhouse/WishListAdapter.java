package com.example.daisoinmyhouse;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class WishListAdapter extends RecyclerView.Adapter<WishListAdapter.ViewHolder> implements WishListClickListener {

    ArrayList<WishList> items = new ArrayList<WishList>();
    WishListClickListener listener;



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.fragment_home_item, parent, false);

        return new ViewHolder(itemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        WishList wishlist = items.get(position);
        try {
            holder.setItem(wishlist);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItem(WishList wishlist){
        items.add(wishlist);
    }

    public void setItems(ArrayList<WishList> items){
        this.items = items;
    }

    public WishList getItem(int position){
        return items.get(position);
    }

    public void setItem(int position, WishList wishlist){
        items.set(position, wishlist);
    }

    public void setOnWishlistClickListener(WishListClickListener listener){
        this.listener = listener;
    }

    @Override
    public void onItemClick(WishListAdapter.ViewHolder holder, View view, int position) {
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

        public ViewHolder(View WishlistView, final WishListClickListener listener){
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

        public void setItem(WishList wishlist) throws ParseException {

            tvName.setText(wishlist.getProduct_name());
            tvPrice.setText(String.valueOf(wishlist.getProduct_price()));
            tvLocation.setText(wishlist.getLocation());
            tvTime.setText(getGap(wishlist.getTime()));

            imageView1.setImageResource(wishlist.getImageRes());
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