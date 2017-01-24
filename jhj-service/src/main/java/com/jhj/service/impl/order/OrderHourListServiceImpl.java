package com.jhj.service.impl.order;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.jhj.common.Constants;
import com.jhj.po.dao.order.OrdersMapper;
import com.jhj.po.model.order.OrderDispatchs;
import com.jhj.po.model.order.OrderPrices;
import com.jhj.po.model.order.Orders;
import com.jhj.po.model.university.PartnerServiceType;
import com.jhj.po.model.user.UserAddrs;
import com.jhj.po.model.user.UserCoupons;
import com.jhj.service.dict.ServiceTypeService;
import com.jhj.service.order.OrderDispatchsService;
import com.jhj.service.order.OrderHourListService;
import com.jhj.service.order.OrderPricesService;
import com.jhj.service.order.OrdersService;
import com.jhj.service.university.PartnerServiceTypeService;
import com.jhj.service.users.UserAddrsService;
import com.jhj.service.users.UserCouponsService;
import com.jhj.vo.order.OrderDispatchSearchVo;
import com.jhj.vo.order.OrderHourListVo;
import com.jhj.vo.order.OrderSearchVo;
import com.meijia.utils.BeanUtilsExp;
import com.meijia.utils.DateUtil;
import com.meijia.utils.OneCareUtil;
import com.meijia.utils.StringUtil;
import com.meijia.utils.TimeStampUtil;
import com.meijia.utils.Week;

/**
 *
 * @author :hulj
 * @Date : 2015年8月4日上午11:42:33
 * @Description: TODO
 *
 */
@Service
public class OrderHourListServiceImpl implements OrderHourListService {

	@Autowired
	private OrdersMapper orderMapper;
	
	@Autowired
	private OrderPricesService orderPriceService;

	@Autowired
	private UserAddrsService userAddrService;
	@Autowired
	private ServiceTypeService dictServiceTypeSerivice;

	@Autowired
	private OrdersService orderService;

	@Autowired
	private PartnerServiceTypeService partService;
	
	@Autowired
	private UserCouponsService userCouponsService;
	
	@Autowired
	private OrderDispatchsService orderDispatchService;

	/*
	 * 统计有订单的日期
	 */
	@Override
	public List<HashMap> userTotalByMonth(OrderSearchVo orderSearchVo) {

		List<HashMap> list = orderMapper.userTotalByMonth(orderSearchVo);

		return list;
	}

	/*
	 * 统计某月订单总数和未完成的订单总数.
	 */
	@Override
	public List<HashMap> userAllTotalByMonth(OrderSearchVo orderSearchVo) {

		List<HashMap> list = orderMapper.userAllTotalByMonth(orderSearchVo);

		return list;
	}

	/*
	 * 所有订单
	 * 更新过期的订单
	 */
	@Override
	public List<Orders> selectByUserListPage(OrderSearchVo orderSearchVo, int pageNo, int pageSize) {
		PageHelper.startPage(pageNo, pageSize);

		long date = TimeStampUtil.getNowSecond();
		List<Orders> lists = orderMapper.selectByListPage(orderSearchVo);
		for (Orders order : lists) {
			Long serviceDate = order.getServiceDate();
			short orderStatus = order.getOrderStatus();
			if (serviceDate != null && serviceDate != 0 && serviceDate < date && isOrderStatus(orderStatus)) {
				order.setOrderStatus(Constants.ORDER_STATUS_0);
				orderMapper.updateByPrimaryKeySelective(order);
			}
		}

		List<Orders> list = orderMapper.selectByListPage(orderSearchVo);
		return list;
	}

	/*
	 * 判断订单状态
	 */
	public boolean isOrderStatus(short orderstatus) {
		boolean flag = false;
		if (orderstatus == Constants.ORDER_AM_STATUS_1 || orderstatus == Constants.ORDER_AM_STATUS_2) {
			flag = true;
		}
		return flag;
	}

	/*
	 * 当前订单
	 */
	@Override
	public List<Orders> selectNowOrderHourListByUserId(Long userId, int pageNo, int pageSize) {
		PageHelper.startPage(pageNo, pageSize);

		OrderSearchVo orderSearchVo = new OrderSearchVo();
		orderSearchVo.setUserId(userId);

		List<Short> orderStatusList = new ArrayList<Short>();
		orderStatusList.add((short) 1);
		orderStatusList.add((short) 2);
		orderStatusList.add((short) 3);
		orderStatusList.add((short) 4);
		orderStatusList.add((short) 5);
		orderStatusList.add((short) 6);
		orderSearchVo.setOrderStatusList(orderStatusList);

		List<Orders> list = orderMapper.selectByListPage(orderSearchVo);

		return list;
	}

	/*
	 * 历史订单
	 */
	@Override
	public List<Orders> selectOldOrderHourListByUserId(Long userId, int pageNo, int pageSize) {
		PageHelper.startPage(pageNo, pageSize);

		OrderSearchVo orderSearchVo = new OrderSearchVo();
		orderSearchVo.setUserId(userId);

		List<Short> orderStatusList = new ArrayList<Short>();
		orderStatusList.add((short) 0);
		orderStatusList.add((short) 7);
		orderStatusList.add((short) 9);
		orderSearchVo.setOrderStatusList(orderStatusList);

		List<Orders> list = orderMapper.selectByListPage(orderSearchVo);

		return list;
	}

	/*
	 * 设置 vo 相关字段供 展示
	 */
	@Override
	public List<OrderHourListVo> transOrderHourListVo(List<Orders> orderHourList) {

		List<OrderHourListVo> voList = new ArrayList<OrderHourListVo>();

		if (orderHourList.isEmpty())
			return voList;

		for (Orders orders : orderHourList) {
			OrderHourListVo orderHourListVo = initOHLV();
			BeanUtilsExp.copyPropertiesIgnoreNull(orders, orderHourListVo);

			// 订单类型
			Short orderType = orders.getOrderType();

			Long serviceType = orders.getServiceType();

			PartnerServiceType type = partService.selectByPrimaryKey(serviceType);

			if (type != null) {
				orderHourListVo.setServiceTypeName(type.getName());
			} else {
				orderHourListVo.setServiceTypeName(OneCareUtil.getJhjOrderTypeName(orderType));
			}

			
			// 地址
			UserAddrs userAddrs = userAddrService.selectByPrimaryKey(orders.getAddrId());

			orderHourListVo.setAddrId(orders.getAddrId());
			if (userAddrs != null) {
				orderHourListVo.setAddress(userAddrs.getName() + " " + userAddrs.getAddr());
			}
			
			//服务日期，格式为 yyy-MM-dd(周几) hh:mm
			Long serviceDateTime = orders.getServiceDate();
			
			Short orderStatus = orders.getOrderStatus();
			Long now = TimeStampUtil.getNowSecond();
			if (serviceDateTime < now && orderStatus == 1) {
				orderStatus = Constants.ORDER_HOUR_STATUS_0;
				orderHourListVo.setOrderStatus(Constants.ORDER_HOUR_STATUS_0);
			}
			
			// 订单状态
			orderHourListVo.setOrderStatusName(OneCareUtil.getJhjOrderStausNameFromOrderType(orderType, orderStatus));

			String serviceDatePart1 = TimeStampUtil.timeStampToDateStr(serviceDateTime * 1000, "yyyy-MM-dd");
			String serviceDatePart2 = TimeStampUtil.timeStampToDateStr(serviceDateTime * 1000, "HH:mm");
			Date serviceDate = TimeStampUtil.timeStampToDate(serviceDateTime * 1000);
			Week w = DateUtil.getWeek(serviceDate);
			
			String serviceDateStr = serviceDatePart1 + "(" + w.getChineseName() + ")" + " " + serviceDatePart2;
			orderHourListVo.setServiceDateStr(serviceDateStr);
			
			OrderPrices orderPrice = orderPriceService.selectByOrderId(orders.getId());
			if (orderPrice != null) {
				BigDecimal orderPay = orderPriceService.getTotalOrderPay(orderPrice);
				orderHourListVo.setOrderPay(orderPay);
				orderHourListVo.setPayType(orderPrice.getPayType());
				orderHourListVo.setCouponId(orderPrice.getCouponId());
				orderHourListVo.setCouponValue(new BigDecimal(0));
				if (orderPrice.getCouponId() > 0L) {
					UserCoupons userCoupon = userCouponsService.selectByPrimaryKey(orderPrice.getCouponId());
					if (userCoupon != null) orderHourListVo.setCouponValue(userCoupon.getValue());
				}
			}
			
			//订单派工信息
			orderHourListVo.setStaffNames("");
			
			if (orderHourListVo.getOrderStatus() >= 3) {
				OrderDispatchSearchVo searchVo = new OrderDispatchSearchVo();
				searchVo.setOrderId(orderHourListVo.getId());
				searchVo.setDispatchStatus((short) 1);
				List<OrderDispatchs> list = orderDispatchService.selectBySearchVo(searchVo);
				
				
				String staffNames = "";
				for (OrderDispatchs item : list) {
					staffNames+=item.getStaffName() + ",";
				}
				
				if (!StringUtil.isEmpty(staffNames)) {
					staffNames = staffNames.substring(0, staffNames.length() -1 );
				}
				orderHourListVo.setStaffNames(staffNames);
				if(!list.isEmpty()){
					orderHourListVo.setStaffMobile(list.get(0).getStaffMobile());
				}
			}
			
			voList.add(orderHourListVo);

		}

		return voList;
	}

	@Override
	public OrderHourListVo initOHLV() {
		Orders orders = orderService.initOrders();
		OrderHourListVo hourListVo = new OrderHourListVo();

		BeanUtilsExp.copyPropertiesIgnoreNull(orders, hourListVo);
		hourListVo.setServiceTypeName("");
		hourListVo.setOrderStatusName("");
		hourListVo.setAddrId(0L);
		hourListVo.setAddress("");
		hourListVo.setServiceDateStr("");
		hourListVo.setOrderPay(new BigDecimal(0));

		return hourListVo;
	}

}
