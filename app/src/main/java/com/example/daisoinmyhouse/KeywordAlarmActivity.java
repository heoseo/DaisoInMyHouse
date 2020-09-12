package com.example.daisoinmyhouse;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class KeywordAlarmActivity extends AppCompatActivity {

    Button btn_register;
    EditText et_keyword;
    ArrayList<String> keyword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keyword_alarm);

        btn_register = (Button)findViewById(R.id.btn_keyword_register);
        et_keyword = (EditText)findViewById(R.id.et_keyword_register);
        keyword = new ArrayList<String>();

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(keyword.size() <= 10){
                    keyword.add(et_keyword.getText().toString());
                    Toast.makeText(KeywordAlarmActivity.this, "등록되었습니다.", Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(KeywordAlarmActivity.this, "등록할 수 있는 키워드 개수를 넘었습니다.", Toast.LENGTH_LONG).show();
                }
            }
        });


    }
}