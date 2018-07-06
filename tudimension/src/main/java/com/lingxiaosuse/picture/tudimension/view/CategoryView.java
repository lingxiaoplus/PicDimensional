package com.lingxiaosuse.picture.tudimension.view;

import com.camera.lingxiao.common.app.BaseView;
import com.lingxiaosuse.picture.tudimension.modle.CategoryModle;
import com.lingxiaosuse.picture.tudimension.modle.CategoryVerticalModle;

public interface CategoryView extends BaseView{
    void onGetCategoryVerticalResult(CategoryVerticalModle modle);
    void onGetCategoryResult(CategoryModle modle);
}
