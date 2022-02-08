package com.foo.diary_android.diary;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.style.ForegroundColorSpan;
import android.util.Log;

import com.foo.diary_android.MainActivity;
import com.foo.diary_android.R;
import com.foo.diary_android.service.UserCalendarData;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class CalendarDeco implements DayViewDecorator {

    private Drawable background;
    private CalendarDay day;
    private Context context;
    private Map<CalendarDay,UserCalendarData> days;


 /*   public CalendarDeco(Context context,CalendarDay date,int emoticonId){
        day=date;
        background = context.getResources().getDrawable(MainActivity.emoticonStore.get(emoticonId));
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) { // 어떤 날짜에 데코를 입힐건지 결정
        return this.day.equals(day);
    }

    @Override
    public void decorate(DayViewFacade view) { // 데코 내용

        view.setSelectionDrawable(background);
        view.addSpan(new ForegroundColorSpan(Color.parseColor("#00000000")));
    }
*/
    public CalendarDeco(Context context, Map<CalendarDay,UserCalendarData> date,int emoticonId){
        this.context = context;
        days = new HashMap<CalendarDay,UserCalendarData>(date);
        background = context.getResources().getDrawable(emoticonId);
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) { // 어떤 날짜에 데코를 입힐건지 결정
        return days.containsKey(day);
    }

    @Override
    public void decorate(DayViewFacade view) { // 데코 내용
        view.setSelectionDrawable(background);
        view.addSpan(new ForegroundColorSpan(Color.parseColor("#00000000")));
    }
}
