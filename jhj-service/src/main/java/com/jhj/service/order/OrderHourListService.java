package com.jhj.service.order;

import java.util.HashMap;
import java.util.List;

import com.jhj.po.model.order.Orders;
import com.jhj.vo.OrderSearchVo;
import com.jhj.vo.order.OrderHourListVo;

/**
 *
 * @author :hulj
 * @Date : 2015年8月4日上午11:39:43
 * @Description: 钟点工--订单列表
 *
 */
public interface OrderHourListService {
	
	//当前订单。待支付
	List<Orders> selectNowOrderHourListByUserId(Long userId,int pageNo, int pageSize);
	
	//历史订单，已关闭 或者 已经支付的 
	List<Orders> selectOldOrderHourListByUserId(Long userId,int pageNo, int pageSize);
	
	
	List<OrderHourListVo> transOrderHourListVo(List<Orders> orderHourList);

	OrderHourListVo initOHLV();

	List<Orders> selectByUserListPage(OrderSearchVo orderSearchVo, int pageNo, int pageSize);

	List<HashMap> userTotalByMonth(OrderSearchVo orderSearchVo);
	
}
