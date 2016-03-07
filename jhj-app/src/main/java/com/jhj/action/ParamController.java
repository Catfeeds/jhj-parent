package com.jhj.action;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/param")
public class ParamController {

	@RequestMapping(value="/test")
	public ModelAndView test(){


		ModelAndView mv = new ModelAndView();
		mv.setViewName("success");

		return mv;
	}



}
