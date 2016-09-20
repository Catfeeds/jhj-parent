package com.jhj.service.impl.survey;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jhj.common.Constants;
import com.jhj.po.dao.survey.SurveyUserRefRecommendMapper;
import com.jhj.po.model.survey.SurveyContent;
import com.jhj.po.model.survey.SurveyContentChild;
import com.jhj.po.model.survey.SurveyUserRefRecommend;
import com.jhj.service.survey.SurveyContentChildService;
import com.jhj.service.survey.SurveyContentService;
import com.jhj.service.survey.SurveyUserRefRecommendService;
import com.jhj.vo.survey.AppSurveyResultPriceVo;
import com.jhj.vo.survey.OaSurveyUserResultVo;
import com.jhj.vo.survey.SurveyResultPriceVo;
import com.meijia.utils.BeanUtilsExp;
import com.meijia.utils.MathBigDecimalUtil;
import com.meijia.utils.OneCareUtil;
import com.meijia.utils.OrderNoUtil;
import com.meijia.utils.TimeStampUtil;

@Service
public class SurveyUserRefRecommendImpl implements SurveyUserRefRecommendService {
	
	@Autowired
	private SurveyUserRefRecommendMapper recommendMapper;
	
	@Autowired
	private SurveyContentService contentService;
	
	@Autowired
	private SurveyContentChildService childService;
	
	
	@Override
	public int deleteByPrimaryKey(Long id) {
		return recommendMapper.deleteByPrimaryKey(id);
	}

	@Override
	public int insertSelective(SurveyUserRefRecommend record) {
		return recommendMapper.insertSelective(record);
	}

	@Override
	public SurveyUserRefRecommend selectByPrimaryKey(Long id) {
		return recommendMapper.selectByPrimaryKey(id);
	}

	@Override
	public int updateByPrimaryKeySelective(SurveyUserRefRecommend record) {
		return recommendMapper.updateByPrimaryKeySelective(record);
	}

	@Override
	public SurveyUserRefRecommend initRecommend() {
		
		SurveyUserRefRecommend recommend = new  SurveyUserRefRecommend();
		
//		String resultNo = String.valueOf(OrderNoUtil.genOrderNo());
		
		recommend.setId(0L);
		recommend.setUserId(0L);
		recommend.setContentId(0L);
		recommend.setTimes(0L);
		recommend.setAddTime(TimeStampUtil.getNowSecond());
		
		recommend.setResultNo("");
		
		recommend.setContentChildId(0L); 
		
		return recommend;
	}

	@Override
	public List<Long> selectByListPage() {
		return recommendMapper.selectUserIdByListPage();
	}

	
	@Override
	public List<List<SurveyUserRefRecommend>> selectByResultNo(List<String> flagList) {
		return recommendMapper.selectByResultNo(flagList);
	}

	@Override
	public List<SurveyUserRefRecommend> selectByUserId(Long userId) {
		return recommendMapper.selectByUserId(userId);
	}

//	@Override
//	public OaSurveyUserResultVo transLateToVo(SurveyUserRefRecommend recommend) {
//		
//		OaSurveyUserResultVo resultVo = initOaResultVo();
//		
//		Map<Short, Map<String, BigDecimal>> map = 
//					new HashMap<Short, Map<String,BigDecimal>>();
//		
//		BigDecimal yearPrice = new BigDecimal(0);
//		
//		BigDecimal monthPrice = new BigDecimal(0);
//		
//		BigDecimal timePrice = new BigDecimal(0);
//		
//		Map<String, BigDecimal> contentMap = new HashMap<String, BigDecimal>();
//		
//		
//		Long childId = recommend.getContentChildId();
//		
//		/*
//		 * 分析:
//		 * 	  1.若 childId = 0 ,表示该服务没有子服务,times 表示该服务的 次数
//		 * 		
//		 * 	  2. childId != 0 , times 表示该子服务的次数	
//		 */
//		
//		//可以转换为  年的 服务
//		
//		if(childId == 0){
//		}
//		
//		//可以转换为 次的服务
//		return null;
//	}

	@Override
	public OaSurveyUserResultVo initOaResultVo() {
		
		SurveyUserRefRecommend recommend = initRecommend();
		
		OaSurveyUserResultVo resultVo = new OaSurveyUserResultVo();
		
		BeanUtilsExp.copyPropertiesIgnoreNull(recommend, resultVo);
		
		resultVo.setMap(new HashMap<Short, Map<String,BigDecimal>>());
		resultVo.setYearPrice(new BigDecimal(0));
		resultVo.setMonthPrice(new BigDecimal(0));
		resultVo.setTimePrice(new BigDecimal(0));
		
		return resultVo;
	}
	
	@Override
	public SurveyUserRefRecommend selectByUserIdAndContentId(Long userId, Long contentId) {
		return recommendMapper.selectByUserIdAndContentId(userId, contentId);
	}
	
	
	/*
	 * 微网站 提交 调查结果,返回价格条目信息
	 */
	@Override
	public Map<Short, SurveyResultPriceVo> getResultPrice(Long userId) {
		
		//最终的返回结果
		Map<Short, SurveyResultPriceVo> map = new HashMap<Short, SurveyResultPriceVo>();
		
		List<SurveyUserRefRecommend> list = selectByUserId(userId);
		
		//用户选择的所有 服务Id  --> contentId   ,不包含免费服务（数据库未存储免费服务,默认全选）
		List<Long> contentIdList = new ArrayList<Long>();
		
		
		//1 . 用户选择的 服务中  包含的 子服务,子服务Id,及其次数
		
		//用来存储  <服务Id,<子服务Id:服务次数>>
		
		Map<Long, Long> boxChildMap = new HashMap<Long, Long>();
		
		for (SurveyUserRefRecommend surveyUserRefRecommend : list) {
			
			Long contentId = surveyUserRefRecommend.getContentId();
			
			
			if(surveyUserRefRecommend.getContentChildId() != 0){
				boxChildMap.put(surveyUserRefRecommend.getContentChildId(), surveyUserRefRecommend.getTimes());
				
			}else{
				//不包含子服务的 服务
				contentIdList.add(contentId);
			}
		}
		
		//按月 计费的所有服务
		List<Long> monthIdList = contentService.selectContentIdByMeasurement(Constants.SURVEY_MEARSUREMENT_0);
		
		//所有的基础家政服务 id
		List<Long> baseContentIdList = contentService.selectBaseContent();
		
		//所有服务中  的基础家政 服务
		baseContentIdList.retainAll(contentIdList);
		
		List<SurveyContent> baseContentList = contentService.selectByIdList(baseContentIdList);
		
		//所有服务
		List<SurveyContent> allContentList = contentService.selectByIdList(contentIdList);
		
		/*
		 * 计算公式
		 * 		都折合为 “年” （月*12+年+次）   消费额后
		 * 
		 * 		 A=基础家政3项服务金额 
		 *       B=助理服务+深度保洁金额		
		 * 		 C=合计总金额=A+B		
		 *			
		 *		k：优惠系数,   B/C
		 * 
		 * 	  	 D： 优惠后总价格=A+（B×K）
		 */
		
		//2. 总价 = 无子服务 的价格+ 子服务的价格
		
		//无子服务的价格
		BigDecimal sumContentPrice = getSumPriceForContentList(allContentList, userId);
		
		//子服务总价
		BigDecimal childSumPrice = new BigDecimal(0);
		
		
		Map<Long, BigDecimal> childMap = calculateChildContentPrice(boxChildMap);
		
		Set<Entry<Long,BigDecimal>> entrySet = childMap.entrySet();
		
		for (Entry<Long, BigDecimal> entry : entrySet) {
			
			Long contentId = entry.getKey();
			
			BigDecimal childPrice = entry.getValue();
			
			if(monthIdList.contains(contentId)){
				
				//如果子服务所属服务是 月计费服务， 则该 价格 *12, 并累加
				childSumPrice = childSumPrice.add(childPrice.multiply(new BigDecimal(12)));
			}else{
				
				//如果子服务所属服务是 年、次 计费服务，则直接累加
				childSumPrice = childSumPrice.add(childPrice);
			}
		}
		
		//最终总价
		BigDecimal sumPrice = MathBigDecimalUtil.add(sumContentPrice, childSumPrice);
		
		
		/*
		 * 2016年3月14日11:37:58     不再根据权重计算优惠系数，优惠系数统一 
		 * 	
		 * 		 优惠幅度为年付 0.85 半年付 0.9 月付 0.95
		 * 
		 */
		
		
		
//		//基础家政价格
//		BigDecimal basePrice = getSumPriceForContentList(baseContentList, userId);
//		
//		//助理 和  深度  价格之和 = 总价 - 基础家政
//		BigDecimal amAndDeepPrice = MathBigDeciamlUtil.sub(sumPrice, basePrice);
//		
//		//3.得到 优惠系数, !! 已保留两位小数
//		
//		/*
//		 * 按年支付
//		 */
//		String yearK = OneCareUtil.getSurveyRatio(sumPrice, amAndDeepPrice, Constants.SURVEY_PAY_TYPE_0);
//		
//		/*
//		 * 按 半年支付
//		 */
//		String quarterK = OneCareUtil.getSurveyRatio(sumPrice, amAndDeepPrice, Constants.SURVEY_PAY_TYPE_1);
//		
//		/*
//		 * 按 月支付
//		 */
//		String monthK = OneCareUtil.getSurveyRatio(sumPrice, amAndDeepPrice, Constants.SURVEY_PAY_TYPE_2);
		
		
		//4. 构造 返回结果
		
		//年优惠总价
//		BigDecimal ratioSumPriceYear = basePrice.add(amAndDeepPrice.multiply(new BigDecimal(yearK)));
		
		BigDecimal ratioSumPriceYear = sumPrice.multiply(new BigDecimal(0.85));
		
		//平均每月需支付的金额, 保留两位小数
		BigDecimal divByYear = MathBigDecimalUtil.div(ratioSumPriceYear, new BigDecimal(12), 2);
		
		SurveyResultPriceVo priceVoYear = new SurveyResultPriceVo();
		
		priceVoYear.setSurveyPayType(Constants.SURVEY_PAY_TYPE_0);
		priceVoYear.setSumPrice(sumPrice);
		priceVoYear.setDiscountPrice(MathBigDecimalUtil.saveTwoDigital(ratioSumPriceYear));
		priceVoYear.setPriceByMonth(divByYear);
		
		//按年计费
		map.put(Constants.SURVEY_PAY_TYPE_0, priceVoYear);
		
		
		//半年优惠总价
//		BigDecimal ratioSumPriceQuarter = basePrice.add(amAndDeepPrice.multiply(new BigDecimal(quarterK)));
		
		BigDecimal ratioSumPriceQuarter = sumPrice.multiply(new BigDecimal(0.9));
		
		//平均每月需支付的金额
		BigDecimal divByQuarter = MathBigDecimalUtil.div(ratioSumPriceQuarter, new BigDecimal(12), 2);
		
		SurveyResultPriceVo priceVoQuarter = new SurveyResultPriceVo();
		
		priceVoQuarter.setSurveyPayType(Constants.SURVEY_PAY_TYPE_1);
		priceVoQuarter.setSumPrice(sumPrice);
		priceVoQuarter.setDiscountPrice(MathBigDecimalUtil.saveTwoDigital(ratioSumPriceQuarter));
		priceVoQuarter.setPriceByMonth(divByQuarter);
		
		//按半年计费
		map.put(Constants.SURVEY_PAY_TYPE_1, priceVoQuarter);
		
		//月优惠总价
//		BigDecimal ratioSumPriceMonth = basePrice.add(amAndDeepPrice.multiply(new BigDecimal(monthK)));
		
		BigDecimal ratioSumPriceMonth =  sumPrice.multiply(new BigDecimal(0.95));
		
		//平均每月需支付的金额
		BigDecimal divByMonth = MathBigDecimalUtil.div(ratioSumPriceMonth, new BigDecimal(12), 2);
		
		SurveyResultPriceVo priceVoMonth = new SurveyResultPriceVo();
		
		priceVoMonth.setSurveyPayType(Constants.SURVEY_PAY_TYPE_2);
		priceVoMonth.setSumPrice(sumPrice);
		priceVoMonth.setDiscountPrice(MathBigDecimalUtil.saveTwoDigital(ratioSumPriceMonth));
		priceVoMonth.setPriceByMonth(divByMonth);
		
		//按月计费
		map.put(Constants.SURVEY_PAY_TYPE_2, priceVoMonth);
		
		return map;
	}
	
	
	
	/**
	 * 
	 *  @Title: getSumPriceForContentList
	  * @Description: 
	  * 
	  *    该用户选择中 ,某些服务（同类型）的  总价
	  * 
	  * @param @param list	
	  * @param @param userId
	  * @return BigDecimal    返回类型
	  * @throws
	 */
	private BigDecimal getSumPriceForContentList(List<SurveyContent> list, Long userId){
		
		BigDecimal sumPrice = new BigDecimal(0);
		
		if(list.size() > 0 && list !=null){
			
			for (SurveyContent surveyContent : list) {
				//得到 该用户、该服务，按年支付的 价格
				SurveyUserRefRecommend recommend = selectByUserIdAndContentId(userId, surveyContent.getContentId());
				
				//服务次数
				Long contentTime = recommend.getTimes();
				
				//该服务单价
				BigDecimal price = surveyContent.getPrice();
				
				/*
				 * 按年统计总价
				 */
				Short measurement = surveyContent.getMeasurement();
				
				Long contentTimeYear = 0L;
				// 按月支付的  次数*12，  其余 按次支付 和 按年支付，次数都默认按年
				
				if(measurement == Constants.SURVEY_MEARSUREMENT_0){
					contentTimeYear = contentTime*12;
				}else if(measurement == Constants.SURVEY_MEARSUREMENT_1){
					contentTimeYear = contentTime;
				}else if(measurement == Constants.SURVEY_MEARSUREMENT_2){
					contentTimeYear = contentTime;
				}
				
				//累加得到 按月 付费的  服务的总价
				sumPrice = sumPrice.add(price.multiply(new BigDecimal(contentTimeYear)));
			}
		}
		return sumPrice;
	}
	
	/*
	 *  计算 用户 的 子服务 的 价格
	 *   
	 */
	private Map<Long, BigDecimal>  calculateChildContentPrice(Map<Long,Long> map){
		
		// <服务id, 该服务的某项子服务的价格>
		Map<Long, BigDecimal> contentMap = new HashMap<Long, BigDecimal>();
		
		List<Long> contentIdList = new ArrayList<Long>();
		
		Set<Entry<Long,Long>> entrySet = map.entrySet();
		
		BigDecimal childSumPrice = new BigDecimal(0);
		
		for (Entry<Long, Long> entry : entrySet) {
			
			//子服务id
			Long childContentId = entry.getKey();
			
			SurveyContentChild contentChild = childService.selectByPrimaryKey(childContentId);
			
			//子服务单价
			BigDecimal childPrice = contentChild.getChildPrice();
			//子服务次数
			Long value = entry.getValue();
			
			//子服务所属 的 服务id
			Long contentId = contentChild.getContentId();
			
			//如果不是 同一种服务， 则重置 累加变量。。目的是得到  同一种 服务的子服务价格之和
			if(!contentIdList.contains(contentId)){
				childSumPrice = new BigDecimal(0);
			}
			
			childSumPrice = childSumPrice.add(childPrice.multiply(new BigDecimal(value)));
			
			/**
			 * 	通过   子服务对应的 服务id， 得到  该子服务 所属的  类型（基础家政、深度、助理）
			 *   	
			 *   最终 得到  总价	
			 */
			contentMap.put(contentId, childSumPrice);
			
			//将当前 服务 留存
			contentIdList.add(contentId);
		}
		
		return contentMap;
	}
	
	@Override
	public void deleteByUserId(Long userId) {
		recommendMapper.deleteByUserId(userId);
	}
}
