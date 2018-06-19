package com.lingxiaosuse.picture.tudimension.view;

import com.camera.lingxiao.common.app.BaseView;
import com.lingxiaosuse.picture.tudimension.modle.BannerModle;

import java.util.List;

public interface HomeView extends BaseView{
    /**
     * 获取到顶部轮播图数据
     * @param banners
     */
    void onGetBannerResult(List<BannerModle> banners,String msg);

    void onGetHomeResult();
}
