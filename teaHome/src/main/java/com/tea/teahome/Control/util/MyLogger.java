package com.tea.teahome.Control.util;

import android.util.Log;

/**
 * 记录日志的时候， 顺带往handler记录一份
 */
public class MyLogger {
    private static final String TAG = "MyLogger";

    private static final String INFO = "INFO";

    private static final String ERROR = "ERROR";

    private static final boolean ENABLE = true;

    public static void info(String message) {
        info(TAG, message);
    }

    public static void info(String tag, String message) {
        log(INFO, tag, message);
    }

    public static void error(String message) {
        error(TAG, message);
    }

    public static void error(String tag, String message) {
        log(ERROR, tag, message);
    }

    private static void log(String level, String tag, String message) {
        if (!ENABLE) {
            return;
        }
        if (level.equals(INFO)) {
            Log.i(tag, message);

        } else if (level.equals(ERROR)) {
            Log.e(tag, message);
        }
    }
}
