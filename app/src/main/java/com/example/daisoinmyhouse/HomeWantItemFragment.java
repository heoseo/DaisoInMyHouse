package com.example.daisoinmyhouse;
import android.content.ContentValues;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
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

public class HomeWantItemFragment extends Fragment {

    RecyclerView recyclerView;
    ItemWantAdapter wantAdapter = new ItemWantAdapter();
    GridLayoutManager layoutManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_home_want_item, container, false);

        //홈화면 RecyclerView 설정
        recyclerView = v.findViewById(R.id.rv_product);
        layoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setAdapter(wantAdapter);



        return v;
    }

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        //prepareData();


//        wantAdapter.addItem(new ItemWant("test1234",
//                "컴퓨터구해요",
//                "천안시 쌍용동",
//                "2020-11-07 13:00:00",
//                1,
//                "컴퓨터",
//                "의류")
//        );
//        wantAdapter.addItem(new ItemWant("test1234",
//                "컴퓨터구해요",
//                "천안시 쌍용동",
//                "2020-11-07 13:00:00",
//                1,
//                "컴퓨터",
//                "생활용품")
//        );
//        wantAdapter.addItem(new ItemWant("test1234",
//                "컴퓨터구해요",
//                "천안시 쌍용동",
//                "2020-11-07 13:00:00",
//                1,
//                "컴퓨터",
//                "주방용품")
//        );
//        wantAdapter.addItem(new ItemWant("test1234",
//                "컴퓨터구해요",
//                "천안시 쌍용동",
//                "2020-11-07 13:00:00",
//                1,
//                "컴퓨터",
//                "디지털")
//        );
//        wantAdapter.addItem(new ItemWant("test1234",
//                "컴퓨터구해요",
//                "천안시 쌍용동",
//                "2020-11-07 13:00:00",
//                1,
//                "컴퓨터",
//                "도서")
//        );
//        wantAdapter.addItem(new ItemWant("test1234",
//                "컴퓨터구해요",
//                "천안시 쌍용동",
//                "2020-11-07 13:00:00",
//                1,
//                "컴퓨터",
//                "기타")
//        );


        ProductList networkTask = new ProductList("http://daisoinmyhouse.cafe24.com/wantList.jsp", null);
        networkTask.execute();

        wantAdapter.setOnItemClickListener(new OnProductItemClickListener() {
            @Override
            public void onItemClick(ItemAdapter.ViewHolder holder, View view, int position) {
            }

            @Override
            public void onItemWantClick(ItemWantAdapter.ViewHolder holder, View view, int position) {
                ItemWant itemWant = (ItemWant) wantAdapter.getItem(position);
//                Toast.makeText(getContext(), "선택된 제품 : " + item.getName(), Toast.LENGTH_LONG).show();

                // 1028 코드추가 (ItemInformationActivyty에 상품ID전달)
                Intent intent = new Intent(getContext(), ItemWantInformationActivity.class);
//                Toast.makeText(getContext(), "선택된 제품번호 : " + item.getProduct_no(), Toast.LENGTH_LONG).show();
//                Toast.makeText(getContext(), "판매자 ID : " + item.getUser_id(), Toast.LENGTH_LONG).show();
                intent.putExtra("want_no", itemWant.getWant_no());
                intent.putExtra("user_id", itemWant.getUser_id());
                intent.putExtra("want_name", itemWant.getWant_name());
                intent.putExtra("want_content", itemWant.getWant_content());
                intent.putExtra("location", itemWant.getLocation());
                intent.putExtra("want_cate", itemWant.getCategory());
                getContext().startActivity(intent);
            }
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
                        JSONArray jArr = json.getJSONArray("WANT_LIST");

                        // JSON이 가진 크기만큼 데이터를 받아옴
                        for (int i = 0; i < jArr.length(); i++) {
                            Log.i("want테스트 ", "ok");
                            json = jArr.getJSONObject(i);

                            wantAdapter.addItem(new ItemWant(json.getString("user_id"),
                                                    json.getString("want_content"),
                                                    json.getString("location"),
                                                    json.getString("time"),
                                                    json.getInt("want_no"),
                                                    json.getString("want_name"),
                                                    json.getString("want_cate")
                            ));

                        }
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