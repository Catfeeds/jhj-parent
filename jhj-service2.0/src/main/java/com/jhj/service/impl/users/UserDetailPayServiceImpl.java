package com.jhj.service.impl.users;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jhj.common.Constants;
import com.jhj.po.dao.user.UserDetailPayMapper;
import com.jhj.po.model.order.OrderPrices;
import com.jhj.po.model.order.Orders;
import com.jhj.po.model.user.UserDetailPay;
import com.jhj.po.model.user.Users;
import com.jhj.service.users.UserDetailPayService;
import com.jhj.vo.UserDetailSearchVo;
import com.meijia.utils.TimeStampUtil;
import com.jhj.po.model.order.OrderCards;


@Service
public class UserDetailPayServiceImpl implements UserDetailPayService {
	
	@Autowired
	private UserDetailPayMapper userDetailPayMapper;

	@Override
	public int deleteByPrimaryKey(Long id) {
		return userDetailPayMapper.deleteByPrimaryKey(id);
	}

	@Override
	public int insert(UserDetailPay record) {
		return userDetailPayMapper.insert(record);
	}

	@Override
	public int insertSelective(UserDetailPay record) {
		return userDetailPayMapper.insertSelective(record);
	}

	@Override
	public UserDetailPay selectByPrimaryKey(Long id) {
		return userDetailPayMapper.selectByPrimaryKey(id);
	}

	@Override
	public int updateByPrimaryKeySelective(UserDetailPay record) {
		return userDetailPayMapper.updateByPrimaryKeySelective(record);
	}

	@Override
	public int updateByPrimaryKey(UserDetailPay record) {
		return userDetailPayMapper.updateByPrimaryKey(record);
	}

	@Override
	public PageInfo searchVoListPage(UserDetailSearchVo searchVo, int pageNo,
			int pageSize) {
		
		PageHelper.startPage(pageNo, pageSize);
		List<UserDetailPay> list = userDetailPayMapper.selectByListPages(searchVo);
		PageInfo result = new PageInfo(list);
		return result;
	}
	
	/**
	 * 用户明细- 订单支付.
	 */
	@Override
	public UserDetailPay addUserDetailPayForOrder(
			Users user, 
			Orders order, 
			OrderPrices orderPrice, 
			String tradeStatus,
			String tradeNo, 
			String payAccount) {
		
		UserDetailPay userDetailPay = new UserDetailPay();
		
		userDetailPay.setUserId(user.getId());
		userDetailPay.setMobile(user.getMobile());
		userDetailPay.setOrderId(order.getId());
		userDetailPay.setOrderNo(order.getOrderNo());

		userDetailPay.setOrderType(order.getOrderType());
		userDetailPay.setPayType(orderPrice.getPayType());
		userDetailPay.setOrderMoney(orderPrice.getOrderMoney());
		userDetailPay.setOrderPay(orderPrice.getOrderPay());
		
		//trade_no
		userDetailPay.setPayAccount(payAccount);
		userDetailPay.setTradeNo(tradeNo);
		userDetailPay.setTradeStatus(tradeStatus);
		
		userDetailPay.setAddTime(TimeStampUtil.getNowSecond());
		
		userDetailPayMapper.insert(userDetailPay);
		return userDetailPay;
	}	
	
	/*
	 * 更新订单明细信息
	 */
	@Override
	public void updateByPayAccount (String tradeNo, String payAccount) {
		//先查找出账号明细记录
		UserDetailPay record = this.selectByTradeNo(tradeNo);
		if (record.getPayAccount() == null || record.getPayAccount().equals("")) {
			record.setPayAccount(payAccount);
			this.updateByPrimaryKey(record);
		}
	}	
	
	@Override
	public UserDetailPay selectByTradeNo(String tradeNo) {
		return userDetailPayMapper.selectByTradeNo(tradeNo);
	}	
	
	/**
	 * 用户明细- 会员卡充值明细
	 */
	@Override
	public UserDetailPay addUserDetailPayForOrderCard(
			Users user, 
			OrderCards orderCard, 
			String tradeStatus,
			String tradeNo, 
			String payAccount) {
		
		UserDetailPay userDetailPay = new UserDetailPay();
		
		userDetailPay.setUserId(user.getId());
		userDetailPay.setMobile(user.getMobile());
		userDetailPay.setOrderId(orderCard.getId());
		userDetailPay.setOrderNo(orderCard.getCardOrderNo());

		userDetailPay.setOrderType(Constants.ORDER_TYPE_4);
		userDetailPay.setPayType(orderCard.getPayType());
		userDetailPay.setOrderMoney(orderCard.getCardMoney());
		userDetailPay.setOrderPay(orderCard.getCardPay());
		
		//trade_no
		userDetailPay.setPayAccount(payAccount);
		userDetailPay.setTradeNo(tradeNo);
		userDetailPay.setTradeStatus(tradeStatus);
		
		userDetailPay.setAddTime(TimeStampUtil.getNowSecond());
		
		userDetailPayMapper.insert(userDetailPay);
		return userDetailPay;
	}	

}
