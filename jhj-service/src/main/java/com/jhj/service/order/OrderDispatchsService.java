package com.jhj.service.order;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jhj.po.model.order.OrderDispatchs;
import com.jhj.vo.order.OrderDispatchSearchVo;
import com.jhj.vo.order.OrgStaffsNewVo;

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

	int totalStaffTodayOrders(Long staffId);

	List<OrderDispatchs> selectByMatchTime(OrderDispatchSearchVo searchVo);

	List<Map<String, Object>> totalByYearAndMonth(Map<String, Object> conditions);

	List<HashMap> totalByStaff(OrderDispatchSearchVo searchVo);
	
	Long autoDispatch(Long orderId, Long serviceDate, Double serviceHour);

	List<OrgStaffsNewVo> manualDispatch(Long orderId, Long serviceDate, Double serviceHour);

	List<OrgStaffsNewVo> getStaffDispatch(List<OrgStaffsNewVo> list, String fromLat, String fromLng);

	List<OrgStaffsNewVo> manualDispatchByOrg(Long orderId, Long serviceDate, Double serviceHour, Long parentId, Long orgId);
   	
}
