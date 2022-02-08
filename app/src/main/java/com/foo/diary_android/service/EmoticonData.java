package com.foo.diary_android.service;


import com.foo.diary_android.MainActivity;

import java.util.HashMap;
import java.util.Map;

public class EmoticonData {
    private int emticonId;
    private String emoticonName;
    private String content;

    public EmoticonData(int emticonId, String emoticonName, String content) {
        this.emticonId = emticonId;
        this.emoticonName = emoticonName;
        this.content = content;
    }

    public int getEmticonId() {
        return emticonId;
    }

    public String getEmoticonName() {
        return emoticonName;
    }

    public String getContent() {
        return content;
    }
}
