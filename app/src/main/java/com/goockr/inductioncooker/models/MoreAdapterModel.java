package com.goockr.inductioncooker.models;

/**
 * Created by CMQ on 2017/6/22.
 */

public class MoreAdapterModel {

    public String title;

    public String des;

    public boolean isGo;
    public boolean isCenter;

    public MoreAdapterModel(String title,String des ,boolean isGo)
    {
        this.title=title;

        this.des=des;

        this.isGo=isGo;

    }

    public MoreAdapterModel setCenter(boolean center) {
        isCenter = center;
        return this;
    }
}
