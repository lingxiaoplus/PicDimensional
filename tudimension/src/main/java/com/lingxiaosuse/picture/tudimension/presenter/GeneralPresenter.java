package com.lingxiaosuse.picture.tudimension.presenter;

import android.util.Log;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.camera.lingxiao.common.app.BaseActivity;
import com.camera.lingxiao.common.app.BasePresenter;
import com.camera.lingxiao.common.app.BaseView;
import com.camera.lingxiao.common.observer.HttpRxCallback;
import com.lingxiaosuse.picture.tudimension.modle.TuWanModle;
import com.lingxiaosuse.picture.tudimension.transation.GeneralTrans;
import com.lingxiaosuse.picture.tudimension.view.GeneralView;

import java.util.ArrayList;
import java.util.List;

public class GeneralPresenter extends BasePresenter<GeneralView, BaseActivity> {
    private static final String TAG = GeneralPresenter.class.getSimpleName();
    public GeneralPresenter(GeneralView view, BaseActivity activity) {
        super(view, activity);
    }

    /**
     * 查询封面信息
     * @param limit
     * @param skip 页数
     */
    public void getCoverData(String tableName,int limit ,int skip){
        List<TuWanModle> imageList = new ArrayList<>();
        AVQuery<AVObject> avQuery = new AVQuery<>(tableName);
        avQuery.limit(limit)
                .skip(skip * 10)
                .findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if (e == null){
                    for (int i = 0; i < list.size(); i++) {
                        AVObject avObject = list.get(i);
                        String id = avObject.getString("id");
                        String title = avObject.getString("title");
                        String url = avObject.getString("url");
                        TuWanModle modle = new TuWanModle();
                        modle.setId(id);
                        modle.setTitle(title);
                        modle.setUrl(url);
                        imageList.add(modle);
                        Log.d(TAG,"获取到的id：" + id +"  获取到的title：" + title);
                    }
                    getView().onGetCoverData(imageList);
                }else {
                    getView().showToast(e.getMessage());
                }
            }
        });
    }

    public void getDetailData(String tableName,String key,String value){
        List<TuWanModle> imageList = new ArrayList<>();
        AVQuery<AVObject> detailQuery = new AVQuery<>(tableName);
        detailQuery.whereEqualTo(key,value)
                .findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if (e == null){
                    for (int i = 0; i < list.size(); i++) {
                        AVObject avObject = list.get(i);
                        String id = avObject.getObjectId();
                        String url = avObject.getString("url");
                        TuWanModle modle = new TuWanModle();
                        modle.setId(id);
                        modle.setUrl(url);
                        imageList.add(modle);
                    }
                    getView().onGetDetailData(imageList);
                }else {
                    getView().showToast(e.getMessage());
                }
            }
        });
    }
}
