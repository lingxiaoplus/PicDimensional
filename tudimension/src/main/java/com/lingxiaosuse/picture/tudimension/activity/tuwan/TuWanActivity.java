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
import com.facebook.drawee.backends.pipeline.Fresco;
import com.lingxiaosuse.picture.tudimension.R;
import com.lingxiaosuse.picture.tudimension.activity.AboutActivity;
import com.lingxiaosuse.picture.tudimension.adapter.TuWanAdapter;
import com.lingxiaosuse.picture.tudimension.modle.TuWanModle;
import com.lingxiaosuse.picture.tudimension.presenter.GeneralPresenter;
import com.lingxiaosuse.picture.tudimension.utils.DialogUtil;
import com.lingxiaosuse.picture.tudimension.utils.FrescoHelper;
import com.lingxiaosuse.picture.tudimension.utils.ToastUtils;
import com.lingxiaosuse.picture.tudimension.utils.UIUtils;
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

    private List<TuWanModle> imageList = new ArrayList<>();
    private int skip = 0;
    private SmartSkinRefreshLayout refreshLayout;

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_tu_wan;
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        Fresco.shutDown();
        Fresco.initialize(this);
        setToolbarBack(toolbar);
        String tableName = getIntent().getStringExtra("table_name");
        final String tabDetailName;
        if (tableName.contains("tuwan")){
            toolbar.setTitle("兔玩君");
            DialogUtil.getInstence().showSingleDia("请注意",
                    "该资源为兔子君网站付费资源，点击即可跳转到浏览器下载图标包，需要的尽快下载叭",
                    TuWanActivity.this);
            tabDetailName = "tuwan";
        }else {
            toolbar.setTitle("乐摄网");
            tabDetailName = "leshe_detail";
        }

        refreshLayout = refresh.getRefreshLayout();
        RecyclerView recyclerView = refresh.getRecyclerView();
        refreshLayout.autoRefresh();
        refreshLayout.setOnRefreshListener(refreshLayout -> {
            skip = 0;
            presenter.getCoverData(tableName,ContentValue.limit,skip);
        });
        refreshLayout.setOnLoadMoreListener(refreshLayout -> {
            skip++;
            presenter.getCoverData(tableName,ContentValue.limit,skip);
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
            TuWanModle modle = imageList.get(position);
            Intent intent = new Intent(getApplicationContext(),TuWanDetailActivity.class);
            intent.putExtra("id",modle.getId());
            intent.putExtra("title",modle.getTitle());
            intent.putExtra("table_name",tabDetailName);
            startActivity(intent);
        });
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
    public void onGetCoverData(List<TuWanModle> modles) {
        if (modles.size() < ContentValue.limit){
            mAdapter.loadMoreEnd();
        }
        for (int i = 0; i < modles.size(); i++) {
            mAdapter.addData(modles.get(i));
        }
        refreshLayout.finishRefresh();
        refreshLayout.finishLoadMore();
    }

    @Override
    public void onGetDetailData(List<TuWanModle> modles) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Fresco.shutDown();
        FrescoHelper.initFresco(UIUtils.getContext());
    }
}
