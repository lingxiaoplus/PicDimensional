package com.lingxiaosuse.picture.tudimension.fragment;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.camera.lingxiao.common.app.BaseFragment;
import com.camera.lingxiao.common.app.ContentValue;
import com.camera.lingxiao.common.utills.LogUtils;
import com.camera.lingxiao.common.utills.SpUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lingxiaosuse.picture.tudimension.R;
import com.lingxiaosuse.picture.tudimension.activity.ImageLoadingActivity;
import com.lingxiaosuse.picture.tudimension.activity.WebActivity;
import com.lingxiaosuse.picture.tudimension.adapter.BannerRecycleAdapter;
import com.lingxiaosuse.picture.tudimension.adapter.BaseRecycleAdapter;
import com.lingxiaosuse.picture.tudimension.modle.BannerModle;
import com.lingxiaosuse.picture.tudimension.modle.HomePageModle;
import com.lingxiaosuse.picture.tudimension.presenter.HomePresenter;
import com.lingxiaosuse.picture.tudimension.transation.HomeTrans;
import com.lingxiaosuse.picture.tudimension.utils.ToastUtils;
import com.lingxiaosuse.picture.tudimension.utils.UIUtils;
import com.lingxiaosuse.picture.tudimension.view.HomeView;
import com.lingxiaosuse.picture.tudimension.widget.SmartSkinRefreshLayout;
import com.liuguangqiang.cookie.CookieBar;
import com.liuguangqiang.cookie.OnActionClickListener;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class BannerFragment extends BaseFragment implements HomeView{
    @BindView(R.id.recycle_vertical_item)
    RecyclerView recycleView;
    @BindView(R.id.swip_vertical_item)
    SmartSkinRefreshLayout refreshLayout;
    @BindView(R.id.fab_vertical_fragment)
    FloatingActionButton fabView;

    private List<BannerModle.WallpaperBean> picList = new ArrayList<>();
    private ArrayList<String> picUrlList = new ArrayList<>();
    private ArrayList<String> IdList = new ArrayList<>();
    private BannerRecycleAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private int skip = 0;
    private HomePresenter mHomePresenter = new HomePresenter(this,this);
    private String id;
    private String type;
    private String order;
    private static final String TAG = "BannerFragment";

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_vertical_pager;
    }

    @Override
    protected void initWidget(View root) {
        super.initWidget(root);
        floatingBtnToogle(recycleView,fabView);
        mLayoutManager = new StaggeredGridLayoutManager(2,
                StaggeredGridLayoutManager.VERTICAL);
        recycleView.setLayoutManager(mLayoutManager);
        recycleView.setHasFixedSize(true);
        refreshLayout.autoRefresh();
        mAdapter = new BannerRecycleAdapter(R.layout.list_banner,picList);
        recycleView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            Intent intent = new Intent(UIUtils.getContext(),
                    ImageLoadingActivity.class);
            intent.putExtra("position", position);
            intent.putExtra("itemCount", mAdapter.getItemCount());
            intent.putExtra("id", picList.get(position).getId());
            intent.putStringArrayListExtra("picList", picUrlList);
            intent.putStringArrayListExtra("picIdList", IdList);
            startActivity(intent);
        });
        mAdapter.setDuration(800);
        mAdapter.openLoadAnimation(view -> new Animator[]{
                ObjectAnimator.ofFloat(view, "scaleY", 0f, 1.05f, 1f),
                ObjectAnimator.ofFloat(view, "scaleX", 0f, 1.05f, 1f)
        });
        refreshLayout.setOnRefreshListener((refreshLayout)-> {
            picUrlList.clear();
            IdList.clear();
            mHomePresenter.getBannerDetailData(id, ContentValue.limit,0,type,order);
        });
        refreshLayout.setOnLoadMoreListener(refreshLayout -> {
            skip += 30;
            mHomePresenter.getBannerDetailData(id, ContentValue.limit,skip,type,order);
        });


    }

    @Override
    protected void initArgs(Bundle bundle) {
        super.initArgs(bundle);
        id = bundle.getString("id");
        type = bundle.getString("type");
        order = bundle.getString("order");
    }

    @Override
    protected void initData() {
        super.initData();
        mHomePresenter.getBannerDetailData(id, ContentValue.limit,skip,type,order);
        LogUtils.i("类型和："+type+"   order : "+order);
    }

    @Override
    public void onGetBannerResult(BannerModle modle) {
        if (modle.getWallpaper().size() < 30){
            mAdapter.loadMoreEnd();
        }
        mAdapter.addData(modle.getWallpaper());
        refreshLayout.finishLoadMore();
        refreshLayout.finishRefresh();

        picUrlList.clear();
        IdList.clear();
        for (int i = 0; i < picList.size(); i++) {
            picUrlList.add(picList.get(i).getImg());
            IdList.add(picList.get(i).getId());
        }

        if (picList.size() < 1){

            int color = SpUtils.getInt(UIUtils.getContext(),
                    ContentValue.SKIN_ID,R.color.colorPrimary);
            new CookieBar.Builder(getActivity())
                    .setTitle("ERROR")
                    .setMessage("图片数据解析失败了")
                    .setBackgroundColor(color)
                    .setAction("跳转到网页版", new OnActionClickListener() {
                        @Override
                        public void onClick() {
                            Intent intent = new Intent(UIUtils.getContext(), WebActivity.class);
                            intent.putExtra("title","");
                            intent.putExtra("url","http://adesk.com/p/album/"+id);
                            startActivity(intent);
                        }
                    })
                    .show();
           /* new Thread(new Runnable() {
                @Override
                public void run() {
                    Connection connection = Jsoup.connect("http://adesk.com/p/album/5b697be5e7bce7670c24213e")
                            .timeout(5000)
                            .userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.75 Safari/537.36");//设置urer-agent  get();;

                    Document doc = null;
                    try {
                        Connection.Response response = connection.execute();
                        response.cookies();
                        doc = connection.get();
                        Log.e(TAG, "地址: "+doc);
                        Element elementCtx = doc.getElementById("album_context");
                        Elements elementsarticle = elementCtx.getElementsByClass("wallpaper-album").select("data-reactid=1");
                        Elements elementsUl = elementsarticle.get(0).getElementsByTag("ul");
                        Elements elementsLi = elementsUl.get(0).getElementsByTag("li");
                        for (Element element : elementsLi) {
                            String targetUrl = element.getElementsByTag("a").attr("href");
                            Log.e(TAG, "地址: "+targetUrl);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.i("MzituActivity", e.getMessage());
                    }

                }

            }).start();*/
        }

    }

    @Override
    public void onGetHomeResult(HomePageModle modle) {

    }

    @Override
    public void showDialog() {

    }

    @Override
    public void diamissDialog() {

    }

    @Override
    public void showToast(String text) {
        ToastUtils.show(text);
    }
}
