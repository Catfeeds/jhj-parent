package com.jhj.action.app.staff;


import java.math.BigDecimal;
import java.text.ParseException;
import java.util.List;

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
import com.jhj.po.model.bs.OrgStaffCash;
import com.jhj.po.model.bs.OrgStaffFinance;
import com.jhj.po.model.bs.OrgStaffs;
import com.jhj.service.bs.OrgStaffAuthService;
import com.jhj.service.bs.OrgStaffCashService;
import com.jhj.service.bs.OrgStaffFinanceService;
import com.jhj.service.bs.OrgStaffsService;
import com.jhj.service.order.OrderQueryService;
import com.jhj.vo.OrderQuerySearchVo;
import com.jhj.vo.staff.OrgStaffFinanceAppVo;
import com.jhj.vo.staff.OrgStaffsVo;
import com.meijia.utils.BeanUtilsExp;
import com.meijia.utils.DateUtil;
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
	private OrderQueryService orderQueryService;
	
	@Autowired
	private OrgStaffFinanceService orgStaffFinanceService;
	
	@Autowired
	private OrgStaffCashService orgStaffCashService;
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
		OrgStaffAuth orgStaffAuth = orgStaffAuthService.selectByStaffIdAndServiceTypeId(staffId);
		
		OrgStaffsVo vo = new OrgStaffsVo();
		BeanUtilsExp.copyPropertiesIgnoreNull(orgStaffs, vo);
		
		
		//返回字段 auth_status
		vo.setAuthStatus((short)0);
		if (orgStaffAuth != null) {
			vo.setAuthStatus(orgStaffAuth.getAutStatus());
		}
		
		String startTime = DateUtil.getfirstDayOfMonth();
		String endTime = DateUtil.getLastDayOfMonth();
		
		OrderQuerySearchVo searchVo = new OrderQuerySearchVo();
		searchVo.setStartTime(TimeStampUtil.getMillisOfDay(startTime)/1000);
		searchVo.setEndTime(TimeStampUtil.getMillisOfDay(endTime)/1000);
		searchVo.setStaffId(staffId);
		
		//当月订单总数
		Long totalOrder = orderQueryService.getTotalOrderCountByMouth(searchVo);
		vo.setTotalOrder(totalOrder);
		//当月收入
		BigDecimal totalIncoming = orderQueryService.getTotalOrderIncomeMoney(searchVo);
		vo.setTotalIncoming(totalIncoming);
		
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
		if (orgStaffFinance == null) {
			return result;
		}
		OrgStaffFinanceAppVo vo = new OrgStaffFinanceAppVo();
		
		//BigDecimal money = orgStaffFinance.getTotalIncoming().subtract(orgStaffFinance.getTotalCash());
		//总提现金额
		BigDecimal totalCashMoney = orgStaffCashService.getTotalCashMoney(staffId);
		//剩余提现金额 = 总收入 - 欠款总金额 - 已提现金额
		BigDecimal money = orgStaffFinance.getTotalIncoming().subtract(orgStaffFinance.getTotalCash()).subtract(totalCashMoney);
				
		vo.setTotalCash(money);
		vo.setTotalDept(orgStaffFinance.getTotalDept());
		
		result.setData(vo);
		return result;
	
	}
	
	
}
