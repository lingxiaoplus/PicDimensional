package com.lingxiaosuse.picture.tudimension.presenter;

import com.camera.lingxiao.common.app.BasePresenter;
import com.camera.lingxiao.common.retrofit.HttpRequest;
import com.lingxiaosuse.picture.tudimension.activity.SplashActivity;
import com.lingxiaosuse.picture.tudimension.view.SplashView;

import java.util.Map;

public class SplashPresenter extends BasePresenter<SplashView,SplashActivity>{
    //private final String TAG = PhoneAddressPresenter.class.getSimpleName();
    public SplashPresenter(SplashView view, SplashActivity activity) {
        super(view, activity);
    }

    /**
     * 检查更新
     */
    public void getVersion(){
        //构建请求数据
        //Map<String, Object> request = HttpRequest.Method
    }

    /**
     * 获取封面图片地址
     */
    public void getImgUrl(){

    }
}
