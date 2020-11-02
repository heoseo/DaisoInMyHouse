package com.example.daisoinmyhouse;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.LocusId;
import android.content.pm.PackageInstaller;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

<<<<<<< HEAD
=======
import com.kakao.auth.*;
>>>>>>> 9d009ef1be94ec94d31efe8b2564bb0d5d29d501
import com.kakao.auth.ApiErrorCode;
import com.kakao.auth.AuthType;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeV2ResponseCallback;
import com.kakao.usermgmt.response.MeV2Response;
import com.kakao.usermgmt.response.model.Profile;
import com.kakao.usermgmt.response.model.UserAccount;
import com.kakao.util.OptionalBoolean;
import com.kakao.util.exception.KakaoException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
<<<<<<< HEAD
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
=======
import org.apache.http.message.BasicNameValuePair;
>>>>>>> 9d009ef1be94ec94d31efe8b2564bb0d5d29d501

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity {

    Button btnJoin;
    Button btnLogin;
    EditText id_et, pw_et;
    String user_id, user_pw;
    Session session;
    private SessionCallback sessionCallback = new SessionCallback();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

<<<<<<< HEAD
        id_et = (EditText)findViewById(R.id.et_login_id);
        pw_et = (EditText)findViewById(R.id.et_login_password);

        btnLogin = (Button)findViewById(R.id.activity_login_login_btn);

//        session = Session.getCurrentSession();
//        session.addCallback(sessionCallback);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    user_id = id_et.getText().toString();
                    user_pw = pw_et.getText().toString();

                    LoginAction loginAction = new LoginAction();
                    String result = loginAction.execute(user_id, user_pw).get();

                    StaticUserInformation.nickName = result;

                    // String[][] parsedData = jsonParserList(result);

                    // Intent intent = new Intent(getApplicationContext(), LoginTest.class);
                    // intent.putExtra("name", parsedData[0][0]);
                    // intent.putExtra("id", parsedData[0][1]);
                    // intent.putExtra("email", parsedData[0][2]);
                    //intent.putExtra("phone", parsedData[0][3]);
                    // intent.putExtra("address", parsedData[0][4]);
                    // startActivity(intent);
                }catch (Exception e){
                    Log.i("DB", "에러");
                }
                //session.open(AuthType.KAKAO_LOGIN_ALL, LoginActivity.this);
=======
        id_et = (EditText)findViewById(R.id.et_id);
        pw_et = (EditText)findViewById(R.id.et_pw);

        btnLogin = (Button)findViewById(R.id.activity_login_login_btn);

        session = Session.getCurrentSession();
        session.addCallback(sessionCallback);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                session.open(AuthType.KAKAO_LOGIN_ALL, LoginActivity.this);
>>>>>>> 9d009ef1be94ec94d31efe8b2564bb0d5d29d501
            }
        });

        btnJoin = (Button)findViewById(R.id.activity_login_join_btn);
        btnJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 회원가입 창으로 넘어감.
                Intent intent = new Intent(getApplicationContext(), JoinActivity.class);
                startActivity(intent);
            }
        });
    }
<<<<<<< HEAD

    protected void onDestroy() {
        super.onDestroy();

        // 세션 콜백 삭제
        Session.getCurrentSession().removeCallback(sessionCallback);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        // 카카오톡|스토리 간편로그인 실행 결과를 받아서 SDK로 전달
        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            return;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
=======

    protected void onDestroy() {
        super.onDestroy();
>>>>>>> 9d009ef1be94ec94d31efe8b2564bb0d5d29d501

        // 세션 콜백 삭제
        Session.getCurrentSession().removeCallback(sessionCallback);
    }

<<<<<<< HEAD
    public String[][] jsonParserList(String pRecvServerPage) {
        Log.i("서버에서 받은 전체 내용 : ", pRecvServerPage);

        try {
            JSONObject json = new JSONObject(pRecvServerPage);
            JSONArray jArr = json.getJSONArray("list");

            // 받아온 pRecvServerPage를 분석하는 부분
            String[] jsonName = {"name", "id", "email", "phone", "address"};
            String[][] parseredData = new String[jArr.length()][jsonName.length];
            for (int i = 0; i < jArr.length(); i++) {
                json = jArr.getJSONObject(i);
                if(json != null) {
                    for(int j = 0; j < jsonName.length; j++) {
                        parseredData[i][j] = json.getString(jsonName[j]);
                    }
                }
            }

            // 분해 된 데이터를 확인하기 위한 부분
            for(int i=0; i<parseredData.length; i++){
                Log.i("JSON을 분석한 데이터 "+i+" : ", parseredData[i][0]);
                Log.i("JSON을 분석한 데이터 "+i+" : ", parseredData[i][1]);
                Log.i("JSON을 분석한 데이터 "+i+" : ", parseredData[i][2]);
                Log.i("JSON을 분석한 데이터 "+i+" : ", parseredData[i][3]);
                Log.i("JSON을 분석한 데이터 "+i+" : ", parseredData[i][4]);
            }
            return parseredData;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
=======
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        // 카카오톡|스토리 간편로그인 실행 결과를 받아서 SDK로 전달
        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            return;
        }

        super.onActivityResult(requestCode, resultCode, data);
>>>>>>> 9d009ef1be94ec94d31efe8b2564bb0d5d29d501
    }
}