package com.jhj.action.survey;

import java.util.ArrayList;
import java.util.HashMap;
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
import com.jhj.po.model.survey.SurveyUser;
import com.jhj.po.model.survey.SurveyUserRefRecommend;
import com.jhj.service.survey.SurveyContentChildService;
import com.jhj.service.survey.SurveyContentService;
import com.jhj.service.survey.SurveyUserRefRecommendService;
import com.jhj.service.survey.SurveyUserService;

/**
 *
 * @author :hulj
 * @Date : 2015年12月31日上午11:16:12
 * @Description: 
 * 
 * 	运营平台--问卷调查 结果展示
 */
@Controller
@RequestMapping(value = "/survey")
public class OaSurveyUserResultController extends BaseController {
	
	@Autowired
	private SurveyUserRefRecommendService recommendService;
	
	@Autowired
	private SurveyUserService userService;
	
	@Autowired
	private SurveyContentService contentService;
	
	@Autowired
	private SurveyContentChildService childService;
	
	@RequestMapping(value = "all_survey_user_list",method = RequestMethod.GET)
	public String  getSurveyUserList(Model model,HttpServletRequest request){
		
		int pageNo = ServletRequestUtils.getIntParameter(request,
				ConstantOa.PAGE_NO_NAME, ConstantOa.DEFAULT_PAGE_NO);
		int pageSize = ServletRequestUtils.getIntParameter(request,
				ConstantOa.PAGE_SIZE_NAME, ConstantOa.DEFAULT_PAGE_SIZE);
		
		
		List<Long> list = recommendService.selectByListPage();
		//分页
		PageHelper.startPage(pageNo, pageSize);
		List<SurveyUser> userList  = new ArrayList<SurveyUser>();
		
		if(list != null && list.size() > 0){
			userList = userService.selectByIdList(list);
		}
		
		PageInfo result = new PageInfo(userList);	
		
		model.addAttribute("surveyUserListModel", result);
		
		return "survey/userList";
	}
	
	/*
	 *  用户调查结果展示页面
	 */
	@RequestMapping(value = "user_result_detail",method = RequestMethod.GET)
	public String userResultDetail(Model model,@RequestParam("id") Long userId){
		
		//用户信息
		SurveyUser user = userService.selectByPrimaryKey(userId);
		
		
		List<SurveyUserRefRecommend> list = recommendService.selectByUserId(userId);
		
		//用户选择的所有 服务Id,不包含免费服务（数据库未存储免费服务,默认全选）
		List<Long> allIdList = new ArrayList<Long>();
		
		
		//用户选择的 服务中  包含的 子服务,子服务Id,及其次数
		Map<Long, Long> boxChildMap = new HashMap<Long, Long>();
		
		for (SurveyUserRefRecommend surveyUserRefRecommend : list) {
			
			Long contentId = surveyUserRefRecommend.getContentId();
			
			
			if(surveyUserRefRecommend.getContentChildId() != 0){
				boxChildMap.put(surveyUserRefRecommend.getContentChildId(), surveyUserRefRecommend.getTimes());
			}else{
				//不包含子服务的 服务
				allIdList.add(contentId);
				
			}
		}
		
		//按月 计费的所有服务
		List<Long> monthIdList = contentService.selectContentIdByMeasurement(Constants.SURVEY_MEARSUREMENT_0);
		//按年 计费的所有服务
		List<Long> yearIdList = contentService.selectContentIdByMeasurement(Constants.SURVEY_MEARSUREMENT_1);
		//按次 计费的所有服务
		List<Long> timeIdList = contentService.selectContentIdByMeasurement(Constants.SURVEY_MEARSUREMENT_2);	
		
		
		
		
//		//1.先把 用户选择结果中的   子服务是 多选的 服务， 移除, 单独统计该类型服务的价格
		
		/*
		 * 2. 使用  retainAll()方法,对id求 交集,得到 用户选择 的服务 的id（按计费方式分类） 
		 */
		
		// 2-1 月付费, 需要和 按年 付费的 累加
		monthIdList.retainAll(allIdList);
		
		List<SurveyContent> monthContent = contentService.selectByIdList(monthIdList);
		
		
		//按月计费的 服务及其对应的 次数
		Map<SurveyContent, Long> monthContentMap = new HashMap<SurveyContent, Long>();
		
		for (SurveyContent surveyContent : monthContent) {
			
			//得到 该用户、该服务，按年支付的 价格
			SurveyUserRefRecommend recommend = recommendService.selectByUserIdAndContentId(userId, surveyContent.getContentId());
			
			//按月支付的
			Long contentTime = recommend.getTimes();
			
			/*
			 * model封装 <服务:次数>
			 */
			monthContentMap.put(surveyContent, contentTime);
			
			
		}
		
		// 2-2 年付费
		yearIdList.retainAll(allIdList);
		
		List<SurveyContent> yearContent = contentService.selectByIdList(yearIdList);
		
		
		//按年计费的 服务及其对应的 次数
		Map<SurveyContent, Long> yearContentMap = new HashMap<SurveyContent, Long>();
		
		for (SurveyContent surveyContent : yearContent) {
			
			SurveyUserRefRecommend recommend = recommendService.selectByUserIdAndContentId(userId, surveyContent.getContentId());
			
			Long contentTime = recommend.getTimes();
			
			/*
			 * model封装 <服务:次数>
			 */
			yearContentMap.put(surveyContent, contentTime);
			
		}
		
		//2-3 
		timeIdList.retainAll(allIdList);
		
		List<SurveyContent> timeContent = contentService.selectByIdList(timeIdList);
		
		//按次计费的 服务及其对应的 次数
		Map<SurveyContent, Long> timeContentMap = new HashMap<SurveyContent, Long>();
		
		for (SurveyContent surveyContent : timeContent) {
			
			SurveyUserRefRecommend recommend = recommendService.selectByUserIdAndContentId(userId, surveyContent.getContentId());
			Long contentTime = recommend.getTimes();
			
			/*
			 * model封装 <服务:次数>
			 */
			timeContentMap.put(surveyContent, contentTime);
			
		}
		
		
		//服务内容及次数
		model.addAttribute("monthContent", monthContentMap);
		model.addAttribute("yearContent", yearContentMap);
		model.addAttribute("timeContent", timeContentMap);
		model.addAttribute("childContent",boxChildMap);
		//用户信息
		model.addAttribute("userInfoModel", user);
		
		
		
		return "survey/resultDetail";
	}
}
