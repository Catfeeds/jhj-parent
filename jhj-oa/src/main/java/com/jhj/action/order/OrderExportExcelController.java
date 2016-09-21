package com.jhj.action.order;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.github.pagehelper.PageHelper;
import com.jhj.action.BaseController;
import com.jhj.common.ConstantOa;
import com.jhj.common.Constants;
import com.jhj.oa.auth.AuthHelper;
import com.jhj.po.dao.order.OrdersMapper;
import com.jhj.po.model.bs.Orgs;
import com.jhj.po.model.order.Orders;
import com.jhj.service.bs.OrgsService;
import com.jhj.service.order.OaOrderService;
import com.jhj.service.order.poi.PoiExportExcelService;
import com.jhj.service.university.PartnerServiceTypeService;
import com.jhj.vo.OaOrderSearchVo;
import com.jhj.vo.OrgSearchVo;
import com.jhj.vo.order.OaOrderListNewVo;
import com.meijia.utils.DateUtil;
import com.meijia.utils.StringUtil;
import com.meijia.utils.poi.POIUtils;

/**
 *
 * @author :hulj
 * @Date : 2016年5月27日下午5:24:55
 * @Description: 
 *		
 *		导出表中数据到 excel
 */
@Controller
@RequestMapping(value = "/order")
public class OrderExportExcelController extends BaseController {
	
	@Autowired
	private OrdersMapper orderMapper;
	
	@Autowired
	private OaOrderService oaOrderService;
	
	@Autowired
	private PoiExportExcelService poiExcelService;
	
	@Autowired
	private OrgsService orgService;
	
	@Autowired
	private PartnerServiceTypeService partService;
	
	/*
	 *  导出 钟点工订单列表
	 */
	@RequestMapping(value = "export_base_order",method = RequestMethod.GET)
	public void exportBaseOrderTable(OaOrderSearchVo oaOrderSearchVo,HttpServletRequest request,HttpServletResponse response) throws IOException, ParseException{
		
		int pageNo = ServletRequestUtils.getIntParameter(request,
				ConstantOa.PAGE_NO_NAME, ConstantOa.DEFAULT_PAGE_NO);
		int pageSize = ServletRequestUtils.getIntParameter(request,
				ConstantOa.PAGE_SIZE_NAME, ConstantOa.DEFAULT_PAGE_SIZE);
		/*
		 *  1. 导出时,搜索条件保持一致 
		 * 
		 */
//		PageHelper.startPage(pageNo, pageSize);
		
//		if(oaOrderSearchVo == null){
//			oaOrderSearchVo  = new OaOrderSearchVo();
//			oaOrderSearchVo.setOrderType(Constants.ORDER_TYPE_0);
//		}else {
//			oaOrderSearchVo.setOrderType(Constants.ORDER_TYPE_0);
//		}
		
		//得到 当前登录 的 门店id，并作为搜索条件
		Long sessionOrgId = AuthHelper.getSessionLoginOrg(request);
		
		List<Long> cloudIdList = new ArrayList<Long>();
		
		if (sessionOrgId > 0L) {
			
			/*
			 * 如果是店长 ，只能看到 自己门店 对应的 所有 云店 的 订单
			 * 而且 只能是 已 派工过的 订单。
			 */
			
			OrgSearchVo searchVo = new OrgSearchVo();
			searchVo.setParentId(sessionOrgId);
			searchVo.setOrgStatus((short) 1);
			
			List<Orgs> cloudList = orgService.selectBySearchVo(searchVo);
			
			for (Orgs orgs : cloudList) {
				cloudIdList.add(orgs.getOrgId());
			}
			
			List<Short> searchOrderStatusList = new ArrayList<Short>();
			
			searchOrderStatusList.add(Constants.ORDER_HOUR_STATUS_3);
			searchOrderStatusList.add(Constants.ORDER_HOUR_STATUS_5);
			searchOrderStatusList.add(Constants.ORDER_HOUR_STATUS_7);
			searchOrderStatusList.add(Constants.ORDER_HOUR_STATUS_8);
			searchOrderStatusList.add(Constants.ORDER_HOUR_STATUS_9);
			
			oaOrderSearchVo.setSearchOrderStatusList(searchOrderStatusList);
			
		}else{
			// 如果是 运营 人员，则能  查看全部 订单, 查看所有 云店
			OrgSearchVo searchVo = new OrgSearchVo();
			searchVo.setIsCloud((short) 1);
			searchVo.setOrgStatus((short) 1);
			List<Orgs> cloudOrgList = orgService.selectBySearchVo(searchVo);
			
			for (Orgs orgs : cloudOrgList) {
				cloudIdList.add(orgs.getOrgId());
			}
			
			/*
			 *  对于   未派工的  订单，，没有 云店， 只有运营人员 可以  看到，以便 后续处理
			 */
			cloudIdList.add(0L);
		}
		
		
		// 根据 登录 角色的门店，确定 云店
		oaOrderSearchVo.setSearchCloudOrgIdList(cloudIdList);
		
		//如果在 列表页面，选择了 云店  作为搜索 条件
		String jspOrgId = request.getParameter("orgId");
		if(!StringUtil.isEmpty(jspOrgId) && !jspOrgId.equals("0")){
			
			cloudIdList.clear();
			cloudIdList.add(Long.valueOf(jspOrgId));
			oaOrderSearchVo.setSearchCloudOrgIdList(cloudIdList);
		}
		
		
		//转换为数据库 参数字段
		String startTimeStr = oaOrderSearchVo.getStartTimeStr();
		if(!StringUtil.isEmpty(startTimeStr)){
			oaOrderSearchVo.setStartTime(DateUtil.getUnixTimeStamp(DateUtil.getBeginOfDay(startTimeStr)));
		}
		
		String endTimeStr = oaOrderSearchVo.getEndTimeStr();
		if(!StringUtil.isEmpty(endTimeStr)){
			oaOrderSearchVo.setEndTime(DateUtil.getUnixTimeStamp(DateUtil.getEndOfDay(endTimeStr)));
		}
		
		oaOrderSearchVo.setOrderType(Constants.ORDER_TYPE_0);
		
//		List<Orders> orderList = oaOrderService.selectVoByListPage(oaOrderSearchVo,pageNo,pageSize);
		
		/*
		 * 取数据时,不使用分页，可以全部导出到一张表
		 */
		List<Orders> orderList = orderMapper.selectOaOrderByListPage(oaOrderSearchVo);
		
		/*
		 * 2. 转换导出字段为  页面的 vo字段
		 */
		List<OaOrderListNewVo> voList = new ArrayList<OaOrderListNewVo>();
		
		for (Orders orders : orderList) {
			voList.add(oaOrderService.completeNewVo(orders));
		}
		
		String fileName="基础服务订单列表";
        
		/*
		 * 3. 映射 excel 字段和数据
		 */
        List<Map<String,Object>> list= poiExcelService.createExcelRecord(voList);
        
        String columnNames[]={"门店名称","云店名称","服务人员","下单时间",
        					"订单类型","服务日期","用户手机号","服务地址",
        					"订单状态","支付方式","总金额","支付金额"};//列名
        
        String keys[] =  {"orgName","cloudOrgName","staffName","addTime",
        				 "orderTypeName","serviceDate","mobile","orderAddress",
        				 "orderStatusName","payTypeName","orderMoney","orderPay"};//map中的key
        
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            POIUtils.createWorkBook(list,keys,columnNames).write(os);
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] content = os.toByteArray();
        InputStream is = new ByteArrayInputStream(content);
        // 设置response参数，可以打开下载页面
        response.reset();
        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        response.setHeader("Content-Disposition", "attachment;filename="+ new String((fileName + ".xls").getBytes(), "iso-8859-1"));
        ServletOutputStream out = response.getOutputStream();
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        try {
            bis = new BufferedInputStream(is);
            bos = new BufferedOutputStream(out);
            byte[] buff = new byte[2048];
            int bytesRead;
            // Simple read/write loop.
            while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
                bos.write(buff, 0, bytesRead);
            }
        } catch (final IOException e) {
            throw e;
        } finally {
            if (bis != null)
                bis.close();
            if (bos != null)
                bos.close();
        }
	}
	
	
	
	/*
	 * 导出 助理 订单 列表
	 */
	@RequestMapping(value = "export_am_order",method = RequestMethod.GET)
	public void exportAmOrderTable(OaOrderSearchVo oaOrderSearchVo,HttpServletRequest request,HttpServletResponse response) throws IOException, ParseException{
		
		int pageNo = ServletRequestUtils.getIntParameter(request,
				ConstantOa.PAGE_NO_NAME, ConstantOa.DEFAULT_PAGE_NO);
		int pageSize = ServletRequestUtils.getIntParameter(request,
				ConstantOa.PAGE_SIZE_NAME, ConstantOa.DEFAULT_PAGE_SIZE);
		/**
		 * 
		 *  1. 导出时，附带 搜索条件
		 * 
		 */
		PageHelper.startPage(pageNo, pageSize);
		
		//得到 当前登录 的 门店id，并作为搜索条件
		Long sessionOrgId = AuthHelper.getSessionLoginOrg(request);
		
		List<Long> cloudIdList = new ArrayList<Long>();
		
		if (sessionOrgId > 0L) {
			
			OrgSearchVo searchVo = new OrgSearchVo();
			searchVo.setParentId(sessionOrgId);
			searchVo.setOrgStatus((short) 1);
			
			List<Orgs> cloudList = orgService.selectBySearchVo(searchVo);
			
			for (Orgs orgs : cloudList) {
				cloudIdList.add(orgs.getOrgId());
			}
			
			List<Short> searchOrderStatusList = new ArrayList<Short>();
			
			searchOrderStatusList.add(Constants.ORDER_AM_STATUS_4);
			searchOrderStatusList.add(Constants.ORDER_AM_STATUS_5);
			searchOrderStatusList.add(Constants.ORDER_AM_STATUS_7);
			searchOrderStatusList.add(Constants.ORDER_AM_STATUS_9);
			
			oaOrderSearchVo.setSearchOrderStatusList(searchOrderStatusList);
			
		}else{
			// 如果是 运营 人员，则能  查看全部 订单, 查看所有 云店
			OrgSearchVo searchVo = new OrgSearchVo();
			searchVo.setIsCloud((short) 1);
			searchVo.setOrgStatus((short) 1);
			List<Orgs> cloudOrgList = orgService.selectBySearchVo(searchVo);
			
			for (Orgs orgs : cloudOrgList) {
				cloudIdList.add(orgs.getOrgId());
			}
			
			/*
			 *  对于   未派工的  订单，，没有 云店， 保证 运营和 市场 人员可以看到即可 ，以便 后续处理
			 */
			cloudIdList.add(0L);
		}
		
		// 根据 登录 角色的门店，确定 云店
		oaOrderSearchVo.setSearchCloudOrgIdList(cloudIdList);
		
		
		//如果在 列表页面，选择了 云店  作为搜索 条件
		String jspOrgId = request.getParameter("orgId");
		if(!StringUtil.isEmpty(jspOrgId) && !jspOrgId.equals("0")){
			
			cloudIdList.clear();
			cloudIdList.add(Long.valueOf(jspOrgId));
			oaOrderSearchVo.setSearchCloudOrgIdList(cloudIdList);
		}
		
		
		/*
		 *  2016年4月13日11:10:21
		 *  处理  与 助理 相关的 统计的 数据下钻。 
		 *  
		 *  统计图表，统计的是 助理下的服务大类。如 贴心家事
		 *  
		 *  而此处 列表页是 服务大类的  子类，如贴心家事--安心托管、、、
		 *  
		 *  需要 转换大类-->子类集合
		 */
		Long parentServiceType = oaOrderSearchVo.getParentServiceType();
		
		if(parentServiceType != null && parentServiceType != 0L){
			
			List<Long> list = partService.selectChildIdByParentId(parentServiceType);
			
			oaOrderSearchVo.setChildServiceTypeList(list);
		}
		
		//转换为数据库 参数字段
		String startTimeStr = oaOrderSearchVo.getStartTimeStr();
		if(!StringUtil.isEmpty(startTimeStr)){
			oaOrderSearchVo.setStartTime(DateUtil.getUnixTimeStamp(DateUtil.getBeginOfDay(startTimeStr)));
		}
		
		String endTimeStr = oaOrderSearchVo.getEndTimeStr();
		if(!StringUtil.isEmpty(endTimeStr)){
			oaOrderSearchVo.setEndTime(DateUtil.getUnixTimeStamp(DateUtil.getEndOfDay(endTimeStr)));
		}
		
		oaOrderSearchVo.setOrderType(Constants.ORDER_TYPE_2);
		
//		List<Orders> orderList = oaOrderService.selectVoByListPage(oaOrderSearchVo,pageNo,pageSize);
		
		/*
		 * 取数据时,不使用分页，可以全部导出到一张表
		 */
		List<Orders> orderList = orderMapper.selectOaOrderByListPage(oaOrderSearchVo);
		
		/*
		 * 2. 转换导出字段为  页面的 vo字段
		 */
		List<OaOrderListNewVo> voList = new ArrayList<OaOrderListNewVo>();
		
		for (Orders orders : orderList) {
			voList.add(oaOrderService.completeNewVo(orders));
		}
		
		String fileName="深度服务订单列表";
        
		/*
		 * 3. 映射 excel 字段和数据
		 */
        List<Map<String,Object>> list= poiExcelService.createExcelRecord(voList);
        
        String columnNames[]={"门店名称","云店名称","服务人员","下单时间",
        					"订单类型","服务日期","用户手机号","服务地址",
        					"订单状态","支付方式","总金额","支付金额"};//列名
        
        String keys[] =  {"orgName","cloudOrgName","staffName","addTime",
        				 "orderTypeName","serviceDate","mobile","orderAddress",
        				 "orderStatusName","payTypeName","orderMoney","orderPay"};//map中的key
        
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            POIUtils.createWorkBook(list,keys,columnNames).write(os);
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] content = os.toByteArray();
        InputStream is = new ByteArrayInputStream(content);
        // 设置response参数，可以打开下载页面
        response.reset();
        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        response.setHeader("Content-Disposition", "attachment;filename="+ new String((fileName + ".xls").getBytes(), "iso-8859-1"));
        ServletOutputStream out = response.getOutputStream();
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        try {
            bis = new BufferedInputStream(is);
            bos = new BufferedOutputStream(out);
            byte[] buff = new byte[2048];
            int bytesRead;
            // Simple read/write loop.
            while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
                bos.write(buff, 0, bytesRead);
            }
        } catch (final IOException e) {
            throw e;
        } finally {
            if (bis != null)
                bis.close();
            if (bos != null)
                bos.close();
        }
	}
	
	
}
