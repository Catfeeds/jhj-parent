package com.jhj.service.impl.period;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jhj.po.dao.period.PeriodOrderAddonsMapper;
import com.jhj.po.model.period.PeriodOrderAddons;
import com.jhj.service.period.PeriodOrderAddonsService;

@Service
public class PeriodOrderAddonsServiceImpl implements PeriodOrderAddonsService{
	
	@Autowired
	private PeriodOrderAddonsMapper periodOrderAddonsMapper;

	@Override
	public int deleteByPrimaryKey(Integer id) {
		return periodOrderAddonsMapper.deleteByPrimaryKey(id);
	}

	@Override
	public int insert(PeriodOrderAddons record) {
		return periodOrderAddonsMapper.insert(record);
	}

	@Override
	public int insertSelective(PeriodOrderAddons record) {
		return periodOrderAddonsMapper.insertSelective(record);
	}

	@Override
	public PeriodOrderAddons selectByPrimaryKey(Integer id) {
		return periodOrderAddonsMapper.selectByPrimaryKey(id);
	}

	@Override
	public int updateByPrimaryKeySelective(PeriodOrderAddons record) {
		return periodOrderAddonsMapper.updateByPrimaryKeySelective(record);
	}

	@Override
	public int updateByPrimaryKey(PeriodOrderAddons record) {
		return periodOrderAddonsMapper.updateByPrimaryKey(record);
	}
   
}