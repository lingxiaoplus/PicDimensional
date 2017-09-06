package com.lingxiaosuse.picture.tudimension.activity;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.lingxiaosuse.picture.tudimension.R;
import com.lingxiaosuse.picture.tudimension.utils.ToastUtils;
import com.lingxiaosuse.picture.tudimension.utils.UIUtils;

import org.zackratos.ultimatebar.UltimateBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AboutActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_about_version)
    TextView textVersion;
    @BindView(R.id.card_about_me)
    CardView cardMe;
    @BindView(R.id.card_about_gank)
    CardView cardGank;
    @BindView(R.id.card_about_wallpaper)
    CardView cardWallpaper;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    private PackageManager mPmanager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        UltimateBar ultimateBar = new UltimateBar(this);
        ultimateBar.setImmersionBar();
        ButterKnife.bind(this);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "点我干嘛？", Snackbar.LENGTH_LONG)
                        .setAction("干", null).show();
            }
        });

        initToolbar();
        //设置版本号
        setVersion();
    }

    private void setVersion() {
        mPmanager = getPackageManager();
        try {
            PackageInfo info = mPmanager.getPackageInfo(getPackageName(),0);
            String versionName = info.versionName;
            textVersion.setText("版本号："+versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void initToolbar() {
        setSupportActionBar(toolbar);
        ActionBar actionBar =  getSupportActionBar();
        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }
        toolbar.setTitle("关于图次元");
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
    @OnClick({R.id.card_about_wallpaper,R.id.card_about_version,R.id.card_about_gank,R.id.card_about_me})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.card_about_wallpaper:
                break;
            case R.id.card_about_version:
                ToastUtils.show("已经是最新版本");
                break;
            case R.id.card_about_gank:
                ToastUtils.show("点击了干货集中营");
                goToInternet(AboutActivity.this,"http://gank.io");
                break;
            case R.id.card_about_me:
                ToastUtils.show("点击了我");
                goToMarket(AboutActivity.this,getPackageName());
                break;
        }
    }

    //跳转到网页
    public void goToInternet(Context context, String marketUrl){
        Uri uri = Uri.parse(marketUrl);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        context.startActivity(intent);
    }
    /**
     *@param packageName 目标应用的包名
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
}
