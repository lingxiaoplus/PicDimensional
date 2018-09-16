package com.lingxiaosuse.picture.tudimension.activity;

import android.app.Service;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.didikee.donate.AlipayDonate;
import android.didikee.donate.WeiXinDonate;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.camera.lingxiao.common.VersionModle;
import com.camera.lingxiao.common.app.BaseActivity;
import com.camera.lingxiao.common.app.ContentValue;
import com.camera.lingxiao.common.utills.SpUtils;
import com.github.zackratos.ultimatebar.UltimateBar;
import com.lingxiaosuse.picture.tudimension.R;
import com.lingxiaosuse.picture.tudimension.presenter.SplashPresenter;
import com.lingxiaosuse.picture.tudimension.service.DownloadService;
import com.lingxiaosuse.picture.tudimension.utils.BitmapUtils;
import com.lingxiaosuse.picture.tudimension.utils.DialogUtil;
import com.lingxiaosuse.picture.tudimension.utils.DownloadUtils;
import com.lingxiaosuse.picture.tudimension.utils.FileUtil;
import com.lingxiaosuse.picture.tudimension.utils.ToastUtils;
import com.lingxiaosuse.picture.tudimension.utils.UIUtils;
import com.lingxiaosuse.picture.tudimension.view.SplashView;
import com.lingxiaosuse.picture.tudimension.widget.SettingCardView;
import com.liuguangqiang.cookie.CookieBar;
import com.liuguangqiang.cookie.OnActionClickListener;
import com.tencent.bugly.crashreport.CrashReport;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AboutActivity extends BaseActivity implements SplashView {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.card_appname)
    SettingCardView cardAppname;
    @BindView(R.id.card_licenses)
    SettingCardView cardLicenses;
    @BindView(R.id.card_update_mark)
    SettingCardView cardUpdateMark;
    @BindView(R.id.card_author)
    SettingCardView cardAuthor;
    @BindView(R.id.card_github)
    SettingCardView cardGithub;
    @BindView(R.id.card_version)
    SettingCardView cardVersion;
    @BindView(R.id.card_blog)
    SettingCardView cardBlog;
    @BindView(R.id.card_from)
    SettingCardView cardFrom;
    @BindView(R.id.app_bar)
    AppBarLayout appBarLayout;
    @BindView(R.id.iv_about)
    ImageView imageAbout;

    private PackageManager mPmanager;
    private View dialogView;
    private SplashPresenter mPresenter;
    private ServiceConnection mConnect;
    private DownloadService mDownloadService;
    private CookieBar cookieBar;
    private Runnable mShowBgRunnable = new Runnable() {
        @Override
        public void run() {
            try {
                List<File> fileList = FileUtil.getFiles(ContentValue.PATH);
                List<String> picList = new ArrayList<>();
                if (null != fileList && fileList.size() != 0) {
                    for (int i = 0; i < fileList.size(); i++) {
                        String path = fileList.get(i).getAbsolutePath();
                        if (BitmapUtils.isLandscape(path) && FileUtil.getFileSize(path) < 2L) {
                            picList.add(path);
                        }
                    }
                }
                // TODO: 2018/7/31  虚拟机报参数不匹配？？
                Random random = new Random();
                int index = random.nextInt(picList.size());
                FileInputStream inputStream = new FileInputStream(picList.get(index));
                bitmap = BitmapUtils.compressImageByResolution(BitmapFactory.decodeStream(inputStream),
                        840f, 400f);
                mHandler.sendEmptyMessage(0);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            try {
                float scale = bitmap.getWidth() / (float) bitmap.getHeight();
                float width = appBarLayout.getWidth();
                float height = width / scale;
                CoordinatorLayout.LayoutParams params = new CoordinatorLayout
                        .LayoutParams((int) width, (int) height);
                appBarLayout.setLayoutParams(params);
                imageAbout.setImageBitmap(bitmap);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    };
    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_about;
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        UltimateBar.newTransparentBuilder()
                .statusColor(getResources().getColor(R.color.transparent))        // 状态栏颜色
                .statusAlpha(100)               // 状态栏透明度
                .applyNav(false)                // 是否应用到导航栏
                .build(this)
                .apply();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (null == cookieBar){
                    int color = SpUtils.getInt(UIUtils.getContext(),
                            ContentValue.SKIN_ID,R.color.colorPrimary);
                    cookieBar = new CookieBar.Builder(AboutActivity.this)
                            .setTitle("恭喜")
                            .setMessage("妹子向你发来一条消息")
                            .setBackgroundColor(color)
                            .setIcon(R.drawable.ic_default_about)
                            .setAction("点击回复", new OnActionClickListener() {
                                @Override
                                public void onClick() {
                                    ToastUtils.show("骗你的，你哪来的妹子");
                                }
                            })
                            .show();
                }

            }
        });

        setToolbarBack(toolbar);
        toolbar.setTitle("关于图次元");
        //设置版本号
        setVersion();
        mPresenter = new SplashPresenter(this, this);
    }

    Bitmap bitmap;

    @Override
    protected void initData() {
        super.initData();
        new Thread(mShowBgRunnable).start();
    }

    private void setVersion() {
        mPmanager = getPackageManager();
        try {
            PackageInfo info = mPmanager.getPackageInfo(getPackageName(), 0);
            String versionName = info.versionName;
            cardVersion.setMessage("当前版本:" + versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }

    @OnClick({R.id.card_appname, R.id.card_licenses,
            R.id.card_update_mark, R.id.card_author,
            R.id.card_github, R.id.card_version,
            R.id.card_blog, R.id.card_from})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.card_appname:
                DialogUtil.getInstence()
                        .setNormalDialog("跳转到应用市场",
                                "是否跳转到应用市场给好评?",
                                AboutActivity.this)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                goToMarket(AboutActivity.this, getPackageName());
                                dialog.dismiss();
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .show();

                break;
            case R.id.card_licenses:
                DialogUtil.getInstence().showSingleDia("版权说明",
                        getResources().getString(R.string.safe_harbor_text), AboutActivity.this);
                break;
            case R.id.card_update_mark:
                DialogUtil.getInstence().showSingleDia("更新日志",
                        getResources().getString(R.string.update_mark_text), AboutActivity.this);
                break;
            case R.id.card_author:
                showPayDialog();
                break;
            case R.id.card_github:
                DialogUtil.getInstence()
                        .setNormalDialog("跳转到github",
                                "是否跳转到作者github?",
                                AboutActivity.this)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                goToInternet(AboutActivity.this, "https://github.com/lingxiaopua/PicDimensional");
                                dialog.dismiss();
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .show();
                break;
            case R.id.card_version:
                showProgressDialog("正在检测更新...");
                mPresenter.getVersion();
                break;
            case R.id.card_blog:
                DialogUtil
                        .getInstence()
                        .setNormalDialog("跳转到博客",
                        "是否跳转到作者博客?",
                        AboutActivity.this)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                goToInternet(AboutActivity.this, "https://www.lingxiaosuse.cn");
                                dialog.dismiss();
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .show();
                break;
            case R.id.card_from:
                showCardFromDia();
                break;
        }
    }

    private AlertDialog dialog;

    private void showCardFromDia() {
        if (dialog == null) {
            View cardFromView = UIUtils.inflate(R.layout.dialog_card_from);
            AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(AboutActivity.this,
                    R.style.Theme_Transparent));
            dialog = builder.create();
            dialog.setView(cardFromView,0,0,0,0);
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        }
        dialog.show();
    }

    /**
     * @param packageName 目标应用的包名
     */
    public static void goToMarket(Context context, String packageName) {
        Uri uri = Uri.parse("market://details?id=" + packageName);
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        try {
            context.startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void showImgUrl(Uri uri, String error) {

    }

    @Override
    public void onVersionResult(VersionModle modle) {
        cancleProgressDialog();
        SpUtils.putInt(UIUtils.getContext(),
                ContentValue.VERSION_CODE,
                modle.getVersionCode());
        SpUtils.putString(UIUtils.getContext(),
                ContentValue.VERSION_DES,
                modle.getVersionDes());
        SpUtils.putString(UIUtils.getContext(),
                ContentValue.DOWNLOAD_URL,
                modle.getDownloadUrl());

        dialogView = UIUtils.inflate(R.layout.dialog_download);
        if (!checkUpdate()) {
            int color = SpUtils.getInt(UIUtils.getContext(),
                    ContentValue.SKIN_ID, R.color.colorPrimary);
            new CookieBar.Builder(AboutActivity.this)
                    .setTitle("恭喜")
                    .setMessage("已经是最新版本了")
                    .setBackgroundColor(color)
                    .show();
        } else {
            //服务器上面有新版本
            String url = SpUtils.getString(AboutActivity.this, ContentValue.DOWNLOAD_URL, "");
            showDialog(url);
        }
    }

    @Override
    public void showDialog() {

    }

    @Override
    public void diamissDialog() {

    }

    @Override
    public void showToast(String text) {
        cancleProgressDialog();
        ToastUtils.show(text);
    }

    private void showDialog(final String url) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("检测到新版本");
        builder.setMessage(SpUtils.getString(this, ContentValue.VERSION_DES, ""));
        builder.setPositiveButton("下载apk", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //下载
                startDownloadService(url);
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        builder.show();
    }

    private void startDownloadService(final String url) {
        mConnect = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                DownloadService.DownloadBinder binder = (DownloadService.DownloadBinder) service;
                mDownloadService = binder.getService();
                mDownloadService.startDownload(url);
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

    /**
     * 支付宝支付
     *
     * @param payCode 收款码后面的字符串；例如：收款二维码里面的字符串为 https://qr.alipay.com/stx00187oxldjvyo3ofaw60 ，则
     *                payCode = stx00187oxldjvyo3ofaw60
     *                注：不区分大小写
     *                FKX014647ZUX0IO5DJW109
     */
    private void donateAlipay(String payCode) {
        boolean hasInstalledAlipayClient = AlipayDonate.hasInstalledAlipayClient(this);
        if (hasInstalledAlipayClient) {
            AlipayDonate.startAlipayClient(this, payCode);
        }
    }

    /**
     * 需要提前准备好 微信收款码 照片，可通过微信客户端生成
     */
    private void donateWeixin() {
        InputStream weixinQrIs = getResources().openRawResource(R.raw.ic_wechat_pay);
        String qrPath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "AndroidDonateSample" + File.separator +
                "lingxiao_weixin.png";
        WeiXinDonate.saveDonateQrImage2SDCard(qrPath, BitmapFactory.decodeStream(weixinQrIs));
        WeiXinDonate.donateViaWeiXin(this, qrPath);
    }

    private void showPayDialog() {
        String[] items = {"支付宝", "微信"};
        AlertDialog.Builder builder = new AlertDialog.Builder(AboutActivity.this);
        builder.setTitle("感谢捐赠~");
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (i == 0) {
                    ToastUtils.show("支付宝");
                    donateAlipay("FKX014647ZUX0IO5DJW109");
                } else {
                    ToastUtils.show("请从相册中选择第一张二维码");
                    donateWeixin();
                }
            }
        });
        builder.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != bitmap) {
            bitmap.recycle();
        }
        mHandler.removeMessages(0);
    }
}
