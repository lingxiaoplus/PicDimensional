package com.camera.lingxiao.common.http;

import android.util.Log;

import com.camera.lingxiao.common.utills.LogUtils;
import com.camera.lingxiao.common.exception.ServerException;
import com.camera.lingxiao.common.http.response.HttpResponse;
import com.google.gson.Gson;

import io.reactivex.functions.Function;

/**
 * @author lingxiao
 * 数据的处理（截取数据）、数据类型转换（由图片path转为Bitmap）等
 * 配合map操作符
 */
public class ServerResultFunction implements Function<HttpResponse,Object> {
    private final String TAG = ServerResultFunction.class.getSimpleName();
    @Override
    public Object apply(HttpResponse httpResponse) {
        //打印服务器回传结果
        Log.e(TAG,"服务器回传结果: "+httpResponse.toString());
        if (!httpResponse.isSuccess()){
            throw new ServerException(httpResponse.getCode(),httpResponse.getMsg());
        }
        return new Gson().toJson(httpResponse.getResult());
    }

}
