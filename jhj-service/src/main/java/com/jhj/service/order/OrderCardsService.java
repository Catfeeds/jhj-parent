package com.jhj.service.order;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jhj.po.model.dict.DictCardType;
import com.jhj.po.model.order.OrderCards;
import com.jhj.po.model.user.Users;
import com.jhj.vo.order.OrderCardsVo;
import com.jhj.vo.user.UserCardVo;
import com.jhj.vo.user.UserChargeVo;

public interface OrderCardsService {
	int deleteByPrimaryKey(Long id);

	Long insert(OrderCards record);

	int insertSelective(OrderCards record);

	OrderCards selectByOrderCardsNo(String cardOrderNo);

	OrderCards selectByPrimaryKey(Long id);

	int updateByPrimaryKeySelective(OrderCards record);

	int updateByPrimaryKey(OrderCards record);

	OrderCards initOrderCards(Users users, Long cardType, DictCardType dictCardType, Short payType);

	int updateOrderByOnlinePay(OrderCards orderCards, String tradeNo, String tradeStatus, String payAccount);
	
	List<OrderCards> selectNoPayByUserId(Long userId);
	
	UserCardVo getUserCardVoByOrderCards(OrderCards orderCards);

	boolean sendCoupons(Long userId, Long cardType);

	OrderCards initOrderCards(Users user, UserChargeVo userChargeVo, Short payType);
	
	List<OrderCards> selectByVo(OrderCardsVo vo);
	
	PageInfo selectByListPage(OrderCardsVo vo,int pageNo,int pageSize);
	
	OrderCardsVo transVo(OrderCards orderCards);
	
	Map<String,Double> countTotal(OrderCardsVo orderCardsVo);

	List<OrderCards> selectBySearchVo(OrderCardsVo vo);
}