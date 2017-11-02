package com.lingxiaosuse.picture.tudimension.fragment;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.google.gson.GsonBuilder;
import com.lingxiaosuse.picture.tudimension.R;
import com.lingxiaosuse.picture.tudimension.activity.BannerDetailActivity;
import com.lingxiaosuse.picture.tudimension.activity.CategoryActivity;
import com.lingxiaosuse.picture.tudimension.activity.ImageLoadingActivity;
import com.lingxiaosuse.picture.tudimension.adapter.GridCategoryAdapter;
import com.lingxiaosuse.picture.tudimension.global.ContentValue;
import com.lingxiaosuse.picture.tudimension.modle.CategoryModle;
import com.lingxiaosuse.picture.tudimension.retrofit.CategoryInterface;
import com.lingxiaosuse.picture.tudimension.retrofit.RetrofitHelper;
import com.lingxiaosuse.picture.tudimension.utils.UIUtils;
import com.liuguangqiang.cookie.CookieBar;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by lingxiao on 2017/8/28.
 */

public class CategoryFragment extends BaseFragment{
    private GridView gridView;
    private List<CategoryModle.ResBean.CategoryBean> categoryList = new ArrayList<>();
    private GridCategoryAdapter mGridAdapter;
    @Override
    protected void initData() {
        getCategory();
    }
    @Override
    public View initView() {
        View view = UIUtils.inflate(R.layout.fragment_category);
        gridView = view.findViewById(R.id.gv_category);
        return view;
    }

    @Override
    public RecyclerView getRecycle() {
        return null;
    }

    /**
     *从服务器上获取分类信息
     */
    private void getCategory(){
        RetrofitHelper.getInstance(UIUtils.getContext())
                .getInterface(CategoryInterface.class)
                .categoryModle()
                .enqueue(new Callback<CategoryModle>() {
                    @Override
                    public void onResponse(Call<CategoryModle> call, Response<CategoryModle> response) {
                        categoryList = response.body().getRes().getCategory();
                        mGridAdapter = new GridCategoryAdapter(categoryList);
                        gridView.setAdapter(mGridAdapter);
                        mGridAdapter.notifyDataSetChanged();
                        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                                /*Intent intent = new Intent(UIUtils.getContext(),CategoryActivity.class);
                                startActivity(intent);*/
                                Intent intent = new Intent(UIUtils.getContext(),
                                        BannerDetailActivity.class);
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
                    public void onFailure(Call<CategoryModle> call, Throwable t) {

                    }
                });
    }
}
