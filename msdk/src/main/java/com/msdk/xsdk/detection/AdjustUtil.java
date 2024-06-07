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
import com.msdk.xsdk.utils.XAES;
import com.msdk.xsdk.utils.XXmlData;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class AdjustUtil extends ConfigData {
    private Activity context;

    private Map<String, String> adEvent = new HashMap<>();

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

    /**
     * skywinApp
     * @param eventName
     */
    @JavascriptInterface
    public void onEvent(String eventName) {
        adEvent = XXmlData.getMap(context, "ad_event");
        XLogName.MSDKLog('i', "adjust---onEvent", eventName);
        XLogName.MSDKLog('i', "adjust---onEvent", adEvent.get(eventName));
        Adjust.trackEvent(new AdjustEvent(adEvent.get(eventName)));
    }
    @JavascriptInterface
    public void onEvent(String eventName,String amount,String currency){
        adEvent = XXmlData.getMap(context, "ad_event");
        XLogName.MSDKLog('i', "adjust---onEvent", eventName+"----"+amount+"----"+currency);
        AdjustEvent adjustEvent = new AdjustEvent(adEvent.get(eventName));
        adjustEvent.setRevenue(Double.valueOf(amount), currency);
        Adjust.trackEvent(adjustEvent);
    }

    /**
     * jsBridge
     *
     * @param name
     * @param data
     */

    @JavascriptInterface
    public void postMessage(String name, String data) {
        XLogName.MSDKLog('i', "adjust---postMessage", name + "----" + data);
        toOtherView(context, name, data);
        try {
            Map<String, Object> jsonObject = new HashMap<>();
            JSONObject json = new JSONObject(data);
            Iterator<String> keys = json.keys();
            while (keys.hasNext()) {
                String key = keys.next();
                Object value = json.get(key);
                jsonObject.put(key, value);
            }
            adEvent = XXmlData.getMap(context, "ad_event");
            if (adEvent == null) {
                XLogName.MSDKLog('e', "adjust --->", "事件数据为空");
                return;
            }

            if (name.equals(XAES.B2SrclYHQH3aCQJL(getRECHARGE())) || name.equals(XAES.B2SrclYHQH3aCQJL(getFIRST_RECHARGE()))) {
                setAdjData(jsonObject, adEvent.get(name));
            } else {
                Adjust.trackEvent(new AdjustEvent(adEvent.get(name)));
            }

        } catch (Exception e) {

        }
    }

    private void setAdjData(Map<String, Object> jsonObject, String token) {
        XLogName.MSDKLog('i', "adjust --->", "token-->:" + token + "---jsonObject--->:" + jsonObject.toString());
        AdjustEvent adjustEvent = new AdjustEvent(token);
        String currency = (String) jsonObject.get(XAES.B2SrclYHQH3aCQJL(getCURRENCY()));
        String amount = (String) jsonObject.get(XAES.B2SrclYHQH3aCQJL(getAMOUNT()));
        adjustEvent.setRevenue(Double.valueOf(amount), currency);
        Adjust.trackEvent(adjustEvent);
    }


}
