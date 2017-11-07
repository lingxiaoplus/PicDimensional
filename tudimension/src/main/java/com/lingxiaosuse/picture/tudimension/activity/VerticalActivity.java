package com.lingxiaosuse.picture.tudimension.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lingxiaosuse.picture.tudimension.R;
import com.lingxiaosuse.picture.tudimension.utils.UIUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VerticalActivity extends BaseActivity {

    @BindView(R.id.toolbar_title)
    Toolbar toolbar;
    @BindView(R.id.tab_vertical)
    TabLayout tabVertical;
    @BindView(R.id.pager_vertical)
    ViewPager pagerVertical;

    private String[] strs = new String[]{"最新","最热","分类"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vertical);
        ButterKnife.bind(this);
        initView();

    }

    private void initView() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        for (int i = 0; i < strs.length; i++) {
            tabVertical.addTab(tabVertical.newTab().setText(strs[i]));
        }
        tabVertical.setupWithViewPager(pagerVertical);
        VerticalPagerAdapter mAdapter = new VerticalPagerAdapter();
        pagerVertical.setAdapter(mAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }

    private class VerticalPagerAdapter extends PagerAdapter{

        @Override
        public int getCount() {
            return strs.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = UIUtils.inflate(R.layout.vertical_pager_item);
            container.addView(view);

            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return strs[position];
        }

    }
}
