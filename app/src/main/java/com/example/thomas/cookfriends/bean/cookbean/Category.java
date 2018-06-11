package com.example.thomas.cookfriends.bean.cookbean;

public class Category {
    private int imgId;
    private String title;

    public int getImgId() {
        return imgId;
    }

    public void setImgId(int imgId) {
        this.imgId = imgId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Category() {
    }

    public Category(int imgId, String title) {
        this.imgId = imgId;
        this.title = title;
    }

    @Override
    public String toString() {
        return "Category{" +
                "imgId=" + imgId +
                ", title='" + title + '\'' +
                '}';
    }
}
