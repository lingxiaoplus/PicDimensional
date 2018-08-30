package com.lingxiaosuse.picture.tudimension.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.AppCompatImageView;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.camera.lingxiao.common.app.BaseActivity;
import com.github.zackratos.ultimatebar.UltimateBar;
import com.lingxiaosuse.picture.tudimension.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity {
    @BindView(R.id.et_username)
    EditText etUsername;
    @BindView(R.id.img_username)
    AppCompatImageView imgUsername;
    @BindView(R.id.progress_name)
    ProgressBar progressName;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.img_password)
    AppCompatImageView imgPassword;
    @BindView(R.id.progress_password)
    ProgressBar progressPassword;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            progressName.setVisibility(View.INVISIBLE);
            progressPassword.setVisibility(View.INVISIBLE);
            imgUsername.setVisibility(View.VISIBLE);
            imgPassword.setVisibility(View.VISIBLE);
        }
    };
    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initData() {
        super.initData();
        UltimateBar.newImmersionBuilder()
                .applyNav(true)         // 是否应用到导航栏
                .build(this)
                .apply();
    }

    @OnClick(R.id.bt_login)
    public void onLogin(){
        progressName.setVisibility(View.VISIBLE);
        progressPassword.setVisibility(View.VISIBLE);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                    mHandler.sendEmptyMessage(0);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @OnClick(R.id.img_exit)
    public void onExit(){
        finish();
    }
}
