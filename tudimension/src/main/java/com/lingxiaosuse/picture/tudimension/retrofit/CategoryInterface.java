package com.lingxiaosuse.picture.tudimension.retrofit;

import com.lingxiaosuse.picture.tudimension.modle.CategoryModle;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by lingxiao on 2017/8/28.
 */

public interface CategoryInterface {
    //http://service.picasso.adesk.com/v1/wallpaper/category
    @GET("/v1/wallpaper/category")
    Call<CategoryModle> categoryModle();
}
