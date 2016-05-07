package com.jhj.service.order;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jhj.po.model.order.OrderDispatchs;
import com.jhj.vo.OrderSearchVo;

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
    
    List<OrderDispatchs> getBadOrgStaff(Map<String,Object> map);
    
    OrderDispatchs  initOrderDisp();
    
    OrderDispatchs selectByOrderNo(String orderNo);
    
	OrderDispatchs selectByUserId(Long userId);

	List<OrderDispatchs> selectEnableStaffNow(Long orgId, Long serviceDateStart, Long serviceDateEnd);

	List<OrderDispatchs> selectByOrderIds(List<Long> orderIds);
	
	//所有 有 阿姨 的 下单 记录
	List<OrderDispatchs> selectAll();
	
	OrderDispatchs selectByOrderId(Long orderId);
	
	List<OrderDispatchs> selectByNoAndDisStatus(String orderNo,Short disStatus);
	
	
	//jhj2.1   服务人员 当天的派工数
	Long getTodayOrderNumForTheSta(Long staffId);

	List<HashMap> getUserIdsByListPage(OrderSearchVo searchVo, int pageNo, int pageSize);
	
	
	//jhj2.1  服务人员 在 订单 服务 时间内  是否有 派工
	Long getDisNumForStaDuringServiceDate(OrderSearchVo searchVo);
}
