package com.foo.diary_android.diaryMe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.foo.diary_android.MainActivity;
import com.foo.diary_android.R;
import com.foo.diary_android.diary.CalendarDeco;
import com.foo.diary_android.emoticon.EmoticonActivity;
import com.foo.diary_android.service.DiaryFindDto;
import com.foo.diary_android.service.MemberDto;
import com.foo.diary_android.service.Service;
import com.foo.diary_android.service.UserCalendarData;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MeDiaryActivity extends AppCompatActivity {

    private MaterialCalendarView materialCalendarView;
    private ImageView userImg;
    private TextView nickName;
    private ImageButton emoticonBox;
    private ImageButton backBtn;
    private ImageView waitingImg;

    private CalendarTodayDeco todayDeco;
    private CalendarDay click_date;
    private MeCalendarDialog dialog;

    private View.OnClickListener lookClickListener;
    private View.OnClickListener writeClickListener;

    private MemberDto user;
    private Map<CalendarDay,UserCalendarData> calendarDays = new HashMap<>();

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(Service.BASE_RUL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    Service service = retrofit.create(Service.class);

    private View.OnClickListener lookListener = new View.OnClickListener() {
        public void onClick(View v) {
            Intent intent = new Intent(MeDiaryActivity.this, MeLookActivity.class);
            intent.putExtra("userCalendarData",calendarDays.get(click_date));
            intent.putExtra("user",user);
            startActivity(intent);
            overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_in_left);
        }
    };

    private View.OnClickListener writeListener = new View.OnClickListener() {
        public void onClick(View v) {
            Intent intent = new Intent(MeDiaryActivity.this, writeActivity.class);
            intent.putExtra("userCalendarData",calendarDays.get(click_date));
            intent.putExtra("user",user);
            intent.putExtra("year",click_date.getYear());
            intent.putExtra("month",click_date.getMonth());
            intent.putExtra("day",click_date.getDay());
            startActivity(intent);
            overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_in_left);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_me_diary);

        materialCalendarView = findViewById(R.id.calendarView);
        userImg = findViewById(R.id.diary_character_img);
        nickName = findViewById(R.id.diary_nickName);
        emoticonBox = findViewById(R.id.diary_btn_emoticon);
        backBtn = findViewById(R.id.diary_btn_back);
        waitingImg = findViewById(R.id.diary_waiting_gif);


        user = (MemberDto) getIntent().getSerializableExtra("user");
        todayDeco=new CalendarTodayDeco(getApplicationContext(),CalendarDay.today());

        nickName.setText(user.getNickName());
        userImg.setImageResource(MainActivity.characterStore.get(user.getCategoryId()));

        click_date = CalendarDay.today();

        materialCalendarView.addDecorator(todayDeco);
        materialCalendarView.setVisibility(View.INVISIBLE);

        backBtn.setOnClickListener(l->{
            finish();
            overridePendingTransition(R.anim.anim_slide_out_left,R.anim.anim_slide_out_right);
        });

        service.findAllDiary(user.getId(),MainActivity.accessToken).enqueue(new Callback<List<DiaryFindDto>>() {
            @Override
            public void onResponse(Call<List<DiaryFindDto>> call, Response<List<DiaryFindDto>> response) {
                long start = System.currentTimeMillis();
                List<DiaryFindDto> diaryFindDtoList = response.body();
                for(DiaryFindDto diaryFindDto : diaryFindDtoList){
                    UserCalendarData userCalendarData = new UserCalendarData(diaryFindDto.getId(),Date.valueOf(diaryFindDto.getDate())
                            ,diaryFindDto.getEmoticonId(),diaryFindDto.getTitle(),diaryFindDto.isOpen());
                    String tmp[] = diaryFindDto.getDate().split("-");
                    Log.e("날짜",tmp[0]);
                    calendarDays.put((CalendarDay.from(Integer.valueOf(tmp[0]).intValue()
                            ,Integer.valueOf(tmp[1]).intValue(),Integer.valueOf(tmp[2]).intValue())),userCalendarData);
                }
                dataInit(calendarDays);
                long end = System.currentTimeMillis();
                Log.e("일기 조회 완료 실행 시간 : " ,String.valueOf((end - start) / 1000.0));
                materialCalendarView.setVisibility(View.VISIBLE);
                waitingImg.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFailure(Call<List<DiaryFindDto>> call, Throwable t) {
                t.printStackTrace();
            }
        });

        emoticonBox.setOnClickListener(l->{
            Intent intent = new Intent(this, EmoticonActivity.class);
            intent.putExtra("user",user);
            startActivity(intent);
            overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_in_left);
        });

        materialCalendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                click_date=date;
                CalendarDay today = CalendarDay.today();
                if(click_date.isBefore(today)||click_date.equals(today)){
                    UserCalendarData userCalendarData;
                    if(calendarDays.containsKey(click_date)){
                        userCalendarData = calendarDays.get(click_date);
                    }
                    else{
                        Date clickDate = Date.valueOf(String.valueOf(click_date.getYear())+"-"
                                +String.valueOf(click_date.getMonth())+"-"
                                +String.valueOf(click_date.getDay()));
                        userCalendarData = new UserCalendarData(0L,clickDate,0,"작성된 일기가 없어요",false);
                    }
                    dialog = new MeCalendarDialog(MeDiaryActivity.this,click_date
                            ,userCalendarData,lookListener,writeListener);
                    dialog.show();
                }
            }
        });
    }

    public void dataInit(Map<CalendarDay,UserCalendarData> calendarDays){
        /*List<CalendarDay> calendarDayList = new ArrayList<>();
        for(CalendarDay calendarDay : calendarDays.keySet()){
            calendarDayList.add(calendarDay);
        }
        for(CalendarDay calendarDay : calendarDayList){
            if(calendarDay.getYear()==CalendarDay.today().getYear()&&calendarDay.getMonth()==CalendarDay.today().getMonth()
                    &&calendarDay.getDay()==CalendarDay.today().getDay()){
                materialCalendarView.removeDecorator(todayDeco);
            }
            materialCalendarView.addDecorator(new CalendarDeco(getApplicationContext()
                    ,calendarDay,calendarDays.get(calendarDay).getEmoticonId()));

        }*/
        Map<CalendarDay,UserCalendarData> calendarDayList1 = new HashMap<>();
        Map<CalendarDay,UserCalendarData> calendarDayList2 = new HashMap<>();
        Map<CalendarDay,UserCalendarData> calendarDayList3 = new HashMap<>();
        Map<CalendarDay,UserCalendarData> calendarDayList4 = new HashMap<>();
        Map<CalendarDay,UserCalendarData> calendarDayList5 = new HashMap<>();
        Map<CalendarDay,UserCalendarData> calendarDayList6 = new HashMap<>();
        Map<CalendarDay,UserCalendarData> calendarDayList7 = new HashMap<>();
        Map<CalendarDay,UserCalendarData> calendarDayList8 = new HashMap<>();
        Map<CalendarDay,UserCalendarData> calendarDayList9 = new HashMap<>();
        Map<CalendarDay,UserCalendarData> calendarDayList10 = new HashMap<>();
        Map<CalendarDay,UserCalendarData> calendarDayList11 = new HashMap<>();
        Map<CalendarDay,UserCalendarData> calendarDayList12 = new HashMap<>();
        Map<CalendarDay,UserCalendarData> calendarDayList13 = new HashMap<>();
        Map<CalendarDay,UserCalendarData> calendarDayList14 = new HashMap<>();
        Map<CalendarDay,UserCalendarData> calendarDayList15 = new HashMap<>();
        Map<CalendarDay,UserCalendarData> calendarDayList16 = new HashMap<>();
        Map<CalendarDay,UserCalendarData> calendarDayList17 = new HashMap<>();
        Map<CalendarDay,UserCalendarData> calendarDayList18 = new HashMap<>();
        Map<CalendarDay,UserCalendarData> calendarDayList19 = new HashMap<>();
        Map<CalendarDay,UserCalendarData> calendarDayList20 = new HashMap<>();
        Map<CalendarDay,UserCalendarData> calendarDayList21 = new HashMap<>();
        Map<CalendarDay,UserCalendarData> calendarDayList22 = new HashMap<>();
        Map<CalendarDay,UserCalendarData> calendarDayList23 = new HashMap<>();
        Map<CalendarDay,UserCalendarData> calendarDayList24 = new HashMap<>();
        Map<CalendarDay,UserCalendarData> calendarDayList25 = new HashMap<>();
        Map<CalendarDay,UserCalendarData> calendarDayList26 = new HashMap<>();
        Map<CalendarDay,UserCalendarData> calendarDayList27 = new HashMap<>();
        Map<CalendarDay,UserCalendarData> calendarDayList28 = new HashMap<>();
        Map<CalendarDay,UserCalendarData> calendarDayList29 = new HashMap<>();
        Map<CalendarDay,UserCalendarData> calendarDayList30 = new HashMap<>();
        Map<CalendarDay,UserCalendarData> calendarDayList31 = new HashMap<>();
        Map<CalendarDay,UserCalendarData> calendarDayList32 = new HashMap<>();
        Map<CalendarDay,UserCalendarData> calendarDayList33 = new HashMap<>();
        Map<CalendarDay,UserCalendarData> calendarDayList34 = new HashMap<>();
        Map<CalendarDay,UserCalendarData> calendarDayList35 = new HashMap<>();
        Map<CalendarDay,UserCalendarData> calendarDayList36 = new HashMap<>();
        Map<CalendarDay,UserCalendarData> calendarDayList37 = new HashMap<>();
        Map<CalendarDay,UserCalendarData> calendarDayList38 = new HashMap<>();
        Map<CalendarDay,UserCalendarData> calendarDayList39 = new HashMap<>();
        Map<CalendarDay,UserCalendarData> calendarDayList40 = new HashMap<>();
        Map<CalendarDay,UserCalendarData> calendarDayList41 = new HashMap<>();
        Map<CalendarDay,UserCalendarData> calendarDayList42 = new HashMap<>();
        Map<CalendarDay,UserCalendarData> calendarDayList43 = new HashMap<>();
        Map<CalendarDay,UserCalendarData> calendarDayList44 = new HashMap<>();
        Map<CalendarDay,UserCalendarData> calendarDayList45 = new HashMap<>();

        for(CalendarDay calendarDay : calendarDays.keySet()){
            if(calendarDay.getYear()==CalendarDay.today().getYear()&&calendarDay.getMonth()==CalendarDay.today().getMonth()
                    &&calendarDay.getDay()==CalendarDay.today().getDay()){
                materialCalendarView.removeDecorator(todayDeco);
            }
            switch (calendarDays.get(calendarDay).getEmoticonId()){
                case 1:calendarDayList1.put(calendarDay,calendarDays.get(calendarDay));break;
                case 2:calendarDayList2.put(calendarDay,calendarDays.get(calendarDay));break;
                case 3:calendarDayList3.put(calendarDay,calendarDays.get(calendarDay));break;
                case 4:calendarDayList4.put(calendarDay,calendarDays.get(calendarDay));break;
                case 5:calendarDayList5.put(calendarDay,calendarDays.get(calendarDay));break;
                case 6:calendarDayList6.put(calendarDay,calendarDays.get(calendarDay));break;
                case 7:calendarDayList7.put(calendarDay,calendarDays.get(calendarDay));break;
                case 8:calendarDayList8.put(calendarDay,calendarDays.get(calendarDay));break;
                case 9:calendarDayList9.put(calendarDay,calendarDays.get(calendarDay));break;
                case 10:calendarDayList10.put(calendarDay,calendarDays.get(calendarDay));break;
                case 11:calendarDayList11.put(calendarDay,calendarDays.get(calendarDay));break;
                case 12:calendarDayList12.put(calendarDay,calendarDays.get(calendarDay));break;
                case 13:calendarDayList13.put(calendarDay,calendarDays.get(calendarDay));break;
                case 14:calendarDayList14.put(calendarDay,calendarDays.get(calendarDay));break;
                case 15:calendarDayList15.put(calendarDay,calendarDays.get(calendarDay));break;
                case 16:calendarDayList16.put(calendarDay,calendarDays.get(calendarDay));break;
                case 17:calendarDayList17.put(calendarDay,calendarDays.get(calendarDay));break;
                case 18:calendarDayList18.put(calendarDay,calendarDays.get(calendarDay));break;
                case 19:calendarDayList19.put(calendarDay,calendarDays.get(calendarDay));break;
                case 20:calendarDayList20.put(calendarDay,calendarDays.get(calendarDay));break;
                case 21:calendarDayList21.put(calendarDay,calendarDays.get(calendarDay));break;
                case 22:calendarDayList22.put(calendarDay,calendarDays.get(calendarDay));break;
                case 23:calendarDayList23.put(calendarDay,calendarDays.get(calendarDay));break;
                case 24:calendarDayList24.put(calendarDay,calendarDays.get(calendarDay));break;
                case 25:calendarDayList25.put(calendarDay,calendarDays.get(calendarDay));break;
                case 26:calendarDayList26.put(calendarDay,calendarDays.get(calendarDay));break;
                case 27:calendarDayList27.put(calendarDay,calendarDays.get(calendarDay));break;
                case 28:calendarDayList28.put(calendarDay,calendarDays.get(calendarDay));break;
                case 29:calendarDayList29.put(calendarDay,calendarDays.get(calendarDay));break;
                case 30:calendarDayList30.put(calendarDay,calendarDays.get(calendarDay));break;
                case 31:calendarDayList31.put(calendarDay,calendarDays.get(calendarDay));break;
                case 32:calendarDayList32.put(calendarDay,calendarDays.get(calendarDay));break;
                case 33:calendarDayList33.put(calendarDay,calendarDays.get(calendarDay));break;
                case 34:calendarDayList34.put(calendarDay,calendarDays.get(calendarDay));break;
                case 35:calendarDayList35.put(calendarDay,calendarDays.get(calendarDay));break;
                case 36:calendarDayList36.put(calendarDay,calendarDays.get(calendarDay));break;
                case 37:calendarDayList37.put(calendarDay,calendarDays.get(calendarDay));break;
                case 38:calendarDayList38.put(calendarDay,calendarDays.get(calendarDay));break;
                case 39:calendarDayList39.put(calendarDay,calendarDays.get(calendarDay));break;
                case 40:calendarDayList40.put(calendarDay,calendarDays.get(calendarDay));break;
                case 41:calendarDayList41.put(calendarDay,calendarDays.get(calendarDay));break;
                case 42:calendarDayList42.put(calendarDay,calendarDays.get(calendarDay));break;
                case 43:calendarDayList43.put(calendarDay,calendarDays.get(calendarDay));break;
                case 44:calendarDayList44.put(calendarDay,calendarDays.get(calendarDay));break;
                case 45:calendarDayList45.put(calendarDay,calendarDays.get(calendarDay));break;
            }
        }
        materialCalendarView.addDecorators(new CalendarDeco(getApplicationContext(),calendarDayList1,R.drawable.em_potato_angry_unck));
        materialCalendarView.addDecorators(new CalendarDeco(getApplicationContext(),calendarDayList2,R.drawable.em_potato_cry_unck));
        materialCalendarView.addDecorators(new CalendarDeco(getApplicationContext(),calendarDayList3,R.drawable.em_potato_fun_unck));
        materialCalendarView.addDecorators(new CalendarDeco(getApplicationContext(),calendarDayList4,R.drawable.em_potato_ignore_unck));
        materialCalendarView.addDecorators(new CalendarDeco(getApplicationContext(),calendarDayList5,R.drawable.em_potato_lol_unck));
        materialCalendarView.addDecorators(new CalendarDeco(getApplicationContext(),calendarDayList6,R.drawable.em_potato_love_unck));
        materialCalendarView.addDecorators(new CalendarDeco(getApplicationContext(),calendarDayList7,R.drawable.em_potato_sick_unck));
        materialCalendarView.addDecorators(new CalendarDeco(getApplicationContext(),calendarDayList8,R.drawable.em_potato_tire_unck));
        materialCalendarView.addDecorators(new CalendarDeco(getApplicationContext(),calendarDayList9,R.drawable.em_potato_yammy_unck));
        materialCalendarView.addDecorators(new CalendarDeco(getApplicationContext(),calendarDayList10,R.drawable.em_swpotato_angry));
        materialCalendarView.addDecorators(new CalendarDeco(getApplicationContext(),calendarDayList11,R.drawable.em_swpotato_cry));
        materialCalendarView.addDecorators(new CalendarDeco(getApplicationContext(),calendarDayList12,R.drawable.em_swpotato_fun));
        materialCalendarView.addDecorators(new CalendarDeco(getApplicationContext(),calendarDayList13,R.drawable.em_swpotato_ignore));
        materialCalendarView.addDecorators(new CalendarDeco(getApplicationContext(),calendarDayList14,R.drawable.em_swpotato_lol));
        materialCalendarView.addDecorators(new CalendarDeco(getApplicationContext(),calendarDayList15,R.drawable.em_swpotato_love));
        materialCalendarView.addDecorators(new CalendarDeco(getApplicationContext(),calendarDayList16,R.drawable.em_swpotato_sick));
        materialCalendarView.addDecorators(new CalendarDeco(getApplicationContext(),calendarDayList17,R.drawable.em_swpotato_surp));
        materialCalendarView.addDecorators(new CalendarDeco(getApplicationContext(),calendarDayList18,R.drawable.em_swpotato_tire));
        materialCalendarView.addDecorators(new CalendarDeco(getApplicationContext(),calendarDayList19,R.drawable.em_abocado_angry));
        materialCalendarView.addDecorators(new CalendarDeco(getApplicationContext(),calendarDayList20,R.drawable.em_abocado_cry));
        materialCalendarView.addDecorators(new CalendarDeco(getApplicationContext(),calendarDayList21,R.drawable.em_abocado_fun));
        materialCalendarView.addDecorators(new CalendarDeco(getApplicationContext(),calendarDayList22,R.drawable.em_abocado_ignore));
        materialCalendarView.addDecorators(new CalendarDeco(getApplicationContext(),calendarDayList23,R.drawable.em_abocado_lol));
        materialCalendarView.addDecorators(new CalendarDeco(getApplicationContext(),calendarDayList24,R.drawable.em_abocado_love));
        materialCalendarView.addDecorators(new CalendarDeco(getApplicationContext(),calendarDayList25,R.drawable.em_abocado_plea));
        materialCalendarView.addDecorators(new CalendarDeco(getApplicationContext(),calendarDayList26,R.drawable.em_abocado_sick));
        materialCalendarView.addDecorators(new CalendarDeco(getApplicationContext(),calendarDayList27,R.drawable.em_abocado_tire));
        materialCalendarView.addDecorators(new CalendarDeco(getApplicationContext(),calendarDayList28,R.drawable.em_apple_angry));
        materialCalendarView.addDecorators(new CalendarDeco(getApplicationContext(),calendarDayList29,R.drawable.em_apple_cry));
        materialCalendarView.addDecorators(new CalendarDeco(getApplicationContext(),calendarDayList30,R.drawable.em_apple_fun));
        materialCalendarView.addDecorators(new CalendarDeco(getApplicationContext(),calendarDayList31,R.drawable.em_apple_ignore));
        materialCalendarView.addDecorators(new CalendarDeco(getApplicationContext(),calendarDayList32,R.drawable.em_apple_lol));
        materialCalendarView.addDecorators(new CalendarDeco(getApplicationContext(),calendarDayList33,R.drawable.em_apple_love));
        materialCalendarView.addDecorators(new CalendarDeco(getApplicationContext(),calendarDayList34,R.drawable.em_apple_plea));
        materialCalendarView.addDecorators(new CalendarDeco(getApplicationContext(),calendarDayList35,R.drawable.em_apple_sick));
        materialCalendarView.addDecorators(new CalendarDeco(getApplicationContext(),calendarDayList36,R.drawable.em_apple_tire));
        materialCalendarView.addDecorators(new CalendarDeco(getApplicationContext(),calendarDayList37,R.drawable.em_mandarin_angry));
        materialCalendarView.addDecorators(new CalendarDeco(getApplicationContext(),calendarDayList38,R.drawable.em_mandarin_cry));
        materialCalendarView.addDecorators(new CalendarDeco(getApplicationContext(),calendarDayList39,R.drawable.em_mandarin_fun));
        materialCalendarView.addDecorators(new CalendarDeco(getApplicationContext(),calendarDayList40,R.drawable.em_mandarin_ignore));
        materialCalendarView.addDecorators(new CalendarDeco(getApplicationContext(),calendarDayList41,R.drawable.em_mandarin_lol));
        materialCalendarView.addDecorators(new CalendarDeco(getApplicationContext(),calendarDayList42,R.drawable.em_mandarin_love));
        materialCalendarView.addDecorators(new CalendarDeco(getApplicationContext(),calendarDayList43,R.drawable.em_mandarin_sick));
        materialCalendarView.addDecorators(new CalendarDeco(getApplicationContext(),calendarDayList44,R.drawable.em_mandarin_sleep));
        materialCalendarView.addDecorators(new CalendarDeco(getApplicationContext(),calendarDayList45,R.drawable.em_mandarin_tire));
    }
}