package com.jhj.action.app;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.alibaba.fastjson.JSON;
import com.jhj.common.ConstantMsg;
import com.jhj.common.Constants;
import com.meijia.utils.vo.AppResultData;
import com.meijia.wx.utils.JsonUtil;
import com.meijia.wx.utils.Sha1Util;
import com.meijia.wx.utils.WxUtil;

@Controller
@RequestMapping("/app")
public class WxShare {
	
	@RequestMapping(value="/wxShare.json", method=RequestMethod.POST)
	public AppResultData<Object> wxShare(String url,HttpServletRequest request) throws Exception{
		
		SortedMap<String, String> map = new TreeMap<String, String>();
		
		String timestamp = String.valueOf(System.currentTimeMillis()/1000);
		String nonceStr = Sha1Util.getNonceStr();
		String jsapiTicket = getTicket(getAccess_token());
		
		
		map.put("timestamp", timestamp);
		map.put("noncestr", nonceStr);
		map.put("jsapi_ticket", jsapiTicket);
		map.put("url", url);
		
		String string1 = "jsapi_ticket=" + jsapiTicket +
                 "&noncestr=" + nonceStr +
                 "&timestamp=" + timestamp +
                 "&url=" + url;
		
		map.put("signature", Sha1Util.getSha1(string1));
		
		map.put("appId", "wx1da3f16a433d8bd8");
		return new AppResultData<Object>(Constants.SUCCESS_0, ConstantMsg.SUCCESS_0_MSG,map);
	}
	
	public static String getAccess_token() {
		
//		Map<String,String> map = new HashMap<String,String>();
		
        String APP_ID = "wx1da3f16a433d8bd8";//微信id
        String APP_SECRET="46a8b0480da4a2338072338478a84fb5";//微信秘钥
        //微信令牌请求网址(由微信提供)
        String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid="+APP_ID+"&secret="+APP_SECRET;
        
        String accessToken = null;
//        String expiresIn = null;
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
