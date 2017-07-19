package com.goockr.inductioncooker.utils;

import android.os.Handler;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

/**
 * Created by CMQ on 2017/7/15.
 */

public class TcpSocket {

    private static final int MAX_IN_SIZE = 1024;

    private static TcpSocket instance;

    public static synchronized TcpSocket getInstance(){

        if (instance==null)
        {
            instance=new TcpSocket();
        }
        return instance;
    }

    private Handler mHandler;
    private Socket socket;

    private TcpSocketCallBack callBack;

    private boolean state;

    public TcpSocket()
    {
        mHandler=new Handler();
    }

    public void connect(final String host,final int port,final TcpSocketCallBack callBack)
    {
        this.callBack=callBack;

        new Thread(){
            @Override
            public void run() {
                DataInputStream reader;

                try {

                    socket = new Socket();
                    SocketAddress socAddress = new InetSocketAddress(host, port);
                    socket.connect(socAddress, 2000);

                } catch (IOException e) {

                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            callBack.onFailConnnect();
                        }
                    });
                    e.printStackTrace();

                    return;
                }

                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        callBack.onConnect();
                    }
                });

                try {
                    // 获取读取流
                    reader = new DataInputStream( socket.getInputStream());

                    state=true;
                    while (state) {
                        byte[] buffer = new byte[MAX_IN_SIZE];
                        System.out.println("*等待客户端输入*");
                        // 读取数据
                        int length = reader.read(buffer);
                        if (length==-1)
                        {

                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    callBack.onDisconnect();
                                }
                            });

                            try {
                                socket.close();
                                socket=null;
                                state=false;
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        }else {

                            final byte[] bytes=buffer;
                            final int le=length;
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    callBack.onRead(bytes,le);
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

    /**
     * 从参数的Socket里获取最新的消息
     */
    private  boolean startReader(final Socket socket) {



        return true;
    }





    public interface TcpSocketCallBack
    {
        void onConnect();

        void onFailConnnect();

        void onDisconnect();

        void onRead(byte[] buffer,int length);

    }




}
