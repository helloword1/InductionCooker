package com.goockr.inductioncooker.models;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by LJN on 2017/9/20.
 */

public class BaseProtocol implements Serializable{
    private String id;
    private String target;
    private JSONObject order;
    private int rectype;
    private int msgtype;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public JSONObject getOrder() {
        return order;
    }

    public void setOrder(JSONObject order) {
        this.order = order;
    }

    public int getRectype() {
        return rectype;
    }

    public void setRectype(int rectype) {
        this.rectype = rectype;
    }

    public int getMsgtype() {
        return msgtype;
    }

    public void setMsgtype(int msgtype) {
        this.msgtype = msgtype;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    private int type;
}
