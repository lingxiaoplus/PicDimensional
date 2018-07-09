package com.lingxiaosuse.picture.tudimension.presenter;

import com.camera.lingxiao.common.app.BasePresenter;
import com.camera.lingxiao.common.observer.HttpRxCallback;
import com.lingxiaosuse.picture.tudimension.fragment.HotFragment;
import com.lingxiaosuse.picture.tudimension.modle.HotModle;
import com.lingxiaosuse.picture.tudimension.transation.HotTrans;
import com.lingxiaosuse.picture.tudimension.view.HotView;

public class HotPresenter extends BasePresenter<HotView,HotFragment>{
    private HotTrans mTrans;
    public HotPresenter(HotView view, HotFragment activity) {
        super(view, activity);
        mTrans = new HotTrans(getActivity());
    }
    public void getHotResult(int current, int page){
        mTrans.getHotResult(current, page, new HttpRxCallback() {
            @Override
            public void onSuccess(Object... object) {
                if (getView() != null){
                    HotModle modle = (HotModle) object[0];
                    getView().onGetHotResult(modle);
                }
            }

            @Override
            public void onError(int code, String desc) {
                if (getView() != null){
                    getView().showToast(desc);
                }
            }

            @Override
            public void onCancel() {

            }
        });
    }
}
