package com.lingxiaosuse.picture.tudimension.presenter;

import com.camera.lingxiao.common.app.BasePresenter;
import com.camera.lingxiao.common.observer.HttpRxCallback;
import com.lingxiaosuse.picture.tudimension.activity.CommentActivity;
import com.lingxiaosuse.picture.tudimension.modle.CommentModle;
import com.lingxiaosuse.picture.tudimension.transation.CommentTrans;
import com.lingxiaosuse.picture.tudimension.view.CommentView;

public class CommentPresenter extends BasePresenter<CommentView,CommentActivity>{
    private CommentTrans mCommentTrans;
    public CommentPresenter(CommentView view, CommentActivity activity) {
        super(view, activity);
        mCommentTrans = new CommentTrans(getActivity());
    }
    public void getCommentResult(String id,int limit,int skip){
        mCommentTrans.getCommentResult(id, limit, skip, new HttpRxCallback() {
            @Override
            public void onSuccess(Object... object) {
                CommentModle modle = (CommentModle) object[0];
                getView().onGetCommentResult(modle);
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
