package com.lingxiaosuse.picture.tudimension.activity;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.lingxiaosuse.picture.tudimension.R;

import java.text.DecimalFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.sharesdk.onekeyshare.OnekeyShare;

public class SettingsActivity extends BaseActivity {
    @BindView(R.id.toolbar_setting)
    Toolbar toolbar;
    @BindView(R.id.switch_upload)
    SwitchCompat switchCompat;
    @BindView(R.id.tv_clear_size)
    TextView clearSize;
    @BindView(R.id.tv_update_wifi)
    TextView textWifi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);
        initToolbar();
        getCacheSize();
    }

    private void getCacheSize() {
        Fresco.getImagePipelineFactory().getMainFileCache().trimToMinimum();
        long size = Fresco.getImagePipelineFactory().getMainFileCache().getSize();
        clearSize.setText(getDataSize(size));
    }

    private void initToolbar() {
        setSupportActionBar(toolbar);
        ActionBar actionBar =  getSupportActionBar();
        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }
        toolbar.setTitle("设置");
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
    @OnClick({R.id.rl_update,R.id.rl_clear,R.id.rl_share})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.rl_update:
                switchCompat.setChecked(!switchCompat.isChecked());
                if (switchCompat.isChecked()){
                    textWifi.setText("WIFI情况下自动下载安装包");
                }else {
                    textWifi.setText("WIFI情况下不自动下载安装包");
                }
                break;
            case R.id.rl_clear:
                clearCache();
                clearSize.setText("缓存清理完毕");
                break;
            case R.id.rl_share:
                showShare();
                break;
        }
    }
    /**
     *将long转换为单位
     */
    public String getDataSize(long size) {
        if (size < 0) {
            size = 0;
        }
        DecimalFormat formater = new DecimalFormat("####.00");
        if (size < 1024) {
            return size + "bytes";
        } else if (size < 1024 * 1024) {
            float kbsize = size / 1024f;
            return formater.format(kbsize) + "KB";
        } else if (size < 1024 * 1024 * 1024) {
            float mbsize = size / 1024f / 1024f;
            return formater.format(mbsize) + "MB";
        } else if (size < 1024 * 1024 * 1024 * 1024) {
            float gbsize = size / 1024f / 1024f / 1024f;
            return formater.format(gbsize) + "GB";
        } else {
            return "缓存无需清理";
        }
    }

    /**
     *清除fresco的缓存
     */
    private void clearCache(){
        ImagePipeline imagePipeline = Fresco.getImagePipeline();
        imagePipeline.clearMemoryCaches();
        imagePipeline.clearDiskCaches();
        // combines above two lines
        imagePipeline.clearCaches();
    }

    private void showShare() {
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间等使用
        oks.setTitle("图次元");
        // titleUrl是标题的网络链接，QQ和QQ空间等使用
        oks.setTitleUrl("http://sharesdk.cn");
        // text是分享文本，所有平台都需要这个字段
        oks.setText("致力于向汉子们分享妹子");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl("http://sharesdk.cn");
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("我是测试评论文本");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(getString(R.string.app_name));
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("http://sharesdk.cn");
        // 启动分享GUI
        oks.show(this);
    }
}
