package com.jhj.action.app.dict;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.jhj.action.BaseController;
import com.jhj.common.ConstantMsg;
import com.jhj.common.Constants;
import com.jhj.po.model.bs.Gifts;
import com.jhj.po.model.bs.OrgStaffs;
import com.jhj.po.model.dict.DictCity;
import com.jhj.po.model.dict.DictCardType;
import com.jhj.po.model.dict.DictRegion;
import com.jhj.service.bs.GiftsService;
import com.jhj.service.bs.OrgStaffsService;
import com.jhj.service.dict.AdService;
import com.jhj.service.dict.DictService;
import com.jhj.service.dict.ServiceTypeService;
import com.meijia.utils.vo.AppResultData;

@Controller
@RequestMapping(value = "/interface-dict")
public class DictInterface extends BaseController {

	@Autowired
	private DictService dictService;

	@Autowired
	private ServiceTypeService serviceTypeService;

    @Autowired
    private AdService adService;
    
    @Autowired
    private GiftsService giftsService;

    @Autowired
    private OrgStaffsService staffService;
    
	/**
	 * 列表显示城市
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "get-region-by-cityid", method = RequestMethod.GET)
    public AppResultData<List> getRegions(
    		@RequestParam(value = "cityId", required = true, defaultValue = "0") Long cityId) {

		List<DictRegion> listRegion =  dictService.getRegionByCityId(cityId);

		if (cityId > 0) {
			listRegion = dictService.getRegionByCityId(cityId);
		}

		AppResultData<List> result = new AppResultData<List>(
				Constants.SUCCESS_0, ConstantMsg.SUCCESS_0_MSG, listRegion);

    	return result;
    }
	/**
	 * 根据省份查询城市
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "get-city-by-provinceId", method = RequestMethod.GET)
	public  AppResultData<Object> getCitys(
			@RequestParam(value = "provinceId", required = true, defaultValue = "0") Long provinceId) {
		
		List<DictCity> listCity =  dictService.getCityByProvinceId(provinceId);
		
		if (provinceId > 0) {
			listCity = dictService.getCityByProvinceId(provinceId);
		}
		
		AppResultData<Object> result = new AppResultData<Object>(
				Constants.SUCCESS_0, ConstantMsg.SUCCESS_0_MSG, listCity);
		
//		DictCity cityVo = null;
//		HashMap result = new HashMap();
//		result.put("0", "全部");
//		for (int i = 0; i < listCity.size(); i++) {
//			cityVo = listCity.get(i);
//			result.put(cityVo.getCityId().toString(), cityVo.getName());
//		}
		return result;
	}

	/**
	 * 检查礼包名称是否重复
	 * @param request
	 * @param model
	 * @param
	 * @return
	 */
	// @AuthPassport Map<String, Object>
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "check-giftName-dumplicate", method = RequestMethod.POST)
	public  AppResultData<Boolean> checkName(
			@RequestParam(value = "giftName", required = true, defaultValue = "") String giftName,
			@RequestParam(value = "giftId", required = true, defaultValue = "0") Long giftId

			) {

		AppResultData<Boolean> result = new AppResultData<Boolean>(
		Constants.SUCCESS_0, " ", false);
		Gifts record = giftsService.selectByNameAndOtherId(giftName,giftId);
		if(record != null && record.getGiftId() > 0){
			result.setMsg("礼包名称已经存在");
			result.setData(true);
		}else{
			result.setData(false);
		}
		return result;
	}
	/**
	 * 检查充值可类型名称是否重复
	 * @param name
	 * @param dictCardType
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "check-cardName-dumplicate", method = RequestMethod.POST)
	public  AppResultData<Boolean> checkCardName(
			@RequestParam(value = "name", required = true, defaultValue = "") String name,
			@RequestParam(value = "dictCardType", required = true, defaultValue = "0") Long dictCardType

			) {

		AppResultData<Boolean> result = new AppResultData<Boolean>(
		Constants.SUCCESS_0, ConstantMsg.SUCCESS_0_MSG, false);
		DictCardType record = serviceTypeService.selectByCardNameAndOtherId(name, dictCardType);

		if(record != null && record.getId() > 0){
			result.setMsg("名称已经存在");
			result.setData(true);
		}else{
			result.setData(false);
		}
		return result;
	}
	
	
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "select-staff-by-cloudOrg.json", method = RequestMethod.POST)
	public  AppResultData<Object> selectCloudStaffList(
			@RequestParam(value = "orgId", required = true, defaultValue = "0") Long cloudOrgId) {

		AppResultData<Object> result = new AppResultData<Object>(
		Constants.SUCCESS_0, ConstantMsg.SUCCESS_0_MSG, false);

		List<OrgStaffs> list = staffService.selectByOrgId(cloudOrgId);
		
		result.setData(list);
		
		return result;
	}
	
	
}
