package com.example.daisoinmyhouse;

public class Rent {

    String item_name;
    String id;
    String date;
    int price;
    int imageRes;

    public Rent(){

    }

    public Rent(int imageRes, String item_name, int price, String id, String date){
        this.imageRes = imageRes;
        this.item_name = item_name;
        this.id = id;
        this.price = price;
        this.date = date;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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
}
