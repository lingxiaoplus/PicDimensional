package com.lingxiaosuse.picture.tudimension.activity.cosplay;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.camera.lingxiao.common.app.BaseActivity;
import com.camera.lingxiao.common.widget.BaseHolder;
import com.camera.lingxiao.common.widget.BaseRecyclerViewAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lingxiaosuse.picture.tudimension.R;
import com.lingxiaosuse.picture.tudimension.activity.ImageLoadingActivity;
import com.lingxiaosuse.picture.tudimension.adapter.CosplayDetailAdapter;
import com.lingxiaosuse.picture.tudimension.adapter.MzituRecyclerAdapter;
import com.lingxiaosuse.picture.tudimension.modle.CosplayDetailModel;
import com.lingxiaosuse.picture.tudimension.modle.CosplayModel;
import com.lingxiaosuse.picture.tudimension.presenter.CosplayDetailPresenter;
import com.lingxiaosuse.picture.tudimension.utils.ToastUtils;
import com.lingxiaosuse.picture.tudimension.utils.UIUtils;
import com.lingxiaosuse.picture.tudimension.view.CosplayView;
import com.lingxiaosuse.picture.tudimension.widget.SmartSkinRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CosplayDetailActivity extends BaseActivity implements CosplayView {

    @BindView(R.id.tv_mzitu_detail_title)
    TextView tvDetailTitle;
    @BindView(R.id.toolbar_mzitu_detail)
    Toolbar toolbarDetail;
    @BindView(R.id.rv_mzitu_detail)
    RecyclerView rvDetail;
    @BindView(R.id.swip_mzitu_detail)
    SmartSkinRefreshLayout refreshLayout;

    private int mShareId;
    private String mTitle;
    private CosplayDetailPresenter mPresenter;
    private List<CosplayDetailModel.DataBean.ShareBean.PhotoListsBean>
            photoList = new ArrayList<>();
    private List<String>
            mImageList = new ArrayList<>();
    private CosplayDetailAdapter mAdapter;

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_mzitu_detail;
    }

    @Override
    protected boolean initArgs(Bundle bundle) {
        mShareId = bundle.getInt("shareid");
        mTitle = bundle.getString("title");
        return super.initArgs(bundle);
    }


    @Override
    protected void initWidget() {
        super.initWidget();
        setToolbarBack(toolbarDetail);
        toolbarDetail.setTitle(mTitle);
        refreshLayout.autoRefresh();
        refreshLayout.setOnRefreshListener((refreshLayout)-> {
            photoList.clear();
            mPresenter.getCosplayDetailResult(mShareId);
        });

        mAdapter = new CosplayDetailAdapter(R.layout.list_mzitu,photoList);
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            Intent intent = new Intent(UIUtils.getContext(),
                    ImageLoadingActivity.class);
            intent.putExtra("position",position);
            intent.putExtra("itemCount",mAdapter.getItemCount());
            intent.putExtra("id",photoList.get(position).getId()+"");
            intent.putStringArrayListExtra("picList", (ArrayList<String>) mImageList);
            intent.putExtra("isHot",true); // 判断是否为最新界面传递过来的
            startActivity(intent);
        });
        StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(2,
                StaggeredGridLayoutManager.VERTICAL);
        rvDetail.setHasFixedSize(true);
        rvDetail.setLayoutManager(manager);
        rvDetail.setAdapter(mAdapter);
    }

    @Override
    protected void initData() {
        super.initData();
        mPresenter = new CosplayDetailPresenter(this, this);
        mPresenter.getCosplayDetailResult(mShareId);
    }

    @Override
    public void onGetCosplayer(CosplayModel model) {

    }

    @Override
    public void onGetCosplayDetail(CosplayDetailModel model) {
        mAdapter.addData(model.getData().getShare().getPhotoLists());
        refreshLayout.finishRefresh();
        refreshLayout.finishLoadMore();
        for (int i = 0; i < model.getData().getShare().getPhotoLists().size(); i++) {
            mImageList.add(model.getData().getShare().getPhotoLists().get(i).getPicPath());
        }
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
