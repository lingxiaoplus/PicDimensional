package com.lingxiaosuse.picture.tudimension.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.lingxiaosuse.picture.tudimension.R;

public class BezierRefreshLayout extends LinearLayout {

    private SmartSkinRefreshLayout mRefreshLayout;
    private RecyclerView mRecyclerView;

    public BezierRefreshLayout(Context context) {
        this(context,null);
    }

    public BezierRefreshLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public BezierRefreshLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        View view = View.inflate(context, R.layout.bezier_refresh_layout,this);
        mRefreshLayout = view.findViewById(R.id.refresh_layout);
        mRecyclerView = view.findViewById(R.id.recyclerview);
    }


    public SmartSkinRefreshLayout getRefreshLayout() {
        return mRefreshLayout;
    }

    public RecyclerView getRecyclerView() {
        return mRecyclerView;
    }
}
