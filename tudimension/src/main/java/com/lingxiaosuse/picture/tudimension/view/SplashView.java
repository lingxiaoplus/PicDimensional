package com.lingxiaosuse.picture.tudimension.view;

import android.net.Uri;

import com.camera.lingxiao.common.VersionModle;
import com.camera.lingxiao.common.app.BaseView;

public interface SplashView extends BaseView{
    void showImgUrl(Uri uri,String error);
    void onVersionResult(VersionModle modle);
}
