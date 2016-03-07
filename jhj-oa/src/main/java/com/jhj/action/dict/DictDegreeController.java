package com.jhj.action.dict;

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
import com.jhj.po.model.dict.DictDegreeType;
import com.jhj.service.dict.DictDegreeTypeService;
import com.meijia.utils.TimeStampUtil;

/**
 *
 * @author :hulj
 * @Date : 2015年11月26日下午6:07:34
 * @Description: 
 *		
 *		运营平台--基础配置--助理类型分类管理
 *
 */
@Controller
@RequestMapping(value = "/base")
public class DictDegreeController extends BaseController {
	
	@Autowired
	private DictDegreeTypeService degreeService;
	
	@RequestMapping(value = "/degree_type_list",method = RequestMethod.GET)
	public String degreeTypeList(Model model,HttpServletRequest request){
		
		int pageNo = ServletRequestUtils.getIntParameter(request,
				ConstantOa.PAGE_NO_NAME, ConstantOa.DEFAULT_PAGE_NO);
		int pageSize = ServletRequestUtils.getIntParameter(request,
				ConstantOa.PAGE_SIZE_NAME, ConstantOa.DEFAULT_PAGE_SIZE);
		
		//分页
		PageHelper.startPage(pageNo, pageSize);
		
		List<DictDegreeType> list = degreeService.selectByListPage(pageNo, pageSize);
		
		PageInfo result = new PageInfo(list);	
		
		model.addAttribute("degreeTypeModel",result);
		
		
		return "dict/degreeType/degreeTypeList";
	}
	
	/*
	 * 跳转到 form页
	 */
	@RequestMapping(value = "/degree_type_form",method = RequestMethod.GET)
	public String degreeTypeForm(HttpServletRequest request, Model model,
					@RequestParam("id") Long id){
		
		if(id == null){
			id = 0L;
		}
		
		DictDegreeType degreeType = degreeService.initDegreeType();
		
		if( id !=null && id >0L){
			degreeType = degreeService.selectByPrimaryKey(id);
		}
		
		model.addAttribute("degreeTypeFormModel", degreeType);
		
		return "dict/degreeType/degreeTypeForm";
	}
	
	/*
	 * 提交form
	 */
	@RequestMapping(value = "/degree_type_form", method = RequestMethod.POST)
	public String submitDegreeForm(@ModelAttribute("degreeTypeFormModel") DictDegreeType degreeType,
			BindingResult result){
		
		DictDegreeType dictDegreeType = degreeService.initDegreeType();
		
		if(degreeType.getId() == 0L){
			dictDegreeType.setName(degreeType.getName());
			dictDegreeType.setEnable(degreeType.getEnable());
			
			degreeService.insertSelective(dictDegreeType);
		}else{
			
			dictDegreeType = degreeService.selectByPrimaryKey(degreeType.getId());
			
			dictDegreeType.setName(degreeType.getName());
			dictDegreeType.setEnable(degreeType.getEnable());
			dictDegreeType.setUpdateTime(TimeStampUtil.getNowSecond());
			
			degreeService.updateByPrimaryKeySelective(dictDegreeType);
		}
		
		return "redirect:degree_type_list";
	}
	
}
