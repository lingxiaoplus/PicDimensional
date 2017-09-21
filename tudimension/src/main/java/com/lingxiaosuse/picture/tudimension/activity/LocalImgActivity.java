package com.lingxiaosuse.picture.tudimension.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.lingxiaosuse.picture.tudimension.R;
import com.lingxiaosuse.picture.tudimension.utils.UIUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

public class LocalImgActivity extends BaseActivity {
    @BindView(R.id.viewpager_local)
    ViewPager viewPager;
    @BindView(R.id.toolbar_local)
    Toolbar toolbar;
    private ArrayList<String> list;
    private int position;
    private PhotoViewAttacher mAttacher;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_img);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        list = intent.getStringArrayListExtra("list");
        position = intent.getIntExtra("position",0);

        ViewPagerAdapter mAdapter = new ViewPagerAdapter();
        viewPager.setAdapter(mAdapter);
        viewPager.setCurrentItem(position);

        setSupportActionBar(toolbar);
        ActionBar actionBar =  getSupportActionBar();
        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }
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
}
