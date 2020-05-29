package com.example.poskorafinaru;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefManager {
    private Context mContext;

    public static final String KEY_ISLOGGEDIN = "is_logged_in";
    public static final String PREF_NAME  = "RekonPref";
    public static final String KEY_DATE_PICK1  = "DatePick1";
    public static final String KEY_DATE_PICK2  = "DatePick2";
    public static final String KEY_TEXT_DATE_PICK1  = "TextDatePick1";
    public static final String KEY_TEXT_DATE_PICK2  = "TextDatePick2";

    public SharedPrefManager(Context context){
        this.mContext = context;
    }

    public static SharedPrefManager getInstance(Context context){
        SharedPrefManager sharedPrefManager = new SharedPrefManager(context);
        return sharedPrefManager;
    }

    public boolean isLoggedIn(){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.contains(KEY_ISLOGGEDIN) && sharedPreferences.getBoolean(KEY_ISLOGGEDIN,false);
    }

    public void login (){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(KEY_ISLOGGEDIN,true);

        editor.apply();
    }

    public void logout(){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

    public void setDatePick1(String datePick1){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(KEY_DATE_PICK1, datePick1);
        editor.apply();
    }

    public void setDatePick2(String datePick2){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(KEY_DATE_PICK2, datePick2);
        editor.apply();
    }

    public void setTextDatePick1(String textDatePick1){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(KEY_TEXT_DATE_PICK1, textDatePick1);
        editor.apply();
    }

    public void setTextDatePick2(String textDatePick2){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(KEY_TEXT_DATE_PICK2, textDatePick2);
        editor.apply();
    }

    public String getDatePick1(){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_DATE_PICK1,null);
    }

    public String getDatePick2(){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_DATE_PICK2,null);
    }

    public String getTextDatePick1(){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_TEXT_DATE_PICK1,null);
    }

    public String getTextDatePick2(){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_TEXT_DATE_PICK2,null);
    }
}
