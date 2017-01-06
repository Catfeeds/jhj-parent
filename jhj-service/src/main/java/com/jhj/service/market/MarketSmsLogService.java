package com.jhj.service.market;

import java.util.List;

import com.jhj.po.model.market.MarketSmsLog;

public interface MarketSmsLogService {
    int deleteByPrimaryKey(Integer id);

    int insert(MarketSmsLog record);

    int insertSelective(MarketSmsLog record);

    MarketSmsLog selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(MarketSmsLog record);

    int updateByPrimaryKey(MarketSmsLog record);
    
    List<MarketSmsLog> selectByMarketSmsId(int marketSmsId);
    
}