package com.lingxiaosuse.picture.tudimension.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;

import com.camera.lingxiao.common.app.ContentValue;
import com.camera.lingxiao.common.utills.SpUtils;
import com.google.gson.Gson;
import com.lingxiaosuse.picture.tudimension.db.DrawerSelect;
import com.lingxiaosuse.picture.tudimension.db.DrawerSelect_Table;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.sql.language.Where;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class DialogUtil {


    public static void showMultiChoiceDia(String title,final String[] items,final boolean[] checkedItems,Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
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
}
