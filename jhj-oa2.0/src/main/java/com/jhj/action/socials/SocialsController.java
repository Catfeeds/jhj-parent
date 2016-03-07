package com.jhj.action.socials;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
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
import com.jhj.oa.auth.AuthPassport;
import com.jhj.po.model.socials.Socials;
import com.jhj.service.socials.SocialsCallService;
import com.jhj.service.socials.SocialsService;
import com.jhj.vo.SocialCallSearchVo;
import com.jhj.vo.SocialsSearchVo;
import com.jhj.vo.socials.SocialsVo;
import com.meijia.utils.BeanUtilsExp;
import com.meijia.utils.DateUtil;
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
		
		PageInfo result = socialsService.searchVoListPage(searchVo, pageNo,
				pageSize);
		
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
        String path = request.getSession().getServletContext().getRealPath("/WEB-INF/upload/socialsImg");
        String addr = request.getRemoteAddr();
        String urlPath = request.getContextPath();
        int port = request.getServerPort();
		 if(multipartResolver.isMultipart(request))
	        {
	             //判断 request 是否有文件上传,即多部分请求...
	            MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest)(request);
	            Iterator<String> iter = multiRequest.getFileNames();
	            while(iter.hasNext()){
	                MultipartFile file = multiRequest.getFile(iter.next());
	                if(file!=null && !file.isEmpty()){

	                	  String fileName = file.getOriginalFilename();
	                      String extensionName = fileName.substring(fileName.lastIndexOf(".") + 1);
	                      // 新的图片文件名 = 获取时间戳+随机六位数+"."图片扩展名
	                      String before = TimeStampUtil.getNow()+String.valueOf(RandomUtil.randomNumber());
	                      String newFileName = String.valueOf(before+"."+extensionName);
	                     //获取系统发布后upload路径
	                     FileUtils.copyInputStreamToFile(file.getInputStream(), new File(path,newFileName));
	                     String imgUrl =  urlPath+"/upload/socialsImg/"+newFileName;
	                     /*
	                      * 设置数据库存储字段的值
	                      */
	                     socialsNew.setTitleImg(imgUrl);
	                     //生成缩略图
	                     BufferedImage bufferedImage1 = new BufferedImage(60,60,BufferedImage.TYPE_INT_BGR);
	                     BufferedImage bufferedImage = ImageIO.read(file.getInputStream());
	                     Image image = bufferedImage.getScaledInstance(60,60,Image.SCALE_DEFAULT);
	                     bufferedImage1.getGraphics().drawImage(image, 0, 0, null);
	                     String newFileName1 = String.valueOf(before+"_small."+extensionName);

	                     FileOutputStream out = new FileOutputStream(path + "/" + newFileName1);
	                     ImageIO.write(bufferedImage1, "jpg",out);//把图片输出
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
