package com.lingxiaosuse.picture.tudimension.presenter;

import com.camera.lingxiao.common.app.BaseFragment;
import com.camera.lingxiao.common.app.BasePresenter;
import com.camera.lingxiao.common.observer.HttpRxCallback;
import com.lingxiaosuse.picture.tudimension.modle.CategoryModle;
import com.lingxiaosuse.picture.tudimension.modle.CategoryVerticalModle;
import com.lingxiaosuse.picture.tudimension.transation.CategoryTrans;
import com.lingxiaosuse.picture.tudimension.view.CategoryView;

public class CategoryPresenter extends BasePresenter<CategoryView,BaseFragment>{
    private CategoryTrans mCategoryTrans;

    public CategoryPresenter(CategoryView view, BaseFragment activity) {
        super(view, activity);
        mCategoryTrans = new CategoryTrans(getActivity());
    }

    public void getCategoryVertical(){
        mCategoryTrans.getCategoryVertical(new HttpRxCallback() {
            @Override
            public void onSuccess(Object... object) {
                CategoryVerticalModle modle = (CategoryVerticalModle) object[0];
                getView().onGetCategoryVerticalResult(modle);
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

    public void getCategor(){
        mCategoryTrans.getCategory(new HttpRxCallback() {
            @Override
            public void onSuccess(Object... object) {
                CategoryModle modle = (CategoryModle) object[0];
                getView().onGetCategoryResult(modle);
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
