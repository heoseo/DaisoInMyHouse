package com.example.daisoinmyhouse;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;


public class WishListActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    WishListAdapter adapter = new WishListAdapter();
    GridLayoutManager layoutManager;
    public static Context CONTEXT;

    ImageButton btn_back;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wishlist);

        recyclerView = (RecyclerView) findViewById(R.id.rv_wishlist);


        SharedPreferences preferences=getSharedPreferences("account",MODE_PRIVATE);
        StaticUserInformation.userID = preferences.getString("userID", null);

        String user_id = StaticUserInformation.userID;

        Log.i("user_id", user_id);

        WishlistList task = new WishlistList();
        task.execute(user_id);

        layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
            }

        }, 100);   // 1000 = 1초 후 도출

        adapter.setOnWishlistClickListener(new WishListClickListener() {
            @Override
            public void onItemClick(WishListAdapter.ViewHolder holder, View view, int position) {
                WishList wishlist = (WishList) adapter.getItem(position);
                Toast.makeText(getApplicationContext(), "선택된 제품 : " + wishlist.getProduct_name(), Toast.LENGTH_LONG).show();

                //(ItemInformationActivity에 상품 ID 전달)
                Intent intent = new Intent(getApplicationContext(), ItemInformationActivity.class);
//                Toast.makeText(getContext(), "선택된 제품번호 : " + item.getProduct_no(), Toast.LENGTH_LONG).show();
//                Toast.makeText(getContext(), "판매자 ID : " + item.getUser_id(), Toast.LENGTH_LONG).show();
                intent.putExtra("product_no", wishlist.getProduct_no());
                intent.putExtra("user_id", wishlist.getUser_id());
                intent.putExtra("product_name", wishlist.getProduct_name());
                intent.putExtra("product_price", wishlist.getProduct_price());
                intent.putExtra("product_content", wishlist.getProduct_content());
                intent.putExtra("location", wishlist.getLocation());
                startActivity(intent);
            }
        });

        btn_back = (ImageButton) findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        CONTEXT = this;
        ((WishListActivity) WishListActivity.CONTEXT).onResume();

    }

    private Drawable drawableFromUrl(String url)
            throws IOException {
        Bitmap x;

        HttpURLConnection connection =
                (HttpURLConnection) new URL(url).openConnection();
        connection.connect();
        InputStream input = connection.getInputStream();

        x = BitmapFactory.decodeStream(input);
        return new BitmapDrawable(x);
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
                    for (int i=0; i < jArr.length(); i++) {
                        json = jArr.getJSONObject(i);


                        adapter.addItem(new WishList(json.getString("user_id"), json.getString("product_content"), json.getString("location"), json.getInt("product_price"),
                                json.getString("time"), json.getInt("product_no"), json.getString("product_name"), drawableFromUrl("http://daisoinmyhouse.cafe24.com/images/" + json.getString("product_img"))));

//                            adapter.addItem(new Item(json.getString("name"), json.getString("address"),
//                                    date, json.getInt("price"), R.drawable.sample1, json.getInt("num")));
//                            adapter.addItem(new Item(json.getString("name"), "두정동", now, json.getInt("price"), R.drawable.sample1, 1));
                        receiveMsg = buffer.toString();
                        Log.i("테스트", receiveMsg);
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