package com.jhj.service.order;

import java.util.List;

import com.jhj.po.model.order.OrderPriceExt;
import com.jhj.po.model.order.Orders;
import com.jhj.vo.order.OrgStaffDispatchVo;

public interface OrderPayService {

	void orderPaySuccessToDoForAm(Orders orders);

	boolean orderPaySuccessToDoForDeep(Orders orders);

	//手机话费充值， 微信支付成功后的 处理
	void orderPaySuccessToDoForPhone(Orders orders);

	Boolean orderPaySuccessToDoOrderPayExt(Orders orders, OrderPriceExt orderPriceExt);

	boolean orderPaySuccessToDoForHour(Long userId, Long orderId, boolean isChangeDispatch);
	
	
}
