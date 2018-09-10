package com.lingxiaosuse.picture.tudimension.fragment.login;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.camera.lingxiao.common.app.BaseFragment;
import com.lingxiaosuse.picture.tudimension.R;
import com.lingxiaosuse.picture.tudimension.presenter.RegisterPresenter;
import com.lingxiaosuse.picture.tudimension.utils.StringUtils;
import com.lingxiaosuse.picture.tudimension.utils.ToastUtils;
import com.lingxiaosuse.picture.tudimension.view.RegisterCodeView;

import butterknife.BindView;
import butterknife.OnClick;

public class RegisterFragment extends BaseFragment implements RegisterCodeView {
    @BindView(R.id.tv_phone)
    TextView tvPhone;
    @BindView(R.id.et_code)
    EditText etCode;
    @BindView(R.id.bt_send_code)
    Button btSendCode;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.bt_next)
    Button btNext;
    private RegisterPresenter mPresenter = new RegisterPresenter(this,this);
    private CountDownTimer timer = new CountDownTimer(60 * 1000, 1000) {
        @Override
        public void onTick(long l) {
            btSendCode.setEnabled(false);
            btSendCode.setText(l / 1000 + "秒");
        }

        @Override
        public void onFinish() {
            btSendCode.setEnabled(true);
            btSendCode.setText("再次发送");
        }
    };
    private String mCodeStr;
    private String mPhone;

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_register;
    }

    @Override
    protected void initWidget(View root) {
        super.initWidget(root);
        Bundle bundle = getArguments();
        if (null != bundle) {
            mPhone = bundle.getString("phone");
            if (null != mPhone) {
                tvPhone.setText("手机号码："+ mPhone);
            }
            mCodeStr = bundle.getString("code");

        }
        timer.start();
    }

    @OnClick(R.id.bt_next)
    public void onRegister(){
        String et_code = etCode.getText().toString().trim();
        String et_password = etPassword.getText().toString().trim();
        String et_name = etName.getText().toString().trim();
        if (et_code.isEmpty()){
            etCode.setError("验证码不能为空!");
            return;
        }
        if (et_password.isEmpty()){
            etPassword.setError("密码不能为空!");
            return;
        }
        if (et_name.isEmpty()){
            etName.setError("昵称不能为空!");
            return;
        }
        if (!TextUtils.equals(et_code,mCodeStr)){
            Log.e("RegisterFragment", "onRegister: 本页面："+et_code+"   上一个页面："+mCodeStr);
            ToastUtils.show("验证码不正确");
            return;
        }
        showProgressDialog("注册中...", getActivity());
        mPresenter.registerPerson(et_name,et_password,mPhone);
    }

    @OnClick(R.id.bt_send_code)
    public void sendCode(){
        mCodeStr = StringUtils.createCode(4);
        mPresenter.sendCode(mPhone,mCodeStr);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        timer.cancel();
    }
    @Override
    public void onCodeResult(boolean success, String msg, String phone) {
        if (success){
            ToastUtils.show("发送成功");
            timer.start();
        }else {
            ToastUtils.show("发送失败："+msg);
        }
    }

    @Override
    public void onRegister(boolean success, String phone, String username, String pssword, String msg) {
        cancleProgressDialog();
        if (success){
            ToastUtils.show("注册成功");
            if (getActivity() != null){
                getActivity().finish();
            }
        }else {
            ToastUtils.show("注册失败:"+msg);
        }
    }

}
