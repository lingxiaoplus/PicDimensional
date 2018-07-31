package com.lingxiaosuse.picture.tudimension.fragment;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.camera.lingxiao.common.app.BaseFragment;
import com.camera.lingxiao.common.app.ContentValue;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lingxiaosuse.picture.tudimension.R;
import com.lingxiaosuse.picture.tudimension.activity.ImageLoadingActivity;
import com.lingxiaosuse.picture.tudimension.adapter.BaseRecycleAdapter;
import com.lingxiaosuse.picture.tudimension.adapter.HotRecycleAdapter;
import com.lingxiaosuse.picture.tudimension.modle.HomePageModle;
import com.lingxiaosuse.picture.tudimension.modle.HotModle;
import com.lingxiaosuse.picture.tudimension.presenter.HotPresenter;
import com.lingxiaosuse.picture.tudimension.retrofit.HomePageInterface;
import com.lingxiaosuse.picture.tudimension.retrofit.HotInterface;
import com.lingxiaosuse.picture.tudimension.utils.HttpUtils;
import com.lingxiaosuse.picture.tudimension.utils.ToastUtils;
import com.lingxiaosuse.picture.tudimension.utils.UIUtils;
import com.lingxiaosuse.picture.tudimension.view.HotView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by lingxiao on 2017/8/28.
 */

public class HotFragment extends BaseFragment implements HotView{
    @BindView(R.id.swip_hot)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.rv_hot)
    RecyclerView recyclerView;
    @BindView(R.id.fab_fragment)
    FloatingActionButton fab;

    private List<HotModle.ResultsBean> previsList = new ArrayList<>();
    private HotRecycleAdapter mAdapter;
    private GridLayoutManager mLayoutManager;
    private int index = 1;
    private ArrayList<String> picUrlList = new ArrayList<>();//取出图片地址传递给下一个activity
    private HotPresenter mPresenter;

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_hot;
    }

    @Override
    protected void initWidget(View root) {
        super.initWidget(root);
        setSwipeColor(refreshLayout);
        refreshLayout.setRefreshing(true);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                previsList.clear();
                picUrlList.clear();
                mPresenter.getHotResult(ContentValue.limit,1);
            }
        });
        mLayoutManager = new GridLayoutManager(getContext(),2,
                LinearLayoutManager.VERTICAL,false);
        mAdapter = new HotRecycleAdapter(previsList,0,1);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(mLayoutManager);
        mAdapter.setRefreshListener(new BaseRecycleAdapter.onLoadmoreListener() {
            @Override
            public void onLoadMore() {
                index++;
                mPresenter.getHotResult(ContentValue.limit,index);
            }
        });
        mAdapter.setOnItemClickListener(new HotRecycleAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View View, int position) {
                if (null == picUrlList){
                    return;
                }
                picUrlList.clear();
                for (int i = 0; i < previsList.size(); i++) {
                    if (picUrlList != null){
                        picUrlList.add(previsList.get(i).getUrl());
                    }
                }
                try {
                    Intent intent = new Intent(UIUtils.getContext(),
                            ImageLoadingActivity.class);
                    intent.putExtra("position",position);
                    intent.putExtra("itemCount",mAdapter.getItemCount());
                    intent.putExtra("id",previsList.get(position).get_id());
                    intent.putStringArrayListExtra("picList",picUrlList);
                    intent.putExtra("isHot",true); // 判断是否为最新界面传递过来的
                    startActivity(intent);
                }catch (IndexOutOfBoundsException e){
                    e.printStackTrace();
                }

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        });
        floatingBtnToogle(recyclerView,fab);
    }

    @Override
    protected void initData() {
        super.initData();
        mPresenter = new HotPresenter(this,this);
        mPresenter.getHotResult(ContentValue.limit,index);
    }


    @Override
    public void onGetHotResult(HotModle modle) {
        List<HotModle.ResultsBean> resultList = modle.getResults();
        if (resultList.size() < ContentValue.limit){
            mAdapter.isFinish(true);
        }
        previsList.addAll(resultList);
        mAdapter.notifyDataSetChanged();
        refreshLayout.setRefreshing(false);
    }

    @Override
    public void showDialog() {

    }

    @Override
    public void diamissDialog() {

    }

    @Override
    public void showToast(String text) {
        ToastUtils.show(text);
        refreshLayout.setRefreshing(false);
    }
}
