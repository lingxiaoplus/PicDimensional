package com.lingxiaosuse.picture.tudimension.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;

import com.lingxiaosuse.picture.tudimension.MainActivity;
import com.lingxiaosuse.picture.tudimension.R;
import com.lingxiaosuse.picture.tudimension.global.ContentValue;
import com.lingxiaosuse.picture.tudimension.utils.AnimationUtils;
import com.lingxiaosuse.picture.tudimension.utils.DownloadUtils;
import com.lingxiaosuse.picture.tudimension.utils.SpUtils;
import com.lingxiaosuse.picture.tudimension.utils.ToastUtils;
import com.lingxiaosuse.picture.tudimension.utils.UIUtils;
import com.lingxiaosuse.picture.tudimension.view.LeafLoadingView;

import org.zackratos.ultimatebar.UltimateBar;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lingxiao on 2017/8/3.
 */

public class BaseActivity extends AppCompatActivity{

    private PackageManager mPmanager;
    private int versionCode;
    private Button cancle,ensure;
    private LeafLoadingView leafProg;
    private ImageView fanImage;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            leafProg.setProgress(mProgress);
        }
    };
    private int mProgress;
    private View dialogView;
    private AlertDialog mDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //完全透明的状态栏和导航栏
        /*UltimateBar ultimateBar = new UltimateBar(this);
        ultimateBar.setImmersionBar();*/
        //半透明
        UltimateBar ultimateBar = new UltimateBar(this);
        ultimateBar.setColorBar(ContextCompat.getColor(this, R.color.colorPrimary));
        ActivityController.addActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityController.removeActivity(this);
    }

    public void StartActivity(Class clzz,boolean isFinish){
        startActivity(new Intent(getApplicationContext(),clzz));
        if (isFinish){
            finish();
        }
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
                showDownLoadDia();
                downLoadApk(url);
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

    private void showDownLoadDia() {
        AlertDialog.Builder downLoadBuilder = new AlertDialog.Builder(this);
        //下载进度条
        mDialog = downLoadBuilder.create();
        dialogView = UIUtils.inflate(R.layout.dialog_download);
        leafProg = (LeafLoadingView) dialogView.findViewById(R.id.leaf_download);
        cancle = (Button) dialogView.findViewById(R.id.bt_download_cancle);
        ensure = (Button) dialogView.findViewById(R.id.bt_download_ensure);
        fanImage = (ImageView) dialogView.findViewById(R.id.iv_download_fan);
        mDialog.setView(dialogView);
        mDialog.show();
        initDownloadDiaView();
    }

    private void initDownloadDiaView() {
        RotateAnimation rotateAnimation =
                AnimationUtils.initRotateAnimation(false, 1500, true,
                        Animation.INFINITE);
        fanImage.startAnimation(rotateAnimation);
        Log.i("BaseActivity", "initDownloadDiaView: "+fanImage);
    }

    /**
     *下载安装包
     */
    private void downLoadApk(String url) {
        DownloadUtils.get().download(url, "download", new DownloadUtils.OnDownloadListener() {
            @Override
            public void onDownloadSuccess(File file) {
                UIUtils.runOnUIThread(new Runnable() {
                    @Override
                    public void run() {
                        mDialog.cancel();
                    }
                });
                installApk(file);
            }

            @Override
            public void onDownloading(int progress) {
                mProgress = progress;
                mHandler.sendEmptyMessage(0);
            }

            @Override
            public void onDownloadFailed() {
                UIUtils.runOnUIThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtils.show("下载失败");
                    }
                });
            }
        });
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
}
