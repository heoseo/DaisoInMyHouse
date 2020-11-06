package com.example.daisoinmyhouse;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.kakao.kakaolink.v2.KakaoLinkResponse;
import com.kakao.kakaolink.v2.KakaoLinkService;
import com.kakao.message.template.ButtonObject;
import com.kakao.message.template.ContentObject;
import com.kakao.message.template.FeedTemplate;
import com.kakao.message.template.LinkObject;
import com.kakao.message.template.SocialObject;
import com.kakao.network.ErrorResult;
import com.kakao.network.callback.ResponseCallback;
import com.kakao.util.helper.log.Logger;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import org.w3c.dom.Text;

public class ItemInformationActivity extends AppCompatActivity {

    ImageView ivShare, ivNoWish, ivWish;
    FrameLayout btn_wish;
    TextView tvProduct_name, tvProduct_price, tvProduct_content, tvNickname;

    String product_no, nickname;

    String yourName;
    // 1028 코드추가(HomeFragment에서 아이템 클릭시 전달한 해당 상품ID 가져옴)
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iteminformation);

        product_no = getIntent().getExtras().get("product_no").toString();
        String product_name = getIntent().getExtras().get("product_name").toString();
        String product_price = getIntent().getExtras().get("product_name").toString();
        String product_content = getIntent().getExtras().get("product_content").toString();
        String user_id = getIntent().getExtras().get("user_id").toString();

        tvProduct_name = (TextView)findViewById(R.id.tv_item_name);
        tvProduct_price = (TextView)findViewById(R.id.tv_item_price);
        tvProduct_content = (TextView)findViewById(R.id.tv_item_detail);
        tvNickname = (TextView)findViewById(R.id.tv_nickname);

        GetNickname getNickname = new GetNickname();
        try {
            nickname = getNickname.execute(user_id).get();
            Log.i("닉네임", nickname);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        tvProduct_name.setText(product_name);
        tvProduct_price.setText(product_price);
        tvProduct_content.setText(product_content);
        tvNickname.setText(nickname);

        // 찜(아이콘) 누르면 찜되기 & 찜해제
        ivWish = (ImageView)findViewById(R.id.imageview_wish);
        ivWish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String userID = "";
                SharedPreferences preferences = getSharedPreferences("account",MODE_PRIVATE);
                StaticUserInformation.userID=preferences.getString("userID", null);
                userID = StaticUserInformation.userID; // !!!! <- 로그인되면 나중에 userID로 고치기!!!

                Drawable tempImg = ivWish.getDrawable();
                Drawable tempRes = ItemInformationActivity.this.getResources().getDrawable(R.drawable.unlike);
                Bitmap tmpBitmap = ((BitmapDrawable)tempImg).getBitmap();
                Bitmap tmpBitmapRes = ((BitmapDrawable)tempRes).getBitmap();

                // unlike면 like로 바꿈.
                if(tmpBitmap.equals(tmpBitmapRes)) {
                    ivWish.setImageResource(R.drawable.like);
                    Toast.makeText(getApplicationContext(), "찜!!", Toast.LENGTH_LONG).show();
                    //로직 수행
                    AddWishListActivity task = new AddWishListActivity();
                    String result = null;     // 찜등록 execute?
                    try {
                        result = task.execute(userID, product_no).get();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
                }
                // like면 unlike로 바꿈.
                else{
                    ivWish.setImageResource(R.drawable.unlike);
                    Toast.makeText(getApplicationContext(), "찜해제.. ", Toast.LENGTH_LONG).show();
                    //로직 수행
                    RemoveWishListActivity task = new RemoveWishListActivity();
                }


            }
        });


        // 공유하기 이미지뷰(아이콘) 누르면 카톡공유.
        ivShare = (ImageView)findViewById(R.id.imageview_share);
        ivShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "선택된 상품ID : " + product_no, Toast.LENGTH_LONG).show();
                kakaolink();
            }
        });

        Button btnChatting = (Button)findViewById(R.id.btn_chat);
        btnChatting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String getNewRoomName;
                SharedPreferences preferences=getSharedPreferences("account",MODE_PRIVATE);
                StaticUserInformation.nickName=preferences.getString("nickName", null);

                if(StaticUserInformation.roomSet.contains(StaticUserInformation.nickName +">"+ yourName))
                    getNewRoomName = StaticUserInformation.nickName +">"+ yourName;
                else if(StaticUserInformation.roomSet.contains(yourName +">"+StaticUserInformation.nickName))
                    getNewRoomName = yourName +">"+StaticUserInformation.nickName;
                else{       // 저장된 채팅이름없음.

                }


                Intent intent = new Intent(getApplicationContext(), ChattingActivity.class);
//                System.out.println("@@@@@@@@@@@@@@@"+ getNewRoomName);
                intent.putExtra("your_name", ((TextView) view).getText().toString());
//                intent.putExtra("room_name", getNewRoomName);
                startActivity(intent);
            }
        });




    }


    // 카카오톡 공유
    public void kakaolink() {
        FeedTemplate params = FeedTemplate
                .newBuilder(ContentObject.newBuilder("디저트 사진",
                        "http://mud-kage.kakao.co.kr/dn/NTmhS/btqfEUdFAUf/FjKzkZsnoeE4o19klTOVI1/openlink_640x640s.jpg",
                        LinkObject.newBuilder().setWebUrl("https://developers.kakao.com")
                                .setMobileWebUrl("https://developers.kakao.com").build())
                        .setDescrption("아메리카노, 빵, 케익")
                        .build())
                .setSocial(SocialObject.newBuilder().setLikeCount(10).setCommentCount(20)
                        .setSharedCount(30).setViewCount(40).build())
                .addButton(new ButtonObject("앱에서 보기", LinkObject.newBuilder()
                        .setWebUrl("'https://developers.kakao.com")
                        .setMobileWebUrl("'https://developers.kakao.com")
                        .setAndroidExecutionParams("key1=value1")
                        .setIosExecutionParams("key1=value1")
                        .build()))
                .build();

        Map<String, String> serverCallbackArgs = new HashMap<String, String>();
        serverCallbackArgs.put("user_id", "${current_user_id}");
        serverCallbackArgs.put("product_id", "${shared_product_id}");

        KakaoLinkService.getInstance().sendDefault(this, params, serverCallbackArgs, new ResponseCallback<KakaoLinkResponse>() {
            @Override
            public void onFailure(ErrorResult errorResult) {
                Logger.e(errorResult.toString());
            }

            @Override
            public void onSuccess(KakaoLinkResponse result) { }
        });
    }

}