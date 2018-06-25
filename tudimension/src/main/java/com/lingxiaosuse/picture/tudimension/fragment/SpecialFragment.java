package com.lingxiaosuse.picture.tudimension.fragment;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.camera.lingxiao.common.app.BaseFragment;
import com.lingxiaosuse.picture.tudimension.R;
import com.lingxiaosuse.picture.tudimension.activity.BannerDetailActivity;
import com.lingxiaosuse.picture.tudimension.adapter.BaseRecycleAdapter;
import com.lingxiaosuse.picture.tudimension.adapter.SpecialRecycleAdapter;
import com.lingxiaosuse.picture.tudimension.modle.SpecialModle;
import com.lingxiaosuse.picture.tudimension.retrofit.RetrofitHelper;
import com.lingxiaosuse.picture.tudimension.retrofit.SpecialInterface;
import com.lingxiaosuse.picture.tudimension.utils.UIUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by lingxiao on 2017/8/28.
 */

public class SpecialFragment extends BaseFragment{
    @BindView(R.id.swip_special)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.rv_special)
    RecyclerView recyclerView;
    @BindView(R.id.fab_fragment)
    FloatingActionButton fab;

    private List<SpecialModle.ResBean.AlbumBean> albumBeanList = new ArrayList<>();
    private List<SpecialModle.ResBean.AlbumBean> moreList = new ArrayList<>();;
    private SpecialRecycleAdapter mAdapter;
    private LinearLayoutManager manager;
    private int skip = 0;

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_special;
    }

    @Override
    protected void initWidget(View root) {
        super.initWidget(root);
        setSwipeColor(swipeRefreshLayout);
        swipeRefreshLayout.setRefreshing(true);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData(30);
            }
        });
        floatingBtnToogle(recyclerView,fab);
    }

    @Override
    protected void initData() {
        getData(30);
    }

    private void getData(final int limit){
        RetrofitHelper
                .getInstance(UIUtils.getContext())
                .getInterface(SpecialInterface.class)
                .specialModle(limit,skip,false)
                .enqueue(new Callback<SpecialModle>() {
            @Override
            public void onResponse(Call<SpecialModle> call, Response<SpecialModle> response) {
                albumBeanList = response.body().getRes().getAlbum();
                mAdapter = new SpecialRecycleAdapter(albumBeanList,0,1);
                manager = new LinearLayoutManager(getContext());
                recyclerView.setLayoutManager(manager);
                recyclerView.setAdapter(mAdapter);
                swipeRefreshLayout.setRefreshing(false);
                mAdapter.setRefreshListener(new SpecialRecycleAdapter.onLoadmoreListener() {
                    @Override
                    public void onLoadMore() {
                        getMoreData(30);
                    }
                });
                mAdapter.setOnItemClickListener(new BaseRecycleAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View View, int position) {
                        Intent intent = new Intent(UIUtils.getContext(),
                                BannerDetailActivity.class);
                        intent.putExtra("url",albumBeanList.get(position).getLcover());
                        intent.putExtra("desc",albumBeanList.get(position).getName());
                        intent.putExtra("id",albumBeanList.get(position).getId());
                        intent.putExtra("title",albumBeanList.get(position).getName());
                        intent.putExtra("type","album");  //说明类型位album
                        startActivity(intent);
                    }
                });

            }
            @Override
            public void onFailure(Call<SpecialModle> call, Throwable t) {

            }
        });
    }

    private void getMoreData(final int limit){
        skip+=10;
        RetrofitHelper
                .getInstance(UIUtils.getContext())
                .getInterface(SpecialInterface.class)
                .specialModle(limit,skip,false)
                .enqueue(new Callback<SpecialModle>() {
                    @Override
                    public void onResponse(Call<SpecialModle> call, Response<SpecialModle> response) {
                        moreList = response.body().getRes().getAlbum();
                        if (skip<300){
                            albumBeanList.addAll(moreList);
                        }else {
                            skip = 0;
                            mAdapter.isFinish(true);
                        }
                        mAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onFailure(Call<SpecialModle> call, Throwable t) {

                    }
                });
    }

    @OnClick(R.id.fab_fragment)
    public void setTopView(){
        recyclerView.smoothScrollToPosition(0);
    }
}
