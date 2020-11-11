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
        try {
            holder.setItem(categoryItem);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItem(CategoryItem categoryItem){
        items.add(categoryItem);
        Log.i("categoryListTest", categoryItem.getProduct_name());
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

        public void setItem(CategoryItem categoryItem) throws ParseException {
            tv1.setText(categoryItem.getProduct_name());
            tv2.setText(String.valueOf(categoryItem.getProduct_price()));
            tv3.setText(categoryItem.getLocation());
            tv4.setText(getGap(categoryItem.getTime()));

            imageView.setImageDrawable(categoryItem.getImageRes());

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
