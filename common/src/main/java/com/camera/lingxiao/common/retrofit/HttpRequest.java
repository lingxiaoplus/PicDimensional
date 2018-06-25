package com.camera.lingxiao.common.retrofit;

import com.camera.lingxiao.common.api.UserApi;
import com.camera.lingxiao.common.http.response.HttpResponse;
import com.camera.lingxiao.common.observable.HttpRxObservable;
import com.camera.lingxiao.common.observer.HttpRxCallback;
import com.camera.lingxiao.common.utills.LogUtils;
import com.camera.lingxiao.common.utills.RetrofitUtil;
import com.trello.rxlifecycle2.LifecycleProvider;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.trello.rxlifecycle2.android.FragmentEvent;

import java.security.Key;
import java.util.TreeMap;

import io.reactivex.Observable;

/**
 * http请求类
 */
public class HttpRequest {
    public static final String API_URL = "API_URL";

    private final String appKey = "1889b37351288";
    private final String k_key = "key";
    //第三个是安卓壁纸
    public enum Method{
        GET,
        POST
    }

    /**
     * 发送请求 不管理生命周期
     * @param method
     * @param prams
     * @param callback
     */
    public void request(Method method, TreeMap<String,Object> prams, HttpRxCallback callback){
        Observable<HttpResponse> apiObservable = handleRequest(method, prams);

        HttpRxObservable.getObservable(apiObservable, callback).subscribe(callback);
    }

    /**
     * 发送请求
     * 备注:自动管理生命周期
     *
     * @param method    请求方式
     * @param lifecycle 实现RxActivity/RxFragment 参数为空不管理生命周期
     * @param prams     参数集合
     * @param callback  回调
     */
    public void request(Method method, TreeMap<String, Object> prams, LifecycleProvider lifecycle, HttpRxCallback callback) {
        Observable<HttpResponse> apiObservable = handleRequest(method, prams);

        HttpRxObservable.getObservable(apiObservable, lifecycle, callback).subscribe(callback);
    }


    /**
     * 发送请求
     * 备注:手动指定生命周期-Activity
     *
     * @param method    请求方式
     * @param lifecycle 实现RxActivity
     * @param event     指定生命周期
     * @param prams     参数集合
     * @param callback  回调
     */
    public void request(Method method, TreeMap<String, Object> prams, LifecycleProvider<ActivityEvent> lifecycle, ActivityEvent event, HttpRxCallback callback) {
        Observable<HttpResponse> apiObservable = handleRequest(method, prams);

        HttpRxObservable.getObservable(apiObservable, lifecycle, event, callback).subscribe(callback);
    }

    /**
     * 发送请求
     * 备注:手动指定生命周期-Fragment
     *
     * @param method    请求方式
     * @param lifecycle 实现RxFragment
     * @param event     指定生命周期
     * @param prams     参数集合
     * @param callback  回调
     */
    public void request(Method method, TreeMap<String, Object> prams, LifecycleProvider<FragmentEvent> lifecycle, FragmentEvent event, HttpRxCallback callback) {
        Observable<HttpResponse> apiObservable = handleRequest(method, prams);

        HttpRxObservable.getObservable(apiObservable, lifecycle, event, callback).subscribe(callback);
    }
    /**
     * 发送请求 安卓壁纸
     * 备注:手动指定生命周期-Fragment
     *
     * @param lifecycle 实现RxFragment
     * @param prams     参数集合
     * @param callback  回调
     * @param strings 5 个参数
     */
    public void request(TreeMap<String, Object> prams, LifecycleProvider lifecycle, HttpRxCallback callback, String... strings) {
        Observable<HttpResponse> apiObservable;
        TreeMap<String,Object> map = getBaseRequest();
        //添加业务参数
        map.putAll(prams);
        String apiUrl = "";
        if (map.containsKey(API_URL)){
            apiUrl = String.valueOf(map.get(API_URL));
            //移除apiurl参数  此参数不纳入业务参数
            map.remove(API_URL);
        }
        apiObservable = RetrofitUtil
                .get()
                .retrofit()
                .create(UserApi.class)
                .desk(strings[0],strings[1],strings[2],strings[3],strings[4], map);

        HttpRxObservable.getObservable(apiObservable, lifecycle, callback).subscribe(callback);
    }
    /**
     * 预处理请求
     * @param method 请求方法
     * @param prams 参数集合
     * @return
     */
    private Observable<HttpResponse> handleRequest(Method method,TreeMap<String,Object> prams){
        TreeMap<String,Object> map = getBaseRequest();
        //添加业务参数
        map.putAll(prams);
        String apiUrl = "";
        if (map.containsKey(API_URL)){
            apiUrl = String.valueOf(map.get(API_URL));
            //移除apiurl参数  此参数不纳入业务参数
            map.remove(API_URL);
        }
        Observable<HttpResponse> apiObservable;
        switch (method) {
            case GET:
                apiObservable = RetrofitUtil.get().retrofit().create(UserApi.class).get(apiUrl, map);
                break;
            case POST:
                apiObservable = RetrofitUtil.get().retrofit().create(UserApi.class).post(apiUrl, map);
                break;
            default:
                apiObservable = RetrofitUtil.get().retrofit().create(UserApi.class).post(apiUrl, map);
                break;
        }
        return apiObservable;
    }
    /**
     * 获取基础request参数
     * @return
     */
    private TreeMap<String,Object> getBaseRequest(){
        TreeMap<String,Object> map = new TreeMap<>();
        //map.put(k_key,appKey);
        return map;
    }

}
