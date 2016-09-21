package com.jhj.action.app.staff;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.jhj.action.app.BaseController;
import com.jhj.common.ConstantMsg;
import com.jhj.common.Constants;
import com.jhj.po.model.bs.OrgStaffDetailPay;
import com.jhj.po.model.bs.OrgStaffFinance;
import com.jhj.po.model.bs.OrgStaffPayDept;
import com.jhj.po.model.bs.OrgStaffs;
import com.jhj.po.model.orderReview.JhjSetting;
import com.jhj.po.model.user.UserPushBind;
import com.jhj.service.bs.OrgStaffBlackService;
import com.jhj.service.bs.OrgStaffDetailPayService;
import com.jhj.service.bs.OrgStaffFinanceService;
import com.jhj.service.bs.OrgStaffPayDeptService;
import com.jhj.service.bs.OrgStaffsService;
import com.jhj.service.orderReview.SettingService;
import com.jhj.service.users.UserPushBindService;
import com.jhj.vo.staff.OrgStaffPayDeptVo;
import com.jhj.vo.staff.OrgStaffPaySearchVo;
import com.jhj.vo.staff.OrgStaffPayVo;
import com.meijia.utils.DateUtil;
import com.meijia.utils.GsonUtil;
import com.meijia.utils.MathBigDecimalUtil;
import com.meijia.utils.OneCareUtil;
import com.meijia.utils.OrderNoUtil;
import com.meijia.utils.PushUtil;
import com.meijia.utils.SmsUtil;
import com.meijia.utils.TimeStampUtil;
import com.meijia.utils.vo.AppResultData;

@Controller
@RequestMapping(value = "/app/staff/pay")
public class StaffPayController extends BaseController {

	@Autowired
	private OrgStaffDetailPayService orgStaffDetailPayService;

	@Autowired
	private OrgStaffFinanceService orgStaffFinanceService;

	@Autowired
	private OrgStaffsService orgStaffsService;

	@Autowired
	private OrgStaffPayDeptService orgStaffPayDeptService;

	@Autowired
	private OrgStaffBlackService orgStaffBlackService;

	@Autowired
	private UserPushBindService bindService;

	@Autowired
	private SettingService settingService;

	/**
	 * 交易明细接口
	 * 
	 * @param request
	 * @param staffId
	 * @param year
	 * @param month
	 * @param page
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping(value = "get_detail", method = RequestMethod.GET)
	public AppResultData<Object> getPayDetail(HttpServletRequest request, @RequestParam("staff_id") Long staffId,
			@RequestParam(value = "year", required = false, defaultValue = "0") int year,
			@RequestParam(value = "month", required = false, defaultValue = "0") int month,
			@RequestParam(value = "page", required = false, defaultValue = "1") int page) throws ParseException {
		AppResultData<Object> result = new AppResultData<Object>(Constants.SUCCESS_0, ConstantMsg.SUCCESS_0_MSG, new String());

		String startTimeStr = DateUtil.getFirstDayOfMonth(year, month) + " 00:00:00";
		String endTimeStr = DateUtil.getLastDayOfMonth(year, month) + " 23:59:59";

		Long startTime = TimeStampUtil.getMillisOfDayFull(startTimeStr) / 1000;
		Long endTime = TimeStampUtil.getMillisOfDayFull(endTimeStr) / 1000;

		OrgStaffPaySearchVo searchVo = new OrgStaffPaySearchVo();
		searchVo.setStaffId(staffId);
		searchVo.setStartTime(startTime);
		searchVo.setEndTime(endTime);

		List<OrgStaffDetailPay> payList = orgStaffDetailPayService.selectByStaffIdAndTimeListPage(searchVo, page, Constants.PAGE_MAX_NUMBER);
		List<OrgStaffPayVo> orgStaffPayVoList = new ArrayList<OrgStaffPayVo>();
		for (int i = 0; i < payList.size(); i++) {
			OrgStaffDetailPay orgStaffDetailPay = payList.get(i);
			OrgStaffPayVo vo = orgStaffDetailPayService.getOrgStaffPayVo(orgStaffDetailPay);
			orgStaffPayVoList.add(vo);
		}
		result.setData(orgStaffPayVoList);
		return result;
	}

	/**
	 * 支付欠款接口
	 * 
	 * @param request
	 * @param staffId
	 * @param payType
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping(value = "pay_dept", method = RequestMethod.GET)
	public AppResultData<Object> getPayDept(HttpServletRequest request, @RequestParam("staff_id") Long staffId, @RequestParam("pay_type") Short payType)
			throws ParseException {
		AppResultData<Object> result = new AppResultData<Object>(Constants.SUCCESS_0, ConstantMsg.SUCCESS_0_MSG, new String());
		OrgStaffs orgstaff = orgStaffsService.selectByPrimaryKey(staffId);
		if (orgstaff == null) {
			result.setStatus(Constants.ERROR_999);
			result.setMsg(ConstantMsg.STAFF_NOT_EXIST_MG);
			return result;
		}
		OrgStaffFinance orgStaffFinance = orgStaffFinanceService.selectByStaffId(staffId);
		if (orgStaffFinance == null) {
			return result;
		}
		if (orgStaffFinance.getTotalDept() == new BigDecimal(0)) {
			result.setStatus(Constants.ERROR_999);
			result.setMsg(ConstantMsg.TOTALDEPT_NOT_EXIST_MG);
			return result;
		}
		// org_staff_pay_dept 新增记录
		String orderNo = String.valueOf(OrderNoUtil.genOrderNo());
		OrgStaffPayDept orgstaffPayDept = orgStaffPayDeptService.initOrgStaffPayDept();
		orgstaffPayDept.setStaffId(staffId);
		orgstaffPayDept.setPayType(payType);
		orgstaffPayDept.setOrderNo(orderNo);
		orgstaffPayDept.setMobile(orgstaff.getMobile());
		orgstaffPayDept.setOrderMoney(orgStaffFinance.getTotalDept());
		orgStaffPayDeptService.insert(orgstaffPayDept);

		OrgStaffPayDeptVo vo = new OrgStaffPayDeptVo();
		vo.setStaffId(staffId);
		vo.setOrderId(orgstaffPayDept.getOrderId());
		vo.setOrderNo(orderNo);
		vo.setOrderMoney(orgstaffPayDept.getOrderMoney());
		vo.setNotifyUrl("http://www.jia-he-jia.com/jhj-app/pay/notify_alipay_dep.jsp");

		result.setData(vo);
		return result;
	}

	/**
	 * 支付欠款成功接口
	 * 
	 * @param request
	 * @param staffId
	 * @param orderNo
	 * @param payType
	 * @param notifyId
	 * @param notifyTime
	 * @param tradeNo
	 * @param tradeStatus
	 * @param payAccount
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "pay_dept_success", method = RequestMethod.POST)
	public AppResultData<Object> getPayDeptSuccess(
			HttpServletRequest request,
			// @RequestParam("staff_id") Long staffId,
			@RequestParam("order_no") String orderNo, @RequestParam("pay_type") Long payType, @RequestParam("notify_id") String notifyId,
			@RequestParam("notify_time") String notifyTime, @RequestParam("trade_no") String tradeNo, @RequestParam("trade_status") String tradeStatus,
			@RequestParam(value = "pay_account", required = false, defaultValue = "") String payAccount) throws Exception {
		AppResultData<Object> result = new AppResultData<Object>(Constants.SUCCESS_0, ConstantMsg.SUCCESS_0_MSG, new String());

		// 判断如果不是正确支付状态，则直接返回.
		Boolean paySuccess = OneCareUtil.isPaySuccess(tradeStatus);
		if (paySuccess == false) {
			result.setStatus(Constants.ERROR_999);
			result.setMsg(ConstantMsg.ORDER_PAY_NOT_SUCCESS_MSG);
			return result;
		}

		// 判断订单号是否存在，操作表 org_staff_pay_dept
		// 判断订单号的状态是否为已支付，如果为已支付则直接返回。不需要往下继续走流程。
		OrgStaffPayDept orgstaffPayDept = orgStaffPayDeptService.selectByOrderNo(orderNo);
		if (orgstaffPayDept == null) {
			return result;
		}
		if (orgstaffPayDept.getOrderStatus().equals((short) 2)) {
			return result;
		}

		Long staffId = orgstaffPayDept.getStaffId();

		// 判断员工是否存在，不存在，则返回错误信息： 员工不存在。
		OrgStaffs orgstaff = orgStaffsService.selectByPrimaryKey(staffId);
		if (orgstaff == null) {
			result.setStatus(Constants.ERROR_999);
			result.setMsg(ConstantMsg.STAFF_NOT_EXIST_MG);
			return result;
		}

		// 更新欠款订单状态
		orgstaffPayDept.setOrderStatus((short) 2);
		orgstaffPayDept.setTradeStatus(tradeStatus);
		orgstaffPayDept.setTradeId(tradeNo);
		orgstaffPayDept.setPayAccount(payAccount);
		orgStaffPayDeptService.updateByPrimaryKey(orgstaffPayDept);

		OrgStaffFinance orgStaffFinance = orgStaffFinanceService.selectByStaffId(staffId);
		if (orgStaffFinance == null) {
			return result;
		}
		// 操作服务人员财务明细表 org_staff_detail_pay，插入一条 order_type = 4 = 还款订单 的记录
		OrgStaffDetailPay orgStaffDetailPay = orgStaffDetailPayService.initStaffDetailPay();
		orgStaffDetailPay.setStaffId(staffId);
		orgStaffDetailPay.setMobile(orgstaff.getMobile());
		orgStaffDetailPay.setOrderType((short) 4);
		orgStaffDetailPay.setOrderId(orgstaffPayDept.getOrderId());
		orgStaffDetailPay.setOrderNo(orderNo);
		orgStaffDetailPay.setOrderMoney(orgStaffFinance.getTotalDept());
		orgStaffDetailPay.setOrderPay(orgStaffFinance.getTotalDept());
		orgStaffDetailPay.setOrderStatusStr("完成支付");
		orgStaffDetailPayService.insert(orgStaffDetailPay);
		// 操作服务人员财务表 org_staff_finance， 将总欠款减去 此次支付成功的金额.
		orgStaffFinance.setTotalDept(orgStaffFinance.getTotalDept().subtract(orgStaffDetailPay.getOrderMoney()));
		orgStaffFinanceService.updateByPrimaryKeySelective(orgStaffFinance);
		// 如果总欠款，低于设定的1000元，则去检查 org_staff_black , 找出是否有记录，并且black_type = 0
		// 的情况，如果有记录，则将他删除掉.

		BigDecimal maxOrderDept = new BigDecimal(1000);
		JhjSetting jhjSetting = settingService.selectBySettingType("total-dept-blank");
		if (jhjSetting != null) {
			maxOrderDept = new BigDecimal(jhjSetting.getSettingValue());
		}

		if (orgStaffFinance.getTotalDept().compareTo(maxOrderDept) == -1) {
			orgStaffFinance.setIsBlack((short) 0);
			orgStaffFinance.setUpdateTime(TimeStampUtil.getNowSecond());
			
				// d. 发送短信，告知员工已经支付欠款成功，并告知已不在黑名单中
				orgStaffsService.userOutBlackSuccessTodo(orgstaff.getMobile());
			
		}

		// 发送短信，支付欠款成功
		String deptStr = MathBigDecimalUtil.round2(orgStaffFinance.getTotalDept());
		String timeStr = DateUtil.getNow("HH:mm");
		String[] content = new String[] { deptStr, timeStr, "1000" };
		HashMap<String, String> sendSmsResult = SmsUtil.SendSms(orgstaff.getMobile(), Constants.STAFF_PAY_DEPT_SUCCESS, content);

		// 发送推送消息，告知欠款支付成功
		UserPushBind userPushBind = bindService.selectByUserId(staffId);

		if (userPushBind != null) {
			String clientId = userPushBind.getClientId();
			// 透传消息 参数 map
			HashMap<String, String> paramsMap = new HashMap<String, String>();

			paramsMap.put("cid", clientId);

			HashMap<String, String> transMap = new HashMap<String, String>();

			transMap.put("is_show", "true");
			transMap.put("action", "msg");
			transMap.put("remind_title", "支付欠款成功");
			transMap.put("remind_content", "您好，您的服务订单欠款已还清.");

			String jsonParams = GsonUtil.GsonString(transMap);

			paramsMap.put("transmissionContent", jsonParams);

			PushUtil.AndroidPushToSingle(paramsMap);
		}

		return result;
	}
}
