package com.lingxiaosuse.picture.tudimension.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;

/**
 * Created by lingxiao on 2017/8/30.
 * android6.0之后运行时权限
 */

public class PremessionUtils {
    public static int REQUEST_TAKE_PHOTO_PERMISSION =200;
    public static void getPremession(Activity activity,String title,
                                     String message,String[] permissions,
                                     onPermissinoListener listener){
        if (isOverMarshmallow()){
            for (int i = 0; i < permissions.length; i++) {
                if (ContextCompat.checkSelfPermission(activity,
                        permissions[i])
                        != PackageManager.PERMISSION_GRANTED) {
                    //申请权限，REQUEST_TAKE_PHOTO_PERMISSION是自定义的常量
                    showDialog(activity,title,message,permissions);
                } else {
                    //有权限
                }
            }
        }
    }

    private static void showDialog(final Activity activity, String title, String message, final String[] permissions) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(activity);
        dialog.setTitle(title);
        dialog.setMessage(message);
        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                for (int i = 0; i < permissions.length; i++) {
                    ActivityCompat.requestPermissions(activity,
                            new String[]{permissions[i]},
                            REQUEST_TAKE_PHOTO_PERMISSION);
                }
            }
        });
        dialog.show();
    }
    /**
     * 判断当前手机API版本是否 >= 6.0
     */
    private static boolean isOverMarshmallow() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    /**
     * 请求权限结果，对应onRequestPermissionsResult()方法。
     */
    public static void onRequestPermissionsResult(int requestCode, String[] permissions,
                                                  int[] grantResults) {
        if (mOnPermissionListener != null){
            if (requestCode == REQUEST_TAKE_PHOTO_PERMISSION){
                mOnPermissionListener.onPermissionGet();
            }else {
                mOnPermissionListener.onPermissionDenied();
            }
        }
    }
    public static onPermissinoListener mOnPermissionListener;
    public interface onPermissinoListener{
        void onPermissionGet();
        void onPermissionDenied();
    }
}
