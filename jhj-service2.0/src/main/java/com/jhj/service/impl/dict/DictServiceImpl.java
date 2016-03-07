package com.jhj.service.impl.dict;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jhj.po.dao.dict.DictCityMapper;
import com.jhj.po.dao.dict.DictServiceTypesMapper;
import com.jhj.po.model.dict.DictCity;
import com.jhj.po.model.dict.DictProvince;
import com.jhj.po.model.dict.DictRegion;
import com.jhj.po.model.dict.DictServiceTypes;
import com.jhj.service.dict.CityService;
import com.jhj.service.dict.DictService;
import com.jhj.service.dict.ProvinceService;
import com.jhj.service.dict.RegionService;
import com.jhj.service.dict.ServiceTypeService;

@Service
public class DictServiceImpl implements DictService {

	public static Map<String, List> memDictMap = new HashMap<String, List>();

	@Autowired
	private DictServiceTypesMapper dictServiceTypesMapper;
	
	@Autowired
	private DictCityMapper dictCityMapper;

	@Autowired
	private ProvinceService provinceService;

	@Autowired
	private CityService cityService;

	@Autowired
	private RegionService regionService;

	@Autowired
	private ServiceTypeService serviceTypeService;

	/**
	 * Spring 容器初始化时加载
	 */
	@Override
	public void loadData() {

		// 省份信息
		this.LoadProvinceData();

		// 城市信息
		this.LoadCityData();

		// 区县信息
		this.LoadRegionData();

		// 服务类型
		this.LoadServiceTypeData();

	}

	@Override
	public String getProvinceName(Long provinceId) {
		String provinceName = "";
		if (provinceId <= 0)
			return provinceName;

		List<DictProvince> listProvince = this.LoadProvinceData();

		DictProvince item = null;
		for (int i = 0; i < listProvince.size(); i++) {
			item = listProvince.get(i);
			if (item.getProvinceId().equals(provinceId)) {
				provinceName = item.getName();
				break;
			}
		}
		return provinceName;
	}	
	
	@Override
	public String getCityName(Long cityId) {
		String cityName = "";
		if (cityId <= 0)
			return cityName;

		List<DictCity> listCity = this.LoadCityData();

		DictCity item = null;
		for (int i = 0; i < listCity.size(); i++) {
			item = listCity.get(i);
			if (item.getCityId().equals(cityId)) {
				cityName = item.getName();
				break;
			}
		}
		return cityName;
	}

	@Override
	public String getServiceTypeName(Long serviceTypeId) {
		String serviceTypeName = "";
		if (serviceTypeId <= 0)
			return serviceTypeName;

		List<DictServiceTypes> listServiceTypes = this.LoadServiceTypeData();

		DictServiceTypes item = null;
		for (int i = 0; i < listServiceTypes.size(); i++) {
			item = listServiceTypes.get(i);
			if (item.getId().equals(serviceTypeId)) {
				serviceTypeName = item.getName();
				break;
			}
		}
		return serviceTypeName;
	}

	private List<DictProvince> LoadProvinceData() {
		// 城市信息
		List<DictProvince> listProvince = memDictMap.get("listProvince");
		if (listProvince == null || listProvince.isEmpty()) {
			listProvince = provinceService.selectAll();
			memDictMap.put("listProvince", listProvince);
		}

		return listProvince;
	}

	private List<DictCity> LoadCityData() {
		// 城市信息
		List<DictCity> listCity = memDictMap.get("listCity");
		if (listCity == null || listCity.isEmpty()) {
			listCity = cityService.selectAll();
			memDictMap.put("listCity", listCity);
		}

		return listCity;
	}

	@Override
	public List<DictRegion> getRegionByCityId(Long cityId){
		List<DictRegion> listRegion = LoadRegionData();
		List<DictRegion> listRegionName = new ArrayList<DictRegion>();
		if(0L==cityId){
			return listRegion;
		}else{
			for (Iterator iterator = listRegion.iterator(); iterator.hasNext();) {
				DictRegion dictRegion = (DictRegion) iterator.next();
				if (dictRegion.getCityId()==cityId) {
					listRegionName.add(dictRegion);
				}
			}
			return listRegionName;
		}
	}
	//更根据省份id，查询出市级信息
	@Override
	public List<DictCity> getCityByProvinceId(Long provinceId){
		List<DictCity> listCity = LoadCityData();
		List<DictCity> listCityName = new ArrayList<DictCity>();
		if(0L==provinceId){
			return listCity;
		}else{
			for (Iterator iterator = listCity.iterator(); iterator.hasNext();) {
				DictCity dictCity = (DictCity) iterator.next();
				if (dictCity.getProvinceId() == provinceId) {
					listCityName.add(dictCity);
				}
			}
			return listCityName;
		}
	}

	private List<DictRegion> LoadRegionData() {
		// 城市信息
		List<DictRegion> listRegion = memDictMap.get("listRegion");
		if (listRegion == null || listRegion.isEmpty()) {
			listRegion = regionService.selectAll();
			memDictMap.put("listRegion", listRegion);
		}

		return listRegion;
	}

	private List<DictServiceTypes> LoadServiceTypeData() {
		// 服务类型
		List<DictServiceTypes> listServiceTypes = memDictMap
				.get("listServiceTypes");
		if (listServiceTypes == null || listServiceTypes.isEmpty()) {
			listServiceTypes = serviceTypeService.getServiceTypes();
			memDictMap.put("listServiceTypes", listServiceTypes);
		}
		return listServiceTypes;
	}



}
