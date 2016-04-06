package com.jhj.action.app.survey;

import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.jhj.action.app.BaseController;
import com.jhj.common.Constants;
import com.jhj.po.model.survey.SurveyContent;
import com.jhj.po.model.survey.SurveyContentChild;
import com.jhj.po.model.survey.SurveyUserRefRecommend;
import com.jhj.service.survey.SurveyContentChildService;
import com.jhj.service.survey.SurveyContentService;
import com.jhj.service.survey.SurveyUserRefQuestionService;
import com.jhj.service.survey.SurveyUserRefRecommendService;
import com.jhj.vo.survey.SurveyResultPriceVo;
import com.meijia.utils.OrderNoUtil;
import com.meijia.utils.vo.AppResultData;

/**
 *
 * @author :hulj
 * @Date : 2015年12月25日下午2:17:48
 * @Description: 
 *	
 *   问卷调查--结果处理
 * 	
 * 		 结果入库、后台展示	
 */
@Controller
@RequestMapping(value = "/app/survey")
public class SurveyResultController extends BaseController {
	
	@Autowired
	private SurveyUserRefQuestionService userRefQuestionService;
	
	@Autowired
	private SurveyContentService contentService;
	
	@Autowired
	private SurveyContentChildService contentChildService;
	
	@Autowired
	private SurveyUserRefRecommendService recommendService;
	/**
	 * 
	 *  @Title: getChildContentList
	  * @Description:	
	  * 		点击调整次数, 弹窗显示 该服务的  子服务
	  * 
	  * @param contentId	服务Id
	  * @param 
	  * @return AppResultData<Object>   子服务list
	  * @throws
	 */
	@RequestMapping(value = "get_child_content_list.json",method = RequestMethod.GET)
	public AppResultData<Object> getChildContentList(
			@RequestParam("content_id")Long contentId){
		
		AppResultData<Object> result =  new AppResultData<Object>(Constants.SUCCESS_0, "", "");
		
		if(contentId == null || contentId == 0L){
			result.setStatus(Constants.ERROR_999);
			result.setMsg("服务不存在");
			
			return result;
		}
		
		SurveyContent surveyContent = contentService.selectByPrimaryKey(contentId);
		
		// 该服务有子服务时，发起查询
		if(surveyContent !=null && surveyContent.getContentChildType() != (short)0){
			
			Long id = surveyContent.getContentId();
			
			List<SurveyContentChild> childList = contentChildService.selectByContentId(id);
			
			result.setData(childList);
		}
		
		return result;
	}
	
	/**
	 * @throws JSONException 
	 * 
	 *  @Title: submitSurveyResult
	  * @Description: 
	  * 	
	  * 	提交订制结果,  返回 价格 明细 VO	
	  * 			
	  * @param  userId					用户填写的信息
	  * @param  baseContentArray		无子服务的内容 数组
	  * 			[主服务:次数]
	  * @param  radioContentArray		子服务为单选
	  * 			[{主服务,子服务:次数},...]
	  * @param  boxContentArray			
	  * 			[{主服务,[{子服务:次数}..]}....]
	  * 
	  * @return AppResultData<Object>    返回类型
	  * @throws
	 */
	@RequestMapping(value = "submit_survey_result.json",method = RequestMethod.POST)
	public AppResultData<Object> submitSurveyResult(
				@RequestParam("user_id")Long userId,
				@RequestParam(value = "base_content_array",required = false, defaultValue = "") String baseContentArray,
				@RequestParam(value ="radio_content_array",required = false,defaultValue ="")String radioContentArray,
				@RequestParam(value = "box_content_array",required = false,defaultValue ="")String boxContentArray,
				@RequestParam(value ="default_box_content_array",required = false,defaultValue ="")String defaultBoxContentArray) throws JSONException{
		
		AppResultData<Object> result =  new AppResultData<Object>(Constants.SUCCESS_0, "", "");
		
		/*
		 * 对于 返回 上一页修改 选择结果
		 */
		if(userId == null){
			
			result.setStatus(Constants.ERROR_999);
			result.setMsg("用户不存在");
			return result;
		}else{
			
			List<SurveyUserRefRecommend> list = recommendService.selectByUserId(userId);
			
			/*
			 *  对于 用户 返回修改  定制结果 的情况, 先判断 该用户是否已经 生成了 结果记录
			 *  		如果有，先删除，再插入新的结果
			 */
			if(list.size() > 0 && list !=null){
				recommendService.deleteByUserId(userId);	
			}
		}
		
		/*
		 * 2. 结果入库
		 */
		JSONArray jsonArrayBase = new JSONArray(baseContentArray);
		
		JSONArray jsonArrayRadio = new JSONArray(radioContentArray);
		
		JSONArray jsonArrayBox = new JSONArray(boxContentArray);
		
		//当 为调整  多选的  内容,且 该内容被选中,则拿到 该多选内容 的Id 后，手动得到 子服务并插入表中
		JSONArray jsonArrayDefaultBox = new JSONArray(defaultBoxContentArray);
		
		// {"baseContentId":baseContentId,"baseContentTimes":Number(times)}
		
		//{"baseContentId":baseContentId,"childRadioArray":{childRadioContentId":childRadioContentId,"childRadioContentTimes":Number}
		
		//{"baseContentId":baseContentId,"childBoxArray":{"boxChildContentId":id,"boxChildContentTimes":times};
		
		String resultNo = String.valueOf(OrderNoUtil.genOrderNo());
		
		
		/**
		 * 先处理多选和单选,最后再插入
		 */
		if(jsonArrayRadio !=null && jsonArrayRadio.length() > 0){
			
			for (int i = 0; i < jsonArrayRadio.length(); i++) {
				
				//都存储在这个表即可
				SurveyUserRefRecommend recommend = recommendService.initRecommend();
				recommend.setResultNo(resultNo);
				
				JSONObject jsonObject = jsonArrayRadio.getJSONObject(i);
				
				String baseContentId = jsonObject.getString("baseContentId");
				
				JSONArray childRadioArray = jsonObject.getJSONArray("childRadioArray");
				
				//对于单选题, 该数组 只有一个元素,无需遍历
				JSONObject radioContent = childRadioArray.getJSONObject(0);
				
				String radioContentId = radioContent.getString("childRadioContentId");
				
				String radioContentTimes =  radioContent.getString("childRadioContentTimes");
				
				
				recommend.setUserId(userId);
				recommend.setContentId(Long.valueOf(baseContentId));
				recommend.setContentChildId(Long.valueOf(radioContentId));
				recommend.setTimes(Long.valueOf(radioContentTimes));
				
				recommendService.insertSelective(recommend);
			}
		}
		
		/*
		 * 多选
		 */
		if(jsonArrayBox !=null && jsonArrayDefaultBox !=null){
			
			//未修改多选次数，但选中多选内容
			if(jsonArrayBox.length() <= 0 && jsonArrayDefaultBox.length() > 0){
				
				for (int i = 0; i < jsonArrayDefaultBox.length(); i++) {
					
					
					JSONObject jsonObject = jsonArrayDefaultBox.getJSONObject(i);
					
					String contentId = jsonObject.getString("baseContentId");
					
					
					List<SurveyContentChild> childList = contentChildService.selectByContentId(Long.valueOf(contentId));
					
					for (SurveyContentChild surveyContentChild : childList) {
						
						//都存储在这个表即可
						SurveyUserRefRecommend recommend = recommendService.initRecommend();
						recommend.setUserId(userId);
						recommend.setContentId(Long.valueOf(contentId));
						
						recommend.setResultNo(resultNo);
						recommend.setContentChildId(surveyContentChild.getId());
						
						String childContent = surveyContentChild.getOptionStr();
						
						//通过正则替换, 得到 默认子服务的  次数 
						
						// 如  空调 4次---> 4
						String replaceAll = childContent.replaceAll("\\D+", "");
						
						recommend.setTimes(Long.valueOf(replaceAll));
						
						recommendService.insertSelective(recommend);
					}
				}
			}else{
				
				for (int i = 0; i < jsonArrayBox.length(); i++) {
					
					
					JSONObject jsonObject = jsonArrayBox.getJSONObject(i);
					
					String baseContentId = jsonObject.getString("baseContentId");
					
					
					JSONArray childBoxArray = jsonObject.getJSONArray("childBoxArray");
					//对于多选题。需要遍历
					for (int j = 0; j < childBoxArray.length(); j++) {
						//都存储在这个表即可
						SurveyUserRefRecommend recommend = recommendService.initRecommend();
						recommend.setResultNo(resultNo);
						
						recommend.setUserId(userId);
						recommend.setContentId(Long.valueOf(baseContentId));
						
						
						JSONObject jsonObject2 = childBoxArray.getJSONObject(j);
						
						String boxContentId = jsonObject2.getString("boxChildContentId");
						
						String boxContentTimes = jsonObject2.getString("boxChildContentTimes");
						
						//将每项子服务：次数，分别作为一条记录，插入表中
						recommend.setContentChildId(Long.valueOf(boxContentId));
						recommend.setTimes(Long.valueOf(boxContentTimes));
						
						recommendService.insertSelective(recommend);
					}
				}
			}
			
		}
		
		
		/*
		 * 无子服务
		 */
		
//		//在 主服务中 出现的 服务 id, 如果存储了该服务的 子服务,则 不录入 该服务
//		List<String> baseContentIdList2 = new ArrayList<String>();
		
		if(jsonArrayBase !=null && jsonArrayBase.length() >0){
			for (int i = 0; i < jsonArrayBase.length(); i++) {
				
				JSONObject jsonObject = jsonArrayBase.getJSONObject(i);
				String baseContentId = jsonObject.getString("baseContentId");
				
				String baseContentTimes = jsonObject.getString("baseContentTimes");
				
				//都存储在这个表即可
				SurveyUserRefRecommend recommend = recommendService.initRecommend();
				recommend.setResultNo(resultNo);
				
				recommend.setUserId(userId);
				recommend.setContentId(Long.valueOf(baseContentId));
				recommend.setTimes(Long.valueOf(baseContentTimes));
				
				//如果无 子服务,则子服务Id 默认为 0
				recommendService.insertSelective(recommend);
			}
		}
		
		
		return result;
	}
	
	/**
	 * 
	 *  @Title: getResultPrice
	  * @Description: 
	  * 
	  * 	提交结果后跳转到  价格明细页
	  * 
	  * @param  userId
	  * @return AppResultData<Object>    返回类型
	  * @throws
	 */
	@RequestMapping(value = "get_result_price.json",method = RequestMethod.GET)
	public AppResultData<Object> getResultPrice(
			@RequestParam("user_id") Long userId){
		
		
		AppResultData<Object> result =  new AppResultData<Object>(Constants.SUCCESS_0, "", "");
		
		if(userId == null){
			
			result.setStatus(Constants.ERROR_999);
			result.setMsg("用户不存在");
			return result;
		}
		
		Map<Short, SurveyResultPriceVo> map = recommendService.getResultPrice(userId);
		
		result.setData(map);
		
		return result;
		
	}
	
	
}	
