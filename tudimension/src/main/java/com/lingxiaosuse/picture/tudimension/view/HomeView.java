package com.lingxiaosuse.picture.tudimension.view;

import com.camera.lingxiao.common.app.BaseView;
import com.lingxiaosuse.picture.tudimension.modle.BannerModle;
import com.lingxiaosuse.picture.tudimension.modle.HomePageModle;

import java.util.List;

public interface HomeView extends BaseView{

    void onGetBannerResult(BannerModle bannerModle);
    /**
     * 获取到homepage数据
     * @param homeModle
     */
    void onGetHomeResult(HomePageModle homeModle);
}
