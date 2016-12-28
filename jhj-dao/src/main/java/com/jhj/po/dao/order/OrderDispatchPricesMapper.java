package com.jhj.po.dao.order;

import java.util.List;

import com.jhj.po.model.order.OrderDispatchPrices;
import com.jhj.vo.order.OrderSearchVo;

public interface OrderDispatchPricesMapper {
    int deleteByPrimaryKey(Long id);

    int insert(OrderDispatchPrices record);

    int insertSelective(OrderDispatchPrices record);

    OrderDispatchPrices selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(OrderDispatchPrices record);

    int updateByPrimaryKey(OrderDispatchPrices record);
    
    List<OrderDispatchPrices> selectByListPage(OrderSearchVo searchVo);
    
    List<OrderDispatchPrices> selectBySearchVo(OrderSearchVo searchVo);
    
}