package com.lingxiaosuse.picture.tudimension.presenter;

import com.camera.lingxiao.common.app.BaseActivity;
import com.camera.lingxiao.common.app.BasePresenter;
import com.camera.lingxiao.common.app.BaseView;
import com.camera.lingxiao.common.observer.HttpRxCallback;
import com.lingxiaosuse.picture.tudimension.modle.TuWanModle;
import com.lingxiaosuse.picture.tudimension.transation.GeneralTrans;
import com.lingxiaosuse.picture.tudimension.view.GeneralView;

public class GeneralPresenter extends BasePresenter<GeneralView, BaseActivity> {
    private GeneralTrans trans;
    public GeneralPresenter(GeneralView view, BaseActivity activity) {
        super(view, activity);
        trans = new GeneralTrans(getActivity());
    }

    public void getData(String url){
        trans.getData(url, new HttpRxCallback() {
            @Override
            public void onSuccess(Object... object) {
                TuWanModle modle = (TuWanModle) object[0];
                getView().onGetData(modle);
            }

            @Override
            public void onError(int code, String desc) {
                getView().showToast(desc);
            }

            @Override
            public void onCancel() {

            }
        });
    }
}
