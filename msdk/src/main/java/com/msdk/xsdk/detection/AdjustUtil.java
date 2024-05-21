package com.msdk.xsdk.detection;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.JavascriptInterface;

import com.adjust.sdk.Adjust;
import com.adjust.sdk.AdjustConfig;
import com.adjust.sdk.AdjustEvent;
import com.msdk.xsdk.bean.XLogName;
import com.msdk.xsdk.ui.XNewActivity;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class AdjustUtil {
    private Activity context;


    public AdjustUtil(Activity context) {
        this.context = context;

    }


    public void setADConfig(String appToken) {
        if (TextUtils.isEmpty(appToken)) {
            XLogName.MSDKLog('e', "", "后台输入错误,检测Token不能为空");
        }
        XLogName.MSDKLog('i', "", "Adjust启动");
        AdjustConfig config = new AdjustConfig(context, appToken, AdjustConfig.ENVIRONMENT_PRODUCTION);
        config.setOnEventTrackingSucceededListener(adjustEventSuccess -> {
            XLogName.MSDKLog('d', "", adjustEventSuccess.jsonResponse);
        });

        config.setOnEventTrackingFailedListener(adjustEventFailure -> {
            XLogName.MSDKLog('e', "", adjustEventFailure.jsonResponse);
        });


        Adjust.onCreate(config);


//            context.registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
//                public void onActivityCreated(Activity activity, Bundle bundle) {
//                }
//
//                public void onActivityStarted(Activity activity) {
//                }
//
//                public void onActivityResumed(Activity activity) {
//                    Adjust.onResume();
//                }
//
//                public void onActivityPaused(Activity activity) {
//                    Adjust.onPause();
//                }
//
//                public void onActivityStopped(Activity activity) {
//                }
//
//                public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
//                }
//
//                public void onActivityDestroyed(Activity activity) {
//                }
//            });

    }

    @JavascriptInterface
    public void postMessage(String name, String data) {
        XLogName.MSDKLog('i', "adjust", name + "----" + data);
        if ("openWindow".equals(name)) {
            Intent intent = new Intent(context, XNewActivity.class);
            intent.putExtra("data", data);
            context.startActivityForResult(intent, 1);
        }
        try {
            Map<String, Object> jsonObject = new HashMap<>();
            JSONObject json = new JSONObject(data);
            Iterator<String> keys = json.keys();
            while (keys.hasNext()) {
                String key = keys.next();
                Object value = json.get(key);
                jsonObject.put(key, value);
            }
            if (name.equals("register")) {
                Adjust.trackEvent(new AdjustEvent("6proqq"));
            }

        } catch (Exception e) {

        }
    }

    private void setAdjData(Map<String, Object> jsonObject, String token) {
        AdjustEvent adjustEvent = new AdjustEvent(token);
        String currency = (String) jsonObject.get("currency");
        String amount = (String) jsonObject.get("amount");
        adjustEvent.setRevenue(Double.valueOf(amount), currency);
        Adjust.trackEvent(adjustEvent);
        Log.d("mao", token);
    }


}
