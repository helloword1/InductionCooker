package com.goockr.inductioncooker.lib.socket;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by CMQ on 2017/8/4.
 */

public class Protocol2 {
    private static final String TAG = "Protocol2";

    private static final String targetId = "123456789";//消息接收方ID(多个用逗号分隔);
//    private static String Phone = "13763065125";
    private static String Phone = "0";

//    private static final String targetId = "8a1c66cb3190282919a";//消息接收方ID(多个用逗号分隔);
//    private static final String Phone = "17620830220";

//    private static final String targetId = "131c92862d902c1c134";//消息接收方ID(多个用逗号分隔);
//    private static final String Phone = "13763085121";

    public static int deviceId = 0;

    //心跳
    public static byte[] Heartbeat(int deviceId) {
//    * type=客户端类型; （1=设备(电磁炉)；2=手机app；3=服务器）
//    * id=客户端标识ID；（APP：用户账号，主机：设备ID）
//    * msgtype=消息类型；（-1=心跳; 1=指令；2=推送）
//    * rectype=消息接收方类型；（-1=心跳; 1=发给设备；2=发给app; 3=发给所有（设备和app））
//    * target=消息接收方ID(多个用逗号分隔);
//    * order=指令内容；（主机与APP交换的数据，第二部分协议主要针对该参数）
        JSONObject jsonObjec = textAppending();
        JSONObject json;
        try {
            json = jsonObjec.getJSONObject("order");
            json.put("code", 1);
            json.put("deviceId", deviceId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "Heartbeat: 心跳 " + jsonObjec.toString());
        return jsonObjec.toString().getBytes();
    }


    /**
     * 查询状态或指令回复
     *
     * @param code     指令码
     * @param deviceId 炉号
     * @return
     */
    public static byte[] statusCheck(int code, int deviceId) {
        JSONObject jsonObjec = textAppending();
        JSONObject order;
        try {
            order = jsonObjec.getJSONObject("order");
            order.put("code", code);
            order.put("deviceId", deviceId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "statusCheck: " + jsonObjec.toString());
        return jsonObjec.toString().getBytes();
    }

    /**
     * 查询状态或指令回复
     *
     * @param code     指令码
     * @param deviceId 炉号
     * @return
     */
    public static String statusCheckString(int code, int deviceId) {
        JSONObject order = new JSONObject();
        try {
            order.put("code", code);
            order.put("deviceId", deviceId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return order.toString();
    }

    /**
     * 开关机回复
     *
     * @param deviceId 炉号
     * @param state    开机是否成功
     * @return
     */
    public static byte[] powerStatus(int deviceId, int state) {
        JSONObject jsonObjec = textAppending();
        JSONObject order;
        try {
            order = jsonObjec.getJSONObject("order");
            order.put("code", 2);
            order.put("deviceId", deviceId);
            order.put("power", state);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObjec.toString().getBytes();
    }

    /**
     * @param deviceId   炉号
     * @param powerState 0:关机,1:开机
     * @param stalls     功率档位
     * @param moden      当前模式
     * @param error      故障码
     * @return
     */
    public static byte[] deviceStatus(int deviceId, int powerState, int stalls, int moden, int error) {
        JSONObject jsonObjec = textAppending();
        JSONObject order;
        try {
            order = jsonObjec.getJSONObject("order");
            order.put("deviceId", deviceId);
            order.put("power", powerState);
            order.put("stall", stalls);
            order.put("moden", moden);
            order.put("error", error);
            order.put("reservation", true);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObjec.toString().getBytes();

    }

    /**
     * 模式设定
     *
     * @param deviceId 炉号 左炉 0 右炉 1
     * @param mod      当前模式
     * @return
     */
    public static byte[] moden(int deviceId, int mod) {
        JSONObject jsonObjec = textAppending();
        JSONObject order;
        try {
            order = jsonObjec.getJSONObject("order");
            order.put("code", 3);
            order.put("deviceId", deviceId);
            order.put("moden", mod);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObjec.toString().getBytes();
    }

    /**
     * 档位设定
     *
     * @param deviceId 炉号 左炉 0 右炉 1
     * @param stall    当前档位
     * @return
     */
    public static byte[] stall(int deviceId, int stall) {
        JSONObject jsonObjec = textAppending();
        JSONObject order;
        try {
            order = jsonObjec.getJSONObject("order");
            order.put("code", 4);
            order.put("deviceId", deviceId);
            order.put("stall", stall);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObjec.toString().getBytes();
    }

    /**
     * 工作时间查询
     *
     * @param deviceId 炉号 左炉 0 右炉 1
     * @param mode     当前模式
     * @return
     */
    public static byte[] timeStatus(int deviceId, int mode) {
        JSONObject jsonObjec = textAppending();
        JSONObject order;
        try {
            order = jsonObjec.getJSONObject("order");
            order.put("code", 5);
            order.put("deviceId", deviceId);
            order.put("moden", mode);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObjec.toString().getBytes();
    }

    /**
     * 设置/取消预约时间
     *
     * @param deviceId    炉号 左炉 0 右炉 1
     * @param mode        当前模式
     * @param time        开机时间
     * @param appointment 工作长度
     * @param setting     是否取消预约
     * @return
     */
    public static byte[] setReservation(int deviceId, int mode, int setting, long time, long appointment) {
        JSONObject jsonObjec = textAppending();
//        JSONObject jsonObjec = textAppending();
        JSONObject order;
        try {
            order = jsonObjec.getJSONObject("order");
            order.put("code", 6);
            order.put("setting", setting);
            order.put("deviceId", deviceId);
            order.put("moden", mode);
            order.put("bootTime", time);
            order.put("appointment", appointment);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "setReservation: " + jsonObjec.toString());
        return jsonObjec.toString().getBytes();
    }

    /**
     * 查询预约
     *
     * @param deviceId 炉号
     * @return
     */
    public static byte[] getReservationStatus(int deviceId) {
        JSONObject jsonObjec = textAppending();
        JSONObject order;
        try {
            order = jsonObjec.getJSONObject("order");
            order.put("code", 7);
            order.put("deviceId", deviceId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "getReservationStatus: " + jsonObjec.toString());
        return jsonObjec.toString().getBytes();
    }

    /**
     * 设置/取消定时时间
     *
     * @param setting 1是设置预约，0是取消预约
     * @param deviceId 左右炉
     * @param mode 模式
     * @param worktime 时间长度
     * @return
     */
    public static byte[] setCookTime(int setting,int deviceId,int mode,long worktime) {
        JSONObject jsonObject = textAppending();
        JSONObject order;
        try {
            order = jsonObject.getJSONObject("order");
            order.put("code",8);
            order.put("setting",setting);
            order.put("deviceId",deviceId);
            order.put("mode",mode);
            order.put("worktime",worktime);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "setCookTime: " + jsonObject.toString());
        return jsonObject.toString().getBytes();
    }

    /**
     * 发送指令
     *
     * @return 指令头
     */
    private static JSONObject textAppending() {
        JSONObject json = new JSONObject();
        JSONObject order = new JSONObject();
        try {
            json.put("type", 2);//表示app
            json.put("id", Phone);
            json.put("msgtype", 1);//表示指令
            json.put("rectype", 1);//接收方类型 发给设备
            json.put("target", targetId);//接收放ID
            json.put("order", order);//指令内容

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }

    /**
     * 发送指令 测试
     *
     * @return 指令头
     */
    private static JSONObject textAppending1() {
        JSONObject json = new JSONObject();
        JSONObject order = new JSONObject();
        try {
            json.put("type", 2);//表示app
            json.put("id", "13763085121");
            json.put("msgtype", 1);//表示指令
            json.put("rectype", 1);//接收方类型 发给设备
            json.put("target", "12450");//接收放ID
            json.put("order", order);//指令内容

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }

    /**
     * 停止接收错误信息
     *
     * @param deviceId 炉号
     * @return
     */
    public static byte[] stopErro(int deviceId) {
        JSONObject jsonObjec = textAppending();
        JSONObject order;
        try {
            order = jsonObjec.getJSONObject("order");
            order.put("code", 9);
            order.put("deviceId", deviceId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObjec.toString().getBytes();
    }

    public static void setPhone(String num) {
        Phone = num;
    }
}
