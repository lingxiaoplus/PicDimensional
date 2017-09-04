package com.lingxiaosuse.picture.tudimension.retrofit;

import com.lingxiaosuse.picture.tudimension.modle.SpecialModle;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by lingxiao on 2017/9/4.
 */

public interface SpecialInterface {
    /**
     *   v1/wallpaper/album?limit=30&skip=0&adult=false
     *
     */
    @GET("/v1/wallpaper/album")
    Call<SpecialModle> specialModle(
            @Query("limit") int limit,
            @Query("skip") int skip,
            @Query("adult") boolean adult
    );
}
