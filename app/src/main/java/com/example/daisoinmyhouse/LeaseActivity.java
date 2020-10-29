package com.example.daisoinmyhouse;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

public class LeaseActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    LeaseAdapter adapter;
    TextView tv_rent;
    TextView btn_back;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lease_list);

        recyclerView = (RecyclerView) findViewById(R.id.rv_lease_list);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new LeaseAdapter();

        adapter.addItem(new Lease(R.drawable.sample1, "상품명", 10000, "아이디", "날짜"));
        adapter.addItem(new Lease(R.drawable.sample2, "상품명", 10000, "아이디", "날짜"));
        adapter.addItem(new Lease(R.drawable.sample3, "상품명", 10000, "아이디", "날짜"));
        adapter.addItem(new Lease(R.drawable.sample4, "상품명", 10000, "아이디", "날짜"));
        adapter.addItem(new Lease(R.drawable.sample5, "상품명", 10000, "아이디", "날짜"));
        adapter.addItem(new Lease(R.drawable.sample6, "상품명", 10000, "아이디", "날짜"));

        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new LeaseItemClickListener(){
            public void onItemClick(LeaseAdapter.ViewHolder holder, View view, int position){
                Lease item = (Lease)adapter.getItem(position);
                Toast.makeText(getApplicationContext(), "선택된 제품 : " + item.getItem_name(), Toast.LENGTH_LONG).show();
            }
        });

        tv_rent = (TextView) findViewById(R.id.tv_rent_button);
        tv_rent.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), RentActivity.class);
                startActivity(intent);
                finish();
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
