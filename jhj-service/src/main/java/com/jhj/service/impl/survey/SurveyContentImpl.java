package com.jhj.service.impl.survey;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jhj.po.dao.survey.SurveyContentMapper;
import com.jhj.po.model.survey.SurveyContent;
import com.jhj.po.model.survey.SurveyContentChild;
import com.jhj.po.model.survey.SurveyService;
import com.jhj.service.survey.SurveyContentService;
import com.jhj.service.survey.SurveyServiceService;
import com.jhj.vo.survey.SurveyContentVo;
import com.jhj.vo.survey.SurveyServiceContentVo;
import com.meijia.utils.BeanUtilsExp;
import com.meijia.utils.TimeStampUtil;

@Service
public class SurveyContentImpl implements SurveyContentService {
	
	@Autowired
	private SurveyContentMapper contentMapper;
	@Autowired
	private SurveyServiceService surveyService;
	
	@Override
	public int deleteByPrimaryKey(Long contentId) {
		return contentMapper.deleteByPrimaryKey(contentId);
	}

	@Override
	public int insertSelective(SurveyContent record) {
		return contentMapper.insertSelective(record);
	}

	@Override
	public SurveyContent selectByPrimaryKey(Long contentId) {
		return contentMapper.selectByPrimaryKey(contentId);
	}

	@Override
	public int updateByPrimaryKeySelective(SurveyContent record) {
		return contentMapper.updateByPrimaryKeySelective(record);
	}

	@Override
	public List<SurveyContent> selectByListPage() {
		return contentMapper.selectByListPage();
	}

	@Override
	public SurveyContent initContent() {
		
		SurveyContent content = new SurveyContent();
		
		content.setContentId(0L);
		content.setServiceId(0L);
		content.setName("");
		content.setPrice(new BigDecimal(0));		//原价
		content.setUnitPrice(new BigDecimal(0));	//折扣价
		
		content.setPriceDescription(""); 	//对于 类似   100㎡ 以下 xx元， 100㎡ 以上xx 元的服务, 用这个字段,来展示价格明细
		
		content.setItemUnit("次");					//量词，这里都定为  次
		content.setMeasurement((short)0);    //计数期限  0=月 1=年 2=次 3=无（赠送，价钱为0）
		content.setEnable((short)1);			//0 不可用  1可用
		content.setAddTime(TimeStampUtil.getNowSecond());
		content.setUpdateTime(TimeStampUtil.getNowSecond());
		
		content.setContentChildType((short)0);	//单选=1 多选=2，若不包含子服务 =0
		
		content.setDefaultTime(0L);
		
		return content;
	}

	@Override
	public SurveyServiceContentVo completeVo(SurveyContent content) {
		
		SurveyServiceContentVo contentVo = new SurveyServiceContentVo();
		
		BeanUtilsExp.copyPropertiesIgnoreNull(content, contentVo);
		
		Long serviceId = content.getServiceId();
		
		SurveyService service = surveyService.selectByPrimaryKey(serviceId);
		
		contentVo.setServiceName(service.getName());
		
		return contentVo;
	}

	@Override
	public List<SurveyContent> selectAllContent() {
		return contentMapper.selectAllContent();
	}

	@Override
	public List<SurveyContent> selectByIds(Long[] ids) {
		return contentMapper.selectByIds(ids);
	}

	@Override
	public SurveyContentVo initContentVo() {
		
		SurveyContentVo contentVo = new SurveyContentVo();
		
		SurveyContent content = initContent();
		
		BeanUtilsExp.copyPropertiesIgnoreNull(content, contentVo);
		
		contentVo.setChildList(new ArrayList<SurveyContentChild>());
		contentVo.setBaseContentRealTime(0L);
		
		
		return contentVo;
	}
	
	
	@Override
	public List<Long> selectContentIdByMeasurement(short mearsurement) {
		return contentMapper.selectContentIdByMeasurement(mearsurement);
	}
	
	@Override
	public List<SurveyContent> selectByIdList(List<Long> list) {
		
		if(list.size() > 0 && list !=null){
			return contentMapper.selectByIdList(list);
		}else{
			return new ArrayList<SurveyContent>();
		}
	}
	
	@Override
	public List<Long> selectBoxChildContent() {
		return contentMapper.selectBoxChildContent();
	}
	
	@Override
	public List<Long> selectSingalContent() {
		return contentMapper.selectSingalContent();
	}
	
	@Override
	public List<Long> selectByChildType(short childType) {
		return contentMapper.selectByChildType(childType);
	}

	@Override
	public List<Long> selectBaseContent() {
		return contentMapper.selectBaseContent();
	}

	@Override
	public List<Long> selectDeepContent() {
		return contentMapper.selectDeepContent();
	}

	@Override
	public List<Long> selectAmContent() {
		return contentMapper.selectAmContent();
	}
	
	@Override
	public List<Long> selectSetDefaultTime() {
		return contentMapper.selectSetDefaultTime();
	}
}
