package com.example.daisoinmyhouse;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyPageFragment extends Fragment {

    private static final int MODE_PRIVATE = 0;
    CircleImageView imgViewProfile;
    ImageButton btnSetting;
    ImageButton btnArrow;
    LinearLayout btnSettingMyArea;
    LinearLayout btnProfile;
    RelativeLayout btnKeyword;
    Button btnLogin;
    Button btnLogout;
    RelativeLayout btnTransaction;
    TextView tvNickName, tvWishlist;
    RelativeLayout btnWishlist;
    SharedPreferences preferences;

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {

//        ViewGroup rootView = null;

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
        tvNickName = rootView.findViewById(R.id.tv_nickname);

        SharedPreferences preferences=getActivity().getSharedPreferences("account",MODE_PRIVATE);
        StaticUserInformation.loadData(preferences);

        if(StaticUserInformation.nickName != null){
            tvNickName.setText(StaticUserInformation.nickName);
            Picasso.get().load( StaticUserInformation.porfileUrl).into(imgViewProfile);
        }

        tvWishlist = rootView.findViewById(R.id.tv_wishlist);
        StaticUserInformation.cntWishList = Integer.parseInt(preferences.getString("cntWishList", null));
        tvWishlist.setText(Integer.toString(StaticUserInformation.cntWishList));


        // 로그아웃, 수정하기, 공유하기 팝업띄우기
        RelativeLayout btnPopUp = rootView.findViewById(R.id.ll_profile);
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

                        switch (str){
                            case "수정하기":
                                Intent intent = new Intent(getContext(), ProfileEditActivity.class);
                                getContext().startActivity(intent);
                                break;
                            case "공유하기":
                                break;
                            case "로그아웃":

                                MyPageLogOutFragment myPageLogOutFragment = new MyPageLogOutFragment();

                                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

//                                fragmentTransaction.remove(MyPageFragment.this).commit();
                                fragmentTransaction.replace(R.id.frame_layout, myPageLogOutFragment).commitAllowingStateLoss();


                                SharedPreferences preferences= getActivity().getSharedPreferences("account",MODE_PRIVATE);
                                SharedPreferences.Editor editor=preferences.edit();
                                editor.putString("nickName", null);
                                editor.putString("profileUrl", null);
                                editor.apply();
                                editor.commit();
                                StaticUserInformation.nickName=preferences.getString("nickName", null);
                                StaticUserInformation.porfileUrl =preferences.getString("profileUrl", null);

                                break;

                        }
                    }
                });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });


        // 키워드 알림 레이아웃 누르면 -> KeywordAlarmActivity 띄우기
        btnKeyword = rootView.findViewById(R.id.ll_keword_notice);
        btnKeyword.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(getContext(), KeywordAlarmActivity.class);
                getContext().startActivity(intent);
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
        btnWishlist = rootView.findViewById((R.id.ll_wishlist));
        btnWishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), WishListActivity.class);
                getContext().startActivity(intent);
            }
        });

        return rootView;
    }

}