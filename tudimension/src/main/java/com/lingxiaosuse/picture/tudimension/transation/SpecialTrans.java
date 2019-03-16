package com.lingxiaosuse.picture.tudimension.transation;

import com.camera.lingxiao.common.app.BaseTransation;
import com.camera.lingxiao.common.app.ContentValue;
import com.camera.lingxiao.common.http.ParseHelper;
import com.camera.lingxiao.common.observer.HttpRxCallback;
import com.camera.lingxiao.common.http.retrofit.HttpRequest;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.lingxiaosuse.picture.tudimension.modle.SpecialModle;
import com.trello.rxlifecycle2.LifecycleProvider;

public class SpecialTrans extends BaseTransation{

    public SpecialTrans(LifecycleProvider lifecycle) {
        super(lifecycle);
    }

    public void getSpecialResult(int limit, int skip, HttpRxCallback callback){
        request.clear();
        request.put(HttpRequest.API_URL, ContentValue.SPECIAL_URL);
        request.put("limit",limit);
        request.put("skip",skip);
        request.put("adult",false);
        /**
         * 解析数据
         */
        callback.setParseHelper(new ParseHelper() {
            @Override
            public Object[] parse(JsonElement jsonElement) {
                SpecialModle bean = new Gson().fromJson(jsonElement, SpecialModle.class);
                Object[] obj = new Object[1];
                obj[0] = bean;
                return obj;
            }
        });
        getRequest().request(HttpRequest.Method.GET,request,mLifecycle,callback);
    }
}
