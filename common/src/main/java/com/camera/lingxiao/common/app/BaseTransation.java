package com.camera.lingxiao.common.app;

import com.camera.lingxiao.common.retrofit.HttpRequest;
import com.trello.rxlifecycle2.LifecycleProvider;

import java.util.TreeMap;

public class BaseTransation {
    protected LifecycleProvider mLifecycle;
    protected HttpRequest mHttpRequest;
    protected TreeMap<String, Object> request;
    public BaseTransation(LifecycleProvider lifecycle) {
        mLifecycle = lifecycle;
        mHttpRequest = new HttpRequest();
        request = new TreeMap<>();
    }

    protected HttpRequest getRequest() {
        if (mHttpRequest == null) {
            mHttpRequest = new HttpRequest();
        }
        return mHttpRequest;
    }
}
