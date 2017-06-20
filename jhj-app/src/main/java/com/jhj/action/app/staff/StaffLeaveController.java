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
import com.jhj.po.model.bs.OrgStaffLeave;
import com.jhj.po.model.bs.OrgStaffSkill;
import com.jhj.po.model.bs.OrgStaffs;
import com.jhj.po.model.order.OrderDispatchs;
import com.jhj.po.model.order.OrderPrices;
import com.jhj.po.model.order.Orders;
import com.jhj.po.model.university.PartnerServiceType;
import com.jhj.service.bs.OrgStaffAuthService;
import com.jhj.service.bs.OrgStaffCashService;
import com.jhj.service.bs.OrgStaffFinanceService;
import com.jhj.service.bs.OrgStaffLeaveService;
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
import com.jhj.vo.org.LeaveSearchVo;
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
public class StaffLeaveController extends BaseController {

	@Autowired
	private OrgStaffsService orgStaffsService;

	@Autowired
	private OrgStaffLeaveService leaveService;

	/**
	 * 获取员工即将或者正在进行的请假记录
	 * 
	 * @param request
	 * @param staffId
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping(value = "get_leave", method = RequestMethod.GET)
	public AppResultData<Object> getLeave(HttpServletRequest request, @RequestParam("staff_id") Long staffId) throws ParseException {
		AppResultData<Object> result = new AppResultData<Object>(Constants.SUCCESS_0, ConstantMsg.SUCCESS_0_MSG, new String());
		
		Long startTime = TimeStampUtil.getBeginOfToday();
		LeaveSearchVo leaveSearchVo = new LeaveSearchVo();
		leaveSearchVo.setStaffId(staffId);
		leaveSearchVo.setLeaveStatus("1");
//		leaveSearchVo.setRangeStartDate(rangeStartDate);
		List<OrgStaffLeave> staffLeave = leaveService.selectBySearchVo(leaveSearchVo);
		
		return result;
	}

}
