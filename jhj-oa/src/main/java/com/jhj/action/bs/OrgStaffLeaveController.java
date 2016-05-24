package com.jhj.action.bs;

import java.text.ParseException;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jhj.action.BaseController;
import com.jhj.common.ConstantOa;
import com.jhj.oa.auth.AccountAuth;
import com.jhj.oa.auth.AuthHelper;
import com.jhj.po.model.bs.OrgStaffLeave;
import com.jhj.po.model.bs.OrgStaffs;
import com.jhj.po.model.bs.Orgs;
import com.jhj.po.model.order.Orders;
import com.jhj.service.bs.OrgStaffLeaveService;
import com.jhj.service.bs.OrgStaffsService;
import com.jhj.service.bs.OrgsService;
import com.jhj.vo.bs.LeaveStaffVo;
import com.jhj.vo.order.OaOrderListNewVo;
import com.jhj.vo.org.GroupSearchVo;
import com.jhj.vo.org.LeaveSearchVo;
import com.meijia.utils.BeanUtilsExp;
import com.meijia.utils.DateUtil;
import com.meijia.utils.StringUtil;
import com.meijia.utils.TimeStampUtil;

/**
 *
 * @author :hulj
 * @Date : 2016年5月12日上午11:39:05
 * @Description: 
 *		
 *		服务人员 请假
 */
@Controller
@RequestMapping(value = "/newbs")
public class OrgStaffLeaveController extends BaseController {
	
	@Autowired
	private OrgStaffLeaveService leaveService;
	
	@Autowired
	private OrgStaffsService staffService;
	
	@Autowired
	private OrgsService orgService;
	
	
	@RequestMapping(value = "/leave_list", method = {RequestMethod.GET})
	public String groupList(Model model, HttpServletRequest request, 
			@ModelAttribute("leaveSearchVoModel") LeaveSearchVo searchVo) throws ParseException{
		
		int pageNo = ServletRequestUtils.getIntParameter(request,
				ConstantOa.PAGE_NO_NAME, ConstantOa.DEFAULT_PAGE_NO);
		int pageSize = ServletRequestUtils.getIntParameter(request,
				ConstantOa.PAGE_SIZE_NAME, ConstantOa.DEFAULT_PAGE_SIZE);
		
		//得到 当前登录 的 门店id，并作为搜索条件
		String org = AuthHelper.getSessionLoginOrg(request);
		
		if(!StringUtil.isEmpty(org) && !org.equals("0")){
			
			searchVo.setCloudOrgId(Long.parseLong(org));
		}
		
		//转换为数据库 参数字段
		
		//请假时间
		String startTimeStr = searchVo.getAddTimeStr();
		if(!StringUtil.isEmpty(startTimeStr)){
			searchVo.setAddStartTime(DateUtil.getUnixTimeStamp(DateUtil.getBeginOfDay(startTimeStr)));
			searchVo.setAddEndTime(DateUtil.getUnixTimeStamp(DateUtil.getEndOfDay(startTimeStr)));
		}
		
		//假期时间
		String leaveStartTimeStr = searchVo.getLeaveTimeStr();
		if(!StringUtil.isEmpty(leaveStartTimeStr)){
			searchVo.setLeaveTimeStamp(DateUtil.getUnixTimeStamp(DateUtil.getBeginOfDay(leaveStartTimeStr)));
		}
		
		
		//员工 手机号
		String mobile = searchVo.getStaffMobile();
		OrgStaffs staffs = staffService.selectByMobile(mobile);
		if(staffs != null){
			searchVo.setStaffId(staffs.getStaffId());
		}
		
		List<OrgStaffLeave> list = leaveService.selectByListPage(searchVo,pageNo,pageSize);
		
		OrgStaffLeave leave = null;
		
		for (int i = 0; i < list.size(); i++) {
			
			leave = list.get(i);
			
			LeaveStaffVo leaveStaffVo = leaveService.transToVO(leave);
			
			list.set(i, leaveStaffVo);
		}
		
		PageInfo result = new PageInfo(list);	
		
		model.addAttribute("leaveModel", result);
		model.addAttribute("leaveSearchVoModel", searchVo);
		
		return "bs/leaveList";
	}
	
	
	@RequestMapping(value = "/leave_form", method = { RequestMethod.GET })
	public String adForm(Model model,
			@RequestParam(value = "id") Long id,
			HttpServletRequest request) {
		String org = AuthHelper.getSessionLoginOrg(request);
		
		if (id == null) {
			id = 0L; 
		}
		
		OrgStaffLeave leave = leaveService.initLeave();
		
		if(id !=null && id > 0){
			leave = leaveService.selectByPrimaryKey(id);
		}
		LeaveStaffVo leaveStaffVo = leaveService.transToVO(leave);
		//如果是新增,则 下拉列表为 登录 角色的id，所确定的
		model.addAttribute("logInParentOrgId", Long.parseLong(org));
		
		//如果是修改,则下拉列表为  回显的 门店
		model.addAttribute("leaveModel", leaveStaffVo);
		
		return "bs/leaveForm";
	}
	
	
	
	@RequestMapping(value = "/leave_form", method = { RequestMethod.POST })
	public String submitLeaveForm(Model model,HttpServletRequest request,
			@ModelAttribute("leaveModel")LeaveStaffVo leaveVo,BindingResult result,
			@RequestParam("id")Long id) {
		
		OrgStaffLeave leave = leaveService.initLeave();
		
		if(id > 0L){
			leave = leaveService.selectByPrimaryKey(id);
		}
		
		BeanUtilsExp.copyPropertiesIgnoreNull(leaveVo, leave);
		
		String leaveDate = request.getParameter("leaveDate");
		
		//请假日期
		leave.setLeaveDate(DateUtil.parse(leaveDate));
		
		Short leaveDuration = leaveVo.getLeaveDuration();
		
		//默认选择 8~12点
		short start = 8;
		short end = 12;
		
		if(leaveDuration == (short)1){
			//选择的是 8~21点
			end = 21;
		}
		
		if(leaveDuration == (short)2){
			//选择的是 12~21点
			start = 12;
			end = 21;
		}
		
		leave.setStart(start);
		leave.setEnd(end);
		
		AccountAuth auth = AuthHelper.getSessionAccountAuth(request);
		
		//操作人 登录 id
		Long authId = auth.getId();
		leave.setAdminId(authId);
		
		Long orgId = leaveVo.getOrgId();
		Orgs org = orgService.selectOrgByCloudOrg(orgId);
		
		leave.setOrgId(org.getOrgId());
		
		leave.setParentId(org.getParentId());
		
		if(id == 0){
			
			leaveService.insert(leave);
		}else{
			leaveService.updateByPrimaryKeySelective(leave);
		}
		
		
		return "redirect:leave_list";
	}
	
}
