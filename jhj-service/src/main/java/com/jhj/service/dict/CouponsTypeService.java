package com.jhj.service.dict;

import java.util.List;

import com.jhj.po.model.dict.CouponsType;

public interface CouponsTypeService {

	int deleteByPrimaryKey(Integer useConditonId);

	int insert(CouponsType record);

	int insertSelective(CouponsType record);

	CouponsType selectByPrimaryKey(Integer useConditonId);

	int updateByPrimaryKeySelective(CouponsType record);

	int updateByPrimaryKey(CouponsType record);
	
	List<CouponsType> selectAll();
}
