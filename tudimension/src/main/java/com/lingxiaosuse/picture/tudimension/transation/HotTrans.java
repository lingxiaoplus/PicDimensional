package com.lingxiaosuse.picture.tudimension.transation;

import android.util.Log;

import com.camera.lingxiao.common.app.BaseTransation;
import com.camera.lingxiao.common.app.ContentValue;
import com.camera.lingxiao.common.http.ParseHelper;
import com.camera.lingxiao.common.observer.HttpRxCallback;
import com.camera.lingxiao.common.retrofit.HttpRequest;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.lingxiaosuse.picture.tudimension.modle.HotModle;
import com.trello.rxlifecycle2.LifecycleProvider;

public class HotTrans extends BaseTransation{

    public HotTrans(LifecycleProvider lifecycle) {
        super(lifecycle);
    }
    public void getHotResult(int current, int page, HttpRxCallback callback){
        String url = ContentValue.GANKURL +current +"/"+page;
        /**
         * 解析数据
         */
        callback.setParseHelper(new ParseHelper() {
            @Override
            public Object[] parse(JsonElement jsonElement) {
                HotModle modle = new Gson().fromJson(jsonElement, HotModle.class);
                Object[] obj = new Object[1];
                obj[0] = modle;
                return obj;
            }
        });
        getRequest().requestOther(url,mLifecycle,callback);
    }
}
