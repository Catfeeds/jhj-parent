package com.jhj.service.impl.users;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jhj.po.dao.order.OrderPricesMapper;
import com.jhj.po.dao.order.OrdersMapper;
import com.jhj.po.model.bs.OrgStaffTags;
import com.jhj.po.model.bs.OrgStaffs;
import com.jhj.po.model.bs.Orgs;
import com.jhj.po.model.bs.Tags;
import com.jhj.po.model.order.OrderPrices;
import com.jhj.po.model.order.Orders;
import com.jhj.po.model.user.UserRefAm;
import com.jhj.service.bs.OrgStaffTagsService;
import com.jhj.service.bs.OrgStaffsService;
import com.jhj.service.bs.OrgsService;
import com.jhj.service.bs.TagsService;
import com.jhj.service.order.OrdersService;
import com.jhj.service.users.UserGetAmService;
import com.jhj.service.users.UserRefAmService;
import com.jhj.vo.user.UserGetAmVo;
import com.meijia.utils.BeanUtilsExp;
import com.meijia.utils.DateUtil;
import com.meijia.utils.MathBigDeciamlUtil;
import com.meijia.utils.OneCareUtil;
import com.meijia.utils.StringUtil;

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
		
		int orderNum = orderService.getIntimacyOrders(map);
		
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
		List<Orders> amOrderList = orderMapper.selectByAmIdAndOrderType(amId,format);
		
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
				
				sumMoney = MathBigDeciamlUtil.add(sumMoney, subtract);
				
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
		
		//亲密度。订单数，订单状态 >4 只要是已支付过的 订单，都可以算做 一条 “亲密度 ”
		Map<String, Long> map = new HashMap<String, Long>();
		
		map.put("userId", userId);
		map.put("amId", staffId);
		
		int orderNum = orderService.getIntimacyOrders(map);
		
		userGetAmVo.setOrderNum(orderNum);
		
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
