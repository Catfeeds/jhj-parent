package com.jhj.po.dao.order;

import java.util.HashMap;
import java.util.List;

import com.jhj.po.model.order.OrderRates;
import com.jhj.vo.order.OrderDispatchSearchVo;

public interface OrderRatesMapper {
    int deleteByPrimaryKey(Long id);

    int insert(OrderRates record);

    int insertSelective(OrderRates record);

    OrderRates selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(OrderRates record);

    int updateByPrimaryKey(OrderRates record);
    
    List<OrderRates> selectBySearchVo(OrderDispatchSearchVo searchVo);
    
    List<OrderRates> selectByListPage(OrderDispatchSearchVo searchVo);
    
    HashMap totalByStaff(OrderDispatchSearchVo searchVo);
}