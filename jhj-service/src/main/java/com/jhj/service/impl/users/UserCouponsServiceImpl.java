package com.jhj.service.impl.users;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.jhj.common.ConstantMsg;
import com.jhj.common.Constants;
import com.jhj.po.dao.user.UserCouponsMapper;
import com.jhj.po.model.bs.DictCoupons;
import com.jhj.po.model.bs.Gifts;
import com.jhj.po.model.order.OrderPrices;
import com.jhj.po.model.order.Orders;
import com.jhj.po.model.university.PartnerServiceType;
import com.jhj.po.model.user.UserCoupons;
import com.jhj.po.model.user.Users;
import com.jhj.service.bs.DictCouponsService;
import com.jhj.service.bs.GiftsService;
import com.jhj.service.order.OrderPricesService;
import com.jhj.service.order.OrdersService;
import com.jhj.service.university.PartnerServiceTypeService;
import com.jhj.service.users.UserCouponsService;
import com.jhj.service.users.UsersService;
import com.jhj.vo.dict.CouponSearchVo;
import com.jhj.vo.user.UserCouponVo;
import com.jhj.vo.user.UserCouponsVo;
import com.meijia.utils.BeanUtilsExp;
import com.meijia.utils.DateUtil;
import com.meijia.utils.TimeStampUtil;
import com.meijia.utils.vo.AppResultData;

@Service
public class UserCouponsServiceImpl implements UserCouponsService {

	@Autowired
	private UserCouponsMapper userCouponsMapper;

	@Autowired
	private GiftsService giftService;

	@Autowired
	private DictCouponsService dictCouponsService;

	@Autowired
	UsersService userService;

	@Autowired
	private OrdersService ordersService;

	@Autowired
	private OrderPricesService orderPricesService;
	
//	@Autowired
//	private CouponsTypeService couponsTypeService;
	
	@Autowired
	private PartnerServiceTypeService partnerServiceTypeService;

	@Override
	public UserCoupons initUserCoupons() {
		UserCoupons record = new UserCoupons();
		record.setId(0L);
		record.setUserId(0L);
		record.setCouponId(0L);
		record.setValue(new BigDecimal(0));
		record.setGiftId(0L);
		record.setFromDate(DateUtil.getNowOfDate());
		record.setToDate(DateUtil.getNowOfDate());
		record.setIsUsed((short) 0);
		record.setUsedTime(0L);
		record.setOrderNo("");
		record.setServiceType("");
		record.setAddTime(TimeStampUtil.getNowSecond());

		return record;

	}

	@Override
	public int deleteByPrimaryKey(Long id) {
		return userCouponsMapper.deleteByPrimaryKey(id);
	}

	@Override
	public int insert(UserCoupons record) {
		return userCouponsMapper.insert(record);
	}

	@Override
	public int insertSelective(UserCoupons record) {
		return userCouponsMapper.insertSelective(record);
	}

	@Override
	public UserCoupons selectByPrimaryKey(Long id) {
		return userCouponsMapper.selectByPrimaryKey(id);
	}

	@Override
	public int updateByPrimaryKeySelective(UserCoupons record) {
		return userCouponsMapper.updateByPrimaryKeySelective(record);
	}

	@Override
	public int updateByPrimaryKey(UserCoupons record) {
		return userCouponsMapper.updateByPrimaryKey(record);
	}

	@Override
	public List<UserCoupons> selectByUserId(Long userId) {
		return userCouponsMapper.selectByUserId(userId);
	}

	@Override
	public List<UserCoupons> selectByCouponId(Long couponId) {
		return userCouponsMapper.selectByCouponId(couponId);
	}

	@Override
	public List<UserCoupons> selectByCouponIdNotUserId(Long couponId,
			Long userId) {
		return userCouponsMapper.selectByCouponIdNotUserId(couponId, userId);
	}

	@Override
	public List<UserCoupons> selectByCouponIdAndUserId(Long couponId,
			Long userId) {
		return userCouponsMapper.selectByCouponIdAndUserId(couponId, userId);
	}

	@Override
	public UserCoupons selectByOrderNo(String orderNo) {
		return userCouponsMapper.selectByOrderNo(orderNo);
	}

	@Override
	public UserCouponVo changeToUserCouponVo(UserCoupons record) {
		UserCouponVo vo = new UserCouponVo();
		BeanUtilsExp.copyPropertiesIgnoreNull(record, vo);
		Gifts gift = giftService.selectByPrimaryKey(vo.getGiftId());
		if (gift != null) {
			vo.setGiftName(gift.getName());
		}
		DictCoupons dictCoupon = dictCouponsService.selectByPrimaryKey(vo
				.getCouponId());
		vo.setValue(dictCoupon.getValue());
		vo.setMaxValue(dictCoupon.getMaxValue());
		vo.setCouponType(dictCoupon.getCouponType());
		vo.setRangType(dictCoupon.getRangType());
		vo.setRangFrom(dictCoupon.getRangFrom());

		String fromDateStr = DateUtil.formatDate(vo.getFromDate());
		String toDateStr = DateUtil.formatDate(vo.getToDate());

		vo.setFromDateStr(fromDateStr);
		vo.setToDateStr(toDateStr);
		return vo;
	}

	@Override
	public List<UserCouponVo> changeToUserCouponVos(List<UserCoupons> list) {

		List<UserCouponVo> result = new ArrayList<UserCouponVo>();
		if (list.isEmpty())
			return result;

		List<DictCoupons> dictCoupons = dictCouponsService.selectAll();
		List<Gifts> gifts = giftService.selectAll();
		UserCoupons record = null;
		for (int i = 0; i < list.size(); i++) {
			record = list.get(i);
			UserCouponVo vo = new UserCouponVo();
			BeanUtilsExp.copyPropertiesIgnoreNull(record, vo);
			vo.setCouponsTypeId("");
			for (Gifts gift : gifts) {
				if (gift.getGiftId().equals(vo.getGiftId())) {
					vo.setGiftName(gift.getName());
					break;
				}
			}

			for (DictCoupons dictCoupon : dictCoupons) {
				if (dictCoupon.getId().equals(vo.getCouponId())) {
					vo.setValue(dictCoupon.getValue());
					vo.setMaxValue(dictCoupon.getMaxValue());
					vo.setCouponType(dictCoupon.getCouponType());
					vo.setRangType(dictCoupon.getRangType());
					vo.setRangFrom(dictCoupon.getRangFrom());
					vo.setIntroduction(dictCoupon.getIntroduction());
					vo.setCouponsTypeId(String.valueOf(dictCoupon.getCouponsTypeId()));
//					couponsTypeService.selectByPrimaryKey(dictCoupon.getCouponsTypeId()).getCouponsTypeDesc()
					vo.setCouponsTypeDesc("");
					String serviceTypeId = dictCoupon.getServiceType();
					if(serviceTypeId!=null && !serviceTypeId.equals("")){
						PartnerServiceType serviceType = partnerServiceTypeService.selectByPrimaryKey(Long.parseLong(serviceTypeId));
						vo.setServiceTypeName((serviceType!=null)?serviceType.getName():"");
					}
					String fromDateStr = DateUtil.formatDate(vo.getFromDate());
					String toDateStr = DateUtil.formatDate(vo.getToDate());
					vo.setFromDateStr(fromDateStr);
					vo.setToDateStr(toDateStr);
					break;
				}
			}
			result.add(vo);
		}
		return result;
	}

	/**
	 * 验证优惠劵有效. 适用于支付 1. 判断优惠劵是否存在. 2. 判断优惠劵是否已经使用过. 3. 判断优惠劵是否在有效期内,包括开始时间和结束时间
	 * 4. 判断优惠劵的服务类型是否正确. 5. 判断优惠劵消费金额必须满多少可使用. 6. 判断优惠劵是否属于该用户.
	 */
	@Override
	public AppResultData<Object> validateCouponForPay(Long userId,
			Long userCouponId, Long orderId) {

		AppResultData<Object> result = new AppResultData<Object>(
				Constants.SUCCESS_0, ConstantMsg.SUCCESS_0_MSG, "");

		Orders order = ordersService.selectByPrimaryKey(orderId);

		OrderPrices orderPrice = orderPricesService.selectByOrderId(orderId);

		Users u = userService.selectByPrimaryKey(userId);

		UserCoupons userCoupon = selectByPrimaryKey(userCouponId);

		UserCouponVo userCouponVo = changeToUserCouponVo(userCoupon);

		// 1. 判断优惠劵是否存在.
		if (userCoupon == null) {
			result.setStatus(Constants.ERROR_999);
			result.setMsg(ConstantMsg.COUPON_INVALID_MSG);
			return result;
		}

		// 2. 判断优惠劵是否已经使用过.
		if (userCoupon.getIsUsed().equals((short) 1)) {
			result.setStatus(Constants.ERROR_999);
			result.setMsg(ConstantMsg.COUPON_IS_USED_MSG);
			return result;
		}

		// 3. 判断优惠劵是否在有效期内,包括开始时间和结束时间
		if (userCoupon.getFromDate().after(DateUtil.getNowOfDate())
				|| userCoupon.getToDate().before(DateUtil.getNowOfDate())) {
			result.setStatus(Constants.ERROR_999);
			result.setMsg(ConstantMsg.COUPON_IS_INVALID);
			return result;
		}

		// 4. 判断优惠劵的服务类型是否正确.
		String serviceType = order.getServiceType().toString();
		if (!userCoupon.getServiceType().equals("0")) {
			if (!serviceType.equals(userCoupon.getServiceType())) {
				result.setStatus(Constants.ERROR_999);
				result.setMsg(ConstantMsg.COUPON_IS_INVALID);
				return result;
			}
		}


		// 5. 判断优惠劵消费金额必须满多少可使用. 3.1版本不需要判断
		BigDecimal orderMoney = orderPrice.getOrderMoney();
		BigDecimal maxValue = userCouponVo.getMaxValue();
		if (!maxValue.equals(BigDecimal.ZERO)) {
			if (orderMoney.compareTo(maxValue) == -1) {
				result.setStatus(Constants.ERROR_999);
				result.setMsg(ConstantMsg.COUPON_IS_INVALID);
				return result;
			}
		}

		// 6. 判断优惠劵是否属于该用户.
		if (!userCoupon.getUserId().equals(userId)) {
			result.setStatus(Constants.ERROR_999);
			result.setMsg(ConstantMsg.COUPON_IS_INVALID);
			return result;
		}

		return result;
	}

	/**
	 * 验证用户优惠劵兑换的方法，适用于用户通过兑换码兑换优惠劵 1. 优惠劵是否已经无效，结束时间超过当前时间 2.
	 * 如果为唯一优惠劵，则需要判断是否被其他用户兑换过 3. 该用户是否已经兑换过该优惠劵
	 */
	@Override
	public AppResultData<Object> validateCouponForPost(Users u,
			DictCoupons dictCoupon) {

		AppResultData<Object> result = new AppResultData<Object>(
				Constants.SUCCESS_0, ConstantMsg.SUCCESS_0_MSG, "");

		// 1. 优惠劵是否已经无效，结束时间超过当前时间
		if (dictCoupon.getToDate().before(DateUtil.getNowOfDate())) {
			result.setStatus(Constants.ERROR_999);
			result.setMsg(ConstantMsg.COUPON_EXP_TIME_MSG);
		}

		// 2. 如果为唯一优惠劵，则需要判断是否被其他用户兑换过
		if (dictCoupon.getRangType().equals((short) 1)) {
			List<UserCoupons> list = selectByCouponIdNotUserId(
					dictCoupon.getId(), u.getId());

			if (list != null && list.size() > 0) {
				result.setStatus(Constants.ERROR_999);
				result.setMsg(ConstantMsg.COUPON_IS_INVALID);
			}
		}

		// 3.该用户是否已经兑换过该优惠劵
		List<UserCoupons> listExist = selectByCouponIdAndUserId(
				dictCoupon.getId(), u.getId());

		if (listExist != null && listExist.size() > 0) {
			result.setStatus(Constants.ERROR_999);
			result.setMsg(ConstantMsg.COUPON_IS_USED_MSG);
		}

		return result;
	}

	@Override
	public List<UserCoupons> searchVoListPage(Long id, int pageNo, int pageSize) {

		PageHelper.startPage(pageNo, pageSize);
		List<UserCoupons> list = userCouponsMapper.selectByCouponId(id);

		return list;
	}

	@Override
	public UserCouponsVo getUsersCounps(UserCoupons userCoupons) {

		UserCouponsVo vo = new UserCouponsVo();

		try {
			BeanUtils.copyProperties(vo, userCoupons);
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// 优惠券名称
		DictCoupons dictCoupons = dictCouponsService
				.selectByPrimaryKey(userCoupons.getCouponId());
		vo.setCouponsName(dictCoupons.getDescription());

		return vo;
	}

	@Override
	public List<UserCoupons> selectAllByUserId(Long userId) {
		
		return userCouponsMapper.selectAllByUserId(userId);
	}

	@Override
	public List<Long> selectLoginGift(Long userId) {
		return userCouponsMapper.selectLoginGift(userId);
	}

	@Override
	public int insertByList(List<UserCoupons> userCouponsList) {
		userCouponsMapper.insertByList(userCouponsList);
		return 0;
	}

	@Override
	public UserCoupons initUserCoupons(Long userId,DictCoupons coupons){
    	UserCoupons uc=new UserCoupons();
    	uc.setUserId(userId);
    	uc.setCouponId(coupons.getId());
    	uc.setValue(coupons.getValue());
    	uc.setGiftId(0L);
    	uc.setServiceType(String.valueOf(coupons.getServiceType()));
    	uc.setFromDate(DateUtil.getNowOfDate());
    	String toDateStr = DateUtil.addDay(DateUtil.getNowOfDate(), coupons.getRangMonth().intValue(), Calendar.MONTH, DateUtil.DEFAULT_PATTERN);
    	uc.setToDate(DateUtil.parse(toDateStr));
    	uc.setIsUsed((short)0);
    	uc.setUsedTime(0L);
    	uc.setAddTime(TimeStampUtil.getNow() / 1000);
    	return uc;
    }

	@Override
	public List<UserCoupons> selectByUserCoupons(UserCoupons userCoupons) {
		return userCouponsMapper.selectByUserCoupons(userCoupons);
	}
}
