package com.camera.lingxiao.common.observable;

import com.camera.lingxiao.common.http.HttpResultFunction;
import com.camera.lingxiao.common.http.ServerResultFunction;
import com.camera.lingxiao.common.http.response.HttpResponse;
import com.camera.lingxiao.common.observer.HttpRxCallback;
import com.camera.lingxiao.common.utills.LogUtils;
import com.google.gson.Gson;
import com.trello.rxlifecycle2.LifecycleProvider;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.trello.rxlifecycle2.android.FragmentEvent;

import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
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

    /**
     * 获取被监听者
     * 备注:网络请求Observable构建
     * data:网络请求参数
     * <h1>补充说明</h1>
     * 无管理生命周期,容易导致内存溢出
     */
    public static Observable getObservable(Observable<HttpResponse> apiObservable, final HttpRxCallback callback) {
        // showLog(request);
        Observable observable = apiObservable
                .map(new ServerResultFunction())
                .onErrorResumeNext(new HttpResultFunction<>())
                .doOnDispose(new Action() {
                    @Override
                    public void run() throws Exception {
                        if (callback != null){
                            callback.onCanceled();
                        }
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        return observable;
    }


    /**
     * 获取被监听者
     * 备注:网络请求Observable构建
     * data:网络请求参数
     * <h1>补充说明</h1>
     * 传入LifecycleProvider自动管理生命周期,避免内存溢出
     * 备注:需要继承RxActivity.../RxFragment...
     *
     */
    public static Observable getObservable(Observable<HttpResponse> apiObservable, LifecycleProvider lifecycle, final HttpRxCallback callback) {
        //showLog(request);
        Observable observable;

        if (lifecycle != null) {
            //随生命周期自动管理.eg:onCreate(start)->onStop(end)
            observable = apiObservable
                    .map(new ServerResultFunction())
                    .compose(lifecycle.bindToLifecycle())//需要在这个位置添加
                    .onErrorResumeNext(new HttpResultFunction<>())
                    .doOnDispose(new Action() {
                        @Override
                        public void run() throws Exception {
                            if (callback != null){
                                callback.onCanceled();
                            }
                        }
                    })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
            ;
        } else {
            observable = getObservable(apiObservable, callback);
        }
        return observable;
    }

    /**
     * 获取被监听者
     * 备注:网络请求Observable构建
     * data:网络请求参数
     * <h1>补充说明</h1>
     * 传入LifecycleProvider<ActivityEvent>手动管理生命周期,避免内存溢出
     * 备注:需要继承RxActivity,RxAppCompatActivity,RxFragmentActivity
     *
     */
    public static Observable getObservable(Observable<HttpResponse> apiObservable, LifecycleProvider<ActivityEvent> lifecycle, ActivityEvent event, final HttpRxCallback callback) {
        // showLog(request);
        Observable observable;
        if (lifecycle != null) {
            //手动管理移除监听生命周期.eg:ActivityEvent.STOP
            observable = apiObservable
                    .map(new ServerResultFunction())
                    .compose(lifecycle.bindUntilEvent(event))//需要在这个位置添加
                    .onErrorResumeNext(new HttpResultFunction<>())
                    .doOnDispose(new Action() {
                        @Override
                        public void run() throws Exception {
                            if (callback != null){
                                callback.onCanceled();
                            }
                        }
                    })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());
        } else {
            observable = getObservable(apiObservable, callback);
        }
        return observable;
    }


    /**
     * 获取被监听者
     * 备注:网络请求Observable构建
     * data:网络请求参数
     * <h1>补充说明</h1>
     * 传入LifecycleProvider<FragmentEvent>手动管理生命周期,避免内存溢出
     * 备注:需要继承RxFragment,RxDialogFragment
     *
     */
    public static Observable getObservable(Observable<HttpResponse> apiObservable, LifecycleProvider<FragmentEvent> lifecycle, FragmentEvent event, final HttpRxCallback callback) {
        //  showLog(request);
        Observable observable;
        if (lifecycle != null) {
            //手动管理移除监听生命周期.eg:FragmentEvent.STOP
            observable = apiObservable
                    .map(new ServerResultFunction())
                    .compose(lifecycle.bindUntilEvent(event))//需要在这个位置添加
                    .onErrorResumeNext(new HttpResultFunction<>())
                    .doOnDispose(new Action() {
                        @Override
                        public void run() throws Exception {
                            if (callback != null)
                                callback.onCanceled();
                        }
                    })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());
        } else {
            observable = getObservable(apiObservable, callback);
        }
        return observable;
    }


    /**
     * 打印log
     *
     * @author ZhongDaFeng
     */
    private static void showLog(Map<String, Object> request) {
        if (request == null || request.size() == 0) {
            LogUtils.e("[http request]:");
        }
        LogUtils.e("[http request]:" + new Gson().toJson(request));
    }
}
