package com.lingxiaosuse.picture.tudimension.view;

import com.avos.avoscloud.AVUser;

public interface LoginView {
    void onLogin(boolean success, AVUser avUser, String msg);
}
