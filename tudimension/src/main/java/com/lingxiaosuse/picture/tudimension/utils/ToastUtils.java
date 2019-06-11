package com.lingxiaosuse.picture.tudimension.utils;

import android.widget.Toast;

/**
 * Created by lingxiao on 2017/8/30.
 */

public class ToastUtils {
    private static Toast mToast;
    public static void show(String msg){
        if (mToast == null){
            mToast = Toast.makeText(UIUtils.getContext(),msg,Toast.LENGTH_SHORT);
        }else {
            mToast.setText(msg);
        }
        mToast.show();
    }


}
