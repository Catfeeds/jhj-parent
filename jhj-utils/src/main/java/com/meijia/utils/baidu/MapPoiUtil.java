package com.meijia.utils.baidu;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.meijia.utils.BeanUtilsExp;
import com.meijia.utils.HttpClientUtil;
import com.meijia.utils.MathBigDecimalUtil;

/**
 * 计算经纬度距离类
 * 提供一种不需要通过百度api来计算经纬度距离的，因为每次调用百度api，消耗较大
 */

public class MapPoiUtil {
	
	
	public static List<BaiduPoiVo> getMapRouteMatrix(String fromLat, String fromLng, List<BaiduPoiVo> destAddrs) throws Exception {
		List<BaiduPoiVo> resultAddrs = new ArrayList<BaiduPoiVo>();
		
		
		for(int i =0 ; i < destAddrs.size(); i++) {
			BaiduPoiVo item = destAddrs.get(i);
			item.setDurationText("");
			int distance = poiDistance(fromLng, fromLat, item.getLng(), item.getLat());
			
			item.setDistanceValue(distance);
			
			if (distance > 1000) {
				BigDecimal d = new BigDecimal(distance);
				BigDecimal t = new BigDecimal(1000);
				BigDecimal r = MathBigDecimalUtil.div(d, t);
				String distanceText = MathBigDecimalUtil.round2(r);
				item.setDistanceText(distanceText + "公里");
			} else {
				item.setDistanceText(String.valueOf(distance) + "米");
			}
			resultAddrs.add(item);
		}

		return resultAddrs;
	}
	

	
	/*
	 * 对结果集排序，索引为0的 第一个对象，即为最近距离的 
	 */
	
	
	/*
	 *  2016-09-20 连工修正
	 *  距离符合20公里内即可
	 */
	
	public static List<BaiduPoiVo>  getMinDest(List<BaiduPoiVo> resultAddrs){
		
		int maxDistance = 20000;
		
		List<BaiduPoiVo> firstList = new ArrayList<BaiduPoiVo>();
		
		//取得 符合20公里  的Vo
		for (int i = 0; i < resultAddrs.size(); i++) {
			BaiduPoiVo baiduPoiVo = resultAddrs.get(i);

			if(baiduPoiVo.getDistanceValue() < maxDistance){
				firstList.add(baiduPoiVo);
			}
		}
		
		if(firstList.size() > 0){
			Collections.sort(firstList, new Comparator<BaiduPoiVo>() {
				public int compare(BaiduPoiVo s1, BaiduPoiVo s2) {
					return Integer.valueOf(s1.getDistanceValue()).compareTo(s2.getDistanceValue());
				}
			});
		}

		return firstList;
	}
	
	/**
	 * 计算地球上任意两点(经纬度)距离
	 * 
	 * @param long1
	 *            第一点经度
	 * @param lat1
	 *            第一点纬度
	 * @param long2
	 *            第二点经度
	 * @param lat2
	 *            第二点纬度
	 * @return 返回距离 单位：米
	 */
	public static Integer poiDistance(String lng1, String lat1, String lng2, String lat2) {
		Integer poiDistance = 0;
		Double dlng1 = Double.valueOf(lng1);
		Double dlat1 = Double.valueOf(lat1);
		Double dlng2 = Double.valueOf(lng2);
		Double dlat2 = Double.valueOf(lat2);
		Double d = Distance(dlng1, dlat1, dlng2, dlat2);
		
		poiDistance = d.intValue();
		
		return poiDistance;
	}
	
	
	public static double Distance(double long1, double lat1, double long2, double lat2) {
		double a, b, R;
		R = 6378137; // 地球半径
		lat1 = lat1 * Math.PI / 180.0;
		lat2 = lat2 * Math.PI / 180.0;
		a = lat1 - lat2;
		b = (long1 - long2) * Math.PI / 180.0;
		double d;
		double sa2, sb2;
		sa2 = Math.sin(a / 2.0);
		sb2 = Math.sin(b / 2.0);
		d = 2 * R * Math.asin(Math.sqrt(sa2 * sa2 + Math.cos(lat1) * Math.cos(lat2) * sb2 * sb2));
		return d;
	}
	
	
	public static void main(String[] args) {
		//计算距离, 并且要做大于5的文本切割
		String fromLat = "39.988612";
		String fromLng = "116.420308";
		
		//需要计算的地址列表
		List<BaiduPoiVo> destAddrs = new ArrayList<BaiduPoiVo>();
		BaiduPoiVo d1 = new BaiduPoiVo();
		d1.setLat("39.796344");
		d1.setLng("116.357301");
		d1.setName("大兴西红门");
		destAddrs.add(d1);
		
//		BaiduPoiVo d2 = new BaiduPoiVo();
//		d2.setLat("39.915285");
//		d2.setLng("116.403857");	
//		d2.setName("天安门");
//		destAddrs.add(d2);
//		
//		//116.400532,40.00077  奥林匹克公园
//		BaiduPoiVo d3 = new BaiduPoiVo();
//		d3.setLat("40.00077");
//		d3.setLng("116.400532");	
//		d3.setName("奥林匹克公园");	
//		destAddrs.add(d3);
//
//		//116.315732,40.016023 圆明园
//		BaiduPoiVo d4 = new BaiduPoiVo();
//		d4.setLat("40.016023");
//		d4.setLng("116.400532");	
//		d4.setName("圆明园");		
//		destAddrs.add(d4);
//		
//		//116.216846,40.00917  植物园
//		BaiduPoiVo d5 = new BaiduPoiVo();
//		d5.setLat("40.00917");
//		d5.setLng("116.216846");	
//		d5.setName("植物园");		
//		destAddrs.add(d5);
//		
//		//116.620724,40.061982 首都国际机场
//		BaiduPoiVo d6 = new BaiduPoiVo();
//		d6.setLat("40.061982");
//		d6.setLng("116.620724");	
//		d6.setName("首都国际机场");	
//		destAddrs.add(d6);
//		
//		//116.383284,39.870869 北京南站
//		BaiduPoiVo d7 = new BaiduPoiVo();
//		d7.setLat("39.870869");
//		d7.setLng("116.383284");	
//		d7.setName("北京南站");		
//		destAddrs.add(d7);
//		
//		//116.433302,39.910286 北京站
//		BaiduPoiVo d8 = new BaiduPoiVo();
//		d8.setLat("39.910286");
//		d8.setLng("116.433302");	
//		d8.setName("北京站");	
//		destAddrs.add(d8);
//		
//		//116.329242,39.900545 北京西站
//		BaiduPoiVo d9 = new BaiduPoiVo();
//		d9.setLat("39.900545");
//		d9.setLng("116.329242");	
//		d9.setName("北京西站");	
//		destAddrs.add(d9);
//		
//		//117.649823,39.033812 塘沽站
//		BaiduPoiVo d10 = new BaiduPoiVo();
//		d10.setLat("39.033812");
//		d10.setLng("117.649823");	
//		d10.setName("塘沽站");	
//		destAddrs.add(d10);
//		
//		//121.810487,31.156731 上海浦东国际机场
//		BaiduPoiVo d11 = new BaiduPoiVo();
//		d11.setLat("31.156731");
//		d11.setLng("121.810487");	
//		d11.setName("上海浦东国际机场");	
//		destAddrs.add(d11);
//		
//		//108.395552,22.792567 广西南宁青秀山
//		BaiduPoiVo d12 = new BaiduPoiVo();
//		d12.setLat("22.792567");
//		d12.setLng("108.395552");	
//		d12.setName("广西南宁青秀山");	
//		destAddrs.add(d12);
		
		
		
		try {
			List<BaiduPoiVo> resultAddrs = MapPoiUtil.getMapRouteMatrix(fromLat, fromLng, destAddrs);
			
			Collections.sort(resultAddrs, new Comparator<BaiduPoiVo>() {
			    public int compare(BaiduPoiVo s1, BaiduPoiVo s2) {
			        return Integer.valueOf(s1.getDistanceValue()).compareTo(s2.getDistanceValue());
			    }
			});
			
			BaiduPoiVo v = null;
			for (int j = 0; j < resultAddrs.size(); j++) {
				v = resultAddrs.get(j);
				System.out.println(v.getName() + "---" + v.getDistanceText() + "--- " +  v.getDistanceValue() + "--" + v.getDurationText() + "---" + v.getDurationValue());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}