package com.lingxiaosuse.picture.tudimension.retrofit;

import com.lingxiaosuse.picture.tudimension.modle.VerticalModle;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by lingxiao on 17-11-9.
 */

public interface VerticalInterface {
    //v1/vertical/vertical?limit=30?adult=false&first=1&order=new
    /**
     * @param order 请求的类型new hot
     */
    @GET("/v1/vertical/vertical")
    Call<VerticalModle> verticalModle(
            @Query("limit") int limit,
            @Query("skip") int skip,
            @Query("adult") boolean adult,
            @Query("order") String order
    );
}
