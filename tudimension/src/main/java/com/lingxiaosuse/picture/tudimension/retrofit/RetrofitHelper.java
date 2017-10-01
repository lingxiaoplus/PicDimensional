package com.lingxiaosuse.picture.tudimension.retrofit;

import android.content.Context;
import android.util.Log;

import com.google.gson.GsonBuilder;
import com.lingxiaosuse.picture.tudimension.global.ContentValue;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by lingxiao on 2017/8/31.
 */

public class RetrofitHelper {
    private Context mContext;
    OkHttpClient client = new OkHttpClient();
    GsonConverterFactory factory = GsonConverterFactory.create(new GsonBuilder().create());
    private static RetrofitHelper instance = null;
    private Retrofit mRetrofit = null;
    public static RetrofitHelper getInstance(Context context){
        if (instance == null){
            instance = new RetrofitHelper(context);
        }
        return instance;
    }
    private RetrofitHelper(Context context){
        this.mContext = context;
        init();
    }

    private void init() {
        resetApp();
    }

    private void resetApp() {
        mRetrofit = new Retrofit.Builder()
                .baseUrl(ContentValue.BASE_URL)
                .client(client)
                .addConverterFactory(factory)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

    }
    public <T> T getInterface(Class<T> reqServer){
        return mRetrofit.create(reqServer);
    }
}
