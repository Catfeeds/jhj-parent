package com.jhj.service.impl.market;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.jhj.po.dao.market.MarketSmsMapper;
import com.jhj.po.model.market.MarketSms;
import com.jhj.service.market.MarketSmsService;
import com.jhj.vo.market.MarketSmsSearchVo;

@Service
public class MarketSmsServiceImpl implements MarketSmsService{
	
	@Autowired
	private MarketSmsMapper marketSmsMapper;

	@Override
	public int deleteByPrimaryKey(Integer marketSmsId) {
		marketSmsMapper.deleteByPrimaryKey(marketSmsId);
		return 0;
	}

	@Override
	public int insert(MarketSms record) {
		marketSmsMapper.insert(record);
		return 0;
	}

	@Override
	public int insertSelective(MarketSms record) {
		marketSmsMapper.insertSelective(record);
		return 0;
	}

	@Override
	public MarketSms selectByPrimaryKey(Integer marketSmsId) {
		MarketSms marketSms = marketSmsMapper.selectByPrimaryKey(marketSmsId);
		return marketSms;
	}

	@Override
	public int updateByPrimaryKeySelective(MarketSms record) {
		marketSmsMapper.updateByPrimaryKeySelective(record);
		return 0;
	}

	@Override
	public int updateByPrimaryKey(MarketSms record) {
		marketSmsMapper.updateByPrimaryKey(record);
		return 0;
	}

	@Override
	public List<MarketSms> selectBySearchVo(MarketSmsSearchVo searchVo) {
		List<MarketSms> marketSmsList = marketSmsMapper.selectBySearchVo(searchVo);
		return marketSmsList;
	}

	@Override
	public List<MarketSms> selectByListPage(MarketSmsSearchVo searchVo,int pageNum,int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		List<MarketSms> marketSmsPage = marketSmsMapper.selectByListPage(searchVo);
		return marketSmsPage;
	}
  
}