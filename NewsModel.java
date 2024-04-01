package com.sp.android_studio_project;

public class NewsModel {
    String title;
    String url;
    String image;

    public NewsModel() {
        //for firebase
    }

    public NewsModel(String title, String url, String image) {
        this.title = title;
        this.url = url;
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() { return url; }

    public String getImage() {
        return image;
    }
}
