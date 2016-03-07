package com.jhj.action.app.user;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import com.jhj.action.app.BaseController;

/**
 *
 * @author :hulj
 * @Date : 2015年8月10日上午10:10:44
 * @Description: 
 *		
 *		用户版--我的--基本信息
 */
@Controller
@RequestMapping(value = "/app/user")
public class UserMessageController extends BaseController {
//	
//	@Autowired
//	private UsersService userService;
//	@Autowired
//	private OrderHourPayService orderHourPayService;
//	
//	
//	@RequestMapping(value = "get_user_message",method = RequestMethod.GET)
//	public AppResultData<Object> getUserMessage(@RequestParam("userId") Long userId){
//		
//		AppResultData<Object> result = new AppResultData<Object>(Constants.SUCCESS_0, ConstantMsg.SUCCESS_0_MSG, "");
//		
//		Users user = userService.selectByUsersId(userId);
//		
//		UserMessageVo messageVo = new UserMessageVo();
//		
//		BeanUtilsExp.copyPropertiesIgnoreNull(user, messageVo);
//		
//		//得到用户可用 优惠券列表
//		List<DictCoupons> couponList = orderHourPayService.getCouponsByUserId(userId);
//		
//		messageVo.setCouponList(couponList);
//		messageVo.setCouponAmount(couponList.size());
//		
//		result.setData(messageVo);
//		
//		return result;
//	}
//	
}
