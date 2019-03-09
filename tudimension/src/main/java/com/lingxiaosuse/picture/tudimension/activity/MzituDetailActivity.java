package com.lingxiaosuse.picture.tudimension.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.camera.lingxiao.common.app.BaseActivity;
import com.camera.lingxiao.common.app.ContentValue;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lingxiaosuse.picture.tudimension.R;
import com.lingxiaosuse.picture.tudimension.adapter.BaseRecycleAdapter;
import com.lingxiaosuse.picture.tudimension.adapter.MzituRecyclerAdapter;
import com.lingxiaosuse.picture.tudimension.modle.MzituModle;
import com.lingxiaosuse.picture.tudimension.utils.SpUtils;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MzituDetailActivity extends BaseActivity {

    @BindView(R.id.tv_mzitu_detail_title)
    TextView tvMzituDetailTitle;
    @BindView(R.id.toolbar_mzitu_detail)
    Toolbar toolbarMzituDetail;
    @BindView(R.id.rv_mzitu_detail)
    RecyclerView rvMzituDetail;
    @BindView(R.id.swip_mzitu_detail)
    SmartSkinRefreshLayout refreshLayout;
    private Intent intent;
    private List<MzituModle> mImgList = new ArrayList<>();

    private int mPage = 1;
    private int mMaxPage = 2;  //最大页数  >1  让其先请求一次数据
    private String imgUrl;
    private MzituRecyclerAdapter mAdapter;

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_mzitu_detail;
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        intent = getIntent();
        String title = intent.getStringExtra("title");
        imgUrl = intent.getStringExtra("imgurl");
        setToolbarBack(toolbarMzituDetail);
        tvMzituDetailTitle.setText(title);

        refreshLayout.autoRefresh();
        refreshLayout.setOnRefreshListener(refreshLayout -> {
            getDataFromJsoup(mPage);
        });
        refreshLayout.setOnLoadMoreListener(refreshLayout -> {
            if (mMaxPage > mPage){
                for (int i = mPage; i < mPage+20; i++) {
                    getDataFromJsoup(i);
                }
            }else {
                mAdapter.loadMoreEnd();
            }
        });
        mAdapter = new MzituRecyclerAdapter(R.layout.list_mzitu,mImgList);
        StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(2,
                StaggeredGridLayoutManager.VERTICAL);
        rvMzituDetail.setHasFixedSize(true);
        rvMzituDetail.setLayoutManager(manager);
        rvMzituDetail.setAdapter(mAdapter);


        mAdapter.setOnItemClickListener((adapter, view, position) -> {
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
        });

    }

    @Override
    protected void initData() {
        super.initData();
        /**
         * 先请求20组数据
         */
        for (int i = 0; i < 20; i++) {
            getDataFromJsoup(i);
        }

        //getDataFromJsoup(mPage);
    }

    private void getDataFromJsoup(final int page) {
        new Thread(() -> {
            Connection connection = Jsoup.connect(imgUrl + "/" + page)
                    .header("Referer", "http://www.mzitu.com")
                    .header("User-Agent", ContentValue.USER_AGENT)
                    .timeout(5000)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.108 Safari/537.36");//设置urer-agent  get();;

            Document doc = null;
            MzituModle modle = new MzituModle();
            try {
                Connection.Response response = connection.execute();
                response.cookies();
                doc = connection.get();

            if (page == 1){
                Elements elementPage = doc.getElementsByClass("pagenavi");
                String page1 = checkPageNum(elementPage.select("span").text());
                int n =2;
                String b = page1.substring(page1.length()-n, page1.length());
                mMaxPage = Integer.valueOf(b);
                Log.i("图片页数：", "run: "+b);
            }

            Elements elementDiv = doc.getElementsByClass("main-image");

            String srcUrl = elementDiv.select("img").attr("src");
            Log.i("图片地址：", "run: "+srcUrl);
            modle.setTitle("");
            modle.setImgUrl(srcUrl);
            //请求次数
            mPage++;
            if (MzituDetailActivity.this.isDestroyed()){
                return;
            }
            UIUtils.runOnUIThread(() -> {
                mAdapter.addData(modle);
                mAdapter.notifyDataSetChanged();
                // TODO: 2018/7/31 部分机型报空指针
                refreshLayout.finishRefresh();
                refreshLayout.finishLoadMore();
            });
            } catch (Exception e) {
                e.printStackTrace();
            }finally {
                UIUtils.runOnUIThread(() -> {
                    refreshLayout.finishRefresh();
                    refreshLayout.finishLoadMore();
                });
            }
        }).start();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }

    private String checkPageNum(String msg) {
        String regIP = "[^0-9]";
        //编译正则表达式
        Pattern pattern = Pattern.compile(regIP);
        Matcher matcher = pattern.matcher(msg);
        return matcher.replaceAll("");
    }

    @OnClick(R.id.iv_mzitu_detail_collect)
    public void onCollect(View v){
        // TODO: 2018/1/20 这里需要用sqlite 
        String url =SpUtils.getString(getApplicationContext(),ContentValue.COLLECT_URL,"");
        if (url.contains(imgUrl)){
            ToastUtils.show("已经收藏过了！");
        }else {
            SpUtils.putString(getApplicationContext(),ContentValue.COLLECT_URL,url+imgUrl);
            ToastUtils.show("收藏成功！");
        }
    }
}
