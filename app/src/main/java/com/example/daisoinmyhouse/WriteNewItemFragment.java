package com.example.daisoinmyhouse;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class WriteNewItemFragment extends Fragment {

    TextView btnSelectCategory;
    TextView btnSetLocation;
    ImageButton btn_back;
    ImageView btn_photo;
    MainActivity activity;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        activity = (MainActivity) getActivity();



        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_write_new_item, container, false);
        btnSelectCategory = rootView.findViewById(R.id.tv_category);
        btnSelectCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), CategoryInWriteActivity.class);
                getContext().startActivity(intent);
            }
        });



        //사진 이미지 누르면 사진선택화면
        btn_photo = rootView.findViewById(R.id.imageView_photo);
        btn_photo.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), CameraActivity.class);
                getContext().startActivity(intent);


//                onActivityResult실행
                startActivityForResult(intent, 100);
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


        btnSetLocation = rootView.findViewById(R.id.tv_location);
        if(StaticUserInformation.myArea != null){
            btnSetLocation.setText(StaticUserInformation.myArea);
        }
        btnSetLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), SettingMyAreaActivity.class);
                getContext().startActivity(intent);

                startActivityForResult(intent, 100);
            }
        });





        return rootView;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent)
    {
        if(requestCode == 100 && resultCode == 1) {

            // cameraactivity에서 받아온 uri
            Uri uri = Uri.parse(intent.getExtras().get("imageUri").toString());
            btn_photo.setImageURI(uri);
//            Toast.makeText(getContext(), "test : " + intent.getExtras().get("test").toString(), Toast.LENGTH_LONG).show();
        }

    }
}