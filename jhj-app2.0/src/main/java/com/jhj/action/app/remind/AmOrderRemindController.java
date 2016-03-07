package com.jhj.action.app.remind;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.jhj.action.app.BaseController;
import com.jhj.common.ConstantMsg;
import com.jhj.common.Constants;
import com.jhj.service.remind.OrderRemindService;
import com.jhj.vo.remind.OrderRemindVo;
import com.meijia.utils.vo.AppResultData;

/**
 *
 * @author :hulj
 * @Date : 2015年9月24日上午9:49:20
 * @Description: 
 *		
 *		提醒类 业务 助理端
 *
 */
@Controller
@RequestMapping(value = "/app/remind")
public class AmOrderRemindController extends BaseController {

	@Autowired
	private OrderRemindService orderRemindService;
	
	/*
	 * 助理端 查看  提醒 列表
	 */
	@RequestMapping(value = "/am_remind_list", method = RequestMethod.GET)
	public AppResultData<Object> amRemindList(
			@RequestParam("am_id") Long amId,
			@RequestParam(value = "page", required = false, defaultValue = "1") int page){
		
		AppResultData<Object> result = new AppResultData<Object>(Constants.SUCCESS_0, ConstantMsg.SUCCESS_0_MSG, "");
		
		List<OrderRemindVo> voList = orderRemindService.selectNowRemindAm(amId, page, Constants.PAGE_MAX_NUMBER);
		
		result.setData(voList);
		
		return result;
	}
	
	/*
	 * 助理端 查看  提醒  详情
	 */
	@RequestMapping(value = "am_remind_detail",method = RequestMethod.GET)
	public AppResultData<Object> amRemindDetail(
			@RequestParam("order_no") String orderNo){
		AppResultData<Object> result = new AppResultData<Object>(Constants.SUCCESS_0, ConstantMsg.SUCCESS_0_MSG, "");
		
		OrderRemindVo remindVo = orderRemindService.getRemindDetail(orderNo);
		
		result.setData(remindVo);
		return result;
	}
	
}
