package com.jhj.action.staff;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jhj.action.BaseController;
import com.jhj.common.ConstantOa;
import com.jhj.oa.auth.AuthPassport;
import com.jhj.po.model.bs.OrgStaffInvite;
import com.jhj.po.model.bs.OrgStaffs;
import com.jhj.po.model.orderReview.JhjSetting;
import com.jhj.service.bs.OrgStaffBlackService;
import com.jhj.service.bs.OrgStaffInviteService;
import com.jhj.service.bs.OrgStaffsService;
import com.jhj.service.orderReview.SettingService;
import com.jhj.vo.staff.OrgStaffFinanceSearchVo;
import com.jhj.vo.staff.OrgStaffInviteVo;
import com.meijia.utils.BeanUtilsExp;

@Controller
@RequestMapping(value = "/staff")
public class OrgStaffInviteController extends BaseController {
	@Autowired
	private OrgStaffInviteService orgStaffInviteService;
	
	@Autowired
	private OrgStaffsService orgStaffsService;
	
	@Autowired
	private OrgStaffBlackService orgStaffBlackService;
	
	@Autowired
	private SettingService settingService;
	
	//服务人员邀请列表   
	@AuthPassport
	@RequestMapping(value = "/invite-list", method = RequestMethod.GET)
	public String getStaffPayList(Model model, HttpServletRequest request,OrgStaffFinanceSearchVo searvhVo){
		
		int pageNo = ServletRequestUtils.getIntParameter(request,
				ConstantOa.PAGE_NO_NAME, ConstantOa.DEFAULT_PAGE_NO);
		int pageSize = ServletRequestUtils.getIntParameter(request,
				ConstantOa.PAGE_SIZE_NAME, ConstantOa.DEFAULT_PAGE_SIZE);
		//分页
		PageHelper.startPage(pageNo, pageSize);
		
		List<OrgStaffInvite> list = orgStaffInviteService.selectByListPage(searvhVo,pageNo,pageSize); 
		OrgStaffInviteVo vo = new OrgStaffInviteVo();
        for (int i = 0; i < list.size(); i++) {
        	OrgStaffInvite orgStaffInvite = list.get(i);
        	BeanUtilsExp.copyPropertiesIgnoreNull(orgStaffInvite, vo);
        	OrgStaffs orgStaffs = orgStaffsService.selectByPrimaryKey(orgStaffInvite.getStaffId());
        	vo.setName(orgStaffs.getName());
        	vo.setMobile(orgStaffs.getMobile());
        	String settingType = "invite_order_count";
        	JhjSetting jhjSetting = settingService.selectBySettingType(settingType);
    		String settingValue = jhjSetting.getSettingValue();
    		Short settingValueShort = new Short(settingValue);
        	vo.setSettingValueShort(settingValueShort);
        	list.set(i, vo);
		}
        PageInfo result = new PageInfo(list);	
		model.addAttribute("contentModel", result);
		return "staff/staffInviteList";
	}
	//操作按钮（点击操作按钮更改状态）
	@RequestMapping(value = "/invite-button",method = RequestMethod.GET)
	public String  orderDetail(
			@RequestParam(value = "id") Long id){
		OrgStaffInvite orgStaffInvite = orgStaffInviteService.selectByPrimaryKey(id);
		String settingType = "invite_order_count";
		JhjSetting jhjSetting = settingService.selectBySettingType(settingType);
		String settingValue = jhjSetting.getSettingValue();
		Short settingValueShort = new Short(settingValue);
		if (orgStaffInvite != null && jhjSetting != null) {
			if (orgStaffInvite.getInviteStaffId() > 0 &&
			    orgStaffInvite.getInviteOrderCount() >= settingValueShort) {
				orgStaffInvite.setInviteStatus((short)1);	
				orgStaffInviteService.updateByPrimaryKeySelective(orgStaffInvite);
			}
		}
		return "redirect:invite-list";
	}
}
