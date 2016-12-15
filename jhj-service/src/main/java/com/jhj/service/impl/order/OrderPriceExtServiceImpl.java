package com.jhj.service.impl.order;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jhj.common.Constants;
import com.jhj.po.dao.order.OrderPriceExtMapper;
import com.jhj.po.model.bs.OrgStaffs;
import com.jhj.po.model.order.OrderAppoint;
import com.jhj.po.model.order.OrderDispatchs;
import com.jhj.po.model.order.OrderPriceExt;
import com.jhj.po.model.order.OrderPrices;
import com.jhj.po.model.order.Orders;
import com.jhj.po.model.orderReview.JhjSetting;
import com.jhj.po.model.user.Users;
import com.jhj.service.bs.OrgStaffsService;
import com.jhj.service.order.OrderAppointService;
import com.jhj.service.order.OrderDispatchsService;
import com.jhj.service.order.OrderPriceExtService;
import com.jhj.service.orderReview.SettingService;
import com.jhj.service.users.UsersService;
import com.jhj.utils.OrderUtils;
import com.jhj.vo.order.OrderDispatchSearchVo;
import com.jhj.vo.order.OrderSearchVo;
import com.meijia.utils.MathBigDecimalUtil;
import com.meijia.utils.TimeStampUtil;


@Service
public class OrderPriceExtServiceImpl implements OrderPriceExtService {
	
    @Autowired
    private OrderPriceExtMapper orderPriceExtMapper;
    
	@Autowired
	private OrderDispatchsService orderDispatchService;
	
	@Autowired
	private UsersService userService;
	
	@Autowired
	private OrderAppointService orderAppointService;
	
	@Autowired
	private OrgStaffsService orgStaffsService;
	
	@Autowired
	private SettingService settingService;
    

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
	
	/**
	 * 获得员工指定的订单的收入.
	 * 1. 普通会员 70%
	 * 2. 金牌会员 75%
	 * 3. 会员指定 78%
	 * @param orders
	 * @params staffId
	 * @return
	 */
	@Override
	public BigDecimal getOrderOverWorkIncoming(Orders order, OrderPriceExt op, Long staffId) {
		
		BigDecimal incoming = new BigDecimal(0);
		Long orderId = order.getId();
		
		BigDecimal orderPay = op.getOrderPay();
	
		//找出派工，是否为多个
		OrderDispatchSearchVo orderDispatchSearchVo = new OrderDispatchSearchVo();
		orderDispatchSearchVo.setOrderId(orderId);
		orderDispatchSearchVo.setDispatchStatus((short) 1);
		List<OrderDispatchs> orderDispatchs = orderDispatchService.selectBySearchVo(orderDispatchSearchVo);
		
		if (orderDispatchs.size() > 1) {
			orderPay = MathBigDecimalUtil.div(orderPay, new BigDecimal(orderDispatchs.size()));
		}
	
		OrgStaffs staffs = orgStaffsService.selectByPrimaryKey(staffId);
		Short level = staffs.getLevel();
		String settingLevel = "-level-" + level.toString();
		Short orderType = order.getOrderType();
		String settingTypeStr = OrderUtils.getOrderSettingType(orderType) + settingLevel;

		JhjSetting settingType = settingService.selectBySettingType(settingTypeStr);

		String settingTypeValue = "0.70";
		if (settingType != null) settingTypeValue = settingType.getSettingValue();
		//提出比例 
		BigDecimal incomingPercent = new BigDecimal(settingTypeValue);
		
		Long userId = order.getUserId();
		Users u = userService.selectByPrimaryKey(userId);
		int isVip = u.getIsVip();
		if (isVip == 1) incomingPercent = new BigDecimal(0.75);
		
		//判断是否为指定用户
		OrderDispatchSearchVo searchVo = new OrderDispatchSearchVo();
		searchVo.setOrderId(orderId);
		List<OrderAppoint> orderAppoints = orderAppointService.selectBySearchVo(searchVo);
		
		if (!orderAppoints.isEmpty()) {
			for (OrderAppoint oa : orderAppoints) {
				if (oa.getStaffId().equals(staffId)) {
					incomingPercent = new BigDecimal(0.78);
				}
			}
		}
	
		orderPay = orderPay.multiply(incomingPercent);		
		orderPay = MathBigDecimalUtil.round(orderPay, 2);
		
		incoming = incoming.add(orderPay);
		
		return incoming;
	}
	
	@Override
	public BigDecimal getTotalOrderExtPay(Orders order, Short orderExtType) {
		
		Long orderId = order.getId();
		BigDecimal orderPayExt = new BigDecimal(0);
		OrderSearchVo orderSearchVo = new OrderSearchVo();
		orderSearchVo.setOrderId(orderId);
		orderSearchVo.setOrderExtType(orderExtType);
		List<OrderPriceExt> orderPriceExts = this.selectBySearchVo(orderSearchVo);
		
		if (orderPriceExts.isEmpty()) return orderPayExt;
		
		for (OrderPriceExt item : orderPriceExts) {
			orderPayExt = orderPayExt.add(item.getOrderPay());
		}
		
		//找出派工，是否为多个
		OrderDispatchSearchVo orderDispatchSearchVo = new OrderDispatchSearchVo();
		orderDispatchSearchVo.setOrderId(orderId);
		orderDispatchSearchVo.setDispatchStatus((short) 1);
		List<OrderDispatchs> orderDispatchs = orderDispatchService.selectBySearchVo(orderDispatchSearchVo);
		
		if (orderDispatchs.size() > 1) {
			orderPayExt = MathBigDecimalUtil.div(orderPayExt, new BigDecimal(orderDispatchs.size()));
		}
		
		orderPayExt = MathBigDecimalUtil.round(orderPayExt, 2);
		return orderPayExt;
	}
}