package com.example.daisoinmyhouse;

import android.content.SharedPreferences;

import java.util.HashSet;

public class StaticUserInformation {
    public static String nickName = null;
    public static String porfileUrl = null;
    public static String myArea= null;
    public static String userID = null;
    public static String cntWishList = null;

    public static HashSet<String> roomSet = new HashSet<String>();

    public static void loadData(SharedPreferences preferences) {

        StaticUserInformation.nickName = preferences.getString("nickName", null);
        StaticUserInformation.porfileUrl = preferences.getString("profileUrl", null);
    }

    public static void resetDate(SharedPreferences preferences){
        SharedPreferences.Editor editor=preferences.edit();
        editor.putString("nickName", null);
        editor.putString("profileUrl", null);
        editor.putString("myArea", null );
        editor.putString("userID", null );
        editor.putString("cntWishList", null );

        editor.apply();
        editor.commit();



        StaticUserInformation.nickName=preferences.getString("nickName", null);
        StaticUserInformation.porfileUrl =preferences.getString("profileUrl", null);
        StaticUserInformation.myArea =preferences.getString("myArea", null);
        StaticUserInformation.userID =preferences.getString("userID", null);
        StaticUserInformation.cntWishList = preferences.getString("cntWishList", null);
    }
}
