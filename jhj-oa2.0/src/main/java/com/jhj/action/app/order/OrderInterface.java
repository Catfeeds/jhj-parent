package com.jhj.action.app.order;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.jhj.action.BaseController;
import com.jhj.service.order.OrderStatService;

@Controller
@RequestMapping(value = "/interface-order")
public class OrderInterface extends BaseController {

	@Autowired
	private OrderStatService orderStatService;

	/**
	 * 根据月份显示该月派工个数
	 * @return
	 * @throws IOException
	 */
	@SuppressWarnings("rawtypes")//传递参数时也要传递带泛型的参数--压制这种警告
	@RequestMapping(value = "get-dispatch-by-month.json", method = RequestMethod.GET)
    public List<Map<String,Object>>  getDispatch(HttpServletResponse response,
    		@RequestParam(value = "org_staff_id", required = true, defaultValue = "0" ) Long  orgStaffId,
    		@RequestParam(value = "start", required = true, defaultValue = "" ) String start,
    		@RequestParam(value="end" ,required = true, defaultValue = "") String end) {

		return orderStatService.selectOrdersCountByYearAndMonth(orgStaffId,start, end);
    }
}
