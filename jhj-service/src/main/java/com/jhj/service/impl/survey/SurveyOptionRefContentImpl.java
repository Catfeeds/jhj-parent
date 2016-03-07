package com.jhj.service.impl.survey;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jhj.po.dao.survey.SurveyContentMapper;
import com.jhj.po.dao.survey.SurveyOptionRefContentMapper;
import com.jhj.po.model.survey.SurveyContent;
import com.jhj.po.model.survey.SurveyOptionRefContent;
import com.jhj.service.survey.SurveyOptionRefContentService;
import com.jhj.vo.survey.SurveyOptionRefContentVo;
import com.meijia.utils.BeanUtilsExp;
import com.meijia.utils.TimeStampUtil;

@Service
public class SurveyOptionRefContentImpl implements SurveyOptionRefContentService {
	
	@Autowired
	private SurveyOptionRefContentMapper refContentMapper;
	
	@Autowired
	private SurveyContentMapper contentMapper;
	
	@Override
	public int deleteByPrimaryKey(Long id) {
		return refContentMapper.deleteByPrimaryKey(id);
	}

	@Override
	public int insertSelective(SurveyOptionRefContent record) {
		return refContentMapper.insertSelective(record);
	}

	@Override
	public SurveyOptionRefContent selectByPrimaryKey(Long id) {
		return refContentMapper.selectByPrimaryKey(id);
	}

	@Override
	public int updateByPrimaryKeySelective(SurveyOptionRefContent record) {
		return refContentMapper.updateByPrimaryKeySelective(record);
	}

	@Override
	public SurveyOptionRefContent initRefContent() {
		SurveyOptionRefContent content = new SurveyOptionRefContent();
		
		content.setId(0L);
		content.setqId(0L);
		content.setOptionId("");
		content.setOptionNo("");
		content.setContentId("");
		content.setEnable((short)1); //0 = 不可用  1=可用
		content.setAddTime(TimeStampUtil.getNowSecond());
		
		return content;
	}

	@Override
	public SurveyOptionRefContentVo initVo() {
		
		SurveyOptionRefContentVo contentVo = new SurveyOptionRefContentVo();
		
		SurveyOptionRefContent content = initRefContent();
		
		BeanUtilsExp.copyPropertiesIgnoreNull(content, contentVo);
		
		contentVo.setQuestionTitle("");
		//选中的服务内容
//		contentVo.setSelectContentList(new ArrayList<SurveyServiceContent>());
		
//		contentVo.setSelectContentList(new ArrayList<Long>());
		
		contentVo.setSelectContent("");
		
		//所有服务内容
		List<SurveyContent> list = contentMapper.selectByListPage();
		
		contentVo.setContentList(list);
		
		return contentVo;
	}

	@Override
	public List<SurveyOptionRefContent> selectByQId(Long qId) {
		return refContentMapper.selectByQId(qId);
	}

	@Override
	public SurveyOptionRefContent selectOneByQIdAndNo(Long qId, String optionNo) {
		return refContentMapper.selectOneByQIdAndNo(qId, optionNo);
	}

}
