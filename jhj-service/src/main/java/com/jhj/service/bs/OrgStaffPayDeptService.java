package com.jhj.service.bs;


import java.util.List;

import com.jhj.po.model.bs.OrgStaffPayDept;
import com.jhj.vo.order.OrderSearchVo;
import com.jhj.vo.staff.OrgStaffFinanceSearchVo;


/**
 *
 * @author :hulj
 * @Date : 2015年7月1日上午11:16:44
 * @Description: TODO
 *
 */
public interface OrgStaffPayDeptService {
	int deleteByPrimaryKey(Long orderId);

    int insert(OrgStaffPayDept record);

    int insertSelective(OrgStaffPayDept record);

    OrgStaffPayDept selectByPrimaryKey(Long orderId);

    int updateByPrimaryKeySelective(OrgStaffPayDept record);

    int updateByPrimaryKey(OrgStaffPayDept record);
    
    OrgStaffPayDept initOrgStaffPayDept();

	OrgStaffPayDept selectByOrderNo(String orderNo);

	List<OrgStaffPayDept> selectByListPage(OrgStaffFinanceSearchVo searchVo,
			int pageNo, int pageSize);

	List<OrgStaffPayDept> selectBySearchVo(OrderSearchVo searchVo);

}
