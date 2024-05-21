package com.msdk.xsdk;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;

import com.msdk.xsdk.bean.XConfigData;
import com.msdk.xsdk.bean.XLogName;
import com.msdk.xsdk.bean.XMSDKData;
import com.msdk.xsdk.detection.AdjustUtil;
import com.msdk.xsdk.detection.AppsFlyersUtils;
import com.msdk.xsdk.ui.XStartMainActivity;
import com.msdk.xsdk.utils.XAES;
import com.msdk.xsdk.utils.XException;
import com.msdk.xsdk.utils.XMapLanguage;
import com.msdk.xsdk.utils.XNetWork;
import com.msdk.xsdk.utils.XRequestManage;
import com.msdk.xsdk.utils.XXmlData;

import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class MSDKSingleton {
    private static MSDKSingleton msdkSingleton;
    private static XConfigData configData;
    private Activity activity;
    private MSDKSingleton(Activity activity) {
        this.activity = activity;
    }

    public static synchronized MSDKSingleton getInstance(Activity activity) {
        if (msdkSingleton == null) {
            msdkSingleton = new MSDKSingleton(activity);
        }
        return msdkSingleton;

    }

    private final Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                XLogName.MSDKLog('w', "", "进行重复请求");
                sendPost();
            } else if (msg.what == 1) {
                // 审核

            } else if (msg.what == 2) {
                // 线上
                String info = msg.getData().getString("info");
                if (!TextUtils.isEmpty(info)) {
                    activity.startActivity(new Intent(activity, XStartMainActivity.class).putExtra("url", info));
                    activity.finish();
                    activity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                } else {
                    XLogName.MSDKLog('i', "线上模式", "重复请求");
                    sendPost();
                }
            }
        }
    };

    private void sendPost() {
        if (configData == null) {
            XLogName.MSDKLog('w', "", "ConfigData数据为空，重新请求");
        } else {

            if (XNetWork.isNetworkConnected(activity)) {
                XRequestManage.hander(configData, new XRequestManage.CallbackOne() {
                            @Override
                            public void onCallback(int type) {
                                mHandler.sendEmptyMessage(type);
                            }
                        }, new XRequestManage.CallbackTwo() {
                            @Override
                            public void onCallback(String url) {
                                Message msg = new Message();
                                msg.what = 2;
                                msg.getData().putString("info", url);
                                mHandler.sendMessage(msg);
                            }
                        }, new XRequestManage.CallbackThree() {
                            @Override
                            public void onCallback(String url) {
                                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                                activity.startActivity(intent);
                            }
                        }
                );
            } else {
                XLogName.MSDKLog('w', "", "ConfigData数据为空，重新请求");
                mHandler.sendEmptyMessage(0);
            }

        }

    }

    /**
     * @param configData 数据为class格式
     * @throws XException
     */
    public void setMSDKConfig(XConfigData configData) throws XException {


        if (TextUtils.isEmpty(configData.getRequestPath())) {
            throw new XException("请求路径不能为空");
        }
        if (TextUtils.isEmpty(configData.getUsername())) {
            throw new XException("账号不能为空");
        }
        MSDKSingleton.configData = configData;


    }

    public static void setDebug(Boolean debug) {
        XLogName.getInstance(debug);
    }


    public void initConfig() throws XException {
        if (configData != null) {
            String http = XMSDKData.MSDK_URL + "info/" + configData.getRequestPath();
            XLogName.MSDKLog('d', "", http);
            OkHttpClient ok = new OkHttpClient.Builder()
                    .connectTimeout(5000, TimeUnit.MILLISECONDS)
                    .readTimeout(5000, TimeUnit.MILLISECONDS)
                    .writeTimeout(5000, TimeUnit.MILLISECONDS)
                    .build();

            HttpUrl.Builder urlBuilder = HttpUrl.parse(http).newBuilder();
            String url = urlBuilder.build().toString();
            Request request = new Request.Builder()
                    .url(url)
                    .addHeader(XMSDKData.MSDK_TOKEN, "e3b2486a648446e89730273c3e00ec43")
                    .addHeader(XMSDKData.MSDK_LANGUAGE, XMapLanguage.mapLanguageToCode())
                    .build();

            ok.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    try {
                        XLogName.MSDKLog('e', "", "请求失败-->"+e.getMessage());
                        Thread.sleep(1000);
                        initConfig();
                    } catch (XException ex) {
                        throw new RuntimeException(ex);
                    } catch (InterruptedException ex) {
                        throw new RuntimeException(ex);
                    }

                }


                @Override
                public void onResponse(Call call, Response response) throws IOException {


                    ResponseBody responseBody = response.body();

                    try {
                        String json = XAES.B2SrclYHQH3aCQJL(responseBody.string());
                        JSONObject jsonObject = new JSONObject(json);
                        String data = jsonObject.getString(configData.getUsername());
//                      adjust  appsflyer
                        XLogName.MSDKLog('i', "后台测试数据-->", data);
                        String[] split = data.split("\\|");
                        if (!TextUtils.isEmpty(split[0])) {
                            XXmlData.setParam(activity, "ad_type", split[0]);
                            XXmlData.setParam(activity, "ad_path", split[2]);
                            if (split[0].equals("true")) {
                                AdjustUtil adjustUtil = new AdjustUtil(activity);
                                adjustUtil.setADConfig(split[1]);
                            } else {
                                AppsFlyersUtils appsFlyersUtils = new AppsFlyersUtils(activity);
                                appsFlyersUtils.setAppsFlyerConfig(split[1]);
                            }
                            mHandler.sendEmptyMessage(0);
                        } else {
                            XLogName.MSDKLog('e', "", "后台输入错误,检测不能为空");
                        }


                    } catch (Exception e) {
                        try {
                            XLogName.MSDKLog('e', "", "数据异常错误-->"+e.getMessage());
                            initConfig();
                        } catch (XException ex) {
                            throw new RuntimeException(ex);
                        }

                    }
                }
            });
        } else {

            throw new XException("未进行数据配置，请先进行数据配置，使用setMSDKConfig方法！！！");
        }


    }
}
