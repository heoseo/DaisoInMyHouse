package com.example.daisoinmyhouse;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

public class ProfileMenuActivity extends AppCompatActivity {

    LinearLayout btnEditProfile;
    LinearLayout btnPreviewProfile;
    LinearLayout btnShareProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);      // 타이틀바 없애기

        setContentView(R.layout.activity_profile_menu);

        // 수정하기
        btnEditProfile = (LinearLayout)findViewById(R.id.ll_edit_profile);
        btnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AlarmSettingActivity.class);
                startActivity(intent);
                // 고치기!!
            }
        });

        // 미리보기
        btnPreviewProfile = (LinearLayout)findViewById(R.id.ll_preview_profile);
        btnPreviewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AlarmSettingActivity.class);
                startActivity(intent);
            }
        });

        // 공유하기
        btnShareProfile = (LinearLayout)findViewById(R.id.ll_share_profile);
        btnShareProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AlarmSettingActivity.class);
                startActivity(intent);
            }
        });


    }
}
