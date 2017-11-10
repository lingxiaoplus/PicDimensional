package com.lingxiaosuse.picture.tudimension.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lingxiaosuse.picture.tudimension.R;
import com.lingxiaosuse.picture.tudimension.activity.VerticalActivity;
import com.lingxiaosuse.picture.tudimension.adapter.BaseRecycleAdapter;
import com.lingxiaosuse.picture.tudimension.adapter.VerticalAdapter;
import com.lingxiaosuse.picture.tudimension.modle.VerticalModle;
import com.lingxiaosuse.picture.tudimension.retrofit.RetrofitHelper;
import com.lingxiaosuse.picture.tudimension.retrofit.VerticalInterface;
import com.lingxiaosuse.picture.tudimension.utils.UIUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by lingxiao on 17-11-10.
 */

public class VerticalFragment extends Fragment{

    private VerticalAdapter mAdapter;
    private int position;
    private RecyclerView mRecyclerView;
    private List<VerticalModle.ResBean.VerticalBean> mPicList = new ArrayList<>();
    private int skip = 0;
    private SwipeRefreshLayout refreshLayout;
    private FloatingActionButton faButton;
    private StaggeredGridLayoutManager manager;
    private VerticalActivity activity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = UIUtils.inflate(R.layout.vertical_pager_item);
        refreshLayout = view.findViewById(R.id.swip_vertical_item);
        mRecyclerView = view.findViewById(R.id.recycle_vertical_item);
        manager = new StaggeredGridLayoutManager(3,
                StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(manager);
        activity = (VerticalActivity) getActivity();
        activity.setOnFabClick(new VerticalActivity.FabClickListener() {
            @Override
            public void onTabClick() {
                Log.i("code", "fragment接受到了事件: "+mRecyclerView);
                mRecyclerView.smoothScrollToPosition(0);
            }
        });
        return view;
    }


    private void getDataFromServer(int limit,int skip,String order) {
        RetrofitHelper.getInstance(UIUtils.getContext())
                .getInterface(VerticalInterface.class)
                .verticalModle(limit,skip,false,order)
                .enqueue(new Callback<VerticalModle>() {
                    @Override
                    public void onResponse(Call<VerticalModle> call,
                                           Response<VerticalModle> response) {
                        List<VerticalModle.ResBean.VerticalBean> verticalBeanList =
                                response.body().getRes().getVertical();
                        mPicList.addAll(verticalBeanList);
                        mAdapter.notifyDataSetChanged();
                        if (refreshLayout.isRefreshing()){
                            refreshLayout.setRefreshing(false);
                        }
                    }

                    @Override
                    public void onFailure(Call<VerticalModle> call, Throwable t) {
                        if (refreshLayout.isRefreshing()){
                            refreshLayout.setRefreshing(false);
                        }
                        Log.i("VerticalActivity", "onFailure: 获取手机壁纸json失败");
                    }
                });
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        try {
            Bundle bundle = getArguments();
            final String order = bundle.getString("order");
            mAdapter = new VerticalAdapter(mPicList,0,1);
            mRecyclerView.setAdapter(mAdapter);
            mAdapter.setRefreshListener(new BaseRecycleAdapter.onLoadmoreListener() {
                @Override
                public void onLoadMore() {
                    if (skip<300){
                        skip+=30;
                    }else {
                        mAdapter.isFinish(true);
                    }
                    getDataFromServer(30,skip,order);
                }
            });
            getDataFromServer(30,skip,order);
            refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    getDataFromServer(30,0,order);
                }
            });
        }catch (NullPointerException e){
            Log.i("verticalFragment", "获取bundle数据失败");
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
    }

}
