package com.jhj.service.order;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jhj.po.model.order.OrderDispatchs;
import com.jhj.po.model.order.Orders;
import com.jhj.vo.order.OrderDispatchSearchVo;
import com.jhj.vo.order.OrderDispatchVo;
import com.jhj.vo.order.OrgStaffsNewVo;
import com.meijia.utils.vo.AppResultData;

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

	List<HashMap> totalByStaff(OrderDispatchSearchVo searchVo);
	
	List<OrgStaffsNewVo> getStaffDispatch(List<OrgStaffsNewVo> list, String fromLat, String fromLng);

	OrgStaffsNewVo initStaffsNew();

	OrderDispatchVo changeToOrderDispatchVo(OrderDispatchs item);

	boolean doOrderDispatch(Orders order, Long serviceDate, Double serviceHour, Long staffId);

	AppResultData<Object> checAppointDispatch(Long orderId, Long staffId);

	List<Map<String, String>> checkDispatchedDay(Long serviceTypeId, String serviceDateStr, Long addrId);

	List<OrgStaffsNewVo> manualDispatch(Long addrId, Long serviceTypeId, Long serviceDate, Double serviceHour, Long sessionOrgId);

	List<OrgStaffsNewVo> manualDispatchByOrg(Long addrId, Long serviceTypeId, Long serviceDate, Double serviceHour, Long parentId, Long orgId);

	List<Long> autoDispatch(Long addrId, Long serviceTypeId, Long serviceDate, Double serviceHour, int staffNums, List<Long> appointStaffIds);
   	
}
