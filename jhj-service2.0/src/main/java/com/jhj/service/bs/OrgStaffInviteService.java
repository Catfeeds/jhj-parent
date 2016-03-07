package com.jhj.service.bs;
import java.util.List;

import com.jhj.po.model.bs.OrgStaffInvite;
import com.jhj.vo.OrgStaffFinanceSearchVo;
/**
*
* @author :Aimee
* @Date : 2016年1月27日上午11:50
* @Description: 
*
*/
public interface OrgStaffInviteService {
	int deleteByPrimaryKey(Long id);

    int insert(OrgStaffInvite orgStaffInvite);

    int insertSelective(OrgStaffInvite orgStaffInvite);

    OrgStaffInvite selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(OrgStaffInvite orgStaffInvite);

    int updateByPrimaryKey(OrgStaffInvite orgStaffInvite);
    
    OrgStaffInvite initOrgStaffInvite();

	OrgStaffInvite selectByMobile(String mobile);

	Boolean userOrderAmSuccessTodo(String mobile);

	List<OrgStaffInvite> selectByInviteStaffIdAndStatus();

	List<OrgStaffInvite> selectByListPage(OrgStaffFinanceSearchVo searvhVo, int pageNo, int pageSize);

}
