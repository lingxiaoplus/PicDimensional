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

import com.avos.avoscloud.AVUser;
import com.camera.lingxiao.common.app.BaseActivity;
import com.camera.lingxiao.common.rxbus.RxBus;
import com.camera.lingxiao.common.rxbus.SkinChangedEvent;
import com.facebook.drawee.view.SimpleDraweeView;
import com.github.zackratos.ultimatebar.UltimateBar;
import com.lingxiaosuse.picture.tudimension.R;
import com.lingxiaosuse.picture.tudimension.presenter.LoginPresenter;
import com.lingxiaosuse.picture.tudimension.utils.ToastUtils;
import com.lingxiaosuse.picture.tudimension.utils.UIUtils;
import com.lingxiaosuse.picture.tudimension.view.LoginView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class LoginActivity extends BaseActivity implements LoginView{
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
    @BindView(R.id.iv_username)
    AppCompatImageView ivUsername;
    @BindView(R.id.iv_password)
    AppCompatImageView ivPassword;
    private LoginPresenter mPresenter;

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
        mPresenter = new LoginPresenter(this,this);
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
        String et_phone = etUsername.getText().toString().trim();
        String et_password = etPassword.getText().toString().trim();
        if (et_phone.isEmpty() || et_phone.length() < 11){
            ToastUtils.show("手机号码不正确");
            return;
        }
        if (et_password.isEmpty()){
            ToastUtils.show("密码不能为空");
            return;
        }
        progressName.setVisibility(View.VISIBLE);
        progressPassword.setVisibility(View.VISIBLE);
        mPresenter.login(et_phone,et_password);
    }

    @OnClick(R.id.img_exit)
    public void onExit() {
        finish();
    }

    @OnClick({R.id.login_weibo, R.id.login_qq, R.id.login_wechat})
    public void onLoginByOther(View view) {
        ToastUtils.show("暂不支持第三方登陆");
        switch (view.getId()) {
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
    public void onRegister() {
        StartActivity(RegisterActivity.class, false);
    }

    @Override
    protected void onSkinChanged(int color) {
        super.onSkinChanged(color);
        ToastUtils.show("皮肤改变");
        UIUtils.changeSVGColor(ivUsername, R.drawable.ic_img_header, color);
        UIUtils.changeSVGColor(ivPassword, R.drawable.ic_img_password, color);
    }

    @Override
    public void onLogin(boolean success, AVUser avUser, String msg) {
        progressName.setVisibility(View.INVISIBLE);
        progressPassword.setVisibility(View.INVISIBLE);
        if (success){
            imgUsername.setVisibility(View.VISIBLE);
            imgPassword.setVisibility(View.VISIBLE);
            finish();
        }else {
            ToastUtils.show(msg);
        }
    }
}
