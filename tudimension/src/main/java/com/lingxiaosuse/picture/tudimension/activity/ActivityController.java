package com.lingxiaosuse.picture.tudimension.activity;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lingxiao on 2017/8/3.
 */

public class ActivityController {
    public static  List<Activity> activities = new ArrayList<>();
    public static void addActivity(Activity activity){
        activities.add(activity);
    }
    public static void removeActivity(Activity activity){
        activities.remove(activity);
    }
    public static void finishAll(){
        for (Activity activity:activities) {
            activity.finish();
        }
        activities.clear();
    }
}
