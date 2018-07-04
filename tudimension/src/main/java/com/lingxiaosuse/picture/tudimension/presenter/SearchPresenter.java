package com.lingxiaosuse.picture.tudimension.presenter;

import com.camera.lingxiao.common.app.BasePresenter;
import com.camera.lingxiao.common.app.ContentValue;
import com.camera.lingxiao.common.observer.HttpRxCallback;
import com.lingxiaosuse.picture.tudimension.activity.SearchActivity;
import com.lingxiaosuse.picture.tudimension.modle.SearchKeyword;
import com.lingxiaosuse.picture.tudimension.modle.SearchResultModle;
import com.lingxiaosuse.picture.tudimension.transation.SearchTrans;
import com.lingxiaosuse.picture.tudimension.view.SearchView;

public class SearchPresenter extends BasePresenter<SearchView,SearchActivity>{
    private SearchTrans mSearchTrans;
    public SearchPresenter(SearchView view, SearchActivity activity) {
        super(view, activity);
        mSearchTrans = new SearchTrans(getActivity());
    }
    public void getSearchKey(){
        mSearchTrans.getSearchKey(ContentValue.SEARCH_KEY_URL, new HttpRxCallback() {
            @Override
            public void onSuccess(Object... object) {
                SearchKeyword modle = (SearchKeyword) object[0];
                getView().onGetSearchKeyWord(modle);
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

    public void getSearchWallResult(String keyWord,int skip){

        mSearchTrans.getSearchResult(keyWord, skip, new HttpRxCallback() {
            @Override
            public void onSuccess(Object... object) {
                SearchResultModle modle = (SearchResultModle) object[0];
                getView().onGetSearchResult(modle);
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
