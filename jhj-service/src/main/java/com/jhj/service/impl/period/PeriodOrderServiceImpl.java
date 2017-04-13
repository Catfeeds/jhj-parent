package com.jhj.service.impl.period;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jhj.po.dao.period.PeriodOrderMapper;
import com.jhj.po.model.period.PeriodOrder;
import com.jhj.service.period.PeriodOrderService;

@Service
public class PeriodOrderServiceImpl implements PeriodOrderService{
	
	@Autowired
	private PeriodOrderMapper periodOrderMapper;

	@Override
	public int deleteByPrimaryKey(Integer id) {
		return periodOrderMapper.deleteByPrimaryKey(id);
	}

	@Override
	public int insert(PeriodOrder record) {
		return periodOrderMapper.insert(record);
	}

	@Override
	public int insertSelective(PeriodOrder record) {
		return periodOrderMapper.insertSelective(record);
	}

	@Override
	public PeriodOrder selectByPrimaryKey(Integer id) {
		return periodOrderMapper.selectByPrimaryKey(id);
	}

	@Override
	public int updateByPrimaryKeySelective(PeriodOrder record) {
		return periodOrderMapper.updateByPrimaryKeySelective(record);
	}

	@Override
	public int updateByPrimaryKey(PeriodOrder record) {
		return periodOrderMapper.updateByPrimaryKey(record);
	}
   
}