package com.example.daisoinmyhouse;

import android.content.ContentValues;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Date;
import android.util.Log;
import android.widget.TextView;
import android.widget.EditText;

public class HomeFragment extends Fragment implements View.OnClickListener{
    //홈화면 RecyclerView 설정
    RecyclerView recyclerView;
    ItemAdapter adapter = new ItemAdapter();
    GridLayoutManager layoutManager;
    ImageButton btn_wishlist, btn_search;
    RelativeLayout btn_clothes, btn_clean, btn_kitchen, btn_digital, btn_book, btn_etc;
    LinearLayout ll_want, ll_rent;
    TextView tv_want, tv_rent;
    EditText et_search;


    private FragmentManager fragmentManager;
    private HomeRentItemFragment fragmentRent;
    private HomeWantItemFragment fragmentWant;
    private FragmentTransaction transaction;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_home, container, false);

        ll_rent = v.findViewById(R.id.ll_rent);
        ll_want = v.findViewById(R.id.ll_want);
        tv_rent = v.findViewById(R.id.tv_rent);
        tv_want = v.findViewById(R.id.tv_want);
        ll_rent.setBackgroundColor(getContext().getResources().getColor(R.color.colorPrimary));
        ll_rent.setOnClickListener(this);
        ll_want.setOnClickListener(this);
        tv_rent.setTextColor(getContext().getResources().getColor(R.color.colorAccent));

        fragmentManager = getActivity().getSupportFragmentManager();

        fragmentRent = new HomeRentItemFragment();
        fragmentWant = new HomeWantItemFragment();

        transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frameLayout, fragmentRent).commitAllowingStateLoss();



        //위시리스트 띄우기
        btn_wishlist = v.findViewById(R.id.img_btn_wishlist);
        btn_wishlist.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), WishListActivity.class);
                getContext().startActivity(intent);
            }
        });

        //검색버튼
        et_search = v.findViewById(R.id.et_search_item);
        btn_search=v.findViewById(R.id.img_btn_search);
        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String search = et_search.getText().toString();
                et_search.setText(null);
                Intent intent = new Intent(getContext(), SearchActivity.class);
                intent.putExtra("search", search);
                getContext().startActivity(intent);
            }
        });

        


        //카테고리별 품목 띄우기
        btn_clothes = (RelativeLayout) v.findViewById(R.id.img_btn_clothes);
        btn_clean = (RelativeLayout) v.findViewById(R.id.img_btn_clean);
        btn_kitchen = (RelativeLayout) v.findViewById(R.id.img_btn_kitchen);
        btn_digital = (RelativeLayout) v.findViewById(R.id.img_btn_digital);
        btn_book = (RelativeLayout) v.findViewById(R.id.img_btn_book);
        btn_etc = (RelativeLayout) v.findViewById(R.id.img_btn_etc);

        btn_clothes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), CategoryClothesItemActivity.class);
                intent.putExtra("category", "의류");
                getContext().startActivity(intent);
            }
        });

        btn_clean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), CategoryCleanItemActivity.class);
                intent.putExtra("category", "생활용품");
                getContext().startActivity(intent);
            }
        });

        btn_kitchen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), CategoryKitchenItemActivity.class);
                intent.putExtra("category", "주방용품");
                getContext().startActivity(intent);
            }
        });

        btn_digital.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), CategoryDigitalItemActivity.class);
                intent.putExtra("category", "디지털");
                getContext().startActivity(intent);
            }
        });

        btn_book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), CategoryBookItemActivity.class);
                intent.putExtra("category", "도서");
                getContext().startActivity(intent);
            }
        });

        btn_etc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), CategoryEtcItemActivity.class);
                intent.putExtra("category", "기타");
                getContext().startActivity(intent);
            }
        });


        return v;
    }

    @Override
    public void onClick(View v)
    {
        transaction = fragmentManager.beginTransaction();

        switch(v.getId())
        {
            case R.id.ll_rent:
                transaction.replace(R.id.frameLayout, fragmentRent).commitAllowingStateLoss();
                ll_rent.setBackgroundColor(getContext().getResources().getColor(R.color.colorPrimary));
                ll_want.setBackgroundColor(getContext().getResources().getColor(R.color.colorAccent));
                tv_rent.setTextColor(getContext().getResources().getColor(R.color.white));
                tv_want.setTextColor(getContext().getResources().getColor(R.color.gray));
                break;
            case R.id.ll_want:
                transaction.replace(R.id.frameLayout, fragmentWant).commitAllowingStateLoss();
                ll_rent.setBackgroundColor(getContext().getResources().getColor(R.color.colorAccent));
                ll_want.setBackgroundColor(getContext().getResources().getColor(R.color.colorPrimary));
                tv_rent.setTextColor(getContext().getResources().getColor(R.color.gray));
                tv_want.setTextColor(getContext().getResources().getColor(R.color.white));
                Log.i("homeTest", "ll_want선택됨");
                break;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }


}