package com.jhj.service.impl.order;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jhj.common.Constants;
import com.jhj.po.dao.bs.OrgStaffTagsMapper;
import com.jhj.po.dao.dict.DictServiceAddonsMapper;
import com.jhj.po.dao.order.OrdersMapper;
import com.jhj.po.model.bs.OrgStaffTags;
import com.jhj.po.model.bs.OrgStaffs;
import com.jhj.po.model.bs.Orgs;
import com.jhj.po.model.bs.Tags;
import com.jhj.po.model.dict.DictServiceAddons;
import com.jhj.po.model.dict.DictServiceTypes;
import com.jhj.po.model.order.OrderDispatchs;
import com.jhj.po.model.order.OrderPrices;
import com.jhj.po.model.order.OrderServiceAddons;
import com.jhj.po.model.order.Orders;
import com.jhj.po.model.university.PartnerServiceType;
import com.jhj.po.model.user.UserAddrs;
import com.jhj.po.model.user.Users;
import com.jhj.service.bs.OrgStaffTagsService;
import com.jhj.service.bs.OrgStaffsService;
import com.jhj.service.bs.OrgsService;
import com.jhj.service.bs.TagsService;
import com.jhj.service.dict.ServiceAddonsService;
import com.jhj.service.dict.ServiceTypeService;
import com.jhj.service.order.OrderDispatchsService;
import com.jhj.service.order.OrderHourAddService;
import com.jhj.service.order.OrderPricesService;
import com.jhj.service.order.OrderServiceAddonsService;
import com.jhj.service.university.PartnerServiceTypeService;
import com.jhj.service.users.UserAddrsService;
import com.jhj.service.users.UsersService;
import com.jhj.vo.ServiceAddonSearchVo;
import com.jhj.vo.TagSearchVo;
import com.jhj.vo.order.OrderDispatchSearchVo;
import com.jhj.vo.order.OrderSearchVo;
import com.jhj.vo.org.OrgSearchVo;
import com.jhj.vo.staff.StaffSearchVo;
import com.meijia.utils.BeanUtilsExp;
import com.meijia.utils.StringUtil;
import com.meijia.utils.baidu.BaiduPoiVo;
import com.meijia.utils.baidu.MapPoiUtil;

/**
 *
 * @author :hulj
 * @Date : 2015年7月23日下午3:52:03
 * @Description: 保洁类-订单提交  ，  接口 所需service 整合
 *
 */
@Service
public class OrderHourAddServiceImpl implements OrderHourAddService {
	
	@Autowired
	private OrdersMapper ordersMapper;
	
	@Autowired
	private UsersService usersService;
	
	@Autowired
	private UserAddrsService userAddrsService;
	
	@Autowired
	private OrgsService orgService;
	
	@Autowired
	private OrgStaffsService orgStaffService;
	
	@Autowired
	private OrgStaffTagsService orgStaTagService;
	
	@Autowired
	private OrderPricesService orderPricesService;
	
	@Autowired
	private OrderDispatchsService orderDispatchsService;
	
    @Autowired
    private  OrderServiceAddonsService  orderServiceAddonsService;
    
    @Autowired
    private ServiceTypeService dictServiceTypeService;
    
    @Autowired
    private ServiceAddonsService serviceAddonsService;    
    
	@Autowired
    private TagsService tagService;
	
    @Autowired
    private OrgStaffTagsMapper orgStaTagMapper;
    
	@Autowired
    private DictServiceAddonsMapper dictSerAddonMapper;
	
	@Autowired
	private PartnerServiceTypeService partService;
	
	/**
	 * 传参 ：  用户 地址 id(找门店) ,   用户Id(找差评)
	 * @param addrId   		用户地址ID
	 * @param userId   		用户ID
	 * @param 
	 * 
	 * 返回 ： 距离最近 门店 ，符合派工逻辑 的  阿姨 Id
	 * 
	 * 实现过程
	 * 1.  找出用户地址，与对应的门店地址列表进行百度地图的距离和时间计算 ，得到集合A
	 * 2.  排除掉集合A距离大于10公里和时间超过60分钟的门店，并且得到最配合的门店ID org1
	 * 3.  找出org1 符合时间段内可用的阿姨列表得到集合B
	 * 4.  排除集合B中，用户已经差评过的。
	 * 5.  优先现在原则如下：
	 * 
	 *      保洁订单优先选择单独保洁技能的阿姨，如没有可选择有其他技能的人。
	 *      做饭订单只选择有做饭技能的阿姨。
	 *      洗衣订单只选择有洗衣技能的阿姨。
	 *      做饭+洗衣只选择有做饭技能和洗衣技能的阿姨。
	 */
	@Override
	//public Long getBestOrgStaff(Long addrId,Long userId,Long serviceType, String serviceAddons,Long serviceDateTable,Short servieHour)  {
	public List<OrgStaffs> getBestOrgStaff(Long userId, Long orderId)  {
		
		List<OrgStaffs> matchOrgStaffs = new ArrayList<OrgStaffs>();
		
		//根据订单的地址， addr_id的坐标，找到最近的门店，再找到门店对应的阿姨列表，得到集合A
		Orders order = ordersMapper.selectByPrimaryKey(orderId);
		
		if (BeanUtilsExp.isNullOrEmpty(order)) return matchOrgStaffs;
		
		
		Long addrId = order.getAddrId();
		if (addrId == null || addrId.equals(0L)) return matchOrgStaffs;
		
		
		
		Long serviceDate = order.getServiceDate();
		double serviceHour = order.getServiceHour();
		
			
		//根据服务地址经纬度获得符合的门店
		Orgs matchOrg = getMatchOrgId(addrId);
		
		if (BeanUtilsExp.isNullOrEmpty(matchOrg)) return matchOrgStaffs;
		Long orgId = matchOrg.getOrgId();

		/*
		 *
		 *    1. 找出匹配门店下所有状态可用的阿姨,得到集合A
		 *    2. 找出该订单下开始时间 - 结束时间已经派工的阿姨,得到集合B
		 *    3. 找出已经被当前用户差评过的阿姨,得到集合C
		 *    4. 集合A移除掉集合B和集合C，得到集合D
		 *    5. 找出集合D中满足订单技能的阿姨列表, 查找原则为
		 *       1）保洁订单优先选择单独保洁技能的阿姨，如没有可选择有其他技能的人。
		 *       2）做饭订单只选择有做饭技能的阿姨。
		 *       3）洗衣订单只选择有洗衣技能的阿姨。
		 *       4）做饭+洗衣只选择有做饭技能和洗衣技能的阿姨.
		 * 
		 */

		// 1. 找出匹配门店下所有状态可用的阿姨,得到集合A，
		StaffSearchVo searchVo = new StaffSearchVo();
		searchVo.setOrgId(orgId);
		searchVo.setStaffType((short) 0);
		List<OrgStaffs> orgStaffAllList = orgStaffService.selectBySearchVo(searchVo);
		
		if (orgStaffAllList.isEmpty()) return matchOrgStaffs;
		
		//所有可用阿姨 Id的集合
		List<Long> staffAllIds = new ArrayList<Long>();
		for (OrgStaffs item : orgStaffAllList) {
			staffAllIds.add(item.getStaffId());
		}		
		
		//结束时间 = 服务开始秒值+ 服务时长秒值
		Long serviceDateEnd = (long) (serviceDate + serviceHour * 3600) ;
		
		OrderDispatchSearchVo searchVo1 = new OrderDispatchSearchVo();
		searchVo1.setOrgId(orgId);
		searchVo1.setStartServiceTime(serviceDate);
		searchVo1.setEndServiceTime(serviceDateEnd);
		List<OrderDispatchs> disList = orderDispatchsService.selectByMatchTime(searchVo1);
						
		// 2. 找出该订单下开始时间 - 结束时间已经派工的阿姨,得到集合B
		List<Long> disStaffIds = new ArrayList<Long>();
		for (OrderDispatchs item : disList) {
			if (!disStaffIds.contains(item.getStaffId())) {
				disStaffIds.add(item.getStaffId());
			}
		}
		
		// 3. 找出已经被当前用户差评过的阿姨,得到集合C
		List<Long> badRateStaffIds = getBadRateStaffIds(userId, orgId);
				

		//4. 集合A移除掉集合B和集合C，得到集合D 排除差评和已经派工的阿姨
		if (!badRateStaffIds.isEmpty()) {
			staffAllIds.removeAll(badRateStaffIds);
		}
				
		//排除 差评 后。。排除 已经派工的 阿姨
		if (!disStaffIds.isEmpty()) {
			staffAllIds.removeAll(disStaffIds);
		}
		
		if (staffAllIds.isEmpty()) return matchOrgStaffs;
		
		//5. 找出集合D中满足订单技能的阿姨列表
		List<Long> matchStaffIds = getMatchTagStaffIds(orderId, staffAllIds);
		
		if (matchStaffIds.isEmpty()) return matchOrgStaffs;
		
		for (OrgStaffs item : orgStaffAllList) {
			if (matchStaffIds.contains(item.getStaffId())) {
				matchOrgStaffs.add(item);
			}
		}

		return matchOrgStaffs;	
		
	}
	
	/** 根据服务地址经纬度找出最符合的门店, 距离不超过10公里和60分钟.
	 *  @param addrId
	 *  @reutn
	 *  
	 */
	@Override
	public Orgs getMatchOrgId(Long addrId) {
		Orgs matchOrg = null;
		
		UserAddrs userAddrs = userAddrsService.selectByPrimaryKey(addrId);
		
		if (BeanUtilsExp.isNullOrEmpty(userAddrs)) return matchOrg;
		
		//1.服务地点： 	 根据经纬度确定 服务地点
		String fromLat = userAddrs.getLatitude();
		String fromLng = userAddrs.getLongitude();
		
		//目标地点：所有门店详细地址的集合 、、可以分页取。每次5个。
		OrgSearchVo searchVo = new OrgSearchVo();
		searchVo.setIsCloud((short) 1);
		searchVo.setOrgStatus((short) 1);
		List<Orgs> orgList = orgService.selectBySearchVo(searchVo);
		
		if (orgList.isEmpty()) return matchOrg;
		
		List<BaiduPoiVo> orgAddrList = new ArrayList<BaiduPoiVo>();
		for (Orgs org : orgList) {
			BaiduPoiVo baiduPoiVo = new BaiduPoiVo();
			
			baiduPoiVo.setLat(org.getPoiLatitude());
			baiduPoiVo.setLng(org.getPoiLongitude());
			baiduPoiVo.setName(org.getOrgName());
			
			orgAddrList.add(baiduPoiVo);
		}
		
		/*
		 * 得到最近的  门店 地理位置名称（目前暂时取时间最短。未考虑时间一样的情况）      
		 * TODO,百度只支持 最多 5个 目标地点的距离检索
		 */
		
		Orgs item = null;
		try {
			List<BaiduPoiVo> destList = MapPoiUtil.getMapRouteMatrix(fromLat, fromLng, orgAddrList);
			List<BaiduPoiVo> baiduPoiVos = MapPoiUtil.getMinDest(destList, Constants.MAX_DISTANCE);
			BaiduPoiVo baiduPoiVo = null;
			
			if (!baiduPoiVos.isEmpty()) {
				baiduPoiVo = baiduPoiVos.get(0);
			}
			
			for (int i =0; i < orgList.size(); i++) {
				item = orgList.get(i);
				/*
				 * 如果 某个 门店 的经纬度 ，和 util中计算出的 经纬度 一致。。则为 最近门店
				 */
				if (baiduPoiVo.getLat().equals(item.getPoiLatitude()) &&
					baiduPoiVo.getLng().equals(item.getPoiLongitude())) {
					matchOrg = item;
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return matchOrg;
		} 
		return matchOrg;
	}
	
	/*
	 * @param userId 当前用户 userId
	 * @param orgId
	 * 返回： 该用户 在 该门店 的 差评 过 的 阿姨 的集合
	 * 
	 */
	@Override
	public List<Long> getBadRateStaffIds(Long userId, Long orgId) {
		List<Long> badStaffIds = new ArrayList<Long>();
		//根据 userId得到 该用户的 所有  差评 order
		
		OrderSearchVo searchVo = new OrderSearchVo();
		searchVo.setUserId(userId);
		searchVo.setOrderRate((short) 2);
		
		List<Orders> orderList = ordersMapper.selectBySearchVo(searchVo);
		
		//如果没有差评。返回 一个 空 list ,即可
		if(orderList.isEmpty() || orderList ==null){
			return badStaffIds;
		}
		
		List<Long> orderIds = new ArrayList<Long>();

		for (Orders order : orderList) {
			orderIds.add(order.getId());
		}
		
		OrderDispatchSearchVo searchVo1 = new OrderDispatchSearchVo();
		searchVo1.setOrgId(orgId);
		searchVo1.setOrderIds(orderIds);
		List<OrderDispatchs> disPatchList = orderDispatchsService.selectBySearchVo(searchVo1);
		
		for (OrderDispatchs ord : disPatchList) {
			badStaffIds.add(ord.getStaffId());
		}
		
		return badStaffIds;
	}
	
	
	/*
	 * 参数： 订单服务类型， 附加服务 id, 服务时长
	 * 
	 * 返回： 设置了属性 orderMoney （订单总金额） 的  orderPrice对象
	 */
	@Override
	public OrderPrices getOrderPriceSum(Long serviceType, String serviceAddons, Short serviceHour) {
		
		OrderPrices orderPrices = orderPricesService.initOrderPrices();
		
		//订单总金额 = (附加服务价格（根据选择的附加服务种类的价格，可能有好几种附加服务）  + 基本单价  ) * 服务时长 
		
		// 单价 = 订单种类确定的基本单价 + 附加服务确定的服务单价 
		DictServiceTypes selectByServiceType = dictServiceTypeService.selectByServiceType(serviceType);
		
		BigDecimal realPrice = selectByServiceType.getPrice();
		
		if (!StringUtil.isEmpty(serviceAddons)) {
			String[] serviceAddonsArray = StringUtil.convertStrToArray(serviceAddons);
			for (int i = 0; i < serviceAddonsArray.length; i++) {
				Long serviceAddonId = Long.valueOf(serviceAddonsArray[i]);
				//这里 的 附加服务 单价，从最新 的  dict_service_addons表中 取，
				// order_service_addons 表中 的单价，可以视为“历史 单价”
				
				DictServiceAddons dictServiceAddons = serviceAddonsService.selectByPrimaryKey(serviceAddonId);
				
				if(dictServiceAddons.getPrice()!=null){
					realPrice =  dictServiceAddons.getPrice().add(realPrice);
				}
			}
		}	
		
		//订单总金额
		BigDecimal orderMoney =  realPrice.multiply(new BigDecimal(serviceHour.doubleValue()));
		
		orderPrices.setOrderMoney(orderMoney);
		orderPrices.setOrderPay(orderMoney);
		
		return orderPrices;
	}
		
	/*  
	 * 	!派工逻辑！
	 * 
	 *    5. 找出集合D中满足订单技能的阿姨列表, 查找原则为
	 *       1）保洁订单优先选择单独保洁技能的阿姨，如没有可选择有其他技能的人。
	 *       2）做饭订单只选择有做饭技能的阿姨。
	 *       3）洗衣订单只选择有洗衣技能的阿姨。
	 *       4）做饭+洗衣只选择有做饭技能和洗衣技能的阿姨.
	 */
	@Override
	public List<Long> getMatchTagStaffIds(Long orderId, List<Long> staffIds){
		
		Orders order = ordersMapper.selectByPrimaryKey(orderId);
		String orderNo = order.getOrderNo();
		List<OrderServiceAddons> orderServiceAddonList = orderServiceAddonsService.selectByOrderNo(orderNo);
		//去掉清洁用品，不需要进行比较
		
		OrderServiceAddons itemRm = null;
		for (int i = 0; i <  orderServiceAddonList.size(); i++) {
			itemRm = orderServiceAddonList.get(i); 
			if (itemRm.getServiceAddonId().equals(8L)) {
				orderServiceAddonList.remove(i);
			}
		}
		
		if(staffIds.isEmpty()){
			staffIds.add(0L);
		}
		
		//得到阿姨对应的所有技能列表.
		List<OrgStaffTags> staffTagList = orgStaTagService.selectByStaffIds(staffIds);
		
		List<Long> hasTagStaffIds = new ArrayList<Long>();
		for (OrgStaffTags item : staffTagList) {
			if (!hasTagStaffIds.contains(item.getStaffId())) {
				hasTagStaffIds.add(item.getStaffId());
			}
		}
		
		//1)保洁订单优先选择单独保洁技能的阿姨，如没有可选择有其他技能的人。
		if (orderServiceAddonList.isEmpty()) {
			List<Long> returnStaffIds = staffIds;
			returnStaffIds.remove(hasTagStaffIds);
			if (returnStaffIds.isEmpty()) {
				return staffIds;
			} else {
				return returnStaffIds;
			}
		}
		
		//获得选择的附加服务的对象.
		List<Long> serviceAddonIds = new ArrayList<Long>();
		for (OrderServiceAddons item : orderServiceAddonList) {
			serviceAddonIds.add(item.getServiceAddonId());
		}
		
		ServiceAddonSearchVo searchVo1 = new ServiceAddonSearchVo();
		searchVo1.setServiceAddonIds(serviceAddonIds);
		List<DictServiceAddons> serviceAddons = serviceAddonsService.selectBySearchVo(searchVo1);
		
		//获得所有阿姨tags的名称.
		TagSearchVo searchVo3 = new TagSearchVo();
		searchVo3.setTagType((short) 0);
		List<Tags> tagList = tagService.selectBySearchVo(searchVo3);
		
		List<Long> tagIds = new ArrayList<Long>();
		//tags名称与选择的服务类型进行比较.
		for (Tags item1 : tagList) {
			for (DictServiceAddons item2 : serviceAddons) {
				if (item1.getTagName().equals(item2.getName())) {
					tagIds.add(item1.getTagId());
				}
			}
		}
		
		List<Long> matchTagStaffIds = new ArrayList<Long>();
		for (OrgStaffTags item : staffTagList) {
			if (tagIds.contains(item.getTagId()) ) {
				matchTagStaffIds.add(item.getStaffId());
			}
		}
		
		int hasTagCount = orderServiceAddonList.size();
		
		/**
		 * ArrayList<String> animals = new ArrayList<String>();
		 *	animals.add("bat");
		 *	animals.add("owl");
		 *	animals.add("bat");
		 *  animals.add("bat");
		 *  
		 *  Collections.frequency(animals, "bat");
		 */
		List<Long> matchStaffIds = new ArrayList<Long>();
		for (int i = 0 ; i < staffIds.size(); i++) {
			int matchCount = Collections.frequency(matchTagStaffIds, staffIds.get(i));
			if (matchCount == hasTagCount) {
				matchStaffIds.add(staffIds.get(i));
			}
		}
		
		return matchStaffIds;
	}
	
	
	/**
	 * 	2016年3月14日18:04:49 
	 * 
	 * 		jhj2.1 得到 保洁服务  订单价格
	 * 
	 */
	@Override
	public OrderPrices getNewOrderPrice(Orders order, Long serviceType) {
		
		OrderPrices prices = orderPricesService.initOrderPrices();
		
		PartnerServiceType type = partService.selectByPrimaryKey(serviceType);
		
		//非会员价 单价
		BigDecimal price = type.getPrice();
		
		//会员价单价
		BigDecimal mprice = type.getMprice();
		
		//非会员价套餐价
		BigDecimal pprice = type.getPprice();
		
		//会员价套餐价格
		BigDecimal mpprice = type.getMpprice();
		
		BigDecimal orderOriginHourPay = price;
		BigDecimal orderOriginPay = pprice;
		BigDecimal orderHourPay = price;
		BigDecimal orderPay = pprice;
		
		Long userId = order.getUserId();
		Users u = usersService.selectByPrimaryKey(userId);
		
		int isVip = u.getIsVip();
		if (isVip == 1) {
			orderHourPay = mprice;
			orderPay = mpprice;
		}
		
		int staffNums = order.getStaffNums();
		double serviceHour = order.getServiceHour();
		
		if (staffNums > 1 || serviceHour > type.getServiceHour()) {
			BigDecimal tmpPrice = orderHourPay.multiply(new BigDecimal(serviceHour));
			tmpPrice = tmpPrice.multiply(new BigDecimal(staffNums));
			orderPay = tmpPrice;
			
			BigDecimal tmpOriginPrice = orderOriginHourPay.multiply(new BigDecimal(serviceHour));
			tmpOriginPrice = tmpOriginPrice.multiply(new BigDecimal(staffNums));
			orderOriginPay = tmpOriginPrice;
			
		}
		prices.setOrderOriginPrice(orderOriginPay);
		prices.setOrderPrimePrice(orderPay);
		prices.setOrderMoney(orderPay);
		prices.setOrderPay(orderPay);
		
		return prices;
	}
}
