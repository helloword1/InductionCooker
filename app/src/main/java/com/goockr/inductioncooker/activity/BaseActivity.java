package com.goockr.inductioncooker.activity;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.goockr.inductioncooker.R;
import com.goockr.inductioncooker.common.Common;
import com.goockr.inductioncooker.lib.socket.TcpSocket;
import com.goockr.inductioncooker.models.BaseProtocol;
import com.goockr.inductioncooker.utils.NotNull;
import com.goockr.ui.view.helper.HudHelper;
import com.goockr.ui.view.view.TipDialog;
import com.google.gson.Gson;

import org.json.JSONObject;

public class BaseActivity extends Activity {
    private static final String TAG = "BaseActivity";
    private static final int ConnetContOut = 5;
    private final int READ_COUNT = 1001;
    private final String READ_TAG = "MSG";
    public static String effectStr0="";
    private static String effectStr1="";
    HudHelper bsaeHudHelper = new HudHelper();
    protected Handler baseHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {

                case READ_COUNT:
                    Bundle data = msg.getData();
                    BaseProtocol mProtocol = (BaseProtocol) data.getSerializable(READ_TAG);
                    handleMsg(mProtocol);
                    break;
            }
        }
    };

    int connetCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//竖屏

        setContentView(R.layout.activity_base);

        initData();

        ConnectSocket();

    }

    private void initData() {

        connetCount = 0;

    }

    protected void handleMsg(BaseProtocol mProtocol) {

    }

    private void ConnectSocket() {
        if (this.getClass() != HomeActivity.class) return;

        if (TcpSocket.getInstance().isConnect()) return;

        //  Log.v("","连接服务器...");

        bsaeHudHelper.hudShow(this, "连接服务器..." + connetCount);

        TcpSocket.getInstance().connect(Common.KIP, Common.KPORT, new TcpSocket.TcpSocketCallBack() {


            @Override
            public void onConnect() {
                bsaeHudHelper.hudUpdateAndHid(getResources().getString(R.string.dialog_connect), 1.5);
            }

            @Override
            public void onFailConnnect() {

                bsaeHudHelper.hudUpdate("onFailConnnect" + connetCount);

                connetCount++;
                if (connetCount < ConnetContOut) {
                    // Log.v("","接到连接失败递归..."+connetCount);

                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    ConnectSocket();
                } else {

                    bsaeHudHelper.hudHide();
                    connetCount = 0;
                    TipDialog tipDialog = new TipDialog(BaseActivity.this, getResources().getString(R.string.dialog_tip), getResources().getString(R.string.dialog_failconnect), false, true);
                    tipDialog.setActionButtonClick(new TipDialog.TipDialogCallBack() {
                        @Override
                        public void buttonClick(TipDialog dialog) {
                            ConnectSocket();
                            dialog.dismiss();
                        }
                    });
                    tipDialog.show();
                }

            }

            @Override
            public void onDisconnect() {
                ConnectSocket();
            }

            @Override
            public void onRead(String read) {
                Log.d(TAG, "onRead: 收到---》 " + read);
                try {
                    JSONObject object = new JSONObject(read);
                    JSONObject order = object.getJSONObject("order");
                    BaseProtocol mProtocol=null;
                    if (NotNull.isNotNull(order)) {
                        mProtocol = new Gson().fromJson(read, BaseProtocol.class);
                        mProtocol.setOrder(order);

                        if (order.getInt("code")==-1){
                            Log.d(TAG, "onRead: "+order.getString("msg"));
                            return;
                        }
                        String deviceId = order.getString("deviceId");
                        //过滤重复字符串
                        if (NotNull.isNotNull(deviceId)&& TextUtils.equals(deviceId,"0")){
                          if (TextUtils.equals(effectStr0,read)){
                              return;
                          }
                            effectStr0=read;

                        }else if (NotNull.isNotNull(deviceId)&&TextUtils.equals(deviceId,"1")){
                            if (TextUtils.equals(effectStr1,read)){
                                return;
                            }
                            effectStr1=read;

                        }
                    }
                    if (mProtocol==null){
                        return;
                    }

                    Message message = baseHandler.obtainMessage();
                    Bundle data = new Bundle();
                    data.putSerializable(READ_TAG, mProtocol);
                    message.setData(data);
                    message.what = READ_COUNT;
                    baseHandler.sendMessage(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


    }


}
