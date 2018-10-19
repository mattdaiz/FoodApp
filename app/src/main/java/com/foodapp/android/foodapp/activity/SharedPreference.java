package com.foodapp.android.foodapp.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

public class SharedPreference {
    public static final String PREF_USER_NAME = "username";
    public static final String PREF_LIKED = "liked";


    public static SharedPreferences getSharedPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static void setUserName(Context context, String userName) {
        Editor editor = getSharedPreferences(context).edit();
        editor.putString(PREF_USER_NAME, userName);
        editor.apply();
    }

    public static String getUserName(Context context) {
        return getSharedPreferences(context).getString(PREF_USER_NAME, "");
    }

    public static void clearUserName(Context ctx) {
        Editor editor = getSharedPreferences(ctx).edit();
        editor.clear(); //clear all stored data
        editor.apply();
    }

    public static void setLiked(Context context, String position) {
        Editor editor = getSharedPreferences(context).edit();
        editor.putString(PREF_LIKED, position);
        editor.apply();
    }

    public static String getLiked(Context context) {
        return getSharedPreferences(context).getString(PREF_LIKED, "");
    }

    public static void removeLiked(Context context) {
        Editor editor = getSharedPreferences(context).edit();
        editor.putString(PREF_LIKED, "EMPTY");
        editor.apply();
    }


}
