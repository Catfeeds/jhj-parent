package com.jhj.action.bs;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jhj.action.BaseController;
import com.jhj.common.ConstantMsg;
import com.jhj.common.ConstantOa;
import com.jhj.common.Constants;
import com.jhj.oa.auth.AuthHelper;
import com.jhj.oa.auth.AuthPassport;
import com.jhj.po.model.bs.OrgStaffAuth;
import com.jhj.po.model.bs.OrgStaffTags;
import com.jhj.po.model.bs.OrgStaffs;
import com.jhj.po.model.bs.Tags;
import com.jhj.po.model.order.OrderDispatchs;
import com.jhj.po.model.user.UserTrailHistory;
import com.jhj.po.model.user.UserTrailReal;
import com.jhj.service.bs.OrgStaffAuthService;
import com.jhj.service.bs.OrgStaffTagsService;
import com.jhj.service.bs.OrgStaffsService;
import com.jhj.service.bs.OrgsService;
import com.jhj.service.bs.TagsService;
import com.jhj.service.order.OrderDispatchsService;
import com.jhj.service.users.UserTrailHistoryService;
import com.jhj.service.users.UserTrailRealService;
import com.jhj.vo.TagSearchVo;
import com.jhj.vo.bs.OrgStaffVo;
import com.jhj.vo.order.OrderDispatchSearchVo;
import com.jhj.vo.staff.OrgStaffPoiVo;
import com.jhj.vo.staff.StaffSearchVo;
import com.jhj.vo.user.UserTrailHistoryVo;
import com.jhj.vo.user.UserTrailSearchVo;
import com.meijia.utils.BeanUtilsExp;
import com.meijia.utils.DateUtil;
import com.meijia.utils.StringUtil;
import com.meijia.utils.TimeStampUtil;
import com.meijia.utils.baidu.MapPoiUtil;
import com.meijia.utils.vo.AppResultData;

/**
 *
 * @author :hulj
 * @Date : 2015年7月6日下午3:26:00
 * @Description: 服务人员管理（阿姨）
 *
 */
@Controller
@RequestMapping(value = "/bs")
public class OrgStaffsController extends BaseController {
	
	@Autowired
	private OrgStaffsService orgStaffsService;
	@Autowired
	private OrgStaffTagsService orgStaTagService;
	@Autowired
	private TagsService tagService;
	@Autowired
	private OrgsService orgService;
	
	@Autowired
	private OrgStaffAuthService authService;
	
	@Autowired
	private OrderDispatchsService orderDispatchService;
	
	@Autowired
	private UserTrailRealService userTrailRealService;
	
	@Autowired
	private UserTrailHistoryService userTrailHistoryService;
	
	
	
	
//	@AuthPassport
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/staff-list", method = RequestMethod.GET)
	public String orgStaffList(Model model, HttpServletRequest request, StaffSearchVo staffSearchVo) throws UnsupportedEncodingException{
		
		int pageNo = ServletRequestUtils.getIntParameter(request,
				ConstantOa.PAGE_NO_NAME, ConstantOa.DEFAULT_PAGE_NO);
		int pageSize = ServletRequestUtils.getIntParameter(request,
				ConstantOa.PAGE_SIZE_NAME, ConstantOa.DEFAULT_PAGE_SIZE);
		
		
		//分页
		PageHelper.startPage(pageNo, pageSize);
		
		if (staffSearchVo == null) {
			staffSearchVo = new StaffSearchVo();
		}
		//staffType
		staffSearchVo.setStaffType((short)0);
		/*
		 * 设置中文 参数 编码，解决 input 乱码 
		 */
		staffSearchVo.setName(new String(staffSearchVo.getName().getBytes("iso-8859-1"),"utf-8"));
		
		
		// 门店Id,  店长搜索条件
		//得到 当前登录 的 门店id，并作为搜索条件
		Long sessionOrgId = AuthHelper.getSessionLoginOrg(request);

		if (sessionOrgId > 0L) {
			staffSearchVo.setOrgId(sessionOrgId);
		}
		
//		List<OrgStaffs> list = orgStaffsService.selectByListPage(staffSearchVo, pageNo, pageSize);
		List<OrgStaffs> list = new ArrayList<OrgStaffs>();
		//tagnames
		OrgStaffs orgStaff = null;
		for (int i = 0; i < list.size(); i++) {
			orgStaff = list.get(i);
			//tagNames
			OrgStaffVo orgStaffVo = orgStaffsService.genOrgStaffVo(orgStaff);
			
			list.set(i,  orgStaffVo);
		}
		
		PageInfo result = new PageInfo(list);
		
		model.addAttribute("orgStaffVoModel", result);
		model.addAttribute("orgStaffSearchVoModel", staffSearchVo);
		return "bs/orgStaffList";
	}
		
	/*
	 *页面跳转
	 */
//	@AuthPassport
	@RequestMapping(value = "/orgStaffForm", method = RequestMethod.GET)
	public String toOrgStaffForm(Model model,HttpServletRequest request,
			@RequestParam("orgStaffId") Long staffId) {
		
		
		OrgStaffVo orgStaffVo = orgStaffsService.initOrgStaffVo();
		
		//标签列表
		
		/*
		 *  2016-3-7 17:16:24  修改为 不再与员工类型相关，都取得全部标签 
		 */
		TagSearchVo searchVo1 = new TagSearchVo();
		searchVo1.setTagType((short) 0);
		List<Tags> tagList = tagService.selectBySearchVo(searchVo1);
		

		OrgStaffs orgStaffs = orgStaffsService.initOrgStaffs();
		
		String id = request.getParameter("orgStaffId");
		
		
		if (Long.valueOf(id) > 0L) {
			orgStaffs = orgStaffsService.selectByPrimaryKey(Long.valueOf(id));
			
		}
		orgStaffVo = orgStaffsService.genOrgStaffVo(orgStaffs);
		
		//动态展示 门店。助理 下拉列表
		Long sessionOrgId = AuthHelper.getSessionLoginOrg(request);

		if (sessionOrgId > 0L) {
			//如果登录的是 店长
			
			StaffSearchVo searchVo = new StaffSearchVo();
			searchVo.setOrgId(sessionOrgId);
			searchVo.setStaffType((short) 1);
			searchVo.setStatus(1);
			
			List<OrgStaffs> amList = orgStaffsService.selectBySearchVo(searchVo);
			
			orgStaffVo.setNowOrgAmList(amList);
		}
		
		model.addAttribute("loginOrgId", sessionOrgId);
		model.addAttribute("tagList", tagList);
		model.addAttribute("orgStaffVoFormModel", orgStaffVo);
		
		return "bs/orgStaffForm";
	}
	
	/*
	 *添加或修改
	 */
//	@AuthPassport
	@RequestMapping(value = "/doOrgStaffForm", method = { RequestMethod.POST })
	public String doOrgStaffForm(HttpServletRequest request, Model model,
			@ModelAttribute("orgStaffVoFormModel") OrgStaffVo orgStaffVo, BindingResult result) throws IOException {
		
		Long id = Long.valueOf(request.getParameter("staffId"));
		OrgStaffs orgStaffs = orgStaffsService.initOrgStaffs();
		
		BeanUtilsExp.copyPropertiesIgnoreNull(orgStaffVo, orgStaffs);
		String birth = request.getParameter("birth");
		orgStaffs.setBirth(DateUtil.parse(birth));
		
		if (id > 0L) {
			orgStaffs.setUpdateTime(TimeStampUtil.getNow()/1000);
			orgStaffsService.updateByPrimaryKeySelective(orgStaffs);
		} else {
			orgStaffsService.insertSelective(orgStaffs);
		}
		
		//存储标签
		String tagListStr = request.getParameter("tagIds");
		
		if (!StringUtil.isEmpty(tagListStr)) {
			orgStaTagService.deleteByStaffId(id);
			String[] tagList = StringUtil.convertStrToArray(tagListStr);
			
			for (int i = 0; i < tagList.length; i++) {
				if (StringUtil.isEmpty(tagList[i])) continue;
				
				OrgStaffTags record = new OrgStaffTags();
				record.setId(0L);
				record.setStaffId(orgStaffs.getStaffId());
				record.setTagId(Long.valueOf(tagList[i]));
				record.setAddTime(TimeStampUtil.getNowSecond());
				orgStaTagService.insert(record);
			}
		}
		
		
		/*
		 * 2016年1月22日19:14:09   录入认证信息
		 */
		
		String authIds = request.getParameter("authIds");
		
		if (!StringUtil.isEmpty(authIds)) {
			
			authService.deleteByStaffId(id);
			
			String[] tagList = StringUtil.convertStrToArray(authIds);
			
			for (int i = 0; i < tagList.length; i++) {
				if (StringUtil.isEmpty(tagList[i])) continue;
				
				OrgStaffAuth staffAuth = authService.initOrgStaffAuth();
				
				staffAuth.setStaffId(id);
				staffAuth.setServiceTypeId(Long.valueOf(tagList[i]));
				staffAuth.setAutStatus(Constants.STAFF_AUTH_STATUS_SUCCESS);
				staffAuth.setUpdateTime(TimeStampUtil.getNowSecond());
				
				authService.insertSelective(staffAuth);
			}
		}
		
		
		return "redirect:/bs/staff-list";
	}
	
	/*
	 * 校验手机号是否重复
	 */
	@RequestMapping(value = "/validMobile", method = RequestMethod.GET)
	public void validMobile(PrintWriter out,String name) throws UnsupportedEncodingException{
		
		String names = URLDecoder.decode(name,"utf-8");
		
		StaffSearchVo searchVo1 = new StaffSearchVo();
		searchVo1.setMobile(names);
		List<OrgStaffs> staffList = orgStaffsService.selectBySearchVo(searchVo1);
		OrgStaffs orgStaff = null;
		if (!staffList.isEmpty()) orgStaff = staffList.get(0);
		
		if(orgStaff !=null){
			//如果输入的名称能查出来记录，则返回 名称已存在 标识
			out.write("no");
		}else{
			out.write("yes");
		}
	}
	
	/*
	 * 校验身份证是否重复
	 */
	@RequestMapping(value = "/validCard", method = RequestMethod.GET)
	public void validCard(PrintWriter out,String name) throws UnsupportedEncodingException{
		
		String names = URLDecoder.decode(name,"utf-8");
		
		StaffSearchVo searchVo = new StaffSearchVo();
		searchVo.setCardId(names);
		List<OrgStaffs> orgStaffs = orgStaffsService.selectBySearchVo(searchVo);
		
		if(!orgStaffs.isEmpty()){
			//如果输入的名称能查出来记录，则返回 名称已存在 标识
			out.write("no");
		}else{
			out.write("yes");
		}
	}
	
	@AuthPassport
	@RequestMapping(value = "/staff-map", method = RequestMethod.GET)
	public String staffMap(Model model,HttpServletRequest request) {
		
		
		//得到 当前登录 的 门店id，并作为搜索条件
		Long sessionOrgId = AuthHelper.getSessionLoginOrg(request);
		model.addAttribute("sessionOrgId", sessionOrgId);
		
		//当天
		String today = DateUtil.getToday();
		model.addAttribute("today", today);
		return "bs/orgStaffMap";
	}	
	
	
	@RequestMapping(value = "get_staff_map.json", method = RequestMethod.GET)
	public AppResultData<Object> submitManuBaseOrder(Model model, 
			HttpServletRequest request,
			@RequestParam(value = "parentId",required =false,defaultValue = "0") Long parentId,
			@RequestParam(value = "orgId",required =false,defaultValue = "0") Long orgId,
			@RequestParam(value = "status",required =false,defaultValue = "0") int status, // 0 = 全部  1 = 在线 2 = 途中  3 = 服务中 
			@RequestParam(value = "staff_name",required =false,defaultValue = "") String staffName
			
			) {
		AppResultData<Object> result = new AppResultData<Object>(Constants.SUCCESS_0, ConstantMsg.SUCCESS_0_MSG, "");
		
		StaffSearchVo searchVo = new StaffSearchVo();
		Long sessionParentId = AuthHelper.getSessionLoginOrg(request);
		
		if (sessionParentId > 0L)
			searchVo.setParentId(sessionParentId);

		if (parentId > 0L) searchVo.setParentId(parentId);

		if (orgId > 0L) searchVo.setOrgId(orgId);
		
		if (!StringUtil.isEmpty(staffName)) searchVo.setName(staffName);
		
		if (searchVo.getStatus() == null) searchVo.setStatus(1);
		
		List<OrgStaffs> staffList = orgStaffsService.selectBySearchVo(searchVo);
		
		List<Long> staffIds = new ArrayList<Long>();
		for (OrgStaffs s : staffList) {
			if (!staffIds.contains(s.getStaffId())) staffIds.add(s.getStaffId());
		}
		
		
		UserTrailSearchVo searchVo1 = new UserTrailSearchVo();
		searchVo1.setUserIds(staffIds);
		searchVo1.setUserType((short) 0);
		
		List<UserTrailReal> userTrails = userTrailRealService.selectBySearchVo(searchVo1);
		
		//两个小时内在线的，两个小时之外的为不在线.
		List<OrgStaffPoiVo> onlines = new ArrayList<OrgStaffPoiVo>();
		List<OrgStaffPoiVo> offLines = new ArrayList<OrgStaffPoiVo>();
		
		Long now = TimeStampUtil.getNowSecond();
//		Long now = 1480042800L; //jhj-online
//		Long now = 1479538800L; //jhj-test
		Long maxLastTime = now - 2 * 3600;
		
		for (OrgStaffs os : staffList) {
			OrgStaffPoiVo vo = new OrgStaffPoiVo();
			BeanUtilsExp.copyPropertiesIgnoreNull(os, vo);
			vo.setLat("");
			vo.setLng("");
			vo.setPoiTime(0L);
			vo.setPoiTimeStr("");
			vo.setPoiStatus(0);
			vo.setPoiName("");
			
			UserTrailReal ut = null;
			for (UserTrailReal item : userTrails) {
				if (os.getStaffId().equals(item.getUserId())) {
					ut = item;
					break;
				}
			}
			
			if (ut == null) {
				//如果找不到，则为离线人员；
				offLines.add(vo);
				continue;
			}
				
			if (ut.getAddTime() >= maxLastTime) {
				vo.setLat(ut.getLat());
				vo.setLng(ut.getLng());
				vo.setPoiTime(ut.getAddTime());
				vo.setPoiStatus(1);
				vo.setPoiName(ut.getPoiName());
				vo.setPoiTimeStr(TimeStampUtil.timeStampToDateStr(ut.getAddTime() * 1000, "yyyy-MM-dd HH:MM"));
				onlines.add(vo);
			} else {
				vo.setLat(ut.getLat());
				vo.setLng(ut.getLng());
				vo.setPoiTime(ut.getAddTime());
				vo.setPoiName(ut.getPoiName());
				vo.setPoiTimeStr(TimeStampUtil.timeStampToDateStr(ut.getAddTime() * 1000, "yyyy-MM-dd HH:MM"));
				offLines.add(vo);
				
			}
			
		
		}
		
		//在线的要找出当前的状态 / 0 = 全部  1 = 在线 2 = 途中  3 = 服务中
		if (!onlines.isEmpty()) {
			List<Long> onlineStaffIds = new ArrayList<Long>();
			for (OrgStaffPoiVo on : onlines) {
				onlineStaffIds.add(on.getStaffId());
			}
			
			
			Long startServiceTime = TimeStampUtil.getBeginOfToday();
			Long endServiceTime = TimeStampUtil.getEndOfToday();
			
			OrderDispatchSearchVo dispatchSearchVo = new OrderDispatchSearchVo();
			dispatchSearchVo.setStaffIds(onlineStaffIds);
			dispatchSearchVo.setDispatchStatus((short) 1);
			dispatchSearchVo.setStartServiceTime(startServiceTime);
			dispatchSearchVo.setEndServiceTime(endServiceTime);
			List<OrderDispatchs> disList = orderDispatchService.selectBySearchVo(dispatchSearchVo);
			
			
			for (OrderDispatchs item : disList) {
				Long serviceTime = item.getServiceDate();
				Long staffId = item.getStaffId();
				double serviceHour = item.getServiceHours();
				Long servicePreTime = serviceTime - Constants.SERVICE_PRE_TIME;
				Long serviceEndTime = (long) (serviceTime + serviceHour * 3600);
				
				
				for (int i = 0; i < onlines.size(); i++) {
					OrgStaffPoiVo poiVo = onlines.get(i);
					if (poiVo.getStaffId().equals(staffId)) {
						//如果服务时间内，则为服务中
						if (now >= servicePreTime && now < serviceTime  ) {
							poiVo.setPoiStatus(2);
							onlines.set(i, poiVo);
						}
						
						if (now >= serviceTime && now <= serviceEndTime) {
							poiVo.setPoiStatus(3);
							onlines.set(i, poiVo);
						}
					}
				}	
			}
		}
		
		
		//根据参数是否需要过滤掉 在线， 在途中， 服务中.
		List<OrgStaffPoiVo> onlineFilter = new ArrayList<OrgStaffPoiVo>();
		if (status > 0) {
			for (OrgStaffPoiVo item : onlines) {
				if (item.getPoiStatus() == status) {
					onlineFilter.add(item);
				}
			}
		} else {
			onlineFilter = onlines;
		}
		
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("online", onlineFilter);
		resultMap.put("offline", offLines);
		
		result.setData(resultMap);
		
		return result;
	}
	
	@RequestMapping(value = "get_staff_trail.json", method = RequestMethod.GET)
	public AppResultData<Object> getStaffTrain(Model model, 
			HttpServletRequest request,
			@RequestParam(value = "service_date",required =false,defaultValue = "") String serviceDateStr,
			@RequestParam(value = "staff_id",required =false,defaultValue = "0") Long staffId,
			@RequestParam(value = "merge_distance",required =false,defaultValue = "2000") int mergeDistance
			
			) {
		AppResultData<Object> result = new AppResultData<Object>(Constants.SUCCESS_0, ConstantMsg.SUCCESS_0_MSG, "");
				
		if (staffId.equals(0L)) return result;
		
		UserTrailSearchVo searchVo1 = new UserTrailSearchVo();
		searchVo1.setUserId(staffId);
		searchVo1.setUserType((short) 0);
		
		//时间
		if (StringUtil.isEmpty(serviceDateStr)) serviceDateStr = DateUtil.getToday();
		
		Long startTime = TimeStampUtil.getMillisOfDayFull(serviceDateStr + " 08:00:00");
		Long endTime = TimeStampUtil.getMillisOfDayFull(serviceDateStr + " 20:00:00");
		searchVo1.setStartTime(startTime / 1000);
		searchVo1.setEndTime(endTime / 1000);
		
		List<UserTrailHistory> userTrailHistory = userTrailHistoryService.selectBySearchVo(searchVo1);
		
		List<UserTrailHistoryVo> vos = new ArrayList<UserTrailHistoryVo>();
		
		if (userTrailHistory.isEmpty()) return result;
		
		OrgStaffs orgStaff = orgStaffsService.selectByPrimaryKey(staffId);
		
		
		String tmpTimeStr = "";
		UserTrailHistory curItem = null;
		UserTrailHistory preItem = null;
		UserTrailHistoryVo vo = new UserTrailHistoryVo();
		for (int i = 0; i < userTrailHistory.size(); i ++) {
			
			curItem = userTrailHistory.get(i);
			
			if (StringUtil.isEmpty(curItem.getLng()) || StringUtil.isEmpty(curItem.getLat())) {
				continue;
			}
			//百度定位失败,安卓返回4.9E-324;
			if (curItem.getLng().equals("4.9E-324") || curItem.getLat().equals("4.9E-324")) {
				continue;
			}
			
			tmpTimeStr = TimeStampUtil.timeStampToDateStr(curItem.getAddTime() * 1000, "HH:mm");
			if (i == 0) {
				preItem = userTrailHistory.get(i);
				
				BeanUtilsExp.copyPropertiesIgnoreNull(curItem, vo);
				
				vo.setName(orgStaff.getName());
				vo.setAddTimeStr(TimeStampUtil.timeStampToDateStr(curItem.getAddTime() * 1000, "HH:mm"));
				continue;
			} else {
				preItem = userTrailHistory.get(i - 1);
			}
			
			String fromLng = curItem.getLng();
			String fromLat = curItem.getLat();
			String destLng = preItem.getLng();
			String destLat = preItem.getLat();
			int distance = MapPoiUtil.poiDistance(fromLng, fromLat, destLng, destLat);
			System.out.println("i = " + i + "--- distncae = " + distance);
			//超过2公里，则标记为一个点;
			if (distance >= mergeDistance) {
				
				vo.setAddTimeStr(vo.getAddTimeStr() + "到" + tmpTimeStr);
				vos = mergeDistance(vos, vo, mergeDistance);

				
				vo = new UserTrailHistoryVo();
				BeanUtilsExp.copyPropertiesIgnoreNull(curItem, vo);
				vo.setName(orgStaff.getName());
				vo.setAddTimeStr(TimeStampUtil.timeStampToDateStr(curItem.getAddTime() * 1000, "HH:mm"));
				
				if (i == (userTrailHistory.size() - 1) ) {
					vos = mergeDistance(vos, vo, mergeDistance);
				}
				
			} else {
				if (i == (userTrailHistory.size() - 1) ) {
					vo.setAddTimeStr(vo.getAddTimeStr() + "到" + tmpTimeStr);
					vos = mergeDistance(vos, vo, mergeDistance);
				}
			}
		}
		
		result.setData(vos);
		return result;
	}
	
	
	public List<UserTrailHistoryVo> mergeDistance(List<UserTrailHistoryVo> vos, UserTrailHistoryVo vo, int mergeDistance) {
		
		if (vos.isEmpty()) {
			vos.add(vo);
			return vos;
		}
		
		int mergeIndex = -1;
		
		for (int i = 0; i < vos.size(); i++) {
			UserTrailHistoryVo item = vos.get(i);
			String fromLng = item.getLng();
			String fromLat = item.getLat();
			String destLng = vo.getLng();
			String destLat = vo.getLat();
							
			int distance = MapPoiUtil.poiDistance(fromLng, fromLat, destLng, destLat);
			if (distance <= mergeDistance) {
				mergeIndex = i;
				break;
			} 
			
		}
		
		if (mergeIndex > -1) {
			UserTrailHistoryVo item = vos.get(mergeIndex);
			String addTimeStr = vo.getAddTimeStr() + "     " + vo.getAddTimeStr();
			item.setAddTimeStr(addTimeStr);
			vos.set(mergeIndex, item);
		} else {
			vos.add(vo);
		}
		
		
		
		return vos;
		
	}
	
	
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "select-staff-by-org.json", method = RequestMethod.POST)
	public  AppResultData<Object> selectByORg(
			@RequestParam(value = "parentId", required = true, defaultValue = "0") Long parentId,
			@RequestParam(value = "orgId", required = true, defaultValue = "0") Long orgId) {

		AppResultData<Object> result = new AppResultData<Object>(
		Constants.SUCCESS_0, ConstantMsg.SUCCESS_0_MSG, false);
		
		StaffSearchVo searchVo = new StaffSearchVo();
		searchVo.setParentId(parentId);
		searchVo.setOrgId(orgId);
		searchVo.setStatus(1);
		List<OrgStaffs> list = orgStaffsService.selectBySearchVo(searchVo);
				
		result.setData(list);
		
		return result;
	}

}
