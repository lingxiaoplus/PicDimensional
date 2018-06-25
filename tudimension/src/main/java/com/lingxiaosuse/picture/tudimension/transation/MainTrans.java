package com.lingxiaosuse.picture.tudimension.transation;

import com.camera.lingxiao.common.app.BaseTransation;
import com.camera.lingxiao.common.app.ContentValue;
import com.camera.lingxiao.common.http.ParseHelper;
import com.camera.lingxiao.common.observer.HttpRxCallback;
import com.camera.lingxiao.common.retrofit.HttpRequest;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.lingxiaosuse.picture.tudimension.modle.HitokotoModle;
import com.lingxiaosuse.picture.tudimension.modle.HomePageModle;
import com.trello.rxlifecycle2.LifecycleProvider;

import java.util.TreeMap;

public class MainTrans extends BaseTransation{
    public MainTrans(LifecycleProvider lifecycle) {
        super(lifecycle);
    }
    public void getHeadImg(int limit,int skip,HttpRxCallback callback){
        request.clear();
        request.put(HttpRequest.API_URL, ContentValue.HOMEPAGE_URL);
        request.put("limit",limit);
        request.put("skip",skip);
        request.put("adult",false);
        /**
         * 解析数据
         */
        callback.setParseHelper(new ParseHelper() {
            @Override
            public Object[] parse(JsonElement jsonElement) {
                HomePageModle bean = new Gson().fromJson(jsonElement, HomePageModle.class);
                Object[] obj = new Object[1];
                obj[0] = bean;
                return obj;
            }
        });
        getRequest().request(HttpRequest.Method.GET,request,mLifecycle,callback);
    }

    public void getHeadText(HttpRxCallback callback){
        request.clear();
        request.put(HttpRequest.API_URL, ContentValue.HITOKOTO_URL);
        /**
         * 解析数据
         */
        callback.setParseHelper(new ParseHelper() {
            @Override
            public Object[] parse(JsonElement jsonElement) {
                HitokotoModle bean = new Gson().fromJson(jsonElement, HitokotoModle.class);
                Object[] obj = new Object[1];
                obj[0] = bean;
                return obj;
            }
        });
        getRequest().request(HttpRequest.Method.GET,request,mLifecycle,callback);
    }
}
