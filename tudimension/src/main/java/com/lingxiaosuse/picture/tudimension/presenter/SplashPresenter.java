package com.lingxiaosuse.picture.tudimension.presenter;

import android.net.Uri;

import com.camera.lingxiao.common.VersionModle;
import com.camera.lingxiao.common.app.BasePresenter;
import com.camera.lingxiao.common.observer.HttpRxCallback;
import com.camera.lingxiao.common.utills.LogUtils;
import com.lingxiaosuse.picture.tudimension.activity.SplashActivity;
import com.lingxiaosuse.picture.tudimension.global.ContentValue;
import com.lingxiaosuse.picture.tudimension.modle.VerticalModle;
import com.lingxiaosuse.picture.tudimension.transation.UpdateTrans;
import com.lingxiaosuse.picture.tudimension.utils.SpUtils;
import com.lingxiaosuse.picture.tudimension.utils.UIUtils;
import com.lingxiaosuse.picture.tudimension.view.SplashView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SplashPresenter extends BasePresenter<SplashView,SplashActivity>{

    //private final String TAG = PhoneAddressPresenter.class.getSimpleName();
    private UpdateTrans transation;
    private List<VerticalModle.VerticalBean> resultList = new ArrayList<>();
    public SplashPresenter(SplashView view, SplashActivity activity) {
        super(view, activity);
        transation = new UpdateTrans(getActivity());
    }
    /**
     * 检查更新
     */
    public void getVersion(){
        transation.getUpdate(new HttpRxCallback() {
            @Override
            public void onSuccess(Object... object) {
                try {
                    VersionModle modle = (VersionModle) object[0];
                    SpUtils.putInt(UIUtils.getContext(), ContentValue.VERSION_CODE, modle.getVersionCode());
                    SpUtils.putString(UIUtils.getContext(), ContentValue.VERSION_DES, modle.getVersionDes());
                    SpUtils.putString(UIUtils.getContext(), ContentValue.DOWNLOAD_URL, modle.getDownloadUrl());
                    LogUtils.i("success presenter:  "+modle.toString());
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(int code, String desc) {
                getView().showToast(desc);
                LogUtils.i("error presenter:  "+code);
            }

            @Override
            public void onCancel() {

            }
        });
    }

    /**
     * 获取封面图片地址
     */
    public void getImgUrl(){
        transation.getSplashImgUrl(new HttpRxCallback() {
            @Override
            public void onSuccess(Object... object) {
                try {
                    VerticalModle modle = (VerticalModle) object[0];
                    resultList = modle.getVertical();
                    Random random = new Random();
                    int result = random.nextInt(resultList.size());
                    Uri uri = Uri.parse(resultList.get(result).getImg());
                    getView().showImgUrl(uri,null);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(int code, String desc) {
                getView().showToast(desc);
                getView().showImgUrl(null,null);
            }

            @Override
            public void onCancel() {

            }
        });
    }
}
