package com.example.daisoinmyhouse;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;

public class MyPageFragment extends Fragment {

    ImageButton btnSetting;
    ImageButton btnArrow;
    LinearLayout btnSettingMyArea;
    LinearLayout btnProfile;
    LinearLayout btnKeyword;
    Button btnLogin;
    Button btnLogout;
    LinearLayout btnTransaction;

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



        // 수정하기, 공유하기 팝업띄우기
        ImageButton btnPopUp = rootView.findViewById(R.id.btn_profile_popup);
        btnPopUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                builder.setTitle("리스트 추가 예제");

                builder.setItems(R.array.menu_profile_popup, new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int pos)
                    {
                        String[] items = getResources().getStringArray(R.array.menu_profile_popup);
                        String str = items[pos];
                        Toast.makeText(getActivity().getApplicationContext(),str,Toast.LENGTH_LONG).show();

                        switch (str){
                            case "수정하기":
                                Intent intent = new Intent(getContext(), ProfileEditActivity.class);
                                getContext().startActivity(intent);
                                break;
                            case "공유하기":
                                break;
                        }
                    }
                });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();
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

        btnLogout = rootView.findViewById(R.id.fragment_mypage_logout_btn);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserManagement.getInstance()
                        .requestLogout(new LogoutResponseCallback() {
                            @Override
                            public void onCompleteLogout() {
                                Log.d("KAKAO_API", "로그아웃 되었습니다.");
                            }
                        });
            }
        });

        // 거래내역
        btnTransaction = rootView.findViewById(R.id.ll_transaction_details);
        btnTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), RentActivity.class);
                getContext().startActivity(intent);
            }
        });

        return rootView;
    }
}