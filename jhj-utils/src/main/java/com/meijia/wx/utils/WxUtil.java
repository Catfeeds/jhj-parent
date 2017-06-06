package com.meijia.wx.utils;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.json.JSONObject;

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
	
	public static String getAccess_token() {
		
//		Map<String,String> map = new HashMap<String,String>();
		
        String APP_ID = "wx1da3f16a433d8bd8";//微信id
        String APP_SECRET="46a8b0480da4a2338072338478a84fb5";//微信秘钥
        //微信令牌请求网址(由微信提供)
        String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid="+APP_ID+"&secret="+APP_SECRET;
        
        String accessToken = null;
        try {
            URL urlGet = new URL(url);
            HttpURLConnection http = (HttpURLConnection) urlGet.openConnection();
            http.setRequestMethod("GET"); // 必须是get方式请求
            http.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            System.setProperty("sun.net.client.defaultConnectTimeout", "30000");// 连接超时30秒
            System.setProperty("sun.net.client.defaultReadTimeout", "30000"); // 读取超时30秒
            http.connect();
            InputStream is = http.getInputStream();
            int size = is.available();
            byte[] jsonBytes = new byte[size];
            is.read(jsonBytes);
            String message = new String(jsonBytes, "UTF-8");
            JSONObject demoJson = new JSONObject(message);
            accessToken = demoJson.getString("access_token");
//            expiresIn = demoJson.getString("expires_in");
            
//            map.put("access_token", accessToken);
//            map.put("expires_in", expiresIn);
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(accessToken);
        return accessToken;
    }
	
	
	 public static String getTicket(String access_token) {
        String ticket = null;
        //获取票据的网址(由微信提供)
        String url = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token="+access_token+"&type=jsapi";
        
        try {
            URL urlGet = new URL(url);
            HttpURLConnection http = (HttpURLConnection) urlGet.openConnection();
            http.setRequestMethod("GET"); // 必须是get方式请求
            http.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            System.setProperty("sun.net.client.defaultConnectTimeout", "30000");// 连接超时30秒
            System.setProperty("sun.net.client.defaultReadTimeout", "30000"); // 读取超时30秒
            http.connect();
            InputStream is = http.getInputStream();
            int size = is.available();
            byte[] jsonBytes = new byte[size];
            is.read(jsonBytes);
            String message = new String(jsonBytes, "UTF-8");
            JSONObject demoJson = new JSONObject(message);
            ticket = demoJson.getString("ticket");
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ticket;
    }
	
}
