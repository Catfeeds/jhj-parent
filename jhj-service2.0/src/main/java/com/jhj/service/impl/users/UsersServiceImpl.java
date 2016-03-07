package com.jhj.service.impl.users;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jhj.po.dao.bs.GiftCouponsMapper;
import com.jhj.po.dao.dict.DictCardTypeMapper;
import com.jhj.po.dao.user.UserAddrsMapper;
import com.jhj.po.dao.user.UsersMapper;
import com.jhj.po.model.bs.DictCoupons;
import com.jhj.po.model.bs.GiftCoupons;
import com.jhj.po.model.bs.Tags;
import com.jhj.po.model.dict.DictCardType;
import com.jhj.po.model.order.Orders;
import com.jhj.po.model.tags.UserRefTags;
import com.jhj.po.model.user.UserAddrs;
import com.jhj.po.model.user.UserCoupons;
import com.jhj.po.model.user.Users;
import com.jhj.service.bs.DictCouponsService;
import com.jhj.service.bs.TagsService;
import com.jhj.service.order.OrderRatesService;
import com.jhj.service.order.OrdersService;
import com.jhj.service.tags.UserRefTagsService;
import com.jhj.service.users.UserAddrsService;
import com.jhj.service.users.UserCouponsService;
import com.jhj.service.users.UsersService;
import com.jhj.vo.UserSearchVo;
import com.jhj.vo.user.UserAppVo;
import com.jhj.vo.user.UserEditViewVo;
import com.meijia.utils.BeanUtilsExp;
import com.meijia.utils.MathBigDeciamlUtil;
import com.meijia.utils.TimeStampUtil;

@Service
public class UsersServiceImpl implements UsersService {

	@Autowired
	private UsersMapper usersMapper;

	@Autowired
	private UserAddrsService userAddrService;

	@Autowired
	private UserAddrsMapper userAddrsMapper;

	@Autowired
	private UserCouponsService userCouponsService;

	@Autowired
	private OrdersService ordersService;

	@Autowired
	private TagsService tagsService;

	@Autowired
	private UserRefTagsService userRefTagsService;

	@Autowired
	private OrderRatesService orderRatesService;
	
	@Autowired
	private DictCardTypeMapper dictCardTypeMapper;
	
	@Autowired
	private GiftCouponsMapper giftCouponMapper;
	
	@Autowired
	private DictCouponsService dictCouponService;
	
	
	@Override
	public int deleteByPrimaryKey(Long id) {
		return usersMapper.deleteByPrimaryKey(id);
	}

	@Override
	public int insert(Users record) {
		return usersMapper.insert(record);
	}

	@Override
	public int insertSelective(Users record) {
		return usersMapper.insertSelective(record);
	}

	@Override
	public int updateByPrimaryKeySelective(Users record) {
		return usersMapper.updateByPrimaryKeySelective(record);
	}

	@Override
	public int updateByPrimaryKey(Users record) {
		return usersMapper.updateByPrimaryKey(record);
	}

	@Override
	public PageInfo searchVoListPage(UserSearchVo searchVo, int pageNo,
			int pageSize) {

		PageHelper.startPage(pageNo, pageSize);
		List<Users> list = usersMapper.selectByListPages(searchVo);
		PageInfo result = new PageInfo(list);
		return result;
	}

	@Override
	public Users selectByUsersId(Long id) {
		return usersMapper.selectByPrimaryKey(id);
	}

	@Override
	public Users getUserById(Long id) {
		return usersMapper.selectByPrimaryKey(id);
	}

	@Override
	public List<Users> selectByUserIds(List<Long> ids) {
		return usersMapper.selectByUserIds(ids);
	}

	@Override
	public Users getUserByMobile(String mobile) {
		return usersMapper.selectByMobile(mobile);
	}

	@Override
	public Users genUser(String mobile, Short addFrom) {
		Users u = this.getUserByMobile(mobile);
		if (u == null) {
			// 验证手机号是否已经注册，如果未注册，则自动注册用户，
			u = this.initUsers(mobile, addFrom);
			String provinceName = "";
			
			/*
			 *  2015-11-5 16:48:30 决定将这处 求 号码归属地的代码去掉。。老报错.没什么意义
			 */
//			try {
//				provinceName = MobileUtil.calcMobileCity(mobile);
//			} catch (MalformedURLException e) {
//				e.printStackTrace();
//			}

			u.setProvinceName(provinceName);
			
			usersMapper.insertSelective(u);
			/* 
			  * 2015年10月27日18:27:17
			  * 	
			  *	    新用户注册，赠送注册优惠券礼包
			  *    1. 只有第一次登陆送，
			  *    2. 送的用完了，不能再送了
			  */
			 
			
			 //此处可以设置一个 固定时间的时间戳，作为活动  的 截止日期
			 
			 /*
			  * gifts 表中 id 为 5的 礼包 表示  注册大礼包
			  * 
			  */
			
			  UserCoupons userCoupons = new UserCoupons();
			 
			  //id 为 5的是 注册大礼包
			  List<GiftCoupons> giftCouponList = giftCouponMapper.selectByGiftId(5L);
			  
			  for (GiftCoupons giftCoupons : giftCouponList) {
				  
				  Long couponId = giftCoupons.getCouponId();
				  DictCoupons dictCoupons = dictCouponService.selectByPrimaryKey(couponId);
				  
				  
				  //根据 优惠券数量，生成多条记录
				  Short num = giftCoupons.getNum();
				  for (int i =(short)0; i < num; i++) {
					
					  //coupon_id	
					  userCoupons.setCouponId(couponId);
					  //user_id
					  userCoupons.setUserId(u.getId()); 
					  
					  //gift_id
					  userCoupons.setGiftId(giftCoupons.getGiftId());
					  
					  Date fromDate = dictCoupons.getFromDate();
					  Date toDate = dictCoupons.getToDate();
					  BigDecimal couponValue = dictCoupons.getValue();
					  String serviceType = dictCoupons.getServiceType();
					  
					  //value
					  userCoupons.setValue(couponValue);
					  //service_Type
					  userCoupons.setServiceType(serviceType);
					  //from_date
					  userCoupons.setFromDate(fromDate);
					  //to_date
					  userCoupons.setToDate(toDate);
					  //is_used
					  userCoupons.setIsUsed((short)0);
					  //used_time
					  userCoupons.setUsedTime(0L);
					  //order_no
					  userCoupons.setOrderNo("");
					  //add_time
					  userCoupons.setAddTime(TimeStampUtil.getNowSecond());
					  
					  //  生成新记录  mybatis 主键自增 useGenerateKeys = "ture"
					  userCouponsService.insertSelective(userCoupons);
					  
				  }
				  
			  }
			
			// 先看看是否已经有赠送
			/*
			  UserCoupons userCoupon =
			  userCouponService.selectByMobileCardPwd(mobile,
			  Constants.NEW_USER_COUPON_CARD_PASSWORD);
			 * 
			 * if (userCoupon == null) { //新用户注册赠送相应的优惠劵. DictCoupons coupon =
			 * couponService
			 * .selectByCardPasswd(Constants.NEW_USER_COUPON_CARD_PASSWORD);
			 * 
			 * userCoupon = userCouponService.initUserCoupon();
			 * userCoupon.setUserId(u.getId());
			 * userCoupon.setMobile(u.getMobile());
			 * userCoupon.setCouponId(coupon.getId());
			 * userCoupon.setCardPasswd(coupon.getCardPasswd());
			 * userCoupon.setValue(coupon.getValue());
			 * userCoupon.setExpTime(coupon.getExpTime());
			 * 
			 * userCouponService.insert(userCoupon); }
			 */
		}
		return u;
	}

	@Override
	public Users initUsers(String mobile, Short addFrom) {
		Users u = new Users();
		u.setId(0L);
		u.setMobile(mobile);
		u.setProvinceName("");
		u.setThirdType(" ");
		u.setOpenid("");
		u.setName(" ");
		u.setSex(" ");
		u.setHeadImg(" ");
		u.setRestMoney(new BigDecimal(0));
		u.setUserType((short) 0);
		u.setAddFrom((short) 1);
		u.setScore(0);
		u.setRemarks(" ");
		u.setAddTime(TimeStampUtil.getNow() / 1000);
		u.setUpdateTime(TimeStampUtil.getNow() / 1000);
		return u;
	}

	@Override
	public List<HashMap> totalByUserIds(List<Long> id) {
		return usersMapper.totalByUserIds(id);
	}

	@Override
	public UserAppVo changeToUserAppVo(Long userId) {
		UserAppVo vo = new UserAppVo();

		Users user = selectByUsersId(userId);

		BeanUtilsExp.copyPropertiesIgnoreNull(user, vo);
		vo.setHasUserAddr(false);
		List<UserAddrs> userAddrs = userAddrService.selectByUserId(userId);
		if (!userAddrs.isEmpty())
			vo.setHasUserAddr(true);

		List<UserCoupons> userCoupons = userCouponsService
				.selectByUserId(userId);
		vo.setTotalCoupon(userCoupons.size());
		// 显示优惠个数
		vo.setTotalCouponSpan(userCoupons.size() + "张");
		// 显示余额
		vo.setRestMoneySpan(user.getRestMoney() + "元");
		// 显示助理服务时间
		BigDecimal restMoney = user.getRestMoney();
		BigDecimal unit = new BigDecimal(100);
		BigDecimal temp = MathBigDeciamlUtil.div(restMoney, unit, 2);
		if (temp.compareTo(unit) >= 100) {
			temp = new BigDecimal(100);
		}
		vo.setAmServiceTimeSpan(temp + "小时");
		return vo;
	}

	@Override
	public UserEditViewVo getUserDetail(String orderNo, Long userId) {

		UserEditViewVo userEditViewVo = initOHVO();

		Users users = usersMapper.selectByPrimaryKey(userId);

		BeanUtilsExp.copyPropertiesIgnoreNull(users, userEditViewVo);

		Orders orders = ordersService.selectByOrderNo(orderNo);

		/*
		 * if (users != null) { // 用户姓名 userEditViewVo.setName(users.getName());
		 * // 用户性别 userEditViewVo.setSex(users.getSex()); // 用户手机号
		 * userEditViewVo.setMobile(users.getMobile()); // 备注
		 * userEditViewVo.setRemarks(users.getRemarks()); }
		 */

		// userAddr 服务地址名称
		Long addrId = orders.getAddrId();

		UserAddrs userAddrs = userAddrsMapper.selectByPrimaryKey(addrId);

		if (userAddrs != null) {

			userEditViewVo.setAddrName(userAddrs.getName() + " "
					+ userAddrs.getAddr());
		}

		// 用户标签
		List<UserRefTags> userRefTagsList = userRefTagsService
				.selectListByUserId(userId);
		
		if (!userRefTagsList.isEmpty()) {
			
		
		//获得UserRefTags 里面的id的集合ids
		List<Long> tagIds = new ArrayList<Long>();
		for (UserRefTags item : userRefTagsList) {
			tagIds.add(item.getTagId());
		}
		
		List<Tags> tag = tagsService.selectByIds(tagIds);

		userEditViewVo.setList(tag);
		}
		return userEditViewVo;
	}

	
	private UserEditViewVo initOHVO() {

		UserEditViewVo userEditViewVo = new UserEditViewVo();

		/* BeanUtilsExp.copyPropertiesIgnoreNull(users, userEditViewVo); */
		userEditViewVo.setId(0L);
		userEditViewVo.setAddrName("");
		userEditViewVo.setMobile("");
		userEditViewVo.setProvinceName("");
		userEditViewVo.setThirdType(" ");
		userEditViewVo.setOpenid("");
		userEditViewVo.setName(" ");
		userEditViewVo.setSex(" ");
		userEditViewVo.setHeadImg(" ");
		userEditViewVo.setRestMoney(new BigDecimal(0));
		userEditViewVo.setUserType((short) 0);
		userEditViewVo.setAddFrom((short) 0);
		userEditViewVo.setScore(0);
		userEditViewVo.setRemarks(" ");
		userEditViewVo.setAddTime(TimeStampUtil.getNow() / 1000);
		userEditViewVo.setUpdateTime(TimeStampUtil.getNow() / 1000);

		return userEditViewVo;

	}

	@Override
	public UserEditViewVo getUserAddrDetail(List<UserAddrs> userAddrsList,
			Long userId) {
		UserEditViewVo userEditViewVo = initOHVO();

		Users users = usersMapper.selectByPrimaryKey(userId);

		BeanUtilsExp.copyPropertiesIgnoreNull(users, userEditViewVo);

		// userAddr 服务地址名称
		List<Long> userIds = new ArrayList<Long>();
		for (UserAddrs item : userAddrsList) {
			userIds.add(item.getId());
		}
		
		Collections.sort(userIds);
		
		UserAddrs userAddrs = userAddrService.selectByPrimaryKey(userIds.get(0));
		
		userEditViewVo.setAddrName(userAddrs.getName() + userAddrs.getAddr());
		// 用户标签
		
		/*List<UserRefTags> userRefTagsList = userRefTagsService.selectList();
		if (!userRefTagsList.isEmpty()) {
		//获得UserRefTags 里面的id的集合ids
		List<Long> tagIds = new ArrayList<Long>();
		for (UserRefTags item : userRefTagsList) {
			tagIds.add(item.getTagId());
		}
		//通过ids到tag表里面获得tag的集合
		
		List<Tags> tag = tagsService.selectByIds(tagIds);*/

		/*List<Tags> tag = tagsService.selectList();
		userEditViewVo.setList(tag);
		
		List<UserRefTags> userRefTags = userRefTagsService.selectListByUserId(userId);
		
		List<Long> tagList =new ArrayList<Long>();
		for (UserRefTags item : userRefTags) {
			tagList.add(item.getTagId());
		}
		userEditViewVo.setTagList(tagList);*/
		List<UserRefTags> userRefTagsList = userRefTagsService
				.selectListByUserId(userId);
		
		if (!userRefTagsList.isEmpty()) {
			
		
		//获得UserRefTags 里面的id的集合ids
		List<Long> tagIds = new ArrayList<Long>();
		for (UserRefTags item : userRefTagsList) {
			tagIds.add(item.getTagId());
		}
		
		List<Tags> tag = tagsService.selectByIds(tagIds);

		userEditViewVo.setList(tag);
		}
 		return userEditViewVo;
	}
	@Override
	public UserEditViewVo getUserAddrEditDetail(List<UserAddrs> userAddrsList,
			Long userId) {
		UserEditViewVo userEditViewVo = initOHVO();

		Users users = usersMapper.selectByPrimaryKey(userId);

		BeanUtilsExp.copyPropertiesIgnoreNull(users, userEditViewVo);

		// userAddr 服务地址名称
		List<Long> userIds = new ArrayList<Long>();
		for (UserAddrs item : userAddrsList) {
			userIds.add(item.getId());
		}
		
		Collections.sort(userIds);
		
		UserAddrs userAddrs = userAddrService.selectByPrimaryKey(userIds.get(0));
		
		userEditViewVo.setAddrName(userAddrs.getName() + userAddrs.getAddr());
		// 用户标签
		
		List<Tags> tag = tagsService.selectList();
		userEditViewVo.setList(tag);
		List<UserRefTags> userRefTags = userRefTagsService.selectListByUserId(userId);
		
		List<Long> tagList =new ArrayList<Long>();
		for (UserRefTags item : userRefTags) {
			tagList.add(item.getTagId());
		}
		userEditViewVo.setTagList(tagList);
		
 		return userEditViewVo;
	}
//用户修改页面展示
	@Override
	public UserEditViewVo getUserEditViewDetail(String orderNo, Long userId) {
		
		UserEditViewVo userEditViewVo = initOHVO();

		Users users = usersMapper.selectByPrimaryKey(userId);

		BeanUtilsExp.copyPropertiesIgnoreNull(users, userEditViewVo);

		Orders orders = ordersService.selectByOrderNo(orderNo);

		// userAddr 服务地址名称
		Long addrId = orders.getAddrId();

		UserAddrs userAddrs = userAddrsMapper.selectByPrimaryKey(addrId);

		if (userAddrs != null) {

			userEditViewVo.setAddrName(userAddrs.getName() + " "
					+ userAddrs.getAddr());
		}

		// 用户标签
		
		List<Tags> tag = tagsService.selectList();
		userEditViewVo.setList(tag);
		List<UserRefTags> userRefTags = userRefTagsService.selectListByUserId(userId);
		
		List<Long> tagList =new ArrayList<Long>();
		for (UserRefTags item : userRefTags) {
			tagList.add(item.getTagId());
		}
		userEditViewVo.setTagList(tagList);
		return userEditViewVo;
	}

	@Override
	public Map<Long, String> selectDictCardDataSource() {
			Map<Long,String> map  = new HashMap<Long, String>();
			List<DictCardType> list = dictCardTypeMapper.selectAll();
			for (Iterator iterator = list.iterator(); iterator.hasNext();) {
				DictCardType dictCardType = (DictCardType) iterator.next();
				map.put(dictCardType.getId(),dictCardType.getName());
			}
			return map;
	}

	@Override
	public DictCardType selectDictCardTypeById(Long id) {
		return dictCardTypeMapper.selectByPrimaryKey(id);
	}

	@Override
	public Map<String, String> getChargeWayDataSource() {
		Map<String ,String> map = new HashMap<String, String>();
		map.put("0","固定金额充值");
		map.put("1","任意金额充值");
		return map;
	}

	@Override
	public List<Users> selectByListPage(List<Long> userIdList, int pageNo,
			int pageSize) {
		PageHelper.startPage(pageNo, pageSize);
		List<Users> list = usersMapper.selectByUserIdPage(userIdList);
		return list;
	}

}
