package com.jhj.action.app.staff;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.jhj.action.app.BaseController;
import com.jhj.common.ConstantMsg;
import com.jhj.common.Constants;
import com.jhj.po.model.bs.OrgStaffCash;
import com.jhj.po.model.bs.OrgStaffFinance;
import com.jhj.po.model.bs.OrgStaffs;
import com.jhj.service.bs.OrgStaffCashService;
import com.jhj.service.bs.OrgStaffFinanceService;
import com.jhj.service.bs.OrgStaffsService;
import com.jhj.service.users.UsersService;
import com.jhj.vo.staff.OrgStaffCashVo;
import com.meijia.utils.vo.AppResultData;
import com.meijia.utils.BeanUtilsExp;
import com.meijia.utils.DateUtil;
import com.meijia.utils.MathBigDecimalUtil;
import com.meijia.utils.OrderNoUtil;
import com.meijia.utils.SmsUtil;

@Controller
@RequestMapping(value = "/app/staff")
public class OrgStaffCashController extends BaseController {

	@Autowired
	private OrgStaffFinanceService ogStaffFinanceService;
	
	@Autowired
	private OrgStaffCashService orgStaffCashService;
	
	@Autowired
	private UsersService usersService;
	
	@Autowired
	private OrgStaffsService orgStaffsService;

	//提现记录接口
	@RequestMapping(value = "post_cash", method = RequestMethod.GET)
	public AppResultData<Object> CachPost(
			@RequestParam("user_id") Long userId,
			@RequestParam("cash_money") BigDecimal cashMoney,
			@RequestParam("account") String account) {

		AppResultData<Object> result = new AppResultData<Object>(
				Constants.SUCCESS_0, ConstantMsg.SUCCESS_0_MSG, "");
		
		OrgStaffFinance orgStaffFinance = ogStaffFinanceService.selectByStaffId(userId);
		
		if (orgStaffFinance.getTotalDept().compareTo(BigDecimal.ZERO)>0) {
			result = new AppResultData<Object>(Constants.ERROR_999,
					ConstantMsg.TOTALDEPT_NO_NULL, "");
			return result;
		}
		//总提现金额
		BigDecimal totalCashMoney = orgStaffCashService.getTotalCashMoney(userId);
		//总收入-总欠款-总提现金额的钱数来比较
		BigDecimal money = orgStaffFinance.getTotalIncoming().subtract(orgStaffFinance.getTotalCash()).subtract(totalCashMoney);
		
		if (cashMoney.compareTo(money)==1) {
			
			result = new AppResultData<Object>(Constants.ERROR_999,
					ConstantMsg.RESTMONEY_NO_MEET, "");
			return result;
			
		}
		
		OrgStaffs orgstaff = orgStaffsService.selectByPrimaryKey(userId);
		
		OrgStaffCash orgStaffCash = orgStaffCashService.initOrgStaffCash();
		//1.调用公共订单号类，生成唯一订单号
		String orderNo = String.valueOf(OrderNoUtil.genOrderNo());
		orgStaffCash.setOrderNo(orderNo);
		orgStaffCash.setStaffId(userId);
		orgStaffCash.setMobile(orgstaff.getMobile());
		orgStaffCash.setOrderMoney(cashMoney);
		orgStaffCash.setAccount(account);
		orgStaffCashService.insert(orgStaffCash);
		
		//申请提现短信发送
		
		String deptStr = MathBigDecimalUtil.round2(orgStaffFinance.getTotalDept());
		String timeStr = DateUtil.getNow("HH:mm");
		String[] content = new String[] { timeStr, "7"};
		HashMap<String, String> sendSmsResult = SmsUtil.SendSms(orgstaff.getMobile(), Constants.STAFF_CASE_REQ, content);
		
		
		return result;
	}
	//获得提现记录接口
	
	@RequestMapping(value = "get_cash", method = RequestMethod.GET)
	public AppResultData<Object> CachListPost(
			@RequestParam("user_id") Long userId) {

		AppResultData<Object> result = new AppResultData<Object>(
				Constants.SUCCESS_0, ConstantMsg.SUCCESS_0_MSG, "");
		
		List<OrgStaffCash> orgStaffCashList = orgStaffCashService.selectByStaffId(userId);

		List<OrgStaffCashVo> userImgVos = new ArrayList<OrgStaffCashVo>();
		for (int i = 0; i < orgStaffCashList.size(); i++) {
			OrgStaffCashVo vo = new OrgStaffCashVo();
			OrgStaffCash item = orgStaffCashList.get(i);
           BeanUtilsExp.copyPropertiesIgnoreNull(item, vo);
			
            vo.setAddTimeStr(item.getAddTime().toString());
            vo.setUpdateTimeStr(item.getUpdateTime().toString());
            userImgVos.add(vo);
			
		}
		
		result.setData(userImgVos);
		return result;
	}
	
}
