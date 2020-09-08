package com.example.daisoinmyhouse;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class MyPageFragment extends Fragment {

    ImageButton btnSetting;
    LinearLayout llSettingMyArea;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_mypage, container, false);


        // 설정 버튼 -> SettingActivity 띄우기
        btnSetting = rootView.findViewById(R.id.btn_setting);
        btnSetting.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), SettingActivity.class);
                getContext().startActivity(intent);
            }
        });


        // 내 동네 설정 버튼 -> SettingMyAreaActivity 띄우기
        llSettingMyArea = rootView.findViewById(R.id.ll_setting_my_area);
        llSettingMyArea.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), SettingMyAreaActivity.class);
                getContext().startActivity(intent);
            }
        });







        return rootView;
    }
}