package com.fht.yuanzhuo.Utils;

import android.content.Context;
import android.content.SharedPreferences;

public class CacheUtils {
//    获取指导页标识缓存
    public static boolean getBoolea(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences("isStart",Context.MODE_PRIVATE);
        return sp.getBoolean(key,false);
    }
//    设置指导页标识缓存
    public static void putBoolean(Context context, String key, boolean value) {
        SharedPreferences sp = context.getSharedPreferences("isStart",Context.MODE_PRIVATE);
        sp.edit().putBoolean(key,value).commit();
    }

//    设置用户token缓存
    public static void setToken(Context context,  String token) {
        SharedPreferences sp = context.getSharedPreferences("userToken",Context.MODE_PRIVATE);
        sp.edit().putString("Token",token).commit();
    }

//    获取用户token缓存,没有用户token缓存使用返回值null
    public static String getToken(Context context) {
        SharedPreferences sp = context.getSharedPreferences("userToken",Context.MODE_PRIVATE);
        return sp.getString("Token",new String());
    }

    //    设置用户token缓存为null
    public static void removeToken(Context context) {
        SharedPreferences sp = context.getSharedPreferences("userToken",Context.MODE_PRIVATE);
        sp.edit().putString("Token",new String()).commit();
    }


}
