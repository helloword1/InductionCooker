package com.goockr.inductioncooker.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

/**
 * Created by CMQ on 2017/8/8.
 */

public class Moden {

    /*模式Id*/
    public int modenId;

    /*是否自动模式*/
    public boolean aotuWork;

    /*默认档位*/
    public JSONArray defaultStalls;

    /*默认时间*/
    public int defaultTime;

    /* 是否支持预约*/
    public  boolean reservation;

    /*是否支持定时*/
    public boolean isTiming;

    /*是否需要显示温度*/
    public boolean showTemperature;

    /*档位*/
    public JSONArray stalls;

    /*是否支持定时取消*/
    public boolean timeCancel;

    /*模式名字*/
    public  String type;

    /*预约日期*/
    public Date reservationStartDate;

    /*预约工作时间*/
    public int reservationWorkTime;

    /*当前档位*/
    public int currentStall;

    public  static  Moden moden(Object object)
    {

        JSONObject jsonObject =(JSONObject)object ;

        Moden moden=new Moden();

//        "modenId": 0,
//            "type": "煲粥",
//            "aotuWork": true,
//            "isTiming": true,
//            "defaultTime": 0,
//            "timeCancel": false,
//            "reservation": true,
//            "stalls": [],
//        "showTemperature":false,
//            "defaultStalls": []

        try {
            moden.modenId=jsonObject.getInt("modenId");
            moden.type=jsonObject.getString("type");
            moden.aotuWork=jsonObject.getBoolean("aotuWork");
            moden.isTiming=jsonObject.getBoolean("isTiming");
            moden.defaultTime=jsonObject.getInt("defaultTime");
            moden.timeCancel=jsonObject.getBoolean("timeCancel");
            moden.reservation=jsonObject.getBoolean("reservation");
            moden.stalls=  jsonObject.getJSONArray("stalls");
            moden.showTemperature=jsonObject.getBoolean("showTemperature");
            moden.defaultStalls=jsonObject.getJSONArray("defaultStalls");

            moden.reservationWorkTime=0;

            moden.currentStall=-1;

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return moden;
    }



}
