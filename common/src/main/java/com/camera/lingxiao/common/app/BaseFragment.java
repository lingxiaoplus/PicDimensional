package com.camera.lingxiao.common.app;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.camera.lingxiao.common.R;
import com.camera.lingxiao.common.listener.LifeCycleListener;
import com.camera.lingxiao.common.utills.LogUtils;
import com.trello.rxlifecycle2.components.support.RxFragment;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import pub.devrel.easypermissions.EasyPermissions;

import static android.content.ContentValues.TAG;


public abstract class BaseFragment extends RxFragment implements EasyPermissions.PermissionCallbacks{
    protected View mRoot;

    private Unbinder mRootUnbinder;
    public LifeCycleListener mListener;
    private ProgressDialog progressDialog;

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
        /*if (mRoot == null){
            int layId = getContentLayoutId();
            //初始化当前的跟布局，但是不在创建时就添加到container中
            View root = inflater.inflate(layId,container,false);
            initWidget(root);
            mRoot = root;
            LogUtils.i("BaseFragment是空：");
        }else {
            if (mRoot.getParent() != null){
                //把当前root从父控件中移除
                ((ViewGroup) mRoot.getParent()).removeView(mRoot);
                LogUtils.i("BaseFragment不是空,并且执行了移除");
            }
            LogUtils.i("BaseFragment不是空：");
        }*/
        // TODO: 18-6-29 上面的方式会报空指针 ,因为我使用的弱引用
        int layId = getContentLayoutId();
        View root = inflater.inflate(layId,container,false);
        initWidget(root);
        mRoot = root;
        return mRoot;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (!setLazyMode()){
            initData();
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (mListener != null) {
            mListener.onActivityCreated(savedInstanceState);
        }
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        //单个的fragment会存在问题
        if (isVisibleToUser){
            if (setLazyMode()){
                initData();
            }
        }
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
            Log.e(TAG, "onDestroy: fragment销毁了");
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
     * 是否开启懒加载，如果只有一个fragment，则重写该方法
     * @return
     */
    protected boolean setLazyMode(){
        return false;
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

    /**
     * 滑动显示隐藏floatingactionbutton
     * @param recyclerView
     * @param fab
     */
    protected void floatingBtnToogle(final RecyclerView recyclerView, final FloatingActionButton fab){
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy>0){
                    fab.hide();
                }else {
                    fab.show();
                }
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerView.smoothScrollToPosition(0);
            }
        });
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

    /**
     *显示进度条
     */
    public void showProgressDialog(String msg,Context context){
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(msg);
        progressDialog.show();
    }

    public void cancleProgressDialog(){
        if (progressDialog != null){
            progressDialog.dismiss();
        }
    }
}
