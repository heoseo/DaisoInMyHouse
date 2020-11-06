package com.example.daisoinmyhouse;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.sql.Date;


public class WishlistActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    WishlistAdapter adapter = new WishlistAdapter();
    GridLayoutManager layoutManager;
    public static Context CONTEXT;

    TextView btn_back;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wishlist);

        recyclerView = (RecyclerView) findViewById(R.id.rv_wishlist);


        SharedPreferences preferences=getSharedPreferences("account",MODE_PRIVATE);
        StaticUserInformation.userID = preferences.getString("userID", null);

        String user_id = StaticUserInformation.userID;

        WishlistList wishlist = new WishlistList();
        wishlist.execute(user_id);

        layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);
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

        CONTEXT = this;
        ((WishlistActivity)WishlistActivity.CONTEXT).onResume();

    }

    public class WishlistList extends AsyncTask<String, Void, String> {
        String sendMsg, receiveMsg;

        @Override
        protected String doInBackground(String... strings) {
            try {
                String str;
                // 접속할 서버 주소 (이클립스에서 android.jsp 실행시 웹브라우저 주소)
                URL url = new URL("http://daisoinmyhouse.cafe24.com/wishlistList.jsp");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestMethod("POST");
                OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
                // 전송할 데이터. GET 방식으로 작성
                sendMsg = "user_id=" + strings[0];
                osw.write(sendMsg);
                osw.flush();
                //jsp와 통신 성공 시 수행
                if (conn.getResponseCode() == conn.HTTP_OK) {
                    InputStreamReader tmp = new InputStreamReader(conn.getInputStream(), "UTF-8");
                    BufferedReader reader = new BufferedReader(tmp);
                    StringBuffer buffer = new StringBuffer();

                    String page = "";
                    // jsp에서 보낸 값을 받는 부분
                    while ((str = reader.readLine()) != null) {
                        buffer.append(str);
                        page += str;
                    }
                    // JSP에서 보낸 JSON 받아오자  JSONObject = siteDataMain
                    JSONObject json = new JSONObject(page);
                    JSONArray jArr = json.getJSONArray("WISHLIST_LIST");

                    // JSON이 가진 크기만큼 데이터를 받아옴
                    for (int i = 0; i < jArr.length(); i++) {
                        json = jArr.getJSONObject(i);

                        long now = System.currentTimeMillis();
                        Date date = new Date(now);

                        adapter.addItem(new Wishlist(json.getString("product_name"),
                                "두정동",
                                "3시간",
                                json.getInt("product_price"),
                                R.drawable.sample1,
                                json.getInt("product_no")));
//                        adapter.addItem(new Wishlist("test", "두정동", "3시간", 30000,  R.drawable.sample1, 3));

//                            adapter.addItem(new Item(json.getString("name"), json.getString("address"),
//                                    date, json.getInt("price"), R.drawable.sample1, json.getInt("num")));
//                            adapter.addItem(new Item(json.getString("name"), "두정동", now, json.getInt("price"), R.drawable.sample1, 1));
                        receiveMsg = buffer.toString();
                        Log.i("테스트", json.getString("product_name"));
                        Log.i("테스트", "두정동");
                        Log.i("테스트", "3시간");
                        Log.i("테스트", String.valueOf(json.getInt("product_price")));
                        Log.i("테스트", String.valueOf(json.getInt("product_no")));
                        Log.i("테스트", receiveMsg);
                        Log.i("테스트2",json.getString("product_name") + "두정동" +"3시간" +json.getInt("product_price") +json.getInt("num")) ;
                    }
                } else{
                    //통신실패
                }
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return receiveMsg;
        }
    }

    @Override public void onResume() {
        super.onResume();
    }

}