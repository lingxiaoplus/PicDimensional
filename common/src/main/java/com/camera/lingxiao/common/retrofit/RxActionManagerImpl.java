package com.camera.lingxiao.common.retrofit;

import android.util.ArrayMap;

import io.reactivex.disposables.Disposable;

/**
 * @author lingxiao
 *
 */
public class RxActionManagerImpl implements RxActionManager{

    private static volatile RxActionManagerImpl mInstance;
    private ArrayMap<Object,Disposable> mMaps; //处理请求列表

    public static RxActionManagerImpl getInstance(){
        if (mInstance == null){
            synchronized (RxActionManagerImpl.class){
                if (mInstance == null){
                    mInstance = new RxActionManagerImpl();
                }
            }
        }
        return mInstance;
    }

    private RxActionManagerImpl(){
        mMaps = new ArrayMap<>();
    }
    @Override
    public void add(Object tag, Disposable disposable) {
        mMaps.put(tag, disposable);
    }

    @Override
    public void remove(Object tag) {
        if (!mMaps.isEmpty()){
            mMaps.remove(tag);
        }
    }

    /**
     * 取消订阅事件
     * @param tag
     */
    @Override
    public void cancel(Object tag) {
        if (mMaps.isEmpty()){
            return;
        }
        if (mMaps.get(tag) == null){
            return;
        }
        //如果不是处于dispose的状态
        //切断所有订阅事件，防止内存泄漏
        if (!mMaps.get(tag).isDisposed()){
            mMaps.get(tag).dispose();
        }
    }

    /**
     * 判断是否取消了请求
     * @param tag
     * @return
     */
    public boolean isDisposed(Object tag){
        if (mMaps.get(tag) == null || mMaps.isEmpty()){
            return true;
        }
        return mMaps.get(tag).isDisposed();
    }
}
