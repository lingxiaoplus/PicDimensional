package com.camera.lingxiao.common.app;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.camera.lingxiao.common.R;
import com.camera.lingxiao.common.RxBus;
import com.camera.lingxiao.common.SkinChangedEvent;
import com.camera.lingxiao.common.listener.LifeCycleListener;
import com.camera.lingxiao.common.utills.SpUtils;
import com.github.zackratos.ultimatebar.UltimateBar;
import com.trello.rxlifecycle2.components.RxActivity;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import org.reactivestreams.Subscription;

import java.io.File;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public abstract class BaseActivity extends RxAppCompatActivity implements EasyPermissions.PermissionCallbacks{

    private PackageManager mPmanager;
    private int versionCode;
    public LifeCycleListener mListener;
    protected Unbinder unBinder;

    private int mBarcolor;
    private Subscription mRxBus;
    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //在界面未初始化之前调用的初始化窗口
        initWindows();

        if (mListener != null) {
            mListener.onCreate(savedInstanceState);
        }
        if (initArgs(getIntent().getExtras())) {
            // 得到界面Id并设置到Activity界面中
            int layId = getContentLayoutId();
            if (layId != 0){
                setContentView(layId);
            }
            initBefore();
            initWidget();
            initData();
        } else {
            finish();
        }
    }

    /**
     * 初始化控件调用之前
     */
    protected void initBefore() {
        mBarcolor = SpUtils.getInt(this,ContentValue.SKIN_ID,R.color.colorPrimary);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT){
            //半透明
            UltimateBar.newColorBuilder()
                    .statusColor(ContextCompat.getColor(this, mBarcolor))   // 状态栏颜色
                    .applyNav(true)             // 是否应用到导航栏
                    .navColor(ContextCompat.getColor(this, mBarcolor))         // 导航栏颜色
                    .navDepth(0)            // 导航栏颜色深度
                    .build(this)
                    .apply();
        }
        ActivityController.addActivity(this);
    }

    /**
     * 初始化
     */
    private void initWindows() {

    }
    /**
     * 初始化相关参数
     * 如果参数正确返回True，错误返回False
     */
    protected boolean initArgs(Bundle bundle){
        return true;
    }

    /**
     * 得到当前界面的资源文件id
     */
    protected abstract int getContentLayoutId();

    /**
     * 初始化控件
     */
    protected void initWidget(){
        unBinder = ButterKnife.bind(this);
        initSubscription();
    }

    /**
     *  初始化数据
     */
    protected void initData(){

    }
    public void StartActivity(Class clzz,boolean isFinish){
        startActivity(new Intent(getApplicationContext(),clzz));
        if (isFinish){
            finish();
        }
    }

    /**
     * 订阅消息
     */
    private void initSubscription() {
        Disposable regist = RxBus.getInstance().register(SkinChangedEvent.class, new Consumer<SkinChangedEvent>() {
            @Override
            public void accept(SkinChangedEvent skinChangedEvent) throws Exception {
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT){
                    UltimateBar.newColorBuilder()
                            .statusColor(ContextCompat.getColor(getApplicationContext(), skinChangedEvent.getColor()))   // 状态栏颜色
                            .applyNav(true)             // 是否应用到导航栏
                            .navColor(ContextCompat.getColor(getApplicationContext(), skinChangedEvent.getColor()))         // 导航栏颜色
                            .build(BaseActivity.this)
                            .apply();
                }
            }
        });
        RxBus.getInstance().addSubscription(this,regist);
    }

    /**
     *检查更新
     */
    public boolean checkUpdate(){

        mPmanager = getPackageManager();
        int serverVersion = SpUtils
                .getInt(BaseActivity.this, ContentValue.VERSION_CODE,1);
        try {
            PackageInfo info = mPmanager.getPackageInfo(getPackageName(),0);
            versionCode = info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            versionCode = serverVersion;
        }
        if (versionCode<serverVersion){
            //服务器上面有新版本
            String url = SpUtils.getString(BaseActivity.this,ContentValue.DOWNLOAD_URL,"");
            showDialog(url);
            return true;
        }else {
            return false;
        }
    }

    private void showDialog(final String url) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("检测到新版本");
        builder.setMessage(SpUtils.getString(this,ContentValue.VERSION_DES,""));
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //下载
                //showDownLoadDia();
                //downLoadApk(url);
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

    /**
     *下载成功后安装
     */
    private void installApk(File file) {
        if(Build.VERSION.SDK_INT>=24) {//判读版本是否在7.0以上
            Uri apkUri = FileProvider.getUriForFile(this, "com.lingxiaosuse.picture.tudimension.fileprovider", file);//在AndroidManifest中的android:authorities值
            Intent install = new Intent(Intent.ACTION_VIEW);
            install.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            install.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            install.setDataAndType(apkUri, "application/vnd.android.package-archive");
            startActivity(install);
        } else{
            Intent install = new Intent(Intent.ACTION_VIEW);
            install.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
            install.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(install);
        }
    }

    /**
     *设置toolbar的返回键
     */
    public void setToolbarBack(Toolbar toolbar){
        setSupportActionBar(toolbar);
        ActionBar actionBar =  getSupportActionBar();
        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }
        toolbar.setTitle("");

    }

    //跳转到网页
    public void goToInternet(Context context, String marketUrl){
        Uri uri = Uri.parse(marketUrl);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        context.startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).build().show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE) {
            //拒绝授权后，从系统设置了授权后，返回APP进行相应的操作

        }

    }

    @Override
    public void onBackPressed() {
        // 得到当前Activity下的所有Fragment
        @SuppressLint("RestrictedApi")
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        // 判断是否为空
        if (fragments != null && fragments.size() > 0) {
            for (Fragment fragment : fragments) {
                // 判断是否为我们能够处理的Fragment类型
                if (fragment instanceof BaseFragment) {
                    // 判断是否拦截了返回按钮
                    if (((BaseFragment) fragment).onBackPressed()) {
                        // 如果有直接Return
                        return;
                    }
                }
            }
        }
        super.onBackPressed();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mListener != null) {
            mListener.onStart();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (mListener != null) {
            mListener.onRestart();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mListener != null) {
            mListener.onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mListener != null) {
            mListener.onPause();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mListener != null) {
            mListener.onStop();
        }
    }

    /**
     * 当点击界面导航返回时，Finish当前界面
     * @return
     */
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mListener != null) {
            mListener.onDestroy();
        }
        //移除view绑定
        if (unBinder != null) {
            unBinder.unbind();
        }
        RxBus.getInstance().unSubscribe(this);
        ActivityController.removeActivity(this);
    }
    protected void setSwipeColor(SwipeRefreshLayout swipeLayout){
        swipeLayout.setColorSchemeResources(
                R.color.colorPrimary,
                android.R.color.holo_blue_light,
                android.R.color.holo_red_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_green_light);
    }
    /**
     * 设置生命周期回调函数
     *
     * @param listener
     */
    public void setOnLifeCycleListener(LifeCycleListener listener) {
        mListener = listener;
    }
}
