package com.jhj.service.order;

import java.util.List;

import com.jhj.po.model.order.OrderPrices;
import com.jhj.po.model.order.OrderServiceAddons;

/**
 * @description：
 * @author： kerryg
 * @date:2015年8月3日 
 */
public interface OrderExpCleanService {
	
	
	List<OrderServiceAddons> updateOrderServiceAddons(Long userId, Long serviceType, String serviceAddonsDatas);

	OrderPrices getOrderPriceOfOrderExpClean(Long userId, Long serviceType, String serviceAddonsDatas, Long orderOpFrom);

	

}
