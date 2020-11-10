package com.example.daisoinmyhouse;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Date;
import java.util.concurrent.ExecutionException;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyPageFragment extends Fragment {

    private static final int MODE_PRIVATE = 0;
    CircleImageView imgViewProfile;
    ImageButton btnSetting;
    ImageButton btnArrow;
    LinearLayout btnSettingMyArea;
    LinearLayout btnProfile;
    RelativeLayout btnKeyword;
    Button btnLogin;
    Button btnLogout;
    RelativeLayout btnTransaction;
    TextView tvNickName, tvWishlist;
    RelativeLayout btnWishlist;
    SharedPreferences preferences;

        Bitmap bitmap;
    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
//        ViewGroup rootView = null;

        final ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_mypage, container, false);
        preferences = this.getActivity().getSharedPreferences("account",MODE_PRIVATE);

        // [ fragment 에서 버튼 누르면 새 activity 띄우기 ]


        // 설정 버튼 -> SettingActivity 띄우기
        btnSetting = rootView.findViewById(R.id.btn_setting);
        btnSetting.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), SettingActivity.class);
                getContext().startActivity(intent);
            }
        });

        imgViewProfile = rootView.findViewById(R.id.imageview_profile);
        tvNickName = rootView.findViewById(R.id.tv_nickname);

        SharedPreferences preferences=getActivity().getSharedPreferences("account",MODE_PRIVATE);
        StaticUserInformation.loadData(preferences);

        if(StaticUserInformation.nickName != null){
            tvNickName.setText(StaticUserInformation.nickName);
            Picasso.get().load( StaticUserInformation.porfileUrl).into(imgViewProfile);
        }

        tvWishlist = rootView.findViewById(R.id.tv_wishlist);
        if(preferences.getString("cntWishList", null)==null){
            StaticUserInformation.cntWishList = "0";
        }
        else StaticUserInformation.cntWishList = preferences.getString("cntWishList", null);
        tvWishlist.setText(StaticUserInformation.cntWishList);

        // 로그아웃, 수정하기, 공유하기 팝업띄우기
        RelativeLayout btnPopUp = rootView.findViewById(R.id.ll_profile);
        btnPopUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                builder.setTitle("프로필");

                builder.setItems(R.array.menu_profile_popup, new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int pos)
                    {
                        String[] items = getResources().getStringArray(R.array.menu_profile_popup);
                        String str = items[pos];

                        switch (str){
                            case "수정하기":
                                Intent intent = new Intent(getContext(), ProfileEditActivity.class);
                                getContext().startActivity(intent);
                                break;
                            case "공유하기":
                                break;
                            case "로그아웃":

                                SharedPreferences preferences= getActivity().getSharedPreferences("account",MODE_PRIVATE);
                                StaticUserInformation.resetDate(preferences);

                                MyPageLogOutFragment myPageLogOutFragment = new MyPageLogOutFragment();

                                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

//                                fragmentTransaction.remove(MyPageFragment.this).commit();
                                fragmentTransaction.replace(R.id.frame_layout, myPageLogOutFragment).commitAllowingStateLoss();
                                break;

                        }
                    }
                });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });


        // 키워드 알림 레이아웃 누르면 -> KeywordAlarmActivity 띄우기
        btnKeyword = rootView.findViewById(R.id.ll_keword_notice);
        btnKeyword.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(getContext(), KeywordAlarmActivity.class);
                getContext().startActivity(intent);
            }
        });


        // 거래내역
        btnTransaction = rootView.findViewById(R.id.ll_transaction_details);
        btnTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), RentActivity.class);
                getContext().startActivity(intent);
            }
        });



        //찜한 목록 띄우기
        btnWishlist = rootView.findViewById((R.id.ll_wishlist));
        btnWishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), WishListActivity.class);
                getContext().startActivity(intent);
            }
        });

        return rootView;
    }

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        //prepareData();
        SharedPreferences preferences = getActivity().getSharedPreferences("account",MODE_PRIVATE);
        StaticUserInformation.userID =preferences.getString("userID", null);


        GetCntWishList getCntWishList = new GetCntWishList();
        String cntWishList = null;    // 성공하면 닉네임 반환
        try {
            cntWishList = getCntWishList.execute(StaticUserInformation.userID).get();
            Log.i("cntWishListTest", "get : "+cntWishList);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        SharedPreferences.Editor editor=preferences.edit();
        editor.putString("cntWishList", cntWishList );

        editor.apply();
        editor.commit();
        StaticUserInformation.cntWishList = preferences.getString("cntWishList", null);

        Log.i("cntWishListTest", "getStatic : "+StaticUserInformation.cntWishList);


    }

    public class GetCntWishList extends AsyncTask<String, Void, String> {
        String sendMsg, receiveMsg;
        @Override
        protected String doInBackground(String... strings) {
            try {
                String str;
                // 접속할 서버 주소 (이클립스에서 android.jsp 실행시 웹브라우저 주소)
                URL url = new URL("http://daisoinmyhouse.cafe24.com/wishlistCount.jsp");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestMethod("POST");
                OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
                // 전송할 데이터. GET 방식으로 작성
                sendMsg = "user_id=" + strings[0] ;
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
                        System.out.println("cnt : " + str);
                    }
                    receiveMsg = buffer.toString();
                    Log.i("cntWishListTest", receiveMsg);
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







}