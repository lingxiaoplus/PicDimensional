package com.lingxiaosuse.picture.tudimension.retrofit;

import com.lingxiaosuse.picture.tudimension.modle.CategoryDetailModle;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by lingxiao on 17-11-13.
 */

public interface CategoryDetailInterface {
    //http://service.picasso.adesk.com
    // /v1/vertical/category/{id}/vertical?limit=30&order=new
    @GET("/v1/vertical/category/{id}/vertical")
    Call<CategoryDetailModle> cateModle(
            @Path("id") String id,
            @Query("limit") int limit,
            @Query("skip") int skip,
            @Query("order") String order
    );
}
