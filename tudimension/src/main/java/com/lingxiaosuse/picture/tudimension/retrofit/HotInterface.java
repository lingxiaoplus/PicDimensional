package com.lingxiaosuse.picture.tudimension.retrofit;

import com.lingxiaosuse.picture.tudimension.modle.HotModle;

import retrofit2.Call;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by lingxiao on 2017/8/30.
 */

public interface HotInterface {
    /**
     * @param count 请求个数
     * @param index 第几页
     */
    @GET("%E7%A6%8F%E5%88%A9/{count}/{index}")
    Call<HotModle> hotModle(@Path("count") int count,
                            @Path("index") int index);
}
