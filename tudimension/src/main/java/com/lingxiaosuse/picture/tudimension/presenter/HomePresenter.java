package com.lingxiaosuse.picture.tudimension.presenter;

import com.camera.lingxiao.common.app.BasePresenter;
import com.camera.lingxiao.common.observer.HttpRxCallback;
import com.lingxiaosuse.picture.tudimension.fragment.HomeFragment;
import com.lingxiaosuse.picture.tudimension.modle.BannerModle;
import com.lingxiaosuse.picture.tudimension.modle.HomePageModle;
import com.lingxiaosuse.picture.tudimension.transation.HomeTrans;
import com.lingxiaosuse.picture.tudimension.view.HomeView;

public class HomePresenter extends BasePresenter<HomeView,HomeFragment>{
    private HomeTrans mTrans;
    public HomePresenter(HomeView view, HomeFragment activity) {
        super(view, activity);
        mTrans = new HomeTrans(getActivity());
    }

    private void getBannerData(String id,int limit,int skip,boolean adult,String type,String order){
        mTrans.getBanner(id, limit, skip, adult, type, order, new HttpRxCallback() {
            @Override
            public void onSuccess(Object... object) {
                HomePageModle modle = (HomePageModle) object[0];
                getView().onGetBannerResult(modle);
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
