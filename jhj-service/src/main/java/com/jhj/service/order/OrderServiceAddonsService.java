package com.jhj.service.order;

import java.util.List;

import com.jhj.po.model.order.OrderServiceAddons;
import com.jhj.vo.order.OrderServiceAddonViewVo;

/**
 *
 * @author :hulj
 * @Date : 2015年7月21日上午10:28:11
 * @Description: TODO
 *
 */
public interface OrderServiceAddonsService {
	
	 int deleteByPrimaryKey(Long id);

	 int deleteByOrderNo(String orderNo);

    int insert(OrderServiceAddons record);

    int insertSelective(OrderServiceAddons record);

    OrderServiceAddons selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(OrderServiceAddons record);

    int updateByPrimaryKey(OrderServiceAddons record);
    
    OrderServiceAddons initOrderServiceAddons();

    List<OrderServiceAddons> selectByOrderNo(String orderNo);

	List<OrderServiceAddonViewVo> changeToOrderServiceAddons(List<OrderServiceAddons> list);
    
}
