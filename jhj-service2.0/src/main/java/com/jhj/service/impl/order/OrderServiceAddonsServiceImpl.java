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
		
		List<DictServiceAddons> dictServiceAddons = serviceAddonsService.getServiceAddonsTypes();
		
		DictServiceAddons item = null;
		
		for (int i =0; i < dictServiceAddons.size(); i++) {
			
			item = dictServiceAddons.get(i);
			if (!item.getServiceType().equals(2L) ) continue;
			OrderServiceAddonViewVo vo = new OrderServiceAddonViewVo();
			vo.setPrice(item.getPrice());
			vo.setItemNum(item.getDefaultNum());
			vo.setServiceAddonName(item.getName());
			vo.setOrderId(0L);
			BeanUtilsExp.copyPropertiesIgnoreNull(item, vo);
			for (OrderServiceAddons d : list) {
				if (item.getServiceAddonId().equals(d.getServiceAddonId())) {
					vo.setItemNum(d.getItemNum());
					vo.setOrderId(d.getOrderId());
					vo.setOrderNo(d.getOrderNo());
				}
			}
			result.add(vo);
		}
		
		return result;
	}	


}
