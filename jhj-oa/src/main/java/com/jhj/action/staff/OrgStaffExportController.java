package com.jhj.action.staff;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
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
import com.jhj.common.Constants;
import com.jhj.oa.auth.AuthHelper;
import com.jhj.oa.auth.AuthPassport;
import com.jhj.po.model.bs.OrgStaffFinance;
import com.jhj.po.model.bs.OrgStaffs;
import com.jhj.po.model.bs.Orgs;
import com.jhj.po.model.order.OrderDispatchs;
import com.jhj.po.model.order.Orders;
import com.jhj.po.model.university.PartnerServiceType;
import com.jhj.po.model.user.UserAddrs;
import com.jhj.po.model.user.Users;
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
import com.jhj.vo.org.OrgSearchVo;
import com.jhj.vo.staff.OrgStaffFinanceSearchVo;
import com.jhj.vo.staff.OrgStaffFinanceVo;
import com.jhj.vo.staff.OrgStaffIncomingVo;
import com.meijia.utils.BeanUtilsExp;
import com.meijia.utils.ExcelUtil;
import com.meijia.utils.MathBigDecimalUtil;
import com.meijia.utils.StringUtil;
import com.meijia.utils.TimeStampUtil;
import com.meijia.utils.poi.ExcelTools;
import com.meijia.utils.poi.HssExcelTools;
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
	@AuthPassport
	@RequestMapping(value = "/export-order", method = RequestMethod.GET)
	public String exportOrder(Model model, HttpServletRequest request, HttpServletResponse response, OrderSearchVo searchVo) throws Exception {
		
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
		
		
		OrgStaffs orgStaff = orgStaffsService.selectByPrimaryKey(staffId);
		if (orgStaff == null) return null;
		
		OrderDispatchSearchVo OrderDispatchSearchVo = new OrderDispatchSearchVo();
		OrderDispatchSearchVo.setStaffId(staffId);
		OrderDispatchSearchVo.setDispatchStatus((short) 1);
		
		if (startServiceTime > 0L) OrderDispatchSearchVo.setStartServiceTime(startServiceTime);
		if (endServiceTime > 0L) OrderDispatchSearchVo.setEndServiceTime(endServiceTime);
		
		List<OrderDispatchs> orderDispatchs = orderDispatchsService.selectBySearchVo(OrderDispatchSearchVo);
		
		if (orderDispatchs.isEmpty()) return null;
		

		String cpath = request.getSession().getServletContext().getRealPath("/WEB-INF") + "/attach/";
		String templateName = "服务人员订单明细表.xlsx";
		
		XssExcelTools excel = new XssExcelTools(cpath + templateName, 0);
		XSSFSheet sh = excel.getXssSheet();
		
		XSSFCellStyle contentStyle = excel.getContentStyle(excel.getXssWb());
		
		int rowNum = 1;
		
		//订单总金额合计
		BigDecimal totalOrderMoneyAll = new BigDecimal(0);
		//订单总收入合计
		BigDecimal totalOrderIncomingAll = new BigDecimal(0);
		for (int i = 0; i < orderDispatchs.size(); i++) {

			OrderDispatchs item = orderDispatchs.get(i);
			Long orderId = item.getOrderId();
			Orders order = orderService.selectByPrimaryKey(orderId);
			
			if (order == null) continue;
			
			//导出的是完成服务和评价后的订单。
			Short orderStatus = order.getOrderStatus();
			if (!orderStatus.equals(Constants.ORDER_HOUR_STATUS_7) && 
				 orderStatus.equals(Constants.ORDER_HOUR_STATUS_8)) 
				continue;
			
			OrgStaffIncomingVo vo = orgStaffFinanceService.getStaffInComingDetail(orgStaff, order, item);
			
			XSSFRow rowData = sh.createRow(rowNum);

			for(int j = 0; j <= 26; j++) {
				rowData.createCell(j);
				XSSFCell c = rowData.getCell(j);
				c.setCellStyle(contentStyle);
			}
			
			//序号
			
			XSSFCell cellNo = rowData.getCell(0);
			
			cellNo.setCellValue(String.valueOf(rowNum));
			
			//门店
			XSSFCell cellParentOrg = rowData.getCell(1);
//			cellParentOrg.setCellStyle(contentStyle);
			cellParentOrg.setCellValue(vo.getParentOrgName());
			
			//云店
			XSSFCell cellOrg = rowData.getCell(2);
//			cellOrg.setCellStyle(contentStyle);
			cellOrg.setCellValue(vo.getOrgName());
			
			//服务人员
			XSSFCell cellStaffName = rowData.getCell(3);
//			cellStaffName.setCellStyle(contentStyle);
			cellStaffName.setCellValue(vo.getStaffName());
			
			//服务人员手机号
			XSSFCell cellStaffMobile = rowData.getCell(4);
			
			cellStaffMobile.setCellValue(vo.getStaffMobile());
//			cellStaffMobile.setCellStyle(contentStyle);
			//订单号
			XSSFCell cellOrderNo = rowData.getCell(5);
//			cellOrderNo.setCellStyle(contentStyle);
			cellOrderNo.setCellValue(vo.getOrderNo());
			
			//下单时间
			XSSFCell cellOrderAddTime = rowData.getCell(6);
			cellOrderAddTime.setCellStyle(contentStyle);
//			cellOrderAddTime.setCellValue(vo.getAddTimeStr());
			
			//服务类型
			XSSFCell cellOrderTypeName = rowData.getCell(7);
//			cellOrderTypeName.setCellStyle(contentStyle);
			cellOrderTypeName.setCellValue(vo.getOrderTypeName());
			
			//服务日期
			XSSFCell cellServiceDate = rowData.getCell(8);
//			cellServiceDate.setCellStyle(contentStyle);
			cellServiceDate.setCellValue(vo.getServiceDateStr());
			
			//服务时长
			XSSFCell cellServiceHour = rowData.getCell(9);
//			cellServiceHour.setCellStyle(contentStyle);
			cellServiceHour.setCellValue(String.valueOf(vo.getServiceHour()));
			
			
			//派工人数
			XSSFCell cellStaffNum = rowData.getCell(10);
//			cellStaffNum.setCellStyle(contentStyle);
			cellStaffNum.setCellValue(String.valueOf(vo.getStaffNum()));
			
			//服务地址
			XSSFCell cellAddr = rowData.getCell(11);
//			cellAddr.setCellStyle(contentStyle);
			cellAddr.setCellValue(vo.getAddr());
			
			//用户手机号,是否为会员
			XSSFCell cellMobile = rowData.getCell(12);
			
			cellMobile.setCellValue(vo.getUserMobile());
//			cellMobile.setCellStyle(contentStyle);
			
			XSSFCell cellIsVip = rowData.getCell(13);
//			cellIsVip.setCellStyle(contentStyle);
			cellIsVip.setCellValue(vo.getIsVipStr());
			
			//支付方式
			XSSFCell cellPayType = rowData.getCell(14);
//			cellPayType.setCellStyle(contentStyle);
			cellPayType.setCellValue(vo.getPayTypeName());
			
			//原价
			XSSFCell cellOrderMoney = rowData.getCell(15);
//			cellOrderMoney.setCellStyle(contentStyle);
			cellOrderMoney.setCellValue(MathBigDecimalUtil.round2(vo.getOrderMoney()));
			
			//优惠劵
			XSSFCell cellCouponName = rowData.getCell(16);
//			cellCouponName.setCellStyle(contentStyle);
			cellCouponName.setCellValue(vo.getCouponName());
			
			//补差价
			XSSFCell cellOrderPayExtDiff = rowData.getCell(17);
//			cellOrderPayExtDiff.setCellStyle(contentStyle);
			cellOrderPayExtDiff.setCellValue(MathBigDecimalUtil.round2(vo.getOrderPayExtDiff()));
			
			//加时
			XSSFCell cellOrderPayExtOverWork = rowData.getCell(18);
			cellOrderPayExtOverWork.setCellValue(MathBigDecimalUtil.round2(vo.getOrderPayExtOverWork()));
//			cellOrderPayExtOverWork.setCellStyle(contentStyle);
			//收入
			XSSFCell cellOrderIncoming = rowData.getCell(19);
			cellOrderIncoming.setCellValue(MathBigDecimalUtil.round2(vo.getOrderIncoming()));
//			cellOrderIncoming.setCellStyle(contentStyle);
			//订单补贴
			XSSFCell cellOrderPayCouon = rowData.getCell(20);
			cellOrderPayCouon.setCellValue(MathBigDecimalUtil.round2(vo.getOrderPayCoupon()));
//			cellOrderPayCouon.setCellStyle(contentStyle);
			//原价
			XSSFCell cellOrderExtDiffIncoming = rowData.getCell(21);
			cellOrderExtDiffIncoming.setCellValue(MathBigDecimalUtil.round2(vo.getOrderPayExtDiffIncoming()));
//			cellOrderExtDiffIncoming.setCellStyle(contentStyle);
			//加时收入
			XSSFCell cellOrderPayOverwokIncoming = rowData.getCell(22);
			cellOrderPayOverwokIncoming.setCellValue(MathBigDecimalUtil.round2(vo.getOrderPayExtOverWorkIncoming()));
//			cellOrderPayOverwokIncoming.setCellStyle(contentStyle);
			//订单产生欠款
			XSSFCell cellTotalOrderDept = rowData.getCell(23);
			cellTotalOrderDept.setCellValue(MathBigDecimalUtil.round2(vo.getTotalOrderDept()));
//			cellTotalOrderDept.setCellStyle(contentStyle);
			//订单总金额
			XSSFCell cellTotalOrderMoney = rowData.getCell(24);
			cellTotalOrderMoney.setCellValue(MathBigDecimalUtil.round2(vo.getTotalOrderMoney()));
//			cellTotalOrderMoney.setCellStyle(contentStyle);
			totalOrderMoneyAll = totalOrderMoneyAll.add(vo.getTotalOrderMoney());
			
			//订单总收入
			XSSFCell cellTotalOrderIncoming = rowData.getCell(25);
			cellTotalOrderIncoming.setCellValue(MathBigDecimalUtil.round2(vo.getTotalOrderIncoming()));
//			cellTotalOrderIncoming.setCellStyle(contentStyle);
			totalOrderIncomingAll = totalOrderIncomingAll.add(vo.getTotalOrderIncoming());
			//订单备注
			XSSFCell cellRemarks = rowData.getCell(26);
//			cellRemarks.setCellStyle(contentStyle);
			cellRemarks.setCellValue(vo.getRemarks());

			
			
			rowNum++;
		}
		
		//写入合计
		XSSFRow rowData = sh.createRow(rowNum);

		for(int j = 0; j <= 26; j++) {
			rowData.createCell(j);
			
			sh.autoSizeColumn((short)j);
		}
		
		XSSFCell cellHeji = rowData.getCell(23);
		cellHeji.setCellStyle(contentStyle);
		cellHeji.setCellValue("合计:");
		
		XSSFCell cellTotalOrderMoneyAll = rowData.getCell(24);
		cellTotalOrderMoneyAll.setCellValue(MathBigDecimalUtil.round2(totalOrderMoneyAll));
		cellTotalOrderMoneyAll.setCellStyle(contentStyle);
		XSSFCell cellTotalOrderIncomingAll = rowData.getCell(25);
		cellTotalOrderIncomingAll.setCellValue(MathBigDecimalUtil.round2(totalOrderIncomingAll));
		cellTotalOrderIncomingAll.setCellStyle(contentStyle);
		

		String fileName = orgStaff.getName() + "-订单收入明细表.xls";
		excel.downloadExcel(response, fileName);
		
		return null;
	}

}
