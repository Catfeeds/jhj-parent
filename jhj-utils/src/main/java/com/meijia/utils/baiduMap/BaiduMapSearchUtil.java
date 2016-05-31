package com.meijia.utils.baiduMap;

import java.util.HashMap;
import java.util.Map;

import com.meijia.utils.HttpClientUtil;
import com.meijia.utils.StringUtil;
import com.meijia.wx.utils.JsonUtil;

/**
 *
 * @author :hulj
 * @Date : 2016年5月25日下午4:40:27
 * @Description: 
 *
 */		
public class BaiduMapSearchUtil {
	
	
	/**
	 *   判断用户 输入的 地址是否在 北京市 范围内
	  * 
	  * 	
	  *   http://api.map.baidu.com/place/v2/search
	  *   
	  *   通用的必选 参数  ： 
	  *   	   q(query) 		关键字
	  *   		 scope 	  默认为1 	检索结果详细程度。取值为1 或空，则返回基本信息；取值为2，返回检索POI详细信息		  	
	  *   		 ak				用户的访问密钥，必填项。v2之前该属性为key
	  *   
	  *   城市内检索请求参数：
	  *   		 region  		必选		检索区域（市级以上行政区域），如果取值为“全国”或某省份，则返回指定区域的POI及数量。	
	  *   		 city_limit		可选（默认false）            是否只返回指定region（城市）内的POI，true(是)，false（否）。
	  * 
	 */
	public static boolean isInBeijingForUserInput(String userInput){
		
		if(StringUtil.isEmpty(userInput)){
			return false;
		}
		
		Map<String, String> params = new HashMap<String, String>();
		
		params.put("query", userInput);
		params.put("output", "json");
		params.put("ak", "2sshjv8D4AOoOzozoutVb6WT");
		
		params.put("region","北京");
		params.put("city_limit", "true");	//是否限定只在北京范围内查找
		params.put("page_num", "0");	// 为了 返回值中 有  total 参数
		
		String url = "http://api.map.baidu.com/place/v2/search";
		String getResult = HttpClientUtil.get(url, params);
		
		Map<String, Object> m = JsonUtil.stringToMap(getResult);
		
		if(Double.valueOf(m.get("total").toString()) > 0){
			System.out.println("添加地址在北京市范围内,可以添加");
			return true;
		}else{
			//未在北京市范围内 找到   结果, 返回 false
			System.out.println("地址不在北京市范围内,不可以添加");
			return false;
		}
		
	}
	
	public static void main(String[] args) {
		
		isInBeijingForUserInput("天安门");
		
	}
	
}
