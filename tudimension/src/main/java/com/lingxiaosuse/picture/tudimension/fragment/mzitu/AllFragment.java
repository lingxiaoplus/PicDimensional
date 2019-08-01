package com.lingxiaosuse.picture.tudimension.fragment.mzitu;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;

import com.camera.lingxiao.common.app.BaseFragment;
import com.camera.lingxiao.common.app.ContentValue;
import com.lingxiaosuse.picture.tudimension.R;
import com.lingxiaosuse.picture.tudimension.activity.MzituDetailActivity;
import com.lingxiaosuse.picture.tudimension.adapter.AllMzituAdapter;
import com.lingxiaosuse.picture.tudimension.adapter.BaseRecycleAdapter;
import com.lingxiaosuse.picture.tudimension.utils.UIUtils;
import com.lingxiaosuse.picture.tudimension.widget.SmartSkinRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import butterknife.BindView;

/**
 * Created by lingxiao on 2018/1/20.
 */

public class AllFragment extends BaseFragment {
    @BindView(R.id.rv_mzitu)
    RecyclerView rvMzitu;
    @BindView(R.id.swip_mzitu)
    SmartSkinRefreshLayout refreshLayout;

    private List<String> mImgList = new ArrayList<>();  //存放图片地址
    private List<String> mTitleList = new ArrayList<>();
    private String type;
    private AllMzituAdapter mAdapter;
    private ExecutorService mSingleExecutor;

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_mzitu;
    }

    @Override
    protected void initWidget(View root) {
        super.initWidget(root);
        refreshLayout.autoRefresh();
        mSingleExecutor = Executors.newSingleThreadExecutor();
        refreshLayout.setOnRefreshListener(refreshLayout -> {
            mSingleExecutor.execute(jsoupRunnable);
        });


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

            @Override
            public void onLongClick(View view, int position) {

            }
        });
    }

    @Override
    protected void initData() {
        Bundle bundle = getArguments();
        type = bundle.getString("type");
        mSingleExecutor.execute(jsoupRunnable);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mSingleExecutor != null) mSingleExecutor.shutdownNow();
        jsoupRunnable = null;
    }

    private Runnable jsoupRunnable = new Runnable() {
        @Override
        public void run() {
            Connection connection = Jsoup.connect(ContentValue.MZITU_URL+type)
                    .timeout(5000);
            Document doc = null;
            try {
                Connection.Response response = connection.execute();
                response.cookies();
                doc = connection.get();

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
                    UIUtils.runOnUIThread(() -> {
                        if (AllFragment.this.getActivity().isDestroyed()){
                            return;
                        }
                        mAdapter.notifyDataSetChanged();
                        refreshLayout.finishLoadMore();
                        refreshLayout.finishRefresh();
                    });
                }

            }catch (Exception e){
                Log.i("AllFragment", e.getMessage());
            }finally {
                UIUtils.runOnUIThread(() -> {
                    refreshLayout.finishLoadMore();
                    refreshLayout.finishRefresh();
                    mAdapter.isFinish(true);
                });
            }
        }
    };

}
