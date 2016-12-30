package com.jhj.service.order;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jhj.po.model.order.OrderDispatchPrices;
import com.jhj.po.model.order.OrderDispatchs;
import com.jhj.po.model.order.Orders;
import com.jhj.vo.order.OrderSearchVo;

/**
 *
 * @author :hulj
 * @Date : 2015年7月20日下午5:16:54
 * @Description: TODO
 *
 */
public interface OrderDispatchPriceService {
	int deleteByPrimaryKey(Long id);

    int insert(OrderDispatchPrices record);

    int insertSelective(OrderDispatchPrices record);

    OrderDispatchPrices selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(OrderDispatchPrices record);

    int updateByPrimaryKey(OrderDispatchPrices record);
       
    OrderDispatchPrices  initOrderDisp();
    
    List<OrderDispatchPrices> selectBySearchVo(OrderSearchVo searchVo);

	PageInfo selectByListPage(OrderSearchVo searchVo, int pageNo, int pageSize);

	boolean doOrderDispatchPrice(Orders order, OrderDispatchs orderDispatch);

	Map<String, String> getTotalOrderMoneyMultiStat(OrderSearchVo searchVo);

}
