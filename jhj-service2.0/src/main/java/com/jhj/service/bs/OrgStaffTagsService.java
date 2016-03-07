package com.jhj.service.bs;

import java.util.List;

import com.jhj.po.model.bs.OrgStaffTags;

/**
 *
 * @author :hulj
 * @Date : 2015年7月7日下午2:38:36
 * @Description: TODO
 *
 */
public interface OrgStaffTagsService {
	int insert(OrgStaffTags record);

    int insertSelective(OrgStaffTags record);

	//int deleteByStaffId(Long staffId);

	List<OrgStaffTags> selectByStaffId(Long staffId);
    
    void deleteByStaffId(Long staffId);
    
    OrgStaffTags initOrgStaffTags();

	List<OrgStaffTags> selectByStaffIds(List<Long> staffIds);
    
}
