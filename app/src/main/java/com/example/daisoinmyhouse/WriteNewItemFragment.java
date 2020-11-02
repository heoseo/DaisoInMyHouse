package com.example.daisoinmyhouse;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.Objects;
import static android.app.Activity.RESULT_OK;

public class WriteNewItemFragment extends Fragment {

    private static final int REQUEST_AREA = 50;
    EditText product_Resister,pricce,conttent,taag;
    TextView cattegory;
    Button writeBtn;
    TextView btnSetLocation;
    TextView btn_back;
    ImageView btn_photo;
    MainActivity activity;
    Spinner spinner;

    private final int REQ_CODE_SELECT_IMAGE = 100;
    private String img_path = new String();
    private Bitmap image_bitmap_copy = null;
    private Bitmap image_bitmap = null;
    private String imageName = null;


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
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                cattegory.setText(parent.getItemAtPosition(position).toString());
                cattegory.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });




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



        // 지역설정
        btnSetLocation = rootView.findViewById(R.id.tv_location);
        if(StaticUserInformation.myArea != null){
            btnSetLocation.setText(StaticUserInformation.myArea);
        }
        btnSetLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(getContext(), SettingMyAreaActivity.class) ,0);

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


        if (requestCode == REQUEST_AREA) {
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