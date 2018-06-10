package com.camera.lingxiao.common;

import com.camera.lingxiao.common.exception.ApiException;
import com.camera.lingxiao.common.http.ParseHelper;
import com.camera.lingxiao.common.observable.HttpRxObservable;
import com.camera.lingxiao.common.observer.HttpRxCallback;
import com.camera.lingxiao.common.observer.HttpRxObserver;
import com.camera.lingxiao.common.retrofit.HttpRequest;
import com.camera.lingxiao.common.utills.LogUtils;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.trello.rxlifecycle2.LifecycleProvider;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.trello.rxlifecycle2.components.RxActivity;

import java.util.Map;
import java.util.TreeMap;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

public class RetrofitTest {
    /**
     * 登录API
     */
    private final String API_LOGIN = "tudimension/update.json";

    protected HttpRequest mHttpRequest;

    public RetrofitTest() {
        mHttpRequest = new HttpRequest();
    }

    protected HttpRequest getRequest() {
        if (mHttpRequest == null) {
            mHttpRequest = new HttpRequest();
        }
        return mHttpRequest;
    }
    /**
     * 用户登录
     * @param callback
     */
    public void login(HttpRxCallback callback) {
        /**
         * 构建请求参数
         */
        TreeMap<String, Object> request = new TreeMap<>();
        //request.put("username", userName);
        //request.put("password", password);
        request.put(HttpRequest.API_URL, API_LOGIN);

        /**
         * 解析数据
         */
        callback.setParseHelper(new ParseHelper() {
            @Override
            public Object[] parse(JsonElement jsonElement) {
                /*UserBean bean = new Gson().fromJson(jsonElement, UserBean.class);
                Object[] obj = new Object[1];
                obj[0] = bean;*/
                VersionModle modle = new Gson().fromJson(jsonElement,VersionModle.class);
                Object[] obj = new Object[1];
                obj[0] = modle;
                return obj;
            }
        });

        /**
         * 发送请求
         */
        //getRequest().request(HttpRequest.Method.POST, request, lifecycle, callback);
        getRequest().request(HttpRequest.Method.GET,request,callback);
    }
}
