package com.camera.lingxiao.common.retrofit;

/**
 * @author lingxiao
 * 请求监听接口
 */
public interface HttpRequestListener {
    /**
     * 取消请求
     */
    void cancel();

    /**
     * 请求被取消
     */
    void onCanceled();
}
