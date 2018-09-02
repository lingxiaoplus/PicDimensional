
package com.lingxiaosuse.picture.tudimension.sendcode;

import com.lingxiaosuse.picture.tudimension.modle.IdentifyCodeModle;
import com.trello.rxlifecycle2.LifecycleProvider;

import org.json.JSONObject;

public class JsonReqClient extends AbsRestClient {
	private LifecycleProvider mLifecycle;
	public JsonReqClient(LifecycleProvider lifecycle){
		this.mLifecycle = lifecycle;
	}
	@Override
	public IdentifyCodeModle sendSms(String sid, String token, String appid, String templateid, String param, String mobile,
									 String uid) {

		IdentifyCodeModle result = null;
		
		try {
			String url = getStringBuffer().append("/sendsms").toString();
			
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("sid", sid);
			jsonObject.put("token", token);
			jsonObject.put("appid", appid);
			jsonObject.put("templateid", templateid);
			jsonObject.put("param", param);
			jsonObject.put("mobile", mobile);
			jsonObject.put("uid", uid);
			
			String body = jsonObject.toString();
			
			System.out.println("body = " + body);
			
			result = HttpClientUtil.postJson(url, body, mLifecycle);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public IdentifyCodeModle sendSmsBatch(String sid, String token, String appid, String templateid, String param, String mobile,
			String uid) {

		IdentifyCodeModle result = null;
		
		try {
			String url = getStringBuffer().append("/sendsms_batch").toString();
			
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("sid", sid);
			jsonObject.put("token", token);
			jsonObject.put("appid", appid);
			jsonObject.put("templateid", templateid);
			jsonObject.put("param", param);
			jsonObject.put("mobile", mobile);
			jsonObject.put("uid", uid);
			
			String body = jsonObject.toString();
			
			System.out.println("body = " + body);
			
			result = HttpClientUtil.postJson(url, body, mLifecycle);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public IdentifyCodeModle addSmsTemplate(String sid, String token, String appid, String type, String template_name,
			String autograph, String content) {

		IdentifyCodeModle result = null;
		
		try {
			String url = getStringBuffer().append("/addsmstemplate").toString();
			
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("sid", sid);
			jsonObject.put("token", token);
			jsonObject.put("appid", appid);
			jsonObject.put("type", type);
			jsonObject.put("template_name", template_name);
			jsonObject.put("autograph", autograph);
			jsonObject.put("content", content);
			
			String body = jsonObject.toString();
			
			System.out.println("body = " + body);
			
			result = HttpClientUtil.postJson(url, body, mLifecycle);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public IdentifyCodeModle getSmsTemplate(String sid, String token, String appid, String templateid, String page_num,
			String page_size) {

		IdentifyCodeModle result = null;
		
		try {
			String url = getStringBuffer().append("/getsmstemplate").toString();
			
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("sid", sid);
			jsonObject.put("token", token);
			jsonObject.put("appid", appid);
			jsonObject.put("templateid", templateid);
			jsonObject.put("page_num", page_num);
			jsonObject.put("page_size", page_size);
			
			String body = jsonObject.toString();
			
			System.out.println("body = " + body);
			
			result = HttpClientUtil.postJson(url, body, mLifecycle);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public IdentifyCodeModle editSmsTemplate(String sid, String token, String appid, String templateid, String type,
			String template_name, String autograph, String content) {

		IdentifyCodeModle result = null;
		
		try {
			String url = getStringBuffer().append("/editsmstemplate").toString();
			
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("sid", sid);
			jsonObject.put("token", token);
			jsonObject.put("appid", appid);
			jsonObject.put("templateid", templateid);
			jsonObject.put("type", type);
			jsonObject.put("template_name", template_name);
			jsonObject.put("autograph", autograph);
			jsonObject.put("content", content);
			
			String body = jsonObject.toString();
			
			System.out.println("body = " + body);
			
			result = HttpClientUtil.postJson(url, body, mLifecycle);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public IdentifyCodeModle deleterSmsTemplate(String sid, String token, String appid, String templateid) {

		IdentifyCodeModle result = null;
		
		try {
			String url = getStringBuffer().append("/deletesmstemplate").toString();
			
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("sid", sid);
			jsonObject.put("token", token);
			jsonObject.put("appid", appid);
			jsonObject.put("templateid", templateid);
			
			String body = jsonObject.toString();
			
			System.out.println("body = " + body);
			
			result = HttpClientUtil.postJson(url, body, mLifecycle);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
}
