package com.example.daisoinmyhouse;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class ProfileEditActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);

        SharedPreferences preferences = getSharedPreferences("account",MODE_PRIVATE);

        // [ fragment 에서 버튼 누르면 새 activity 띄우기 ]


        ImageView imgViewProfile = (ImageView) findViewById(R.id.imageview_profile);
        TextView tvID = (TextView) findViewById(R.id.tv_id);


        StaticUserInformation.nickName=preferences.getString("nickName", null);
        StaticUserInformation.porfileUrl=preferences.getString("profileUrl", null);

        if(StaticUserInformation.nickName != null){
            tvID.setText(StaticUserInformation.nickName);
            Picasso.get().load(StaticUserInformation.porfileUrl).into(imgViewProfile);
        }






        // 뒤로가기
        ImageButton btnBack = (ImageButton) findViewById(R.id.btn_back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }


}