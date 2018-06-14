package com.camera.lingxiao.common.app;

import com.camera.lingxiao.common.retrofit.HttpRequest;

public class BaseTransation {
    protected HttpRequest mHttpRequest;
    public BaseTransation() {
        mHttpRequest = new HttpRequest();
    }

    protected HttpRequest getRequest() {
        if (mHttpRequest == null) {
            mHttpRequest = new HttpRequest();
        }
        return mHttpRequest;
    }
}
