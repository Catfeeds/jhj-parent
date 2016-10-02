package com.jhj.service.impl.order;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jhj.po.dao.order.OrderServiceAddonsMapper;
import com.jhj.po.model.dict.DictServiceAddons;
import com.jhj.po.model.order.OrderServiceAddons;
import com.jhj.service.dict.ServiceAddonsService;
import com.jhj.service.order.OrderServiceAddonsService;
import com.jhj.vo.ServiceAddonSearchVo;
import com.jhj.vo.order.OrderServiceAddonViewVo;
import com.meijia.utils.BeanUtilsExp;
import com.meijia.utils.TimeStampUtil;

/**
 *
 * @author :hulj
 * @Date : 2015年7月21日上午10:29:22
 * @Description: TODO
 *
 */
@Service
public class OrderServiceAddonsServiceImpl implements OrderServiceAddonsService {
	
	@Autowired
	private OrderServiceAddonsMapper orderSerAddMapper;
	
	@Autowired
	private ServiceAddonsService serviceAddonsService;
	
	@Override
	public int deleteByPrimaryKey(Long id) {
		return orderSerAddMapper.deleteByPrimaryKey(id);
	}

	@Override
	public int insert(OrderServiceAddons record) {
		return orderSerAddMapper.insert(record);
	}

	@Override
	public int insertSelective(OrderServiceAddons record) {
		return orderSerAddMapper.insertSelective(record);
	}

	@Override
	public OrderServiceAddons selectByPrimaryKey(Long id) {
		return orderSerAddMapper.selectByPrimaryKey(id);
	}

	@Override
	public int updateByPrimaryKeySelective(OrderServiceAddons record) {
		return orderSerAddMapper.updateByPrimaryKeySelective(record);
	}

	@Override
	public int updateByPrimaryKey(OrderServiceAddons record) {
		return orderSerAddMapper.updateByPrimaryKey(record);
	}

	@Override
	public OrderServiceAddons initOrderServiceAddons() {
		OrderServiceAddons orderServiceAddons = new OrderServiceAddons();
		orderServiceAddons.setItemNum((short)0);
		orderServiceAddons.setItemUnit("");
		orderServiceAddons.setOrderId(0L);
		orderServiceAddons.setOrderNo("");
		orderServiceAddons.setPrice(new BigDecimal(0.0));
		orderServiceAddons.setServiceAddonId(0L);
		orderServiceAddons.setUserId(0L);
		orderServiceAddons.setAddTime(TimeStampUtil.getNowSecond());
		return orderServiceAddons;
	}
	
	@Override
	public List<OrderServiceAddons> selectByOrderId(Long orderId) {
		return orderSerAddMapper.selectByOrderId(orderId);
	}

	@Override
	public List<OrderServiceAddons> selectByOrderNo(String orderNo) {
		
		return orderSerAddMapper.selectByOrderNo(orderNo);
	}

	@Override
	public int deleteByOrderNo(String orderNo) {
		return orderSerAddMapper.deleteByOrderNo(orderNo);
	}

	
	@Override
	public List<OrderServiceAddonViewVo> changeToOrderServiceAddons(List<OrderServiceAddons> list) {
		
		List<OrderServiceAddonViewVo> result = new ArrayList<OrderServiceAddonViewVo>();
		
		if (list.isEmpty()) return result;
		
		List<Long> serviceAddonIds = new ArrayList<Long>();
		
		for (OrderServiceAddons item : list) {
			if (!serviceAddonIds.contains(item.getServiceAddonId())) serviceAddonIds.add(item.getServiceAddonId());
		}
		
		ServiceAddonSearchVo searchVo1 = new ServiceAddonSearchVo();
		searchVo1.setServiceAddonIds(serviceAddonIds);
		List<DictServiceAddons> dictServiceAddons = serviceAddonsService.selectBySearchVo(searchVo1);
		
		OrderServiceAddons item = null;
		
		for (int i =0; i < list.size(); i++) {
			
			item = list.get(i);
			OrderServiceAddonViewVo vo = new OrderServiceAddonViewVo();
			
			BeanUtilsExp.copyPropertiesIgnoreNull(item, vo);
			
			vo.setPrice(item.getPrice());
			vo.setOrderId(0L);
			
			for (DictServiceAddons d : dictServiceAddons) {
				if (vo.getServiceAddonId().equals(d.getServiceAddonId())) {
					
					vo.setServiceAddonName(d.getName());
					
					vo.setDefaultNum(d.getDefaultNum());
					
					vo.setServiceHour(d.getServiceHour());
				}
			}
			result.add(vo);
		}
		
		return result;
	}	


}
