package com.foo.diary_android.diary;

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

public class CalendarDialog extends Dialog {
    private TextView dateText;
    private TextView tileText;
    private TextView lockMsg;
    private Button btnOk;
    private Button btnLook;
    private ImageView emoticon;
    private ImageView lockImg;
    private ImageButton closeBtn;


    private View.OnClickListener lookClickListener;
    private View.OnClickListener okClickListener;

    private CalendarDay date;
    private UserCalendarData userCalendarData;


    public CalendarDialog(@NonNull Context context, CalendarDay date,
                          UserCalendarData userCalendarData, View.OnClickListener lookListener,
                          View.OnClickListener okListener) {
        super(context);
        this.date=date;
        this.userCalendarData=userCalendarData;
        this.lookClickListener=lookListener;
        this.okClickListener=okListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_calendar);

        btnLook = (Button)findViewById(R.id.dialog_calendar_btn_look);
        btnOk = (Button)findViewById(R.id.dialog_calendar_btn_ok);
        dateText = (TextView)findViewById(R.id.dialog_calendar_date);
        tileText = (TextView)findViewById(R.id.dialog_calendar_title);
        emoticon = (ImageView)findViewById(R.id.dialog_calendar_emoticon);
        lockImg = (ImageView)findViewById(R.id.dialog_calendar_lock);
        lockMsg = (TextView)findViewById(R.id.dialog_calendar_lock_msg);
        closeBtn = findViewById(R.id.close_btn);

        dateText.setText(String.valueOf(date.getMonth())+"월"+String.valueOf(date.getDay())+"일");
        tileText.setText(userCalendarData.getTitle());
        emoticon.setImageResource(MainActivity.emoticonStore.get(userCalendarData.getEmoticonId()));

        btnLook.setOnClickListener(lookClickListener);
        btnOk.setOnClickListener(okClickListener);
        closeBtn.setOnClickListener(l->{
            this.dismiss();
        });

        btnSelect(userCalendarData.isPublic());
    }

    public void btnSelect(boolean isPublic){
        if(isPublic){
            btnLook.setVisibility(View.VISIBLE);
            btnOk.setVisibility(View.INVISIBLE);
            tileText.setVisibility(View.VISIBLE);
            lockImg.setVisibility(View.INVISIBLE);
            dateText.setVisibility(View.VISIBLE);
            lockMsg.setVisibility(View.INVISIBLE);
        }
        else{
            btnLook.setVisibility(View.INVISIBLE);
            btnOk.setVisibility(View.INVISIBLE);
            tileText.setVisibility(View.INVISIBLE);
            lockImg.setVisibility(View.VISIBLE);
            dateText.setVisibility(View.INVISIBLE);
            lockMsg.setVisibility(View.VISIBLE);
        }
    }
}
