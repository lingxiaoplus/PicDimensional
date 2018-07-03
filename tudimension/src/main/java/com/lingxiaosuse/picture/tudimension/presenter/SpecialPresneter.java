package com.lingxiaosuse.picture.tudimension.presenter;

import com.camera.lingxiao.common.app.BasePresenter;
import com.camera.lingxiao.common.observer.HttpRxCallback;
import com.lingxiaosuse.picture.tudimension.fragment.SpecialFragment;
import com.lingxiaosuse.picture.tudimension.modle.SpecialModle;
import com.lingxiaosuse.picture.tudimension.transation.SpecialTrans;
import com.lingxiaosuse.picture.tudimension.view.SpecialView;

public class SpecialPresneter extends BasePresenter<SpecialView,SpecialFragment>{
    private SpecialTrans mTrans;
    public SpecialPresneter(SpecialView view, SpecialFragment activity) {
        super(view, activity);
        mTrans = new SpecialTrans(getActivity());
    }

    public void getSpecialResult(int limit,int skip){
        mTrans.getSpecialResult(limit, skip, new HttpRxCallback() {
            @Override
            public void onSuccess(Object... object) {
                SpecialModle modle = (SpecialModle) object[0];
                getView().onGetSpecialData(modle);
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
