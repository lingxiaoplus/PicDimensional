package com.camera.lingxiao.common.app;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.camera.lingxiao.common.R;
import com.camera.lingxiao.common.listener.LifeCycleListener;
import com.trello.rxlifecycle2.components.support.RxFragment;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import pub.devrel.easypermissions.EasyPermissions;


public abstract class BaseFragment extends RxFragment implements EasyPermissions.PermissionCallbacks{
    protected View mRoot;

    private Unbinder mRootUnbinder;
    public LifeCycleListener mListener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (mListener != null) {
            mListener.onAttach(activity);
        }
        initArgs(getArguments());
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (mListener != null) {
            mListener.onCreate(savedInstanceState);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mRoot == null){
            int layId = getContentLayoutId();
            //初始化当前的跟布局，但是不在创建时就添加到container中
            View root = inflater.inflate(layId,container,false);
            initWidget(root);
            mRoot = root;
        }else {
            if (mRoot.getParent() != null){
                //把当前root从父控件中移除
                ((ViewGroup) mRoot.getParent()).removeView(mRoot);
            }
        }
        return mRoot;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (mListener != null) {
            mListener.onActivityCreated(savedInstanceState);
        }
        initData();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mListener != null) {
            mListener.onStart();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mListener != null) {
            mListener.onResume();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mListener != null) {
            mListener.onPause();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mListener != null) {
            mListener.onStop();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mListener != null) {
            mListener.onDestroyView();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mListener != null) {
            mListener.onDestroy();
        }
        //移除view绑定
        if (mRootUnbinder != null) {
            mRootUnbinder.unbind();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (mListener != null) {
            mListener.onDetach();
        }
    }

    protected abstract int getContentLayoutId();
    /**
     * 初始化控件
     */
    protected void initWidget(View root){
        mRootUnbinder = ButterKnife.bind(this,root);
    }

    /**
     *  初始化数据
     */
    protected void initData(){

    }

    /**
     * 初始化相关参数
     */
    protected void initArgs(Bundle bundle){
    }

    /**
     *  返回按键出发
     *  @return true代表拦截
     */
    public boolean onBackPressed(){
        return false;
    }

    protected void setSwipeColor(SwipeRefreshLayout swipeLayout){
        swipeLayout.setColorSchemeResources(
                R.color.colorPrimary,
                android.R.color.holo_blue_light,
                android.R.color.holo_red_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_green_light);
    }
    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {

    }
    /**
     * 设置生命周期回调函数
     *
     * @param listener
     */
    public void setOnLifeCycleListener(LifeCycleListener listener) {
        mListener = listener;
    }
}
