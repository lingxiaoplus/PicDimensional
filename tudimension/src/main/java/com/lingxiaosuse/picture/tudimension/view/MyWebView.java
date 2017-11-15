package com.lingxiaosuse.picture.tudimension.view;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.webkit.WebView;

/**
 * Created by lingxiao on 17-11-14.
 */

public class MyWebView extends WebView{
    //添加滑动监听,不然会和swiperefrelayout滑动冲突
    private OnScrollChangedCallback callback;
    public MyWebView(Context context) {
        super(context);
    }

    public MyWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (callback != null){
            callback.onScroll(l,t);
        }
    }

    public OnScrollChangedCallback getOnScrollChangedCallback() {
        return callback;
    }

    public void setOnScrollChangedCallback(final OnScrollChangedCallback
                                                   onScrollChangedCallback) {
        callback = onScrollChangedCallback;
    }


    public interface OnScrollChangedCallback {
        void onScroll(int l, int t);
    }
}
