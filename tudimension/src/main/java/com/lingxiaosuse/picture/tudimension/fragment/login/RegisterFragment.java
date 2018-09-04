package com.lingxiaosuse.picture.tudimension.fragment.login;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.camera.lingxiao.common.app.BaseFragment;
import com.lingxiaosuse.picture.tudimension.R;
import com.lingxiaosuse.picture.tudimension.modle.IdentifyCodeModle;
import com.lingxiaosuse.picture.tudimension.sendcode.JsonReqClient;
import com.lingxiaosuse.picture.tudimension.utils.ToastUtils;
import com.lingxiaosuse.picture.tudimension.utils.UIUtils;

import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class RegisterFragment extends BaseFragment {
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

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_register;
    }

    @Override
    protected void initWidget(View root) {
        super.initWidget(root);
        Bundle bundle = getArguments();
        if (null != bundle) {
            String phone = bundle.getString("phone");
            if (null != phone) {
                tvPhone.setText("手机号码："+phone);
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
            ToastUtils.show("验证码不正确");
            return;
        }
        showProgressDialog("注册中...", getActivity());
    }

    @OnClick(R.id.bt_send_code)
    public void sendCode(){
        mCodeStr = createCode(4);
        JsonReqClient client = new JsonReqClient(this);
        String sid = "79e97a4f75301edaec354149c09c2f7e";
        String token = "f33f9435e36610e71c924d8b480eba55";
        String appid = "a868bbc024f24b18b90a3ea922299252";
        String templateid = "35082";
        String param = "图次元,"+mCodeStr+",1";
        String mobile = "15182631360";
        String uid = "";
        IdentifyCodeModle result = client.sendSms(sid, token, appid, templateid, param, mobile, uid);
        if (result.getCode() == "000000"){
            ToastUtils.show("发送成功");
            timer.start();
        }


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        timer.cancel();
    }

    private String createCode(int max){
        String code = "";
        Random random = new Random();
        for (int i = 0; i < max; i++) {
            int r = random.nextInt(10); //每次随机出一个数字（0-9）
            code = code + r;  //把每次随机出的数字拼在一起

        }
        return code;
    }
}
