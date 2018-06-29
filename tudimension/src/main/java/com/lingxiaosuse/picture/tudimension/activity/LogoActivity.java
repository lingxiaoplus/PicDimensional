package com.lingxiaosuse.picture.tudimension.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.camera.lingxiao.common.app.BaseActivity;

public class LogoActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StartActivity(SplashActivity.class,true);
    }

    @Override
    protected int getContentLayoutId() {
        return 0;
    }

}
