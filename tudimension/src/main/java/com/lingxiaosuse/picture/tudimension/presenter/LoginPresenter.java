package com.lingxiaosuse.picture.tudimension.presenter;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogInCallback;
import com.camera.lingxiao.common.app.BasePresenter;
import com.camera.lingxiao.common.utills.MD5Util;
import com.lingxiaosuse.picture.tudimension.activity.LoginActivity;
import com.lingxiaosuse.picture.tudimension.db.UserModel;
import com.lingxiaosuse.picture.tudimension.view.LoginView;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

public class LoginPresenter extends BasePresenter<LoginView,LoginActivity>{
    private String mDecodePsd;
    public LoginPresenter(LoginView view, LoginActivity activity) {
        super(view, activity);
    }

    public void login(String phone,String password){
        try {
            this.mDecodePsd = MD5Util.getEncryptedPwd(password);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        AVUser.loginByMobilePhoneNumberInBackground(phone, mDecodePsd, new LogInCallback<AVUser>() {
            @Override
            public void done(AVUser avUser, AVException e) {
                if (e == null){
                    UserModel model = new UserModel();
                    model.setUsername(avUser.getUsername());
                    model.setObjId(avUser.getObjectId());
                    /*model.setDesc(avUser.getString(ContentValue.DESCRIPTION));
                    model.setProtrait(avUser.getString(ContentValue.PROTRAIT));
                    model.setNickname(avUser.getString(ContentValue.NICKNAME));
                    model.setAge(avUser.getInt(ContentValue.AGE));*/
                    model.setObjId(avUser.getObjectId());
                    model.setPhone(avUser.getMobilePhoneNumber());
                    model.setToken(avUser.getSessionToken());
                    model.setCreateTime(avUser.getCreatedAt().getTime());
                    model.setUpdateTime(avUser.getUpdatedAt().getTime());
                    model.save();
                    getView().onLogin(true,avUser,null);
                }else {
                    getView().onLogin(false,avUser,e.getMessage());
                }
            }
        });
    }
}
