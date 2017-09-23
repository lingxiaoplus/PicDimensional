package com.lingxiaosuse.picture.tudimension.activity;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.lingxiaosuse.picture.tudimension.R;
import com.lingxiaosuse.picture.tudimension.utils.ToastUtils;
import com.lingxiaosuse.picture.tudimension.utils.UIUtils;
import com.lingxiaosuse.picture.tudimension.view.PhotoViewPager;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

public class LocalImgActivity extends BaseActivity {
    @BindView(R.id.viewpager_local)
    PhotoViewPager viewPager;
    @BindView(R.id.toolbar_local)
    Toolbar toolbar;
    private ArrayList<String> list;
    private int mPosition;
    private PhotoViewAttacher mAttacher;
    private static final int SET_WALLPAPER = 200;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_img);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        list = intent.getStringArrayListExtra("list");
        mPosition = intent.getIntExtra("position",0);

        ViewPagerAdapter mAdapter = new ViewPagerAdapter();
        viewPager.setAdapter(mAdapter);
        viewPager.setCurrentItem(mPosition);

        setToolbarBack(toolbar);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                mPosition = position;
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
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
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.local_img_menu,menu);
        return true;
    }

    private class ViewPagerAdapter extends PagerAdapter{

        @Override
        public int getCount() {
            return list==null?0:list.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = UIUtils.inflate(R.layout.local_viewpager_item);
            PhotoView photoView = view.findViewById(R.id.photoview);
            Uri uri = Uri.parse(list.get(position));
            mAttacher = new PhotoViewAttacher(photoView);
            photoView.setImageURI(uri);
            mAttacher.update();
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
    /**
     *隐藏toolbar
     */
    private void toggleToolbar() {
        float current = toolbar.getTranslationY();
        ObjectAnimator.ofFloat(toolbar, "translationY", current, current == 0 ? -toolbar.getHeight() : 0).start();
    }

    private void setWallpaper(){
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
                    localUrl,null,null));
            intent.setData(uri);
            startActivityForResult(intent, SET_WALLPAPER);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Log.i("返回值", "requestCode: "+requestCode+"  resultCode:"+resultCode);
        if (requestCode == 200){
            //ToastUtils.show("设置成功");
        }else {
            //ToastUtils.show("设置失败");
        }
    }
}
