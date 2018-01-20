package com.lingxiaosuse.picture.tudimension.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.lingxiaosuse.picture.tudimension.R;
import com.lingxiaosuse.picture.tudimension.adapter.MzituRecyclerAdapter;
import com.lingxiaosuse.picture.tudimension.fragment.CategoryVerticalFragment;
import com.lingxiaosuse.picture.tudimension.fragment.VerticalFragment;
import com.lingxiaosuse.picture.tudimension.fragment.mzitu.AllFragment;
import com.lingxiaosuse.picture.tudimension.fragment.mzitu.DailyFragment;
import com.lingxiaosuse.picture.tudimension.fragment.mzitu.MzituFragment;
import com.lingxiaosuse.picture.tudimension.global.ContentValue;
import com.lingxiaosuse.picture.tudimension.utils.UIUtils;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MzituActivity extends BaseActivity {

    @BindView(R.id.toolbar_title)
    Toolbar toolbarTitle;
    @BindView(R.id.tab_mzitu)
    TabLayout tabMzitu;
    @BindView(R.id.pager_mzitu)
    ViewPager pagerMzitu;

    private List<String> tabTitle = new ArrayList<>();
    private MzituPagerAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mzitu);
        ButterKnife.bind(this);
        setToolbarBack(toolbarTitle);
        toolbarTitle.setTitle("mzitu");
        initTab();
        initPager();
    }

    private void initPager() {
        mAdapter = new MzituPagerAdapter(getSupportFragmentManager());
        pagerMzitu.setAdapter(mAdapter);
    }

    private void initTab() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Connection connection = Jsoup.connect(ContentValue.MZITU_URL)
                        .header("Referer", "http://www.mzitu.com")
                        .header("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:48.0) Gecko/20100101 Firefox/48.0")
                        .timeout(5000)
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.108 Safari/537.36");//设置urer-agent  get();;

                Document doc = null;
                try {
                    Connection.Response response = connection.execute();
                    response.cookies();
                    doc = connection.get();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //获取tab的数据
                Element elementDiv = doc.getElementById("menu-nav");
                //Elements elementsUl = elementDiv.getElementsByTag("ul");
                Elements elements = elementDiv.getElementsByTag("li");
                for (Element element : elements) {
                    //Elements elements1 = element.children();
                    final String targetTitle = element.getElementsByTag("a").attr("title");
                    final String targetUrl = element.getElementsByTag("a").attr("href");
                    //ToastUtils.show(targetUrl);
                    tabTitle.add(targetTitle);
                    /*String img = elements1.get(0).getElementsByTag("img").first().attr("data-src");
                    if (img.contains(".jpg")) {
                        int a = img.indexOf(".jpg");
                        img = img.substring(0, a + 4);
                    }*/
                }
                UIUtils.runOnUIThread(new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 0; i < tabTitle.size(); i++) {
                            tabMzitu.addTab(tabMzitu.newTab().setText(tabTitle.get(i)));
                        }
                        mAdapter.notifyDataSetChanged();
                        tabMzitu.setupWithViewPager(pagerMzitu);
                    }
                });
            }

        }).start();

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
            } else if (position == 2){
                bundle.putString("type", "japan");
            }else if (position == 3){
                bundle.putString("type", "taiwan");
            }else if (position == 4){
                bundle.putString("type", "mm");
            }else if (position == 5){
                bundle.putString("type", "zipai");
                DailyFragment dailyFragment = new DailyFragment();
                dailyFragment.setArguments(bundle);
                return dailyFragment;
            }else if (position == 6){
                bundle.putString("type", "all");
                AllFragment allFragment = new AllFragment();
                allFragment.setArguments(bundle);
                return allFragment;
            }else {
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
