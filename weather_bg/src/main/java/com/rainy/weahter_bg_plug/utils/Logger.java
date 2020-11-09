package com.rainy.weahter_bg_plug.utils;

import android.util.Log;

/**
 * create by Rainy on 2020/10/17.
 * email: im.wyu@qq.com
 * github: Rainvy
 * describe:
 */
public class Logger {

    public static final boolean DEBUG = true;

    public static void d(String TAG, String msg) {
        if (DEBUG) Log.d(TAG, msg);
    }

    public static void e(String TAG, String msg) {
        if (DEBUG) Log.e(TAG, msg);
    }

    public static void w(String TAG, String msg) {
        if (DEBUG) Log.w(TAG, msg);
    }

    public static void v(String TAG, String msg) {
        if (DEBUG) Log.v(TAG, msg);
    }

    public static void i(String TAG, String msg) {
        if (DEBUG) Log.i(TAG, msg);
    }


}
