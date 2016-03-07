package com.jhj.service.dict;

import java.util.List;

import com.jhj.po.model.dict.DictCity;
import com.jhj.po.model.dict.DictRegion;

public interface DictService {

	void loadData();

	String getCityName(Long cityId);

	String getServiceTypeName(Long serviceTypeId);

	List<DictRegion> getRegionByCityId(Long cityId);
	
	List<DictCity> getCityByProvinceId(Long provinceId);

	String getProvinceName(Long provinceId);


	


}
