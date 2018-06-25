package com.camera.lingxiao.common.api;

import com.camera.lingxiao.common.http.response.HttpResponse;

import java.util.Map;
import java.util.TreeMap;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

public interface UserApi {
    /**
     * GET请求
     *
     * @param url     api接口url
     * @param request 请求参数map
     * @return
     */
    @GET
    Observable<HttpResponse> get(@Url String url, @QueryMap TreeMap<String, Object> request);

    /**
     * POST请求
     *
     * @param url     api接口url
     * @param request 请求参数map
     * @return
     */
    @FormUrlEncoded
    @POST
    Observable<HttpResponse> post(@Url String url, @FieldMap TreeMap<String, Object> request);

    /**
     *首页轮播图
     * /v1/wallpaper/album/{id}/wallpaper/limit/skip/adult/order
     */
    @GET("/{version}/{wallpaper}/{category}/{id}/{wallpapertype}")
    Observable<HttpResponse> desk(
            @Path("version") String version,
            @Path("wallpaper") String wallpaper,
            @Path("category") String category,
            @Path("id") String id,
            @Path("wallpapertype") String wallpapertype,
            @QueryMap TreeMap<String, Object> request
    );
}
