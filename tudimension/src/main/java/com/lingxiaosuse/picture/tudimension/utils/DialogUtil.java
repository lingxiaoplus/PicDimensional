package com.lingxiaosuse.picture.tudimension.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;

import com.camera.lingxiao.common.app.ContentValue;
import com.camera.lingxiao.common.utills.SpUtils;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DialogUtil {
    private static boolean[] checkedItems;
    public static void showMultiChoiceDia(String title,final String[] items,Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setIcon(android.R.drawable.alert_dark_frame);
        builder.setTitle(title);
        checkedItems = new boolean[items.length];
        String json = SpUtils.getString(UIUtils.getContext(),ContentValue.DRAWER_MODEL,"");
        try {
            JSONObject jsonObject= new JSONObject(json);
            JSONArray array = jsonObject.getJSONArray(ContentValue.DRAWER_MODEL);
            for (int i = 0; i < array.length(); i++) {
                if (Integer.valueOf((Integer) array.get(i)) != -1){
                    checkedItems[i] = true;
                }else {
                    checkedItems[i] = false;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

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
                JSONObject object = new JSONObject();
                JSONArray array = new JSONArray();
                for (int i = 0; i < items.length; i++) {
                    if (checkedItems[i]) {
                        array.put(i);
                    }else {
                        array.put(-1);
                    }
                }
                try {
                    object.put(ContentValue.DRAWER_MODEL,array);
                    SpUtils.putString(UIUtils.getContext(),ContentValue.DRAWER_MODEL,array.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
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
}
