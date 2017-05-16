package com.jhj.service.bs;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jhj.po.model.bs.OrgStaffs;
import com.jhj.po.model.user.Users;
import com.jhj.vo.bs.NewStaffFormVo;
import com.jhj.vo.bs.NewStaffListVo;
import com.jhj.vo.bs.OrgStaffVo;
import com.jhj.vo.bs.SecInfoVo;
import com.jhj.vo.order.OrderStaffRateVo;
import com.jhj.vo.order.OrgStaffDispatchVo;
import com.jhj.vo.staff.StaffSearchVo;

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

    int updateByPrimaryKeySelective(OrgStaffs orgStaffs);

    int updateByPrimaryKey(OrgStaffs orgStaffs);
        
//    List<OrgStaffs> selectByListPage(StaffSearchVo staffSearchVo, int pageNo, int pageSize);
    
    OrgStaffs initOrgStaffs();
    
    OrgStaffVo initOrgStaffVo();
    
    OrgStaffs selectByPrimaryKey(Long staffId);
    
    List<OrgStaffs> selectBySearchVo(StaffSearchVo searchVo);
        
    OrgStaffVo genOrgStaffVo(OrgStaffs orgStaff);
		
	SecInfoVo changeSecToVo(OrgStaffs orgStaffs);

	List<Users> amGetUserList(Long amId,int pageNo, int pageSize);

	Boolean userOutBlackSuccessTodo(String mobile);
		    
    //2016年3月9日15:03:28  jhj2.1 列表页VO
    NewStaffListVo  transToNewStaffListVo(OrgStaffs staffs);
    
    //2016年3月10日17:58:27 jhj2.1 表单页VO  TODO
    NewStaffFormVo transToNewStaffFormVo(Long staffId);
    
    NewStaffFormVo initFormVo();
    
    Map<Long, String> selectSkillEntity();
        
    OrgStaffDispatchVo initOrgStaffNewVo();

	PageInfo selectByListPage(StaffSearchVo staffSearchVo, int pageNo, int pageSize);

	OrderStaffRateVo getOrderStaffRateVo(OrgStaffs orgStaff);
    	
}
