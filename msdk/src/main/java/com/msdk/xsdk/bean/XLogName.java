package com.msdk.xsdk.bean;

import android.util.Log;

public class XLogName {
    private static final String TAG_LOG = "msdk_x";
    private static boolean isOn = false;
    private static XLogName xLogName;

    private XLogName(boolean isOn) {
        this.isOn = isOn;
    }

    public static synchronized XLogName getInstance(boolean isOn) {
        if (xLogName == null) {
            xLogName = new XLogName(isOn);
        }
        return xLogName;
    }

    /**
     * @param type i,e,v,w,d,other
     * @param tag
     * @param value
     */
    public static void MSDKLog(char type, String tag, Object value) {
        if (isOn) {
            switch (type) {
                case 'i':
                    Log.i(TAG_LOG, "提醒模式--"+tag+"：-->" + value);
                    break;
                case 'e':
                    Log.e(TAG_LOG, "错误模式--"+tag+"：-->" + value);
                    break;
                case 'v':
                    Log.v(TAG_LOG, "详细模式--"+tag+"：-->" + value);
                    break;
                case 'w':
                    Log.w(TAG_LOG, "警告模式--"+tag+"：-->" + value);
                    break;
                default:
                    Log.d(TAG_LOG, "正常模式--"+tag+"：-->" + value);
                    break;
            }

        }
    }
}
