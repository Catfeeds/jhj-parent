package com.jhj.service.dict;

import java.util.List;

import com.jhj.po.model.dict.DictRegion;


public interface RegionService {

	DictRegion getRegionById(Long id);

	List<DictRegion> getRegionByCityId(Long cityId);

	List<DictRegion> getRegionByProvinceId(Long provinceId);

	List<DictRegion> selectAll();

}
