package com.jhj.action.app.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.jhj.action.app.BaseController;
import com.jhj.common.ConstantMsg;
import com.jhj.common.Constants;
import com.meijia.utils.vo.AppResultData;
import com.jhj.po.model.bs.OrgStaffs;
import com.jhj.po.model.dict.DictCardType;
import com.jhj.po.model.order.OrderCards;
import com.jhj.po.model.user.Users;
import com.jhj.service.bs.OrgStaffsService;
import com.jhj.service.dict.CardTypeService;
import com.jhj.service.order.OrderCardsService;
import com.jhj.service.users.UsersService;
import com.jhj.vo.staff.StaffAuthSearchVo;
import com.jhj.vo.staff.StaffSearchVo;

@Controller
@RequestMapping(value="/app/user")
public class UserCardController extends BaseController {
	
	@Autowired
	private UsersService usersService;
	
	@Autowired
	private OrderCardsService orderCardsService;
	
	@Autowired
	private CardTypeService cardTypeService;
	
	@Autowired
	private OrgStaffsService orgStaffService;
	

	// 8. 会员充值接口
	/**
	 * userId true long 用户Id
	 * card_type true int 充值卡类型
	 * pay_type true int 支付类型 0 =
	 * 余额支付 1 = 支付宝 2 = 微信支付 3 = 智慧支付 4 = 上门刷卡（保留，站位）
	 * staffCode员工推荐码
	 * 
	 */
	@RequestMapping(value = "card_buy", method = RequestMethod.POST)
	public AppResultData<Object> cardBuy(
			@RequestParam("user_id")	Long userId,
			@RequestParam("card_type") int cardType,
			@RequestParam("pay_type")  Short payType,
			@RequestParam( value="staff_code",required=false)  String staffCode) {
//	    操作表 order_cards
//	    根据card_type 传递参数从表 dict_card_type 获取相应的金额
		AppResultData<Object> result = new AppResultData<Object>( Constants.SUCCESS_0, ConstantMsg.SUCCESS_0_MSG, "");		
		
		Users users = usersService.selectByPrimaryKey(userId);
		
		// 判断是否为注册用户，非注册用户返回 999
		if (users == null) {
			result.setStatus(Constants.ERROR_999);
			result.setMsg(ConstantMsg.USER_NOT_EXIST_MG);
			return result;
		}		
		long cardId=cardType;
		DictCardType dictCardType = cardTypeService.selectByPrimaryKey(cardId);

		OrderCards record = orderCardsService.initOrderCards(users, cardId, dictCardType, payType);
		if(staffCode!=null && !staffCode.equals("")){
			StaffSearchVo vo=new StaffSearchVo();
			vo.setStaffCode(staffCode);
			OrgStaffs staff = orgStaffService.selectBySearchVo(vo).get(0);
			record.setParentId(staff.getParentOrgId());
			record.setOrgId(staff.getOrgId());
		}
		record.setReferee(staffCode);
		orderCardsService.insert(record);

		result.setData(record);
		
		return result;
	}

}
