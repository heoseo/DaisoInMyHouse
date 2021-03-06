package com.example.daisoinmyhouse;


import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MainActivity extends AppCompatActivity {

    // FrameLayout에 각 메뉴의 Fragment를 바꿔 줌
    private FragmentManager fragmentManager = getSupportFragmentManager();
    // 4개의 메뉴에 들어갈 Fragment들
    private HomeFragment homeFragment = new HomeFragment();
    private WriteNewItemFragment writeNewItemFragment = new WriteNewItemFragment();
    public WriteWantItemFragment writeWantItemFragment = new WriteWantItemFragment();
    private ChattingFragment chattingFragment = new ChattingFragment();
    private MyPageFragment myPageFragment = new MyPageFragment();
    private MyPageLogOutFragment myPageLogOutFragment = new MyPageLogOutFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        requestWindowFeature(Window.FEATURE_NO_TITLE);      // 타이틀바 없애기
        setContentView(R.layout.activity_main);
        getHashKey();

        Intent intent = new Intent(this, LoadingActivity.class);
        startActivity(intent);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_view);
        // 첫 화면 지정
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frame_layout, homeFragment).commitAllowingStateLoss();

        // bottomNavigationView의 아이템이 선택될 때 호출될 리스너 등록
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                switch (item.getItemId()) {
                    case R.id.homeItem: {
                        transaction.replace(R.id.frame_layout, homeFragment).commitAllowingStateLoss();
                        break;
                    }
                    case R.id.writeNewItem: {
                        SharedPreferences preferences = getSharedPreferences("account",MODE_PRIVATE);
                        StaticUserInformation.nickName=preferences.getString("nickName", null);
                        StaticUserInformation.porfileUrl =preferences.getString("profileUrl", null);

                        if (StaticUserInformation.nickName != null) {
                            transaction.replace(R.id.frame_layout, writeNewItemFragment).commitAllowingStateLoss();
                        }
                        else if(StaticUserInformation.nickName == null){
                            transaction.replace(R.id.frame_layout, myPageLogOutFragment).commitAllowingStateLoss();
                        }
                        break;
                    }
                    case R.id.chattingItem: {
                        SharedPreferences preferences = getSharedPreferences("account",MODE_PRIVATE);
                        StaticUserInformation.nickName=preferences.getString("nickName", null);
                        StaticUserInformation.porfileUrl =preferences.getString("profileUrl", null);

                        if (StaticUserInformation.nickName != null) {
                            transaction.replace(R.id.frame_layout, chattingFragment).commitAllowingStateLoss();
                        }
                        else if(StaticUserInformation.nickName == null){
                            transaction.replace(R.id.frame_layout, myPageLogOutFragment).commitAllowingStateLoss();
                        }
                        break;
                    }
                    case R.id.mypageItem: {
                        SharedPreferences preferences = getSharedPreferences("account",MODE_PRIVATE);
                        StaticUserInformation.nickName=preferences.getString("nickName", null);



                        if (StaticUserInformation.nickName != null) {
                            transaction.replace(R.id.frame_layout, myPageFragment).commitAllowingStateLoss();
                        }
                        else if(StaticUserInformation.nickName == null){
                            transaction.replace(R.id.frame_layout, myPageLogOutFragment).commitAllowingStateLoss();
                        }
                        break;
                    }
                }

                return true;
            }
        });
    }

    public void onFragmentChange(int fragmentNum) {
        //프래그먼트의 번호에 따라 다르게 작동하는 조건문
        if(fragmentNum == 1) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, homeFragment).commit();
        } else if(fragmentNum == 2) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, writeNewItemFragment).commit();
        }else if(fragmentNum == 3) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, chattingFragment).commit();
        }else if(fragmentNum == 4) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, myPageFragment).commit();
        }
    }

    public void replaceFragment1(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, writeWantItemFragment).commit();
    }

    public void replaceFragment2(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, writeNewItemFragment).commit();
    }

    private void getHashKey(){
        PackageInfo packageInfo = null;
        try {
            packageInfo = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (packageInfo == null)
            Log.e("KeyHash", "KeyHash:null");

        for (Signature signature : packageInfo.signatures) {
            try {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            } catch (NoSuchAlgorithmException e) {
                Log.e("KeyHash", "Unable to get MessageDigest. signature=" + signature, e);
            }
        }
    }
}