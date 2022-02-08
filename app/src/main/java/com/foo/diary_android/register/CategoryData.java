package com.foo.diary_android.register;

public class CategoryData {
    private int categoryId;
    private int categoryImg;
    private String name;

    public CategoryData(int categoryId, int categoryImg, String name) {
        this.categoryId = categoryId;
        this.categoryImg = categoryImg;
        this.name = name;
    }

    public int getCategoryImg() {
        return categoryImg;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public String getName() {
        return name;
    }
}
