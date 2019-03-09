package com.lingxiaosuse.picture.tudimension.fragment.mzitu;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;

import com.camera.lingxiao.common.app.BaseFragment;
import com.camera.lingxiao.common.app.ContentValue;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lingxiaosuse.picture.tudimension.R;
import com.lingxiaosuse.picture.tudimension.activity.ImageLoadingActivity;
import com.lingxiaosuse.picture.tudimension.activity.MzituDetailActivity;
import com.lingxiaosuse.picture.tudimension.adapter.BaseRecycleAdapter;
import com.lingxiaosuse.picture.tudimension.adapter.MzituRecyclerAdapter;
import com.lingxiaosuse.picture.tudimension.modle.MzituModle;
import com.lingxiaosuse.picture.tudimension.utils.StringUtils;
import com.lingxiaosuse.picture.tudimension.utils.ToastUtils;
import com.lingxiaosuse.picture.tudimension.utils.UIUtils;
import com.lingxiaosuse.picture.tudimension.widget.SmartSkinRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by lingxiao on 2018/1/20.
 */

public class DailyFragment extends BaseFragment {
    @BindView(R.id.rv_mzitu)
    RecyclerView rvMzitu;
    @BindView(R.id.swip_mzitu)
    SmartSkinRefreshLayout refreshLayout;

    private String type;

    private int mPage = 1;
    private MzituRecyclerAdapter mAdapter;
    private List<MzituModle> mImgList = new ArrayList<>();  //存放图片地址

    private String mUrl;

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_mzitu;
    }

    @Override
    protected void initWidget(View root) {
        super.initWidget(root);
        refreshLayout.autoRefresh();
        refreshLayout.setOnRefreshListener(refreshLayout -> {
            mImgList.clear();
            getDataFromJsoup();
        });
        refreshLayout.setOnLoadMoreListener(refreshLayout -> getDataFromJsoup());
        mAdapter = new MzituRecyclerAdapter(R.layout.list_mzitu,mImgList);
        StaggeredGridLayoutManager  manager = new StaggeredGridLayoutManager(2,
                StaggeredGridLayoutManager.VERTICAL);
        rvMzitu.setHasFixedSize(true);
        rvMzitu.setLayoutManager(manager);
        rvMzitu.setAdapter(mAdapter);
        //mAdapter.setTitle(mTitleList);
        mAdapter.setDuration(800);
        mAdapter.openLoadAnimation(view -> new Animator[]{
                ObjectAnimator.ofFloat(view, "scaleY", 0f, 1.05f, 1f),
                ObjectAnimator.ofFloat(view, "scaleX", 0f, 1.05f, 1f)
        });
        mAdapter.isFirstOnly(false);
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            try {
                Intent intent = new Intent(UIUtils.getContext(),
                        ImageLoadingActivity.class);
                intent.putExtra("position",position);
                intent.putExtra("itemCount",mAdapter.getItemCount());
                intent.putExtra("id",mImgList.get(position).getImgUrl());
                ArrayList<String> images = new ArrayList<>();
                for (int i = 0; i < mImgList.size(); i++) {
                    images.add(mImgList.get(i).getImgUrl());
                }
                intent.putStringArrayListExtra("picList", images);
                intent.putExtra("isHot",true); // 判断是否为最新界面传递过来的
                startActivity(intent);
            }catch (Exception e){
                e.printStackTrace();
                ToastUtils.show(e.getMessage());
            }
        });

    }

    @Override
    protected void initData() {
        Bundle bundle = getArguments();
        type = bundle.getString("type");
        mUrl = ContentValue.MZITU_URL+type;
        getDataFromJsoup();
    }

    private void getDataFromJsoup() {
        new Thread(() -> {
            Connection connection = Jsoup.connect(mUrl)
                    .timeout(5000);
            Document doc = null;

            try {
                Connection.Response response = connection.execute();
                response.cookies();
                doc = connection.get();

                Element elementHome = doc.getElementById("comments");
                Elements elementImgs = elementHome.getElementsByTag("li");
                //获取到当前页数
                Elements pageSize = elementHome.getElementsByClass("pagenavi-cm");
                for (Element elementImg:elementImgs) {
                    final String imgUrl = elementImg.getElementsByTag("img").attr("src");
                    Log.i("图片地址", "run: "+imgUrl);
                    MzituModle modle = new MzituModle();
                    modle.setTitle("");
                    modle.setImgUrl(imgUrl);
                    mImgList.add(modle);
                }

                for (Element element: pageSize) {
                    String pageStr = element.getElementsByTag("span").text();
                    String pagePattern = StringUtils.getPatternPageNum(pageStr);
                    int page = Integer.valueOf(pagePattern);
                    if (page > 1){
                        page--;
                    }else {
                        mAdapter.loadMoreEnd();
                        refreshLayout.finishLoadMore();
                        refreshLayout.finishRefresh();
                    }
                    mUrl = ContentValue.MZITU_URL + type + "/comment-page-"+page+"/#comments";
                }

            }catch (Exception e){
                Log.i("DailyFragment", e.getMessage());
            }finally {
                UIUtils.runOnUIThread(() -> {
                    if (DailyFragment.this.getActivity().isDestroyed()){
                        return;
                    }
                    mAdapter.notifyDataSetChanged();
                    refreshLayout.finishLoadMore();
                    refreshLayout.finishRefresh();
                });
            }
        }).start();
    }
}
