package com.example.daisoinmyhouse;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
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
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
    String image, imagestr;

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

        Bitmap bitmap = ((BitmapDrawable)btn_photo.getDrawable()).getBitmap();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.setHasAlpha(true);
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] imageBytes = byteArrayOutputStream.toByteArray();
        image = Base64.encodeToString(imageBytes, Base64.DEFAULT);

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
                    String location = btnSetLocation.getText().toString();
                    String product_img = imageName;

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
                    }
                }  catch (Exception e) {
                    Log.i("DBtest", ".....ERROR.....!");
                }
            }
        });

        return rootView;
    }


    public class getImg extends AsyncTask<String, Void, String> {
        String sendMsg, receiveMsg;
        @Override
        protected String doInBackground(String... strings) {
            try {
                String str;
                // 접속할 서버 주소 (이클립스에서 android.jsp 실행시 웹브라우저 주소)
                URL url = new URL("http://daisoinmyhouse.cafe24.com/getImg.jsp");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestMethod("POST");

                //jsp와 통신 성공 시 수행
                if (conn.getResponseCode() == conn.HTTP_OK) {
                    InputStreamReader tmp = new InputStreamReader(conn.getInputStream(), "UTF-8");
                    BufferedReader reader = new BufferedReader(tmp);
                    StringBuffer buffer = new StringBuffer();
                    // jsp에서 보낸 값을 받는 부분
                    while ((str = reader.readLine()) != null) {
                        buffer.append(str);
                    }
                    receiveMsg = buffer.toString();
                    Log.i("테스트", receiveMsg);
                } else {
                    // 통신 실패
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            //jsp로부터 받은 리턴 값
            return receiveMsg;
        }

    }

    public class imgTest extends AsyncTask<String, Void, String> {
        String sendMsg, receiveMsg;
        @Override
        protected String doInBackground(String... strings) {
            try {
                String str;
                // 접속할 서버 주소 (이클립스에서 android.jsp 실행시 웹브라우저 주소)
                URL url = new URL("http://daisoinmyhouse.cafe24.com/imageTest.jsp");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestMethod("POST");
                OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
                // 전송할 데이터. GET 방식으로 작성
                sendMsg = "img=" + strings[0];
                osw.write(sendMsg);
                osw.flush();
                //jsp와 통신 성공 시 수행
                if (conn.getResponseCode() == conn.HTTP_OK) {
                    InputStreamReader tmp = new InputStreamReader(conn.getInputStream(), "UTF-8");
                    BufferedReader reader = new BufferedReader(tmp);
                    StringBuffer buffer = new StringBuffer();
                    // jsp에서 보낸 값을 받는 부분
                    while ((str = reader.readLine()) != null) {
                        buffer.append(str);
                    }
                    receiveMsg = buffer.toString();
                    Log.i("테스트", receiveMsg);
                } else {
                    // 통신 실패
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            //jsp로부터 받은 리턴 값
            return receiveMsg;
        }

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

                int height = image_bitmap.getHeight();
                int width  = image_bitmap.getWidth();

                //image_bitmap 으로 받아온 이미지의 사이즈를 임의적으로 조절함. width: 400 , height: 300
                image_bitmap_copy = Bitmap.createScaledBitmap(image_bitmap, 400, 400, true);
                // ImageView image = (ImageView) findViewById(R.id.imageView);  //이미지를 띄울 위젯 ID값
                btn_photo.setImageBitmap(image_bitmap_copy);

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


    /* image폴더에 있는 이미지 띄우기
        btn_photo = rootView.findViewById(R.id.imageView_photo);
        Thread uThread = new Thread() {
            @Override
            public void run() {
                try {
                    //서버에 올려둔 이미지 URL
                    URL url = new URL("http://daisoinmyhouse.cafe24.com/images/%EC%9E%AC%ED%95%99%EC%A6%9D%EB%AA%85%EC%84%9C_%EC%B2%9C%EA%B3%A0%EB%A7%88%EB%B9%84_%EA%B9%80%ED%98%9C%EB%AF%BC.jpg");
                    //Web에서 이미지 가져온 후 ImageView에 지정할 Bitmap 만들기
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setDoInput(true); //Server 통신에서 입력 가능한 상태로 만듦
                    conn.connect(); //연결된 곳에 접속할 때 (connect() 호출해야 실제 통신 가능함)
                    InputStream is = conn.getInputStream(); //inputStream 값 가져오기
                    bitmap = BitmapFactory.decodeStream(is); // Bitmap으로 반환
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        uThread.start(); // 작업 Thread 실행
        try {
            //메인 Thread는 별도의 작업을 완료할 때까지 대기한다!
            //join() 호출하여 별도의 작업 Thread가 종료될 때까지 메인 Thread가 기다림
            //join() 메서드는 InterruptedException을 발생시킨다.
            uThread.join();
            //작업 Thread에서 이미지를 불러오는 작업을 완료한 뒤
            //UI 작업을 할 수 있는 메인 Thread에서 ImageView에 이미지 지정
            btn_photo.setImageBitmap(bitmap);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        */
}