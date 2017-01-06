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
import com.jhj.po.model.bs.OrgStaffBlack;
import com.jhj.po.model.bs.OrgStaffFinance;
import com.jhj.po.model.bs.OrgStaffs;
import com.jhj.service.bs.OrgStaffBlackService;
import com.jhj.service.bs.OrgStaffFinanceService;
import com.jhj.service.bs.OrgStaffsService;
import com.jhj.vo.order.OrderSearchVo;
import com.jhj.vo.staff.OrgStaffBlackVo;
import com.jhj.vo.staff.OrgStaffFinanceSearchVo;
import com.jhj.vo.staff.OrgStaffFinanceVo;
import com.meijia.utils.BeanUtilsExp;

@Controller
@RequestMapping(value = "/staff")
public class OrgStaffBlackController extends BaseController {
	@Autowired
	private OrgStaffBlackService orgStaffBlackService;

	@Autowired
	private OrgStaffsService orgStaffsService;

	@Autowired
	private OrgStaffFinanceService orgStaffFinanceService;

	// 黑名单列表
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@AuthPassport
	@RequestMapping(value = "/staffBlack-list", method = RequestMethod.GET)
	public String blackList(Model model, HttpServletRequest request,
			OrgStaffFinanceSearchVo searchVo){
		
		int pageNo = ServletRequestUtils.getIntParameter(request,
				ConstantOa.PAGE_NO_NAME, ConstantOa.DEFAULT_PAGE_NO);
		int pageSize = ServletRequestUtils.getIntParameter(request,
				ConstantOa.PAGE_SIZE_NAME, ConstantOa.DEFAULT_PAGE_SIZE);
		//分页
//		PageHelper.startPage(pageNo, pageSize);
		
		if (searchVo == null)
			searchVo = new OrgStaffFinanceSearchVo();
		
		searchVo.setIsBlack((short) 1);
		
		PageInfo result = orgStaffFinanceService.selectByListPage(searchVo,pageNo,pageSize);
		List<OrgStaffFinance> orgStaffBlackList = result.getList();
		
        for (int i = 0; i < orgStaffBlackList.size(); i++) {
        	OrgStaffFinance orgStaffBlack = orgStaffBlackList.get(i);
        	
        	OrgStaffFinanceVo vo = new OrgStaffFinanceVo();
        	
        	BeanUtilsExp.copyPropertiesIgnoreNull(orgStaffBlack, vo);
        	OrgStaffs orgStaffs = orgStaffsService.selectByPrimaryKey(orgStaffBlack.getStaffId());
        	vo.setName(orgStaffs.getName());
        	orgStaffBlackList.set(i, vo);
		}
        
        result = new PageInfo(orgStaffBlackList);	
		
		model.addAttribute("contentModel", result);
		return "staff/staffBlackList";
	}

	// 黑名单日志列表
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@AuthPassport
	@RequestMapping(value = "/black-log", method = RequestMethod.GET)
	public String blackLog(Model model, HttpServletRequest request, OrderSearchVo searchVo) {

		int pageNo = ServletRequestUtils.getIntParameter(request, ConstantOa.PAGE_NO_NAME, ConstantOa.DEFAULT_PAGE_NO);
		int pageSize = ServletRequestUtils.getIntParameter(request, ConstantOa.PAGE_SIZE_NAME, ConstantOa.DEFAULT_PAGE_SIZE);
		// 分页
		PageHelper.startPage(pageNo, pageSize);

		List<OrgStaffBlack> orgStaffBlackList = orgStaffBlackService.selectByListPage(searchVo, pageNo, pageSize);
		OrgStaffBlackVo vo = new OrgStaffBlackVo();
		for (int i = 0; i < orgStaffBlackList.size(); i++) {
			OrgStaffBlack orgStaffBlack = orgStaffBlackList.get(i);
			BeanUtilsExp.copyPropertiesIgnoreNull(orgStaffBlack, vo);
			OrgStaffs orgStaffs = orgStaffsService.selectByPrimaryKey(orgStaffBlack.getStaffId());
			vo.setName(orgStaffs.getName());
			orgStaffBlackList.set(i, vo);
		}

		PageInfo result = new PageInfo(orgStaffBlackList);

		model.addAttribute("contentModel", result);
		// model.addAttribute("orgStaffDetailPaySearchVoModel", searchVo);
		return "staff/staffBlackLog";
	}

	/*
	 * 服务人员消费明细表单
	 */
	// @AuthPassport
	/*
	 * @RequestMapping(value = "/staffBlackForm",method = RequestMethod.GET)
	 * public String staffBlackForm(Long id,Model model){
	 * 
	 * OrgStaffBlack orgStaffBlack = orgStaffBlackService.initOrgStaffBlack();
	 * 
	 * model.addAttribute("contentModel", orgStaffBlack);
	 * 
	 * return "staff/staffBlackForm";
	 * }
	 */
	/*
	 * 
	 * 修改服务人员消费明细
	 */

	// @AuthPassport
	/*
	 * @RequestMapping(value = "/staffBlackForm",method = RequestMethod.POST)
	 * public String staffBlackForm(@ModelAttribute("orgStaffBlack")
	 * OrgStaffBlack orgStaffBlack){
	 * //后台输入手机号后，进入各接口查询服务人员是否存在，不存在提示，存在继续修改
	 * OrgStaffBlack
	 * orgStaffBlack.setAddTime(TimeStampUtil.getNowSecond());
	 * orgStaffBlackService.updateByPrimaryKeySelective(orgStaffBlack);
	 * 
	 * return "redirect:staffBlack-list";
	 * }
	 */

}
