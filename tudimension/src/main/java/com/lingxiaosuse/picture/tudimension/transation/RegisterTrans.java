package com.lingxiaosuse.picture.tudimension.transation;

import com.camera.lingxiao.common.app.BaseTransation;
import com.camera.lingxiao.common.observer.HttpRxCallback;
import com.lingxiaosuse.picture.tudimension.modle.IdentifyCodeModle;
import com.lingxiaosuse.picture.tudimension.sendcode.JsonReqClient;
import com.lingxiaosuse.picture.tudimension.utils.StringUtils;
import com.trello.rxlifecycle2.LifecycleProvider;

import java.util.Random;

public class RegisterTrans extends BaseTransation{

    private JsonReqClient client;
    private String sid = "79e97a4f75301edaec354149c09c2f7e";
    private String token = "f33f9435e36610e71c924d8b480eba55";
    private String appid = "a868bbc024f24b18b90a3ea922299252";
    private String templateid = "35082";
    private String uid = "";
    public RegisterTrans(LifecycleProvider lifecycle) {
        super(lifecycle);
        client = new JsonReqClient(lifecycle);
    }

    public void sendCode(String mobile, String code,HttpRxCallback callback){
        String param = code+",1";
        IdentifyCodeModle result =
                client.sendSms(sid, token, appid, templateid, param, mobile, uid,callback);
    }


}
