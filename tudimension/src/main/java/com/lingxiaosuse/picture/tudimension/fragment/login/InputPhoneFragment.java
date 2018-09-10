package com.lingxiaosuse.picture.tudimension.fragment.login;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.camera.lingxiao.common.app.BaseFragment;
import com.lingxiaosuse.picture.tudimension.R;
import com.lingxiaosuse.picture.tudimension.activity.RegisterActivity;
import com.lingxiaosuse.picture.tudimension.presenter.RegisterPresenter;
import com.lingxiaosuse.picture.tudimension.utils.StringUtils;
import com.lingxiaosuse.picture.tudimension.utils.ToastUtils;
import com.lingxiaosuse.picture.tudimension.view.RegisterCodeView;

import butterknife.BindView;
import butterknife.OnClick;

public class InputPhoneFragment extends BaseFragment implements RegisterCodeView {
    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.bt_next)
    Button btNext;

    private RegisterPresenter mPresenter = new RegisterPresenter(this,this);
    private String mCode;

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_register_phone;
    }

    @OnClick(R.id.bt_next)
    public void onNext(){
        String mobile = etPhone.getText().toString().trim();
        if (mobile.isEmpty()){
            ToastUtils.show("不能为空");
            return;
        }else if (mobile.length() != 11){
            ToastUtils.show("手机号码位数不正确");
            return;
        }
        showProgressDialog("发送中...",getActivity());
        mCode = StringUtils.createCode(4);
        mPresenter.sendCode(mobile,mCode);
    }


    @Override
    public void onCodeResult(boolean success, String msg,String phone) {
        cancleProgressDialog();
        if (success){
            RegisterActivity activity = (RegisterActivity) getActivity();
            Bundle bundle = new Bundle();
            bundle.putString("phone",phone);
            bundle.putString("code", mCode);
            activity.checkoutFragment(1,bundle);
        }else {
            ToastUtils.show("发送失败："+msg);
        }
    }

    @Override
    public void onRegister(boolean success, String phone, String username, String pssword, String msg) {

    }

}
