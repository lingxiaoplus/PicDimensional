package com.camera.lingxiao.common.app;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.trello.rxlifecycle2.components.RxFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public abstract class BaseFragment extends RxFragment{
    protected View mRoot;

    private Unbinder mRootUnbinder;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        initArgs(getArguments());

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

        initData();
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
}
