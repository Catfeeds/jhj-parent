package com.jhj.po.dao.dict;

import java.util.List;

import com.jhj.po.model.dict.DictAd;

public interface DictAdMapper {

	int deleteByPrimaryKey(Long id);

	int insertSelective(DictAd record);

	int updateByPrimaryKeySelective(DictAd record);

	int updateByPrimaryKey(DictAd record);

	List<DictAd> selectAll();

	List<DictAd> selectByListPage();

	DictAd selectByPrimaryKey(Long id);

	int insert(DictAd record);

	List<DictAd> selectByAdType(Short adType);

}