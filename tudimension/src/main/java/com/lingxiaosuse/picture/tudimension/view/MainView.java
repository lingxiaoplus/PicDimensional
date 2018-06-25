package com.lingxiaosuse.picture.tudimension.view;

import android.net.Uri;

public interface MainView {
    /**
     * 获取侧滑背景图
     * @param uri
     */
    void onGetHeadBackGround(Uri uri);

    /**
     * 获取背景文字
     * @param result
     */
    void onGetHeadText(String result);
}
