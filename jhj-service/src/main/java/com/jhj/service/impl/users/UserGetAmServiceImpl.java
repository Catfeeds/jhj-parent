package com.jhj.service.impl.users;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jhj.po.dao.order.OrderDispatchsMapper;
import com.jhj.po.dao.order.OrderPricesMapper;
import com.jhj.po.dao.order.OrdersMapper;
import com.jhj.po.model.bs.OrgStaffTags;
import com.jhj.po.model.bs.OrgStaffs;
import com.jhj.po.model.bs.Orgs;
import com.jhj.po.model.bs.Tags;
import com.jhj.po.model.order.OrderDispatchs;
import com.jhj.po.model.order.OrderPrices;
import com.jhj.po.model.order.Orders;
import com.jhj.po.model.user.UserRefAm;
import com.jhj.service.bs.OrgStaffSkillService;
import com.jhj.service.bs.OrgStaffTagsService;
import com.jhj.service.bs.OrgStaffsService;
import com.jhj.service.bs.OrgsService;
import com.jhj.service.bs.TagsService;
import com.jhj.service.order.OrderStatService;
import com.jhj.service.order.OrdersService;
import com.jhj.service.university.PartnerServiceTypeService;
import com.jhj.service.users.UserGetAmService;
import com.jhj.service.users.UserRefAmService;
import com.jhj.vo.order.OrderDispatchSearchVo;
import com.jhj.vo.order.OrderSearchVo;
import com.jhj.vo.user.UserGetAmVo;
import com.meijia.utils.BeanUtilsExp;
import com.meijia.utils.DateUtil;
import com.meijia.utils.MathBigDecimalUtil;
import com.meijia.utils.OneCareUtil;
import com.meijia.utils.StringUtil;
import com.meijia.utils.TimeStampUtil;

/**
 *
 * @author :hulj
 * @Date : 2015年8月11日下午5:26:44
 * @Description: 
 *	公共service：	
 *		1、 用户版--用户查看助理页签
 *      2、 助理版 -- 我的
 */
@Service
public class UserGetAmServiceImpl implements UserGetAmService {
	
	@Autowired
	private UserRefAmService userRefAmService;
	@Autowired
	private OrgStaffsService orgStaffService;
	@Autowired
	private OrgStaffTagsService orgStaTagService;
	@Autowired
	private TagsService tagService;
	@Autowired
	private OrdersService orderService;
	@Autowired
	private OrdersMapper orderMapper;
	@Autowired
	private OrderPricesMapper orderPriceMapper;
	@Autowired
	private OrgsService orgService;
	
	@Autowired
	private OrgStaffSkillService skillService;
	
	@Autowired
	private PartnerServiceTypeService partService;
	
	@Autowired
	private OrderStatService orderStatService;
	
	@Autowired
	private OrderDispatchsMapper disMapper;
	
	/*
	 * 用户版--用户查看助理页签
	 */
	@Override
	public UserGetAmVo getAmVoByUserId(Long userId) {
		
		UserRefAm userRefAm = userRefAmService.selectByAmId(userId);
		UserGetAmVo Vo = new UserGetAmVo();
		if (userRefAm != null) {
			
		
		Long staffId = userRefAm.getStaffId();
		
		//公共代码块。处理身份证号、助理技能
		UserGetAmVo userGetAmVo = commonConvert(staffId);
		
		//亲密度。订单数，订单状态 >4 只要是已支付过的 订单，都可以算做 一条 “亲密度 ”
		Map<String, Long> map = new HashMap<String, Long>();
		
		map.put("userId", userId);
		map.put("amId", staffId);
		
		int orderNum = orderStatService.totalIntimacyOrders(map);
		
		userGetAmVo.setOrderNum(orderNum);
		
		
		return userGetAmVo;
		}
		return Vo;
	}
	
	/*
	 * 助理版--我的
	 */
	@Override
	public UserGetAmVo getAmVoByAmId(Long amId) {
		
		UserGetAmVo userGetAmVo = commonConvert(amId);
		
		/*
		 * 所属门店
		 */
		
		OrgStaffs orgStaffs = orgStaffService.selectByPrimaryKey(amId);
		
		Long orgId = orgStaffs.getOrgId();
		
		Orgs orgs = orgService.selectByPrimaryKey(orgId);
		if(orgs==null){
			userGetAmVo.setOrgName("");
		}else {
			userGetAmVo.setOrgName(orgs.getOrgName());
		}
		
		/*
		 * 助理流水,  当月该助理   所有 助理预约单 的 订单金额 总和
		 */
		
		String format = com.meijia.utils.DateUtil.format(new Date(), "YYYYMM");
		
		//本月该助理  所有助理预约单( 订单状态> 3 && !=9 )  
		OrderSearchVo searchVo = new OrderSearchVo();
		searchVo.setOrderType((short) 2);
		searchVo.setAmId(amId);
		List<Short> orderStatusList = new ArrayList<Short>();
		orderStatusList.add((short) 4);
		orderStatusList.add((short) 5);
		orderStatusList.add((short) 6);
		orderStatusList.add((short) 7);
		searchVo.setOrderStatusList(orderStatusList);
		
		int year = DateUtil.getYear();
		int month = DateUtil.getMonth();
		Long startTime = TimeStampUtil.getBeginOfMonth(year, month);
		Long endTime = TimeStampUtil.getEndOfMonth(year, month);
		searchVo.setStartUpdateTime(startTime);
		searchVo.setEndUpdateTime(endTime);
		List<Orders> amOrderList = orderMapper.selectBySearchVo(searchVo);
		
		List<Long> orderIdList = new ArrayList<Long>();
		
		BigDecimal sumMoney  = new BigDecimal(0);
		
		if(amOrderList.size()>0){
			for (Orders order : amOrderList) {
				orderIdList.add(order.getId());
			}
			
			
			List<OrderPrices> priceList = orderPriceMapper.selectByOrderIds(orderIdList);
			
			//本月流水（订单总额 + 退款手续费 - 退款）的总和
			
			for (OrderPrices orderPrices : priceList) {
				
				BigDecimal orderMoney = orderPrices.getOrderMoney();
				
				BigDecimal orderPayBackFee = orderPrices.getOrderPayBackFee();
				
				BigDecimal orderPayBack = orderPrices.getOrderPayBack();
				
				BigDecimal subtract = orderMoney.add(orderPayBackFee).subtract(orderPayBack);
				
				sumMoney = MathBigDecimalUtil.add(sumMoney, subtract);
				
			}
			
		}
		
		userGetAmVo.setSumMoney(sumMoney);
		
		return userGetAmVo;
	}
	
	
	/*
	 * 助理版--我的
	 */
	@Override
	public UserGetAmVo getStaffByUserId(Long userId, Long staffId) {
		
		//公共代码块。处理身份证号、助理技能
		UserGetAmVo userGetAmVo = commonConvert(staffId);
		
		// 计算  服务人员对  用户的 服务次数。。只要有 派工记录就算，
		OrderDispatchSearchVo searchVo = new OrderDispatchSearchVo();
		
		searchVo.setUserId(userId);
		searchVo.setStaffId(staffId);
		
		//从派工表确定服务次数
		List<OrderDispatchs> disList = disMapper.selectBySearchVo(searchVo);
		
		int orderNum = disList.size();
		
		//服务次数
		userGetAmVo.setOrderNum(orderNum);
		
		
		List<OrgStaffTags> list = orgStaTagService.selectByStaffId(staffId);
		
		List<Long> tagIdList = new ArrayList<Long>();
		
		for (OrgStaffTags orgStaffTags : list) {
			tagIdList.add(orgStaffTags.getTagId());
		}
		
		List<Tags> tagsList = tagService.selectByIds(tagIdList);
		
		// 标签
		userGetAmVo.setTagList(tagsList);
		
		/*
		 *  技能
		 */
//		List<OrgStaffSkill> skillList = skillService.selectByStaffId(staffId);
//		
//		// 当前 staff 会的 所有技能 ， 录入时决定了。只能是  二级技能
//		List<Long> staffSkillList = new ArrayList<Long>();
//		
//		if(skillList.size()<=0){
//			staffSkillList.add(0L);
//		}
//		
//		for (OrgStaffSkill orgStaffSkill : skillList) {
//			staffSkillList.add(orgStaffSkill.getServiceTypeId());
//		}
//		
//		// 根据 员工会的 二级 技能-->  {一级技能id, 二级技能名称(属于分组一级技能、员工会)  }
//		List<AmSkillVo> amSkillList = partService.selectSkillNameAndParent(staffSkillList);
//		
//		
//		// <一级名称，二级名称集合>
//		Map<String, List<String>> skillNameMap = new LinkedHashMap<String, List<String>>();
//		
//		for (AmSkillVo amSkillVo : amSkillList) {
//			
//			Long firstServiceType = amSkillVo.getFirstServiceType();
//			
//			PartnerServiceType serviceType = partService.selectByPrimaryKey(firstServiceType);
//			
//			//一级技能 名称
//			String name = serviceType.getName();
//			
//			// 二级技能 名称 string
//			String childrenName = amSkillVo.getChildrenServiceName();
//			
//			String[] convertStrToArray = StringUtil.convertStrToArray(childrenName);
//			
//			//二级技能名称 list 集合
//			List<String> asList = Arrays.asList(convertStrToArray);
//			
//			skillNameMap.put(name, asList);
//			
//		}
//		// 技能树
//		userGetAmVo.setSkillMap(skillNameMap);
		
		return userGetAmVo;
	}	
	
	/*
	 * 
	 *  公用代码段。。处理 身份证号 和 助理技能
	 */
	public UserGetAmVo commonConvert(Long staffId){
		
		UserGetAmVo userGetAmVo = initUserGetAmVo();
		//助理基本信息 -- 每个用户 只绑定一个助理
		OrgStaffs orgStaffs = orgStaffService.selectByPrimaryKey(staffId);
		BeanUtilsExp.copyPropertiesIgnoreNull(orgStaffs,userGetAmVo);
		
		/*
		 * 处理 身份证号，中间用 **显示
		 */
		
		String cardId = orgStaffs.getCardId();
		
		if(!StringUtil.isEmpty(cardId)){
			String cardIds = com.meijia.utils.StringUtil.replaceCardIdByStar(cardId);
			userGetAmVo.setCardId(cardIds);
		}
		
		
		//助理技能
		List<OrgStaffTags> orgStaTagList = orgStaTagService.selectByStaffId(staffId);
		
		List<Long> tagIdList = new ArrayList<Long>();
		if(orgStaTagList.size()>0){
			for (OrgStaffTags orgStaTags : orgStaTagList) {
				tagIdList.add(orgStaTags.getTagId());
			}
			
			List<Tags> tagList = tagService.selectByIds(tagIdList);
			userGetAmVo.setTagList(tagList);
		}else{
			userGetAmVo.setTagList(new ArrayList<Tags>());
		}
		
		//星座
		String astroName =  OneCareUtil.getAstroName(userGetAmVo.getAstro());
		userGetAmVo.setAstroName(astroName);
		
		//年龄
		String age = "";
		try {
			age = DateUtil.getAge(userGetAmVo.getBirth());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (!StringUtil.isEmpty(age)) age = age + "岁";
		userGetAmVo.setAge(age);
		
		//血型
		String bloodTypeName = OneCareUtil.getBloodTypeName(userGetAmVo.getBloodType());
		userGetAmVo.setBloodType(bloodTypeName);
		return userGetAmVo;
	}
	
	@Override
	public UserGetAmVo initUserGetAmVo(){
		
		UserGetAmVo amVo = new UserGetAmVo();
		OrgStaffs orgStaffs = orgStaffService.initOrgStaffs();
		BeanUtilsExp.copyPropertiesIgnoreNull(orgStaffs, amVo);
		
		amVo.setTagList(new ArrayList<Tags>());
		amVo.setOrderNum(0);
		amVo.setSumMoney(new BigDecimal(0));
		amVo.setOrgName("");
		amVo.setCardId("");
		
		return amVo;
	}
	
}
