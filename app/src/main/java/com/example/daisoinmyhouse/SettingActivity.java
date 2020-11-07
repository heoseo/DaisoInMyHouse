package com.example.daisoinmyhouse;

import android.app.AppComponentFactory;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Text;

public class SettingActivity extends AppCompatActivity {

    LinearLayout btnAlarmSetting;
    LinearLayout btnAreaSetting;
    ImageButton btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        btnAlarmSetting = (LinearLayout)findViewById(R.id.btn_alarm_setting);
        btnAlarmSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AlarmSettingActivity.class);
                startActivity(intent);
            }
        });
        btnAreaSetting = (LinearLayout)findViewById(R.id.btn_region_setting);
        btnAreaSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SettingMyAreaActivity.class);
                startActivity(intent);
            }
        });

        btnBack = (ImageButton) findViewById(R.id.btn_back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }
}
