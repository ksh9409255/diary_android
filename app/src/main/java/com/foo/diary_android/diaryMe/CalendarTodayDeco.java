package com.foo.diary_android.diaryMe;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.style.ForegroundColorSpan;

import com.foo.diary_android.MainActivity;
import com.foo.diary_android.R;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

public class CalendarTodayDeco implements DayViewDecorator {

    private final Drawable background;
    private CalendarDay day;

    public CalendarTodayDeco(Context context, CalendarDay date){
        day=date;
        background = context.getResources().getDrawable(R.drawable.diary_today);
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return this.day.equals(day);
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.setBackgroundDrawable(background);
    }
}
