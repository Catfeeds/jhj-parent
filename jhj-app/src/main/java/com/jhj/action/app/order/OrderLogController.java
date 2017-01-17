package com.jhj.action.app.order;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.jhj.action.app.BaseController;
import com.jhj.common.ConstantMsg;
import com.jhj.common.Constants;
import com.jhj.po.model.order.OrderLog;
import com.jhj.service.order.OrderLogService;
import com.jhj.vo.order.OrderLogVo;
import com.meijia.utils.vo.AppResultData;

@Controller
@RequestMapping("/app/orderLog")
public class OrderLogController extends BaseController{
	
	@Autowired
	private OrderLogService orderLogService;
	
	@RequestMapping(value="/orderLog-list",method=RequestMethod.GET)
	public AppResultData<Object> getOrderLogList(@RequestParam(value="order_no",required=true) String orderNo){
		
		AppResultData<Object> result = new AppResultData<Object>(Constants.SUCCESS_0, ConstantMsg.SUCCESS_0_MSG, "");
		
		List<OrderLog> orderLogList = orderLogService.selectByOrderNo(orderNo);
		
		if(orderLogList.isEmpty())
			result = new AppResultData<Object>(Constants.ERROR_999, ConstantMsg.ERROR_999_MSG_10, "");
		else{
			for(int i=0,len=orderLogList.size();i<len;i++){
				OrderLogVo transVo = orderLogService.transVo(orderLogList.get(i));
				orderLogList.set(i, transVo);
			}
		}
			result.setData(orderLogList);
		return result;
	}
	
}
