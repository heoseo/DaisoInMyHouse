package com.example.daisoinmyhouse;

public class Item {
    String name;
    String location;
    String time;
    int price;
    int imageRes;
    //1028코드추가
    String productID;

    public Item(){

    }

    public Item(String name, String location, String time, int price, int imageRes, String productID){
        this.name = name;
        this.location = location;
        this.time = time;
        this.price = price;
        this.imageRes = imageRes;
        //1028 코드추가
        this.productID = productID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getTime(){
        return time;
    }

    public void setTime(String time){
        this.time = time;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getImageRes() {
        return imageRes;
    }

    public void setImageRes(int imageRes) {
        this.imageRes = imageRes;
    }

    // 1028코드추가
    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }
}
