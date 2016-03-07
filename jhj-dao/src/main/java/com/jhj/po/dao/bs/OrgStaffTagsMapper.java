package com.jhj.po.dao.bs;

import java.util.List;

import com.jhj.po.model.bs.OrgStaffTags;
import com.jhj.po.model.bs.Tags;

public interface OrgStaffTagsMapper {
	
    int insert(OrgStaffTags record);

    int insertSelective(OrgStaffTags record);
    //通过  staffId 查找  tagId
    List<OrgStaffTags> selectByStaffId(Long staffId);
    
    int deleteByStaffId(Long staffId);
        
    List<Long> selectTagIdsByStaId(Long StaffId);

	List<OrgStaffTags> selectByStaffIds(List<Long> staffIds);
}