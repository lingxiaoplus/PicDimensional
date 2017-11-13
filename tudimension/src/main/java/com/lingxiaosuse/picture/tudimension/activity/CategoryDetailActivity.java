package com.lingxiaosuse.picture.tudimension.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.lingxiaosuse.picture.tudimension.R;
import com.lingxiaosuse.picture.tudimension.fragment.CategoryDetailFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CategoryDetailActivity extends BaseActivity {

    @BindView(R.id.toolbar_title)
    Toolbar toolbar;
    @BindView(R.id.tab_category)
    TabLayout tabCategory;
    @BindView(R.id.pager_category)
    ViewPager pagerCategory;
    @BindView(R.id.fab_category)
    FloatingActionButton fabCategory;
    private String[] strs = new String[]{"最新", "最热"};
    private String title = "";
    private String id;
    private CategoryAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_detail);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        title = intent.getStringExtra("title");
        id = intent.getStringExtra("id");
        initView();
    }

    private void initView() {
        toolbar.setTitle(title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        for (int i = 0; i < strs.length; i++) {
            tabCategory.addTab(tabCategory.newTab().setText(strs[i]));
        }

        tabCategory.setupWithViewPager(pagerCategory);
        mAdapter = new CategoryAdapter(getSupportFragmentManager());
        pagerCategory.setAdapter(mAdapter);
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

    private class CategoryAdapter extends FragmentPagerAdapter{

        public CategoryAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            CategoryDetailFragment fragment = new CategoryDetailFragment();
            Bundle bundle = new Bundle();
            bundle.putString("id",id);
            if (position == 0){
                bundle.putString("order","new");
            }else {
                bundle.putString("order","hot");
            }
            fragment.setArguments(bundle);
            return fragment;
        }

        @Override
        public int getCount() {
            return strs.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return strs[position];
        }
    }
}
