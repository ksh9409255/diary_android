package com.foo.diary_android.diary;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.foo.diary_android.MainActivity;
import com.foo.diary_android.R;
import com.foo.diary_android.service.DiaryDto;
import com.foo.diary_android.service.Service;
import com.foo.diary_android.service.UserCalendarData;
import com.prolificinteractive.materialcalendarview.CalendarDay;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LookActivity extends AppCompatActivity {

    private ImageButton backBtn;
    private ImageView emoticonImg;
    private TextView date;
    private TextView title;
    private TextView content;

    private UserCalendarData userCalendarData;
    private CalendarDay clickDate;
    private String diaryContent;

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(Service.BASE_RUL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    Service service = retrofit.create(Service.class);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_look);

        backBtn = findViewById(R.id.look_btn_back);
        emoticonImg = findViewById(R.id.look_emoticon);
        date = findViewById(R.id.look_date_text);
        title = findViewById(R.id.look_diary_title);
        content = findViewById(R.id.look_diary_content);

        userCalendarData = (UserCalendarData) getIntent().getSerializableExtra("userCalendarData");
        clickDate = userCalendarData.transDate(userCalendarData.getDate());

        content.setMovementMethod(new ScrollingMovementMethod());

        backBtn.setOnClickListener(l->{
            finish();
            overridePendingTransition(R.anim.anim_slide_out_left,R.anim.anim_slide_out_right);
        });

        service.findDiary(userCalendarData.getDiaryId()).enqueue(new Callback<DiaryDto>() {
            @Override
            public void onResponse(Call<DiaryDto> call, Response<DiaryDto> response) {
                diaryContent=response.body().getContent();
                dataInit(clickDate.getMonth(),clickDate.getDay(),userCalendarData.getTitle(),diaryContent
                        , MainActivity.emoticonStore.get(userCalendarData.getEmoticonId()));
            }

            @Override
            public void onFailure(Call<DiaryDto> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public void dataInit(int month, int day, String title, String content, int emoImg){
        this.date.setText(String.valueOf(month)+"월"+String.valueOf(day)+"일");
        this.title.setText(title);
        this.content.setText(content);
        this.emoticonImg.setImageResource(emoImg);
    }
}