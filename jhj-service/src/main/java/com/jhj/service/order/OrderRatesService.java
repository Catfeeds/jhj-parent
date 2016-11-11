package com.jhj.service.order;

import java.util.List;

import com.github.pagehelper.PageInfo;
import com.jhj.po.model.order.OrderPriceExt;
import com.jhj.po.model.order.OrderRates;
import com.jhj.vo.order.OrderDispatchSearchVo;

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

}
