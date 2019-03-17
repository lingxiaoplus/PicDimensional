package com.lingxiaosuse.picture.tudimension.activity;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;

import com.camera.lingxiao.common.rxbus.RxBus;
import com.camera.lingxiao.common.app.BaseActivity;
import com.camera.lingxiao.common.utills.PopwindowUtil;
import com.github.chrisbanes.photoview.OnPhotoTapListener;
import com.github.chrisbanes.photoview.PhotoView;
import com.github.chrisbanes.photoview.PhotoViewAttacher;
import com.lingxiaosuse.picture.tudimension.R;
import com.lingxiaosuse.picture.tudimension.rxbusevent.DeleteEvent;
import com.lingxiaosuse.picture.tudimension.utils.StringUtils;
import com.lingxiaosuse.picture.tudimension.utils.UIUtils;
import com.lingxiaosuse.picture.tudimension.widget.ZoomableViewpager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;


public class LocalImgActivity extends BaseActivity {
    @BindView(R.id.viewpager_local)
    ZoomableViewpager viewPager;
    @BindView(R.id.toolbar_local)
    Toolbar toolbar;
    //@BindView(R.id.conslayout_root)
    //ConstraintLayout conslayoutRoot;
    private ArrayList<String> list;
    private int mPosition;
    private PhotoViewAttacher mAttacher;
    private static final int SET_WALLPAPER = 200;
    private PhotoView photoView;
    private ViewPagerAdapter mAdapter;
    private AlertDialog.Builder builder = null;

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_local_img;
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        Intent intent = getIntent();
        list = intent.getStringArrayListExtra("list");
        mPosition = intent.getIntExtra("position", 0);

        mAdapter = new ViewPagerAdapter();
        viewPager.setAdapter(mAdapter);
        viewPager.setCurrentItem(mPosition);
        setToolbarBack(toolbar);
        String localUrl = list.get(mPosition).substring(6);
        File file = new File(localUrl);
        toolbar.setTitle(file.getName());
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                mPosition = position;
                String localUrl = list.get(mPosition).substring(6);
                File file = new File(localUrl);
                toolbar.setTitle(file.getName());
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        viewPager.setFinishScreenListener(() -> finish());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String localUrl = list.get(mPosition).substring(6);
        File file = new File(localUrl);
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.menu_wallpaper:
                //设置壁纸卡顿，放在子线程中运行
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        setWallpaper();
                    }
                }).start();
                break;
            case R.id.menu_delete:
                showDeleteDialog();
                break;
            case R.id.menu_share:

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
                file = null;
                break;
            case R.id.menu_info:
                backgroundAlpha(0.5f);
                final PopwindowUtil popwindowUtil = new PopwindowUtil
                        .PopupWindowBuilder(LocalImgActivity.this)
                        .setView(R.layout.pop_image_info)
                        .setAnimationStyle(R.style.contextMenuAnim)
                        .setBackgroundDrawable(new BitmapDrawable())
                        .setOnDissmissListener(() -> backgroundAlpha(1f))
                        .setFocusable(true)
                        .setTouchable(true)
                        .setOutsideTouchable(true)
                        .create();
                View rootview = LayoutInflater.from(LocalImgActivity.this)
                        .inflate(R.layout.activity_local_img, null);
                popwindowUtil.showAsLocation(rootview, Gravity.BOTTOM, 0, 20);

                popwindowUtil.setText(R.id.tv_pop_name, "名称：" + file.getName());
                popwindowUtil.setText(R.id.tv_pop_time, "时间：" + StringUtils.longToString(file.lastModified(), "yyyy-MM-dd HH:mm:ss"));
                popwindowUtil.setText(R.id.tv_pop_size, "文件大小：" + StringUtils.getDataSize(getFileSize(file)));
                popwindowUtil.setText(R.id.tv_pop_path, "路径：" + file.getAbsolutePath());

                popwindowUtil.getView(R.id.tv_pop_close)
                        .setOnClickListener(v -> popwindowUtil.dissmiss());
                //showInfoDialog();
                break;
        }
        return true;
    }

    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     */
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getWindow().setAttributes(lp);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.local_img_menu, menu);
        return true;
    }

    private class ViewPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return list == null ? 0 : list.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = UIUtils.inflate(R.layout.local_viewpager_item);
            photoView = view.findViewById(R.id.photoview);
            Uri uri = Uri.parse(list.get(position));
            mAttacher = new PhotoViewAttacher(photoView);
            //加载大图会oom
            photoView.setImageURI(uri);
            mAttacher.update();
            //不能给photoview设置单击事件  不然没有效果
            mAttacher.setOnPhotoTapListener(new OnPhotoTapListener() {
                @Override
                public void onPhotoTap(ImageView view, float x, float y) {
                    toggleToolbar();
                }

            });

            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
            //释放不用的图片资源
            if (object instanceof PhotoView) {
                PhotoView s = (PhotoView) object;
                BitmapDrawable bitmapDrawable = (BitmapDrawable) s.getDrawable();
                if (bitmapDrawable != null) {
                    Bitmap bm = bitmapDrawable.getBitmap();
                    if (bm != null && !bm.isRecycled()) {
                        s.setImageResource(0);
                        bm.recycle();
                    }
                }
            }
        }

        //可以随时刷新viewpager
        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }
    }

    /**
     * 隐藏toolbar 注意根布局不能是LinearLayout，最好是relativelayout，不然隐藏后显示的是Activity的底色
     */
    private boolean isHiddened;
    private void toggleToolbar() {
        final float current = toolbar.getTranslationY();
        if (current == 0){
            isHiddened = false;
        }else {
            isHiddened = true;
        }
        ObjectAnimator translationY = ObjectAnimator
                .ofFloat(toolbar, "translationY", current,isHiddened ? 0 : -toolbar.getHeight());
        ObjectAnimator alpha = ObjectAnimator.ofFloat(toolbar, "alpha", isHiddened ? 0f:1.0f, isHiddened ? 1.0f:0f);
        AnimatorSet set = new AnimatorSet();
        set.play(translationY).with(alpha);
        set.setDuration(500);
        set.start();
        set.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                //toolbar显示与隐藏
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
    }

    private void setWallpaper() {

        //截取字符串，不需要file://
        String localUrl = list.get(mPosition).substring(6);
        Intent intent = new Intent(Intent.ACTION_ATTACH_DATA);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.putExtra("mimeType", "image/*");
        /*Uri uri = Uri.parse(MediaStore.Images.Media
                .insertImage(this.getContentResolver(),
                        ((BitmapDrawable) wallpaper).getBitmap(), null, null));
        intent.setData(uri);
        startActivityForResult(intent, SET_WALLPAPER);*/
        try {
            Uri uri = Uri.parse(MediaStore.Images.Media.insertImage(this.getContentResolver(),
                    localUrl, null, null));
            intent.setData(uri);
            startActivityForResult(intent, SET_WALLPAPER);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        // TODO: 18-7-28  上面的方法对小米等手机无效
        /*try {
            WallpaperManager wpm = (WallpaperManager) getSystemService(Context.WALLPAPER_SERVICE);
            if (wallpaper != null) {

                wpm.setBitmap(bitmap);
                Log.i("xzy", "wallpaper not null");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }

    /**
     * 删除图片
     */
    private void deleteWallpaper() {
        String localUrl = list.get(mPosition).substring(6);
        list.remove(mPosition);
        File file = new File(localUrl);
        file.delete();
        if (list.size() == 0){
            finish();
        }else {
            mAdapter.notifyDataSetChanged();
            viewPager.setCurrentItem(mPosition);

        }

        //通过rxbus发送消息通知订阅者更新数据
        RxBus.getInstance().post(new DeleteEvent(mPosition));
        Log.i("code", "发送删除数据了: ");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Log.i("返回值", "requestCode: "+requestCode+"  resultCode:"+resultCode);
        if (requestCode == 200) {
            //ToastUtils.show("设置成功");
        } else {
            //ToastUtils.show("设置失败");
        }
    }

    private void showDeleteDialog() {
        builder = new AlertDialog.Builder(this);
        builder.setTitle("提示");
        builder.setMessage("确定删除所选内容");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                /*Log.i("viewpager的相关信息", "viewPager.getChildCount(): "+viewPager.getChildCount()+"mPosition:"+mPosition);
                if (list.size() == mPosition+1){
                    viewPager.setCurrentItem(mPosition-1);
                }else {
                    viewPager.setCurrentItem(mPosition+1);
                }*/
                deleteWallpaper();
                dialogInterface.dismiss();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }

    public long getFileSize(File file) {
        try {
            if (file == null) {
                return 0;
            }
            long size = 0;
            if (file.exists()) {
                FileInputStream fis = null;
                fis = new FileInputStream(file);
                size = fis.available();
                fis.close();
            }
            return size;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
