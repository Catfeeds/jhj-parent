package com.jhj.po.dao.bs;

import java.util.List;



import com.jhj.po.model.bs.OrgStaffs;
import com.jhj.vo.StaffSearchVo;

public interface OrgStaffsMapper{
    int deleteByPrimaryKey(Long staffId);

    int insert(OrgStaffs record);

    int insertSelective(OrgStaffs record);

    OrgStaffs selectByPrimaryKey(Long staffId);

    int updateByPrimaryKeySelective(OrgStaffs record);

    int updateByPrimaryKey(OrgStaffs record);
    
    List<OrgStaffs> selectAll();

	OrgStaffs selectVoByMobile(String mobile);

	List<OrgStaffs> selectByIds(List<Long> ids);
    

	List<OrgStaffs> selectByListPage(StaffSearchVo staffSearchVo);
	
	
	OrgStaffs selectByCardId(String cardId);
	
	OrgStaffs selectByMobile(String mobile);

	OrgStaffs selectOrgIdByStaffId(Long staffId);
	
	List<OrgStaffs> selectByOrgId(Long orgId); 
	
	List<OrgStaffs> selectAllAm();
	
	List<OrgStaffs> selectStaffByAmId(Long amId);

	List<OrgStaffs> selectAmByOrgId(Long orgId);

	List<OrgStaffs> selectByOrgIdAndType(StaffSearchVo staffSearchVo);
	
	List<OrgStaffs> selectStaffByOrgId(Long orgId);
	
	
	List<OrgStaffs>  selectAllStaff();
	//根据阿姨找到对应的  助理
	OrgStaffs selectAmByStaffId(Long staffId);
	//查找可用的服务人员 or 助理
	List<OrgStaffs> selectStaffByStaffType();
	
	List<OrgStaffs> selectHourAuthStaff();
	
	List<OrgStaffs> selectAmAuthStaff();
	
	//可以 推送消息的服务人员，有cid
	List<OrgStaffs> selectAbleToSendMsgStaff();
	
}