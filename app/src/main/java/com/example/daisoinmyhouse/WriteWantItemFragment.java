package com.example.daisoinmyhouse;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;


public class WriteWantItemFragment extends Fragment {

    private static final int SEARCH_LOCATION_ACTIVITY = 1000;
    private final int REQ_CODE_LOCAION = 50;
    EditText et_want_name, et_want_content;
    Button writeBtn;
    TextView btnSetLocation;
    MainActivity activity;
    Spinner want_spinner;
    LinearLayout btn_otherwrite;

    public static WriteWantItemFragment newInstance(){
        return new WriteWantItemFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        activity = (MainActivity) getActivity();

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_write_want_item, container, false);

        et_want_name =rootView.findViewById(R.id.et_want_name);
        et_want_content = rootView.findViewById(R.id.et_want_content);

        want_spinner = rootView.findViewById(R.id.spinner_want_cate);



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

                    String want_name = et_want_name.getText().toString();
                    String want_cate = want_spinner.getSelectedItem().toString();
                    String location = btnSetLocation.getText().toString();
                    String want_content = et_want_content.getText().toString();

                    WantWriteAction task = new WantWriteAction();

                    // 빈칸이 있는지 검사사
                    if(want_name.getBytes().length <=0 || want_cate.getBytes().length <= 0 || want_content.getBytes().length <= 0 ){
                        Toast.makeText(activity.getApplicationContext(), "모든 입력창을 입력해주세요!", Toast.LENGTH_LONG).show();
                    }else{
                        String result = task.execute(user_id, want_cate, want_name, want_content, location).get();
                        Toast.makeText(activity.getApplicationContext(), result, Toast.LENGTH_LONG).show();

                        et_want_name.setText("");
                        et_want_content.setText("");
                        want_spinner.setSelection(0);
                        btnSetLocation.setText("위치설정");
                    }
                } catch (Exception e) {
                    Log.i("DBtest", ".....ERROR.....!");
                }
            }
        });


        return rootView;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent)
    {
        super.onActivityResult(requestCode, resultCode, intent);

        if (resultCode != RESULT_OK) {
//            Toast.makeText(getContext(), "결과가 성공이 아님.", Toast.LENGTH_SHORT).show();
            return;
        }


//        if (requestCode == REQ_CODE_LOCAION) {
//            String resultMsg = intent.getStringExtra("result_msg");
//            btnSetLocation.setText(resultMsg);
//
////            Toast.makeText(MainActivity.this, "결과 : " + resultMsg, Toast.LENGTH_SHORT).show();
//        } else {
////            Toast.makeText(MainActivity.this, "REQUEST_ACT가 아님", Toast.LENGTH_SHORT).show();
//        }

        if(requestCode == SEARCH_LOCATION_ACTIVITY) {
            if (resultCode == RESULT_OK){
                String data = intent.getStringExtra("location");

                if(data != null){
                    btnSetLocation.setText(data);
                }
            }
        }


    }


}