package com.lingxiaosuse.picture.tudimension.activity;

import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SaveCallback;
import com.camera.lingxiao.common.app.BaseActivity;
import com.camera.lingxiao.common.app.ContentValue;
import com.camera.lingxiao.common.oss.Auth;
import com.camera.lingxiao.common.oss.QiNiuSdkHelper;
import com.camera.lingxiao.common.oss.StringMap;
import com.camera.lingxiao.common.utills.LogUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.github.zackratos.ultimatebar.UltimateBar;
import com.lingxiaosuse.picture.tudimension.R;
import com.lingxiaosuse.picture.tudimension.utils.BitmapUtils;
import com.lingxiaosuse.picture.tudimension.utils.FileUtil;
import com.lingxiaosuse.picture.tudimension.utils.ToastUtils;
import com.lingxiaosuse.picture.tudimension.utils.UIUtils;
import com.lingxiaosuse.picture.tudimension.widget.SettingCardView;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UserInfoActivity extends BaseActivity {


    @BindView(R.id.iv_about)
    ImageView ivBack;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.collapsing_Toolbar)
    CollapsingToolbarLayout collapsingToolbar;
    @BindView(R.id.app_bar)
    AppBarLayout appBar;
    @BindView(R.id.card_name)
    SettingCardView cardName;
    @BindView(R.id.card_sex)
    SettingCardView cardSex;
    @BindView(R.id.card_phone)
    SettingCardView cardPhone;
    @BindView(R.id.card_desc)
    SettingCardView cardDesc;
    @BindView(R.id.card_about_safe)
    CardView cardAboutSafe;
    @BindView(R.id.image_header)
    SimpleDraweeView imageHeader;
    private Bitmap mBitmap;
    private static final int CHOOSE_PHOTO = 1;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            try {
                float scale = mBitmap.getWidth() / (float) mBitmap.getHeight();
                float width = appBar.getWidth();
                float height = width / scale;
                CoordinatorLayout.LayoutParams params = new CoordinatorLayout
                        .LayoutParams((int) width, (int) height);
                appBar.setLayoutParams(params);
                ivBack.setImageBitmap(mBitmap);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    };
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
                Random random = new Random();
                int index = random.nextInt(picList.size());
                FileInputStream inputStream = new FileInputStream(picList.get(index));
                mBitmap = BitmapUtils.compressImageByResolution(BitmapFactory.decodeStream(inputStream),
                        840f, 400f);
                mHandler.sendEmptyMessage(0);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_user_info;
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
        setToolbarBack(toolbar);
    }

    @Override
    protected void initData() {
        super.initData();
        AVUser avUser = AVUser.getCurrentUser();
        /*UserModel model = SQLite.select()
                .from(UserModel.class)
                .where(UserModel_Table.objId.eq(avUser.getObjectId()))
                .querySingle();*/
        cardName.setMessage(avUser.getUsername());
        cardPhone.setMessage(avUser.getMobilePhoneNumber());
        toolbar.setTitle(avUser.getUsername());
        new Thread(mShowBgRunnable).start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeMessages(0);
    }

    @OnClick(R.id.image_header)
    public void onHeaderClick(){
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent,CHOOSE_PHOTO);//打开相册
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CHOOSE_PHOTO && data != null){
            if (resultCode == RESULT_OK){
                //4.4及以上系统使用这个方法处理图片
                AVUser avUser = AVUser.getCurrentUser();
                String path = handleImageOnKitKat(data);
                showProgressDialog("请稍后...");
                QiNiuSdkHelper
                        .getInstance()
                        .upload(path,avUser.getObjectId(),
                                getToken(avUser.getObjectId()),this)
                        .setUploadListener(new QiNiuSdkHelper.uploadListener() {
                    @Override
                    public void onSuccess(String url) {
                        cancleProgressDialog();
                        ToastUtils.show("成功");
                        AVUser.getCurrentUser().saveInBackground(new SaveCallback() {
                            @Override
                            public void done(AVException e) {
                                //AVUser.getCurrentUser().put(ContentValue.NICKNAME,path);
                            }
                        });

                    }

                    @Override
                    public void onFaild(String msg) {
                        cancleProgressDialog();
                        ToastUtils.show(msg);
                    }
                });
            }
        }
    }

    private String handleImageOnKitKat(Intent data){
        String imagePath = null;
        Uri uri = data.getData();
        if (DocumentsContract.isDocumentUri(this,uri)){
            //如果是document类型的Uri,则通过document id处理
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())){
                String id = docId.split(":")[1];//解析出数字格式的id
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,selection);
            }else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())){
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),Long.valueOf(docId));
                imagePath = getImagePath(contentUri,null);
            }
        }else if ("content".equalsIgnoreCase(uri.getScheme())){
            //如果是content类型的Uri，则使用普通方式处理
            imagePath = getImagePath(uri,null);
        }else if ("file".equalsIgnoreCase(uri.getScheme())){
            //如果是file类型的Uri，直接获取图片路径即可
            imagePath = uri.getPath();
        }
        return imagePath;
    }
    private String getImagePath(Uri uri,String selection){
        String path = null;
        //通过Uri和selection来获取真实的图片路径
        Cursor cursor = getContentResolver().query(uri,null,selection,null,null);
        if (cursor != null){
            if (cursor.moveToFirst()){
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    private String getToken(String key){
        //这句就是生成token  bucket:key 允许覆盖同名文件
        //insertOnly 如果希望只能上传指定key的文件，
        //并且不允许修改，那么可以将下面的 insertOnly 属性值设为 1
        LogUtils.i("七牛云的bucket和key："+ContentValue.BUCKET+"   "+key);
        String token = Auth.create(UIUtils
                .getContext()
                .getResources()
                .getString(R.string.AccessKey), UIUtils
                .getContext()
                .getResources()
                .getString(R.string.SecretKey))
                .uploadToken(ContentValue.BUCKET,
                        key,3600, new StringMap().put("insertOnly", 0));
        return token;
    }
}
