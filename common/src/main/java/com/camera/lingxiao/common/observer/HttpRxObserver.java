package com.camera.lingxiao.common.observer;


import android.text.TextUtils;

import com.camera.lingxiao.common.exception.ApiException;
import com.camera.lingxiao.common.exception.ExceptionEngine;
import com.camera.lingxiao.common.http.retrofit.HttpRequestListener;
import com.camera.lingxiao.common.http.retrofit.RxActionManagerImpl;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public abstract class HttpRxObserver<T> implements Observer<T>,HttpRequestListener{
    private String mTag;//请求标识


    public HttpRxObserver(){
        this.mTag = String.valueOf(System.currentTimeMillis());
    }
    public HttpRxObserver(String s) {
        this.mTag = s;
    }

    @Override
    public void cancel() {
        if (!TextUtils.isEmpty(mTag)){
            RxActionManagerImpl.getInstance().cancel(mTag);
        }
    }

    @Override
    public void onCanceled() {

    }

    /**
     * 添加请求标识
     * @param d
     */
    @Override
    public void onSubscribe(Disposable d) {
        if (!TextUtils.isEmpty(mTag)){
            RxActionManagerImpl.getInstance().add(mTag,d);
        }
        onStart(d);
    }

    /**
     * 移除请求
     * @param t
     */
    @Override
    public void onNext(T t) {
        if (!TextUtils.isEmpty(mTag)){
            RxActionManagerImpl.getInstance().remove(mTag);
        }
        onSuccess(t);
    }

    /**
     * 封装错误/异常处理 移除请求
     * @param e
     */
    @Override
    public void onError(Throwable e) {
        RxActionManagerImpl.getInstance().remove(mTag);
        if (e instanceof ApiException){
            onError(e);
        }else {
            onError(new ApiException(e, ExceptionEngine.UN_KNOWN_ERROR));
        }
    }

    @Override
    public void onComplete() {

    }

    public boolean isDisposed(){
        if (TextUtils.isEmpty(mTag)){
            return true;
        }
        return RxActionManagerImpl.getInstance().isDisposed(mTag);
    }
    protected abstract void onStart(Disposable d);

    /**
     * 错误/异常回调
     */
    protected abstract void onError(ApiException e);

    /**
     * 成功回调
     */
    protected abstract void onSuccess(T response);
}
