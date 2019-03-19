package com.lingxiaosuse.picture.tudimension.activity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.app.Service;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.camera.lingxiao.common.app.BaseActivity;
import com.camera.lingxiao.common.app.ContentValue;
import com.camera.lingxiao.common.exception.ApiException;
import com.camera.lingxiao.common.http.RxJavaHelper;
import com.camera.lingxiao.common.observer.HttpRxObserver;
import com.camera.lingxiao.common.utills.SpUtils;
import com.facebook.binaryresource.FileBinaryResource;
import com.facebook.cache.common.SimpleCacheKey;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.github.zackratos.ultimatebar.UltimateBar;
import com.lingxiaosuse.picture.tudimension.R;
import com.lingxiaosuse.picture.tudimension.adapter.ImageLoadAdapter;
import com.lingxiaosuse.picture.tudimension.service.DownloadService;
import com.lingxiaosuse.picture.tudimension.transformer.DepthPageTransformer;
import com.lingxiaosuse.picture.tudimension.utils.DownloadImgUtils;
import com.lingxiaosuse.picture.tudimension.utils.DownloadUtils;
import com.lingxiaosuse.picture.tudimension.utils.FileUtil;
import com.lingxiaosuse.picture.tudimension.utils.FrescoHelper;
import com.lingxiaosuse.picture.tudimension.utils.ToastUtils;
import com.lingxiaosuse.picture.tudimension.utils.UIUtils;
import com.lingxiaosuse.picture.tudimension.widget.ZoomableViewpager;
import com.liuguangqiang.cookie.CookieBar;
import com.liuguangqiang.cookie.OnActionClickListener;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.disposables.Disposable;
import me.relex.photodraweeview.PhotoDraweeView;

public class ImageLoadingActivity extends BaseActivity {

    @BindView(R.id.tv_image_loading)
    TextView textCurrent;
    @BindView(R.id.vp_image_load)
    ZoomableViewpager viewPager;
    @BindView(R.id.iv_image_save)
    ImageView saveImg;
    @BindView(R.id.ll_loading)
    LinearLayout linearLayout;
    @BindView(R.id.rl_loading)
    RelativeLayout relativeLayout;
    @BindView(R.id.iv_img_comment)
    ImageView commentImg;
    @BindView(R.id.img_head)
    SimpleDraweeView headView;

    private Intent intent;
    private int mPosition;
    private int itemCount;
    private String id;
    private ArrayList<String> picList, IdList;
    private ImageLoadAdapter mAdapter;
    private boolean isHot, isVertical;
    private File file;
    private DownloadService mDownloadService;
    private ServiceConnection mConnect;

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_image_loading;
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        UltimateBar.newImmersionBuilder()
                .applyNav(false)         // 是否应用到导航栏
                .build(this)
                .apply();
        intent = getIntent();
    }

    @Override
    protected void initData() {
        super.initData();
        mPosition = intent.getIntExtra("position", 0);
        itemCount = intent.getIntExtra("itemCount", 0);
        id = intent.getStringExtra("id");
        isHot = intent.getBooleanExtra("isHot", false); //判断是否是hot界面
        isVertical = intent.getBooleanExtra("isVertical", false); //判断是否是壁纸界面
        picList = intent.getStringArrayListExtra("picList");

        if (!isHot) {
            IdList = intent.getStringArrayListExtra("picIdList");
            commentImg.setVisibility(View.VISIBLE);
            String imageRule = UIUtils.getImageRule(isVertical);
            Log.i("code", "instantiateItem: 图片规则  " + imageRule);
            for (int i = 0; i < picList.size(); i++) {
                String url = picList.get(i) + imageRule;
                picList.set(i,url);
            }

        }

        mAdapter = new ImageLoadAdapter(picList);
        mAdapter.setMoveListener(viewPager);
        viewPager.setAdapter(mAdapter);
        viewPager.setCurrentItem(mPosition);
        textCurrent.setText(mPosition + 1 + "/" + itemCount);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                textCurrent.setText(position + 1 + "/" + itemCount);
                mPosition = position;
                if (!isHot || isVertical) {
                    id = IdList.get(position);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        viewPager.setFinishScreenListener(() -> finishAfterTransition());
        //给viewpager设置动画
        viewPager.setPageTransformer(true, new DepthPageTransformer());
        //设置viewpager的点击事件
        mAdapter.setOnItemclick(() -> toggleButtomView());
        mAdapter.setLongClickListener(() -> showDialog());
        bindDownloadService();
    }

    private void bindDownloadService() {
        mConnect = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                DownloadService.DownloadBinder binder = (DownloadService.DownloadBinder) service;
                mDownloadService = binder.getService();
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                mDownloadService = null;
            }
        };
        Intent intent = new Intent(this, DownloadService.class);
        //最后一个参数，是否自动创建service 这个是自动创建
        bindService(intent, mConnect, Service.BIND_AUTO_CREATE);
    }

    @OnClick(R.id.iv_image_back)
    public void imageBack() {
        finishAfterTransition();
    }

    @OnClick(R.id.iv_image_save)
    public void imageSave(final View view) {
        //downloadImg();
        if (isHot){
            downloadImgByFresco();
        }else {
            if (mDownloadService != null){
                ToastUtils.show("正在下载");
                mDownloadService.startDownload(picList.get(mPosition));
            }
        }
    }

    @OnClick(R.id.iv_img_comment)
    public void imageComment() {
        //评论界面
        Intent intent = new Intent(getApplicationContext()
                , CommentActivity.class);
        intent.putExtra("id", id);
        startActivity(intent);
        //动画
        //overridePendingTransition(R.anim.slide_in_top, R.anim.slide_in_top);
    }

    //分享图片
    @OnClick(R.id.iv_img_share)
    public void shareImg() {
        showProgressDialog("请稍后...");
        RxJavaHelper.workWithLifecycle(ImageLoadingActivity.this, (ObservableOnSubscribe<File>) e -> {
            File file = FrescoHelper.saveImageByFresco(picList.get(mPosition));
            e.onNext(file);
        }, new HttpRxObserver() {
            @Override
            protected void onStart(Disposable d) {
            }

            @Override
            protected void onError(ApiException e) {
                ToastUtils.show("下载失败：" + e.getMsg());
                cancleProgressDialog();
            }

            @Override
            protected void onSuccess(Object response) {
                cancleProgressDialog();
                File file = (File) response;
                Intent shareImgIntent = new Intent(Intent.ACTION_SEND);
                shareImgIntent.setType("image/*");
                Uri uri;
                //android7.0
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    shareImgIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    uri = FileProvider.getUriForFile(UIUtils.getContext(),
                            UIUtils.getContext().getApplicationContext().getPackageName() + ".fileprovider",
                            file);
                } else {
                    uri = Uri.fromFile(file);
                    shareImgIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                }
                shareImgIntent.putExtra(Intent.EXTRA_STREAM, uri);
                startActivity(shareImgIntent);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != mConnect){
            unbindService(mConnect);
        }
    }

    @Override
    public void onBackPressed() {
        finishAfterTransition();
        super.onBackPressed();
    }

    /**
     * 隐藏底部导航栏
     */
    private void toggleButtomView() {
        // TODO: 2018/7/31  可能会为空，原因不明
        if (null == relativeLayout){
            Log.d("ImageLoading", "relativeLayout: is null ");
            return;
        }
        float current = relativeLayout.getTranslationY();
        boolean isHiddened = current == 0;
        ObjectAnimator animator = ObjectAnimator
                .ofFloat(relativeLayout, "translationY", current, isHiddened ? relativeLayout.getHeight() + 100 : 0);
        ObjectAnimator alpha = ObjectAnimator.ofFloat(relativeLayout, "alpha", isHiddened ? 1.0f:0f, isHiddened ? 0f:1.0f);
        AnimatorSet set = new AnimatorSet();
        set.play(animator).with(alpha);
        set.setDuration(500);
        set.start();
        Log.d("ImageLoading", "toggleButtomView: "+current);
    }


    private void showDialog() {
        String[] items = {"下载", "复制下载链接", "取消"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setItems(items, (dialogInterface, i) -> {
            if (i == 0) {
                //downloadImg();
                if (mDownloadService != null){
                    if (isHot){
                        downloadImgByFresco();
                    }else {
                        ToastUtils.show("正在下载");
                        mDownloadService.startDownload(picList.get(mPosition));
                    }
                }
            } else if (i == 1) {
                copyImgUrl();
            }
        });
        builder.show();
    }


    private void downloadImgByFresco(){
        RxJavaHelper.workWithLifecycle(ImageLoadingActivity.this, (ObservableOnSubscribe<File>) e -> {
            File file = FrescoHelper.saveImageByFresco(picList.get(mPosition));
            e.onNext(file);
        }, new HttpRxObserver() {
            @Override
            protected void onStart(Disposable d) {

            }

            @Override
            protected void onError(ApiException e) {
                ToastUtils.show("下载失败：" + e.getMsg());
            }

            @Override
            protected void onSuccess(Object response) {
                ToastUtils.show("下载成功");
            }
        });
    }
    private void copyImgUrl() {
        ClipboardManager manager = (ClipboardManager)
                getSystemService(Context.CLIPBOARD_SERVICE);
        // 创建普通字符型ClipData
        ClipData mClipData = ClipData.newPlainText("Label", picList.get(mPosition));
        // 将ClipData内容放到系统剪贴板里。
        manager.setPrimaryClip(mClipData);
        ToastUtils.show("复制成功!");
    }
}
