package com.lingxiaosuse.picture.tudimension.sendcode;

import com.camera.lingxiao.common.api.UserApi;
import com.camera.lingxiao.common.http.ParseHelper;
import com.camera.lingxiao.common.observable.HttpRxObservable;
import com.camera.lingxiao.common.observer.HttpRxCallback;
import com.camera.lingxiao.common.utills.RetrofitUtil;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.lingxiaosuse.picture.tudimension.modle.IdentifyCodeModle;
import com.trello.rxlifecycle2.LifecycleProvider;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class HttpClientUtil {
	
	public static boolean isTest = true;
	private static IdentifyCodeModle result = null;
	public static IdentifyCodeModle postJson(String url, String body ,LifecycleProvider lifecycle,HttpRxCallback callback) {
		Map<String,String> headerMap = new HashMap<>();
		RequestBody requestBody = RequestBody.create(
				MediaType.parse("application/json; charset=utf-8"),
				body);
		headerMap.put("Accept","application/json");
		headerMap.put("Content-Type","application/json;charset=utf-8");

		Observable apiObservable;
		callback.setParseHelper(new ParseHelper() {
			@Override
			public Object[] parse(JsonElement element) {
				IdentifyCodeModle modle = new Gson().fromJson(element,IdentifyCodeModle.class);
				Object[] obj = new Object[1];
				obj[0] = modle;
				return obj;
			}
		});
		apiObservable = RetrofitUtil
				.get()
				.retrofit()
				.create(UserApi.class)
				.sendCode(url,headerMap,requestBody);
		HttpRxObservable.getOtherObservable(apiObservable, lifecycle, callback)
				.subscribe(callback);
		
		return result;
	}
}
