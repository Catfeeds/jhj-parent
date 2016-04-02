package com.jhj.action.app.newService;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.jhj.action.app.BaseController;
import com.jhj.common.ConstantMsg;
import com.jhj.common.Constants;
import com.jhj.po.model.university.PartnerServiceType;
import com.jhj.service.university.PartnerServiceTypeService;
import com.jhj.vo.PartnerServiceTypeVo;
import com.meijia.utils.BeanUtilsExp;
import com.meijia.utils.vo.AppResultData;

/**
 *
 * @author :hulj
 * @Date : 2016年3月16日下午3:09:18
 * @Description: 
 *	
 *		jhj2.1 新版 首页 加载 服务类型
 */	
@Controller
@RequestMapping(value = "/app/newPartServiceType")
public class AppServiceTypeController extends BaseController {
	
	@Autowired
	private PartnerServiceTypeService partService;
	
	/**
	 *  @Title: getServiceTypeList
	  * @Description: 
	  * 	从首页点击进入 二级服务类型页面
	  * 
	  * 	得到 金牌保洁 初体验 等 具体 服务的页面
	  * 
	  *   根据 服务id,得到 子服务
	  *   
	 */	
	@RequestMapping(value = "second_service_type.json",method = RequestMethod.GET)
	public AppResultData<Object> getServiceTypeList(
			@RequestParam("service_type_id") Long serviceTypeId){
		
		AppResultData<Object> result = new AppResultData<Object>(Constants.SUCCESS_0, ConstantMsg.SUCCESS_0_MSG, "");
		
		List<PartnerServiceType> list = partService.selectByParentId(serviceTypeId);
		
		List<PartnerServiceTypeVo> volist = new ArrayList<PartnerServiceTypeVo>();
		
		for(PartnerServiceType item : list) {
			PartnerServiceTypeVo vo = new PartnerServiceTypeVo();
			BeanUtilsExp.copyPropertiesIgnoreNull(item, vo);
			
			String priceAndUnit = "";
			
			if (vo.getServiceProperty().equals((short)0)) {
				priceAndUnit =  vo.getPrice() + vo.getUnit();
			} else {
				Integer weekNum = 0;
				Double serviceTimes = vo.getServiceTimes();
				if (serviceTimes < 1) {
					weekNum = (int) (1/serviceTimes);
					priceAndUnit = "每" + weekNum + "周1次";
				} else {
					weekNum = serviceTimes.intValue();
					priceAndUnit = "每周"+ weekNum + "次";
				}
			}
			
			vo.setPriceAndUnit(priceAndUnit);
			
			
			if (vo.getEnable().equals((short)1)) {
				vo.setButtonWord("立即预定");
			} else {
				vo.setButtonWord("敬请期待");
			}
			
			volist.add(vo);
		}
		
		result.setData(volist);
		
		return result;
	}
	
	
	/*
	 *  “半自动”，依靠客服完成订单 的 服务
	 *  
	 *  根据id 得到下单页面所需的 服务信息
	 */
	@RequestMapping(value = "service_type_detail.json",method = RequestMethod.GET)
	public AppResultData<Object> getServiceTypeDetail(
			@RequestParam("service_type_id") Long serviceTypeId){
		
		AppResultData<Object> result = new AppResultData<Object>(Constants.SUCCESS_0, ConstantMsg.SUCCESS_0_MSG, "");
		
		PartnerServiceType serviceType = partService.selectByPrimaryKey(serviceTypeId);
		
		result.setData(serviceType);
		
		return result;
	}
	
}
