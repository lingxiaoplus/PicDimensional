package com.lingxiaosuse.picture.tudimension.fragment;

import android.view.View;
import android.widget.GridView;

import com.google.gson.GsonBuilder;
import com.lingxiaosuse.picture.tudimension.R;
import com.lingxiaosuse.picture.tudimension.adapter.GridCategoryAdapter;
import com.lingxiaosuse.picture.tudimension.modle.CategoryModle;
import com.lingxiaosuse.picture.tudimension.retrofit.CategoryInterface;
import com.lingxiaosuse.picture.tudimension.utils.UIUtils;

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
        View view = UIUtils.inflate(R.layout.pager_category);
        gridView = view.findViewById(R.id.gv_category);
        return view;
    }

    /**
     *从服务器上获取分类信息
     */
    private void getCategory(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://service.picasso.adesk.com")
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().create()))
                .build();
        CategoryInterface category = retrofit.create(CategoryInterface.class);
        Call<CategoryModle> call = category.categoryModle();
        call.enqueue(new Callback<CategoryModle>() {
            @Override
            public void onResponse(Call<CategoryModle> call, Response<CategoryModle> response) {
                categoryList = response.body().getRes().getCategory();
                mGridAdapter = new GridCategoryAdapter(categoryList);
                gridView.setAdapter(mGridAdapter);
                mGridAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<CategoryModle> call, Throwable t) {

            }
        });
    }
}
