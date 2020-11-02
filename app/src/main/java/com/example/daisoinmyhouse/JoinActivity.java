package com.example.daisoinmyhouse;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;


public class JoinActivity extends AppCompatActivity {

    private static final int SEARCH_ADDRESS_ACTIVITY = 10000;

    Button joinBtn, zipCodeBtn;
    EditText nameet, idet, pwet, pwet_confirm, emailet, phoneet1, phoneet2, phoneet3, addresset1, addresset2;
    TextView zipcodetv;
    Spinner emails;
    LinearLayout pwll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        joinBtn = (Button)findViewById(R.id.activity_join_join_btn);
        zipCodeBtn = (Button)findViewById(R.id.btn_zip_code);
        zipcodetv = (TextView)findViewById(R.id.tv_zip_code);

        nameet = (EditText)findViewById(R.id.et_name);
        idet = (EditText)findViewById(R.id.et_id);
        pwet = (EditText)findViewById(R.id.et_pw);
        pwet_confirm = (EditText)findViewById(R.id.et_pw_confirm);
        emailet = (EditText)findViewById(R.id.et_email);
        phoneet1 = (EditText)findViewById(R.id.et_phone1);
        phoneet2 = (EditText)findViewById(R.id.et_phone2);
        phoneet3 = (EditText)findViewById(R.id.et_phone3);
        addresset1 = (EditText)findViewById(R.id.et_address1);
        addresset2 = (EditText)findViewById(R.id.et_address2);

        emails = (Spinner)findViewById(R.id.spinner_email);

        pwll = (LinearLayout)findViewById(R.id.ll_pw);


        zipCodeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), FindAddressActivity.class);
                startActivityForResult(intent, SEARCH_ADDRESS_ACTIVITY);
            }
        });

        joinBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                try {
                    String name = nameet.getText().toString();
                    String id = idet.getText().toString();
                    String pw = pwet.getText().toString();
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

                    RegisterActivity task = new RegisterActivity();

                    String result = task.execute(name, id, pw, email, phone, address).get();



                    //패스워드 일치 검사
                    if(pwet.getText().toString().equals(pwet_confirm.getText().toString())){
                        // 빈칸이 있는지 검사
                        pwll.setVisibility(View.INVISIBLE);
                        if(name.getBytes().length <=0 || id.getBytes().length <=0 || pw.getBytes().length <=0 || email.getBytes().length <=0 || phone.getBytes().length <=0 || address.getBytes().length <=0){
                            Toast.makeText(getApplicationContext(), "모든 입력창을 입력해주세요!", Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
                            finish();
                        }
                    }else{
                        pwll.setVisibility(View.VISIBLE);
                    }
                    /*if(name.getBytes().length <=0 && id.getBytes().length <=0 && pw.getBytes().length <=0 && email.getBytes().length <=0 && phone.getBytes().length <=0 && address.getBytes().length <=0){
                        Toast.makeText(getApplicationContext(), "모든 입력창을 입력해주세요!", Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
                        finish();
                    }*/
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
                if (resultCode == RESULT_OK) {
                    String data = intent.getExtras().getString("data");

                    String zipcode = data.substring(0, 5);
                    String adr = data.substring(7);

                    if (data != null) {
                        zipcodetv.setText(zipcode);
                        addresset1.setText(adr);
                    }
                }
                break;
        }
    }
}