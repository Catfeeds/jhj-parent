package com.jhj.service.impl.market;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jhj.po.dao.market.MarketSmsFailMapper;
import com.jhj.po.model.market.MarketSmsFail;
import com.jhj.service.market.MarketSmsFailService;

@Service
public class MarketSmsFailServiceImpl implements MarketSmsFailService{

	@Autowired
	private MarketSmsFailMapper maketSmsFailMapper;
	
	@Override
	public int deleteByPrimaryKey(Integer id) {
		maketSmsFailMapper.deleteByPrimaryKey(id);
		return 0;
	}

	@Override
	public int insert(MarketSmsFail record) {
		maketSmsFailMapper.insert(record);
		return 0;
	}

	@Override
	public int insertSelective(MarketSmsFail record) {
		maketSmsFailMapper.insertSelective(record);
		return 0;
	}

	@Override
	public MarketSmsFail selectByPrimaryKey(Integer id) {
		MarketSmsFail marketSmsFail = maketSmsFailMapper.selectByPrimaryKey(id);
		return marketSmsFail;
	}

	@Override
	public int updateByPrimaryKeySelective(MarketSmsFail record) {
		maketSmsFailMapper.updateByPrimaryKeySelective(record);
		return 0;
	}

	@Override
	public int updateByPrimaryKey(MarketSmsFail record) {
		maketSmsFailMapper.updateByPrimaryKey(record);
		return 0;
	}

   
}