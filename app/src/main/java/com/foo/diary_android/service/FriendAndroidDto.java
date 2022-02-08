package com.foo.diary_android.service;

public class FriendAndroidDto {
    private Member memberId_1;
    private String memberId_2_nickname;

    public FriendAndroidDto(Member memberId_1, String memberId_2_nickname) {
        this.memberId_1 = memberId_1;
        this.memberId_2_nickname = memberId_2_nickname;
    }

    public Member getMemberId_1() {
        return memberId_1;
    }

    public String getMemberId_2_nickname() {
        return memberId_2_nickname;
    }
}
