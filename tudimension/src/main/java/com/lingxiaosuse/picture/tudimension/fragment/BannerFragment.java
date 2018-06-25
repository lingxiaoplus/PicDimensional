package com.lingxiaosuse.picture.tudimension.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.camera.lingxiao.common.app.BaseFragment;
import com.camera.lingxiao.common.app.ContentValue;
import com.lingxiaosuse.picture.tudimension.R;
import com.lingxiaosuse.picture.tudimension.activity.ImageLoadingActivity;
import com.lingxiaosuse.picture.tudimension.adapter.BannerRecycleAdapter;
import com.lingxiaosuse.picture.tudimension.modle.BannerModle;
import com.lingxiaosuse.picture.tudimension.modle.HomePageModle;
import com.lingxiaosuse.picture.tudimension.presenter.HomePresenter;
import com.lingxiaosuse.picture.tudimension.transation.HomeTrans;
import com.lingxiaosuse.picture.tudimension.utils.ToastUtils;
import com.lingxiaosuse.picture.tudimension.utils.UIUtils;
import com.lingxiaosuse.picture.tudimension.view.HomeView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class BannerFragment extends BaseFragment implements HomeView{
    @BindView(R.id.recycle_vertical_item)
    RecyclerView recycleView;
    @BindView(R.id.swip_vertical_item)
    SwipeRefreshLayout swipView;
    @BindView(R.id.fab_vertical_fragment)
    FloatingActionButton fabView;

    private List<BannerModle.WallpaperBean> picList = new ArrayList<>();
    private ArrayList<String> picUrlList = new ArrayList<>();
    private ArrayList<String> IdList = new ArrayList<>();
    private BannerRecycleAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private int skip = 0;
    private HomePresenter mHomePresenter = new HomePresenter(this,this);
    private String id;
    private String type;
    private String order;

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_vertical_pager;
    }

    @Override
    protected void initWidget(View root) {
        super.initWidget(root);
        setSwipeColor(swipView);
        floatingBtnToogle(recycleView,fabView);
        mLayoutManager = new StaggeredGridLayoutManager(3,
                StaggeredGridLayoutManager.VERTICAL);
        recycleView.setLayoutManager(mLayoutManager);
        swipView.setRefreshing(true);
        mAdapter = new BannerRecycleAdapter(picList,UIUtils.getContext());
        recycleView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new BannerRecycleAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, Uri uri) {
                Intent intent = new Intent(UIUtils.getContext(),
                        ImageLoadingActivity.class);
                intent.putExtra("position", position);
                intent.putExtra("itemCount", mAdapter.getItemCount());
                intent.putExtra("id", picList.get(position).getId());
                intent.putStringArrayListExtra("picList", picUrlList);
                intent.putStringArrayListExtra("picIdList", IdList);
                startActivity(intent);
            }
        });
        mAdapter.setRefreshListener(new BannerRecycleAdapter.onLoadmoreListener() {
            @Override
            public void onLoadMore(int position) {
                skip += 30;
                mHomePresenter.getBannerDetailData(id, ContentValue.limit,skip,type,order);
            }
        });
        swipView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mHomePresenter.getBannerDetailData(id, ContentValue.limit,0,type,order);
            }
        });
    }

    @Override
    protected void initArgs(Bundle bundle) {
        super.initArgs(bundle);
        id = bundle.getString("id");
        type = bundle.getString("type");
        order = bundle.getString("order");
    }

    @Override
    protected void initData() {
        super.initData();
        mHomePresenter.getBannerDetailData(id, ContentValue.limit,skip,type,order);
    }

    @Override
    public void onGetBannerResult(BannerModle modle) {
        List<BannerModle.WallpaperBean> mBeanList =
                modle.getWallpaper();
        if (mBeanList.size() < 30){
            mAdapter.isFinish(true);
        }
        picList.addAll(mBeanList);
        mAdapter.notifyDataSetChanged();
        swipView.setRefreshing(false);

        picUrlList.clear();
        IdList.clear();
        for (int i = 0; i < picList.size(); i++) {
            picUrlList.add(picList.get(i).getImg());
            IdList.add(picList.get(i).getId());
        }
    }

    @Override
    public void onGetHomeResult(HomePageModle modle) {

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
