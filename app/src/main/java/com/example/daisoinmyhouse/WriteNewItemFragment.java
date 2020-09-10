package com.example.daisoinmyhouse;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class WriteNewItemFragment extends Fragment {

    TextView btnSelectCategory;
    ImageButton btn_photo,btn_back;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_write_new_item, container, false);
        btnSelectCategory = rootView.findViewById(R.id.tv_category);
        btnSelectCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), CategoryInWriteActivity.class);
                getContext().startActivity(intent);
            }
        });



        //사진 이미지 누르면 다음화면
        btn_photo = rootView.findViewById(R.id.btn_photo);
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
                Intent intent = new Intent(getContext(), MainActivity.class);
                getContext().startActivity(intent);
            }
        });




        return rootView;
    }
}