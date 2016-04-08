package com.jhj.action.socials;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
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

import com.github.pagehelper.PageInfo;
import com.jhj.action.BaseController;
import com.jhj.common.ConstantOa;
import com.jhj.common.Constants;
import com.jhj.oa.auth.AuthPassport;
import com.jhj.po.model.bs.OrgStaffs;
import com.jhj.po.model.socials.Socials;
import com.jhj.service.socials.SocialsCallService;
import com.jhj.service.socials.SocialsService;
import com.jhj.vo.SocialCallSearchVo;
import com.jhj.vo.SocialsSearchVo;
import com.jhj.vo.bs.OrgStaffVo;
import com.jhj.vo.socials.SocialsVo;
import com.meijia.utils.BeanUtilsExp;
import com.meijia.utils.DateUtil;
import com.meijia.utils.ImgServerUtil;
import com.meijia.utils.RandomUtil;
import com.meijia.utils.StringUtil;
import com.meijia.utils.TimeStampUtil;

/**
 * @description：
 * @author： kerryg
 * @date:2015年9月7日 
 */
@Controller
@RequestMapping(value = "/socials")
public class SocialsController extends BaseController {
	
	@Autowired
	private SocialsService socialsService;

	@Autowired
	private SocialsCallService socialsCallService;
	/**
	 * 根据searchVo查询社区活动列表
	 * @param request
	 * @param model
	 * @param searchVo
	 * @return
	 */
	@AuthPassport
	@RequestMapping(value = "/socials-list", method = { RequestMethod.GET })
	public String socialsList(HttpServletRequest request, Model model,
			SocialsSearchVo searchVo) {
		model.addAttribute("requestUrl", request.getServletPath());
		model.addAttribute("requestQuery", request.getQueryString());
		
		model.addAttribute("searchModel", searchVo);
		int pageNo = ServletRequestUtils.getIntParameter(request,
				ConstantOa.PAGE_NO_NAME, ConstantOa.DEFAULT_PAGE_NO);
		int pageSize = ServletRequestUtils.getIntParameter(request,
				ConstantOa.PAGE_SIZE_NAME, ConstantOa.DEFAULT_PAGE_SIZE);
		
		List<Socials> list = socialsService.searchVoListPage(searchVo, pageNo, pageSize);
		
		Socials socials = null;
		for (int i = 0; i < list.size(); i++) {
			socials = list.get(i);
			
			SocialsVo socialsVo = socialsService.oaListTransToVo(socials);
			
			list.set(i,socialsVo);
		}
		
		PageInfo result = new PageInfo(list);
		
		model.addAttribute("contentModel", result);
		
		return "socials/socialsList";
	}
	@AuthPassport
	@RequestMapping(value = "/social-call-list", method = { RequestMethod.GET })
	public String SocialsCallList(HttpServletRequest request, Model model,
		SocialCallSearchVo searchVo) {
		model.addAttribute("requestUrl", request.getServletPath());
		model.addAttribute("requestQuery", request.getQueryString());
		
		model.addAttribute("searchModel", searchVo);
		int pageNo = ServletRequestUtils.getIntParameter(request,
			ConstantOa.PAGE_NO_NAME, ConstantOa.DEFAULT_PAGE_NO);
		int pageSize = ServletRequestUtils.getIntParameter(request,
			ConstantOa.PAGE_SIZE_NAME, ConstantOa.DEFAULT_PAGE_SIZE);
		
		PageInfo result = socialsCallService.searchVoListPage(searchVo, pageNo,
			pageSize);
		model.addAttribute("contentModel", result);
		
		return "socials/socialCallList";
	}

	/**
	 * to社区活动表单页面
	 * @param model
	 * @param id
	 * @param request
	 * @return
	 */
	@AuthPassport
	@RequestMapping(value = "/socials-form", method = { RequestMethod.GET })
	public String serviceTypeForm(Model model,
		@RequestParam(value="id" ,required = false)Long id,HttpServletRequest request) {
		if (id == null) {
			id = 0L;
		}
		Socials socials =socialsService.initSocial();
		SocialsVo  socialsVo = new SocialsVo();
		if (id != null && id > 0) {
			socials = socialsService.selectByPrimaryKey(id);
			BeanUtilsExp.copyPropertiesIgnoreNull(socials,socialsVo);
			socialsVo.setTitleImgs(socials.getTitleImg());
		}else {
			//新增设置题图url=''
			BeanUtilsExp.copyPropertiesIgnoreNull(socials,socialsVo);
			socialsVo.setTitleSmallImg("");
			socialsVo.setTitleImgs(socials.getTitleImg());
		}

		model.addAttribute("socialsVo", socialsVo);

		return "socials/socialsForm";
	}
	/**
	 * 保存社区活动信息
	 * @param request
	 * @param model
	 * @param socials
	 * @param result
	 * @return
	 * @throws IOException
	 */
	@AuthPassport
	@RequestMapping(value = "/socials-form", method = RequestMethod.POST)
	public String doOrgStaffAsForm(HttpServletRequest request, Model model,
			@ModelAttribute("socialsVo") SocialsVo socialsVo, BindingResult result) throws IOException{
		
		
		Long id = socialsVo.getId();
		Socials socialsNew =socialsService.initSocial();
		BeanUtilsExp.copyPropertiesIgnoreNull(socialsVo, socialsNew);
		if(id>0){
			Socials socials =socialsService.selectByPrimaryKey(id);
			socialsNew.setTitleImg(socials.getTitleImg());
		}
		//  添加时：  处理上传头像
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
		 if(multipartResolver.isMultipart(request))
	        {
	             //判断 request 是否有文件上传,即多部分请求...
	            MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest)(request);
	            Iterator<String> iter = multiRequest.getFileNames();
	            while(iter.hasNext()){
	                MultipartFile file = multiRequest.getFile(iter.next());
	                if(file!=null && !file.isEmpty()){
	                	 
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

						socialsNew.setTitleImg(imgUrl);
	                }
	            }
	        }
		 String beginDate = request.getParameter("beginDate");
		 String endDate = request.getParameter("endDate");
		 socialsNew.setBeginDate(DateUtil.parse(beginDate));
		 socialsNew.setEndDate(DateUtil.parse(endDate));
		 if (id > 0L) {
			 socialsNew.setUpdateTime(TimeStampUtil.getNowSecond());
				socialsService.updateByPrimaryKeySelective(socialsNew);
			} else {
				socialsNew.setAddTime(TimeStampUtil.getNowSecond());
				socialsNew.setUpdateTime(TimeStampUtil.getNowSecond());
				socialsService.insertSelective(socialsNew);
			}
			
		return "redirect:/socials/socials-list";
	}
	/**
	 * 根据Id删除社区活动记录
	 * @param socials
	 * @param id
	 * @return
	 */
	@AuthPassport
	@RequestMapping(value = "/deleteBySocialsId", method = { RequestMethod.GET })
	public String deleteBySocialsId(
			@ModelAttribute("socials") Socials socials,
			@RequestParam(value="id")Long id) {
			String path = "redirect:/socials/socials-list";
			if(id!=null){
				int result = socialsService.deleteByPrimaryKey(id);
				if(result > 0){
					path = "redirect:/socials/socials-list";
				}else{
					path = "error";
				}
			}
		return path;
	}
}
