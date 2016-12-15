package com.jhj.service.impl.market;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jhj.po.dao.market.MarketSmsLogMapper;
import com.jhj.po.model.market.MarketSmsLog;
import com.jhj.service.market.MarketSmsLogService;

@Service
public class MarketSmsLogServiceImpl implements MarketSmsLogService{

	@Autowired
	private MarketSmsLogMapper maketSmsLogMapper;
	
	@Override
	public int deleteByPrimaryKey(Integer id) {
		maketSmsLogMapper.deleteByPrimaryKey(id);
		return 0;
	}

	@Override
	public int insert(MarketSmsLog record) {
		maketSmsLogMapper.insert(record);
		return 0;
	}

	@Override
	public int insertSelective(MarketSmsLog record) {
		maketSmsLogMapper.insertSelective(record);
		return 0;
	}

	@Override
	public MarketSmsLog selectByPrimaryKey(Integer id) {
		MarketSmsLog marketSmsFail = maketSmsLogMapper.selectByPrimaryKey(id);
		return marketSmsFail;
	}

	@Override
	public int updateByPrimaryKeySelective(MarketSmsLog record) {
		maketSmsLogMapper.updateByPrimaryKeySelective(record);
		return 0;
	}

	@Override
	public int updateByPrimaryKey(MarketSmsLog record) {
		maketSmsLogMapper.updateByPrimaryKey(record);
		return 0;
	}

	@Override
	public List<MarketSmsLog> selectByMarketSmsId(int marketSmsId) {
		return maketSmsLogMapper.selectByMarketSmsId(marketSmsId);
	}
   
}