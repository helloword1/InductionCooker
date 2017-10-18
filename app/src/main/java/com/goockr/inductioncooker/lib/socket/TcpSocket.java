package com.goockr.inductioncooker.lib.socket;

import android.os.Handler;
import android.util.Log;

import com.goockr.inductioncooker.utils.NotNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;

/**
 * Created by CMQ on 2017/7/15.
 */

public class TcpSocket {
    private static final String TAG = "TcpSocket";
    private static final int MAX_IN_SIZE = 1024;

    private static TcpSocket instance;
    private Thread heartBeatRequestThread;
    private getStringCallback McallBack;
    private Thread mDeviceTimeThread;

    public static synchronized TcpSocket getInstance() {

        if (instance == null) {
            instance = new TcpSocket();
        }
        return instance;
    }

    private Handler mHandler;
    private Socket socket;

    private TcpSocketCallBack callBack;

    private boolean state;

    Thread heartBeatThread;

    private static final String codeKey = "code";
    private static final String deviceIdKey = "deviceId";

    private static final String powerKey = "power";

    private static final String modenKey = "moden";

    private static final String stallKey = "stall";

    private static final String reservationKey = "reservation ";

    private static final String errorKey = "error";

    private static final String orderKey = "order";

    public TcpSocket() {
        mHandler = new Handler();

    }

    /**
     *
     * @param host  主机地址
     * @param port  端口号
     * @param callBack  回调
     */
    public void connect(final String host, final int port, final TcpSocketCallBack callBack) {
        this.callBack = callBack;

        new Thread() {
            @Override
            public void run() {
                DataInputStream reader;

                boolean isConnect = true;
                try {
                    socket = new Socket();
                    SocketAddress socAddress = new InetSocketAddress(host, port);
                    //  Log.v("","开始连接");
                    socket.connect(socAddress, 2000);
                } catch (UnknownHostException e) {
                    isConnect = false;
                    e.printStackTrace();
                } catch (IOException e) {
                    isConnect = false;
                    e.printStackTrace();
                } catch (Exception e) {
                    isConnect = false;
                    e.printStackTrace();
                }
                //  Log.v("","连接结果："+isConnect);
                if (isConnect == false) {
                    // Log.v("","连接失败回调："+isConnect);
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            callBack.onFailConnnect();
                        }
                    });
                    disConnect();
                    return;
                }
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        callBack.onConnect();
                    }
                });

                heartBeatThread = new Thread(heartbeatRunnable);
                heartBeatThread.start();
                heartBeatRequestThread = new Thread(heartbeatRequestRunnable);
                heartBeatRequestThread.start();

                try {
                    // 获取读取流
                    reader = new DataInputStream(socket.getInputStream());
                    while (socket != null) {
                        if (socket.isClosed()) {
                            break;
                        }
                        byte[] buffer = new byte[MAX_IN_SIZE];
                        // 读取数据
                        int length = reader.read(buffer);
                        if (length == -1) {
                            close();
                        } else {
                            final byte[] bytes = buffer;
                            final int le = length;
                            final String reciveStr = new String(bytes, 0, le);
                            if (reciveStr.contains("heartbeat")) {
                                continue;
                            } else {
                                JSONObject jsonObject = null;
                                try {
                                    jsonObject = new JSONObject(reciveStr);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    callBack.onRead(reciveStr);
                                }
                            });
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public void connect(final String host, final int port, final TcpSocketCallBack callBack, final int count) {
        this.callBack = callBack;

        new Thread() {
            @Override
            public void run() {
                DataInputStream reader;

                boolean isConnect = true;
                try {
                    socket = new Socket();
                    SocketAddress socAddress = new InetSocketAddress(host, port);
                    Log.v("", "开始连接" + count);
                    socket.connect(socAddress, 2000);
                } catch (UnknownHostException e) {
                    isConnect = false;
                    e.printStackTrace();

                } catch (IOException e) {
                    isConnect = false;
                    e.printStackTrace();
                } catch (Exception e) {
                    isConnect = false;
                    e.printStackTrace();
                }

                Log.v("", count + "次连接结果：" + isConnect);

                if (isConnect == false) {
                    Log.v("", count + "次连接失败回调：" + isConnect);
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            callBack.onFailConnnect();
                        }
                    });
                    disConnect();
                    return;
                }

                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        callBack.onConnect();
                    }
                });
                heartBeatThread = new Thread(heartbeatRunnable);
                heartBeatThread.start();
                heartBeatRequestThread = new Thread(heartbeatRequestRunnable);
                heartBeatRequestThread.start();
                try {
                    // 获取读取流
                    reader = new DataInputStream(socket.getInputStream());
                    while (socket != null) {
                        if (socket.isClosed()) break;
                        byte[] buffer = new byte[MAX_IN_SIZE];
                        System.out.println("*等待客户端输入*");
                        // 读取数据
                        int length = reader.read(buffer);
                        if (length == -1) {
                            close();
                        } else {
                            final byte[] bytes = buffer;
                            final int le = length;
                            final String reciveStr = new String(bytes, 0, le);
                            if (reciveStr.contains("heartbeat")) {
                                Log.v("", reciveStr);
                                continue;
                            } else {
                                JSONObject jsonObject = null;
                                try {
                                    jsonObject = new JSONObject(reciveStr);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    callBack.onRead(reciveStr);
                                }
                            });


                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();


    }


    public void send(String text) {
        DataOutputStream out = null;
        try {
            if (socket == null) return;
            out = new DataOutputStream(socket.getOutputStream());
            //out.writeUTF();
            byte[] bytes = text.getBytes();
            out.write(bytes);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void send(byte b[]) {
        DataOutputStream out = null;
        try {

            if (socket == null) return;

            out = new DataOutputStream(socket.getOutputStream());

            out.write(b);

            out.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void write(byte b[]) {
        DataOutputStream out = null;
        try {
            if (socket == null) return;
            out = new DataOutputStream(socket.getOutputStream());
            Log.v("write", new String(b));
            out.write(b);
            out.flush();
        } catch (IOException e) {
            isServerClose(socket);
            e.printStackTrace();
        }
    }

    public void read() {
        DataInputStream in = null;
        try {
            if (socket == null) return;
            in = new DataInputStream(socket.getInputStream());
            String s = String.valueOf(in);
            Log.d(TAG, "read: " + s);
            while (socket != null) {
                if (socket.isClosed()) break;

                byte[] buffer = new byte[MAX_IN_SIZE];
                // 读取数据
                int length = in.read(buffer);
                if (length == -1) {
                    close();
                } else {
                    final byte[] bytes = buffer;
                    final int le = length;
                    String reciveStr = new String(bytes, 0, le);
                    // 再读一次*/
                    callBack.onRead(reciveStr);
                    if (NotNull.isNotNull(McallBack))
                    McallBack.onRead(reciveStr);
                }
            }
        } catch (Exception e) {
            isServerClose(socket);
            e.printStackTrace();
        }

    }

    public boolean isConnect() {
        boolean isConnect = false;
        if (socket == null || socket.isClosed()) {
            isConnect = false;
        } else {
            isConnect = true;
        }
        return isConnect;
    }

    /**
     * 判断是否断开连接，断开返回true,没有返回false
     *
     * @param socket
     * @return
     */
    public Boolean isServerClose(Socket socket) {
        try {
            socket.sendUrgentData(0);//发送1个字节的紧急数据，默认情况下，服务器端没有开启紧急数据处理，不影响正常通信
            return false;
        } catch (Exception se) {

            close();

            return true;
        }
    }

    /**
     * 从参数的Socket里获取最新的消息
     */
    private boolean startReader(final Socket socket) {

        return true;
    }

    public interface TcpSocketCallBack {
        void onConnect();

        void onFailConnnect();

        void onDisconnect();

        void onRead(String s);

    }public interface getStringCallback {
        void onRead(String s);

    }

    public void setTcpSocketCallBack(getStringCallback callBack) {
        this.McallBack = callBack;
    }

    public void disConnect() {
        if (heartBeatThread != null) {
            heartBeatThread.interrupt();
        }
        if (heartBeatRequestThread != null) {
            heartBeatRequestThread.interrupt();
        }

        if (socket != null) {
            try {
                socket.close();
                socket = null;
            } catch (IOException e1) {
                e1.printStackTrace();
            }

        }
    }

    private void close() {
        if (heartBeatThread != null) {
            heartBeatThread.interrupt();
        }
        if (heartBeatRequestThread != null) {
            heartBeatRequestThread.interrupt();
        }
        if (socket != null) {
            try {
                socket.close();
                socket = null;
            } catch (IOException e1) {
                e1.printStackTrace();
            }

        }
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                callBack.onDisconnect();
            }
        });
    }

    //线程发送心跳包
    Runnable heartbeatRunnable = new Runnable() {
        @Override
        public void run() {
            while (!heartBeatThread.isInterrupted()) {
                try {
                    write(Protocol2.Heartbeat(0));
                    Thread.sleep(1000);
                    write(Protocol2.Heartbeat(1));
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    new Handler().post(new Runnable() {
                        @Override
                        public void run() {
                            Log.d(TAG, "run: 断开连接");
                        }
                    });
                    //断开连接
                    e.printStackTrace();
                    break;
                }
            }
            heartBeatThread = null;
        }
    };


    //线程接收心跳包
    Runnable heartbeatRequestRunnable = new Runnable() {
        @Override
        public void run() {
            while (!heartBeatRequestThread.isInterrupted()) {
                try {
                    read();
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    break;
                }
            }
            heartBeatRequestThread = null;
        }
    };

    public Socket getSocket() {
        return socket;
    }
}
