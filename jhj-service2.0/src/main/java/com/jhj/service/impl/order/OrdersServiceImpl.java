package com.jhj.service.impl.order;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jhj.common.Constants;
import com.jhj.po.dao.bs.OrgStaffsMapper;
import com.jhj.po.dao.order.OrderDispatchsMapper;
import com.jhj.po.dao.order.OrderLogMapper;
import com.jhj.po.dao.order.OrderPricesMapper;
import com.jhj.po.dao.order.OrdersMapper;
import com.jhj.po.dao.user.UserAddrsMapper;
import com.jhj.po.dao.user.UsersMapper;
import com.jhj.po.model.bs.OrgStaffs;
import com.jhj.po.model.order.OrderLog;
import com.jhj.po.model.order.OrderPrices;
import com.jhj.po.model.order.Orders;
import com.jhj.po.model.user.UserAddrs;
import com.jhj.service.order.OrderLogService;
import com.jhj.service.order.OrderPricesService;
import com.jhj.service.order.OrdersService;
import com.jhj.vo.OrderSearchVo;
import com.jhj.vo.order.OrderViewVo;
import com.meijia.utils.RandomUtil;
import com.meijia.utils.SmsUtil;
import com.meijia.utils.TimeStampUtil;

@Service
public class OrdersServiceImpl implements OrdersService {

	@Autowired
	private OrdersMapper ordersMapper;
	@Autowired
	private OrderPricesMapper orderPricesMapper;

	@Autowired
	private OrderLogMapper orderLogMapper;

	@Autowired
	private OrderLogService orderLogService;

	@Autowired
	private OrderPricesService orderPricesService;

	@Autowired
	private OrgStaffsMapper orgStaffsMapper;

	@Autowired
	private UserAddrsMapper userAddrsMapper;
	
	@Autowired
	private OrderDispatchsMapper orderDispatchsMapper;

	@Autowired
	private UsersMapper usersMapper;
	
	@Override
	public int deleteByPrimaryKey(Long id) {
		return ordersMapper.deleteByPrimaryKey(id);
	}

	@Override
	public Orders initOrders() {

		Orders record = new Orders();
		record.setId(0L);
		record.setAmId(0L);
		record.setMobile("");
		record.setUserId(0L);
		record.setCityId(0L);
		record.setOrgId(1L);
		record.setAddrId(0L);
		record.setOrderType((short) 0);// 0 = 钟点工 1 = 深度保洁 2 = 助理预约单
		record.setServiceType(0L);
		record.setServiceContent("");
		record.setServiceDate(0L);
		record.setServiceHour((short) 0);
		record.setOrderNo("");
		record.setOrderRate((short) 0);// 0 = 好 1 = 一般 2 = 差
		record.setOrderRateContent("");
		record.setOrderStatus(Constants.ORDER_HOUR_STATUS_1);
		record.setOrderFrom(Constants.USER_NET);
		record.setRemarks("");
		record.setRemarksConfirm("");
		
		record.setAddTime(TimeStampUtil.getNowSecond());
		record.setUpdateTime(TimeStampUtil.getNowSecond());
		return record;
	}

	@Override
	public Long insert(Orders record) {
		return ordersMapper.insert(record);
	}

	@Override
	public int insertSelective(Orders record) {
		return ordersMapper.insertSelective(record);
	}

	@Override
	public int updateByPrimaryKeySelective(Orders record) {
		return ordersMapper.updateByPrimaryKeySelective(record);
	}

	@Override
	public Orders selectByUserId(Long userId) {

		return ordersMapper.selectByUserId(userId);
	}

	@Override
	public List<Orders> selectByUserIdList(Long userId) {

		return ordersMapper.selectByUserIdList(userId);
	}

	@Override
	public OrderViewVo changeOrderViewVo(Orders orders) {

		OrderViewVo orderViewVo = new OrderViewVo();

		BeanUtils.copyProperties(orders, orderViewVo);

		if (orderViewVo.getId() > 0L) {

			OrderPrices orderPrices = orderPricesService
					.selectByOrderId(orderViewVo.getId());
			BigDecimal orderMoney = orderPrices.getOrderMoney();
			orderViewVo.setOrderMoney(orderMoney);
		}

		return orderViewVo;

	}

	@Override
	public Boolean orderAmSuccessTodo(String orderNo) {

		Orders order = ordersMapper.selectByOrderNo(orderNo);

		if (order == null) {
			return false;
		}

		// 记录订单日志.
		OrderLog orderLog = orderLogService.initOrderLog(order);
		orderLogService.insert(orderLog);

		
		orderAmPushSms(order);
		return true;
	}



	@Override
	public Boolean orderExpCleanSuccessTodo(String orderNo) {
		Orders order = ordersMapper.selectByOrderNo(orderNo);

		if (order == null) {
			return false;
		}
		// 记录订单日志.
		OrderLog orderLog = orderLogService.initOrderLog(order);
		orderLogService.insert(orderLog);
		// 深度保洁订单短信推送
		orderExpCleanPushSms(order);
		return true;
	}
	@Override
	public Boolean userOrderAmSuccessTodo(String orderNo){
		
		Orders order = ordersMapper.selectByOrderNo(orderNo);
		
		if (order==null) {
			return false;
		}
		// 记录订单日志.
				OrderLog orderLog = orderLogService.initOrderLog(order);
				orderLogService.insert(orderLog);
		//助理订单短信推送
		userOrderAmPushSms(order);
		return true;
	}
	private boolean userOrderAmPushSms(Orders order) {

		Short orderStatus = order.getOrderStatus();

		Long userId = order.getUserId();
		Long amId = order.getAmId();
		
		/*OrgStaffs orgStaffs = orgStaffsMapper.selectByPrimaryKey(amId);
		// 助理收到待确认的助理订单
		if (orderStatus.equals(Constants.ORDER_STATUS_2)) {
			String mobile = orgStaffs.getMobile();

			String code = RandomUtil.randomNumber();
			if (mobile.equals("18610807136")) {
				code = "000000";
			}
			String mobileContent = order.getMobile();
			String [] content = new String [] {mobileContent};
 			String serviceContent =order.getServiceContent(); 
			String[] content = new String[] {"服务内容:"+ serviceContent };
			
			HashMap<String, String> sendSmsResult = SmsUtil.SendSms(mobile,
					Constants.GET_USER_ORDER_AM_ID, content);
		}*/
		// 用户收到未支付的助理订单通知
		OrderPrices orderPrices = orderPricesService.selectByOrderId(order.getId());
		if (orderStatus.equals(Constants.ORDER_STATUS_3)) {
			String mobile = order.getMobile();
			String code = RandomUtil.randomNumber();
			if (mobile.equals("18610807136")) {
				code = "000000";
			}
			String orderMoney = orderPrices.getOrderMoney().toString();
		
			String[] content = new String[] { code , orderMoney};
			HashMap<String, String> sendSmsResult = SmsUtil.SendSms(mobile,
					Constants.AM_NOTICE_CUSTOMER_Message, content);
		}
		return true;
	}
	private boolean orderExpCleanPushSms(Orders order) {

		Short orderStatus = order.getOrderStatus();

		Long userId = order.getUserId();
		Long amId = order.getAmId();
		OrgStaffs orgStaffs = orgStaffsMapper.selectByPrimaryKey(amId);
		// 助理收到待确认的深度保洁订单通知
		if (orderStatus.equals(Constants.ORDER_STATUS_1)) {
			String mobile = orgStaffs.getMobile();

			String code = RandomUtil.randomNumber();
			if (mobile.equals("18610807136")) {
				code = "000000";
			}
			UserAddrs userAddrs = userAddrsMapper.selectByUserIdAndAddrId(
					order.getAddrId(), order.getUserId());
			String addrs = userAddrs.getName() + userAddrs.getAddress()
					+ userAddrs.getAddr();
			Long serviceDateLong = order.getServiceDate();
			String serviceDateStr = TimeStampUtil
					.timeStampToDateStr(serviceDateLong*1000);
			String[] content = new String[] { "服务时间：" + serviceDateStr
					+ "服务地点：" + addrs };
			HashMap<String, String> sendSmsResult = SmsUtil.SendSms(mobile,
					Constants.GET_AM_EXP_CLEAN_ORDER_ID, content);
		}
		// 用户收到未支付的深度保洁订单通知
		if (orderStatus.equals(Constants.ORDER_STATUS_2)) {
			String mobile = order.getMobile();
			String code = RandomUtil.randomNumber();
			if (mobile.equals("18610807136")) {
				code = "000000";
			}
			String[] content = new String[] { code,
					Constants.GET_CODE_MAX_VALID };
			HashMap<String, String> sendSmsResult = SmsUtil.SendSms(mobile,
					Constants.GET_USER_NO_PAY_ORDER_ID, content);
		}
		return true;
	}

	private boolean orderAmPushSms(Orders order) {

		Short orderStatus = order.getOrderStatus();

		Long userId = order.getUserId();
		Long amId = order.getAmId();
		OrgStaffs orgStaffs = orgStaffsMapper.selectByPrimaryKey(amId);
		// 发短信给助理
		if (orderStatus.equals(Constants.ORDER_STATUS_1)) {
			String mobile = orgStaffs.getMobile();

			String code = RandomUtil.randomNumber();
			if (mobile.equals("18610807136")) {
				code = "000000";
			}
			String[] content = new String[] { code,
					Constants.GET_CODE_MAX_VALID };
			HashMap<String, String> sendSmsResult = SmsUtil.SendSms(mobile,
					Constants.GET_CODE_TEMPLE_ID, content);
		}
		// 发短信给用户
		if (orderStatus.equals(Constants.ORDER_STATUS_2)) {
			String mobile = order.getMobile();
			String code = RandomUtil.randomNumber();
			if (mobile.equals("18610807136")) {
				code = "000000";
			}
			String[] content = new String[] { code,
					Constants.GET_CODE_MAX_VALID };
			HashMap<String, String> sendSmsResult = SmsUtil.SendSms(mobile,
					Constants.GET_CODE_TEMPLE_ID, content);
		}

		return true;
	}

	@Override
	public Orders selectByPrimaryKey(Long id) {

		return ordersMapper.selectByPrimaryKey(id);
	}

	@Override
	public int updateUpdateTime(Orders orders) {

		return ordersMapper.updateByUpdateTimeSelective(orders);
	}

	@Override
	public int insert(OrderLog orderLog) {

		return orderLogMapper.insert(orderLog);
	}

	@Override
	public List<Orders> getAmOrderList(Long amId) {
		return ordersMapper.selectAmOrderList(amId);
	}

	@Override
	public Orders selectByOrderNo(String orderNo) {
		return ordersMapper.selectByOrderNo(orderNo);
	}

	@Override
	public List<Orders> selectOrderListByAmId(Long amId, int pageNo,
			int pageSize) {
		PageHelper.startPage(pageNo, pageSize);
		OrderSearchVo orderSearchVo = new OrderSearchVo();
		orderSearchVo.setAmId(amId);
		List<Orders> list = ordersMapper.selectByListPage(orderSearchVo);
		return list;
	}

	@Override
	public List<Orders> selectListByAmId(Long amId) {

		List<Orders> list = ordersMapper.selectAmOrderList(amId);
		return list;
	}

	@Override
	public List<HashMap> totalByUserIds(List<Long> id) {
		return ordersMapper.totalByUserIds(id);
	}

	@Override
	public PageInfo searchVoListPage(int pageNo, int pageSize) {
		PageHelper.startPage(pageNo, pageSize);
		List<Orders> list = ordersMapper.selectByListPages();
		PageInfo result = new PageInfo(list);
		return result;
	}

	@Override
	public int updateCleanUpdateTime(Orders orders) {

		return ordersMapper.updateByCleanUpdateTimeSelective(orders);
	}

	@Override
	public List<Orders> selectAmId(Long amId) {

		List<Orders> list = ordersMapper.selectAmOrderList(amId);
		return list;
	}

	@Override
	public Orders selectByAmId(Long amId) {

		return ordersMapper.selectByAmId(amId);
	}
	

	@Override
	public int getIntimacyOrders(Map<String, Long> map) {
		return ordersMapper.getIntimacyOrders(map);
	}

	@Override
	public List<Orders> selectByAmIdGroupByUserId(Long amId) {

		return ordersMapper.selectByAmIdGroupByUserId(amId);
	}

	@Override
	public Orders selectbyOrderId(Long orderId) {

		return ordersMapper.selectByPrimaryKey(orderId);
	}

	@Override
	public List<Orders> selectByOrderStatus() {
	
		return ordersMapper.selectByOrderStatus();
	}

	@Override
	public Boolean userOrderPostBeginSuccessTodo(Orders orders) {
		
		String mobile = orders.getMobile();
		String code = RandomUtil.randomNumber();
		if (mobile.equals("18610807136")) {
			code = "000000";
		}
		String[] content = new String[] { code,
				Constants.GET_CODE_MAX_VALID };
		HashMap<String, String> sendSmsResult = SmsUtil.SendSms(mobile,
				Constants.STAFF_POST_BEGIN, content);
		return true;
	}
	@Override
	public Boolean userOrderPostDoneSuccessTodo(Orders orders) {
		
		String mobile = orders.getMobile();
		String code = RandomUtil.randomNumber();
		if (mobile.equals("18610807136")) {
			code = "000000";
		}
		String[] content = new String[] { code,
				Constants.GET_CODE_MAX_VALID };
		HashMap<String, String> sendSmsResult = SmsUtil.SendSms(mobile,
				Constants.STAFF_POST_DONE, content);
		return true;
	}

	@Override
	public Boolean userJoinBlackSuccessTodo(String mobile) {

		String code = RandomUtil.randomNumber();
		if (mobile.equals("18610807136")) {
			code = "000000";
		}
		String[] content = new String[] { code,
				Constants.GET_CODE_MAX_VALID };
		HashMap<String, String> sendSmsResult = SmsUtil.SendSms(mobile,
				Constants.STAFF_JOIN_BLACK, content);
		return true;
	}

	
}