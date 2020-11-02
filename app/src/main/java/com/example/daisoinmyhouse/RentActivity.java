package com.example.daisoinmyhouse;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class RentActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    RentAdapter adapter;
    TextView tv_lease;
<<<<<<< HEAD
    TextView btn_back;
=======
>>>>>>> 9d009ef1be94ec94d31efe8b2564bb0d5d29d501

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rent_list);

        recyclerView = (RecyclerView) findViewById(R.id.rv_rent_list);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new RentAdapter();

        adapter.addItem(new Rent(R.drawable.sample1, "상품명", 10000, "아이디", "날짜"));
        adapter.addItem(new Rent(R.drawable.sample2, "상품명", 10000, "아이디", "날짜"));
        adapter.addItem(new Rent(R.drawable.sample3, "상품명", 10000, "아이디", "날짜"));
        adapter.addItem(new Rent(R.drawable.sample4, "상품명", 10000, "아이디", "날짜"));
        adapter.addItem(new Rent(R.drawable.sample5, "상품명", 10000, "아이디", "날짜"));
        adapter.addItem(new Rent(R.drawable.sample6, "상품명", 10000, "아이디", "날짜"));

        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new RentItemClickListener() {
            @Override
            public void onItemClick(RentAdapter.ViewHolder holder, View view, int position) {
                Rent item = (Rent) adapter.getItem(position);
                Toast.makeText(getApplicationContext(), "선택된 제품 : " + item.getItem_name(), Toast.LENGTH_LONG).show();
            }
        });

        tv_lease = (TextView) findViewById(R.id.tv_lease_button);
        tv_lease.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LeaseActivity.class);
                startActivity(intent);
                finish();
            }
        });
<<<<<<< HEAD

        btn_back = (TextView) findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                finish();
            }
        });
=======
>>>>>>> 9d009ef1be94ec94d31efe8b2564bb0d5d29d501
    }
}
