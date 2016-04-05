package com.jhj.service.users;

import java.util.List;

import com.github.pagehelper.PageInfo;
import com.jhj.po.model.order.OrderCards;
import com.jhj.po.model.order.OrderPrices;
import com.jhj.po.model.order.Orders;
import com.jhj.po.model.user.UserDetailPay;
import com.jhj.po.model.user.Users;
import com.jhj.vo.UserDetailSearchVo;
import com.jhj.vo.user.AppUserDetailPayVo;

public interface UserDetailPayService {
	
    int deleteByPrimaryKey(Long id);

    int insert(UserDetailPay record);

    int insertSelective(UserDetailPay record);

    UserDetailPay selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(UserDetailPay record);

    int updateByPrimaryKey(UserDetailPay record);
    
	PageInfo searchVoListPage(UserDetailSearchVo searchVo,int pageNo,int pageSize);

	UserDetailPay addUserDetailPayForOrder(Users user, Orders order, OrderPrices orderPrice, String tradeStatus, String tradeNo, String payAccount);

	void updateByPayAccount(String tradeNo, String payAccount);

	UserDetailPay selectByTradeNo(String tradeNo);

	UserDetailPay addUserDetailPayForOrderCard(Users user, OrderCards orderCard, String tradeStatus, String tradeNo, String payAccount);
	
	/*
	 *  微网站--我的--余额--消费明细
	 */
	List<UserDetailPay> appSelectByListPage(UserDetailSearchVo searchVo,int pageNo,int pageSize);
	
	List<AppUserDetailPayVo> transToListVo(List<UserDetailPay> list);
	
	AppUserDetailPayVo initPayVo(UserDetailPay detailPay);
}
