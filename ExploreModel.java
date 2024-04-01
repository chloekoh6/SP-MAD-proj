package com.sp.android_studio_project;

public class ExploreModel {
    String exploreTitle;
    String exploreDesc;
    int exploreImage;

    public ExploreModel(String exploreTitle, String exploreDesc, int exploreImage) {
        this.exploreTitle = exploreTitle;
        this.exploreDesc = exploreDesc;
        this.exploreImage = exploreImage;
    }

    public String getExploreTitle() {
        return exploreTitle;
    }

    public String getExploreDesc() {
        return exploreDesc;
    }

    public int getExploreImage() {
        return exploreImage;
    }
}
