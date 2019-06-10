package com.lingxiaosuse.picture.tudimension.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

import com.camera.lingxiao.common.app.BaseActivity;
import com.camera.lingxiao.common.app.ContentValue;
import com.camera.lingxiao.common.utills.SpUtils;
import com.github.zackratos.ultimatebar.UltimateBar;
import com.lingxiaosuse.picture.tudimension.R;

import butterknife.BindView;
import butterknife.OnClick;

public class GuidePulldownActivity extends BaseActivity {
    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_guide_pulldown;
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        UltimateBar.newImmersionBuilder()
                .applyNav(false)         // 是否应用到导航栏
                .build(this)
                .apply();
    }

    @OnClick(R.id.bt_sure)
    public void onKnowClick(){
        SpUtils.putBoolean(getApplicationContext(),ContentValue.IS_SHOW_GUIDE,false);
        finish();
    }
}
