package com.lingxiaosuse.picture.tudimension.presenter;

import com.camera.lingxiao.common.app.BasePresenter;
import com.camera.lingxiao.common.observer.HttpRxCallback;
import com.lingxiaosuse.picture.tudimension.activity.cosplay.CosplayDetailActivity;
import com.lingxiaosuse.picture.tudimension.modle.CosplayDetailModel;
import com.lingxiaosuse.picture.tudimension.transation.CosplayTrans;
import com.lingxiaosuse.picture.tudimension.view.CosplayView;

public class CosplayDetailPresenter extends BasePresenter<CosplayView,CosplayDetailActivity>{
    private CosplayTrans mTrans;
    public CosplayDetailPresenter(CosplayView view, CosplayDetailActivity activity) {
        super(view, activity);
        mTrans = new CosplayTrans(getActivity());
    }

    public void getCosplayDetailResult(int shareId){
        mTrans.getCosplayerDetail(shareId, new HttpRxCallback() {
            @Override
            public void onSuccess(Object... object) {
                CosplayDetailModel model = (CosplayDetailModel) object[0];
                getView().onGetCosplayDetail(model);
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
