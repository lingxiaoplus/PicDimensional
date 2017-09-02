package com.lingxiaosuse.picture.tudimension.activity;

import android.net.Uri;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.lingxiaosuse.picture.tudimension.R;
import com.lingxiaosuse.picture.tudimension.adapter.BannerRecycleAdapter;
import com.lingxiaosuse.picture.tudimension.adapter.MyRecycleViewAdapter;
import com.lingxiaosuse.picture.tudimension.modle.BannerModle;
import com.lingxiaosuse.picture.tudimension.retrofit.BannerInterface;
import com.lingxiaosuse.picture.tudimension.retrofit.RetrofitHelper;
import com.lingxiaosuse.picture.tudimension.utils.ToastUtils;
import com.lingxiaosuse.picture.tudimension.utils.UIUtils;

import org.zackratos.ultimatebar.UltimateBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BannerDetailActivity extends BaseActivity {
    // 控制ToolBar的变量
    private static final float PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR = 0.9f;
    private static final float PERCENTAGE_TO_HIDE_TITLE_DETAILS = 0.3f;

    private static final int ALPHA_ANIMATIONS_DURATION = 200;

    private boolean mIsTheTitleVisible = false;
    private boolean mIsTheTitleContainerVisible = true;

    private int skip = 0;
    private List<BannerModle.ResBean.WallpaperBean> picList = new ArrayList<>();
    @BindView(R.id.main_iv_placeholder)
    SimpleDraweeView mIvPlaceholder; // 大图片

    @BindView(R.id.main_ll_title_container)
    LinearLayout mLlTitleContainer; // Title的LinearLayout

    @BindView(R.id.main_fl_title)
    FrameLayout mFlTitleContainer; // Title的FrameLayout

    @BindView(R.id.main_abl_app_bar)
    AppBarLayout mAblAppBar; // 整个可以滑动的AppBar

    @BindView(R.id.main_tv_title)
    TextView mTvToolbarTitle; // 标题栏Title

    @BindView(R.id.main_tb_toolbar)
    Toolbar mTbToolbar; // 工具栏

    @BindView(R.id.iv_banner_back)
    ImageView imageBack;

    @BindView(R.id.tv_banner_message)
    TextView textMessage;

    @BindView(R.id.rv_banner)
    RecyclerView recyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private BannerRecycleAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banner_detail);
        ButterKnife.bind(this);
        UltimateBar ultimateBar = new UltimateBar(this);
        ultimateBar.setImmersionBar();
        mTbToolbar.setTitle("");

        // AppBar的监听
        mAblAppBar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                int maxScroll = appBarLayout.getTotalScrollRange();
                float percentage = (float) Math.abs(verticalOffset) / (float) maxScroll;
                handleAlphaOnTitle(percentage);
                handleToolbarTitleVisibility(percentage);
            }
        });
        // or 网格布局，可以设置列数和方向，是否反向显示
        mLayoutManager = new GridLayoutManager(this,1,
                LinearLayoutManager.VERTICAL,false);
        initIntentValue(); //接受intent参数

        initParallaxValues(); // 自动滑动效果

        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    /**
     *用于根据传递过来的值初始化控件
     */
    private void initIntentValue() {
        String url = getIntent().getStringExtra("url");
        String message = getIntent().getStringExtra("desc");
        final String id = getIntent().getStringExtra("id");
        String title = getIntent().getStringExtra("title");
        final String type = getIntent().getStringExtra("type");
        textMessage.setText(message);
        Uri uri = Uri.parse(url);
        mIvPlaceholder.setImageURI(uri);
        mTvToolbarTitle.setText(title);
        getDataFromServere(type,id,skip,false);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                /*if (isBottom(recyclerView)){
                    ToastUtils.show("滑动到最底部了");
                    getDataFromServere(type,id,skip,true);
                    mAdapter.notifyDataSetChanged();
                }
                if (skip >300){
                    skip = 0;
                }*/
            }
        });
    }

    private void getDataFromServere(String type, String id, int skip, final boolean isGetMore) {
        if (isGetMore){
            skip+=30;
        }
        RetrofitHelper
                .getInstance(this)
                .getInterface(BannerInterface.class)
                .bannerModle(type,id,30,skip,false)
                .enqueue(new Callback<BannerModle>() {
                    @Override
                    public void onResponse(Call<BannerModle> call, Response<BannerModle> response) {
                        picList = response.body().getRes().getWallpaper();
                        if (isGetMore){
                            picList.addAll(picList);
                        }
                        mAdapter = new BannerRecycleAdapter(picList,
                                UIUtils.getContext()
                        );
                        recyclerView.setLayoutManager(mLayoutManager);
                        recyclerView.setAdapter(mAdapter);
                    }

                    @Override
                    public void onFailure(Call<BannerModle> call, Throwable t) {

                    }
                });
    }

    // 设置自动滑动的动画效果
    private void initParallaxValues() {
        CollapsingToolbarLayout.LayoutParams petDetailsLp =
                (CollapsingToolbarLayout.LayoutParams) mIvPlaceholder.getLayoutParams();

        CollapsingToolbarLayout.LayoutParams petBackgroundLp =
                (CollapsingToolbarLayout.LayoutParams) mFlTitleContainer.getLayoutParams();

        petDetailsLp.setParallaxMultiplier(0.9f);
        petBackgroundLp.setParallaxMultiplier(0.3f);

        mIvPlaceholder.setLayoutParams(petDetailsLp);
        mFlTitleContainer.setLayoutParams(petBackgroundLp);
    }

    // 处理ToolBar的显示
    private void handleToolbarTitleVisibility(float percentage) {
        if (percentage >= PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR) {
            if (!mIsTheTitleVisible) {
                startAlphaAnimation(mTvToolbarTitle, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
                startAlphaAnimation(imageBack, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
                mIsTheTitleVisible = true;
            }
        } else {
            if (mIsTheTitleVisible) {
                startAlphaAnimation(mTvToolbarTitle, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
                startAlphaAnimation(imageBack, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
                mIsTheTitleVisible = false;
            }
        }
    }

    // 控制Title的显示
    private void handleAlphaOnTitle(float percentage) {
        if (percentage >= PERCENTAGE_TO_HIDE_TITLE_DETAILS) {
            if (mIsTheTitleContainerVisible) {
                startAlphaAnimation(mLlTitleContainer, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
                mIsTheTitleContainerVisible = false;
            }
        } else {
            if (!mIsTheTitleContainerVisible) {
                startAlphaAnimation(mLlTitleContainer, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
                mIsTheTitleContainerVisible = true;
            }
        }
    }

    // 设置渐变的动画
    public static void startAlphaAnimation(View v, long duration, int visibility) {
        AlphaAnimation alphaAnimation = (visibility == View.VISIBLE)
                ? new AlphaAnimation(0f, 1f)
                : new AlphaAnimation(1f, 0f);

        alphaAnimation.setDuration(duration);
        alphaAnimation.setFillAfter(true);
        v.startAnimation(alphaAnimation);
    }

    /**
     *判断recycleview是否滑动到底部
     */
    private boolean isBottom(RecyclerView recyclerView1){
        //得到当前显示的最后一个item的view
        View lastChildView = recyclerView1
                .getLayoutManager()
                .getChildAt(recyclerView1
                        .getLayoutManager()
                        .getChildCount()-1);
        //得到lastChildView的bottom坐标值
        int lastChildBottom = lastChildView.getBottom();
        //得到Recyclerview的底部坐标减去底部padding值，也就是显示内容最底部的坐标
        int recyclerBottom =  recyclerView1
                .getBottom()-recyclerView1.getPaddingBottom();
        //通过这个lastChildView得到这个view当前的position值
        int lastPosition  = recyclerView1
                .getLayoutManager()
                .getPosition(lastChildView);

        //判断lastChildView的bottom值跟recyclerBottom
        //判断lastPosition是不是最后一个position
        //如果两个条件都满足则说明是真正的滑动到了底部
        if(lastChildBottom == recyclerBottom &&
                lastPosition == recyclerView1.getLayoutManager().getItemCount()-1 ){
            return true;
        }
        return false;
    }
}
