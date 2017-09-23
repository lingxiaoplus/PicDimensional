package com.lingxiaosuse.picture.tudimension.retrofit;

import com.lingxiaosuse.picture.tudimension.modle.SearchKeyword;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by lingxiao on 2017/9/23.
 */

public interface SearchKeyInterface {
    @GET("/v1/push/keyword?versionCode=181&channel=huawei&first=0&adult=false")
    Call<SearchKeyword> searchModle();
}
