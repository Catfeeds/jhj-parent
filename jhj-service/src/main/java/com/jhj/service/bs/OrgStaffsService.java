package com.jhj.service.bs;

import java.util.List;
import java.util.Map;

import com.jhj.po.model.bs.OrgStaffs;
import com.jhj.po.model.user.Users;
import com.jhj.vo.StaffSearchVo;
import com.jhj.vo.bs.NewStaffFormVo;
import com.jhj.vo.bs.NewStaffListVo;
import com.jhj.vo.bs.OrgStaffVo;
import com.jhj.vo.bs.SecInfoVo;

/**
 *
 * @author :hulj
 * @Date : 2015年7月6日下午2:40:57
 * @Description: 
 *
 */
public interface OrgStaffsService {
	int deleteByPrimaryKey(Long staffId);

    int insert(OrgStaffs orgStaffs);

    int insertSelective(OrgStaffs org);

    OrgStaffs selectByPrimaryKey(Long staffId);

    int updateByPrimaryKeySelective(OrgStaffs orgStaffs);

    int updateByPrimaryKey(OrgStaffs orgStaffs);
    
    List<OrgStaffs> selectAll();
    
    List<OrgStaffs> selectByListPage(StaffSearchVo staffSearchVo, int pageNo, int pageSize);
    
    OrgStaffs initOrgStaffs();
    
    OrgStaffVo initOrgStaffVo();
    
    OrgStaffs selectOrgIdByStaffId(Long staffId);
    
    OrgStaffVo genOrgStaffVo(OrgStaffs orgStaff);
    
    OrgStaffs selectByCardId(String cardId);
	
	OrgStaffs selectByMobile(String mobile);
	
	SecInfoVo changeSecToVo(OrgStaffs orgStaffs);

	List<OrgStaffs> selectByids(List<Long> ids);

	List<OrgStaffs> selectByOrgId(Long orgId); 
	
	List<OrgStaffs> selectAllAm();
	
//	String getAmName(OrgStaffs orgStaff);
	
//	List<OrgStaffs> selectStaffByAmIdListPage(Long amId,int pageNo, int pageSize);

	List<OrgStaffs> selectAmByOrgId(Long orgId);

	List<OrgStaffs> selectByOrgIdAndType(StaffSearchVo staffSearchVo);
	
	List<OrgStaffs> selectStaffByOrgId(Long orgId);
	
	List<Users> amGetUserList(Long amId,int pageNo, int pageSize);
	
//	List<OrgStaffs> selectStaffByAmId(Long amId);
	
	//所有阿姨
	List<OrgStaffs> selectAllStaff();
	//根据阿姨得到阿姨的 助理
	OrgStaffs selectAmByStaffId(Long staffId);

	Boolean userOutBlackSuccessTodo(String mobile);
	
	//可以推送消息的服务人员
	List<OrgStaffs> selectAbleToSendMsgStaff();
	
	//2016年3月9日11:52:13  jhj2.1 云店列表
    List<OrgStaffs>	selectNewStaffList(StaffSearchVo searchVo);
    
    //2016年3月9日15:03:28  jhj2.1 列表页VO
    NewStaffListVo  transToNewStaffListVo(OrgStaffs staffs);
    
    //2016年3月10日17:58:27 jhj2.1 表单页VO  TODO
    NewStaffFormVo transToNewStaffFormVo(Long staffId);
    
    NewStaffFormVo initFormVo();
    
    Map<Long, String> selectSkillEntity();
    
    List<Long> getProperStaffByOrgAndServiceType(Long orgId,Long serviceType);
    
}
