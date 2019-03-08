package com.lingxiaosuse.picture.tudimension.fragment.cosplay;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.camera.lingxiao.common.app.BaseFragment;
import com.camera.lingxiao.common.utills.LogUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lingxiaosuse.picture.tudimension.R;
import com.lingxiaosuse.picture.tudimension.activity.cosplay.CosplayDetailActivity;
import com.lingxiaosuse.picture.tudimension.adapter.BaseRecycleAdapter;
import com.lingxiaosuse.picture.tudimension.adapter.CosplayAdapter;
import com.lingxiaosuse.picture.tudimension.modle.CosplayDetailModel;
import com.lingxiaosuse.picture.tudimension.modle.CosplayModel;
import com.lingxiaosuse.picture.tudimension.presenter.CosplayPresenter;
import com.lingxiaosuse.picture.tudimension.utils.ToastUtils;
import com.lingxiaosuse.picture.tudimension.view.CosplayView;
import com.lingxiaosuse.picture.tudimension.widget.SmartSkinRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class CosplayFragment extends BaseFragment implements CosplayView{
    @BindView(R.id.rv_category)
    RecyclerView recyclerView;
    @BindView(R.id.swip_category)
    SmartSkinRefreshLayout refreshLayout;
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
        refreshLayout.autoRefresh();
        refreshLayout.setOnRefreshListener((refreshLayout)-> {
            mCosplayerList.clear();
            mCosplayPresenter.getCosplayer(16,order);
        });
        mAdapter = new CosplayAdapter(R.layout.cosplay_item,mCosplayerList);
        recyclerView.setAdapter(mAdapter);
        mLayoutManager = new GridLayoutManager(getActivity(),2,
                LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(mLayoutManager);

        refreshLayout.setOnLoadMoreListener(refreshLayout -> {
            psize+=16;
            mCosplayPresenter.getCosplayer(psize,order);
        });

        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            Intent intent = new Intent(getActivity(), CosplayDetailActivity.class);
            intent.putExtra("shareid",mCosplayerList.get(position).getId());
            intent.putExtra("title",mCosplayerList.get(position).getTitle());
            startActivity(intent);
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
            mAdapter.loadMoreEnd();
        }
        mAdapter.addData(model.getData());
        refreshLayout.finishRefresh();
        refreshLayout.finishLoadMore();
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
        refreshLayout.finishRefresh();
        refreshLayout.finishLoadMore();
    }
}
