package com.jhj.action.app.dict;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.jhj.common.ConstantMsg;
import com.jhj.common.Constants;
import com.jhj.po.model.bs.OrgStaffs;
import com.jhj.po.model.dict.DictAd;
import com.jhj.po.model.dict.DictCardType;
import com.jhj.po.model.dict.DictServiceAddons;
import com.jhj.po.model.dict.DictServiceTypes;
import com.jhj.po.model.socials.SocialCall;
import com.jhj.po.model.socials.Socials;
import com.jhj.po.model.user.UserRefAm;
import com.jhj.po.model.user.Users;
import com.jhj.service.bs.OrgStaffsService;
import com.jhj.service.dict.AdService;
import com.jhj.service.dict.CardTypeService;
import com.jhj.service.dict.ServiceAddonsService;
import com.jhj.service.dict.ServiceTypeService;
import com.jhj.service.socials.SocialsCallService;
import com.jhj.service.socials.SocialsService;
import com.jhj.service.users.UserRefAmService;
import com.jhj.service.users.UsersService;
import com.jhj.vo.dict.CardTypeVO;
import com.jhj.vo.dict.DictCardTypeVo;
import com.jhj.vo.socials.SocialsVo;
import com.meijia.utils.BeanUtilsExp;
import com.meijia.utils.DateUtil;
import com.meijia.utils.SmsUtil;
import com.meijia.utils.TimeStampUtil;
import com.meijia.utils.vo.AppResultData;

@Controller
@RequestMapping(value="/app/dict")
public class CardTypeController<T> {

	@Autowired
	private CardTypeService cardTypeService;
	@Autowired
	private ServiceTypeService serviceTypeService;
	
	@Autowired
	private ServiceAddonsService serviceAddonsService;
	
	@Autowired
	private AdService adService;
	
	@Autowired
	private SocialsService socialsService;
	
	@Autowired
	private UserRefAmService userRefAmService;
	
	@Autowired
	private OrgStaffsService orgStaffsService;
	
	@Autowired
	private SocialsCallService socialsCallService;
	
	@Autowired
	private UsersService usersService;
	
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "get_cards", method = RequestMethod.GET)
    public AppResultData<List> getCardTypes (
    	@RequestParam(value="user_id", required=false,defaultValue="0") Long userId) {

    	//获得广告配置定义列表项
    	List<DictCardType> list = cardTypeService.getCardTypes();
//    	List<DictCardTypeVo> clist = cardTypeService.changeToDictCardTypeVo(list,userId);
    	List<CardTypeVO> clist = cardTypeService.cardSendMoney(list);
    	AppResultData<List> result = null;
    	result = new AppResultData<List>(0, "ok", clist);

    	return result;
    }
	
	
	@RequestMapping(value = "get_card_detail", method = RequestMethod.GET)
    public AppResultData<Object> getCardType (
    		@RequestParam("card_id") Long cardId) {

    	//获得广告配置定义列表项
		DictCardType item = cardTypeService.selectByPrimaryKey(cardId);
		AppResultData<Object> result = null;
    	result = new AppResultData<Object>(0, "ok", item);

    	return result;
    }	
	
	@RequestMapping(value = "get_ads", method = RequestMethod.GET)
	public AppResultData<List<DictAd>> dictList(
		
			@RequestParam(value = "ad_type", required = false, defaultValue = "0") Short adType) {

		List<DictAd> dictAdList = new ArrayList<DictAd>();
		
		AppResultData<List<DictAd>> result = new AppResultData<List<DictAd>>(Constants.SUCCESS_0,
				ConstantMsg.SUCCESS_0_MSG,dictAdList);	
		if (dictAdList != null) {
			
			dictAdList = adService.selectByAdType(adType);
			
	        result.setData(dictAdList);
		}
	    		
		return result;
}
	@RequestMapping(value = "get_serviceType",method = RequestMethod.GET)
	public AppResultData<Object> serviceTypeList(){
		
		List<DictServiceTypes> list = serviceTypeService.getServiceTypes();
		
		AppResultData<Object> result = null;
    	result = new AppResultData<Object>(0, "ok", list);

    	return result;
	}
	@RequestMapping(value = "get_service_addons_type",method = RequestMethod.GET)
	public AppResultData<List> serviceAddonsTypeList(){
		
		List<DictServiceAddons> list = serviceAddonsService.getServiceAddonsTypes();
		AppResultData<List> result = null;
		result = new AppResultData<List>(0, "ok", list);
		
		return result;
	}
	@RequestMapping(value = "get_socilas", method = RequestMethod.GET)  
    public AppResultData<Object> getSocials () {

    	//获得广告配置定义列表项
    	List<Socials> list = socialsService.getSocialsList();
    	List<SocialsVo> listVo = new ArrayList<SocialsVo>();
    	for (Iterator<Socials> iterator = list.iterator(); iterator.hasNext();) {
			Socials socials = (Socials) iterator.next();
			SocialsVo socialsVo = new SocialsVo();
			BeanUtilsExp.copyPropertiesIgnoreNull(socials,socialsVo);
			socialsVo.setBeginDateStr(DateUtil.formatDate(socials.getBeginDate()));
			socialsVo.setEndDateStr(DateUtil.formatDate(socials.getEndDate()));
			listVo.add(socialsVo);
		}
    	AppResultData<Object> result = null;
    	result = new AppResultData<Object>(0, "ok", listVo);
    	
    	return result;
    }
	
	@RequestMapping(value = "get_socials_detail", method = RequestMethod.GET)
    public AppResultData<Object> getsocialsDetail (
    		@RequestParam("id") Long id,
    		@RequestParam("user_id")Long userId) {

		Socials socials = socialsService.selectByPrimaryKey(id);
		SocialsVo socialsVo = socialsService.initSocialsVo();
		BeanUtilsExp.copyPropertiesIgnoreNull(socials, socialsVo);
		socialsVo.setBeginDateStr(DateUtil.formatDate(socials.getBeginDate()));
		socialsVo.setEndDateStr(DateUtil.formatDate(socials.getEndDate()));
		UserRefAm userRefAm = userRefAmService.selectByAmId(userId);
		if(userRefAm !=null){
			Long staffId = userRefAm.getStaffId();
			OrgStaffs orgStaffs =orgStaffsService.selectByPrimaryKey(staffId);
			if(orgStaffs!=null){
				socialsVo.setAmMobile(orgStaffs.getMobile());
			}
		}
		AppResultData<Object> result = null;
    	result = new AppResultData<Object>(0, "ok", socialsVo);
    	return result;
    }
	/**
	 * 保存报名记录
	 * @param socialsId
	 * @param userId
	 * @return
	 */
	@RequestMapping(value = "post_socials_call", method = RequestMethod.POST)
	public AppResultData<Object> postSocialsCall (
		@RequestParam("socials_id") Long socialsId,
		@RequestParam("user_id")Long userId) {
		
		SocialCall socialCall  = socialsCallService.selectBySocialsIdAndUserId(socialsId, userId);
		if(socialCall ==null){
			socialCall = socialsCallService.initSocialCall();
			socialCall.setSocialId(socialsId);
			socialCall.setAddTime(TimeStampUtil.getNowSecond());
			Users users = usersService.selectByUsersId(userId);
			if(users !=null){
				socialCall.setUserId(userId);
				socialCall.setUserMobile(users.getMobile());
				UserRefAm userRefAm = userRefAmService.selectByAmId(userId);
				if(userRefAm !=null){
					Long staffId = userRefAm.getStaffId();
					OrgStaffs orgStaffs =orgStaffsService.selectByPrimaryKey(staffId);
					if(orgStaffs!=null){
						socialCall.setAmId(orgStaffs.getOrgId());
						socialCall.setAmMobile(orgStaffs.getMobile());
					}
				}
			}
			socialsCallService.insertSelective(socialCall);
		}
		
		
		//点击报名。给用户推送一条短信
		
		Users users = usersService.selectByUsersId(userId);
		
		String[] contentForUser = new String[] {};
		SmsUtil.SendSms(users.getMobile(),  "43485", contentForUser);
		
		
		
		AppResultData<Object> result = null;
		result = new AppResultData<Object>(0, "ok", socialCall);
		return result;
	}
	
	

}
