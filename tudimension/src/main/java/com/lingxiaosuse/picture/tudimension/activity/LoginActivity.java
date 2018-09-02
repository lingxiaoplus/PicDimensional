package com.lingxiaosuse.picture.tudimension.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.AppCompatImageView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.camera.lingxiao.common.app.BaseActivity;
import com.facebook.drawee.view.SimpleDraweeView;
import com.github.zackratos.ultimatebar.UltimateBar;
import com.lingxiaosuse.picture.tudimension.R;
import com.lingxiaosuse.picture.tudimension.utils.ToastUtils;

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
    @BindView(R.id.img_exit)
    AppCompatImageView imgExit;
    @BindView(R.id.bt_login)
    Button btLogin;
    @BindView(R.id.tv_forget_password)
    TextView tvForgetPassword;
    @BindView(R.id.tv_register)
    TextView tvRegister;
    @BindView(R.id.tv_or)
    TextView tvOr;
    @BindView(R.id.login_weibo)
    SimpleDraweeView loginWeibo;
    @BindView(R.id.login_qq)
    SimpleDraweeView loginQq;
    @BindView(R.id.login_wechat)
    SimpleDraweeView loginWechat;

    private Handler mHandler = new Handler() {
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
    protected void initWidget() {
        super.initWidget();
        SpannableString spannableString = new SpannableString("or");
        RelativeSizeSpan sizeSpan01 = new RelativeSizeSpan(1.2f);
        RelativeSizeSpan sizeSpan02 = new RelativeSizeSpan(1.0f);
        spannableString.setSpan(sizeSpan01, 0, 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(sizeSpan02, 1, 2, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        tvOr.setText(spannableString);
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
    public void onLogin() {
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
    public void onExit() {
        finish();
    }

    @OnClick({R.id.login_weibo,R.id.login_qq,R.id.login_wechat})
    public void onLoginByOther(View view) {
        ToastUtils.show("暂不支持第三方登陆");
        switch (view.getId()){
            case R.id.login_weibo:
                break;
            case R.id.login_qq:
                break;
            case R.id.login_wechat:
                break;
            default:
                break;
        }
    }

    @OnClick(R.id.tv_register)
    public void onRegister(){
        StartActivity(RegisterActivity.class,false);
    }

}
