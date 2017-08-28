package com.lingxiaosuse.picture.tudimension.fragment;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.google.gson.GsonBuilder;
import com.lingxiaosuse.picture.tudimension.R;
import com.lingxiaosuse.picture.tudimension.SpaceItemDecoration;
import com.lingxiaosuse.picture.tudimension.adapter.MyRecycleViewAdapter;
import com.lingxiaosuse.picture.tudimension.modle.HomePageModle;
import com.lingxiaosuse.picture.tudimension.retrofit.HomePageInterface;
import com.lingxiaosuse.picture.tudimension.utils.UIUtils;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by lingxiao on 2017/8/28.
 */

public class RecommendFragment extends BaseFragment{
    private RecyclerView recycleView;
    private ProgressBar progressBar;
    private SwipeRefreshLayout swipeLayout;
    private MyRecycleViewAdapter adapter;
    private ArrayList<HomePageModle.HomeImg> slidePage;
    private ArrayList<HomePageModle.slidePic> slideList;
    private ArrayList<HomePageModle.Picture> picList = new ArrayList<>();
    private ArrayList<HomePageModle.Picture> picMoreList = new ArrayList<>();//加载更多图片

    @Override
    protected void initData() {
        getData(30);
    }

    @Override
    public View initView() {
        View view = View.inflate(UIUtils.getContext(), R.layout.pager_item,null);
        recycleView = view.findViewById(R.id.rv_main);
        swipeLayout = view.findViewById(R.id.sl_main);
        progressBar = view.findViewById(R.id.pb_main);
        //设置item之间的间隔
        SpaceItemDecoration space = new SpaceItemDecoration(10);
        recycleView.addItemDecoration(space);

        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData(30);
            }
        });
        swipeLayout.setColorSchemeResources(
                android.R.color.holo_blue_light,
                android.R.color.holo_red_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_green_light);
        return view;
    }

    /**
     *从服务器上获取数据
     */
    private void getData(final int index) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://service.picasso.adesk.com")
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().create()))
                .build();
        HomePageInterface homwpage = retrofit.create(HomePageInterface.class);
        Call<HomePageModle> call = homwpage.homePageModle(index,false);
        call.enqueue(new Callback<HomePageModle>() {
            @Override
            public void onResponse(Call<HomePageModle> call, final Response<HomePageModle> response) {
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
                    if (homeDesList.get(j).isStatus()){
                        //做一个判断是否是轮播图，如果是，在建一个集合专门放图
                        slideList.add(homeDesList.get(j).value);
                    }
                }
                progressBar.setVisibility(View.INVISIBLE);
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
        final int count = adapter.getItemCount() + 5;
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://service.picasso.adesk.com")
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().create()))
                .build();
        HomePageInterface homwpage = retrofit.create(HomePageInterface.class);
        Call<HomePageModle> call = homwpage.homePageModle(count,false);
        call.enqueue(new Callback<HomePageModle>() {
            @Override
            public void onResponse(Call<HomePageModle> call, Response<HomePageModle> response) {
                picMoreList = response.body().res.getWallpaper();
                //只有45条数据
                if (adapter.getItemCount()<45){
                    picList.add(picMoreList.get(adapter.getItemCount()));
                }else {
                    adapter.isFinish(true);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<HomePageModle> call, Throwable t) {

            }
        });
    }
}
