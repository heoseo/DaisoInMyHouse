package com.example.daisoinmyhouse;

import android.content.SharedPreferences;

import java.util.HashSet;

public class StaticUserInformation {
    public static String nickName = null;
    public static String porfileUrl = null;
    public static String myArea= null;
    public static String userID = null; // <- 로그인되면 나중에 구현.

    public static HashSet<String> roomSet = new HashSet<String>();

    public static void loadData(SharedPreferences preferences) {

        StaticUserInformation.nickName = preferences.getString("nickName", null);
        StaticUserInformation.porfileUrl = preferences.getString("profileUrl", null);
    }
}
