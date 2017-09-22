package com.goockr.inductioncooker.lib.http;

import com.goockr.inductioncooker.common.Common;

import java.io.IOException;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by CMQ on 2017/8/9.
 */

public class HttpHelper {

    public static void getLoginSmmCode(Map<String,Object> map, OKHttp.HttpCallback  callback)
    {
        String urlString= HttpCommon.KHttpIp+HttpCommon.KHttpLoginSmsCode;
        OKHttp.post(urlString,map,callback);

    }



    public static void loginSmmCode(Map<String,Object> map, OKHttp.HttpCallback  callback)
    {
        String urlString= HttpCommon.KHttpIp+HttpCommon.KHttpLoginSms;
        OKHttp.post(urlString,map,callback);

    }
    public static void loginPwd(Map<String,Object> map, OKHttp.HttpCallback  callback)
    {
        String urlString= HttpCommon.KHttpIp+HttpCommon.KHttpLoginPwd;
        OKHttp.post(urlString,map,callback);

    }

    public static void getForgetSmmCode(Map<String,Object> map, OKHttp.HttpCallback  callback)
    {
        String urlString= HttpCommon.KHttpIp+HttpCommon.KHttpForgetCode;
        OKHttp.post(urlString,map,callback);

    }

    public static void forgetPwd(Map<String,Object> map, OKHttp.HttpCallback  callback)
    {
        String urlString= HttpCommon.KHttpIp+HttpCommon.KHttpForgetPwd;
        OKHttp.post(urlString,map,callback);

    }

    public static void regist(Map<String,Object> map, OKHttp.HttpCallback  callback)
    {
        String urlString= HttpCommon.KHttpIp+HttpCommon.KHttpRegist;
        OKHttp.post(urlString,map,callback);

    }

    public static void updatePwd(Map<String,Object> map, OKHttp.HttpCallback  callback)
    {
        String urlString= HttpCommon.KHttpIp+HttpCommon.KHttpUpdatePwd;
        OKHttp.post(urlString,map,callback);

    }

}
