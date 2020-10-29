package com.example.daisoinmyhouse;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class HomeFragment extends Fragment {
    //홈화면 RecyclerView 설정
    RecyclerView recyclerView;
    ItemAdapter adapter = new ItemAdapter();
    GridLayoutManager layoutManager;
    ImageButton btn_wishlist;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_home, container, false);

        //홈화면 RecyclerView 설정
        recyclerView = (RecyclerView) v.findViewById(R.id.rv_product);
        layoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setAdapter(adapter);

        //위시리스트 띄우기
        btn_wishlist = v.findViewById(R.id.img_btn_heart);
        btn_wishlist.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), WishlistActivity.class);
                getContext().startActivity(intent);
            }
        });


        return v;
    }

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        prepareData();

        adapter.setOnItemClickListener(new OnProductItemClickListener() {
            @Override
            public void onItemClick(ItemAdapter.ViewHolder holder, View view, int position) {
                Item item = (Item) adapter.getItem(position);
                Toast.makeText(getContext(), "선택된 제품 : " + item.getName(), Toast.LENGTH_LONG).show();

                // 1028 코드추가 (ItemInformationActivyty에 상품ID전달)
                Intent intent = new Intent(getContext(), ItemInformationActivity.class);
                intent.putExtra("productID", item.getProductID());
                getContext().startActivity(intent);
            }
        });
    }

    public void prepareData(){
        adapter.addItem(new Item("상품명1", "두정동", "3시간", 10000, R.drawable.sample1, "1"));
        adapter.addItem(new Item("상품명2", "천안시", "7시간", 20000, R.drawable.sample2,"2"));
        adapter.addItem(new Item("상품명3", "서울특별시", "1일", 50000, R.drawable.sample3, "3"));
        adapter.addItem(new Item("상품명4", "대전광역시", "7일", 30000, R.drawable.sample4, "4"));
        adapter.addItem(new Item("상품명5", "부산광역시", "1달", 100000, R.drawable.sample5 , "5"));
        adapter.addItem(new Item("상품명6", "인천광역시", "3일", 70000, R.drawable.sample6, "6"));


    }





}