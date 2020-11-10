package com.example.daisoinmyhouse;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
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
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.sql.Date;

public class CategoryBookItemActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    CategoryItemAdapter adapter = new CategoryItemAdapter();
    GridLayoutManager layoutManager;

    ImageButton btn_back;
    TextView tv_category;

    String category;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_item);

        recyclerView = (RecyclerView) findViewById(R.id.rv_category_item);

        layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);

        tv_category = (TextView) findViewById(R.id.tv_item_category);
        category = getIntent().getExtras().getString("category");

        CategoryList categorylist = new CategoryList();

        tv_category.setText(category);
        categorylist.execute(category);
        recyclerView.setAdapter(adapter);

        adapter.setOnCategoryItemClickListener(new CategoryItemClickListener() {
            @Override
            public void onItemClick(CategoryItemAdapter.ViewHolder holder, View view, int position) {
                CategoryItem item = (CategoryItem) adapter.getItem(position);
                Toast.makeText(getApplicationContext(), "선택된 제품 : " + item.getProduct_name(), Toast.LENGTH_LONG).show();

                Intent intent = new Intent(getApplicationContext(), ItemInformationActivity.class);
//                Toast.makeText(getContext(), "선택된 제품번호 : " + item.getProduct_no(), Toast.LENGTH_LONG).show();
//                Toast.makeText(getContext(), "판매자 ID : " + item.getUser_id(), Toast.LENGTH_LONG).show();
                intent.putExtra("product_no", item.getProduct_no());
                intent.putExtra("user_id", item.getUser_id());
                intent.putExtra("product_name", item.getProduct_name());
                intent.putExtra("product_price", item.getProduct_price());
                intent.putExtra("product_content", item.getProduct_content());
                intent.putExtra("location", item.getLocation());
                startActivity(intent);
            }
        });

        btn_back = (ImageButton) findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                finish();
            }
        });


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

    public class CategoryList extends AsyncTask<String, Void, String> {
        String sendMsg, receiveMsg;
        @Override
        protected String doInBackground(String... strings) {
            try {
                String str;
                // 접속할 서버 주소 (이클립스에서 android.jsp 실행시 웹브라우저 주소)
                URL url = new URL("http://daisoinmyhouse.cafe24.com/categoryList.jsp");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestMethod("POST");
                OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
                // 전송할 데이터. GET 방식으로 작성
                sendMsg = "product_cate=" + strings[0];
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
                    JSONArray jArr = json.getJSONArray("CATEGORY_LIST");
                    // JSON이 가진 크기만큼 데이터를 받아옴
                    for (int i = 0; i < jArr.length(); i++) {
                        json = jArr.getJSONObject(i);
                        long now = System.currentTimeMillis();
                        Date date = new Date(now);

                        //카테고리 adapter따로 만들거나 해야될듯?
                        adapter.addItem(new CategoryItem(json.getString("user_id"), json.getString("product_content"), json.getString("location"), json.getInt("product_price"),
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
