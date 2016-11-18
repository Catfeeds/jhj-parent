package com.jhj.service.impl.order;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jhj.po.dao.order.OrderRatesMapper;
import com.jhj.po.model.bs.OrgStaffs;
import com.jhj.po.model.order.OrderRateImg;
import com.jhj.po.model.order.OrderRates;
import com.jhj.po.model.order.Orders;
import com.jhj.po.model.user.Users;
import com.jhj.service.bs.OrgStaffsService;
import com.jhj.service.dict.DictService;
import com.jhj.service.order.OrderRateImgService;
import com.jhj.service.order.OrderRatesService;
import com.jhj.service.order.OrdersService;
import com.jhj.service.university.PartnerServiceTypeService;
import com.jhj.service.users.UsersService;
import com.jhj.vo.order.OrderDispatchSearchVo;
import com.jhj.vo.order.OrderRatesVo;
import com.jhj.vo.order.OrderSearchVo;
import com.jhj.vo.order.OrderStaffRateVo;
import com.jhj.vo.staff.OrgStaffRateVo;
import com.jhj.vo.staff.StaffSearchVo;
import com.meijia.utils.BeanUtilsExp;
import com.meijia.utils.DateUtil;
import com.meijia.utils.MobileUtil;
import com.meijia.utils.StringUtil;
import com.meijia.utils.TimeStampUtil;

@Service
public class OrderRatesServiceImpl implements OrderRatesService {

	@Autowired
	private OrderRatesMapper orderRatesMapper;

	@Autowired
	private OrdersService ordersService;
	
	@Autowired
	private DictService dictService;
	
	@Autowired
	private OrgStaffsService orgStaffsService;
	
	@Autowired
	private OrderRateImgService orderRateImgService;
	
	@Autowired
	private PartnerServiceTypeService partnerServiceTypeService;
	
	@Autowired
	private UsersService userService;

	@Override
	public OrderRates initOrderRates() {

		OrderRates record = new OrderRates();
		record.setId(0L);
		record.setOrderId(0L);
		record.setOrderNo("");
		record.setStaffId(0L);
		record.setUserId(0L);
		record.setMobile("");
		record.setRateArrival(0);
		record.setRateAttitude(0);
		record.setRateSkill(0);
		record.setRateContent("");
		record.setAddTime(TimeStampUtil.getNow() / 1000);

		return record;

	}

	@Override
	public int deleteByPrimaryKey(Long id) {
		return orderRatesMapper.deleteByPrimaryKey(id);
	}

	@Override
	public int insert(OrderRates record) {
		return orderRatesMapper.insert(record);
	}

	@Override
	public int insertSelective(OrderRates record) {
		return orderRatesMapper.insertSelective(record);
	}

	@Override
	public OrderRates selectByPrimaryKey(Long id) {
		return orderRatesMapper.selectByPrimaryKey(id);
	}

	@Override
	public int updateByPrimaryKey(OrderRates record) {
		return orderRatesMapper.updateByPrimaryKey(record);
	}
	
	@Override
	public int updateByPrimaryKeySelective(OrderRates record) {
		return orderRatesMapper.updateByPrimaryKeySelective(record);
	}
	
	@Override
	public List<OrderRates> selectBySearchVo(OrderDispatchSearchVo searchVo) {
		return orderRatesMapper.selectBySearchVo(searchVo);
	}
	
	@Override
	public HashMap totalByStaff(OrderDispatchSearchVo searchVo) {
		return orderRatesMapper.totalByStaff(searchVo);
	}
	
	@Override
	public PageInfo selectByListPage(OrderDispatchSearchVo searchVo, int pageNo, int pageSize) {
		PageHelper.startPage(pageNo, pageSize);
		List<OrderRates> list = orderRatesMapper.selectByListPage(searchVo);
		
		Set<Long> set=new HashSet<Long>();
		List<OrderRates> orderRates=new ArrayList<OrderRates>();
		for(int i=0;i<list.size();i++){
			OrderRates or = list.get(i);
			if(!set.contains(or.getOrderId())){
				set.add(or.getOrderId());
			}else{
				list.remove(or);
			}
		}
		PageInfo result = new PageInfo(list);
		
		return result;
	}
	
	@Override
	public List<OrderStaffRateVo> changeToOrderStaffReteVo(List<OrderRates> list) {
		
		List<OrderStaffRateVo> result = new ArrayList<OrderStaffRateVo>();
		
		if (list.isEmpty()) return result;
		
		
		for (OrderRates item : list) {
			Long staffId = item.getStaffId();
			OrgStaffs orgStaff = orgStaffsService.selectByPrimaryKey(staffId);
			
			OrderStaffRateVo vo = new OrderStaffRateVo();
			vo.setStaffId(staffId);
			vo.setName(orgStaff.getName());
			vo.setMobile(orgStaff.getMobile());
			
			String headImg = orgStaff.getHeadImg();
			if (StringUtil.isEmpty(headImg)) headImg = "http://www.jia-he-jia.com/jhj-oa/upload/headImg/default-head-img.png";
			vo.setHeadImg(headImg);
			
			//年龄
			String age = "";
			try {
				age = DateUtil.getAge(orgStaff.getBirth());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (!StringUtil.isEmpty(age)) age = age + "岁";
			vo.setAge(age);
			
			String provinceName = dictService.getProvinceName(orgStaff.getProvinceId());
			String cityName = dictService.getCityName(orgStaff.getCityId());
			
			vo.setHukou(provinceName + cityName);
			
			vo.setIntro(orgStaff.getIntro());
			
			String skill = "初级";
			Short level = orgStaff.getLevel();
			if (level.equals((short)1)) skill = "初级";
			if (level.equals((short)2)) skill = "中级";
			if (level.equals((short)3)) skill = "金牌";
			if (level.equals((short)4)) skill = "VIP";
			vo.setSkill(skill);
			
			//统计平均到达率， 平均好评度
			int totalRateStar = 0;
			String totalArrival = ""; 
			
			
			OrderDispatchSearchVo odSearchVo = new OrderDispatchSearchVo();
			odSearchVo.setStaffId(staffId);
			HashMap<String, Object> totalRates = this.totalByStaff(odSearchVo);
			if (!totalRates.isEmpty()) {
				int totalOrders = 0;
				int totalArrival0 = 0;
				int totalAttitude = 0;
				int totalSkill = 0;
				
				if (totalRates.get("total_orders") != null)
					totalOrders = Integer.valueOf(totalRates.get("total_orders").toString());
				
				if (totalRates.get("total_arrival_0") != null)
					totalArrival0 = Integer.valueOf(totalRates.get("total_arrival_0").toString());
				
				if (totalRates.get("total_attitude") != null)
					totalAttitude = Integer.valueOf(totalRates.get("total_attitude").toString());
				
				if (totalRates.get("total_skill") != null)
					totalSkill = Integer.valueOf(totalRates.get("total_skill").toString());
				
				if (totalOrders > 0) {
					
					//平均到达率
					NumberFormat numberFormat = NumberFormat.getInstance();  
					numberFormat.setMaximumFractionDigits(0);  
					totalArrival = numberFormat.format((float) totalArrival0 / (float) totalOrders * 100) + "%";
					
					//客户好评度
					double avgAttitude = (double)totalAttitude /  (double)totalOrders;
					double avgSkill =  (double)totalSkill /  (double)totalOrders;
					double avgStar = (avgAttitude + avgSkill) / (double)2;
					totalRateStar = (int) Math.round(avgStar);
				}
			}
			
			vo.setTotalRateStar(totalRateStar);
			vo.setTotalArrival(totalArrival);
			
			result.add(vo);
		}
		
		return result;
	}

	@Override
	public OrderRatesVo transVo(OrderRates orderRate) {
		if(orderRate==null) return null;
		OrderRatesVo orderRagesVo=new OrderRatesVo();
		BeanUtilsExp.copyPropertiesIgnoreNull(orderRate, orderRagesVo);
		
		Long orderId = orderRate.getOrderId();
		
		Orders order = ordersService.selectByPrimaryKey(orderId);
		
		orderRagesVo.setServiceDateStr(TimeStampUtil.timeStampToDateStr(order.getServiceDate()*1000));
		orderRagesVo.setServiceTypeName(
				partnerServiceTypeService.selectByPrimaryKey(order.getServiceType()).getName());
		
		OrderDispatchSearchVo searchVo=new OrderDispatchSearchVo();
		searchVo.setOrderId(orderId);
		List<OrderRates> orderRatesList = this.selectBySearchVo(searchVo);
		if(orderRatesList!=null && orderRatesList.size()>0){
			Set<Long> set=new HashSet<Long>();
			for(int i=0,len=orderRatesList.size();i<len;i++){
				Long staffId = orderRatesList.get(i).getStaffId();
				set.add(staffId);
			}
			StaffSearchVo staffVo=new StaffSearchVo();
			staffVo.setStaffIds(new ArrayList<Long>(set));
			List<OrgStaffs> staffs = orgStaffsService.selectBySearchVo(staffVo);
			orderRagesVo.setStaffList(staffs);
		}
		
		
		OrderSearchVo orderSearchVo=new OrderSearchVo();
		orderSearchVo.setOrderId(orderId);
		orderSearchVo.setUserId(orderRate.getUserId());
		List<OrderRateImg> orderRateImgList = orderRateImgService.selectBySearchVo(orderSearchVo);
		if(orderRateImgList!=null && orderRateImgList.size()>0){
			List<String> imgUrls=new ArrayList<String>();
			for(int i=0,len=orderRateImgList.size();i<len;i++){
				OrderRateImg orderRateImg = orderRateImgList.get(i);
				imgUrls.add(orderRateImg.getImgUrl());
			}
			orderRagesVo.setOrderRateUrl(imgUrls);
		}
		
		return orderRagesVo;
	}
	
	@Override
	public List<OrgStaffRateVo> changeToOrgStaffReteVo(List<OrderRates> list) {
		
		List<OrgStaffRateVo> result = new ArrayList<OrgStaffRateVo>();
		
		if (list.isEmpty()) return result;
		
		
		for (OrderRates item : list) {
			OrgStaffRateVo vo = new OrgStaffRateVo();
			
			BeanUtilsExp.copyPropertiesIgnoreNull(item, vo);
			
			Long userId = item.getUserId();
			Users u = userService.selectByPrimaryKey(userId);
			
			String mobile = u.getMobile();
			mobile = MobileUtil.getMobileStar(mobile);
			
			vo.setUserId(userId);
			vo.setMobile(mobile);
			
			String userTypeStr = "普通会员";
			int isVip = u.getIsVip();
			if (isVip == 1) userTypeStr = "金牌会员";
			vo.setUserTypeStr(userTypeStr);
			
			vo.setOrderId(item.getOrderId());
			vo.setOrderNo(item.getOrderNo());
			vo.setRateArrival(item.getRateArrival());
			vo.setRateAttitude(item.getRateAttitude());
			vo.setRateContent(item.getRateContent());
			
			Long addTime = item.getAddTime();
			String addTimeStr = TimeStampUtil.timeStampToDateStr(addTime * 1000, "yyyy-MM-dd");
			vo.setAddTimeStr(addTimeStr);
			
			
			
			result.add(vo);
		}
		
		return result;
	}

}
