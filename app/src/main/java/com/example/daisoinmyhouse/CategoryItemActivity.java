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

public class CategoryItemActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    CategoryItemAdapter adapter = new CategoryItemAdapter();
    GridLayoutManager layoutManager;

    ImageButton btn_back;
    TextView tv_category;

    int categoryNum;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_item);

        recyclerView = (RecyclerView) findViewById(R.id.rv_category_item);

        layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);

        tv_category = (TextView) findViewById(R.id.tv_item_category);
        categoryNum = getIntent().getExtras().getInt("categoryNum");
        CategoryList categorylist = new CategoryList();

        if(categoryNum == 1) {
            tv_category.setText("의류");
            categorylist.execute("의류");

        }
        else if(categoryNum == 2){
            tv_category.setText("생활용품");
            categorylist.execute("생활용품");

        }
        else if(categoryNum == 3){
            tv_category.setText("주방용품");
            categorylist.execute("주방용품");

        }
        else if(categoryNum == 4){
            tv_category.setText("디지털");
            categorylist.execute("디지털");

        }
        else if(categoryNum == 5){
            tv_category.setText("도서");
            categorylist.execute("도서");

        }
        else if(categoryNum == 6){
            tv_category.setText("기타");
            categorylist.execute("기타");

        }

        recyclerView.setAdapter(adapter);

        adapter.setOnCategoryItemClickListener(new CategoryItemClickListener() {
            @Override
            public void onItemClick(CategoryItemAdapter.ViewHolder holder, View view, int position) {
                CategoryItem item = (CategoryItem) adapter.getItem(position);
                Toast.makeText(getApplicationContext(), "선택된 제품 : " + item.getProduct_name(), Toast.LENGTH_LONG).show();

                Intent intent = new Intent(getApplicationContext(), ItemInformationActivity.class);
                intent.putExtra("productID", item.getProduct_no());
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