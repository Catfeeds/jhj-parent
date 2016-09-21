package com.meijia.utils.baidu;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.meijia.utils.HttpClientUtil;

/**
 * 访问百度 API 的类
 */

public class BaiduMapUtil {
	
	
	public static List<BaiduPoiVo> getMapRouteMatrix(String fromLat, String fromLng, List<BaiduPoiVo> destAddrs) throws Exception {
		List<BaiduPoiVo> resultAddrs = new ArrayList<BaiduPoiVo>();
		
		List<BaiduPoiVo> destAddrsMod4 = new ArrayList<BaiduPoiVo>();
		for(int i =0 ; i < destAddrs.size(); i++) {
			destAddrsMod4.add(destAddrs.get(i));
			if (i > 0 && i % 4 == 0 ) {
				System.out.println(i);
				List<BaiduPoiVo> resultDestAddrsMod4 = toMapRouteMatrix(fromLat, fromLng, destAddrsMod4);
				for (int j = 0 ; j < resultDestAddrsMod4.size(); j++) {
					resultAddrs.add(resultDestAddrsMod4.get(j));
				}
				destAddrsMod4 = new ArrayList<BaiduPoiVo>();
			}
		}
		
		//最后也需要再请求一次
		List<BaiduPoiVo> resultDestAddrsMod4 = toMapRouteMatrix(fromLat, fromLng, destAddrsMod4);
		
		for (int j = 0 ; j < resultDestAddrsMod4.size(); j++) {
			resultAddrs.add(resultDestAddrsMod4.get(j));
		}
		
		return resultAddrs;
	}
	
	/**
	 * http://developer.baidu.com/map/index.php?title=webapi/route-matrix-api
	 * Route Matrix API是一套以HTTP形式提供的批量线路查询接口，用于返回多个起点和多个终点间的线路距离和行驶时间。
	 * 该服务并不会返回详细的线路信息 一个起点，到多个终点的间距和时间
	 * 
	 * http://api.map.baidu.com/direction/v1/routematrix?output=json&ak=2sshjv8D4AOoOzozoutVb6WT&origins=39.894585,116.471626&destinations=39.896014,116.47341|39.915285,116.403857
	 * 
	 * @param startAddress
	 *            起点地址名称 或者 经纬度坐标，以,分割，最多传5个点
	 * @param endAddresses
	 *            终点地址名称 或者 经纬度坐标，以,分割，最多传5个点 名称：百度大厦 经纬度：40.056878, 116.30815
	 *            坐标格式为：lat<纬度>,lng<经度>
	 * @param mode
	 *            导航模式，包括：driving（驾车）、walking（步行）
	 * @return
	 * @throws Exception
	 */
	public static List<BaiduPoiVo> toMapRouteMatrix(String fromLat, String fromLng, List<BaiduPoiVo> destAddrs) throws Exception {
				
		List<BaiduPoiVo> resultAddrs = new ArrayList<BaiduPoiVo>();
		
		String url = BaiduConfigUtil.getInstance().getRb().getString("routematrixUrl");
		String ak = BaiduConfigUtil.getInstance().getRb().getString("ak");
		Map<String, String> params = new HashMap<String, String>();
		params.put("output", "json");
		params.put("ak", ak);
		params.put("origins", fromLat + "," + fromLng);
		
		BaiduPoiVo item = null;
		String destinations = "";
		for (int i =0; i < destAddrs.size(); i++) {
			item = destAddrs.get(i);
			destinations += item.getLat() + "," + item.getLng();
			if (i < destAddrs.size() -1 ) {
				destinations += "|";
			}
		}
		
		params.put("destinations", destinations);
		System.out.println(params.toString());
		String getResult = HttpClientUtil.get(url, params);
		System.out.println(getResult);
		JSONObject dataJson;
		try {
			dataJson = new JSONObject(getResult);
			String status = dataJson.getString("status");
			if (!status.equals("0")) return resultAddrs;
			
			JSONObject result = dataJson.getJSONObject("result");
			
			JSONArray elements = result.getJSONArray("elements");
			
			BaiduPoiVo vo = null;
			 
				
			for (int i = 0; i < elements.length(); i++) {
				JSONObject element = elements.getJSONObject(i);
				
				JSONObject duration = element.getJSONObject("duration");
				JSONObject distance = element.getJSONObject("distance");
				
				if (destAddrs.get(i) != null) {
					vo = destAddrs.get(i);
					vo.setDistanceText(distance.getString("text"));
					vo.setDistanceValue(distance.getInt("value"));
					vo.setDurationText(duration.getString("text"));
					vo.setDurationValue(duration.getInt("value"));
					resultAddrs.add(vo);
				}
			}
				
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		
		return resultAddrs;
	}
	
	/*
	 * 对结果集排序，索引为0的 第一个对象，即为最近距离的 
	 */
	
	
	/*
	 * 
	 *  2016年6月1日17:42:11 
	 *  
	 *  	决定 调换成  20 公里 3小时  范围内均可
	 */
	
	public static List<BaiduPoiVo>  getMinDest(List<BaiduPoiVo> resultAddrs, int maxDistance, int maxDuration){
		
		if (maxDistance <= 0 ) maxDistance = 20000;
		
		//2016年5月16日14:51:27  修改为 10公里 120分钟
		if (maxDuration <= 0 ) maxDuration = 3600*3;
		
		List<BaiduPoiVo> firstList = new ArrayList<BaiduPoiVo>();
		
		//取得 符合60分钟。10公里  的Vo
		for (int i = 0; i < resultAddrs.size(); i++) {
			BaiduPoiVo baiduPoiVo = resultAddrs.get(i);
			//10000米，3600秒
			if(baiduPoiVo.getDistanceValue() < maxDistance && baiduPoiVo.getDurationValue() < maxDuration){
				firstList.add(baiduPoiVo);
			}
		}
		
		if(firstList.size() > 0){
			Collections.sort(firstList, new Comparator<BaiduPoiVo>() {
				@Override
				public int compare(BaiduPoiVo s1, BaiduPoiVo s2) {
					return Integer.valueOf(s1.getDistanceValue()).compareTo(s2.getDistanceValue());
				}
			});
		}

		
//		排序之后
//		BaiduPoiVo baiduPoiVo  = initBaiduPoiVo();
//		
//		if(firstList.size() > 0){
//			
//			baiduPoiVo = firstList.get(0);
//		}
		
		/*
		 *  2016年3月17日11:48:27  
		 *  	
		 *  	自动派工时, 
		 *  		1>如果在 距离最近。时间最短的门店 不能找到 合适的 服务人员		
		 *  			则 循环 符合条件的 下一家云店, 继续派工
		 *  
		 *  		2>同时, 也可以供  后台 客服人员，手动派工时 使用 云店
		 *  		
		 */
		return firstList;
	}
	
	private static BaiduPoiVo initBaiduPoiVo(){
		BaiduPoiVo baiduPoiVo = new BaiduPoiVo();
		
		baiduPoiVo.setDistanceText("");
		baiduPoiVo.setDistanceValue(-1);
		baiduPoiVo.setDurationText("");
		baiduPoiVo.setDurationValue(-1);
		baiduPoiVo.setLat("");
		baiduPoiVo.setLng("");
		baiduPoiVo.setName("");
		
		return baiduPoiVo;
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
			List<BaiduPoiVo> resultAddrs = BaiduMapUtil.getMapRouteMatrix(fromLat, fromLng, destAddrs);
			
			Collections.sort(resultAddrs, new Comparator<BaiduPoiVo>() {
			    @Override
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