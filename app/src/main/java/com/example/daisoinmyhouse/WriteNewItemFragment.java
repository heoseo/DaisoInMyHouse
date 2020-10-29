package com.example.daisoinmyhouse;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import static android.app.Activity.RESULT_OK;

public class WriteNewItemFragment extends Fragment {

    EditText product_Resister,pricce,conttent,taag;
    TextView cattegory;
    Button writeBtn;
    ImageButton btn_back;
    ImageView btn_photo;
    MainActivity activity;
    Spinner spinner;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        activity = (MainActivity) getActivity();

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_write_new_item, container, false);

        product_Resister =rootView.findViewById(R.id.et_register);
        cattegory = rootView.findViewById(R.id.et_category);
        pricce = rootView.findViewById(R.id.et_price);
        taag=rootView.findViewById(R.id.et_tag);
        conttent = rootView.findViewById(R.id.et_explain);

        spinner = rootView.findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                cattegory.setText(parent.getItemAtPosition(position).toString());
                cattegory.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        //글쓰기 등록시 db연결
        writeBtn = rootView.findViewById(R.id.btn_register);
        writeBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                try {
                    String name = product_Resister.getText().toString();
                    String category = cattegory.getText().toString();
                    String price = pricce.getText().toString();
                    String tag = taag.getText().toString();
                    String content = conttent.getText().toString();
                    Write_RegisterActivity task =new Write_RegisterActivity();

                    String result = task.execute(name, category, price, content).get();
                    // 빈칸이 있는지 검사사
                    if(name.getBytes().length <=0 && category.getBytes().length <=0 && price.getBytes().length <=0 && tag.getBytes().length <=0 &&content.getBytes().length <=0 ){
                        Toast.makeText(activity.getApplicationContext(), "모든 입력창을 입력해주세요!", Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(activity.getApplicationContext(), result, Toast.LENGTH_LONG).show();
                        activity.finish();
                    }
                } catch (Exception e) {
                    Log.i("DBtest", ".....ERROR.....!");
                }
            }
        });


        //사진 이미지 누르면 다음화면
        btn_photo = rootView.findViewById(R.id.imageView_photo);
        btn_photo.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), CameraActivity.class);
                getContext().startActivity(intent);
            }
        });

        //뒤로가기 이미지 누르면 처음 메인
        btn_back = rootView.findViewById(R.id.btn_back);
        btn_back.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                activity.onFragmentChange(1);
            }
        });

        return rootView;
    }


}