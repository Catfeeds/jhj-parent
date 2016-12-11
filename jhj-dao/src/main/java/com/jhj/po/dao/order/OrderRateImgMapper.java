package com.jhj.po.dao.order;

import java.util.List;

import com.jhj.po.model.order.OrderRateImg;
import com.jhj.vo.order.OrderSearchVo;

public interface OrderRateImgMapper {
    int deleteByPrimaryKey(Long id);

    int insert(OrderRateImg record);

    int insertSelective(OrderRateImg record);

    OrderRateImg selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(OrderRateImg record);

    int updateByPrimaryKey(OrderRateImg record);
    
    List<OrderRateImg> selectBySearchVo(OrderSearchVo searchVo);
}