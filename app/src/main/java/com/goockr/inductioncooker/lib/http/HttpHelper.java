package com.goockr.inductioncooker.lib.http;

import java.util.Map;

/**
 * Created by CMQ on 2017/8/9.
 */

public class HttpHelper {

    public static void getLoginSmmCode(Map<String, Object> map, OKHttp.HttpCallback callback) {
        String urlString = HttpCommon.K_HTTP_IP + HttpCommon.COOKER_SERVLET_APP_SERVLET;
        OKHttp.post(urlString, map, callback);

    }

    public static void loginSmmCode(Map<String, Object> map, OKHttp.HttpCallback callback) {
        String urlString = HttpCommon.K_HTTP_IP + HttpCommon.COOKER_SERVLET_APP_SERVLET1;
        OKHttp.post(urlString, map, callback);

    }

    public static void loginPwd(Map<String, Object> map, OKHttp.HttpCallback callback) {
        String urlString = HttpCommon.K_HTTP_IP + HttpCommon.COOKER_SERVLET_APP_SERVLET2;
        OKHttp.post(urlString, map, callback);

    }

    public static void getForgetSmmCode(Map<String, Object> map, OKHttp.HttpCallback callback) {
        String urlString = HttpCommon.K_HTTP_IP + HttpCommon.COOKER_SERVLET_APP_SERVLET3;
        OKHttp.post(urlString, map, callback);

    }

    public static void forgetPwd(Map<String, Object> map, OKHttp.HttpCallback callback) {
        String urlString = HttpCommon.K_HTTP_IP + HttpCommon.COOKER_SERVLET_APP_SERVLET4;
        OKHttp.post(urlString, map, callback);

    }

    public static void regist(Map<String, Object> map, OKHttp.HttpCallback callback) {
        String urlString = HttpCommon.K_HTTP_IP + HttpCommon.COOKER_SERVLET_APP_SERVLET5;
        OKHttp.post(urlString, map, callback);

    }

    public static void updatePwd(Map<String, Object> map, OKHttp.HttpCallback callback) {
        String urlString = HttpCommon.K_HTTP_IP + HttpCommon.COOKER_SERVLET_APP_SERVLET6;
        OKHttp.post(urlString, map, callback);

    }

    public static void tranferRight(Map<String, Object> map, OKHttp.HttpCallback callback) {
        String urlString = HttpCommon.K_HTTP_IP + HttpCommon.COOKER_SERVLET_APP_SERVLET;
        OKHttp.post(urlString, map, callback);

    }public static void tranferRightReady(Map<String, Object> map, OKHttp.HttpCallback callback) {
        String urlString = HttpCommon.K_HTTP_IP + HttpCommon.COOKER_SERVLET_APP_SERVLET;
        OKHttp.post(urlString, map, callback);

    }
    public static void checkDevice(Map<String, Object> map, OKHttp.HttpCallback callback) {
        String urlString = HttpCommon.K_HTTP_IP + HttpCommon.COOKER_A_COMM_COMM_DEVICE_REF_FIND_APP_REF;
        OKHttp.post(urlString, map, callback);

    }
    public static void addDevice(Map<String, Object> map, OKHttp.HttpCallback callback) {
        String urlString = HttpCommon.K_HTTP_IP + HttpCommon.COOKER_A_COMM_COMM_DEVICE_REF_SAVE_DEVICE_REF;
        OKHttp.post(urlString, map, callback);

    }
}
