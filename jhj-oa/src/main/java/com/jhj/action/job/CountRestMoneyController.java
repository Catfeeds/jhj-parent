package com.jhj.action.job;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jhj.po.model.user.Users;
import com.jhj.service.users.CountUserRestService;
import com.jhj.service.users.UsersService;
import com.meijia.utils.DateUtil;

@Controller
@RequestMapping("/job")
public class CountRestMoneyController {
	
	@Autowired
	private UsersService userService;
	
	@Autowired
	private CountUserRestService countUserRestService;
	
	@RequestMapping("/saveCountRestMoney")
	public String saveCountRestMoney() {
		
		List<Users> listUserRestMoneyGtZero = userService.listUserRestMoneyGtZero();
		if(listUserRestMoneyGtZero.isEmpty()) return null;
		
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		
		for(int i=0;i<listUserRestMoneyGtZero.size();i++){
			Map<String,Object> userMap = new HashMap<String,Object>();
			Users users = listUserRestMoneyGtZero.get(i);
			userMap.put("userId", users.getId());
			userMap.put("restMoney", users.getRestMoney());
			userMap.put("addTime",DateUtil.getNowOfDate());
			list.add(userMap);
		}
		countUserRestService.insertList(list);
		return null;
	}
}
