package com.lingxiaosuse.picture.tudimension.transation;

import com.camera.lingxiao.common.app.BaseTransation;
import com.camera.lingxiao.common.http.ParseHelper;
import com.camera.lingxiao.common.observer.HttpRxCallback;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.lingxiaosuse.picture.tudimension.modle.TuWanModle;
import com.trello.rxlifecycle2.LifecycleProvider;

public class GeneralTrans extends BaseTransation {

    public GeneralTrans(LifecycleProvider lifecycle) {
        super(lifecycle);
    }


    public void getData(String url, HttpRxCallback callback){
        request.clear();
        callback.setParseHelper(new ParseHelper() {
            @Override
            public Object[] parse(JsonElement element) {
                TuWanModle modle = new Gson().fromJson(element, TuWanModle.class);
                Object[] obj = new Object[1];
                obj[0] = modle;
                return obj;
            }
        });
        getRequest().requestOther(url,mLifecycle,callback);
    }
}
