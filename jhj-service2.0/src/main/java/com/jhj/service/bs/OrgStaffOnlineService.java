package com.jhj.service.bs;

import java.util.List;

import com.jhj.po.model.bs.OrgStaffOnline;


/**
 *
 * @author :hulj
 * @Date : 2015年7月1日上午11:16:44
 * @Description: TODO
 *
 */
public interface OrgStaffOnlineService {
	int deleteByPrimaryKey(Long id);

    int insert(OrgStaffOnline record);

    int insertSelective(OrgStaffOnline record);

    OrgStaffOnline selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(OrgStaffOnline record);

    int updateByPrimaryKey(OrgStaffOnline record);
    
    OrgStaffOnline initOnline();

	OrgStaffOnline selectByStaffId(Long userId);


	List<OrgStaffOnline> selectByStaffIdLimitTwo(Long staffId);


}
