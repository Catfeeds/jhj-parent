package com.jhj.service.impl.bs;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jhj.common.Constants;
import com.jhj.po.dao.bs.OrgStaffDetailPayMapper;
import com.jhj.po.model.bs.OrgStaffDetailPay;
import com.jhj.po.model.bs.OrgStaffPayDept;
import com.jhj.po.model.bs.OrgStaffs;
import com.jhj.po.model.order.OrderDispatchPrices;
import com.jhj.po.model.order.OrderPriceExt;
import com.jhj.po.model.order.OrderPrices;
import com.jhj.po.model.order.Orders;
import com.jhj.po.model.university.PartnerServiceType;
import com.jhj.service.bs.OrgStaffDetailPayService;
import com.jhj.service.bs.OrgStaffPayDeptService;
import com.jhj.service.bs.OrgStaffsService;
import com.jhj.service.order.OrderDispatchPriceService;
import com.jhj.service.order.OrderPriceExtService;
import com.jhj.service.order.OrderPricesService;
import com.jhj.service.order.OrdersService;
import com.jhj.service.university.PartnerServiceTypeService;
import com.jhj.service.users.UserAddrsService;
import com.jhj.vo.order.OrderSearchVo;
import com.jhj.vo.staff.OrgStaffDetailPayOaVo;
import com.jhj.vo.staff.OrgStaffPayVo;
import com.meijia.utils.BeanUtilsExp;
import com.meijia.utils.OneCareUtil;
import com.meijia.utils.TimeStampUtil;

/**
 *
 * @author :hulj
 * @Date : 2015年7月6日下午2:44:25
 * @Description:
 *
 */
@Service
public class OrgStaffDetailPayServiceImpl implements OrgStaffDetailPayService {

	@Autowired
	private OrgStaffDetailPayMapper orgStaffDetailPayMapper;
	
	@Autowired
	private OrgStaffsService orgStaffsService;
	
	@Autowired
	private OrdersService orderService;
	
	@Autowired
	private OrderPricesService orderPriceService;
	
	@Autowired
	private OrderPriceExtService orderPriceExtService;
	
	@Autowired
	private UserAddrsService userAddrsService;
	
	@Autowired
	private PartnerServiceTypeService partnerService;
	
	@Autowired
	private OrderDispatchPriceService orderDispatchPriceService;
	
	@Autowired
	private OrgStaffPayDeptService orgStaffPayDeptService;

	@Override
	public int deleteByPrimaryKey(Long id) {

		return orgStaffDetailPayMapper.deleteByPrimaryKey(id);
	}

	@Override
	public int insert(OrgStaffDetailPay record) {

		return orgStaffDetailPayMapper.insert(record);
	}

	@Override
	public int insertSelective(OrgStaffDetailPay record) {

		return orgStaffDetailPayMapper.insertSelective(record);
	}

	@Override
	public OrgStaffDetailPay selectByPrimaryKey(Long id) {

		return orgStaffDetailPayMapper.selectByPrimaryKey(id);
	}

	@Override
	public int updateByPrimaryKeySelective(OrgStaffDetailPay record) {

		return orgStaffDetailPayMapper.updateByPrimaryKeySelective(record);
	}

	@Override
	public int updateByPrimaryKey(OrgStaffDetailPay record) {

		return orgStaffDetailPayMapper.updateByPrimaryKey(record);
	}

	@Override
	public OrgStaffDetailPay initStaffDetailPay() {

		OrgStaffDetailPay record = new OrgStaffDetailPay();

		record.setId(0L);
		record.setStaffId(0L);
		record.setMobile("");
		record.setOrderType(Constants.ORDER_TYPE_0);
		record.setOrderId(0L);
		record.setOrderNo("");
		record.setRemarks("");
		record.setOrderMoney(new BigDecimal(0));
		record.setOrderPay(new BigDecimal(0));
		record.setOrderStatusStr("");
		record.setAddTime(TimeStampUtil.getNowSecond());

		return record;
	}

	@Override
	public OrgStaffPayVo getOrgStaffPayVo(OrgStaffDetailPay orgStaffDetailPay) {
		OrgStaffPayVo vo = new OrgStaffPayVo();
		vo.setStaffId(orgStaffDetailPay.getStaffId());
		// 用户手机号
		vo.setMobile(orgStaffDetailPay.getMobile());
		// 发生时间
		Long addTime = orgStaffDetailPay.getAddTime() * 1000;
		vo.setAddTimeStr(TimeStampUtil.timeStampToDateStr(addTime, "MM-dd HH:mm"));

		String orderTypeName = OneCareUtil.getOrderTypeForStaffDetailPay(orgStaffDetailPay.getOrderType());
		vo.setOrderTypeName(orderTypeName);
		if (orgStaffDetailPay.getOrderType() == Constants.STAFF_DETAIL_ORDER_TYPE_0 
				|| orgStaffDetailPay.getOrderType() == Constants.STAFF_DETAIL_ORDER_TYPE_1
				|| orgStaffDetailPay.getOrderType() == Constants.STAFF_DETAIL_ORDER_TYPE_2
				|| orgStaffDetailPay.getOrderType() == Constants.STAFF_DETAIL_ORDER_TYPE_3) {
			// +号
			Long orderId = orgStaffDetailPay.getOrderId();
			Orders order = orderService.selectByPrimaryKey(orderId);
			
			Long serviceType = order.getServiceType();
			PartnerServiceType type = partnerService.selectByPrimaryKey(serviceType);
			// 订单类型 具体名称 （具体到服务类型）, 如 钟点工-->金牌保洁初体验、金牌保洁深度体验。。。
			if (type != null) {
				vo.setOrderTypeName(type.getName());
			} else {
				vo.setOrderTypeName("");
			}			
		}

		vo.setOrderPay("+" + orgStaffDetailPay.getOrderPay());
		// 订单金额
		if (orgStaffDetailPay.getOrderType() == Constants.STAFF_DETAIL_ORDER_TYPE_0 
				|| orgStaffDetailPay.getOrderType() == Constants.STAFF_DETAIL_ORDER_TYPE_1
				|| orgStaffDetailPay.getOrderType() == Constants.STAFF_DETAIL_ORDER_TYPE_2
				|| orgStaffDetailPay.getOrderType() == Constants.STAFF_DETAIL_ORDER_TYPE_3
				|| orgStaffDetailPay.getOrderType() == Constants.STAFF_DETAIL_ORDER_TYPE_5) {
			// +号
			vo.setOrderPay("+" + orgStaffDetailPay.getOrderPay());
		}
		if (orgStaffDetailPay.getOrderType() == Constants.STAFF_DETAIL_ORDER_TYPE_4) {
			// -号
			vo.setOrderPay("-" + orgStaffDetailPay.getOrderPay());
		}

		return vo;
	}

	@Override
	public OrgStaffDetailPayOaVo getOrgStaffPayOaVo(OrgStaffDetailPay orgStaffDetailPay) {

		OrgStaffPayVo vo = this.getOrgStaffPayVo(orgStaffDetailPay);
		
		OrgStaffDetailPayOaVo oaVo = new OrgStaffDetailPayOaVo();

		BeanUtilsExp.copyPropertiesIgnoreNull(orgStaffDetailPay, oaVo);
		
		OrgStaffs orgStaffs = orgStaffsService.selectByPrimaryKey(orgStaffDetailPay.getStaffId());

		oaVo.setName(orgStaffs.getName());
		oaVo.setOrderTypeName(vo.getOrderTypeName());
		oaVo.setOrderPay(new BigDecimal(vo.getOrderPay()));
		
		oaVo.setUserMobile("");
		oaVo.setAddr("");
		
		if (orgStaffDetailPay.getOrderType() == Constants.STAFF_DETAIL_ORDER_TYPE_0 
				|| orgStaffDetailPay.getOrderType() == Constants.STAFF_DETAIL_ORDER_TYPE_1
				|| orgStaffDetailPay.getOrderType() == Constants.STAFF_DETAIL_ORDER_TYPE_2
				|| orgStaffDetailPay.getOrderType() == Constants.STAFF_DETAIL_ORDER_TYPE_3) {
			// +号
			Long orderId = orgStaffDetailPay.getOrderId();
			Orders order = orderService.selectByPrimaryKey(orderId);
			oaVo.setUserMobile(order.getMobile());
			oaVo.setValidateCodeName("否");
			if(order.getValidateCode()==1){
				oaVo.setValidateCodeName("是");
			}
			
			OrderSearchVo searchVo = new OrderSearchVo();
			searchVo.setOrderId(orderId);
			searchVo.setOrderNo(order.getOrderNo());
			List<OrderDispatchPrices> orderDispatchPricesList = orderDispatchPriceService.selectBySearchVo(searchVo);
			if(orderDispatchPricesList!=null && orderDispatchPricesList.size()>0){
				OrderDispatchPrices orderDispatchPrices = orderDispatchPricesList.get(0);
				oaVo.setTotalOrderPay(orderDispatchPrices.getTotalOrderPay());
				oaVo.setTotalStaffIncoming(orderDispatchPrices.getTotalOrderIncoming());
				oaVo.setAddr(orderDispatchPrices.getAddr());
			}else{
				oaVo.setTotalOrderPay(new BigDecimal(0));
				oaVo.setTotalStaffIncoming(new BigDecimal(0));
			}
		}
		
		//加时和补差价
		BigDecimal overtimeCompensation = new BigDecimal(0);
		if(orgStaffDetailPay.getOrderType() == Constants.STAFF_DETAIL_ORDER_TYPE_0){
			OrderSearchVo searchVo = new OrderSearchVo();
			searchVo.setOrderNo(orgStaffDetailPay.getOrderNo());
			searchVo.setOrderStatus((short)2);
			List<OrderPriceExt> orderPriceExtList = orderPriceExtService.selectBySearchVo(searchVo);
			if(orderPriceExtList!=null && orderPriceExtList.size()>0){
				for(int i=0;i<orderPriceExtList.size();i++){
					OrderPriceExt orderPriceExt = orderPriceExtList.get(i);
					overtimeCompensation = overtimeCompensation.add(orderPriceExt.getOrderPay());
				}
			}
			oaVo.setOvertimeCompensation(overtimeCompensation);
		}

		OrderPrices orderPrices = orderPriceService.selectByOrderNo(orgStaffDetailPay.getOrderNo());
		
		oaVo.setPayTypeName("");
		if (orderPrices != null && orderPrices.getPayType() != null) {
			oaVo.setPayTypeName(OneCareUtil.getPayTypeName(orderPrices.getPayType()));
		}
		
		if (oaVo.getOrderType().equals(Constants.STAFF_DETAIL_ORDER_TYPE_1) ||
				oaVo.getOrderType().equals(Constants.STAFF_DETAIL_ORDER_TYPE_2)) {
			OrderPriceExt orderPriceExt = orderPriceExtService.selectByOrderNoExt(oaVo.getOrderNo());
			
			if (orderPriceExt != null) {
				oaVo.setPayTypeName(OneCareUtil.getPayTypeName(orderPriceExt.getPayType()));
			}
		}
		
		//跳转链接
		String orderListLink = "";
		if (oaVo.getOrderType().equals(Constants.STAFF_DETAIL_ORDER_TYPE_0) ||
			oaVo.getOrderType().equals(Constants.STAFF_DETAIL_ORDER_TYPE_1) ||
			oaVo.getOrderType().equals(Constants.STAFF_DETAIL_ORDER_TYPE_2) ||
			oaVo.getOrderType().equals(Constants.STAFF_DETAIL_ORDER_TYPE_3) ||
			oaVo.getOrderType().equals(Constants.STAFF_DETAIL_ORDER_TYPE_20) ||
			oaVo.getOrderType().equals(Constants.STAFF_DETAIL_ORDER_TYPE_21) || 
			oaVo.getOrderType().equals(Constants.STAFF_DETAIL_ORDER_TYPE_21) ||
			oaVo.getOrderType().equals(Constants.STAFF_DETAIL_ORDER_TYPE_22) ) {
			
			Long orderId = orgStaffDetailPay.getOrderId();
			Orders order = orderService.selectByPrimaryKey(orderId);
			if (order != null) {
				orderListLink = "/jhj-oa/order/order-list?orderNo="+order.getOrderNo();
				
				oaVo.setServiceFinishTime(order.getOrderDoneTime());
			}
			
		}
		
		oaVo.setOrderListLink(orderListLink);
		return oaVo;
	}

	@Override
	public PageInfo<OrgStaffDetailPay> selectByListPage(OrderSearchVo searchVo, int pageNo, int pageSize) {
		PageHelper.startPage(pageNo, pageSize);
		List<OrgStaffDetailPay> list = orgStaffDetailPayMapper.selectByListPage(searchVo);
		PageInfo<OrgStaffDetailPay> result = new PageInfo<>(list);
		return result;

	}

	@Override
	public List<OrgStaffDetailPay> selectBySearchVo(OrderSearchVo searchVo) {
		return orgStaffDetailPayMapper.selectBySearchVo(searchVo);
	}

	@Override
	public Map<String, Double> selectTotalData(OrderSearchVo searchVo) {
		return orgStaffDetailPayMapper.selectTotalData(searchVo);
	}

	@Override
	public boolean setStaffDetailPay(Long staffId, String mobile, Short orderType, Long orderId, String orderNo, BigDecimal orderMoney, BigDecimal orderPay,
			String orderStatusStr, String remarks, Long addTime) {

		OrderSearchVo paySearchVo = new OrderSearchVo();
		paySearchVo.setStaffId(staffId);
		paySearchVo.setOrderId(orderId);
		paySearchVo.setOrderType(orderType);
		List<OrgStaffDetailPay> orgStaffDetailPays = this.selectBySearchVo(paySearchVo);

		if (!orgStaffDetailPays.isEmpty())
			return false;

		OrgStaffDetailPay orgStaffDetailPay = this.initStaffDetailPay();
		// 新增收入明细表 org_staff_detail_pay
		orgStaffDetailPay.setStaffId(staffId);
		orgStaffDetailPay.setMobile(mobile);
		orgStaffDetailPay.setOrderType(orderType);
		orgStaffDetailPay.setOrderId(orderId);
		orgStaffDetailPay.setOrderNo(orderNo);
		orgStaffDetailPay.setOrderMoney(orderMoney);
		orgStaffDetailPay.setOrderPay(orderPay);
		orgStaffDetailPay.setOrderStatusStr(orderStatusStr);
		orgStaffDetailPay.setRemarks(remarks);
		
		if (addTime.equals(0L)) {
			orgStaffDetailPay.setAddTime(TimeStampUtil.getNowSecond());
		} else {
			orgStaffDetailPay.setAddTime(addTime);
		}

		
		this.insert(orgStaffDetailPay);

		return true;
	}

	@Override
	public OrgStaffDetailPayOaVo getOrgStaffRepaymentVo(OrgStaffDetailPay orgStaffDetailPay) {
		
		OrgStaffDetailPayOaVo vo = new OrgStaffDetailPayOaVo();
		
		BeanUtilsExp.copyPropertiesIgnoreNull(orgStaffDetailPay, vo);
		
		OrgStaffs orgStaffs = orgStaffsService.selectByPrimaryKey(orgStaffDetailPay.getStaffId());
		vo.setName(orgStaffs.getName());
		
		OrgStaffPayDept staffPaydept = orgStaffPayDeptService.selectByOrderNo(orgStaffDetailPay.getOrderNo());
		vo.setPayTypeName(OneCareUtil.getPayTypeName(staffPaydept.getPayType()));
		vo.setAddTime(staffPaydept.getAddTime());
		String orderTypeName = OneCareUtil.getOrderTypeForStaffDetailPay(orgStaffDetailPay.getOrderType());
		vo.setOrderTypeName(orderTypeName);
		
		vo.setRepaymentMoney(staffPaydept.getOrderMoney());
		vo.setPayAccount(staffPaydept.getPayAccount());
		vo.setTrandId(staffPaydept.getTradeId());
		
		return vo;
	}
	
	

}
