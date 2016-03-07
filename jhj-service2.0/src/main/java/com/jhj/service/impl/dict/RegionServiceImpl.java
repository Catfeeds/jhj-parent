package com.jhj.service.impl.dict;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jhj.service.dict.RegionService;
import com.jhj.po.dao.dict.DictRegionMapper;
import com.jhj.po.model.dict.DictRegion;

@Service
public class RegionServiceImpl implements RegionService {

	@Autowired
	private DictRegionMapper regionMapper;

	/*
	 * 获取表dict_region的数据，通过regionId
	 * @param
	 * @return  List<DictCity>
	 */
	@Override
	public DictRegion getRegionById(Long id) {
		return regionMapper.selectByPrimaryKey(id);
	}

	/*
	 * 获取表dict_region的数据，通过cityId
	 * @param
	 * @return  List<DictCity>
	 */
	@Override
	public List<DictRegion> getRegionByCityId(Long cityId) {
		return regionMapper.selectByCityId(cityId);
	}

	/*
	 * 获取表dict_region的数据，通过provinceId
	 * @param
	 * @return  List<DictCity>
	 */
	@Override
	public List<DictRegion> getRegionByProvinceId(Long provinceId) {
		return regionMapper.selectByCityId(provinceId);
	}


	/*
	 * 获取表dict_region的数据
	 * @param
	 * @return  List<DictRegion>
	 */
	@Override
	public List<DictRegion> selectAll() {
		return regionMapper.selectAll();
	}
}
