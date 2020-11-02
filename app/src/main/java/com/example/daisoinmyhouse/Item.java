package com.example.daisoinmyhouse;

import java.sql.Date;

public class Item {
    String name;
    String location;
    Date time;
    int price;
    int imageRes;
    //1028코드추가
    int num;

    public Item(){

    }

    public Item(String name, String location, Date time, int price, int imageRes, int num){
        this.name = name;
        this.location = location;
        this.time = time;
        this.price = price;
        this.imageRes = imageRes;
        //1028 코드추가
        this.num = num;
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

    public Date getTime(){
        return time;
    }

    public void setTime(Date time){
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
    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }
}
