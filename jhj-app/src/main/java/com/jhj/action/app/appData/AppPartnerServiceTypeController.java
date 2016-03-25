package com.jhj.action.app.appData;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.jhj.action.app.BaseController;
import com.jhj.common.ConstantMsg;
import com.jhj.common.Constants;
import com.jhj.service.university.PartnerServiceTypeService;
import com.jhj.vo.bs.NewPartnerServiceVo;
import com.meijia.utils.vo.AppResultData;

/**
 *
 * @author :hulj
 * @Date : 2016年3月14日下午4:57:06
 * @Description: 
 *		
 *		微网站  服务类别数据
 */
@Controller
@RequestMapping(value = "/app/partService")
public class AppPartnerServiceTypeController extends BaseController {
	
	@Autowired
	private PartnerServiceTypeService partService;
	
	@RequestMapping(value = "part_service_tree_list.json",method = RequestMethod.GET)
	public AppResultData<Object> getServiceTreeList(){
		
		AppResultData<Object> result = 
				new AppResultData<Object>(Constants.SUCCESS_0, ConstantMsg.SUCCESS_0_MSG, "");
		
		List<NewPartnerServiceVo> list = partService.getTreeList();
		
		result.setData(list);
		
		return result;
	}
	
	
	
}
