package com.example.daisoinmyhouse;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;
import com.squareup.picasso.Picasso;

public class MyPageFragment extends Fragment {

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
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_mypage, container, false);

        preferences = this.getActivity().getSharedPreferences("account",MODE_PRIVATE);

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

       if (StaticUserInformation.nickName != null) {
            tvID.setText(StaticUserInformation.nickName);
            Picasso.get().load(StaticUserInformation.porfileUrl).into(imgViewProfile);

        }
       else if(StaticUserInformation.nickName == null){
           tvID.setText("로그인해주세요");

           Resources res = getResources();
           Drawable drawable = res.getDrawable(R.drawable.ic_baseline_person_24);
           Picasso.get().load(String.valueOf(drawable)).into(imgViewProfile);
       }


        // 로그아웃, 수정하기, 공유하기 팝업띄우기
        ImageButton btnPopUp = rootView.findViewById(R.id.btn_profile_popup);
        btnPopUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                builder.setTitle("프로필");

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

        // 로그아웃
        btnLogout = rootView.findViewById(R.id.fragment_mypage_logout_btn);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor=preferences.edit();
                editor.putString("nickName", null);
                editor.putString("profileUrl", null);
                editor.apply();
                StaticUserInformation.nickName=preferences.getString("nickName", null);
                StaticUserInformation.porfileUrl=preferences.getString("profileUrl", null);

                refresh();

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



        //찜한 목록 띄우기
        btnWishlist = rootView.findViewById((R.id.ll_heart));
        btnWishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), WishlistActivity.class);
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