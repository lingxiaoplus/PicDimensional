package com.lingxiaosuse.picture.tudimension.activity;

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
import com.lingxiaosuse.picture.tudimension.R;
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

    private int mPage = 1;
    private int mMaxPage = 2;  //最大页数  >1  让其先请求一次数据
    private String imgUrl;
    private MzituRecyclerAdapter mAdapter;
    private static final int MESSAGE_GET_PICTURE = 1;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case MESSAGE_GET_PICTURE:
                    MzituModle modle = (MzituModle) msg.obj;
                    mAdapter.addData(modle);
                    mAdapter.notifyDataSetChanged();
                    refreshLayout.finishRefresh();
                    refreshLayout.finishLoadMore();
                    break;
                default:
                    mAdapter.loadMoreEnd();
                    refreshLayout.finishRefresh();
                    refreshLayout.finishLoadMore();
                    break;
            }
        }
    };
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
            getDataFromJsoup(1,20);
        });
        refreshLayout.setOnLoadMoreListener(refreshLayout -> {
            if (mMaxPage > mPage){
                getDataFromJsoup(mPage,mPage + 20);
            }else {
                mAdapter.loadMoreEnd();
                refreshLayout.finishLoadMore();
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

    private void getDataFromJsoup(int startPage,int endPage) {
        new Thread(() -> {
            try {
                while (mPage < endPage){
                    Connection connection = Jsoup.connect(imgUrl + "/" + mPage)
                            .header("Referer", "http://www.mzitu.com")
                            .header("User-Agent", ContentValue.USER_AGENT)
                            .timeout(5000);
                            //.userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.108 Safari/537.36");//设置urer-agent  get();;
                    MzituModle modle = new MzituModle();
                    Connection.Response response = connection.execute();
                    response.cookies();
                    Document doc = connection.get();

                    if (mPage == 1) {
                        Elements elementPage = doc.getElementsByClass("pagenavi");
                        String page1 = checkPageNum(elementPage.select("span").text());
                        int n = 2;
                        Log.i("图片页数：", "run: ");
                        String b = page1.substring(page1.length() - n, page1.length());
                        Log.i("图片页数：", "run: " + b);
                        mMaxPage = Integer.valueOf(b);
                        Log.i("图片页数：", "run: " + mMaxPage);
                    }
                    Elements elementDiv = doc.getElementsByClass("main-image");
                    String srcUrl = elementDiv.select("img").attr("src");
                    Log.i("图片地址：", "run: " + srcUrl);
                    modle.setTitle("");
                    modle.setImgUrl(srcUrl);
                    //请求次数
                    mPage++;
                    Message message = Message.obtain();
                    message.what = MESSAGE_GET_PICTURE;
                    message.obj = modle;
                    mHandler.sendMessage(message);
                    Thread.sleep(500);
                }
            }catch (Exception ex){
                ex.printStackTrace();
                mHandler.sendEmptyMessage(0);
            }
        }).start();
        /*RxJavaHelper.workWithLifecycle(this, (ObservableOnSubscribe<MzituModle>) e -> {
            while (mPage < endPage){
                Connection connection = Jsoup.connect(imgUrl + "/" + mPage)
                        .header("Referer", "http://www.mzitu.com")
                        .header("User-Agent", ContentValue.USER_AGENT)
                        .timeout(5000)
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.108 Safari/537.36");//设置urer-agent  get();;
                MzituModle modle = new MzituModle();
                Connection.Response response = connection.execute();
                response.cookies();
                Document doc = connection.get();
                if (mPage == 1) {   //第一次爬完，才知道总共有多少页
                    Elements elementPage = doc.getElementsByClass("pagenavi");
                    String page1 = checkPageNum(elementPage.select("span").text());
                    int n = 2;
                    String b = page1.substring(page1.length() - n, page1.length());
                    mMaxPage = Integer.valueOf(b);
                    Log.i("图片页数：", "run: " + b);
                }
                Elements elementDiv = doc.getElementsByClass("main-image");
                String srcUrl = elementDiv.select("img").attr("src");
                Log.i("图片地址：", "run: " + srcUrl);
                modle.setTitle("");
                modle.setImgUrl(srcUrl);
                //请求次数
                mPage++;
                e.onNext(modle);
            }
            e.onComplete();
            //mPage = endPage;
        }, new HttpRxObserver() {
            @Override
            protected void onStart(Disposable d) {
                mDisposable = d;
            }

            @Override
            protected void onError(ApiException e) {
                refreshLayout.finishRefresh();
                refreshLayout.finishLoadMore();
            }

            @Override
            protected void onSuccess(Object response) {
                MzituModle modle = (MzituModle) response;
                mAdapter.addData(modle);
            }

            @Override
            public void onComplete() {
                super.onComplete();
                refreshLayout.finishRefresh();
                refreshLayout.finishLoadMore();
            }
        });*/
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeMessages(MESSAGE_GET_PICTURE);
    }

}
