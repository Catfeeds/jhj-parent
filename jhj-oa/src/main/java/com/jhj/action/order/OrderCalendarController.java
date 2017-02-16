package com.jhj.action.order;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.google.gson.Gson;
import com.jhj.action.BaseController;
import com.jhj.oa.auth.AuthHelper;
import com.jhj.oa.auth.AuthPassport;
import com.jhj.po.model.bs.OrgStaffFinance;
import com.jhj.po.model.bs.OrgStaffLeave;
import com.jhj.po.model.bs.OrgStaffs;
import com.jhj.po.model.order.OrderDispatchs;
import com.jhj.po.model.order.Orders;
import com.jhj.po.model.university.PartnerServiceType;
import com.jhj.service.bs.OrgStaffBlackService;
import com.jhj.service.bs.OrgStaffFinanceService;
import com.jhj.service.bs.OrgStaffLeaveService;
import com.jhj.service.bs.OrgStaffsService;
import com.jhj.service.bs.OrgsService;
import com.jhj.service.order.OrderDispatchsService;
import com.jhj.service.order.OrdersService;
import com.jhj.service.university.PartnerServiceTypeService;
import com.jhj.vo.order.OrderDispatchSearchVo;
import com.jhj.vo.order.newDispatch.EventVo;
import com.jhj.vo.order.newDispatch.OaStaffDisAndLeaveVo;
import com.jhj.vo.order.newDispatch.TimeEventVo;
import com.jhj.vo.org.LeaveSearchVo;
import com.jhj.vo.staff.OrgStaffFinanceSearchVo;
import com.jhj.vo.staff.StaffSearchVo;
import com.meijia.utils.DateUtil;
import com.meijia.utils.StringUtil;
import com.meijia.utils.TimeStampUtil;

/**
 *
 * @author :hulj
 * @Date : 2016年5月30日上午11:01:10
 * @Description:
 *
 */
@Controller
@RequestMapping(value = "/orderCanlendar")
public class OrderCalendarController extends BaseController {

	@Autowired
	private OrderDispatchsService orderDispatchsService;

	@Autowired
	private OrgStaffLeaveService leaveService;

	@Autowired
	private OrgsService orgService;

	@Autowired
	private OrgStaffsService staffService;

	@Autowired
	private OrdersService orderService;

	@Autowired
	private PartnerServiceTypeService partService;
	
	@Autowired
	private OrgStaffFinanceService staffFinanceService;
	
	/**
	 *   
	 *  订单日历--控件展示
	 *  
	 */
	
	@AuthPassport
	@RequestMapping(value = "/order-scheduling", method = RequestMethod.GET )
	public String orderCalender(HttpServletRequest request, Model model,
		@RequestParam("org_staff_id") Long orgStaffId) {
		
		model.addAttribute("orgStaffId", orgStaffId);

		return "order/orderCalendarList";
	}

	/**
	 * @throws ParseException
	 * @throws UnsupportedEncodingException
	 * @Title: staffOrderList
	 * @Description:
	 * 
	 *               门店 工作人员 排班表 和 请假表
	 */
	@AuthPassport
	@RequestMapping(value = "calendar_list", method = {RequestMethod.GET,RequestMethod.POST})
	public String staffOrderList(OrderDispatchSearchVo searchVo, HttpServletRequest request, Model model) throws ParseException, UnsupportedEncodingException {

		// 得到 当前登录 的 门店id，并作为搜索条件
		Long parentId = AuthHelper.getSessionLoginOrg(request);
		//==================================================所有员工
		StaffSearchVo staffSearchVo = new StaffSearchVo();
		staffSearchVo.setStatus(1);
		if (parentId > 0L) {
			staffSearchVo.setParentId(parentId);
		}
		String name = searchVo.getStaffName();
		if (name != null && name != "") {
			String staffName = new String(name.getBytes("ISO-8859-1"), "UTF-8");
			staffSearchVo.setName(staffName);
			searchVo.setStaffName(staffName);
		}
		if (searchVo.getStaffMobile() != null && searchVo.getStaffMobile() != "") {
			staffSearchVo.setMobile(searchVo.getStaffMobile());
		}
		if(searchVo.getParentId()!=null){
			staffSearchVo.setParentId(searchVo.getParentId());
		}
		if(searchVo.getOrgId()!=null){
			staffSearchVo.setOrgId(searchVo.getOrgId());
		}
		// 登录门店下的 所有 员工
		List<OrgStaffs> staffList = staffService.selectBySearchVo(staffSearchVo);
		
		int staffListSize=staffList.size();
		//==================================================查出派工列表

		String startTimeStr = request.getParameter("startTimeStr");
		if (StringUtil.isEmpty(startTimeStr)) {
			startTimeStr = DateUtil.getToday();
		}
		Long startTime = TimeStampUtil.getMillisOfDay(startTimeStr) / 1000;
		searchVo.setStartServiceTime(startTime);
		String endTimeStr = request.getParameter("endTimeStr");
		if (StringUtil.isEmpty(endTimeStr)) {
			endTimeStr =  DateUtil.sevenDayAfterToday(DateUtil.parse(startTimeStr));
		}
		Long endTime = TimeStampUtil.getMillisOfDayFull(endTimeStr + " 23:59:59") / 1000;
		searchVo.setEndServiceTime(endTime);
		searchVo.setDispatchStatus((short)1);
		
		// 排班列表人员统计
		int dispatchSizeAM=0;
		int dispatchSizePM=0;

		//==================================================查出请假信息
		LeaveSearchVo leaveSearchVo = new LeaveSearchVo();
		if (parentId > 0L) {
			// 所有员工的请假情况
			leaveSearchVo.setParentId(parentId);
		}
		
		Date startDate = DateUtil.parse(startTimeStr);
		Date endDate = DateUtil.parse(endTimeStr);
		leaveSearchVo.setRangeStartDate(startDate);
		leaveSearchVo.setRangeEndDate(endDate);
		leaveSearchVo.setLeaveStatus("1");

		//请假人员统计
		int leaveStaffSize=0;
		
		// 页面 Vo
		List<OaStaffDisAndLeaveVo> listVo = new ArrayList<OaStaffDisAndLeaveVo>(staffList.size());

		// 一个 7天的 时间段, 由 开始时间决定。如果没有，则从今天起 往前 7天
		List<String> weekDateList = DateUtil.getLastWeekArray(startTimeStr);
		Long compareTime=TimeStampUtil.getMillisOfDayFull(startTimeStr+" 12:00:00");
		
		int blackNum=0;
		Set<Long> set=new HashSet<Long>();
		
		//黑名单员工
		OrgStaffFinanceSearchVo vo=new OrgStaffFinanceSearchVo();
		vo.setIsBlack((short)1);
		List<OrgStaffFinance> staffFinanceList = staffFinanceService.selectBySearchVo(vo);
		
		// 初始化时间、staff
		for (OrgStaffs orgStaff : staffList) {
			Long staffId = orgStaff.getStaffId();
			OaStaffDisAndLeaveVo disAndLeaveVo = new OaStaffDisAndLeaveVo();
			disAndLeaveVo.setStaffId(orgStaff.getStaffId());
			disAndLeaveVo.setStaffName(orgStaff.getName());
			List<TimeEventVo> timeEventList = new ArrayList<TimeEventVo>();
			
			// 加入请假事件
			leaveSearchVo.setStaffId(staffId);
			List<OrgStaffLeave> leaveList = leaveService.selectBySearchVo(leaveSearchVo);
			//加入派工
			searchVo.setStaffId(staffId);
			List<OrderDispatchs> disList = orderDispatchsService.selectBySearchVo(searchVo);
			for (String weekDate : weekDateList) {
				TimeEventVo timeEventVo = new TimeEventVo();
				timeEventVo.setTimeStr(weekDate);
				// 具体事件
				List<EventVo> eventList = new ArrayList<EventVo>();
				if(leaveList!=null && leaveList.size()>0){
					for (OrgStaffLeave staffLeave : leaveList) {
						Long leaveDate = staffLeave.getLeaveDate().getTime();
						Long leaveDateEnd = staffLeave.getLeaveDateEnd().getTime();
						Long weekDateToday=TimeStampUtil.getMillisOfDay(weekDate);
						if (leaveDate<=weekDateToday && leaveDateEnd >=weekDateToday) {
							EventVo eventVo = new EventVo();
							eventVo.setDateDuration(weekDate);
							eventVo.setEventName("请假中");
							eventVo.setServiceTime(leaveDate);
							eventList.add(eventVo);
						}
					}
					if(startTimeStr.equals(weekDate)){
						leaveStaffSize++;
					}
				}
				
				// 加入 排班事件
				if(disList!=null && disList.size()>0){
					for(OrderDispatchs staffDisVo : disList){
						// 服务日期 , 格式 'yyyy-MM-dd'
						Long serviceDate = staffDisVo.getServiceDate() * 1000;
						String serviceDateStr = TimeStampUtil.timeStampToDateStr(serviceDate, "yyyy-MM-dd");
						String orderNo = staffDisVo.getOrderNo();
						if (serviceDateStr.equals(weekDate)) {
							EventVo eventVo = new EventVo();
							Orders orders = orderService.selectByOrderNo(orderNo);
							double serviceHour = orders.getServiceHour();
							Long serviceType = orders.getServiceType();
							 //得到 订单服务时间的 时间点--> 如 16:30、 08:00
							String startHourMinStr = TimeStampUtil.timeStampToDateStr(serviceDate, "HH:mm");
							// 订单服务时间的 结束时间点
							String endHourMinStr = TimeStampUtil.timeStampToDateStr((long) (serviceDate + serviceHour * 3600 * 1000), "HH:mm");
							eventVo.setEventName("");
							PartnerServiceType type = partService.selectByPrimaryKey(serviceType);
							if (type != null) {
								String typeName = type.getName();
								if(typeName.length()>4){
									eventVo.setEventName(typeName.substring(0, 4));
								}else{
									eventVo.setEventName(typeName);
								}
								
							}
							eventVo.setDateDuration(startHourMinStr + "~" + endHourMinStr);
							eventVo.setServiceTime(serviceDate);
							eventVo.setOrderNo(orderNo);
							eventVo.setOrderType(orders.getOrderType());
							eventList.add(eventVo);
							if(startTimeStr.equals(serviceDateStr)&& serviceDate>=compareTime){
								dispatchSizePM++;
							}
							if(startTimeStr.equals(serviceDateStr)&& serviceDate<compareTime){
								dispatchSizeAM++;
							}
						}
					}
				}
				if(!staffFinanceList.isEmpty() && staffFinanceList.size()>0){
					for(int i=0;i<staffFinanceList.size();i++){
						OrgStaffFinance orgStaffFinance = staffFinanceList.get(i);
						if(orgStaffFinance.getStaffId()==staffId){
							EventVo eventVo = new EventVo();
							eventVo.setDateDuration(weekDate);
							eventVo.setEventName("黑名单");
//							eventVo.setServiceTime(leaveDate);
							eventList.add(eventVo);
						}
					}
				}
				timeEventVo.setEventList(eventList);
				timeEventList.add(timeEventVo);
			}
			
			disAndLeaveVo.setTimeEventList(timeEventList);
			listVo.add(disAndLeaveVo);
		}
		if(staffFinanceList!=null && staffFinanceList.size()>0){
			blackNum=staffFinanceList.size();
			for(OrgStaffFinance sf:staffFinanceList){
				if(set.contains(sf.getStaffId())){
//					set.add(sf.getStaffId());
					blackNum--;
				}
			}
		}

		Gson gson = new Gson();
		String json = gson.toJson(listVo);
		System.out.println(json);
		
		model.addAttribute("listVoModel", json);
		model.addAttribute("disAndLeaveSearchVoModel", searchVo);
		model.addAttribute("loginOrgId", parentId); // 当前登录的 id,动态显示搜索 条件
		model.addAttribute("weekDateModel", weekDateList);
		model.addAttribute("amStaffSize",staffListSize-leaveStaffSize-dispatchSizeAM );
		model.addAttribute("pmStaffSize",staffListSize-leaveStaffSize-dispatchSizePM );
		model.addAttribute("dispatchNum",staffListSize-blackNum-leaveStaffSize);

		return "staffDisAndLeave/staffDisAndLeaveList";
	}
}
