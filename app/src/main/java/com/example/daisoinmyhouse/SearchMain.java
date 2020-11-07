package com.example.daisoinmyhouse;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import java.util.ArrayList;


public class SearchMain extends AppCompatActivity implements TextWatcher{
    RecyclerView recyclerView;
    EditText editText;
    RecyclerViewAdapter adapter;

    ArrayList<Item> items = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_search_list);

        recyclerView = (RecyclerView)findViewById(R.id.search_list);
        editText = (EditText)findViewById(R.id.et_search_item);
        editText.addTextChangedListener(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);

        SearchMain.ProductList networkTask = new SearchMain.ProductList("http://daisoinmyhouse.cafe24.com/recyclerviewList.jsp", null);
        networkTask.execute();



//        adapter = new RecyclerViewAdapter(getApplicationContext(), items);
//        adapter.setOnItemClickListener(new OnProductItemClickListener() {
//            @Override
//            public void onItemClick(ItemAdapter.ViewHolder holder, View view, int position) {
//                Item item = (Item) adapter.getItem(position);
////                Toast.makeText(getContext(), "선택된 제품 : " + item.getName(), Toast.LENGTH_LONG).show();
//
//                // 1028 코드추가 (ItemInformationActivyty에 상품ID전달)
//                Intent intent = new Intent(this,ItemInformationActivity.class);
//                Toast.makeText(getApplicationContext(), "선택된 제품ID : " + item.getProduct_no(), Toast.LENGTH_LONG).show();
//                intent.putExtra("productID", item.getProduct_no());
//                startActivity(intent);
//            }
//
//        });


}

    public class ProductList extends AsyncTask<Void, Void, String> {
        private String url;
        private ContentValues values;

        public ProductList(String url, ContentValues values) {
            this.url = url;
            this.values = values;
        }

        @Override
        protected String doInBackground(Void... params) {
            String result; // 요청 결과를 저장할 변수.
            SearchMain.RequestHttpURLConnection requestHttpURLConnection = new SearchMain.RequestHttpURLConnection();

            // 해당 URL로 부터 결과물을 얻어온다.
            result = requestHttpURLConnection.request(url, values);

            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }

    public class RequestHttpURLConnection {
        public String request(String _url, ContentValues _params) {
            // HttpURLConnection 참조 변수.
            HttpURLConnection urlConn = null;

            // URL 뒤에 붙여서 보낼 파라미터.
            StringBuffer sbParams = new StringBuffer();

            /**
             * 1. StringBuffer에 파라미터 연결
             * */

            // 보낼 데이터가 없으면 파라미터를 비운다.
            if (_params == null)

            /**
             * 2. HttpURLConnection을 통해 web의 데이터를 가져온다.
             * */
                try {
                    URL url = new URL(_url);
                    urlConn = (HttpURLConnection) url.openConnection();

                    // [2-1]. urlConn 설정.
                    urlConn.setRequestMethod("POST"); // URL 요청에 대한 메소드 설정 : POST.
                    urlConn.setRequestProperty("Accept-Charset", "UTF-8"); // Accept-Charset 설정.
                    urlConn.setRequestProperty("Context_Type", "application/x-www-form-urlencoded;cahrset=UTF-8");

                    // [2-2]. parameter 전달 및 데이터 읽어오기.
                    String strParams = sbParams.toString(); //sbParams에 정리한 파라미터들을 스트링으로 저장. 예)id=id1&pw=123;
                    OutputStream os = urlConn.getOutputStream();
                    os.write(strParams.getBytes("UTF-8")); // 출력 스트림에 출력.
                    os.flush(); // 출력 스트림을 플러시(비운다)하고 버퍼링 된 모든 출력 바이트를 강제 실행.
                    os.close(); // 출력 스트림을 닫고 모든 시스템 자원을 해제.

                    // [2-3]. 연결 요청 확인.
                    // 실패 시 null을 리턴하고 메서드를 종료.
                    if (urlConn.getResponseCode() != HttpURLConnection.HTTP_OK)
                        return null;

                    // [2-4]. 읽어온 결과물 리턴.
                    // 요청한 URL의 출력물을 BufferedReader로 받는다.
                    BufferedReader reader = new BufferedReader(new InputStreamReader(urlConn.getInputStream(), "UTF-8"));

                    // 출력물의 라인과 그 합에 대한 변수.
                    String line;
                    String page = "";

                    // 라인을 받아와 합친다.
                    // 버퍼의 웹문서 소스를 줄 단위로 읽어(line), page에 저장함
                    while ((line = reader.readLine()) != null) {
                        page += line;
                    }
                    try {
                        // JSP에서 보낸 JSON 받아오자  JSONObject = siteDataMain
                        JSONObject json = new JSONObject(page);
                        JSONArray jArr = json.getJSONArray("PRODUCT_LIST");

                        // JSON이 가진 크기만큼 데이터를 받아옴
                        for (int i = 0; i < jArr.length(); i++) {
                            json = jArr.getJSONObject(i);


                            long now = System.currentTimeMillis();
                            Date date = new Date(now);
//                            adapter.addItem(new Item(json.getString("name"), "두정동",
//                                    date, json.getInt("price"), R.drawable.sample1, json.getInt("num")));

                           // adapter.addItem(new Item(json.getString("name"), json.getString("address"),
                                    //date, json.getInt("price"), R.drawable.sample1, json.getInt("num")));
                            //adapter.addItem(new Item(json.getString("name"), "두정동",date, json.getInt("price"), R.drawable.sample1, json.getInt("num")));
                        }
                        // 가져온 데이터들 확인
                        // textView.setText(가공 데이터);
                    } catch(Exception e){
                        e.printStackTrace();;
                    }
                } catch (MalformedURLException e) { // for URL.
                    e.printStackTrace();
                } catch (IOException e) { // for openConnection().
                    e.printStackTrace();
                } finally {
                    if (urlConn != null)
                        urlConn.disconnect();
                }
            return null;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        adapter.getFilter().filter(charSequence);
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

}
