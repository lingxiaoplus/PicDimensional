package com.lingxiaosuse.picture.tudimension.transation;

import com.camera.lingxiao.common.app.BaseTransation;
import com.camera.lingxiao.common.app.ContentValue;
import com.camera.lingxiao.common.http.ParseHelper;
import com.camera.lingxiao.common.observer.HttpRxCallback;
import com.camera.lingxiao.common.retrofit.HttpRequest;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.lingxiaosuse.picture.tudimension.modle.CommentModle;
import com.trello.rxlifecycle2.LifecycleProvider;

public class CommentTrans extends BaseTransation{
    public CommentTrans(LifecycleProvider lifecycle) {
        super(lifecycle);
    }

    public void getCommentResult(String id, int limit, int skip, HttpRxCallback callback){
        request.clear();
        request.put(HttpRequest.API_URL, ContentValue.COMMENT_URL+"/"+id+"/comment");
        request.put("limit",limit);
        request.put("skip",skip);

        callback.setParseHelper(new ParseHelper() {
            @Override
            public Object[] parse(JsonElement jsonElement) {
                CommentModle bean = new Gson().fromJson(jsonElement, CommentModle.class);
                Object[] obj = new Object[1];
                obj[0] = bean;
                return obj;
            }
        });
        getRequest().request(HttpRequest.Method.GET,request,mLifecycle,callback);
    }
}
