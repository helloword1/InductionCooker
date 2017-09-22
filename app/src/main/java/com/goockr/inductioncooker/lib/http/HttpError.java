package com.goockr.inductioncooker.lib.http;

/**
 * Created by CMQ on 2017/8/9.
 */

public class HttpError {

    public int code=0;

    public String msg="";

    public HttpError(int code,String msg)
    {
        this.code=code;
        this.msg=msg;
    }

}
