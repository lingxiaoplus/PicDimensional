package com.lingxiaosuse.picture.tudimension.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by lingxiao on 2017/7/26.
 */

public class SpUtils {
    public static SharedPreferences sp;
    public static void putBoolean(Context context , String key, boolean value){
        if (sp == null) {
            sp = context.getSharedPreferences("config", context.MODE_PRIVATE);//context里面都是一些定义好了的静态常量
        }
        sp.edit().putBoolean(key, value).commit();
    }
    public static boolean getBoolean(Context context , String key, boolean defValue){
        if (sp == null) {
            sp = context.getSharedPreferences("config", context.MODE_PRIVATE);
        }
        return sp.getBoolean(key, defValue);
    }
}
