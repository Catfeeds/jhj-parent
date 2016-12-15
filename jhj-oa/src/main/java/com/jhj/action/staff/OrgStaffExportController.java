package com.jhj.action.staff;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
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
import com.jhj.vo.org.OrgSearchVo;
import com.jhj.vo.staff.OrgStaffFinanceSearchVo;
import com.jhj.vo.staff.OrgStaffFinanceVo;
import com.meijia.utils.BeanUtilsExp;
import com.meijia.utils.ExcelUtil;
import com.meijia.utils.StringUtil;
import com.meijia.utils.TimeStampUtil;

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
	public String exportOrder(Model model, HttpServletRequest request, Long staffId) throws Exception {
		
		OrgStaffs orgStaff = orgStaffsService.selectByPrimaryKey(staffId);
		if (orgStaff == null) return null;
		
		Long orgId = orgStaff.getOrgId();
		Long parentId = orgStaff.getParentOrgId();
		
		Orgs org = orgService.selectByPrimaryKey(orgId);
		Orgs parentOrg = orgService.selectByPrimaryKey(parentId);
		
		OrderDispatchSearchVo searchVo = new OrderDispatchSearchVo();
		searchVo.setStaffId(staffId);
		searchVo.setDispatchStatus((short) 1);
		List<OrderDispatchs> orderDispatchs = orderDispatchsService.selectBySearchVo(searchVo);
		
		if (orderDispatchs.isEmpty()) return null;
		
		List<PartnerServiceType> serviceTypes = partnerServiceTypeService.selectAll();
		
		String cpath = request.getSession().getServletContext().getRealPath("/WEB-INF") + "/attach/";
		String templateName = "服务人员订单明细表.xlsx";
		
		InputStream in = new FileInputStream(cpath + templateName);

		HSSFWorkbook wb = (HSSFWorkbook) ExcelUtil.loadWorkbook(templateName, in);
		HSSFSheet sh = wb.getSheetAt(0);
		int rows = sh.getPhysicalNumberOfRows();
		
		int rowNum = 1;
		for (int i = 0; i < orderDispatchs.size(); i++) {
			OrderDispatchs item = orderDispatchs.get(i);
			Long orderId = item.getOrderId();
			String orderNo = item.getOrderNo();
			Orders order = orderService.selectByPrimaryKey(orderId);
			
			if (order == null) continue;
			
			//导出的是完成服务和评价后的订单。
			Short orderStatus = order.getOrderStatus();
			if (!orderStatus.equals(Constants.ORDER_HOUR_STATUS_7) && 
				 orderStatus.equals(Constants.ORDER_HOUR_STATUS_8)) 
				continue;
			
			
			HSSFRow rowData = sh.getRow(rowNum);
			
			//序号
			HSSFCell cellNo = rowData.getCell(0);
			cellNo.setCellValue(String.valueOf(rowNum));
			
			//门店
			HSSFCell cellParentOrg = rowData.getCell(1);
			cellParentOrg.setCellValue(parentOrg.getOrgName());
			
			//云店
			HSSFCell cellOrg = rowData.getCell(2);
			cellOrg.setCellValue(org.getOrgName());
			
			//服务人员
			HSSFCell cellStaffName = rowData.getCell(3);
			cellStaffName.setCellValue(orgStaff.getName());
			
			//服务人员手机号
			HSSFCell cellStaffMobile = rowData.getCell(4);
			cellStaffMobile.setCellValue(orgStaff.getMobile());
			
			//订单号
			HSSFCell cellOrderNo = rowData.getCell(5);
			cellOrderNo.setCellValue(item.getOrderNo());
			
			//下单时间
			Long addTime = order.getAddTime();
			String addTimeStr = TimeStampUtil.timeStampToDateStr(addTime * 1000, "yyyy-MM-dd HH:mm");
			HSSFCell cellOrderAddTime = rowData.getCell(6);
			cellOrderAddTime.setCellValue(addTimeStr);
			
			//服务类型
			Long serviceTypeId = order.getServiceType();
			PartnerServiceType serviceType = partnerServiceTypeService.findServiceType(serviceTypes, serviceTypeId);
			String orderTypeName = "";
			if (serviceType != null) orderTypeName = serviceType.getName();
			HSSFCell cellOrderTypeName = rowData.getCell(7);
			cellOrderTypeName.setCellValue(orderTypeName);
			
			//服务日期
			String serviceDateStr = TimeStampUtil.timeStampToDateStr(order.getServiceDate() * 1000, "yyyy-MM-dd HH:mm");
			HSSFCell cellServiceDate = rowData.getCell(8);
			cellServiceDate.setCellValue(serviceDateStr);
			
			//服务时长
			HSSFCell cellServiceHour = rowData.getCell(9);
			cellServiceHour.setCellValue(String.valueOf(order.getServiceHour()));
			
			//派工人数
			HSSFCell cellStaffNum = rowData.getCell(10);
			cellStaffNum.setCellValue(String.valueOf(order.getStaffNums()));
			
			//服务地址
			Long addrId = order.getAddrId();
			UserAddrs userAddrs = userAddrService.selectByPrimaryKey(addrId);
			String addr = "";
			if (userAddrs != null) addr = userAddrs.getName()+""+userAddrs.getAddr();
			HSSFCell cellAddr = rowData.getCell(11);
			cellAddr.setCellValue(addr);
			
			//用户手机号,是否为会员
			Long userId = order.getUserId();
			Users u = userService.selectByPrimaryKey(userId);
			String mobile = "";
			String isVipStr = "否";
			if (u != null) {
				mobile = u.getMobile();
				if (u.getIsVip() == 1) isVipStr = "是";
			}
			HSSFCell cellMobile = rowData.getCell(12);
			cellMobile.setCellValue(mobile);
			
			HSSFCell cellIsVip = rowData.getCell(13);
			cellIsVip.setCellValue(isVipStr);
		}
		
		
		return null;
	}

}
