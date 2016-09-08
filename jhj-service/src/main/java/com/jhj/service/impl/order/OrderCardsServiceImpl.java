package com.jhj.service.impl.order;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jhj.po.dao.dict.DictCardTypeMapper;
import com.jhj.po.dao.order.OrderCardsMapper;
import com.jhj.po.dao.user.UserPayStatusMapper;
import com.jhj.po.model.bs.Gifts;
import com.jhj.po.model.dict.DictCardType;
import com.jhj.po.model.order.OrderCards;
import com.jhj.po.model.user.UserCoupons;
import com.jhj.po.model.user.UserPayStatus;
import com.jhj.po.model.user.Users;
import com.jhj.service.bs.GiftsService;
import com.jhj.service.order.OrderCardsService;
import com.jhj.service.users.UserCouponsService;
import com.jhj.service.users.UserDetailPayService;
import com.jhj.service.users.UsersService;
import com.jhj.vo.bs.GiftCouponVo;
import com.jhj.vo.bs.GiftVo;
import com.jhj.vo.user.UserCardVo;
import com.jhj.vo.user.UserChargeVo;
import com.meijia.utils.DateUtil;
import com.meijia.utils.OrderNoUtil;
import com.meijia.utils.TimeStampUtil;

@Service
public class OrderCardsServiceImpl implements OrderCardsService {
	@Autowired
	OrderCardsMapper orderCardsMapper;
	
	@Autowired
	UsersService usersService;
	
	@Autowired
	UserCouponsService userCouponService;	
	
	@Autowired
	UserPayStatusMapper userPayStatusMapper;
	
	@Autowired
	private UserDetailPayService userDetailPayService;
	
	@Autowired
	private DictCardTypeMapper dictCardTypeMapper;
	
	@Autowired
	private GiftsService giftService;	

	@Override
	public int deleteByPrimaryKey(Long id) {
		return orderCardsMapper.deleteByPrimaryKey(id);
	}

	@Override
	public Long insert(OrderCards record) {
		return orderCardsMapper.insert(record);
	}

	@Override
	public int updateOrderByOnlinePay(OrderCards orderCards, String tradeNo, String tradeStatus, String payAccount) {
		// 1) 判断字段order_status = 1
		//   4. 如果该订单为未支付的状态，则需要做如下的操作
		// 1) 操作表user_pay_status ，插入一条新的记录，记录支付的信息
		// 2) 用户的消费明细记录，操作表为user_detail_pay
		// 3) 将card_orders 表的状态改变为 order_status = 2 ,已支付状态, 支付状态pay_type = 1
		// 支付宝支付
		// 4) 用户余额，扣除相应的金额，注意如果有优惠卷的金额，操作表为users
		// 6) 查看是否有需要赠送的优惠劵，并且赠送到用户下.
		// 注意以上4个步骤必须为同一个事务。
		Long userId = orderCards.getUserId();
		Users users = usersService.getUserById(userId);

		UserPayStatus userPayStatus = new UserPayStatus();
		userPayStatus.setMobile(users.getMobile());
		userPayStatus.setOrderId(orderCards.getId());
		userPayStatus.setOrderNo(orderCards.getCardOrderNo());
		userPayStatus.setOrderType(orderCards.getPayType());
		userPayStatus.setTradeId(tradeNo);
		userPayStatus.setTradeStatus(tradeStatus);
		userPayStatus.setUserId(orderCards.getUserId());
		userPayStatus.setPostParams("");// TODO PostParams
		userPayStatus.setIsSync((short) 0);
		userPayStatus.setAddTime(TimeStampUtil.getNowSecond());
		userPayStatusMapper.insert(userPayStatus);

		
		BigDecimal cardMoney = orderCards.getCardMoney();
		DictCardType dictCardType = dictCardTypeMapper.selectByPrimaryKey(orderCards.getCardType());
		BigDecimal restMoney = users.getRestMoney().add(cardMoney);
		BigDecimal lastRestMoney = restMoney.add(dictCardType.getSendMoney());
		users.setRestMoney(lastRestMoney);
		users.setUpdateTime(TimeStampUtil.getNowSecond());
		usersService.updateByPrimaryKeySelective(users);

		orderCardsMapper.updateByPrimaryKeySelective(orderCards);
		
		userDetailPayService.addUserDetailPayForOrderCard(
				users, orderCards, tradeStatus, tradeNo, payAccount);
				
		return 1;

	}
	
	@Override 
	public boolean sendCoupons(Long userId, Long orderId) {

		OrderCards orderCard = orderCardsMapper.selectByPrimaryKey(orderId);
		DictCardType dictCardType = dictCardTypeMapper.selectByPrimaryKey(orderCard.getCardType());
		Long giftId = dictCardType.getGiftId();
		if (giftId <= 0L) return true;
		
		Gifts gift = giftService.selectByPrimaryKey(giftId);
		GiftVo giftVo = giftService.changeToGiftVo(gift);
		List<GiftCouponVo> giftCoupons = giftVo.getGiftCoupons();
		if (giftVo.getGiftCoupons().isEmpty()) return true;
		
		GiftCouponVo item = null;
		for (int i = 0; i < giftCoupons.size(); i++) {
			item = giftCoupons.get(i);
			UserCoupons record = userCouponService.initUserCoupons();
			record.setUserId(userId);
			record.setCouponId(item.getCouponId());
			record.setValue(item.getValue());
			record.setGiftId(item.getGiftId());
			record.setServiceType(item.getServiceType());
			
			Date fromDate = DateUtil.getNowOfDate();
			record.setFromDate(fromDate);
			
			
			String toDateStr = DateUtil.addDay(fromDate, item.getRangMonth().intValue(), Calendar.MONTH, DateUtil.DEFAULT_PATTERN);
			Date toDate = DateUtil.parse(toDateStr);
			record.setToDate(toDate);
			
			int num = item.getNum().intValue();
			if (num > 0) {
				for (int j =0 ; j < num; j++) {
					userCouponService.insert(record);
				}
			}
		}

		return true;
	}

	@Override
	public OrderCards initOrderCards(Users users, Long cardType, 
			DictCardType dictCardType, Short payType) {
		OrderCards record = new OrderCards();
		record.setCardMoney(dictCardType.getCardValue());
		record.setCardPay(dictCardType.getCardPay());
		record.setUserId(users.getId());
		record.setMobile(users.getMobile());
		record.setCardType(cardType);
		record.setAddTime(TimeStampUtil.getNowSecond());
		record.setOrderStatus((short) 0);
		record.setPayType(payType);
		record.setUpdateTime(0L);

		String cardOrderNo = String.valueOf(OrderNoUtil.getOrderCardNo());
		record.setCardOrderNo(cardOrderNo);

		return record;
	}
	@Override
	public OrderCards initOrderCards(Users users, 
		UserChargeVo userChargeVo, Short payType) {
		OrderCards record = new OrderCards();
		record.setCardMoney(userChargeVo.getChargeMoney());
		record.setCardPay(userChargeVo.getChargeMoney());
		record.setUserId(users.getId());
		record.setMobile(users.getMobile());
		record.setCardType(0L);//此为任意金额充值
		record.setAddTime(TimeStampUtil.getNowSecond());
		record.setOrderStatus((short) 0);
		record.setPayType(payType);
		record.setUpdateTime(0L);
		
		String cardOrderNo = String.valueOf(OrderNoUtil.getOrderCardNo());
		record.setCardOrderNo(cardOrderNo);
		
		return record;
	}

	@Override
	public int insertSelective(OrderCards record) {
		return orderCardsMapper.insertSelective(record);
	}

	@Override
	public OrderCards selectByPrimaryKey(Long id) {
		return orderCardsMapper.selectByPrimaryKey(id);
	}

	@Override
	public int updateByPrimaryKey(OrderCards record) {
		return orderCardsMapper.updateByPrimaryKey(record);
	}

	@Override
	public int updateByPrimaryKeySelective(OrderCards record) {
		return orderCardsMapper.updateByPrimaryKeySelective(record);
	}

	@Override
	public OrderCards selectByOrderCardsNo(String cardOrderNo) {
		return orderCardsMapper.selectByOrderCardsNo(cardOrderNo);
	}

	@Override
	public List<OrderCards> selectNoPayByUserId(Long userId) {
		return orderCardsMapper.selectNoPayByUserId(userId);
	}

	@Override
	public UserCardVo getUserCardVoByOrderCards(OrderCards orderCards) {
		UserCardVo userCardVo = new UserCardVo();
		DictCardType dictCardType = dictCardTypeMapper.selectByPrimaryKey(orderCards.getCardType());
		try {
			BeanUtils.copyProperties(userCardVo, orderCards);
			userCardVo.setDescription(dictCardType.getDescription());
			userCardVo.setCardTypName(dictCardType.getName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return userCardVo;
	}
	
	
	
}