package com.lingxiaosuse.picture.tudimension;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.GsonBuilder;
import com.lingxiaosuse.picture.tudimension.activity.BaseActivity;
import com.lingxiaosuse.picture.tudimension.adapter.GridCategoryAdapter;
import com.lingxiaosuse.picture.tudimension.adapter.MyRecycleViewAdapter;
import com.lingxiaosuse.picture.tudimension.fragment.BaseFragment;
import com.lingxiaosuse.picture.tudimension.fragment.FragmentFactory;
import com.lingxiaosuse.picture.tudimension.modle.CategoryModle;
import com.lingxiaosuse.picture.tudimension.modle.HomePageModle;
import com.lingxiaosuse.picture.tudimension.retrofit.CategoryInterface;
import com.lingxiaosuse.picture.tudimension.retrofit.HomePageInterface;
import com.lingxiaosuse.picture.tudimension.utils.UIUtils;

import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends BaseActivity {
    @BindView(R.id.tab_main)
    TabLayout tabLayout;
    @BindView(R.id.vp_main)
    ViewPager viewPager;
    private String[] tabStr = new String[]{"推荐","分类","最新","专辑"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        //tabLayout = (TabLayout) findViewById(R.id.tab_main);
        initView();
    }

    private void initView() {
        for (int i = 0; i < tabStr.length; i++) {
            tabLayout.addTab(tabLayout.newTab().setText(tabStr[i]));
        }
        MainPageAdapter adapter = new MainPageAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                BaseFragment fragment = FragmentFactory
                        .createFragment(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    class MainPageAdapter extends FragmentPagerAdapter{
        public MainPageAdapter(FragmentManager fm) {
            super(fm);
        }
        @Override
        public int getCount() {
            return tabStr.length;
        }
        @Override
        public Fragment getItem(int position) {
            BaseFragment fragment = FragmentFactory.createFragment(position);
            return fragment;
        }
        @Override
        public CharSequence getPageTitle(int position) {
            return tabStr[position];
        }
    }


}
