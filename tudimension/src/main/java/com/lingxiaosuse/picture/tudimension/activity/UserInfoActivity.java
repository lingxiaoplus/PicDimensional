package com.lingxiaosuse.picture.tudimension.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;

import com.avos.avoscloud.AVUser;
import com.camera.lingxiao.common.app.BaseActivity;
import com.camera.lingxiao.common.app.ContentValue;
import com.facebook.drawee.view.SimpleDraweeView;
import com.github.zackratos.ultimatebar.UltimateBar;
import com.lingxiaosuse.picture.tudimension.R;
import com.lingxiaosuse.picture.tudimension.db.UserModel;
import com.lingxiaosuse.picture.tudimension.db.UserModel_Table;
import com.lingxiaosuse.picture.tudimension.utils.BitmapUtils;
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
        toolbar.setTitle("姓名");
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
        new Thread(new Runnable() {
            @Override
            public void run() {
                File file = new File(ContentValue.PATH);
                List<File> fileList = getFiles(file);
                List<String> picList = new ArrayList<>();
                if (null != fileList && fileList.size() != 0) {
                    for (int i = 0; i < fileList.size(); i++) {
                        String path = fileList.get(i).getAbsolutePath();
                        if (BitmapUtils.isLandscape(path) && getFileSize(new File(path)) < 2L) {
                            picList.add(path);
                        }
                    }
                }

                try {
                    // TODO: 2018/7/31  虚拟机报参数不匹配？？
                    Random random = new Random();
                    int index = random.nextInt(picList.size());
                    FileInputStream inputStream = new FileInputStream(picList.get(index));
                    mBitmap = BitmapUtils.compressImageByResolution(BitmapFactory.decodeStream(inputStream),
                            840f, 400f);

                    UIUtils.runOnUIThread(new Runnable() {
                        @Override
                        public void run() {
                            float scale = mBitmap.getWidth() / (float) mBitmap.getHeight();
                            float width = appBar.getWidth();
                            float height = width / scale;
                            CoordinatorLayout.LayoutParams params = new CoordinatorLayout
                                    .LayoutParams((int) width, (int) height);
                            appBar.setLayoutParams(params);
                            ivBack.setImageBitmap(mBitmap);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private List<File> getFiles(File file) {

        try {
            List<File> fileList = new ArrayList<>();
            File[] files = file.listFiles();
            for (File f : files) {
                if (f.isFile()) {
                    fileList.add(f);
                } else {
                    getFiles(f);
                }
            }
            return fileList;
        } catch (NullPointerException e) {
            //ToastUtils.show("出错了："+e.getMessage());
            Log.i("seedownloadimgact", "出错了");
        }
        return null;
    }

    private long getFileSize(File file) {
        if (file == null) {
            return 0;
        }
        long size = 0;
        if (file.exists()) {
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(file);
                size = fis.available();
                fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        size = size / 1024 / 1024; //mb
        return size;
    }
}
