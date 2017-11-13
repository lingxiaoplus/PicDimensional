package com.lingxiaosuse.picture.tudimension.retrofit;

import com.lingxiaosuse.picture.tudimension.modle.CategoryVerticalModle;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by lingxiao on 17-11-13.
 */

public interface CategoryVerticalInterface {
    //http://service.picasso.adesk.com/v1/vertical/category
    @GET("/v1/vertical/category")
    Call<CategoryVerticalModle> categoryModle();
}
