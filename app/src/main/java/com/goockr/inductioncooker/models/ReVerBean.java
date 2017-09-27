package com.goockr.inductioncooker.models;

/**
 * Created by LJN on 2017/9/22.
 */

public class ReVerBean {

    /**
     * id : 123456789
     * target : 17620830220
     * order : {"appointment":"86280000","bootTime":"1506162341748","code":"7","deviceId":"0","error":"0","moden":"4","worktime":0}
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

    public static class OrderBean {
        /**
         * appointment : 86280000
         * bootTime : 1506162341748
         * code : 7
         * deviceId : 0
         * error : 0
         * moden : 4
         * worktime : 0
         */

        private String appointment;
        private String bootTime;
        private String code;
        private String deviceId;
        private String error;
        private String moden;
        private int worktime;

        public String getAppointment() {
            return appointment;
        }

        public void setAppointment(String appointment) {
            this.appointment = appointment;
        }

        public String getBootTime() {
            return bootTime;
        }

        public void setBootTime(String bootTime) {
            this.bootTime = bootTime;
        }

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

        public String getError() {
            return error;
        }

        public void setError(String error) {
            this.error = error;
        }

        public String getModen() {
            return moden;
        }

        public void setModen(String moden) {
            this.moden = moden;
        }

        public int getWorktime() {
            return worktime;
        }

        public void setWorktime(int worktime) {
            this.worktime = worktime;
        }
    }
}
