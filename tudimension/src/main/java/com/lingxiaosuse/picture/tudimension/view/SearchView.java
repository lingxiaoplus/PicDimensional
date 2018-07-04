package com.lingxiaosuse.picture.tudimension.view;

import com.camera.lingxiao.common.app.BaseView;
import com.lingxiaosuse.picture.tudimension.modle.SearchKeyword;
import com.lingxiaosuse.picture.tudimension.modle.SearchResultModle;

public interface SearchView extends BaseView{
    void onGetSearchResult(SearchResultModle modle);
    void onGetSearchKeyWord(SearchKeyword keyword);
}
