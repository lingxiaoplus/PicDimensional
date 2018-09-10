/**
 * @author Glan.duanyj
 * @date 2014-05-12
 * @project rest_demo
 */
package com.lingxiaosuse.picture.tudimension.sendcode;

import com.camera.lingxiao.common.observer.HttpRxCallback;
import com.lingxiaosuse.picture.tudimension.modle.IdentifyCodeModle;

public abstract class AbsRestClient {
	//public String server=SysConfig.getInstance().getProperty("rest_server");
	
	/**
	 * 
	 * @param sid
	 * @param token
	 * @param appid
	 * @param templateid
	 * @param param
	 * @param mobile
	 * @param uid
	 * @return
	 */
	public abstract IdentifyCodeModle sendSms(String sid, String token, String appid, String templateid, String param, String mobile, String uid, HttpRxCallback callback);
	
	/**
	 * 
	 * @param sid
	 * @param token
	 * @param appid
	 * @param templateid
	 * @param param
	 * @param mobile
	 * @param uid
	 * @return
	 */
	public abstract IdentifyCodeModle sendSmsBatch(String sid, String token, String appid, String templateid, String param, String mobile, String uid, HttpRxCallback callback);
	
	/**
	 * 
	 * @param sid
	 * @param token
	 * @param appid
	 * @param type
	 * @param template_name
	 * @param autograph
	 * @param content
	 * @return
	 */
	public abstract IdentifyCodeModle addSmsTemplate(String sid, String token, String appid, String type, String template_name, String autograph, String content, HttpRxCallback callback);
	 
	/**
	 * 
	 * @param sid
	 * @param token
	 * @param appid
	 * @param templateid
	 * @param page_num
	 * @param page_size
	 * @return
	 */
	public abstract IdentifyCodeModle getSmsTemplate(String sid, String token, String appid, String templateid, String page_num, String page_size, HttpRxCallback callback);
	
	/**
	 * 
	 * @param sid
	 * @param token
	 * @param appid
	 * @param templateid
	 * @param type
	 * @param template_name
	 * @param autograph
	 * @param content
	 * @return
	 */
	public abstract IdentifyCodeModle editSmsTemplate(String sid, String token, String appid, String templateid, String type, String template_name, String autograph, String content, HttpRxCallback callback);
	
	/**
	 * 
	 * @param sid
	 * @param token
	 * @param appid
	 * @param templateid
	 * @return
	 */
	public abstract IdentifyCodeModle deleterSmsTemplate(String sid, String token, String appid, String templateid, HttpRxCallback callback);
	
	
	public StringBuffer getStringBuffer() {
		StringBuffer sb = new StringBuffer("https://");
		sb.append("open.ucpaas.com").append("/ol/sms");
		return sb;
	}
}
