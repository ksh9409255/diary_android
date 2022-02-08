package com.foo.diary_android.service;

public class Member {
    private Long id;
    private String nickName;
    private int categoryId;

    public Member(){}

    public Member(Long id, String nickName, int categoryId) {
        this.id = id;
        this.nickName = nickName;
        this.categoryId = categoryId;
    }

    public Long getId() {
        return id;
    }

    public String getNickName() {
        return nickName;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setMember(Long id, String nickName, int categoryId){
        this.id = id;
        this.nickName = nickName;
        this.categoryId = categoryId;
    }
}
