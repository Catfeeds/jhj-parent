package com.jhj.action.staff;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jhj.action.BaseController;
import com.jhj.common.ConstantOa;
import com.jhj.oa.auth.AuthHelper;
import com.jhj.oa.auth.AuthPassport;
import com.jhj.po.model.bs.OrgStaffFinance;
import com.jhj.po.model.bs.OrgStaffs;
import com.jhj.po.model.bs.Orgs;
import com.jhj.service.bs.OrgStaffFinanceService;
import com.jhj.service.bs.OrgStaffPayDeptService;
import com.jhj.service.bs.OrgStaffsService;
import com.jhj.service.bs.OrgsService;
import com.jhj.vo.OrgStaffFinanceSearchVo;
import com.jhj.vo.org.GroupSearchVo;
import com.jhj.vo.staff.OrgStaffFinanceVo;
import com.meijia.utils.BeanUtilsExp;
import com.meijia.utils.StringUtil;

@Controller
@RequestMapping(value = "/staff")
public class OrgStaffPayDeptController extends BaseController {
	@Autowired
	private OrgStaffPayDeptService orgStaffPayDeptService;

	@Autowired
	private OrgStaffsService orgStaffsService;

	@Autowired
	private OrgStaffFinanceService orgStaffFinanceService;
	
	@Autowired
	private OrgsService orgService;

	// 归还欠款列表
	@AuthPassport
	@RequestMapping(value = "/staffPayDept-list", method = RequestMethod.GET)
	public String getStaffPayList(Model model, HttpServletRequest request, OrgStaffFinanceSearchVo searchVo) {

		int pageNo = ServletRequestUtils.getIntParameter(request, ConstantOa.PAGE_NO_NAME, ConstantOa.DEFAULT_PAGE_NO);
		int pageSize = ServletRequestUtils.getIntParameter(request, ConstantOa.PAGE_SIZE_NAME, ConstantOa.DEFAULT_PAGE_SIZE);
		// 分页
		PageHelper.startPage(pageNo, pageSize);

		// 判断是否为店长登陆，如果org > 0L ，则为某个店长，否则为运营人员.
		String org = AuthHelper.getSessionLoginOrg(request);

		// 处理查询条件云店--------------------------------开始
		// 1) 如果有查询条件云店org_id，则以查询条件的云店为准
		// 2) 如果没有查询条件，则判断是否为店长，并且只能看店长所在门店下的所有云店.
		String paramOrgId = request.getParameter("orgId");
		List<Long> cloudIdList = new ArrayList<Long>();
		if (!StringUtil.isEmpty(paramOrgId) && !paramOrgId.equals("0")) {
			cloudIdList.add(Long.valueOf(paramOrgId));
		} else {

			if (!org.equals("0") && !StringUtil.isEmpty(org)) {
				GroupSearchVo groupSearchVo = new GroupSearchVo();
				groupSearchVo.setParentId(Long.parseLong(org));

				List<Orgs> cloudList = orgService.selectCloudOrgByParentOrg(groupSearchVo);

				for (Orgs orgs : cloudList) {
					cloudIdList.add(orgs.getOrgId());
				}
			}
		}

		if (!cloudIdList.isEmpty()) {
			searchVo.setSearchCloudOrgIdList(cloudIdList);
		}
		// 处理查询条件云店--------------------------------结束

		List<OrgStaffFinance> orgStaffFinanceList = orgStaffFinanceService.selectByListPage(searchVo, pageNo, pageSize);

		for (int i = 0; i < orgStaffFinanceList.size(); i++) {

			OrgStaffFinanceVo vo = new OrgStaffFinanceVo();

			OrgStaffFinance orgStaffFinance = orgStaffFinanceList.get(i);
			BeanUtilsExp.copyPropertiesIgnoreNull(orgStaffFinance, vo);

			OrgStaffs orgStaffs = orgStaffsService.selectByPrimaryKey(orgStaffFinance.getStaffId());
			vo.setName(orgStaffs.getName());

			orgStaffFinanceList.set(i, vo);
		}

		PageInfo result = new PageInfo(orgStaffFinanceList);

		model.addAttribute("contentModel", result);
		model.addAttribute("orgStaffDetailPaySearchVoModel", searchVo);
		return "staff/staffPayDeptList";
	}

}
