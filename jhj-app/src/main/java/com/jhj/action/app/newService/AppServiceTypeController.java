package com.jhj.action.app.newService;

import java.math.BigDecimal;
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
import com.jhj.vo.AppAmPartnerVo;
import com.jhj.vo.PartnerServiceTypeVo;
import com.meijia.utils.BeanUtilsExp;
import com.meijia.utils.MathBigDecimalUtil;
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
		
		PartnerServiceTypeVo serviceTypeVo=new PartnerServiceTypeVo();
		serviceTypeVo.setParentId(serviceTypeId);
		serviceTypeVo.setEnable((short)1);
		List<PartnerServiceType> list = partService.selectByPartnerServiceTypeVo(serviceTypeVo);
//		List<PartnerServiceType> list = partService.selectByParentId(serviceTypeId);
		
		List<PartnerServiceTypeVo> volist = new ArrayList<PartnerServiceTypeVo>();
		
		/*
		 * 2016年4月13日17:01:41  助理 二级下单页 vo, 新增 头部 图片地址 
		 */
		AppAmPartnerVo partnerVo = new AppAmPartnerVo();
		
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
				vo.setYearTimes(String.valueOf(serviceTimes * 52));
			}
			
			vo.setPriceAndUnit(priceAndUnit);
			
			if (vo.getEnable().equals((short)1)) {
				vo.setButtonWord("立即预定");
			} else {
				vo.setButtonWord("敬请期待");
			}
			
			
			volist.add(vo);
		}
		
		
		/*
		 *  2016年4月13日16:22:09
		 *  助理类订单下单页面。之前是 没有 头部 图片。
		 *  而且公用了 一个二级页面，此时需要 新增 vo字段传递 该 头部图片的url
		 */
		
		if(serviceTypeId == 25){
			//如果是 贴心家事 二级页面
			partnerVo.setTopImgUrl("img/icon-fiveservice/tiexinjiashi-2.jpg");
		}
		
		if(serviceTypeId == 26){
			//如果是 深度养护 二级页面
			partnerVo.setTopImgUrl("img/icon-fiveservice/shenduyanghu-2.jpg");
		}
		
		if(serviceTypeId == 27){
			//如果是 企业服务 二级页面
			partnerVo.setTopImgUrl("img/icon-fiveservice/qiyefuwu-2.jpg");
		}
		
		partnerVo.setList(volist);
		result.setData(partnerVo);
		
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
		PartnerServiceTypeVo vo = new PartnerServiceTypeVo();
		BeanUtilsExp.copyPropertiesIgnoreNull(serviceType, vo);
		
		BigDecimal price = vo.getPrice();
		BigDecimal monthPrice = MathBigDecimalUtil.mul(price, new BigDecimal(0.95));
		monthPrice = MathBigDecimalUtil.div(monthPrice, new BigDecimal(12));
		
		
		BigDecimal yearPrice = MathBigDecimalUtil.mul(price, new BigDecimal(0.85));
		
		String priceStr = "原价:"+MathBigDecimalUtil.round2(price)+"元";
		String monthPriceStr = "月付:"+MathBigDecimalUtil.round2(monthPrice)+"元(享95折)";
		String yearPriceStr = "年付:"+MathBigDecimalUtil.round2(yearPrice)+"元(享85折)";
		vo.setPriceStr(priceStr);
		vo.setMonthPrice(monthPriceStr);
		vo.setYearPrice(yearPriceStr);
		
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
			Integer t = (int) (serviceTimes * 52);
			vo.setYearTimes(String.valueOf(t));
		}
		
		vo.setPriceAndUnit(priceAndUnit);
		
		
		result.setData(vo);
		
		return result;
	}
	
}
