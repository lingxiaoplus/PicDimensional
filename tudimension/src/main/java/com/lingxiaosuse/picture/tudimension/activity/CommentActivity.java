package com.lingxiaosuse.picture.tudimension.activity;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.camera.lingxiao.common.app.BaseActivity;
import com.camera.lingxiao.common.app.ContentValue;
import com.lingxiaosuse.picture.tudimension.R;
import com.lingxiaosuse.picture.tudimension.adapter.BaseRecycleAdapter;
import com.lingxiaosuse.picture.tudimension.adapter.CommentRecycleAdapter;
import com.lingxiaosuse.picture.tudimension.modle.CommentModle;
import com.lingxiaosuse.picture.tudimension.presenter.CommentPresenter;
import com.lingxiaosuse.picture.tudimension.utils.ToastUtils;
import com.lingxiaosuse.picture.tudimension.view.CommentView;
import com.lingxiaosuse.picture.tudimension.widget.BezierRefreshLayout;
import com.lingxiaosuse.picture.tudimension.widget.SmartSkinRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommentActivity extends BaseActivity implements CommentView{
    @BindView(R.id.toolbar_title)
    Toolbar toolbarTitle;
    @BindView(R.id.swip_comment)
    BezierRefreshLayout bezierRefreshLayout;

    RecyclerView mRecyclerView;
    SmartSkinRefreshLayout mRefreshLayout;

    private String id;

    private List<CommentModle.CommentBean> mBeanList = new ArrayList<>();
    private int skip = 0;
    private CommentRecycleAdapter mAdapter;
    private CommentPresenter mPresenter = new CommentPresenter(this,this);
    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_comment;
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        setToolbarBack(toolbarTitle);
        toolbarTitle.setTitle("评论板");
        mRefreshLayout = bezierRefreshLayout.getRefreshLayout();
        mRecyclerView = bezierRefreshLayout.getRecyclerView();
        mRefreshLayout.autoRefresh();
        mRefreshLayout.setOnRefreshListener(refreshLayout -> {
            mBeanList.clear();
            mPresenter.getCommentResult(id, ContentValue.limit,0);
        });
        mRefreshLayout.setOnLoadMoreListener(refreshLayout -> {
            skip+=30;
            mPresenter.getCommentResult(id, ContentValue.limit,skip);
        });

        mAdapter = new CommentRecycleAdapter(R.layout.list_comment,mBeanList);
        View view = View.inflate(this,R.layout.empty_data_layout,null);
        mAdapter.setEmptyView(view);
        mAdapter.setDuration(800);
        mAdapter.openLoadAnimation();
        mRecyclerView.setAdapter(mAdapter);
        LinearLayoutManager mLayoutManager =
                new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        ImageView imageView = view.findViewById(R.id.refresh_data);
        imageView.setOnClickListener(v -> {
            mRefreshLayout.autoRefresh();
            mPresenter.getCommentResult(id, ContentValue.limit,0);
        });
    }

    @Override
    protected void initData() {
        super.initData();
        id = getIntent().getStringExtra("id");
        mPresenter.getCommentResult(id, ContentValue.limit,0);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                //动画
                //overridePendingTransition(R.anim.slide_in_top,R.anim.slide_out_top);
                break;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //动画
        //overridePendingTransition(R.anim.slide_in_top,R.anim.slide_out_top);
    }

    @Override
    public void onGetCommentResult(CommentModle modle) {
        mAdapter.addData(modle.getComment());
        mRefreshLayout.finishRefresh();
        mRefreshLayout.finishLoadMore();
        if (modle.getComment().size()<30){
            mAdapter.loadMoreEnd();
            mRefreshLayout.finishLoadMoreWithNoMoreData();
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
        mRefreshLayout.finishRefresh();
        mRefreshLayout.finishLoadMore();
    }
}
