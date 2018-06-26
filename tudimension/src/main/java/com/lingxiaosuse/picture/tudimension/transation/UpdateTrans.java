package com.lingxiaosuse.picture.tudimension.transation;

import com.camera.lingxiao.common.VersionModle;
import com.camera.lingxiao.common.app.BaseTransation;
import com.camera.lingxiao.common.app.ContentValue;
import com.camera.lingxiao.common.http.ParseHelper;
import com.camera.lingxiao.common.observer.HttpRxCallback;
import com.camera.lingxiao.common.retrofit.HttpRequest;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.lingxiaosuse.picture.tudimension.modle.VerticalModle;
import com.trello.rxlifecycle2.LifecycleProvider;

import java.util.TreeMap;

public class UpdateTrans extends BaseTransation{

    public UpdateTrans(LifecycleProvider lifecycle){
        super(lifecycle);
    }
    /**
     * 获取服务器版本信息
     * @param callback
     */
    public void getUpdate(HttpRxCallback callback) {
        /**
         * 构建请求参数
         */
        request.clear();
        request.put(HttpRequest.API_URL, ContentValue.UPDATEURL);

        /**
         * 解析数据
         */
        callback.setParseHelper(new ParseHelper() {
            @Override
            public Object[] parse(JsonElement jsonElement) {
                VersionModle bean = new Gson().fromJson(jsonElement, VersionModle.class);
                Object[] obj = new Object[1];
                obj[0] = bean;
                return obj;
            }
        });

        /**
         * 发送请求
         */
        getRequest().request(HttpRequest.Method.GET, request, mLifecycle, callback);
    }


    public void getSplashImgUrl(HttpRxCallback callback){
        /**
         * 构建请求参数
         */
        request.clear();
        request.put(HttpRequest.API_URL, ContentValue.VERTICAL_URLS);

        /**
         * 解析数据
         */
        callback.setParseHelper(new ParseHelper() {
            @Override
            public Object[] parse(JsonElement jsonElement) {
                VerticalModle verticalModle = new Gson().fromJson(jsonElement, VerticalModle.class);
                Object[] obj = new Object[1];
                obj[0] = verticalModle;
                return obj;
            }
        });

        /**
         * 发送请求
         */
        getRequest().request(HttpRequest.Method.GET, request, mLifecycle, callback);
    }
}
