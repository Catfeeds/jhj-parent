package com.jhj.action.bs;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jhj.action.BaseController;
import com.jhj.common.ConstantMsg;
import com.jhj.common.ConstantOa;
import com.jhj.common.Constants;
import com.jhj.oa.auth.AuthHelper;
import com.jhj.oa.auth.AuthPassport;
import com.jhj.oa.vo.AmVo;
import com.jhj.po.model.bs.OrgStaffAuth;
import com.jhj.po.model.bs.OrgStaffTags;
import com.jhj.po.model.bs.OrgStaffs;
import com.jhj.po.model.bs.Tags;
import com.jhj.po.model.user.UserRefAm;
import com.jhj.po.model.user.Users;
import com.jhj.service.bs.OrgStaffAuthService;
import com.jhj.service.bs.OrgStaffTagsService;
import com.jhj.service.bs.OrgStaffsService;
import com.jhj.service.bs.OrgsService;
import com.jhj.service.bs.TagsService;
import com.jhj.service.users.UserRefAmService;
import com.jhj.vo.StaffSearchVo;
import com.jhj.vo.bs.OrgStaffVo;
import com.meijia.utils.BeanUtilsExp;
import com.meijia.utils.DateUtil;
import com.meijia.utils.ImgServerUtil;
import com.meijia.utils.StringUtil;
import com.meijia.utils.TimeStampUtil;
import com.meijia.utils.vo.AppResultData;

/**
 *
 * @author :hulj
 * @Date : 2015年7月11日下午3:18:29
 * @Description: 助理人员管理
 *
 */
@Controller
@RequestMapping(value = "/bs")
public class OrgStaffsAssistantController extends BaseController {

	@Autowired
	private OrgStaffsService orgStaAsService;
	@Autowired
	private OrgStaffTagsService orgStaTagService;
	@Autowired
	private TagsService tagService;
	@Autowired
	private OrgsService orgService;
	@Autowired
	private UserRefAmService userRefAmService;
	
	@Autowired
	private OrgStaffAuthService authService;
	
	/*
	 * 助理信息列表页，将搜索和 展示 功能，合并为一个方法
	 */
	@AuthPassport
	@RequestMapping(value = "/am-list", method = RequestMethod.GET)
	public String orgStaffAsList(Model model, HttpServletRequest request,
			StaffSearchVo staffSearchVo) throws UnsupportedEncodingException {

		int pageNo = ServletRequestUtils.getIntParameter(request,
				ConstantOa.PAGE_NO_NAME, ConstantOa.DEFAULT_PAGE_NO);
		int pageSize = ServletRequestUtils.getIntParameter(request,
				ConstantOa.PAGE_SIZE_NAME, ConstantOa.DEFAULT_PAGE_SIZE);
		// 分页
		PageHelper.startPage(pageNo, pageSize);

		// 若搜索条件为空，则展示全部
		if (staffSearchVo == null) {
			staffSearchVo = new StaffSearchVo();
		}
		// 助理 人员的类型默认 为1
		staffSearchVo.setStaffType((short) 1);
		
		/*
		 * 设置中文 参数 编码，解决 input 乱码 
		 */
		if(!StringUtil.isEmpty(staffSearchVo.getName())){
			staffSearchVo.setName(new String(staffSearchVo.getName().getBytes("iso-8859-1"),"utf-8"));
		}
		
		
		// 门店Id,  店长搜索条件
		//得到 当前登录 的 门店id，并作为搜索条件
		String org = AuthHelper.getSessionLoginOrg(request);
		
		if(!StringUtil.isEmpty(org)){
			staffSearchVo.setOrgId(Long.parseLong(org));
		}
		
		
		List<OrgStaffs> orgStaAslist = orgStaAsService.selectByListPage(staffSearchVo, pageNo, pageSize);
		OrgStaffs orgStaff = null;
		for (int i = 0; i < orgStaAslist.size(); i++) {
			orgStaff = orgStaAslist.get(i);
			// tagNames
			OrgStaffVo orgStaffVo = orgStaAsService.genOrgStaffVo(orgStaff);

			orgStaAslist.set(i, orgStaffVo);
		}
		
		
//		List<OrgStaffs> orgStaAslistNew = new ArrayList<OrgStaffs>();
		for (Iterator iterator = orgStaAslist.iterator(); iterator.hasNext();) {
			OrgStaffs orgStaffs = (OrgStaffs) iterator.next();
				
			//2016年2月19日14:05:47 此处无需生成缩略图，通过img标签控制展示大小即可
			if (orgStaffs.getHeadImg().equals("")) {

				
				//如果该属性为空，则设置默认
				orgStaffs.setHeadImg("/jhj-oa/upload/headImg/default-head-img.png");
			}
		}

		PageInfo result = new PageInfo(orgStaAslist);
		
		
		model.addAttribute("loginOrgId", org);
		model.addAttribute("orgStaffAsVoModel", result);
		model.addAttribute("orgStaffAsSearchVoModel", staffSearchVo);

		return "bs/asStaffList";
	}

	/*
	 * 页面跳转
	 */
//	@AuthPassport
	@RequestMapping(value = "/staffAsForm", method = RequestMethod.GET)
	public String toOrgStaffAsForm(Model model,HttpServletRequest request,@RequestParam("orgStaffId") Long orgStaffId) {

		
		OrgStaffVo orgStaffVo = orgStaAsService.initOrgStaffVo();
		
		
		//标签列表
		
		/*
		 *  2016-3-7 17:16:24  修改为 不再与员工类型相关，都取得全部标签 
		 */
		List<Tags> tagList = tagService.selectAll();

		OrgStaffs orgStaffs = orgStaAsService.initOrgStaffs();

		
		String id = request.getParameter("orgStaffId");
		if (Long.valueOf(id) > 0L) {
			orgStaffs = orgStaAsService.selectByPrimaryKey(Long.valueOf(id));
		}

		orgStaffVo = orgStaAsService.genOrgStaffVo(orgStaffs);
		
		String staffId = request.getParameter("orgStaffId");
		
		if(Long.valueOf(staffId) == 0L){
			//如果是新增，需要处理 几种 下拉框的 默认值
			orgStaffVo.setNation("0");
			orgStaffVo.setEdu("0");
			orgStaffVo.setBloodType("o"); //默认设置为o型血
		}
		
		model.addAttribute("tagList", tagList);
		model.addAttribute("orgStaffVoModel", orgStaffVo);

		return "bs/asStaffForm";
	}

	/*
	 * 添加、修改功能的实现
	 */
	@AuthPassport
	@RequestMapping(value = "/doOrgStaffAsForm", method = RequestMethod.POST)
	public String doOrgStaffAsForm(HttpServletRequest request, Model model,
			@ModelAttribute("orgStaffVoModel") OrgStaffVo orgStaffVo,
			BindingResult result) throws IOException {

		Long id = Long.valueOf(request.getParameter("staffId"));

		OrgStaffs orgStaffs = orgStaAsService.initOrgStaffs();
		
		if (id > 0L) {
			orgStaffs = orgStaAsService.selectByPrimaryKey(id);
		}
		
		
		BeanUtilsExp.copyPropertiesIgnoreNull(orgStaffVo, orgStaffs);

		// 解决 传递 日期参数（出生年月）的 typeMismatch 问题, 方法参数要有 BindingResult
		String birth = request.getParameter("birth");
		orgStaffs.setBirth(DateUtil.parse(birth));
		// 助理类型 为 1
		orgStaffs.setStaffType((short) 1);

		// 添加时： 处理上传头像
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(
				request.getSession().getServletContext());
		String path = request.getSession().getServletContext()
				.getRealPath("/WEB-INF/upload/headImg");
		String addr = request.getRemoteAddr();
		int port = request.getServerPort();
		if (multipartResolver.isMultipart(request)) {
			// 判断 request 是否有文件上传,即多部分请求...
			MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) (request);
			Iterator<String> iter = multiRequest.getFileNames();
			while (iter.hasNext()) {
				MultipartFile file = multiRequest.getFile(iter.next());
				if (file != null && !file.isEmpty()) {

					//在图片服务器上的 图片 存放位置
                	String url = Constants.IMG_SERVER_HOST + "/upload/";
                	
					String fileName = file.getOriginalFilename();
					String fileType = fileName.substring(fileName.lastIndexOf(".") + 1);
					fileType = fileType.toLowerCase();
					String sendResult = ImgServerUtil.sendPostBytes(url, file.getBytes(), fileType);
					
					ObjectMapper mapper = new ObjectMapper();

					HashMap<String, Object> o = mapper.readValue(sendResult, HashMap.class);

					String ret = o.get("ret").toString();

					HashMap<String, String> info = (HashMap<String, String>) o.get("info");

					String imgUrl = Constants.IMG_SERVER_HOST + "/" + info.get("md5").toString();

					orgStaffs.setHeadImg(imgUrl);
					
				}
			}
		}

		if (id > 0L) {
			
			orgStaffs.setUpdateTime(TimeStampUtil.getNow() / 1000);
			orgStaAsService.updateByPrimaryKeySelective(orgStaffs);
		} else {
			orgStaAsService.insertSelective(orgStaffs);
		}

		// 存储标签
		String tagListStr = request.getParameter("tagIds");

		if (!StringUtil.isEmpty(tagListStr)) {
			orgStaTagService.deleteByStaffId(id);
			String[] tagList = StringUtil.convertStrToArray(tagListStr);

			for (int i = 0; i < tagList.length; i++) {
				if (StringUtil.isEmpty(tagList[i]))
					continue;

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
		
		return "redirect:/bs/am-list";
	}

	/*
	 * 服务 人员管理--form 表单页， 根据门店 选择 门店下 助理
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/get-am-by-orgId", method = RequestMethod.GET)
	public AppResultData<Object> getAmsByOrgId(
			@RequestParam(value = "orgId", required = true, defaultValue = "0") Long orgId) {

		List<OrgStaffs> allAmList = orgStaAsService.selectAmByOrgId(orgId);

		if (orgId > 0) {
			allAmList = orgStaAsService.selectAmByOrgId(orgId);
		}
		
		AppResultData<Object> result = new AppResultData<Object>(
				Constants.SUCCESS_0, ConstantMsg.SUCCESS_0_MSG, allAmList);

		return result;
	}
	
	/*
	 * 助理查看，名下管辖的所有阿姨
	 */
	@RequestMapping(value = "/getOwnUser",method = RequestMethod.GET)
	public String getOwnUsers(Model model,HttpServletRequest request){
		
		int pageNo = ServletRequestUtils.getIntParameter(request,
				ConstantOa.PAGE_NO_NAME, ConstantOa.DEFAULT_PAGE_NO);
		int pageSize = ServletRequestUtils.getIntParameter(request,
				ConstantOa.PAGE_SIZE_NAME, ConstantOa.DEFAULT_PAGE_SIZE);
		
		//分页
		PageHelper.startPage(pageNo, pageSize);
		
		String parameter = request.getParameter("orgStaffId");
		
		Long staffId = Long.parseLong(parameter);
		
		//用户列表
		List<Users> userList = orgStaAsService.amGetUserList(staffId,pageNo, pageSize);
		
		//助理列表： 根据当前助理，找到 当前门店下的 所有助理
		
		OrgStaffs orgStaffs = orgStaAsService.selectByPrimaryKey(staffId);
		Long orgId = orgStaffs.getOrgId();
		List<OrgStaffs> staffList = orgStaAsService.selectAmByOrgId(orgId);
		
		List<UserRefAm> refList = userRefAmService.selectAllUserByAmId(staffId);
		
		List<Long> userIdList = new ArrayList<Long>();
		
		for (UserRefAm refAm : refList) {
			userIdList.add(refAm.getUserId());
		}
		
		AmVo amVo = new AmVo();
		amVo.setAmList(staffList);
		amVo.setUserIdList(userIdList);
		amVo.setOldAmId(staffId);
		amVo.setAmName(orgStaffs.getName());
		
		
		PageInfo result = new PageInfo(userList);
		
		model.addAttribute("amVoModel", amVo);
		model.addAttribute("userListModel", result);
		
		return "bs/amUserList";
	}
	
	/*
	 *  当前门店下 调整  
	 *  		某助理名下 的用户 的 对应助理
	 */
	
	@RequestMapping(value = "changeAmForUser",method = RequestMethod.POST)
	public AppResultData<Object> changeAmForUser(HttpServletRequest request){
		
		AppResultData<Object> result = new AppResultData<Object>(Constants.SUCCESS_0, ConstantMsg.SUCCESS_0_MSG, "");
		
		//新助理 id
		String amId = request.getParameter("am_id");	
		
		//需要修改的用户
		String userIds = request.getParameter("user_ids");
		
		//老助理 id
		
		String oldAmId = request.getParameter("old_am_id");
		if(StringUtil.isEmpty(userIds)|| amId == "0"){
			
			result.setStatus(0);
			result.setMsg("请选择合适的用户");
			return result;
		}
		
		long myAmId = Long.parseLong(amId);
		
		long myOldAmId = Long.parseLong(oldAmId);
		
		String[]  userIdArray = StringUtil.convertStrToArray(userIds);
		
		//重复，提示选择新助理
		if(myOldAmId == myAmId){
			
			result.setStatus(1);
			result.setMsg("请选择新助理");
			return result;
		}
		
		
		Long[] idArray = new Long[userIdArray.length];
		
		for (int i = 0; i < userIdArray.length; i++) {
			idArray[i] = Long.valueOf(userIdArray[i]);
		}
		
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("oldAmId", myOldAmId);
		map.put("amId", myAmId);
		map.put("array", idArray);
		
		List<UserRefAm> list = userRefAmService.selectByUserIdAndAmId(map);
		
		//先删除在插入
		
		userRefAmService.delectByStaffId(map);
		
		for (UserRefAm userRefAm2 : list) {
			
			userRefAm2.setStaffId(myAmId);
			userRefAm2.setAddTime(TimeStampUtil.getNow()/1000);
			
			userRefAmService.insertSelective(userRefAm2);
		}
		
		result.setStatus(2);
		result.setMsg("调整成功");
		return result;
	}
}
