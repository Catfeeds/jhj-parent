package com.jhj.service.impl.order;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jhj.po.dao.order.OrderDispatchPricesMapper;
import com.jhj.po.model.bs.OrgStaffs;
import com.jhj.po.model.order.OrderDispatchPrices;
import com.jhj.po.model.order.OrderDispatchs;
import com.jhj.po.model.order.OrderPrices;
import com.jhj.po.model.order.Orders;
import com.jhj.service.bs.OrgStaffFinanceService;
import com.jhj.service.bs.OrgStaffLeaveService;
import com.jhj.service.bs.OrgStaffSkillService;
import com.jhj.service.bs.OrgStaffsService;
import com.jhj.service.bs.OrgsService;
import com.jhj.service.newDispatch.NewDispatchStaffService;
import com.jhj.service.order.OrderDispatchPriceService;
import com.jhj.service.order.OrderPricesService;
import com.jhj.service.order.OrdersService;
import com.jhj.service.users.UserAddrsService;
import com.jhj.service.users.UserTrailRealService;
import com.jhj.service.users.UsersService;
import com.jhj.vo.order.OrderSearchVo;
import com.jhj.vo.staff.OrgStaffIncomingVo;
import com.meijia.utils.MathBigDecimalUtil;
import com.meijia.utils.TimeStampUtil;

@Service
public class OrderDispatchPriceServiceImpl implements OrderDispatchPriceService {

	@Autowired
	private OrderDispatchPricesMapper orderDispatchPriceMapper;

	@Autowired
	private OrgStaffFinanceService orgStaffFinanceService;
	
	@Autowired
	private UsersService userService;	

	@Autowired
	private UserAddrsService userAddrService;

	@Autowired
	private OrdersService orderService;

	@Autowired
	private OrderPricesService orderPriceService;
	
	@Autowired
	private OrgStaffSkillService orgStaffSkillService;

	@Autowired
	private OrgsService orgService;

	@Autowired
	private OrgStaffLeaveService orgStaffLeaveService;

	@Autowired
	private OrgStaffsService orgStaffService;

	@Autowired
	private UserTrailRealService trailRealService;
	
	@Autowired
	private NewDispatchStaffService newDisStaService;

	@Override
	public int deleteByPrimaryKey(Long id) {
		return orderDispatchPriceMapper.deleteByPrimaryKey(id);
	}

	@Override
	public int insert(OrderDispatchPrices record) {
		return orderDispatchPriceMapper.insert(record);
	}

	@Override
	public int insertSelective(OrderDispatchPrices record) {
		return orderDispatchPriceMapper.insertSelective(record);
	}

	@Override
	public OrderDispatchPrices selectByPrimaryKey(Long id) {
		return orderDispatchPriceMapper.selectByPrimaryKey(id);
	}

	@Override
	public int updateByPrimaryKeySelective(OrderDispatchPrices record) {
		return orderDispatchPriceMapper.updateByPrimaryKeySelective(record);
	}

	@Override
	public int updateByPrimaryKey(OrderDispatchPrices record) {
		return orderDispatchPriceMapper.updateByPrimaryKey(record);
	}

	@Override
	public OrderDispatchPrices initOrderDisp() {

		OrderDispatchPrices record = new OrderDispatchPrices();
		record.setId(0L);
		record.setUserId(0L);
		record.setMobile("");
		record.setIsVip((short) 0);
		record.setOrderId(0L);
		record.setOrderNo("");
		record.setOrderType((short) 0);
		record.setServiceTypeId(0L);
		record.setOrderStatus((short) 0);
		record.setAddrId(0L);
		record.setAddr("");
		record.setOrderTime(0L);
		record.setServiceDate(0L);
		record.setServiceHours((double) 0);
		record.setStaffNum((short) 0);
		record.setOrgId(0L);
		record.setParentId(0L);
		record.setStaffId(0L);
		record.setStaffName("");
		record.setStaffMobile("");
		record.setDispatchStatus((short) 0);
		record.setUserAddrDistance(0);
		record.setPayType((short) 0);
		record.setOrderMoney(new BigDecimal(0));
		record.setOrderPay(new BigDecimal(0));
		record.setOrderPayIncoming(new BigDecimal(0));
		record.setCouponId(0L);
		record.setOrderPayCoupon(new BigDecimal(0));
		record.setOrderPayCouponIncoming(new BigDecimal(0));
		record.setOrderPayExtDiff(new BigDecimal(0));
		record.setOrderPayExtDiffIncoming(new BigDecimal(0));
		record.setOrderPayExtOverwork(new BigDecimal(0));
		record.setOrderPayExtOverworkIncoming(new BigDecimal(0));
		record.setIncomingPercent(new BigDecimal(0));
		record.setTotalOrderIncoming(new BigDecimal(0));
		record.setTotalOrderDept(new BigDecimal(0));
		record.setAddTime(TimeStampUtil.getNowSecond());
		record.setUpdateTime(TimeStampUtil.getNowSecond());

		return record;
	}

	@Override
	public List<OrderDispatchPrices> selectBySearchVo(OrderSearchVo searchVo) {
		return orderDispatchPriceMapper.selectBySearchVo(searchVo);
	}
	
	@Override
	public PageInfo selectByListPage(OrderSearchVo searchVo, int pageNo,int pageSize) {
		PageHelper.startPage(pageNo, pageSize);
		List<OrderDispatchPrices> list = orderDispatchPriceMapper.selectByListPage(searchVo);
		PageInfo<OrderDispatchPrices> page =new PageInfo<OrderDispatchPrices>(list);
		return page;
	}
	
	@Override
	public boolean doOrderDispatchPrice(Orders order, OrderDispatchs orderDispatch) {
		Long orderId = order.getId();
		Long staffId = orderDispatch.getStaffId();
		
		//重复性判断
		OrderSearchVo searchVo = new OrderSearchVo();
		searchVo.setOrderId(orderId);
		searchVo.setStaffId(staffId);
		List<OrderDispatchPrices> list = this.selectBySearchVo(searchVo);
		if (!list.isEmpty()) return true;
		
		OrderPrices orderPrice = orderPriceService.selectByOrderId(orderId);
		
		OrgStaffs orgStaff = orgStaffService.selectByPrimaryKey(staffId);
		OrgStaffIncomingVo vo = orgStaffFinanceService.getStaffInComingDetail(orgStaff, order, orderDispatch);
		
		OrderDispatchPrices record = this.initOrderDisp();
		record.setUserId(order.getUserId());
		record.setMobile(order.getMobile());
		record.setIsVip(vo.getIsVip());
		record.setOrderId(orderId);
		record.setOrderNo(order.getOrderNo());
		record.setOrderType(order.getOrderType());
		record.setServiceTypeId(order.getServiceType());
		record.setOrderStatus(order.getOrderStatus());
		record.setAddrId(order.getAddrId());
		record.setAddr(vo.getAddr());
		record.setOrderTime(order.getAddTime());
		record.setServiceDate(order.getServiceDate());
		record.setServiceHours(order.getServiceHour());
		record.setStaffNum(order.getStaffNums());
		record.setOrgId(orgStaff.getOrgId());
		record.setParentId(orgStaff.getParentOrgId());
		record.setStaffId(staffId);
		record.setStaffName(orgStaff.getName());
		record.setStaffMobile(orgStaff.getMobile());
		record.setDispatchStatus(orderDispatch.getDispatchStatus());
		record.setUserAddrDistance(orderDispatch.getUserAddrDistance());
		record.setPayType(orderPrice.getPayType());
		record.setOrderMoney(vo.getOrderMoney());
		record.setOrderPay(vo.getTotalOrderPay());
		record.setOrderPayIncoming(vo.getOrderIncoming());
		record.setCouponId(orderPrice.getCouponId());
		record.setOrderPayCoupon(vo.getOrderPayCoupon());
		record.setOrderPayCouponIncoming(vo.getOrderPayCouponIncoming());
		record.setOrderPayExtDiff(vo.getOrderPayExtDiff());
		record.setOrderPayExtDiffIncoming(vo.getOrderPayExtDiffIncoming());
		record.setOrderPayExtOverwork(vo.getOrderPayExtOverWork());
		record.setOrderPayExtOverworkIncoming(vo.getOrderPayExtOverWorkIncoming());
		record.setIncomingPercent(vo.getIncomingPercent());
		record.setTotalOrderIncoming(vo.getTotalOrderIncoming());
		record.setTotalOrderDept(vo.getTotalOrderDept());
		record.setAddTime(order.getUpdateTime());
		record.setUpdateTime(order.getUpdateTime());
		
		this.insert(record);
		
		return true;
	}
	
	@Override
	public Map<String, String> getTotalOrderMoneyMultiStat(OrderSearchVo searchVo) {
		
		BigDecimal totalOrderMoney = new BigDecimal(0);
		BigDecimal totalOrderPay = new BigDecimal(0);
		BigDecimal totalOrderCoupon = new BigDecimal(0);
		BigDecimal totalOrderIncoming = new BigDecimal(0);
		BigDecimal totalOrderPayType0 = new BigDecimal(0);
		BigDecimal totalOrderPayType1 = new BigDecimal(0);
		BigDecimal totalOrderPayType2 = new BigDecimal(0);
		BigDecimal totalOrderPayType6 = new BigDecimal(0);
		BigDecimal totalOrderPayType7 = new BigDecimal(0);
		
		Map<String, String> statResult = new HashMap<String, String>();
		searchVo.setDispatchStatus((short) 1);
		Map<String, Object> stats =  orderDispatchPriceMapper.getTotalOrderMoneyMultiStat(searchVo);
		
		if (stats.get("totalOrderMoney") != null) {
			totalOrderMoney =  (BigDecimal) stats.get("totalOrderMoney");
		}
		
		if (stats.get("totalOrderPay") != null) {
			totalOrderPay =  (BigDecimal) stats.get("totalOrderPay");
		}
		
		if (stats.get("totalOrderCoupon") != null) {
			totalOrderCoupon =  (BigDecimal) stats.get("totalOrderCoupon");
		}
		
		if (stats.get("totalOrderIncoming") != null) {
			totalOrderIncoming =  (BigDecimal) stats.get("totalOrderIncoming");
		}
		
		if (stats.get("totalOrderPayType0") != null) {
			totalOrderPayType0 =  (BigDecimal) stats.get("totalOrderPayType0");
		}
		
		if (stats.get("totalOrderPayType1") != null) {
			totalOrderPayType1 =  (BigDecimal) stats.get("totalOrderPayType1");
		}
		
		if (stats.get("totalOrderPayType2") != null) {
			totalOrderPayType2 =  (BigDecimal) stats.get("totalOrderPayType2");
		}
		
		if (stats.get("totalOrderPayType6") != null) {
			totalOrderPayType6 =  (BigDecimal) stats.get("totalOrderPayType6");
		}
		
		if (stats.get("totalOrderPayType7") != null) {
			totalOrderPayType7 =  (BigDecimal) stats.get("totalOrderPayType7");
		}
		
		statResult.put("totalOrderMoney", MathBigDecimalUtil.round2(totalOrderMoney));
		statResult.put("totalOrderPay", MathBigDecimalUtil.round2(totalOrderPay));
		statResult.put("totalOrderCoupon", MathBigDecimalUtil.round2(totalOrderCoupon));
		statResult.put("totalOrderIncoming", MathBigDecimalUtil.round2(totalOrderIncoming));
		statResult.put("totalOrderPayType0", MathBigDecimalUtil.round2(totalOrderPayType0));
		statResult.put("totalOrderPayType1", MathBigDecimalUtil.round2(totalOrderPayType1));
		statResult.put("totalOrderPayType2", MathBigDecimalUtil.round2(totalOrderPayType2));
		statResult.put("totalOrderPayType6", MathBigDecimalUtil.round2(totalOrderPayType6));
		statResult.put("totalOrderPayType7", MathBigDecimalUtil.round2(totalOrderPayType7));		
		
		
			
		return statResult;
	}
	

}
