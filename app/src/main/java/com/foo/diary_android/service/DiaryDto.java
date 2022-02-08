package com.foo.diary_android.service;

import java.util.Date;

public class DiaryDto {
    private boolean open;
    private String title;
    private String content;
    private String date;
    private int emoticonId;
    private Long memberId;

    public DiaryDto(boolean open, String title, String content, String date, int emoticonId, Long memberId) {
        this.open = open;
        this.title = title;
        this.content = content;
        this.date = date;
        this.emoticonId = emoticonId;
        this.memberId = memberId;
    }

    public boolean isOpen() {
        return open;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getDate() {
        return date;
    }

    public int getEmoticonId() {
        return emoticonId;
    }

    public Long getMemberId() {
        return memberId;
    }
}
