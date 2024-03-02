package com.app.bigprize.utils;

import android.util.Log;

import com.app.bigprize.BuildConfig;


public class BIG_AppLogger {
    private BIG_AppLogger() {
        isLogEnabled = BuildConfig.DEBUG;
    }

    private static BIG_AppLogger instance = new BIG_AppLogger();

    public static BIG_AppLogger getInstance() {
        return instance;
    }

    private boolean isLogEnabled = false;

    public void d(String a, String b) {
        if (isLogEnabled) Log.d(a, b);
    }

    public void e(String a, String b) {
        if (isLogEnabled) Log.e(a, b);
    }

    public void e_long(String TAG, String message) {
        int maxLogSize = 2000;
        for (int i = 0; i <= message.length() / maxLogSize; i++) {
            int start = i * maxLogSize;
            int end = (i + 1) * maxLogSize;
            end = end > message.length() ? message.length() : end;
            e(TAG, message.substring(start, end));
        }
    }

    public void e(String a, String b, Throwable t) {
        if (isLogEnabled) BIG_AppLogger.getInstance().e(a, b, t);
    }

    public void i(String a, String b) {
        if (isLogEnabled) Log.i(a, b);
    }

    public void w(String a, String b) {
        if (isLogEnabled) Log.w(a, b);
    }

    public void v(String a, String b) {
        if (isLogEnabled) Log.v(a, b);
    }
}
