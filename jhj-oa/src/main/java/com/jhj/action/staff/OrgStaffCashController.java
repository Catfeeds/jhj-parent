package com.jhj.action.staff;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.github.pagehelper.PageInfo;
import com.jhj.action.BaseController;
import com.jhj.common.ConstantOa;
import com.jhj.common.Constants;
import com.jhj.oa.auth.AuthPassport;
import com.jhj.po.model.bs.OrgStaffCash;
import com.jhj.po.model.bs.OrgStaffDetailPay;
import com.jhj.po.model.bs.OrgStaffFinance;
import com.jhj.po.model.bs.OrgStaffs;
import com.jhj.po.model.user.UserPushBind;
import com.jhj.service.bs.OrgStaffCashService;
import com.jhj.service.bs.OrgStaffDetailPayService;
import com.jhj.service.bs.OrgStaffFinanceService;
import com.jhj.service.bs.OrgStaffsService;
import com.jhj.service.users.UserPushBindService;
import com.jhj.vo.staff.OrgStaffCashSearchVo;
import com.jhj.vo.staff.OrgStaffCashVo;
import com.jhj.vo.user.UserPushBindSearchVo;
import com.meijia.utils.GsonUtil;
import com.meijia.utils.MathBigDecimalUtil;
import com.meijia.utils.PushUtil;
import com.meijia.utils.SmsUtil;
import com.meijia.utils.TimeStampUtil;

/**
 *
 * @author :hulj
 * @Date : 2015年8月10日下午6:12:56
 * @Description: 
 *		
 *		运营平台--订单管理模块
 */
@Controller
@RequestMapping(value = "/staff")
public class OrgStaffCashController extends BaseController {
	@Autowired
	private OrgStaffCashService orgStaffCashService;
		
	@Autowired
	private UserPushBindService bindService;
	
	@Autowired
	private OrgStaffsService orgStaffsService;
	
	@Autowired
	private OrgStaffDetailPayService orgStaffDetailPayService;

	@Autowired
	private OrgStaffFinanceService orgStaffFinanceService;	
	
	//提现列表
	@AuthPassport
	@RequestMapping(value = "/cash-list", method = RequestMethod.GET)
	public String getOrderList(Model model, HttpServletRequest request, OrgStaffCashSearchVo searchVo){
		
		int pageNo = ServletRequestUtils.getIntParameter(request,
				ConstantOa.PAGE_NO_NAME, ConstantOa.DEFAULT_PAGE_NO);
		int pageSize = ServletRequestUtils.getIntParameter(request,
				ConstantOa.PAGE_SIZE_NAME, ConstantOa.DEFAULT_PAGE_SIZE);
		//分页
//		PageHelper.startPage(pageNo, pageSize);
		
		if(searchVo == null){
			searchVo  = new OrgStaffCashSearchVo();
		}
		
		PageInfo result = orgStaffCashService.selectByListPage(searchVo,pageNo,pageSize);
        List<OrgStaffCash> staffCashlist = result.getList();
        OrgStaffCash orgStaffCash=null;
        for(int i=0;i<staffCashlist.size();i++){
        	orgStaffCash = staffCashlist.get(i);
        	OrgStaffCashVo vo = orgStaffCashService.transVo(orgStaffCash);
        	staffCashlist.set(i,vo);
        }
        result = new PageInfo(staffCashlist);
		model.addAttribute("contentModel", result);
		model.addAttribute("oaOrderSearchVoModel",searchVo);
		
		return "staff/staffCashList";
	}
	
	/*
	 * 提现表单
	 * 
	 * 
	 * 	
	 */
//	@AuthPassport
	@RequestMapping(value = "/cash-form",method = RequestMethod.GET)
	public String  orderDetail(Long orderId,Model model){
		
		OrgStaffCash orgStaffCash = orgStaffCashService.selectByPrimaryKey(orderId);
		
		//总提现金额
	//	BigDecimal totalCashMoney = orgStaffCashService.getTotalCashMoney(account);

		model.addAttribute("contentModel", orgStaffCash);
		
		return "staff/staffCashForm";
	}
	/*
	 * 
	 * 修改状态
	 */
	
	//@AuthPassport
	@RequestMapping(value = "/cash-form",method = RequestMethod.POST)
	public String  orderDetail(@ModelAttribute("orgStaffCash") OrgStaffCash orgStaffCash) throws Exception{
		
		orgStaffCash.setUpdateTime(TimeStampUtil.getNowSecond());
		orgStaffCashService.updateByPrimaryKeySelective(orgStaffCash);
		
		orgStaffCash = orgStaffCashService.selectByPrimaryKey(orgStaffCash.getOrderId());
		
		Long staffId = orgStaffCash.getStaffId();
		OrgStaffs orgstaff = orgStaffsService.selectByPrimaryKey(staffId);
		
		//提现已打款，则需要在财务表操作
		if (orgStaffCash.getOrderStatus().equals((short)3)) {
			//写入员工的用户明细，
			// 操作服务人员财务明细表 org_staff_detail_pay，插入一条 order_type = 5 提现 的记录
			BigDecimal orderMoney = orgStaffCash.getOrderMoney();
			String orderMoneyStr = MathBigDecimalUtil.round2(orderMoney);
			
			OrgStaffDetailPay orgStaffDetailPay = orgStaffDetailPayService.initStaffDetailPay();
			orgStaffDetailPay.setStaffId(staffId);
			orgStaffDetailPay.setMobile(orgstaff.getMobile());
			orgStaffDetailPay.setOrderType(Constants.ORDER_TYPE_5);
			orgStaffDetailPay.setOrderId(orgStaffCash.getOrderId());
			orgStaffDetailPay.setOrderNo(orgStaffCash.getOrderNo());
			orgStaffDetailPay.setOrderMoney(orderMoney);
			orgStaffDetailPay.setOrderPay(orderMoney);
			orgStaffDetailPay.setOrderStatusStr("提现已打款");
			orgStaffDetailPayService.insert(orgStaffDetailPay);
			
			//更新服务人员总财务表
			OrgStaffFinance orgStaffFinance = orgStaffFinanceService.selectByStaffId(staffId);
			
			orgStaffFinance.setTotalCash(orgStaffFinance.getTotalCash().add(orgStaffCash.getOrderMoney()));
			orgStaffFinance.setUpdateTime(TimeStampUtil.getNowSecond());
			orgStaffFinanceService.updateByPrimaryKey(orgStaffFinance);
			
			//发送短信
			String addTimeStr = TimeStampUtil.timeStampToDateStr(orgStaffCash.getAddTime() * 1000, "yyyy-MM-dd HH:mm");
			String[] content = new String[] { addTimeStr };
			SmsUtil.SendSms(orgstaff.getMobile(), "65012", content);
		}
		
		
		return "redirect:cash-list";
	}
	
}
