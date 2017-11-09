package com.lingxiaosuse.picture.tudimension.activity;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lingxiaosuse.picture.tudimension.R;
import com.lingxiaosuse.picture.tudimension.adapter.VerticalAdapter;
import com.lingxiaosuse.picture.tudimension.modle.VerticalModle;
import com.lingxiaosuse.picture.tudimension.retrofit.RetrofitHelper;
import com.lingxiaosuse.picture.tudimension.retrofit.VerticalInterface;
import com.lingxiaosuse.picture.tudimension.utils.UIUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VerticalActivity extends BaseActivity {

    @BindView(R.id.toolbar_title)
    Toolbar toolbar;
    @BindView(R.id.tab_vertical)
    TabLayout tabVertical;
    @BindView(R.id.pager_vertical)
    ViewPager pagerVertical;

    private String[] strs = new String[]{"最新","最热","分类"};
    private VerticalAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vertical);
        ButterKnife.bind(this);
        initView();

        getDataFromServer();
    }

    private void getDataFromServer() {
        RetrofitHelper.getInstance(this)
                .getInterface(VerticalInterface.class)
                .verticalModle(30,0,false,"hot")
                .enqueue(new Callback<VerticalModle>() {
                    @Override
                    public void onResponse(Call<VerticalModle> call,
                                           Response<VerticalModle> response) {
                        List<VerticalModle.ResBean.VerticalBean> verticalBeanList =
                                response.body().getRes().getVertical();
                        mAdapter = new VerticalAdapter(verticalBeanList,0,1);
                    }

                    @Override
                    public void onFailure(Call<VerticalModle> call, Throwable t) {
                        Log.i("VerticalActivity", "onFailure: 获取手机壁纸json失败");
                    }
                });
    }

    private void initView() {
        toolbar.setTitle("手机壁纸");
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
            SwipeRefreshLayout refreshLayout =
                    view.findViewById(R.id.swip_vertical_item);
            RecyclerView recyclerView =
                    view.findViewById(R.id.recycle_vertical_item);
            GridLayoutManager manager = new GridLayoutManager(UIUtils.getContext(),
                    3, LinearLayoutManager.VERTICAL,false);
            recyclerView.setLayoutManager(manager);
            if (mAdapter != null){
                recyclerView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
            }
            container.addView(view);

            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                }

                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    if (dy >0){
                        //隐藏
                        toggleToolbar(true);
                    }else {
                        toggleToolbar(false);
                    }
                }
            });
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

    /**
     *隐藏toolbar
     */
    private void toggleToolbar(boolean hide) {
        //float current = toolbar.getTranslationY();

        ObjectAnimator animator = null;
        if (hide){
            animator = ObjectAnimator
                    .ofFloat(toolbar, "translationY",
                            -toolbar.getHeight());
        }else {
            animator = ObjectAnimator
                    .ofFloat(toolbar, "translationY",
                            0);
        }
        animator.start();
    }
}
