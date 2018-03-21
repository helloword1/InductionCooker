package com.goockr.inductioncooker.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.goockr.inductioncooker.MyApplication;

/**
 * Created by CMQ on 2017/8/11.
 */

public class SharePreferencesUtils {

    public static String USERINFO = "userinfo";

    private static final String USERIDKEY = "USERIDKEY";

    private static final String TOKENKEY = "TOKENKEY";

    private static final String MOBILEKEY = "MOBILEKEY";

    private static final String NAMEKEY = "NAMEKEY";

    private static final String DEVICEID = "DEVICEID";


    public static String getUserID() {
        return getInfo(USERINFO, USERIDKEY);
    }

    public static String getDeviceId() {
        return getInfo(DEVICEID, DEVICEID);
    }

    public static String getToken() {
        return getInfo(USERINFO, TOKENKEY);
    }

    public static String getMobile() {
        return getInfo(USERINFO, MOBILEKEY);
    }

    public static String getUserName() {
        return getInfo(USERINFO, NAMEKEY);
    }


    public static void setUserID(String value) {
        setInfo(USERINFO, USERIDKEY, value);
    }

    public static void setDeviceId(String value) {
        setInfo(DEVICEID, DEVICEID, value);
    }

    public static void setToken(String value) {
        setInfo(USERINFO, TOKENKEY, value);
    }

    public static void setMobile(String value) {
        setInfo(USERINFO, MOBILEKEY, value);
    }

    public static void setUserName(String value) {
        setInfo(USERINFO, NAMEKEY, value);
    }

    public static void cleanUserINfo() {
        cleanInfo(USERINFO, USERIDKEY);
        cleanInfo(USERINFO, TOKENKEY);
        cleanInfo(USERINFO, MOBILEKEY);
        cleanInfo(USERINFO, NAMEKEY);

    }

    public static void cleanInfo(String filename, String key) {
        setInfo(filename, key, null);
    }

    private static void setInfo(String filename, String key, String value) {
        SharedPreferences sharedPreferences = MyApplication.getContext().getSharedPreferences(filename, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    private static String getInfo(String filename, String key) {
        SharedPreferences sharedPreferences = MyApplication.getContext().getSharedPreferences(filename, Context.MODE_PRIVATE);
        return sharedPreferences.getString(key, null);

    }


}
