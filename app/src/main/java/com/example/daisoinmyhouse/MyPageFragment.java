package com.example.daisoinmyhouse;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class MyPageFragment extends Fragment {

    ImageButton btnSetting;
    ImageButton btnArrow;
    LinearLayout btnSettingMyArea;
    LinearLayout btnProfile;
    LinearLayout btnKeyword;
    Button btnLogin;
    Button btnJoin;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_mypage, container, false);


        // [ fragment 에서 버튼 누르면 새 activity 띄우기 ]

        // 설정 버튼 -> SettingActivity 띄우기
        btnSetting = rootView.findViewById(R.id.btn_setting);
        btnSetting.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), SettingActivity.class);
                getContext().startActivity(intent);
            }
        });



        // 프로필 레이아웃 누르면 -> ProfileMenuActivity 띄우기
        btnProfile = rootView.findViewById(R.id.ll_profile);
        btnProfile.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ProfileMenuActivity.class);
                getContext().startActivity(intent);
            }
        });
        btnArrow = rootView.findViewById(R.id.btn_profile_setting);
        btnArrow.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ProfileMenuActivity.class);
                getContext().startActivity(intent);
            }
        });

        // 키워드 알림 레이아웃 누르면 -> KeywordAlarmActivity 띄우기
        btnKeyword = rootView.findViewById(R.id.ll_setting_keyword_notice);
        btnKeyword.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(getContext(), KeywordAlarmActivity.class);
                getContext().startActivity(intent);
            }
        });

        // 로그인 & 회원가입
        btnLogin = rootView.findViewById(R.id.fragment_mypage_login_btn);
        btnLogin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(getContext(), LoginActivity.class);
                getContext().startActivity(intent);
            }
        });



        return rootView;
    }
}