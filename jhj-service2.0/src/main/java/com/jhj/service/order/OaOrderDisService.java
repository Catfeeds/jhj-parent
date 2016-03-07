package com.jhj.service.order;

import java.util.List;

import com.jhj.po.model.order.OrderDispatchs;
import com.jhj.vo.OaOrderDisSearchVo;
import com.jhj.vo.order.OaOrderDisVo;

/**
 *
 * @author :hulj
 * @Date : 2015年8月14日下午8:49:11
 * @Description: 
 *		运营平台--订单管理--下单
 */
public interface OaOrderDisService {
	
	List<OrderDispatchs> selectOrderDisByListPage(OaOrderDisSearchVo oaOrderDisSearchVo, 
			int pageNo, int pageSize);
	
	OaOrderDisVo  compleVo(OrderDispatchs dispatchs);
	
	OaOrderDisVo initVo();
	
	List<OaOrderDisVo>  getDisEveryDay(String day);
}
