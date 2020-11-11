package com.example.daisoinmyhouse;
import android.graphics.drawable.Drawable;

import java.util.Date;

public class CategoryItem {
    String user_id;
    String item_name;
    String location;
    String time;
    String product_content;
    int price;
    Drawable imageRes;
    int product_no;



    public CategoryItem(){

    }

    public CategoryItem(String user_id, String product_content, String location, int price, String time, int productID, String item_name, Drawable imageRes){
        this.user_id = user_id;
        this.product_content = product_content;
        this.location = location;
        this.price = price;
        this.time = time;
        this.item_name = item_name;
        this.imageRes = imageRes;
        this.product_no = productID;
    }

    public String getProduct_content() {
        return product_content;
    }

    public void setProduct_content(String product_content) {
        this.product_content = product_content;
    }

    public String getUser_id(){
        return user_id;
    }

    public void setUser_id(String user_id){
        this.user_id = user_id;
    }

    public String getProduct_name() {
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

    public int getProduct_price() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public Drawable getImageRes() {
        return imageRes;
    }

    public void setImageRes(Drawable imageRes) {
        this.imageRes = imageRes;
    }

    public int getProduct_no() {
        return product_no;
    }

    public void setProductID(int productID) {
        this.product_no = productID;
    }
}