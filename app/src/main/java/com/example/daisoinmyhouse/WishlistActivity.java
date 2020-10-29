package com.example.daisoinmyhouse;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class WishlistActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    WishlistAdapter adapter = new WishlistAdapter();
    GridLayoutManager layoutManager;

    TextView btn_back;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wishlist);

        recyclerView = (RecyclerView) findViewById(R.id.rv_wishlist);

        layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);

        adapter.addItem(new Wishlist("상품명1", "두정동", "3시간", 10000, R.drawable.sample1, "1"));
        adapter.addItem(new Wishlist("상품명2", "천안시", "7시간", 20000, R.drawable.sample2,"2"));
        adapter.addItem(new Wishlist("상품명3", "서울특별시", "1일", 50000, R.drawable.sample3, "3"));
        adapter.addItem(new Wishlist("상품명4", "대전광역시", "7일", 30000, R.drawable.sample4, "4"));
        adapter.addItem(new Wishlist("상품명5", "부산광역시", "1달", 100000, R.drawable.sample5 , "5"));
        adapter.addItem(new Wishlist("상품명6", "인천광역시", "3일", 70000, R.drawable.sample6, "6"));

        recyclerView.setAdapter(adapter);

        adapter.setOnWishlistClickListener(new WishlistClickListener() {
            @Override
            public void onItemClick(WishlistAdapter.ViewHolder holder, View view, int position) {
                Wishlist item = (Wishlist) adapter.getItem(position);
                Toast.makeText(getApplicationContext(), "선택된 제품 : " + item.getItem_name(), Toast.LENGTH_LONG).show();

                //(ItemInformationActivity에 상품 ID 전달)
                Intent intent = new Intent(getApplicationContext(), ItemInformationActivity.class);
                intent.putExtra("productID", item.getProductID());
                startActivity(intent);
            }
        });

        btn_back = (TextView) findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}