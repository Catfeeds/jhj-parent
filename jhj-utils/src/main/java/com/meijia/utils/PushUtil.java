package com.meijia.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.baidu.yun.push.exception.PushClientException;
import com.baidu.yun.push.exception.PushServerException;
import com.gexin.rp.sdk.base.IPushResult;
import com.gexin.rp.sdk.base.impl.ListMessage;
import com.gexin.rp.sdk.base.impl.SingleMessage;
import com.gexin.rp.sdk.base.impl.Target;
import com.gexin.rp.sdk.base.payload.APNPayload;
import com.gexin.rp.sdk.exceptions.RequestException;
import com.gexin.rp.sdk.http.IGtPush;
import com.gexin.rp.sdk.template.TransmissionTemplate;
import com.meijia.utils.GsonUtil;
import com.gexin.rp.sdk.base.IQueryResult;
/**
 * 百度配置文件
 *
 */
public class PushUtil {
	
	public static String appId = "xjd3PmtMTa94K64Qm71jH1";
	
	public static String appKey = "H6Je2pDeu59cKisfDQpjC3";

	public static String appSecret = "wlb6ISDUae6mYcvUI3TTfA";
	
	public static String masterSecret = "1jRygkmNFQ8OYuaRCvgXH";
	
	public static String pushHost = "http://sdk.open.api.igexin.com/apiex.htm";
	
	// 推送消息的有效时间
	public static int msgExpires = 3600;
	
	/**
	 * 推送IOS 多个设备推送
	 * @return
	 * @throws PushClientException 
	 * @throws PushServerException 
	 */
	public static boolean IOSPushNotificationToMultiDevice(
			String[] cids,
			String msgContent, 
			Map<String, String> params)  {

		
		return true;
	}
	
	/**
	 * 推送IOS 单个设备推送
	 * @Param map<String, String> Params
	 *     key = cid 
	 *     key = title
	 *     key = msgContent
	 *     key = transmissionType
	 *     key = transmissionContent
	 * 
	 * @param pushType   notification or  alertClock   是发生消息和闹钟提醒， 区别为设置的声音不一样.
	 */
	public static boolean IOSPushToSingle(HashMap<String, String> params, String pushType) throws Exception {
		
		String cid = "";
		String transmissionContent = "";
		
		if (params.containsKey("cid")) 
			cid = params.get("cid").toString();
		
		if (params.containsKey("transmissionContent")) 
			transmissionContent = params.get("transmissionContent").toString();
		System.out.println("推送消息debug======================");
		System.out.println(params.toString());
		IGtPush push = new IGtPush(pushHost, appKey, masterSecret);
		
		String userStatus = getUserStatus(cid);
		
		TransmissionTemplate template = TransmissionTemplateIos(userStatus, transmissionContent, pushType);
		
		System.out.println(template.toString());
		SingleMessage message = new SingleMessage();
		message.setOffline(true);
		message.setOfflineExpireTime(2 * 1000 * 3600);
		message.setData(template);
			
		Target target1 = new Target();
		target1.setAppId(appId);
		target1.setClientId(cid);

		try {
			IPushResult ret = push.pushMessageToSingle(message, target1);
			System.out.println("================推送信息====================");
			System.out.println("push cid = " + cid);
			System.out.println("正常：" + ret.getResponse().toString());
			
		} catch (RequestException e) {
			String requstId = e.getRequestId();
			IPushResult ret = push.pushMessageToSingle(message, target1,
					requstId);

			System.out.println("异常：" + ret.getResponse().toString());
		}

		return true;
	}	
	
//	/**
//	 * 推送android 多个设备透传消息推送
//	 * @Param map<String, String> Params
//	 *     key = transmissionContent
//	 * 
//	 */
//	public static boolean IosPushToMulti(List<String> clientIds, HashMap<String, String> params) throws Exception {
//		
//		String transmissionContent = "";
//
//		if (params.containsKey("transmissionContent")) 
//			transmissionContent = params.get("transmissionContent").toString();
//		
//		
//		IGtPush push = new IGtPush(pushHost, appKey, masterSecret);
//		
//		TransmissionTemplate template = TransmissionTemplateIos();
//
//		template.setTransmissionContent(transmissionContent);
//		
//		ListMessage message = new ListMessage();
//		message.setOffline(true);
//		message.setOfflineExpireTime(2 * 1000 * 3600);
//		message.setData(template);
//		
//		List<Target> targets = new ArrayList<Target>();
//		
//		for (String cid : clientIds) {
//			Target target1 = new Target();
//			target1.setAppId(appId);
//			target1.setClientId(cid);
//			targets.add(target1);
//		}
//		
//		String taskId = push.getContentId(message);
//		IPushResult ret = push.pushMessageToList(taskId, targets);
//		System.out.println("正常：" + ret.getResponse().toString());
//				
//		return true;
//	}				
	
	/**
	 * 推送android 单个设备透传消息推送
	 * @Param map<String, String> Params
	 *     key = cid 
	 *     key = title
	 *     key = msgContent
	 *     key = transmissionContent
	 * 
	 */
	public static boolean AndroidPushToSingle(HashMap<String, String> params) throws Exception {
		
		//如果为debug模式，则不需要真正推送消息
		if (ConfigUtil.getInstance().getRb().getString("debug").equals("true")) {
			return true;
		}
		
		String cid = "";
		String transmissionContent = "";
		
		if (params.containsKey("cid")) 
			cid = params.get("cid").toString();

		if (params.containsKey("transmissionContent")) 
			transmissionContent = params.get("transmissionContent").toString();
		
		
		IGtPush push = new IGtPush(pushHost, appKey, masterSecret);
		
		TransmissionTemplate template = TransmissionTemplateDefault();

		template.setTransmissionContent(transmissionContent);
		
		SingleMessage message = new SingleMessage();
		message.setOffline(true);
		message.setOfflineExpireTime(2 * 1000 * 3600);
		message.setData(template);
			
		Target target1 = new Target();
		target1.setAppId(appId);
		target1.setClientId(cid);

		try {
			IPushResult ret = push.pushMessageToSingle(message, target1);
			System.out.println("正常：" + ret.getResponse().toString());
			
		} catch (RequestException e) {
			String requstId = e.getRequestId();
			IPushResult ret = push.pushMessageToSingle(message, target1,
					requstId);

			System.out.println("异常：" + ret.getResponse().toString());
		}
		return true;
	}			
	

	/**
	 * 推送android 多个设备透传消息推送
	 * @Param map<String, String> Params
	 *     key = cid 
	 *     key = title
	 *     key = msgContent
	 *     key = transmissionType
	 *     key = transmissionContent
	 * 
	 */
	public static boolean AndroidToMulti(List<String> clientIds, HashMap<String, String> params) throws Exception {
		
		String transmissionContent = "";

		if (params.containsKey("transmissionContent")) 
			transmissionContent = params.get("transmissionContent").toString();
		
		
		IGtPush push = new IGtPush(pushHost, appKey, masterSecret);
		
		TransmissionTemplate template = TransmissionTemplateDefault();

		template.setTransmissionContent(transmissionContent);
		
		ListMessage message = new ListMessage();
		message.setOffline(true);
		message.setOfflineExpireTime(2 * 1000 * 3600);
		message.setData(template);
		
		List<Target> targets = new ArrayList<Target>();
		
		for (String cid : clientIds) {
			Target target1 = new Target();
			target1.setAppId(appId);
			target1.setClientId(cid);
			targets.add(target1);
		}
		
		String taskId = push.getContentId(message);
		IPushResult ret = push.pushMessageToList(taskId, targets);
		System.out.println("正常：" + ret.getResponse().toString());
				
		return true;
	}			
		
	public static TransmissionTemplate TransmissionTemplateDefault()
			throws Exception {
		TransmissionTemplate template = new TransmissionTemplate();
		template.setAppId(appId);
		template.setAppkey(appKey);
		template.setTransmissionType(2);
		template.setTransmissionContent("");
//		template.setPushInfo("actionLocKey", 3, "message", "sound", "payload",
//				"locKey", "locArgs", "launchImage");
		return template;
	}	
	
	/**
	 * 
	 * @param userStatus           用户状态  Online   Offline
	 * @param transmissionContent  透传消息内容
	 * @param pushType   notification or  alertClock   是发生消息和闹钟提醒， 区别为设置的声音不一样.
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public static TransmissionTemplate TransmissionTemplateIos(String userStatus, String transmissionContent, String pushType)
			throws Exception {
		TransmissionTemplate template = new TransmissionTemplate();
		template.setAppId(appId);
		template.setAppkey(appKey);
		template.setTransmissionType(1);
		template.setTransmissionContent("");
		//在线的情况
//		if (userStatus.equals("Online")) {
			template.setTransmissionContent(transmissionContent);
//		}

		// template.setDuration("2015-01-16 11:40:00", "2015-01-16 12:24:00");
//		template.setPushInfo("", 1, "", "", "", "", "", "");
		
		//**********APN简单推送********//
//		APNPayload apnpayload = new APNPayload();
//		com.gexin.rp.sdk.base.payload.APNPayload.SimpleAlertMsg alertMsg = new com.gexin.rp.sdk.base.payload.APNPayload.SimpleAlertMsg(
//				"hahahaha");
//		apnpayload.setAlertMsg(alertMsg);
//		apnpayload.setBadge(5);
//		apnpayload.setContentAvailable(1);
//		apnpayload.setCategory("ACTIONABLE");
//		template.setAPNInfo(apnpayload);
		
			//************APN高级推送*******************//
			APNPayload apnpayload = new APNPayload();
			apnpayload.setBadge(1);
//			apnpayload.setSound("");
			apnpayload.setContentAvailable(1);
//			apnpayload.setCategory("cardView");
			
//			if (pushType.equals("alertClock")) {
				apnpayload.setSound("1000");
//			}
			APNPayload.DictionaryAlertMsg alertMsg = new APNPayload.DictionaryAlertMsg();
			
			
//			if (userStatus.equals("Offline")) {
				apnpayload.addCustomMsg("a", transmissionContent);
				
				HashMap<String, String> tranParams = GsonUtil.GsonToObject(transmissionContent, HashMap.class);
				String isShow = tranParams.get("is_show").toString().trim();
				String remindContent = tranParams.get("remind_content").toString();
				if (isShow.equals("true")) {
					alertMsg.setBody(remindContent);
				}
//			}
			
			
			
//			alertMsg.setActionLocKey("ActionLockey");
//			alertMsg.setLocKey("LocKey");
//			alertMsg.addLocArg("loc-args");
//			alertMsg.setLaunchImage("launch-image");
////			// IOS8.2以上版本支持
//			alertMsg.setTitle("Title");
//			alertMsg.setTitleLocKey("TitleLocKey");
//			alertMsg.addTitleLocArg("TitleLocArg");
//
			apnpayload.setAlertMsg(alertMsg);
			template.setAPNInfo(apnpayload);
		
		
		return template;
	}	
	
	public static String getUserStatus(String cid) {
		String status = "";
	    IGtPush push = new IGtPush(pushHost, appKey, masterSecret);
	    IQueryResult abc = push.getClientIdStatus(appId, cid);
	    status = abc.getResponse().get("result").toString().trim();
	    return status;
	}
	
	
	
	
	@SuppressWarnings("unchecked")
	public static void main(String[] args) 
			throws Exception {
		
		String clientId = "242fb15a9814815d86bf57bc48d1f9e6";
		
		getUserStatus(clientId);
		
		
		
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("cid", clientId);
		
		/*透传消息格式
		 *  title : 标题   :  通知栏标题
		 *  text  : 内容   :  通知栏内容
		 *  
		 *  说明： title 和 text 应该是成对出现，如果没有 title,text ,则只会有 todo操作的功能，只有
		 *  title和text都有值的情况下，才会弹出提示框信息
		 *  
		 *  todo  : 后续操作   get_reminds  = 需要获取闹钟信息列表.
		 *   
		 *  
		 */
		 HashMap<String, String> tranParams = new HashMap<String, String>();
		 

		 tranParams.put("is_show", "true");		
		 tranParams.put("action", "msg");		
		 tranParams.put("order_id", "");
		 tranParams.put("remind_title", "辛苦辛苦");
		 tranParams.put("remind_content", "测试action=dispatch 后台第2条");

		 
		 String jsonParams = GsonUtil.GsonString(tranParams);
		 
//		 tranParams = GsonUtil.GsonToObject(jsonParams, HashMap.class);
//		 
//		 System.out.println("is_show=" + tranParams.get("is_show").toString());

//		 
		params.put("transmissionContent", jsonParams);
		boolean flag = PushUtil.AndroidPushToSingle(params);
	System.out.println(flag);	
//		PushUtil.IOSPushToSingle(params, "alertClock");
		
//		params = new HashMap<String, String>();
//		params.put("cid", clientId);
//		params.put("title", "测试推送消息");
//		params.put("msgContent", "会议安排安排");
//		PushUtil.AndroidPushNotificationToSingle(params);

	}	

}
