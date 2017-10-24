package com.goockr.inductioncooker.lib.http;

import java.util.Map;

/**
 * Created by CMQ on 2017/8/9.
 */

public class HttpHelper {

    public static void getLoginSmmCode(Map<String, Object> map, OKHttp.HttpCallback callback) {
        String urlString = HttpCommon.KHttpIp + HttpCommon.KHttpLoginSmsCode;
        OKHttp.post(urlString, map, callback);

    }

    public static void loginSmmCode(Map<String, Object> map, OKHttp.HttpCallback callback) {
        String urlString = HttpCommon.KHttpIp + HttpCommon.KHttpLoginSms;
        OKHttp.post(urlString, map, callback);

    }

    public static void loginPwd(Map<String, Object> map, OKHttp.HttpCallback callback) {
        String urlString = HttpCommon.KHttpIp + HttpCommon.KHttpLoginPwd;
        OKHttp.post(urlString, map, callback);

    }

    public static void getForgetSmmCode(Map<String, Object> map, OKHttp.HttpCallback callback) {
        String urlString = HttpCommon.KHttpIp + HttpCommon.KHttpForgetCode;
        OKHttp.post(urlString, map, callback);

    }

    public static void forgetPwd(Map<String, Object> map, OKHttp.HttpCallback callback) {
        String urlString = HttpCommon.KHttpIp + HttpCommon.KHttpForgetPwd;
        OKHttp.post(urlString, map, callback);

    }

    public static void regist(Map<String, Object> map, OKHttp.HttpCallback callback) {
        String urlString = HttpCommon.KHttpIp + HttpCommon.KHttpRegist;
        OKHttp.post(urlString, map, callback);

    }

    public static void updatePwd(Map<String, Object> map, OKHttp.HttpCallback callback) {
        String urlString = HttpCommon.KHttpIp + HttpCommon.KHttpUpdatePwd;
        OKHttp.post(urlString, map, callback);

    }

    public static void tranferRight(Map<String, Object> map, OKHttp.HttpCallback callback) {
        String urlString = HttpCommon.KHttpIp + HttpCommon.KHttpLoginSmsCode;
        OKHttp.post(urlString, map, callback);

    }public static void tranferRightReady(Map<String, Object> map, OKHttp.HttpCallback callback) {
        String urlString = HttpCommon.KHttpIp + HttpCommon.KHttpLoginSmsCode;
        OKHttp.post(urlString, map, callback);

    }
    public static void checkDevice(Map<String, Object> map, OKHttp.HttpCallback callback) {
        String urlString = HttpCommon.KHttpIp + HttpCommon.KHttpCheckDevices;
        OKHttp.post(urlString, map, callback);

    }
    public static void addDevice(Map<String, Object> map, OKHttp.HttpCallback callback) {
        String urlString = HttpCommon.KHttpIp + HttpCommon.KHttpaddDevices;
        OKHttp.post(urlString, map, callback);

    }
}
