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

public class ItemWantAdapter extends RecyclerView.Adapter<ItemWantAdapter.ViewHolder> implements OnProductItemClickListener {
    ArrayList<ItemWant> items = new ArrayList<ItemWant>();
    OnProductItemClickListener listener;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View itemView = inflater.inflate(R.layout.item_want, viewGroup, false);

        return new ViewHolder(itemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        ItemWant itemWant = items.get(position);
        try {
            viewHolder.setItem(itemWant);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItem(ItemWant itemWant) {
        items.add(itemWant);
    }

    public void setItems(ArrayList<ItemWant> items){
        this.items = items;
    }

    public ItemWant getItem(int position){
        return items.get(position);
    }

    public void setItem(int position, ItemWant itemWant){
        items.set(position, itemWant);
    }

    public void setOnItemClickListener(OnProductItemClickListener listener){
        this.listener = listener;
    }

    @Override
    public void onItemClick(ItemAdapter.ViewHolder holder, View view, int position) { }

    @Override
    public void onItemWantClick(ItemWantAdapter.ViewHolder holder, View view, int position) {
        if(listener != null){
            listener.onItemWantClick(holder, view, position);
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvName;
        TextView tvLocation;
        TextView tvTime;

        ImageView imageView1;


        public ViewHolder(View itemView, final OnProductItemClickListener listener){
            super(itemView);

            tvName = itemView.findViewById(R.id.tv_item_name);
            tvLocation = itemView.findViewById(R.id.tv_item_location);
            tvTime = itemView.findViewById(R.id.tv_item_time);

            imageView1 = itemView.findViewById(R.id.imageView_item);


            itemView.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();

                    if(listener != null){
                        listener.onItemWantClick(ViewHolder.this, view, position);
                    }
                }
            });
        }

        public void setItem(ItemWant itemWant) throws ParseException {
            tvName.setText(itemWant.getWant_name());
            tvLocation.setText(itemWant.getLocation());
            tvTime.setText(getGap(itemWant.getTime()));

            switch(itemWant.getCategory()){
                case "의류":
                    imageView1.setImageResource(R.drawable.img_want_clothes);
                    break;
                case "생활용품":
                    imageView1.setImageResource(R.drawable.img_want_clean);
                    break;
                case "주방용품":
                    imageView1.setImageResource(R.drawable.img_want_kitchen);
                    break;
                case "디지털":
                    imageView1.setImageResource(R.drawable.img_want_digital);
                    break;
                case "도서":
                    imageView1.setImageResource(R.drawable.img_want_book);
                    break;
                case "기타":
                    imageView1.setImageResource(R.drawable.img_want_etc);
                    break;

            }

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
