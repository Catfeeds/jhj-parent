package com.jhj.action.staff;

import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.github.pagehelper.PageInfo;
import com.jhj.action.admin.AdminController;
import com.jhj.common.ConstantOa;
import com.jhj.oa.auth.AuthHelper;
import com.jhj.oa.auth.AuthPassport;
import com.jhj.po.model.bs.OrgStaffs;
import com.jhj.po.model.user.UserTrailHistory;
import com.jhj.po.model.user.UserTrailReal;
import com.jhj.service.bs.OrgStaffsService;
import com.jhj.service.bs.OrgsService;
import com.jhj.service.users.UserTrailHistoryService;
import com.jhj.service.users.UserTrailRealService;
import com.jhj.vo.staff.OrgStaffPoiListVo;
import com.jhj.vo.staff.StaffSearchVo;
import com.jhj.vo.user.UserTrailHistoryVo;
import com.jhj.vo.user.UserTrailSearchVo;
import com.meijia.utils.BeanUtilsExp;
import com.meijia.utils.DateUtil;
import com.meijia.utils.StringUtil;
import com.meijia.utils.TimeStampUtil;

/**
 *
 * @author :hulj
 * @Date : 2016年3月9日上午11:39:07
 * @Description:
 *
 *               jhj2.1 服务人员管理 ： 不再区分助理和服务人员, 统一管理为服务人员
 */
@Controller
@RequestMapping(value = "/staff")
public class OrgStaffPoiController extends AdminController {

	@Autowired
	private OrgStaffsService staffService;

	@Autowired
	private OrgsService orgService;

	@Autowired
	private UserTrailRealService userTrailRealService;

	@Autowired
	private UserTrailHistoryService userTrailHistoryService;

	/**
	 * @throws UnsupportedEncodingException
	 * @throws UnsupportedEncodingException
	 * @Title: newStaffList
	 * @Description:
	 *               服务人员列表页
	 * @param staffSearchVo
	 * @throws
	 */
	@AuthPassport
	@RequestMapping(value = "poi-list", method = { RequestMethod.GET, RequestMethod.POST })
	public String poiList(Model model, HttpServletRequest request, @RequestParam(value = "orgId", required = false, defaultValue = "0") Long orgId,
			@ModelAttribute("staffSearchVoModel") StaffSearchVo staffSearchVo) throws UnsupportedEncodingException {

		int pageNo = ServletRequestUtils.getIntParameter(request, ConstantOa.PAGE_NO_NAME, ConstantOa.DEFAULT_PAGE_NO);
		int pageSize = ServletRequestUtils.getIntParameter(request, ConstantOa.PAGE_SIZE_NAME, ConstantOa.DEFAULT_PAGE_SIZE);

		// 得到 当前登录 的 门店id，并作为搜索条件
		Long sessionOrgId = AuthHelper.getSessionLoginOrg(request);

		if (sessionOrgId > 0L) {
			// 未选择 门店， 且 当前 登录 用户 为 店长 （ session中的 orgId 不为 0）,设置搜索条件为 店长的门店
			staffSearchVo.setParentId(sessionOrgId);
		}
		if (staffSearchVo.getName() != null) {
			String name = staffSearchVo.getName();
			staffSearchVo.setName(new String(name.getBytes("ISO-8859-1"), "UTF-8"));
		}

		if (staffSearchVo.getStatus() == null)
			staffSearchVo.setStatus(1);

		PageInfo infoList = userTrailRealService.selectByStaffListPage(staffSearchVo, pageNo, pageSize);
		List<UserTrailReal> list = infoList.getList();
		UserTrailReal item = null;
		for (int i = 0; i < list.size(); i++) {
			item = list.get(i);
			OrgStaffPoiListVo vo = userTrailRealService.getOrgStaffPoiListVo(item);

			list.set(i, vo);
		}
		PageInfo result = new PageInfo(list);

		model.addAttribute("loginOrgId", sessionOrgId); // 当前登录的 id,动态显示搜索 条件
		model.addAttribute("staffModel", result);
		model.addAttribute("staffSearchVoModel", staffSearchVo);

		return "staff/staffPoiList";
	}

	@AuthPassport
	@RequestMapping(value = "poi-list-detail", method = { RequestMethod.GET, RequestMethod.GET })
	public String poiListDetail(Model model, HttpServletRequest request, 
			@RequestParam(value = "staffId") Long staffId,
			@RequestParam(value = "serviceDateStr",required =false,defaultValue = "") String serviceDateStr) throws UnsupportedEncodingException {

		int pageNo = ServletRequestUtils.getIntParameter(request, ConstantOa.PAGE_NO_NAME, ConstantOa.DEFAULT_PAGE_NO);
		int pageSize = ServletRequestUtils.getIntParameter(request, ConstantOa.PAGE_SIZE_NAME, ConstantOa.DEFAULT_PAGE_SIZE);
		
		UserTrailSearchVo searchVo = new UserTrailSearchVo();
		searchVo.setUserType((short) 0);
		searchVo.setUserId(staffId);
		
		if (!StringUtil.isEmpty(serviceDateStr)) {
			Long startTime = TimeStampUtil.getMillisOfDayFull(serviceDateStr + " 00:00:00");
			Long endTime = TimeStampUtil.getMillisOfDayFull(serviceDateStr + " 23:59:59");
			searchVo.setStartTime(startTime / 1000);
			searchVo.setEndTime(endTime / 1000);
		}
		
		PageInfo page = userTrailHistoryService.selectByListPage(searchVo, pageNo, pageSize);
		OrgStaffs orgStaff = staffService.selectByPrimaryKey(staffId);
		List<UserTrailHistory> list = page.getList();
		for (int i = 0; i < list.size(); i++) {
			UserTrailHistory item = list.get(i);
			UserTrailHistoryVo vo = new UserTrailHistoryVo();
			BeanUtilsExp.copyPropertiesIgnoreNull(item, vo);
			vo.setName(orgStaff.getName());
			vo.setMobile(orgStaff.getMobile());
			Long addTime = vo.getAddTime();
			String addTimeStr = TimeStampUtil.timeStampToDateStr(addTime * 1000, DateUtil.DEFAULT_FULL_PATTERN);
			vo.setAddTimeStr(addTimeStr);
			list.set(i, vo);
		}
		PageInfo result = new PageInfo(list);
		model.addAttribute("contentModel", result);
		model.addAttribute("staffId", staffId);
		return "staff/staffPoiListDetail";
	}
}
