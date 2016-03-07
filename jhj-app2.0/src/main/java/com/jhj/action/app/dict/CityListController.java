package com.jhj.action.app.dict;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.jhj.action.app.BaseController;
import com.jhj.common.ConstantMsg;
import com.jhj.common.Constants;
import com.jhj.po.model.dict.DictCity;
import com.jhj.service.dict.CityService;
import com.meijia.utils.vo.AppResultData;

@Controller
@RequestMapping(value = "/app/city")
public class CityListController extends BaseController {
	
	@Autowired
	private CityService cityService;
	/**
	 * 查出所有城市的信息
	 * @return
	 */
	@RequestMapping(value = "get_list", method = RequestMethod.GET)
	public AppResultData<Object> cityList() {
	 	
		AppResultData<Object> result = new AppResultData<Object>(Constants.SUCCESS_0,
				ConstantMsg.SUCCESS_0_MSG, new String());
		List<DictCity> cityList  =  cityService.selectAll(); 
	
		if(cityList !=null){
			result.setData(cityList);
		}else {     
			 result = new AppResultData<Object>(Constants.ERROR_999,"城市信息查询失败", new String());
		}
		return result;
	}
}
