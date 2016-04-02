package com.jhj.po.dao.order;


import java.util.List;

import com.jhj.po.model.order.OrderCustomizationYear;

public interface OrderCustomizationYearMapper {
    int deleteByPrimaryKey(Long id);

    int insert(OrderCustomizationYear record);

    int insertSelective(OrderCustomizationYear record);

    OrderCustomizationYear selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(OrderCustomizationYear record);

    int updateByPrimaryKey(OrderCustomizationYear record);
    
    List<OrderCustomizationYear> selectByListPage();
}