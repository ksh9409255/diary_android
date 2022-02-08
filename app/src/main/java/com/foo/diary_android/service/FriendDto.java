package com.foo.diary_android.service;

public class FriendDto {
    private Member memberId_1;
    private Member memberId_2;

    public FriendDto(Member memberId_1, Member memberId_2) {
        this.memberId_1 = memberId_1;
        this.memberId_2 = memberId_2;
    }

    public Member getMemberId_1() {
        return memberId_1;
    }

    public Member getMemberId_2() {
        return memberId_2;
    }
}
