package com.jhj.action.bs;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.github.pagehelper.PageInfo;
import com.jhj.action.BaseController;
import com.jhj.common.ConstantOa;
import com.jhj.common.Constants;
import com.jhj.oa.auth.AccountAuth;
import com.jhj.oa.auth.AuthHelper;
import com.jhj.oa.auth.AuthPassport;
import com.jhj.po.model.bs.OrgStaffLeave;
import com.jhj.po.model.bs.Orgs;
import com.jhj.po.model.order.OrderDispatchs;
import com.jhj.po.model.order.Orders;
import com.jhj.service.bs.OrgStaffLeaveService;
import com.jhj.service.bs.OrgStaffsService;
import com.jhj.service.bs.OrgsService;
import com.jhj.service.order.OrderDispatchsService;
import com.jhj.service.order.OrdersService;
import com.jhj.vo.bs.LeaveStaffVo;
import com.jhj.vo.order.OrderDispatchSearchVo;
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
 *               服务人员 请假
 */
@Controller
@RequestMapping(value = "/newbs")
public class OrgStaffLeaveController extends BaseController {
	
	@Autowired
	private OrdersService orderService;

	@Autowired
	private OrgStaffLeaveService leaveService;

	@Autowired
	private OrgStaffsService staffService;

	@Autowired
	private OrgsService orgService;
	
	@Autowired
	private OrderDispatchsService orderDispatchService;
	
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
		
		if(searchVo.getStaffName()!=null && !searchVo.getStaffName().equals("")){
			String staffName = searchVo.getStaffName();
			try {
				searchVo.setStaffName(new String(staffName.getBytes("ISO-8859-1"),"UTF-8"));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
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
	
	@AuthPassport
	@RequestMapping(value = "/leave_form", method = { RequestMethod.GET })
	public String leavelForm(HttpServletRequest request, Model model, 
			@RequestParam(value = "id") Long id, 
			@RequestParam(value = "errors", required = false, defaultValue = "") String errors) {
				
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
		model.addAttribute("errors", errors);
		return "bs/leaveForm";
	}
}
