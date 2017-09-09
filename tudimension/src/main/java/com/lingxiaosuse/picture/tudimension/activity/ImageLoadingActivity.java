package com.lingxiaosuse.picture.tudimension.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lingxiaosuse.picture.tudimension.R;
import com.lingxiaosuse.picture.tudimension.adapter.ImageLoadAdapter;
import com.lingxiaosuse.picture.tudimension.transformer.DepthPageTransformer;
import com.lingxiaosuse.picture.tudimension.utils.DownloadImgUtils;
import com.lingxiaosuse.picture.tudimension.utils.ToastUtils;
import com.lingxiaosuse.picture.tudimension.utils.UIUtils;
import com.liuguangqiang.cookie.CookieBar;

import org.zackratos.ultimatebar.UltimateBar;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ImageLoadingActivity extends AppCompatActivity {

    private Intent intent;
    private int mPosition;
    private int itemCount;
    private String id;
    @BindView(R.id.tv_image_loading)
    TextView textCurrent;
    @BindView(R.id.vp_image_load)
    ViewPager viewPager;
    @BindView(R.id.tv_image_save)
    TextView saveText;
    @BindView(R.id.ll_loading)
    LinearLayout linearLayout;
    private ArrayList<String> picList;
    private ImageLoadAdapter mAdapter;
    private boolean isHot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_loading);
        UltimateBar ultimateBar = new UltimateBar(this);
        ultimateBar.setImmersionBar();
        ButterKnife.bind(this);
        intent = getIntent();
        initData();
    }

    private void initData() {
        mPosition = intent.getIntExtra("position",0);
        itemCount = intent.getIntExtra("itemCount",0);
        id = intent.getStringExtra("id");
        isHot = intent.getBooleanExtra("isHot",false); //判断是否是hot界面
        picList = intent.getStringArrayListExtra("picList");

        if(!isHot){
            linearLayout.setBackgroundColor(Color.BLACK);
        }
        mAdapter = new ImageLoadAdapter(picList,isHot);
        viewPager.setAdapter(mAdapter);
        viewPager.setCurrentItem(mPosition);
        textCurrent.setText(mPosition+1+"/"+itemCount);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                textCurrent.setText(position+1+"/"+itemCount);
                mPosition = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        //给viewpager设置动画
        viewPager.setPageTransformer(true,new DepthPageTransformer());
    }
    @OnClick(R.id.iv_image_back)
    public void imageBack(){
        finish();
    }
    @OnClick(R.id.tv_image_save)
    public void imageSave(final View view){
        ToastUtils.show("正在下载中...");
        DownloadImgUtils.downLoadImg(Uri.parse(picList.get(mPosition)), new DownloadImgUtils.OnDownloadListener() {
            @Override
            public void onDownloadSuccess(Bitmap bitmap) {
                String saveInfo = saveBitmapFile(bitmap);
                new CookieBar.Builder(ImageLoadingActivity.this)
                        .setTitle("提示")
                        .setMessage(saveInfo)
                        .setBackgroundColor(R.color.colorPrimary)
                        .show();
            }

            @Override
            public void onDownloadFailed(String s) {
                new CookieBar.Builder(ImageLoadingActivity.this)
                        .setTitle("下载失败")
                        .setMessage(s)
                        .setBackgroundColor(R.color.colorPrimary)
                        .show();
            }
        });

    }

    public String saveBitmapFile(Bitmap bitmap){
        String path = Environment
                .getExternalStorageDirectory()
                .getAbsolutePath() + "/tudimension";
        String msg = "保存成功";
        File fileDir =new File(path);//将要保存图片的文件夹
        if (!fileDir.exists() || fileDir.isFile()){
            fileDir.mkdirs();
        }
        File file = new File(path+"/"+id+mPosition+".jpg");
        if (file.exists()){
            msg = "图片已经保存了！";
            return msg;
        }
        BufferedOutputStream bos = null;
        try {
            bos = new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            msg = "保存失败";
            return msg;
        }finally {
            try {
                bos.flush();
                bos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // 其次把文件插入到系统图库
        try {
            MediaStore.Images.Media.insertImage(this.getContentResolver(),
                    file.getAbsolutePath(), file.getName(), null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        // 最后通知图库更新
        UIUtils.getContext().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                Uri.fromFile(new File(file.getPath()))));
        return msg;
    }
}
