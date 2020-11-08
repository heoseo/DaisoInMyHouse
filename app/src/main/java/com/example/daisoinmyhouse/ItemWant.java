package com.example.daisoinmyhouse;

public class ItemWant {
    String want_name;
    String user_id;
    String location;
    String time;
    //1028코드추가
    int want_no;
    String want_content;
    String category;

    public ItemWant(){

    }

    public ItemWant(String user_id, String want_content, String location, String time, int want_no, String want_name, String category){
        this.want_name = want_name;
        this.user_id = user_id;
        this.location = location;
        this.time = time;
        //1028 코드추가
        this.want_no = want_no;
        this.want_content = want_content;
        this.category = category;
    }

    public String getWant_name() {
        return want_name;
    }

    public void setWant_name(String want_name) {
        this.want_name = want_name;
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

    public int getWant_no() {
        return want_no;
    }

    public void setWant_no(int want_no) {
        this.want_no = want_no;
    }

    public String getWant_content(){
        return want_content;
    }

    public void setWant_content(String want_content){
        this.want_content = want_content;
    }

    public String getCategory(){
        return category;
    }

    public void setCategory(String category){
        this.category = category;
    }

}
