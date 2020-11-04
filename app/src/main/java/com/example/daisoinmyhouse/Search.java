package com.example.daisoinmyhouse;

public class Search {

    String item_name;
    String location;
    String time;
    int price;
    int imageRes;
    String productID;

    public Search(){

    }

    public Search(String item_name, String location, String time, int price, int imageRes, String productID) {
        this.item_name = item_name;
        this.location = location;
        this.time = time;
        this.price = price;
        this.imageRes = imageRes;
        this.productID = productID;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
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

    public String getProductID(){
        return productID;
    }

    public void setProductID(String productID){
        this.productID = productID;
    }
}