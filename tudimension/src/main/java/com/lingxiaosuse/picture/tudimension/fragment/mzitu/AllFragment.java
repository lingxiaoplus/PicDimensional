package com.lingxiaosuse.picture.tudimension.fragment.mzitu;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;

import com.lingxiaosuse.picture.tudimension.R;
import com.lingxiaosuse.picture.tudimension.activity.MzituDetailActivity;
import com.lingxiaosuse.picture.tudimension.adapter.AllMzituAdapter;
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
 * Created by lingxiao on 2018/1/20.
 */

public class AllFragment extends BaseFragment{
    private View view;
    private RecyclerView rvMzitu;
    private SwipeRefreshLayout swipMzitu;

    private List<String> mImgList = new ArrayList<>();  //存放图片地址
    private List<String> mTitleList = new ArrayList<>();
    private String type;
    private AllMzituAdapter mAdapter;

    @Override
    protected void initData() {
        Bundle bundle = getArguments();
        type = bundle.getString("type");
        getDataFromJsoup();
    }

    @Override
    public View initView() {
        view = View.inflate(getContext(), R.layout.fragment_mzitu,null);
        rvMzitu = view.findViewById(R.id.rv_mzitu);
        swipMzitu = view.findViewById(R.id.swip_mzitu);
        swipMzitu.setRefreshing(true);
        swipMzitu.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getDataFromJsoup();
            }
        });
        initRecyclerView();
        return view;
    }
    private void initRecyclerView() {
        swipMzitu.setColorSchemeResources(
                R.color.colorPrimary,
                android.R.color.holo_blue_light,
                android.R.color.holo_red_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_green_light
        );

        mAdapter = new AllMzituAdapter(mTitleList,0,1);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        rvMzitu.setHasFixedSize(true);
        rvMzitu.setLayoutManager(manager);
        rvMzitu.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new BaseRecycleAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View View, int position) {
                Intent intent = new Intent(getContext(), MzituDetailActivity.class);
                intent.putExtra("title",mTitleList.get(position));
                intent.putExtra("imgurl",mImgList.get(position));
                startActivity(intent);
            }
        });
    }
    @Override
    public RecyclerView getRecycle() {
        return null;
    }

    private void getDataFromJsoup() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Connection connection = Jsoup.connect(ContentValue.MZITU_URL+type)
                        .timeout(5000);
                Document doc = null;
                try {
                    Connection.Response response = connection.execute();
                    response.cookies();
                    doc = connection.get();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    Elements elementHome = doc.getElementsByClass("archives");

                    for (Element elementImg:elementHome) {
                        Elements elements = elementImg.getElementsByTag("a");
                        for (Element element:elements) {
                            final String imgUrl = element.attr("href");
                            final String imgTitle = element.text();
                            Log.i("图片地址", "run: "+imgUrl+"  图片标题"+imgTitle);
                            mImgList.add(imgUrl);
                            mTitleList.add(imgTitle);
                        }
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

                }catch (Exception e){
                    Log.i("AllFragment", e.getMessage());
                }finally {
                    UIUtils.runOnUIThread(new Runnable() {
                        @Override
                        public void run() {
                            if (swipMzitu.isRefreshing()){
                                swipMzitu.setRefreshing(false);
                            }
                            mAdapter.isFinish(true);
                        }
                    });
                }
            }
        }).start();
    }
}