package com.jhj.service.orderYear;


import java.util.List;

import com.jhj.po.model.order.OrderCustomizationYear;
import com.jhj.vo.order.year.OrderCusYearVo;

/**
 *
 * @author :hulj
 * @Date : 2016年4月2日上午11:11:24
 * @Description: 
 *
 */
public interface OrderCustomizYearService {
	
	int deleteByPrimaryKey(Long id);

    int insert(OrderCustomizationYear record);

    int insertSelective(OrderCustomizationYear record);

    OrderCustomizationYear selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(OrderCustomizationYear record);

    int updateByPrimaryKey(OrderCustomizationYear record);
	
    OrderCustomizationYear  initCusYear();
    
    List<OrderCustomizationYear> selectByListPage();
    
    OrderCusYearVo  transToVo(OrderCustomizationYear year);
}
