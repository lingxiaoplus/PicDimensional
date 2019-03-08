package com.lingxiaosuse.picture.tudimension.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import skin.support.content.res.SkinCompatResources;
import skin.support.widget.SkinCompatHelper;
import skin.support.widget.SkinCompatSupportable;

import static skin.support.widget.SkinCompatHelper.INVALID_ID;

public class SmartSkinRefreshLayout extends SmartRefreshLayout implements SkinCompatSupportable {

    private int mPrimaryColorId = INVALID_ID;
    protected int[] mPrimaryColors;
    private int mAccentColor;

    private static final String TAG = SmartSkinRefreshLayout.class.getSimpleName();
    public SmartSkinRefreshLayout(Context context) {
        this(context,null);
    }

    public SmartSkinRefreshLayout(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public SmartSkinRefreshLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, com.scwang.smartrefresh.layout.R.styleable.SmartRefreshLayout,
                defStyleAttr, 0);
        if (a.hasValue(com.scwang.smartrefresh.layout.
                R.styleable.SmartRefreshLayout_srlPrimaryColor)){
            mPrimaryColorId = a.getResourceId(com.scwang.smartrefresh.layout.
                    R.styleable.SmartRefreshLayout_srlPrimaryColor, INVALID_ID);
        }
        mAccentColor = a.getColor(com.scwang.smartrefresh.layout.R.styleable.SmartRefreshLayout_srlAccentColor, 0);
        a.recycle();
        applySkin();
    }

    @Override
    public void applySkin() {
        mPrimaryColorId = SkinCompatHelper.checkResourceId(mPrimaryColorId);
        if (mPrimaryColorId != INVALID_ID) {
            int primaryColor = SkinCompatResources.getColor(getContext(), mPrimaryColorId);
            if (mAccentColor != INVALID_ID) {
                mPrimaryColors = new int[]{primaryColor, mAccentColor};
            } else {
                mPrimaryColors = new int[]{primaryColor};
            }
            setPrimaryColors(mPrimaryColors);
        }
    }
}
