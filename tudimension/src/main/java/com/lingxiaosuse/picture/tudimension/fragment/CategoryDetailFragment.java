package com.lingxiaosuse.picture.tudimension.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;

import com.lingxiaosuse.picture.tudimension.R;
import com.lingxiaosuse.picture.tudimension.activity.ImageLoadingActivity;
import com.lingxiaosuse.picture.tudimension.adapter.BaseRecycleAdapter;
import com.lingxiaosuse.picture.tudimension.adapter.VerticalAdapter;
import com.lingxiaosuse.picture.tudimension.modle.CategoryDetailModle;
import com.lingxiaosuse.picture.tudimension.retrofit.CategoryDetailInterface;
import com.lingxiaosuse.picture.tudimension.retrofit.RetrofitHelper;
import com.lingxiaosuse.picture.tudimension.utils.UIUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by lingxiao on 17-11-13.
 */

public class CategoryDetailFragment extends BaseFragment{
    private RecyclerView mRecyclerView;
    private int skip = 0;
    private SwipeRefreshLayout refreshLayout;
    private FloatingActionButton faButton;
    private StaggeredGridLayoutManager manager;
    private String id;
    private List<CategoryDetailModle.ResBean.VerticalBean> verticalBeanList;
    private List<CategoryDetailModle.ResBean.VerticalBean> mPicList = new ArrayList<>();
    private ArrayList<String> picIdList = new ArrayList<>();
    private ArrayList<String> picUrlList = new ArrayList<>();
    private VerticalAdapter mAdapter;
    private FloatingActionButton floatingActionButton;

    @Override
    protected void initData() {
        try {
            Bundle bundle = getArguments();
            final String order = bundle.getString("order");
            id = bundle.getString("id");
            mAdapter = new VerticalAdapter(mPicList,0,1,true);
            mRecyclerView.setAdapter(mAdapter);
            mAdapter.setRefreshListener(new BaseRecycleAdapter.onLoadmoreListener() {
                @Override
                public void onLoadMore() {
                    if (skip<300){
                        skip+=30;
                    }else {
                        mAdapter.isFinish(true);
                    }
                    getDataFromServer(id,30,skip,order);
                }
            });
            getDataFromServer(id,30,skip,order);
            refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    getDataFromServer(id,30,0,order);
                }
            });
        }catch (NullPointerException e){
            Log.i("verticalFragment", "获取bundle数据失败");
        }
    }

    @Override
    public View initView() {
        View view = View.inflate(getContext(),R.layout.fragment_vertical_pager,null);
        refreshLayout = view.findViewById(R.id.swip_vertical_item);
        mRecyclerView = view.findViewById(R.id.recycle_vertical_item);
        floatingActionButton = view.findViewById(R.id.fab_vertical_fragment);
        manager = new StaggeredGridLayoutManager(3,
                StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(manager);

        refreshLayout.setRefreshing(true);

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0){
                    floatingActionButton.hide();
                }else {
                    floatingActionButton.show();
                }
            }
        });
        return view;
    }

    @Override
    public RecyclerView getRecycle() {
        return null;
    }

    private void getDataFromServer(String id,int limit,int skip,String order){
        RetrofitHelper
                .getInstance(UIUtils.getContext())
                .getInterface(CategoryDetailInterface.class)
                .cateModle(id,limit,skip,order)
                .enqueue(new Callback<CategoryDetailModle>() {
                    @Override
                    public void onResponse(Call<CategoryDetailModle> call, Response<CategoryDetailModle> response) {
                        verticalBeanList = response.body().getRes().getVertical();
                        mPicList.addAll(verticalBeanList);
                        mAdapter.notifyDataSetChanged();
                        if (refreshLayout.isRefreshing()){
                            refreshLayout.setRefreshing(false);
                        }
                        picUrlList.clear();
                        picIdList.clear();
                        for (int i = 0; i < mPicList.size(); i++) {
                            picIdList.add(mPicList.get(i).getId());
                            //修复图片名字识别失败
                            picUrlList.add(mPicList.get(i).getWp());
                        }

                        mAdapter.setOnItemClickListener(new BaseRecycleAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(View View, int position) {
                                Intent intent = new Intent(UIUtils.getContext(),
                                        ImageLoadingActivity.class);
                                intent.putExtra("position",position);
                                intent.putExtra("itemCount",mAdapter.getItemCount());
                                intent.putExtra("id",mPicList.get(position).getId());
                                intent.putExtra("isHot",true);
                                intent.putExtra("isVertical",true);
                                intent.putStringArrayListExtra("picList",picUrlList);
                                intent.putStringArrayListExtra("picIdList",picIdList);
                                startActivity(intent);
                            }
                        });
                    }

                    @Override
                    public void onFailure(Call<CategoryDetailModle> call, Throwable t) {
                        if (refreshLayout.isRefreshing()){
                            refreshLayout.setRefreshing(false);
                        }
                    }
                });
    }

}
