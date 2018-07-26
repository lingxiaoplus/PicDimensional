package com.lingxiaosuse.picture.tudimension.activity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.Nullable;
import android.view.ActionMode;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.TextView;

import com.camera.lingxiao.common.app.BaseActivity;
import com.camera.lingxiao.common.utills.LogUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.github.zackratos.ultimatebar.UltimateBar;
import com.google.gson.Gson;
import com.lingxiaosuse.picture.tudimension.MainActivity;
import com.lingxiaosuse.picture.tudimension.R;
import com.lingxiaosuse.picture.tudimension.global.ContentValue;
import com.lingxiaosuse.picture.tudimension.modle.VersionModle;
import com.lingxiaosuse.picture.tudimension.modle.VerticalModle;
import com.lingxiaosuse.picture.tudimension.presenter.SplashPresenter;
import com.lingxiaosuse.picture.tudimension.utils.HttpUtils;
import com.lingxiaosuse.picture.tudimension.utils.SpUtils;
import com.lingxiaosuse.picture.tudimension.utils.ToastUtils;
import com.lingxiaosuse.picture.tudimension.utils.UIUtils;
import com.lingxiaosuse.picture.tudimension.view.SplashView;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class SplashActivity extends BaseActivity implements SplashView{

    @BindView(R.id.tv_next)
    TextView tvNext;
    private List<VerticalModle.VerticalBean> resultList;
    @BindView(R.id.splash_image)
    SimpleDraweeView draweeView;
    private boolean isFirst;

    private SplashPresenter presenter = new SplashPresenter(this,this);
    @Override
    protected int getContentLayoutId() {
        //判断是否打开了日图
        if (!SpUtils.getBoolean(this, ContentValue.IS_OPEN_DAILY, true)) {
            StartActivity(MainActivity.class, true);
        }

        return R.layout.activity_splash;
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        isFirst = SpUtils.getBoolean(this, ContentValue.ISFIRST_KEY, true);

        tvNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StartActivity(MainActivity.class, true);
            }
        });
    }

    @Override
    protected void initData() {
        super.initData();
        presenter.getVersion();
        presenter.getImgUrl();
        UltimateBar.newImmersionBuilder()
                .applyNav(true)         // 是否应用到导航栏
                .build(this)
                .apply();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            UltimateBar.newHideBuilder()
                    .applyNav(true)     // 是否应用到导航栏
                    .build(this)
                    .apply();
        }
    }

    private void startAnim() {
        AnimationSet animationSet = new AnimationSet(true);
        ScaleAnimation scaleAnimation = new ScaleAnimation(1f, 1.05f, 1f, 1.05f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        //3秒完成动画
        scaleAnimation.setDuration(3000);
        //将AlphaAnimation这个已经设置好的动画添加到 AnimationSet中
        animationSet.addAnimation(scaleAnimation);
        animationSet.setFillAfter(true);
        //启动动画
        draweeView.startAnimation(animationSet);
        scaleAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (isFirst) {
                    StartActivity(IndicatorActivity.class, true);
                } else {
                    StartActivity(MainActivity.class, true);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    @Override
    public void showImgUrl(Uri uri,String error) {
        if (null == error){
            draweeView.setImageURI(uri);
            startAnim();
        }else {
            if (isFirst) {
                StartActivity(IndicatorActivity.class, true);
            } else {
                StartActivity(MainActivity.class, true);
            }
        }
    }

    @Override
    public void onVersionResult(com.camera.lingxiao.common.VersionModle modle) {

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
