package com.jhj.action.job;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.jhj.action.app.BaseController;
import com.jhj.common.ConstantMsg;
import com.jhj.common.Constants;
import com.jhj.po.dao.dict.DictCardTypeMapper;
import com.jhj.po.model.order.OrderCards;
import com.jhj.po.model.order.Orders;
import com.jhj.po.model.user.UserDetailPay;
import com.jhj.po.model.user.Users;
import com.jhj.service.order.OrderCardsService;
import com.jhj.service.order.OrderQueryService;
import com.jhj.service.users.UserDetailPayService;
import com.jhj.service.users.UsersService;
import com.jhj.vo.order.OrderSearchVo;
import com.jhj.vo.user.UserDetailSearchVo;
import com.jhj.vo.user.UserSearchVo;
import com.meijia.utils.vo.AppResultData;

@Controller
@RequestMapping("/app/job/cleanup")
public class CleanupUserDetailPayController extends BaseController{

	@Autowired
	private UsersService userService;
	
	@Autowired
	private UserDetailPayService userDetailPayService;
	
	@Autowired
	private DictCardTypeMapper dictCardTypeMapper;
	
	@Autowired
	private OrderCardsService orderCardService;
	
	@Autowired
	private OrderQueryService orderQueryService;
	
	@RequestMapping(value="/detail_pay",method=RequestMethod.GET)
	public AppResultData<String> cleanupUserDetailPay(){
		
		AppResultData<String> result =new AppResultData<String>(Constants.SUCCESS_0, ConstantMsg.SUCCESS_0_MSG, new String());
		UserSearchVo userVo =new UserSearchVo();
		userVo.setIsVip((short)1);
		List<Users> userList = userService.selectBySearchVo(userVo);
		UserDetailSearchVo userdetailPayVo =new UserDetailSearchVo();
		if(userList.size()>0){
			for(int i =0 ; i <userList.size();i++){
				Users users = userList.get(i);
				Long userId = users.getId();
				BigDecimal rest = users.getRestMoney();
				userdetailPayVo.setUserId(userId);
				userdetailPayVo.setOrderByParam("order by add_time asc");
				
				List<UserDetailPay> userDetailPayList = userDetailPayService.selectBySearchVo(userdetailPayVo);
				BigDecimal restMoney = new BigDecimal(0);
				if(!userDetailPayList.isEmpty()){
					for(int j=0;j<userDetailPayList.size();j++){
						UserDetailPay userDetailPay = userDetailPayList.get(j);
						Short orderType = userDetailPay.getOrderType();
						Short payType = userDetailPay.getPayType();
						BigDecimal orderPay = userDetailPay.getOrderPay();
						
						if(orderType ==0 && payType==0){
							OrderSearchVo orderVo =new OrderSearchVo();
							orderVo.setOrderNo(userDetailPay.getOrderNo());
							Orders orders = orderQueryService.selectBySearchVo(orderVo).get(0);
							if(orders.getOrderStatus()>0){
								if(restMoney.compareTo(orderPay)>=0)
									restMoney = restMoney.subtract(orderPay);
								else
									restMoney = new BigDecimal(0);
							}
						}
						if(orderType==1){
							restMoney = restMoney.add(orderPay);
							if(userDetailPay.getAddTime()>1470758400 && userDetailPay.getAddTime()<1476979200){
								OrderCards orderCard = orderCardService.selectByOrderCardsNo(userDetailPay.getOrderNo());
								if(orderCard.getOrderStatus()==1){
									if(orderCard.getCardPay().compareTo(new BigDecimal(500))==0){
										restMoney = restMoney.add(new BigDecimal(50));
									}
									if(orderCard.getCardPay().compareTo(new BigDecimal(1000))==0){
										restMoney = restMoney.add(new BigDecimal(100));
									}
									if(orderCard.getCardPay().compareTo(new BigDecimal(2000))==0){
										restMoney = restMoney.add(new BigDecimal(300));
									}
									if(orderCard.getCardPay().compareTo(new BigDecimal(5000))==0){
										restMoney = restMoney.add(new BigDecimal(1000));
									}
								}
							}
						}
						if(orderType==3 && payType ==0){
							restMoney = restMoney.subtract(orderPay);
						}
//						if(orderType==5 && payType==0){
//							restMoney = restMoney.add(orderPay);
//						}
						userDetailPay.setRestMoney(restMoney);
						userDetailPayService.updateByPrimaryKeySelective(userDetailPay);
					}
				}
			}
		}
		
		return result;
	}
}
