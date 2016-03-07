package com.jhj.po.dao.order;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.jhj.po.model.order.OrderDispatchs;
import com.jhj.vo.OaOrderDisSearchVo;

public interface OrderDispatchsMapper {
    int deleteByPrimaryKey(Long id);

    int insert(OrderDispatchs record);

    int insertSelective(OrderDispatchs record);

    OrderDispatchs selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(OrderDispatchs record);

    int updateByPrimaryKey(OrderDispatchs record);
    
    //找出差评阿姨
    List<OrderDispatchs> getBadOrgStaff(Map<String,Object> map);
    
    //找出 当前用户  下单服务时间段 内 的    所有出现在   派工表 里的  阿姨
    List<OrderDispatchs> selectEnableStaffNow(@Param("orgId") Long orgId,
    										  @Param("serviceDateStart") Long serviceDateStart,
    										  @Param("serviceDateEnd") Long serviceDateEnd);
    
    // 由于换人  操作。导致  orderNo 不能 再 唯一 确定一条记录 
    OrderDispatchs selectByOrderNo(String orderNo);
    
	OrderDispatchs selectByUserId(Long userId);

	List<OrderDispatchs> selectByOrderIds(List<Long> orderIds);
    
	List<OrderDispatchs> selectDis(OaOrderDisSearchVo disSearchVo);
	
	List<OrderDispatchs> selectAll();
	
	List<OrderDispatchs> selectDisEveryDay(String day);
	
	List<Map<String, Object>> selectOrdersCountByYearAndMonth(Map<String, Object> conditions);

	
	OrderDispatchs selectByOrderId(Long orderId);
	
	List<OrderDispatchs> selectAllForOneOrderId(Long orderId);
	
	List<OrderDispatchs> selectByNoAndDisStatus(@Param("orderNo") String orderNo,@Param("disStatus") Short disStatus);
	
	List<OrderDispatchs>   selectAbledOrderDis(OaOrderDisSearchVo oaOrderDisSearchVo);
	
	// 获得某段时间内的派工列表 
	List<OrderDispatchs> selectByStartTimeAndEndTime(@Param("startTime")Long startTime,@Param("endTime")Long endTime);


}