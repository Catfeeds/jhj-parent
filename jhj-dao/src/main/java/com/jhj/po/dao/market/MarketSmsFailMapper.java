package com.jhj.po.dao.market;

import com.jhj.po.model.market.MarketSmsFail;

public interface MarketSmsFailMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(MarketSmsFail record);

    int insertSelective(MarketSmsFail record);

    MarketSmsFail selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(MarketSmsFail record);

    int updateByPrimaryKey(MarketSmsFail record);
}