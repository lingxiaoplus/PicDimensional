package com.lingxiaosuse.picture.tudimension.fragment;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lingxiaosuse.picture.tudimension.R;
import com.lingxiaosuse.picture.tudimension.activity.ImageLoadingActivity;
import com.lingxiaosuse.picture.tudimension.adapter.HotRecycleAdapter;
import com.lingxiaosuse.picture.tudimension.global.ContentValue;
import com.lingxiaosuse.picture.tudimension.modle.HomePageModle;
import com.lingxiaosuse.picture.tudimension.modle.HotModle;
import com.lingxiaosuse.picture.tudimension.retrofit.HomePageInterface;
import com.lingxiaosuse.picture.tudimension.retrofit.HotInterface;
import com.lingxiaosuse.picture.tudimension.utils.HttpUtils;
import com.lingxiaosuse.picture.tudimension.utils.ToastUtils;
import com.lingxiaosuse.picture.tudimension.utils.UIUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lingxiao on 2017/8/28.
 */

public class HotFragment extends BaseFragment{
    @BindView(R.id.swip_hot)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.rv_hot)
    RecyclerView recyclerView;
    private List<HotModle.ResultsBean> resultList = new ArrayList<>();
    private List<HotModle.ResultsBean> previsList = new ArrayList<>();
    private HotRecycleAdapter mAdapter;
    private GridLayoutManager mLayoutManager;
    private int index = 1;
    private int current = 10;
    private ArrayList<String> picUrlList = new ArrayList<>();//取出图片地址传递给下一个activity
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mAdapter.setResultList(previsList);
            if (refreshLayout.isRefreshing()){
                refreshLayout.setRefreshing(false);
            }
            mAdapter.notifyDataSetChanged();
        }
    };
    @Override
    protected void initData() {
        getData(current,index);
    }

    @Override
    public View initView() {
        View view = UIUtils.inflate(R.layout.fragment_hot);
        ButterKnife.bind(this,view);
        refreshLayout.setColorSchemeResources(
                android.R.color.holo_blue_light,
                android.R.color.holo_red_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_green_light);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData(current,index);
            }
        });
        mLayoutManager = new GridLayoutManager(getContext(),2,
                LinearLayoutManager.VERTICAL,false);
        mAdapter = new HotRecycleAdapter(previsList);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                //得到当前显示的最后一个item的view
                View lastChildView = recyclerView
                        .getLayoutManager()
                        .getChildAt(recyclerView
                                .getLayoutManager()
                                .getChildCount()-1);
                //得到lastChildView的bottom坐标值
                int lastChildBottom = lastChildView.getBottom();
                //得到Recyclerview的底部坐标减去底部padding值，也就是显示内容最底部的坐标
                int recyclerBottom =  recyclerView
                        .getBottom()-recyclerView.getPaddingBottom();
                //通过这个lastChildView得到这个view当前的position值
                int lastPosition  = recyclerView
                        .getLayoutManager()
                        .getPosition(lastChildView);

                //判断lastChildView的bottom值跟recyclerBottom
                //判断lastPosition是不是最后一个position
                //如果两个条件都满足则说明是真正的滑动到了底部
                if(lastChildBottom == recyclerBottom &&
                        lastPosition == recyclerView.getLayoutManager().getItemCount()-1 ){
                    index++;
                    if (index<55){
                        getData(current,index);
                    }else {
                        ToastUtils.show("没有数据了");
                    }
                }
            }
        });

        mAdapter.setOnItemClickListener(new HotRecycleAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View View, int position) {
                if (null == picUrlList){
                    return;
                }
                picUrlList.clear();
                for (int i = 0; i < previsList.size(); i++) {
                    if (picUrlList != null){
                        picUrlList.add(previsList.get(i).getUrl());
                    }
                }
                Intent intent = new Intent(UIUtils.getContext(),
                        ImageLoadingActivity.class);
                intent.putExtra("position",position);
                intent.putExtra("itemCount",mAdapter.getItemCount());
                intent.putExtra("id",previsList.get(position).get_id());
                intent.putStringArrayListExtra("picList",picUrlList);
                intent.putExtra("isHot",true); // 判断是否为最新界面传递过来的
                startActivity(intent);
            }
        });
        return view;
    }
    private void getData(int current,int page){
        HttpUtils.doGet(ContentValue.GANKURL+current+"/"+page,
                new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {

            }

            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                String result = response.body().string();
                Gson gson = new Gson();
                HotModle hotModle = gson.fromJson(result,HotModle.class);
                resultList = hotModle.getResults();
                Log.i("最新模块返回的数据", "onResponse: "+result);
                previsList.addAll(resultList);
                mHandler.sendEmptyMessage(0);
            }
        });
    }
}
