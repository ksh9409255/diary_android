package com.foo.diary_android.service;

import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UserCalendarData implements Serializable {
    private Long diaryId;
    private Date date;
    private int emoticonId;
    private String title;
    private boolean isPublic;

    public UserCalendarData(Long diaryId, Date date, int emoticonId, String title, boolean isPublic) {
        this.diaryId = diaryId;
        this.date = date;
        this.emoticonId = emoticonId;
        this.title = title;
        this.isPublic = isPublic;
    }

    public Date getDate() {
        return date;
    }

    public int getEmoticonId() {
        return emoticonId;
    }

    public String getTitle() {
        return title;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public Long getDiaryId() {
        return diaryId;
    }

    public CalendarDay transDate(Date date){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String[] dates =simpleDateFormat.format(date).split("-");
        return CalendarDay.from(Integer.valueOf(dates[0]),Integer.valueOf(dates[1]),Integer.valueOf(dates[2]));
    }

    public void setDiaryId(Long id){
        this.diaryId=id;
    }
}
