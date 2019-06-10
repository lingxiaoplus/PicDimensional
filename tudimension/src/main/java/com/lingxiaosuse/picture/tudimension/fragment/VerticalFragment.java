package com.lingxiaosuse.picture.tudimension.fragment;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.camera.lingxiao.common.app.BaseFragment;
import com.camera.lingxiao.common.app.ContentValue;
import com.lingxiaosuse.picture.tudimension.R;
import com.lingxiaosuse.picture.tudimension.activity.ImageLoadingActivity;
import com.lingxiaosuse.picture.tudimension.adapter.VerticalAdapter;
import com.lingxiaosuse.picture.tudimension.modle.VerticalModle;
import com.lingxiaosuse.picture.tudimension.presenter.VerticalPresenter;
import com.lingxiaosuse.picture.tudimension.utils.ToastUtils;
import com.lingxiaosuse.picture.tudimension.utils.UIUtils;
import com.lingxiaosuse.picture.tudimension.view.VerticalView;
import com.lingxiaosuse.picture.tudimension.widget.SmartSkinRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by lingxiao on 17-11-10.
 */

public class VerticalFragment extends BaseFragment implements VerticalView{

    private VerticalAdapter mAdapter;
    private List<VerticalModle.VerticalBean> mPicList = new ArrayList<>();
    private int skip = 0;


    private StaggeredGridLayoutManager manager;
    private ArrayList<String> picIdList = new ArrayList<>();
    private ArrayList<String> picUrlList = new ArrayList<>();
    private VerticalPresenter mPresenter;

    @BindView(R.id.recycle_vertical_item)
    RecyclerView mRecyclerView;
    @BindView(R.id.swip_vertical_item)
    SmartSkinRefreshLayout refreshLayout;
    @BindView(R.id.fab_vertical_fragment)
    FloatingActionButton floatingActionButton;
    private String order;

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_vertical_pager;
    }

    @Override
    protected void initWidget(View root) {
        super.initWidget(root);
        floatingBtnToogle(mRecyclerView,floatingActionButton);
        manager = new StaggeredGridLayoutManager(3,
                StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(manager);


        mAdapter = new VerticalAdapter(R.layout.list_vertical,mPicList);
        mRecyclerView.setAdapter(mAdapter);
        refreshLayout.setOnRefreshListener(refreshLayout -> {
            mPicList.clear();
            mPresenter.getVerticalData(ContentValue.limit,0,order);
        });
        refreshLayout.setOnLoadMoreListener((refreshLayout)->{
            skip+=30;
            mPresenter.getVerticalData(ContentValue.limit,skip,order);
        });

        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            Intent intent = new Intent(UIUtils.getContext(),
                    ImageLoadingActivity.class);
            intent.putExtra("position",position);
            intent.putExtra("itemCount",mAdapter.getItemCount());
            intent.putExtra("id",mPicList.get(position).getId());
            intent.putExtra("isVertical",true);
            intent.putStringArrayListExtra("picList",picUrlList);
            intent.putStringArrayListExtra("picIdList",picIdList);
            Bundle bundle = ActivityOptionsCompat.makeScaleUpAnimation(view,
                    view.getWidth() / 2, view.getHeight() / 2, 0, 0).toBundle();
            startActivity(intent,bundle);
        });
        mAdapter.setDuration(800);
        mAdapter.openLoadAnimation(view -> new Animator[]{
                ObjectAnimator.ofFloat(view, "scaleY", 0f, 1.05f, 1f),
                ObjectAnimator.ofFloat(view, "scaleX", 0f, 1.05f, 1f)
        });
        mAdapter.isFirstOnly(false);
    }

    @Override
    protected void onFirstVisiblity() {
        super.onFirstVisiblity();
        mPresenter = new VerticalPresenter(this,this);
        Bundle bundle = getArguments();
        order = bundle.getString("order");
        refreshLayout.autoRefresh();
    }

    /*@Override
    protected void initData() {
        super.initData();
        mPresenter = new VerticalPresenter(this,this);
        Bundle bundle = getArguments();
        order = bundle.getString("order");
        mPresenter.getVerticalData(30,skip,order);
    }*/

    @Override
    public void onGetVerticalResult(VerticalModle modle) {
        if (modle.getVertical().size() < 30){
            mAdapter.loadMoreEnd();
        }
        mAdapter.addData(modle.getVertical());
        picUrlList.clear();
        picIdList.clear();
        for (int i = 0; i < mPicList.size(); i++) {
            picIdList.add(mPicList.get(i).getId());
            //获取url，getimg无法获取文件名，所以用getwp
            picUrlList.add(mPicList.get(i).getWp());
        }
        refreshLayout.finishLoadMore();
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
        ToastUtils.show(text);
        refreshLayout.finishLoadMore();
        refreshLayout.finishRefresh();
    }
}
