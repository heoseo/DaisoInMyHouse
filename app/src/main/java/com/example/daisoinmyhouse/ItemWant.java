package com.example.daisoinmyhouse;

import java.sql.Date;

public class ItemWant {
    String product_name;
    String user_id;
    String location;
    String time;
    //1028코드추가
    int product_no;
    String product_content;
    String category;

    public ItemWant(){

    }

    public ItemWant(String user_id, String product_content, String location, String time, int product_no,  String product_name, String category){
        this.product_name = product_name;
        this.user_id = user_id;
        this.location = location;
        this.time = time;
        //1028 코드추가
        this.product_no = product_no;
        this.product_content = product_content;
        this.category = category;
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

    public String getCategory(){
        return category;
    }

    public void setCategory(String category){
        this.category = category;
    }

}
