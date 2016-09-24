package com.jhj.action.job;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.jhj.action.app.BaseController;
import com.jhj.common.ConstantMsg;
import com.jhj.common.Constants;
import com.jhj.po.model.bs.OrgStaffInvite;
import com.jhj.po.model.bs.OrgStaffs;
import com.jhj.po.model.orderReview.JhjSetting;
import com.jhj.service.bs.OrgStaffInviteService;
import com.jhj.service.bs.OrgStaffsService;
import com.jhj.service.order.OrderQueryService;
import com.jhj.service.order.OrderStatService;
import com.jhj.service.orderReview.SettingService;
import com.jhj.vo.order.OrderQuerySearchVo;
import com.jhj.vo.staff.StaffSearchVo;
import com.meijia.utils.vo.AppResultData;

@Controller
@RequestMapping(value = "/app/job/staff")
public class StaffTimeController extends BaseController {


	@Autowired
	private OrgStaffInviteService orgStaffInviteService;

	@Autowired
	private OrgStaffsService orgStaffsService;
	
	@Autowired
	private SettingService settingService;
	
	@Autowired
	private OrderQueryService orderQueryService;
	
	@Autowired
	private OrderStatService orderStatService;
	/**
	 * 定时任务
	 */

	@RequestMapping(value = "inviteTime", method = RequestMethod.POST)
	public AppResultData<Object> inviteTime(HttpServletRequest request) {

		AppResultData<Object> result = new AppResultData<Object>(
				Constants.SUCCESS_0, ConstantMsg.SUCCESS_0_MSG, new String());
		List<OrgStaffInvite> list = orgStaffInviteService.selectByInviteStaffIdAndStatus();

		String settingType = "invite_days";
		JhjSetting jhjSetting = settingService.selectBySettingType(settingType);
		//天数
		Long settingValue = Long.valueOf(jhjSetting.getSettingValue());
		
		for (int i = 0; i < list.size(); i++) {
			OrgStaffInvite orgStaffInvite = list.get(i);
			
			StaffSearchVo searchVo1 = new StaffSearchVo();
			searchVo1.setMobile(orgStaffInvite.getInviteMobile());
			List<OrgStaffs> staffList = orgStaffsService.selectBySearchVo(searchVo1);
			
			OrgStaffs orgStaff = null;
			if (!staffList.isEmpty()) orgStaff = staffList.get(0);
			if (orgStaff != null) {
				orgStaffInvite.setInviteStatus((short)1);
				orgStaffInviteService.updateByPrimaryKeySelective(orgStaffInvite);
			}
			//开始时间、结束时间
			Long startTime = orgStaffInvite.getAddTime();
			Long endTime = startTime + settingValue * 86400;
			
			OrderQuerySearchVo searchVo = new OrderQuerySearchVo();
			searchVo.setStartTime(startTime);
			searchVo.setEndTime(endTime);
			searchVo.setOrderStatus((short)7);
			searchVo.setStaffId(orgStaffInvite.getInviteStaffId());
			//被邀请的服务人员的订单
			Long count = orderStatService.getTotalOrderCountByMouth(searchVo);
			if (count != null) {
				//把推荐这个用户的id更新，完成多少单更新
				orgStaffInvite.setInviteOrderCount(count.shortValue());
				orgStaffInvite.setInviteStaffId(orgStaff.getStaffId());
				orgStaffInviteService.updateByPrimaryKeySelective(orgStaffInvite);
			}
			
		}
		return result;
	}

}
