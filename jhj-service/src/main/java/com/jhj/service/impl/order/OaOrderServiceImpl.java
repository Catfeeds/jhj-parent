package com.jhj.service.impl.order;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jhj.common.Constants;
import com.jhj.po.dao.order.OrdersMapper;
import com.jhj.po.model.bs.DictCoupons;
import com.jhj.po.model.bs.OrgStaffs;
import com.jhj.po.model.bs.Orgs;
import com.jhj.po.model.cooperate.CooperativeBusiness;
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
import com.jhj.service.cooperate.CooperateBusinessService;
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
import com.jhj.vo.order.OaOrderListNewVo;
import com.jhj.vo.order.OaOrderListVo;
import com.jhj.vo.order.OrderDispatchSearchVo;
import com.meijia.utils.BeanUtilsExp;
import com.meijia.utils.DateUtil;
import com.meijia.utils.OneCareUtil;
import com.meijia.utils.TimeStampUtil;

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
	private OrderDispatchsService orderDispatchsService;
	
	@Autowired
	private OrdersService orderService;
	
	@Autowired
	private UserAddrsService userAddrService;
	
	@Autowired
	private DictCouponsService dictCouponService;
	
	@Autowired
	private UsersService userService;

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
	
	@Autowired
	private CooperateBusinessService cooperateBusinessService;
	
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
			oaOrderListVo.setPayType(orderPrices.getPayType());
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
		OrderDispatchSearchVo searchVo = new OrderDispatchSearchVo();
		searchVo.setOrderId(orders.getId());
		List<OrderDispatchs> disTwo = orderDispatchsService.selectBySearchVo(searchVo);

		// map 存放 <派工状态， 当前派工状态对应的 阿姨 >
		// IdentityHashMap<Short, String> statuNameMap = new
		// IdentityHashMap<Short, String>();

		Map<String, String> statuNameMap = new Hashtable<String, String>();
		if (disTwo.size() > 0) {

			for (int j = 0; j < disTwo.size(); j++) {
				OrderDispatchs od = disTwo.get(j);
				statuNameMap.put(od.getDispatchStatus() + "," + j, od.getStaffName());
				Short isApply = od.getIsApply();
				if(isApply==1){
					oaOrderListVo.setApplyStatus("否");
				}else {
					Long now = TimeStampUtil.getNowSecond();
					Long dispatchTime = od.getAddTime();
					Long lastTime = now - dispatchTime;
					if ( lastTime > 60 * 30) {
						oaOrderListVo.setApplyStatus("超");
					} else {
						oaOrderListVo.setApplyStatus("否");
					}
				}
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

		Users users = userService.selectByPrimaryKey(userId);
		if (users != null) {
			oaOrderListVo.setUserName(users.getName());
		}

		/*
		 * 可能 会有 多次换人，多条 无效记录 的情况，需要分别展示
		 */
		
		OrderDispatchSearchVo searchVo = new OrderDispatchSearchVo();
		searchVo.setOrderNo(orderNo);
		searchVo.setDispatchStatus(disStatus);
		List<OrderDispatchs> orderDisList = orderDispatchsService.selectBySearchVo(searchVo);
		
		
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
		
		
		oaOrderListVo.setDisWay((short)0);	//派工方案，  方案一 = 0. 方案二 = 1
		
		return oaOrderListVo;
	}
	/**
	 * 钟点工获取订单详情
	 */
	@Override
	public OaOrderListVo getOrderVoDetailHour(String orderNo, Short disStatus) {
		
		Orders orders = orderService.selectByOrderNo(orderNo);
		
		OaOrderListVo oaOrderListVo = completeVo(orders);
		
		oaOrderListVo.setApplyStatus("-");
		oaOrderListVo.setApplyTimeStr("");
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
		
		// 查找对应订单有效派工
		
		OrderDispatchSearchVo searchVo = new OrderDispatchSearchVo();
		searchVo.setOrderNo(orderNo);
		searchVo.setDispatchStatus((short) 1);
		List<OrderDispatchs> orderDisList = orderDispatchsService.selectBySearchVo(searchVo);
		
		if(orderDisList.size() > 0){
			
			OrderDispatchs dispatchs = orderDisList.get(0);
						
			//已经派工的员工id
			oaOrderListVo.setStaffId(dispatchs.getStaffId());
			
			
			OrgStaffs orgStaffs = staffService.selectByPrimaryKey(dispatchs.getStaffId());
			
			Long orgId = orgStaffs.getOrgId();
			
			Orgs orgs = orgService.selectByPrimaryKey(orgId);
			
			oaOrderListVo.setStaffName(orgs.getOrgName()+"--"+dispatchs.getStaffName());
			
			
			//是否接单状态;
			Short isApply = dispatchs.getIsApply();
			if (isApply.equals((short)1)) {
				oaOrderListVo.setApplyStatus("是");
				oaOrderListVo.setApplyTimeStr(TimeStampUtil.timeStampToDateStr(dispatchs.getApplyTime() * 1000));
			} else {
				Long now = TimeStampUtil.getNowSecond();
				Long dispatchTime = dispatchs.getAddTime();
				Long lastTime = now - dispatchTime;
				if ( lastTime > 60 * 30) {
					oaOrderListVo.setApplyStatus("超");
				} else {
					oaOrderListVo.setApplyStatus("否");
				}
			}
			
			
		}
				
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

		Users users = userService.selectByPrimaryKey(userId);
		if (users != null) {
			oaOrderListVo.setUserName(users.getName());
		}

		/*
		 * 可能 会有 多次换人，多条 无效记录 的情况，需要分别展示
		 */
		OrderDispatchSearchVo searchVo = new OrderDispatchSearchVo();
		searchVo.setOrderNo(orderNo);
		searchVo.setDispatchStatus((short) 1);
		List<OrderDispatchs> orderDisList = orderDispatchsService.selectBySearchVo(searchVo);

		// 有派工记录 的订单，则展示 当前 阿姨

		for (OrderDispatchs orDis : orderDisList) {
			oaOrderListVo.setStaffName(orDis.getStaffName());
			oaOrderListVo.setStaffId(orDis.getStaffId());
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
			if(orders.getOrderStatus()>=Constants.ORDER_HOUR_STATUS_2){
				String payTypeName = OneCareUtil.getPayTypeName(orderPrices.getPayType());
				oaOrderListVo.setPayTypeName(payTypeName);
			}else{
				oaOrderListVo.setPayTypeName("-");
			}
			
			oaOrderListVo.setPayType(orderPrices.getPayType());
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

		// 查找出有效派工
		OrderDispatchSearchVo searchVo = new OrderDispatchSearchVo();
		searchVo.setOrderNo(orderNo);
		searchVo.setDispatchStatus((short) 1);
		List<OrderDispatchs> disList_yes = orderDispatchsService.selectBySearchVo(searchVo);
		
		// 查找出无效派工
		searchVo = new OrderDispatchSearchVo();
		searchVo.setOrderNo(orderNo);
		searchVo.setDispatchStatus((short) 0);
		List<OrderDispatchs> disList_no = orderDispatchsService.selectBySearchVo(searchVo);

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
		OrderDispatchSearchVo searchVo1 = new OrderDispatchSearchVo();
		searchVo1.setOrderNo(orderNo);
		searchVo1.setDispatchStatus((short) 1);
		List<OrderDispatchs> list = orderDispatchsService.selectBySearchVo(searchVo1);
		
		//如果有派工记录
		if(list.size() >0){
			
			OrderDispatchs dispatchs = list.get(0);
			//派工人员姓名
			oaOrderListNewVo.setStaffName(dispatchs.getStaffName());
			
			Long staffId = dispatchs.getStaffId();
			
			OrgStaffs staffs = staffService.selectByPrimaryKey(staffId);
			
			if(staffs != null){
				
				Orgs orgs2 = orgService.selectByPrimaryKey(staffs.getOrgId());
				
				if(orgs2 != null){
					
					//云店名称
					oaOrderListNewVo.setCloudOrgName(orgs2.getOrgName());
					
					Long parentId = orgs2.getParentId();
					
					//处理历史数据，  直接记录的是员工的  门店，而不是 jhj2.1的 云店
					if(parentId != 0L){
						//门店名称
						Orgs orgs3 = orgService.selectByPrimaryKey(parentId);
						oaOrderListNewVo.setOrgName(orgs3.getOrgName());
					}
				}
				
			}
			
			//是否接单状态;
			Short isApply = dispatchs.getIsApply();
			if (isApply.equals((short)1)) {
				oaOrderListNewVo.setApplyStatus("是");
			} else {
				Long now = TimeStampUtil.getNowSecond();
				Long dispatchTime = dispatchs.getAddTime();
				Long lastTime = now - dispatchTime;
				if ( lastTime > 60 * 30) {
					oaOrderListNewVo.setApplyStatus("超");
				} else {
					oaOrderListNewVo.setApplyStatus("否");
				}
			}
			
		}else{
			oaOrderListNewVo.setStaffName("暂无");
			
			oaOrderListNewVo.setCloudOrgName("");
			oaOrderListNewVo.setOrgName("");
			oaOrderListNewVo.setApplyStatus("-");
		}
		
		
		
		Long serviceType = orders.getServiceType();
		PartnerServiceType type = partnerService.selectByPrimaryKey(serviceType);
		//订单类型 具体名称 （具体到服务类型）, 如 钟点工-->金牌保洁初体验、金牌保洁深度体验。。。
		if(type !=null){
			oaOrderListNewVo.setOrderTypeName(type.getName());
		}else{
			oaOrderListNewVo.setOrderTypeName("");
		}
		
		//订单来源
		Long orderOpFrom = orders.getOrderOpFrom();
		if(orderOpFrom!=null){
			if(orderOpFrom==1){
				oaOrderListNewVo.setOrderOpFromName("来电订单");
			}else{
				CooperativeBusiness cooperativeBusiness = cooperateBusinessService.selectByPrimaryKey(orderOpFrom);
				oaOrderListNewVo.setOrderOpFromName(cooperativeBusiness.getBusinessName());
			}
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
