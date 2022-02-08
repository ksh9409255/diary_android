package com.foo.diary_android.mode;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.foo.diary_android.MainActivity;
import com.foo.diary_android.R;
import com.foo.diary_android.diaryMe.MeDiaryActivity;
import com.foo.diary_android.friend.FriendActivity;
import com.foo.diary_android.mypage.MypageActivity;
import com.foo.diary_android.service.MemberDto;
import com.foo.diary_android.service.Service;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ModeActivity extends AppCompatActivity {

    private ImageView charaterImg;
    private TextView nickName;
    private Button friendBtn;
    private Button diaryBtn;
    private Button mypageBtn;
    private ImageButton friendNewBtn;
    private ImageButton friendNewAlarmBtn;

    private DrawerLayout drawerLayout;

    private MemberDto user;

    RecyclerView mRecyclerView = null ;
    NewFriendAdapter mAdapter = null ;
    ArrayList<NewFriendData> mList = new ArrayList<NewFriendData>();

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(Service.BASE_RUL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    Service service = retrofit.create(Service.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mode);

        charaterImg = findViewById(R.id.mode_character);
        nickName = findViewById(R.id.mode_nickname);
        friendBtn = findViewById(R.id.mode_btn_friend);
        diaryBtn = findViewById(R.id.mode_btn_diary);
        mypageBtn = findViewById(R.id.mode_btn_mypage);
        friendNewBtn = findViewById(R.id.mode_btn_new_friend);
        drawerLayout = findViewById(R.id.mode_drager);
        friendNewAlarmBtn = findViewById(R.id.mode_btn_new_friend_alarm);

        user = (MemberDto) getIntent().getSerializableExtra("user");

        mRecyclerView = findViewById(R.id.mode_rc);

        // 리사이클러뷰에 SimpleTextAdapter 객체 지정.
        mAdapter = new NewFriendAdapter(mList);
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        dataInit();

        friendNewBtn.setOnClickListener(l->{
            drawerLayout.openDrawer(Gravity.RIGHT);
        });

        friendNewAlarmBtn.setOnClickListener(l->{
            drawerLayout.openDrawer(Gravity.RIGHT);
        });

        friendBtn.setOnClickListener(l->{
            Intent intent = new Intent(this, FriendActivity.class);
            intent.putExtra("user",user);
            startActivity(intent);
            overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_in_left);
        });

        diaryBtn.setOnClickListener(l->{
            Intent intent = new Intent(this, MeDiaryActivity.class);
            intent.putExtra("user",user);
            Log.e("유저",String.valueOf(user.getCategoryId()));
            startActivity(intent);
            overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_in_left);
        });

        mypageBtn.setOnClickListener(l->{
            Intent intent = new Intent(this, MypageActivity.class);
            intent.putExtra("user",user);
            startActivity(intent);
            overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_in_left);
        });

        nickName.setText(user.getNickName());
        charaterImg.setImageResource(MainActivity.characterStore.get(user.getCategoryId()));
    }

    private void dataInit() {
        service.getNewFriend(user.getId(),MainActivity.accessToken).enqueue(new Callback<List<MemberDto>>() {
            @Override
            public void onResponse(Call<List<MemberDto>> call, Response<List<MemberDto>> response) {
                List<MemberDto> memberDtoList = response.body();
                if(memberDtoList.size()==0){ // 신청 친구 없음
                    friendNewAlarmBtn.setVisibility(View.INVISIBLE);
                    friendNewBtn.setVisibility(View.VISIBLE);
                }
                else{
                    friendNewAlarmBtn.setVisibility(View.VISIBLE);
                    friendNewBtn.setVisibility(View.INVISIBLE);
                    for(MemberDto memberDto : memberDtoList){
                        mAdapter.addItem(user,memberDto.getId(),
                                MainActivity.characterStore.get(memberDto.getCategoryId()),
                                memberDto.getNickName());
                    }
                    mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<MemberDto>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

}