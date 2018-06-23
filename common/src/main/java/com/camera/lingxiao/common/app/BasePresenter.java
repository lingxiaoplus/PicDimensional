package com.camera.lingxiao.common.app;

import android.app.Activity;
import android.arch.lifecycle.Lifecycle;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.camera.lingxiao.common.listener.LifeCycleListener;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

/**
 * @author lingxiao
 * 使用java弱引用，管理View的引用，以及activity的引用，
 * 避免强引用导致资源无法释放而造成的内存溢出
 * @param <V>
 * @param <T>
 */
public class BasePresenter<V,T> implements LifeCycleListener{

    protected Reference<V> mViewRef;
    protected V mView;
    protected Reference<T> mActivityRef;
    protected T mActivity;

    public BasePresenter(V view,T activity){
        attachView(view);
        attachActivity(activity);
        setListener(activity);
    }

    /**
     * 设置生命周期监听
     * @param activity
     */
    private void setListener(T activity) {
        if (getActivity() != null){
            if (activity instanceof BaseActivity){
                ((BaseActivity) getActivity()).setOnLifeCycleListener(this);
            }else if (activity instanceof BaseFragment){
                ((BaseFragment) getActivity()).setOnLifeCycleListener(this);
            }
        }
    }

    /**
     * 关联act
     * @param activity
     */
    private void attachActivity(T activity) {
        mActivityRef = new WeakReference<T>(activity);
        mActivity = mActivityRef.get();
    }

    /**
     * 关联view
     * @param view
     */
    private void attachView(V view) {
        mViewRef = new WeakReference<V>(view);
        mView = mViewRef.get();
    }

    /**
     * 销毁view
     */
    private void detachView(){
        if (isViewAttached()) {
            mViewRef.clear();
            mViewRef = null;
        }
    }

    private void detachActivity() {
        if (isActivityAttached()) {
            mActivityRef.clear();
            mActivityRef = null;
        }
    }

    /**
     * 获取view
     * @return
     */
    public V getView(){
        if (mViewRef == null){
            return null;
        }
        return mViewRef.get();
    }

    /**
     * 获取activity
     * @return
     */
    public T getActivity(){
        if (mActivityRef == null){
            return null;
        }
        return mActivityRef.get();
    }

    /**
     * view是否已经关联
     * @return
     */
    public boolean isViewAttached(){
        return mViewRef != null && mViewRef.get() != null;
    }

    /**
     * activity是否已经关联
     * @return
     */
    public boolean isActivityAttached(){
        return mActivityRef != null && mActivityRef.get() != null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

    }

    @Override
    public void onStart() {

    }

    @Override
    public void onRestart() {

    }

    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onStop() {

    }

    @Override
    public void onDestroy() {
        detachActivity();
        detachView();
    }

    @Override
    public void onAttach(Activity activity) {

    }

    @Override
    public void onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle bundle) {

    }

    @Override
    public void onActivityCreated(Bundle bundle) {

    }

    @Override
    public void onDestroyView() {

    }

    @Override
    public void onDetach() {

    }
}
