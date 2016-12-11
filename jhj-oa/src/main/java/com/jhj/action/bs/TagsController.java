package com.jhj.action.bs;

import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

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
import com.jhj.common.ConstantOa;
import com.jhj.oa.auth.AuthPassport;
import com.jhj.po.model.bs.Tags;
import com.jhj.service.bs.TagsService;
import com.jhj.vo.TagSearchVo;
import com.meijia.utils.TimeStampUtil;

/**
 *
 * @author :hulj
 * @Date : 2015年7月15日下午6:26:17
 * @Description: 标签库
 *
 */
@Controller
@RequestMapping(value = "/bs")
public class TagsController extends BaseController {
	
	@Autowired
	private TagsService tagsService;
	@AuthPassport
	@RequestMapping(value = "/tag-list",method = {RequestMethod.GET})
	public String tagList(Model model, HttpServletRequest request, TagSearchVo tagSearchVo) throws UnsupportedEncodingException{

		int pageNo = ServletRequestUtils.getIntParameter(request,
				ConstantOa.PAGE_NO_NAME, ConstantOa.DEFAULT_PAGE_NO);
		int pageSize = ServletRequestUtils.getIntParameter(request,
				ConstantOa.PAGE_SIZE_NAME, ConstantOa.DEFAULT_PAGE_SIZE);
		//分页
		PageHelper.startPage(pageNo, pageSize);
		
		if (tagSearchVo == null) {
			tagSearchVo = new TagSearchVo();
		}
		
		List<Tags> list = tagsService.selectByListPage(tagSearchVo, pageNo, pageSize);
		
		
		PageInfo result = new PageInfo(list);
		
		model.addAttribute("tagVoModel", result);
		model.addAttribute("tagSearchVoModel", tagSearchVo);
		
		return "bs/tagList";
		
	}
	
	@AuthPassport
	@RequestMapping(value = "/tagForm", method = RequestMethod.GET)
	public String tagForm(Model model,
			@RequestParam(value = "tagId") Long id,
			HttpServletRequest request) {
		if (id == null) {
			id = 0L; 
		}
		Tags tags = tagsService.initTags();
		
		if(id !=null && id > 0){
			tags = tagsService.selectByPrimaryKey(id);
		}
		model.addAttribute("tagVoFormModel", tags);
		return "bs/tagForm";
	}
	
	@AuthPassport
	@RequestMapping(value = "/doTagForm", method = RequestMethod.POST)
	public String doTagForm(HttpServletRequest request, Model model,
			@ModelAttribute("tagVoModel") Tags tags, BindingResult result){
		
		Long id = Long.valueOf(request.getParameter("tagId"));
		//更新或修改	
		if(id!=null && id>0){
			tagsService.updateByPrimaryKeySelective(tags);
		}else {
			tags.setAddTime(TimeStampUtil.getNow()/1000);
			tagsService.insertSelective(tags);
		}
		return "redirect:/bs/tag-list";
	}
	
	/*
	 * 校验标签名称是否重复
	 */
	@RequestMapping(value = "/validTag", method = RequestMethod.GET)
	public void validTagName(PrintWriter out,String name) throws UnsupportedEncodingException{
		
		String names = URLDecoder.decode(name,"utf-8");
		
		TagSearchVo searchVo1 = new TagSearchVo();
		searchVo1.setTagName(names);
		List<Tags> list = tagsService.selectBySearchVo(searchVo1);
		
		if(list.size()>0 && list !=null){
			//如果输入的名称能查出来记录，则返回 名称已存在 标识
			out.write("no");
		}else{
			out.write("yes");
		}
	}
	
}
