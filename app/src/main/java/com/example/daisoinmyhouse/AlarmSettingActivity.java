package com.example.daisoinmyhouse;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class AlarmSettingActivity extends AppCompatActivity {

    Switch sbAlarm, sbChatAlarm, sbReplyitem, sbWish, sbReplyStore, sbDelivery, sbDeliveryComplete;
    LinearLayout btnKeywordAlarm;
    TextView btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_setting);

        sbAlarm = (Switch)findViewById(R.id.sb_alarm);
        sbChatAlarm = (Switch)findViewById(R.id.sb_chat_alarm);
        sbReplyitem = (Switch)findViewById(R.id.sb_reply_item);
        sbWish = (Switch)findViewById(R.id.sb_wish);
        sbReplyStore = (Switch)findViewById(R.id.sb_reply_store);
        sbDelivery = (Switch)findViewById(R.id.sb_delivery);
        sbDeliveryComplete = (Switch)findViewById(R.id.sb_delivery_complete);

        sbAlarm.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    sbChatAlarm.setEnabled(true);
                    sbReplyitem.setEnabled(true);
                    sbWish.setEnabled(true);
                    sbReplyStore.setEnabled(true);
                    sbDelivery.setEnabled(true);
                    sbDeliveryComplete.setEnabled(true);
                }
                else{
                    sbChatAlarm.setEnabled(false);
                    sbReplyitem.setEnabled(false);
                    sbWish.setEnabled(false);
                    sbReplyStore.setEnabled(false);
                    sbDelivery.setEnabled(false);
                    sbDeliveryComplete.setEnabled(false);
                }
            }
        });


    // 뒤로가기 버튼
        btnBack = (TextView) findViewById(R.id.btn_back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
}