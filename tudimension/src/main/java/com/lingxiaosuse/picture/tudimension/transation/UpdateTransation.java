package com.lingxiaosuse.picture.tudimension.transation;

import com.camera.lingxiao.common.VersionModle;
import com.camera.lingxiao.common.app.BaseTransation;
import com.camera.lingxiao.common.http.ParseHelper;
import com.camera.lingxiao.common.observer.HttpRxCallback;
import com.camera.lingxiao.common.retrofit.HttpRequest;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.trello.rxlifecycle2.LifecycleProvider;

import java.util.TreeMap;

public class UpdateTransation extends BaseTransation{
    /**
     * 号码归属地查询API
     */
    private final String API_UPDATE = "http://www.lingxiaosuse.cn/tudimension/update.json";

    public void getUpdate(LifecycleProvider lifecycle, HttpRxCallback callback) {
        /**
         * 构建请求参数
         */
        TreeMap<String, Object> request = new TreeMap<>();
        request.put(HttpRequest.API_URL, API_UPDATE);

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
        getRequest().request(HttpRequest.Method.GET, request, lifecycle, callback);

    }

}
