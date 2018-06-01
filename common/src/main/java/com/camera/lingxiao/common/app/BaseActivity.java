package com.camera.lingxiao.common.app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //在界面未初始化之前调用的初始化窗口
        initWindows();
    }

    /**
     * 初始化
     */
    private void initWindows() {

    }
    /**
     * 初始化相关参数
     */
    protected boolean initArgs(Bundle bundle){
        return true;
    }

    /**
     * 得到当前界面的资源文件id
     */
    protected abstract int getContentLayoutId();

    /**
     * 初始化控件
     */
    protected void initWidget(){
        //ButterKnife.bind(this);
    }

    /**
     *  初始化数据
     */
    protected void initData(){

    }
}
