package com.jhj.service.market;

import java.util.List;

import com.jhj.po.model.market.MarketSms;
import com.jhj.vo.market.MarketSmsSearchVo;

public interface MarketSmsService {
    int deleteByPrimaryKey(Integer marketSmsId);

    int insert(MarketSms record);

    int insertSelective(MarketSms record);

    MarketSms selectByPrimaryKey(Integer marketSmsId);

    int updateByPrimaryKeySelective(MarketSms record);

    int updateByPrimaryKey(MarketSms record);
    
    List<MarketSms> selectBySearchVo(MarketSmsSearchVo searchVo);

	List<MarketSms> selectByListPage(MarketSmsSearchVo searchVo,int pageNum,int pageSize);
}