package com.lingxiaosuse.picture.tudimension.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lingxiaosuse.picture.tudimension.R;

/**
 * Created by lingxiao on 2017/8/28.
 */

public abstract class BaseFragment extends Fragment{

    public FloatingActionButton faButton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = initView();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //暂时解决fab点击的问题
        faButton = getActivity()
                .findViewById(R.id.fab_main);
        initData();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && faButton != null && getRecycle() != null){
            faButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getRecycle().smoothScrollToPosition(0);
                }
            });
        }

    }

    protected abstract void initData();

    public abstract View initView();

    public abstract RecyclerView getRecycle();

}
