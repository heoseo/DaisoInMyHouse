package com.example.daisoinmyhouse;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;


public class JoinActivity extends AppCompatActivity {

    private static final int SEARCH_ADDRESS_ACTIVITY = 10000;
    private static final int SEARCH_LOCATION_ACTIVITY = 1000;

    Button joinBtn, zipCodeBtn, locationBtn;
    EditText nameet, nicknameet, idet, pwet, pwet_confirm, emailet, phoneet1, phoneet2, phoneet3, addresset1, addresset2, birthet;
    TextView zipcodetv, locationtv;
    Spinner emails;
    LinearLayout pwll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        joinBtn = (Button)findViewById(R.id.activity_join_join_btn);
        zipCodeBtn = (Button)findViewById(R.id.btn_zip_code);
        locationBtn = (Button)findViewById(R.id.btn_find_location);
        zipcodetv = (TextView)findViewById(R.id.tv_zip_code);

        locationtv = (TextView)findViewById(R.id.tv_region);
        nameet = (EditText)findViewById(R.id.et_name);
        idet = (EditText)findViewById(R.id.et_id);
        nicknameet = (EditText)findViewById(R.id.et_nickname);
        pwet = (EditText)findViewById(R.id.et_pw);
        pwet_confirm = (EditText)findViewById(R.id.et_pw_confirm);
        emailet = (EditText)findViewById(R.id.et_email);
        phoneet1 = (EditText)findViewById(R.id.et_phone1);
        phoneet2 = (EditText)findViewById(R.id.et_phone2);
        phoneet3 = (EditText)findViewById(R.id.et_phone3);
        addresset1 = (EditText)findViewById(R.id.et_address1);
        addresset2 = (EditText)findViewById(R.id.et_address2);

        birthet = (EditText)findViewById(R.id.et_birth);
        emails = (Spinner)findViewById(R.id.spinner_email);

        pwll = (LinearLayout)findViewById(R.id.ll_pw);

        locationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), FindLocationActivity.class);
                startActivityForResult(intent, SEARCH_LOCATION_ACTIVITY);
            }
        });

        zipCodeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), FindAddressActivity.class);
                startActivityForResult(intent, SEARCH_ADDRESS_ACTIVITY);
            }
        });

        // 뒤로가기 버튼
        ImageButton btnBack = (ImageButton) findViewById(R.id.btn_back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        joinBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                try {

                    String user_name = nameet.getText().toString();
                    String nickname = nicknameet.getText().toString();
                    String user_id = idet.getText().toString();
                    String user_password = pwet.getText().toString();

                    String email1 = emailet.getText().toString();
                    String email2 = emails.getSelectedItem().toString();


                    String email = email1 + "@" + email2;


                    String phone1 = phoneet1.getText().toString();
                    String phone2 = phoneet2.getText().toString();
                    String phone3 = phoneet3.getText().toString();


                    String phone = phone1 + phone2 + phone3;

                    String zip = zipcodetv.getText().toString();
                    String adr1 = addresset1.getText().toString();
                    String adr2 = addresset2.getText().toString();

                    String address = "(" + zip + ") " + adr1 + " " + adr2;

                    String birth = birthet.getText().toString();
                    String location = locationtv.getText().toString();
                    RegisterActivity task = new RegisterActivity();




                    //패스워드 일치 검사
                    if(pwet.getText().toString().equals(pwet_confirm.getText().toString())){
                        // 빈칸이 있는지 검사
                        pwll.setVisibility(View.GONE);
                        if(user_name.getBytes().length <=0 || user_id.getBytes().length <=0 || user_password.getBytes().length <=0
                                || email.getBytes().length <=0 || phone.getBytes().length <=0 || address.getBytes().length <=0){
                            Toast.makeText(getApplicationContext(), "모든 입력창을 입력해주세요", Toast.LENGTH_LONG).show();
                        }else{
                            String result = task.execute(user_name, nickname, user_id, user_password, email, phone, address, birth, location).get();
                            if(result.equals("0")){
                                Toast.makeText(getApplicationContext(), "이미 존재하는 닉네임입니다", Toast.LENGTH_LONG).show();
                                nicknameet.requestFocus();
                            }else if(result.equals("1")){
                                Toast.makeText(getApplicationContext(), "이미 존재하는 아이디입니다", Toast.LENGTH_LONG).show();
                                idet.requestFocus();
                            }else{
                                Toast.makeText(getApplicationContext(), user_name + "님 회원가입이 완료되었습니다", Toast.LENGTH_LONG).show();
                                finish();
                            }
                        }
                    }else{
                        pwll.setVisibility(View.VISIBLE);
                    }

                } catch (Exception e) {
                    Log.i("DBtest", ".....ERROR.....!");
                }
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent)
    {
        super.onActivityResult(requestCode, resultCode, intent);
        switch (requestCode) {
            case SEARCH_ADDRESS_ACTIVITY:
                if (resultCode == RESULT_OK){
                    String data = intent.getExtras().getString("data");

                    String zipcode = data.substring(0, 5);
                    String adr = data.substring(7);

                    if (data != null) {
                        zipcodetv.setText(zipcode);
                        addresset1.setText(adr);
                    }
                }
                break;
            case SEARCH_LOCATION_ACTIVITY:
                if (resultCode == RESULT_OK){
                    String data = intent.getStringExtra("location");

                    if(data != null){
                        locationtv.setText(data);
                    }
                }
                break;
        }
    }
}