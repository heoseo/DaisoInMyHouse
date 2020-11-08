package com.example.daisoinmyhouse;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import static android.content.ContentValues.TAG;
import static android.content.Context.MODE_PRIVATE;


public class WriteWantItemFragment extends Fragment {

    private static final int SEARCH_LOCATION_ACTIVITY = 1000;
    EditText product_name,conttent,taag;
    TextView cattegory;
    Button writeBtn;
    TextView btnSetLocation;
    MainActivity activity;
    Spinner spinner;
    LinearLayout btn_otherwrite;

    public static WriteWantItemFragment newInstance(){
        return new WriteWantItemFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        activity = (MainActivity) getActivity();


        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_write_want_item, container, false);

        product_name =rootView.findViewById(R.id.et_product_name);
        conttent = rootView.findViewById(R.id.et_product_content);

        spinner = rootView.findViewById(R.id.spinner_product_cate);
/*        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });*/



        //대여 해주는 글 작성화면 전환
        btn_otherwrite = rootView.findViewById(R.id.ll_rent);
        btn_otherwrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) getActivity()).replaceFragment2(WriteNewItemFragment.newInstance());
            }
        });



        // 지역설정
        btnSetLocation = rootView.findViewById(R.id.tv_location);
        if(StaticUserInformation.myArea != null){
            btnSetLocation.setText(StaticUserInformation.myArea);
        }
        btnSetLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), FindLocationActivity.class);
                startActivityForResult(intent, SEARCH_LOCATION_ACTIVITY);

            }
        });



        //글쓰기 등록시 db연결
        writeBtn = rootView.findViewById(R.id.btn_register);
        writeBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                try {
                    SharedPreferences preferences = getContext().getSharedPreferences("account", MODE_PRIVATE);
                    StaticUserInformation.userID = preferences.getString("userID", null);

                    String user_id = StaticUserInformation.userID;

                    String want_name  = product_name.getText().toString();
                    String want_cate  = cattegory.getText().toString();
                    String location = btnSetLocation.getText().toString();
                    String want_content = conttent.getText().toString();

                    Log.i("테스트", want_name);

                    wantWriteActivity task =new wantWriteActivity();

                    //String result = task.execute(user_id,want_cate , want_name , want_content,location).get();

                    // 빈칸이 있는지 검사사
                    if(want_name.getBytes().length <=0 || want_cate.getBytes().length <= 0 || location.getBytes().length <= 0 || want_content.getBytes().length <= 0 ){
                        Toast.makeText(activity.getApplicationContext(), "모든 입력창을 입력해주세요!", Toast.LENGTH_LONG).show();
                    }else{
                        String result = task.execute(user_id,want_cate , want_name , want_content,location).get();
                        Toast.makeText(activity.getApplicationContext(), result, Toast.LENGTH_LONG).show();
                        //activity.finish();
                    }
                } catch (Exception e) {
                    Log.i("DBtest", ".....ERROR.....!");
                }
            }
        });





        return rootView;
    }


}