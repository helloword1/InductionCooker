package com.goockr.inductioncooker.utils;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.DhcpInfo;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by JJT-ssd on 2016/8/31.
 */
public class WifiHelper {

    /**
     * 检查当前网络是否可用
     */
    static ConnectivityManager connectivityManager;
    public static boolean isNetworkAvailable(Context mContext)
    {
        Map netWorkState = WifiHelper.networkState(mContext);
        boolean isAvailable = (boolean)netWorkState.get("isNetworkAvailable");
        return isAvailable;
    }

    public static Map networkState(Context mContext)
    {
        Context context = mContext.getApplicationContext();
        // 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
        connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        Map maps =new HashMap();
        maps.put("isNetworkAvailable",false);
        if (connectivityManager == null)
        {
            maps.put("isNetworkAvailable",false);
            return maps;
        }
        else
        {
            // 获取NetworkInfo对象
            NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();

            if (networkInfo != null && networkInfo.length > 0)
            {
                for (int i = 0; i < networkInfo.length; i++)
                {
  //                  System.out.println(i + "===状态===" + networkInfo[i].getState());
                    //                 System.out.println(i + "===类型===" + networkInfo[i].getTypeName());
                    maps.put(networkInfo[i].getTypeName(),networkInfo[i].getState());
                    // 判断当前网络状态是否为连接状态
                    if (networkInfo[i].getState() == NetworkInfo.State.CONNECTED)
                    {
                        maps.put("isNetworkAvailable",true);
                    }
                }
            }
        }
        return maps;
    }



    public static String getWifiIP(WifiManager wifiManager)
    {
        //获取wifi下网关
        DhcpInfo dhcpInfo =  wifiManager.getDhcpInfo();
        int gateway =dhcpInfo.ipAddress;
        return intToIp(gateway);
    }

    public static String getWifiGateWay(Context mContext)
    {
        WifiManager wifiManager =(WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
        //获取wifi下网关
        DhcpInfo dhcpInfo =  wifiManager.getDhcpInfo();

        int gateway =dhcpInfo.gateway;
        return intToIp(gateway);
    }

    public static String getEthernetIP()
    {
        String iP=null;
        NetworkInfo info  = connectivityManager.getActiveNetworkInfo();
        if (info!=null) {
            boolean isConnect = info.isAvailable();
            if (isConnect == true) {
                //得到ip
                Process process = null;
                try {
                    process = Runtime.getRuntime().exec("su");
                    DataInputStream dis = new DataInputStream(process.getInputStream());
                    DataOutputStream dos = new DataOutputStream(process.getOutputStream());
                    dos.writeBytes("getprop | grep eth0.ipaddress" + "\n");
                    dos.flush();
                    iP = dis.readLine();
                    iP = iP.split(":")[1];
                    iP = iP.substring(2, iP.length() - 1);
                } catch (IOException e) {
                }
            }
        }
        return iP;
    }

    public static String getEthernetGateWay()
    {
        String gateWay=null;
        //获取有线下网关
        NetworkInfo info  = connectivityManager.getActiveNetworkInfo();
        if (info!=null) {
            boolean isConnect = info.isAvailable();
            if (isConnect == true) {
                //得到网关
                Process process = null;
                try {
                    process = Runtime.getRuntime().exec("su");
                    DataInputStream dis = new DataInputStream(process.getInputStream());
                    DataOutputStream dos = new DataOutputStream(process.getOutputStream());
                    dos.writeBytes("getprop | grep eth0.gateway" + "\n");
                    dos.flush();
                    gateWay = dis.readLine();
                    gateWay = gateWay.split(":")[1];
                    gateWay = gateWay.substring(2, gateWay.length() - 1);
                } catch (IOException e) {
                }
            }
        }
        return gateWay;
    }

    /**
     * @param context
     * @return
     */
    public static String getWifiSSID(Context context)
    {
        WifiManager wifiManager = (WifiManager)context.getSystemService(context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
//        Log.d("wifiInfo", wifiInfo.toString());
//        Log.d("SSID",wifiInfo.getSSID());

        String ssid=wifiInfo.getSSID()==null?"":wifiInfo.getSSID();

        return  ssid;
    }

    //IP地址转换
    private static String intToIp(int i) {

        return (i & 0xFF ) + "." +
                ((i >> 8 ) & 0xFF) + "." +
                ((i >> 16 ) & 0xFF) + "." +
                ( i >> 24 & 0xFF) ;
    }





}
