package com.jhj.po.dao.market;

import com.jhj.po.model.market.MarketSmsLog;

public interface MarketSmsLogMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(MarketSmsLog record);

    int insertSelective(MarketSmsLog record);

    MarketSmsLog selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(MarketSmsLog record);

    int updateByPrimaryKey(MarketSmsLog record);
}