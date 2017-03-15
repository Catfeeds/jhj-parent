package com.jhj.service.order;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

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

	BigDecimal getTotalOrderMoney(OrderPrices orderPrice);

	BigDecimal getTotalOrderPay(OrderPrices orderPrice);
	
//	BigDecimal getTotalOrderIncoming(Orders order, Long staffId);	
	
	BigDecimal getTotalOrderDept(Orders order, Long staffId);

	BigDecimal getOrderPercent(Orders order, Long staffId);

	Map<String, String> getTotalOrderIncomingHour(Orders order, Long staffId);

	Map<String, String> getTotalOrderIncomingDeep(Orders order, Long staffId);
	
//	BigDecimal getOrderMoneyStaff(Orders order, Long staffId);
//
//	BigDecimal getOrderPayStaff(Orders order, Long staffId);

	

	
}