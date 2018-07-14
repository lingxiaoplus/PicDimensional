package com.lingxiaosuse.picture.tudimension.view;

import com.camera.lingxiao.common.app.BaseView;
import com.lingxiaosuse.picture.tudimension.modle.CosplayDetailModel;
import com.lingxiaosuse.picture.tudimension.modle.CosplayModel;

public interface CosplayView extends BaseView{
    void onGetCosplayer(CosplayModel model);
    void onGetCosplayDetail(CosplayDetailModel model);
}
