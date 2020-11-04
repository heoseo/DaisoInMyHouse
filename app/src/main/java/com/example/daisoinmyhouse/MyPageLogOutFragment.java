package com.example.daisoinmyhouse;

import android.content.Intent;
import android.content.SharedPreferences;
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

import static android.app.Activity.RESULT_OK;

public class MyPageLogOutFragment extends Fragment {

    private static final int MODE_PRIVATE = 0;
    private static final int REQ_COD_LOGIN = 33;
    ImageView imgViewProfile;
    ImageButton btnSetting;
    ImageButton btnArrow;
    LinearLayout btnSettingMyArea;
    LinearLayout btnProfile;
    LinearLayout btnKeyword;
    LinearLayout btnLogin;
    Button btnLogout;
    LinearLayout btnTransaction;
    TextView tvID;
    SharedPreferences preferences;

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        preferences = this.getActivity().getSharedPreferences("account",MODE_PRIVATE);

        StaticUserInformation.nickName=preferences.getString("nickName", null);
        StaticUserInformation.porfileUrl =preferences.getString("profileUrl", null);
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
        tvID = rootView.findViewById(R.id.tv_nickname);


        StaticUserInformation.nickName=preferences.getString("nickName", null);
        StaticUserInformation.porfileUrl =preferences.getString("profileUrl", null);



//        // 로그인 & 회원가입
        btnLogin = rootView.findViewById(R.id.ll_login);
        btnLogin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(getContext(), LoginActivity.class);
                startActivityForResult(intent ,REQ_COD_LOGIN);
//                getContext().startActivity(intent);
//                fragmentTransaction.replace(R.id.frame_layout, myPageLogOutFragment).commitAllowingStateLoss();
            }
        });








        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if (resultCode != RESULT_OK) {
//            Toast.makeText(getContext(), "결과가 성공이 아님.", Toast.LENGTH_SHORT).show();
            return;
        }


        if (requestCode == REQ_COD_LOGIN) {
            String resultMsg = intent.getStringExtra("result_msg");
            if(!resultMsg.equals("1")){  // 로그인성공
                MyPageFragment myPageFragment = new MyPageFragment();

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                fragmentTransaction.replace(R.id.frame_layout, myPageFragment).commitAllowingStateLoss();
            }

//            Toast.makeText(MainActivity.this, "결과 : " + resultMsg, Toast.LENGTH_SHORT).show();
        }
    }

    private void refresh(){
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach(this).attach(this).commit();

    }
}