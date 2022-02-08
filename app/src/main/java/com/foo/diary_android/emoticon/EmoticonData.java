package com.foo.diary_android.emoticon;

public class EmoticonData {

    private int emoticonNum;
    private String name;
    private int emoticonImage;
    private String content;

    public EmoticonData(int emoticonNum, String name, int emoticonImage, String content) {
        this.emoticonNum = emoticonNum;
        this.name = name;
        this.emoticonImage = emoticonImage;
        this.content = content;
    }

    public int getEmoticonNum() {
        return emoticonNum;
    }

    public String getName() {
        return name;
    }

    public int getEmoticonImage() {
        return emoticonImage;
    }

    public String getContent() {
        return content;
    }

}
