package com.jhj.service.impl.order;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jhj.common.Constants;
import com.jhj.po.dao.bs.OrgStaffAuthMapper;
import com.jhj.po.dao.bs.OrgStaffBlackMapper;
import com.jhj.po.dao.bs.OrgStaffOnlineMapper;
import com.jhj.po.dao.bs.OrgStaffTagsMapper;
import com.jhj.po.dao.bs.OrgStaffsMapper;
import com.jhj.po.dao.order.OrderDispatchsMapper;
import com.jhj.po.dao.user.UserAddrsMapper;
import com.jhj.po.dao.user.UserPushBindMapper;
import com.jhj.po.dao.user.UserTrailRealMapper;
import com.jhj.po.model.bs.OrgStaffAuth;
import com.jhj.po.model.bs.OrgStaffBlack;
import com.jhj.po.model.bs.OrgStaffOnline;
import com.jhj.po.model.bs.OrgStaffs;
import com.jhj.po.model.order.OrderDispatchs;
import com.jhj.po.model.order.OrderLog;
import com.jhj.po.model.order.OrderPrices;
import com.jhj.po.model.order.Orders;
import com.jhj.po.model.university.PartnerServiceType;
import com.jhj.po.model.user.UserAddrs;
import com.jhj.po.model.user.UserPushBind;
import com.jhj.po.model.user.UserTrailReal;
import com.jhj.po.model.user.Users;
import com.jhj.service.dict.DictService;
import com.jhj.service.order.DispatchStaffFromOrderService;
import com.jhj.service.order.OrderDispatchsService;
import com.jhj.service.order.OrderLogService;
import com.jhj.service.order.OrderPricesService;
import com.jhj.service.order.OrdersService;
import com.jhj.service.university.PartnerServiceTypeService;
import com.jhj.service.users.UserAddrsService;
import com.jhj.vo.UserTrailVo;
import com.jhj.vo.order.OrgStaffsNewVo;
import com.meijia.utils.BeanUtilsExp;
import com.meijia.utils.GsonUtil;
import com.meijia.utils.MathBigDecimalUtil;
import com.meijia.utils.OneCareUtil;
import com.meijia.utils.PushUtil;
import com.meijia.utils.RegexUtil;
import com.meijia.utils.SmsUtil;
import com.meijia.utils.StringUtil;
import com.meijia.utils.TimeStampUtil;
import com.meijia.utils.baidu.BaiduMapUtil;
import com.meijia.utils.baidu.BaiduPoiVo;
import com.meijia.utils.baidu.MapPoiUtil;

/**
 * @description：订单派工接口实现
 * @author： kerryg
 * @date:2016年1月13日
 */
@Service
public class DispatchStaffFromOrderServiceImpl implements DispatchStaffFromOrderService {

	@Autowired
	private OrgStaffsMapper orgStaffsMapper;

	@Autowired
	private OrderDispatchsMapper orderDispatchsMapper;

	@Autowired
	private OrgStaffOnlineMapper orgStaffOnlineMapper;

	@Autowired
	private OrgStaffBlackMapper orgStaffBlackMapper;

	@Autowired
	private OrgStaffTagsMapper orgStaffTagsMapper;

	@Autowired
	private UserTrailRealMapper userTrailRealMapper;
	
	@Autowired
	private OrderHourAddServiceImpl orderHourAddServiceImpl;
	
	@Autowired
	private UserAddrsMapper userAddrsMapper;
	
	@Autowired
	private OrderDispatchsService orderDispatchService;	
	
	@Autowired
	private OrderLogService orderLogService;
	
	@Autowired
	private OrderPricesService orderPricesService;
	
	@Autowired
	private UserPushBindMapper userPushBindMapper;
	
	@Autowired
	private OrdersService ordersService;
	
	@Autowired
	private OrgStaffAuthMapper orgStaffAuthMapper;
	
	@Autowired
	private UserAddrsService userAddrsService;
	
	@Autowired
	private OrderDispatchsService orderDispatchsService;
		
	@Autowired
	PartnerServiceTypeService partnerServiceTypeService;

	/**
	 * 钟点工派工逻辑:
	 * @param startTime  服务开始时间 		
	 * @param endTime    服务结束时间
	 * @param addrId     用户地址Id
	 * @param orderId    下单订单Id
	 * 	目的：筛选出符合条件的服务人员列表
	 *  实现步骤如下：
	 *  1.找出所有的钟点工.操作表就是 org_staffs where staff_type = 0 得到集合A
	 *  2.根据集合A找出所有开工状态的钟点工，操作表是 org_staff_online，得到集合B
	 *  3.把集合B中，在U-S 时段已经有派工的排除掉，操作表为 order_dispatch, 得到集合C
	 *  4.根据集合C排除掉在黑名单表里面的数据，操作表为 org_staff_black 得到集合D
	 *  5.根据集合D，排除掉特性标签不符的数据，得到集合E, 操作表 org_staff_tags
	 *  6.根据集合E找出他们最后的地理位置的点
	 */
	@Override
	public List<OrgStaffsNewVo> getNewBestStaffForHour(Long startTime, Long endTime, Long addrId, Long orderId) {
		List<OrgStaffs> orgStaffNew = new ArrayList<OrgStaffs>();
		List<OrgStaffsNewVo> orgStaffsNewVos = new ArrayList<OrgStaffsNewVo>();
		List<Long> staffAllIds = new ArrayList<Long>();
/*
		// 1.查找所有可用服务人员
		List<OrgStaffs> orgStaffs = orgStaffsMapper.selectStaffByStaffType();// 0=服务人员
		
		//1.1 通过身份验证和钟点工考试的验证
		if (BeanUtilsExp.isNullOrEmpty(orgStaffs)) return orgStaffNew;
		List<OrgStaffAuth> orgStaffAuths =orgStaffAuthMapper.selectHourStaffByServiceTypeIdAndAuthStatus();
		orgStaffs = staffsIsAuth(orgStaffs,orgStaffAuths);
	*/
		List<OrgStaffs> orgStaffs = orgStaffsMapper.selectHourAuthStaff();// 0=服务人员
		
		// 2.查找处于开工状态的服务人员Id
		if (BeanUtilsExp.isNullOrEmpty(orgStaffs)) return orgStaffsNewVos;
		List<OrgStaffOnline> orgStaffOnlines = orgStaffOnlineMapper.selectByIsWork((short) 1);// 1=开工状态
		orgStaffs = staffsIsWork(orgStaffs, orgStaffOnlines);

		// 3.排除处于派工状态的人员
		if (BeanUtilsExp.isNullOrEmpty(orgStaffs))return orgStaffsNewVos;
		List<OrderDispatchs> orderDispatchs = orderDispatchsMapper.selectByStartTimeAndEndTime(startTime, endTime);
		orgStaffs = staffsNoDispatch(orgStaffs, orderDispatchs);

		// 4.排除处于黑名单的人员
		if (BeanUtilsExp.isNullOrEmpty(orgStaffs))return orgStaffsNewVos;
		List<OrgStaffBlack> orgStaffBlacks = orgStaffBlackMapper.selectAll();
		orgStaffs = staffsNoBlack(orgStaffs, orgStaffBlacks);

		if (BeanUtilsExp.isNullOrEmpty(orgStaffs))return orgStaffsNewVos;
		//获得过滤之后服务人员Id集合
		for (OrgStaffs item : orgStaffs) {
				staffAllIds.add(item.getStaffId());
			}	
		//5. 找出满足订单技能的服务人员列表
		List<Long> matchStaffIds = orderHourAddServiceImpl.getMatchTagStaffIds(orderId, staffAllIds);
		if (matchStaffIds.isEmpty()) return orgStaffsNewVos;
		for (OrgStaffs item : orgStaffs) {
			if (matchStaffIds.contains(item.getStaffId())) {
				orgStaffNew.add(item);
			}
		}
		staffAllIds.clear();
		//获得过滤之后服务人员Id集合
		for (OrgStaffs item : orgStaffNew) {
				staffAllIds.add(item.getStaffId());
			}	
		// 获得符合位置的服务人员Id集合
		UserTrailVo userTrailVo =getMatchStaffIds(addrId,staffAllIds);
	/*	List<UserTrailReal> userTrailReals = userTrailVo.getUserTrailReals();
		List<OrgStaffs> orgStaffsList = new ArrayList<OrgStaffs>();
		for (UserTrailReal userTrailReal : userTrailReals) {
			OrgStaffs orgStaff = orgStaffsMapper.selectByPrimaryKey(userTrailReal.getUserId());
			orgStaffsList.add(orgStaff);
		}*/
//		orgStaffNew = staffsNear(orgStaffNew,userTrailVo.getUserTrailReals());
		orgStaffsNewVos =competeNewVo(userTrailVo);
		return orgStaffsNewVos;
	}
	/**
	 * OrgStaff 转换成  OrgStaffsNewVo
	 */
	private List<OrgStaffsNewVo> changeToNewVo(List<OrgStaffs> orgStaffs){
		List<OrgStaffsNewVo> orgStaffsNewVos = new ArrayList<OrgStaffsNewVo>();
		for (Iterator iterator = orgStaffs.iterator(); iterator.hasNext();) {
			OrgStaffs orgStaff = (OrgStaffs) iterator.next();
			OrgStaffsNewVo orgStaffsNewVo = new OrgStaffsNewVo();
			BeanUtilsExp.copyPropertiesIgnoreNull(orgStaff, orgStaffsNewVo);
			orgStaffsNewVos.add(orgStaffsNewVo);
		}
		return orgStaffsNewVos;
	}
	
	
	/**
	 * 
	 * @param userTrailVo
	 * @return
	 */
	private List<OrgStaffsNewVo> competeNewVo(UserTrailVo userTrailVo){
		List<OrgStaffsNewVo> orgStaffsNewVos = new ArrayList<OrgStaffsNewVo>();
		List<UserTrailReal> userTrailReals = userTrailVo.getUserTrailReals();
		List<BaiduPoiVo> baiduPoiVos = userTrailVo.getBaiduPoiVos();
		if(null !=userTrailReals && userTrailReals.size()>0){
			for (int i = 0; i < userTrailReals.size(); i++) {
				UserTrailReal userTrailReal = userTrailReals.get(i);
				OrgStaffsNewVo vo = new OrgStaffsNewVo();
				Long staffId = userTrailReals.get(i).getUserId();
				BaiduPoiVo baiduPoiVo = baiduPoiVos.get(i);
				OrgStaffs orgStaffs = orgStaffsMapper.selectByPrimaryKey(staffId);
				BeanUtilsExp.copyPropertiesIgnoreNull(orgStaffs,vo);
				vo.setLocName(userTrailReal.getPoiName());//位置描述
				vo.setDistanceText(baiduPoiVo.getDistanceText());//线路距离文本描述
				vo.setDurationText(baiduPoiVo.getDurationText());//距离时间描述
				vo.setDistanceValue(baiduPoiVo.getDistanceValue());//服务人员距离用户距离
				orgStaffsNewVos.add(vo);
			}
		}
		return orgStaffsNewVos;
	}
	
	
	
	/**
	 * 助理 和 配送 派工逻辑:
	 * @param startTime  服务开始时间 		
	 * @param endTime    服务结束时间
	 * @param addrId     用户地址Id
	 * @param orderId    下单订单Id
	 * 	目的：筛选出符合条件的服务人员列表
	 *  实现步骤如下：
	 *  1.找出所有的钟点工.操作表就是 org_staffs where staff_type = 0 得到集合A
	 *  2.根据集合A找出所有开工状态的钟点工，操作表是 org_staff_online，得到集合B
	 *  3.把集合B中，在U-S 时段已经有派工的排除掉，操作表为 order_dispatch, 得到集合C
	 *  4.根据集合C排除掉在黑名单表里面的数据，操作表为 org_staff_black 得到集合D
	 * 
	 */
	@Override
	public List<OrgStaffsNewVo> getNewBestStaffForAm(Long startTime, Long endTime,String poiLongitude,String poiLatitude, Long orderId) {
		List<OrgStaffs> orgStaffNew = new ArrayList<OrgStaffs>();
		List<Long> staffAllIds = new ArrayList<Long>();
		List<OrgStaffsNewVo> orgStaffsNewVos = new ArrayList<OrgStaffsNewVo>();

	/*	// 1.查找所有可用服务人员
		List<OrgStaffs> orgStaffs = orgStaffsMapper.selectStaffByStaffType();// 1=助理
	
		//1.1 通过身份验证和助理考试的验证
		if (BeanUtilsExp.isNullOrEmpty(orgStaffs)) return orgStaffsNewVos;
		List<OrgStaffAuth> orgStaffAuths =orgStaffAuthMapper.selectAmStaffByServiceTypeIdAndAuthStatus();
		orgStaffs = staffsIsAuth(orgStaffs,orgStaffAuths);
		*/
		List<OrgStaffs> orgStaffs = orgStaffsMapper.selectAmAuthStaff();// 1=助理
		
		// 2.查找处于开工状态的服务人员Id
		if (BeanUtilsExp.isNullOrEmpty(orgStaffs)) return orgStaffsNewVos;
		List<OrgStaffOnline> orgStaffOnlines = orgStaffOnlineMapper.selectByIsWork((short) 1);// 1=开工状态
		orgStaffs = staffsIsWork(orgStaffs, orgStaffOnlines);

		// 3.排除处于派工状态的人员
		/*if (BeanUtilsExp.isNullOrEmpty(orgStaffs))return orgStaffsNewVos;
		List<OrderDispatchs> orderDispatchs = orderDispatchsMapper.selectByStartTimeAndEndTime(startTime, endTime);
		orgStaffs = staffsNoDispatch(orgStaffs, orderDispatchs);*/

		// 4.排除处于黑名单的人员
		if (BeanUtilsExp.isNullOrEmpty(orgStaffs))return orgStaffsNewVos;
		List<OrgStaffBlack> orgStaffBlacks = orgStaffBlackMapper.selectAll();
		orgStaffs = staffsNoBlack(orgStaffs, orgStaffBlacks);

		if (BeanUtilsExp.isNullOrEmpty(orgStaffs))return orgStaffsNewVos;
		//获得过滤之后服务人员Id集合
		for (OrgStaffs item : orgStaffs) {
				staffAllIds.add(item.getStaffId());
			}	
		// 获得符合位置的服务人员Id集合
		UserTrailVo userTrailVo =getMatchStaffIds(poiLongitude,poiLatitude,staffAllIds);
//		orgStaffs = staffsNear(orgStaffs,userTrailVo.getUserTrailReals());
		
		orgStaffsNewVos =competeNewVo(userTrailVo);
		
		return orgStaffsNewVos;
	}
	@Override
	public List<OrgStaffsNewVo> getNewBestStaffForDel(Long startTime, Long endTime,Long userAddrId, Long orderId) {
		List<OrgStaffs> orgStaffNew = new ArrayList<OrgStaffs>();
		List<Long> staffAllIds = new ArrayList<Long>();
		List<OrgStaffsNewVo> orgStaffsNewVos = new ArrayList<OrgStaffsNewVo>();
		
		// 1.查找所有可用服务人员
		List<OrgStaffs> orgStaffs = orgStaffsMapper.selectStaffByStaffType();// 0=服务人员
		
		// 2.查找处于开工状态的服务人员Id
		if (BeanUtilsExp.isNullOrEmpty(orgStaffs)) return orgStaffsNewVos;
		List<OrgStaffOnline> orgStaffOnlines = orgStaffOnlineMapper.selectByIsWork((short) 1);// 1=开工状态
		orgStaffs = staffsIsWork(orgStaffs, orgStaffOnlines);
		
		// 3.排除处于派工状态的人员
		/*if (BeanUtilsExp.isNullOrEmpty(orgStaffs))return orgStaffsNewVos;
		List<OrderDispatchs> orderDispatchs = orderDispatchsMapper.selectByStartTimeAndEndTime(startTime, endTime);
		orgStaffs = staffsNoDispatch(orgStaffs, orderDispatchs);*/
		
		// 4.排除处于黑名单的人员
		if (BeanUtilsExp.isNullOrEmpty(orgStaffs))return orgStaffsNewVos;
		List<OrgStaffBlack> orgStaffBlacks = orgStaffBlackMapper.selectAll();
		orgStaffs = staffsNoBlack(orgStaffs, orgStaffBlacks);
		
		if (BeanUtilsExp.isNullOrEmpty(orgStaffs))return orgStaffsNewVos;
		//获得过滤之后服务人员Id集合
		for (OrgStaffs item : orgStaffs) {
			staffAllIds.add(item.getStaffId());
		}	
		// 获得符合位置的服务人员Id集合
		UserTrailVo userTrailVo =getMatchStaffIds(userAddrId,staffAllIds);
//		orgStaffs = staffsNear(orgStaffs,userTrailVo.getUserTrailReals());
		
		orgStaffsNewVos =competeNewVo(userTrailVo);
		
		return orgStaffsNewVos;
	}
	
	
	
	/**
	 * 根据用户的服务地址经纬度找出最符合的服务人员列表，距离不超过10公里和60分钟
	 * @param addrId
	 * @param staffIds
	 * @return
	 */
	public UserTrailVo getMatchStaffIds(Long addrId,List<Long> staffIds) {
		List<UserTrailReal> userTrailRealList = new ArrayList<UserTrailReal>();
		UserTrailVo userTrailVo = new UserTrailVo();

		UserAddrs userAddrs = userAddrsMapper.selectByPrimaryKey(addrId);
		if (BeanUtilsExp.isNullOrEmpty(userAddrs)) return userTrailVo;
		//1.服务地点：根据经纬度确定用户服务地点
		String fromLat = userAddrs.getLatitude();
		String fromLng = userAddrs.getLongitude();
		
		//2.服务人员最新地址集合:可以分页取。每次5个
		List<UserTrailReal> userTrailReals = new ArrayList<UserTrailReal>();
		for (Long staffId : staffIds) {												//服务人员  type=1;用户 type =0
			UserTrailReal	userTrailReal = userTrailRealMapper.selectByUserIdAndType(staffId, (short)1);     
			if(!BeanUtilsExp.isNullOrEmpty(userTrailReal)){
				userTrailReals.add(userTrailReal);
			}
		}
		if (userTrailReals.isEmpty()) return userTrailVo;
		List<BaiduPoiVo> staffAddrList = new ArrayList<BaiduPoiVo>();
		//3.组装服务人员百度地图位置Vo
		for (UserTrailReal userTrailReal : userTrailReals) {
			BaiduPoiVo baiduPoiVo = new BaiduPoiVo();
			baiduPoiVo.setLat(userTrailReal.getLat());
			baiduPoiVo.setLng(userTrailReal.getLng());
//			baiduPoiVo.setName(org.getOrgName());
			staffAddrList.add(baiduPoiVo);
		}
		/**
		 * 得到符合条件的服务人员UserTralReal集合（目前暂时取时间最短。未考虑时间一样的情况）      
		 * TODO,百度只支持 最多 5个 目标地点的距离检索
		 */
		UserTrailReal item = null;
		try {
			List<BaiduPoiVo> destList = BaiduMapUtil.getMapRouteMatrix(fromLat, fromLng, staffAddrList);
			List<BaiduPoiVo> baiduPoiVos = new ArrayList<BaiduPoiVo>();
			
			baiduPoiVos = BaiduMapUtil.getMinDest(destList, 10000, 3600);
			
			BaiduPoiVo baiduPoiVo = null;
			if (!baiduPoiVos.isEmpty()) {
				baiduPoiVo = baiduPoiVos.get(0);
			}
			
			for (int i =0; i < userTrailReals.size(); i++) {
				item = userTrailReals.get(i);
				 //4.如果 某个 服务人员 的经纬度 ，和 util中计算出的 经纬度 一致。。则为 符合条件的服务人员
				if (baiduPoiVo.getLat().equals(item.getLat()) &&
					baiduPoiVo.getLng().equals(item.getLng())) {
					userTrailRealList.add(item);
				}
			}
			
			//对于距离符合要求的服务人员，重新获得具体位置
			List<BaiduPoiVo> staffAddrList1 = new ArrayList<BaiduPoiVo>();
			for (UserTrailReal userTrailReal : userTrailRealList) {
				BaiduPoiVo baiduPoiVo1 = new BaiduPoiVo();
				baiduPoiVo1.setLat(userTrailReal.getLat());
				baiduPoiVo1.setLng(userTrailReal.getLng());
//				baiduPoiVo1.setName(org.getOrgName());
				staffAddrList1.add(baiduPoiVo1);
			}
			List<BaiduPoiVo> destList1 = MapPoiUtil.getMapRouteMatrix(fromLat, fromLng, staffAddrList1);
			
			userTrailVo.setBaiduPoiVos(destList1);
			userTrailVo.setUserTrailReals(userTrailRealList);
			
			
		} catch (Exception e) {
			e.printStackTrace();
			return userTrailVo;
		} 
		return userTrailVo;
	}
	public UserTrailVo getMatchStaffIds(String poiLongitude,String poiLatitude,List<Long> staffIds) {
		List<UserTrailReal> userTrailRealList = new ArrayList<UserTrailReal>();
		UserTrailVo userTrailVo = new UserTrailVo();
		
		
		//1.服务地点：根据经纬度确定用户服务地点
		String fromLat = poiLatitude;
		String fromLng = poiLongitude;
		
		//2.服务人员最新地址集合:可以分页取。每次5个
		List<UserTrailReal> userTrailReals = new ArrayList<UserTrailReal>();
		for (Long staffId : staffIds) {												//服务人员  type=1;用户 type =0
			UserTrailReal	userTrailReal = userTrailRealMapper.selectByUserIdAndType(staffId, (short)1);     
			if(!BeanUtilsExp.isNullOrEmpty(userTrailReal)){
				userTrailReals.add(userTrailReal);
			}
		}
		if (userTrailReals.isEmpty()) return userTrailVo;
		List<BaiduPoiVo> staffAddrList = new ArrayList<BaiduPoiVo>();
		//3.组装服务人员百度地图位置Vo
		for (UserTrailReal userTrailReal : userTrailReals) {
			BaiduPoiVo baiduPoiVo = new BaiduPoiVo();
			baiduPoiVo.setLat(userTrailReal.getLat());
			baiduPoiVo.setLng(userTrailReal.getLng());
//			baiduPoiVo.setName(org.getOrgName());
			staffAddrList.add(baiduPoiVo);
		}
		/**
		 * 得到符合条件的服务人员UserTralReal集合（目前暂时取时间最短。未考虑时间一样的情况）      
		 * TODO,百度只支持 最多 5个 目标地点的距离检索
		 */
		UserTrailReal item = null;
		try {
			List<BaiduPoiVo> destList = MapPoiUtil.getMapRouteMatrix(fromLat, fromLng, staffAddrList);
			List<BaiduPoiVo> baiduPoiVos = new ArrayList<BaiduPoiVo>();
			
			baiduPoiVos = MapPoiUtil.getMinDest(destList);
			
			BaiduPoiVo baiduPoiVo = null;
			if (!baiduPoiVos.isEmpty()) {
				baiduPoiVo = baiduPoiVos.get(0);
			}

			
			for (int i =0; i < userTrailReals.size(); i++) {
				item = userTrailReals.get(i);
				//4.如果 某个 服务人员 的经纬度 ，和 util中计算出的 经纬度 一致。。则为 符合条件的服务人员
				if (baiduPoiVo.getLat().equals(item.getLat()) &&
					baiduPoiVo.getLng().equals(item.getLng())) {
					userTrailRealList.add(item);
				}
			}
			
			//对于距离符合要求的服务人员，重新获得具体位置
			List<BaiduPoiVo> staffAddrList1 = new ArrayList<BaiduPoiVo>();
			for (UserTrailReal userTrailReal : userTrailRealList) {
				BaiduPoiVo baiduPoiVo1 = new BaiduPoiVo();
				baiduPoiVo1.setLat(userTrailReal.getLat());
				baiduPoiVo1.setLng(userTrailReal.getLng());
//				baiduPoiVo1.setName(org.getOrgName());
				staffAddrList1.add(baiduPoiVo1);
			}
			List<BaiduPoiVo> destList1 = MapPoiUtil.getMapRouteMatrix(fromLat, fromLng, staffAddrList1);
			
			userTrailVo.setBaiduPoiVos(destList1);
			userTrailVo.setUserTrailReals(userTrailRealList);
			
			
		} catch (Exception e) {
			e.printStackTrace();
			return userTrailVo;
		} 
		return userTrailVo;
	}
	/**
	 * 钟点工+助理是否通过验证(身份验证+考试验证)
	 * @param orgStaffs
	 * @param orgStaffOnlines
	 * @return
	 */
	public List<OrgStaffs> staffsIsAuth(List<OrgStaffs> orgStaffs, List<OrgStaffAuth> orgStaffAuths) {
		List<OrgStaffs> orgStaffNew = new ArrayList<>();
		// 如果开工状态为空，直接返回集合
		if (orgStaffAuths != null && orgStaffAuths.size() > 0) {
			for (Iterator iterator = orgStaffs.iterator(); iterator.hasNext();) {
				OrgStaffs orgStaff = (OrgStaffs) iterator.next();
				Long staffId = orgStaff.getStaffId();
				for (Iterator iterator2 = orgStaffAuths.iterator(); iterator2.hasNext();) {
					OrgStaffAuth orgStaffAuth = (OrgStaffAuth) iterator2.next();
					Long staffTempId = orgStaffAuth.getStaffId();
					if (staffId == staffTempId) {
						orgStaffNew.add(orgStaff);
					}
				}
			}
		}
		return orgStaffNew;
	}
	
	/**
	 * 获得处于开工状态的员工
	 * 
	 * @param orgStaffs
	 * @param orgStaffOnlines
	 * @return
	 */
	public List<OrgStaffs> staffsIsWork(List<OrgStaffs> orgStaffs, List<OrgStaffOnline> orgStaffOnlines) {
		List<OrgStaffs> orgStaffNew = new ArrayList<>();
		// 如果开工状态为空，直接返回集合
		if (orgStaffOnlines != null && orgStaffOnlines.size() > 0) {
			for (Iterator iterator = orgStaffs.iterator(); iterator.hasNext();) {
				OrgStaffs orgStaff = (OrgStaffs) iterator.next();
				Long staffId = orgStaff.getStaffId();
				for (Iterator iterator2 = orgStaffOnlines.iterator(); iterator2.hasNext();) {
					OrgStaffOnline orgStaffOnline = (OrgStaffOnline) iterator2.next();
					Long staffTempId = orgStaffOnline.getStaffId();
					if (staffId == staffTempId) {
						orgStaffNew.add(orgStaff);
					}
				}
			}
		}
		return orgStaffNew;
	}

	/**
	 * 获得没有派工的员工
	 * 
	 * @param orgStaffs
	 * @param orderDispatchs
	 * @return
	 */
	public List<OrgStaffs> staffsNoDispatch(List<OrgStaffs> orgStaffs, List<OrderDispatchs> orderDispatchs) {
		List<OrgStaffs> orgStaffNew = new ArrayList<>();
		for (Iterator iterator = orgStaffs.iterator(); iterator.hasNext();) {
			OrgStaffs orgStaffs2 = (OrgStaffs) iterator.next();
			orgStaffNew.add(orgStaffs2);
		}
		// 如果派工单为空不进行排除，否则进行筛查
		if (orderDispatchs != null && orderDispatchs.size() > 0) {
			for (Iterator iterator = orgStaffs.iterator(); iterator.hasNext();) {
				OrgStaffs orgStaff = (OrgStaffs) iterator.next();
				Long staffId = orgStaff.getStaffId();
				for (Iterator iterator2 = orderDispatchs.iterator(); iterator2.hasNext();) {
					OrderDispatchs orderDispatch = (OrderDispatchs) iterator2.next();
					Long staffTempId = orderDispatch.getStaffId();
					if (staffId == staffTempId) {
						orgStaffNew.remove(orgStaff);
					}
				}
			}
			return orgStaffNew;
		}else {
			return orgStaffs;
		}
	}
	/**
	 * 获得不处于黑名单的员工
	 * 
	 * @param orgStaffs
	 * @param orderDispatchs
	 * @return
	 */
	public List<OrgStaffs> staffsNoBlack(List<OrgStaffs> orgStaffs, List<OrgStaffBlack> orgStaffBlacks) {
		List<OrgStaffs> orgStaffNew = new ArrayList<>();
		for (Iterator iterator = orgStaffs.iterator(); iterator.hasNext();) {
			OrgStaffs orgStaffs2 = (OrgStaffs) iterator.next();
			orgStaffNew.add(orgStaffs2);
		}
		// 如果黑名单为空不进行排除，否则进行筛查
		if (orgStaffBlacks != null && orgStaffBlacks.size() > 0) {
			for (Iterator iterator = orgStaffs.iterator(); iterator.hasNext();) {
				OrgStaffs orgStaff = (OrgStaffs) iterator.next();
				Long staffId = orgStaff.getStaffId();
				for (Iterator iterator2 = orgStaffBlacks.iterator(); iterator2.hasNext();) {
					OrgStaffBlack OrgStaffBlack = (OrgStaffBlack) iterator2.next();
					Long staffTempId = OrgStaffBlack.getStaffId();
					if (staffId == staffTempId) {
						orgStaffNew.remove(orgStaff);
					}
				}
			}
			return orgStaffNew;
		}else {
			return orgStaffs;
		}
	}
	/**
	 * 
	 * @param orgStaffs
	 * @param userTrailReals
	 * @return
	 */
	public List<OrgStaffs> staffsNear(List<OrgStaffs> orgStaffs, List<UserTrailReal> userTrailReals) {
		// 如果黑名单为空不进行排除，否则进行筛查
		List<OrgStaffs> orgStaffNew = new ArrayList<>();
		// 如果开工状态为空，直接返回集合
		if (userTrailReals != null && userTrailReals.size() > 0) {
			for (Iterator iterator = orgStaffs.iterator(); iterator.hasNext();) {
				OrgStaffs orgStaff = (OrgStaffs) iterator.next();
				Long staffId = orgStaff.getStaffId();
				for (Iterator iterator2 = userTrailReals.iterator(); iterator2.hasNext();) {
					UserTrailReal userTrailReal = (UserTrailReal) iterator2.next();
					Long staffTempId = userTrailReal.getUserId();
					if (staffId == staffTempId) {
						orgStaffNew.add(orgStaff);
					}
				}
			}
			return orgStaffNew;
		}else {
			return orgStaffs;
		}
	}
	/**
	 * 随机分配服务人员 and 插入日志
	 */
	@Override
	public void disRadomStaff(List<OrgStaffs> orgStaffs,Orders order,Users user) {
		//随机取一个阿姨.
		Random r = new Random();
		int index = r.nextInt(orgStaffs.size());
		OrgStaffs orgStaff = orgStaffs.get(index);
		
		OrderDispatchs orderDispatchs = orderDispatchService.initOrderDisp(); //派工状态默认有效  1
		
		orderDispatchs.setUserId(user.getId());
		orderDispatchs.setOrgId(order.getOrgId());
		orderDispatchs.setMobile(user.getMobile());
		orderDispatchs.setOrderId(order.getId());
		orderDispatchs.setOrderNo(order.getOrderNo());
		
		
		// 服务开始时间， serviceDate（服务时间）的前一小时， 当前秒值 -3600s
		orderDispatchs.setServiceDatePre(order.getServiceDate() - 3600);  
		orderDispatchs.setServiceDate(order.getServiceDate());
		orderDispatchs.setServiceHours(order.getServiceHour());
		
		//工作人员相关
		orderDispatchs.setStaffId(orgStaff.getStaffId());

		orderDispatchs.setStaffName(orgStaff.getName());
		orderDispatchs.setMobile(orgStaff.getMobile());
//		orderDispatchs.setAmId(orgStaff.getAmId());
				
		orderDispatchService.insert(orderDispatchs);
		
		
		//2.插入订单日志表  order_log
		OrderLog orderLog = orderLogService.initOrderLog(order);
		orderLogService.insert(orderLog);
		
		String beginTimeStr = TimeStampUtil.timeStampToDateStr(order.getServiceDate() * 1000, "MM月-dd日HH:mm");
		String endTimeStr = TimeStampUtil.timeStampToDateStr( (order.getServiceDate() + order.getServiceHour() * 3600) * 1000, "HH:mm");
		String timeStr = beginTimeStr + "-" + endTimeStr;
		
	}

	@Override
	public void disStaff(OrgStaffs orgStaff, Orders order, Users user) {
		
		OrderDispatchs orderDispatchs = orderDispatchService.initOrderDisp(); //派工状态默认有效  1
		
		orderDispatchs.setUserId(user.getId());
		orderDispatchs.setOrgId(order.getOrgId());
		orderDispatchs.setMobile(user.getMobile());
		orderDispatchs.setOrderId(order.getId());
		orderDispatchs.setOrderNo(order.getOrderNo());
		
		
		// 服务开始时间， serviceDate（服务时间）的前一小时， 当前秒值 -3600s
		orderDispatchs.setServiceDatePre(order.getServiceDate() - 3600);  
		orderDispatchs.setServiceDate(order.getServiceDate());
		orderDispatchs.setServiceHours(order.getServiceHour());
		
		//工作人员相关
		orderDispatchs.setStaffId(orgStaff.getStaffId());

		orderDispatchs.setStaffName(orgStaff.getName());
		orderDispatchs.setStaffMobile(orgStaff.getMobile());
//		orderDispatchs.setAmId(orgStaff.getAmId());
				
		orderDispatchService.insert(orderDispatchs);
		
		
		//2.插入订单日志表  order_log
		OrderLog orderLog = orderLogService.initOrderLog(order);
		orderLogService.insert(orderLog);
		
		String beginTimeStr = TimeStampUtil.timeStampToDateStr(order.getServiceDate() * 1000, "MM月-dd日HH:mm");
		String endTimeStr = TimeStampUtil.timeStampToDateStr( (order.getServiceDate() + order.getServiceHour() * 3600) * 1000, "HH:mm");
		String timeStr = beginTimeStr + "-" + endTimeStr;
		
		if(orderDispatchs.getDispatchStatus()==1){
			pushToStaff(orgStaff.getStaffId(), "true","dispatch", order.getId(), OneCareUtil.getJhjOrderTypeName(order.getOrderType()), order.getServiceContent());
		}
		
	}
	@Override
	public void disStaff(OrgStaffs orgStaff, Orders order, Users user,String poiLongitude,
		String poiLatitude,String pickAddrName,String pickAddr,String userAddrDistance) {
		
		OrderDispatchs orderDispatchs = orderDispatchService.initOrderDisp(); //派工状态默认有效  1
		
		orderDispatchs.setUserId(user.getId());
		orderDispatchs.setOrgId(order.getOrgId());
		orderDispatchs.setMobile(user.getMobile());
		orderDispatchs.setOrderId(order.getId());
		orderDispatchs.setOrderNo(order.getOrderNo());
		
		
		// 服务开始时间， serviceDate（服务时间）的前一小时， 当前秒值 -3600s
		orderDispatchs.setServiceDatePre(order.getServiceDate() - 3600);  
		orderDispatchs.setServiceDate(order.getServiceDate());
		orderDispatchs.setServiceHours(order.getServiceHour());
		
		//工作人员相关
		orderDispatchs.setStaffId(orgStaff.getStaffId());
		orderDispatchs.setStaffName(orgStaff.getName());
		orderDispatchs.setStaffMobile(orgStaff.getMobile());
//		orderDispatchs.setAmId(orgStaff.getAmId());
		
		//助理订单派工保存服务地址(经纬度+地址名称+门牌号)
		orderDispatchs.setPickAddrName(pickAddrName);
		orderDispatchs.setPickAddr(pickAddr);
		orderDispatchs.setPickAddrLng(poiLongitude);
		orderDispatchs.setPickAddrLat(poiLatitude);
		
		orderDispatchs.setUserAddrDistance(0);
		if (!StringUtil.isEmpty(userAddrDistance)) {
			userAddrDistance = userAddrDistance.replaceAll("米", "");
			if (RegexUtil.isInteger(userAddrDistance)) {
				orderDispatchs.setUserAddrDistance(new Integer(userAddrDistance));
			}
		}
		
		
		orderDispatchService.insert(orderDispatchs);
		
		//更新订单表中的助理Id
		order.setAmId(orgStaff.getStaffId());
		ordersService.updateByPrimaryKeySelective(order);
		
		
		//2.插入订单日志表  order_log
		OrderLog orderLog = orderLogService.initOrderLog(order);
		orderLogService.insert(orderLog);
		
		String beginTimeStr = TimeStampUtil.timeStampToDateStr(order.getServiceDate() * 1000, "MM月-dd日HH:mm");
		String endTimeStr = TimeStampUtil.timeStampToDateStr( (order.getServiceDate() + order.getServiceHour() * 3600) * 1000, "HH:mm");
		String timeStr = beginTimeStr + "-" + endTimeStr;
		
		if(orderDispatchs.getDispatchStatus()==1){
			pushToStaff(orgStaff.getStaffId(), "true","dispatch", order.getId(), OneCareUtil.getJhjOrderTypeName(order.getOrderType()), Constants.ALERT_STAFF_MSG);
		}
		
	}
	
	/**
	 * 派工成功，为员工发送推送消息
	 */
	@Override
	public void pushToStaff(Long staffId,String isShow,String action,
		Long orderId,String remindTitle,String remindContent){
		
		//1.下单成功，为员工推送消息
		UserPushBind userPushBind = userPushBindMapper.selectByUserId(staffId);//
		if(userPushBind!=null){
			String clientId = userPushBind.getClientId();
			PushUtil.getUserStatus(clientId);
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("cid", clientId);
			/**
			 * 订单派工透传消息格式：
			 * is_show:是否在通知栏展示 true/false
			 * action:diaptch(点击打开派工信息)，msg(点击打开app)
			 * order_id:订单Id
			 * remind_title:消息栏标题
			 * remind_content:消息展示内容
			 */
			HashMap<String, String> tranParams = new HashMap<String, String>();
			tranParams.put("is_show", isShow);	//true=展现,false=不显示	
			tranParams.put("action", action);//=dispatch进入派工详情，=msg打开app		
		    tranParams.put("order_id", orderId+"");
		    tranParams.put("remind_title", remindTitle);
		    tranParams.put("remind_content",remindContent);
		    
		    
		    Orders order = ordersService.selectbyOrderId(orderId);
		    OrderPrices orderPrice = orderPricesService.selectByOrderId(orderId);
		    OrderDispatchs orderDispatchs = orderDispatchsService.selectByOrderNo(order.getOrderNo());
		    //服务地址：
		    String serviceAddr = "";
		    if (order.getAddrId() > 0L) {
				UserAddrs userAddr = userAddrsService.selectByPrimaryKey(order.getAddrId());
				serviceAddr = userAddr.getName() + userAddr.getAddr();
			} 
		    
		    if (order.getOrderType().equals((short)2)) {
		    	serviceAddr = orderDispatchs.getPickAddrName() + orderDispatchs.getPickAddr();
			}
		    
		    tranParams.put("service_addr", serviceAddr);
		
		    //服务时间
		    Long serviceDate = order.getServiceDate();
		    
		    if (order.getOrderType().equals((short)2)) {
		    	serviceDate = order.getAddTime();
		    }
		    
		    String serviceTime = TimeStampUtil.timeStampToDateStr(serviceDate * 1000, "MM-dd HH:mm");
		    
		    tranParams.put("service_time", serviceTime);
		    
		    //服务时长 
		    String serviceHour = order.getServiceHour().toString() + "小时";
		    tranParams.put("service_hour", serviceHour);
		    
		    //服务项目
		    String serviceContent = "";
		    if (order.getServiceType() > 0L) {
		    	PartnerServiceType serviceType = partnerServiceTypeService.selectByPrimaryKey(order.getServiceType());
		    	serviceContent = serviceType.getName();
			}
		    tranParams.put("service_content", serviceContent);
		    
		    //服务金额
		    String orderMoney = MathBigDecimalUtil.round2(orderPrice.getOrderMoney());
		    tranParams.put("order_money", orderMoney);
		    
		    //订单类型
		    tranParams.put("order_type", order.getOrderType().toString());
		    
			String jsonParams = GsonUtil.GsonString(tranParams);
			params.put("transmissionContent", jsonParams);
			try {
				PushUtil.AndroidPushToSingle(params);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		//2.0为员工发送短信
//		//2.1)派工成功,为服务人员发送短信
//		OrgStaffs orgStaffs = orgStaffsMapper.selectByPrimaryKey(staffId);
//		Orders orders = ordersService.selectByPrimaryKey(orderId);
//		String beginTimeStr = TimeStampUtil.timeStampToDateStr(orders.getServiceDate() * 1000, "MM月-dd日HH:mm");
//		String endTimeStr = TimeStampUtil.timeStampToDateStr( (orders.getServiceDate() + orders.getServiceHour() * 3600) * 1000, "HH:mm");
//		String timeStr = beginTimeStr + "-" + endTimeStr;
//		String[] contentForUser = new String[] { timeStr };
//		SmsUtil.SendSms(orgStaffs.getMobile(),  "64746", contentForUser);
		
	}
	
	
}
