package com.lingxiaosuse.picture.tudimension.fragment;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.lingxiaosuse.picture.tudimension.R;
import com.lingxiaosuse.picture.tudimension.SpaceItemDecoration;
import com.lingxiaosuse.picture.tudimension.activity.BannerDetailActivity;
import com.lingxiaosuse.picture.tudimension.activity.ImageLoadingActivity;
import com.lingxiaosuse.picture.tudimension.adapter.MyRecycleViewAdapter;
import com.lingxiaosuse.picture.tudimension.modle.HomePageModle;
import com.lingxiaosuse.picture.tudimension.retrofit.HomePageInterface;
import com.lingxiaosuse.picture.tudimension.retrofit.RetrofitHelper;
import com.lingxiaosuse.picture.tudimension.utils.UIUtils;
import com.lingxiaosuse.picture.tudimension.widget.WaveLoading;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by lingxiao on 2017/8/28.
 */

public class RecommendFragment extends BaseFragment{
    private RecyclerView recycleView;
    private WaveLoading waveLoading;
    private SwipeRefreshLayout swipeLayout;
    private MyRecycleViewAdapter adapter;
    private ArrayList<HomePageModle.HomeImg> slidePage;
    private ArrayList<HomePageModle.slidePic> slideList;
    private ArrayList<HomePageModle.Picture> picList = new ArrayList<>();
    private ArrayList<HomePageModle.Picture> picMoreList = new ArrayList<>();//加载更多图片
    private ArrayList<String> picUrlList = new ArrayList<>();//取出图片地址传递给下一个activity
    private ArrayList<String> picIdList = new ArrayList<>();//取出图片id传递给下一个activity
    private int skip = 0;

    @Override
    protected void initData() {
        waveLoading = getActivity().findViewById(R.id.pb_menu);
        getData(30,0);
        faButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getRecycle().smoothScrollToPosition(0);
            }
        });
    }

    @Override
    public View initView() {
        View view = View.inflate(UIUtils.getContext(), R.layout.fragment_recommend,null);
        recycleView = view.findViewById(R.id.rv_main);
        swipeLayout = view.findViewById(R.id.sl_main);
        //设置item之间的间隔
        SpaceItemDecoration space = new SpaceItemDecoration(10);
        recycleView.addItemDecoration(space);

        swipeLayout.setRefreshing(true);
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData(30,0);
            }
        });
        swipeLayout.setColorSchemeResources(
                R.color.colorPrimary,
                android.R.color.holo_blue_light,
                android.R.color.holo_red_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_green_light);
        /*RefWatcher refWatcher = MyApplication.getRefWatcher(getActivity());
        refWatcher.watch(this);*/
        return view;
    }

    /**
     *从服务器上获取数据
     */
    private void getData(final int limit,int skip) {
        RetrofitHelper.getInstance(UIUtils.getContext())
                .getInterface(HomePageInterface.class)
                .homePageModle(limit,skip,false)
                .enqueue(new Callback<HomePageModle>() {
                    @Override
                    public void onResponse(Call<HomePageModle> call, Response<HomePageModle> response) {
                        picList = response.body().res.getWallpaper();

                        //首页轮播图
                        slidePage = response.body().res.getHomepage();
                        ArrayList<HomePageModle.HomeDes> homeDesList = new ArrayList<HomePageModle.HomeDes>();
                        slideList = new ArrayList<HomePageModle.slidePic>();
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
                        adapter = new MyRecycleViewAdapter(picList,slideList,UIUtils.getContext());
                        // 错列网格布局
                        recycleView.setHasFixedSize(true);      //设置固定大小
                        recycleView.setLayoutManager(new StaggeredGridLayoutManager(2,
                                StaggeredGridLayoutManager.VERTICAL));
                        recycleView.setAdapter(adapter);
                        Log.i("code", "setRefreshing执行了 ");
                        swipeLayout.setRefreshing(false);
                        adapter.notifyDataSetChanged();
                        adapter.setRefreshListener(new MyRecycleViewAdapter.onLoadmoreListener() {
                            @Override
                            public void onLoadMore() {
                                getMoreDataFromServer();
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
                                intent.putStringArrayListExtra("picList",picUrlList);
                                intent.putStringArrayListExtra("picIdList",picIdList);
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

                        waveLoading.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onFailure(Call<HomePageModle> call, Throwable t) {

                    }
                });
    }

    /**
     *从服务器加载更多数据
     */
    private void getMoreDataFromServer() {
        skip+=30;
        RetrofitHelper.getInstance(UIUtils.getContext())
                .getInterface(HomePageInterface.class)
                .homePageModle(30,skip,false)
                .enqueue(new Callback<HomePageModle>() {
                    @Override
                    public void onResponse(Call<HomePageModle> call, Response<HomePageModle> response) {
                        picMoreList = response.body().res.getWallpaper();
                        //不清楚有多少条数据 ，暂时设置为300
                        if (skip<300){
                           picList.addAll(picMoreList);
                        }else {
                            adapter.isFinish(true);
                            skip = 0;
                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onFailure(Call<HomePageModle> call, Throwable t) {

                    }
                });
    }

    @Override
    public RecyclerView getRecycle() {
        return recycleView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        slideList = null;
        slidePage = null;
        picMoreList = null;
        picList = null;
    }
}
