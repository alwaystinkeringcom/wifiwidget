package com.alwaystinkering.wifi.util;

/**
 * Wrap around log to add in tag to filter logcat on
 */
public class Log {
    private static final String TAG_PREFIX = "_WIFI_";

    public static void i(String tag, String message) {
        android.util.Log.i(TAG_PREFIX + tag, message);
    }

    public static void v(String tag, String message) {
        android.util.Log.v(TAG_PREFIX + tag, message);
    }

    public static void d(String tag, String message) {
        android.util.Log.d(TAG_PREFIX + tag, message);
    }

    public static void w(String tag, String message) {
        android.util.Log.w(TAG_PREFIX + tag, message);
    }

    public static void w(String tag, String message, Exception e) {
        android.util.Log.w(TAG_PREFIX + tag, message, e);
    }

    public static void e(String tag, String message) {
        android.util.Log.e(TAG_PREFIX + tag, message);
    }

    public static void e(String tag, String message, Exception e) {
        android.util.Log.e(TAG_PREFIX + tag, message, e);
    }
}
