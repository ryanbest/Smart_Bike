package com.example.myapplication;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Calendar;

public class Utils {

    public static void saveSpeedLimit(Context context, int limit) {
        SharedPreferences sharedpreferences = context.getSharedPreferences("limit_pref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putInt("limit", limit);
        editor.apply();

    }

    public static int getSpeedLimit(Context context) {
        SharedPreferences sharedpreferences = context.getSharedPreferences("limit_pref", Context.MODE_PRIVATE);
        return sharedpreferences.getInt("limit", 10);

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

    public static long getFirstTimeStampOfCurrentMonth(int month, int year) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);//leap year 29 Feb;)
        cal.set(Calendar.MONTH, month);
        cal.set(year, month, cal.getActualMinimum(Calendar.DAY_OF_MONTH), cal.getActualMinimum(Calendar.HOUR_OF_DAY), cal.getActualMinimum(Calendar.MINUTE), cal.getActualMaximum(Calendar.SECOND));
        cal.set(Calendar.MILLISECOND, cal.getActualMaximum(Calendar.MILLISECOND));
        return cal.getTimeInMillis();
    }
}
