package com.example.daisoinmyhouse;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

public class FirtstSettingAreaActivity extends AppCompatActivity {

    Button btnSettingArea;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_setting_area);

        btnSettingArea = (Button) findViewById(R.id.btn_set_area_and_start_app);
        btnSettingArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SettingMyAreaActivity.class);
                startActivity(intent);
            }
        });


    }

}
