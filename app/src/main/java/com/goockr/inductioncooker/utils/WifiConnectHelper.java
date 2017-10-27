package com.goockr.inductioncooker.utils;

import com.goockr.inductioncooker.MyApplication;
import com.goockr.inductioncooker.common.Common;

/**
 * Created by CMQ on 2017/7/18.
 */

public class WifiConnectHelper {


    public static boolean isConnectWifi()
    {
        boolean isSuccess=false;
        if (WifiHelper.getWifiSSID(MyApplication.getContext()).contains(Common.KWIFI_NAME_HEAD))
        {
            isSuccess=true;
        }
        return isSuccess;
    }


}
