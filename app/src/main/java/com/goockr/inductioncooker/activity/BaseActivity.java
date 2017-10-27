package com.goockr.inductioncooker.activity;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.goockr.inductioncooker.R;
import com.goockr.inductioncooker.common.Common;
import com.goockr.inductioncooker.lib.socket.TcpSocket;
import com.goockr.ui.view.helper.HudHelper;
import com.goockr.ui.view.view.TipDialog;

public class BaseActivity extends Activity {
    private static final String TAG = "BaseActivity";
    private static final int CONNET_CONT_OUT = 5;
    private final int READ_COUNT = 1001;
    private final String READ_TAG = "MSG";
    HudHelper bsaeHudHelper = new HudHelper();
    protected Handler baseHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {

                case READ_COUNT:
                    Bundle data = msg.getData();
//                    BaseProtocol mProtocol = (BaseProtocol) data.getSerializable(READ_TAG);
                    String mProtocol = data.getString(READ_TAG);
                    handleMsg(mProtocol);
                    break;
                default:
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

        connectSocket();

    }

    private void initData() {
        connetCount = 0;
    }

    protected void handleMsg(String mProtocol) {

    }

    private void connectSocket() {
        if (this.getClass() != HomeActivity.class){
            return;
        }

        if (TcpSocket.getInstance().isConnect()){
            return;
        }

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
                if (connetCount < CONNET_CONT_OUT) {
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    connectSocket();
                } else { // 如果上面递归连接失败，就弹出连接超时提示框
                    bsaeHudHelper.hudHide();
                    connetCount = 0;
                    TipDialog tipDialog = new TipDialog(BaseActivity.this, getResources().getString(R.string.dialog_tip), getResources().getString(R.string.dialog_failconnect), false, true);
                    tipDialog.setActionButtonClick(new TipDialog.TipDialogCallBack() {
                        @Override
                        public void buttonClick(TipDialog dialog) {
                            connectSocket();
                            dialog.dismiss();
                        }
                    });
                    tipDialog.show();
                }

            }

            @Override
            public void onDisconnect() {
                connectSocket();
            }

            @Override
            public void onRead(String read) {
                Log.d(TAG, "onRead: 收到---> " + read);
                Message message = baseHandler.obtainMessage();
                Bundle data = new Bundle();
                data.putString(READ_TAG, read);
                message.setData(data);
                message.what = READ_COUNT;
                baseHandler.sendMessage(message);
            }
        });


    }

}
