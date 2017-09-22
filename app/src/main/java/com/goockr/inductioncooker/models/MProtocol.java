package com.goockr.inductioncooker.models;

import java.io.Serializable;

/**
 * Created by LJN on 2017/9/19.
 * 解析协议
 */

public class MProtocol implements Serializable{

    /**
     * id : 123456789
     * target : 13763085121
     * order : {"code":"1","deviceId":"1","moden":"0","power":"0","reservation":"false","stall":"0","worktime":0}
     * rectype : 2
     * msgtype : 1
     * type : 1
     */

    private String id;
    private String target;
    private OrderBean order;
    private int rectype;
    private int msgtype;
    private int type;

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

    public OrderBean getOrder() {
        return order;
    }

    public void setOrder(OrderBean order) {
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

    public static class OrderBean implements Serializable{
        /**
         * code : 1
         * deviceId : 1
         * moden : 0
         * power : 0
         * reservation : false
         * stall : 0
         * worktime : 0
         */

        private String code;
        private String deviceId;
        private String moden;
        private String power;
        private String reservation;
        private String stall;
        private int worktime;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getDeviceId() {
            return deviceId;
        }

        public void setDeviceId(String deviceId) {
            this.deviceId = deviceId;
        }

        public String getModen() {
            return moden;
        }

        public void setModen(String moden) {
            this.moden = moden;
        }

        public String getPower() {
            return power;
        }

        public void setPower(String power) {
            this.power = power;
        }

        public String getReservation() {
            return reservation;
        }

        public void setReservation(String reservation) {
            this.reservation = reservation;
        }

        public String getStall() {
            return stall;
        }

        public void setStall(String stall) {
            this.stall = stall;
        }

        public int getWorktime() {
            return worktime;
        }

        public void setWorktime(int worktime) {
            this.worktime = worktime;
        }
    }
}
