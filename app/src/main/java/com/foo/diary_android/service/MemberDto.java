package com.foo.diary_android.service;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class MemberDto implements Serializable {
    private Long id;
    private String nickName;
    private int categoryId;

    public MemberDto(){}

    public MemberDto(Long id, String nickName, int categoryId) {
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
