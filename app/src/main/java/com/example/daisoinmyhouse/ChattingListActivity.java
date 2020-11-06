package com.example.daisoinmyhouse;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
//import android.support.v7.app.AlertDialog;
//import android.support.v7.app.AppCompatActivity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class ChattingListActivity extends AppCompatActivity {


    private ListView listView;
    private Button btn_create;

    private ArrayAdapter<String> arrayAdapter;
    private ArrayList<String> arr_roomList = new ArrayList<>();
    private DatabaseReference reference = FirebaseDatabase.getInstance().getReference("chat_list");
    private String name;
    String getNewRoomName;

    private String yourName;

    Map<String, Object> map = new HashMap<String, Object>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);

        //MainChatActivity에서 닉네임을 가져옵니다.
        Intent intent = getIntent();

        listView = (ListView) findViewById(R.id.list);

        // 채팅방 리스트를 보여줍니다
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arr_roomList);

        listView.setAdapter(arrayAdapter);

        // 다이얼로그에서 채팅방 이름을 적어서 채팅방을 생성합니다
        btn_create = (Button) findViewById(R.id.btn_create);
        btn_create.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                final EditText et_inDialog = new EditText(ChattingListActivity.this);

                final AlertDialog.Builder builder = new AlertDialog.Builder(ChattingListActivity.this);
                builder.setTitle("채팅방 이름 입력");
                builder.setView(et_inDialog);
                builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        yourName = et_inDialog.getText().toString();
                        map.put(StaticUserInformation.nickName + ">" + yourName, StaticUserInformation.nickName + ">" + yourName);
                        reference.updateChildren(map);
                    }
                });
                builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                builder.show();
            }
        });

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

                while (i.hasNext()) {
//                    set.add(((DataSnapshot) i.next()).getKey());
                     getNewRoomName = ((DataSnapshot) i.next()).getKey();

                    // >를 기준으로 문자열을 추출할 것이다.
                    // 먼저 >의 인덱스를 찾는다.
                    int idx = getNewRoomName.indexOf(">");

                    // > 앞부분을 추출
                    // substring은 첫번째 지정한 인덱스는 포함하지 않는다.
                    String firstName = getNewRoomName.substring(0, idx);

                    // 뒷부분을 추출
                    // 아래 substring은 @ 바로 뒷부분인 n부터 추출된다.
                    String lastName = getNewRoomName.substring(idx+1);

                    try{

                        if(StaticUserInformation.nickName.equals(firstName))
                        {
                            set.add(lastName);
                        }
                        else if(StaticUserInformation.nickName.equals(lastName))
                        {
                            set.add(firstName);
                        }

                        StaticUserInformation.roomSet.add(getNewRoomName);
                    }
                    catch(Exception e){
                        System.out.println("예외발생함");
                        e.printStackTrace();
                    }

                }

                arr_roomList.clear();
                arr_roomList.addAll(set);

                arrayAdapter.notifyDataSetChanged();
            }

            @Override public void onCancelled(DatabaseError databaseError) {

            }
        });




        // 리스트뷰의 채팅방을 클릭했을 때 반응
        // 채팅방의 이름과 입장하는 유저의 이름을 전달
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, final View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), ChattingActivity.class);
                System.out.println("@@@@@@@@@@@@@@@"+ getNewRoomName);
                intent.putExtra("your_name", ((TextView) view).getText().toString());
                intent.putExtra("room_name", getNewRoomName);
                startActivity(intent);
            }
        });


    }
    void loadData(){
        SharedPreferences preferences=getSharedPreferences("account",MODE_PRIVATE);
        StaticUserInformation.nickName=preferences.getString("nickName", null);
        StaticUserInformation.porfileUrl =preferences.getString("profileUrl", null);


    }
}