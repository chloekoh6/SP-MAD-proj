package com.sp.android_studio_project;

public class ShopData {

    private int quantity;
    private int points;
    private String item;
    private String image;
    private String type;
    private String id;

    public ShopData(){
        // Empty constructor for firebase
    }

    public ShopData(int quantity, int points, String item, String image, String type, String id) {
        this.quantity = quantity;
        this.points = points;
        this.item = item;
        this.image = image;
        this.type = type;
        this.id = id;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getPoints() {
        return points;
    }

    public String getItem() {
        return item;
    }

    public String getImage() {
        return image;
    }

    public String getType() { return type; }

    public void setType(String type) { this.type = type; }

    public String getId() { return id; }

    public void setId(String id) { this.id = id; }
}
