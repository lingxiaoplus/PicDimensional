package com.lingxiaosuse.picture.tudimension.fragment;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.camera.lingxiao.common.app.BaseFragment;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lingxiaosuse.picture.tudimension.R;
import com.lingxiaosuse.picture.tudimension.activity.BannerDetailActivity;
import com.lingxiaosuse.picture.tudimension.activity.CategoryDetailActivity;
import com.lingxiaosuse.picture.tudimension.adapter.BaseRecycleAdapter;
import com.lingxiaosuse.picture.tudimension.adapter.CategoryAdapter;
import com.lingxiaosuse.picture.tudimension.modle.CategoryModle;
import com.lingxiaosuse.picture.tudimension.presenter.CategoryPresenter;
import com.lingxiaosuse.picture.tudimension.utils.UIUtils;
import com.lingxiaosuse.picture.tudimension.view.CategoryView;
import com.lingxiaosuse.picture.tudimension.widget.SmartSkinRefreshLayout;

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
    SmartSkinRefreshLayout refreshLayout;

    private CategoryAdapter mCateAdapter;
    private GridLayoutManager mLayoutManager;
    private List<CategoryModle.CategoryBean> categoryList = new ArrayList<>();
    private CategoryPresenter mCategoryPresenter;

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_category;
    }

   /* @Override
    protected void initData() {
        mCategoryPresenter = new CategoryPresenter(this,this);
        mCategoryPresenter.getCategoryVertical();
    }*/

    @Override
    protected void onFirstVisiblity() {
        super.onFirstVisiblity();
        mCategoryPresenter = new CategoryPresenter(this,this);
        refreshLayout.autoRefresh();
    }

    @Override
    protected void initWidget(View root) {
        super.initWidget(root);

        refreshLayout.setOnRefreshListener((refreshLayout)->{
            categoryList.clear();
            mCategoryPresenter.getCategoryVertical();
        });

        mCateAdapter = new CategoryAdapter(R.layout.category_item,categoryList,true);
        recyclerView.setAdapter(mCateAdapter);
        mLayoutManager = new GridLayoutManager(getActivity(),2,
                LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(mLayoutManager);

        mCateAdapter.setOnItemClickListener((adapter, view, position) -> {
            Intent intent = new Intent(UIUtils.getContext(),
                    CategoryDetailActivity.class);
            intent.putExtra("url",categoryList.get(position).getCover());
            intent.putExtra("desc",categoryList.get(position).getName());
            intent.putExtra("id",categoryList.get(position).getId());
            intent.putExtra("title",categoryList.get(position).getName());
            intent.putExtra("type","category");  //说明类型是轮播图
            startActivity(intent);
        });
        mCateAdapter.setDuration(800);
        mCateAdapter.openLoadAnimation(view -> new Animator[]{
                ObjectAnimator.ofFloat(view, "scaleY", 0f, 1.05f, 1f),
                ObjectAnimator.ofFloat(view, "scaleX", 0f, 1.05f, 1f)
        });

    }

    @Override
    public void onGetCategoryResult(CategoryModle modle) {
        mCateAdapter.addData(modle.getCategory());
        refreshLayout.finishRefresh();
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
