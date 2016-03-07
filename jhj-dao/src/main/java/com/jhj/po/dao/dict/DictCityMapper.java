package com.jhj.po.dao.dict;

import java.util.List;

import com.jhj.po.model.dict.DictCity;

public interface DictCityMapper {
    int deleteByPrimaryKey(Long cityId);

	int insert(DictCity record);

	int insertSelective(DictCity record);

	DictCity selectByPrimaryKey(Long cityId);

	int updateByPrimaryKeySelective(DictCity record);

	int updateByPrimaryKey(DictCity record);

	List<DictCity> selectAll();

}