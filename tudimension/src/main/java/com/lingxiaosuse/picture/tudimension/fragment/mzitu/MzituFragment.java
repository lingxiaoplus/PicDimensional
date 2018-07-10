package com.lingxiaosuse.picture.tudimension.fragment.mzitu;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.lingxiaosuse.picture.tudimension.R;
import com.lingxiaosuse.picture.tudimension.activity.MzituDetailActivity;
import com.lingxiaosuse.picture.tudimension.adapter.BaseRecycleAdapter;
import com.lingxiaosuse.picture.tudimension.adapter.MzituRecyclerAdapter;
import com.lingxiaosuse.picture.tudimension.fragment.BaseFragment;
import com.lingxiaosuse.picture.tudimension.global.ContentValue;
import com.lingxiaosuse.picture.tudimension.utils.UIUtils;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lingxiao on 2018/1/19.
 */

public class MzituFragment extends BaseFragment{
    private List<String> mImgList = new ArrayList<>();  //存放图片地址
    private List<String> mTitleList = new ArrayList<>();  //存放标题
    private List<String> mTabList = new ArrayList<>();  //存放tab标题
    private List<String> mImgDetailList = new ArrayList<>();  //存放专辑图片具体的
    private StaggeredGridLayoutManager manager;
    private MzituRecyclerAdapter mAdapter;
    private int mPage = 1;
    private View view;
    private RecyclerView rvMzitu;
    private SwipeRefreshLayout swipMzitu;
    private String type;

    @Override
    protected void initData() {
        Bundle bundle = getArguments();
        type = bundle.getString("type");
        initJsoup(mPage);
    }

    @Override
    public View initView() {
        view = View.inflate(getContext(), R.layout.fragment_mzitu,null);
        rvMzitu = view.findViewById(R.id.rv_mzitu);
        swipMzitu = view.findViewById(R.id.swip_mzitu);
        initRecyclerView();
        return view;
    }

    @Override
    public RecyclerView getRecycle() {
        return null;
    }

    private void initRecyclerView() {
        swipMzitu.setColorSchemeResources(
                R.color.colorPrimary,
                android.R.color.holo_blue_light,
                android.R.color.holo_red_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_green_light
        );
        swipMzitu.setRefreshing(true);
        swipMzitu.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initJsoup(mPage);
            }
        });
        mAdapter = new MzituRecyclerAdapter(mImgList,0,1);
        manager = new StaggeredGridLayoutManager(2,
                StaggeredGridLayoutManager.VERTICAL);
        rvMzitu.setHasFixedSize(true);
        rvMzitu.setLayoutManager(manager);
        rvMzitu.setAdapter(mAdapter);
        mAdapter.setTitle(mTitleList);

        mAdapter.setRefreshListener(new BaseRecycleAdapter.onLoadmoreListener() {
            @Override
            public void onLoadMore() {
                if (mPage<150){
                    mPage++;
                    initJsoup(mPage);
                }else {
                    mAdapter.isFinish(true);
                }

            }
        });

        mAdapter.setOnItemClickListener(new BaseRecycleAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View View, int position) {
                Intent intent = new Intent(getContext(), MzituDetailActivity.class);
                intent.putExtra("title",mTitleList.get(position));
                intent.putExtra("imgurl",mImgDetailList.get(position));
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        });
    }

    private void initJsoup(final int page){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Connection connection = Jsoup.connect(ContentValue.MZITU_URL+type+"/page/"+page)
                        .header("Referer","http://www.mzitu.com")
                        .header("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64; rv:48.0) Gecko/20100101 Firefox/48.0")
                        .timeout(5000)
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.108 Safari/537.36");//设置urer-agent  get();;

                Document doc = null;
                try {
                    Connection.Response response = connection.execute();
                    response.cookies();
                    doc = connection.get();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //获取首页详细数据
                try {
                    Element elementHome = doc.getElementById("pins");
                    Elements elementImgs = elementHome.getElementsByTag("li");
                    for (Element elementImg:elementImgs) {
                        final String imgUrl = elementImg.getElementsByTag("a").attr("href");
                        //专辑图片
                        final String imgSrc = elementImg.select("img").attr("data-original");
                        //专辑名字
                        final String imgAlt = elementImg.select("img").attr("alt");
                        mImgList.add(imgSrc);
                        mTitleList.add(imgAlt);
                        mImgDetailList.add(imgUrl);
                    }
                }catch (Exception e){

                }finally {
                    UIUtils.runOnUIThread(new Runnable() {
                        @Override
                        public void run() {
                            mAdapter.notifyDataSetChanged();
                            if (swipMzitu.isRefreshing()){
                                swipMzitu.setRefreshing(false);
                            }

                        }
                    });
                }

            }
        }).start();
    }

}
