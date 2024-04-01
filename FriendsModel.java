package com.sp.android_studio_project;

public class FriendsModel {
    private String image;
    private String username;
    private int points;
    //private Map<String, Object> mImageUrl;

    public FriendsModel() {
        //for firebase
    }

    public FriendsModel(String image, String username, int points) {
        this.image = image;
        this.username = username;
        this.points = points;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImage() {
        return image;
    }

    public String getUsername() {
        return username;
    }

    public int getPoints() {
        return points;
    }

}
