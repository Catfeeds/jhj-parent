package com.jhj.service.order;

import java.util.List;

import com.jhj.po.model.order.Orders;
import com.jhj.vo.order.OrgStaffsNewVo;

public interface OrderPayService {

	void orderPaySuccessToDoForAm(Orders orders);

	void orderPaySuccessToDoForDeep(Orders orders);

	boolean orderPaySuccessToDoForHour(Long userId, Long orderId, List<OrgStaffsNewVo> orgStaffsNewVos, boolean isChangeDispatch);
	
	//手机话费充值， 微信支付成功后的 处理
	void orderPaySuccessToDoForPhone(Orders orders);
	
	
}
