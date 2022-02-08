package com.foo.diary_android.diaryMe;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.foo.diary_android.MainActivity;
import com.foo.diary_android.R;
import com.foo.diary_android.service.UserCalendarData;
import com.prolificinteractive.materialcalendarview.CalendarDay;

public class MeCalendarDialog extends Dialog {
    private TextView dateText;
    private TextView tileText;
    private Button btnWrite;
    private Button btnLook;
    private ImageView emoticon;
    private ImageButton closeBtn;

    private CalendarDay date;
    private UserCalendarData userCalendarData;

    private View.OnClickListener lookClickListener;
    private View.OnClickListener writeClickListener;


    public MeCalendarDialog(@NonNull Context context, CalendarDay date,
                          UserCalendarData userCalendarData, View.OnClickListener lookListener,
                          View.OnClickListener writeListener) {
        super(context);
        this.date=date;
        this.userCalendarData=userCalendarData;
        this.lookClickListener=lookListener;
        this.writeClickListener=writeListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_me_calendar);

        dateText = findViewById(R.id.dialog_calendar_date);
        tileText = findViewById(R.id.dialog_calendar_title);
        btnWrite = findViewById(R.id.dialog_calendar_btn_write);
        btnLook = findViewById(R.id.dialog_calendar_btn_look);
        emoticon = findViewById(R.id.dialog_calendar_emoticon);
        closeBtn = findViewById(R.id.close_btn);

        dateText.setText(String.valueOf(date.getMonth())+"월"+String.valueOf(date.getDay())+"일");
        tileText.setText(userCalendarData.getTitle());
        if(userCalendarData.getEmoticonId()==0){
            emoticon.setImageResource(R.drawable.dialog_emoticon_nothing);
        }
        else{
            emoticon.setImageResource(MainActivity.emoticonStore.get(userCalendarData.getEmoticonId()));
        }

        btnLook.setOnClickListener(lookClickListener);
        btnWrite.setOnClickListener(writeClickListener);
        closeBtn.setOnClickListener(l->{
            this.dismiss();
        });

        btnSelect(userCalendarData.getDiaryId());
    }

    public void btnSelect(Long diaryId){
        if(!diaryId.equals(0L)){ // true면 일기 존재
            btnLook.setVisibility(View.VISIBLE);
            btnWrite.setVisibility(View.INVISIBLE);
        }
        else{
            btnLook.setVisibility(View.INVISIBLE);
            btnWrite.setVisibility(View.VISIBLE);
        }
    }
}
