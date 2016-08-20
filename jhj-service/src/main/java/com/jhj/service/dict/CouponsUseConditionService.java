package com.jhj.service.dict;

import java.util.List;

import com.jhj.po.model.dict.CouponsUseCondition;

public interface CouponsUseConditionService {

	int deleteByPrimaryKey(Integer useConditonId);

	int insert(CouponsUseCondition record);

	int insertSelective(CouponsUseCondition record);

	CouponsUseCondition selectByPrimaryKey(Integer useConditonId);

	int updateByPrimaryKeySelective(CouponsUseCondition record);

	int updateByPrimaryKey(CouponsUseCondition record);
	
	List<CouponsUseCondition> selectAll();
}
