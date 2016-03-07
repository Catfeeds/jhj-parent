package com.jhj.service.remind;

import java.util.List;

import com.jhj.po.model.order.Orders;
import com.jhj.vo.remind.OrderRemindVo;


/*
 * 提醒类订单 service
 */
public interface OrderRemindService {
	
	/*
	 * 用户端
	 */
	
	//我的提醒（当前提醒） 
	List<Orders>  selectNowRemind(Long userId,int pageNo,int pageSize);
	
	//历史提醒（历史提醒） 
	List<Orders> selectOldRemind(Long userId,int pageNo,int pageSize);
	
	//列表页、详情页 VO
	List<OrderRemindVo> transListToVO(List<Orders> orderList);
	
	OrderRemindVo initRemindVo();
	
	OrderRemindVo getRemindDetail(String orderNo);
	
	//已预约 提醒 类 订单的 数量
	Long getRemindCountToDo(Long userId);
	
	/*
	 * 助理端
	 */
	
	// 助理端 只显示  已预约 状态 的  列表 即可
	List<OrderRemindVo> selectNowRemindAm(Long amId,int pageNo,int pageSize);
	
}


