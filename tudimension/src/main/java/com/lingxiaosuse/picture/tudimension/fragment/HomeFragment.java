package com.lingxiaosuse.picture.tudimension.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityOptionsCompat;
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
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lingxiaosuse.picture.tudimension.R;
import com.lingxiaosuse.picture.tudimension.SpaceItemDecoration;
import com.lingxiaosuse.picture.tudimension.activity.BannerDetailActivity;
import com.lingxiaosuse.picture.tudimension.activity.ImageLoadingActivity;
import com.lingxiaosuse.picture.tudimension.adapter.BaseRecycleAdapter;
import com.lingxiaosuse.picture.tudimension.adapter.HomeRecyclerAdapter;
import com.lingxiaosuse.picture.tudimension.modle.BannerModle;
import com.lingxiaosuse.picture.tudimension.modle.HomePageModle;
import com.lingxiaosuse.picture.tudimension.presenter.HomePresenter;
import com.lingxiaosuse.picture.tudimension.transformer.RecyclerViewAnimator;
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
    private List<HomePageModle> homeList = new ArrayList<>();
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
                skip = 0;
                mPresenter.getHomePageData(ContentValue.limit,skip);
            }
        });
        setSwipeColor(swipeLayout);
        mHomeAdapter = new HomeRecyclerAdapter(R.layout.list_page,homeList);
        mHomeAdapter.addHeaderView(View.inflate(getContext(),R.layout.item_head,null));
        // 错列网格布局
        recycleView.setHasFixedSize(true);      //设置固定大小
        recycleView.setLayoutManager(new StaggeredGridLayoutManager(2,
                StaggeredGridLayoutManager.VERTICAL));
        recycleView.setAdapter(mHomeAdapter);

        mHomeAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                skip+=30;
                mPresenter.getHomePageData(ContentValue.limit,skip);
            }
        });
        mHomeAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                List<String> picUrlList = new ArrayList<>();//取出图片地址传递给下一个activity
                List<String> picIdList = new ArrayList<>();//取出图片id传递给下一个activity
                HomePageModle homeModle = (HomePageModle) adapter.getData().get(position);
                List<HomePageModle.Picture> picList = homeModle.getWallpaper();
                for (int i = 0; i < picList.size(); i++) {
                    picUrlList.add(picList.get(i).img);
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
                /*Bundle bundle = ActivityOptionsCompat
                        .makeSceneTransitionAnimation(getActivity(),view,"image").toBundle();*/
                Bundle bundle = ActivityOptionsCompat.makeScaleUpAnimation(view,
                        view.getWidth() / 2, view.getHeight() / 2, 0, 0).toBundle();
                startActivity(intent,bundle);
            }
        });

        mHomeAdapter.setOnBannerClickListener(new HomeRecyclerAdapter.OnBannerClickListener() {
            @Override
            public void onBannerClick(int position) {
                List<HomePageModle.slidePic> slideList = mHomeAdapter.getSlideList();
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
            mHomeAdapter.loadMoreEnd();
        }
        mHomeAdapter.addData(modle);
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
