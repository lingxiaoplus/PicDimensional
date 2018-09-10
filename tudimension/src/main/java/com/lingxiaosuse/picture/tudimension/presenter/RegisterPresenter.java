package com.lingxiaosuse.picture.tudimension.presenter;

import android.text.TextUtils;
import android.util.Log;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SignUpCallback;
import com.camera.lingxiao.common.app.BaseFragment;
import com.camera.lingxiao.common.app.BasePresenter;
import com.camera.lingxiao.common.app.ContentValue;
import com.camera.lingxiao.common.observer.HttpRxCallback;
import com.camera.lingxiao.common.utills.MD5Util;
import com.camera.lingxiao.common.utills.SpUtils;
import com.lingxiaosuse.picture.tudimension.modle.IdentifyCodeModle;
import com.lingxiaosuse.picture.tudimension.transation.RegisterTrans;
import com.lingxiaosuse.picture.tudimension.utils.UIUtils;
import com.lingxiaosuse.picture.tudimension.view.RegisterCodeView;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

public class RegisterPresenter extends BasePresenter<RegisterCodeView, BaseFragment> {
    private RegisterTrans mTrans;
    private String md5Psd;
    private String mPhone,mUserName;
    public RegisterPresenter(RegisterCodeView view, BaseFragment activity) {
        super(view, activity);
        mTrans = new RegisterTrans(getActivity());
    }

    public void sendCode(String phone,String code) {
        HttpRxCallback callback = new HttpRxCallback() {
            @Override
            public void onSuccess(Object... object) {
                IdentifyCodeModle result = (IdentifyCodeModle) object[0];
                if (TextUtils.equals("000000",result.getCode())){
                    getView().onCodeResult(true,result.getMsg(),result.getMobile());
                }else {
                    getView().onCodeResult(false,result.getMsg(),result.getMobile());
                }
                Log.e("HttpClient", "onSuccess: " + result.toString());
            }

            @Override
            public void onError(int code, String desc) {
                getView().onCodeResult(false,desc,null);
                Log.e("HttpClient", "onError: " + desc);
            }

            @Override
            public void onCancel() {

            }
        };
        mTrans.sendCode(phone, code,callback);
    }

    public void registerPerson(String username,String psd,String phone){
        //注册时加密
        this.md5Psd = psd;
        this.mPhone = phone;
        this.mUserName = username;
        try {
            md5Psd = MD5Util.getEncryptedPwd(psd);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        AVUser user = new AVUser();// 新建 AVUser 对象实例
        user.setUsername(username);// 设置用户名
        user.setPassword(md5Psd);// 设置密码
        user.setMobilePhoneNumber(phone); //设置手机号
        //在子线程中进行
        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(AVException e) {
                if (e == null){
                    SpUtils.putString(UIUtils.getContext(),
                            ContentValue.KEY_USERNAME, mUserName);
                    SpUtils.putString(UIUtils.getContext(),
                            ContentValue.KEY_PSD, md5Psd);
                    getView().onRegister(true,mPhone,mUserName,md5Psd,null);
                }else {
                    if (e.getCode() == 202){
                        getView().onRegister(false,mPhone,mUserName,md5Psd,"用户名已经存在");
                    }else if (e.getCode() == 214){
                        getView().onRegister(false,mPhone,mUserName,md5Psd,"手机号已经被注册");
                    }else {
                        getView().onRegister(false,mPhone,mUserName,md5Psd,e.getMessage());
                    }
                }
            }
        });
    }
}
