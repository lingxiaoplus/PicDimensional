package com.lingxiaosuse.picture.tudimension.activity;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.os.IBinder;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lingxiaosuse.picture.tudimension.R;
import com.lingxiaosuse.picture.tudimension.adapter.ImageLoadAdapter;
import com.lingxiaosuse.picture.tudimension.global.ContentValue;
import com.lingxiaosuse.picture.tudimension.service.DownloadService;
import com.lingxiaosuse.picture.tudimension.transformer.DepthPageTransformer;
import com.lingxiaosuse.picture.tudimension.utils.DownloadImgUtils;
import com.lingxiaosuse.picture.tudimension.utils.DownloadUtils;
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
    @BindView(R.id.iv_image_save)
    ImageView saveImg;
    @BindView(R.id.ll_loading)
    LinearLayout linearLayout;
    @BindView(R.id.rl_loading)
    RelativeLayout relativeLayout;
    @BindView(R.id.iv_img_comment)
    ImageView commentImg;
    private ArrayList<String> picList,IdList;
    private ImageLoadAdapter mAdapter;
    private boolean isHot,isVertical;
    private File file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_loading);
        UltimateBar ultimateBar = new UltimateBar(this);
        ultimateBar.setImmersionBar(true);
        ButterKnife.bind(this);
        intent = getIntent();
        initData();
    }

    private void initData() {
        mPosition = intent.getIntExtra("position",0);
        itemCount = intent.getIntExtra("itemCount",0);
        id = intent.getStringExtra("id");
        isHot = intent.getBooleanExtra("isHot",false); //判断是否是hot界面
        isVertical = intent.getBooleanExtra("isVertical",false); //判断是否是壁纸界面
        picList = intent.getStringArrayListExtra("picList");

        if(!isHot){
            linearLayout.setBackgroundColor(Color.BLACK);
            IdList = intent.getStringArrayListExtra("picIdList");
            commentImg.setVisibility(View.VISIBLE);
        }
        if (isVertical){
            IdList = intent.getStringArrayListExtra("picIdList");
            commentImg.setVisibility(View.VISIBLE);
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
                if (!isHot || isVertical){
                    id = IdList.get(position);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        //给viewpager设置动画
        viewPager.setPageTransformer(true,new DepthPageTransformer());

        //设置viewpager的点击事件
        mAdapter.setOnItemclick(new ImageLoadAdapter.OnItemClickListener() {
            @Override
            public void onClick() {
                toggleButtomView();
            }
        });
        mAdapter.setLongClickListener(new ImageLoadAdapter.onItemLongClickListener() {
            @Override
            public void onLongClick() {
                showDialog();
            }
        });
    }
    @OnClick(R.id.iv_image_back)
    public void imageBack(){
        finish();
    }
    @OnClick(R.id.iv_image_save)
    public void imageSave(final View view){
        downloadImg();
    }

    @OnClick(R.id.iv_img_comment)
    public void imageComment(){
        //评论界面
        Intent intent = new Intent(getApplicationContext()
                ,CommentActivity.class);
        intent.putExtra("id",id);
        startActivity(intent);
        //动画
        overridePendingTransition(R.anim.slide_in_top,R.anim.slide_in_top);
    }
    //分享图片
    @OnClick(R.id.iv_img_share)
    public void shareImg(){
        DownloadImgUtils.downLoadImg(Uri.parse(picList.get(mPosition)), new DownloadImgUtils.OnDownloadListener(){

            @Override
            public void onDownloadSuccess(Bitmap bitmap) {
                saveBitmapFile(bitmap);
                Intent shareImgIntent = new Intent(Intent.ACTION_SEND);
                shareImgIntent.setType("image/*");
                Uri u = Uri.fromFile(file);
                shareImgIntent.putExtra(Intent.EXTRA_STREAM, u);
                startActivity(shareImgIntent);
            }

            @Override
            public void onDownloadFailed(String errormsg) {

            }
        });
    }

    public String saveBitmapFile(Bitmap bitmap){
        String path = ContentValue.PATH;
        String msg = "保存成功";
        File fileDir =new File(path);//将要保存图片的文件夹
        if (!fileDir.exists() || fileDir.isFile()){
            fileDir.mkdirs();
        }
        file = new File(path+"/"+id+mPosition+".jpg");
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //stopService(mDownloadIntent);
        //unbindService(connection);
    }

    /**
     *隐藏底部导航栏
     */
    private void toggleButtomView() {
        float current = relativeLayout.getTranslationY();
        ObjectAnimator animator = ObjectAnimator
                .ofFloat(relativeLayout, "translationY", current, current == 0 ? relativeLayout.getHeight()+100 : 0);
        animator.start();
    }

    private void downloadImg(){
        DownloadUtils.get().download(true, picList.get(mPosition), "tudimension", new DownloadUtils.OnDownloadListener() {
            @Override
            public void onDownloadSuccess(File file) {
                UIUtils.runOnUIThread(new Runnable() {
                    @Override
                    public void run() {
                        new CookieBar.Builder(ImageLoadingActivity.this)
                                .setTitle("提示")
                                .setMessage("下载成功")
                                .setBackgroundColor(R.color.colorPrimary)
                                .show();
                        ToastUtils.show("下载完成");
                    }
                });
                try {
                    MediaStore.Images.Media.insertImage(UIUtils.getContext()
                                    .getContentResolver(),
                            file.getAbsolutePath(), file.getName(), null);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                // 最后通知图库更新
                UIUtils.getContext().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                        Uri.fromFile(new File(file.getPath()))));
            }

            @Override
            public void onDownloading(final int progress) {
                UIUtils.runOnUIThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtils.show("正在下载"+progress+"%");
                    }
                });
            }

            @Override
            public void onDownloadFailed(final String error) {
                UIUtils.runOnUIThread(new Runnable() {
                    @Override
                    public void run() {
                        new CookieBar.Builder(ImageLoadingActivity.this)
                                .setTitle("下载失败")
                                .setMessage(error)
                                .setBackgroundColor(R.color.colorPrimary)
                                .show();
                    }
                });
            }
        });
    }

    private void showDialog() {
        String[] items = {"下载", "复制下载链接", "取消"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (i == 0){
                    downloadImg();
                }else if (i == 1){
                    copyImgUrl();
                }else {
                    
                }
            }
        });
        builder.show();
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
