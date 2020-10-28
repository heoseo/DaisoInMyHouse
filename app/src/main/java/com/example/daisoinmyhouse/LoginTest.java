package com.example.daisoinmyhouse;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class LoginTest extends AppCompatActivity {

    TextView name, id, email, phone, address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_test);

        name = (TextView)findViewById(R.id.login_test1);
        id = (TextView)findViewById(R.id.login_test2);
        email = (TextView)findViewById(R.id.login_test3);
        phone = (TextView)findViewById(R.id.login_test4);
        address = (TextView)findViewById(R.id.login_test5);

        Intent intent = getIntent();
        String name1 = intent.getStringExtra("name");
        String id1 = intent.getStringExtra("id");
        String email1 = intent.getStringExtra("email");
        String phone1 = intent.getStringExtra("phone");
        String address1 = intent.getStringExtra("address");

        name.setText(name1);
        id.setText(id1);
        email.setText(email1);
        phone.setText(phone1);
        address.setText(address1);
    }
}