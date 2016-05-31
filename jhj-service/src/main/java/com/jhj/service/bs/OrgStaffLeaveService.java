package com.jhj.service.bs;

import java.util.List;

import com.jhj.po.model.bs.OrgStaffLeave;
import com.jhj.vo.bs.LeaveStaffVo;
import com.jhj.vo.org.LeaveSearchVo;

/**
 *
 * @author :hulj
 * @Date : 2016年5月12日上午11:33:16
 * @Description:  
 */
public interface OrgStaffLeaveService {
	
	int deleteByPrimaryKey(Long id);

    int insert(OrgStaffLeave record);

    OrgStaffLeave selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(OrgStaffLeave record);

	List<OrgStaffLeave> selectByListPage(LeaveSearchVo searchVo,int pageNo,int pageSize);
	
	LeaveStaffVo transToVO(OrgStaffLeave leave);
	
	OrgStaffLeave initLeave();
	
	LeaveStaffVo initLeaveVo();
	
	List<OrgStaffLeave> selectByLeaveSearchVo(LeaveSearchVo searchVo);
}
