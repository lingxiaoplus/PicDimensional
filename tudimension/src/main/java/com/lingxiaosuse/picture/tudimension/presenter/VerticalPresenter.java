package com.lingxiaosuse.picture.tudimension.presenter;

import com.camera.lingxiao.common.app.BasePresenter;
import com.camera.lingxiao.common.observer.HttpRxCallback;
import com.lingxiaosuse.picture.tudimension.fragment.VerticalFragment;
import com.lingxiaosuse.picture.tudimension.modle.VerticalModle;
import com.lingxiaosuse.picture.tudimension.transation.VerticalTrans;
import com.lingxiaosuse.picture.tudimension.view.VerticalView;

public class VerticalPresenter extends BasePresenter<VerticalView,VerticalFragment>{
    private VerticalTrans mTrans;
    public VerticalPresenter(VerticalView view, VerticalFragment activity) {
        super(view, activity);
        mTrans = new VerticalTrans(getActivity());
    }

    public void getVerticalData(int limit,int skip,String order){
        mTrans.getVertical(limit, skip, order, new HttpRxCallback() {
            @Override
            public void onSuccess(Object... object) {
                try {
                    VerticalModle modle = (VerticalModle) object[0];
                    getView().onGetVerticalResult(modle);
                }catch (Exception e){
                    e.printStackTrace();
                }
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
