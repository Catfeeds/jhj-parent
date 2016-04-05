package com.jhj.action.survey;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jhj.action.BaseController;
import com.jhj.common.ConstantOa;
import com.jhj.common.Constants;
import com.jhj.po.model.survey.SurveyContent;
import com.jhj.po.model.survey.SurveyContentChild;
import com.jhj.service.survey.SurveyContentChildService;
import com.jhj.service.survey.SurveyContentService;
import com.jhj.service.survey.SurveyQuestionServcie;
import com.jhj.vo.survey.SurveyServiceContentVo;
import com.meijia.utils.vo.AppResultData;

/**
 *
 * @author :hulj
 * @Date : 2015年12月12日上午10:39:12
 * @Description: 
 *		
 *		问卷调查--服务内容管理
 */
@Controller
@RequestMapping(value = "/survey")
public class SurveyContentController extends BaseController {
	
	@Autowired
	private SurveyContentService contentService;
	@Autowired
	private SurveyContentChildService contentChildService;
	@Autowired
	private SurveyQuestionServcie questionService;
	
	@RequestMapping(value  ="content_list",method = RequestMethod.GET)
	public String contentList(HttpServletRequest request,Model model){
		
		int pageNo = ServletRequestUtils.getIntParameter(request,
				ConstantOa.PAGE_NO_NAME, ConstantOa.DEFAULT_PAGE_NO);
		int pageSize = ServletRequestUtils.getIntParameter(request,
				ConstantOa.PAGE_SIZE_NAME, ConstantOa.DEFAULT_PAGE_SIZE);
		//分页
		PageHelper.startPage(pageNo, pageSize);
		
		List<SurveyContent> list = contentService.selectByListPage();
		
		SurveyContent content = null;
		
		for (int i = 0; i < list.size(); i++) {
			content = list.get(i);
			
			SurveyServiceContentVo completeVo = contentService.completeVo(content);
			
			list.set(i, completeVo);
		}
		
		PageInfo result = new PageInfo(list);	
		
		model.addAttribute("contentListVoModel", result);
		
		return "survey/contentList";
		
	}
	
	/*
	 * 跳转到 form页
	 */
	@RequestMapping(value = "content_form",method = RequestMethod.GET)
	public String contentForm(HttpServletRequest request, Model model,
					@RequestParam("id") Long id){
								
		if(id == null){
			id = 0L;
		}
		
		SurveyContent content = contentService.initContent();
		
		
		List<SurveyContentChild> list = new ArrayList<SurveyContentChild>();
		
		if( id !=null && id >0L){
			content = contentService.selectByPrimaryKey(id);
			
			//填空题
//			if(){
				
//			}
			
			// 子服务是  单选题 和多选题 ，都进行回显
			if(content.getContentChildType() == (short)1 || content.getContentChildType() == (short)2){
				
				list = contentChildService.selectByContentId(id);
				
				
			}
			
		}
		
		model.addAttribute("childList", list);
//		model.addAttribute("childDescription", content.getContentChildDescription());
		model.addAttribute("contentFormModel", content);
		
		return "survey/contentForm";
	}
	
	/**
	 * 表单提交
	 *  @Title: submitContentForm
	  * @Description: TODO
	  * @param @param contentId
	  * @param @param serviceId
	  * @param @param name
	  * @param @param price
	  * @param @param priceDescription
	  * @param @param description
	  * @param @param contentChildType
	  * @param @param contentChildDescription
	  * @param @param optionList
	  * @param @param measurement
	  * @param @param enable
	  * @param @return    设定文件
	  * @return AppResultData<Object>    返回类型
	  * @throws
	 */
	@RequestMapping(value = "content_form",method = RequestMethod.POST)
	public AppResultData<Object> submitContentForm(
			@RequestParam("contentId")Long contentId,
			@RequestParam("serviceId")Long serviceId,
			@RequestParam("name")String name,
			@RequestParam("price")BigDecimal price,
			@RequestParam(value="priceDescription",required=false,defaultValue="")String priceDescription,
			@RequestParam(value="description",required=false,defaultValue="")String description,
			@RequestParam("contentChildType")Short contentChildType,
			@RequestParam(value="contentChildDescription",required = false,defaultValue="")String contentChildDescription,
			@RequestParam(value="optionArray",required = false,defaultValue="")List<String> optionList,
			@RequestParam("measurement")Short measurement,
			@RequestParam("enable")Short enable){
		
		AppResultData<Object> result = new AppResultData<Object>(Constants.SUCCESS_0, "", "");
		
		
		SurveyContent surveyContent = contentService.initContent();
		//如果内容id为0,表示为新增
		if(contentId == 0L){
			
			surveyContent.setServiceId(serviceId);
			surveyContent.setName(name);
			surveyContent.setPrice(price);
			surveyContent.setPriceDescription(priceDescription);
//			surveyContent.setDescription(description);
			surveyContent.setContentChildType(contentChildType);
			
			//如果是 填空题形式（次数可修改，如家电清洗：空调1次。。。次数可修改）
//			if(contentChildType == (short)2){
//				surveyContent.setContentChildDescription(contentChildDescription);
//			}
			
			surveyContent.setMeasurement(measurement);
			surveyContent.setEnable(enable);
			
			
			contentService.insertSelective(surveyContent);
			
		}else{
			
			surveyContent = contentService.selectByPrimaryKey(contentId);
			
			surveyContent.setServiceId(serviceId);
			surveyContent.setName(name);
			surveyContent.setPrice(price);
			surveyContent.setPriceDescription(priceDescription);
//			surveyContent.setDescription(description);
			surveyContent.setContentChildType(contentChildType);
			
//			//如果是 填空题形式（次数可修改，如家电清洗：空调1次。。。次数可修改）
//			if(contentChildType == (short)2){
//				surveyContent.setContentChildDescription(contentChildDescription);
//			}
			
			surveyContent.setMeasurement(measurement);
			surveyContent.setEnable(enable);
			
			contentService.updateByPrimaryKeySelective(surveyContent);
			
			contentChildService.deleteByContentId(contentId);
			
			
		}
		
		
		/*
		 *  子服务为 单选题类型（次数可选择，如除尘除螨 口4次   口6次  口8次）
		 *      为多选类型（家电清洗，空调1次，擦玻璃2次、、、、）
		 */
		if(contentChildType == (short)1 || contentChildType == (short)2){
			
			SurveyContentChild contentChild = contentChildService.initContentChild();
			
			/**
			 * 通用方法，为选项生成 序号,并返回  key,val 键值对
			 */
			Map<String, String> map = questionService.generateNoForOption(optionList);
			
			//插入数据库后的 自增 主键
			Long contentId2 = surveyContent.getContentId();
			contentChild.setContentId(contentId2);
			
			
			
			for (Map.Entry<String, String> entry : map.entrySet()) {
				
				contentChild.setOptionNo(entry.getKey());
				contentChild.setOptionStr(entry.getValue());
				
				contentChildService.insertSelective(contentChild);
			} 
			
		}
		
		
		result.setMsg("success");
		
		return result;
	}
	
	
	
	
	
}
