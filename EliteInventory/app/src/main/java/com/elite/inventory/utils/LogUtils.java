package com.elite.inventory.utils;

import android.util.Log;

import com.elite.inventory.config.AppConfig;


public class LogUtils {

    public static void i(String tag, String msg) {
        if (!AppConfig.IS_PUBLISH) {
            Log.i(tag, msg);
        }
    }

    public static void i(String tag, Object msg) {
        i(tag, String.valueOf(msg));
    }

    public static void d(String tag, String msg) {
        Log.d(tag, msg);
    }

    public static void e(String tag, String msg) {
        Log.e(tag, msg);
    }

    public static void w(String tag, String msg) {
        Log.w(tag, msg);
    }

    public static void v(String tag, String msg) {
        Log.v(tag, msg);
    }

}
