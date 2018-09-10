package com.lingxiaosuse.picture.tudimension.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
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

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommentActivity extends BaseActivity implements CommentView{
    @BindView(R.id.ll_comment)
    LinearLayout linearLayout;
    @BindView(R.id.toolbar_title)
    Toolbar toolbarTitle;
    @BindView(R.id.recycle_comment)
    RecyclerView recycleComment;
    @BindView(R.id.swip_comment)
    SwipeRefreshLayout swipComment;
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
        setSwipeColor(swipComment);
        swipComment.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mBeanList.clear();
                mPresenter.getCommentResult(id, ContentValue.limit,0);
            }
        });
        mAdapter = new CommentRecycleAdapter(mBeanList,0,1);
        recycleComment.setAdapter(mAdapter);
        LinearLayoutManager mLayoutManager =
                new LinearLayoutManager(getApplicationContext());
        recycleComment.setLayoutManager(mLayoutManager);
        mAdapter.setRefreshListener(new BaseRecycleAdapter.onLoadmoreListener() {
            @Override
            public void onLoadMore() {
                skip+=30;
                mPresenter.getCommentResult(id, ContentValue.limit,skip);
            }
        });
    }

    @Override
    protected void initData() {
        super.initData();
        id = getIntent().getStringExtra("id");
        mPresenter.getCommentResult(id, ContentValue.limit,0);
    }

    @OnClick(R.id.ll_comment)
    public void setRefreshing(){
        if (swipComment.isRefreshing()){
            return;
        }
        swipComment.setRefreshing(true);
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
        List<CommentModle.CommentBean> commentBeanList = modle.getComment();
        if (commentBeanList.size()<30){
            mAdapter.isFinish(true);
        }
        mBeanList.addAll(commentBeanList);
        if (mBeanList.size()>0){
            linearLayout.setVisibility(View.GONE);
            recycleComment.setVisibility(View.VISIBLE);
            mAdapter.notifyDataSetChanged();
        }else {
            linearLayout.setVisibility(View.VISIBLE);
            recycleComment.setVisibility(View.GONE);
        }
        mAdapter.notifyDataSetChanged();
        swipComment.setRefreshing(false);
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
