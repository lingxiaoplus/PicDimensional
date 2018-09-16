package com.lingxiaosuse.picture.tudimension.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.camera.lingxiao.common.app.ContentValue;
import com.camera.lingxiao.common.utills.SpUtils;

public class DialogUtil {
    private static AlertDialog.Builder mBuilder;
    private static DialogUtil mDialogutil;
    public static DialogUtil getInstence(){
        if (mDialogutil == null){
            mDialogutil = new DialogUtil();
        }
        return mDialogutil;
    }
    public static void showMultiChoiceDia(String title,final String[] items,final boolean[] checkedItems,Context context) {
        AlertDialog.Builder builder  = new AlertDialog.Builder(context);
        builder.setIcon(android.R.drawable.alert_dark_frame);
        builder.setTitle(title);
        builder.setMultiChoiceItems(items, checkedItems,
                new DialogInterface.OnMultiChoiceClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which,
                                        boolean isChecked) {
                        checkedItems[which] = isChecked;
                    }
                });
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String checkedStr = "";
                for (int i = 0; i < items.length; i++) {
                    if (checkedItems[i]) {
                        checkedStr +=i+",";
                    }
                }
                SpUtils.putString(UIUtils.getContext(),ContentValue.DRAWER_MODEL,checkedStr);
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    public void showSingleDia(String title,String message,Context context){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setIcon(android.R.drawable.alert_dark_frame);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    public AlertDialog.Builder setNormalDialog(String title,String message,Context context){
        AlertDialog.Builder builder  = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(message);
        return builder;
    }

    private static DialogButtonCallback mCallback;
    public static void setDialogButtonCallback(DialogButtonCallback callback){
        mCallback = callback;
    }
    public interface DialogButtonCallback{
        void onPositiveClick();
        void onNegativeClick();
    }
}
