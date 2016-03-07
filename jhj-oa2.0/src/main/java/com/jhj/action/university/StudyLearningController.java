package com.jhj.action.university;

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
import com.jhj.po.model.university.StudyLearning;
import com.jhj.service.university.StudyLearningService;
import com.jhj.vo.university.StudyVo;
import com.meijia.utils.BeanUtilsExp;

/**
 *
 * @author :hulj
 * @Date : 2015年12月3日下午2:30:17
 * @Description: 
 *		
 *		叮当大学--培训学习
 */
@Controller
@RequestMapping(value = "/university")
public class StudyLearningController extends BaseController {

	@Autowired
	private StudyLearningService studyService;
	
	@RequestMapping(value = "study_list",method = RequestMethod.GET)
	public  String studyList(Model model,HttpServletRequest request){
		
		int pageNo = ServletRequestUtils.getIntParameter(request,
				ConstantOa.PAGE_NO_NAME, ConstantOa.DEFAULT_PAGE_NO);
		int pageSize = ServletRequestUtils.getIntParameter(request,
				ConstantOa.PAGE_SIZE_NAME, ConstantOa.DEFAULT_PAGE_SIZE);
		//分页
		PageHelper.startPage(pageNo, pageSize);
		
		
		List<StudyLearning> list = studyService.selectByListPage();
		
		
        
        StudyLearning studyLearning = null;
		for (int i = 0; i < list.size(); i++) {
			studyLearning = list.get(i);
			StudyVo completeVo = studyService.completeVo(studyLearning);
			list.set(i, completeVo);
		}
		
		PageInfo result = new PageInfo(list);	
		
		
		model.addAttribute("studyVoListModel", result);
		
		return "university/studyList";
	}
	
	/*
	 * 跳转到 form页
	 */
	@RequestMapping(value = "/study_form",method = RequestMethod.GET)
	public String degreeTypeForm(HttpServletRequest request, Model model,
					@RequestParam("id") Long id){
								
		if(id == null){
			id = 0L;
		}
		
		
		StudyLearning studyLearning = studyService.initStudyLearning();
		
		if( id !=null && id >0L){
			studyLearning = studyService.selectByPrimaryKey(id);
			
			
		}
		
		model.addAttribute("studyFormModel", studyLearning);
		
		return "university/studyForm";
	}
	
	/*
	 * 提交form
	 */
	@RequestMapping(value = "/study_form", method = RequestMethod.POST)
	public String submitDegreeForm(@ModelAttribute("studyFormModel") StudyVo vo,
			BindingResult result){
		
		StudyLearning studyLearning = studyService.initStudyLearning();
		
		//参数 id
		Long id = vo.getId();
		if(id == 0L){
			
			BeanUtilsExp.copyPropertiesIgnoreNull(vo, studyLearning);
			
			studyService.insertSelective(studyLearning);
			
		}else{
			
			StudyLearning learning = studyService.selectByPrimaryKey(id);
			
			BeanUtilsExp.copyPropertiesIgnoreNull(vo, learning);
			
			studyService.updateByPrimaryKeySelective(learning);
			
		}
		
		return "redirect:study_list";
	}
	
}
