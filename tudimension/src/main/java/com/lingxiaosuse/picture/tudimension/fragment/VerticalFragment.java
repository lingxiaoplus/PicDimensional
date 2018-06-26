package com.lingxiaosuse.picture.tudimension.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.camera.lingxiao.common.app.BaseFragment;
import com.camera.lingxiao.common.app.ContentValue;
import com.lingxiaosuse.picture.tudimension.R;
import com.lingxiaosuse.picture.tudimension.activity.ImageLoadingActivity;
import com.lingxiaosuse.picture.tudimension.activity.VerticalActivity;
import com.lingxiaosuse.picture.tudimension.adapter.BaseRecycleAdapter;
import com.lingxiaosuse.picture.tudimension.adapter.VerticalAdapter;
import com.lingxiaosuse.picture.tudimension.modle.VerticalModle;
import com.lingxiaosuse.picture.tudimension.presenter.VerticalPresenter;
import com.lingxiaosuse.picture.tudimension.retrofit.RetrofitHelper;
import com.lingxiaosuse.picture.tudimension.retrofit.VerticalInterface;
import com.lingxiaosuse.picture.tudimension.utils.ToastUtils;
import com.lingxiaosuse.picture.tudimension.utils.UIUtils;
import com.lingxiaosuse.picture.tudimension.view.VerticalView;

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
    private List<VerticalModle.VerticalBean> verticalBeanList;
    private ArrayList<String> picIdList = new ArrayList<>();
    private ArrayList<String> picUrlList = new ArrayList<>();
    private VerticalPresenter mPresenter = new VerticalPresenter(this,this);

    @BindView(R.id.recycle_vertical_item)
    RecyclerView mRecyclerView;
    @BindView(R.id.swip_vertical_item)
    SwipeRefreshLayout refreshLayout;
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
        setSwipeColor(refreshLayout);
        floatingBtnToogle(mRecyclerView,floatingActionButton);
        manager = new StaggeredGridLayoutManager(3,
                StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(manager);
        refreshLayout.setRefreshing(true);

        mAdapter = new VerticalAdapter(mPicList,0,1,false);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setRefreshListener(new BaseRecycleAdapter.onLoadmoreListener() {
            @Override
            public void onLoadMore() {
                skip+=30;
                mPresenter.getVerticalData(ContentValue.limit,skip,order);
            }
        });
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPicList.clear();
                mPresenter.getVerticalData(ContentValue.limit,0,order);
            }
        });
    }

    @Override
    protected void initData() {
        super.initData();
        Bundle bundle = getArguments();
        order = bundle.getString("order");
        mPresenter.getVerticalData(30,skip,order);
    }

    @Override
    public void onGetVerticalResult(VerticalModle modle) {
        if (modle.getVertical().size() < 30){
            mAdapter.isFinish(true);
        }
        verticalBeanList = modle.getVertical();
        mPicList.addAll(verticalBeanList);
        mAdapter.notifyDataSetChanged();
        if (refreshLayout.isRefreshing()){
            refreshLayout.setRefreshing(false);
        }
        picUrlList.clear();
        picIdList.clear();
        for (int i = 0; i < mPicList.size(); i++) {
            picIdList.add(mPicList.get(i).getId());
            //获取url，getimg无法获取文件名，所以用getwp
            picUrlList.add(mPicList.get(i).getWp());
        }

        mAdapter.setOnItemClickListener(new BaseRecycleAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View View, int position) {
                Intent intent = new Intent(UIUtils.getContext(),
                        ImageLoadingActivity.class);
                intent.putExtra("position",position);
                intent.putExtra("itemCount",mAdapter.getItemCount());
                intent.putExtra("id",mPicList.get(position).getId());
                intent.putExtra("isHot",true);
                intent.putExtra("isVertical",true);
                intent.putStringArrayListExtra("picList",picUrlList);
                intent.putStringArrayListExtra("picIdList",picIdList);
                startActivity(intent);
            }
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
}
