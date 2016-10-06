package com.jhj.service.order;

import java.util.List;

import com.github.pagehelper.PageInfo;
import com.jhj.po.model.order.OrderDispatchs;
import com.jhj.vo.order.OrderDispatchVo;
import com.jhj.vo.order.OrderDispatchSearchVo;

/**
 *
 * @author :hulj
 * @Date : 2015年8月14日下午8:49:11
 * @Description:
 *               运营平台--订单管理--下单
 */
public interface OaOrderDisService {

	PageInfo selectOrderDisByListPage(OrderDispatchSearchVo searchVo, int pageNo, int pageSize);

	OrderDispatchVo compleVo(OrderDispatchs dispatchs);

	OrderDispatchVo initVo();

	List<OrderDispatchVo> getDisEveryDay(String day);
}
