package com.elite.display01.utils.serialport;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.elite.display01.AppApplication;
import com.elite.display01.activity.MainActivity;
import com.elite.display01.config.AppConfig;
import com.elite.display01.utils.ExceptionUtil;
import com.elite.display01.utils.LogUtil;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android_serialport_api.SerialPort;

/**
 * Created by Administrator on 2018/1/29.
 */

public class SerialPortUtil {

    private static final String TAG_TEST = "SerialPort";

    public static SerialPort serialPort = null;
    public static InputStream inputStream = null;
    public static OutputStream outputStream = null;

    public static SharedPreferences sp;
    public static boolean flag = false;

    public static Thread receiveThread = null;
    public static Handler mainHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (MainActivity.instance != null) {
                Bundle bundle = msg.getData();
                MainActivity.instance.receiveDatas(bundle.getByteArray("bytes")
                        , bundle.getInt("size"), bundle.getString("data"));
            }
        }
    };

    /**
     * 打开串口的方法
     */
    public static void openSerialPort(){
        LogUtil.i(TAG_TEST, "打开串口");
        try {
            sp = AppApplication.getSharedPreferences();
            serialPort = new SerialPort(new File(
                    sp.getString(AppConfig.KEY_PORT, AppConfig.DEFAULT_PORT)),
                    sp.getInt(AppConfig.KEY_BAUD, AppConfig.DEFAULT_BAUD), 0);
            //获取打开的串口中的输入输出流，以便于串口数据的收发
            inputStream = serialPort.getInputStream();
            outputStream = serialPort.getOutputStream();
            flag = true;
            receiveSerialPort();
        } catch (IOException e) {
            ExceptionUtil.handle(e);
        }
    }

    /**
     * 关闭串口的方法
     * 关闭串口中的输入输出流
     * 然后将flag的值设为flag，终止接收数据线程
     */
    public static void closeSerialPort(){
        LogUtil.i(TAG_TEST, "关闭串口");
        try {
            if(inputStream != null) {
                inputStream.close();
            }
            if(outputStream != null) {
                outputStream.close();
            }
            if (mainHandler != null) {
                mainHandler.removeCallbacksAndMessages(null);
            }
            flag = false;
        } catch (IOException e) {
            ExceptionUtil.handle(e);
        }
    }

    /**
     * 发送串口数据的方法
     * @param data 要发送的数据
     */
    public static void sendSerialPort(String data){
        LogUtil.i(TAG_TEST, "发送串口数据");
        try {
            //byte[] sendData = data.getBytes();
            byte[] sendData = MyFunc.HexToByteArr(data);
            outputStream.write(sendData);
            outputStream.flush();
            LogUtil.i(TAG_TEST, "串口数据发送成功");
        } catch (IOException e) {
            ExceptionUtil.handle(e);
            LogUtil.i(TAG_TEST, "串口数据发送失败");
        }
    }

    /**
     * 接收串口数据的方法
     */
    public static void receiveSerialPort(){
        LogUtil.i(TAG_TEST, "启动接收串口数据线程");
        //创建子线程接收串口数据
        receiveThread = new Thread(){
            @Override
            public void run() {
                while (flag) {
                    try {
                        byte[] readData = new byte[1024];
                        if (inputStream == null) {
                            return;
                        }
                        int size = inputStream.read(readData);
                        if (size > 0 && flag) {
                            //String recinfo = new String(readData,0,size);
                            String recinfo = MyFunc.ByteArrToHex(readData);
                            if (recinfo.contains(" 00 ")) {
                                recinfo = recinfo.substring(0, recinfo.indexOf(" 00 "));
                            }
                            LogUtil.i(TAG_TEST, "接收到串口数据:" + recinfo);
                            //将接收到的数据封装进Message中，然后发送给主线程
                            Message message = new Message();
                            Bundle bundle = new Bundle();
                            bundle.putByteArray("bytes", readData);
                            bundle.putInt("size", size);
                            bundle.putString("data",recinfo);
                            message.setData(bundle);
                            mainHandler.sendMessage(message);
                        }
                    } catch (IOException e) {
                        ExceptionUtil.handle(e);
                    }
                }
            }
        };
        //开启线程
        receiveThread.start();
    }

    /**
     * 测试串口权限的方法
     */
    public static boolean testSerialPort(File device){
        if (device.canRead() && device.canWrite()){
            return true;
        }
        return false;
    }
}
