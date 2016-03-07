package com.jhj.action.bs;



import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.github.pagehelper.PageInfo;
import com.jhj.action.BaseController;
import com.jhj.common.ConstantOa;
import com.jhj.oa.auth.AuthPassport;
import com.jhj.oa.vo.GiftsFormVo;
import com.jhj.po.model.bs.GiftCoupons;
import com.jhj.po.model.bs.Gifts;
import com.jhj.service.bs.DictCouponsService;
import com.jhj.service.bs.GiftCouponsService;
import com.jhj.service.bs.GiftsService;
import com.jhj.vo.bs.GiftsSearchVo;
import com.meijia.utils.StringUtil;
import com.meijia.utils.TimeStampUtil;



/**
 * @description：礼包 表操作
 * @author： kerryg
 * @date:2015年7月16日 
 */
@Controller
@RequestMapping(value = "/bs")
public class GiftsController extends BaseController {

	@Autowired
	private GiftsService  giftsService;
	
	@Autowired
	private DictCouponsService couponService;
	
	@Autowired
	private GiftCouponsService giftCouponsService;
	

	
	/**列表显示礼包信息
	 * @param request
	 * @param model
	 * @param searchVo
	 * @return
	 */
	@AuthPassport
	@RequestMapping(value = "/gifts-list", method = { RequestMethod.GET })
	public String convertCouponList(HttpServletRequest request, Model model,
			GiftsSearchVo searchVo) {
		model.addAttribute("requestUrl", request.getServletPath());
		model.addAttribute("requestQuery", request.getQueryString());
		
		model.addAttribute("searchModel", searchVo);
		int pageNo = ServletRequestUtils.getIntParameter(request,
				ConstantOa.PAGE_NO_NAME, ConstantOa.DEFAULT_PAGE_NO);
		int pageSize = ServletRequestUtils.getIntParameter(request,
				ConstantOa.PAGE_SIZE_NAME, ConstantOa.DEFAULT_PAGE_SIZE);
		
		PageInfo result = giftsService.searchVoListPage(searchVo, pageNo,
				pageSize);
		
		model.addAttribute("contentModel", result);
		
		return "coupons/giftsList";
	}
	/**
	 * to Gifts表单
	 * @param gifts
	 * @param model
	 * @param id
	 * @return
	 */
	@AuthPassport
	@RequestMapping(value = "/toGiftsForm", method = { RequestMethod.GET })
	public String toAddConvertCoupons(
			@ModelAttribute("gifts") Gifts gifts,
			 Model model,
			@RequestParam(value="giftId" ,required = false)Long giftId) {
		
		if (giftId == null) {
			giftId = 0L;
			gifts = giftsService.initGifts();
		}else {
			gifts = giftsService.selectByPrimaryKey(giftId);
			}
		GiftsFormVo giftsVo = new GiftsFormVo();
		try {
			BeanUtils.copyProperties(giftsVo,gifts);
		} catch (Exception e) {
			e.printStackTrace();
		}
		

		//获得礼包关联优惠券表
		List<GiftCoupons> giftCoupons = giftCouponsService.selectByGiftId(giftId);
		
		//保证至少有一个，默认为空的列表
		if (giftCoupons == null || giftCoupons.size() == 0) {
			GiftCoupons giftCouponsVo = giftCouponsService.initGiftCoupons();
			giftCoupons.add(giftCouponsVo);
		}

		giftsVo.setGiftCoupons(giftCoupons);;
		model.addAttribute("gifts", giftsVo);
		model.addAttribute("selectDataSource",couponService.getSelectRangMonthSource());
		model.addAttribute("selectDataSources",couponService.getSelectRechargeCouponSource());
		return "coupons/giftsForm";
	}
	
	
	@AuthPassport
	@RequestMapping(value = "/deleteByGiftId", method = { RequestMethod.GET })
	public String deleteByRechargeCouponId(
			@ModelAttribute("gifts") Gifts gifts,
			@RequestParam(value="giftId")Long giftId) {
		String path = "redirect:gifts-list";
		if(giftId!=null){
			int result = giftsService.deleteByPrimaryKey(giftId);
			if(result > 0){
				path = "redirect:gifts-list";
			}else{
				path = "error";
			}
		}
		return path;
	}
	/**
	 * 新增礼包
	 */
	@AuthPassport
	@RequestMapping(value = "/giftsForm", method = { RequestMethod.POST })
	public String addRechargeCoupons(HttpServletRequest request, Model model,
			@ModelAttribute("dictCoupons") GiftsFormVo giftsFormVo,
			BindingResult result) {
		
		
		model.addAttribute("requestUrl", request.getServletPath());
		model.addAttribute("requestQuery", request.getQueryString());
		
		Long giftId = Long.valueOf(request.getParameter("giftId"));
		Gifts gifts = giftsService.initGifts();
		//更新或者新增
		if (giftId != null && giftId > 0) {
			//更新礼包表
			gifts = giftsService.selectByPrimaryKey(giftId);
			gifts.setName(giftsFormVo.getName());
			gifts.setRangeMonth((short) 0);
			gifts.setUpdateTime(TimeStampUtil.getNow()/1000);
			giftsService.updateByPrimaryKeySelective(gifts);
			
		} else {
			
			 gifts = giftsService.initGifts();
			gifts.setName(giftsFormVo.getName());
			gifts.setRangeMonth((short) 0);
			gifts.setAddTime(TimeStampUtil.getNow()/1000);
			giftsService.insertSelective(gifts);
			
		}
		//更新礼包优惠券关联表	
		//1、更具giftId删除数据
		giftCouponsService.deleteByGiftId(giftId);
		String couponId[] = request.getParameterValues("selectCouponId");
		String num[] = request.getParameterValues("num");
		
		String couponIdItem = "";
		String numItem = "";
		
		for (int i = 0; i < couponId.length; i++) {
			GiftCoupons giftCoupons = giftCouponsService.initGiftCoupons();
			
			 couponIdItem = couponId[i];
			 numItem = num[i];
			 
			 if (StringUtil.isEmpty(couponIdItem) || StringUtil.isEmpty(numItem)) {
					continue;
				}

			 if (!StringUtil.isEmpty(couponId[i])) {
				 couponIdItem = couponId[i];
			}
			
			if (!StringUtil.isEmpty(num[i])) {
				numItem = num[i];
			}
			giftCoupons.setCouponId(Long.valueOf(couponIdItem));
			giftCoupons.setGiftId(gifts.getGiftId());
			giftCoupons.setNum(Short.valueOf(numItem));
			giftCouponsService.insertSelective(giftCoupons);
		}
		return "redirect:gifts-list";
	}
	

}
