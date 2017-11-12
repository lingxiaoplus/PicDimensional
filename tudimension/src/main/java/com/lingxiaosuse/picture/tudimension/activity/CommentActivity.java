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

import com.lingxiaosuse.picture.tudimension.R;
import com.lingxiaosuse.picture.tudimension.adapter.BaseRecycleAdapter;
import com.lingxiaosuse.picture.tudimension.adapter.CommentRecycleAdapter;
import com.lingxiaosuse.picture.tudimension.modle.CommentModle;
import com.lingxiaosuse.picture.tudimension.retrofit.CommentInterface;
import com.lingxiaosuse.picture.tudimension.retrofit.RetrofitHelper;
import com.lingxiaosuse.picture.tudimension.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommentActivity extends BaseActivity {
    @BindView(R.id.ll_comment)
    LinearLayout linearLayout;
    @BindView(R.id.toolbar_title)
    Toolbar toolbarTitle;
    @BindView(R.id.recycle_comment)
    RecyclerView recycleComment;
    @BindView(R.id.swip_comment)
    SwipeRefreshLayout swipComment;
    private String id;
    private List<CommentModle.ResBean.CommentBean> commentBeanList;
    private List<CommentModle.ResBean.CommentBean> mBeanList = new ArrayList<>();
    private int skip = 0;
    private CommentRecycleAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        ButterKnife.bind(this);
        id = getIntent().getStringExtra("id");
        initView();
        getDataFromeServer(skip);
    }

    private void initView() {
        toolbarTitle.setTitle("评论板");
        setSupportActionBar(toolbarTitle);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        swipComment.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getDataFromeServer(0);
            }
        });

        mAdapter = new CommentRecycleAdapter(mBeanList,0,1);
        recycleComment.setAdapter(mAdapter);
        LinearLayoutManager mLayoutManager =
                new LinearLayoutManager(getApplicationContext());
        recycleComment.setLayoutManager(mLayoutManager);
        mAdapter.notifyDataSetChanged();

        mAdapter.setRefreshListener(new BaseRecycleAdapter.onLoadmoreListener() {
            @Override
            public void onLoadMore() {
                skip+=30;
                getDataFromeServer(skip);
            }
        });
    }

    @OnClick(R.id.ll_comment)
    public void setRefreshing(){
        if (swipComment.isRefreshing()){
            return;
        }
        swipComment.setRefreshing(true);
        getDataFromeServer(0);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                //动画
                overridePendingTransition(R.anim.slide_in_top,R.anim.slide_out_top);
                break;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //动画
        overridePendingTransition(R.anim.slide_in_top,R.anim.slide_out_top);
    }

    private void getDataFromeServer(int skip){
        RetrofitHelper.getInstance(this)
                .getInterface(CommentInterface.class)
                .commentModle(id,30,skip)
                .enqueue(new Callback<CommentModle>() {
                    @Override
                    public void onResponse(Call<CommentModle> call, Response<CommentModle> response) {
                        commentBeanList = response.body().getRes().getComment();
                        mBeanList.addAll(commentBeanList);
                        if (mBeanList.size()>0){
                            linearLayout.setVisibility(View.GONE);
                            recycleComment.setVisibility(View.VISIBLE);
                            mAdapter.notifyDataSetChanged();
                        }else {
                            linearLayout.setVisibility(View.VISIBLE);
                            recycleComment.setVisibility(View.GONE);
                        }
                        if (commentBeanList.size()<30){
                            mAdapter.isFinish(true);
                        }
                        if (swipComment.isRefreshing()){
                            swipComment.setRefreshing(false);
                        }
                    }

                    @Override
                    public void onFailure(Call<CommentModle> call, Throwable t) {
                        if (swipComment.isRefreshing()){
                            swipComment.setRefreshing(false);
                            ToastUtils.show("刷新完成");
                        }
                    }
                });
    }
}
