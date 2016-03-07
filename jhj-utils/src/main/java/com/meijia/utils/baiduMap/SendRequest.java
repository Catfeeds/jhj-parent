package com.meijia.utils.baiduMap;

import java.io.IOException;
import java.net.URLDecoder;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

/**
 *
 * @author :hulj
 * @Date : 2015年6月25日上午10:04:07
 * @Description: TODO
 *
 */
public class SendRequest {
	/*
	 * 发送get请求
	 */
	public  static String get(String url) {
		 //get请求的返回结果，xml格式的字符串
		String xmlResult = null;
		 CloseableHttpClient httpclient = HttpClients.createDefault();  
	        try {   
	            // 创建httpget.    
	            HttpGet httpget = new HttpGet(url);  
	            System.out.println("request " + httpget.getURI());  
	            // 执行get请求.    
	            CloseableHttpResponse response = httpclient.execute(httpget);  
	            try {  
	                // 获取响应实体     
	                HttpEntity entity = response.getEntity();  
	                if (entity != null) {  
	                    xmlResult = URLDecoder.decode(EntityUtils.toString(entity), "utf-8");
	                }  
	            } finally {  
	                response.close();  
	            }  
	        } catch (Exception e) {  
	            e.printStackTrace();  
	        } finally {  
	            // 关闭连接,释放资源    
	            try {  
	                httpclient.close();  
	            } catch (IOException e) {  
	                e.printStackTrace();  
	            }  
	        }  
	        return xmlResult;
	}
}
