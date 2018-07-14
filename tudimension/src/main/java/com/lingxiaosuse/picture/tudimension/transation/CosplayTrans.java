package com.lingxiaosuse.picture.tudimension.transation;

import com.camera.lingxiao.common.api.UserApi;
import com.camera.lingxiao.common.app.BaseTransation;
import com.camera.lingxiao.common.app.ContentValue;
import com.camera.lingxiao.common.body.CosplayBody;
import com.camera.lingxiao.common.http.ParseHelper;
import com.camera.lingxiao.common.observable.HttpRxObservable;
import com.camera.lingxiao.common.observer.HttpRxCallback;
import com.camera.lingxiao.common.retrofit.HttpRequest;
import com.camera.lingxiao.common.utills.LogUtils;
import com.camera.lingxiao.common.utills.RetrofitUtil;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.lingxiaosuse.picture.tudimension.modle.CosplayDetailModel;
import com.lingxiaosuse.picture.tudimension.modle.CosplayModel;
import com.trello.rxlifecycle2.LifecycleProvider;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;

public class CosplayTrans extends BaseTransation{
    private Map<String,String> headerMap;
    private Map<String,Object> bodyMap;
    public CosplayTrans(LifecycleProvider lifecycle) {
        super(lifecycle);
        headerMap = new HashMap<>();
        bodyMap = new HashMap<>();
    }
    public void getCosplayer(int psize,int order,HttpRxCallback callback){
        headerMap.clear();
        bodyMap.clear();
        headerMap.put("RequestCode",ContentValue.COSPLAY_REQUEST_CODE);
        bodyMap.put("key","");
        bodyMap.put("p","1");
        bodyMap.put("psize",psize);
        bodyMap.put("order",order);
        callback.setParseHelper(new ParseHelper() {
            @Override
            public Object[] parse(JsonElement jsonElement) {
                CosplayModel bean = new Gson().fromJson(jsonElement, CosplayModel.class);
                Object[] obj = new Object[1];
                obj[0] = bean;
                return obj;
            }
        });
        Observable apiObservable;
        apiObservable = RetrofitUtil
                .get()
                .retrofit()
                .create(UserApi.class)
                .otherHeader(ContentValue.COSPLAY_LA_URL,headerMap,bodyMap);
        HttpRxObservable.getOtherObservable(apiObservable, mLifecycle, callback).subscribe(callback);
    }

    public void getCosplayerDetail(int shareid,HttpRxCallback callback){
        headerMap.clear();
        bodyMap.clear();
        headerMap.put("RequestCode",ContentValue.COSPLAY_REQUEST_CODE);
        bodyMap.put("shareid",shareid);

        callback.setParseHelper(new ParseHelper() {
            @Override
            public Object[] parse(JsonElement jsonElement) {
                CosplayDetailModel bean = new Gson().fromJson(jsonElement, CosplayDetailModel.class);
                Object[] obj = new Object[1];
                obj[0] = bean;
                return obj;
            }
        });
        Observable apiObservable;
        apiObservable = RetrofitUtil
                .get()
                .retrofit()
                .create(UserApi.class)
                .otherHeader(ContentValue.COSPLAY_LA_DETAIL_URL,headerMap,bodyMap);
        HttpRxObservable.getOtherObservable(apiObservable, mLifecycle, callback).subscribe(callback);
    }
}
