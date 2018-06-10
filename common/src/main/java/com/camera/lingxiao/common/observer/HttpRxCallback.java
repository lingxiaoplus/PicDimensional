package com.camera.lingxiao.common.observer;

import android.text.TextUtils;

import com.camera.lingxiao.common.exception.ApiException;
import com.camera.lingxiao.common.exception.ExceptionEngine;
import com.camera.lingxiao.common.http.ParseHelper;
import com.camera.lingxiao.common.retrofit.HttpRequestListener;
import com.camera.lingxiao.common.retrofit.RxActionManagerImpl;
import com.camera.lingxiao.common.utills.LogUtils;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

public abstract class HttpRxCallback<T> implements Observer<T>,HttpRequestListener{
    private String mTag;//请求标识
    private ParseHelper parseHelper;//数据解析

    public HttpRxCallback(){
        this.mTag = String.valueOf(System.currentTimeMillis());
    }

    public HttpRxCallback(String tag){
        this.mTag = tag;
    }

    /**
     * 手动取消请求
     */
    @Override
    public void cancel() {
        if (!TextUtils.isEmpty(mTag)) {
            RxActionManagerImpl.getInstance().cancel(mTag);
        }
    }

    /**
     * 请求被取消
     */
    @Override
    public void onCanceled() {
        onCancel();
    }

    @Override
    public void onSubscribe(Disposable d) {
        if (!TextUtils.isEmpty(mTag)) {
            RxActionManagerImpl.getInstance().add(mTag, d);
        }
    }

    @Override
    public void onNext(@NonNull T t) {
        if (!TextUtils.isEmpty(mTag)) {
            RxActionManagerImpl.getInstance().remove(mTag);
        }
        try {
            JsonElement jsonElement = new JsonParser().parse((String) t);
            if (parseHelper != null) {
                Object[] res = parseHelper.parse(jsonElement);
                onSuccess(res);
            } else {
                onSuccess(jsonElement);
            }
        } catch (JsonSyntaxException jsonException) {
            LogUtils.e("JsonSyntaxException:" + jsonException.getMessage());
            onError(ExceptionEngine.ANALYTIC_SERVER_DATA_ERROR, "解析错误");
        }
    }

    @Override
    public void onError(Throwable e) {
        RxActionManagerImpl.getInstance().remove(mTag);
        if (e instanceof ApiException) {
            ApiException exception = (ApiException) e;
            int code = exception.getCode();
            String msg = exception.getMsg();
            if (code == 1001) { //系统公告(示例)
                //此处在UI主线程
            } else if (code == 1002) {//token失效
                //处理对应的逻辑
            } else {//其他错误回调
                onError(code, msg);
            }
        } else {
            onError(ExceptionEngine.UN_KNOWN_ERROR, "未知错误");
        }
    }

    @Override
    public void onComplete() {

    }

    /**
     * 是否已经处理
     *
     * @author ZhongDaFeng
     */
    public boolean isDisposed() {
        if (TextUtils.isEmpty(mTag)) {
            return true;
        }
        return RxActionManagerImpl.getInstance().isDisposed(mTag);
    }

    /**
     * 设置解析回调
     * @param parseHelper
     */
    public void setParseHelper(ParseHelper parseHelper) {
        this.parseHelper = parseHelper;
    }

    /**
     * 成功回调
     *
     * @param object
     */
    public abstract void onSuccess(Object... object);

    /**
     * 失败回调
     *
     * @param code
     * @param desc
     */
    public abstract void onError(int code, String desc);

    /**
     * 取消回调
     */
    public abstract void onCancel();
}
