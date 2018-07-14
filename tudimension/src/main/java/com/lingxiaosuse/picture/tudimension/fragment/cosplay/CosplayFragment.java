package com.lingxiaosuse.picture.tudimension.fragment.cosplay;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.camera.lingxiao.common.app.BaseFragment;
import com.camera.lingxiao.common.utills.LogUtils;
import com.lingxiaosuse.picture.tudimension.R;
import com.lingxiaosuse.picture.tudimension.activity.cosplay.CosplayDetailActivity;
import com.lingxiaosuse.picture.tudimension.adapter.BaseRecycleAdapter;
import com.lingxiaosuse.picture.tudimension.adapter.CosplayAdapter;
import com.lingxiaosuse.picture.tudimension.modle.CosplayDetailModel;
import com.lingxiaosuse.picture.tudimension.modle.CosplayModel;
import com.lingxiaosuse.picture.tudimension.presenter.CosplayPresenter;
import com.lingxiaosuse.picture.tudimension.utils.ToastUtils;
import com.lingxiaosuse.picture.tudimension.view.CosplayView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class CosplayFragment extends BaseFragment implements CosplayView{
    @BindView(R.id.rv_category)
    RecyclerView recyclerView;
    @BindView(R.id.swip_category)
    SwipeRefreshLayout refreshLayout;
    private CosplayPresenter mCosplayPresenter;
    private List<CosplayModel.DataBean> mCosplayerList = new ArrayList<>();
    private CosplayAdapter mAdapter;
    private GridLayoutManager mLayoutManager;
    private int order = 0;
    private int psize = 16;

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_category;
    }

    @Override
    protected void initArgs(Bundle bundle) {
        super.initArgs(bundle);
        order = bundle.getInt("order");
    }

    @Override
    protected void initWidget(View root) {
        super.initWidget(root);
        setSwipeColor(refreshLayout);
        refreshLayout.setRefreshing(true);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mCosplayerList.clear();
                mCosplayPresenter.getCosplayer(16,order);
            }
        });
        mAdapter = new CosplayAdapter(mCosplayerList,0,1);
        recyclerView.setAdapter(mAdapter);
        mLayoutManager = new GridLayoutManager(getActivity(),2,
                LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(mLayoutManager);
        mAdapter.setOnItemClickListener(new BaseRecycleAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View View, int position) {
                //  /share/GetById   shareid=10218
                Intent intent = new Intent(getActivity(), CosplayDetailActivity.class);
                intent.putExtra("shareid",mCosplayerList.get(position).getId());
                intent.putExtra("title",mCosplayerList.get(position).getTitle());
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        });
        mAdapter.setRefreshListener(new BaseRecycleAdapter.onLoadmoreListener() {
            @Override
            public void onLoadMore() {
                psize+=16;
                mCosplayPresenter.getCosplayer(psize,order);
            }
        });
    }

    @Override
    protected void initData() {
        super.initData();
        mCosplayPresenter = new CosplayPresenter(this,this);
        mCosplayPresenter.getCosplayer(psize,order);
    }

    @Override
    public void onGetCosplayer(CosplayModel model) {
        if (model.getData().size() <16){
            mAdapter.isFinish(true);
        }
        mCosplayerList.addAll(model.getData());
        mAdapter.notifyDataSetChanged();
        refreshLayout.setRefreshing(false);
        LogUtils.d("cosplay获取到的数据： "+model.toString());
    }

    @Override
    public void onGetCosplayDetail(CosplayDetailModel model) {

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
    }
}
