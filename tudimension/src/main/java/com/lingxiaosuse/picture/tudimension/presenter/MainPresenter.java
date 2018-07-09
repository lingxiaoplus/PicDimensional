package com.lingxiaosuse.picture.tudimension.presenter;

import android.net.Uri;

import com.camera.lingxiao.common.app.BasePresenter;
import com.camera.lingxiao.common.app.ContentValue;
import com.camera.lingxiao.common.observer.HttpRxCallback;
import com.google.gson.Gson;
import com.lingxiaosuse.picture.tudimension.MainActivity;
import com.lingxiaosuse.picture.tudimension.modle.HitokotoModle;
import com.lingxiaosuse.picture.tudimension.modle.HomePageModle;
import com.lingxiaosuse.picture.tudimension.transation.MainTrans;
import com.lingxiaosuse.picture.tudimension.utils.HttpUtils;
import com.lingxiaosuse.picture.tudimension.utils.UIUtils;
import com.lingxiaosuse.picture.tudimension.view.MainView;

import java.io.IOException;
import java.util.List;
import java.util.Random;

public class MainPresenter extends BasePresenter<MainView,MainActivity>{
    private MainTrans mTrans;
    public MainPresenter(MainView view, MainActivity activity) {
        super(view, activity);
        mTrans = new MainTrans(getActivity());
    }

    public void getHeadImg(){
        mTrans.getHeadImg(30, 0, new HttpRxCallback() {
            @Override
            public void onSuccess(Object... object) {
                if (getView() != null){
                    HomePageModle modle = (HomePageModle) object[0];
                    List<HomePageModle.Picture> picList =modle.getWallpaper();
                    Random random = new Random();
                    int result = random.nextInt(picList.size());
                    Uri uri = Uri.parse(picList.get(result).getPreview()+ ContentValue.bigImgRule);
                    getView().onGetHeadBackGround(uri);
                }
            }

            @Override
            public void onError(int code, String desc) {

            }

            @Override
            public void onCancel() {

            }
        });
    }

    /**
     * api不规范
     */
    public void getHeadText(){
        HttpUtils.doGet(ContentValue.HITOKOTO_URL, new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {

            }

            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                String result = response.body().string();
                Gson gson = new Gson();
                final HitokotoModle hotModle =
                        gson.fromJson(result,HitokotoModle.class);
                UIUtils.runOnUIThread(new Runnable() {
                    @Override
                    public void run() {
                        getView().onGetHeadText(hotModle.getHitokoto());
                    }
                });
            }
        });
    }
}
