package com.jhj.service.order;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jhj.po.model.order.OrderDispatchs;
import com.jhj.po.model.order.Orders;
import com.jhj.vo.bs.OrgDispatchPoiVo;
import com.jhj.vo.order.OrderDispatchSearchVo;
import com.jhj.vo.order.OrderDispatchVo;
import com.jhj.vo.order.OrgStaffDispatchVo;
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
	
	List<OrgStaffDispatchVo> getStaffDispatch(List<OrgStaffDispatchVo> list, String fromLat, String fromLng);

	OrderDispatchVo changeToOrderDispatchVo(OrderDispatchs item);

	AppResultData<Object> checAppointDispatch(Long orderId, Long staffId);


	List<OrgStaffDispatchVo> autoDispatch(Long addrId, Long serviceTypeId, Long serviceDate, Double serviceHour, int staffNums, List<Long> appointStaffIds);

	List<OrgDispatchPoiVo> getMatchOrgs(String fromLat, String fromLng, Long parentId, Long orgId, Boolean needMatchMaxDistance);

	int getLatestDistance(String userLat, String userLon, Long staffId);

	void pushToStaff(Long staffId, String isShow, String action, Long orderId, String remindTitle, String remindContent);

	List<HashMap> getTotalStaffOrders(Long serviceDate, List<Long> staffIds);

	List<OrgStaffDispatchVo> getStaffDistance(String fromLat, String fromLng, List<Long> staIdList);

	List<OrgStaffDispatchVo> manualDispatch(Long addrId, Long serviceTypeId, Long serviceDate, Double serviceHour, Long selectParentId, Long orgId);

	boolean doOrderDispatch(Orders order, Long serviceDate, Double serviceHour, Long staffId, int allocate, String allocateReason);

	List<Map<String, String>> checkDispatchedDay(Long serviceTypeId, String serviceDateStr, String lat, String lng);

	List<Map<String, String>> checkDispatchedDayByStaffId(Long serviceTypeId, String serviceDateStr, Long addrId, String lat, String lng);
   	
}
