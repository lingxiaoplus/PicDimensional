package com.lingxiaosuse.picture.tudimension.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.camera.lingxiao.common.app.BaseActivity;
import com.lingxiaosuse.picture.tudimension.R;
import com.lingxiaosuse.picture.tudimension.fragment.CategoryVerticalFragment;
import com.lingxiaosuse.picture.tudimension.fragment.VerticalFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VerticalActivity extends BaseActivity {

    @BindView(R.id.toolbar_title)
    Toolbar toolbar;
    @BindView(R.id.tab_vertical)
    TabLayout tabVertical;
    @BindView(R.id.pager_vertical)
    ViewPager pagerVertical;
    @BindView(R.id.fab_vertical)
    FloatingActionButton fabVertical;
    private String[] strs = new String[]{"最新", "最热", "分类"};

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_vertical;
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        toolbar.setTitle("手机壁纸");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        for (int i = 0; i < strs.length; i++) {
            tabVertical.addTab(tabVertical.newTab().setText(strs[i]));
        }
        tabVertical.setupWithViewPager(pagerVertical);
        VerticalPagerAdapter mPagerAdapter = new
                VerticalPagerAdapter(getSupportFragmentManager());
        pagerVertical.setAdapter(mPagerAdapter);
        pagerVertical.setCurrentItem(0);
        //加载三个页面
        pagerVertical.setOffscreenPageLimit(3);
        fabVertical.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("code", "vertical的fab触摸了: ");
                if (fabListener != null){
                    //提供给fragment回调
                    fabListener.onTabClick();
                    Log.i("code", "fab事件传递过去了: ");
                }
            }
        });
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

    private class VerticalPagerAdapter extends FragmentPagerAdapter {

        public VerticalPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return strs.length;
        }

        @Override
        public Fragment getItem(int position) {
            VerticalFragment fragment = new VerticalFragment();
            Bundle bundle = new Bundle();
            if (position == 0) {
                bundle.putString("order", "new");
            } else if (position == 1) {
                bundle.putString("order", "hot");
            } else {
                bundle.putString("order", "category");
                return new CategoryVerticalFragment();
            }
            fragment.setArguments(bundle);
            return fragment;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return strs[position];
        }

    }

    private FabClickListener fabListener;
    public void setOnFabClick(FabClickListener listener){
        this.fabListener = listener;
    }
    public interface FabClickListener{
        void onTabClick();
    }
}
