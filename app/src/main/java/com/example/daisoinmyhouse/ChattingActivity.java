package com.example.daisoinmyhouse;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;

public class ChattingActivity extends AppCompatActivity {

    String yourName;
    String roomName;

    EditText et;
    ListView listView;

    ArrayList<ChattingMessageItem> chattingMessageItems =new ArrayList<>();
    ChattingAdapter adapter;

    //Firebase Database 관리 객체참조변수
    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference;

    //'chat'노드의 참조객체 참조변수
    DatabaseReference chatRef;
    DatabaseReference chatRef2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        //제목줄 제목글시를 닉네임으로(또는 채팅방)
//        getSupportActionBar().setTitle(G.nickName);


        yourName = getIntent().getExtras().get("your_name").toString();

        firebaseDatabase= FirebaseDatabase.getInstance();


        Iterator itr = StaticUserInformation.roomSet.iterator();
        while(itr.hasNext()) {
            String get = (String)itr.next();

            if(get.equals(StaticUserInformation.nickName +">"+ yourName))
                roomName = StaticUserInformation.nickName +">"+ yourName;
            else if(get.equals(yourName +">"+StaticUserInformation.nickName))
                roomName = yourName +">"+StaticUserInformation.nickName;
        }

        chatRef= firebaseDatabase.getReference().child("chat_list").child(roomName);

        et=findViewById(R.id.et);
        listView=findViewById(R.id.listview);
        adapter=new ChattingAdapter(chattingMessageItems,getLayoutInflater());
        listView.setAdapter(adapter);

        // 리스트뷰가 갱신될때 하단으로 자동 스크롤
        listView.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);

        //Firebase DB관리 객체와 'caht'노드 참조객체 얻어오기
        System.out.println("!!!!!!!!!!!roomname은 " + roomName + "임");
        System.out.println("roomName: " + roomName);




        //firebaseDB에서 채팅 메세지들 실시간 읽어오기..
        //'chat'노드에 저장되어 있는 데이터들을 읽어오기
        //chatRef에 데이터가 변경되는 것으 듣는 리스너 추가
        chatRef.addChildEventListener(new ChildEventListener() {
            //새로 추가된 것만 줌 ValueListener는 하나의 값만 바뀌어도 처음부터 다시 값을 줌
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                //새로 추가된 데이터(값 : MessageItem객체) 가져오기
                ChattingMessageItem chattingMessageItem = dataSnapshot.getValue(ChattingMessageItem.class);

                //새로운 메세지를 리스뷰에 추가하기 위해 ArrayList에 추가
                chattingMessageItems.add(chattingMessageItem);

                //리스트뷰를 갱신
                adapter.notifyDataSetChanged();
                listView.setSelection(chattingMessageItems.size()-1); //리스트뷰의 마지막 위치로 스크롤 위치 이동
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }



    public void clickSend(View view) {

        //firebase DB에 저장할 값들( 닉네임, 메세지, 프로필 이미지URL, 시간)
        String nickName= StaticUserInformation.nickName;
        String message= et.getText().toString();
        String pofileUrl= StaticUserInformation.porfileUrl;



        // 시간, 날짜 관련
        Calendar calendar= Calendar.getInstance(); //현재 시간을 가지고 있는 객체


        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int date = calendar.get(Calendar.DATE);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);



        String korDayOfWeek = "";
        switch (dayOfWeek) {
            case 1:
                korDayOfWeek = "일요일";
                break;
            case 2:
                korDayOfWeek = "월요일";
                break;
            case 3:
                korDayOfWeek = "화요일";
                break;
            case 4:
                korDayOfWeek = "수요일";
                break;
            case 5:
                korDayOfWeek = "목요일";
                break;
            case 6:
                korDayOfWeek = "금요일";
                break;
            case 7:
                korDayOfWeek = "토요일";
                break;
        }

        //메세지 작성 시간 문자열로..

        String time=calendar.get(Calendar.HOUR_OF_DAY)+":"+calendar.get(Calendar.MINUTE); //14:16
        String todayDate = year + "." + month+"."+date+" " + korDayOfWeek;



        //firebase DB에 저장할 값(MessageItem객체) 설정
        ChattingMessageItem chattingMessageItem = new ChattingMessageItem(nickName,message,time,pofileUrl);
        //'roomName'노드에 MessageItem객체를 통해
        chatRef.push().setValue(chattingMessageItem);



        //EditText에 있는 글씨 지우기
        et.setText("");

        //소프트키패드를 안보이도록..
        InputMethodManager imm=(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);

        //처음 시작할때 EditText가 다른 뷰들보다 우선시 되어 포커스를 받아 버림.
        //즉, 시작부터 소프트 키패드가 올라와 있음.

        //그게 싫으면...다른 뷰가 포커스를 가지도록
        //즉, EditText를 감싼 Layout에게 포커스를 가지도록 속성을 추가!![[XML에]
    }
}


