package com.example.foodbuddy;

public class Utility {
    private static String userToken;

    public static void setUserToken(String token){
        userToken = token;
    }

    public static String getUserToken(){
        return userToken;
    }
}
