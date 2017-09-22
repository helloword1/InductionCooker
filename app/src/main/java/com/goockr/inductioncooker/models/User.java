package com.goockr.inductioncooker.models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by CMQ on 2017/8/10.
 */

public class User {


    public String name;

    public String token;

    public String userId;

    public String mobile;



    public Device device;




    static User user;

    public static User getInstance(){


        if (user==null) {
            user=new User();
        }
        return  user;
    }
    public  void updateUserInfoWithdict(JSONObject jsonObject)
    {
        try {

            this.name=jsonObject.getString("name");
            this.token=jsonObject.getString("token");
            this.mobile=jsonObject.getString("mobile");
            this.userId=jsonObject.getString("id");

        } catch (JSONException e) {

            e.printStackTrace();

        }

    }



}
