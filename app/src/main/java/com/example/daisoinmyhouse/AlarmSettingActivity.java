package com.example.daisoinmyhouse;

import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;

public class AlarmSettingActivity extends AppCompatActivity {

    Switch sbAlarm, sbChatAlarm, sbReplyitem, sbWish, sbReplyStore, sbDelivery, sbDeliveryComplete;

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
                    sbChatAlarm.setChecked(true);
                    sbReplyitem.setChecked(true);
                    sbWish.setChecked(true);
                    sbReplyStore.setChecked(true);
                    sbDelivery.setChecked(true);
                    sbDeliveryComplete.setChecked(true);
                }
                else{
                    sbChatAlarm.setChecked(false);
                    sbReplyitem.setChecked(false);
                    sbWish.setChecked(false);
                    sbReplyStore.setChecked(false);
                    sbDelivery.setChecked(false);
                    sbDeliveryComplete.setChecked(false);
                }
            }
        });
    }
}
