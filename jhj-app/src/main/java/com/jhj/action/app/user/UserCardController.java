package com.jhj.action.app.user;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.jhj.action.app.BaseController;
import com.jhj.common.ConstantMsg;
import com.jhj.common.Constants;
import com.meijia.utils.SmsUtil;
import com.meijia.utils.TimeStampUtil;
import com.meijia.utils.vo.AppResultData;
import com.jhj.po.model.dict.DictCardType;
import com.jhj.po.model.order.OrderCards;
import com.jhj.po.model.university.PartnerServiceType;
import com.jhj.po.model.user.Users;
import com.jhj.service.dict.CardTypeService;
import com.jhj.service.order.OrderCardsService;
import com.jhj.service.users.UsersService;

@Controller
@RequestMapping(value="/app/user")
public class UserCardController extends BaseController {
	
	@Autowired
	private UsersService usersService;
	
	@Autowired
	private OrderCardsService orderCardsService;
	
	@Autowired
	private CardTypeService cardTypeService;	
	
	

	// 8. 会员充值接口
	/**
	 * userId true long 用户Id
	 * card_type true int 充值卡类型
	 * pay_type true int 支付类型 0 =
	 * 余额支付 1 = 支付宝 2 = 微信支付 3 = 智慧支付 4 = 上门刷卡（保留，站位）
	 */
	@RequestMapping(value = "card_buy", method = RequestMethod.POST)
	public AppResultData<Object> cardBuy(
			@RequestParam("user_id")	Long userId,
			@RequestParam("card_type") Long cardType,
			@RequestParam("pay_type")  Short payType) {
//	    操作表 order_cards
//	    根据card_type 传递参数从表 dict_card_type 获取相应的金额

		AppResultData<Object> result = new AppResultData<Object>( Constants.SUCCESS_0, ConstantMsg.SUCCESS_0_MSG, "");		
		
		Users users = usersService.getUserById(userId);
		
		// 判断是否为注册用户，非注册用户返回 999
		if (users == null) {
			result.setStatus(Constants.ERROR_999);
			result.setMsg(ConstantMsg.USER_NOT_EXIST_MG);
			return result;
		}		
		
		DictCardType dictCardType = cardTypeService.selectByPrimaryKey(cardType);

		OrderCards record = orderCardsService.initOrderCards(users, cardType, dictCardType, payType);
		orderCardsService.insert(record);

		result.setData(record);
		
		
		/*
		 * 2016年4月15日17:06:44  新增短信
		 * 
		 * 您好，您的账户于{1}充值{2}。定制全年套餐，让美好生活不再有家务之忧
		 * 
		 */
		//充值时间
		String serviceTime = TimeStampUtil.timeStampToDateStr(TimeStampUtil.getNow(), "MM月-dd日HH:mm");
		
		//充值金额
		BigDecimal value = dictCardType.getCardValue();
		
		String[] paySuccessForUser = new String[] {serviceTime,value.toString()};
		
		SmsUtil.SendSms(users.getMobile(),  Constants.MESSAGE_CHARGE_PAY_SUCCESS, paySuccessForUser);
		
		return result;
	}

}
