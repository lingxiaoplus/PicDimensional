package com.lingxiaosuse.picture.tudimension.view;

import com.camera.lingxiao.common.app.BaseView;
import com.lingxiaosuse.picture.tudimension.modle.TuWanModle;

import java.util.List;

public interface GeneralView extends BaseView {
    void onGetCoverData(List<TuWanModle> modles);
    void onGetDetailData(List<TuWanModle> modles);
}
