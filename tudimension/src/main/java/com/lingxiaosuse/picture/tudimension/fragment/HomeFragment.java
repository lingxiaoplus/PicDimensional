package com.lingxiaosuse.picture.tudimension.fragment;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.camera.lingxiao.common.app.BaseFragment;
import com.camera.lingxiao.common.app.ContentValue;
import com.lingxiaosuse.picture.tudimension.R;
import com.lingxiaosuse.picture.tudimension.SpaceItemDecoration;
import com.lingxiaosuse.picture.tudimension.activity.BannerDetailActivity;
import com.lingxiaosuse.picture.tudimension.activity.ImageLoadingActivity;
import com.lingxiaosuse.picture.tudimension.adapter.MyRecycleViewAdapter;
import com.lingxiaosuse.picture.tudimension.modle.BannerModle;
import com.lingxiaosuse.picture.tudimension.modle.HomePageModle;
import com.lingxiaosuse.picture.tudimension.presenter.HomePresenter;
import com.lingxiaosuse.picture.tudimension.utils.ToastUtils;
import com.lingxiaosuse.picture.tudimension.utils.UIUtils;
import com.lingxiaosuse.picture.tudimension.view.HomeView;
import com.lingxiaosuse.picture.tudimension.widget.WaveLoading;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by lingxiao on 2017/8/28.
 */

public class HomeFragment extends BaseFragment implements HomeView{

    private WaveLoading waveLoading;
    private MyRecycleViewAdapter adapter;
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
    private HomePresenter mPresenter = new HomePresenter(this,this);
    @Override
    protected void initData() {
        waveLoading = getActivity().findViewById(R.id.pb_menu);
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
        swipeLayout.setRefreshing(true);
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
        adapter = new MyRecycleViewAdapter(picList,slideList,UIUtils.getContext());
        // 错列网格布局
        recycleView.setHasFixedSize(true);      //设置固定大小
        recycleView.setLayoutManager(new StaggeredGridLayoutManager(2,
                StaggeredGridLayoutManager.VERTICAL));
        recycleView.setAdapter(adapter);
        adapter.setRefreshListener(new MyRecycleViewAdapter.onLoadmoreListener() {
            @Override
            public void onLoadMore() {
                skip+=30;
                mPresenter.getHomePageData(ContentValue.limit,skip);
            }
        });
        adapter.setOnItemClickListener(new MyRecycleViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, Uri uri) {
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
                intent.putExtra("itemCount",adapter.getItemCount());
                intent.putExtra("id",picList.get(position).id);
                intent.putStringArrayListExtra("picList", (ArrayList<String>) picUrlList);
                intent.putStringArrayListExtra("picIdList", (ArrayList<String>) picIdList);
                Log.i("图片浏览详情页", "传过去的数组大小picUrlList：" +
                        ""+picUrlList.size()
                        +"实际大小"+picList.size());
                startActivity(intent);
            }
        });

        adapter.setOnBannerClickListener(new MyRecycleViewAdapter.OnBannerClickListener() {
            @Override
            public void onBannerClick(int position) {
                Intent intent = new Intent(UIUtils.getContext(),
                        BannerDetailActivity.class);
                intent.putExtra("url",slideList.get(position).lcover);
                intent.putExtra("desc",slideList.get(position).desc);
                intent.putExtra("id",slideList.get(position).id);
                intent.putExtra("title",slideList.get(position).name);
                intent.putExtra("type","album");  //说明类型是轮播图
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
        if (modle.getWallpaper().size() < 30){
            adapter.isFinish(true);
        }
        picList.addAll(modle.getWallpaper());
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
        Log.i("code", "setRefreshing执行了 picList:"+picList.size());
        swipeLayout.setRefreshing(false);
        adapter.notifyDataSetChanged();
        waveLoading.setVisibility(View.INVISIBLE);
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