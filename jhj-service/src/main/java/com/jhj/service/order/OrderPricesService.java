package com.jhj.service.order;

import java.math.BigDecimal;
import java.util.List;

import com.jhj.po.model.order.OrderPrices;
import com.jhj.po.model.order.Orders;


public interface OrderPricesService {

	OrderPrices initOrderPrices();

	int deleteByPrimaryKey(Long id);

	int insert(OrderPrices record);

	int insertSelective(OrderPrices record);

	//OrderPrices selectByOrderId(Long id);

	OrderPrices selectByPrimaryKey(Long id);

	int updateByPrimaryKeySelective(OrderPrices record);

	int updateByPrimaryKey(OrderPrices record);

	OrderPrices selectByOrderId(Long id);

	List<OrderPrices> selectByOrderIds(List<Long> orderIds);

	OrderPrices selectByOrderIds(String orderNo);

	OrderPrices selectByOrderNo(String orderNo);

	BigDecimal getPayByOrder(Long orderId, Long userCouponId);

	BigDecimal getOrderPay(OrderPrices orderPrice);

	BigDecimal getOrderMoney(OrderPrices orderPrice);

	BigDecimal getOrderIncoming(Orders order, Long staffId);

	BigDecimal getOrderMoneyStaff(Orders order, Long staffId);
	
}