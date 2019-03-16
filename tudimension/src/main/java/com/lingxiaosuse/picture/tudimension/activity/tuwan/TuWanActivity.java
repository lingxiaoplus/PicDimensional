package com.lingxiaosuse.picture.tudimension.activity.tuwan;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.camera.lingxiao.common.app.BaseActivity;
import com.camera.lingxiao.common.app.ContentValue;
import com.camera.lingxiao.common.exception.ApiException;
import com.camera.lingxiao.common.http.RxJavaHelper;
import com.camera.lingxiao.common.http.retrofit.HttpRequest;
import com.camera.lingxiao.common.observer.HttpRxObserver;
import com.lingxiaosuse.picture.tudimension.R;
import com.lingxiaosuse.picture.tudimension.activity.AboutActivity;
import com.lingxiaosuse.picture.tudimension.adapter.TuWanAdapter;
import com.lingxiaosuse.picture.tudimension.modle.TuWanModle;
import com.lingxiaosuse.picture.tudimension.presenter.GeneralPresenter;
import com.lingxiaosuse.picture.tudimension.utils.DialogUtil;
import com.lingxiaosuse.picture.tudimension.view.GeneralView;
import com.lingxiaosuse.picture.tudimension.widget.BezierRefreshLayout;
import com.lingxiaosuse.picture.tudimension.widget.SmartSkinRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.disposables.Disposable;

public class TuWanActivity extends BaseActivity implements GeneralView {
    @BindView(R.id.refresh)
    BezierRefreshLayout refresh;

    @BindView(R.id.toolbar_title)
    Toolbar toolbar;
    private GeneralPresenter presenter = new GeneralPresenter(this,this);
    private TuWanAdapter mAdapter;

    private List<TuWanModle.ImageData> imageList = new ArrayList<>();

    private List<TuWanModle.ImageData> allImageList = new ArrayList<>();
    private int start = 0;
    private int temp = 30;
    private int end = 30;
    private SmartSkinRefreshLayout refreshLayout;

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_tu_wan;
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        setToolbarBack(toolbar);
        toolbar.setTitle("兔玩君");

        DialogUtil.getInstence().showSingleDia("请注意",
                "该资源为兔子君网站付费资源，点击即可跳转到浏览器下载图标包，需要的请尽快下载，不然失效了哭唧唧",
                TuWanActivity.this);
        presenter.getData(ContentValue.TUWAN);
        refreshLayout = refresh.getRefreshLayout();
        RecyclerView recyclerView = refresh.getRecyclerView();
        refreshLayout.autoRefresh();
        refreshLayout.setOnRefreshListener(refreshLayout1 -> {
            presenter.getData(ContentValue.TUWAN);
        });
        refreshLayout.setOnLoadMoreListener(refreshLayout1 -> {
            start = end + 1;
            end += temp;
            if (end > allImageList.size()){
                mAdapter.loadMoreEnd();
                return;
            }
            mAdapter.addData(allImageList.subList(start,end));
            refreshLayout1.finishLoadMore();
        });

        mAdapter = new TuWanAdapter(R.layout.list_mzitu,imageList);
        mAdapter.setDuration(800);
        mAdapter.openLoadAnimation(view -> new Animator[]{
                ObjectAnimator.ofFloat(view, "scaleY", 0f, 1.05f, 1f),
                ObjectAnimator.ofFloat(view, "scaleX", 0f, 1.05f, 1f)
        });
        StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(2,
                StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            TuWanModle.ImageData data = (TuWanModle.ImageData) adapter.getData().get(position);
            goToInternet(TuWanActivity.this, data.getDownload());
        });
    }

    @Override
    public void onGetData(TuWanModle modle) {
        imageList.clear();
        allImageList = modle.getRes();
        if (allImageList.size() > end){
            mAdapter.addData(allImageList.subList(start,end));
        }
        refreshLayout.finishRefresh();
    }

    @Override
    public void showDialog() {

    }

    @Override
    public void diamissDialog() {

    }

    @Override
    public void showToast(String text) {

    }
}
