package com.foo.diary_android.diaryMe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.foo.diary_android.MainActivity;
import com.foo.diary_android.OnSingleClickListener;
import com.foo.diary_android.R;
import com.foo.diary_android.diary.DiaryActivity;
import com.foo.diary_android.emoticon.EmoticonAdapter;
import com.foo.diary_android.emoticon.EmoticonData;
import com.foo.diary_android.register.InfoDialog;
import com.foo.diary_android.register.RegisterActivity;
import com.foo.diary_android.service.DiaryDto;
import com.foo.diary_android.service.DiaryModifyDto;
import com.foo.diary_android.service.EmoticonDto;
import com.foo.diary_android.service.MemberDto;
import com.foo.diary_android.service.Service;
import com.foo.diary_android.service.UserCalendarData;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class writeActivity extends AppCompatActivity {

    private SlidingUpPanelLayout slidingUpPanelLayout;

    private ImageButton backBtn;
    private TextView date;
    private ImageButton emoticonImg;
    private EditText title;
    private ImageButton writeBtn;
    private EditText content;
    private Button emoticonSelectBtn;
    private CheckBox publicChb;

    private int year;
    private int month;
    private int day;
    private Long diaryId;
    private InfoDialog infoDialog;

    private DiaryDto diaryDto;
    private MemberDto user;
    private UserCalendarData userCalendarData;
    private CalendarDay clickDate;
    private int selectEmoticonImg;
    private int SelectEmoticonIndex;
    private boolean selectControll;

    private RewardedAd mRewardedAd;

    RecyclerView mRecyclerView = null ;
    EmoticonAdapter mAdapter = null ;
    ArrayList<EmoticonData> mList = new ArrayList<EmoticonData>();

    private View.OnClickListener infoOkListener = new View.OnClickListener() {
        public void onClick(View v) {
            infoDialog.dismiss();
        }
    };

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(Service.BASE_RUL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    Service service = retrofit.create(Service.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write);

        slidingUpPanelLayout = findViewById(R.id.write_main_frame);

        backBtn = findViewById(R.id.look_btn_back);
        date = findViewById(R.id.look_date_text);
        emoticonImg = findViewById(R.id.look_emoticon);
        title = findViewById(R.id.look_diary_title);
        writeBtn = findViewById(R.id.me_look_btn_write);
        content = findViewById(R.id.look_diary_content);
        mRecyclerView = findViewById(R.id.write_emoticon_rc);
        emoticonSelectBtn = findViewById(R.id.write_btn_select);
        publicChb = findViewById(R.id.write_chb_public);


        emoticonImg.setOnClickListener(l->{
            emoticonSelectBtn.setVisibility(View.INVISIBLE);
            slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
        });

        slidingUpPanelLayout.setFadeOnClickListener(l->{ // 투명한 뒷부분 클릭시 작동하는 메서드
            slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN); // 페널 상태 설정 메서드
        });


        mAdapter = new EmoticonAdapter(mList);
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.setLayoutManager(new GridLayoutManager(this,3));


        backBtn.setOnClickListener(l->{
            finish();
            overridePendingTransition(R.anim.anim_slide_out_left,R.anim.anim_slide_out_right);
        });

        emoticonSelectBtn.setOnClickListener(l->{
            emoticonImg.setImageResource(selectEmoticonImg);
            for(int i=1;i<10;i++){
                if(MainActivity.emoticonStore.get(i)==selectEmoticonImg){
                    SelectEmoticonIndex=i;
                    break;
                }
            }
            selectControll=true;
            slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
        });


        mAdapter.setOnItemClickListener(new EmoticonAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                selectControll=false;
                selectEmoticonImg = mList.get(position).getEmoticonImage();
                SelectEmoticonIndex=mList.get(position).getEmoticonNum();
                emoticonSelectBtn.setVisibility(View.VISIBLE);
                mAdapter.notifyItemRangeChanged(0,position);
                mAdapter.notifyItemRangeChanged(position+1,10);
            }
        });

        user = (MemberDto) getIntent().getSerializableExtra("user");
        userCalendarData = (UserCalendarData) getIntent().getSerializableExtra("userCalendarData");
        year = getIntent().getIntExtra("year",-1);
        month = getIntent().getIntExtra("month",-1);
        day = getIntent().getIntExtra("day",-1);
        RcDataInit();

        if(userCalendarData != null){ //읽기가 있는 경우
            service.findDiary(userCalendarData.getDiaryId()).enqueue(new Callback<DiaryDto>() {
                @Override
                public void onResponse(Call<DiaryDto> call, Response<DiaryDto> response) {
                    content.setText(response.body().getContent());
                    title.setText(response.body().getTitle());
                    publicChb.setChecked(!response.body().isOpen());
                    clickDate = userCalendarData.transDate(userCalendarData.getDate());
                    dataInit(clickDate.getYear(),clickDate.getMonth(),clickDate.getDay()
                            ,userCalendarData.getTitle(),content.getText().toString()// 통신으로 일기 가져옴
                            ,MainActivity.emoticonStore.get(userCalendarData.getEmoticonId()),userCalendarData.getEmoticonId());
                }
                @Override
                public void onFailure(Call<DiaryDto> call, Throwable t) {
                    t.printStackTrace();
                }
            });
        }
        else{ // 없는 경우
            this.date.setText(String.valueOf(year)+"년"+String.valueOf(month)+"월"+String.valueOf(day)+"일");
        }

        writeBtn.setOnClickListener(new OnSingleClickListener(){
            @Override
            public void onSingleClick(View v){
                Intent intent = new Intent(getApplicationContext(),MeLookActivity.class);
                intent.putExtra("user",user);
                if(userCalendarData!=null){ // 일기가 있는 경우 수정 통신을 넣어줌
                    service.updateDiary(new DiaryModifyDto(userCalendarData.getDiaryId(),!publicChb.isChecked(),
                            title.getText().toString(),content.getText().toString(),SelectEmoticonIndex),MainActivity.accessToken,user.getId())
                            .enqueue(new Callback<ResponseBody>() {
                                @Override
                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                    ;
                                }

                                @Override
                                public void onFailure(Call<ResponseBody> call, Throwable t) {
                                    t.printStackTrace();
                                }
                            });
                    CalendarDay tmp = userCalendarData.transDate(userCalendarData.getDate());
                    intent.putExtra("year",tmp.getYear());
                    intent.putExtra("month",tmp.getMonth());
                    intent.putExtra("day",tmp.getDay());
                    userCalendarData = new UserCalendarData(userCalendarData.getDiaryId(),userCalendarData.getDate(),
                            SelectEmoticonIndex,title.getText().toString(),!publicChb.isChecked());
                    intent.putExtra("userCalendarData",userCalendarData);
                    intent.addFlags(intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_in_left);
                    finish();
                }
                else{ // 없는 경우
                    if(selectControll){
                        Date clickDate = Date.valueOf(String.valueOf(year)+"-"
                                +String.valueOf(month)+"-"
                                +String.valueOf(day));
                        String dtoDate = String.valueOf(year)+"-"+String.valueOf(month)+"-"+String.valueOf(day);
                        diaryDto = new DiaryDto(!publicChb.isChecked(),title.getText().toString(),
                                content.getText().toString(),dtoDate,SelectEmoticonIndex,user.getId());
                        service.addDiary(diaryDto,MainActivity.accessToken).enqueue(new Callback<Long>() {
                            @Override
                            public void onResponse(Call<Long> call, Response<Long> response) {
                                userCalendarData = new UserCalendarData(response.body(),clickDate
                                        ,SelectEmoticonIndex,String.valueOf(title.getText()),!publicChb.isChecked()); // 통신할때 바꿔줘야함
                                intent.putExtra("year",year);
                                intent.putExtra("month",year);
                                intent.putExtra("day",day);
                                intent.putExtra("userCalendarData",userCalendarData);
                                intent.addFlags(intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_in_left);
                                finish();
                            }
                            @Override
                            public void onFailure(Call<Long> call, Throwable t) {
                                t.printStackTrace();
                            }
                        });
                    }
                    else{
                        infoDialog = new InfoDialog(writeActivity.this,infoOkListener,
                                "이모티콘을 선택해주세요");
                        infoDialog.show();
                    }
                }
            }
        });
    }

    public void dataInit(int year,int month, int day, String title, String content, int emoImg, int emoIndex){
        this.date.setText(String.valueOf(year)+"년 "+String.valueOf(month)+"월 "+String.valueOf(day)+"일");
        this.title.setText(title);
        this.content.setText(content);
        this.emoticonImg.setImageResource(emoImg);
        this.selectEmoticonImg=emoImg;
        this.SelectEmoticonIndex = emoIndex;
    }

    public void RcDataInit() {// 통신으로도 한번 해보기
        service.getEmoticonAllList(user.getId(),MainActivity.accessToken).enqueue(new Callback<List<EmoticonDto>>() {
            @Override
            public void onResponse(Call<List<EmoticonDto>> call, Response<List<EmoticonDto>> response) {
                List<EmoticonDto> emoticonDtoList = response.body();
                for (EmoticonDto emoticonDto : emoticonDtoList) {
                    mAdapter.addItem(emoticonDto.getId(), emoticonDto.getName()
                            , MainActivity.emoticonStore.get(emoticonDto.getId()), emoticonDto.getDescription());
                }
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<EmoticonDto>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}