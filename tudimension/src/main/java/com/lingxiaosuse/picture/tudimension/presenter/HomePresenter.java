package com.lingxiaosuse.picture.tudimension.presenter;

import com.camera.lingxiao.common.app.BaseFragment;
import com.camera.lingxiao.common.app.BasePresenter;
import com.camera.lingxiao.common.observer.HttpRxCallback;
import com.camera.lingxiao.common.utills.LogUtils;
import com.lingxiaosuse.picture.tudimension.fragment.HomeFragment;
import com.lingxiaosuse.picture.tudimension.modle.BannerModle;
import com.lingxiaosuse.picture.tudimension.modle.HomePageModle;
import com.lingxiaosuse.picture.tudimension.transation.HomeTrans;
import com.lingxiaosuse.picture.tudimension.view.HomeView;

public class HomePresenter extends BasePresenter<HomeView,BaseFragment>{
    private HomeTrans mTrans;
    public HomePresenter(HomeView view, BaseFragment activity) {
        super(view, activity);
        mTrans = new HomeTrans(getActivity());
    }

    public void getHomePageData(int limit,int skip){
        mTrans.getHomePage(limit, skip,new HttpRxCallback() {
            @Override
            public void onSuccess(Object... object) {
                if (getView() != null){
                    HomePageModle modle = (HomePageModle) object[0];
                    LogUtils.i("Homepresnter:   "+getView());
                    getView().onGetHomeResult(modle);
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

    public void getBannerDetailData(String id,int limit,int skip,String type,String order){
        mTrans.getBanner(id, limit, skip, false, type, order, new HttpRxCallback() {
            @Override
            public void onSuccess(Object... object) {
                BannerModle modle = (BannerModle) object[0];
                getView().onGetBannerResult(modle);
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
