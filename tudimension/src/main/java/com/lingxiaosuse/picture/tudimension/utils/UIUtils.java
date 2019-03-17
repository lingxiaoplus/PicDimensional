package com.lingxiaosuse.picture.tudimension.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.AppCompatDrawableManager;
import android.support.v7.widget.AppCompatImageView;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;

import com.camera.lingxiao.common.app.ContentValue;
import com.camera.lingxiao.common.utills.SpUtils;
import com.lingxiaosuse.picture.tudimension.App;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.ref.Reference;

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
        float denstty = getContext().getResources().getDisplayMetrics().density;
        return px/denstty;
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     */
    public static int px2sp(float pxValue) {
        final float fontScale = getContext().getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }
    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     * @param spValue
     *            （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public static int sp2px(float spValue) {
        final float fontScale = getContext().getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
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
     * 获取最小宽度限定
     * @param activity
     * @return
     */
    public static float smallestWidth(Activity activity){
        float density = activity.getResources().getDisplayMetrics().density;
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        int heightPixels = dm.heightPixels;
        int widthPixels = dm.widthPixels;
        float heightDP = heightPixels / density;
        float widthDP = widthPixels / density;
        float smallestWidthDP;
        if(widthDP < heightDP) {
            smallestWidthDP = widthDP;
        }else {
            smallestWidthDP = heightDP;
        }
        return smallestWidthDP;
    }

    /**
     * 改变SVG图片着色
     * @param imageView
     * @param iconResId svg资源id
     * @param color 期望的着色
     */
    public static void changeSVGColor(ImageView imageView, int iconResId, int color){
        @SuppressLint("RestrictedApi") Drawable drawable =  AppCompatDrawableManager.get().getDrawable(getContext(), iconResId);
        imageView.setImageDrawable(drawable);
        Drawable.ConstantState state = drawable.getConstantState();
        Drawable drawable1 = DrawableCompat.wrap(state == null ? drawable : state.newDrawable()).mutate();
        drawable1.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        DrawableCompat.setTint(drawable1, ContextCompat.getColor(getContext(), color));
        imageView.setImageDrawable(drawable1);
    }

    public static void changeSVGColor(AppCompatImageView imageView, int iconResId, int color){
        @SuppressLint("RestrictedApi") Drawable drawable =  AppCompatDrawableManager.get().getDrawable(getContext(), iconResId);
        imageView.setImageDrawable(drawable);
        Drawable.ConstantState state = drawable.getConstantState();
        Drawable drawable1 = DrawableCompat.wrap(state == null ? drawable : state.newDrawable()).mutate();
        drawable1.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        DrawableCompat.setTint(drawable1, ContextCompat.getColor(getContext(), color));
        imageView.setImageDrawable(drawable1);
    }


    /**
     * 更新系统图库  下载的图片可以在相册中查看
     * @param file
     */
    public static void updateImageDb(File file){
        // 其次把文件插入到系统图库
        try {
            MediaStore.Images.Media.insertImage(getContext().getContentResolver(),
                    file.getAbsolutePath(), file.getName(), null);
            // 最后通知图库更新
            getContext().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                    Uri.fromFile(new File(file.getPath()))));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


    /**
     * 获取图片分辨率
     * @param isVertical 横屏还是竖屏
     */
    public static String getImageRule(boolean isVertical){
        String imgRule = "";
        int type = SpUtils.getInt(getContext(), ContentValue.PIC_RESOLUTION,ContentValue.PIC_2K);
        if (type == ContentValue.PIC_720P){
            imgRule = isVertical?ContentValue.ImgRule_vertical_720:ContentValue.ImgRule_720;
        }else if (type == ContentValue.PIC_1080P){
            imgRule = isVertical?ContentValue.ImgRule_vertical_1080:ContentValue.ImgRule_1080;
        }
        return imgRule;
    }
}
