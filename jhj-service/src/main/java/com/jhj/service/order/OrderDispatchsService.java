package com.jhj.service.order;

import java.util.List;
import java.util.Map;

import com.jhj.po.model.order.OrderDispatchs;
import com.jhj.vo.order.OrderDispatchSearchVo;

/**
 *
 * @author :hulj
 * @Date : 2015年7月20日下午5:16:54
 * @Description: TODO
 *
 */
public interface OrderDispatchsService {
	int deleteByPrimaryKey(Long id);

    int insert(OrderDispatchs record);

    int insertSelective(OrderDispatchs record);

    OrderDispatchs selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(OrderDispatchs record);

    int updateByPrimaryKey(OrderDispatchs record);
       
    OrderDispatchs  initOrderDisp();
    
    List<OrderDispatchs> selectBySearchVo(OrderDispatchSearchVo searchVo);

	Long totalStaffTodayOrders(Long staffId);

	List<OrderDispatchs> selectByMatchTime(OrderDispatchSearchVo searchVo);

	List<Map<String, Object>> totalByYearAndMonth(Map<String, Object> conditions);
   	
}
