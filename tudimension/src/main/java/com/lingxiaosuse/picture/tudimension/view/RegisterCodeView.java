package com.lingxiaosuse.picture.tudimension.view;

public interface RegisterCodeView {
    /**
     * 发送验证码返回
     * @param success
     * @param msg
     * @param phone
     */
    void onCodeResult(boolean success,String msg,String phone);

    void onRegister(boolean success,String phone,String username,String pssword,String msg);
}
