package com.example.daisoinmyhouse;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
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

import java.io.InputStream;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

public class HomeRentItemFragment extends Fragment {

    RecyclerView recyclerView;
    ItemAdapter adapter = new ItemAdapter();
    GridLayoutManager layoutManager;
    String url = "http://daisoinmyhouse.cafe24.com/images/";


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_home_rent_item, container, false);





        //홈화면 RecyclerView 설정
        recyclerView = v.findViewById(R.id.rv_product);
        layoutManager = new GridLayoutManager(getActivity(), 2);
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



        return v;
    }

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        //prepareData();

        ProductList networkTask = new ProductList("http://daisoinmyhouse.cafe24.com/productList.jsp", null);
        networkTask.execute();

//
//        adapter.addItem(new Item("test1234", "기타 대여해줍니다.", "천안시 쌍용동", 30000,
//                "2020-11-10 00:00:00", 1, "기타", R.drawable.guitar));
//        adapter.addItem(new Item("labasimys", "실내자전거 대여해줍니다.", "천안시 두정동", 50000,
//                "2020-11-09 21:00:00", 2, "실내자전거", R.drawable.cycle));
//        adapter.addItem(new Item("labasimys", "드릴 대여해줍니다.", "천안시 백석동", 10000,
//                "2020-11-09 15:00:00", 3, "드릴", R.drawable.drill));
//        adapter.addItem(new Item("khm3813","카메라 대여해줍니다.", "천안시 신부동", 30000,
//                "2020-11-08 21:00:00", 4, "카메라", R.drawable.camera));


        adapter.setOnItemClickListener(new OnProductItemClickListener() {
            @Override
            public void onItemClick(ItemAdapter.ViewHolder holder, View view, int position) {
                Item item = (Item) adapter.getItem(position);
//                Toast.makeText(getContext(), "선택된 제품 : " + item.getName(), Toast.LENGTH_LONG).show();

                // 1028 코드추가 (ItemInformationActivyty에 상품ID전달)
                Intent intent = new Intent(getContext(), ItemInformationActivity.class);
//                Toast.makeText(getContext(), "선택된 제품번호 : " + item.getProduct_no(), Toast.LENGTH_LONG).show();
//                Toast.makeText(getContext(), "판매자 ID : " + item.getUser_id(), Toast.LENGTH_LONG).show();
                intent.putExtra("product_no", item.getProduct_no());
                intent.putExtra("user_id", item.getUser_id());
                intent.putExtra("product_name", item.getProduct_name());
                intent.putExtra("product_price", item.getProduct_price());
                intent.putExtra("product_content", item.getProduct_content());
                intent.putExtra("location", item.getLocation());
                getContext().startActivity(intent);
            }

            @Override
            public void onItemWantClick(ItemWantAdapter.ViewHolder holder, View view, int position) { }
        });
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
            RequestHttpURLConnection requestHttpURLConnection = new RequestHttpURLConnection();

            // 해당 URL로 부터 결과물을 얻어온다.
            result = requestHttpURLConnection.request(url, values);

            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
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

                            adapter.addItem(new Item(json.getString("user_id"), json.getString("product_content"), json.getString("location"), json.getInt("product_price"),
                                    json.getString("time"), json.getInt("product_no"), json.getString("product_name"), drawableFromUrl("http://daisoinmyhouse.cafe24.com/images/" + json.getString("product_img"))));

                            Log.i("테스트", "ok");
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
}