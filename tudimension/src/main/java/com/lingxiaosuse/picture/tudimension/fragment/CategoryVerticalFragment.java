package com.lingxiaosuse.picture.tudimension.fragment;

import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.lingxiaosuse.picture.tudimension.R;
import com.lingxiaosuse.picture.tudimension.activity.BannerDetailActivity;
import com.lingxiaosuse.picture.tudimension.activity.CategoryDetailActivity;
import com.lingxiaosuse.picture.tudimension.adapter.BaseRecycleAdapter;
import com.lingxiaosuse.picture.tudimension.adapter.CategoryAdapter;
import com.lingxiaosuse.picture.tudimension.modle.CategoryModle;
import com.lingxiaosuse.picture.tudimension.modle.CategoryVerticalModle;
import com.lingxiaosuse.picture.tudimension.retrofit.CategoryVerticalInterface;
import com.lingxiaosuse.picture.tudimension.retrofit.RetrofitHelper;
import com.lingxiaosuse.picture.tudimension.utils.UIUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by lingxiao on 17-11-13.
 */

public class CategoryVerticalFragment extends BaseFragment{
    private RecyclerView recyclerView;
    private List<CategoryVerticalModle.ResBean.CategoryBean> categoryList = new ArrayList<>();
    private CategoryAdapter mCateAdapter;
    private GridLayoutManager mLayoutManager;
    @Override
    protected void initData() {
        getCategory();
    }


    @Override
    public View initView() {
        View view = UIUtils.inflate(R.layout.fragment_category);
        recyclerView = view.findViewById(R.id.rv_category);
        return view;
    }

    @Override
    public RecyclerView getRecycle() {
        return null;
    }

    private void getCategory() {
        RetrofitHelper
                .getInstance(UIUtils.getContext())
                .getInterface(CategoryVerticalInterface.class)
                .categoryModle()
                .enqueue(new Callback<CategoryVerticalModle>() {
                    @Override
                    public void onResponse(Call<CategoryVerticalModle> call, Response<CategoryVerticalModle> response) {
                        categoryList = response.body().getRes().getCategory();
                        mCateAdapter = new CategoryAdapter(categoryList,0,0,true);
                        recyclerView.setAdapter(mCateAdapter);
                        mLayoutManager = new GridLayoutManager(getActivity(),2,
                                LinearLayoutManager.VERTICAL,false);
                        recyclerView.setLayoutManager(mLayoutManager);
                        mCateAdapter.notifyDataSetChanged();

                        mCateAdapter.setOnItemClickListener(new BaseRecycleAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(View View, int position) {
                                /*Intent intent = new Intent(UIUtils.getContext(),CategoryDetailActivity.class);
                                startActivity(intent);*/
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
                    public void onFailure(Call<CategoryVerticalModle> call, Throwable t) {

                    }
                });
    }

}
