package com.jhj.action.job;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.jhj.action.app.BaseController;
import com.jhj.common.ConstantMsg;
import com.jhj.common.Constants;
import com.jhj.service.job.CleanupUserService;
import com.meijia.utils.vo.AppResultData;

@Controller
@RequestMapping(value = "/app/job/cleanup")
public class CleanupUserController extends BaseController {

	@Autowired
	private CleanupUserService cleanupUserService;
	
	/**
	 * 重建用户的充值记录及消费明细，生成余额
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "user-order-card", method = RequestMethod.GET)
	public AppResultData<Object> cleanupUserOrderCard(HttpServletRequest request) {

		AppResultData<Object> result = new AppResultData<Object>(
				Constants.SUCCESS_0, ConstantMsg.SUCCESS_0_MSG, new String());
		
		String reqHost = request.getRemoteHost();// 如果用的 IPV6 得到的 将是 0:0.。。。。1

		// String reqHost = request.getRemoteAddr();
		// 限定只有localhost能访问
		if (reqHost.equals("localhost") || reqHost.equals("127.0.0.1")) {
			cleanupUserService.reBuildOrderCards();
		}
		return result;
	}

}
