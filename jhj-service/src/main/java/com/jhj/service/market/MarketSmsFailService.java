package com.jhj.service.market;

import com.jhj.po.model.market.MarketSmsFail;

public interface MarketSmsFailService {
    int deleteByPrimaryKey(Integer id);

    int insert(MarketSmsFail record);

    int insertSelective(MarketSmsFail record);

    MarketSmsFail selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(MarketSmsFail record);

    int updateByPrimaryKey(MarketSmsFail record);
}