package com.jhj.po.dao.bs;

import java.util.List;

import com.jhj.po.model.bs.OrgStaffLeave;
import com.jhj.vo.org.LeaveSearchVo;

public interface OrgStaffLeaveMapper {
    int deleteByPrimaryKey(Long id);

    int insert(OrgStaffLeave record);

    int insertSelective(OrgStaffLeave record);

    OrgStaffLeave selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(OrgStaffLeave record);

    int updateByPrimaryKey(OrgStaffLeave record);
    
    List<OrgStaffLeave> selectByListPage(LeaveSearchVo searchVo);
    
    List<OrgStaffLeave> selectLeavingStaff(LeaveSearchVo searchVo);
    
}