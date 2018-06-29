package com.camera.lingxiao.common.http;


import com.camera.lingxiao.common.utills.LogUtils;
import com.google.gson.Gson;

import io.reactivex.functions.Function;

public class OtherServerFunction<T> implements Function<T,Object>{
    @Override
    public Object apply(T t) {
        //其他api..未规范
        String gson = new Gson().toJson(t);
        return gson;
    }
}
