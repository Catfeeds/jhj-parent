package com.jhj.action.period;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.jhj.action.BaseController;
import com.jhj.po.model.period.PeriodOrderAddons;
import com.jhj.service.period.PeriodOrderAddonsService;

@Controller
@RequestMapping("/period")
public class PeriodOrderAddonsController extends BaseController{
	
	@Autowired
	private PeriodOrderAddonsService periodOrderAddonsService;

	
	@RequestMapping(value = "/delete/{id}",method = RequestMethod.GET)
	public String deleteByPrimaryKey(@PathVariable("id") Integer id) {
		periodOrderAddonsService.deleteByPrimaryKey(id);
		return "";
	}

	@RequestMapping(value = "/insert",method = RequestMethod.GET)
	public String insert(@ModelAttribute PeriodOrderAddons record) {
		periodOrderAddonsService.insert(record);
		return "";
	}

	@RequestMapping(value = "/get",method = RequestMethod.GET)
	public PeriodOrderAddons selectByPrimaryKey(Model model,Integer id) {
		
		PeriodOrderAddons periodOrderAddons = periodOrderAddonsService.selectByPrimaryKey(id);
		
		model.addAttribute("periodOrderAddons", periodOrderAddons);
		return null;
	}
	
	@RequestMapping(value = "/update",method = RequestMethod.GET)
	public String updateByPrimaryKey(PeriodOrderAddons record) {
		periodOrderAddonsService.updateByPrimaryKey(record);
		return "";
	}
   
}