package com.camera.lingxiao.common.app;

import com.camera.lingxiao.common.retrofit.HttpRequest;
import com.trello.rxlifecycle2.LifecycleProvider;

public class BaseTransation {
    protected LifecycleProvider mLifecycle;
    protected HttpRequest mHttpRequest;
    public BaseTransation(LifecycleProvider lifecycle) {
        mLifecycle = lifecycle;
        mHttpRequest = new HttpRequest();
    }

    protected HttpRequest getRequest() {
        if (mHttpRequest == null) {
            mHttpRequest = new HttpRequest();
        }
        return mHttpRequest;
    }
}
