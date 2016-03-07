package com.meijia.utils.baiduMap;

import java.net.URLEncoder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 *
 * @author :hulj
 * @Date : 2015年6月25日下午2:57:12
 * @Description: 
 *     已知：门店地址（多个），用户位置
 * 	   	
 *     得到：距离门店10公里以内， 距离门店时间60分钟以内，的 最近 的门店地址 
 */
public class BMapShortestOrgUtils {
	 
	public static String compleUrl(String userAddr,List<String> orgAddrList) throws Exception{
		
		
		String s = URLEncoder.encode("|", "utf-8");
		String url = "http://api.map.baidu.com/direction/v1/routematrix?"
				+ "origins="+userAddr
				+ "&ak=2sshjv8D4AOoOzozoutVb6WT"
				+ "&destinations=";//+s+"西直门"+s+"天通苑北地铁站";
		
		for (int i = 0; i < orgAddrList.size(); i++) {
			url = url+orgAddrList.get(i)+s;
		}
		url = url.substring(0, url.length()-3);
		return url;
	}
	
	/*
	 * 传入参数 ：用户地址（出发地），      
	 * 		       所有目的地 地址 list （门店地址名称list），//TODO 百度 接口，只允许最多5个目的地址
	 *     
	 *    返回 ：到达时间最短 （理论上距离此时也最近），的门店地址名称
	 */
	public static String getNearestOrg(String userAddr, List<String> orgAddrList) throws Exception{
		
		String a = SendRequest.get(BMapShortestOrgUtils.compleUrl(userAddr, orgAddrList));
		//解析报文得到 {距离，时间，门店地址   ...} 的集合
		List<String> destTime = XmlUtils.getShortDestination(a,orgAddrList);
		//得到 符合条件  的 集合
		List<String> list = XmlUtils.getMin(destTime);
		

		Map<Double, String> map  = new HashMap<Double, String>();
		if(list.size()>0){
			for (int i = 0; i < list.size(); i++) {
				String[] strings = list.get(i).split(",");
				//去掉单位，得到时间 的数值
				String num = MyStringUtils.delChinese(strings[1]);
				
				map.put(Double.parseDouble(num), strings[2]);
			}
			//最小的时间数值
			Object key = getMinKey(map);
			//最短时间 对应的门店名称
			String value = map.get(key);
			return value;
		}
		
		return "";
	}
	
	//得到 map中 最小 的  key值
	public static Object getMinKey(Map<Double, String> map) {
		 if (map == null) return null;
		 Set<Double> set = map.keySet();
		 Object[] obj = set.toArray();
		 Arrays.sort(obj);
		 return obj[0];
	}
	
	/*public static void main(String[] args) throws Exception {
		//百度地图api ，最多允许 5个终点
		List<String> orgAddrList = new ArrayList<String>();
		orgAddrList.add("天安门东地铁站");
		orgAddrList.add("西直门");
		orgAddrList.add("天通苑北地铁站");
		
		String userAddr = "天安门";
		
		System.out.println(getNearestOrg(userAddr,orgAddrList));
		
	}*/
	
	
}
