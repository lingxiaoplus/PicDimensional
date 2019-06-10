package com.lingxiaosuse.picture.tudimension.fragment;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.camera.lingxiao.common.app.BaseFragment;
import com.camera.lingxiao.common.app.ContentValue;
import com.camera.lingxiao.common.utills.LogUtils;
import com.lingxiaosuse.picture.tudimension.R;
import com.lingxiaosuse.picture.tudimension.SpaceItemDecoration;
import com.lingxiaosuse.picture.tudimension.activity.BannerDetailActivity;
import com.lingxiaosuse.picture.tudimension.activity.ImageLoadingActivity;
import com.lingxiaosuse.picture.tudimension.adapter.HomeRecyclerAdapter;
import com.lingxiaosuse.picture.tudimension.modle.BannerModle;
import com.lingxiaosuse.picture.tudimension.modle.HomePageModle;
import com.lingxiaosuse.picture.tudimension.presenter.HomePresenter;
import com.lingxiaosuse.picture.tudimension.utils.ToastUtils;
import com.lingxiaosuse.picture.tudimension.utils.UIUtils;
import com.lingxiaosuse.picture.tudimension.view.HomeView;
import com.lingxiaosuse.picture.tudimension.widget.GlideImageLoader;
import com.lingxiaosuse.picture.tudimension.widget.ScrollerloadRecyclerView;
import com.lingxiaosuse.picture.tudimension.widget.SmartSkinRefreshLayout;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by lingxiao on 2017/8/28.
 */

public class HomeFragment extends BaseFragment implements HomeView{

    private HomeRecyclerAdapter mHomeAdapter;
    private final String TAG = HomeFragment.class.getSimpleName();
    private List<HomePageModle.Picture> homeList = new ArrayList<>();
    private int skip = 0;
    @BindView(R.id.rv_main)
    ScrollerloadRecyclerView recycleView;
    @BindView(R.id.home_refresh_layout)
    SmartSkinRefreshLayout smartRefreshLayout;
    @BindView(R.id.fab_fragment)
    FloatingActionButton fab;
    private HomePresenter mPresenter;
    @Override
    protected void initData() {

    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initWidget(View root) {
        super.initWidget(root);
        //设置item之间的间隔
        SpaceItemDecoration space = new SpaceItemDecoration(4);
        recycleView.addItemDecoration(space);

        smartRefreshLayout.setOnRefreshListener(refreshLayout -> {
            skip = 0;
            recycleView.showShimmerAdapter();
            mPresenter.getHomePageData(ContentValue.limit,skip);
        });
        smartRefreshLayout.setOnLoadMoreListener(refreshLayout -> {
            skip += 30;
            mPresenter.getHomePageData(ContentValue.limit,skip);
        });

        mHomeAdapter = new HomeRecyclerAdapter(R.layout.list_page,homeList);
        mHomeAdapter.addHeaderView(View.inflate(getContext(),R.layout.item_head,null));
        //mHomeAdapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
        mHomeAdapter.setDuration(800);
        mHomeAdapter.openLoadAnimation(view -> new Animator[]{
                ObjectAnimator.ofFloat(view, "scaleY", 0f, 1.05f, 1f),
                ObjectAnimator.ofFloat(view, "scaleX", 0f, 1.05f, 1f)
        });
        mHomeAdapter.isFirstOnly(false);
        // 错列网格布局
        recycleView.setHasFixedSize(true);      //设置固定大小
        recycleView.setLayoutManager(new StaggeredGridLayoutManager(2,
                StaggeredGridLayoutManager.VERTICAL));
        recycleView.setAdapter(mHomeAdapter);
        mHomeAdapter.setOnItemClickListener((adapter, view, position) -> {
            List<String> picUrlList = new ArrayList<>();//取出图片地址传递给下一个activity
            List<String> picIdList = new ArrayList<>();//取出图片id传递给下一个activity
            List<HomePageModle.Picture> picList = adapter.getData();
            for (int i = 0; i < picList.size(); i++) {
                picUrlList.add(picList.get(i).img);
                picIdList.add(picList.get(i).id);
            }
            Intent intent = new Intent(UIUtils.getContext(),
                    ImageLoadingActivity.class);
            intent.putExtra("position",position);
            intent.putExtra("itemCount",mHomeAdapter.getItemCount());
            intent.putExtra("id",picList.get(position).id);
            intent.putStringArrayListExtra("picList", (ArrayList<String>) picUrlList);
            intent.putStringArrayListExtra("picIdList", (ArrayList<String>) picIdList);
            Log.i("图片浏览详情页", "传过去的数组大小picUrlList：" +
                    ""+picUrlList.size()
                    +"实际大小"+picList.size());
                /*Bundle bundle = ActivityOptionsCompat
                        .makeSceneTransitionAnimation(getActivity(),view,"image").toBundle();*/
            Bundle bundle = ActivityOptionsCompat.makeScaleUpAnimation(view,
                    view.getWidth() / 2, view.getHeight() / 2, 0, 0).toBundle();
            startActivity(intent,bundle);
        });
        floatingBtnToogle(recycleView,fab);
    }

    @Override
    protected void onFirstVisiblity() {
        super.onFirstVisiblity();
        smartRefreshLayout.autoRefresh(500);
        mPresenter = new HomePresenter(this,this);
        //mPresenter.getHomePageData(ContentValue.limit,0);
    }

    @Override
    public void onGetBannerResult(BannerModle modle) {

    }

    @Override
    public void onGetHomeResult(HomePageModle modle) {
        if (recycleView.isShowShimmer()) recycleView.hideShimmerAdapter();
        if (modle.getWallpaper().size() < ContentValue.limit){
            mHomeAdapter.loadMoreEnd();
        }
        if (skip == 0){
            //首页轮播图
            List<HomePageModle.slidePic> slideList = new ArrayList<>();
            List<HomePageModle.HomeDes> homeDesList = new ArrayList<>();
            for (int i = 0; i < modle.getHomepage().size(); i++) {
                //循环遍历该集合，取出首页轮播图
                homeDesList.addAll(modle.getHomepage().get(i).items);
            }
            for (int j = 0; j < homeDesList.size(); j++) {
                if (homeDesList.get(j).isStatus() && !TextUtils.isEmpty(homeDesList.get(j).value.cover)){
                    //做一个判断是否是轮播图，因为这个数据里有广告，需要去除  如果是，在建一个集合专门放图
                    slideList.add(homeDesList.get(j).value);
                }
            }
            bindHeaderData(slideList);
        }
        mHomeAdapter.addData(modle.wallpaper);
        smartRefreshLayout.finishRefresh();
        smartRefreshLayout.finishLoadMore();
    }


    protected void bindHeaderData(List mList) {
        final List<HomePageModle.slidePic> slideList = mList;
        View view = mHomeAdapter.getHeaderLayout();
        Banner banner = view.findViewById(R.id.banner_head);
        // 获取cardview的布局属性，记住这里要是布局的最外层的控件的布局属性，如果是里层的会报cast错误
        /*StaggeredGridLayoutManager
                .LayoutParams clp
                = (StaggeredGridLayoutManager.LayoutParams) holder
                .getView(R.id.ll_head).getLayoutParams();
        // 最最关键一步，设置当前view占满列数，这样就可以占据两列实现头部了
        clp.setFullSpan(true);*/

        if (slideList.size() > 0){
            List<String> urlList = new ArrayList<String>();
            List<String> titleList = new ArrayList<String>();
            for (int i = 0; i < slideList.size(); i++) {
                urlList.add(slideList.get(i).lcover);
                titleList.add(slideList.get(i).desc);
            }
            //设置图片加载器
            banner.setImageLoader(new GlideImageLoader())
                    .setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE)//设置banner样式
                    .setImages(urlList)//设置图片集合
                    .setBannerAnimation(Transformer.DepthPage)
                    .setBannerTitles(titleList)//设置标题集合（当banner样式有显示title时）
                    .isAutoPlay(true) //设置自动轮播，默认为true
                    .setDelayTime(ContentValue.BANNER_TIMER)//设置轮播时间
                    .setIndicatorGravity(BannerConfig.TITLE_BACKGROUND)//设置指示器位置（当banner模式中有指示器时）
                    .start();
            banner.setOnBannerListener(position -> {
                Intent intent = new Intent(UIUtils.getContext(),
                        BannerDetailActivity.class);
                intent.putExtra("url",slideList.get(position).lcover);
                intent.putExtra("desc",slideList.get(position).desc);
                intent.putExtra("id",slideList.get(position).id);
                intent.putExtra("title",slideList.get(position).name);
                intent.putExtra("type",ContentValue.TYPE_ALBUM);  //说明类型是轮播图
                startActivity(intent);
            });
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtils.i("HomeFragment销毁");
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
        smartRefreshLayout.finishRefresh();
        smartRefreshLayout.finishLoadMore();
        mHomeAdapter.loadMoreFail();
    }

}
