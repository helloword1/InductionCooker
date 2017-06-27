package com.goockr.inductioncooker.models;

import android.content.res.Resources;

import java.util.Date;

/**
 * Created by CMQ on 2017/6/23.
 */

public class NotiseAdapterModel {

    public int imageId;

    public String title;

    public Date date;

    public NotiseAdapterModel(int imageId, String title, Date date)
    {
        this.imageId=imageId;

        this.title=title;

        this.date=date;
    }



}
