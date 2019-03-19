package com.lingxiaosuse.picture.tudimension.activity.tuwan;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;

import com.camera.lingxiao.common.app.BaseActivity;
import com.camera.lingxiao.common.app.ContentValue;
import com.lingxiaosuse.picture.tudimension.R;
import com.lingxiaosuse.picture.tudimension.activity.ImageLoadingActivity;
import com.lingxiaosuse.picture.tudimension.adapter.TuWanAdapter;
import com.lingxiaosuse.picture.tudimension.modle.TuWanModle;
import com.lingxiaosuse.picture.tudimension.presenter.GeneralPresenter;
import com.lingxiaosuse.picture.tudimension.utils.DialogUtil;
import com.lingxiaosuse.picture.tudimension.utils.ToastUtils;
import com.lingxiaosuse.picture.tudimension.view.GeneralView;
import com.lingxiaosuse.picture.tudimension.widget.BezierRefreshLayout;
import com.lingxiaosuse.picture.tudimension.widget.SmartSkinRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class TuWanDetailActivity extends BaseActivity implements GeneralView {
    @BindView(R.id.refresh)
    BezierRefreshLayout refresh;

    @BindView(R.id.toolbar_title)
    Toolbar toolbar;
    private GeneralPresenter presenter = new GeneralPresenter(this,this);
    private TuWanAdapter mAdapter;

    private List<TuWanModle> imageList = new ArrayList<>();
    private SmartSkinRefreshLayout refreshLayout;

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_tu_wan;
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        setToolbarBack(toolbar);
        Intent it = getIntent();
        String title = it.getStringExtra("title");
        String id = it.getStringExtra("id");
        toolbar.setTitle(title);
        presenter.getCoverData(ContentValue.limit,0);
        refreshLayout = refresh.getRefreshLayout();
        RecyclerView recyclerView = refresh.getRecyclerView();
        refreshLayout.autoRefresh();
        refreshLayout.setOnRefreshListener(refreshLayout1 -> {
            presenter.getDetailData(id);
        });
        refreshLayout.setOnLoadMoreListener(refreshLayout1 -> {
            refreshLayout.finishLoadMore();
        });

        mAdapter = new TuWanAdapter(R.layout.list_mzitu,imageList);
        mAdapter.setDuration(800);
        mAdapter.openLoadAnimation(view -> new Animator[]{
                ObjectAnimator.ofFloat(view, "scaleY", 0f, 1.05f, 1f),
                ObjectAnimator.ofFloat(view, "scaleX", 0f, 1.05f, 1f)
        });
        mAdapter.isFirstOnly(false);
        StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(2,
                StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            List<String> picUrlList = new ArrayList<>();//取出图片地址传递给下一个activity
            List<String> picIdList = new ArrayList<>();//取出图片id传递给下一个activity
            List<TuWanModle> picList = adapter.getData();
            for (int i = 0; i < picList.size(); i++) {
                picUrlList.add(picList.get(i).getUrl());
                picIdList.add(picList.get(i).getId());
            }
            Intent intent = new Intent(TuWanDetailActivity.this,
                    ImageLoadingActivity.class);
            intent.putExtra("position",position);
            intent.putExtra("itemCount",adapter.getItemCount());
            intent.putExtra("id",picList.get(position).getId());
            intent.putStringArrayListExtra("picList", (ArrayList<String>) picUrlList);
            intent.putStringArrayListExtra("picIdList", (ArrayList<String>) picIdList);
            Bundle bundle = ActivityOptionsCompat.makeScaleUpAnimation(view,
                    view.getWidth() / 2, view.getHeight() / 2, 0, 0).toBundle();
            startActivity(intent,bundle);
        });
    }

    @Override
    public void onGetCoverData(List<TuWanModle> modles) {

    }

    @Override
    public void onGetDetailData(List<TuWanModle> modles) {
        for (int i = 0; i < modles.size(); i++) {
            mAdapter.addData(modles.get(i));
        }
        mAdapter.loadMoreEnd();
        refreshLayout.finishRefresh();
        refreshLayout.finishLoadMore();
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
