package com.jhj.service.dict;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jhj.service.impl.dict.DictServiceImpl;
import com.jhj.po.model.dict.DictCity;
import com.jhj.po.model.dict.DictProvince;
import com.jhj.po.model.dict.DictRegion;
import com.jhj.po.model.dict.DictServiceTypes;

public class DictUtil {
	
	/**
	 * 获取内存省份数据
	 * @return
	 */
	public static List<DictProvince> getProvinces() {
		List<DictProvince> listProvince = DictServiceImpl.memDictMap.get("listProvince");
		return listProvince;
	}
	
	/**
	 * 获取内存城市数据
	 * @return
	 */
	public static List<DictCity> getCitys() {
		List<DictCity> listCity = DictServiceImpl.memDictMap.get("listCity");
		return listCity;
	}
	
	/**
	 * 获取内存区县数据
	 * @return
	 */
	public static List<DictRegion> getRegions() {
		List<DictRegion> listRegion = DictServiceImpl.memDictMap.get("listRegion");
		return listRegion;
	}
	
	/**
	 * 获取内存服务类型数据
	 * @return
	 */
	public static List<DictServiceTypes> getServiceTypes() {
		List<DictServiceTypes>  listServiceTypes = DictServiceImpl.memDictMap.get("listServiceTypes");
		return listServiceTypes;
	}

	/**
	 * 通过省份id获取省份名称
	 * @return
	 */
	public static String getProvinceName(Long provinceId) {

		String provinceName = "";
		if (provinceId <=0) return provinceName;

		List<DictProvince> listProvince = DictServiceImpl.memDictMap.get("listProvince");

		DictProvince item = null;
		for (int i = 0 ; i < listProvince.size(); i++) {
			item = listProvince.get(i);
			if (item.getProvinceId().equals(provinceId)) {
				provinceName = item.getName();
				break;
			}
		}
		return provinceName;
	}	
	
	/**
	 * 通过城市id获取城市名称
	 * @return
	 */
	public static String getCityName(Long cityId) {

		String cityName = "";
		if (cityId <=0) return cityName;

		List<DictCity> listCity = DictServiceImpl.memDictMap.get("listCity");

		DictCity item = null;
		for (int i = 0 ; i < listCity.size(); i++) {
			item = listCity.get(i);
			if (item.getCityId().equals(cityId)) {
				cityName = item.getName();
				break;
			}
		}
		return cityName;
	}
	
	/**
	 * 通过区县id获取区县名称
	 * @return
	 */
	public static String getRegionName(Long regionId) {

		String regionName = "";
		if (regionId <=0) return regionName;

		List<DictRegion> listRegion = DictServiceImpl.memDictMap.get("listRegion");

		DictRegion item = null;
		for (int i = 0 ; i < listRegion.size(); i++) {
			item = listRegion.get(i);
			if (item.getRegionId().equals(regionId)) {
				regionName = item.getName();
				break;
			}
		}
		return regionName;
	}

	public static String getAddServiceTypeItemName(String serviceTypeId) {
		Map<String,String> map1 = new HashMap<String,String>();
		map1.put("1", "钟点工");
		map1.put("2", "深度保洁");
		map1.put("3", "助理");
		return map1.get(serviceTypeId);
	}
	
	public static String getServiceTypeItemName(String serviceTypeId) {
		Map<String,String> map1 = new HashMap<String,String>();
		map1.put("0", "钟点工");
		map1.put("1", "深度保洁");
		map1.put("2", "助理");
		
		//话费充值类型， key 对应 orders 表的  order_type 订单类型，
		//具体可在constans中查看各个订单类型  ORDER_TYPE_6
		map1.put("6", "缴费充值");
		return map1.get(serviceTypeId);
	}
	public static String getServiceTypeName(Long serviceTypeId) {
		String serviceTypeName = "";
		if (serviceTypeId <=0) return serviceTypeName;

		List<DictServiceTypes>  listServiceTypes = DictServiceImpl.memDictMap.get("listServiceTypes");

		DictServiceTypes item = null;
		for (int i = 0 ; i < listServiceTypes.size(); i++) {
			item = listServiceTypes.get(i);
			if (item.getId().equals(serviceTypeId)) {
				serviceTypeName = item.getName();
				break;
			}
		}
		return serviceTypeName;
	}
	
}
