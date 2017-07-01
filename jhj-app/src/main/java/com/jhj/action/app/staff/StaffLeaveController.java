package com.jhj.action.app.staff;

import java.text.ParseException;
import java.util.Date;
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
import com.jhj.po.model.bs.OrgStaffLeave;
import com.jhj.po.model.bs.OrgStaffs;
import com.jhj.po.model.order.OrderDispatchs;
import com.jhj.po.model.order.Orders;
import com.jhj.service.bs.OrgStaffLeaveService;
import com.jhj.service.bs.OrgStaffsService;
import com.jhj.service.order.OrderDispatchsService;
import com.jhj.service.order.OrdersService;
import com.jhj.vo.order.OrderDispatchSearchVo;
import com.jhj.vo.org.LeaveSearchVo;
import com.meijia.utils.DateUtil;
import com.meijia.utils.TimeStampUtil;
import com.meijia.utils.vo.AppResultData;

@Controller
@RequestMapping(value = "/app/staff")
public class StaffLeaveController extends BaseController {

	@Autowired
	private OrgStaffsService orgStaffsService;

	@Autowired
	private OrgStaffLeaveService leaveService;
	
	@Autowired
	private OrderDispatchsService orderDispatchService;
	
	@Autowired
	private OrdersService orderService;


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
		
		
		Date today = DateUtil.parse(DateUtil.getToday());
		LeaveSearchVo leaveSearchVo = new LeaveSearchVo();
		leaveSearchVo.setStaffId(staffId);
		leaveSearchVo.setLeaveStatus("1");
//		leaveSearchVo.setRangeStartDate(rangeStartDate);
		leaveSearchVo.setRangeStartDate(today);
		leaveSearchVo.setRangeEndDate(today);
		List<OrgStaffLeave> staffLeave = leaveService.selectBySearchVo(leaveSearchVo);
		if (!staffLeave.isEmpty()) {
			OrgStaffLeave item = staffLeave.get(0);
			result.setData(item);
		}
		
		return result;
	}
	
	
	/**
	 * 获取员工即将或者正在进行的请假记录
	 * 
	 * @param request
	 * @param staffId
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping(value = "do_leave", method = RequestMethod.POST)
	public AppResultData<Object> doLeave(
			HttpServletRequest request, 
			@RequestParam("id") Long id,
			@RequestParam("staff_id") Long staffId,
			@RequestParam("leaveDate") String leaveDateStr,
			@RequestParam("leaveDateEnd") String leaveDateEndStr,
			@RequestParam("halfDay") int halfDay,
			@RequestParam("leaveStatus") String leaveStatus,
			@RequestParam(value = "adminId", required = false, defaultValue = "0") Long adminId,
			@RequestParam(value = "remarks", required = false, defaultValue = "") String remarks
			) throws ParseException {
		AppResultData<Object> result = new AppResultData<Object>(Constants.SUCCESS_0, ConstantMsg.SUCCESS_0_MSG, new String());
		
		Date startDate = DateUtil.parse(leaveDateStr);
		Date endDate = DateUtil.parse(leaveDateEndStr);
		int totalDays = DateUtil.daysOfTwo(startDate, endDate);
		if (totalDays == 0) totalDays = 1;
		
		//校验1, 结束日期不能小于开始日期
		if (!DateUtil.compare(leaveDateStr, leaveDateEndStr)) {
			result.setStatus(Constants.ERROR_999);
			result.setMsg("结束日期不能小于开始日期");
			return result;
		}
		

		
		int start = 0;
		int end = 23;
		if (halfDay == 1) {
			start  = 0; end = 13;
		}
		if (halfDay == 2) {
			start = 13; end = 23;
		}
		
		//校验2: 用户在请假范围内是否有相应的派工信息.
		if (leaveStatus.equals("1")) {
			String startTimeStr = leaveDateStr + String.valueOf(start) + ":00:00";
			String endTimeStr = leaveDateEndStr + String.valueOf(end) + ":59:59";
			
			Long startServiceTime = TimeStampUtil.getMillisOfDayFull(startTimeStr) / 1000;
			Long endServiceTime = TimeStampUtil.getMillisOfDayFull(endTimeStr) / 1000;
			
			OrderDispatchSearchVo searchVo1 = new OrderDispatchSearchVo();
			searchVo1.setStaffId(staffId);
			searchVo1.setDispatchStatus((short) 1);
			searchVo1.setStartServiceTime(startServiceTime);
			searchVo1.setEndServiceTime(endServiceTime);
			List<OrderDispatchs> disList = orderDispatchService.selectByMatchTime(searchVo1);
			
			for (OrderDispatchs item : disList) {
				Long orderId = item.getOrderId();
				Orders order = orderService.selectByPrimaryKey(orderId);
				Short orderStatus = order.getOrderStatus();
				if (orderStatus.equals(Constants.ORDER_STATUS_3) || 
					orderStatus.equals(Constants.ORDER_STATUS_5)) {
					Long serviceDateTime = item.getServiceDate();
					String serviceDateStr = TimeStampUtil.timeStampToDateStr(serviceDateTime, "MM-dd HH:mm");
					result.setStatus(Constants.ERROR_999);
					result.setMsg(item.getStaffName() + "在" + serviceDateStr + "有派工，不能进行此时间段请假");
					return result;
				}
			}
		}
				
		OrgStaffLeave record = leaveService.initLeave();
		if (id > 0L) {
			record = leaveService.selectByPrimaryKey(staffId);
		}
		
		if (id.equals(0L)) {

			LeaveSearchVo leaveSearchVo = new LeaveSearchVo();
			leaveSearchVo.setStaffId(staffId);
			leaveSearchVo.setLeaveStatus("1");
//			leaveSearchVo.setRangeStartDate(rangeStartDate);
			leaveSearchVo.setRangeStartDate(startDate);
			leaveSearchVo.setRangeEndDate(endDate);
			List<OrgStaffLeave> staffLeave = leaveService.selectBySearchVo(leaveSearchVo);
			if (!staffLeave.isEmpty()) {
				record = staffLeave.get(0);
			}
		}
		
		if (record == null) record = leaveService.initLeave();
		
		OrgStaffs staff = orgStaffsService.selectByPrimaryKey(staffId);
		record.setParentId(staff.getParentOrgId());
		record.setOrgId(staff.getOrgId());
		record.setStaffId(staffId);
		record.setAdminId(adminId);
		record.setLeaveDate(startDate);
		record.setLeaveDateEnd(endDate);
		record.setStart((short) start);
		record.setEnd((short) end);
		record.setTotalDays(totalDays);
		record.setLeaveStatus(leaveStatus);
		record.setRemarks(remarks);
		
		if (id > 0L) {
			leaveService.updateByPrimaryKeySelective(record);
		} else {
			leaveService.insert(record);
		}
		
		return result;
	}

}
