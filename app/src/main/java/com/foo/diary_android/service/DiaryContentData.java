package com.foo.diary_android.service;

public class DiaryContentData {
    private Long diaryId;
    private String content;

    public DiaryContentData(Long diaryId, String content) {
        this.diaryId = diaryId;
        this.content = content;
    }

    public Long getDiaryId() {
        return diaryId;
    }

    public String getContent() {
        return content;
    }
}
