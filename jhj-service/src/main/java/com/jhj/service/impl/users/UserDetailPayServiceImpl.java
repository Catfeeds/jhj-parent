package com.jhj.service.impl.users;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jhj.common.Constants;
import com.jhj.po.dao.user.UserDetailPayMapper;
import com.jhj.po.model.bs.OrgStaffs;
import com.jhj.po.model.order.OrderCards;
import com.jhj.po.model.order.OrderPriceExt;
import com.jhj.po.model.order.OrderPrices;
import com.jhj.po.model.order.Orders;
import com.jhj.po.model.period.PeriodOrder;
import com.jhj.po.model.user.UserDetailPay;
import com.jhj.po.model.user.Users;
import com.jhj.service.bs.OrgStaffsService;
import com.jhj.service.order.OrderCardsService;
import com.jhj.service.users.UserDetailPayService;
import com.jhj.vo.staff.StaffSearchVo;
import com.jhj.vo.user.AppUserDetailPayVo;
import com.jhj.vo.user.UserDetailSearchVo;
import com.meijia.utils.BeanUtilsExp;
import com.meijia.utils.DateUtil;
import com.meijia.utils.OneCareUtil;
import com.meijia.utils.TimeStampUtil;

@Service
public class UserDetailPayServiceImpl implements UserDetailPayService {

	@Autowired
	private UserDetailPayMapper userDetailPayMapper;

	@Autowired
	private OrderCardsService orderCardsService;

	@Autowired
	private OrgStaffsService staffService;

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
	public PageInfo searchVoListPage(UserDetailSearchVo searchVo, int pageNo, int pageSize) {

		PageHelper.startPage(pageNo, pageSize);
		List<UserDetailPay> list = userDetailPayMapper.selectByListPages(searchVo);
		PageInfo result = new PageInfo(list);
		return result;
	}

	/**
	 * 用户明细- 订单支付.
	 */
	@Override
	public UserDetailPay addUserDetailPayForOrder(Users user, Orders order, OrderPrices orderPrice, String tradeStatus, String tradeNo, String payAccount) {

		UserDetailPay userDetailPay = this.initUserDetailPay();

		userDetailPay.setUserId(user.getId());
		userDetailPay.setMobile(user.getMobile());
		userDetailPay.setOrderId(order.getId());
		userDetailPay.setOrderNo(order.getOrderNo());

		userDetailPay.setOrderType(Constants.USER_DETAIL_ORDER_TYPE_0);
		userDetailPay.setPayType(orderPrice.getPayType());
		userDetailPay.setOrderMoney(orderPrice.getOrderMoney());
		userDetailPay.setOrderPay(orderPrice.getOrderPay());
		userDetailPay.setRestMoney(user.getRestMoney());
		// trade_no
		userDetailPay.setPayAccount(payAccount);
		userDetailPay.setTradeNo(tradeNo);
		userDetailPay.setTradeStatus(tradeStatus);

		userDetailPay.setAddTime(TimeStampUtil.getNowSecond());

		userDetailPayMapper.insert(userDetailPay);
		return userDetailPay;
	}

	/**
	 * 用户明细- 订单差价支付.
	 */
	@Override
	public UserDetailPay addUserDetailPayForOrderPayExt(Users user, Orders order, OrderPriceExt orderPriceExt, String tradeStatus, String tradeNo,
			String payAccount) {

		UserDetailPay userDetailPay = this.initUserDetailPay();

		userDetailPay.setUserId(user.getId());
		userDetailPay.setMobile(user.getMobile());
		userDetailPay.setOrderId(order.getId());
		userDetailPay.setOrderNo(order.getOrderNo());
		userDetailPay.setOrderType(Constants.USER_DETAIL_ORDER_TYPE_3);
		userDetailPay.setPayType(orderPriceExt.getPayType());
		userDetailPay.setOrderMoney(orderPriceExt.getOrderPay());
		userDetailPay.setOrderPay(orderPriceExt.getOrderPay());
		userDetailPay.setRestMoney(user.getRestMoney());
		// trade_no
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
	public void updateByPayAccount(String tradeNo, String payAccount) {
		// 先查找出账号明细记录
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
	public UserDetailPay addUserDetailPayForOrderCard(Users user, OrderCards orderCard, String tradeStatus, String tradeNo, String payAccount) {

		UserDetailPay userDetailPay = this.initUserDetailPay();

		userDetailPay.setUserId(user.getId());
		userDetailPay.setMobile(user.getMobile());
		userDetailPay.setOrderId(orderCard.getId());
		userDetailPay.setOrderNo(orderCard.getCardOrderNo());

		userDetailPay.setOrderType(Constants.USER_DETAIL_ORDER_TYPE_1);
		userDetailPay.setPayType(orderCard.getPayType());
		userDetailPay.setOrderMoney(orderCard.getCardMoney());
		userDetailPay.setOrderPay(orderCard.getCardPay());
		userDetailPay.setRestMoney(user.getRestMoney());
		// trade_no
		userDetailPay.setPayAccount(payAccount);
		userDetailPay.setTradeNo(tradeNo);
		userDetailPay.setTradeStatus(tradeStatus);

		userDetailPay.setAddTime(TimeStampUtil.getNowSecond());

		userDetailPayMapper.insert(userDetailPay);
		return userDetailPay;
	}

	@Override
	public List<UserDetailPay> appSelectByListPage(UserDetailSearchVo searchVo, int page, int pageSize) {

		PageHelper.startPage(page, pageSize);

		return userDetailPayMapper.selectByListPages(searchVo);
	}

	@Override
	public List<AppUserDetailPayVo> transToListVo(List<UserDetailPay> list) {

		List<AppUserDetailPayVo> voList = new ArrayList<AppUserDetailPayVo>();

		if (!list.isEmpty() || list.size() > 0) {

			for (UserDetailPay pay : list) {

				AppUserDetailPayVo vo = initVo(pay);

				Short orderType = pay.getOrderType();
				String orderTypeName = OneCareUtil.getOrderTypeForDetailPay(orderType);
				vo.setOrderTypeName(orderTypeName);
				
				vo.setOrderFlag(Constants.USER_PAY_FLAG_MINUS);
				
				if (orderType.equals(Constants.USER_DETAIL_ORDER_TYPE_1) || orderType.equals(Constants.USER_DETAIL_ORDER_TYPE_5) ) {
					vo.setOrderFlag(Constants.USER_PAY_FLAG_PLUS);
					vo.setImgUrl("img/userRestMoney/iconfont-jiahao.png");
				} else {
					vo.setOrderFlag(Constants.USER_PAY_FLAG_MINUS);
					vo.setImgUrl("img/userRestMoney/iconfont-jianhao.png");
				}

				vo.setAddTimeStr(DateUtil.convTimeStampToStringDate(pay.getAddTime(), "yyyy-MM-dd HH:mm:ss"));

				voList.add(vo);
			}
		}

		return voList;
	}

	@Override
	public AppUserDetailPayVo initVo(UserDetailPay pay) {

		AppUserDetailPayVo payVo = new AppUserDetailPayVo();

		BeanUtilsExp.copyPropertiesIgnoreNull(pay, payVo);

		payVo.setOrderTypeName("");

		payVo.setPayTypeName("");

		// 用户 消费记录的 作用。。 0= 支付 1=充值
		payVo.setOrderFlag(Constants.USER_PAY_FLAG_MINUS);

		// 默认设置 为 "+" 号的图片吧
		payVo.setImgUrl("img/userRestMoney/iconfont-jiahao.png");

		payVo.setAddTimeStr("");

		return payVo;
	}

	@Override
	public UserDetailPay initUserDetailPay() {

		UserDetailPay detailPay = new UserDetailPay();

		detailPay.setId(0L);
		detailPay.setPayAccount("");
		detailPay.setUserId(0L);
		detailPay.setMobile("");
		detailPay.setOrderType(Constants.ORDER_TYPE_0);
		detailPay.setOrderId(0L);
		detailPay.setOrderNo("");
		detailPay.setOrderMoney(new BigDecimal(0));
		detailPay.setOrderPay(new BigDecimal(0));
		detailPay.setRestMoney(new BigDecimal(0));
		detailPay.setTradeNo("");
		detailPay.setTradeStatus("");
		detailPay.setPayType((short) 0);
		detailPay.setAddTime(TimeStampUtil.getNowSecond());
		detailPay.setRemarks("");

		return detailPay;
	}

	@Override
	public List<UserDetailPay> selectBySearchVo(UserDetailSearchVo searchVo) {
		return userDetailPayMapper.selectBySearchVo(searchVo);
	}

	@Override
	public Map<String, BigDecimal> totolMoeny(UserDetailSearchVo userDetailSearchVo) {
		return userDetailPayMapper.totolMoeny(userDetailSearchVo);
	}

	@Override
	public UserDetailPay addUserDetailPayForOrder(Users user,
			PeriodOrder periodOrder, OrderPrices orderPrice,
			String tradeStatus, String tradeNo, String payAccount) {
		
		UserDetailPay userDetailPay = this.initUserDetailPay();

		userDetailPay.setUserId(user.getId());
		userDetailPay.setMobile(user.getMobile());
		userDetailPay.setOrderId(periodOrder.getId().longValue());
		userDetailPay.setOrderNo(periodOrder.getOrderNo());

		userDetailPay.setOrderType(Constants.USER_DETAIL_ORDER_TYPE_10);
		userDetailPay.setPayType(orderPrice.getPayType());
		userDetailPay.setOrderMoney(orderPrice.getOrderMoney());
		userDetailPay.setOrderPay(orderPrice.getOrderPay());
		userDetailPay.setRestMoney(user.getRestMoney());
		// trade_no
		userDetailPay.setPayAccount(payAccount);
		userDetailPay.setTradeNo(tradeNo);
		userDetailPay.setTradeStatus(tradeStatus);

		userDetailPay.setAddTime(TimeStampUtil.getNowSecond());

		userDetailPayMapper.insert(userDetailPay);
		return userDetailPay;
		
	}

	
}
