package com.meijia.wx.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.meijia.utils.HttpClientUtil;
import com.meijia.utils.StringUtil;
import com.meijia.utils.redis.RedisUtil;

/**
 * 微信支付配置
 *
 */
public class WxUtil {

	// 公众号id
	public static String appId = WxConfig.getInstance().getRb().getString("wx_appid");

	// 商户id
	public static String mchId = WxConfig.getInstance().getRb().getString(
			"wx_mch_id");

	// 密钥
	public static String appSecret = WxConfig.getInstance().getRb().getString(
			"wx_appsecret");

	// 商户KEY
	public static String wxKey = WxConfig.getInstance().getRb().getString("wx_key");

	//统一支付接口
	public static String payUrl = WxConfig.getInstance().getRb().getString("wx_pay_url");
	

	//微信get_code_url
	public static String getCodeUrl = WxConfig.getInstance().getRb().getString("wx_get_code_url");

	//微信get_openid_url
	public static String getOpenIdUrl = WxConfig.getInstance().getRb().getString("wx_get_openid_url");

	//微信get_refresh_token_url
	public static String getRefreshTokenUrl = WxConfig.getInstance().getRb().getString("wx_get_refresh_token_url");

	//微信get_check_token_url
	public static String getCheckTokenUrl = WxConfig.getInstance().getRb().getString("wx_check_token_url");

	public static String getNotifyUrl(Short orderType) {
		String notifyUrl = "";
		switch (orderType) {
			case (short)0 :
				notifyUrl = WxConfig.getInstance().getRb().getString("wx_order_notify_url");
				break;
			case (short)1 :
				notifyUrl = WxConfig.getInstance().getRb().getString("wx_ordercard_notify_url");
				break;
			case (short)3 :
				notifyUrl = WxConfig.getInstance().getRb().getString("wx_orderext_notify_url");
				break;	
			case (short)4:
				notifyUrl = WxConfig.getInstance().getRb().getString("wx_period_order_notify_url");
				break;
			//TODO  话费充值和 订单 支付  此处 共用 一个 通知 类
			case (short)6 :
				notifyUrl = WxConfig.getInstance().getRb().getString("wx_order_notify_url");
				break;
			default :
				notifyUrl = WxConfig.getInstance().getRb().getString("wx_order_notify_url");
				break;
		}

		return notifyUrl;
	}

	/**
	 * 获取access_token
	 * 访问 wx_get_openid_url=https://api.weixin.qq.com/sns/oauth2/access_token
	 * @return
	 */
	public static Map<String, Object>  getAccessToken(String code) {
		String result = "";
		try {
			
			String cacheAccessTokenStr = RedisUtil.getInstance().strings().get("wx_access_token");
			if (!StringUtil.isEmpty(cacheAccessTokenStr)) {
				return JsonUtil.stringToMap(cacheAccessTokenStr);
			}
			
			Map<String, String> map = new HashMap<String, String>();
			map.put("appid", WxUtil.appId);
			map.put("secret", WxUtil.appSecret);
			map.put("code", code);
			map.put("grant_type", "authorization_code");
			String openIdUrl = WxUtil.getOpenIdUrl;
			System.out.println(openIdUrl);
			System.out.println("");
			result = HttpClientUtil.get(openIdUrl, map);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return JsonUtil.stringToMap(result);
	}

	/**
	 * 刷新access_token（如果需要）
	 * wx_get_refresh_token_url=https://api.weixin.qq.com/sns/oauth2/refresh_token
	 * @return
	 */
	public static Map<String, Object> getRefreshToken(String refreshToken) {
		String result = "";
		try {
			Map<String, String> map = new HashMap<String, String>();
			map.put("appid", WxUtil.appId);
			map.put("refresh_token", refreshToken);
			map.put("grant_type", "refresh_token");
			String refreshUrl = WxUtil.getRefreshTokenUrl;
			result = HttpClientUtil.get(refreshUrl, map);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return JsonUtil.stringToMap(result);
	}

	/**
	 * 检验授权凭证（access_token）是否有效
	 * wx_check_token_url=https://api.weixin.qq.com/sns/auth
	 * @param args
	 */
	public static boolean checkAccessToken(String access_token, String openid) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("access_token", access_token);
		map.put("openid", openid);
		String result = HttpClientUtil.get(WxUtil.getCheckTokenUrl, map);
		Map<String, Object> m = JsonUtil.stringToMap(result);
		boolean flog = m.get("errmsg").equals("ok");
		return flog;
	}
	
	
	public static String getNonceStr() {
		Random random = new Random();
		return MD5Util.MD5Encode(String.valueOf(random.nextInt(10000)), "GBK");
	}	
	
}
