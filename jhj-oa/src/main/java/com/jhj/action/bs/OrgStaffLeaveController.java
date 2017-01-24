package com.jhj.action.bs;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspWriter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.github.pagehelper.PageInfo;
import com.jhj.action.BaseController;
import com.jhj.common.ConstantOa;
import com.jhj.oa.auth.AccountAuth;
import com.jhj.oa.auth.AuthHelper;
import com.jhj.oa.auth.AuthPassport;
import com.jhj.po.model.bs.OrgStaffLeave;
import com.jhj.po.model.bs.OrgStaffs;
import com.jhj.po.model.bs.Orgs;
import com.jhj.service.bs.OrgStaffLeaveService;
import com.jhj.service.bs.OrgStaffsService;
import com.jhj.service.bs.OrgsService;
import com.jhj.vo.bs.LeaveStaffVo;
import com.jhj.vo.org.LeaveSearchVo;
import com.jhj.vo.staff.StaffSearchVo;
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
 *               服务人员 请假
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
	
	@AuthPassport
	@RequestMapping(value = "/leave_list", method = { RequestMethod.GET })
	public String groupList(Model model, HttpServletRequest request, @ModelAttribute("leaveSearchVoModel") LeaveSearchVo searchVo) throws ParseException {

		int pageNo = ServletRequestUtils.getIntParameter(request, ConstantOa.PAGE_NO_NAME, ConstantOa.DEFAULT_PAGE_NO);
		int pageSize = ServletRequestUtils.getIntParameter(request, ConstantOa.PAGE_SIZE_NAME, ConstantOa.DEFAULT_PAGE_SIZE);

		// 得到 当前登录 的 门店id，并作为搜索条件
		Long sessionOrgId = AuthHelper.getSessionLoginOrg(request);

		if (sessionOrgId > 0L) {
			searchVo.setParentId(sessionOrgId);
		}

		if (!StringUtil.isEmpty(searchVo.getLeaveDateStr())) {
			Date leaveDate = DateUtil.parse(searchVo.getLeaveDateStr());
			searchVo.setLeaveDate(leaveDate);
		}
		
		PageInfo pageList = leaveService.selectByListPage(searchVo, pageNo, pageSize);
		List<OrgStaffLeave> list = pageList.getList();

		OrgStaffLeave leave = null;

		for (int i = 0; i < list.size(); i++) {

			leave = list.get(i);

			LeaveStaffVo leaveStaffVo = leaveService.transToVO(leave);

			list.set(i, leaveStaffVo);
		}

		PageInfo result = new PageInfo(list);

		model.addAttribute("leaveModel", result);
		model.addAttribute("leaveSearchVoModel", searchVo);
		model.addAttribute("loginOrgId", sessionOrgId);

		return "bs/leaveList";
	}

	@RequestMapping(value = "/leave_form", method = { RequestMethod.GET })
	public String adForm(Model model, @RequestParam(value = "id") Long id, HttpServletRequest request) {
		Long sessionOrgId = AuthHelper.getSessionLoginOrg(request);

		if (id == null) {
			id = 0L;
		}

		OrgStaffLeave leave = leaveService.initLeave();

		if (id != null && id > 0) {
			leave = leaveService.selectByPrimaryKey(id);
		}
		LeaveStaffVo leaveStaffVo = leaveService.transToVO(leave);
		// 如果是新增,则 下拉列表为 登录 角色的id，所确定的
		model.addAttribute("logInParentOrgId", sessionOrgId);

		// 如果是修改,则下拉列表为 回显的 门店
		model.addAttribute("leaveModel", leaveStaffVo);
		
		return "bs/leaveForm";
	}
	
	@AuthPassport
	@RequestMapping(value = "/leave_form", method = { RequestMethod.POST })
	public String submitLeaveForm(Model model, HttpServletRequest request, @ModelAttribute("leaveModel") LeaveStaffVo leaveVo, BindingResult result,
			@RequestParam("id") Long id) {

		OrgStaffLeave leave = leaveService.initLeave();
		
		if (id > 0L) {
			leave = leaveService.selectByPrimaryKey(id);
			leaveVo.setOrgId(leave.getOrgId());
			leaveVo.setParentId(leave.getParentId());
		}

		BeanUtilsExp.copyPropertiesIgnoreNull(leaveVo, leave);
		
		String leaveDate = request.getParameter("leaveDate");
		String leaveDateEnd = request.getParameter("leaveDateEnd");
		leave.setLeaveDate(DateUtil.parse(leaveDate));
		leave.setLeaveDateEnd(DateUtil.parse(leaveDateEnd));
		long dublDate = TimeStampUtil.compareTimeStr(leave.getLeaveDate().getTime(),leave.getLeaveDateEnd().getTime());
		int dayNum = (int)dublDate/(1000 * 60 * 60 * 24)+1;
		leave.setTotalDays(dayNum);

		// 请假日期
//		leave.setLeaveStatus();
//		Short leaveStatus = leaveVo.getLeaveStatus();

		Short leaveDuration = leaveVo.getLeaveDuration();

		// 默认选择 8~12点
		short start = 8;
		short end = 12;

		if (leaveDuration == (short) 1) {
			// 选择的是 8~21点
			end = 21;
		}

		if (leaveDuration == (short) 2) {
//			 选择的是 12~21点
			start = 12;
			end = 21;
		}

		leave.setStart(start);
		leave.setEnd(end);

		AccountAuth auth = AuthHelper.getSessionAccountAuth(request);

		// 操作人 登录 id
		Long authId = auth.getId();
		leave.setAdminId(authId);

		Long orgId = leaveVo.getOrgId();
		Orgs org = orgService.selectByPrimaryKey(orgId);

		leave.setOrgId(org.getOrgId());

		leave.setParentId(org.getParentId());

		if (id == 0) {
			LeaveSearchVo leaveSearchVo = new LeaveSearchVo();
			leaveSearchVo.setStaffId(leave.getStaffId());
			leaveSearchVo.setLeaveStatus("1");
			List<OrgStaffLeave> staffLeave = leaveService.selectBySearchVo(leaveSearchVo);
			if(staffLeave.size()==0){
				leaveService.insert(leave);
			}
			
		} else {
			leaveService.updateByPrimaryKeySelective(leave);
		}

		return "redirect:leave_list";
	}

}
