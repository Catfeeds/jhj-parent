package com.jhj.service.impl.dict;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jhj.po.dao.dict.CouponsUseConditionMapper;
import com.jhj.po.model.dict.CouponsUseCondition;
import com.jhj.service.dict.CouponsUseConditionService;

@Service
public class CouponsUseConditionServiceImpl implements CouponsUseConditionService{

	@Autowired
	private CouponsUseConditionMapper couponsUseCondition;
	
	@Override
	public int deleteByPrimaryKey(Integer useConditonId) {
		couponsUseCondition.deleteByPrimaryKey(useConditonId);
		return 0;
	}

	@Override
	public int insert(CouponsUseCondition record) {
		couponsUseCondition.insert(record);
		return 0;
	}

	@Override
	public int insertSelective(CouponsUseCondition record) {
		couponsUseCondition.insertSelective(record);
		return 0;
	}

	@Override
	public CouponsUseCondition selectByPrimaryKey(Integer useConditonId) {
		return couponsUseCondition.selectByPrimaryKey(useConditonId);
	}

	@Override
	public int updateByPrimaryKeySelective(CouponsUseCondition record) {
		couponsUseCondition.updateByPrimaryKeySelective(record);
		return 0;
	}

	@Override
	public int updateByPrimaryKey(CouponsUseCondition record) {
		couponsUseCondition.updateByPrimaryKey(record);
		return 0;
	}

	@Override
	public List<CouponsUseCondition> selectAll() {
		return couponsUseCondition.selectAll();
	}

}
