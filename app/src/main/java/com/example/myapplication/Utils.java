package com.example.myapplication;

import android.content.Context;
import android.content.SharedPreferences;

public class Utils {

    public static void saveSpeedLimit(Context context, int limit) {
        SharedPreferences sharedpreferences = context.getSharedPreferences("limit_pref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putInt("limit", limit);
        editor.apply();

    }

    public static int getSpeedLimit(Context context) {
        SharedPreferences sharedpreferences = context.getSharedPreferences("limit_pref", Context.MODE_PRIVATE);
        return sharedpreferences.getInt("limit", 0);

    }


    public static void saveContact(Context context, String contact) {
        SharedPreferences sharedpreferences = context.getSharedPreferences("contact_pref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("contact", contact);
        editor.apply();

    }

    public static String getContact(Context context) {
        SharedPreferences sharedpreferences = context.getSharedPreferences("contact_pref", Context.MODE_PRIVATE);
        return sharedpreferences.getString("contact", "");

    }

    public static void saveUserid(Context context, String id) {
        SharedPreferences sharedpreferences = context.getSharedPreferences("user_pref" +
                "", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("userid", id);
        editor.apply();

    }

    public static String getUserId(Context context) {
        SharedPreferences sharedpreferences = context.getSharedPreferences("user_pref", Context.MODE_PRIVATE);
        return sharedpreferences.getString("userid", "");

    }

}
