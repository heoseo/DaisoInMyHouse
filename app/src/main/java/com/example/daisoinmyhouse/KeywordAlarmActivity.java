package com.example.daisoinmyhouse;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class KeywordAlarmActivity extends AppCompatActivity {

    Button btn_register;
    EditText et_keyword;
    ListView lv_keyword;
    String selected_item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keyword_alarm);

        btn_register = (Button)findViewById(R.id.btn_keyword_register);
        et_keyword = (EditText)findViewById(R.id.et_keyword_register);
        lv_keyword = (ListView)findViewById(R.id.lv_keyword);
        final ArrayList<String> keyword = new ArrayList<String>();
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, keyword);

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(keyword.size() < 10){
                    if(et_keyword.getText().toString().length() > 0){
                        keyword.add(et_keyword.getText().toString());
                        lv_keyword.setAdapter(arrayAdapter);
                        Toast.makeText(KeywordAlarmActivity.this, "등록되었습니다.", Toast.LENGTH_LONG).show();
                        et_keyword.setText("");
                    }
                }
                else{
                    Toast.makeText(KeywordAlarmActivity.this, "등록할 수 있는 키워드 개수를 넘었습니다.", Toast.LENGTH_LONG).show();
                    et_keyword.setText("");
                }
            }
        });

        lv_keyword.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                selected_item = (String)adapterView.getItemAtPosition(i);
                keyword.remove(selected_item);
                arrayAdapter.notifyDataSetChanged();
            }
        });



    }
}