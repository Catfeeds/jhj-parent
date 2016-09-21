package com.meijia.utils.serviceCharge;

import java.io.IOException;
import java.math.BigDecimal;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONException;
import com.meijia.wx.utils.MD5Util;

/**
 *
 * @author :hulj
 * @Date : 2015年10月13日下午7:58:33
 * @Description: 
 *		
 *		手机话费充值 ，工具类
 *
 */	
public class PhoneReChargeUtil {
	
	private static Log log = LogFactory.getLog(PhoneReChargeUtil.class);
	
	
	/*
	 *  话费充值-- 发起充值
	 * 	@param phone 		待充值手机号
	 *  @param price 		充值面值
	 *  
	 *  @param orderNo 		该充值订单的 订单号 ,保证该值 能唯一确定该订单即可, 
	 *  					即两次请求，不能是相同的订单 号
	 *  
	 *  					服务商要求： 商家订单号，	8到32	位数字或字母组合（注意：商家不能提交相同的订单号），值由您来设置				
	 * 		
	 */
	public static String paySubmit(String phone,int price,String orderNo){
		
		//手机话费充值 接口
		String phoneRechargeUrl = ApixConfigUtil.getInstance().getResourceBundle().getString("url-phone-recharge");
		//手机话费充值所需 key (服务商提供，可申请)
		String phoneKey = ApixConfigUtil.getInstance().getResourceBundle().getString("apix-key-phone");
		
		//充值成功后的回调
		String callBackUrl =  ApixConfigUtil.getInstance().getResourceBundle().getString("url-phone-callback");
		
		//测试环境地址
		
//		String callBackUrl = "http://www.jia-he-jia.com/jhj-app/app/order/recharge_real.json";
		
		//MD5加密,32位小写签名
		String sign = MD5Util.MD5(phone+price+orderNo).toLowerCase();
		
		phoneRechargeUrl = phoneRechargeUrl 
				+"?phone="+phone
				+"&price="+price
				+"&orderid="+orderNo
				+"&sign="+sign
				+"&callback_url="+callBackUrl;
		
		System.out.println("=============APIX URL ============"+phoneRechargeUrl);
		
		HttpClient httpClient = new HttpClient();
		
		GetMethod getMethod = new GetMethod(phoneRechargeUrl);
		
		getMethod.addRequestHeader("accept", "application/json");
		getMethod.addRequestHeader("content-type", "application/json");
		getMethod.addRequestHeader("apix-key", phoneKey);
		
		int statusCode;
		String response = "";
		try {
			statusCode = httpClient.executeMethod(getMethod);
			if (statusCode == HttpStatus.SC_OK) {
				response = getMethod.getResponseBodyAsString();
				System.out.println(response);
			} else {
				log.debug("响应状态码 = " + getMethod.getStatusCode());
			}
		} catch (IOException e) {
			log.error("发生网络异常", e);
			e.printStackTrace();
		} finally {
			if (getMethod != null) {
				getMethod.releaseConnection();
				getMethod = null;
			}
		}
		
		
		return response;
	}
	
	/*
	 * 话费充值接口-- 订单状态 查询 接口
	 * 
	 * 	@parma orderNo  我们自己的 订单号
	 * 
	 * 
	 * 	返回报文：
 	 * 	Code 为0 表示请求成功 其他为失败
		Msg 表示请求成功或者失败信息
		Data：
		    UserOrderId： 商家订单
		    State： 0表示充值中 1表示成功 其他为失败
		    Price： 充值金额
		    Cost： 商品价格
	 * 
	 */
	public static String getRechargeDetail(String orderNo){
		
		//手机话费  订单状态查询  接口
		String phoneOrderStateUrl = ApixConfigUtil.getInstance().getResourceBundle().getString("url-phone-detail");
		
		phoneOrderStateUrl = phoneOrderStateUrl + "?orderid="+orderNo;
		
		GetMethod method = setRequestHeader(phoneOrderStateUrl);
		
		HttpClient client = new HttpClient();
		
		int statusCode;
		String response = "";
		try {
			
			statusCode = client.executeMethod(method);
			if (statusCode == HttpStatus.SC_OK) {
				response = method.getResponseBodyAsString();
				
				System.out.println(response);
			} else {
				log.debug("响应状态码 = " + method.getStatusCode());
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return response;
	}
	
	/*
	 *	根据手机号码 和 金额 查询 能否充值  
	 */
	public static String isRecharge(String phone,BigDecimal price){
		
		//手机话费    能否充值  接口
		String abRechargeUrl = ApixConfigUtil.getInstance().getResourceBundle().getString("url-phone-ableCharge");
		
		abRechargeUrl = abRechargeUrl + "?phone="+phone
				+"&price="+price.toString();
		
		
		GetMethod method = setRequestHeader(abRechargeUrl);
		
		HttpClient client = new HttpClient();
		
		int statusCode;
		String response = "";
		try {
			
			statusCode = client.executeMethod(method);
			if (statusCode == HttpStatus.SC_OK) {
				response = method.getResponseBodyAsString();
				System.out.println(response);
				
				
				
			} else {
				log.debug("响应状态码 = " + method.getStatusCode());
			}
			
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		
		return response;
	}
	
	/*	
	 * 
	 *  查询 我方  在 apix.cn 的账户余额, 及时发送短信 通知 相关人员, 给 账户充值,避免影响正常使用
	 */
	public static String myRestMoney() throws JSONException{
		
		//手机话费充值所需 key (服务商提供，可申请)
		String phoneKey = ApixConfigUtil.getInstance().getResourceBundle().getString("apix-key-phone");
		
		//查询余额 接口
		String url = ApixConfigUtil.getInstance().getResourceBundle().getString("url-rest-money");
		
		GetMethod method = setRequestHeader(url);
		
		HttpClient client = new HttpClient();
		int statusCode;
		String response = "";
		try {
			
			statusCode = client.executeMethod(method);
			if (statusCode == HttpStatus.SC_OK) {
				response = method.getResponseBodyAsString();
//				System.out.println(response);
				
				net.sf.json.JSONObject fromObject = net.sf.json.JSONObject.fromObject(response);
				if(fromObject.containsKey("Data")){
					
					Object object = fromObject.getJSONObject("Data").get("UserBalance");
					
					if(Double.valueOf(object.toString())<= 500){
						System.out.println(1);
					}
					
				}
				
				System.out.println(2);
//				net.sf.json.JSONObject fromObject = net.sf.json.JSONObject.fromObject(response);
//				
//				System.out.println(fromObject.containsKey("Data"));
//				
//				Object object = fromObject.getJSONObject("Data").get("UserBalance");
//				
//				System.out.println(object);
				
				
			} else {
				log.debug("响应状态码 = " + method.getStatusCode());
			}
			
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		
		return response;
	}
	
	
	
	/*
	 * 设置 通用 请求 头 
	 * @param url 拼接好的 url地址
	 */
	private static GetMethod setRequestHeader(String url){
		
		//手机话费充值所需 key (服务商提供，可申请)
		String phoneKey = ApixConfigUtil.getInstance().getResourceBundle().getString("apix-key-phone");
		
		GetMethod getMethod = new GetMethod(url);
		
		getMethod.addRequestHeader("accept", "application/json");
		getMethod.addRequestHeader("content-type", "application/json");
		getMethod.addRequestHeader("apix-key", phoneKey);
		
		return getMethod;
	}
	
	
	public static void main(String[] args) throws JSONException {
//		myRestMoney();
		
//		http://p.apix.cn/apixlife/pay/phone/phone_recharge
//			?phone=13521193653&price=1.00
//			&orderid=656299887209480192
//			&sign=c6c3022cc030ff978c8abf407a335ead
//			&callback_url=http://www.jia-he-jia.com/jhj-app/app/order/recharge_real.json
		
//		isRecharge("03745719716", new BigDecimal(1));
		
//		System.out.println(MD5Util.MD5("135211936531.0012345678").toLowerCase());
//		
//		paySubmit("13521193653", new BigDecimal(1.33).intValue(), "656299887209480192");
				
//		getRechargeDetail("657824881823449088");
		
		myRestMoney();
		
//		getRechargeDetail("");
	}
	
}
