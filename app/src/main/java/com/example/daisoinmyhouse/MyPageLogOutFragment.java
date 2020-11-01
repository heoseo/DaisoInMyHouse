package com.example.daisoinmyhouse;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.squareup.picasso.Picasso;

public class MyPageLogOutFragment extends Fragment {

    private static final int MODE_PRIVATE = 0;
    ImageView imgViewProfile;
    ImageButton btnSetting;
    ImageButton btnArrow;
    LinearLayout btnSettingMyArea;
    LinearLayout btnProfile;
    LinearLayout btnKeyword;
    Button btnLogin;
    Button btnLogout;
    LinearLayout btnTransaction;
    TextView tvID;
    LinearLayout btnWishlist;
    SharedPreferences preferences;

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        preferences = this.getActivity().getSharedPreferences("account",MODE_PRIVATE);

        StaticUserInformation.nickName=preferences.getString("nickName", null);
        StaticUserInformation.porfileUrl=preferences.getString("profileUrl", null);
        final ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_mypage_logout, container, false);

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

        imgViewProfile = rootView.findViewById(R.id.imageview_profile);
        tvID = rootView.findViewById(R.id.tv_id);


        StaticUserInformation.nickName=preferences.getString("nickName", null);
        StaticUserInformation.porfileUrl=preferences.getString("profileUrl", null);



//        // 로그인 & 회원가입
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

    private void refresh(){
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach(this).attach(this).commit();

    }
}