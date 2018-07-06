package com.lingxiaosuse.picture.tudimension.fragment;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.camera.lingxiao.common.app.BaseFragment;
import com.lingxiaosuse.picture.tudimension.R;
import com.lingxiaosuse.picture.tudimension.activity.BannerDetailActivity;
import com.lingxiaosuse.picture.tudimension.activity.CategoryDetailActivity;
import com.lingxiaosuse.picture.tudimension.adapter.BaseRecycleAdapter;
import com.lingxiaosuse.picture.tudimension.adapter.CategoryAdapter;
import com.lingxiaosuse.picture.tudimension.modle.CategoryModle;
import com.lingxiaosuse.picture.tudimension.modle.CategoryVerticalModle;
import com.lingxiaosuse.picture.tudimension.presenter.CategoryPresenter;
import com.lingxiaosuse.picture.tudimension.retrofit.CategoryVerticalInterface;
import com.lingxiaosuse.picture.tudimension.retrofit.RetrofitHelper;
import com.lingxiaosuse.picture.tudimension.utils.UIUtils;
import com.lingxiaosuse.picture.tudimension.view.CategoryView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by lingxiao on 17-11-13.
 */

public class CategoryVerticalFragment extends BaseFragment implements CategoryView{
    @BindView(R.id.rv_category)
    RecyclerView recyclerView;
    @BindView(R.id.swip_category)
    SwipeRefreshLayout refreshLayout;

    private CategoryAdapter mCateAdapter;
    private GridLayoutManager mLayoutManager;
    private List<CategoryVerticalModle.CategoryBean> categoryList = new ArrayList<>();
    private CategoryPresenter mCategoryPresenter = new CategoryPresenter(this,this);

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_category;
    }

    @Override
    protected void initData() {
        mCategoryPresenter.getCategoryVertical();
    }

    @Override
    protected void initWidget(View root) {
        super.initWidget(root);
        setSwipeColor(refreshLayout);
        refreshLayout.setRefreshing(true);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                categoryList.clear();
                mCategoryPresenter.getCategoryVertical();
            }
        });

        mCateAdapter = new CategoryAdapter(categoryList,0,0,true);
        recyclerView.setAdapter(mCateAdapter);
        mLayoutManager = new GridLayoutManager(getActivity(),2,
                LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(mLayoutManager);
        mCateAdapter.setOnItemClickListener(new BaseRecycleAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View View, int position) {
                Intent intent = new Intent(UIUtils.getContext(),
                        CategoryDetailActivity.class);
                intent.putExtra("url",categoryList.get(position).getCover());
                intent.putExtra("desc",categoryList.get(position).getName());
                intent.putExtra("id",categoryList.get(position).getId());
                intent.putExtra("title",categoryList.get(position).getName());
                intent.putExtra("type","category");  //说明类型是轮播图
                startActivity(intent);
            }
        });
    }

    @Override
    public void onGetCategoryVerticalResult(CategoryVerticalModle modle) {
        categoryList.addAll(modle.getCategory());
        mCateAdapter.notifyDataSetChanged();
        refreshLayout.setRefreshing(false);
    }

    @Override
    public void onGetCategoryResult(CategoryModle modle) {

    }

    @Override
    public void showDialog() {

    }

    @Override
    public void diamissDialog() {

    }

    @Override
    public void showToast(String text) {

    }
}
