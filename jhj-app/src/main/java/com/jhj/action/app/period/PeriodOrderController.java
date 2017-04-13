package com.jhj.action.app.period;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jhj.action.app.BaseController;
import com.jhj.service.period.PeriodOrderService;

@Controller
@RequestMapping("/period")
public class PeriodOrderController extends BaseController{
	
	@Autowired
	private PeriodOrderService periodOrderService;


}