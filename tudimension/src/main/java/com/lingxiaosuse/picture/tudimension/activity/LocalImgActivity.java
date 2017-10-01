package com.lingxiaosuse.picture.tudimension.activity;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lingxiaosuse.picture.tudimension.R;
import com.lingxiaosuse.picture.tudimension.utils.StringUtils;
import com.lingxiaosuse.picture.tudimension.utils.ToastUtils;
import com.lingxiaosuse.picture.tudimension.utils.UIUtils;
import com.lingxiaosuse.picture.tudimension.view.PhotoViewPager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.DecimalFormat;
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
    private PhotoView photoView;
    private ViewPagerAdapter mAdapter;
    private AlertDialog.Builder builder = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_img);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        list = intent.getStringArrayListExtra("list");
        mPosition = intent.getIntExtra("position",0);

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
                file = null;
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
            case R.id.menu_delete:
                showDeleteDialog();
                break;
            case R.id.menu_share:
                String localUrl = list.get(mPosition).substring(6);
                File file = new File(localUrl);
                Intent shareImgIntent = new Intent(Intent.ACTION_SEND);
                shareImgIntent.setType("image/*");
                Uri u = Uri.fromFile(file);
                shareImgIntent.putExtra(Intent.EXTRA_STREAM, u);
                startActivity(shareImgIntent);
                file = null;
                break;
            case R.id.menu_info:
                showInfoDialog();
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
            photoView = view.findViewById(R.id.photoview);
            Uri uri = Uri.parse(list.get(position));
            mAttacher = new PhotoViewAttacher(photoView);
            photoView.setImageURI(uri);
            mAttacher.update();
            //不能给photoview设置单击事件  不然没有效果
            mAttacher.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
                @Override
                public void onPhotoTap(View view, float v, float v1) {
                    toggleToolbar();
                }

                @Override
                public void onOutsidePhotoTap() {

                }
            });
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
        ObjectAnimator animator = ObjectAnimator
                .ofFloat(toolbar, "translationY", current, current == 0 ? -toolbar.getHeight() : 0);
        animator.start();
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
    }

    private void setWallpaper(){
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
                    localUrl,null,null));
            intent.setData(uri);
            startActivityForResult(intent, SET_WALLPAPER);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    /**
     *删除图片
     */
    private void deleteWallpaper(){
        String localUrl = list.get(mPosition).substring(6);
        list.remove(mPosition);
        File file = new File(localUrl);
        file.delete();
        mAdapter.notifyDataSetChanged();
        viewPager.setCurrentItem(mPosition);
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
    private void showDeleteDialog(){
        builder = new AlertDialog.Builder(this);
        builder.setTitle("提示");
        builder.setMessage("确定删除所选内容");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Log.i("viewpager的相关信息", "viewPager.getChildCount(): "+viewPager.getChildCount()+"mPosition:"+mPosition);
                if (list.size() == mPosition+1){
                    viewPager.setCurrentItem(mPosition-1);
                }else {
                    viewPager.setCurrentItem(mPosition+1);
                }
                //deleteWallpaper();
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
    private void showInfoDialog(){
        String localUrl = list.get(mPosition).substring(6);
        File file = new File(localUrl);
        builder = new AlertDialog.Builder(this);
        final AlertDialog dialog = builder.create();
        View view = UIUtils.inflate(R.layout.dialog_img_info);
        dialog.setView(view);
        TextView title = view.findViewById(R.id.tv_info_title);
        TextView time = view.findViewById(R.id.tv_info_time);
        TextView path = view.findViewById(R.id.tv_info_path);
        TextView size = view.findViewById(R.id.tv_info_size);
        Button button = view.findViewById(R.id.bt_info);
        try {
        title.setText("名称："+file.getName());
        time.setText("时间："+StringUtils.longToDate(file.lastModified(),"yyyy-MM-dd HH:mm:ss"));
        path.setText("路径："+file.getAbsolutePath());
            size.setText("文件大小："+ StringUtils.getDataSize(getFileSize(file)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });
        dialog.show();
        file = null;
    }

    public long getFileSize(File file) throws Exception{
        if (file == null){
            return 0;
        }
        long size = 0;
        if (file.exists()){
            FileInputStream fis = null;
            fis = new FileInputStream(file);
            size = fis.available();
            fis.close();
        }
        return size;
    }
}
