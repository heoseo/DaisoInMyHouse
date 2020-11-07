package com.example.daisoinmyhouse;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChattingAdapter extends BaseAdapter {

    ArrayList<ChattingMessageItem> chattingMessageItems;
    LayoutInflater layoutInflater;

    public ChattingAdapter(ArrayList<ChattingMessageItem> chattingMessageItems, LayoutInflater layoutInflater) {
        this.chattingMessageItems = chattingMessageItems;
        this.layoutInflater = layoutInflater;
    }

    @Override
    public int getCount() {
        return chattingMessageItems.size();
    }

    @Override
    public Object getItem(int position) {
        return chattingMessageItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        //현재 보여줄 번째의(position)의 데이터로 뷰를 생성
        ChattingMessageItem item= chattingMessageItems.get(position);

        //재활용할 뷰는 사용하지 않음!!
        View itemView=null;

        //메세지가 내 메세지인지??
        if(item.getName().equals(StaticUserInformation.nickName)){
            itemView= layoutInflater.inflate(R.layout.my_msgbox,viewGroup,false);
        }else{
            itemView= layoutInflater.inflate(R.layout.other_msgbox,viewGroup,false);
            CircleImageView iv= itemView.findViewById(R.id.iv);
            Glide.with(itemView).load(item.getPofileUrl()).into(iv);
        }

        //만들어진 itemView에 값들 설정
        TextView tvName= itemView.findViewById(R.id.tv_name);
        TextView tvMsg= itemView.findViewById(R.id.tv_msg);
        TextView tvTime= itemView.findViewById(R.id.tv_time);

        tvName.setText(item.getName());
        tvMsg.setText(item.getMessage());

        String[] split = item.getTime().split(":");
        String time12;  // 12시간단위
        int hour = Integer.parseInt(split[0]);
        int minute = Integer.parseInt(split[1]);

        if(hour == 12)
            time12 = "오후 " + hour + "시 " + minute + "분";
        else if(hour == 24)
            time12 = "오전 0시 " + minute + "분";
        if(hour > 12)
            time12 = "오후 " + (hour-12) + "시 " + minute + "분";
        else
            time12 = "오전 " + hour + "시 " + minute + "분";

        tvTime.setText(time12);


        return itemView;
    }
}
