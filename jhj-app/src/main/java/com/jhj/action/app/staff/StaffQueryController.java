package com.jhj.action.app.staff;


import java.math.BigDecimal;
import java.sql.Time;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.jhj.action.app.BaseController;
import com.jhj.common.ConstantMsg;
import com.jhj.common.Constants;
import com.jhj.po.model.bs.OrgStaffAuth;
import com.jhj.po.model.bs.OrgStaffFinance;
import com.jhj.po.model.bs.OrgStaffSkill;
import com.jhj.po.model.bs.OrgStaffs;
import com.jhj.po.model.order.OrderDispatchs;
import com.jhj.po.model.order.OrderPrices;
import com.jhj.po.model.order.Orders;
import com.jhj.po.model.university.PartnerServiceType;
import com.jhj.service.bs.OrgStaffAuthService;
import com.jhj.service.bs.OrgStaffCashService;
import com.jhj.service.bs.OrgStaffFinanceService;
import com.jhj.service.bs.OrgStaffSkillService;
import com.jhj.service.bs.OrgStaffsService;
import com.jhj.service.order.OrderDispatchsService;
import com.jhj.service.order.OrderPricesService;
import com.jhj.service.order.OrderQueryService;
import com.jhj.service.order.OrderStatService;
import com.jhj.service.order.OrdersService;
import com.jhj.service.university.PartnerServiceTypeService;
import com.jhj.vo.order.OrderDispatchSearchVo;
import com.jhj.vo.order.OrderQuerySearchVo;
import com.jhj.vo.order.OrderSearchVo;
import com.jhj.vo.staff.OrgStaffCashSearchVo;
import com.jhj.vo.staff.OrgStaffFinanceAppVo;
import com.jhj.vo.staff.OrgStaffSkillSearchVo;
import com.jhj.vo.staff.OrgStaffsVo;
import com.jhj.vo.staff.StaffAuthSearchVo;
import com.meijia.utils.BeanUtilsExp;
import com.meijia.utils.DateUtil;
import com.meijia.utils.MathBigDecimalUtil;
import com.meijia.utils.TimeStampUtil;
import com.meijia.utils.vo.AppResultData;

@Controller
@RequestMapping(value = "/app/staff")
public class StaffQueryController extends BaseController {

	@Autowired
	private OrgStaffsService orgStaffsService;
	
	@Autowired
	private OrgStaffAuthService orgStaffAuthService;
	
	@Autowired
	private OrdersService ordersService;
	
	@Autowired
	private OrderQueryService orderQueryService;
	
	@Autowired
	private OrgStaffFinanceService orgStaffFinanceService;
	
	@Autowired
	private OrgStaffCashService orgStaffCashService;
	
	@Autowired
	private OrgStaffSkillService orgStaffSkillService;
	
	@Autowired
	private PartnerServiceTypeService partService;
	
	@Autowired
	private OrderStatService orderStatService;
	
	@Autowired
	private OrderDispatchsService orderDispatchsService;
	
	@Autowired
	private OrderPricesService orderPricesService;
	
	/**
	 * 我的接口
	 * @param request
	 * @param staffId
	 * @return
	 * @throws ParseException 
	 */
	@RequestMapping(value = "get_info",method = RequestMethod.GET)
	public AppResultData<Object> mineInfo(
		HttpServletRequest request,
		@RequestParam("user_id") Long staffId) throws ParseException{
		AppResultData<Object> result = new AppResultData<Object>(
				Constants.SUCCESS_0, ConstantMsg.SUCCESS_0_MSG, new String());

		OrgStaffs orgStaffs = orgStaffsService.selectByPrimaryKey(staffId);
		if (orgStaffs == null) {
			result = new AppResultData<Object>(Constants.ERROR_999,
					ConstantMsg.MINE_NO_EXIST, "");
			return result;
		}
		
		OrgStaffAuth orgStaffAuth = null;
		StaffAuthSearchVo searchVo2 = new StaffAuthSearchVo();
		searchVo2.setStaffId(staffId);
		searchVo2.setServiceTypeId(0L);
		List<OrgStaffAuth> orgStaffAuths = orgStaffAuthService.selectBySearchVo(searchVo2);
		if (!orgStaffAuths.isEmpty()) orgStaffAuth = orgStaffAuths.get(0);
		
		OrgStaffsVo vo = new OrgStaffsVo();
		BeanUtilsExp.copyPropertiesIgnoreNull(orgStaffs, vo);
		
		
		//返回字段 auth_status
		vo.setAuthStatus((short)0);
		if (orgStaffAuth != null) {
			vo.setAuthStatus(orgStaffAuth.getAutStatus());
		}
		

//		String endTime = DateUtil.getLastDayOfMonth() + "23:59:59";
		int year = DateUtil.getYear();
		int month = DateUtil.getMonth();
		OrderQuerySearchVo searchVo = new OrderQuerySearchVo();
		searchVo.setStartTime(TimeStampUtil.getBeginOfMonth(year, month));
		searchVo.setEndTime(TimeStampUtil.getNowSecond());
		searchVo.setStaffId(staffId);
		
		//当月订单总数
		Long totalOrder = orderStatService.getTotalOrderCountByMouth(searchVo);
		vo.setTotalOrder(totalOrder);
		//当月收入
		OrderSearchVo orderSearchVo = new OrderSearchVo();
		orderSearchVo.setStaffId(staffId);
		orderSearchVo.setStartServiceTime(TimeStampUtil.getBeginOfMonth(year, month));
		orderSearchVo.setEndServiceTime(TimeStampUtil.getNowSecond());
		BigDecimal totalIncoming = orderStatService.getTotalOrderIncomeMoney(orderSearchVo);
		vo.setTotalIncoming(totalIncoming);
		
		//获取技能信息
		List<String> skills = new ArrayList<String>();
		
		OrgStaffSkillSearchVo searchVo1 = new OrgStaffSkillSearchVo();
		searchVo1.setStaffId(staffId);
		List<OrgStaffSkill> hasSkills = orgStaffSkillService.selectBySearchVo(searchVo1);
		for (OrgStaffSkill item : hasSkills) {
			PartnerServiceType serviceType = partService.selectByPrimaryKey(item.getServiceTypeId());
			skills.add(serviceType.getName());
		}
		vo.setSkills(skills);
		
		result.setData(vo);
		return result;
	}
	/**
	 * 获取钱款总额接口
	 * @param request
	 * @param staffId
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping(value = "get_total_dept",method = RequestMethod.GET)
	public AppResultData<Object> getTotalDept(
		HttpServletRequest request,
		@RequestParam("staff_id") Long staffId
		) throws ParseException{
		AppResultData<Object> result = new AppResultData<Object>(
				Constants.SUCCESS_0, ConstantMsg.SUCCESS_0_MSG, new String());
	    
		OrgStaffFinance orgStaffFinance = orgStaffFinanceService.selectByStaffId(staffId);
		
		OrgStaffFinanceAppVo vo = new OrgStaffFinanceAppVo();
		
		vo.setTotalCash(new BigDecimal(0));
		vo.setTotalDept(new BigDecimal(0));
		if (orgStaffFinance == null) {
			result.setData(vo);
			return result;
		}
		
		
		
		//总提现中的金额
		OrgStaffCashSearchVo searchVo = new OrgStaffCashSearchVo();
		searchVo.setStaffId(staffId);
		List<Short> orderStatusList = new ArrayList<Short>();
		orderStatusList.add((short) 0);
		orderStatusList.add((short) 1);
		searchVo.setOrderStatusList(orderStatusList);
		BigDecimal totalCashIngMoney = orgStaffCashService.getTotalCashMoney(searchVo);
		
		//已经提现的金额
		searchVo = new OrgStaffCashSearchVo();
		searchVo.setStaffId(staffId);
		searchVo.setOrderStatus((short) 3);
		BigDecimal totalCashedMoney = orgStaffCashService.getTotalCashMoney(searchVo);
		
		//剩余提现金额 = 总收入 - 提现中的金额 - 已提现金额
		BigDecimal money = orgStaffFinance.getTotalIncoming().subtract(totalCashIngMoney).subtract(totalCashedMoney);
		
		vo.setTotalCash(money);
		vo.setTotalDept(orgStaffFinance.getTotalDept());
		
		result.setData(vo);
		return result;
	
	}
	
	/**
	 * 获取员工的派工时间
	 * @param staffId  员工ID
	 * 
	 * @return 
	 * */
	@RequestMapping(value = "get_dispatch_dates.json",method = RequestMethod.GET)
	public AppResultData<Object> getOrderDispatchDates(@RequestParam("staff_id") Long staffId){
		AppResultData<Object> result = new AppResultData<Object>(
				Constants.SUCCESS_0, ConstantMsg.SUCCESS_0_MSG, new String());
		
		OrderDispatchSearchVo searchVo = new OrderDispatchSearchVo();
		searchVo.setDispatchStatus((short) 1);
		searchVo.setStaffId(staffId);
		searchVo.setStartServiceTime(TimeStampUtil.getNow()/1000);
		Calendar cal=Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH, 7);
		searchVo.setEndServiceTime(TimeStampUtil.getMillisOfDate(cal.getTime())/1000);
		List<OrderDispatchs> orderDispatchs = orderDispatchsService.selectBySearchVo(searchVo);
		List<String> list=new ArrayList<String>();
		if(orderDispatchs!=null && orderDispatchs.size()>0){
			for(int i=0;i<orderDispatchs.size();i++){
				OrderDispatchs od = orderDispatchs.get(i);
				Long serviceDate = od.getServiceDate();
				String strDate = TimeStampUtil.timeStampToDateStr(serviceDate*1000);
				list.add(strDate);
			}
		}
		
		result = new AppResultData<Object>(
				Constants.SUCCESS_0, ConstantMsg.SUCCESS_0_MSG, list.toArray());
		return result;
	}
	

	/**
	 * 获取员工的技能信息
	 * @param staffId  员工ID
	 * 
	 * @return 
	 * */
	@RequestMapping(value = "get_skills.json",method = RequestMethod.GET)
	public AppResultData<Object> getSkills(@RequestParam("staff_id") Long staffId){
		AppResultData<Object> result = new AppResultData<Object>(
				Constants.SUCCESS_0, ConstantMsg.SUCCESS_0_MSG, new String());
	
		OrgStaffSkillSearchVo searchVo = new OrgStaffSkillSearchVo();
		searchVo.setStaffId(staffId);
		List<OrgStaffSkill> hasSkills = orgStaffSkillService.selectBySearchVo(searchVo);
		
		
		List<Map<String, Object>> skills = new ArrayList<Map<String, Object>>();
		for (OrgStaffSkill item : hasSkills) {
			PartnerServiceType serviceType = partService.selectByPrimaryKey(item.getServiceTypeId());
			
			Map<String, Object> skill = new HashMap<String, Object>();
			skill.put("service_type_id", item.getServiceTypeId());
			skill.put("service_type_name", serviceType.getName());
			
			skills.add(skill);
		}
		
		result.setData(skills);
		return result;
	}
	
	//计算服务人员的实际收入.
	@RequestMapping(value = "get_staff_incoming.json",method = RequestMethod.GET)
	public AppResultData<Object> getStaffIncoming(@RequestParam("staff_id") Long staffId){
		AppResultData<Object> result = new AppResultData<Object>(
				Constants.SUCCESS_0, ConstantMsg.SUCCESS_0_MSG, new String());
		
		
		OrgStaffFinance orgStaffFinance = orgStaffFinanceService.selectByStaffId(staffId);
		
		BigDecimal staffInComing = orgStaffFinance.getTotalIncoming();
		
		//1. 找出服务人员的派工记录，对于的订单列表集合A。
		OrderDispatchSearchVo searchVo = new OrderDispatchSearchVo();
		searchVo.setStaffId(staffId);
		searchVo.setDispatchStatus((short) 1);
		searchVo.setStartAddTime(1476892800L);
		
		List<OrderDispatchs> orderDispatchs = orderDispatchsService.selectBySearchVo(searchVo);
		
		if (orderDispatchs.isEmpty()) return result;
		
		BigDecimal totalIncoming = new BigDecimal(0);
		
		for (OrderDispatchs item : orderDispatchs) {
			Long orderId = item.getOrderId();
			Orders order = ordersService.selectByPrimaryKey(orderId);
			BigDecimal orderIncoming = orderPricesService.getTotalOrderIncoming(order, staffId);
			totalIncoming = MathBigDecimalUtil.add(totalIncoming, orderIncoming);
		}
		
		result.setData("staffInComing =" + MathBigDecimalUtil.round2(staffInComing) + "---- totalInComing = " + MathBigDecimalUtil.round2(totalIncoming));
		return result;
	}
	
	
}
