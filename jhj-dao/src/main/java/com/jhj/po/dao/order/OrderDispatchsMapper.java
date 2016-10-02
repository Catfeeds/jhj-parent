package com.jhj.po.dao.order;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jhj.po.model.order.OrderDispatchs;
import com.jhj.vo.order.OrderDispatchSearchVo;
import com.jhj.vo.order.OrderSearchVo;

public interface OrderDispatchsMapper {
    int deleteByPrimaryKey(Long id);

    int insert(OrderDispatchs record);

    int insertSelective(OrderDispatchs record);

    OrderDispatchs selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(OrderDispatchs record);

    int updateByPrimaryKey(OrderDispatchs record);
           	
	List<OrderDispatchs> selectBySearchVo(OrderDispatchSearchVo searchVo);
	
	List<OrderDispatchs> selectByListPage(OrderDispatchSearchVo searchVo);
	
	List<HashMap> totalByStaff(OrderDispatchSearchVo searchVo);
	
	List<Map<String, Object>> totalByYearAndMonth(Map<String, Object> conditions);
	
	Long totalStaffTodayOrders(Long staffId);
	
	List<HashMap> totalByUserIds(OrderSearchVo searchVo);

	List<OrderDispatchs> selectByMatchTime(OrderDispatchSearchVo searchVo);
}