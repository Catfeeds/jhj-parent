package com.jhj.service.impl.dict;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jhj.po.dao.dict.CouponsTypeMapper;
import com.jhj.po.model.dict.CouponsType;
import com.jhj.service.dict.CouponsTypeService;

@Service
public class CouponsTypeServiceImpl implements CouponsTypeService{

	@Autowired
	private CouponsTypeMapper couponsType;
	
	@Override
	public int deleteByPrimaryKey(Integer useConditonId) {
		couponsType.deleteByPrimaryKey(useConditonId);
		return 0;
	}

	@Override
	public int insert(CouponsType record) {
		couponsType.insert(record);
		return 0;
	}

	@Override
	public int insertSelective(CouponsType record) {
		couponsType.insertSelective(record);
		return 0;
	}

	@Override
	public CouponsType selectByPrimaryKey(Integer useConditonId) {
		return couponsType.selectByPrimaryKey(useConditonId);
	}

	@Override
	public int updateByPrimaryKeySelective(CouponsType record) {
		couponsType.updateByPrimaryKeySelective(record);
		return 0;
	}

	@Override
	public int updateByPrimaryKey(CouponsType record) {
		couponsType.updateByPrimaryKey(record);
		return 0;
	}

	@Override
	public List<CouponsType> selectAll() {
		return couponsType.selectAll();
	}

}
