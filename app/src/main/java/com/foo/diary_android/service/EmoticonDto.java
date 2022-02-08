package com.foo.diary_android.service;

public class EmoticonDto {
    private int id;
    private String name;
    private CategoryDto categoryId;
    private String description;

    public EmoticonDto(int id, String name, CategoryDto categoryId, String description) {
        this.id = id;
        this.name = name;
        this.categoryId = categoryId;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public CategoryDto getCategoryId() {
        return categoryId;
    }

    public String getDescription() {
        return description;
    }
}
