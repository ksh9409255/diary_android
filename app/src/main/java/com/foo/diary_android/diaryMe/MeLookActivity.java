package com.foo.diary_android.diaryMe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.foo.diary_android.MainActivity;
import com.foo.diary_android.OnSingleClickListener;
import com.foo.diary_android.R;
import com.foo.diary_android.diary.DiaryActivity;
import com.foo.diary_android.service.DiaryDto;
import com.foo.diary_android.service.MemberDto;
import com.foo.diary_android.service.Service;
import com.foo.diary_android.service.UserCalendarData;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.prolificinteractive.materialcalendarview.CalendarDay;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MeLookActivity extends AppCompatActivity {

    private ImageButton backBtn;
    private ImageView emoticonImg;
    private TextView date;
    private TextView title;
    private TextView content;
    private ImageButton editBtn;
    private ImageButton delBtn;

    private MemberDto user;
    private UserCalendarData userCalendarData;
    private CalendarDay clickDate;
    private String diaryContent;
    private InterstitialAd mInterstitialAd;

    private LookDialog dialog;

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(Service.BASE_RUL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    Service service = retrofit.create(Service.class);

    private View.OnClickListener okListener = new View.OnClickListener() { // 삭제 통신 넣어줌
        public void onClick(View v) {
            service.removeDiary(userCalendarData.getDiaryId(),MainActivity.accessToken,user.getId()).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    ;
                }
                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    t.printStackTrace();
                }
            });
            Intent intent = new Intent(MeLookActivity.this, MeDiaryActivity.class);
            intent.putExtra("user",user);
            intent.addFlags(intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_in_left);
        }
    };

    private View.OnClickListener noListener = new View.OnClickListener() {
        public void onClick(View v) {
            dialog.dismiss();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_me_look);

        backBtn = findViewById(R.id.look_btn_back);
        emoticonImg = findViewById(R.id.look_emoticon);
        date = findViewById(R.id.look_date_text);
        title = findViewById(R.id.look_diary_title);
        content = findViewById(R.id.look_diary_content);
        editBtn = findViewById(R.id.me_look_btn_edit);
        delBtn = findViewById(R.id.me_look_btn_del);

        userCalendarData = (UserCalendarData) getIntent().getSerializableExtra("userCalendarData");
        user = (MemberDto) getIntent().getSerializableExtra("user");
        clickDate = userCalendarData.transDate(userCalendarData.getDate());

        content.setMovementMethod(new ScrollingMovementMethod());

        backBtn.setOnClickListener(l->{
            Intent intent = new Intent(this,MeDiaryActivity.class);
            intent.putExtra("user",user);
            intent.addFlags(intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.anim_slide_out_left,R.anim.anim_slide_out_right);
        });

        service.findDiary(userCalendarData.getDiaryId()).enqueue(new Callback<DiaryDto>() {
            @Override
            public void onResponse(Call<DiaryDto> call, Response<DiaryDto> response) {
                diaryContent=response.body().getContent();
                dataInit(clickDate.getYear(),clickDate.getMonth(),clickDate.getDay()
                        ,userCalendarData.getTitle(),diaryContent
                        ,MainActivity.emoticonStore.get(userCalendarData.getEmoticonId()));// 통신해서 바꾸기
            }
            @Override
            public void onFailure(Call<DiaryDto> call, Throwable t) {
                t.printStackTrace();
            }
        });


        delBtn.setOnClickListener(new OnSingleClickListener(){
            @Override
            public void onSingleClick(View v){
                dialog = new LookDialog(MeLookActivity.this,okListener,noListener);
                dialog.show();
            }
        });

        editBtn.setOnClickListener(l->{
            Intent intent = new Intent(this,writeActivity.class);
            CalendarDay tmp = userCalendarData.transDate(userCalendarData.getDate());
            intent.putExtra("userCalendarData",userCalendarData);
            intent.putExtra("user",user);
            intent.putExtra("year",tmp.getYear());
            intent.putExtra("month",tmp.getMonth());
            intent.putExtra("day",tmp.getDay());
            intent.addFlags(intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(intent);
            overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_in_left);
        });

    }

    public void dataInit(int year,int month, int day, String title, String content, int emoImg){
        this.date.setText(String.valueOf(year)+"년 "+String.valueOf(month)+"월 "+String.valueOf(day)+"일");
        Log.e("년",String.valueOf(year));
        this.title.setText(title);
        this.content.setText(content);
        emoticonImg.setImageResource(emoImg);
    }
}