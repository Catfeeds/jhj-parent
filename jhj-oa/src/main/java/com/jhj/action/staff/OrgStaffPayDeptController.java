package com.jhj.action.staff;

import java.math.BigDecimal;
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
import com.jhj.vo.org.OrgSearchVo;
import com.jhj.vo.staff.OrgStaffFinanceSearchVo;
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

		// 判断是否为店长登陆，如果org > 0L ，则为某个店长，否则为运营人员.
		Long sessionOrgId = AuthHelper.getSessionLoginOrg(request);
		if (sessionOrgId > 0L){
			searchVo.setParentId(sessionOrgId);
			
		}

		String parentIdParam = request.getParameter("parentId");
		if(parentIdParam!=null && parentIdParam!="" && Long.valueOf(parentIdParam)>0){
			searchVo.setParentId(Long.valueOf(parentIdParam));
		}
		
		String paramOrgId = request.getParameter("orgId");
		if (!StringUtil.isEmpty(paramOrgId) && Long.valueOf(paramOrgId)>0L) {
			searchVo.setOrgId(Long.valueOf(paramOrgId));
		} 
		PageInfo result = orgStaffFinanceService.selectByListPage(searchVo, pageNo, pageSize);
		List<OrgStaffFinance> orgStaffFinanceList = result.getList();

		for (int i = 0; i < orgStaffFinanceList.size(); i++) {

			OrgStaffFinanceVo vo = new OrgStaffFinanceVo();

			OrgStaffFinance orgStaffFinance = orgStaffFinanceList.get(i);
			BeanUtilsExp.copyPropertiesIgnoreNull(orgStaffFinance, vo);
			
			BigDecimal totalIncoming = orgStaffFinance.getTotalIncoming();
			BigDecimal totalCash = orgStaffFinance.getTotalCash();
			BigDecimal totalDept = orgStaffFinance.getTotalDept();
			BigDecimal totalRest = totalIncoming.subtract(totalCash).subtract(totalDept);
			BigDecimal totalCashValid = totalIncoming.subtract(totalCash);
			vo.setRestMoney(totalRest);
			vo.setTotalCashValid(totalCashValid);
			
			OrgStaffs orgStaffs = orgStaffsService.selectByPrimaryKey(orgStaffFinance.getStaffId());
			vo.setName(orgStaffs.getName());

			orgStaffFinanceList.set(i, vo);
		}
		//云店下拉框选项
		List<Orgs> orgList = new ArrayList<Orgs>();
		
		if (sessionOrgId > 0L) {
			//如果登录的是店长
			
			OrgSearchVo searchVo1 = new OrgSearchVo();
			searchVo1.setParentId(sessionOrgId);
			searchVo1.setOrgStatus((short) 1);
			orgList = orgService.selectBySearchVo(searchVo1);
		}else{
			//如果登录的是 运营人员
			OrgSearchVo searchVo2 = new OrgSearchVo();
			searchVo2.setIsCloud((short) 1);
			searchVo2.setOrgStatus((short) 1);
			orgList = orgService.selectBySearchVo(searchVo2);
		}
		
		model.addAttribute("orgList", orgList);

		result = new PageInfo(orgStaffFinanceList);

		model.addAttribute("contentModel", result);
		model.addAttribute("orgStaffDetailPaySearchVoModel", searchVo);
		return "staff/staffPayDeptList";
	}

}
