package com.example.daisoinmyhouse;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class SearchMain extends AppCompatActivity implements TextWatcher{
    RecyclerView recyclerView;
    EditText editText;
    RecyclerViewAdapter adapter;

    ArrayList<String> items = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView)findViewById(R.id.search_list);
        editText = (EditText)findViewById(R.id.et_search_item);
        editText.addTextChangedListener(this);

        items.add("김씨");
        items.add("이씨");
        items.add("정씨");
        items.add("박씨");
        items.add("오씨");
        items.add("박씨");
        items.add("금씨");
        items.add("최씨");

        adapter = new RecyclerViewAdapter(getApplicationContext(), items);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);

    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        adapter.getFilter().filter(charSequence);
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

}
