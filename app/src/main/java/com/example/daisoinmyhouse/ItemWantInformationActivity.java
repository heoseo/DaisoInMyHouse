package com.example.daisoinmyhouse;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;

public class ItemWantInformationActivity extends AppCompatActivity {

    ImageView ivShare, ivWish;
    TextView tvProduct_name, tvProduct_content, tvNickname, tvLocation, tvCategory;

    String product_no, nickname, user_id, product_name;
    String myID = "";
    String myNickName="";
    String yourName;

    ImageButton btn_back;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_want_information);

        product_no = getIntent().getExtras().get("want_no").toString();
        product_name = getIntent().getExtras().get("want_name").toString();
        String product_content = getIntent().getExtras().get("want_content").toString();
        String location = getIntent().getExtras().get("location").toString();
        String category = getIntent().getExtras().get("want_cate").toString();
        user_id = getIntent().getExtras().get("user_id").toString();    // 상품판매자ID

        tvProduct_name = findViewById(R.id.tv_item_name);
        tvProduct_content = findViewById(R.id.tv_item_detail);
        tvNickname = findViewById(R.id.tv_nickname);
        tvLocation=findViewById(R.id.tv_location);

        // 상품 판매자 닉네임
        GetNickname getNickname = new GetNickname();
        try {
            nickname = getNickname.execute(user_id).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        tvProduct_name.setText(product_name);
        tvProduct_content.setText(product_content);
        tvNickname.setText(nickname);
        tvLocation.setText(location);

        SharedPreferences preferences = getSharedPreferences("account",MODE_PRIVATE);
        StaticUserInformation.userID=preferences.getString("userID", null);
        myID = StaticUserInformation.userID; // !!!! <- 로그인되면 나중에 userID로 고치기!!!


//        // 공유하기 이미지뷰(아이콘) 누르면 카톡공유.
//        ivShare = (ImageView)findViewById(R.id.imageview_share);
//        ivShare.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(getApplicationContext(), "선택된 상품ID : " + product_no, Toast.LENGTH_LONG).show();
//                kakaolink();
//            }
//        });

        //뒤로가기 버튼
        btn_back = (ImageButton) findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                finish();
            }
        });


        // 채팅하기 버튼
        Button btnChatting = (Button)findViewById(R.id.btn_chat);
        btnChatting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String getNewRoomName;
                SharedPreferences preferences=getSharedPreferences("account",MODE_PRIVATE);
                StaticUserInformation.nickName=preferences.getString("nickName", null);



                // 특정 경로의 전체 내용에 대한 변경 사항을 읽고 수신 대기함
                // onDataChange는 Database가 변경되었을때 호출되고
                // onCancelled는 취소됬을때 호출됩니다
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("chat_list");
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        System.out.println(3);

                        Set<String> set = new HashSet<String>();
                        Iterator i = dataSnapshot.getChildren().iterator();
                        String findRoomName = null;

                        while (i.hasNext()) {
                            Log.i("테스트", "while문 들어옴");
//                    set.add(((DataSnapshot) i.next()).getKey());
                            findRoomName = ((DataSnapshot) i.next()).getKey();

                            // >를 기준으로 문자열을 추출할 것이다.
                            // 먼저 >의 인덱스를 찾는다.
                            int idx = findRoomName.indexOf(">");

                            // > 앞부분을 추출
                            // substring은 첫번째 지정한 인덱스는 포함하지 않는다.
                            String firstName = findRoomName.substring(0, idx);

                            // 뒷부분을 추출
                            // 아래 substring은 @ 바로 뒷부분인 n부터 추출된다.
                            String lastName = findRoomName.substring(idx+1);

                            try{
                                if(StaticUserInformation.nickName.equals(firstName))
                                {
                                    findRoomName = StaticUserInformation.nickName +">"+ nickname;
                                }
                                else if(StaticUserInformation.nickName.equals(lastName))
                                {
                                    findRoomName = nickname +">"+StaticUserInformation.nickName;
                                }


                                Log.i("want채팅테스트", "findRoomName:"+findRoomName);
                                StaticUserInformation.roomSet.add(findRoomName);
                            }
                            catch(Exception e){
                                e.printStackTrace();
                            }
                        }



                    }
                    @Override public void onCancelled(DatabaseError databaseError) {
                    }
                });


                Intent intent = new Intent(getApplicationContext(), ChattingActivity.class);
                intent.putExtra("your_name", nickname);
                startActivity(intent);
            }
        });
    }

//    // 카카오톡 공유
//    public void kakaolink() {
//        FeedTemplate params = FeedTemplate
//                .newBuilder(ContentObject.newBuilder(product_name,
//                        "http://mud-kage.kakao.co.kr/dn/NTmhS/btqfEUdFAUf/FjKzkZsnoeE4o19klTOVI1/openlink_640x640s.jpg",
//                        LinkObject.newBuilder().setWebUrl("https://developers.kakao.com")
//                                .setMobileWebUrl("https://developers.kakao.com").build())
//                        .build())
//                .addButton(new ButtonObject("앱에서 보기", LinkObject.newBuilder()
//                        .setWebUrl("'https://developers.kakao.com")
//                        .setMobileWebUrl("'https://developers.kakao.com")
//                        .setAndroidExecutionParams("key1=value1")
//                        .setIosExecutionParams("key1=value1")
//                        .build()))
//                .build();
//
//        Map<String, String> serverCallbackArgs = new HashMap<String, String>();
//        serverCallbackArgs.put("user_id", "${current_user_id}");
//        serverCallbackArgs.put("product_id", "${shared_product_id}");
//
//        KakaoLinkService.getInstance().sendDefault(getApplicationContext(), params, serverCallbackArgs, new ResponseCallback<KakaoLinkResponse>() {
//            @Override
//            public void onFailure(ErrorResult errorResult) {
//                Logger.e(errorResult.toString());
//            }
//
//            @Override
//            public void onSuccess(KakaoLinkResponse result) { }
//        });
//    }
}
