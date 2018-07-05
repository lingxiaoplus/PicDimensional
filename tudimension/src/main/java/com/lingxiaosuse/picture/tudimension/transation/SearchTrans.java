package com.lingxiaosuse.picture.tudimension.transation;

import com.camera.lingxiao.common.app.BaseTransation;
import com.camera.lingxiao.common.app.ContentValue;
import com.camera.lingxiao.common.http.ParseHelper;
import com.camera.lingxiao.common.observer.HttpRxCallback;
import com.camera.lingxiao.common.retrofit.HttpRequest;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.lingxiaosuse.picture.tudimension.modle.SearchKeyword;
import com.lingxiaosuse.picture.tudimension.modle.SearchResultModle;
import com.trello.rxlifecycle2.LifecycleProvider;

public class SearchTrans extends BaseTransation{

    public SearchTrans(LifecycleProvider lifecycle) {
        super(lifecycle);
    }

    public void getSearchResult(String keyWord,int skip,HttpRxCallback callback){
        String url = ContentValue.SEARCH_URL+"/v1/search/all/resource/"+keyWord
                +"?version=181&channel=huawei&skip="+skip;
        request.clear();
        request.put(HttpRequest.API_URL,url);
        callback.setParseHelper(new ParseHelper() {
            @Override
            public Object[] parse(JsonElement jsonElement) {
                SearchResultModle bean = new Gson().fromJson(jsonElement, SearchResultModle.class);
                Object[] obj = new Object[1];
                obj[0] = bean;
                return obj;
            }
        });
        getRequest().request(HttpRequest.Method.GET,request,mLifecycle,callback);
    }

    public void getSearchKey(String url,HttpRxCallback callback){
        request.clear();
        request.put(HttpRequest.API_URL,url);
        callback.setParseHelper(new ParseHelper() {
            @Override
            public Object[] parse(JsonElement jsonElement) {
                SearchKeyword bean = new Gson().fromJson(jsonElement, SearchKeyword.class);
                Object[] obj = new Object[1];
                obj[0] = bean;
                return obj;
            }
        });
        getRequest().request(HttpRequest.Method.GET,request,mLifecycle,callback);
    }
}
