package com.lingxiaosuse.picture.tudimension.transation;

import com.camera.lingxiao.common.app.BaseTransation;
import com.camera.lingxiao.common.app.ContentValue;
import com.camera.lingxiao.common.http.ParseHelper;
import com.camera.lingxiao.common.observer.HttpRxCallback;
import com.camera.lingxiao.common.retrofit.HttpRequest;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.lingxiaosuse.picture.tudimension.modle.CategoryModle;
import com.lingxiaosuse.picture.tudimension.modle.CategoryVerticalModle;
import com.trello.rxlifecycle2.LifecycleProvider;

public class CategoryTrans extends BaseTransation{
    public CategoryTrans(LifecycleProvider lifecycle) {
        super(lifecycle);
    }
    public void getCategoryVertical(HttpRxCallback callback){
        request.clear();
        request.put(HttpRequest.API_URL, ContentValue.CATEGORY_VERTICAL_URL);
        callback.setParseHelper(new ParseHelper() {
            @Override
            public Object[] parse(JsonElement jsonElement) {
                CategoryVerticalModle bean = new Gson().fromJson(jsonElement, CategoryVerticalModle.class);
                Object[] obj = new Object[1];
                obj[0] = bean;
                return obj;
            }
        });
        getRequest().request(HttpRequest.Method.GET,request,mLifecycle,callback);
    }

    public void getCategory(HttpRxCallback callback){
        request.clear();
        request.put(HttpRequest.API_URL, ContentValue.CATEGORY_URL);
        callback.setParseHelper(new ParseHelper() {
            @Override
            public Object[] parse(JsonElement jsonElement) {
                CategoryModle bean = new Gson().fromJson(jsonElement, CategoryModle.class);
                Object[] obj = new Object[1];
                obj[0] = bean;
                return obj;
            }
        });
        getRequest().request(HttpRequest.Method.GET,request,mLifecycle,callback);
    }
}
