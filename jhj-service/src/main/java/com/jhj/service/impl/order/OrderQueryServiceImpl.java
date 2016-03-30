package com.jhj.service.impl.order;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jhj.common.Constants;
import com.jhj.po.dao.bs.DictCouponsMapper;
import com.jhj.po.dao.order.OrderDispatchsMapper;
import com.jhj.po.dao.order.OrdersMapper;
import com.jhj.po.dao.user.UserCouponsMapper;
import com.jhj.po.model.bs.DictCoupons;
import com.jhj.po.model.bs.OrgStaffs;
import com.jhj.po.model.bs.Orgs;
import com.jhj.po.model.order.OrderDispatchs;
import com.jhj.po.model.order.OrderPrices;
import com.jhj.po.model.order.Orders;
import com.jhj.po.model.orderReview.JhjSetting;
import com.jhj.po.model.user.UserAddrs;
import com.jhj.po.model.user.UserCoupons;
import com.jhj.po.model.user.Users;
import com.jhj.service.bs.OrgStaffsService;
import com.jhj.service.bs.OrgsService;
import com.jhj.service.dict.DictService;
import com.jhj.service.order.OrderDispatchsService;
import com.jhj.service.order.OrderPricesService;
import com.jhj.service.order.OrderQueryService;
import com.jhj.service.orderReview.SettingService;
import com.jhj.service.users.UserAddrsService;
import com.jhj.service.users.UsersService;
import com.jhj.utils.OrderUtils;
import com.jhj.vo.OrderQuerySearchVo;
import com.jhj.vo.OrderSearchVo;
import com.jhj.vo.order.OrderDetailVo;
import com.jhj.vo.order.OrderListVo;
import com.jhj.vo.order.OrderViewVo;
import com.jhj.vo.order.UserListVo;
import com.meijia.utils.BeanUtilsExp;
import com.meijia.utils.MathBigDeciamlUtil;
import com.meijia.utils.OneCareUtil;
import com.meijia.utils.StringUtil;
import com.meijia.utils.TimeStampUtil;

@Service
public class OrderQueryServiceImpl implements OrderQueryService {

	@Autowired
	private OrdersMapper ordersMapper;

	@Autowired
	private OrderPricesService orderPricesService;

	@Autowired
	private UsersService usersService;

	@Autowired
	private OrderDispatchsService orderDispatchsService;

	@Autowired
	private UserAddrsService userAddrsService;

	@Autowired
	DictService dictService;

	@Autowired
	DictCouponsMapper dictCouponsMapper;

	@Autowired
	private UserCouponsMapper userCouponsMapper;

	@Autowired
	private OrderDispatchsMapper orderDispatchsMapper;

	@Autowired
	private SettingService settingService;
	
	@Autowired
	private OrgStaffsService orgStaffsService;
	
	@Autowired
	private OrgsService orgsService;

	/*
	 * 进行orderViewVo 结合了 orders , order_prices, 两张表的元素
	 */
	@Override
	public OrderViewVo getOrderView(Orders order) {

		// 加载更多订单的信息
		OrderViewVo vo = new OrderViewVo();
		BeanUtils.copyProperties(order, vo);

		// 订单价格信息
		vo.setOrderMoney(new BigDecimal(0));
		vo.setOrderPay(new BigDecimal(0));
		vo.setPayType((short) 0);
		OrderPrices orderPrice = orderPricesService.selectByOrderId(vo.getId());
		if (orderPrice != null) {
			vo.setPayType(orderPrice.getPayType());
			vo.setOrderMoney(orderPrice.getOrderMoney());
			vo.setOrderPay(orderPrice.getOrderPay());
		}

		// 城市名称
		vo.setCityName("");
		if (vo.getCityId() > 0L) {
			String cityName = dictService.getCityName(vo.getCityId());
			vo.setCityName(cityName);
		}

		// 服务类型名称
		vo.setServiceTypeName("");
		if (vo.getServiceType() > 0L) {
			String serviceTypeName = dictService.getServiceTypeName(vo.getServiceType());
			vo.setServiceTypeName(serviceTypeName);
		}

		// 用户称呼
		vo.setName("");
		if (vo.getUserId() > 0L) {
			Users user = usersService.getUserById(vo.getUserId());
			vo.setName(user.getName());
		}

		// 用户地址
		vo.setServiceAddr("");
		if (vo.getAddrId() > 0L) {
			UserAddrs userAddr = userAddrsService.selectByPrimaryKey(vo.getAddrId());
			vo.setServiceAddr(userAddr.getName() + userAddr.getAddr());
		}
		// 订单类型
		vo.setOrderTypeName("");
		if (vo.getUserId() > 0L) {

			if (vo.getOrderType() == 0) {
				vo.setOrderTypeName("钟点工");
			} else if (vo.getOrderType() == 1) {
				vo.setOrderTypeName("深度保洁");
			} else if (vo.getOrderType() == 2) {
				vo.setOrderTypeName("助理预约单");
			} else if (vo.getOrderType() == 3) {
				vo.setOrderTypeName("配送服务单");
			} else if (vo.getOrderType() == 4) {
				vo.setOrderTypeName("充值订单");
			} else if (vo.getOrderType() == 5) {
				vo.setOrderTypeName("提醒订单");
			} else {
				vo.setOrderTypeName("缴费单");
			}
		}
		// 服务日期转换
		Long serviceDate = order.getServiceDate();
		vo.setServiceDateStr(TimeStampUtil.timeStampToDateStr(serviceDate * 1000));
		// 优惠券描述
		UserCoupons userCoupons = userCouponsMapper.selectByOrderNo(order.getOrderNo());
		if (userCoupons != null) {
			DictCoupons dictCoupons = dictCouponsMapper.selectByPrimaryKey(userCoupons.getCouponId());
			if (dictCoupons != null) {
				vo.setIntroduction(dictCoupons.getIntroduction());
			}
		}
		// 订单状态
		String orderStatusName = OneCareUtil.getJhjOrderStausNameFromOrderType(order.getOrderType(), order.getOrderStatus());
		// String orderStatusName = getOrderStatusName(order.getOrderStatus());
		vo.setOrderStatusName(orderStatusName);

		return vo;
	}

	@Override
	public String getOrderStatusName(Short status) {

		String statusName = "";
		if (status.equals(Constants.ORDER_STATUS_0)) {
			statusName = "已取消";
		}

		if (status.equals(Constants.ORDER_STATUS_1)) {
			statusName = "待确认";
		}

		if (status.equals(Constants.ORDER_STATUS_2)) {
			statusName = "已确认";
		}

		if (status.equals(Constants.ORDER_STATUS_3)) {
			statusName = "待支付";
		}

		if (status.equals(Constants.ORDER_STATUS_4)) {
			statusName = "已支付";
		}

		if (status.equals(Constants.ORDER_STATUS_5)) {
			statusName = "服务中";
		}

		if (status.equals(Constants.ORDER_STATUS_6)) {
			statusName = "待评价";
		}

		if (status.equals(Constants.ORDER_STATUS_7)) {
			statusName = "已评价";
		}

		if (status.equals(Constants.ORDER_STATUS_9)) {
			statusName = "已关闭";
		}
		return statusName;
	}

	@Override
	public PageInfo selectByListPage(OrderSearchVo orderSearchVo, int pageNo, int pageSize) {

		PageHelper.startPage(pageNo, pageSize);
		List<Orders> list = ordersMapper.selectByListPage(orderSearchVo);
		if (list != null && list.size() != 0) {
			List<OrderViewVo> orderViewList = this.getOrderViewList(list);

			for (int i = 0; i < list.size(); i++) {
				if (orderViewList.get(i) != null) {
					list.set(i, orderViewList.get(i));
				}
			}
		}
		PageInfo result = new PageInfo(list);
		return result;
	}

	// . 根据开始时间，接收时间，staff_id ，得出订单总金额
	@Override
	public BigDecimal getTotalOrderMoney(OrderQuerySearchVo vo) {

		BigDecimal orderMoney = ordersMapper.getTotalOrderMoney(vo);

		if (orderMoney == null) {
			BigDecimal a = new BigDecimal(0);
			return a;
		}
		orderMoney = MathBigDeciamlUtil.round(orderMoney, 2);
		return orderMoney;

	}

	// . 根据开始时间，接收时间，staff_id ，得出订单总收入金额
	@Override
	public BigDecimal getTotalOrderIncomeMoney(OrderQuerySearchVo vo) {

		//BigDecimal money = ordersMapper.getTotalOrderIncomeMoney(vo);
		//钟点工订单0订单总金额
		BigDecimal hourMoney = ordersMapper.getTotalOrderIncomeHourMoney(vo);
		//深度保洁1订单总金额
		BigDecimal cleanMoney = ordersMapper.getTotalOrderIncomeCleanMoney(vo);
		//助理订单2订单总金额
		BigDecimal staffMoney = ordersMapper.getTotalOrderIncomeStaffMoney(vo);
		//配送订单3订单总金额
		BigDecimal disMoney = ordersMapper.getTotalOrderIncomeRunMoney(vo);
		
		Long staffId = vo.getStaffId();
		
		OrgStaffs staffs = orgStaffsService.selectByPrimaryKey(staffId);
		
		Short level = staffs.getLevel();
		String settingLevel = "-level-"+level.toString();
		
		String hoursettingType = "hour-ratio" + settingLevel;
		String cleansettingType = "deep-ratio" + settingLevel;
		String amsettingType = "am-ratio" + settingLevel;
		String dissettingType = "dis-ratio" + settingLevel;
		
		JhjSetting hour = settingService.selectBySettingType(hoursettingType);
		JhjSetting clean = settingService.selectBySettingType(cleansettingType);
		JhjSetting am = settingService.selectBySettingType(amsettingType);
		JhjSetting dis = settingService.selectBySettingType(dissettingType);
		
		//钟点工提成
		BigDecimal hourRate = new BigDecimal(hour.getSettingValue());	
		//深度保洁提成
		BigDecimal cleanRate = new BigDecimal(clean.getSettingValue());	
		//助理提成
		BigDecimal amRate = new BigDecimal(am.getSettingValue());		
		//配送订单提成
		BigDecimal disRate = new BigDecimal(dis.getSettingValue());		
		
		if (hourMoney == null ) {
			hourMoney = new BigDecimal(0);
		}
		if (cleanMoney == null ) {
			cleanMoney = new BigDecimal(0);
		}
		if (staffMoney == null ) {
			staffMoney = new BigDecimal(0);
		}
		if (disMoney == null ) {
			disMoney = new BigDecimal(0);
		}
		//钟点工收入
		BigDecimal hourIncomingMoney = hourMoney.multiply(hourRate);
		//深度保洁收入
		BigDecimal cleanIncomingMoney = cleanMoney.multiply(cleanRate);
		//助理收入
		BigDecimal amIncomingMoney = staffMoney.multiply(amRate);
		//配送订单收入
		BigDecimal disIncomingMoney = disMoney.multiply(disRate);
		//订单总收入
		BigDecimal totalIncomingMoney =hourIncomingMoney.add(cleanIncomingMoney)
				.add(amIncomingMoney).add(disIncomingMoney);
		
		if (totalIncomingMoney == null ) {
			BigDecimal b = new BigDecimal(0);
			return b;
		}
		totalIncomingMoney = MathBigDeciamlUtil.round(totalIncomingMoney, 2);		
				
		return totalIncomingMoney;
	}

	// . 根据开始时间，接收时间，staff_id ，得出订单总数:
	@Override
	public Long getTotalOrderCount(OrderQuerySearchVo vo) {

		return ordersMapper.getTotalOrderCount(vo);
	}

	// 当月订单总数（order_status=7,8)
	@Override
	public Long getTotalOrderCountByMouth(OrderQuerySearchVo searchVo) {

		return ordersMapper.getTotalOrderCountByMouth(searchVo);
	}

	@Override
	public PageInfo selectByListVoPage(OrderSearchVo searchVo, int pageNo, int pageSize) {

		PageHelper.startPage(pageNo, pageSize);
		List<Orders> list = ordersMapper.selectByListPage(searchVo);
		PageInfo result = new PageInfo(list);

		return result;
	}

	@Override
	public List<OrderViewVo> getOrderViewList(List<Orders> list) {

		// 加载更多订单的信息
		List<Long> userIds = new ArrayList<Long>();
		List<Long> addrIds = new ArrayList<Long>();
		List<Long> orderIds = new ArrayList<Long>();
		List<String> orderNos = new ArrayList<String>();
		Orders item = null;
		for (int i = 0; i < list.size(); i++) {
			item = list.get(i);
			if (item.getAddrId() > 0L) {
				addrIds.add(item.getAddrId());
			}
			userIds.add(item.getUserId());
			orderIds.add(item.getId());
			orderNos.add(item.getOrderNo());
		}

		List<OrderPrices> orderPricesList = orderPricesService.selectByOrderIds(orderIds);

		List<Users> userList = usersService.selectByUserIds(userIds);

		List<UserAddrs> addrList = new ArrayList<UserAddrs>();
		if (addrIds.size() > 0) {
			addrList = userAddrsService.selectByIds(addrIds);
		}
		// 进行orderViewVo 结合了 orders , order_prices, order_dispatchs 三张表的元素
		List<OrderViewVo> result = new ArrayList<OrderViewVo>();
		Long orderId = 0L;
		Long addrId = 0L;
		Long userId = 0L;

		for (int i = 0; i < list.size(); i++) {
			item = list.get(i);
			orderId = item.getId();
			addrId = item.getAddrId();
			userId = item.getUserId();

			OrderViewVo vo = new OrderViewVo();
			BeanUtils.copyProperties(item, vo);

			// 查找订单金额信息.
			OrderPrices orderPrice = null;
			for (int k = 0; k < orderPricesList.size(); k++) {
				orderPrice = orderPricesList.get(k);
				if (orderPrice.getOrderId().equals(orderId)) {
					vo.setOrderMoney(orderPrice.getOrderMoney());
					vo.setOrderPay(orderPrice.getOrderPay());
					vo.setPayType(orderPrice.getPayType());
					break;
				}
			}

			// 城市名称
			vo.setCityName("");
			if (vo.getCityId() > 0L) {
				String cityName = dictService.getCityName(vo.getCityId());
				vo.setCityName(cityName);
			}

			// 服务类型名称
			vo.setServiceTypeName("");
			if (vo.getServiceType() > 0L) {
				String serviceTypeName = dictService.getServiceTypeName(vo.getServiceType());
				vo.setServiceTypeName(serviceTypeName);
			}

			// 用户姓名
			String name = "";
			Users u = null;
			for (int j = 0; j < userList.size(); j++) {
				u = userList.get(j);
				if (u.getId().equals(userId)) {
					name = u.getName();
					break;
				}
			}
			vo.setName("");

			// 用户地址
			String addrName = "";
			UserAddrs addr = null;
			for (int n = 0; n < addrList.size(); n++) {
				addr = addrList.get(n);
				if (addr.getId().equals(addrId)) {
					addrName = addr.getName() + addr.getAddr();
					break;
				}
			}

			vo.setServiceAddr(addrName);

			result.add(vo);
		}

		return result;
	}

	@Override
	public UserListVo getUserList(Orders order) {
		// 加载更多订单的信息
		UserListVo vo = new UserListVo();
		BeanUtils.copyProperties(order, vo);

		// 订单价格信息
		OrderPrices orderPrice = orderPricesService.selectByOrderId(vo.getId());
		if (orderPrice != null) {
			vo.setPayType(orderPrice.getPayType());
			vo.setOrderMoney(orderPrice.getOrderMoney());
			vo.setOrderPay(orderPrice.getOrderPay());
		}

		// 城市名称
		vo.setCityName("");
		if (vo.getCityId() > 0L) {
			String cityName = dictService.getCityName(vo.getCityId());
			vo.setCityName(cityName);
		}

		// 服务类型名称
		vo.setServiceTypeName("");
		if (vo.getServiceType() > 0L) {
			String serviceTypeName = dictService.getServiceTypeName(vo.getServiceType());
			vo.setServiceTypeName(serviceTypeName);
		}

		// 用户称呼
		vo.setName("");
		if (vo.getUserId() > 0L) {
			Users user = usersService.getUserById(vo.getUserId());
			vo.setName(user.getName());
		}

		// 用户地址
		vo.setServiceAddr("");
		if (vo.getAddrId() > 0L) {
			UserAddrs userAddr = userAddrsService.selectByPrimaryKey(vo.getAddrId());
			vo.setServiceAddr(userAddr.getName() + userAddr.getAddr());
		}
		// 订单类型
		vo.setOrderTypeName("");
		if (vo.getUserId() > 0L) {

			if (vo.getOrderType() == 0) {
				vo.setOrderTypeName("钟点工预约单");
			} else if (vo.getOrderType() == 1) {
				vo.setOrderTypeName("深度保洁预约单");
			} else {
				vo.setOrderTypeName("助理预约单");
			}
		}
		// 订单状态
		String orderStatusName = getOrderStatusName(order.getOrderStatus());
		vo.setOrderStatusName(orderStatusName);

		// 服务次数
		vo.setServiceTimes("0");
		// 1.获得用户列表
		List<Orders> list = ordersMapper.selectAmOrderList(order.getAmId());

		// 2.获得用户对应的ids
		List<Long> userIds = new ArrayList<Long>();
		for (Orders item : list) {
			userIds.add(item.getUserId());
		}
		// 3.获得ids的数量
		List<HashMap> counts = new ArrayList<HashMap>();
		if (!userIds.isEmpty()) {
			counts = ordersMapper.totalByUserIds(userIds);
		}

		for (HashMap serviceCounts : counts) {

			Long userId = Long.valueOf(serviceCounts.get("user_id").toString());
			if (userId.equals(vo.getUserId())) {
				vo.setServiceTimes(serviceCounts.get("total").toString());
			}
		}

		return vo;
	}

	@Override
	public OrderListVo getOrderListVo(Orders item) {

		OrderListVo vo = new OrderListVo();

		OrderDispatchs orderDispatchs = orderDispatchsService.selectByOrderId(item.getId());
		OrderPrices orderPrices = orderPricesService.selectByOrderId(item.getId());
		Users users = usersService.selectByUsersId(item.getUserId());

		BeanUtilsExp.copyPropertiesIgnoreNull(item, vo);
		vo.setServiceTypeId(item.getServiceType());
		vo.setOrderId(item.getId());
		vo.setStaffId(item.getAmId());
		vo.setMobile(users.getMobile());
		// 服务日期
		Long addTime = item.getServiceDate() * 1000;
		vo.setServiceDate(TimeStampUtil.timeStampToDateStr(addTime, "MM-dd HH:mm"));
		
		//如果为助理订单，则取addTime
		if (vo.getOrderType().equals((short)2)) {
			vo.setServiceDate(TimeStampUtil.timeStampToDateStr(item.getAddTime() * 1000, "MM-dd HH:mm"));
		}
		
		// 服务类型名称
		vo.setServiceTypeName("");
		if (item.getServiceType() > 0L) {
			String serviceTypeName = dictService.getServiceTypeName(item.getServiceType());
			vo.setServiceTypeName(serviceTypeName);
		}
		// 订单类型
		vo.setOrderTypeName(OrderUtils.getOrderTypeName(item.getOrderType()));
		// 订单支付名称
		vo.setPayTypeName("");
		if (orderPrices != null) {
			vo.setPayTypeName(OrderUtils.getPayTypeName(item.getOrderStatus(), orderPrices.getPayType()));
		}
		// 订单价格信息
		OrderPrices orderPrice = orderPricesService.selectByOrderId(item.getId());
		vo.setOrderIncoming(new BigDecimal(0));
		vo.setOrderMoney(new BigDecimal(0));
		if (orderPrice != null) {
			vo.setOrderMoney(orderPrice.getOrderMoney());
			// 总金额C * 85% = 结果.
			if (vo.getOrderType() == 0) {
				// 助理收入比例 hour-ratio
				String settingType = "hour-ratio";
				JhjSetting jhjSetting = settingService.selectBySettingType(settingType);
				if (jhjSetting != null) {
					BigDecimal settingValue = new BigDecimal(jhjSetting.getSettingValue());
					BigDecimal orderIncoming = orderPrice.getOrderMoney().multiply(settingValue);
					orderIncoming = MathBigDeciamlUtil.round(orderIncoming, 2);
					vo.setOrderIncoming(orderIncoming);
				}
			}
			
			
			if (vo.getOrderType() == 2) {
				// 助理收入比例 am-ratio
				String settingType = "am-ratio";
				JhjSetting jhjSetting = settingService.selectBySettingType(settingType);
				if (jhjSetting != null) {
					BigDecimal settingValue = new BigDecimal(jhjSetting.getSettingValue());
					BigDecimal orderIncoming = orderPrice.getOrderMoney().multiply(settingValue);
					orderIncoming = MathBigDeciamlUtil.round(orderIncoming, 2);
					vo.setOrderIncoming(orderIncoming);
				}
			}
			if (vo.getOrderType() == 3) {
				// 配送服务收入比例 dispatch-ratio
				String settingType = "dis-ratio";
				JhjSetting jhjSetting = settingService.selectBySettingType(settingType);
				if (jhjSetting != null) {
					BigDecimal settingValue = new BigDecimal(jhjSetting.getSettingValue());
					BigDecimal orderIncoming = orderPrice.getOrderMoney().multiply(settingValue);
					orderIncoming = MathBigDeciamlUtil.round(orderIncoming, 2);
					vo.setOrderIncoming(orderIncoming);
				}
			}
		}

		// button_word
		vo.setButtonWord(OrderUtils.getButtonWordName(item.getOrderType(), item.getOrderStatus()));
		
		// 订单状态名称
		vo.setOrderStatusStr(OrderUtils.getOrderStatusName(item.getOrderType(), item.getOrderStatus()));
		
		// 服务地址
		vo.setServiceAddr("");
		// 服务地址经度
		vo.setServiceAddrLat("");
		// 服务地址经度
		vo.setServiceAddrLng("");
		if (item.getAddrId() > 0L) {
			UserAddrs userAddr = userAddrsService.selectByPrimaryKey(item.getAddrId());
			vo.setServiceAddr(userAddr.getName() + userAddr.getAddr());
			vo.setServiceAddrLat(userAddr.getLatitude());
			vo.setServiceAddrLng(userAddr.getLongitude());
		}
		// 取货地址
		vo.setPickAddr("");
		// 取货地址经度
		vo.setPickAddrLat("");
		// 取货地址纬度
		vo.setPickAddrLng("");

		// 距离服务地址多少米/千米
		if (orderDispatchs != null) {
			
			int userAddrDisance = orderDispatchs.getUserAddrDistance();
			if (userAddrDisance < 0) userAddrDisance = 0;
			
			vo.setServiceAddrDistance(String.valueOf(userAddrDisance) + "米");
			if (orderDispatchs.getUserAddrDistance() > 1000) {
				Double userAddrDisanceM = StringUtil.getKilometre(userAddrDisance);
				vo.setServiceAddrDistance(userAddrDisanceM.toString() + "千米");
			}
			// 取货地址
			vo.setPickAddr("");
			if (orderDispatchs.getPickAddr() != null) {
				vo.setPickAddr(orderDispatchs.getPickAddr());
			}

			// 距离多少米/千米
			int pickDistance = orderDispatchs.getPickDistance();
			if (pickDistance < 0) pickDistance = 0;
			vo.setPickAddrDistance(String.valueOf(pickDistance) + "米");
			if (orderDispatchs.getPickDistance() > 1000) {
				Double pickDistanceM = StringUtil.getKilometre(pickDistance);
				vo.setPickAddrDistance(pickDistanceM.toString() + "千米");
			}
			// 取货地址经度
			vo.setPickAddrLat("");
			if (orderDispatchs.getPickAddrLat() != null) {
				vo.setPickAddrLat(orderDispatchs.getPickAddrLat());
			}

			// 取货地址纬度
			vo.setPickAddrLng("");
			if (orderDispatchs.getPickAddrLng() != null) {
				vo.setPickAddrLng(orderDispatchs.getPickAddrLng());
			}
			
			//如果为助理订单，则把pickAddr 也赋值到servcieAddr
			if (vo.getOrderType().equals((short)2)) {
				vo.setServiceAddr(orderDispatchs.getPickAddrName() + orderDispatchs.getPickAddr());
				vo.setServiceAddrLat(orderDispatchs.getPickAddrLat());
				vo.setServiceAddrLng(orderDispatchs.getPickAddrLng());
			}
		}

		return vo;
	}
	
	@Override
	public OrderDetailVo getOrderDetailVo(Orders item) {
		
		OrderDetailVo result = new OrderDetailVo();
		OrderListVo vo = this.getOrderListVo(item);
		
		BeanUtilsExp.copyPropertiesIgnoreNull(vo, result);
		
		//计算订单的收入比例
		result.setOrderRatio("");
		String settingType = "";
		if (vo.getOrderType() == 0) {
			// 钟点功能收入比例 hour-ratio
			settingType = "hour-ratio";
		}
		if (vo.getOrderType() == 2) {
			// 配送服务收入比例 am-ratio
			settingType = "am-ratio";
		}
		
		if (vo.getOrderType() == 3) {
			// 配送服务收入比例 am-ratio
			settingType = "dis-ratio";
		}
		
		JhjSetting jhjSetting = settingService.selectBySettingType(settingType);
		if (jhjSetting != null) {
			result.setOrderRatio(jhjSetting.getSettingValue());
		}
		
		//得出服务人员的客服电话
		
		jhjSetting = settingService.selectBySettingType("tell-staff");
		if (jhjSetting != null) {
			result.setTelStaff(jhjSetting.getSettingValue());
		}
		
		Long staffId = vo.getStaffId();
		Long orgId = 0L;
		OrgStaffs orgStaffs = orgStaffsService.selectByPrimaryKey(staffId);
		
		if (orgStaffs != null) {
			orgId = orgStaffs.getOrgId();
		}
		
		if (orgId != null && orgId > 0L) {
			Orgs org = orgsService.selectByPrimaryKey(orgId);
			if (org != null) {
				result.setTelStaff(org.getOrgTel());
			}
		}
		
		
		return result;
	}
}
