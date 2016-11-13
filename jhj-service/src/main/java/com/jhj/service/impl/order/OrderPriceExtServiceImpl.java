package com.jhj.service.impl.order;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jhj.common.Constants;
import com.jhj.po.dao.order.OrderPriceExtMapper;
import com.jhj.po.model.order.OrderPriceExt;
import com.jhj.service.order.OrderPriceExtService;
import com.jhj.vo.order.OrderSearchVo;
import com.meijia.utils.MathBigDecimalUtil;
import com.meijia.utils.TimeStampUtil;


@Service
public class OrderPriceExtServiceImpl implements OrderPriceExtService {
	
    @Autowired
    private OrderPriceExtMapper orderPriceExtMapper;
    

    @Override
	public int deleteByPrimaryKey(Long id) {
		return orderPriceExtMapper.deleteByPrimaryKey(id);
	}

	@Override
	public int insert(OrderPriceExt record) {
		return orderPriceExtMapper.insert(record);
	}

	@Override
	public int insertSelective(OrderPriceExt record) {
		return orderPriceExtMapper.insertSelective(record);
	}

	@Override
	public OrderPriceExt selectByPrimaryKey(Long id) {
		return orderPriceExtMapper.selectByPrimaryKey(id);
	}

	@Override
	public int updateByPrimaryKey(OrderPriceExt record) {
		return orderPriceExtMapper.updateByPrimaryKey(record);
	}
	
	@Override
	public int updateByPrimaryKeySelective(OrderPriceExt record) {
		return orderPriceExtMapper.updateByPrimaryKeySelective(record);
	}

	@Override
	public OrderPriceExt initOrderPriceExt() {
		
		OrderPriceExt record = new OrderPriceExt();
		record.setId(0L);
		record.setUserId(0L);
		record.setMobile("");
		record.setOrderId(0L);
		record.setOrderNo("");
		record.setOrderNoExt("");
		record.setOrderExtType(0);
		record.setServiceHour(0);
		record.setPayType((short)Constants.PAY_TYPE_0);
		BigDecimal defaultValue = new BigDecimal(0);
		record.setOrderPay(defaultValue);
		record.setOrderStatus(1);
		record.setRemarks("");
		record.setAddTime(TimeStampUtil.getNowSecond());
		record.setUpdateTime(TimeStampUtil.getNowSecond());
		return record;
	}
	
	@Override
	public OrderPriceExt selectByOrderNoExt(String orderNoExt) {
		return orderPriceExtMapper.selectByOrderNoExt(orderNoExt);
	}
		
	@Override
	public List<OrderPriceExt> selectBySearchVo(OrderSearchVo searchVo) {
		return orderPriceExtMapper.selectBySearchVo(searchVo);
	}
	
	@Override
	public String getOverWorkStr(Long orderId) {
		String overWorkStr = "";
		OrderSearchVo osearchVo = new OrderSearchVo();
		osearchVo.setOrderId(orderId);
		osearchVo.setOrderExtType((short) 1);
		List<OrderPriceExt> list = this.selectBySearchVo(osearchVo);
		
		if (!list.isEmpty()) {
			for (OrderPriceExt oe : list) {
				overWorkStr+= "加时" + oe.getServiceHour() + "小时";
				overWorkStr+= ",价格" + MathBigDecimalUtil.round2(oe.getOrderPay());
			}
		}
		return overWorkStr;
	}

	

}