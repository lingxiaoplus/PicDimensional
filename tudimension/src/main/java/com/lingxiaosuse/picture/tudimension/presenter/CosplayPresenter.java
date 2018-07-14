package com.lingxiaosuse.picture.tudimension.presenter;

import com.camera.lingxiao.common.app.BasePresenter;
import com.camera.lingxiao.common.observer.HttpRxCallback;
import com.lingxiaosuse.picture.tudimension.fragment.cosplay.CosplayFragment;
import com.lingxiaosuse.picture.tudimension.modle.CosplayModel;
import com.lingxiaosuse.picture.tudimension.transation.CosplayTrans;
import com.lingxiaosuse.picture.tudimension.view.CosplayView;

public class CosplayPresenter extends BasePresenter<CosplayView,CosplayFragment> {
    private CosplayTrans mCosplayTrans;
    public CosplayPresenter(CosplayView view, CosplayFragment activity) {
        super(view, activity);
        mCosplayTrans = new CosplayTrans(getActivity());
    }

    public void getCosplayer(int pSize,int order){
        mCosplayTrans.getCosplayer(pSize, order, new HttpRxCallback() {
            @Override
            public void onSuccess(Object... object) {
                CosplayModel model = (CosplayModel) object[0];
                getView().onGetCosplayer(model);
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
