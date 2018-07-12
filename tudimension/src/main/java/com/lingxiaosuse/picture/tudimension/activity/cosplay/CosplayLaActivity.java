package com.lingxiaosuse.picture.tudimension.activity.cosplay;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;

import com.camera.lingxiao.common.app.BaseActivity;
import com.lingxiaosuse.picture.tudimension.R;
import com.lingxiaosuse.picture.tudimension.fragment.mzitu.AllFragment;
import com.lingxiaosuse.picture.tudimension.fragment.mzitu.DailyFragment;
import com.lingxiaosuse.picture.tudimension.fragment.mzitu.MzituFragment;
import com.lingxiaosuse.picture.tudimension.widget.SkinTabLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CosplayLaActivity extends BaseActivity {

    @BindView(R.id.toolbar_title)
    Toolbar toolbarTitle;
    @BindView(R.id.tab_mzitu)
    SkinTabLayout tabMzitu;
    @BindView(R.id.pager_mzitu)
    ViewPager pagerMzitu;

    private String[] tabTitle;
    private PagerAdapter mAdapter;
    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_mzitu;
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        setToolbarBack(toolbarTitle);
        toolbarTitle.setTitle("CosplayLa");
        tabTitle = getResources().getStringArray(R.array.cosplayla_tab);
        for (int i = 0; i < tabTitle.length; i++) {
            tabMzitu.addTab(tabMzitu.newTab().setText(tabTitle[i]));
        }
        mAdapter = new PagerAdapter(getSupportFragmentManager());
        pagerMzitu.setAdapter(mAdapter);
        tabMzitu.setupWithViewPager(pagerMzitu);
    }

    @Override
    protected void initData() {
        super.initData();
    }

    private class PagerAdapter extends FragmentPagerAdapter {

        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return tabTitle.length;
        }

        @Override
        public Fragment getItem(int position) {
            MzituFragment fragment = new MzituFragment();
            Bundle bundle = new Bundle();
            if (position == 0) {
                bundle.putString("type", "");
            } else if (position == 1) {
                bundle.putString("type", "xinggan");
            } else if (position == 2) {
                bundle.putString("type", "japan");
            } else if (position == 3) {
                bundle.putString("type", "taiwan");
            } else if (position == 4) {
                bundle.putString("type", "mm");
            } else if (position == 5) {
                bundle.putString("type", "zipai");
                DailyFragment dailyFragment = new DailyFragment();
                dailyFragment.setArguments(bundle);
                return dailyFragment;
            } else if (position == 6) {
                bundle.putString("type", "all");
                AllFragment allFragment = new AllFragment();
                allFragment.setArguments(bundle);
                return allFragment;
            } else {
                bundle.putString("type", "");
            }
            fragment.setArguments(bundle);
            return fragment;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitle[position];
        }
    }
}
