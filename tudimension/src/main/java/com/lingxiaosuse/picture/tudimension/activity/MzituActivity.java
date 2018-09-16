package com.lingxiaosuse.picture.tudimension.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.camera.lingxiao.common.app.BaseActivity;
import com.camera.lingxiao.common.app.ContentValue;
import com.camera.lingxiao.common.utills.LogUtils;
import com.lingxiaosuse.picture.tudimension.R;
import com.lingxiaosuse.picture.tudimension.db.MzituTabModel;
import com.lingxiaosuse.picture.tudimension.db.MzituTabModel_Table;
import com.lingxiaosuse.picture.tudimension.fragment.mzitu.AllFragment;
import com.lingxiaosuse.picture.tudimension.fragment.mzitu.DailyFragment;
import com.lingxiaosuse.picture.tudimension.fragment.mzitu.MzituFragment;
import com.lingxiaosuse.picture.tudimension.utils.UIUtils;
import com.lingxiaosuse.picture.tudimension.widget.SkinTabLayout;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MzituActivity extends BaseActivity {

    @BindView(R.id.toolbar_title)
    Toolbar toolbarTitle;
    @BindView(R.id.tab_mzitu)
    SkinTabLayout tabMzitu;
    @BindView(R.id.pager_mzitu)
    ViewPager pagerMzitu;

    private List<String> tabTitle = new ArrayList<>();
    private MzituPagerAdapter mAdapter;
    private Runnable mTabRunnable = new Runnable() {
        @Override
        public void run() {
            Connection connection = Jsoup.connect(ContentValue.MZITU_URL)
                    .header("Referer", ContentValue.MZITU_URL)
                    .header("User-Agent", ContentValue.USER_AGENT)
                    .timeout(5000)
                    .userAgent(ContentValue.USER_AGENT);//设置urer-agent  get();;
            Document doc = null;
            try {
                Connection.Response response = connection.execute();
                response.cookies();
                doc = connection.get();
                //获取tab的数据
                Element elementDiv = doc.getElementById("menu-nav");
                //Elements elementsUl = elementDiv.getElementsByTag("ul");
                Elements elements = elementDiv.getElementsByTag("li");
                for (Element element : elements) {
                    String targetTitle = element.getElementsByTag("a").attr("title");
                    String targetUrl = element.getElementsByTag("a").attr("href");
                    tabTitle.add(targetTitle);
                    MzituTabModel model = new MzituTabModel();
                    model.setTabName(targetTitle);
                    model.setCreateTime(System.currentTimeMillis());
                    model.save();
                    LogUtils.e("Connection: "+targetTitle);
                    UIUtils.runOnUIThread(new Runnable() {
                        @Override
                        public void run() {
                            //这里调用；两次是某些奇葩机器会报异常
                            mAdapter.notifyDataSetChanged();
                            for (int i = 0; i < tabTitle.size(); i++) {
                                tabMzitu.addTab(tabMzitu.newTab().setText(tabTitle.get(i)));
                            }
                            mAdapter.notifyDataSetChanged();
                        }
                    });

                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    };
    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_mzitu;
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        setToolbarBack(toolbarTitle);
        toolbarTitle.setTitle("mzitu");
        initTab();
    }

    @Override
    protected void initData() {
        super.initData();
    }

    private void initTab() {
        mAdapter = new MzituPagerAdapter(getSupportFragmentManager());
        pagerMzitu.setAdapter(mAdapter);
        tabMzitu.setupWithViewPager(pagerMzitu);
        List<MzituTabModel> tabList = SQLite.select()
                .from(MzituTabModel.class)
                .queryList();
        if (tabList.size() > 0){
            //3天缓存
            if (System.currentTimeMillis() - tabList.get(0).getCreateTime()
                    < 3 * 1000 * 60 * 60 * 24){
                for (int i = 0; i < tabList.size(); i++) {
                    tabTitle.add(tabList.get(i).getTabName());
                    tabMzitu.addTab(tabMzitu.newTab().setText(tabList.get(i).getTabName()));
                    LogUtils.e(tabList.get(i).getTabName());
                }
                mAdapter.notifyDataSetChanged();

            }else {
                SQLite.delete(MzituTabModel.class).execute();
                new Thread(mTabRunnable).start();
            }
        }else {
            new Thread(mTabRunnable).start();
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

    private class MzituPagerAdapter extends FragmentPagerAdapter {

        public MzituPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return tabTitle.size();
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
            return tabTitle.get(position);
        }
    }
}
