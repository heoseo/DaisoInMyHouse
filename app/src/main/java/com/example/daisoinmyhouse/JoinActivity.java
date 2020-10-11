package com.example.daisoinmyhouse;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class JoinActivity extends AppCompatActivity {

    Button joinBtn;
    EditText nameet, idet, pwet, pwet_confirm, emailet, phoneet, addresset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        joinBtn = (Button)findViewById(R.id.activity_join_join_btn);
        nameet = (EditText)findViewById(R.id.et_name);
        idet = (EditText)findViewById(R.id.et_id);
        pwet = (EditText)findViewById(R.id.et_pw);
        pwet_confirm = (EditText)findViewById(R.id.et_pw_confirm);
        emailet = (EditText)findViewById(R.id.et_email);
        phoneet = (EditText)findViewById(R.id.et_phone);
        addresset = (EditText)findViewById(R.id.et_address);

        joinBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                try {
                    String result;
                    String name = nameet.getText().toString();
                    String id = idet.getText().toString();
                    String pw = pwet.getText().toString();
                    String email = emailet.getText().toString();
                    String phone = phoneet.getText().toString();
                    String address = addresset.getText().toString();

                    RegisterActivity task = new RegisterActivity();
                    result = task.execute(name, id, pw, email, phone, address).get();
                    Toast.makeText(getApplicationContext(),"회원가입이 완료되었습니다!", Toast.LENGTH_LONG);
                } catch (Exception e) {
                    Log.i("DBtest", ".....ERROR.....!");
                }
            }
        });

    }
}