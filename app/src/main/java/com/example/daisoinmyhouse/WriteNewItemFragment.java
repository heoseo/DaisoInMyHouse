package com.example.daisoinmyhouse;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;
import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

public class WriteNewItemFragment extends Fragment {

    EditText product_name_et, product_price_et, product_content_et;
    Button writeProductBtn;
    TextView btnSetLocation, cate_tv;
    TextView btn_back;
    ImageView btn_photo;
    MainActivity activity;
    Spinner spinner_cate;
    LinearLayout btn_otherwrite;

    private static final int SEARCH_LOCATION_ACTIVITY = 1000;
    private final int REQ_CODE_LOCAION = 50;
    private final int REQ_CODE_SELECT_IMAGE = 100;
    private String img_path = new String();
    private Bitmap image_bitmap_copy = null;
    private Bitmap image_bitmap = null;
    private String imageName = null;

    public static WriteNewItemFragment newInstance(){
        return new WriteNewItemFragment();
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        activity = (MainActivity) getActivity();
        final ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_write_rent_item, container, false);

        product_name_et =rootView.findViewById(R.id.et_product_name);
        product_price_et = rootView.findViewById(R.id.et_product_price);
        product_content_et = rootView.findViewById(R.id.et_product_content);

        spinner_cate = rootView.findViewById(R.id.spinner_product_cate);
        /*spinner_cate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                cate_tv.setText(parent.getItemAtPosition(position).toString());
                cate_tv.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });*/




        //사진 이미지 누르면 사진선택화면
        btn_photo = rootView.findViewById(R.id.imageView_photo);
        btn_photo.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                        .permitDiskReads()
                        .permitDiskWrites()
                        .permitNetwork().build());

                //카메라 권한 허용
                PermissionListener permissionListener = new PermissionListener() {
                    @Override
                    public void onPermissionGranted() {
                        Toast.makeText(getContext(), "권한이 허용됨",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                        Toast.makeText(getContext(), "권한이 거부됨",Toast.LENGTH_SHORT).show();
                    }
                };

                // 권한 체크
                TedPermission.with(getContext())
                        .setPermissionListener(permissionListener)
                        .setRationaleMessage("카메라 권한이 필요합니다.")
                        .setDeniedMessage("거부하셨습니다.")
                        .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                        .check();


                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, REQ_CODE_SELECT_IMAGE);
            }
        });

        //대여 원하는 글 작성화면 전환
        btn_otherwrite = rootView.findViewById(R.id.ll_want);
        btn_otherwrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) getActivity()).replaceFragment1(WriteWantItemFragment.newInstance());
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
        writeProductBtn = rootView.findViewById(R.id.btn_register);
        writeProductBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                try {
                    SharedPreferences preferences = getContext().getSharedPreferences("account", MODE_PRIVATE);
                    StaticUserInformation.userID = preferences.getString("userID", null);

                    String user_id = StaticUserInformation.userID;

                    String product_cate = spinner_cate.getSelectedItem().toString();
                    String product_name = product_name_et.getText().toString();
                    String product_price = product_price_et.getText().toString();
                    String product_content = product_content_et.getText().toString();
                    String product_img = imageName;
                    String location = btnSetLocation.getText().toString();

                    Log.i("location", location);

                    Write_RegisterActivity write = new Write_RegisterActivity();

                    if(product_name.getBytes().length <= 0 || product_content.getBytes().length <= 0 || product_price.getBytes().length <=  0){
                        Toast.makeText(activity.getApplicationContext(), "모든 입력창을 입력해주세요", Toast.LENGTH_LONG).show();
                    }else{
                        String result = write.execute(user_id, product_cate, product_name, product_price, product_content, product_img, location).get();

                        ImageUpload uploader = new ImageUpload(product_name_et.getText().toString(), imageName);
                        uploader.uploadPicture(img_path);

                        Toast.makeText(activity.getApplicationContext(), result, Toast.LENGTH_LONG).show();

                        spinner_cate.setSelection(0);
                        product_name_et.setText("");
                        product_price_et.setText("");
                        product_content_et.setText("");
                        btnSetLocation.setText("위치설정");
                        btn_photo.setImageResource(R.drawable.btn_add_img);
                    }
                } catch (Exception e) {
                    Log.i("DBtest", ".....ERROR.....!");
                }
            }
        });

        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent)
    {
        super.onActivityResult(requestCode, resultCode, intent);

        if (resultCode != RESULT_OK) {
//            Toast.makeText(getContext(), "결과가 성공이 아님.", Toast.LENGTH_SHORT).show();
            return;
        }


        if (requestCode == REQ_CODE_LOCAION) {
            String resultMsg = intent.getStringExtra("result_msg");
            btnSetLocation.setText(resultMsg);

//            Toast.makeText(MainActivity.this, "결과 : " + resultMsg, Toast.LENGTH_SHORT).show();
        } else {
//            Toast.makeText(MainActivity.this, "REQUEST_ACT가 아님", Toast.LENGTH_SHORT).show();
        }

        if (requestCode == REQ_CODE_SELECT_IMAGE) {
            try {
                img_path = getImagePathToUri(intent.getData()); //이미지의 URI를 얻어 경로값으로 반환.
                //       Toast.makeText(getBaseContext(), "img_path : " + img_path, Toast.LENGTH_SHORT).show();
                //이미지를 비트맵형식으로 반환
                image_bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), intent.getData());

                //사용자 단말기의 width , height 값 반환
                int reWidth = (int) (getActivity().getWindowManager().getDefaultDisplay().getWidth());
                int reHeight = (int) (getActivity().getWindowManager().getDefaultDisplay().getHeight());

                //image_bitmap 으로 받아온 이미지의 사이즈를 임의적으로 조절함. width: 400 , height: 300
                image_bitmap_copy = Bitmap.createScaledBitmap(image_bitmap, 400, 300, true);
                // ImageView image = (ImageView) findViewById(R.id.imageView);  //이미지를 띄울 위젯 ID값
                btn_photo.setImageBitmap(image_bitmap);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if(requestCode == SEARCH_LOCATION_ACTIVITY) {
            if (resultCode == RESULT_OK){
                String data = intent.getStringExtra("location");

                if(data != null){
                    btnSetLocation.setText(data);
                }
            }
        }
    }

    public String getImagePathToUri(Uri data) {
        //사용자가 선택한 이미지의 정보를 받아옴
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getActivity().managedQuery(data, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();

        //이미지의 경로 값
        String imgPath = cursor.getString(column_index);
        Log.d("test", imgPath);

        //이미지의 이름 값
        String imgName = imgPath.substring(imgPath.lastIndexOf("/") + 1);
        // Toast.makeText(CameraActivity.this, "이미지 이름 : " + imgName, Toast.LENGTH_SHORT).show();
        this.imageName = imgName;

        return imgPath;
    }//end of getImagePathToUri()
}