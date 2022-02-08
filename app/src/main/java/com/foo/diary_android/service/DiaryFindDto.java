package com.foo.diary_android.service;

public class DiaryFindDto {
    private Long id; // 다이어리 id
    private boolean open;
    private String title;
    private String date;
    private int emoticonId;

    public DiaryFindDto(Long id, boolean open, String title, String date, int emoticonId) {
        this.id = id;
        this.open = open;
        this.title = title;
        this.date = date;
        this.emoticonId = emoticonId;
    }

    public Long getId() {
        return id;
    }

    public boolean isOpen() {
        return open;
    }

    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }

    public int getEmoticonId() {
        return emoticonId;
    }
}
