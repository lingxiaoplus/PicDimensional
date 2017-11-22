package com.lingxiaosuse.picture.tudimension.activity;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.lingxiaosuse.picture.tudimension.MainActivity;
import com.lingxiaosuse.picture.tudimension.R;
import com.lingxiaosuse.picture.tudimension.global.ContentValue;
import com.lingxiaosuse.picture.tudimension.modle.VersionModle;
import com.lingxiaosuse.picture.tudimension.modle.VerticalModle;
import com.lingxiaosuse.picture.tudimension.utils.HttpUtils;
import com.lingxiaosuse.picture.tudimension.utils.SpUtils;
import com.lingxiaosuse.picture.tudimension.utils.UIUtils;

import org.zackratos.ultimatebar.UltimateBar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class SplashActivity extends BaseActivity {

    @BindView(R.id.tv_next)
    TextView tvNext;
    private List<VerticalModle.ResBean.VerticalBean> resultList;
    @BindView(R.id.splash_image)
    SimpleDraweeView draweeView;
    private boolean isFirst;
    private String url = "http://service.picasso.adesk.com/v1/vertical/vertical" +
            "?limit=30?adult=false&first=1&order=hot";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        //判断是否打开了日图
        if (!SpUtils.getBoolean(this, ContentValue.IS_OPEN_DAILY, true)) {
            StartActivity(MainActivity.class, true);
        }

        ButterKnife.bind(this);
        UltimateBar ultimateBar = new UltimateBar(this);
        ultimateBar.setImmersionBar(true);
        isFirst = SpUtils.getBoolean(this, ContentValue.ISFIRST_KEY, true);

        tvNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StartActivity(MainActivity.class, true);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        chcekVersion();
        HttpUtils.doGet(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                UIUtils.runOnUIThread(new Runnable() {
                    @Override
                    public void run() {
                        if (isFirst) {
                            StartActivity(IndicatorActivity.class, true);
                        } else {
                            StartActivity(MainActivity.class, true);
                        }

                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (resultList == null) {
                    resultList = new ArrayList<>();
                }
                String result = response.body().string();
                Gson gson = new Gson();
                VerticalModle verticalModle = gson.fromJson(result, VerticalModle.class);
                resultList = verticalModle.getRes().getVertical();
                UIUtils.runOnUIThread(new Runnable() {
                    @Override
                    public void run() {
                        Random random = new Random();
                        int result = random.nextInt(resultList.size());
                        Uri uri = Uri.parse(resultList.get(result).getImg());
                        draweeView.setImageURI(uri);
                        startAnim();
                    }
                });
            }
        });
    }

    private void startAnim() {
        AnimationSet animationSet = new AnimationSet(true);
            /*
                参数解释：
                    第一个参数：X轴水平缩放起始位置的大小（fromX）。1代表正常大小
                    第二个参数：X轴水平缩放完了之后（toX）的大小，0代表完全消失了
                    第三个参数：Y轴垂直缩放起始时的大小（fromY）
                    第四个参数：Y轴垂直缩放结束后的大小（toY）
                    第五个参数：pivotXType为动画在X轴相对于物件位置类型
                    第六个参数：pivotXValue为动画相对于物件的X坐标的开始位置
                    第七个参数：pivotXType为动画在Y轴相对于物件位置类型
                    第八个参数：pivotYValue为动画相对于物件的Y坐标的开始位置

                   （第五个参数，第六个参数），（第七个参数,第八个参数）是用来指定缩放的中心点
                    0.5f代表从中心缩放
             */
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

    /**
     * 从服务器获取版本信息并保存
     */
    private void chcekVersion() {
        HttpUtils.doGet(ContentValue.UPDATEURL, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                Gson gson = new Gson();
                VersionModle modle = gson.fromJson(result, VersionModle.class);
                SpUtils.putInt(UIUtils.getContext(), ContentValue.VERSION_CODE, modle.getVersionCode());
                SpUtils.putString(UIUtils.getContext(), ContentValue.VERSION_DES, modle.getVersionDes());
                SpUtils.putString(UIUtils.getContext(), ContentValue.DOWNLOAD_URL, modle.getDownloadUrl());
            }
        });
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            ultimateBar.setHideBar(true);
        }
    }
}
