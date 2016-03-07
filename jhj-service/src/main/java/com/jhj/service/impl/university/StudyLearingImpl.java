package com.jhj.service.impl.university;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jhj.po.dao.university.StudyLearningMapper;
import com.jhj.po.model.university.PartnerServiceType;
import com.jhj.po.model.university.StudyLearning;
import com.jhj.service.university.PartnerServiceTypeService;
import com.jhj.service.university.StudyLearningService;
import com.jhj.vo.university.StudyVo;
import com.meijia.utils.BeanUtilsExp;
import com.meijia.utils.TimeStampUtil;

/**
 *
 * @author :hulj
 * @Date : 2015年12月3日下午2:56:51
 * @Description: 
 *
 */
@Service
public class StudyLearingImpl implements StudyLearningService {
	
	@Autowired
	private StudyLearningMapper studyMapper;
	
	@Autowired
	private PartnerServiceTypeService partnerService;
	
	@Override
	public int deleteByPrimaryKey(Long id) {
		return studyMapper.deleteByPrimaryKey(id);
	}

	@Override
	public int insertSelective(StudyLearning record) {
		return studyMapper.insertSelective(record);
	}

	@Override
	public StudyLearning selectByPrimaryKey(Long id) {
		return studyMapper.selectByPrimaryKey(id);
	}

	@Override
	public int updateByPrimaryKeySelective(StudyLearning record) {
		return studyMapper.updateByPrimaryKeySelective(record);
	}

	@Override
	public List<StudyLearning> selectByListPage() {
		return studyMapper.selectByListPage();
	}

	@Override
	public StudyVo completeVo(StudyLearning learning){
		
		StudyVo studyVo = initStudyVo();
		
		BeanUtilsExp.copyPropertiesIgnoreNull(learning, studyVo);
		
		Long serviceTypeId = learning.getServiceTypeId();
		
		PartnerServiceType partner = partnerService.selectByPrimaryKey(serviceTypeId);
		
		studyVo.setName(partner.getName());
		
		return studyVo;
	}

	@Override
	public StudyVo initStudyVo() {
		
		StudyVo studyVo = new StudyVo();
		
//		studyVo.setId(0L);
//		studyVo.setServiceTypeId(0L);
//		studyVo.setContent("");
//		studyVo.setAddTime(TimeStampUtil.getNowSecond());
		
		
		StudyLearning learning = initStudyLearning();
		
		BeanUtilsExp.copyPropertiesIgnoreNull(learning, studyVo);
		
		studyVo.setName("");
		
		return studyVo;
	}

	@Override
	public StudyLearning initStudyLearning() {
		
		StudyLearning learning = new StudyLearning();
		
		learning.setId(0L);
		learning.setServiceTypeId(0L);
		learning.setContent("");
		learning.setAddTime(TimeStampUtil.getNowSecond());
		
		return learning;
	}

	@Override
	public List<StudyLearning> selectByServiceTypeId(Long serviceTypeId) {
		return studyMapper.selectByServiceTypeId(serviceTypeId);
	}
	
	@Override
	public List<StudyLearning> selectAllLearning() {
		return studyMapper.selectAllLearning();
	}
}
