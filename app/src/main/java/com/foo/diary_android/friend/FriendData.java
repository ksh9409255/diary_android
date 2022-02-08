package com.foo.diary_android.friend;

public class FriendData {
    private Long friendId;
    private int friendImg;
    private String friendNickName;

    public FriendData(Long friendId, int friendImg, String friendNickName) {
        this.friendId = friendId;
        this.friendImg = friendImg;
        this.friendNickName = friendNickName;
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
