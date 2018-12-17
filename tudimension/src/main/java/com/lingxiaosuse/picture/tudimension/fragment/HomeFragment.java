package com.lingxiaosuse.picture.tudimension.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.camera.lingxiao.common.app.BaseFragment;
import com.camera.lingxiao.common.app.ContentValue;
import com.camera.lingxiao.common.utills.LogUtils;
import com.camera.lingxiao.common.widget.RecyclerAnimator;
import com.lingxiaosuse.picture.tudimension.R;
import com.lingxiaosuse.picture.tudimension.SpaceItemDecoration;
import com.lingxiaosuse.picture.tudimension.activity.BannerDetailActivity;
import com.lingxiaosuse.picture.tudimension.activity.ImageLoadingActivity;
import com.lingxiaosuse.picture.tudimension.adapter.BaseRecycleAdapter;
import com.lingxiaosuse.picture.tudimension.adapter.HomeRecyclerAdapter;
import com.lingxiaosuse.picture.tudimension.modle.BannerModle;
import com.lingxiaosuse.picture.tudimension.modle.HomePageModle;
import com.lingxiaosuse.picture.tudimension.presenter.HomePresenter;
import com.lingxiaosuse.picture.tudimension.utils.ToastUtils;
import com.lingxiaosuse.picture.tudimension.utils.UIUtils;
import com.lingxiaosuse.picture.tudimension.view.HomeView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by lingxiao on 2017/8/28.
 */

public class HomeFragment extends BaseFragment implements HomeView{

    private HomeRecyclerAdapter mHomeAdapter;
    private final String TAG = HomeFragment.class.getSimpleName();
    private List<HomePageModle.slidePic> slideList = new ArrayList<>();
    private List<HomePageModle.Picture> picList = new ArrayList<>();
    private List<HomePageModle.HomeDes> homeDesList = new ArrayList<>();
    private List<String> picUrlList = new ArrayList<>();//取出图片地址传递给下一个activity
    private List<String> picIdList = new ArrayList<>();//取出图片id传递给下一个activity
    private int skip = 0;
    @BindView(R.id.rv_main)
    RecyclerView recycleView;
    @BindView(R.id.sl_main)
    SwipeRefreshLayout swipeLayout;
    @BindView(R.id.fab_fragment)
    FloatingActionButton fab;
    private HomePresenter mPresenter;
    @Override
    protected void initData() {
        swipeLayout.setRefreshing(true);
        mPresenter = new HomePresenter(this,this);
        picList.clear();
        homeDesList.clear();
        slideList.clear();
        mPresenter.getHomePageData(ContentValue.limit,0);
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initWidget(View root) {
        super.initWidget(root);
        //设置item之间的间隔
        SpaceItemDecoration space = new SpaceItemDecoration(10);
        recycleView.addItemDecoration(space);

        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                picList.clear();
                homeDesList.clear();
                slideList.clear();
                mPresenter.getHomePageData(ContentValue.limit,0);
            }
        });
        setSwipeColor(swipeLayout);
        mHomeAdapter = new HomeRecyclerAdapter(picList,slideList,1,1);
        // 错列网格布局
        recycleView.setHasFixedSize(true);      //设置固定大小
        recycleView.setLayoutManager(new StaggeredGridLayoutManager(2,
                StaggeredGridLayoutManager.VERTICAL));
        recycleView.setAdapter(mHomeAdapter);

        mHomeAdapter.setRefreshListener(new BaseRecycleAdapter.onLoadmoreListener() {
            @Override
            public void onLoadMore() {
                skip+=30;
                mPresenter.getHomePageData(ContentValue.limit,skip);
            }
        });
        mHomeAdapter.setOnItemClickListener(new BaseRecycleAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View View, int position) {
                if (null == picUrlList){
                    return;
                }
                picUrlList.clear();
                picIdList.clear();
                for (int i = 0; i < picList.size(); i++) {
                    if (picUrlList != null){
                        picUrlList.add(picList.get(i).img);
                    }
                    picIdList.add(picList.get(i).id);
                }
                Intent intent = new Intent(UIUtils.getContext(),
                        ImageLoadingActivity.class);
                intent.putExtra("position",position);
                intent.putExtra("itemCount",mHomeAdapter.getItemCount());
                intent.putExtra("id",picList.get(position).id);
                intent.putStringArrayListExtra("picList", (ArrayList<String>) picUrlList);
                intent.putStringArrayListExtra("picIdList", (ArrayList<String>) picIdList);
                Log.i("图片浏览详情页", "传过去的数组大小picUrlList：" +
                        ""+picUrlList.size()
                        +"实际大小"+picList.size());
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        });

        mHomeAdapter.setOnBannerClickListener(new HomeRecyclerAdapter.OnBannerClickListener() {
            @Override
            public void onBannerClick(int position) {
                Intent intent = new Intent(UIUtils.getContext(),
                        BannerDetailActivity.class);
                intent.putExtra("url",slideList.get(position).lcover);
                intent.putExtra("desc",slideList.get(position).desc);
                intent.putExtra("id",slideList.get(position).id);
                intent.putExtra("title",slideList.get(position).name);
                intent.putExtra("type",ContentValue.TYPE_ALBUM);  //说明类型是轮播图
                startActivity(intent);
            }
        });

        floatingBtnToogle(recycleView,fab);
    }

    @Override
    public void onGetBannerResult(BannerModle modle) {

    }

    @Override
    public void onGetHomeResult(HomePageModle modle) {
        Log.e(TAG, "获取到返回结果了，隐藏swipeLayout");
        if (modle.getWallpaper().size() < 30){
            mHomeAdapter.isFinish(true);
        }

        //首页轮播图
        List<HomePageModle.HomeImg> slidePage = modle.getHomepage();
        for (int i = 0; i < slidePage.size(); i++) {
            homeDesList.addAll(slidePage.get(i).items);
        }
        //循环遍历该集合，取出首页轮播图
        for (int j = 0; j < homeDesList.size(); j++) {
            if (homeDesList.get(j).isStatus()&&!TextUtils.isEmpty(homeDesList.get(j).value.cover)){
                //做一个判断是否是轮播图，如果是，在建一个集合专门放图
                slideList.add(homeDesList.get(j).value);
            }
        }

        picList.addAll(modle.getWallpaper());
        //mHomeAdapter.add(modle.getWallpaper());

        mHomeAdapter.notifyDataSetChanged();
        swipeLayout.setRefreshing(false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtils.i("HomeFragment销毁");
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
