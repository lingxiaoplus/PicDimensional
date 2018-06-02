package com.camera.lingxiao.common.observable;

import com.camera.lingxiao.common.http.HttpResultFunction;
import com.camera.lingxiao.common.http.ServerResultFunction;
import com.camera.lingxiao.common.http.response.HttpResponse;
import com.trello.rxlifecycle2.LifecycleProvider;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author lingxiao
 * 适用Retrofit网络请求Observable(被观察者)
 */
public class HttpRxObservable {
    /**
     * 获取被观察者 网络请求Observable构建
     * @param apiObservable
     * @param provider LifecycleProvider自动管理生命周期,避免内存溢出
     * @return
     */
    public static Observable getObservable(Observable<HttpResponse> apiObservable, LifecycleProvider provider){
        Observable observable;
        observable = apiObservable
                .map(new ServerResultFunction()) //返回一个Observable(将上个Observable的发射的每个Emitter都经过指定函数变化)，并将变化后的事件发射。
                .compose(provider.bindToLifecycle()) //需要在这个位置添加
                .onErrorResumeNext(new HttpResultFunction<>())
                .subscribeOn(Schedulers.io()) //指定observable发送事件的线程
                .observeOn(AndroidSchedulers.mainThread()); //指定Observer接收事件的线程
        return observable;
    }
}
