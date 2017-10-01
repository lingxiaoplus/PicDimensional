package com.lingxiaosuse.picture.tudimension.retrofit;

import com.lingxiaosuse.picture.tudimension.modle.SearchResultModle;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Url;

/**
 * Created by lingxiao on 2017/9/28.
 */

public interface SearchKeyResultInterface {
    //@GET("/v1/search/all/resource/{keyword}?version=181&channel=huawei&adult=false")
    @GET
    Call<SearchResultModle> searchResult(@Url String url);
    //Call<SearchResultModle> searchResult(@Url String url,@Path("keyword") String keyword);
}
