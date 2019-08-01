package com.lingxiaosuse.picture.tudimension.activity;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.camera.lingxiao.common.app.BaseActivity;
import com.camera.lingxiao.common.app.ContentValue;
import com.camera.lingxiao.common.exception.ApiException;
import com.camera.lingxiao.common.http.RxJavaHelper;
import com.camera.lingxiao.common.observer.HttpRxCallback;
import com.camera.lingxiao.common.observer.HttpRxObserver;
import com.camera.lingxiao.common.utills.LogUtils;
import com.lingxiaosuse.picture.tudimension.R;
import com.lingxiaosuse.picture.tudimension.SpaceItemDecoration;
import com.lingxiaosuse.picture.tudimension.adapter.MzituRecyclerAdapter;
import com.lingxiaosuse.picture.tudimension.modle.MzituModle;
import com.lingxiaosuse.picture.tudimension.utils.SpUtils;
import com.lingxiaosuse.picture.tudimension.utils.ToastUtils;
import com.lingxiaosuse.picture.tudimension.utils.UIUtils;
import com.lingxiaosuse.picture.tudimension.widget.SmartSkinRefreshLayout;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

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


    private volatile int mMaxPage = 20;  //最大页数  >1  让其先请求一次数据
    private static int LIMIT = 20;
    private String imgUrl;
    private MzituRecyclerAdapter mAdapter;
    private ExecutorService mExecutorService;

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

        mExecutorService = Executors.newSingleThreadExecutor();
        refreshLayout.autoRefresh();
        refreshLayout.setOnRefreshListener(refreshLayout -> {
            mImgList.clear();
            for (int i=0;i<LIMIT;i++){
                mExecutorService.submit(new JsonRunnable(i));
            }
        });
        refreshLayout.setOnLoadMoreListener(refreshLayout -> {
            //if (mImageIndex != LIMIT) return;  //说明线程池还没有执行完毕
            int nextLimit = Math.min(LIMIT+20, mMaxPage);
            if (LIMIT == nextLimit) {
                refreshLayout.finishLoadMore();
                return;
            }
            for (int i=LIMIT;i <nextLimit;i++){
                mExecutorService.submit(new JsonRunnable(i));
            }
            LIMIT = nextLimit;
        });
        mAdapter = new MzituRecyclerAdapter(R.layout.list_mzitu,mImgList);
        mAdapter.setDuration(800);
        mAdapter.openLoadAnimation(view -> new Animator[]{
                ObjectAnimator.ofFloat(view, "scaleY", 0.3f, 1.05f, 1f),
                ObjectAnimator.ofFloat(view, "scaleX", 0.3f, 1.05f, 1f)
        });
        mAdapter.isFirstOnly(false);
        StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(2,
                StaggeredGridLayoutManager.VERTICAL);
        SpaceItemDecoration space = new SpaceItemDecoration(4);
        rvMzituDetail.addItemDecoration(space);
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



    private class JsonRunnable implements Runnable{
        private int mImageIndex;
        public JsonRunnable(int index){
            mImageIndex = index;
        }
        @Override
        public void run() {
            try {

                if (null != refreshLayout && mMaxPage <= mImageIndex){
                    refreshLayout.post(() -> {
                        mAdapter.loadMoreEnd();
                    });
                    return;
                }
                LogUtils.e("图片索引>>>  " + mImageIndex+ "limit >>>>> " + LIMIT);
                Connection connection = Jsoup.connect(imgUrl + "/" + mImageIndex)
                        .header("Referer", "http://www.mzitu.com")
                        .header("User-Agent", ContentValue.USER_AGENT)
                        .timeout(5000)
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.108 Safari/537.36");//设置urer-agent  get();;

                Document doc = null;
                MzituModle modle = new MzituModle();
                Connection.Response response = connection.response();
                response.cookies();
                doc = connection.get();

                if (mImageIndex == 0) {  //第一次爬的时候，获取到最大页数
                    Elements elementPage = doc.getElementsByClass("pagenavi");
                    String page1 = checkPageNum(elementPage.select("span").text());
                    String b = page1.substring(page1.length() - 2, page1.length());
                    mMaxPage = Integer.valueOf(b);
                    Log.i("图片页数：", "run: " + mMaxPage);
                    //LIMIT = Math.min(LIMIT,mMaxPage);
                }
                Elements elementDiv = doc.getElementsByClass("main-image");
                String srcUrl = elementDiv.select("img").attr("src");
                Log.i("图片地址：", "run: " + srcUrl);
                modle.setTitle("");
                modle.setImgUrl(srcUrl);
                rvMzituDetail.post(()->mAdapter.addData(modle));
                Thread.sleep(180);
            }catch (Exception ex){
                ex.printStackTrace();
            }finally {
                //请求次数
                if (null != refreshLayout){
                    refreshLayout.post(() -> {
                        refreshLayout.finishRefresh();
                        refreshLayout.finishLoadMore();
                    });
                }
                if (mImageIndex == mMaxPage) {
                    //mExecutorService.shutdownNow();
                }
            }
        }
    }
    /*private Runnable jsonRunnable = new Runnable() {
        @Override
        public void run() {


        }
    };*/

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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mExecutorService.shutdownNow();
        /*try {
            mExecutorService.shutdown(); // 先终止执行完成的任务
            if (!mExecutorService.awaitTermination(1000,TimeUnit.MILLISECONDS)){
                mExecutorService.shutdownNow();  //等待1秒，如果还没完成就立即终止
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
    }
}
