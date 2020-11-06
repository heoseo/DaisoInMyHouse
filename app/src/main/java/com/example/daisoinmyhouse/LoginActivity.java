package com.example.daisoinmyhouse;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.kakao.auth.Session;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

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

        id_et = (EditText)findViewById(R.id.et_login_id);
        pw_et = (EditText)findViewById(R.id.et_login_password);

        btnLogin = (Button)findViewById(R.id.activity_login_login_btn);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{

                    user_id = id_et.getText().toString();
                    user_pw = pw_et.getText().toString();

                    LoginAction loginAction = new LoginAction();
                    String resultNickname = loginAction.execute(user_id, user_pw).get();    // 성공하면 닉네임 반환
                    System.out.println("@@@@@result:" + resultNickname);

                    if(!resultNickname.equals("1")){    // 로그인성공

                        Uri uri = Uri.parse("android.resource://com.example.daisoinmyhouse/" + R.drawable.profile);    // 기본이미지로 설정



                        saveData(resultNickname, user_id, uri);

                        Toast.makeText(getApplicationContext(), StaticUserInformation.nickName+"님 로그인되었습니다.", Toast.LENGTH_LONG).show();

                        Intent intent = new Intent();
                        intent.putExtra("result_msg", resultNickname);
                        setResult(RESULT_OK, intent);
                        finish();

                    }
                    else if(!resultNickname.equals("1")||resultNickname == null) { // 로그인 실패
                        Toast.makeText(getApplicationContext(), "아이디와 비밀번호를 확인하세요.", Toast.LENGTH_LONG).show();
                    }

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

        // 뒤로가기 버튼
        TextView btnBack = (TextView) findViewById(R.id.btn_back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

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
    }

    // 닉네임(ID)랑 profileURI firebase에 저장
    void saveData(String resultNickname, String user_id, Uri uri){
        //EditText의 닉네임 가져오기 [전역변수에]

        //Firebase storage에 이미지 저장하기 위해 파일명 만들기(날짜를 기반으로)
        SimpleDateFormat sdf= new SimpleDateFormat("yyyMMddhhmmss"); //20191024111224
        String fileName= sdf.format(new Date())+".png";

        //Firebase storage에 저장하기
        FirebaseStorage firebaseStorage= FirebaseStorage.getInstance();
        final StorageReference imgRef= firebaseStorage.getReference("profileImages/"+fileName);

        //1. Firebase Database에 nickName, profileUrl을 저장
        //firebase DB관리자 객체 소환
        FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
        //'profiles'라는 이름의 자식 노드 참조 객체 얻어오기
        DatabaseReference profileRef= firebaseDatabase.getReference("profiles");


        //2. 내 phone에 nickName, profileUrl을 저장
        SharedPreferences preferences = getSharedPreferences("account",MODE_PRIVATE);
        SharedPreferences.Editor editor=preferences.edit();

        editor.putString("nickName", resultNickname);      // !!!!!<- 회원가입 수정되면 nickname으로 고치기. 우선 id사용
        editor.putString("userID", user_id);
        editor.putString("profileUrl", uri.toString());
        editor.apply();

        StaticUserInformation.nickName = preferences.getString("nickName", uri.toString());
        StaticUserInformation.userID = preferences.getString("userID", uri.toString());
        StaticUserInformation.porfileUrl =preferences.getString("profileUrl", uri.toString());

        //닉네임을 key 식별자로 하고 프로필 이미지의 주소를 값으로 저장
        profileRef.child(StaticUserInformation.nickName).setValue(StaticUserInformation.porfileUrl);
//        Toast.makeText(this, StaticUserInformation.nickName + "님 프로필 저장 완료", Toast.LENGTH_SHORT).show();
    }//saveData() ..
}