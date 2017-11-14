package com.lingxiaosuse.picture.tudimension.retrofit;

import com.lingxiaosuse.picture.tudimension.modle.FileUploadModle;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by lingxiao on 17-11-14.
 */

public interface FileUploadInterface {
    @Multipart
    @POST("/wiseshitu/a_upload?exif=1")
    Call<FileUploadModle> fileModle(
            @Part("name") RequestBody name,
            @Part  MultipartBody.Part file
            );
}
