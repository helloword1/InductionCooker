package com.goockr.inductioncooker.utils;

import android.content.res.AssetManager;

import com.goockr.inductioncooker.MyApplication;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by CMQ on 2017/8/8.
 */

public class ReadAdssetsJson {

    public  static JSONObject readJson(String fileName)
    {
        String str = "";
        AssetManager assetManager = MyApplication.getContext().getAssets();
        try {
            InputStream is = assetManager.open(fileName+".json");
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            StringBuffer stringBuffer = new StringBuffer();

            while((str = br.readLine())!=null){
                stringBuffer.append(str);
            }

            str=new String(stringBuffer);


        } catch (IOException e) {

            e.printStackTrace();
        }

        JSONObject object= null;
        try {
            object = new JSONObject(str);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return  object;
    }

}
