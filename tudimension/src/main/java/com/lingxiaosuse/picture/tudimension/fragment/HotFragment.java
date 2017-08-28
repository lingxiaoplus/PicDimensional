package com.lingxiaosuse.picture.tudimension.fragment;

import android.view.View;
import android.widget.TextView;

import com.lingxiaosuse.picture.tudimension.utils.UIUtils;

/**
 * Created by lingxiao on 2017/8/28.
 */

public class HotFragment extends BaseFragment{
    @Override
    protected void initData() {

    }

    @Override
    public View initView() {
        TextView textView = new TextView(UIUtils.getContext());
        textView.setText("最新");
        textView.setTextScaleX(18);
        return textView;
    }
}
