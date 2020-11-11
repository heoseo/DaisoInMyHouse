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
import android.widget.EditText;
import android.widget.ImageButton;
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

public class SearchActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    SearchAdapter adapter = new SearchAdapter();
    GridLayoutManager layoutManager;
    ImageButton btn_back;
    public static Context CONTEXT;
    String search;
    EditText et_search;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_list);

        search = getIntent().getExtras().getString("search");
        et_search = (EditText) findViewById(R.id.et_search_item);
        et_search.setText(search);
        et_search.requestFocus();

        recyclerView = (RecyclerView) findViewById(R.id.search_list);

        SharedPreferences preferences=getSharedPreferences("account",MODE_PRIVATE);
        StaticUserInformation.userID = preferences.getString("userID", null);

        String user_id = StaticUserInformation.userID;

        Log.i("user_id", user_id);

        SearchList task = new SearchList();
        task.execute(search);

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

        adapter.setOnSearchClickListener(new SearchItemClickListener() {
            @Override
            public void onItemClick(SearchAdapter.ViewHolder holder, View view, int position) {
                Item item = (Item) adapter.getItem(position);

                //(ItemInformationActivity에 상품 ID 전달)
                Intent intent = new Intent(getApplicationContext(), ItemInformationActivity.class);
                intent.putExtra("product_no", item.getProduct_no());
                intent.putExtra("user_id", item.getUser_id());
                intent.putExtra("product_name", item.getProduct_name());
                intent.putExtra("product_price", item.getProduct_price());
                intent.putExtra("product_content", item.getProduct_content());
                intent.putExtra("location", item.getLocation());
                startActivity(intent);
            }
        });

        btn_back = (ImageButton) findViewById(R.id.img_btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        CONTEXT = this;
        ((SearchActivity) SearchActivity.CONTEXT).onResume();


//        //검색버튼
//        et_search = v.findViewById(R.id.et_search_item);
//        btn_search=v.findViewById(R.id.img_btn_search);
//        btn_search.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String search = et_search.getText().toString();
//                et_search.setText(null);
//                Intent intent = new Intent(getContext(), SearchActivity.class);
//                intent.putExtra("search", search);
//                getContext().startActivity(intent);
//            }
//        });

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

    public class SearchList extends AsyncTask<String, Void, String> {
        String sendMsg, receiveMsg;

        @Override
        protected String doInBackground(String... strings) {
            try {
                String str;
                // 접속할 서버 주소 (이클립스에서 android.jsp 실행시 웹브라우저 주소)
                URL url = new URL("http://daisoinmyhouse.cafe24.com/searchList.jsp");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestMethod("POST");
                OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
                // 전송할 데이터. GET 방식으로 작성
                sendMsg = "product_name=" + strings[0];
                osw.write(sendMsg);
                osw.flush();
                //jsp와 통신 성공 시 수행
                if (conn.getResponseCode() == conn.HTTP_OK) {
                    InputStreamReader tmp = new InputStreamReader(conn.getInputStream(), "EUC-kr");
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
                    JSONArray jArr = json.getJSONArray("SEARCH_LIST");

                    // JSON이 가진 크기만큼 데이터를 받아옴
                    for (int i = 0; i < jArr.length(); i++) {
                        json = jArr.getJSONObject(i);


                        adapter.addItem(new Item(json.getString("user_id"), json.getString("product_content"), json.getString("location"), json.getInt("product_price"),
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
}