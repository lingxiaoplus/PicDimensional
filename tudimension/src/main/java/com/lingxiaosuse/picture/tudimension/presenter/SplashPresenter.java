package com.lingxiaosuse.picture.tudimension.presenter;

import com.camera.lingxiao.common.VersionModle;
import com.camera.lingxiao.common.app.BasePresenter;
import com.camera.lingxiao.common.http.ParseHelper;
import com.camera.lingxiao.common.observer.HttpRxCallback;
import com.camera.lingxiao.common.retrofit.HttpRequest;
import com.camera.lingxiao.common.utills.LogUtils;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.lingxiaosuse.picture.tudimension.activity.SplashActivity;
import com.lingxiaosuse.picture.tudimension.view.SplashView;

import java.util.Map;
import java.util.TreeMap;

public class SplashPresenter extends BasePresenter<SplashView,SplashActivity>{
    private final String API_UPDATE = "http://www.lingxiaosuse.cn/tudimension/update.json";
    protected HttpRequest mHttpRequest;
    //private final String TAG = PhoneAddressPresenter.class.getSimpleName();
    public SplashPresenter(SplashView view, SplashActivity activity) {
        super(view, activity);
        mHttpRequest = new HttpRequest();
    }

    protected HttpRequest getRequest() {
        if (mHttpRequest == null) {
            mHttpRequest = new HttpRequest();
        }
        return mHttpRequest;
    }
    /**
     * 检查更新
     */
    public void getVersion(){
        /**
         * 构建请求参数
         */
        TreeMap<String, Object> request = new TreeMap<>();
        //request.put("username", userName);
        //request.put("password", password);
        request.put(HttpRequest.API_URL, API_UPDATE);


        HttpRxCallback callback = new HttpRxCallback() {
            @Override
            public void onSuccess(Object... object) {
                LogUtils.i("success presenter:  "+object[0].toString());
            }

            @Override
            public void onError(int code, String desc) {
                LogUtils.i("error presenter:  "+desc);
            }

            @Override
            public void onCancel() {

            }
        };
        /**
         * 解析数据
         */
        callback.setParseHelper(new ParseHelper() {
            @Override
            public Object[] parse(JsonElement jsonElement) {
                VersionModle modle = new Gson().fromJson(jsonElement,VersionModle.class);
                Object[] obj = new Object[1];
                obj[0] = modle;
                return obj;
            }
        });

        getRequest().request(HttpRequest.Method.GET,request,getActivity(),callback);

    }

    /**
     * 获取封面图片地址
     */
    public void getImgUrl(){

    }
}
