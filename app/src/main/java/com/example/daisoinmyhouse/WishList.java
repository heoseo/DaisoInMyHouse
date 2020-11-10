package com.example.daisoinmyhouse;

import android.graphics.drawable.Drawable;

public class WishList {

    String product_name;
    String user_id;
    String location;
    String time;
    int product_price;
    Drawable imageRes;
    //1028코드추가
    int product_no;
    String product_content;

    public WishList(){

    }

    public WishList(String user_id, String product_content, String location, int product_price, String time, int product_no, String product_name, Drawable imageRes){
        this.product_name = product_name;
        this.user_id = user_id;
        this.location = location;
        this.time = time;
        this.product_price = product_price;
        this.imageRes = imageRes;
        //1028 코드추가
        this.product_no = product_no;
        this.product_content = product_content;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getUser_id(){
        return user_id;
    }

    public void setUser_id(String user_id){
        this.user_id = user_id;
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

    public int getProduct_price() {
        return product_price;
    }

    public void setProduct_price(int product_price) {
        this.product_price = product_price;
    }

    public Drawable getImageRes() {
        return imageRes;
    }

    public void setImageRes(Drawable imageRes) {
        this.imageRes = imageRes;
    }

    // 1028코드추가
    public int getProduct_no() {
        return product_no;
    }

    public void setProduct_no(int product_no) {
        this.product_no = product_no;
    }

    public String getProduct_content(){
        return product_content;
    }

    public void setProduct_content(String product_content){
        this.product_content = product_content;
    }
}