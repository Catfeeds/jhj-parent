package com.meijia.utils;

import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.Set;

import com.cloopen.rest.sdk.CCPRestSDK;
import com.cloopen.rest.sdk.CCPRestSmsSDK;

public class SmsUtil {

	/**
	 * 云通信发送短信方法
	 * @param mobile      手机号
	 * @param templeId   短信模板
	 * @param content     替换内容
	 * @return  hashmap
	 * 			statusCode :  000000 = 成功  ，其他为失败
	 * 			msg	  :  返回参数字符串
	 */
	@SuppressWarnings("null")
	public static HashMap<String, String>  SendSms(String mobile, String templeId, String[] content) {

		ResourceBundle rb = ResourceBundle.getBundle("sms_config");
		String accountSid = rb.getString("accountSid");
		String accountToken = rb.getString("accountToken");
		String appid = rb.getString("appId");

		String url = rb.getString("url");
		String port = rb.getString("port");

		//初始化SDK
		CCPRestSmsSDK restAPI = new CCPRestSmsSDK();

		//******************************注释*********************************************
		//*初始化服务器地址和端口                                                       *
		//*沙盒环境（用于应用开发调试）：restAPI.init("sandboxapp.cloopen.com", "8883");*
		//*生产环境（用户应用上线使用）：restAPI.init("app.cloopen.com", "8883");       *
		//*******************************************************************************
		restAPI.init(url, port);

		//******************************注释*********************************************
		//*初始化主帐号和主帐号令牌,对应官网开发者主账号下的ACCOUNT SID和AUTH TOKEN     *
		//*ACOUNT SID和AUTH TOKEN在登陆官网后，在“应用-管理控制台”中查看开发者主账号获取*
		//*参数顺序：第一个参数是ACOUNT SID，第二个参数是AUTH TOKEN。                   *
		//*******************************************************************************
		restAPI.setAccount(accountSid, accountToken);

		//******************************注释*********************************************
		//*初始化应用ID                                                                 *
		//*测试开发可使用“测试Demo”的APP ID，正式上线需要使用自己创建的应用的App ID     *
		//*应用ID的获取：登陆官网，在“应用-应用列表”，点击应用名称，看应用详情获取APP ID*
		//*******************************************************************************
		restAPI.setAppId(appid);


		//******************************注释****************************************************************
		//*调用发送模板短信的接口发送短信                                                                  *
		//*参数顺序说明：                                                                                  *
		//*第一个参数:是要发送的手机号码，可以用逗号分隔，一次最多支持100个手机号                          *
		//*第二个参数:是模板ID，在平台上创建的短信模板的ID值；测试的时候可以使用系统的默认模板，id为1。    *
		//*系统默认模板的内容为“【云通讯】您使用的是云通讯短信模板，您的验证码是{1}，请于{2}分钟内正确输入”*
		//*第三个参数是要替换的内容数组。																														       *
		//**************************************************************************************************

		//**************************************举例说明***********************************************************************
		//*假设您用测试Demo的APP ID，则需使用默认模板ID 1，发送手机号是13800000000，传入参数为6532和5，则调用方式为           *
		//*result = restAPI.sendTemplateSMS("13800000000","1" ,new String[]{"6532","5"});																		  *
		//*则13800000000手机号收到的短信内容是：【云通讯】您使用的是云通讯短信模板，您的验证码是6532，请于5分钟内正确输入     *
		//*********************************************************************************************************************
		HashMap<String, Object> ccp = null;
		ccp = restAPI.sendTemplateSMS(mobile,templeId ,content);

		HashMap<String, String> result = new HashMap<String, String>();

		String statusCode = (String) ccp.get("statusCode");

		if("000000".equals(statusCode)) {
			result.put("statusCode", statusCode);
			String msg = null;
			HashMap<String,Object> data = (HashMap<String, Object>) ccp.get("data");
			Set<String> keySet = data.keySet();
			for(String key:keySet){
				Object object = data.get(key);
				msg += key +" = "+object.toString();
			}
			result.put("msg", msg);
		} else {
			result.put("statusCode", statusCode);
			result.put("msg", (String) ccp.get("statusMsg"));
		}
		return result;
	}
	
	
	/**
	 *  2015-11-24 11:24:23
	 *  		云通讯发送语音短信验证码
	 *  
	 *  @param mobile  接收验证码的 号码
	 *  
	 *  @param sms     验证码内容，为数字和英文字母，不区分大小写，长度4-8位 
	 *  
	 */
	public static HashMap<String, String> sendYuYinSms(String mobile, String sms){
		
		ResourceBundle rb = ResourceBundle.getBundle("sms_config");
		String accountSid = rb.getString("accountSid");
		String accountToken = rb.getString("accountToken");
		String appid = rb.getString("appId");

		String url = rb.getString("url");
		String port = rb.getString("port");

		//初始化SDK
		CCPRestSDK restAPI = new CCPRestSDK();

		
		
		restAPI.init(url, port);

		restAPI.setAccount(accountSid, accountToken);

		restAPI.setAppId(appid);

		
		/**
		 * 发起语音验证码请求
		 * 
		 * @param verifyCode
		 *            必选参数 验证码内容，为数字和英文字母，不区分大小写，长度4-8位
		 * @param to
		 *            必选参数 接收号码
		 * @param displayNum
		 *            可选参数 显示主叫号码，显示权限由服务侧控制
		 * @param playTimes
		 *            可选参数 循环播放次数，1－3次，默认播放1次
		 * @param respUrl
		 *            可选参数 语音验证码状态通知回调地址，云通讯平台将向该Url地址发送呼叫结果通知
		 * @param lang
		 *            可选参数 语言类型
		 * @param userData
		 *            可选参数 第三方私有数据
		 * voiceVerify(String verifyCode, String to,String displayNum, String playTimes, String respUrl, String lang,String userData)
		 * 
		 * @return HashMap<String,object>
		 */
		
		 HashMap<String,Object> result =  restAPI.voiceVerify(sms, mobile,"","3", "", "zh", "");
		
		 String statusCode =  (String) result.get("statusCode");
		 
		 
		 HashMap<String, String> returnResult = new HashMap<String, String>();
		 
		 
		 if("000000".equals(statusCode)){
			 
			 	returnResult.put("statusCode",statusCode);
			
				String msg = null;
				HashMap<String,String> data = (HashMap<String, String>) result.get("data");
				Set<String> keySet = data.keySet();
				for(String key:keySet){
					Object object = data.get(key);
					msg += key +" = "+object.toString();
				}
			
			    returnResult.put("msg", msg);
				
		}else{
				
			returnResult.put("statusCode", statusCode);
			returnResult.put("msg", (String) result.get("statusMsg"));
//			//异常返回输出错误码和错误信息
//			System.out.println("错误码=" + result.get("statusCode") +" 错误信息= "+result.get("statusMsg"));
		}
		 
		 return returnResult;
		 
	}
	
	
	

	/**
	 * @param args
	 */
	public static void main(String[] args) {

//		String mobile = "18612514665";
//		String templeId = "20911";
		
//		String mobile = "13521193653";
//		String templeId = "29156";
//		String token = RandomUtil.randomNumber();
//
//		System.out.println("token =" + token );
//
////		String[] content = new String[]{token, "10"};
		
		String[] content = new String[]{""};
		HashMap<String, String> result = SmsUtil.SendSms("13701187136", "65012", content);
//
//		System.out.println(result.get("statusCode"));
//		System.out.println(result.get("msg"));
		
		
		/**************语音验证码*********************/
//		String sms = RandomUtil.randomNumber();
//		System.out.println(sms);
//		String mobile = "17090397818";
//				
//		HashMap<String,String> map = SmsUtil.sendYuYinSms(mobile, sms);
//		
//		
//		System.out.println(map.get("statusCode"));
//		System.out.println(map.get("msg"));
		
	}
}
