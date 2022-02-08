package com.foo.diary_android.friend;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.foo.diary_android.MainActivity;
import com.foo.diary_android.OnSingleClickListener;
import com.foo.diary_android.R;
import com.foo.diary_android.diary.DiaryActivity;
import com.foo.diary_android.diaryMe.MeDiaryActivity;
import com.foo.diary_android.service.MemberDto;
import com.foo.diary_android.service.Service;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FriendActivity extends AppCompatActivity {

    RecyclerView mRecyclerView = null ;
    FriendAdapter mAdapter = null ;
    ArrayList<FriendData> mList = new ArrayList<FriendData>();

    private ImageButton backBtn;
    private ImageButton addBtn;
    private ImageButton delBtn;
    private ImageView nothingImg;

    private MemberDto user;

    private FriendAddDialog addDialog;
    private FriendDelDialog delDialog;

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(Service.BASE_RUL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    Service service = retrofit.create(Service.class);



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend);

        user = (MemberDto) getIntent().getSerializableExtra("user");

        backBtn = findViewById(R.id.friend_btn_back);
        addBtn = findViewById(R.id.friend_btn_add);
        delBtn = findViewById(R.id.friend_btn_del);
        nothingImg = findViewById(R.id.friend_nothing);

        mRecyclerView = findViewById(R.id.friend_rc);

        // 리사이클러뷰에 SimpleTextAdapter 객체 지정.
        mAdapter = new FriendAdapter(mList);
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        dataInit();

        addBtn.setOnClickListener(new OnSingleClickListener(){
            @Override
            public void onSingleClick(View v) {
                addDialog = new FriendAddDialog(FriendActivity.this,user);
                addDialog.show();
            }
        });

        delBtn.setOnClickListener(new OnSingleClickListener(){
            @Override
            public void onSingleClick(View v) {
                delDialog = new FriendDelDialog(FriendActivity.this,user);
                delDialog.show();
            }
        });

        backBtn.setOnClickListener(l->{
            finish();
            overridePendingTransition(R.anim.anim_slide_out_left,R.anim.anim_slide_out_right);
        });

        mAdapter.setOnItemClickListener(new FriendAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                Intent intent = new Intent(getApplicationContext(), DiaryActivity.class);
                intent.putExtra("user",user);
                intent.putExtra("friendId",mList.get(position).getFriendId());
                startActivity(intent);
                overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_in_left);
            }
        });
    }

    public void dataInit(){
        service.getFriendList(user.getId(),MainActivity.accessToken).enqueue(new Callback<List<MemberDto>>() {
            @Override
            public void onResponse(Call<List<MemberDto>> call, Response<List<MemberDto>> response) {
                List<MemberDto> memberDtoList = response.body();
                if(memberDtoList.size()==0){
                    nothingImg.setVisibility(View.VISIBLE);
                }
                else{
                    nothingImg.setVisibility(View.INVISIBLE);
                    for(MemberDto memberDto : memberDtoList){
                        mAdapter.addItem(memberDto.getId(), MainActivity.characterStore.get(memberDto.getCategoryId()),
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