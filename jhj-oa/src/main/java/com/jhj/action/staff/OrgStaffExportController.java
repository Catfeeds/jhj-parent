package com.jhj.action.staff;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.jhj.action.BaseController;
import com.jhj.common.Constants;
import com.jhj.oa.auth.AuthPassport;
import com.jhj.po.model.bs.OrgStaffPayDept;
import com.jhj.po.model.bs.OrgStaffs;
import com.jhj.po.model.bs.Orgs;
import com.jhj.po.model.order.OrderDispatchs;
import com.jhj.po.model.order.Orders;
import com.jhj.service.bs.OrgStaffFinanceService;
import com.jhj.service.bs.OrgStaffPayDeptService;
import com.jhj.service.bs.OrgStaffsService;
import com.jhj.service.bs.OrgsService;
import com.jhj.service.order.OrderDispatchsService;
import com.jhj.service.order.OrdersService;
import com.jhj.service.university.PartnerServiceTypeService;
import com.jhj.service.users.UserAddrsService;
import com.jhj.service.users.UsersService;
import com.jhj.vo.order.OrderDispatchSearchVo;
import com.jhj.vo.order.OrderSearchVo;
import com.jhj.vo.staff.OrgStaffIncomingVo;
import com.meijia.utils.MathBigDecimalUtil;
import com.meijia.utils.OneCareUtil;
import com.meijia.utils.StringUtil;
import com.meijia.utils.TimeStampUtil;
import com.meijia.utils.poi.XssExcelTools;

@Controller
@RequestMapping(value = "/staff")
public class OrgStaffExportController extends BaseController {
	@Autowired
	private OrgStaffPayDeptService orgStaffPayDeptService;

	@Autowired
	private OrgStaffsService orgStaffsService;

	@Autowired
	private OrgStaffFinanceService orgStaffFinanceService;
	
	@Autowired
	private OrgsService orgService;
	
	@Autowired
	private OrderDispatchsService orderDispatchsService;
	
	@Autowired
	private OrdersService orderService;
	
	@Autowired
	private PartnerServiceTypeService partnerServiceTypeService;
	
	@Autowired
	private UserAddrsService userAddrService;
	
	@Autowired
	private UsersService userService;
	
	//服务人员所有订单明细表
	@RequestMapping(value = "/export-order", method = RequestMethod.GET)
	public void exportOrder(Model model, HttpServletRequest request, HttpServletResponse response, OrderSearchVo searchVo) throws Exception {
		
		Long staffId = 0L;
		if (searchVo.getStaffId() != null) {
			staffId = searchVo.getStaffId();
		} else {
			staffId = searchVo.getSelectStaff();
		}
		
		String startTimeStr = searchVo.getStartTimeStr();
		Long startServiceTime = 0L;
		if (!StringUtil.isEmpty(startTimeStr)) {
			startServiceTime = TimeStampUtil.getMillisOfDayFull(startTimeStr+" 00:00:00") / 1000;
		}

		Long endServiceTime = 0L;
		String endTimeStr = searchVo.getEndTimeStr();
		if (!StringUtil.isEmpty(endTimeStr)) {
			endServiceTime = TimeStampUtil.getMillisOfDayFull(endTimeStr+" 23:59:59") / 1000;
		}
		
		
//		OrgStaffs orgStaff = orgStaffsService.selectByPrimaryKey(staffId);
		//if (orgStaff == null) return ;
		
		OrderDispatchSearchVo orderDispatchSearchVo = new OrderDispatchSearchVo();
		orderDispatchSearchVo.setDispatchStatus((short) 1);
		
		if (startServiceTime > 0L) orderDispatchSearchVo.setStartServiceTime(startServiceTime);
		if (endServiceTime > 0L) orderDispatchSearchVo.setEndServiceTime(endServiceTime);
		orderDispatchSearchVo.setStaffId(searchVo.getStaffId());
		orderDispatchSearchVo.setStaffName(searchVo.getStaffName());
		orderDispatchSearchVo.setOrderNo(searchVo.getOrderNo());
		
		Short[] orderStatusArry = {7,8};
		orderDispatchSearchVo.setOrderStatusList(Arrays.asList(orderStatusArry));
		
		List<OrderDispatchs> orderDispatchs = orderDispatchsService.selectBySearchVo(orderDispatchSearchVo);
		

		String cpath = request.getSession().getServletContext().getRealPath("/WEB-INF") + "/attach/";
		String templateName = "服务人员订单明细表.xlsx";
		
		XssExcelTools excel = new XssExcelTools(cpath + templateName, 0);
		XSSFSheet sh = excel.getXssSheet();
		
		XSSFCellStyle contentStyle = excel.getContentStyle(excel.getXssWb());
		
		int rowNum = 1;
		
		//订单支付金额合计
		BigDecimal totalOrderMoneyAll = new BigDecimal(0);
		//订单总收入合计
		BigDecimal totalOrderIncomingAll = new BigDecimal(0);
		//订单欠款合计
		BigDecimal totalDeptAll = new BigDecimal(0);
		
		for (int i = 0; i < orderDispatchs.size(); i++) {

			OrderDispatchs item = orderDispatchs.get(i);
			Long orderId = item.getOrderId();
			Orders order = orderService.selectByPrimaryKey(orderId);
			
			if (order == null) continue;
			
			
			//导出的是完成服务和评价后的订单。
			Short orderStatus = order.getOrderStatus();
			if (!orderStatus.equals(Constants.ORDER_HOUR_STATUS_7) && 
				!orderStatus.equals(Constants.ORDER_HOUR_STATUS_8)) 
				continue;
//			logger.info("order_no = " + order.getOrderNo());
			//订单完成时间是否在查询条件范围之内
			Long orderUpdateTime = order.getOrderDoneTime();
			/*if (orderUpdateTime < startServiceTime || orderUpdateTime > endServiceTime) {
				continue;
			}*/
			
			OrgStaffs orgStaff = orgStaffsService.selectByPrimaryKey(item.getStaffId());
			
			OrgStaffIncomingVo vo = orgStaffFinanceService.getStaffInComingDetail(orgStaff, order, item);
			
			XSSFRow rowData = sh.createRow(rowNum);

			for(int j = 0; j <= 27; j++) {
				rowData.createCell(j);
				XSSFCell c = rowData.getCell(j);
				c.setCellType(XSSFCell.CELL_TYPE_STRING);
//				c.setCellValue("");  
//				c.setCellStyle(contentStyle);
			}
			
			
			//序号
			this.setCellValueForString(rowData, 0, String.valueOf(rowNum));
			
			//门店
			this.setCellValueForString(rowData, 1, vo.getParentOrgName());
			
			//云店
			this.setCellValueForString(rowData, 2, vo.getOrgName());
			
			//服务人员
			this.setCellValueForString(rowData, 3, vo.getStaffName());
			
			//服务人员手机号
			this.setCellValueForDouble(rowData, 4, Double.valueOf(vo.getStaffMobile()));
			
			//订单号
			this.setCellValueForString(rowData, 5, vo.getOrderNo());
			
			//下单时间
			this.setCellValueForString(rowData, 6, vo.getAddTimeStr());
			
			//服务类型
			this.setCellValueForString(rowData, 7, vo.getOrderTypeName());

			//服务日期
			this.setCellValueForString(rowData, 8, vo.getServiceDateStr());

			//服务时长
			this.setCellValueForDouble(rowData, 9, vo.getServiceHour());
			
			//派工人数
			this.setCellValueForDouble(rowData, 10, Double.valueOf(vo.getStaffNum()));
			
			//服务地址
			this.setCellValueForString(rowData, 11, vo.getAddr());

			//用户手机号
			this.setCellValueForDouble(rowData, 12, Double.valueOf(vo.getUserMobile()));
			
			//是否为会员
			this.setCellValueForString(rowData, 13, vo.getIsVipStr());
			
			//支付方式
			this.setCellValueForString(rowData, 14, vo.getPayTypeName());

			//原价
			this.setCellValueForDouble(rowData, 15, Double.valueOf(MathBigDecimalUtil.round2(vo.getOrderMoney())));
			
			//优惠劵
			this.setCellValueForDouble(rowData, 16,  Double.valueOf(MathBigDecimalUtil.round2(vo.getOrderPayCoupon())));
			
			//补差价
			this.setCellValueForDouble(rowData, 17, Double.valueOf(MathBigDecimalUtil.round2(vo.getOrderPayExtDiff())));

			//加时
			this.setCellValueForDouble(rowData, 18, Double.valueOf(MathBigDecimalUtil.round2(vo.getOrderPayExtOverWork())));

			//收入
			/*this.setCellValueForDouble(rowData, 19, Double.valueOf(MathBigDecimalUtil.round2(vo.getOrderIncoming())));*/

			//订单补贴
			this.setCellValueForDouble(rowData, 20, Double.valueOf(MathBigDecimalUtil.round2(vo.getOrderPayCouponIncoming())));

			//补差价收入
			this.setCellValueForDouble(rowData, 21, Double.valueOf(MathBigDecimalUtil.round2(vo.getOrderPayExtDiffIncoming())));

			//加时收入
			this.setCellValueForDouble(rowData, 22, Double.valueOf(MathBigDecimalUtil.round2(vo.getOrderPayExtOverWorkIncoming())));

			//订单产生欠款
			this.setCellValueForDouble(rowData, 23, Double.valueOf(MathBigDecimalUtil.round2(vo.getTotalOrderDept())));
			
			totalDeptAll = totalDeptAll.add(vo.getTotalOrderDept());
			//订单总金额
			this.setCellValueForDouble(rowData, 24, Double.valueOf(MathBigDecimalUtil.round2(vo.getTotalOrderPay())));

			totalOrderMoneyAll = totalOrderMoneyAll.add(vo.getTotalOrderPay());
			
			//订单总收入
			this.setCellValueForDouble(rowData, 25, Double.valueOf(MathBigDecimalUtil.round2(vo.getTotalOrderIncoming())));

			totalOrderIncomingAll = totalOrderIncomingAll.add(vo.getTotalOrderIncoming());
			//订单备注
			this.setCellValueForString(rowData, 26, vo.getRemarks());
			
			//订单来源
			this.setCellValueForString(rowData, 27, vo.getOrderFromStr());

			rowNum++;
		}
		
		//写入合计
		XSSFRow rowData = sh.createRow(rowNum);

		for(int j = 0; j <= 26; j++) {
			XSSFCell c = rowData.createCell(j);
//			c.setCellStyle(contentStyle);
//			sh.autoSizeColumn((short)j);
		}
		this.setCellValueForString(rowData, 22, "合计:");
		
		this.setCellValueForDouble(rowData, 23, Double.valueOf(MathBigDecimalUtil.round2(totalDeptAll)));

		this.setCellValueForDouble(rowData, 24, Double.valueOf(MathBigDecimalUtil.round2(totalOrderMoneyAll)));
		
		this.setCellValueForDouble(rowData, 25, Double.valueOf(MathBigDecimalUtil.round2(totalOrderIncomingAll)));

		//自动调整列宽
//		for(int j = 0; j <= 26; j++) {
//
//			sh.autoSizeColumn((short)j);
//		}
		
//		String fileName = orgStaff.getName() + "-订单收入明细表.xls";
		excel.downloadExcel(response, templateName);
		
	}
	
	
	//服务人员所有订单明细表
	@AuthPassport
	@RequestMapping(value = "/export-pay-dept", method = RequestMethod.GET)
	public String exportPayDept(Model model, HttpServletRequest request, HttpServletResponse response, OrderSearchVo searchVo) throws Exception {
		
		Long staffId = 0L;
		if (searchVo.getStaffId() != null) {
			staffId = searchVo.getStaffId();
		} else {
			staffId = searchVo.getSelectStaff();
		}
				
		OrgStaffs orgStaff = orgStaffsService.selectByPrimaryKey(staffId);
		if (orgStaff == null) return null;
		
		searchVo.setStaffId(staffId);
		
		Long parentId = orgStaff.getParentOrgId();
		Orgs parentOrg = orgService.selectByPrimaryKey(parentId);
		String parentOrgName = parentOrg.getOrgName();

		Long orgId = orgStaff.getOrgId();
		Orgs org = orgService.selectByPrimaryKey(orgId);
		String orgName = org.getOrgName();
		
		
		searchVo.setOrderStatus((short) 2);
		
		List<OrgStaffPayDept> list = orgStaffPayDeptService.selectBySearchVo(searchVo);
		
		String cpath = request.getSession().getServletContext().getRealPath("/WEB-INF") + "/attach/";
		String templateName = "服务人员还款明细表.xlsx";
		
		XssExcelTools excel = new XssExcelTools(cpath + templateName, 0);
		XSSFSheet sh = excel.getXssSheet();
		
		XSSFCellStyle contentStyle = excel.getContentStyle(excel.getXssWb());
		
		int rowNum = 1;
		
		BigDecimal totalPayDept = new BigDecimal(0);
		for (int i = 0; i < list.size(); i++) {

			OrgStaffPayDept item = list.get(i);

			XSSFRow rowData = sh.createRow(rowNum);

			for(int j = 0; j <= 10; j++) {
				rowData.createCell(j);
				XSSFCell c = rowData.getCell(j);
				c.setCellType(XSSFCell.CELL_TYPE_STRING);
//				c.setCellValue("");  
//				c.setCellStyle(contentStyle);
			}
			
			//序号
			this.setCellValueForString(rowData, 0, String.valueOf(rowNum));
			
			//门店
			this.setCellValueForString(rowData, 1, parentOrgName);
			
			//云店
			this.setCellValueForString(rowData, 2, orgName);
			
			//服务人员
			this.setCellValueForString(rowData, 3, orgStaff.getName());
			
			//服务人员手机号
			this.setCellValueForDouble(rowData, 4, Double.valueOf(orgStaff.getMobile()));
			
			//还款时间
			Long addTime = item.getAddTime();
			String addTimeStr = TimeStampUtil.timeStampToDateStr(addTime * 1000,  "yyyy-MM-dd HH:mm:ss");
			this.setCellValueForString(rowData, 5, addTimeStr);
			
			//订单号
			this.setCellValueForString(rowData, 6, item.getOrderNo());
			
			//支付方式
			String payTypeName = OneCareUtil.getPayTypeName(item.getPayType());
			this.setCellValueForString(rowData, 7, payTypeName);
			
			//还款金额
			this.setCellValueForDouble(rowData, 8, Double.valueOf(MathBigDecimalUtil.round2(item.getOrderMoney())));
			
			//还款账号
			this.setCellValueForString(rowData, 9, item.getPayAccount());
			
			//交易流水号
			this.setCellValueForString(rowData, 10, item.getTradeId());
		
			totalPayDept = totalPayDept.add(item.getOrderMoney());
			rowNum++;
			
		}
		
		//写入合计
		XSSFRow rowData = sh.createRow(rowNum);

		for(int j = 0; j <= 10; j++) {
			XSSFCell c = rowData.createCell(j);
//					c.setCellStyle(contentStyle);
//					sh.autoSizeColumn((short)j);
		}
		this.setCellValueForString(rowData, 7, "合计:");

		this.setCellValueForDouble(rowData, 8, Double.valueOf(MathBigDecimalUtil.round2(totalPayDept)));
		
//		String fileName = orgStaff.getName() + "-还款明细表.xls";
		excel.downloadExcel(response, templateName);
		
		return null;
	}
	
	
	private  void setCellValueForString(XSSFRow rowData, int rowNum, String v) {
		XSSFCell c = rowData.getCell(rowNum);
		c.setCellType(XSSFCell.CELL_TYPE_STRING);
		c.setCellValue(v);
	}
	
	private  void setCellValueForDouble(XSSFRow rowData, int cellNum, Double v) {
		XSSFCell c = rowData.getCell(cellNum);
		c.setCellType(XSSFCell.CELL_TYPE_NUMERIC);
		c.setCellValue(v);
	}

}
