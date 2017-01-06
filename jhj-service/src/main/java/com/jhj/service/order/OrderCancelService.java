package com.jhj.service.order;

import com.jhj.po.model.order.Orders;
import com.meijia.utils.vo.AppResultData;

public interface OrderCancelService {

	AppResultData<Object> cancleOrderDone(Orders orders);
	
}