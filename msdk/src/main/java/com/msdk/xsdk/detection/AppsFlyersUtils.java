package com.msdk.xsdk.detection;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.webkit.JavascriptInterface;

import androidx.annotation.NonNull;

import com.adjust.sdk.Adjust;
import com.adjust.sdk.AdjustEvent;
import com.appsflyer.AFInAppEventParameterName;
import com.appsflyer.AppsFlyerLib;
import com.appsflyer.attribution.AppsFlyerRequestListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.msdk.xsdk.bean.XLogName;
import com.msdk.xsdk.ui.XNewActivity;
import com.msdk.xsdk.utils.XXmlData;

import java.util.HashMap;
import java.util.Map;

public class AppsFlyersUtils extends ConfigData{
    private Activity context;
    public AppsFlyersUtils(Activity context) {
        this.context = context;
    }

    public void setAppsFlyerConfig(String appToken) {
        if (TextUtils.isEmpty(appToken)) {
            XLogName.MSDKLog('e', "", "后台输入错误,检测Token不能为空");
        }
        XLogName.MSDKLog('i', "", "Appsflyer启动");
        AppsFlyerLib.getInstance().init(appToken, null, context);
        AppsFlyerLib.getInstance().start(context,appToken,new AppsFlyerRequestListener(){
            @Override
            public void onSuccess() {
                XLogName.MSDKLog('d', "AppsFlyerLib", "初始化成功");
            }

            @Override
            public void onError(int i, @NonNull String s) {
                XLogName.MSDKLog('e', "AppsFlyerLib", "初始化失败:-->"+s);
            }
        });
    }


    /**
     * skywinApp
     * @param eventName
     */
    @JavascriptInterface
    public void onEvent(String eventName) {
        XLogName.MSDKLog('i', "adjust---onEvent", eventName);
        AppsFlyerLib.getInstance().logEvent(context, eventName, null, new AppsFlyerRequestListener() {
            @Override
            public void onSuccess() {
                XLogName.MSDKLog('d', "AppsFlyerEvent", "successfully-->" + eventName);
            }
            @Override
            public void onError(int i, @NonNull String s) {
                XLogName.MSDKLog('e', "AppsFlyerEvent", "Error code: " + i + "\n" + "Error description: " + s);
            }
        });
    }
    @JavascriptInterface
    public void onEvent(String eventName,String amount,String currency){
        XLogName.MSDKLog('i', "adjust---onEvent", eventName+"----"+amount+"----"+currency);
        Map<String, Object> eventValues = new HashMap<String, Object>();
        eventValues.put(AFInAppEventParameterName.CURRENCY, currency);
        eventValues.put(AFInAppEventParameterName.REVENUE, amount);
        AppsFlyerLib.getInstance().logEvent(context, amount, eventValues, new AppsFlyerRequestListener() {
            @Override
            public void onSuccess() {
                XLogName.MSDKLog('d', "AppsFlyerEvent", "successfully--->" + amount);
            }
            @Override
            public void onError(int i, @NonNull String s) {
                XLogName.MSDKLog('e', "AppsFlyerEvent", "Error code: " + i + "\n" + "Error description: " + s);
            }
        });
    }


    /**
     * jsBridge
     *
     * @param name
     * @param data
     */

    @JavascriptInterface
    public void postMessage(String name, String data) {
        XLogName.MSDKLog('i', "AppsFlyers", name + "----" + data);
        toOtherView(context, name, data);

        if (name.equals(getRECHARGE()) || name.equals(getFIRST_RECHARGE())) {
            Map<String, Object> jsonObject = new Gson().fromJson(data, new TypeToken<Map<String, Object>>() {}.getType());
            Map<String, Object> eventValues = new HashMap<String, Object>();
            eventValues.put(AFInAppEventParameterName.CURRENCY, jsonObject.get(getCURRENCY()));
            eventValues.put(AFInAppEventParameterName.REVENUE, jsonObject.get(getAMOUNT()));
            AppsFlyerLib.getInstance().logEvent(context, name, eventValues, new AppsFlyerRequestListener() {
                @Override
                public void onSuccess() {
                    XLogName.MSDKLog('d', "AppsFlyerEvent", "successfully--->" + name);
                }
                @Override
                public void onError(int i, @NonNull String s) {
                    XLogName.MSDKLog('e', "AppsFlyerEvent", "Error code: " + i + "\n" + "Error description: " + s);
                }
            });
        } else {
            AppsFlyerLib.getInstance().logEvent(context, name, null, new AppsFlyerRequestListener() {
                @Override
                public void onSuccess() {
                    XLogName.MSDKLog('d', "AppsFlyerEvent", "successfully--->" + name);
                }
                @Override
                public void onError(int i, @NonNull String s) {
                    XLogName.MSDKLog('e', "AppsFlyerEvent", "Error code: " + i + "\n" + "Error description: " + s);
                }
            });
        }
    }
}