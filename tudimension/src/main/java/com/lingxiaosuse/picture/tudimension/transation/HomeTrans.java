package com.lingxiaosuse.picture.tudimension.transation;

import com.camera.lingxiao.common.VersionModle;
import com.camera.lingxiao.common.app.BaseTransation;
import com.camera.lingxiao.common.app.ContentValue;
import com.camera.lingxiao.common.http.ParseHelper;
import com.camera.lingxiao.common.observer.HttpRxCallback;
import com.camera.lingxiao.common.retrofit.HttpRequest;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.lingxiaosuse.picture.tudimension.modle.BannerModle;
import com.lingxiaosuse.picture.tudimension.modle.HomePageModle;
import com.trello.rxlifecycle2.LifecycleProvider;

import java.util.TreeMap;

public class HomeTrans extends BaseTransation{
    public HomeTrans(LifecycleProvider lifecycle) {
        super(lifecycle);
    }

    public void getHomePage(int limit,int skip,HttpRxCallback callback){
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
    /**
     * 获取banner
     * @param callback
     */
    public void getBanner(String id,int limit,int skip,boolean adult,String type,String order,HttpRxCallback callback) {
        /**
         * 构建请求参数
         */
        request.clear();
        request.put(HttpRequest.API_URL, ContentValue.BANNER_URL);
        request.put("limit",limit);
        request.put("skip",skip);
        request.put("adult",adult);
        request.put("order",order);
        request.put("first",1);

        /**
         * 解析数据
         */
        callback.setParseHelper(new ParseHelper() {
            @Override
            public Object[] parse(JsonElement jsonElement) {
                BannerModle bean = new Gson().fromJson(jsonElement, BannerModle.class);
                Object[] obj = new Object[1];
                obj[0] = bean;
                return obj;
            }
        });

        /**
         * 发送请求
         */
        getRequest().request(request, mLifecycle, callback,"v1","wallpaper",type,id,"wallpaper");
    }
}
