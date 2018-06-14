package com.lingxiaosuse.picture.tudimension.presenter;

import com.camera.lingxiao.common.VersionModle;
import com.camera.lingxiao.common.app.BasePresenter;
import com.camera.lingxiao.common.http.ParseHelper;
import com.camera.lingxiao.common.observer.HttpRxCallback;
import com.camera.lingxiao.common.retrofit.HttpRequest;
import com.camera.lingxiao.common.utills.LogUtils;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.lingxiaosuse.picture.tudimension.activity.SplashActivity;
import com.lingxiaosuse.picture.tudimension.global.ContentValue;
import com.lingxiaosuse.picture.tudimension.transation.UpdateTransation;
import com.lingxiaosuse.picture.tudimension.utils.SpUtils;
import com.lingxiaosuse.picture.tudimension.utils.UIUtils;
import com.lingxiaosuse.picture.tudimension.view.SplashView;

import java.util.Map;
import java.util.TreeMap;

public class SplashPresenter extends BasePresenter<SplashView,SplashActivity>{

    //private final String TAG = PhoneAddressPresenter.class.getSimpleName();
    public SplashPresenter(SplashView view, SplashActivity activity) {
        super(view, activity);
    }
    /**
     * 检查更新
     */
    public void getVersion(){
        new UpdateTransation().getUpdate(getActivity(), new HttpRxCallback() {
            @Override
            public void onSuccess(Object... object) {
                VersionModle modle = (VersionModle) object[0];
                SpUtils.putInt(UIUtils.getContext(), ContentValue.VERSION_CODE, modle.getVersionCode());
                SpUtils.putString(UIUtils.getContext(), ContentValue.VERSION_DES, modle.getVersionDes());
                SpUtils.putString(UIUtils.getContext(), ContentValue.DOWNLOAD_URL, modle.getDownloadUrl());
                LogUtils.i("success presenter:  "+modle.toString());
            }

            @Override
            public void onError(int code, String desc) {
                LogUtils.i("error presenter:  "+code);
            }

            @Override
            public void onCancel() {

            }
        });
    }

    /**
     * 获取封面图片地址
     */
    public void getImgUrl(){

    }
}
