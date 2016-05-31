package com.jhj.action.order;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.google.gson.Gson;
import com.jhj.action.BaseController;
import com.jhj.oa.auth.AuthHelper;
import com.jhj.po.model.bs.OrgStaffLeave;
import com.jhj.po.model.bs.OrgStaffs;
import com.jhj.po.model.bs.Orgs;
import com.jhj.po.model.order.Orders;
import com.jhj.po.model.university.PartnerServiceType;
import com.jhj.service.bs.OrgStaffLeaveService;
import com.jhj.service.bs.OrgStaffsService;
import com.jhj.service.bs.OrgsService;
import com.jhj.service.order.OrderDispatchsService;
import com.jhj.service.order.OrdersService;
import com.jhj.service.university.PartnerServiceTypeService;
import com.jhj.vo.OaOrderDisSearchVo;
import com.jhj.vo.StaffSearchVo;
import com.jhj.vo.dispatch.StaffDispatchVo;
import com.jhj.vo.order.newDispatch.EventVo;
import com.jhj.vo.order.newDispatch.OaStaffDisAndLeaveVo;
import com.jhj.vo.order.newDispatch.TimeEventVo;
import com.jhj.vo.org.GroupSearchVo;
import com.jhj.vo.org.LeaveSearchVo;
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
	private OrderDispatchsService disPatchService;

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
	
	/**
	 * @throws ParseException 
	 * @Title: staffOrderList
	 * @Description:
	 * 
	 *               门店 工作人员 排班表 和 请假表
	 */
	@RequestMapping(value = "calendar_list", method = RequestMethod.GET)
	public String staffOrderList(OaOrderDisSearchVo disSearchVo, HttpServletRequest request, Model model) throws ParseException {

		// 得到 当前登录 的 门店id，并作为搜索条件
		String org = AuthHelper.getSessionLoginOrg(request);
		
		 org = "1";
		
		List<Long> cloudIdList = new ArrayList<Long>();

		if (!org.equals("0") && !StringUtil.isEmpty(org)) {

			/*
			 * 如果是店长 ，只能看到 自己门店 对应的 所有 云店 的 派工记录  （成功派工）。
			 */

			GroupSearchVo groupSearchVo = new GroupSearchVo();
			groupSearchVo.setParentId(Long.parseLong(org));

			List<Orgs> cloudList = orgService.selectCloudOrgByParentOrg(groupSearchVo);

			for (Orgs orgs : cloudList) {
				cloudIdList.add(orgs.getOrgId());
			}

		} else {
			// 如果是 运营 人员，查看所有 云店
			List<Orgs> cloudOrgList = orgService.selectCloudOrgs();

			for (Orgs orgs : cloudOrgList) {
				cloudIdList.add(orgs.getOrgId());
			}

			/*
			 * 对于 未派工的 订单，，没有 云店， 只有运营人员 可以 看到，以便 后续处理
			 */
			cloudIdList.add(0L);
		}

		
		//1. 能查看的云店
		disSearchVo.setCloudOrgList(cloudIdList);
		
		/*
		 * 2. 时间段, 默认从今天起  往前 7天  
		 * 
		 * 	选择 时间段时, 也只能是  一个  跨度为7天的时间段
		 * 
		 *  格式为  "yyyy-MM-dd"
		 */
		
		if(StringUtil.isEmpty(disSearchVo.getStartTimeStr())){
			disSearchVo.setStartTimeStr(DateUtil.sevenDayBeforeToday());
			
//			disSearchVo.setStartTimeStr("2016-05-01");
			
		}
		
		if(StringUtil.isEmpty(disSearchVo.getEndTimeStr())){
			disSearchVo.setEndTimeStr(DateUtil.format(new Date(), "yyyy-MM-dd"));
			
//			disSearchVo.setEndTimeStr("2016-05-07");
		}
		
		//排班列表
		List<StaffDispatchVo> disList = disPatchService.selectStaffDisBySevenDay(disSearchVo);
		
		LeaveSearchVo leaveSearchVo = new LeaveSearchVo();
		
		//登录门店下的 所有 员工
		List<OrgStaffs> staffList = new ArrayList<OrgStaffs>();
		
		if (!org.equals("0") && !StringUtil.isEmpty(org)) {
			
			//所有员工的请假情况
			leaveSearchVo.setParentOrgId(Long.valueOf(org));
			
			//门店下的所有员工 ............
			StaffSearchVo staffSearchVo = new StaffSearchVo();
			
			staffSearchVo.setParentId(Long.valueOf(org));
			
			staffList = staffService.selectNewStaffList(staffSearchVo);
			
		}
		
		leaveSearchVo.setDispatchDateStartStr(DateUtil.getUnixTimeStamp(DateUtil.getBeginOfDay(disSearchVo.getStartTimeStr())));
		
		leaveSearchVo.setDispatchDateEndStr(DateUtil.getUnixTimeStamp(DateUtil.getBeginOfDay(disSearchVo.getEndTimeStr())));
		//请假列表
		List<OrgStaffLeave> leaveList = leaveService.selectByLeaveSearchVo(leaveSearchVo);
		
		
		//页面 Vo
		List<OaStaffDisAndLeaveVo> listVo = new ArrayList<OaStaffDisAndLeaveVo>(staffList.size());
		
		//一个 7天的 时间段, 由 开始时间决定。如果没有，则从今天起 往前 7天
		List<String> weekDateList = DateUtil.getLastWeekArray(disSearchVo.getStartTimeStr());
		
		
		// 初始化时间、staff
		for (OrgStaffs orgStaff : staffList) {
			
			OaStaffDisAndLeaveVo disAndLeaveVo = new OaStaffDisAndLeaveVo();
			
			disAndLeaveVo.setStaffId(orgStaff.getStaffId());
			disAndLeaveVo.setStaffName(orgStaff.getName());
			
			Long staffId = orgStaff.getStaffId();
			
			List<TimeEventVo> timeEventList = new ArrayList<TimeEventVo>();
			
			for (String  weekDate : weekDateList) {
				
				TimeEventVo timeEventVo = new TimeEventVo();
				timeEventVo.setTimeStr(weekDate);
				
				//具体事件
				List<EventVo> eventList = new ArrayList<EventVo>();
				
				//加入请假事件
				for (OrgStaffLeave staffLeave : leaveList) {
					
					Long leaveStaffId = staffLeave.getStaffId();
					String leaveDate =  DateUtil.formatDate(staffLeave.getLeaveDate());
					
					if(leaveStaffId == staffId && leaveDate.equals(weekDate)){
						
						EventVo eventVo = new EventVo();
						
						//请假开始时间点
						Short start = staffLeave.getStart();
						//请假结束时间点
						Short end = staffLeave.getEnd();
						
						eventVo.setDateDuration(start +"点~"+end+"点");
						eventVo.setEventName("请假");
						
						eventList.add(eventVo);
					}
				}
				
				//加入 排班事件
				for (StaffDispatchVo staffDisVo : disList) {
					
					// 服务日期 , 格式  'yyyy-MM-dd'
					String serviceDateStr = staffDisVo.getServiceDateStr();
					
					Long disStaffId = staffDisVo.getStaffId();
					
					Long orderId = staffDisVo.getOrderId();
					
					
					if (disStaffId == staffId && serviceDateStr.equals(weekDate)) {
						
						EventVo eventVo = new EventVo();
						
						Orders orders = orderService.selectByPrimaryKey(orderId);
						
						Long serviceDate = orders.getServiceDate()*1000;
						Short serviceHour = orders.getServiceHour();
						
						Long serviceType = orders.getServiceType();
						
						// 得到 订单服务时间的 时间点--> 如 16:30、 08:00
						String startHourMinStr = TimeStampUtil.timeStampToDateStr(serviceDate, "HH:mm");
						
						// 订单服务时间的 结束时间点
						String endHourMinStr = TimeStampUtil.timeStampToDateStr(serviceDate + serviceHour*3600*1000, "HH:mm");
						
						
						PartnerServiceType type = partService.selectByPrimaryKey(serviceType);
						
						eventVo.setEventName(type.getName()); 
						eventVo.setDateDuration(startHourMinStr +"~"+ endHourMinStr);
						
						eventList.add(eventVo);
					}
			    }
				
				timeEventVo.setEventList(eventList);	
				timeEventList.add(timeEventVo);
			}
			disAndLeaveVo.setTimeEventList(timeEventList);
			
			listVo.add(disAndLeaveVo);
		}
		
		Gson gson = new Gson();
		
		String json = gson.toJson(listVo);
		
		System.out.println(json);
		
		model.addAttribute("listVoModel", json);
		model.addAttribute("disAndLeaveSearchVoModel", disSearchVo);
		model.addAttribute("loginOrgId", org);	//当前登录的 id,动态显示搜索 条件
		
		model.addAttribute("weekDateModel", weekDateList);
		
		return "staffDisAndLeave/staffDisAndLeaveList";
	}
}
