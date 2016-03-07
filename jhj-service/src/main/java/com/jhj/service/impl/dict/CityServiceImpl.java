package com.jhj.service.impl.dict;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jhj.service.dict.CityService;
import com.jhj.po.dao.dict.DictCityMapper;
import com.jhj.po.model.dict.DictCity;

@Service
public class CityServiceImpl implements CityService {

	@Autowired
	private DictCityMapper cityMapper;

	/*
	 * 获取表dict_city的数据，通过cityId
	 * @param
	 * @return  List<DictCity>
	 */
	@Override
	public DictCity getCityById(Long id) {
		return cityMapper.selectByPrimaryKey(id);
	}

	/*
	 * 获取表dict_city的数据
	 * @param
	 * @return  List<DictCity>
	 */
	@Override
	public List<DictCity> selectAll() {
		return cityMapper.selectAll();
	}
	
	@Override
	public DictCity selectByCityId(Long cityId) {
		
		return cityMapper.selectByPrimaryKey(cityId);
	}
}
