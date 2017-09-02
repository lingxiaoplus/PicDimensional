package com.lingxiaosuse.picture.tudimension.retrofit;

import com.lingxiaosuse.picture.tudimension.modle.HomePageModle;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by lingxiao on 2017/8/3.
 */

public interface HomePageInterface {
    //http://service.picasso.adesk.com/v3/homepage?limit=30&skip=0&adult=false
    /**
     *@param limit 每次请求加载多少数据
     *@param skip 分页 0 30 60
     */
    @GET("/v3/homepage")
    Call<HomePageModle> homePageModle(
            @Query("limit") int limit,
            @Query("skip") int skip,
            @Query("adult") boolean adult
    );
}
