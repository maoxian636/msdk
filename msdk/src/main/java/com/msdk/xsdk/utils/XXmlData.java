package com.msdk.xsdk.utils;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;
import java.util.Map;


public class XXmlData {
    private static final String FILE_NAME = "MSDK";
    public static void setParam(Context context , String key, Object object){

        String type = object.getClass().getSimpleName();
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        if("String".equals(type)){
            editor.putString(key, (String)object);
        }
        else if("Integer".equals(type)){
            editor.putInt(key, (Integer)object);
        }
        else if("Boolean".equals(type)){
            editor.putBoolean(key, (Boolean)object);
        }
        else if("Float".equals(type)){
            editor.putFloat(key, (Float)object);
        }
        else if("Long".equals(type)){
            editor.putLong(key, (Long)object);
        }

        editor.commit();
    }
    public static Object getParam(Context context , String key, Object defaultObject){
        String type = defaultObject.getClass().getSimpleName();
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, MODE_PRIVATE);

        if("String".equals(type)){
            return sp.getString(key, (String)defaultObject);
        }
        else if("Integer".equals(type)){
            return sp.getInt(key, (Integer)defaultObject);
        }
        else if("Boolean".equals(type)){
            return sp.getBoolean(key, (Boolean)defaultObject);
        }
        else if("Float".equals(type)){
            return sp.getFloat(key, (Float)defaultObject);
        }
        else if("Long".equals(type)){
            return sp.getLong(key, (Long)defaultObject);
        }

        return null;
    }
    public static boolean isKeyExists(Context context, String key) {
        SharedPreferences preferences = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        return preferences.contains(key);
    }

    public static void clean(Context context ){
        SharedPreferences sharedPreferences = context.getSharedPreferences(FILE_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

    public static boolean isTimeGreaterThan5Minutes(Context context,  String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        long savedTime = sharedPreferences.getLong(key, 0); // 默认值为0
        long currentTime = System.currentTimeMillis();
        long fiveMinutesInMillis = 5 * 60 * 1000; // 5分钟的毫秒数
        return (currentTime - savedTime) > fiveMinutesInMillis;
    }


    /**
     * 存储Map集合
     * @param key 键
     * @param map 存储的集合
     * @param <K> 指定Map的键
     * @param <T> 指定Map的值
     */
    public static  <K,T> void setMap(Context context,String key , Map<K,T> map){
        SharedPreferences sharedPreferences = context.getSharedPreferences("MSDK_MAP", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (map == null || map.isEmpty() || map.size() < 1){
            return;
        }

        Gson gson = new Gson();
        String strJson  = gson.toJson(map);
        editor.clear();
        editor.putString(key ,strJson);
        editor.commit();
    }
    /**
     * 获取Map集合
     * */
    public static <K,T> Map<K,T> getMap(Context context,String key){
        SharedPreferences sharedPreferences = context.getSharedPreferences("MSDK_MAP", MODE_PRIVATE);
        Map<K,T> map = new HashMap<>();
        String strJson = sharedPreferences.getString(key,null);
        if (strJson == null){
            return map;
        }
        Gson gson = new Gson();
        map = gson.fromJson(strJson,new TypeToken<Map<K,T> >(){}.getType());
        return map;
    }
}
