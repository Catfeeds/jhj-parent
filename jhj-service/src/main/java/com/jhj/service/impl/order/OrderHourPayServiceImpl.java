package com.jhj.service.impl.order;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jhj.po.model.bs.DictCoupons;
import com.jhj.po.model.user.UserCoupons;
import com.jhj.service.bs.DictCouponsService;
import com.jhj.service.order.OrderHourPayService;
import com.jhj.service.users.UserCouponsService;
import com.jhj.vo.order.OrderHourPayVo;
import com.meijia.utils.DateUtil;

/**
 *
 * @author :hulj
 * @Date : 2015年8月3日下午4:43:17
 * @Description: TODO
 *
 */

@Service
public class OrderHourPayServiceImpl implements OrderHourPayService {

	@Autowired
	private UserCouponsService userCouponService;
	@Autowired
	private DictCouponsService dictCouponService;
	
	/*
	 * 得到当前用户 可用 的 优惠券
	 */
	@Override
	public List<DictCoupons> getCouponsByUserId(Long userId) {
		
		List<UserCoupons> list = userCouponService.selectByUserId(userId);
		List<DictCoupons> listNew = new ArrayList<DictCoupons>();
		//过滤优惠券是否失效
		for (Iterator<UserCoupons> iterator = list.iterator(); iterator.hasNext();) {
			UserCoupons userCoupons = iterator.next();
			DictCoupons dictCoupons = dictCouponService.selectByPrimaryKey(userCoupons.getCouponId());
			if(DateUtil.getNowOfDate().before(dictCoupons.getToDate())){
				
				listNew.add(dictCoupons);
			}
		}
		
		return listNew;
	}

	@Override
	public OrderHourPayVo initOrderHourPayVo() {
		
		OrderHourPayVo orderHourPayVo  = new OrderHourPayVo();
		
		orderHourPayVo.setOrderMoney(new BigDecimal(0));
		orderHourPayVo.setServiceDate(0L);
		orderHourPayVo.setServiceHour((short)0);
		orderHourPayVo.setServiceAddr("");
		orderHourPayVo.setRemark("");
		orderHourPayVo.setDictList(new ArrayList<DictCoupons>());
		orderHourPayVo.setRealOrderMoney(0L);
		orderHourPayVo.setPayType((short)0);	//付款方式 0 = 余额支付 1 = 支付宝 2 = 微信支付 3 = 智慧支付 4 = 上门刷卡（保留，站位）
		
		return orderHourPayVo;
	}

}
