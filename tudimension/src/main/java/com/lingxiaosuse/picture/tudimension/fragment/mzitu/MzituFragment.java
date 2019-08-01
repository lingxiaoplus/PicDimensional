package com.lingxiaosuse.picture.tudimension.fragment.mzitu;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
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
import com.lingxiaosuse.picture.tudimension.service.DownloadService;
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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import butterknife.BindView;

/**
 * Created by lingxiao on 2018/1/19.
 */

public class MzituFragment extends BaseFragment{
    @BindView(R.id.rv_mzitu)
    RecyclerView rvMzitu;
    @BindView(R.id.swip_mzitu)
    SmartSkinRefreshLayout refreshLayout;

    private List<String> mImgList = new ArrayList<>();  //存放图片地址

    private List<MzituModle> mImageDataList = new ArrayList<>();  //存放标题 图片地址
    private List<String> mImgDetailList = new ArrayList<>();  //存放专辑图片具体的
    private StaggeredGridLayoutManager manager;
    private MzituRecyclerAdapter mAdapter;
    private int mPage = 1;
    private View view;
    private ExecutorService mSingleExecutor;

    private String type;

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_mzitu;
    }

    @Override
    protected void initWidget(View root) {
        super.initWidget(root);
        mSingleExecutor = Executors.newSingleThreadExecutor();
        refreshLayout.setOnRefreshListener(refreshLayout -> {
            mPage = 1;
            mSingleExecutor.execute(jsoupRunnable);
        });
        refreshLayout.setOnLoadMoreListener(refreshLayout -> {
            mPage++;
            mSingleExecutor.execute(jsoupRunnable);
        });

        mAdapter = new MzituRecyclerAdapter(R.layout.list_mzitu,mImageDataList);
        mAdapter.setDuration(800);
        mAdapter.openLoadAnimation(view -> new Animator[]{
                ObjectAnimator.ofFloat(view, "scaleY", 0f, 1.05f, 1f),
                ObjectAnimator.ofFloat(view, "scaleX", 0f, 1.05f, 1f)
        });
        manager = new StaggeredGridLayoutManager(2,
                StaggeredGridLayoutManager.VERTICAL);
        rvMzitu.setHasFixedSize(true);
        rvMzitu.setLayoutManager(manager);
        rvMzitu.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            Intent intent = new Intent(getContext(), MzituDetailActivity.class);
            intent.putExtra("title",mImageDataList.get(position).getTitle());
            intent.putExtra("imgurl",mImgDetailList.get(position));
            startActivity(intent);
        });

        mAdapter.setOnItemLongClickListener((adapter, view, position) ->

                false
        );
    }

    @Override
    protected void initArgs(Bundle bundle) {
        super.initArgs(bundle);
        type = bundle.getString("type");
    }

    @Override
    protected void onFirstVisiblity() {
        super.onFirstVisiblity();
        refreshLayout.autoRefresh();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mSingleExecutor.shutdownNow();
        jsoupRunnable = null;
    }

    private Runnable jsoupRunnable = new Runnable() {
        @Override
        public void run() {
            Connection connection = Jsoup.connect(ContentValue.MZITU_URL+type+"/page/"+mPage)
                    .header("Referer","http://www.mzitu.com")
                    .header("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64; rv:48.0) Gecko/20100101 Firefox/48.0")
                    .timeout(5000)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.108 Safari/537.36");//设置urer-agent  get();;

            Document doc = null;
            //获取首页详细数据
            try {

                Connection.Response response = connection.execute();
                response.cookies();
                doc = connection.get();

                Element elementHome = doc.getElementById("pins");
                Elements elementImgs = elementHome.getElementsByTag("li");
                for (Element elementImg:elementImgs) {
                    final String imgUrl = elementImg.getElementsByTag("a").attr("href");
                    //专辑图片
                    final String imgSrc = elementImg.select("img").attr("data-original");
                    //专辑名字
                    final String imgAlt = elementImg.select("img").attr("alt");
                    MzituModle imageData = new MzituModle();
                    imageData.setTitle(imgAlt);
                    imageData.setImgUrl(imgSrc);
                    mImageDataList.add(imageData);
                    mImgDetailList.add(imgUrl);
                }
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                if (refreshLayout != null){
                    refreshLayout.post(()->{
                        mAdapter.notifyDataSetChanged();
                        refreshLayout.finishLoadMore();
                        refreshLayout.finishRefresh();
                    });
                }
            }
        }
    };

}
