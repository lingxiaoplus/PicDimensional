package com.lingxiaosuse.picture.tudimension.presenter;

import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.camera.lingxiao.common.app.BaseFragment;
import com.camera.lingxiao.common.app.BasePresenter;
import com.camera.lingxiao.common.observer.HttpRxCallback;
import com.camera.lingxiao.common.utills.LogUtils;
import com.google.gson.Gson;
import com.lingxiaosuse.picture.tudimension.db.NetCacheModel;
import com.lingxiaosuse.picture.tudimension.db.NetCacheModel_Table;
import com.lingxiaosuse.picture.tudimension.fragment.HomeFragment;
import com.lingxiaosuse.picture.tudimension.modle.BannerModle;
import com.lingxiaosuse.picture.tudimension.modle.HomePageModle;
import com.lingxiaosuse.picture.tudimension.transation.HomeTrans;
import com.lingxiaosuse.picture.tudimension.utils.StringUtils;
import com.lingxiaosuse.picture.tudimension.utils.ToastUtils;
import com.lingxiaosuse.picture.tudimension.view.HomeView;
import com.raizlabs.android.dbflow.sql.language.SQLite;

public class HomePresenter extends BasePresenter<HomeView,BaseFragment>{
    private HomeTrans mTrans;
    private final String TAG = HomePresenter.class.getSimpleName();
    public HomePresenter(HomeView view, BaseFragment activity) {
        super(view, activity);
        mTrans = new HomeTrans(getActivity());
    }

    public void getHomePageData(int limit,int skip){

        /*try {
            NetCacheModel model = SQLite.select().from(NetCacheModel.class).querySingle();
            if (model != null){
                if (StringUtils.isNotEmpty(model.homeData)){
                    getView().onGetHomeResult(new Gson().fromJson(model.homeData,HomePageModle.class));
                    return;
                }
            }
        }catch (SQLiteException e){
            e.printStackTrace();
        }*/

        mTrans.getHomePage(limit, skip,new HttpRxCallback() {
            @Override
            public void onSuccess(Object... object) {
                if (getView() != null){
                    HomePageModle modle = (HomePageModle) object[0];
                    LogUtils.i("Homepresnter:   "+getView());
                    getView().onGetHomeResult(modle);

                    /*NetCacheModel model = new NetCacheModel();
                    model.homeData = new Gson().toJson(model);
                    model.save();*/
                }else {
                    ToastUtils.show("getView为空");
                    Log.e(TAG,"getView为空");
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
