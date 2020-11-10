package com.example.daisoinmyhouse;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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
        try {
            viewHolder.setItem(item);
        } catch (ParseException e) {
            e.printStackTrace();
        }
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

    @Override
    public void onItemWantClick(ItemWantAdapter.ViewHolder holder, View view, int position) { }

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

        public void setItem(Item item) throws ParseException {
            tv1.setText(item.getProduct_name());
            tv2.setText(String.valueOf(item.getProduct_price()));
            tv3.setText(item.getLocation());

            tv4.setText(getGap(item.getTime()));

            imageView1.setImageDrawable(item.getImageRes());
        }

        public String getGap(String productTime) throws ParseException{

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
