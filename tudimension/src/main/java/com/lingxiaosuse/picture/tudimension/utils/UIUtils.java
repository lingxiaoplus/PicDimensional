package com.lingxiaosuse.picture.tudimension.utils;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.view.View;

import com.lingxiaosuse.picture.tudimension.global.App;
/**
 * Created by lingxiao on 2017/6/13.
 */
public class UIUtils {
    public static Context getContext(){
        return App.getContext();
    }
    public static Handler getHandler(){
        return App.getHandler();
    }
    public static int getMainThreadId(){
        return App.getMainThreadId();
    }
    ///////////////////////////加载资源文件///////////////////////
    //获取字符串
    public static String getString(int id){
        return getContext().getResources().getString(id);
    }
    //获取字符串数组
    public static String[] getStringArray(int id){
        return getContext().getResources().getStringArray(id);
    }
    //获取图片
    public static Drawable getDrawable(int id){
        return getContext().getResources().getDrawable(id);
    }
    //获取颜色
    public static int getColor(int id){
        return getContext().getResources().getColor(id);
    }
    //根据id获取颜色的状态选择器
    public static ColorStateList getColorStateList(int id){
        return getContext().getResources().getColorStateList(id);
    }
    //获取尺寸
    public static int getDimen(int id){
        return getContext().getResources().getDimensionPixelSize(id);
    }
    // /////////////////dip和px转换//////////////////////////
    public static int dip2px(float dip){
        float density = getContext().getResources().getDisplayMetrics().density;
        return (int) (dip * density + 0.5f);
    }
    public static float px2dip(int px){
        float denstty  =getContext().getResources().getDisplayMetrics().density;
        return px/denstty;
    }
    // /////////////////加载布局文件//////////////////////////
    public static View inflate(int id){
        return View.inflate(getContext(),id,null);
    }
    //////////////////判断是否运行在主线程///////////
    public static boolean isRunOnUIThread(){
        //在android6.0系统中不行
      /*  int mypid = android.os.Process.myPid();
        if (mypid == getMainThreadId()){
            return true;
        }
        return false;*/
        return Looper.getMainLooper().getThread() == Thread.currentThread();
    }
    //运行在主线程
    public static void runOnUIThread(Runnable r){
        if(isRunOnUIThread()){
            //如果是主线程，就直接运行
            r.run();
        }else{
            //如果在子线程，就借助handler让其在主线程运行
            getHandler().post(r);
        }
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     * @param spValue
     *            （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }
}
