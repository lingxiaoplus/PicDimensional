package com.camera.lingxiao.common.api;

import com.camera.lingxiao.common.http.response.HttpResponse;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

public interface UserApi {
    @GET("user/login")
    Observable<HttpResponse> login(@QueryMap Map<String,Object> request);
}
