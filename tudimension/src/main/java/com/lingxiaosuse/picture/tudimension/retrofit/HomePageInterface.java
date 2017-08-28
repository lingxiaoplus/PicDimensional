package com.lingxiaosuse.picture.tudimension.retrofit;

import com.lingxiaosuse.picture.tudimension.modle.HomePageModle;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by lingxiao on 2017/8/3.
 */

public interface HomePageInterface {
    //http://service.picasso.adesk.com/v3/homepage?limit=30&adult=false
    @GET("/v3/homepage")
    Call<HomePageModle> homePageModle(
            @Query("limit") int limit,
            @Query("adult") boolean adult
    );
}
