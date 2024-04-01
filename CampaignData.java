package com.sp.android_studio_project;

public class CampaignData {

    private int num;
    private String name;
    private String desc;
    private String url;
    private String image;

    public CampaignData(){
        // Empty constructor for firebase
    }

    public CampaignData(int num, String name, String desc, String url, String image) {
        this.num = num;
        this.name = name;
        this.desc = desc;
        this.url = url;
        this.image = image;
    }

    public int getNum() {
        return num;
    }

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }

    public String getUrl() {
        return url;
    }

    public String getImage() {
        return image;
    }

}
