package com.jhj.service.impl.bs;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jhj.po.dao.bs.OrgStaffTagsMapper;
import com.jhj.po.model.bs.OrgStaffTags;
import com.jhj.service.bs.OrgStaffTagsService;
import com.meijia.utils.TimeStampUtil;

/**
 *
 * @author :hulj
 * @Date : 2015年7月7日下午2:41:56
 * @Description: TODO
 *
 */
@Service
public class OrgStaffTagsServiceImpl implements OrgStaffTagsService {
	
    @Autowired
	private OrgStaffTagsMapper orgMapper;
    
	@Override
	public int insert(OrgStaffTags record) {
		return orgMapper.insert(record);
	}

	@Override
	public int insertSelective(OrgStaffTags record) {
		return orgMapper.insertSelective(record);
	}
	
	/*@Override
	public int deleteByStaffId(Long staffId) {
		return orgMapper.deleteByStaffId(staffId);
	}*/	

	@Override
	public List<OrgStaffTags> selectByStaffId(Long staffId) {
		return orgMapper.selectByStaffId(staffId);
	}
	
	@Override
	public List<OrgStaffTags> selectByStaffIds(List<Long> staffIds) {
		return orgMapper.selectByStaffIds(staffIds);
	}	

	@Override
	public void deleteByStaffId(Long staffId) {
		orgMapper.deleteByStaffId(staffId);
	}

	@Override
	public OrgStaffTags initOrgStaffTags() {
		OrgStaffTags orgStaffTags = new OrgStaffTags();
		
		orgStaffTags.setId(0L);
		orgStaffTags.setStaffId(0L);
		orgStaffTags.setTagId(0L);
		orgStaffTags.setAddTime(TimeStampUtil.getNow()/1000);
		return orgStaffTags;
	}

}
