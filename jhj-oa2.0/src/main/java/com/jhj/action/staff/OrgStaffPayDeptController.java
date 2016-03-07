package com.jhj.action.staff;

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
import com.jhj.oa.auth.AuthPassport;
import com.jhj.po.model.bs.OrgStaffFinance;
import com.jhj.po.model.bs.OrgStaffs;
import com.jhj.service.bs.OrgStaffFinanceService;
import com.jhj.service.bs.OrgStaffPayDeptService;
import com.jhj.service.bs.OrgStaffsService;
import com.jhj.vo.OrgStaffFinanceSearchVo;
import com.jhj.vo.staff.OrgStaffFinanceVo;
import com.meijia.utils.BeanUtilsExp;

@Controller
@RequestMapping(value = "/staff")
public class OrgStaffPayDeptController extends BaseController {
	@Autowired
	private OrgStaffPayDeptService orgStaffPayDeptService;
	
	@Autowired
	private OrgStaffsService orgStaffsService;
	
	@Autowired
	private OrgStaffFinanceService orgStaffFinanceService;
	
	//归还欠款列表
	@AuthPassport
	@RequestMapping(value = "/staffPayDept-list", method = RequestMethod.GET)
	public String getStaffPayList(Model model, HttpServletRequest request,
			OrgStaffFinanceSearchVo searchVo){
		
		int pageNo = ServletRequestUtils.getIntParameter(request,
				ConstantOa.PAGE_NO_NAME, ConstantOa.DEFAULT_PAGE_NO);
		int pageSize = ServletRequestUtils.getIntParameter(request,
				ConstantOa.PAGE_SIZE_NAME, ConstantOa.DEFAULT_PAGE_SIZE);
		//分页
		PageHelper.startPage(pageNo, pageSize);
		
    //    List<OrgStaffPayDept> orgStaffPayDeptList = orgStaffPayDeptService.selectByListPage(searchVo,pageNo,pageSize);
        List<OrgStaffFinance> orgStaffFinanceList = orgStaffFinanceService.selectByListPage(searchVo,pageNo,pageSize);
        OrgStaffFinanceVo vo = new OrgStaffFinanceVo();
        for (int i = 0; i < orgStaffFinanceList.size(); i++) {
        	OrgStaffFinance orgStaffFinance = orgStaffFinanceList.get(i);
        	BeanUtilsExp.copyPropertiesIgnoreNull(orgStaffFinance, vo);
        	OrgStaffs orgStaffs = orgStaffsService.selectByPrimaryKey(orgStaffFinance.getStaffId());
        	vo.setName(orgStaffs.getName());
        	orgStaffFinanceList.set(i, vo);
		}
        
        PageInfo result = new PageInfo(orgStaffFinanceList);	
		
		model.addAttribute("contentModel", result);
		//model.addAttribute("orgStaffDetailPaySearchVoModel", searchVo);
		return "staff/staffPayDeptList";
	}
	
}
