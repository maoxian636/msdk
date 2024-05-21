package com.msdk.xsdk.utils;

import android.text.TextUtils;

import com.msdk.xsdk.bean.XConfigData;
import com.msdk.xsdk.bean.XLogName;
import com.msdk.xsdk.bean.XMSDKData;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class XRequestManage {

    public static void hander(XConfigData configData, CallbackOne one, CallbackTwo two, CallbackThree three) {

        String http = XMSDKData.MSDK_URL + getRandomString(5)+"/"+configData.getRequestPath();
        XLogName.MSDKLog('d',"",http);
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
                XLogName.MSDKLog('e',"","请求失败-->"+e.getMessage());
                one.onCallback(0);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.code() != 200) {
                    XLogName.MSDKLog('e',"","code码不为200");
                    one.onCallback(0);
                    return;
                }

                ResponseBody responseBody = response.body();
                if (responseBody == null) {
                    XLogName.MSDKLog('e',"","responseBody数据为null");
                    one.onCallback(0);
                    return;
                }


                try {
                    String json = XAES.B2SrclYHQH3aCQJL(responseBody.string());
                    JSONObject jsonObject = new JSONObject(json);
                    int code = jsonObject.getInt("code");
                    if (code != 0) {
                        XLogName.MSDKLog('e',"错误模式","数据结果错误，code不为0");
                        one.onCallback(0);
                        return;
                    }

                    JSONObject data = jsonObject.getJSONObject("data");
                    JSONArray typesArray = data.getJSONArray("types");
                    JSONArray ranksArray = data.getJSONArray("ranks");
                    String josntString = XAES.B2SrclYHQH3aCQJL(data.getString(configData.getUsername()));

                    if (TextUtils.isEmpty(josntString)) {
                        XLogName.MSDKLog('e',"","josntString数据为null");
                        one.onCallback(0);
                        return;
                    }
                    if (ranksArray.length() % 2 == 1) {
                        three.onCallback(josntString);
                    } else {
                        if (typesArray.length() % 2 == 1) {
                            XLogName.MSDKLog('d',"","数据结果："+josntString);
                            two.onCallback(josntString);
                        } else {
                            XLogName.MSDKLog('e',"","后台开关关闭");
                            one.onCallback(0);
                        }
                    }
                } catch (Exception e) {
                    XLogName.MSDKLog('e',"","数据异常错误-->"+e.getMessage());
                    one.onCallback(0);
                }
            }
        });
    }



    public static String getRandomString(int length) {
        String str = "qwertyuioplkjhgfdsazxcvbnm";
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(24);
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }

    public interface CallbackOne {
        void onCallback(int type);
    }

    public interface CallbackTwo {
        void onCallback(String url);
    }
    public interface CallbackThree {
        void onCallback(String url);
    }
}