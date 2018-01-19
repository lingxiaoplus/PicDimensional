package com.lingxiaosuse.picture.tudimension.retrofit;

import com.lingxiaosuse.picture.tudimension.modle.BannerModle;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by lingxiao on 2017/9/1.
 */

public interface BannerInterface {
    /**
     *首页轮播图
     */
    @GET("/v1/wallpaper/{category}/{id}/wallpaper")
    Call<BannerModle> bannerModle(
            @Path("category") String category,
            @Path("id") String id,
            @Query("limit") int limit,
            @Query("skip") int skip,
            //@Query("first") int first,
            @Query("adult") boolean adult,
            @Query("order") String order
    );
}
