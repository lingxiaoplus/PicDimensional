package com.lingxiaosuse.picture.tudimension.activity.cosplay;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;

import com.camera.lingxiao.common.app.BaseActivity;
import com.lingxiaosuse.picture.tudimension.R;
import com.lingxiaosuse.picture.tudimension.fragment.cosplay.CosplayFragment;
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
            CosplayFragment fragment = new CosplayFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("order", position);
            fragment.setArguments(bundle);
            return fragment;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitle[position];
        }
    }
}
