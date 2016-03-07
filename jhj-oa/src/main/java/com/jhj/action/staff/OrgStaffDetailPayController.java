package com.jhj.action.staff;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
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
import com.jhj.po.model.bs.OrgStaffDetailPay;
import com.jhj.po.model.bs.OrgStaffs;
import com.jhj.service.bs.OrgStaffDetailPayService;
import com.jhj.service.bs.OrgStaffsService;
import com.jhj.vo.OrgStaffDetailPaySearchVo;
import com.jhj.vo.staff.OrgStaffDetailPayOaVo;
import com.jhj.vo.staff.OrgStaffPayVo;
import com.meijia.utils.BeanUtilsExp;
import com.meijia.utils.DateUtil;
import com.meijia.utils.StringUtil;

@Controller
@RequestMapping(value = "/staff")
public class OrgStaffDetailPayController extends BaseController {
	@Autowired
	private OrgStaffDetailPayService orgStaffDetailPayService;
	
	@Autowired
	private OrgStaffsService orgStaffsService;
	/**
	 * 服务人员财务明细
	 * @param model
	 * @param request
	 * @param searchVo
	 * @return
	 * @throws ParseException 
	 */
	@AuthPassport
	@RequestMapping(value = "/staffPay-list", method = RequestMethod.GET)
	public String getStaffPayList(Model model, HttpServletRequest request,
			@RequestParam(value = "mobile", required = false, defaultValue = "") String mobile,
			OrgStaffDetailPaySearchVo searchVo) throws ParseException{
		
		int pageNo = ServletRequestUtils.getIntParameter(request,
				ConstantOa.PAGE_NO_NAME, ConstantOa.DEFAULT_PAGE_NO);
		int pageSize = ServletRequestUtils.getIntParameter(request,
				ConstantOa.PAGE_SIZE_NAME, ConstantOa.DEFAULT_PAGE_SIZE);
		//分页
		PageHelper.startPage(pageNo, pageSize);
		
		if(searchVo == null){
			searchVo  = new OrgStaffDetailPaySearchVo();
		}
		searchVo.setMobile(mobile);
		//在提醒订单，点击搜索
				String startTimeStr = searchVo.getStartTimeStr();
				if(!StringUtil.isEmpty(startTimeStr)){
					searchVo.setStartTime(DateUtil.getUnixTimeStamp(DateUtil.getBeginOfDay(startTimeStr)));
				}
				
				String endTimeStr = searchVo.getEndTimeStr();
				if(!StringUtil.isEmpty(endTimeStr)){
					searchVo.setEndTime(DateUtil.getUnixTimeStamp(DateUtil.getEndOfDay(endTimeStr)));
				}
		
		
        List<OrgStaffDetailPay> orgStaffdetailPayList = orgStaffDetailPayService.selectVoByListPage(searchVo,pageNo,pageSize);
        List<OrgStaffDetailPayOaVo> orgStaffPayVoList = new ArrayList<OrgStaffDetailPayOaVo>();
		for (int i = 0; i < orgStaffdetailPayList.size(); i++) {
			OrgStaffDetailPay orgStaffDetailPay = orgStaffdetailPayList.get(i);
			OrgStaffPayVo vo = orgStaffDetailPayService.getOrgStaffPayVo(orgStaffDetailPay);
			
			OrgStaffDetailPayOaVo oaVo = new OrgStaffDetailPayOaVo();
			
			BeanUtilsExp.copyPropertiesIgnoreNull(orgStaffDetailPay, oaVo);
			
			OrgStaffs orgStaffs = orgStaffsService.selectByPrimaryKey(orgStaffDetailPay.getStaffId());
			
			oaVo.setName(orgStaffs.getName());
			oaVo.setOrderTypeName(vo.getOrderTypeName());
			oaVo.setOrderPay(new BigDecimal(vo.getOrderPay()));
			
			orgStaffdetailPayList.set(i, oaVo);
			//orgStaffPayVoList.add(oaVo);
		}
        PageInfo result = new PageInfo(orgStaffdetailPayList);	
		
		model.addAttribute("contentModel", result);
		model.addAttribute("orgStaffDetailPaySearchVoModel", searchVo);
		return "staff/staffDetailPayList";
	}
	
	/*
	 * 服务人员消费明细表单【
	 * 
	 */
	//@AuthPassport
	/*@RequestMapping(value = "/staffPayForm",method = RequestMethod.GET)
	public String  staffPayForm(Long orderId,Model model){
		
		OrgStaffDetailPay orgStaffDetailPay = orgStaffDetailPayService.selectByPrimaryKey(orderId);
		
		model.addAttribute("contentModel", orgStaffDetailPay);
		
		return "staff/staffDetailPayForm";
	}*/
	/*
	 * 
	 * 修改服务人员消费明细
	 */
	
	//@AuthPassport
	/*@RequestMapping(value = "/staffPayForm",method = RequestMethod.POST)
	public String  orderDetail(@ModelAttribute("orgStaffDetailPay") OrgStaffDetailPay orgStaffDetailPay){
		
	
		orgStaffDetailPay.setAddTime(TimeStampUtil.getNowSecond());
		orgStaffDetailPayService.updateByPrimaryKeySelective(orgStaffDetailPay);
		
		return "redirect:staffPay-list";
	}*/
	
}
