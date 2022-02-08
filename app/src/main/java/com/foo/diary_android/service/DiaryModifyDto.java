package com.foo.diary_android.service;

public class DiaryModifyDto {
    private Long id; // 일기 id
    private boolean open;
    private String title;
    private String content;
    private int emoticonId;

    public DiaryModifyDto(Long id, boolean open, String title, String content, int emoticonId) {
        this.id = id;
        this.open = open;
        this.title = title;
        this.content = content;
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

    public String getContent() {
        return content;
    }

    public int getEmoticonId() {
        return emoticonId;
    }
}
