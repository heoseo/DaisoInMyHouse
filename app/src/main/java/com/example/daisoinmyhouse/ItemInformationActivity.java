package com.example.daisoinmyhouse;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
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
import java.util.Map;

public class ItemInformationActivity extends AppCompatActivity {

    ImageView ivShare, ivNoWish, ivWish;
    FrameLayout btn_wish;

    int imageIndex;

    // 1028 코드추가(HomeFragment에서 아이템 클릭시 전달한 해당 상품ID 가져옴)
    String productID;
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iteminformation);

        productID = getIntent().getExtras().get("productID").toString();

        // 찜(아이콘) 누르면 찜되기
        imageIndex = 0;
        ivNoWish = (ImageView) findViewById(R.id.imageview_nowish);
        ivWish = (ImageView) findViewById(R.id.imageview_wish);
        ivWish.setVisibility(View.INVISIBLE);
        btn_wish = (FrameLayout) findViewById(R.id.fl_wish);
        btn_wish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {

                    changeImage();

                } catch (Exception e) {
                    Log.i("DBtest", ".....ERROR.....!");
                }
            }
        });

        // 공유하기 이미지뷰(아이콘) 누르면 카톡공유.
        ivShare = (ImageView)findViewById(R.id.imageview_share);
        ivShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "선택된 상품ID : " + productID, Toast.LENGTH_LONG).show();
                kakaolink();
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

    public void changeImage(){
        if(imageIndex == 0){
            ivNoWish.setVisibility(View.INVISIBLE);
            ivWish.setVisibility(View.VISIBLE);

            imageIndex = 1;
            try {
                SharedPreferences preferences = getSharedPreferences("account",MODE_PRIVATE);
                StaticUserInformation.nickName=preferences.getString("nickName", null);
                StaticUserInformation.porfileUrl=preferences.getString("profileUrl", null);

                String userID = StaticUserInformation.userID; // !!!! <- 로그인되면 나중에 userID로 고치기!!!

                AddWishListActivity task = new AddWishListActivity();
                String result = task.execute(userID, productID).get();

                Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();


            } catch (Exception e) {
                Log.i("DBtest", ".....ERROR.....!");
            }

        }else if(imageIndex == 1){
            ivNoWish.setVisibility(View.VISIBLE);
            ivWish.setVisibility(View.INVISIBLE);

            imageIndex = 0;
        }
    }
}