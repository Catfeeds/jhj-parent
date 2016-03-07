package com.meijia.utils.baidu;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import net.sf.json.JSONObject;

import com.baidu.yun.core.log.YunLogEvent;
import com.baidu.yun.core.log.YunLogHandler;
import com.baidu.yun.push.auth.PushKeyPair;
import com.baidu.yun.push.client.BaiduPushClient;
import com.baidu.yun.push.constants.BaiduPushConstants;
import com.baidu.yun.push.exception.PushClientException;
import com.baidu.yun.push.exception.PushServerException;
import com.baidu.yun.push.model.PushMsgToAllRequest;
import com.baidu.yun.push.model.PushMsgToAllResponse;
import com.baidu.yun.push.model.PushMsgToSingleDeviceRequest;
import com.baidu.yun.push.model.PushMsgToSingleDeviceResponse;

/**
 * 百度配置文件
 *
 */
public class BaiduUtil {

	// 百度推送 apiKey
	public static String apiKey = BaiduConfigUtil.getInstance().getRb().getString("apiKey");

	// 百度推送 secretKey
	public static String secretKey = BaiduConfigUtil.getInstance().getRb().getString("secretKey");
	
	// 百度推送消息的有效时间
	public static int msgExpires = 3600;
	
	/**
	 * 百度推送IOS 多个设备推送
	 * @return
	 * @throws PushClientException 
	 * @throws PushServerException 
	 */
	public static boolean IOSPushNotificationToMultiDevice(String[] channelIds, String msgContent, Map<String, String> params) throws PushClientException, PushServerException {
//		String apiKey = "Y31eOZA3t0OH8YfTQg9rKefl";
//		String secretKey = "nUaYDsXIc2sNRxS01R3svQ3kCKSvXgXU";		
		PushKeyPair pair = new PushKeyPair(apiKey, secretKey);
		BaiduPushClient pushClient = new BaiduPushClient(pair,
				BaiduPushConstants.CHANNEL_REST_URL);
		
		pushClient.setChannelLogHandler(new YunLogHandler() {
			@Override
			public void onHandle(YunLogEvent event) {
				System.out.println(event.getMessage());
			}
		});
		
		try {
			// 4. specify request arguments
			// make IOS Notification
			JSONObject notification = new JSONObject();
			JSONObject jsonAPS = new JSONObject();
			jsonAPS.put("alert", msgContent);
//			jsonAPS.put("sound", "ttt"); // 设置通知铃声样式，例如"ttt"，用户自定义。
			notification.put("aps", jsonAPS);

			for(Entry<String,String> entry : params.entrySet()){
				 
		           String strkey = entry.getKey();
		 
		           String strval = entry.getValue();
		          
		           notification.put(strkey, strval);
		    }			
			
			for (int i = 0; i < channelIds.length; i ++) {
				String channelId = channelIds[i];
				PushMsgToSingleDeviceRequest request = new PushMsgToSingleDeviceRequest()
						.addChannelId(channelId)
						.addMsgExpires(msgExpires) // 设置message的有效时间
						.addMessageType(1)// 1：通知,0:透传消息.默认为0 注：IOS只有通知.
						.addMessage(notification.toString()).addDeployStatus(1) // IOS,DeployStatus=> 1: Developer 2: Production.
						.addDeviceType(4);// deviceType => 3:android, 4:ios
				// 5. http request
				PushMsgToSingleDeviceResponse response = pushClient
						.pushMsgToSingleDevice(request);
				// Http请求结果解析打印
				System.out.println("msgId: " + response.getMsgId() + ",sendTime: "
						+ response.getSendTime());
			}
		} catch (PushClientException e) {
			/*
			 * ERROROPTTYPE 用于设置异常的处理方式 -- 抛出异常和捕获异常,'true' 表示抛出, 'false' 表示捕获。
			 */
			if (BaiduPushConstants.ERROROPTTYPE) {
				throw e;
			} else {
				e.printStackTrace();
			}
		} catch (PushServerException e) {
			if (BaiduPushConstants.ERROROPTTYPE) {
				throw e;
			} else {
				System.out.println(String.format(
						"requestId: %d, errorCode: %d, errorMessage: %s",
						e.getRequestId(), e.getErrorCode(), e.getErrorMsg()));
			}
		}		
			
		
		return true;
	}
	
	/**
	 * 百度推送IOS 单个设备推送
	 * @return
	 * @throws PushClientException 
	 * @throws PushServerException 
	 */
	public static boolean IOSPushNotificationToAll(String msgContent, Map<String, String> params) throws PushClientException, PushServerException {
//		String apiKey = "Y31eOZA3t0OH8YfTQg9rKefl";
//		String secretKey = "nUaYDsXIc2sNRxS01R3svQ3kCKSvXgXU";		
		PushKeyPair pair = new PushKeyPair(apiKey, secretKey);
		BaiduPushClient pushClient = new BaiduPushClient(pair,
				BaiduPushConstants.CHANNEL_REST_URL);
		
		pushClient.setChannelLogHandler(new YunLogHandler() {
			@Override
			public void onHandle(YunLogEvent event) {
				System.out.println(event.getMessage());
			}
		});
		
		try {
			// 4. specify request arguments
			// make IOS Notification
			JSONObject notification = new JSONObject();
			JSONObject jsonAPS = new JSONObject();
			jsonAPS.put("alert", msgContent);
//			jsonAPS.put("sound", "ttt"); // 设置通知铃声样式，例如"ttt"，用户自定义。
			notification.put("aps", jsonAPS);
			
			for(Entry<String,String> entry : params.entrySet()){
				 
		           String strkey = entry.getKey();
		 
		           String strval = entry.getValue();
		          
		           notification.put(strkey, strval);
		    }
			
			
//			notification.put("key1", "value1");
//			notification.put("key2", "value2");
			
			PushMsgToAllRequest request = new PushMsgToAllRequest()
				.addMsgExpires(msgExpires)
				.addMessageType(1)
				.addMessage(notification.toString())
				.addSendTime(System.currentTimeMillis() / 1000 + 120) // 设置定时推送时间，必需超过当前时间一分钟，单位秒.实例2分钟后推送
				.addDepolyStatus(1).addDeviceType(4);
			// 5. http request
			PushMsgToAllResponse response = pushClient.pushMsgToAll(request);
			// Http请求结果解析打印
			System.out.println("msgId: " + response.getMsgId() + ",sendTime: "
			+ response.getSendTime() + ",timerId: "
			+ response.getTimerId());
		} catch (PushClientException e) {
			/*
			 * ERROROPTTYPE 用于设置异常的处理方式 -- 抛出异常和捕获异常,'true' 表示抛出, 'false' 表示捕获。
			 */
			if (BaiduPushConstants.ERROROPTTYPE) {
				throw e;
			} else {
				e.printStackTrace();
			}
		} catch (PushServerException e) {
			if (BaiduPushConstants.ERROROPTTYPE) {
				throw e;
			} else {
				System.out.println(String.format(
						"requestId: %d, errorCode: %d, errorMessage: %s",
						e.getRequestId(), e.getErrorCode(), e.getErrorMsg()));
			}
		}		
			
		
		return true;
	}	
	
	
	public static void main(String[] args) 
			throws PushClientException,PushServerException {
		
		Map<String, String> params = new HashMap<String, String>();
//		params.put("url", "http://www/yougeguanjia.com/onecare-oa/upload/html/2.html");
		params.put("msgid", "2");
//		String[] channelIds = {"5411241166191134005", "4880573112432126390"};
		String[] channelIds = {"4880573112432126390"};
		String msgContent = "仅售180元，价值158元首席设计师洗剪吹，长短发不限！（另有其他套餐可选）";
		BaiduUtil.IOSPushNotificationToMultiDevice(channelIds, msgContent, params);

//		BaiduUtil.IOSPushNotificationToAll("测试url2", params);
	}	

}
