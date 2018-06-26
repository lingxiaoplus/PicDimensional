package com.lingxiaosuse.picture.tudimension.transation;

import com.camera.lingxiao.common.app.BaseTransation;
import com.camera.lingxiao.common.app.ContentValue;
import com.camera.lingxiao.common.http.ParseHelper;
import com.camera.lingxiao.common.observer.HttpRxCallback;
import com.camera.lingxiao.common.retrofit.HttpRequest;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.lingxiaosuse.picture.tudimension.modle.HomePageModle;
import com.lingxiaosuse.picture.tudimension.modle.VerticalModle;
import com.trello.rxlifecycle2.LifecycleProvider;

public class VerticalTrans extends BaseTransation{
    public VerticalTrans(LifecycleProvider lifecycle) {
        super(lifecycle);
    }

    public void getVertical(int limit,int skip,String order,HttpRxCallback callback){
        request.clear();
        request.put(HttpRequest.API_URL, ContentValue.VERTICAL_URL);
        request.put("limit",limit);
        request.put("skip",skip);
        request.put("adult",false);
        request.put("order",order);
        /**
         * 解析数据
         */
        callback.setParseHelper(new ParseHelper() {
            @Override
            public Object[] parse(JsonElement jsonElement) {
                VerticalModle bean = new Gson().fromJson(jsonElement, VerticalModle.class);
                Object[] obj = new Object[1];
                obj[0] = bean;
                return obj;
            }
        });
        getRequest().request(HttpRequest.Method.GET,request,mLifecycle,callback);
    }
}
