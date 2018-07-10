package com.lingxiaosuse.picture.tudimension.fragment.mzitu;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;

import com.lingxiaosuse.picture.tudimension.R;
import com.lingxiaosuse.picture.tudimension.activity.ImageLoadingActivity;
import com.lingxiaosuse.picture.tudimension.activity.MzituDetailActivity;
import com.lingxiaosuse.picture.tudimension.adapter.BaseRecycleAdapter;
import com.lingxiaosuse.picture.tudimension.adapter.MzituRecyclerAdapter;
import com.lingxiaosuse.picture.tudimension.fragment.BaseFragment;
import com.lingxiaosuse.picture.tudimension.global.ContentValue;
import com.lingxiaosuse.picture.tudimension.utils.StringUtils;
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

public class DailyFragment extends BaseFragment{

    private View view;
    private RecyclerView rvMzitu;
    private SwipeRefreshLayout swipMzitu;
    private String type;

    private int mPage = 1;
    private MzituRecyclerAdapter mAdapter;
    private List<String> mImgList = new ArrayList<>();  //存放图片地址

    private String mUrl;
    @Override
    protected void initData() {
        Bundle bundle = getArguments();
        type = bundle.getString("type");
        mUrl = ContentValue.MZITU_URL+type;
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

        mAdapter = new MzituRecyclerAdapter(mImgList,0,1);
       StaggeredGridLayoutManager  manager = new StaggeredGridLayoutManager(2,
                StaggeredGridLayoutManager.VERTICAL);
        rvMzitu.setHasFixedSize(true);
        rvMzitu.setLayoutManager(manager);
        rvMzitu.setAdapter(mAdapter);
        //mAdapter.setTitle(mTitleList);

        mAdapter.setRefreshListener(new BaseRecycleAdapter.onLoadmoreListener() {
            @Override
            public void onLoadMore() {
                getDataFromJsoup();
            }
        });

        mAdapter.setOnItemClickListener(new BaseRecycleAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View View, int position) {
                Intent intent = new Intent(UIUtils.getContext(),
                        ImageLoadingActivity.class);
                intent.putExtra("position",position);
                intent.putExtra("itemCount",mAdapter.getItemCount());
                intent.putExtra("id",mImgList.get(position));
                intent.putStringArrayListExtra("picList", (ArrayList<String>) mImgList);
                intent.putExtra("isHot",true); // 判断是否为最新界面传递过来的
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        });
    }

    private void getDataFromJsoup() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Connection connection = Jsoup.connect(mUrl)
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
                    Element elementHome = doc.getElementById("comments");
                    Elements elementImgs = elementHome.getElementsByTag("li");
                    //获取到当前页数
                    Elements pageSize = elementHome.getElementsByClass("pagenavi-cm");
                    for (Element elementImg:elementImgs) {
                        final String imgUrl = elementImg.getElementsByTag("img").attr("src");
                        Log.i("图片地址", "run: "+imgUrl);
                        mImgList.add(imgUrl);
                    }

                    for (Element element: pageSize) {
                        String pageStr = element.getElementsByTag("span").text();
                        String pagePattern = StringUtils.getPatternPageNum(pageStr);
                        int page = Integer.valueOf(pagePattern);
                        if (page > 1){
                            page--;
                        }else {
                            mAdapter.isFinish(true);
                        }

                        mUrl = ContentValue.MZITU_URL + type + "/comment-page-"+page+"/#comments";
                    }

                }catch (Exception e){
                    Log.i("DailyFragment", e.getMessage());
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
