package com.jhj.action.survey;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
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
		model.addAttribute("contentFormModel", content);
		
		return "survey/contentForm";
	}
	
	/**
	 * @throws JSONException 
	 * 表单提交
	 *  @Title: submitContentForm
	  * @Description: 
	  * 
	  * 
	  * @param contentId			服务id,判断是 新增还是 修改
	  * @param serviceId			服务大类
	  * @param name					服务名称
	  * @param price				服务价格（/次）
	  * @param priceDescription		价格描述（冗余，没多大用）
	  * @param defaultTime			服务默认次数	
	  * @param contentChildType		子服务的类型。单选=1，多选=2，或者没有子服务=0
	  * @param optionList			
	  * 
	  * 			[{"defaultTimeChild":xx,"optionStr":xx,"childPrice":xx},...]
	  * 			
	  * 		==	[{"子服务默认次数":xx,"子服务内容":xx,"子服务单价":xx}]
	  * @param measurement			计费方式   月=0  年=1 次=2 3=赠送  
	  * @param enable				是否可用   否=0  是=1
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
			@RequestParam(value="defaultTime",required=false,defaultValue="0")Long defaultTime,
			@RequestParam("contentChildType")Short contentChildType,
			@RequestParam(value="optionArray",required = false,defaultValue="")String optionArray,
			@RequestParam("measurement")Short measurement,
			@RequestParam("enable")Short enable) throws JSONException{
		
		AppResultData<Object> result = new AppResultData<Object>(Constants.SUCCESS_0, "", "");
		
		
		/*
		 * 1. 设置  服务 相关
		 * 		
		 * 		其中, 对于  没有子服务的 服务 ，默认次数为  defaultTime的 值
		 * 		
		 * 		对于有子服务，则 defaultTime为 0, 子服务次数在 json数组（optionArray参数）中
		 * 		设置到  surveyContentChild 对象的 defaultTimeChild中
		 * 	
		 */
		SurveyContent surveyContent = contentService.initContent();
		//如果内容id为0,表示为新增
		if(contentId == 0L){
			
			surveyContent.setServiceId(serviceId);
			surveyContent.setName(name);
			surveyContent.setPrice(price);
			surveyContent.setPriceDescription(priceDescription);
			surveyContent.setContentChildType(contentChildType);
			
			surveyContent.setMeasurement(measurement);
			surveyContent.setEnable(enable);
			surveyContent.setDefaultTime(defaultTime);
			
			contentService.insertSelective(surveyContent);
			
		}else{
			
			surveyContent = contentService.selectByPrimaryKey(contentId);
			
			surveyContent.setServiceId(serviceId);
			surveyContent.setName(name);
			surveyContent.setPrice(price);
			surveyContent.setPriceDescription(priceDescription);
			surveyContent.setContentChildType(contentChildType);
			surveyContent.setMeasurement(measurement);
			surveyContent.setEnable(enable);
			
			surveyContent.setDefaultTime(defaultTime);
			
			contentService.updateByPrimaryKeySelective(surveyContent);
			
			contentChildService.deleteByContentId(contentId);
		}
		
		
		/*
		 *  子服务为 单选题类型（次数可选择，如除尘除螨 口4次   口6次  口8次）
		 *      为多选类型（家电清洗，空调1次，擦玻璃2次、、、、）
		 */
		if(contentChildType == (short)1 || contentChildType == (short)2){
			
			JSONArray jsonArray = new JSONArray(optionArray);
			
			for (int i = 0; i < jsonArray.length(); i++) {
				
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				
				String defaultTimeChild = jsonObject.getString("defaultTimeChild");
				String optionStr = jsonObject.getString("optionStr");
				String childPrice = jsonObject.getString("childPrice");
				
				SurveyContentChild contentChild = contentChildService.initContentChild();
				
				Long contentId2 = surveyContent.getContentId();
				
				contentChild.setContentId(contentId2);
				contentChild.setDefaultTimeChild(Long.valueOf(defaultTimeChild));
				contentChild.setOptionStr(optionStr);
				contentChild.setChildPrice(new BigDecimal(childPrice));
				
				contentChildService.insertSelective(contentChild);
			}
			
		}
		
		result.setMsg("success");
		
		return result;
	}
	
	
	
	
	
}
