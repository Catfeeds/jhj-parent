package com.jhj.service.dict;

import java.util.List;

import com.jhj.po.model.dict.DictCity;

public interface CityService {

	DictCity getCityById(Long id);

	List<DictCity> selectAll();

	DictCity selectByCityId(Long secId);

}
