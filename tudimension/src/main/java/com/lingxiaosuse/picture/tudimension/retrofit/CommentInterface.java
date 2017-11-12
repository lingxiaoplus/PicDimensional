package com.lingxiaosuse.picture.tudimension.retrofit;

import com.lingxiaosuse.picture.tudimension.modle.CommentModle;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by lingxiao on 17-11-6.
 */

public interface CommentInterface {
    @GET("/v2/wallpaper/wallpaper/{id}/comment")
    Call<CommentModle> commentModle(
            @Path("id") String id,
            @Query("limit") int limit,
            @Query("skip") int skip
    );
}
