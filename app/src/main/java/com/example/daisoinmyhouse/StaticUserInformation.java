package com.example.daisoinmyhouse;

import java.util.HashSet;

public class StaticUserInformation {
    public static String nickName = null;
    public static String porfileUrl= null;
    public static String myArea= null;
    public static String userID = null; // <- 로그인되면 나중에 구현.

    public static HashSet<String> roomSet = new HashSet<String>();
}
