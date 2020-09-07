package com.example.daisoinmyhouse;


import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    // FrameLayout에 각 메뉴의 Fragment를 바꿔 줌
    private FragmentManager fragmentManager = getSupportFragmentManager();
    // 4개의 메뉴에 들어갈 Fragment들
    private HomeFragment homeFragment = new HomeFragment();
    private WriteNewItemFragment writeNewItemFragment = new WriteNewItemFragment();
    private ChattingFragment chattingFragment = new ChattingFragment();
    private MyPageFragment myPageFragment = new MyPageFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
                        transaction.replace(R.id.frame_layout, writeNewItemFragment).commitAllowingStateLoss();
                        break;
                    }
                    case R.id.chattingItem: {
                        transaction.replace(R.id.frame_layout, chattingFragment).commitAllowingStateLoss();
                        break;
                    }
                    case R.id.mypageItem: {
                        transaction.replace(R.id.frame_layout, myPageFragment).commitAllowingStateLoss();
                        break;
                    }
                }

                return true;
            }
        });
    }
}