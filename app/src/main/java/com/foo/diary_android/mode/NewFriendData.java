package com.foo.diary_android.mode;

import com.foo.diary_android.service.MemberDto;

public class NewFriendData {
    private MemberDto userId;
    private Long friendId;
    private int friendImg;
    private String friendNickName;

    public NewFriendData(MemberDto userId, Long friendId, int friendImg, String friendNickName) {
        this.userId = userId;
        this.friendId = friendId;
        this.friendImg = friendImg;
        this.friendNickName = friendNickName;
    }

    public MemberDto getUserId() {
        return userId;
    }

    public Long getFriendId() {
        return friendId;
    }

    public int getFriendImg() {
        return friendImg;
    }

    public String getFriendNickName() {
        return friendNickName;
    }
}
