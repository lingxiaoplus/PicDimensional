package com.lingxiaosuse.picture.tudimension.fragment.login;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.camera.lingxiao.common.app.BaseFragment;
import com.lingxiaosuse.picture.tudimension.R;
import com.lingxiaosuse.picture.tudimension.activity.RegisterActivity;
import com.lingxiaosuse.picture.tudimension.modle.IdentifyCodeModle;
import com.lingxiaosuse.picture.tudimension.sendcode.JsonReqClient;
import com.lingxiaosuse.picture.tudimension.utils.ToastUtils;

import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class InputPhoneFragment extends BaseFragment {
    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.bt_next)
    Button btNext;

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

        String code = createCode(4);
        JsonReqClient client = new JsonReqClient(this);
        String sid = "79e97a4f75301edaec354149c09c2f7e";
        String token = "f33f9435e36610e71c924d8b480eba55";
        String appid = "a868bbc024f24b18b90a3ea922299252";
        String templateid = "35082";
        String param = "图次元,"+code+",1";
        String uid = "";
        IdentifyCodeModle result = client.sendSms(sid, token, appid, templateid, param, mobile, uid);

        RegisterActivity activity = (RegisterActivity) getActivity();
        Bundle bundle = new Bundle();
        bundle.putString("phone",mobile);
        bundle.putString("code",code);
        activity.checkoutFragment(1,bundle);
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
