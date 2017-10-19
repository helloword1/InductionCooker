package com.goockr.inductioncooker.lib.http;

import android.os.Handler;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by CMQ on 2017/8/9.
 */

public class OKHttp {


    static Handler mHandler = new Handler();

    public static void post(String url, Map<String, Object> map, final HttpCallback callback) {
        OkHttpClient client = new OkHttpClient();
        // RequestBody body = new FormBody.Builder().add("price", "25").add("历史", "张三").build();

        FormBody.Builder builder = new FormBody.Builder();
        for (String key : map.keySet()) {
            Object value = map.get(key);
            builder.add(key, value.toString());
        }
        RequestBody body = builder.build();

        // RequestBody body = new FormBody.Builder().add("functype", "vc").add("mobile", "13763085121").build();

        Request request = new Request.Builder().url(url).post(body).build();
        Call call = client.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        callback.onFailure(new HttpError(-1, "网络请求错误"));
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String result = response.body().string();
                Log.v("OKHttp", "http接收到数据：" + result);

                JSONObject jsonObject = new JSONObject();
                int code = 0;
                String msg = "";
                try {
                    jsonObject = new JSONObject(result);
                    code = jsonObject.getInt("result");
                    if (code != 0) {
                        msg = jsonObject.getString("msg");
                    }
                } catch (JSONException e) {

                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            callback.onFailure(new HttpError(-1, "网络请求异常"));
                        }
                    });
                    e.printStackTrace();

                }

                if (response.isSuccessful()) {

                    if (code == 0) {
                        final JSONObject respone = jsonObject;
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                callback.onSuccess(respone);
                            }
                        });

                    } else {

                        final int errorCode = code;

                        final String errorMsg = msg;
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                callback.onFailure(new HttpError(errorCode, errorMsg));
                            }
                        });
                    }

                } else {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            callback.onFailure(new HttpError(-1, "网络请求失败"));
                        }
                    });
                }
            }
        });
    }

    public interface HttpCallback {

        void onFailure(HttpError error);

        void onSuccess(JSONObject jsonObject);

    }


}
