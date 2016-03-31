package com.jhj.service.impl.order;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.jhj.common.Constants;
import com.jhj.po.dao.order.OrderDispatchsMapper;
import com.jhj.po.dao.order.OrdersMapper;
import com.jhj.po.model.bs.DictCoupons;
import com.jhj.po.model.bs.OrgStaffs;
import com.jhj.po.model.bs.Orgs;
import com.jhj.po.model.order.OrderDispatchs;
import com.jhj.po.model.order.OrderPrices;
import com.jhj.po.model.order.Orders;
import com.jhj.po.model.university.PartnerServiceType;
import com.jhj.po.model.user.UserAddrs;
import com.jhj.po.model.user.UserCoupons;
import com.jhj.po.model.user.Users;
import com.jhj.service.bs.DictCouponsService;
import com.jhj.service.bs.OrgStaffsService;
import com.jhj.service.bs.OrgsService;
import com.jhj.service.newDispatch.NewDispatchStaffService;
import com.jhj.service.order.DispatchStaffFromOrderService;
import com.jhj.service.order.OaOrderService;
import com.jhj.service.order.OrderDispatchsService;
import com.jhj.service.order.OrderHourAddService;
import com.jhj.service.order.OrderPricesService;
import com.jhj.service.order.OrdersService;
import com.jhj.service.university.PartnerServiceTypeService;
import com.jhj.service.users.UserAddrsService;
import com.jhj.service.users.UserCouponsService;
import com.jhj.service.users.UsersService;
import com.jhj.vo.OaOrderSearchVo;
import com.jhj.vo.order.OaOrderListNewVo;
import com.jhj.vo.order.OaOrderListVo;
import com.jhj.vo.order.OrgStaffsNewVo;
import com.jhj.vo.order.newDispatch.DisStaffWithUserVo;
import com.meijia.utils.BeanUtilsExp;
import com.meijia.utils.DateUtil;
import com.meijia.utils.OneCareUtil;

/**
 *
 * @author :hulj
 * @Date : 2015年8月12日上午11:37:40
 * @Description: 运营平台--订单管理--订单列表
 */
@Service
public class OaOrderServiceImpl implements OaOrderService {
	@Autowired
	private OrdersMapper orderMapper;
	@Autowired
	private OrderPricesService orderPriceService;
	@Autowired
	private OrderDispatchsService orderDisService;
	@Autowired
	private OrdersService orderService;
	@Autowired
	private UserAddrsService userAddrService;
	@Autowired
	private DictCouponsService dictCouponService;
	@Autowired
	private UsersService userService;
	@Autowired
	private OrderDispatchsMapper orderDisMapper;
	@Autowired
	private UserCouponsService userCouponService;
	@Autowired
	private OrderHourAddService hourAddService;
	@Autowired
	private OrgsService orgService;
	@Autowired
	private DispatchStaffFromOrderService dispatchStaffFromOrderService;

	//jhj2.1  派工service
	@Autowired
	private NewDispatchStaffService newDisStaService;
	
	@Autowired
	private PartnerServiceTypeService partnerService;
	
	@Autowired
	private OrgStaffsService staffService;
	
	@Override
	public List<Orders> selectVoByListPage(OaOrderSearchVo oaOrderSearchVo, int pageNo, int pageSize) {

		PageHelper.startPage(pageNo, pageSize);

		List<Orders> orderList = orderMapper.selectOaOrderByListPage(oaOrderSearchVo);

		return orderList;
	}

	@Override
	public OaOrderListNewVo completeVo(Orders orders) {
		OaOrderListVo oaOrderListVo = initVO();

		BeanUtilsExp.copyPropertiesIgnoreNull(orders, oaOrderListVo);

		String orderNo = orders.getOrderNo();

		OrderPrices orderPrices = orderPriceService.selectByOrderNo(orderNo);

		// 助理预约单，存在 ，没有价格 的 一个过程
		if (orderPrices != null) {
			// 订单总金额
			oaOrderListVo.setOrderMoney(orderPrices.getOrderMoney());
			// 订单实际支付金额
			oaOrderListVo.setOrderPay(orderPrices.getOrderPay());
			// 支付方式
			String payTypeName = OneCareUtil.getPayTypeName(orderPrices.getPayType());
			oaOrderListVo.setPayTypeName(payTypeName);

		}

		// 地址
		Long addrId = orders.getAddrId();
		UserAddrs userAddrs = userAddrService.selectByPrimaryKey(addrId);

		if (userAddrs != null) {
			oaOrderListVo.setOrderAddress(userAddrs.getName() + " " + userAddrs.getAddr());
		}

		// 门店名称
		Orgs orgs = orgService.selectByPrimaryKey(oaOrderListVo.getOrgId());

		if (orgs != null) {
			oaOrderListVo.setOrgName(orgs.getOrgName());
		}
		/*
		 * 通过 订单 Id，在 order_dispaths 中 找出 是否 有派工记录
		 */
		List<OrderDispatchs> disTwo = orderDisMapper.selectAllForOneOrderId(orders.getId());

		// map 存放 <派工状态， 当前派工状态对应的 阿姨 >
		// IdentityHashMap<Short, String> statuNameMap = new
		// IdentityHashMap<Short, String>();

		Map<String, String> statuNameMap = new Hashtable<String, String>();
		if (disTwo.size() > 0) {

			for (int j = 0; j < disTwo.size(); j++) {
				statuNameMap.put(disTwo.get(j).getDispatchStatus() + "," + j, disTwo.get(j).getStaffName());
			}

		} else {
			// 暂未派工,订单状态 < 4
			statuNameMap.put(3 + ",0", "无");
		}

		OaOrderListNewVo oaOrderListNewVo = new OaOrderListNewVo();

		BeanUtilsExp.copyPropertiesIgnoreNull(oaOrderListVo, oaOrderListNewVo);

		oaOrderListNewVo.setStatusNameMap(statuNameMap);

		return oaOrderListNewVo;
	}

	/*
	 * 查看订单详情
	 */
	@Override
	public OaOrderListVo getOrderVoDetail(String orderNo, Short disStatus) {

		Orders orders = orderService.selectByOrderNo(orderNo);

		OaOrderListVo oaOrderListVo = completeVo(orders);

		Long addTime = orders.getAddTime();

		String date = DateUtil.getDefaultDate(addTime * 1000);
		// 下单时间
		oaOrderListVo.setOrderDate(date);

		// 订单状态名称
		String orderStausName = OneCareUtil.getJhjOrderStausName(orders.getOrderStatus());

		oaOrderListVo.setOrderStatusName(orderStausName);

		// 优惠券

		OrderPrices orderPrices = orderPriceService.selectByOrderIds(orderNo);

		if (orderPrices != null) {
			Long couponId = orderPrices.getCouponId();
			if (couponId > 0L) {
				// 优惠券id
				UserCoupons userCoupons = userCouponService.selectByPrimaryKey(couponId);
				DictCoupons dictCoupon = dictCouponService.selectByPrimaryKey(userCoupons.getCouponId());

				// 优惠券面值
				if (userCoupons != null) {
					oaOrderListVo.setCouponValue(dictCoupon.getValue());
					oaOrderListVo.setCouponName(dictCoupon.getIntroduction());
				}
			}
		}

		// 姓名
		Long userId = orders.getUserId();

		Users users = userService.selectByUsersId(userId);
		if (users != null) {
			oaOrderListVo.setUserName(users.getName());
		}

		/*
		 * 可能 会有 多次换人，多条 无效记录 的情况，需要分别展示
		 */
		List<OrderDispatchs> orderDisList = orderDisService.selectByNoAndDisStatus(orderNo, disStatus);
		// 有派工记录 的订单，则展示 当前 阿姨

		for (OrderDispatchs orDis : orderDisList) {
			oaOrderListVo.setStaffName(orDis.getStaffName());
			oaOrderListVo.setStaffId(orDis.getStaffId());
		}

		// 状态 为 已支付 的 订单 的 可用 阿姨列表，否则 为null
		if (orders.getOrderStatus() == 4) {

			// 根据 派工逻辑 ，找出 这条 订单 的 符合条件的 阿姨
			List<OrgStaffs> staffList = hourAddService.getBestOrgStaff(orders.getUserId(), orders.getId());
			Map<Long, String> staMap = new HashMap<Long, String>();
			for (OrgStaffs orgStaffs : staffList) {
				staMap.put(orgStaffs.getStaffId(), orgStaffs.getName());

				oaOrderListVo.setStaffId(orgStaffs.getStaffId());
			}

			oaOrderListVo.setStaMap(staMap);
		}

		return oaOrderListVo;
	}
	

	@Override
	public OaOrderListVo initVO() {
		OaOrderListVo oaOrderListVo = new OaOrderListVo();

		Orders orders = orderService.initOrders();

		BeanUtilsExp.copyPropertiesIgnoreNull(orders, oaOrderListVo);

		oaOrderListVo.setOrderMoney(new BigDecimal(0));
		oaOrderListVo.setOrderPay(new BigDecimal(0));
		oaOrderListVo.setPayType((short) 0);// 付款方式 0 = 余额支付 1 = 支付宝 2 = 微信支付 3
											// = 智慧支付 4 = 上门刷卡（保留，站位）
		oaOrderListVo.setPayTypeName("");

		oaOrderListVo.setStaffName("");
		oaOrderListVo.setStaffMobile("");
		oaOrderListVo.setUserName("");
		oaOrderListVo.setOrderAddress("");
		oaOrderListVo.setCouponValue(new BigDecimal(0));

		oaOrderListVo.setCouponName("");

		oaOrderListVo.setCityName("");
		oaOrderListVo.setOrderDate("");

		oaOrderListVo.setStaffList(new ArrayList<OrgStaffs>());
		oaOrderListVo.setStaMap(new HashMap<Long, String>());

		oaOrderListVo.setDisStatus((short) 1);

		oaOrderListVo.setOrgName("");

		oaOrderListVo.setOrderTypeName("");//订单类型名称。。实际具体到 某种服务类型的名称
		
		return oaOrderListVo;
	}
	/**
	 * 钟点工获取订单详情
	 */
	@Override
	public OaOrderListVo getOrderVoDetailHour(String orderNo, Short disStatus) {
		
		Orders orders = orderService.selectByOrderNo(orderNo);
		
		OaOrderListVo oaOrderListVo = completeVo(orders);
		
		Long addTime = orders.getAddTime();
		
		String date = DateUtil.getDefaultDate(addTime * 1000);
		
		// 下单时间
		oaOrderListVo.setOrderDate(date);
		
		Long serviceDate = orders.getServiceDate();
		
		//服务时间
		String date1 = DateUtil.getDefaultDate(serviceDate * 1000);
		
		oaOrderListVo.setServiceDateStr(date1);
		
		// 订单状态名称
		String orderStausName = OneCareUtil.getJhjOrderStausNameFromOrderType(orders.getOrderType(),orders.getOrderStatus());
		oaOrderListVo.setOrderStatusName(orderStausName);
		
		// 优惠券
		OrderPrices orderPrices = orderPriceService.selectByOrderIds(orderNo);
		
		if (orderPrices != null) {
			Long couponId = orderPrices.getCouponId();
			if (couponId > 0L) {
				// 优惠券id
				UserCoupons userCoupons = userCouponService.selectByPrimaryKey(couponId);
				DictCoupons dictCoupon = dictCouponService.selectByPrimaryKey(userCoupons.getCouponId());
				
				// 优惠券面值
				if (userCoupons != null) {
					oaOrderListVo.setCouponValue(dictCoupon.getValue());
					oaOrderListVo.setCouponName(dictCoupon.getIntroduction());
				}
			}
		}
		
		UserAddrs userAddrs = userAddrService.selectByPrimaryKey(orders.getAddrId());
		if (userAddrs != null) {
			oaOrderListVo.setOrderAddress(userAddrs.getName()+""+userAddrs.getAddr());
		}
		
		/*
		 * 2016年3月23日19:05:58  jhj2.1     基础保洁类订单 新的基本派工逻辑
		 */
		List<Long> staIdList = newDisStaService.autoDispatchForBaseOrder(orders.getId(),orders.getServiceDate());
		
		Long addrId = orders.getAddrId();
		UserAddrs addrs = userAddrService.selectByPrimaryKey(addrId);
		
		
		
		// 查找对应订单有效派工
		List<OrderDispatchs> orderDisList = orderDisService.selectByNoAndDisStatus(orderNo, (short) 1);
		
		if(orderDisList.size() > 0){
			
			OrderDispatchs dispatchs = orderDisList.get(0);
			
			/*
			 *  如果  派工逻辑 未找到可用 派工。
			 * 
			 *  但是 已经有派工记录
			 */
			if(staIdList.size() == 0){
				staIdList.add(0, dispatchs.getStaffId());
			}
			
			//已经派工的员工id
			oaOrderListVo.setStaffId(dispatchs.getStaffId());
			
			oaOrderListVo.setStaffName(dispatchs.getStaffName());
		}else{
			
			/*
			 * 如果派工逻辑 未找到 可用派工 
			 * 	
			 * 也无 派工记录	
			 */
			if(staIdList.size() == 0){
				staIdList.add(0,0L);
			}
		}
		
	    //可用 服务人员 VO
		List<OrgStaffsNewVo> staVoList = newDisStaService.getTheNearestStaff(addrs.getLatitude(), addrs.getLongitude(), staIdList);
		
		oaOrderListVo.setVoList(staVoList);
		
		return oaOrderListVo;
	}
	/**
	 * 获取助理订单详情
	 */
	@Override
	public OaOrderListVo getOrderVoDetailAm(String orderNo, Short disStatus,String poiLongitude,String poiLatitude) {

		Orders orders = orderService.selectByOrderNo(orderNo);

		OaOrderListVo oaOrderListVo = completeNewVo(orders);

		Long addTime = orders.getAddTime();

		String date = DateUtil.getDefaultDate(addTime * 1000);
		// 下单时间
		oaOrderListVo.setOrderDate(date);
		// 订单状态名称
		String orderStausName = OneCareUtil.getJhjOrderStausNameFromOrderType(orders.getOrderType(), orders.getOrderStatus());
		oaOrderListVo.setOrderStatusName(orderStausName);
		// 优惠券
		OrderPrices orderPrices = orderPriceService.selectByOrderIds(orderNo);

		if (orderPrices != null) {
			Long couponId = orderPrices.getCouponId();
			if (couponId > 0L) {
				// 优惠券id
				UserCoupons userCoupons = userCouponService.selectByPrimaryKey(couponId);
				DictCoupons dictCoupon = dictCouponService.selectByPrimaryKey(userCoupons.getCouponId());

				// 优惠券面值
				if (userCoupons != null) {
					oaOrderListVo.setCouponValue(dictCoupon.getValue());
					oaOrderListVo.setCouponName(dictCoupon.getIntroduction());
				}
			}
		}
		// 姓名
		Long userId = orders.getUserId();

		Users users = userService.selectByUsersId(userId);
		if (users != null) {
			oaOrderListVo.setUserName(users.getName());
		}
		// 获的用户地址
	/*	UserAddrs userAddrs = userAddrService.selectByDefaultAddr(userId);
		if (userAddrs != null) {
			oaOrderListVo.setOrderAddress(userAddrs.getName()+""+userAddrs.getAddr());
		}*/
		
		//5.用户常用地址列表显示
		Map<String, String> userAddrMap = new HashMap<String, String>();
		List<UserAddrs> userAddrsList = userAddrService.selectByUserId(userId);
		if(userAddrsList!=null && userAddrsList.size()>0){
			for (UserAddrs addr : userAddrsList) {
				userAddrMap.put(addr.getName()+"="+addr.getAddr()+"="+addr.getLongitude()+"="+addr.getLatitude()
					,addr.getName()+addr.getAddr());
			}
		}
		oaOrderListVo.setUserAddrMap(userAddrMap);
		
		
		/*
		 * 2016年3月23日19:05:58  jhj2.1     助理类订单 新的基本派工逻辑
		 */
		List<Long> staIdList = newDisStaService.autoDispatchForBaseOrder(orders.getId(),orders.getServiceDate());
		
		Long addrId = orders.getAddrId();
		UserAddrs addrs = userAddrService.selectByPrimaryKey(addrId);
		
		if(addrs != null){
			
		 	staIdList = newDisStaService.autoDispatchForAmOrder(addrs.getLatitude(), addrs.getLongitude(), orders.getServiceType());
		}
		/*
		 * 设置一个 默认值。。解决 mybatis list参数size为0
		 */
		if(staIdList.size() == 0){
			staIdList.add(0, 0L);
		}
	    //可用 服务人员 VO
		List<OrgStaffsNewVo> staVoList = newDisStaService.getTheNearestStaff(addrs.getLatitude(), addrs.getLongitude(), staIdList);
		
		oaOrderListVo.setVoList(staVoList);
		
//		// 查找对应订单有效派工
//		List<OrderDispatchs> orderDisList = orderDisService.selectByNoAndDisStatus(orderNo, (short) 1);
//		// 有派工记录 的订单，则展示当前 阿姨
//		for (OrderDispatchs orDis : orderDisList) {
//			oaOrderListVo.setStaffName(orDis.getStaffName());
//			oaOrderListVo.setStaffMobile(orDis.getStaffMobile());
//			oaOrderListVo.setStaffId(orDis.getStaffId());
//		}
//		
//		
//		
//		// 查找对应订单有效派工
//		List<OrderDispatchs> orderDisList = orderDisService.selectByNoAndDisStatus(orderNo, (short) 1);
//		// 有派工记录 的订单，则展示 当前 阿姨
//		for (OrderDispatchs orDis : orderDisList) {
//			oaOrderListVo.setStaffName(orDis.getStaffName());
//			oaOrderListVo.setStaffMobile(orDis.getStaffMobile());
//			oaOrderListVo.setStaffId(orDis.getStaffId());
//		}
//		Long startTime = orders.getServiceDate();
//		Long endTime = (startTime + orders.getServiceHour() * 3600);
//
//		if (orders.getOrderStatus() == 1) {//预约状态显示确认派工
//			// 根据 派工逻辑 ，找出 这条 订单 的 符合条件的 阿姨
//			List<OrgStaffsNewVo> staffList = dispatchStaffFromOrderService.getNewBestStaffForAm(startTime, endTime,poiLongitude,poiLatitude, orders.getId());
//			// 保存可选的派工人员
//			oaOrderListVo.setVoList(staffList);
//		}
		return oaOrderListVo;
	}
	@Override
	public OaOrderListVo getOrderVoDetailAm(String orderNo, Short disStatus) {
		
		Orders orders = orderService.selectByOrderNo(orderNo);
		
		OaOrderListVo oaOrderListVo = completeNewVo(orders);
		
		//1.显示下单时间
		Long addTime = orders.getAddTime();
		String date = DateUtil.getDefaultDate(addTime * 1000);
		oaOrderListVo.setOrderDate(date);
		//2.显示订单状态名称
		String orderStausName = OneCareUtil.getJhjOrderStausNameFromOrderType(orders.getOrderType(), orders.getOrderStatus());
		oaOrderListVo.setOrderStatusName(orderStausName);
		//3.显示优惠券
		OrderPrices orderPrices = orderPriceService.selectByOrderIds(orderNo);
		if (orderPrices != null) {
			Long couponId = orderPrices.getCouponId();
			if (couponId > 0L) {
				// 优惠券id
				UserCoupons userCoupons = userCouponService.selectByPrimaryKey(couponId);
				DictCoupons dictCoupon = dictCouponService.selectByPrimaryKey(userCoupons.getCouponId());
				// 优惠券面值
				if (userCoupons != null) {
					oaOrderListVo.setCouponValue(dictCoupon.getValue());
					oaOrderListVo.setCouponName(dictCoupon.getIntroduction());
				}
			}
		}
		//4.显示用户姓名
		Long userId = orders.getUserId();
		Users users = userService.selectByUsersId(userId);
		if (users != null) {
			oaOrderListVo.setUserName(users.getName());
		}
		//5.用户常用地址列表显示
		Map<String, String> userAddrMap = new HashMap<String, String>();
		List<UserAddrs> userAddrsList = userAddrService.selectByUserId(userId);
		if(userAddrsList!=null && userAddrsList.size()>0){
			for (UserAddrs addr : userAddrsList) {
				userAddrMap.put(addr.getName()+"="+addr.getAddr()+"="+addr.getLongitude()+"="+addr.getLatitude()
					,addr.getName()+addr.getAddr());
			}
		}
		oaOrderListVo.setUserAddrMap(userAddrMap);
		
		/*
		 * 2016年3月23日19:05:58  jhj2.1     助理类订单 新的基本派工逻辑
		 */
		
		List<OrderDispatchs> list = orderDisService.selectByNoAndDisStatus(orderNo, Constants.ORDER_DIS_ENABLE);
		
		List<Long> staIdList = new ArrayList<Long>();
		
		List<OrgStaffsNewVo> staVoList = new ArrayList<OrgStaffsNewVo>();
		
		if(list.size() > 0){
			
			OrderDispatchs dispatchs = list.get(0);
			
			staIdList =  newDisStaService.autoDispatchForAmOrder(
							dispatchs.getPickAddrLat(), dispatchs.getPickAddrLng(), orders.getServiceType());
			
			/*
			 * 设置一个 默认值。。解决 mybatis list参数size为0
			 */
			if(staIdList.size() == 0){
				staIdList.add(0, 0L);
			}
			
		    //可用 服务人员 VO
			staVoList = newDisStaService.getTheNearestStaff(dispatchs.getPickAddrLat(), dispatchs.getPickAddrLng(), staIdList);
		}
		oaOrderListVo.setVoList(staVoList);
		
//		//6.查找对应订单有效派工,显示派工信息
//		List<OrderDispatchs> orderDisList = orderDisService.selectByNoAndDisStatus(orderNo, (short) 1);
//		for (OrderDispatchs orDis : orderDisList) {
//			oaOrderListVo.setStaffName(orDis.getStaffName());
//			oaOrderListVo.setStaffMobile(orDis.getStaffMobile());
//			oaOrderListVo.setPickAddrName(orDis.getPickAddrName());
//			oaOrderListVo.setPickAddr(orDis.getPickAddr());
//			oaOrderListVo.setUserAddrDistance(orDis.getUserAddrDistance());
//			oaOrderListVo.setDisStatus(orDis.getDispatchStatus());
//			oaOrderListVo.setStaffId(orDis.getStaffId());
//		}
		return oaOrderListVo;
	}
	
	
	/**
	 *	深度保洁订单详情
	 */
	@Override
	public OaOrderListVo getOrderExpVoDetail(String orderNo, Short disStatus) {

		Orders orders = orderService.selectByOrderNo(orderNo);

		OaOrderListVo oaOrderListVo = completeVo(orders);

		Long addTime = orders.getAddTime();

		String date = DateUtil.getDefaultDate(addTime * 1000);
		// 下单时间
		oaOrderListVo.setOrderDate(date);

		// 订单状态名称
		String orderStausName = OneCareUtil.getJhjOrderStausName(orders.getOrderStatus());

		oaOrderListVo.setOrderStatusName(orderStausName);

		// 优惠券

		OrderPrices orderPrices = orderPriceService.selectByOrderIds(orderNo);

		if (orderPrices != null) {
			Long couponId = orderPrices.getCouponId();
			if (couponId > 0L) {
				// 优惠券id
				UserCoupons userCoupons = userCouponService.selectByPrimaryKey(couponId);
				DictCoupons dictCoupon = dictCouponService.selectByPrimaryKey(userCoupons.getCouponId());

				// 优惠券面值
				if (userCoupons != null) {
					oaOrderListVo.setCouponValue(dictCoupon.getValue());
					oaOrderListVo.setCouponName(dictCoupon.getIntroduction());
				}
			}
		}

		// 姓名
		Long userId = orders.getUserId();

		Users users = userService.selectByUsersId(userId);
		if (users != null) {
			oaOrderListVo.setUserName(users.getName());
		}

		/*
		 * 可能 会有 多次换人，多条 无效记录 的情况，需要分别展示
		 */
		List<OrderDispatchs> orderDisList = orderDisService.selectByNoAndDisStatus(orderNo, (short)1);
		// 有派工记录 的订单，则展示 当前 阿姨

		for (OrderDispatchs orDis : orderDisList) {
			oaOrderListVo.setStaffName(orDis.getStaffName());
			oaOrderListVo.setStaffId(orDis.getStaffId());
		}

		return oaOrderListVo;
	}
	/**
	 * 配送订单详情
	 * 
	 * @param orderNo
	 * @param disStatus
	 * @return
	 */
	@Override
	public OaOrderListVo getOrderVoDetailDel(String orderNo, Short disStatus) {

		Orders orders = orderService.selectByOrderNo(orderNo);

		OaOrderListVo oaOrderListVo = completeVo(orders);

		Long addTime = orders.getAddTime();

		String date = DateUtil.getDefaultDate(addTime * 1000);
		// 下单时间
		oaOrderListVo.setOrderDate(date);
		// 订单状态名称
		String orderStausName = OneCareUtil.getJhjOrderStausNameFromOrderType(orders.getOrderType(), orders.getOrderStatus());
		oaOrderListVo.setOrderStatusName(orderStausName);
		// 优惠券
		OrderPrices orderPrices = orderPriceService.selectByOrderIds(orderNo);
		if (orderPrices != null) {
			Long couponId = orderPrices.getCouponId();
			if (couponId > 0L) {
				// 优惠券id
				UserCoupons userCoupons = userCouponService.selectByPrimaryKey(couponId);
				DictCoupons dictCoupon = dictCouponService.selectByPrimaryKey(userCoupons.getCouponId());

				// 优惠券面值
				if (userCoupons != null) {
					oaOrderListVo.setCouponValue(dictCoupon.getValue());
					oaOrderListVo.setCouponName(dictCoupon.getIntroduction());
				}
			}
		}

		// 姓名
		Long userId = orders.getUserId();

		Users users = userService.selectByUsersId(userId);
		if (users != null) {
			oaOrderListVo.setUserName(users.getName());
		}
		// 获取用户默认地址
		UserAddrs userAddrs = userAddrService.selectByDefaultAddr(userId);
		if (userAddrs != null) {
			oaOrderListVo.setOrderAddress(userAddrs.getName()+""+userAddrs.getAddr());
		}

		List<OrderDispatchs> orderDisList = orderDisService.selectByNoAndDisStatus(orderNo, (short) 1);// 显示有效派工信息
		// 有派工记录 的订单，则展示 当前 阿姨
		for (OrderDispatchs orDis : orderDisList) {
			oaOrderListVo.setStaffName(orDis.getStaffName());
			oaOrderListVo.setStaffMobile(orDis.getStaffMobile());
			oaOrderListVo.setStaffId(orDis.getStaffId());
		}
		Long startTime = orders.getServiceDate();
		Long endTime = (startTime + orders.getServiceHour() * 3600);

		// 订单状态=4(已支付),进行服务人员查找
		if (orders.getOrderStatus() == 4) {
			// 根据 派工逻辑 ，找出 这条 订单 的 符合条件的 阿姨
			List<OrgStaffsNewVo> staffList = dispatchStaffFromOrderService.getNewBestStaffForDel(startTime, endTime, userAddrs.getId(), orders.getId());
			// 保存可选的派工人员
			oaOrderListVo.setVoList(staffList);
		}

		return oaOrderListVo;
	}

	@Override
	public OaOrderListNewVo completeNewVo(Orders orders) {
		OaOrderListVo oaOrderListVo = initVO();

		BeanUtilsExp.copyPropertiesIgnoreNull(orders, oaOrderListVo);

		String orderNo = orders.getOrderNo();

		OrderPrices orderPrices = orderPriceService.selectByOrderNo(orderNo);

		// 助理预约单，存在 ，没有价格 的 一个过程
		if (orderPrices != null) {
			// 订单总金额
			oaOrderListVo.setOrderMoney(orderPrices.getOrderMoney());
			// 订单实际支付金额
			oaOrderListVo.setOrderPay(orderPrices.getOrderPay());
			// 支付方式
			String payTypeName = OneCareUtil.getPayTypeName(orderPrices.getPayType());
			oaOrderListVo.setPayTypeName(payTypeName);
		}

		// 地址
		Long addrId = orders.getAddrId();
		UserAddrs userAddrs = userAddrService.selectByPrimaryKey(addrId);
		if(userAddrs!=null){
			if(orders.getOrderType()==Constants.ORDER_TYPE_1 ||
				orders.getOrderType()==Constants.ORDER_TYPE_0){//钟点工和深度保洁服务地址从userAddr获取
				oaOrderListVo.setOrderAddress(userAddrs.getName()+ " " + userAddrs.getAddress());
			}
		}
//		// 门店名称
		Orgs orgs = orgService.selectByPrimaryKey(oaOrderListVo.getOrgId());

		if (orgs != null) {
			oaOrderListVo.setOrgName(orgs.getOrgName());
		}
		// 查找出有效派工
		List<OrderDispatchs> disList_yes = orderDisService.selectByNoAndDisStatus(orderNo, (short) 1);
		// 查找出无效派工
		List<OrderDispatchs> disList_no = orderDisService.selectByNoAndDisStatus(orderNo, (short) 0);

		if (disList_yes !=null && disList_yes.size() > 0) {
			OrderDispatchs orderDispatchs= disList_yes.get(0);
			
			oaOrderListVo.setStaffId(orderDispatchs.getStaffId());
			
			oaOrderListVo.setDisStatusName("有效");
			oaOrderListVo.setStaffName(orderDispatchs.getStaffName());
			if(orders.getOrderType()==Constants.ORDER_TYPE_2){//助理订单服务地址从派工表获取
				oaOrderListVo.setOrderAddress(orderDispatchs.getPickAddrName() + " " + orderDispatchs.getPickAddr());
			}
		} else if (null != disList_no && disList_no.size() > 0) {
			oaOrderListVo.setDisStatusName("无效");
			oaOrderListVo.setStaffName("无");
		} else {
			oaOrderListVo.setDisStatusName("暂未派工");
			oaOrderListVo.setStaffName("无");
		}
		Map<String, String> statuNameMap = new Hashtable<String, String>();

		statuNameMap.put(orders.getOrderStatus() + ",", "");

		OaOrderListNewVo oaOrderListNewVo = new OaOrderListNewVo();

		BeanUtilsExp.copyPropertiesIgnoreNull(oaOrderListVo, oaOrderListNewVo);

		oaOrderListNewVo.setStatusNameMap(statuNameMap);
		
		
		/*
		 *  2016年3月29日17:43:59   细节修改。添加字段
		 *  
		 *   云店名称、派工人员姓名(如果已派工)、 订单具体类型
		 */
		
		List<OrderDispatchs> list = orderDisService.selectByNoAndDisStatus(orderNo, Constants.ORDER_DIS_ENABLE);
		
		//如果有派工记录
		if(list.size() >0){
			
			OrderDispatchs dispatchs = list.get(0);
			//派工人员姓名
			oaOrderListNewVo.setStaffName(dispatchs.getStaffName());
			
			Long staffId = dispatchs.getStaffId();
			
			OrgStaffs staffs = staffService.selectByPrimaryKey(staffId);
			
			
			Orgs orgs2 = orgService.selectByPrimaryKey(staffs.getOrgId());
			
			//云店名称
			oaOrderListNewVo.setCloudOrgName(orgs2.getOrgName());
			
			
		}else{
			oaOrderListNewVo.setStaffName("暂无");
			
			oaOrderListNewVo.setCloudOrgName("");
			oaOrderListNewVo.setOrgName("");
		}
		
		
		
		Long serviceType = orders.getServiceType();
		PartnerServiceType type = partnerService.selectByPrimaryKey(serviceType);
		//订单类型 具体名称 （具体到服务类型）, 如 钟点工-->金牌保洁初体验、金牌保洁深度体验。。。
		if(type !=null){
			oaOrderListNewVo.setOrderTypeName(type.getName());
		}else{
			oaOrderListNewVo.setOrderTypeName("");
		}
		
		
		return oaOrderListNewVo;
	}

	@Override
	public Map<String, String> getUserAddrMap(Long userId) {
		Map<String, String> userAddrMap = new HashMap<String, String>();
		List<UserAddrs> userAddrsList = userAddrService.selectByUserId(userId);
		if(userAddrsList!=null && userAddrsList.size()>0){
			for (UserAddrs addr : userAddrsList) {
				userAddrMap.put(addr.getName()+"="+addr.getAddr(),addr.getName()+addr.getAddr());
			}
		}
		return userAddrMap;
	}
	
}
