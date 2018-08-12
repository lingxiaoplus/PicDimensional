package com.lingxiaosuse.picture.tudimension.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.camera.lingxiao.common.app.BaseActivity;
import com.camera.lingxiao.common.app.ContentValue;
import com.lingxiaosuse.picture.tudimension.R;
import com.lingxiaosuse.picture.tudimension.fragment.BannerFragment;
import com.lingxiaosuse.picture.tudimension.fragment.CategoryVerticalFragment;
import com.lingxiaosuse.picture.tudimension.fragment.VerticalFragment;
import com.lingxiaosuse.picture.tudimension.utils.UIUtils;
import com.lingxiaosuse.picture.tudimension.widget.SkinTabLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

public class
BannerDetailActivity extends BaseActivity {
    @BindView(R.id.toolbar_title)
    Toolbar mTbToolbar;
    @BindView(R.id.tab_vertical)
    SkinTabLayout tabView;
    @BindView(R.id.pager_vertical)
    ViewPager viewPager;

    private String[] tabStr = new String[2];
    private PagerAdapter mPagerAdapter;
    private String id;
    private String title;
    private String type;
    private int total = 1;

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_banner_detail;
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        tabStr[0] = getResources().getString(R.string.tab_new);
        tabStr[1] = getResources().getString(R.string.tab_hot);
        try {
            String url = getIntent().getStringExtra("url");
            String message = getIntent().getStringExtra("desc");
            id = getIntent().getStringExtra("id");
            title = getIntent().getStringExtra("title");
            type = getIntent().getStringExtra("type");
            mTbToolbar.setTitle(title);
            setSupportActionBar(mTbToolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);

            if (type.equals(ContentValue.TYPE_ALBUM)){
                //从轮播图过来的
                //tabView.addTab(tabView.newTab().setText(tabStr[0]));
                tabView.setVisibility(View.GONE);
                total = 1;
            }else if (type.equals(ContentValue.TYPE_CATEGORY)){
                for (int i = 0; i < tabStr.length; i++) {
                    tabView.addTab(tabView.newTab().setText(tabStr[i]));
                }
                tabView.setupWithViewPager(viewPager);
                total = 2;
            }

            mPagerAdapter = new
                    PagerAdapter(getSupportFragmentManager());
            viewPager.setAdapter(mPagerAdapter);
            viewPager.setCurrentItem(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class PagerAdapter extends FragmentPagerAdapter {

        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return total;
        }

        @Override
        public Fragment getItem(int position) {
            BannerFragment fragment = new BannerFragment();
            Bundle bundle = new Bundle();
            if (position == 0) {
                bundle.putString("order", "new");
            } else if (position == 1) {
                bundle.putString("order", "hot");
            }
            bundle.putString("id",id);
            bundle.putString("type",type);
            fragment.setArguments(bundle);
            return fragment;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabStr[position];
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
}
