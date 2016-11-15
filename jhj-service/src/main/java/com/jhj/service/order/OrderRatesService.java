package com.jhj.service.order;

import java.util.HashMap;
import java.util.List;

import com.github.pagehelper.PageInfo;
import com.jhj.po.model.order.OrderRates;
import com.jhj.vo.order.OrderDispatchSearchVo;
import com.jhj.vo.order.OrderRatesVo;
import com.jhj.vo.order.OrderStaffRateVo;
import com.jhj.vo.staff.OrgStaffRateVo;

public interface OrderRatesService {

	int deleteByPrimaryKey(Long id);

	int insertSelective(OrderRates record);

	int insert(OrderRates record);

	int updateByPrimaryKeySelective(OrderRates orderRates);

	int updateByPrimaryKey(OrderRates record);

	OrderRates initOrderRates();

	OrderRates selectByPrimaryKey(Long id);

	List<OrderRates> selectBySearchVo(OrderDispatchSearchVo searchVo);

	PageInfo selectByListPage(OrderDispatchSearchVo searchVo, int pageNo, int pageSize);

	HashMap totalByStaff(OrderDispatchSearchVo searchVo);
	
	OrderRatesVo transVo(OrderRates orderRate);

	List<OrderStaffRateVo> changeToOrderStaffReteVo(List<OrderRates> list);

	List<OrgStaffRateVo> changeToOrgStaffReteVo(List<OrderRates> list);

}
