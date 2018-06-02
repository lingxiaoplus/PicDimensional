package com.camera.lingxiao.common.http;

import com.camera.lingxiao.common.utills.LogUtils;
import com.camera.lingxiao.common.exception.ExceptionEngine;

import io.reactivex.Observable;
import io.reactivex.functions.Function;

/**
 * @author lingxiao
 * 使用onErrorResumeNext(new HttpResultFunction<>())操作符
 * 对Retrofit网络请求抛出的Exception进行处理
 * @param <T>
 */
public class HttpResultFunction<T> implements Function<Throwable, Observable<T>> {

    @Override
    public Observable<T> apply(Throwable throwable) throws Exception {
        //打印具体错误
        LogUtils.e("HttpResultFunction:" + throwable);
        return Observable.error(ExceptionEngine.handlerException(throwable));
    }
}
